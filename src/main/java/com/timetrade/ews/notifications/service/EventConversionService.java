package com.timetrade.ews.notifications.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.EnumMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.timetrade.ews.notifications.model.Event;
import com.timetrade.ews.notifications.model.Event.Status;
import com.timetrade.ews.notifications.model.UserOfAccount;

import microsoft.exchange.webservices.data.core.enumeration.property.LegacyFreeBusyStatus;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.Appointment;

public class EventConversionService {

    private static final Logger LOG = LoggerFactory.getLogger(EventConversionService.class);
    private static final DateTimeFormatter ICALFORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssVV");
    private static final ZoneId UTC = ZoneId.of("UTC");

    private static final EnumMap<LegacyFreeBusyStatus, Status> freeBusyMappings = new EnumMap<>(LegacyFreeBusyStatus.class);
    static  {
        freeBusyMappings.put(LegacyFreeBusyStatus.Busy, Status.BUSY);
        freeBusyMappings.put(LegacyFreeBusyStatus.Free, Status.FREE);
        freeBusyMappings.put(LegacyFreeBusyStatus.Tentative, Status.TENT);
        freeBusyMappings.put(LegacyFreeBusyStatus.OOF, Status.OOO);
    }

    public Event convertToDeletedEvent(UserOfAccount userOfAccount, String appId) {

        LOG.info("Single event to delete for {}", userOfAccount);

        return Event.deletedItem(userOfAccount.getUsername(), appId);
    }

    public Event convertToModifiedEvent(UserOfAccount userOfAccount, Appointment appointment)
            throws ServiceLocalException {
        return convertToModifiedEvent(userOfAccount, appointment, appointment.getId().getUniqueId());
    }

    public Event convertToModifiedEvent(UserOfAccount userOfAccount, Appointment appointment, String id)
            throws ServiceLocalException {

        LOG.info("Single event to insert/update for {}", userOfAccount);

        logBasicInfo(appointment);

        return new Event(userOfAccount.getUsername(), id, freeBusyMappings.get(appointment.getLegacyFreeBusyStatus()),
                Instant.ofEpochMilli(appointment.getStart().getTime()).getEpochSecond(),
                Instant.ofEpochMilli(appointment.getEnd().getTime()).getEpochSecond());
    }

    public static String makeUpId(String baseId, Date date) {
        return String.format("%s-%s", baseId, Instant.ofEpochMilli(date.getTime())
                .atZone(UTC).format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    private void logBasicInfo(Appointment appointment) throws ServiceLocalException {
        Date startDate = appointment.getStart();
        ZonedDateTime startDateZoned = Instant.ofEpochMilli(startDate.getTime()).atZone(UTC);

        Date endDate = appointment.getEnd();
        ZonedDateTime endDateZoned = Instant.ofEpochMilli(endDate.getTime()).atZone(UTC);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Subject: {}, FreeBusy: {}, StartDate: {} - EndDate: {} TZ: {}", appointment.getSubject(),
                    appointment.getLegacyFreeBusyStatus(), startDateZoned.format(ICALFORMATTER),
                    endDateZoned.format(ICALFORMATTER), appointment.getTimeZone());
        }
    }

}
