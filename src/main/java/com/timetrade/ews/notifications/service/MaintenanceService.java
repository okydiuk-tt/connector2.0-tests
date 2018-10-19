package com.timetrade.ews.notifications.service;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.timetrade.ews.notifications.model.UserOfAccount;

public class MaintenanceService {

    private static final Logger LOG = LoggerFactory.getLogger(MaintenanceService.class);

    private int heartBeat;

    private StorageSyncingService storageSyncingService;

    private SubscriptionService subscriptionService;

    private static final AtomicBoolean IN_PROGRESS = new AtomicBoolean(false);

    public MaintenanceService(int heartBeat, StorageSyncingService storageSyncingService,
            SubscriptionService subscriptionService) {
        this.heartBeat = heartBeat;
        this.storageSyncingService = storageSyncingService;
        this.subscriptionService = subscriptionService;
    }

    // NEEDSWORK: (ENT-5285) Why do we need the IN_PROGRESS boolean? If this is injected as a bean, we've got a singleton. Can this method be
    // called from multiple threads?

    public void reconcileSubscriptions() {
        LOG.info("Looking for outdated subscriptions for all accounts");

        if (IN_PROGRESS.compareAndSet(false, true)) {
            try {
                int i = 0;
                Set<String> allAccounts = subscriptionService.getAllAccounts();
                for (String accountId : allAccounts) {
                    Set<UserOfAccount> lostSubscriptions = findLostSubscriptions(accountId);
                    if (!lostSubscriptions.isEmpty()) {
                        LOG.info("Resubscribing {} subscriptions for {}", lostSubscriptions.size(), accountId);
                        subscriptionService.subscribeAll(lostSubscriptions, true, u -> true);
                        LOG.info("Done with {}, {}/{}", accountId, ++i, allAccounts.size());
                    } else {
                        LOG.info("No lost subscriptions found for {}", accountId);
                    }
                }
            } finally {
                IN_PROGRESS.set(false);
            }
        } else {
            LOG.info("Another maintenance task is detected");
        }

        LOG.info("Subscriptions reconciliation is complete");
    }

    private Set<UserOfAccount> findLostSubscriptions(String accountId) {
        LOG.info("Looking for outdated subscriptions for {}", accountId);

        long now = System.currentTimeMillis() / 1000;
        LOG.info("Now: {}", now);

        // two heart-beats and little more:
        long minAllowedHeartBeat = now - (heartBeat * 2 + heartBeat / 10);
        LOG.info("Minimal allowed heart-beat: {}", minAllowedHeartBeat);

        Set<UserOfAccount> outdatedSubscriptionsKeys = storageSyncingService.findOutdatedCheckins(accountId, minAllowedHeartBeat);

        LOG.info("Found {} subscriptions for {}", outdatedSubscriptionsKeys.size(), accountId);

        return outdatedSubscriptionsKeys;

    }

}
