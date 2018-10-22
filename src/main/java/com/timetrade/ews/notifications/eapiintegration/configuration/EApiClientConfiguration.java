package com.timetrade.ews.notifications.eapiintegration.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.platform.common.apicreds.EnableSoapApiCreds;
import com.timetrade.platform.eapi.client.ConfigurationServicesClient;
import com.timetrade.platform.eapi.client.annotations.EnableTimeTradeEApiClient;

@Configuration
@EnableTimeTradeEApiClient
@EnableSoapApiCreds
public class EApiClientConfiguration {

}
