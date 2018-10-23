package com.timetrade.connector2.qa.configurationprovider.service;

import com.timetrade.connector2.qa.configuration.EwsProperties.ExchangeCredentials;
import com.timetrade.connector2.qa.configuration.EwsProperties.SubscriptionsProperties;
import com.timetrade.connector2.qa.configurationprovider.model.EwsConfig.Endpoint;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;

public class ConfigurationResolver {
    private SubscriptionsProperties subscriptionsProperties;

    public Endpoint resolveExchangeEndpoint(String accountId) {

        return new Endpoint();
    }

    public ExchangeVersion resolveExchangeVersion() {
        return subscriptionsProperties.getEwsApiVersion();
    }

    public ExchangeCredentials resolveMasterUserPasswordAccess(String accountId) {

        return new ExchangeCredentials();
    }
}
