package com.timetrade.ews.notifications.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.timetrade.ews.notifications.model.PushSubscriptionMeta;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.model.history.HistoryRecord;

public class RedisSyncingService implements StorageSyncingService {

    private static final Logger LOG = LoggerFactory.getLogger(RedisSyncingService.class);

    private RedisTemplate<String, PushSubscriptionMeta> subscriptionsTemplate;
    private RedisTemplate<String, Set<HistoryRecord>> historyTemplate;
    private RedisTemplate<String, String> checkInsTemplate;
    private RedisTemplate<String, String> reattemptTemplate;

    private static final String CHECKINS_PATTERN = "checkins:*";

    public RedisSyncingService(RedisTemplate<String, PushSubscriptionMeta> subscrpitionsTemplate,
            RedisTemplate<String, String> checkInsTemplate, RedisTemplate<String, Set<HistoryRecord>> historyTemplate,
            RedisTemplate<String, String> failedTemplate) {
        this.subscriptionsTemplate = subscrpitionsTemplate;
        this.checkInsTemplate = checkInsTemplate;
        this.historyTemplate = historyTemplate;
        this.reattemptTemplate = failedTemplate;
    }

    private static String getCheckInKey(String accountId) {
        return String.format("checkins:%s", accountId);
    }

    private static String fromCheckInKey(String checkinKey) {
        return checkinKey.replace("checkins:", "");
    }

    private static String getAllAccountsCheckinsPattern() {
        return CHECKINS_PATTERN;
    }

    private static String getSubscriptionKey(UserOfAccount userOfAccount) {
        return String.format("%s:%s", userOfAccount.getAccountId(), userOfAccount.getUsername());
    }

    private static UserOfAccount getFromSubscriptionKey(String emailKey) {
        String[] keySplit = emailKey.split(":");
        return new UserOfAccount(keySplit[1], keySplit[0]);
    }

    private static String getHistoryKey(UserOfAccount userOfAccount) {
        return String.format("history:%s:%s", userOfAccount.getAccountId(), userOfAccount.getUsername());
    }

    private static String getEmailFromSubscriptionKey(String subscriptionKey) {
        return subscriptionKey.substring(subscriptionKey.indexOf(':') + 1);
    }

    private static String getAllAccountsSubscriptionsPattern(String accountId) {
        return String.format("%s:*", accountId);
    }

    private static String getReattemptPattern(boolean isResub) {
        return String.format("reattempts:%s", isResub ? "resub" : "sub");
    }

    public void persistCheckIn(UserOfAccount userOfAccount, long timestamp) {
        String checkInKey = getCheckInKey(userOfAccount.getAccountId());
        LOG.info("Adding checkin at {} for {} to pool {}", timestamp, userOfAccount, checkInKey);
        checkInsTemplate.boundZSetOps(checkInKey)
        .add(getSubscriptionKey(userOfAccount), timestamp);
    }

    public Set<UserOfAccount> findOutdatedCheckins(String accountId, long latestAllowed) {
        return checkInsTemplate.boundZSetOps(getCheckInKey(accountId))
                .rangeByScore(0, latestAllowed).stream()
                .map(RedisSyncingService::getFromSubscriptionKey)
                .collect(Collectors.toSet());
    }

    public PushSubscriptionMeta getSubscription(UserOfAccount userOfAccount) {
        return subscriptionsTemplate.boundValueOps(getSubscriptionKey(userOfAccount)).get();
    }

    public void removeSubscription(UserOfAccount userOfAccount) {
        subscriptionsTemplate.delete(getSubscriptionKey(userOfAccount));
    }

    public void removeCheckin(UserOfAccount userOfAccount) {
        checkInsTemplate.boundZSetOps(getCheckInKey(userOfAccount.getAccountId()))
        .remove(getSubscriptionKey(userOfAccount));
    }

