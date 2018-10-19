package com.timetrade.ews.notifications.configurationprovider.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "configurationProvider")
public class ConfigurationProviderProperties {
    private String singleConfigUrl;
    private String allAccountsConfigUrl;

    public ConfigurationProviderProperties() {
    }

    public ConfigurationProviderProperties(String singleConfigUrl, String allAccountsConfigUrl) {
        this.singleConfigUrl = singleConfigUrl;
        this.allAccountsConfigUrl = allAccountsConfigUrl;
    }

    public String getSingleConfigUrl() {
        return singleConfigUrl;
    }
    public void setSingleConfigUrl(String singleConfigUrl) {
        this.singleConfigUrl = singleConfigUrl;
    }
    public String getAllAccountsConfigUrl() {
        return allAccountsConfigUrl;
    }
    public void setAllAccountsConfigUrl(String allAccountsConfigUrl) {
        this.allAccountsConfigUrl = allAccountsConfigUrl;
    }
}