package com.timetrade.ews.notifications.eapiintegration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.eapiintegration.service.ResourcesProvider;
import com.timetrade.ews.notifications.service.SubscriptionService;
import com.timetrade.ews.notifications.service.SyncService;
import com.timetrade.platform.common.apicreds.EnableSoapApiCreds;
import com.timetrade.platform.eapi.client.ConfigurationServicesClient;
import com.timetrade.platform.eapi.client.annotations.EnableTimeTradeEApiClient;

@Configuration
@EnableTimeTradeEApiClient
@EnableSoapApiCreds
public class EApiClientConfiguration {

    @Bean
    public ResourcesProvider resourcesProvider(ConfigurationServicesClient configurationServiceClient, 
            SyncService syncService, SubscriptionService subscriptionService, ConfigurationResolver configurationResolver) {
        return new ResourcesProvider(configurationServiceClient, syncService, subscriptionService, configurationResolver);
    }

}
