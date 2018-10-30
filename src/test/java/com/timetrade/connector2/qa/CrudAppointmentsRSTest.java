package com.timetrade.connector2.qa;

import com.timetrade.connector2.qa.service.wrappers.TTExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.LegacyFreeBusyStatus;
import microsoft.exchange.webservices.data.core.enumeration.service.ConflictResolutionMode;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.enumeration.service.SendInvitationsMode;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.property.complex.time.OlsonTimeZoneDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Title;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by oleksandr.kydiuk on Oct, 2018
 */
public class CrudAppointmentsRSTest extends BaseTest {

    private static final Log logger = LogFactory.getLog(CrudAppointmentsRSTest.class);
    private Appointment appointment;
    private Calendar calStart;
    private Calendar calEnd;

    @Step
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
        appointment.setSubject("Dentist Appointment to RS 333");
        appointment.setBody(new MessageBody("The appointment is with Dr. Smith."));
        appointment.setStart(calStart.getTime());
        appointment.setStartTimeZone(new OlsonTimeZoneDefinition(TimeZone.getTimeZone("UTC")));
        appointment.setEnd(calEnd.getTime());
        appointment.save(SendInvitationsMode.SendToNone);
    }

    @Step
    @AfterMethod
    public void tearDown() {
        try {
            appointment.delete(DeleteMode.HardDelete);
            assertSlotIsFree(11);
            Thread.sleep(3000);
        } catch (Exception e) {
            logger.info("Deleting appointment after test error. Message: " + e.getMessage());
        }
    }

    @Title("Create Appointment RS Test")
    @Test
    public void testCreateAppointmentRS() throws ServiceLocalException, InterruptedException {
        assertEventCreatedInRS(appointment.getId().getUniqueId(), 1, "CREATE_EVENT", "BUSY", calStart, calEnd);
    }

    @Title("Update Appointment RS Test")
    @Test
    public void testUpdateAppointmentRS() throws Exception {
        assertEventCreatedInRS(appointment.getId().getUniqueId(), 1, "CREATE_EVENT", "BUSY", calStart, calEnd);

        appointment.setLegacyFreeBusyStatus(LegacyFreeBusyStatus.Tentative);
        appointment.update(ConflictResolutionMode.AlwaysOverwrite);

        assertEventCreatedInRS(appointment.getId().getUniqueId(), 2, "UPDATE_EVENT", "TENT", calStart, calEnd);
    }

    @Title("Reschedule Appointment RS Test")
    @Test
    public void testRescheduleAppointmentRS() throws Exception {
        assertEventCreatedInRS(appointment.getId().getUniqueId(), 1, "CREATE_EVENT", "BUSY", calStart, calEnd);

        //shifting appointment by 1 hour
        calStart.add(Calendar.HOUR, 1);
        calEnd.add(Calendar.HOUR, 1);
        appointment.setStart(calStart.getTime());
        appointment.setEnd(calEnd.getTime());
        appointment.update(ConflictResolutionMode.AlwaysOverwrite);

        assertEventCreatedInRS(appointment.getId().getUniqueId(), 2, "MOVE_EVENT", "BUSY", calStart, calEnd);
    }

    @Title("Delete Appointment RS Test")
    @Test
    public void testDeleteAppointmentRS() throws Exception {
        assertEventCreatedInRS(appointment.getId().getUniqueId(), 1, "CREATE_EVENT", "BUSY", calStart, calEnd);

        appointment.delete(DeleteMode.SoftDelete);

        assertEventCreatedInRS(appointment.getId().getUniqueId(), 2, "DELETE_EVENT", "BUSY", calStart, calEnd);
    }
}
