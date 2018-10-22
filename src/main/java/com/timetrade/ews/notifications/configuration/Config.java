package com.timetrade.ews.notifications.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBException;

import com.timetrade.ews.notifications.service.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;

import com.timetrade.ews.notifications.configurationprovider.configuration.ConfigurationProviderConfig;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigStoreClient;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.platform.common.apicreds.SoapCredentialsHolder;
import com.timetrade.platform.eapi.client.config.EapiConfig;

@Import(
//        { KafkaConfig.class, HAConfig.class, RedisConfig.class, PeriodicTasksConfig.class, CacheConfiguration.class,
//    ConfigurationProviderConfig.class,
 EapiConfig.class )

// EApiClientConfiguration.class
@Configuration
@EnableConfigurationProperties(EwsProperties.class)
public class Config {


    @Bean
    public ConfigurationResolver configurationResolver(
            ObjectProvider<ConfigStoreClient> configStoreClient, SoapCredentialsHolder soapCredentialsHolder,
            EwsProperties ewsProperties) {
        return new ConfigurationResolver(configStoreClient.getIfAvailable(), soapCredentialsHolder, ewsProperties);
    }

}
