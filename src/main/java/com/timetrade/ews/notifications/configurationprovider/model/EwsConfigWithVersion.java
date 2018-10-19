package com.timetrade.ews.notifications.configurationprovider.model;

import java.io.Serializable;

public class EwsConfigWithVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    private final EwsConfig ewsConfig;
    private final String version;
    
    public EwsConfigWithVersion(EwsConfig ewsConfig, String version) {
        this.ewsConfig = ewsConfig;
        this.version = version;
    }
    
    public EwsConfig getEwsConfig() {
        return ewsConfig;
    }
    public String getVersion() {
        return version;
    }
}
