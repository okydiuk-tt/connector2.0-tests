package com.ms.ews;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import microsoft.exchange.webservices.data.core.EwsServiceXmlReader;
import microsoft.exchange.webservices.data.core.XmlElementNames;
import microsoft.exchange.webservices.data.core.enumeration.misc.XmlNamespace;
import microsoft.exchange.webservices.data.core.response.GetEventsResponse;
import microsoft.exchange.webservices.data.notification.GetEventsResults;

public class SendNotificationReader {

	private static final Logger LOG = LoggerFactory.getLogger(SendNotificationReader.class);

	private static final String SEND_NOTIFICATION_TAG = "SendNotification";
	private static final String SEND_NOTIFICATION_RESPONSE_MEESAGE_TAG = "SendNotificationResponseMessage";
	
	public GetEventsResults loadFromXml(EwsServiceXmlReader reader) throws Exception {
		reader.read();
		reader.readStartElement(XmlNamespace.Messages, SEND_NOTIFICATION_TAG);

		reader.readToDescendant(XmlNamespace.Messages, XmlElementNames.ResponseMessages);
		LOG.info("Reading till find ResponseMessages: " + reader.getLocalName());
		
		do {
			reader.read();

			if (reader.isStartElement()) {
				String eventElementName = reader.getLocalName();
				LOG.debug("Read xml node: " + eventElementName);

				if (SEND_NOTIFICATION_RESPONSE_MEESAGE_TAG.equals(reader.getLocalName())) {
					GetEventsResponse getEventsResponse = new GetEventsResponse();
					getEventsResponse.loadFromXml(reader, SEND_NOTIFICATION_RESPONSE_MEESAGE_TAG);
					
					LOG.info("SendNotificationResponseMessage loadedFromXml: {}", getEventsResponse);

					return getEventsResponse.getResults();
				}

				reader.skipCurrentElement();
			}
		} while (!reader.isEndElement(XmlNamespace.Messages, SEND_NOTIFICATION_TAG));
		
		return null;
	}

}
