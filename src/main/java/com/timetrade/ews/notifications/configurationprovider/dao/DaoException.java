package com.timetrade.ews.notifications.configurationprovider.dao;

public class DaoException extends RuntimeException {

    public DaoException(Exception e) {
        super(e);
    }

    public DaoException(String message) {
        super(message);
    }
}
