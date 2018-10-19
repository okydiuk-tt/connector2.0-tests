package com.timetrade.ews.notifications.service;

import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.service.wrappers.TTExchangeService;

public class ExchangeServiceFactory {

    private ConfigurationResolver configurationResolver;

    public ExchangeServiceFactory(ConfigurationResolver configurationResolver) {
        this.configurationResolver = configurationResolver;
    }

    public TTExchangeService createExchangeService(UserOfAccount userOfAccount) {
        return new TTExchangeService(userOfAccount, configurationResolver);
    }
}
