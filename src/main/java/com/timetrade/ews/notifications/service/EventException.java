package com.timetrade.ews.notifications.service;

public class EventException extends RuntimeException {

    public EventException(Exception e) {
        super(e);
    }
}
