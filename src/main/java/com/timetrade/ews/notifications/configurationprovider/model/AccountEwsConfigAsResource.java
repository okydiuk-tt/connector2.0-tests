package com.timetrade.ews.notifications.configurationprovider.model;

import java.util.Objects;

import org.springframework.hateoas.Link;

public class AccountEwsConfigAsResource extends AccountEwsConfig {
    private Link[] links;

    public AccountEwsConfigAsResource() {
        super();
    }

    public AccountEwsConfigAsResource(Link[] links,String accountId, EwsConfig ewsConfig) {
        super(accountId, ewsConfig);
        this.links = links;
    }

    public Link[] getLinks() {
        return links;
    }

    public void setLinks(Link[] links) {
        this.links = links;
    }

    @Override
    public int hashCode() {
        return Objects.hash(links, getAccountId(), getEwsConfig());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;

        if((obj == null) || (getClass() != obj.getClass()))
            return false;

        AccountEwsConfigAsResource aec = (AccountEwsConfigAsResource)obj;

        return super.equals((AccountEwsConfig)obj) && (links ==aec.links);

    }
}
