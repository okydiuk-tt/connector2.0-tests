package com.timetrade.connector2.qa.model;

import java.util.Objects;
import java.util.StringJoiner;

public class UserOfAccount {
    private final String username;
    private final String accountId;
    
    public UserOfAccount(String username, String accountId) {
        this.username = username;
        this.accountId = accountId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username, accountId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if((obj == null) || (getClass() != obj.getClass()))
            return false;
        
        UserOfAccount uoa = (UserOfAccount)obj;
        
        return Objects.equals(username, uoa.username) && Objects.equals(accountId, uoa.accountId);
    }
    
    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("");
        joiner.add("UserOfAccount(Username=").add(username).add(", ")
        .add("accountId=").add(accountId).add(")");
        
        return joiner.toString();
    }

    public String getUsername() {
        return username;
    }

    public String getAccountId() {
        return accountId;
    }
}
