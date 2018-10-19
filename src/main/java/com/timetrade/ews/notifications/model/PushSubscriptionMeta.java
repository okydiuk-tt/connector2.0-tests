package com.timetrade.ews.notifications.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import microsoft.exchange.webservices.data.notification.PushSubscription;

public class PushSubscriptionMeta {

    private final String id;
    private final String waterMark;
    private boolean markedToDelete = false;
    private boolean restored = false;

    public PushSubscriptionMeta(String id, String waterMark) {
        this.id = id;
        this.waterMark = waterMark;
    }

    @JsonCreator
    public PushSubscriptionMeta(
            @JsonProperty("id") String id, 
            @JsonProperty("waterMark") String waterMark, 
            @JsonProperty("markedToDelete") Boolean markedToDelete, 
            @JsonProperty("restored") Boolean restored) {
        this(id, waterMark);
        this.markedToDelete = markedToDelete;
        this.restored = restored;
    }

    public PushSubscriptionMeta(PushSubscription origin) {
        this(origin.getId(), origin.getWaterMark());
    }

    public PushSubscriptionMeta markToDelete() {
        this.markedToDelete = true;
        return this;
    }

    public boolean isMarkedToDelete() {
        return markedToDelete;
    }

    public void setMarkedToDelete(boolean markedToDelete) {
        this.markedToDelete = markedToDelete;
    }

    public boolean isRestored() {
        return restored;
    }

    public void setRestored(boolean restored) {
        this.restored = restored;
    }

    public String getId() {
        return id;
    }

    public String getWaterMark() {
        return waterMark;
    }

}
