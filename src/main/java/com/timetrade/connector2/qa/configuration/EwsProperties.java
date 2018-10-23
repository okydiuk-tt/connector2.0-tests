package com.timetrade.connector2.qa.configuration;

import java.util.HashMap;
import java.util.Map;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.properties.ConfigurationProperties;

import com.timetrade.connector2.qa.configurationprovider.model.EwsConfig;

import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;

//@ConfigurationProperties
public class EwsProperties {
    private Boolean logIncomingXml = false;
    private Integer recurringEventsPreloadPeriod;
    private Integer initialAvailabilityLoadPeriod;
    private Map<String, String> cacheSpecs;
    private KafkaProperties kafka;
    private SubscriptionsProperties subscriptions;
    private ExchangeServerProperties exchangeServerMappings;
    private RedisDbProperties redisDb;

    public static class SubscriptionsProperties {
        private boolean dryRun = false;
//        private SubscriptionRate rate;
        private ReattemptsPolicy reattempts;
        private ExchangeVersion ewsApiVersion;
        private Integer heartBeat;
        private String callbackEndpoint;

//        public static class SubscriptionRate {
//            private final int batchSize;
//            private final long delayMillis;
//
//            public SubscriptionRate(int batchSize, long delayMillis) {
//                this.batchSize = batchSize;
//                this.delayMillis = delayMillis;
//            }
//
//            public int getBatchSize() {
//                return batchSize;
//            }
//
//            public long getDelayMillis() {
//                return delayMillis;
//            }
//        }

        public static class ReattemptsPolicy {
            private int maxTimes;
            private int minutes;

            public ReattemptsPolicy() {

            }

            public ReattemptsPolicy(int maxTimes, int minutes) {
                this.maxTimes = maxTimes;
                this.minutes = minutes;
            }

            public int getMaxTimes() {
                return maxTimes;
            }
            public void setMaxTimes(int maxTimes) {
                this.maxTimes = maxTimes;
            }
            public int getMinutes() {
                return minutes;
            }
            public void setMinutes(int minutes) {
                this.minutes = minutes;
            }
        }

        public boolean isDryRun() {
            return dryRun;
        }

        public void setDryRun(boolean dryRun) {
            this.dryRun = dryRun;
        }

        public ReattemptsPolicy getReattempts() {
            return reattempts;
        }

        public void setReattempts(ReattemptsPolicy reattempts) {
            this.reattempts = reattempts;
        }

        public ExchangeVersion getEwsApiVersion() {
            return ewsApiVersion;
        }

        public void setEwsApiVersion(ExchangeVersion ewsApiVersion) {
            this.ewsApiVersion = ewsApiVersion;
        }

        public Integer getHeartBeat() {
            return heartBeat;
        }

        public void setHeartBeat(Integer heartBeat) {
            this.heartBeat = heartBeat;
        }

        public String getCallbackEndpoint() {
            return callbackEndpoint;
        }

        public void setCallbackEndpoint(String callbackEndpoint) {
            this.callbackEndpoint = callbackEndpoint;
        }
    }

    public static class KafkaProperties {
        private String bootstrapServers;
        private String topicPattern;
        private Integer numberOfConsumers;

        public KafkaProperties(){

        }

        public KafkaProperties(String bootstrapServers, String topicPattern, Integer numberOfConsumers) {
            this.bootstrapServers = bootstrapServers;
            this.topicPattern = topicPattern;
            this.numberOfConsumers = numberOfConsumers;
        }

        public String getBootstrapServers() {
            return bootstrapServers;
        }
        public void setBootstrapServers(String bootstrapServers) {
            this.bootstrapServers = bootstrapServers;
        }
        public String getTopicPattern() {
            return topicPattern;
        }
        public void setTopicPattern(String topicPattern) {
            this.topicPattern = topicPattern;
        }
        public Integer getNumberOfConsumers() {
            return numberOfConsumers;
        }
        public void setNumberOfConsumers(Integer numberOfConsumers) {
            this.numberOfConsumers = numberOfConsumers;
        }
    }

    public static class ExchangeServerProperties extends HashMap<String, ExchangeServerSettings> {
        private static final long serialVersionUID = 1L;

