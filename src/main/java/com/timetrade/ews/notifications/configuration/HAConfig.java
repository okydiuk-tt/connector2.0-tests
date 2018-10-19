package com.timetrade.ews.notifications.configuration;

import java.util.EnumMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("discovery")
@Configuration
@EnableDiscoveryClient
@EnableConfigurationProperties(EwsProperties.class)
public class HAConfig {

    @Bean(initMethod = "start", destroyMethod = "close")
    public LeaderLatch maintenanceLeaderLatch(CuratorFramework curator,
            @Value("${latchPath:/${spring.application.name}/maintenance}") String latchPath) {
        return new LeaderLatch(curator, latchPath);
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public LeaderLatch resourcesSyncLeaderLatch(CuratorFramework curator,
            @Value("${latchPath:/${spring.application.name}/resourcesSync}") String latchPath) {
        return new LeaderLatch(curator, latchPath);
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public LeaderLatch reattemptsLeaderLatch(CuratorFramework curator,
            @Value("${latchPath:/${spring.application.name}/reattemptsLeaderLatch}") String latchPath) {
        return new LeaderLatch(curator, latchPath);
    }

    @Bean
    public LeaderLatchRegistry leaderLatchRegistry(LeaderLatch maintenanceLeaderLatch,
            LeaderLatch resourcesSyncLeaderLatch, LeaderLatch reattemptsLeaderLatch) {
        return new LeaderLatchRegistry(maintenanceLeaderLatch, resourcesSyncLeaderLatch, reattemptsLeaderLatch);
    }

    public static class LeaderLatchRegistry {
        public enum LatchKey {
            MAINTENANCE, RESOURCES_SYNC, REATTEMPTS
        }

        private EnumMap<LatchKey, LeaderLatch> registry = new EnumMap<>(LatchKey.class);

        public LeaderLatchRegistry(LeaderLatch maintenanceLeaderLatch,
                LeaderLatch resourcesSyncLeaderLatch, LeaderLatch reattemptsLeaderLatch) {
            registry.put(LatchKey.MAINTENANCE, maintenanceLeaderLatch);
            registry.put(LatchKey.RESOURCES_SYNC, resourcesSyncLeaderLatch);
            registry.put(LatchKey.REATTEMPTS, reattemptsLeaderLatch);
        }

        public LeaderLatch getLeaderLatch(LatchKey latchKey) {
            return registry.get(latchKey);
        }

    }

}
