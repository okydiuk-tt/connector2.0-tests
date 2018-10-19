package com.timetrade.connector_2;

import com.timetrade.ews.notifications.service.wrappers.TTExchangeService;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.LegacyFreeBusyStatus;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.enumeration.service.SendInvitationsMode;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.time.OlsonTimeZoneDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by oleksandr.kydiuk on Oct, 2018
 */
public class CrudAppointmentsTest extends BaseTest{

    private static final Log logger = LogFactory.getLog(CrudAppointmentsTest.class);

    @Test
    public void testCreateAppointment() throws Exception {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 10);
        calStart.set(Calendar.MINUTE, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 10);
        calEnd.set(Calendar.MINUTE, 30);

        assertSlotIsFree(11);

        logger.info("Creating appointment with startTime " + calStart.getTime().toString() + "and endTime " + calEnd.getTime().toString());

        ExchangeService service = new TTExchangeService(userOfAccount, configurationResolver);
        Appointment appointment = new Appointment(service);
        try{
            appointment.setSubject("Dentist Appointment333");
            appointment.setBody(new MessageBody("The appointment is with Dr. Smith."));
            appointment.setStart(calStart.getTime());
            appointment.setStartTimeZone(new OlsonTimeZoneDefinition(TimeZone.getTimeZone("UTC")));
            appointment.setEnd(calEnd.getTime());
            appointment.save(SendInvitationsMode.SendToNone);
            //waiting for processing appointment
            Thread.sleep(5000);

            assertSlotIsBooked(11);
        }
        finally {
            appointment.delete(DeleteMode.HardDelete);
            Thread.sleep(5000);
        }
    }

    @Test
    public void testUpdateAppointment() throws Exception {

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 10);
        calStart.set(Calendar.MINUTE, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 10);
        calEnd.set(Calendar.MINUTE, 30);

        assertSlotIsFree(11);

        logger.info("Creating appointment with startTime " + calStart.getTime().toString() + "and endTime " + calEnd.getTime().toString());

        ExchangeService service = new TTExchangeService(userOfAccount, configurationResolver);
        Appointment appointment = new Appointment(service);
        try{
            appointment.setSubject("Dentist Appointment333");
            appointment.setBody(new MessageBody("The appointment is with Dr. Smith."));
            appointment.setStart(calStart.getTime());
            appointment.setStartTimeZone(new OlsonTimeZoneDefinition(TimeZone.getTimeZone("UTC")));
            appointment.setEnd(calEnd.getTime());
            appointment.save(SendInvitationsMode.SendToNone);
            //waiting for processing appointment
            Thread.sleep(5000);

            assertSlotIsBooked(11);

            appointment.setLegacyFreeBusyStatus(LegacyFreeBusyStatus.Tentative);
            appointment.update(ConflictResolutionMode.AlwaysOverwrite);
            //waiting for processing appointment
            Thread.sleep(5000);

            assertSlotIsTentative(11);
        }
        finally {
            appointment.delete(DeleteMode.HardDelete);
            Thread.sleep(5000);
        }
    }
}
