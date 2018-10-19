package com.timetrade.ews.notifications.configurationprovider.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.timetrade.ews.notifications.configurationprovider.dao.ConfigurationDao;
import com.timetrade.ews.notifications.configurationprovider.dao.RestConfigurationDao;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigStoreClient;

@Profile("configProvider")
@Configuration
@EnableConfigurationProperties(ConfigurationProviderProperties.class)
public class ConfigurationProviderConfig {

    @Bean
    public ConfigStoreClient configStoreClient(ConfigurationDao configurationDao, CacheManager cacheManager) {
        return new ConfigStoreClient(configurationDao, cacheManager);
    }

    @Bean
    public ConfigurationDao restConfigurationDao(ConfigurationProviderProperties properties) {
        return new RestConfigurationDao(restTemplate(), properties);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
