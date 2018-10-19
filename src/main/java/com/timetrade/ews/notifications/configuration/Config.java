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
import com.timetrade.ews.notifications.converter.SubscriptionRateConverter;
import com.timetrade.ews.notifications.converter.UriConverter;
import com.timetrade.ews.notifications.eapiintegration.configuration.EApiClientConfiguration;
import com.timetrade.ews.notifications.model.PushSubscriptionMeta;
import com.timetrade.ews.notifications.model.history.HistoryRecord;
import com.timetrade.platform.common.apicreds.SoapCredentialsHolder;
import com.timetrade.platform.eapi.client.config.EapiConfig;

@Import({ KafkaConfig.class, HAConfig.class, RedisConfig.class, PeriodicTasksConfig.class, CacheConfiguration.class,
    ConfigurationProviderConfig.class, EapiConfig.class, EApiClientConfiguration.class })
@Configuration
@EnableConfigurationProperties(EwsProperties.class)
public class Config {

    @Bean
    public SubscriptionService subscriptionService(StorageSyncingService redisSyncingService, SyncService syncService,
            ConfigurationResolver configurationResolver) {
        return new SubscriptionService(redisSyncingService, syncService, configurationResolver);
    }

    @Bean
    public StorageSyncingService redisSyncingService(RedisTemplate<String, PushSubscriptionMeta> subscrpitionsTemplate,
            RedisTemplate<String, String> checkInsTemplate, RedisTemplate<String, Set<HistoryRecord>> historyTemplate,
            RedisTemplate<String, String> reattemptTemplate) {
        return new RedisSyncingService(subscrpitionsTemplate, checkInsTemplate, historyTemplate, reattemptTemplate);
    }

    @Bean
    public SyncService syncService(EventConversionService eventConversionService,
            KafkaTemplate<Integer, String> kafkaTemplate, ConfigurationResolver configurationResolver,
            CacheManager cacheManager, ExchangeServiceFactory exchangeServiceFactory) {
        return new SyncService(eventConversionService, kafkaTemplate, configurationResolver, cacheManager, exchangeServiceFactory);
    }

    @Bean
    public MaintenanceService maintenanceService(StorageSyncingService storageSyncingService,
            SubscriptionService subscriptionService, EwsProperties ewsProperties) {
        return new MaintenanceService(ewsProperties.getSubscriptions().getHeartBeat(), storageSyncingService, subscriptionService);
    }

    @Bean
    public ConfigurationResolver configurationResolver(
            ObjectProvider<ConfigStoreClient> configStoreClient, SoapCredentialsHolder soapCredentialsHolder,
            EwsProperties ewsProperties) {
        return new ConfigurationResolver(configStoreClient.getIfAvailable(), soapCredentialsHolder, ewsProperties);
    }

    @Bean
    public ParsingService parsingService(ExchangeServiceFactory exchangeServiceFactory) throws JAXBException {
        return new ParsingService(exchangeServiceFactory);
    }

    @Bean
    public EventConversionService eventConversionService() {
        return new EventConversionService();
    }

    @Bean
    public ConversionService conversionService(List<Converter<?, ?>> converters) {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(new HashSet<>(converters));
        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean
    public UriConverter uriConverter() {
        return new UriConverter();
    }

    @Bean
    public SubscriptionRateConverter subscriptionRateConverter() {
        return new SubscriptionRateConverter();
    }

    @Bean
    public ExchangeServiceFactory exchangeServiceFactory(ConfigurationResolver configurationResolver) {
        return new ExchangeServiceFactory(configurationResolver);
    }
}
