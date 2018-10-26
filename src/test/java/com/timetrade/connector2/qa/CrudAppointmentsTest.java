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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Title;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by oleksandr.kydiuk on Oct, 2018
 */
@Description("CRUD Appointments Tests")
public class CrudAppointmentsTest extends BaseTest {

    private static final Log logger = LogFactory.getLog(CrudAppointmentsTest.class);
    private Appointment appointment;
    private Calendar calStart;
    private Calendar calEnd;

    @BeforeMethod
    public void setUpInitialAppointment() throws Exception {
        calStart = Calendar.getInstance();
        calStart.set(Calendar.HOUR_OF_DAY, 10);
        calStart.set(Calendar.MINUTE, 0);

        calEnd = Calendar.getInstance();
        calEnd.set(Calendar.HOUR_OF_DAY, 10);
        calEnd.set(Calendar.MINUTE, 29);
        calEnd.set(Calendar.SECOND, 59);

        assertSlotIsFree(11);

        logger.info("Creating appointment with startTime " + calStart.getTime().toString() + "and endTime " + calEnd.getTime().toString());

        TTExchangeService service = new TTExchangeService(userOfAccount);
        appointment = new Appointment(service);
        appointment.setSubject("Dentist Appointment333");
        appointment.setBody(new MessageBody("The appointment is with Dr. Smith."));
        appointment.setStart(calStart.getTime());
        appointment.setStartTimeZone(new OlsonTimeZoneDefinition(TimeZone.getTimeZone("UTC")));
        appointment.setEnd(calEnd.getTime());
        appointment.save(SendInvitationsMode.SendToNone);
    }

    @AfterMethod
    public void tearDown(){
        try {
            appointment.delete(DeleteMode.HardDelete);
        } catch (Exception e) {
            logger.info("Deleting appointment after test error. Message: " + e.getMessage());
        }
    }

    @Title("Create Appointment")
    @Test
    public void testCreateAppointment() throws InterruptedException {
        assertSlotIsBooked(11);
    }

    @Title("Update Appointment")
    @Test
    public void testUpdateAppointment() throws Exception {
        assertSlotIsBooked(11);

        appointment.setLegacyFreeBusyStatus(LegacyFreeBusyStatus.Tentative);
        appointment.update(ConflictResolutionMode.AlwaysOverwrite);

        assertSlotIsTentative(11);

    }

    @Title("Reschedule Appointment")
    @Test
    public void testRescheduleAppointment() throws Exception {
        assertSlotIsBooked(11);

        //shifting appointment by 1 hour
        calStart.add(Calendar.HOUR, 1);
        calEnd.add(Calendar.HOUR, 1);
        appointment.setStart(calStart.getTime());
        appointment.setEnd(calEnd.getTime());
        appointment.update(ConflictResolutionMode.AlwaysOverwrite);

        assertSlotIsBooked(12);

        //shifting appointment backwards
        calStart.add(Calendar.HOUR, -1);
        calEnd.add(Calendar.HOUR, -1);
        appointment.setStart(calStart.getTime());
        appointment.setEnd(calEnd.getTime());
        appointment.update(ConflictResolutionMode.AlwaysOverwrite);

        assertSlotIsBooked(11);
    }

    @Title("Delete Appointment")
    @Test
    public void testDeleteAppointment() throws Exception {
        assertSlotIsBooked(11);

        appointment.delete(DeleteMode.SoftDelete);

        assertSlotIsFree(11);
    }
}