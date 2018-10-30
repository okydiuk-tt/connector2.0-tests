package com.timetrade.connector2.qa;

import com.timetrade.connector2.qa.config.Config;
import com.timetrade.connector2.qa.model.UserOfAccount;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import ru.yandex.qatools.allure.annotations.Step;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

/**
 * Created by oleksandr.kydiuk on Oct, 2018
 */
public class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final String PLAT01_GA = Config.properties.getProperty("ga");
    private static final String PLAT01_EWS = Config.properties.getProperty("ews");
    static final String PLAT01_RS = Config.properties.getProperty("rs");

    private static final String testUsername = Config.properties.getProperty("user");
    private static final String testAccountId = Config.properties.getProperty("account");

    UserOfAccount userOfAccount = new UserOfAccount(testUsername, testAccountId);

    @Step
    @BeforeSuite
    public void subscribeUser() throws InterruptedException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        logger.info("Subscribing user " + userOfAccount.getUsername() + " to push notifications " +
                "Url:" + PLAT01_EWS + userOfAccount.getAccountId() + "/subscriptions");
        given()
                .contentType(ContentType.JSON)
                .body("[\"" + userOfAccount.getUsername() + "\"]")
                .post(PLAT01_EWS + userOfAccount.getAccountId() + "/subscriptions")
                .then()
                .assertThat().statusCode(202);
        Thread.sleep(3000);
    }

    @Step
    void assertSlotIsBooked(int index) throws InterruptedException {
        logger.info("Querying get-availability service for booked slot. Url: " + PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy");
        String response = "";
        boolean result = false;
        for (int i = 0; i < 10; i++) {
            response = given()
                    .param("start", bounderyStartTime())
                    .param("end", bounderyEndTime())
                    .param("granularity", "60")
                    .get(PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy")
                    .then()
                    .assertThat().statusCode(200)
                    .and()
                    .assertThat().contentType(ContentType.JSON)
                    .extract().path("freeBusyTime");
            result = response.charAt(index) == '2';
            if (result) {
                break;
            }
            Thread.sleep(1000);
        }
        assertTrue(result, "Slot by index of " + index + " should be booked. The status of slot is - " + response.charAt(index) + " The response is - " + response);
    }

    @Step
    void assertSlotIsTentative(int index) throws InterruptedException {
        logger.info("Querying get-availability service for tentative slot. Url: " + PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy");
        String response = "";
        boolean result = false;
        for (int i = 0; i < 10; i++) {
            response = given()
                    .param("start", bounderyStartTime())
                    .param("end", bounderyEndTime())
                    .param("granularity", "60")
                    .get(PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy")
                    .then()
                    .assertThat().statusCode(200)
                    .and()
                    .assertThat().contentType(ContentType.JSON)
                    .extract().path("freeBusyTime");
            result = response.charAt(index) == '1';
            if (result) {
                break;
            }
            Thread.sleep(1000);
        }
        assertTrue(result, "Slot by index of " + index + " should be tentative. The status of slot is - " + response.charAt(index) + " The response is - " + response);
    }

    @Step
    void assertSlotIsFree(int index) throws InterruptedException {
        logger.info("Querying get-availability service for free slot. Url: " + PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy");
        String response = "";
        boolean result = false;
        for (int i = 0; i < 10; i++) {
            response = given()
                    .param("start", bounderyStartTime())
                    .param("end", bounderyEndTime())
                    .param("granularity", "60")
                    .get(PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy")
                    .then()
                    .assertThat().statusCode(200)
                    .and()
                    .assertThat().contentType(ContentType.JSON)
                    .extract().path("freeBusyTime");
            result = response.charAt(index) == '0';
            if (result) {
                break;
            }
            Thread.sleep(1000);
        }
        assertTrue(result, "Slot by index of " + index + " should be free. The status of slot is - " + response.charAt(index) + " The response is - " + response);
    }

    @Step
    void assertSlotsAreBooked(int start, int end) throws InterruptedException {
        logger.info("Querying get-availability service for booked slots. Url: " + PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy");
        String response = "";
        boolean result = false;
        for (int i = 0; i < 10; i++) {
            response = given()
                    .param("start", bounderyStartTime())
                    .param("end", bounderyEndTime())
                    .param("granularity", "60")
                    .get(PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy")
                    .then()
                    .assertThat().statusCode(200)
                    .and()
                    .assertThat().contentType(ContentType.JSON)
                    .extract().path("freeBusyTime");
            result = response.substring(start, end).equals("2222222222");
            if (result) {
                break;
            }
            Thread.sleep(1000);
        }
        assertTrue(result, "Period from index " + start + " till " + end + " should be booked. The status of slot is - " + response.substring(start, end) + " The response is - " + response);
    }

    @Step
    void assertEventCreatedInRS(Appointment appointment, int index, String eventType, String status, Calendar calStart, Calendar calEnd) throws InterruptedException, ServiceLocalException {
        logger.info("Querying RS service for created slot. Url: " + PLAT01_RS + userOfAccount.getAccountId() + "/getFullEventDetails?id=" + appointment.getId().getUniqueId());

        List list = null;
        for (int i = 0; i < 10; i++) {
            Response response = given()
                    .param("id", appointment.getId().getUniqueId())
                    .get(PLAT01_RS + userOfAccount.getAccountId() + "/getFullEventDetails")
                    .then()
                    .assertThat().statusCode(200)
                    .and()
                    .assertThat().contentType(ContentType.JSON)
                    .extract().response();
            list = response.path("");
            if (list.size() == index) break;
            Thread.sleep(1000);
        }

        String timeStart = calStart.getTime().toInstant().toString();
        String timeEnd = calEnd.getTime().toInstant().toString();

        assertThat("There response length should be - " + index + ", but it's = " + list.size(), list.size(), is(equalTo(index)));
        assertThat("The event has wrong eventType", ((HashMap) list.get(index - 1)).get("eventType"), is(equalTo(eventType)));
        assertThat("The event has wrong status", ((HashMap) list.get(index - 1)).get("status"), is(equalTo(status)));
        assertThat("The event has wrong username", ((HashMap) list.get(index - 1)).get("resource"), is(equalTo(userOfAccount.getUsername())));
        assertThat("The event has wrong accountId", ((HashMap) list.get(index - 1)).get("licensee"), is(equalTo(userOfAccount.getAccountId())));
        assertThat("The event has wrong startTime", ((HashMap) list.get(index - 1)).get("startTime"), is(equalTo(timeStart.substring(0, timeStart.length() - 5))));
        assertThat("The event has wrong endTime", ((HashMap) list.get(index - 1)).get("endTime"), is(equalTo(timeEnd.substring(0, timeEnd.length() - 5))));
    }

    String bounderyStartTime() {
        ZonedDateTime nowBeginning = LocalDate.now().atStartOfDay(UTC).plusNanos(1);
        return nowBeginning.toInstant().toString();
    }

    String bounderyEndTime() {
        ZonedDateTime nowBeginning = LocalDate.now().plusDays(1).atStartOfDay(UTC).plusNanos(-1);
        return nowBeginning.toInstant().toString();
    }
}

