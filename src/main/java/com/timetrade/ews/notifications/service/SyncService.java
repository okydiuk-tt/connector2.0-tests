package com.timetrade.ews.notifications.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver.TimeRange;
import com.timetrade.ews.notifications.model.Event;
import com.timetrade.ews.notifications.model.ParsedNotification;
import com.timetrade.ews.notifications.model.ParsedNotification.ItemInFolder;
import com.timetrade.ews.notifications.model.PushSubscriptionMeta;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.service.wrappers.TTExchangeService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.calendar.AppointmentType;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.schema.AppointmentSchema;
import microsoft.exchange.webservices.data.property.complex.DeletedOccurrenceInfo;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.RecurringAppointmentMasterId;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.*;

public class SyncService {

    private static final Logger LOG = LoggerFactory.getLogger(SyncService.class);

    private static final String DELETED_ITEMS_FOLDER_NAME = "Deleted Items";

    private EventConversionService eventConversionService;
    private KafkaTemplate<Integer, String> kafkaTemplate;
    private ConfigurationResolver configurationResolver;
    private ExchangeServiceFactory exchangeServiceFactory;
    private Optional<Cache> foldersCache;
    private ObjectMapper objectMapper = new ObjectMapper();

    // TODO to remove subject eventually
    private PropertySet properties = new PropertySet(AppointmentSchema.AppointmentType, AppointmentSchema.ICalUid,
            AppointmentSchema.Start, AppointmentSchema.End, AppointmentSchema.TimeZone,
            AppointmentSchema.LegacyFreeBusyStatus, AppointmentSchema.Subject,
            AppointmentSchema.DeletedOccurrences);

    public interface Command<R> {// it's so complex to make it possible to mock ExchangeService
        R execute(UserOfAccount userOfAccount, TTExchangeService service) throws Exception;
    }

    public SyncService(EventConversionService eventConversionService, KafkaTemplate<Integer, String> kafkaTemplate,
            ConfigurationResolver configurationResolver, CacheManager cacheManager, ExchangeServiceFactory exchangeServiceFactory) {
        this.eventConversionService = eventConversionService;
        this.kafkaTemplate = kafkaTemplate;
        this.configurationResolver = configurationResolver;
        this.foldersCache = Optional.ofNullable(cacheManager.getCache("folders"));
        this.exchangeServiceFactory = exchangeServiceFactory;
    }

    private String getTopic(UserOfAccount userOfAccount) {
        return configurationResolver.resolveKafkaProperties().getTopicPattern().replace("{accountId}",
                userOfAccount.getAccountId());
    }

    public boolean getInitialAvailabilityAndSend(UserOfAccount userOfAccount, TimeRange timeRange) {
        return getInitialAvailabilityAndSend(userOfAccount, timeRange.getStartTime(), timeRange.getEndTime());
    }

    public boolean getInitialAvailabilityAndSend(UserOfAccount userOfAccount, long startTime, long endTime) {
        Set<Event> events = getExistingAppointments(userOfAccount, startTime, endTime);

        String topic = getTopic(userOfAccount);
        for (Event event : events) {
            try {
                kafkaTemplate.send(topic, objectMapper.writeValueAsString(event));
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                return false;
            }
            LOG.info("Event {} published to kafka/{}", event.getId(), topic);
        }

        return true;
    }

    public Set<Event> getExistingAppointments(UserOfAccount userOfAccount, long startTime, long endTime) {
        return executeWithExchange(userOfAccount, new GetEventsCommand(startTime, endTime));
    }

