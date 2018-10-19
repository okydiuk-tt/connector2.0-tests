package com.timetrade.ews.notifications.service;

import java.util.Map;
import java.util.Set;

import com.timetrade.ews.notifications.model.PushSubscriptionMeta;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.model.history.HistoryRecord;

public interface StorageSyncingService {

    PushSubscriptionMeta getSubscription(UserOfAccount account);

    void removeSubscription(UserOfAccount account);

    void removeCheckin(UserOfAccount account);

    void sync(UserOfAccount account, PushSubscriptionMeta subscriptionMeta);

    Map<UserOfAccount, PushSubscriptionMeta> getSubscriptions(String accountId);

    void sync(UserOfAccount account, HistoryRecord historyRecord);

    Map<UserOfAccount, Set<HistoryRecord>> getHistory(UserOfAccount... accounts);	

    Set<UserOfAccount> getSubscribedEmails(String accountId);

    void persistCheckIn(UserOfAccount userOfAccount, long timestamp);

    Set<UserOfAccount> findOutdatedCheckins(String accountId, long latestAllowed);

    Set<String> getAllAccounts();

    void addToReattempt(UserOfAccount userOfAccount, boolean isResub);

    int getFromReattempt(UserOfAccount userOfAccount, boolean isResub);

    void removeFromReattempt(UserOfAccount userOfAccount, boolean isResub);

    Set<UserOfAccount> getReattempts(int maxReattempts, boolean isResub);

}
