package com.timetrade.ews.notifications.service;

import com.google.common.base.Strings;
import com.ms.ews.*;
import com.timetrade.ews.notifications.model.ParsedNotification;
import com.timetrade.ews.notifications.model.ParsedNotification.FolderId;
import com.timetrade.ews.notifications.model.ParsedNotification.ItemInFolder;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.service.wrappers.TTExchangeService;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceResponseException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ParsingService {

    private static final Logger LOG = LoggerFactory.getLogger(ParsingService.class);

    private JAXBContext jaxbContext;
    private ExchangeServiceFactory exchangeServiceFactory;

    public ParsingService(ExchangeServiceFactory exchangeServiceFactory) throws JAXBException {
        this.jaxbContext = JAXBContext.newInstance(SendNotificationResponseType.class);
        this.exchangeServiceFactory = exchangeServiceFactory;
    }

    public ParsedNotification parseResponse(String xml, UserOfAccount username) {

        LinkedList<ParsedNotification> parsedNotifications = new LinkedList<>();

        try (InputStream is = new ByteArrayInputStream(extractSoapBody(xml).getBytes())) {

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            // TODO to remove this trash
            SendNotificationResponseType notif = jaxbUnmarshaller.unmarshal(new StreamSource(is),
                    SendNotificationResponseType.class).getValue();

            for (JAXBElement<? extends ResponseMessageType> el : notif.getResponseMessages()
                    .getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage()) {
                ResponseMessageType responseMessage = el.getValue();
                if (!(responseMessage instanceof SendNotificationResponseMessageType)) {
                    continue;
                }
                NotificationType notifPl = ((SendNotificationResponseMessageType) responseMessage).getNotification();
                parsedNotifications.add(processSendNotification(notifPl, username));
            }
        } catch (JAXBException | IOException e) {
            LOG.error(e.getMessage(), e);
            throw new ParserException(e);
        }

        if (parsedNotifications.size() != 1) {
            String error;
            if (parsedNotifications.isEmpty()) {
                error = "Didn't manage to extract subscription information";
            } else {
                error = "Only one notification is expected";
            }
            LOG.error(error);
            throw new IllegalArgumentException(error);
        }

        return parsedNotifications.getLast();
    }

    private ParsedNotification processSendNotification(NotificationType notificationPayload, UserOfAccount username) {
        Set<ItemInFolder> itemsInFolder = new HashSet<>();

        String waterMark = null;

        String subscriptionId = notificationPayload.getSubscriptionId();
        String eventId = null;
        for (JAXBElement<? extends BaseNotificationEventType> evWr : notificationPayload
                .getCopiedEventOrCreatedEventOrDeletedEvent()) {
            BaseNotificationEventType notifEvent = evWr.getValue();

            waterMark = notifEvent.getWatermark();
            if (!(notifEvent instanceof BaseObjectChangedEventType)) {
                // TODO to double-check if only status event are not of BaseObjectChangedEventType type
                //Status Event
                return ParsedNotification.statusEvent(subscriptionId, waterMark);
            }
            if(evWr.getValue() instanceof MovedCopiedEventType
                    && (evWr.getValue()) != null
                    && ((MovedCopiedEventType) evWr.getValue()).getOldItemId() != null) {

                LOG.info("Moved event detected");
                FolderIdType oldFolderId = ((MovedCopiedEventType) evWr.getValue()).getOldParentFolderId();
                String previousEventId = ((MovedCopiedEventType) evWr.getValue()).getOldItemId().getId();
                try (TTExchangeService service = exchangeServiceFactory.createExchangeService(username)) {
                    microsoft.exchange.webservices.data.property.complex.FolderId folderIdFromCalendar =
                            new microsoft.exchange.webservices.data.property.complex.FolderId(oldFolderId.getId());
                    try {
                        if (Folder.bind(service, folderIdFromCalendar).getXmlElementName().equals("CalendarFolder")) {
                            eventId = previousEventId;
                            LOG.info("New event id: {}", eventId);
                        }
                    } catch (ServiceResponseException ex) {
                        LOG.info("The specified calendar was not shared with you. \n" + ex);
                    }
                } catch (Exception e) {
                    LOG.info("Moved nof free busy event", e);
                }
                continue;
            }
            BaseObjectChangedEventType evDet = (BaseObjectChangedEventType) evWr.getValue();
            if (evDet.getItemId() == null) {
                // event on folders only: shouldn't happen
                continue;
            }
            //event on item
            FolderId folderId = new FolderId(evDet.getParentFolderId().getId(), evDet.getParentFolderId().getChangeKey());
            itemsInFolder.add(new ItemInFolder(Strings.isNullOrEmpty(eventId) ? evDet.getItemId().getId() : eventId, folderId));
        }

        return new ParsedNotification(subscriptionId, waterMark, itemsInFolder);
    }

    private static String extractSoapBody(String envelope) {
        final String startTag = "<soap11:Body>";
        final String closeTag = "</soap11:Body>";

        return envelope.substring(envelope.indexOf(startTag) + startTag.length(), envelope.indexOf(closeTag));
    }

}
