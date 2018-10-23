package com.timetrade.connector2.qa;

import com.timetrade.connector2.qa.service.wrappers.TTExchangeService;
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
public class CrudAppointmentsTest extends BaseTest {

    private static final Log logger = LogFactory.getLog(CrudAppointmentsTest.class);

    @Test
    public void testCreateAppointment() throws Exception {

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 10);
        calStart.set(Calendar.MINUTE, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 10);
        calEnd.set(Calendar.MINUTE, 30);

        assertSlotIsFree(11);

        logger.info("Creating appointment with startTime " + calStart.getTime().toString() + "and endTime " + calEnd.getTime().toString());
        TTExchangeService service = new TTExchangeService(userOfAccount, mockCR);
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

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 10);
        calStart.set(Calendar.MINUTE, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 10);
        calEnd.set(Calendar.MINUTE, 30);

        assertSlotIsFree(11);

        logger.info("Creating appointment with startTime " + calStart.getTime().toString() + "and endTime " + calEnd.getTime().toString());
        TTExchangeService service = new TTExchangeService(userOfAccount, mockCR);
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

    @Test
    public void testRescheduleAppointment() throws Exception {

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 10);
        calStart.set(Calendar.MINUTE, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 10);
        calEnd.set(Calendar.MINUTE, 30);

        assertSlotIsFree(11);

        logger.info("Creating appointment with startTime " + calStart.getTime().toString() + "and endTime " + calEnd.getTime().toString());
        TTExchangeService service = new TTExchangeService(userOfAccount, mockCR);
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

            //shifting appointment by 1 hour
            calStart.add(Calendar.HOUR, 1);
            calEnd.add(Calendar.HOUR, 1);
            appointment.setStart(calStart.getTime());
            appointment.setEnd(calEnd.getTime());
            appointment.update(ConflictResolutionMode.AlwaysOverwrite);

            //waiting for processing appointment
            Thread.sleep(5000);

            assertSlotIsBooked(12);
        }
        finally {
            appointment.delete(DeleteMode.HardDelete);
            Thread.sleep(5000);
        }
    }

    @Test
    public void testDeleteAppointment() throws Exception {

        Calendar calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 10);
        calStart.set(Calendar.MINUTE, 0);

        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 10);
        calEnd.set(Calendar.MINUTE, 30);

        assertSlotIsFree(11);

        logger.info("Creating appointment with startTime " + calStart.getTime().toString() + "and endTime " + calEnd.getTime().toString());
        TTExchangeService service = new TTExchangeService(userOfAccount, mockCR);
        Appointment appointment = new Appointment(service);
        try{
            appointment.setSubject("Dentist Appointment33");
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
            assertSlotIsFree(11);
        }
    }
}
