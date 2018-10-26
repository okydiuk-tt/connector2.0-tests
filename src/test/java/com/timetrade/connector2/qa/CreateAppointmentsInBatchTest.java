package com.timetrade.connector2.qa;

import com.timetrade.connector2.qa.service.wrappers.TTExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.enumeration.service.MessageDisposition;
import microsoft.exchange.webservices.data.core.enumeration.service.SendCancellationsMode;
import microsoft.exchange.webservices.data.core.enumeration.service.SendInvitationsMode;
import microsoft.exchange.webservices.data.core.enumeration.service.calendar.AffectedTaskOccurrence;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.time.OlsonTimeZoneDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by oleksandr.kydiuk on Oct, 2018
 */
@Ignore
public class CreateAppointmentsInBatchTest extends BaseTest {

    private static final Log logger = LogFactory.getLog(CreateAppointmentsInBatchTest.class);

    @Test
    public void testCreateAppointmentsInBatch() throws Exception {
        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 10);
        calStart.set(Calendar.MINUTE, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 10);
        calEnd.set(Calendar.MINUTE, 30);

        assertSlotIsFree(11);

        logger.info("Creating appointment with startTime " + calStart.getTime().toString() + "and endTime " + calEnd.getTime().toString());
        TTExchangeService service = new TTExchangeService(userOfAccount);

        List<Item> appList = new ArrayList<>();
        List<ItemId> itemList = new ArrayList<>();
        try {
            for (int i = 0; i < 10; i++) {
                Appointment appointment = new Appointment(service);
                appointment.setSubject("Dentist Appointment333");
                appointment.setBody(new MessageBody("The appointment is with Dr. Smith."));
                appointment.setStart(calStart.getTime());
                appointment.setStartTimeZone(new OlsonTimeZoneDefinition(TimeZone.getTimeZone("UTC")));
                appointment.setEnd(calEnd.getTime());
                appList.add(appointment);

                calStart.add(Calendar.MINUTE, 30);
                calEnd.add(Calendar.MINUTE, 30);
            }

            service.createItems(appList, FolderId.getFolderIdFromWellKnownFolderName(WellKnownFolderName.Calendar), MessageDisposition.SendAndSaveCopy, SendInvitationsMode.SendToNone);

            for (Item anAppList : appList) {
                itemList.add(anAppList.getId());
            }

            assertSlotsAreBooked(11, 21);

        } finally {
            service.deleteItems(itemList, DeleteMode.HardDelete, SendCancellationsMode.SendToNone, AffectedTaskOccurrence.SpecifiedOccurrenceOnly);
        }
        System.out.println("*");
    }
}


