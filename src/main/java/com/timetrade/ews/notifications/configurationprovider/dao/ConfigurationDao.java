package com.timetrade.ews.notifications.configurationprovider.dao;

import java.util.Optional;
import java.util.Set;

import com.timetrade.ews.notifications.configurationprovider.model.AccountEwsConfig;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfigWithVersion;

public interface ConfigurationDao {
    EwsConfigWithVersion get(String accountId, Optional<EwsConfigWithVersion> existing);
    Set<AccountEwsConfig> getAccountConfigs();
}
