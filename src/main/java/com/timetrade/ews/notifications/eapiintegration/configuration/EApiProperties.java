package com.timetrade.ews.notifications.eapiintegration.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "timetrade.eapi")
public class EApiProperties {
    private String host;
    
    public EApiProperties() {
        
    }
    
    public EApiProperties(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
