package com.timetrade.ews.notifications.service.wrappers;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.timetrade.ews.notifications.configuration.EwsProperties.AccessType;
import com.timetrade.ews.notifications.configuration.EwsProperties.ExchangeCredentials;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfig.Endpoint;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.model.UserOfAccount;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ConnectingIdType;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.ImpersonatedUserId;
import microsoft.exchange.webservices.data.notification.PushSubscription;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.Mailbox;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;

/**
 * Created by oleksandr.romakh on 5/24/2017.
 */
public class TTExchangeService extends ExchangeService {
    private static final Logger logger = LoggerFactory.getLogger(TTExchangeService.class);

    private static final List<FolderId> FOLDERS_TO_SUB =
            Arrays.asList(new FolderId(WellKnownFolderName.Calendar), new FolderId(WellKnownFolderName.DeletedItems));

    private UserOfAccount userOfAccount;
    private ExchangeCredentials exchangeCredentials;
    private ConfigurationResolver configurationResolver;

    public TTExchangeService(UserOfAccount userOfAccount, ConfigurationResolver configurationResolver) {
        super(configurationResolver.resolveExchangeVersion());

        this.configurationResolver = configurationResolver;
        this.userOfAccount = userOfAccount;
        this.exchangeCredentials = configurationResolver.resolveMasterUserPasswordAccess(userOfAccount.getAccountId());

        Endpoint endpoint = configurationResolver.resolveExchangeEndpoint(userOfAccount.getAccountId());
        if (endpoint.getAutodiscover()) {
            try {
                this.autodiscoverUrl(userOfAccount.getUsername());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        } else {
            this.setUrl(URI.create(endpoint.getUrl()));
        }

        setCredentials(userOfAccount, this, exchangeCredentials);
    }

    private static ExchangeCredentials setCredentials(UserOfAccount userOfAccount, ExchangeService exchangeService,
            ExchangeCredentials masterUserPasswordAccess) {

        exchangeService.setCredentials(
                new WebCredentials(masterUserPasswordAccess.getExchangeUsername(), masterUserPasswordAccess.getExchangePassword()));

        if (AccessType.IMPERSONATION == masterUserPasswordAccess.getAccessType()) {
            logger.debug("Impersonation is active: {} will act on behalf of {}", masterUserPasswordAccess.getExchangeUsername(),
                    userOfAccount.getUsername());
            exchangeService.setImpersonatedUserId(new ImpersonatedUserId(ConnectingIdType.SmtpAddress, userOfAccount.getUsername()));
        } else {
            logger.debug("Delegation is active: {} will act on behalf of {}", masterUserPasswordAccess.getExchangeUsername(),
                    userOfAccount.getUsername());
        }

        return masterUserPasswordAccess;

    }

    @Override
    public PushSubscription subscribeToPushNotifications(Iterable<FolderId> folderIds, URI url, int frequency,
            String waterMark, EventType... eventTypes) throws Exception {

        Iterable<FolderId> resultFolderIds;
        if (exchangeCredentials.getAccessType() == AccessType.DELEGATION) {
            List<FolderId> folders = new ArrayList<>();
            for (FolderId folderId : folderIds) {
                folders.add(new FolderId(folderId.getFolderName(), new Mailbox(userOfAccount.getUsername())));
            }
            resultFolderIds = folders;
        } else {
            resultFolderIds = folderIds;
        }

        try {
            return super.subscribeToPushNotifications(resultFolderIds, url, frequency, waterMark, eventTypes);
        } catch (Exception e) {
            if (e.getMessage().contains("401") && exchangeCredentials.getAccessType() == AccessType.IMPERSONATION) {
                logger.error("ExchangeServerClient is badly configured or user doesn't have permission to impersonate accounts");
            } else if ("The watermark used for creating this subscription was not found.".equals(e.getMessage())) {
                logger.warn("Detected expired subscription waterMark for account: {}, waterMark: {}, doing resub",
                        userOfAccount, waterMark);
                return super.subscribeToPushNotifications(resultFolderIds, url, frequency, null, eventTypes);
            } else {
                logger.error(e.getMessage(), e);
                logger.error("Failed to subscribe: {}", userOfAccount);
            }
            throw e;
        }

    }

    @Override
    public FindItemsResults<Appointment> findAppointments(WellKnownFolderName parentFolderName,
            CalendarView calendarView) throws Exception {
        FindItemsResults<Appointment> result;
        if (exchangeCredentials.getAccessType() == AccessType.IMPERSONATION) {
            result = this.findAppointments(new FolderId(parentFolderName), calendarView);
        } else {
            result = this.findAppointments(
                    new FolderId(parentFolderName, new Mailbox(userOfAccount.getUsername())), calendarView);
        }

        return result;
    }
//
//    public PushSubscriptionMeta subscribeToFreeBusyPushNotifications(String watermark)
//            throws Exception {
//
//        String callbackEndpoint = configurationResolver.resolveSubscriptionsProperties().getCallbackEndpoint();
//        if (callbackEndpoint == null) {
//            logger.error("No endpoint specified for callback for account: " + userOfAccount.getAccountId());
//            return null;
//        }
//
//        URI listenerUrl = URI.create(callbackEndpoint
//                                         .replace("{username}", userOfAccount.getUsername())
//                                         .replace("{accountId}", userOfAccount.getAccountId()));
//        int heartBeatInMins = configurationResolver.resolveSubscriptionsProperties().getHeartBeat() / 60;
//        logger.info("Listener-Url: {}, Heart-Beat: {} min", listenerUrl, heartBeatInMins);
//
//        PushSubscriptionMeta meta;
//        if (!configurationResolver.resolveSubscriptionsProperties().isDryRun()) {
//            meta = new PushSubscriptionMeta(subscribeToPushNotifications(FOLDERS_TO_SUB,
//                    listenerUrl, heartBeatInMins, watermark, EventType.FreeBusyChanged, EventType.Moved));
//        } else {
//            logger.info("Dry-run is ON");
//            meta = new PushSubscriptionMeta(java.util.UUID.randomUUID().toString(),
//                    String.valueOf(System.currentTimeMillis()));
//        }
//
//        return meta;
//    }

    public UserOfAccount getUserOfAccount() {
        return userOfAccount;
    }

}
