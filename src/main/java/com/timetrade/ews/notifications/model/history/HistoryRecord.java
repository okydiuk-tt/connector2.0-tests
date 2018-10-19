package com.timetrade.ews.notifications.model.history;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.StringJoiner;

import com.timetrade.ews.notifications.model.PushSubscriptionMeta;

public class HistoryRecord {
    private static final ZoneId UTC = ZoneId.of("UTC");

    private final ActionType action;
    private final Long time;

    public enum ActionType {
        SUBSCRIBED, UNSUBSCRIBED, RESTORED
    }
    
    public HistoryRecord() {
        action = null;
        time = null;
    }
    
    public HistoryRecord(ActionType action, Long time) {
        this.action = action;
        this.time = time;
    }

    public static HistoryRecord justSubscribed() {
        return new HistoryRecord(ActionType.SUBSCRIBED, nowUtcMillis());
    }

    public static HistoryRecord justUnsubscribed() {
        return new HistoryRecord(ActionType.UNSUBSCRIBED, nowUtcMillis());
    }

    public static HistoryRecord justRestored() {
        return new HistoryRecord(ActionType.RESTORED, nowUtcMillis());
    }

    public static HistoryRecord fromSubscriptionMeta(PushSubscriptionMeta subscriptionMeta) {
        HistoryRecord historyRecord;
        if (subscriptionMeta.isMarkedToDelete()) {
            historyRecord = HistoryRecord.justUnsubscribed();
        } else {
            historyRecord = subscriptionMeta.isRestored() ? HistoryRecord.justRestored() : HistoryRecord.justSubscribed();
        }
        return historyRecord;
    }

    public static String toHumanReadableTime(HistoryRecord historyRecord) {
        return Instant.ofEpochMilli(historyRecord.getTime()).atZone(UTC).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private static long nowUtcMillis() {
        return ZonedDateTime.now(UTC).toInstant().toEpochMilli();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("");
        joiner.add("HistoryRecord(ActionType=").add(action.toString()).add(", ")
        .add("time=").add(toHumanReadableTime(this)).add(")");
        
        return joiner.toString();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(action, time);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if((obj == null) || (getClass() != obj.getClass()))
            return false;
        
        HistoryRecord hrec = (HistoryRecord)obj;
        
        return Objects.equals(action, hrec.action) && Objects.equals(time, hrec.time);
    }
    
    public ActionType getAction() {
        return action;
    }

    public Long getTime() {
        return time;
    }

}
