package com.timetrade.ews.notifications.configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.timetrade.ews.notifications.configuration.HAConfig.LeaderLatchRegistry;
import com.timetrade.ews.notifications.eapiintegration.service.ResourcesProvider;
import com.timetrade.ews.notifications.scheduled.PeriodicTasks;
import com.timetrade.ews.notifications.service.MaintenanceService;
import com.timetrade.ews.notifications.service.SubscriptionService;

@EnableScheduling
@Configuration
public class PeriodicTasksConfig {

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(3);
    }

    @Bean
    public PeriodicTasks scheduledTasks(MaintenanceService maintenanceService, ResourcesProvider resourcesProvider, 
            SubscriptionService subscriptionService, ObjectProvider<LeaderLatchRegistry> leaderLatchRegistry) {
        return new PeriodicTasks(maintenanceService, resourcesProvider, subscriptionService, 
                leaderLatchRegistry.getIfAvailable());
    }

}
