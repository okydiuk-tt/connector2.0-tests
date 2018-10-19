package com.timetrade.ews.notifications.configurationprovider.model;

import java.util.Objects;

public class AccountEwsConfig {
    private String accountId;
    private EwsConfig ewsConfig;
    
    public AccountEwsConfig() {
        
    }
    
    public AccountEwsConfig(String accountId, EwsConfig ewsConfig) {
        this.accountId = accountId;
        this.ewsConfig = ewsConfig;
    }
    
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    public EwsConfig getEwsConfig() {
        return ewsConfig;
    }
    public void setEwsConfig(EwsConfig ewsConfig) {
        this.ewsConfig = ewsConfig;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, ewsConfig);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (getClass() != obj.getClass()))
            return false;
        
        AccountEwsConfig aec = (AccountEwsConfig)obj;
        return Objects.equals(accountId, aec.accountId) && Objects.equals(ewsConfig, aec.ewsConfig);
    }
}
