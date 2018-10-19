package com.timetrade.ews.notifications.configurationprovider.dao;

public class NoConfigAtOriginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoConfigAtOriginException(String message) {
        super(message);
    }

    public NoConfigAtOriginException(String message, Throwable cause) {
        super(message, cause);
    }

}