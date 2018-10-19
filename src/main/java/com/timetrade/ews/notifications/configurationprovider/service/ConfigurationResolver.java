package com.timetrade.ews.notifications.configurationprovider.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.timetrade.commons.crypto.QuickCipher;
import com.timetrade.ews.notifications.configuration.EwsProperties;
import com.timetrade.ews.notifications.configuration.EwsProperties.AccessType;
import com.timetrade.ews.notifications.configuration.EwsProperties.ExchangeCredentials;
import com.timetrade.ews.notifications.configuration.EwsProperties.ExchangeServerProperties;
import com.timetrade.ews.notifications.configuration.EwsProperties.KafkaProperties;
import com.timetrade.ews.notifications.configuration.EwsProperties.SubscriptionsProperties;
import com.timetrade.ews.notifications.configurationprovider.model.AccountEwsConfig;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfig;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfig.Delegate;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfig.Endpoint;
import com.timetrade.platform.common.apicreds.SoapCredentialsHolder;
import com.timetrade.platform.common.apicreds.security.ApiCredentials;

import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;

public class ConfigurationResolver {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationResolver.class);
    private ConfigStoreClient configStoreClient;
    private ExchangeServerProperties exchangeServiceProperties;
    private EwsProperties ewsProperties;
    private KafkaProperties kafkaProperties;
    private SoapCredentialsHolder soapCredentialsHolder;
    private SubscriptionsProperties subscriptionsProperties;

    public ConfigurationResolver(ConfigStoreClient configStoreClient, SoapCredentialsHolder soapCredentialsHolder,
            EwsProperties properties) {
        this.configStoreClient = configStoreClient;
        this.soapCredentialsHolder = soapCredentialsHolder;

        this.ewsProperties = properties;
        this.exchangeServiceProperties = properties.getExchangeServerMappings();
        this.kafkaProperties = properties.getKafka();
        this.subscriptionsProperties = properties.getSubscriptions();
    }

    public Endpoint resolveExchangeEndpoint(String accountId) {
        Endpoint endpoint;
        if (configStoreClient != null) {
            try {
                endpoint = configStoreClient.get(accountId).getEndpoint();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                endpoint = exchangeServiceProperties.getExchangeServerSettings(accountId).getEndpoint();
            }
        } else {
            endpoint = exchangeServiceProperties.getExchangeServerSettings(accountId).getEndpoint();
        }
        return endpoint;
    }

    public ExchangeVersion resolveExchangeVersion() {
        return subscriptionsProperties.getEwsApiVersion();
    }

    public ExchangeCredentials resolveMasterUserPasswordAccess(String accountId) {
        ExchangeCredentials masterCredentials;
        if (configStoreClient == null) {
            logger.info("Configuration-store client is disabled");
            masterCredentials = exchangeServiceProperties.getMasterCredentials(accountId);
        } else {
            try {
                EwsConfig ewsConfig = configStoreClient.get(accountId);
                Delegate delegate = ewsConfig.getDelegates().iterator().next();
                masterCredentials = new ExchangeCredentials(delegate.getUserid(), decryptPassword(delegate.getPassword()),
                        convertAccessType(ewsConfig.getAccessType()));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                masterCredentials = exchangeServiceProperties.getMasterCredentials(accountId);
            }
        }

        return masterCredentials;
    }

    private static String decryptPassword(String encrypted) {
        return QuickCipher.decrypt(encrypted);
    }

    public KafkaProperties resolveKafkaProperties() {
        return kafkaProperties;
    }

    public EwsProperties resolveEwsProperties() {
        return ewsProperties;
    }

    public SubscriptionsProperties resolveSubscriptionsProperties() {
        return subscriptionsProperties;
    }

    // NEEDSWORK: This is dumb.  Take a look into whether we can combine these two enums.  As it stands,
    // it's impossible for the default case to ever be executed.
    private static AccessType convertAccessType(
            com.timetrade.ews.notifications.configurationprovider.model.EwsConfig.AccessType accessType) {
        switch (accessType) {
        case IMPERSONATION:
            return AccessType.IMPERSONATION;
        case DELEGATE:
            return AccessType.DELEGATION;
        default :
            String error = "Unsupported acccessType " + accessType;
            logger.error(error);
            throw new IllegalArgumentException(error);

        }
    }

    public ApiCredentials resolveApiCredentials(String accountId) {
        Optional<ApiCredentials> credentialsOpional = soapCredentialsHolder.getCredentialsForAccount(accountId);
        if (credentialsOpional.isPresent()) {
            return credentialsOpional.get();
        }

        String error = "No api credentials for " + accountId;
        logger.error(error);
        throw new IllegalStateException(error);
    }

    public Set<String> resolveConfiguredAccounts() {
        if (configStoreClient != null) {
            try {
                return configStoreClient.getAccountsConfigs().stream()
                        .map(AccountEwsConfig::getAccountId)
                        .collect(Collectors.toSet());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                logger.warn("Falling back to properties");
                return exchangeServiceProperties.keySet();
            }
        }
        logger.info("Configuration-store client is disabled");
        return exchangeServiceProperties.keySet();
    }

    public static class TimeRange {
        private static final ZoneId UTC = ZoneId.of("UTC");

        private final long startTime;
        private final long endTime;

        private TimeRange(long initialAvailabilityLoadPeriod) {
            ZonedDateTime nowBeginning = LocalDate.now().atStartOfDay().plusSeconds(1).atZone(UTC);
            startTime = nowBeginning.toEpochSecond();
            endTime = nowBeginning.plusMonths(initialAvailabilityLoadPeriod).toEpochSecond();
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

    }

    public TimeRange resolveInitialAvailabilityTimeRange() {
        return new TimeRange(ewsProperties.getInitialAvailabilityLoadPeriod());
    }

    public TimeRange resolveRecurringEventsPreloadTimeRange() {
        return new TimeRange(ewsProperties.getRecurringEventsPreloadPeriod());
    }

}
