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

        public Delegate() {
        }

        public Delegate(String userid, String password) {
            this.userid = userid;
            this.password = password;
        }

        public String getUserid() {
            return userid;
        }
        public void setUserid(String userid) {
            this.userid = userid;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

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

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public Set<Delegate> getDelegates() {
        return delegates;
    }

    public void setDelegates(Set<Delegate> delegates) {
        this.delegates = delegates;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

}
