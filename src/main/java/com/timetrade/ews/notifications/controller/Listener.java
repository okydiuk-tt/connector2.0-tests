package com.timetrade.ews.notifications.controller;

import static com.codahale.metrics.MetricRegistry.name;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver.TimeRange;
import com.timetrade.ews.notifications.model.ParsedNotification;
import com.timetrade.ews.notifications.model.PushSubscriptionMeta;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.model.history.HistoryRecord;
import com.timetrade.ews.notifications.model.history.HistoryRecord.ActionType;
import com.timetrade.ews.notifications.service.ParsingService;
import com.timetrade.ews.notifications.service.SubscriptionService;
import com.timetrade.ews.notifications.service.SyncService;

@RestController("/")
public class Listener {

    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);

    private static final ResponseEntity<String> okResponse = soapResponse("OK");
    private static final ResponseEntity<String> unsubscribeResponse = soapResponse("Unsubscribe");

    private SubscriptionService subscriptionService;

    private ParsingService parsingService;

    private SyncService syncService;

    private ConfigurationResolver configurationResolver;

    private EnumMap<Timers, Timer> timers = new EnumMap<>(Timers.class);

    private enum Timers {
        PARSING, SUB_FROM_STORAGE, WHOLE_PROCESSING, ENRICH_AND_SYNC
    }

    public Listener(SubscriptionService subscriptionService, ParsingService parsingService, SyncService syncService,
            ConfigurationResolver configurationResolver, MetricRegistry metricRegistry) {

        this.syncService = syncService;
        this.subscriptionService = subscriptionService;
        this.parsingService = parsingService;
        this.configurationResolver = configurationResolver;

        Arrays.stream(Timers.values())
        .forEach(key -> this.timers.put(key, metricRegistry.timer(name(Listener.class, key.toString()))));

        LOG.info("Started {}", this);

    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("notify/{accountId:.+}/{username:.+}/test")
    public String sendFakeKafkaMessage(
        @PathVariable("accountId") String accountId,
        @PathVariable("username") String username,
        @RequestBody String notification) {

        return syncService.sendTestMessage(new UserOfAccount(username, accountId), notification);
//        return okResponse();
    }

    @GetMapping
    public void index() {
        LOG.debug("Index");
    }

    @PostMapping(path = "notify/{accountId:.+}/{username:.+}/xml",
            consumes = MediaType.TEXT_XML_VALUE, produces = MediaType.TEXT_XML_VALUE)
    public ResponseEntity<String> listenXml(
            @PathVariable("accountId") String accountId,
            @PathVariable("username") String username,
            @RequestBody String notification) {

        if (LOG.isInfoEnabled()) {
            LOG.info("Caught XML!!!\n{}", configurationResolver.resolveEwsProperties().getLogIncomingXml() ? notification : "");
        }

        UserOfAccount userOfAccount = new UserOfAccount(username, accountId);

        ParsedNotification parsedNotification;
        try (Timer.Context parsingContext = timers.get(Timers.PARSING).time()) {
            parsedNotification = parsingService.parseResponse(notification, userOfAccount);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return okResponse();
        }

        SyncActions syncActions = getActions(userOfAccount, parsedNotification);

        if (syncActions.contains(Action.IGNORE)) {
            return syncActions.contains(Action.UNSUBSCRIBE) ? unsubscribeResponse() : okResponse();
        }

        new Thread(() -> {
            try (Timer.Context wholeProcessingContext = timers.get(Timers.WHOLE_PROCESSING).time()) {
                processNotification(userOfAccount, parsedNotification, syncActions);
            }
        }).start();
        return syncActions.contains(Action.UNSUBSCRIBE) ? unsubscribeResponse() : okResponse();

    }

    public enum Action {
        SYNC, DELETE, IGNORE, UNSUBSCRIBE
    }

    //it's so complex, because some actions should be performed sync, others - async
    public static class SyncActions {
        private final PushSubscriptionMeta subscriptionMeta;
        private final Set<Action> actions = EnumSet.noneOf(Action.class);

        public SyncActions(PushSubscriptionMeta subscriptionMeta, Action... actions) {
            this.subscriptionMeta = subscriptionMeta;
            this.actions.addAll(Arrays.asList(actions));
        }

        public PushSubscriptionMeta getSubscriptionMeta() {
            return subscriptionMeta;
        }

        public boolean contains(Action action) {
            return actions.contains(action);
        }

    }

    private SyncActions getActions(UserOfAccount userOfAccount, ParsedNotification parsedNotification) {
        PushSubscriptionMeta exisitingSubscriptionMeta;
        try (Timer.Context getSubContext = timers.get(Timers.SUB_FROM_STORAGE).time()) {
            exisitingSubscriptionMeta = subscriptionService.getSubscription(userOfAccount);
        }
        if (exisitingSubscriptionMeta != null) {
            LOG.info("Recongnized subscription of {}", userOfAccount.getUsername());
        } else {
            // if nothing found, stub it:
            LOG.warn("Unrecognized subscription of {}, will be restored", userOfAccount.getUsername());
            exisitingSubscriptionMeta = new PushSubscriptionMeta(parsedNotification.getSubscriptionId(),
                    parsedNotification.getWaterMark());
            exisitingSubscriptionMeta.setRestored(true);
            return new SyncActions(exisitingSubscriptionMeta, Action.SYNC);
        }

        if (!parsedNotification.getSubscriptionId().equalsIgnoreCase(exisitingSubscriptionMeta.getId())) {
            LOG.info("Subscription {} to be unsubscribed as it has id not equal to one in cache",
                    parsedNotification.getSubscriptionId());
            return new SyncActions(exisitingSubscriptionMeta, Action.IGNORE, Action.UNSUBSCRIBE);
        }
        if (exisitingSubscriptionMeta.isMarkedToDelete()) {
            LOG.info("Subscription {} to be unsubscribed as it's marked as deleted",
                    parsedNotification.getSubscriptionId());
            return new SyncActions(exisitingSubscriptionMeta, Action.DELETE, Action.UNSUBSCRIBE);
        }

        return new SyncActions(exisitingSubscriptionMeta);
    }

    public void processNotification(UserOfAccount userOfAccount, ParsedNotification parsedNotification,
            SyncActions syncActions) {

        if (syncActions.contains(Action.DELETE)) {
            subscriptionService.removeSubscription(userOfAccount);
            subscriptionService.removeCheckin(userOfAccount);
            LOG.info("Subscription {} has been completely removed", userOfAccount.getUsername());
            return;
        }
        if (syncActions.contains(Action.SYNC)) {
            subscriptionService.syncWithStorage(userOfAccount, syncActions.getSubscriptionMeta());
        }

        subscriptionService.checkIn(userOfAccount);

        if (parsedNotification.isStatusEvent()) {
            LOG.info("Status event");
            return;
        }

        LOG.info("Free-busy event for {}", userOfAccount.getUsername());

        TimeRange recurringPeriod = configurationResolver.resolveRecurringEventsPreloadTimeRange();

        try (Timer.Context enrichAndSyncContext = timers.get(Timers.ENRICH_AND_SYNC).time()) {
            syncService.enrichAndSyncItems(userOfAccount, parsedNotification,
                    recurringPeriod.getStartTime(), recurringPeriod.getEndTime());
            LOG.info("Processed {} of user {}", parsedNotification.getSubscriptionId(), userOfAccount.getUsername());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

    }

    private static ResponseEntity<String> okResponse() {
        LOG.info("Response OK");
        return okResponse;
    }

    private static ResponseEntity<String> unsubscribeResponse() {
        LOG.info("Response Unsubscribe");
        return unsubscribeResponse;
    }

    private static ResponseEntity<String> soapResponse(String response) {
        String responseAsStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                + "xmlns:m=\"http://schemas.microsoft.com/exchange/services/2006/messages\" "
                + "xmlns:t=\"http://schemas.microsoft.com/exchange/services/2006/types\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<soap:Body><m:SendNotificationResult><m:SubscriptionStatus>" + response
                + "</m:SubscriptionStatus></m:SendNotificationResult></soap:Body></soap:Envelope>";

        return ResponseEntity.ok().body(responseAsStr);
    }

    //NON-PROD
    @PostMapping(path = "{accountId:.+}/subscriptions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> subscribe(@PathVariable("accountId") String accountId,
            @RequestBody List<String> accounts) {

        LOG.info("Received {} item(s) to subscribe", accounts.size());

        List<UserOfAccount> toSubscribe = accounts.stream().map(a -> new UserOfAccount(a, accountId))
                .collect(Collectors.toList());

        if (!toSubscribe.isEmpty()) {
            new Thread(() -> subscriptionService.subscribeAll(toSubscribe, false,
                    u -> syncService.getInitialAvailabilityAndSend(u,
                            configurationResolver.resolveInitialAvailabilityTimeRange()))
                    ).start();
        }

        return ResponseEntity.accepted().build();
    }

    //NON-PROD
    @GetMapping(path = "{accountId:.+}/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<UserOfAccount, PushSubscriptionMeta> getSubscriptions(
            @PathVariable("accountId") String accountId) {

        LOG.info("Printing out subscriptions for {}", accountId);

        return subscriptionService.getSubscriptions(accountId);
    }

    //NON-PROD
    @DeleteMapping(path = "{accountId:.+}/subscriptions", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void unsubscribe(@PathVariable("accountId") String accountId, @RequestBody List<String> emails) {

        LOG.info("Adding to unsubscribe: +{} for {}", emails.size(), accountId);

        subscriptionService.markToUnsubscribe(
                emails.stream().map(e -> new UserOfAccount(e, accountId)).collect(Collectors.toSet()));
    }

    @GetMapping(path = "{accountId:.+}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Map<String, ActionType>> getHistory(@PathVariable("accountId") String accountId,
            @RequestParam("email") List<String> emails) {

        UserOfAccount[] accounts = emails.stream()
                .map(e -> new UserOfAccount(e, accountId))
                .toArray(UserOfAccount[]::new);

        return subscriptionService.getHistory(accounts).entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getUsername(), e -> toReadable(e.getValue())));
    }

    private static Map<String, ActionType> toReadable(Set<HistoryRecord> notReadable) {
        return notReadable.stream().collect(Collectors.toMap(
                HistoryRecord::toHumanReadableTime, HistoryRecord::getAction,
                (k1, k2) -> k1, TreeMap<String, ActionType>::new));
    }

}
