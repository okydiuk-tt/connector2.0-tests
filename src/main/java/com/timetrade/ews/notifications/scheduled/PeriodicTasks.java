package com.timetrade.ews.notifications.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.timetrade.ews.notifications.configuration.HAConfig.LeaderLatchRegistry;
import com.timetrade.ews.notifications.configuration.HAConfig.LeaderLatchRegistry.LatchKey;
import com.timetrade.ews.notifications.eapiintegration.service.ResourcesProvider;
import com.timetrade.ews.notifications.service.MaintenanceService;
import com.timetrade.ews.notifications.service.SubscriptionService;

public class PeriodicTasks {

    private static final Logger LOG = LoggerFactory.getLogger(PeriodicTasks.class);

    private MaintenanceService maintenanceService;

    private ResourcesProvider resourcesProvider;

    private SubscriptionService subscriptionService;

    private LeaderLatchRegistry leaderLatchRegistry;

    public PeriodicTasks(MaintenanceService maintenanceService, ResourcesProvider resourcesProvider,
            SubscriptionService subscriptionService, LeaderLatchRegistry leaderLatchRegistry) {
        this.maintenanceService = maintenanceService;
        this.resourcesProvider = resourcesProvider;
        this.subscriptionService = subscriptionService;
        this.leaderLatchRegistry = leaderLatchRegistry;
    }

    @Scheduled(initialDelayString = "#{${subscriptions.heartBeat} * 1000}", 
            fixedRateString = "#{${subscriptions.heartBeat} * 1000}")
    public void reconcileSubscriptions() {

        LOG.info("Looking for outdated subscriptions to reanimate them");

        runJob(leaderLatchRegistry, LatchKey.MAINTENANCE, () -> maintenanceService.reconcileSubscriptions());

    }

    @Scheduled(initialDelayString = "#{${resourcesSyncPeriod} * 1000}", 
            fixedRateString = "#{${resourcesSyncPeriod} * 1000}")
    public void syncResources() {

        LOG.info("Syncing resources");

        runJob(leaderLatchRegistry, LatchKey.RESOURCES_SYNC, () -> resourcesProvider.syncResources());

    }

    @Scheduled(initialDelayString = "#{${subscriptions.reattempts.minutes} * 60 * 1000}", 
            fixedRateString = "#{${subscriptions.reattempts.minutes} * 60 * 1000}")
    public void reattamptSubscriptions() {

        LOG.info("Reattempting subsctiptions");

        runJob(leaderLatchRegistry, LatchKey.REATTEMPTS, () -> subscriptionService.runReattempts());

    }

    private void runJob(LeaderLatchRegistry leaderLatchRegistry, LatchKey latchKey, Job job) {
        if (leaderLatchRegistry != null) {
            if (leaderLatchRegistry.getLeaderLatch(latchKey).hasLeadership()) {
                LOG.info("This intance is a leader, it will run the job");
                job.donRun();
            } else {
                LOG.info("This instance is NOT a leader, linger");
            }
        } else {
            LOG.info("No client-discovery, this instance will run the job");
            job.donRun();
        }
    }

    private static interface Job {
        void donRun();
    }

}