    public void sync(UserOfAccount userOfAccount, PushSubscriptionMeta subscriptionMeta) {
        subscriptionsTemplate.boundValueOps(getSubscriptionKey(userOfAccount)).set(subscriptionMeta);
    }

    public void sync(UserOfAccount userOfAccount, HistoryRecord historyRecord) {
        Optional<Set<HistoryRecord>> existingRecords =
                Optional.ofNullable(historyTemplate.boundValueOps(getHistoryKey(userOfAccount)).get());

        Set<HistoryRecord> updatedRecords = existingRecords.orElse(new HashSet<HistoryRecord>());
        updatedRecords.add(historyRecord);

        historyTemplate.boundValueOps(getHistoryKey(userOfAccount)).set(updatedRecords);
    }

    public Map<UserOfAccount, Set<HistoryRecord>> getHistory(UserOfAccount... accounts) {
        List<String> keys = Arrays.stream(accounts).map(RedisSyncingService::getHistoryKey).collect(Collectors.toList());
        List<Set<HistoryRecord>> allValues = historyTemplate.opsForValue().multiGet(keys);

        Map<UserOfAccount, Set<HistoryRecord>> histories = new HashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            histories.put(accounts[i], Optional.ofNullable(allValues.get(i)).orElse(Collections.emptySet()));
        }

        return histories;
    }

    public Set<String> getAllAccounts() {
        return checkInsTemplate.keys(getAllAccountsCheckinsPattern())
                .stream()
                .map(RedisSyncingService::fromCheckInKey)
                .collect(Collectors.toSet());
    }

    public Map<UserOfAccount, PushSubscriptionMeta> getSubscriptions(String accountId) {
        Set<String> keys = subscriptionsTemplate.keys(getAllAccountsSubscriptionsPattern(accountId));
        LOG.info("Found {} subscriptions for {}", keys.size(), accountId);

        List<String> allKeys = new ArrayList<>(keys);

        List<PushSubscriptionMeta> allValues = subscriptionsTemplate.opsForValue().multiGet(allKeys);

        Map<UserOfAccount, PushSubscriptionMeta> subscriptions = new HashMap<>();
        for (int i = 0; i < allKeys.size(); i++) {
            subscriptions.put(new UserOfAccount(getEmailFromSubscriptionKey(allKeys.get(i)), accountId), allValues.get(i));
        }

        return subscriptions;
    }

    public Set<UserOfAccount> getSubscribedEmails(String accountId) {
        Set<String> keys = subscriptionsTemplate.keys(getAllAccountsSubscriptionsPattern(accountId));
        LOG.info("Found {} subscriptions for {}", keys.size(), accountId);
        return keys.stream()
                .map(k -> new UserOfAccount(getEmailFromSubscriptionKey(k), accountId))
                .collect(Collectors.toSet());
    }

    public Set<UserOfAccount> getReattempts(int maxReattempts, boolean isResub) {
        return reattemptTemplate.boundZSetOps(getReattemptPattern(isResub)).range(0, maxReattempts).stream()
                .map(RedisSyncingService::getFromSubscriptionKey)
                .collect(Collectors.toSet());
    }

    public void addToReattempt(UserOfAccount userOfAccount, boolean isResub) {
        reattemptTemplate.boundZSetOps(getReattemptPattern(isResub))
        .incrementScore(getSubscriptionKey(userOfAccount), 1);
    }

    public int getFromReattempt(UserOfAccount userOfAccount, boolean isResub) {
        return Optional.ofNullable(reattemptTemplate.boundZSetOps(getReattemptPattern(isResub))
                .score(getSubscriptionKey(userOfAccount)))
                .orElse(Double.valueOf(0))
                .intValue();

    }

    public void removeFromReattempt(UserOfAccount userOfAccount, boolean isResub) {
        reattemptTemplate.boundZSetOps(getReattemptPattern(isResub))
        .remove(getSubscriptionKey(userOfAccount));
    }

}