        public ExchangeCredentials getMasterCredentials(String accountId) {
            ExchangeServerSettings exchangeServerSettings = getExchangeServerSettings(accountId);
            if (exchangeServerSettings == null) {
                String error = "No impersonate/delegate user configured for account " + accountId;
                throw new IllegalStateException(error);
            }
            return new ExchangeCredentials(exchangeServerSettings.getCreds().getExchangeUsername(),
                    exchangeServerSettings.getCreds().getExchangePassword(), exchangeServerSettings.getCreds().getAccessType());
        }

        public ExchangeServerSettings getExchangeServerSettings(String accountId) {
            return get(accountId);
        }

    }

    public static class ExchangeServerSettings {
        private EwsConfig.Endpoint endpoint;
        private ExchangeCredentials creds;

        public EwsConfig.Endpoint getEndpoint() {
            return endpoint;
        }
        public void setEndpoint(EwsConfig.Endpoint endpoint) {
            this.endpoint = endpoint;
        }
        public ExchangeCredentials getCreds() {
            return creds;
        }
        public void setCreds(ExchangeCredentials creds) {
            this.creds = creds;
        }

    }

    public static class ExchangeCredentials {
        private String exchangeUsername;
        private String exchangePassword;
        private AccessType accessType;

        public ExchangeCredentials() {

        }

        public ExchangeCredentials(String username, String password, AccessType accessType) {
            this.exchangeUsername = username;
            this.exchangePassword = password;
            this.accessType = accessType;
        }

        public String getExchangeUsername() {
            return exchangeUsername;
        }
        public void setExchangeUsername(String exchangeUsername) {
            this.exchangeUsername = exchangeUsername;
        }
        public String getExchangePassword() {
            return exchangePassword;
        }
        public void setExchangePassword(String exchangePassword) {
            this.exchangePassword = exchangePassword;
        }
        public AccessType getAccessType() {
            return accessType;
        }
        public void setAccessType(AccessType accessType) {
            this.accessType = accessType;
        }

    }

    public enum AccessType {
        DELEGATION, IMPERSONATION
    }

    public static class RedisDbProperties {
        private String host;
        private Integer port;

        public RedisDbProperties() {

        }

        public RedisDbProperties(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        public String getHost() {
            return host;
        }
        public void setHost(String host) {
            this.host = host;
        }
        public Integer getPort() {
            return port;
        }
        public void setPort(Integer port) {
            this.port = port;
        }
    }

    public Boolean getLogIncomingXml() {
        return logIncomingXml;
    }

    public void setLogIncomingXml(Boolean logIncomingXml) {
        this.logIncomingXml = logIncomingXml;
    }

    public Integer getRecurringEventsPreloadPeriod() {
        return recurringEventsPreloadPeriod;
    }

    public void setRecurringEventsPreloadPeriod(Integer recurringEventsPreloadPeriod) {
        this.recurringEventsPreloadPeriod = recurringEventsPreloadPeriod;
    }

    public Integer getInitialAvailabilityLoadPeriod() {
        return initialAvailabilityLoadPeriod;
    }

    public void setInitialAvailabilityLoadPeriod(Integer initialAvailabilityLoadPeriod) {
        this.initialAvailabilityLoadPeriod = initialAvailabilityLoadPeriod;
    }

    public Map<String, String> getCacheSpecs() {
        return cacheSpecs;
    }

    public void setCacheSpecs(Map<String, String> cacheSpecs) {
        this.cacheSpecs = cacheSpecs;
    }

    public KafkaProperties getKafka() {
        return kafka;
    }

    public void setKafka(KafkaProperties kafka) {
        this.kafka = kafka;
    }

    public SubscriptionsProperties getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(SubscriptionsProperties subscriptions) {
        this.subscriptions = subscriptions;
    }

    public ExchangeServerProperties getExchangeServerMappings() {
        return exchangeServerMappings;
    }

    public void setExchangeServerMappings(ExchangeServerProperties exchangeServerMappings) {
        this.exchangeServerMappings = exchangeServerMappings;
    }

    public RedisDbProperties getRedisDb() {
        return redisDb;
    }

    public void setRedisDb(RedisDbProperties redisDb) {
        this.redisDb = redisDb;
    }
}
