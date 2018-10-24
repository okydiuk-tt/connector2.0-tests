package com.timetrade.connector2.qa.configurationprovider.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class EwsConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private Endpoint endpoint;
    private Set<Delegate> delegates;
    private AccessType accessType;

    // NEEDSWORK: This enum is exactly the same as the EwsProperties enum. There's even a conversion routine
    // in ConfigurationResolver to go between them. We should look at whether it's meaningful to keep them separate
    // or just combine them.

    public enum AccessType {
        DELEGATE, IMPERSONATION
    }

    public static class Endpoint implements Serializable {
        private static final long serialVersionUID = 1L;

        private String url;
        private Boolean autodiscover;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Boolean getAutodiscover() {
            return autodiscover;
        }

        public void setAutodiscover(Boolean autodiscover) {
            this.autodiscover = autodiscover;
        }
    }

    public static class Delegate implements Serializable {
        private static final long serialVersionUID = 1L;

        private String userid;
        private String password;

        @Override
        public int hashCode() {
            return Objects.hash(serialVersionUID, userid, password);
        }

        @Override
        public boolean equals(Object obj) {
            if(this == obj)
                return true;

            if((obj == null) || (getClass() != obj.getClass()))
                return false;

            Delegate del = (Delegate)obj;

            return Objects.equals(userid, del.userid) && Objects.equals(password, del.password);
        }
    }
}
