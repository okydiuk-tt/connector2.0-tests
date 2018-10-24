package com.timetrade.connector2.qa.configuration;

public class EwsProperties {

    public static class ExchangeCredentials {
        private String exchangeUsername;
        private String exchangePassword;
        private AccessType accessType;

        public ExchangeCredentials(String username, String password, AccessType accessType) {
            this.exchangeUsername = username;
            this.exchangePassword = password;
            this.accessType = accessType;
        }

        public String getExchangeUsername() {
            return exchangeUsername;
        }
        public String getExchangePassword() {
            return exchangePassword;
        }
        public AccessType getAccessType() {
            return accessType;
        }

    }

    public enum AccessType {
        DELEGATION, IMPERSONATION
    }

}
