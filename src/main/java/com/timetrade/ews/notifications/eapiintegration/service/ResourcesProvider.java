package com.timetrade.ews.notifications.eapiintegration.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.service.SubscriptionService;
import com.timetrade.ews.notifications.service.SyncService;
import com.timetrade.platform.common.apicreds.SoapApiCredentialsContext;
import com.timetrade.platform.common.apicreds.SoapApiUserInfo;
import com.timetrade.platform.common.apicreds.security.ApiCredentials;
import com.timetrade.platform.eapi.client.ConfigurationServicesClient;
import com.timetrade.platform.eapi.client.model.configurationservices.Location;
import com.timetrade.platform.eapi.client.model.configurationservices.LocationSearchCriteria;
import com.timetrade.platform.eapi.client.model.configurationservices.Resource;
import com.timetrade.platform.eapi.client.model.configurationservices.ResourceSearchCriteria;

public class ResourcesProvider {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesProvider.class);
    private static final AtomicBoolean IN_PROGRESS = new AtomicBoolean(false);

    private ConfigurationServicesClient configurationServicesClient;

    private SyncService syncService;

    private ConfigurationResolver configurationResolver;

    private SubscriptionService subscriptionService;

    public ResourcesProvider(ConfigurationServicesClient configurationServicesClient, SyncService syncService,
            SubscriptionService subscriptionService, ConfigurationResolver configurationResolver) {
        this.configurationServicesClient = configurationServicesClient;
        this.syncService = syncService;
        this.subscriptionService = subscriptionService;
        this.configurationResolver = configurationResolver;
    }

    public void syncResources() {
        logger.info("Syncing all configured accounts' resources");

        if (IN_PROGRESS.compareAndSet(false, true)) {
            try {
                Set<String> accounts = configurationResolver.resolveConfiguredAccounts();
                logger.info("Got {} configured accounts", accounts.size());

                int i = 0;
                for (String accountId : accounts) {
                    processAccount(accountId);
                    logger.info("Done syncing {}/{}", ++i, accounts.size());
                }
            } finally {
                IN_PROGRESS.set(false);
            }
        } else {
            logger.info("Another syncing task is in progress");
        }

        logger.info("Syncing resources is complete");
    }

    private void processAccount(String accountId) {

        logger.info("Processing sync for {}", accountId);

        Set<UserOfAccount> resourcesInEApi = new HashSet<>();
        try {
            resourcesInEApi.addAll(getResourcesFromAllLocations(accountId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return;
        }

        Set<UserOfAccount> resourcesInStorage = new HashSet<>();
        try {
            resourcesInStorage.addAll(subscriptionService.getSubscribedEmails(accountId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return;
        }

        Set<UserOfAccount> commonResources = new HashSet<>(resourcesInEApi);
        commonResources.retainAll(resourcesInStorage);

        //remaining elements to be deleted
        resourcesInStorage.removeAll(commonResources);
        logger.info("{} has {} accounts to delete", accountId, resourcesInStorage.size());

        //remaining elements to be subscribed
        resourcesInEApi.removeAll(commonResources);
        logger.info("{} has {} accounts to subscribe", accountId, resourcesInEApi.size());

        if (!resourcesInEApi.isEmpty()) {
            subscriptionService.subscribeAll(resourcesInEApi, false,
                    u -> syncService.getInitialAvailabilityAndSend(u, configurationResolver.resolveInitialAvailabilityTimeRange()));
        } else {
            logger.info("No new {} accounts to subscribe", accountId);
        }

        if (!resourcesInStorage.isEmpty()) {
            subscriptionService.markToUnsubscribe(resourcesInStorage);
        } else {
            logger.info("No {} accounts to unsubscribe", accountId);
        }

    }

    public Set<UserOfAccount> getResourcesFromAllLocations(String accountId) {

        ApiCredentials credentials = configurationResolver.resolveApiCredentials(accountId);

        Set<UserOfAccount> resourcesInEApi = new HashSet<>();
        try {
            SoapApiCredentialsContext.setCurrentAccountApiCredentials(
                    new SoapApiUserInfo(credentials.getAccessUser(), credentials.getAccessPassword(), null));
            Set<String> locations = getLocationsFromEApi();
            logger.info("Got {} locations for {}", locations.size(), accountId);
            int i = 0;
            for (String location : locations) {
                resourcesInEApi.addAll(getResourcesFromEApi(location, accountId));
                logger.info("Done {}/{} locations for {}", ++i, locations.size(), accountId);
            }
        } catch (Exception e) {
            logger.error("Failed to get resources from " + accountId, e);
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            SoapApiCredentialsContext.setCurrentAccountApiCredentials(null);
        }

        return resourcesInEApi;
    }

    private Set<String> getLocationsFromEApi() {

        logger.info("Getting locations from E-Api");

        List<Location> locations = configurationServicesClient.getLocationsByCriteria(new LocationSearchCriteria());
        logger.info("Got {} locations for E-Api", locations.size());

        return locations.stream()
                .filter(l -> l.getLocationExternalId() != null)
                .map(Location::getLocationExternalId)
                .collect(Collectors.toSet());
    }

    private Set<UserOfAccount> getResourcesFromEApi(String locationExternalId, String accountId) {

        logger.info("Getting {} resources from E-Api", locationExternalId);

        ResourceSearchCriteria criteria = new ResourceSearchCriteria();
        criteria.setCampaignExternalId("-1");
        criteria.setLocationExternalId(locationExternalId);

        List<Resource> resources = configurationServicesClient.getResourcesByCriteria(criteria);
        logger.info("Got {} resources for {}", resources.size(), locationExternalId);

        return resources.stream()
                .filter(r -> r.getResourceExternalId() != null)
                .map(Resource::getResourceExternalId)
                .map(String::toLowerCase)
                .map(r -> new UserOfAccount(r, accountId))
                .collect(Collectors.toSet());
    }

}
