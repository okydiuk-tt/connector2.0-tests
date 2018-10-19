package com.timetrade.ews.notifications.configurationprovider.service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;

import com.timetrade.ews.notifications.configurationprovider.dao.ConfigurationDao;
import com.timetrade.ews.notifications.configurationprovider.dao.NoConfigAtOriginException;
import com.timetrade.ews.notifications.configurationprovider.model.AccountEwsConfig;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfig;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfigWithVersion;

public class ConfigStoreClient {
    private static final Logger logger = LoggerFactory.getLogger(ConfigStoreClient.class);
    private ConfigurationDao configurationDao;
    private Optional<Cache> cache;

    public ConfigStoreClient(ConfigurationDao configurationDao, CacheManager cacheManager) {
        this.configurationDao = configurationDao;
        this.cache = Optional.ofNullable(cacheManager.getCache("configurations-meta"));
    }

    @Cacheable(value = "configurations-data", key = "#accountId")
    public EwsConfig get(String accountId) {
        requireAccountId(accountId);

        logger.info("Getting config for {}", accountId);

        Optional<EwsConfigWithVersion> existingConfig;
        if (cache.isPresent()) {
            existingConfig = Optional.ofNullable(cache.get().get(accountId, EwsConfigWithVersion.class));
        } else {
            existingConfig = Optional.empty();
        }

        EwsConfigWithVersion configToReturn;
        try {
            configToReturn = configurationDao.get(accountId, existingConfig);
        } catch (NoConfigAtOriginException e) {
            logger.error(e.getMessage(), e);
            throw new NoConfigAtOriginException(e.getMessage(), e);
        }

        cache.ifPresent(c -> c.put(accountId, configToReturn));

        return configToReturn.getEwsConfig();
    }

    public Set<AccountEwsConfig> getAccountsConfigs() {
        logger.info("Getting accounts' congigs from config-store");
        return configurationDao.getAccountConfigs();
    }

    private static void requireAccountId(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            String error = "AccountId is not set";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }
    }
}
