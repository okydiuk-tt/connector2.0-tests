package com.timetrade.ews.notifications.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.StringJoiner;

public class Event {
    private static final ZoneId UTC = ZoneId.of("UTC");
    private final String id;
    private final String resource;
    private final Origin origin;
    private final long timestamp;
    private Status status;
    private Long startTime;
    private Long endTime;
    private boolean isDeleted;

    public enum Status {
        BUSY, FREE, OOO, TENT
    }

    public enum Origin {
        EWS
    }

    public Event(String resource, String id, Status status, long startTime, long endTime) {
        this(resource, id, false);
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private Event(String resource, String id, boolean isDeleted) {
        this.resource = resource;
        this.id = id;
        this.origin = Origin.EWS;
        this.isDeleted = isDeleted;
        this.timestamp = ZonedDateTime.now(UTC).toEpochSecond();
    }

    public static Event deletedItem(String resource, String id) {
        return new Event(resource, id, true);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("");
        joiner.add("Event(id=").add(id).add(", ")
        .add("resource=").add(resource).add(", ")
        .add("origin=").add(origin.toString()).add(", ")
        .add("timestamp=").add(Long.toString(timestamp)).add(", ")
        .add("status=").add(status.toString()).add(", ")
        .add("startTime=").add(Long.toString(startTime)).add(", ")
        .add("endTime=").add(Long.toString(endTime)).add(", ")
        .add("isDeleted=").add(Boolean.toString(isDeleted)).add(")");

        return joiner.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, resource, origin, timestamp, status, startTime, endTime, isDeleted);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if((obj == null) || (getClass() != obj.getClass()))
            return false;

        Event evt = (Event)obj;

        return Objects.equals(id, evt.id) && Objects.equals(resource, evt.resource) && Objects.equals(origin, evt.origin)
                && Objects.equals(timestamp, evt.timestamp) && Objects.equals(status, evt.status)
                && Objects.equals(startTime, evt.startTime) && Objects.equals(endTime, evt.endTime) && Objects.equals(isDeleted, evt.isDeleted);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public String getResource() {
        return resource;
    }

    public Origin getOrigin() {
        return origin;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