    public String sendTestMessage(UserOfAccount userOfAccount, String payload)  {

        try {

            Map<String, Object> map = objectMapper.readValue(payload, new TypeReference<Map<String,Object>>(){});

            long startTime = System.currentTimeMillis();
            long endTime = System.currentTimeMillis();
            if (map.get("startTime") != null) {
                startTime = Instant.parse((String)map.get("startTime")).toEpochMilli();
            }
            if (map.get("endTime") != null) {
                endTime = Instant.parse((String)map.get("endTime")).toEpochMilli();
            }

            // NEEDSWORK: Clean up the implementation of these timestamps. The downstream components want seconds, not millis, but the API is all coded with longs and "timestamps" that
            // should be treated as seconds and not millis.  For now, we're just going to whack the value here but all the downstream code is really confusing
            // about the units.

            Event.Status status = (map.get("status") != null) ? Event.Status.valueOf((String) map.get("status")) : Event.Status.BUSY;
            Event fakeEvent = new Event(userOfAccount.getUsername(), Long.toString(System.currentTimeMillis()), status, startTime/1000L, endTime/1000L);

            return sendMessages(userOfAccount, Collections.singletonList(fakeEvent));
        }
        catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new MessagingException(e);
        }
    }

    public String enrichAndSyncItems(UserOfAccount userOfAccount, ParsedNotification parsedNotification,
            long startTime, long endTime) {
        return executeWithExchange(userOfAccount, new EnrichAndSyncCommand(parsedNotification, startTime, endTime));
    }

    public PushSubscriptionMeta subscribeToPushNotifications(UserOfAccount userOfAccount, String waterMark) {
        return executeWithExchange(userOfAccount, new SubscribeCommand(waterMark));
    }

    protected <T> T executeWithExchange(UserOfAccount userOfAccount, Command<T> command) {

        Objects.requireNonNull(userOfAccount, () -> {
            String error = "Username is not set";
            LOG.error(error);
            return error;
        });
        Objects.requireNonNull(command, () -> {
            String error = "Command is not set";
            LOG.error(error);
            return error;
        });

        try (TTExchangeService service = exchangeServiceFactory.createExchangeService(userOfAccount)) {
            return command.execute(userOfAccount, service);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new MessagingException(e);
        }

    }

    private String sendMessages(UserOfAccount userOfAccount, List<Event> events) throws Exception {

        // Choose kafka topic for events:
        String topic = getTopic(userOfAccount);

        // Compute partition. This approach sets a constant number of partitions across all accounts. We might want the flexibility later
        // to be able to configure the number of partitions per account.  But for now, we won't prematurely optimize this and just select a reasonable default
        // (which we get from the configuration, for now).

        int numberOfConsumers = configurationResolver.resolveKafkaProperties().getNumberOfConsumers();
        int partition = userOfAccount.getUsername().hashCode() % numberOfConsumers;

        int totalSent = 0;
        for (Event event : events) {
            kafkaTemplate.send(topic, partition, objectMapper.writeValueAsString(event));
            LOG.info("Event {} published to kafka/{}", event.getId(), topic);
            totalSent++;
        }
        return objectMapper.writeValueAsString(events.get(0));
    }

    private class EnrichAndSyncCommand implements Command<String> {

        private final ParsedNotification parsedNotification;
        private final long startTime;
        private final long endTime;

        public EnrichAndSyncCommand(ParsedNotification parsedNotification, long startTime, long endTime) {
            this.parsedNotification = parsedNotification;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public String execute(UserOfAccount userOfAccount, TTExchangeService service) throws Exception {

            int initialItemsInSetNumber = parsedNotification.getItemsInFolder().size();
            // checking if notification signals that deletion of events is
            // happening
            boolean deletionHappened = false;
            for (Iterator<ItemInFolder> itemInFolderItr = parsedNotification.getItemsInFolder()
                    .iterator(); itemInFolderItr.hasNext();) {
                com.timetrade.ews.notifications.model.ParsedNotification.FolderId folderId = itemInFolderItr.next()
                        .getFolderId();
                String folderName = bindFolder(folderId.getId(), folderId.getChangeKey(), service).getDisplayName();
                if (DELETED_ITEMS_FOLDER_NAME.equalsIgnoreCase(folderName)) {
                    deletionHappened = true;
                    if (initialItemsInSetNumber > 1) {
                        // sometimes one, sometimes two events are present in the notification
                        itemInFolderItr.remove();
                    }
                }
            }

            // converting appointments to free/busy events
            List<Event> events = new LinkedList<>();
            for (ItemInFolder item : parsedNotification.getItemsInFolder()) {
                if (!deletionHappened) {
                    Appointment apmt = bindToAppointment(item.getId(), service);
                    if (apmt != null) {
                        events.addAll(convertToEvents(userOfAccount, service, apmt));
                    } else {
                        events.add(eventConversionService.convertToDeletedEvent(userOfAccount, item.getId()));
                    }
                } else {
                    events.add(eventConversionService.convertToDeletedEvent(userOfAccount, item.getId()));
                }
            }

            return sendMessages(userOfAccount, events);

        }

        private Set<Event> convertToEvents(UserOfAccount userOfAccount, TTExchangeService service, Appointment apmt)
                throws ServiceLocalException {
            if (apmt.getAppointmentType() == AppointmentType.Single) {
                return Collections.singleton(eventConversionService.convertToModifiedEvent(userOfAccount, apmt));
            } else if (apmt.getAppointmentType() == AppointmentType.RecurringMaster) {
                return getEvents(service, Optional.of(apmt), startTime, endTime);
            }
            return Collections.emptySet();
        }

        // since this is not spring bean, using cache explicitly
        private Folder bindFolder(String folderId, String changeKey, ExchangeService service) {
            if (foldersCache.isPresent()) {
                Folder folderFromCache = foldersCache.get().get(folderId + changeKey, Folder.class);
                if (folderFromCache != null) {
                    LOG.info("Got folder details from cache");
                    return folderFromCache;
                }
            }

            FolderId fid = new FolderId();
            fid.setUniqueId(folderId);
            fid.setChangeKey(changeKey);

            Folder folder;
            try {
                folder = Folder.bind(service, fid);
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }

            LOG.info("Got folder details: {}", folder);
            foldersCache.ifPresent(c -> c.put(folderId + changeKey, folder));

            return folder;
        }

        private Appointment bindToAppointment(String itemId, ExchangeService service) {
            try {
                LOG.debug("Trying to bind {}", itemId);
                return Appointment.bind(service, new ItemId(itemId), properties);
            } catch (Exception e) {
                if (e.getMessage().contains("object was not found")) {
                    LOG.error("Suspicious notification: {} was not found and no delete event found", itemId);
                    return null;
                }
                LOG.error(e.getMessage(), e);
                throw new IllegalStateException(e.getMessage(), e);
            }
        }



    }

    private class GetEventsCommand implements Command<Set<Event>> {

        private final long startTime;
        private final long endTime;

        public GetEventsCommand(long startTime, long endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Set<Event> execute(UserOfAccount userOfAccount, TTExchangeService service) throws Exception {
            LOG.info("Getting initial availability for {}", userOfAccount);
            return getEvents(service, Optional.empty(), startTime, endTime);
        }

    }

    public Set<Event> getEvents(TTExchangeService service, Optional<Appointment> bindTo, long startTime, long endTime) {

        Date startDate = new Date(Instant.ofEpochSecond(startTime).toEpochMilli());
        Date endDate = new Date(Instant.ofEpochSecond(endTime).toEpochMilli());

        Set<Event> events = new HashSet<>();

        try {
            //grabbing all appointments in range
            FindItemsResults<Appointment> foundAppointments = service.findAppointments(WellKnownFolderName.Calendar,
                    new CalendarView(startDate, endDate));

            LOG.info("Found {} appointments for {}/{}", foundAppointments.getTotalCount(),
                    service.getUserOfAccount().getUsername(), service.getUserOfAccount().getAccountId());

            //mapping occurrences of recurrent event to its master;
            //single event is considered as master without occurrences
            Map<String, Appointment> reccurrences = new HashMap<>();
            for (Appointment apmt : foundAppointments) {
                if (bindTo.isPresent() && (!apmt.getICalUid().equalsIgnoreCase(bindTo.get().getICalUid()))) {
                        //if master explicitly specified, ignoring all occurrences that are not related to given master
                        continue;
                }

                processAppointment(service, apmt, events, reccurrences);
            }

            LOG.info("Found {} unique masters for {}/{}", reccurrences.size(),
                    service.getUserOfAccount().getUsername(), service.getUserOfAccount().getAccountId());

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new EventException(e);
        }

        return events;

    }

    private void processAppointment(TTExchangeService service, Appointment appointment, Set<Event> events,
            Map<String, Appointment> reccurrences) throws Exception {

        switch (appointment.getAppointmentType()) {
        case Single: {
            events.add(eventConversionService.convertToModifiedEvent(service.getUserOfAccount(), appointment));
            break;
        }
        case Occurrence:
        case Exception: {
            // if certain master is not yet loaded, do bind it, to extract deleted occurrences and include them
            if (!reccurrences.containsKey(appointment.getICalUid())) {
                ItemId recurrenceMasterId = new RecurringAppointmentMasterId(appointment.getId().getUniqueId());
                Appointment master = bindMaster(service, recurrenceMasterId);
                reccurrences.put(appointment.getICalUid(), master);

                if (master.getDeletedOccurrences() != null) {
                    for (DeletedOccurrenceInfo deleted : master.getDeletedOccurrences()) {
                        String id = EventConversionService.makeUpId(
                                master.getId().getUniqueId(), deleted.getOriginalStart());
                        events.add(eventConversionService.convertToDeletedEvent(service.getUserOfAccount(), id));
                    }
                }

            }
            // adding occurrences/exceptions when master is loaded, master's id is used to build up occurrence id
            String id = EventConversionService.makeUpId(
                    reccurrences.get(appointment.getICalUid()).getId().getUniqueId(), appointment.getStart());
            events.add(eventConversionService.convertToModifiedEvent(service.getUserOfAccount(), appointment, id));
            break;
        }
        default:
            String error = "Unexpected AppointmentType: " + appointment.getAppointmentType();
            LOG.error(error);
            throw new RuntimeException(error);
        }

    }

    protected Appointment bindMaster(TTExchangeService service, ItemId recurrenceMasterId) throws Exception {
        return Appointment.bind(service, recurrenceMasterId, properties);
    }

    private static class SubscribeCommand implements Command<PushSubscriptionMeta> {

        private String waterMark;

        public SubscribeCommand(String waterMark) {
            this.waterMark = waterMark;
        }

        public PushSubscriptionMeta execute(UserOfAccount userOfAccount, TTExchangeService service) throws Exception {
            return service.subscribeToFreeBusyPushNotifications(waterMark);
        }

    }

}
