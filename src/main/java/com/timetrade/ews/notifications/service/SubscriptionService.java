package com.timetrade.ews.notifications.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.timetrade.ews.notifications.configuration.EwsProperties.SubscriptionsProperties;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver.TimeRange;
import com.timetrade.ews.notifications.model.PushSubscriptionMeta;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.model.history.HistoryRecord;

public class SubscriptionService {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);

    private StorageSyncingService storageSyncingService;
    private SyncService syncService;
    private SubscriptionsProperties subscriptionsProperties;
    private ConfigurationResolver configurationResolver;
    private ExecutorService subscriberExecutor = Executors.newSingleThreadExecutor();

    public SubscriptionService(StorageSyncingService storageSyncingService, SyncService syncService,
            ConfigurationResolver configurationResolver) {
        this.storageSyncingService = storageSyncingService;
        this.syncService = syncService;
        this.configurationResolver = configurationResolver;
        this.subscriptionsProperties = configurationResolver.resolveSubscriptionsProperties();
    }

    public void subscribeAll(Collection<UserOfAccount> accounts, boolean resubscribe,
            Predicate<UserOfAccount> subscribeHook) {

        // NEEDSWORK: This method used to be synchronized. Why?  Regardless, it's not a good idea to put somebody elses
        // threads to sleep so we'll do this in an executor.

        LOG.info("Attempting to subscribe {} accounts", accounts.size());

        LOG.info("Subscribing by {} with {} sec delay",
                subscriptionsProperties.getRate().getBatchSize(),
                subscriptionsProperties.getRate().getDelayMillis() / 1000);

        subscriberExecutor.submit(new Runnable() {
            @Override
            public void run() {
                subscribeAllSynchronously(accounts, resubscribe, subscribeHook);
            }
        });
    }

    public void subscribeAllSynchronously(Collection<UserOfAccount> accounts, boolean resubscribe, Predicate<UserOfAccount> subscribeHook) {
        int currently = 0;
        int success = 0;
        for (UserOfAccount account : accounts) {
            if (subscribe(account, resubscribe, subscribeHook)) {
                LOG.debug("Successful subscription for {}.", account);
                success++;
            }
            currently++;
            LOG.info("Processed {}/{}: {} success, {} failed",
                currently, accounts.size(), success, currently - success);

            if (currently % subscriptionsProperties.getRate().getBatchSize() == 0
                    && currently >= subscriptionsProperties.getRate().getBatchSize()) {
                LOG.info("Relaxx");
                try {
                    Thread.sleep(subscriptionsProperties.getRate().getDelayMillis());
                } catch (InterruptedException e) {
                    LOG.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
        }
        LOG.info("Completed adding subscriptions: {} successful", success);
    }
    private boolean subscribe(UserOfAccount userOfAccount, boolean resubscribe, Predicate<UserOfAccount> subscribeHook) {
        PushSubscriptionMeta exisitingMeta = getSubscription(userOfAccount);
        try {
            return resubscribe ? resubscribe(exisitingMeta, userOfAccount, subscribeHook)
                    : newSubscribe(exisitingMeta, userOfAccount, subscribeHook);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            addToReattempt(userOfAccount, resubscribe);
            return false;
        }
    }

    private boolean newSubscribe(PushSubscriptionMeta exisitingMeta, UserOfAccount userOfAccount,
            Predicate<UserOfAccount> subscribeHook) {
        if (exisitingMeta != null) {
            LOG.warn("Subscribe failed due to existance of sub for such account");
            return false;
        }
        if (subscribeHook != null && !subscribeHook.test(userOfAccount)) {
            LOG.error("No subscribe as pre-sub action has failed");
            return false;
        }
        return subscribe(userOfAccount, null);
    }

    private boolean resubscribe(PushSubscriptionMeta exisitingMeta, UserOfAccount userOfAccount,
            Predicate<UserOfAccount> subscribeHook) {
        if (exisitingMeta == null) {
            LOG.warn("Resubscribe failed due to missing of previously existing sub");
            return false;
        }
        if (subscribeHook != null && !subscribeHook.test(userOfAccount)) {
            LOG.error("No subscribe as pre-sub action has failed");
            return false;
        }
        return subscribe(userOfAccount, exisitingMeta.getWaterMark());
    }

    private boolean subscribe(UserOfAccount userOfAccount, String waterMark) {

        LOG.info("Subscribing {}", userOfAccount);

        PushSubscriptionMeta meta;
        try {
            meta = syncService.subscribeToPushNotifications(userOfAccount, waterMark);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return false;
        }

        syncWithStorage(userOfAccount, meta);

        checkIn(userOfAccount);

        LOG.info("Subscribe: {} [id {}, watermark {}]", userOfAccount, meta.getId(), meta.getWaterMark());

        return true;
    }

    public void addToReattempt(UserOfAccount userOfAccount, boolean isResub) {
        LOG.info("Adding {} of {} to reattempt later", userOfAccount.getUsername(), userOfAccount.getAccountId());
        int attempts = storageSyncingService.getFromReattempt(userOfAccount, isResub);
        if (attempts < subscriptionsProperties.getReattempts().getMaxTimes()) {
            storageSyncingService.addToReattempt(userOfAccount, isResub);
        } else {
            LOG.warn("Reattempt max tries for {}/{} has been exhausted, stoppped trying", userOfAccount.getUsername(),
                    userOfAccount.getAccountId());
            storageSyncingService.removeFromReattempt(userOfAccount, isResub);
        }
    }

    public void runReattempts() {
        LOG.info("Running reattempts: resubscribe");
        subscribeAll(storageSyncingService.getReattempts(subscriptionsProperties.getReattempts().getMaxTimes(), true), true, u -> true);

        TimeRange initialAvailability = configurationResolver.resolveInitialAvailabilityTimeRange();

        LOG.info("Running reattempts: subscribe");
        subscribeAll(storageSyncingService.getReattempts(subscriptionsProperties.getReattempts().getMaxTimes(), false), false,
                u -> syncService.getInitialAvailabilityAndSend(u,
                        initialAvailability.getStartTime(), initialAvailability.getEndTime()));
    }

    public void checkIn(UserOfAccount userOfAccount) {
        LOG.info("Checking in subscription: {}", userOfAccount);
        storageSyncingService.persistCheckIn(userOfAccount, System.currentTimeMillis() / 1000);
    }

    public PushSubscriptionMeta getSubscription(UserOfAccount userOfAccount) {
        return storageSyncingService.getSubscription(userOfAccount);
    }

    public void syncWithStorage(UserOfAccount userOfAccount, PushSubscriptionMeta subscriptionMeta) {
        storageSyncingService.sync(userOfAccount, subscriptionMeta);
        storageSyncingService.sync(userOfAccount, HistoryRecord.fromSubscriptionMeta(subscriptionMeta));
    }

    public void removeSubscription(UserOfAccount userOfAccount) {
        storageSyncingService.removeSubscription(userOfAccount);
    }

    public void removeCheckin(UserOfAccount userOfAccount) {
        storageSyncingService.removeCheckin(userOfAccount);
    }

    //only for debug
    public Map<UserOfAccount, PushSubscriptionMeta> getSubscriptions(String accountId) {
        return storageSyncingService.getSubscriptions(accountId);
    }

    public Set<UserOfAccount> getSubscribedEmails(String accountId) {
        return storageSyncingService.getSubscribedEmails(accountId);
    }

    public Set<String> getAllAccounts() {
        return storageSyncingService.getAllAccounts();
    }

    public void markToUnsubscribe(Collection<UserOfAccount> usersOfAccounts) {
        LOG.info("Added to unsubscribe: +{}", usersOfAccounts.size());

        for (UserOfAccount userOfAccount : usersOfAccounts) {
            LOG.info("Marking {} to unsubscribe", userOfAccount.getUsername());
            PushSubscriptionMeta subscrMeta = getSubscription(userOfAccount);
            if (subscrMeta != null) {
                syncWithStorage(userOfAccount, subscrMeta.markToDelete());
            } else {
                LOG.warn("Couldn't find subscription for {} to unsubscribe", userOfAccount.getUsername());
            }
        }

    }

    public Map<UserOfAccount, Set<HistoryRecord>> getHistory(UserOfAccount... accounts) {
        return storageSyncingService.getHistory(accounts);
    }

}
