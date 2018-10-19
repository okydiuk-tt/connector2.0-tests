package com.timetrade.connector_2;

import com.timetrade.connector_2.config.Config;
import com.timetrade.ews.notifications.Application;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.model.UserOfAccount;
import io.restassured.http.ContentType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeSuite;
import ru.yandex.qatools.allure.annotations.Step;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;

/**
 * Created by oleksandr.kydiuk on Oct, 2018
 */
@SpringBootTest(classes = Application.class)
public class BaseTest extends AbstractTestNGSpringContextTests {

	private static final Log logger = LogFactory.getLog(BaseTest.class);
	private static final ZoneId UTC = ZoneId.of("UTC");
	private static final String PLAT01_GA = Config.properties.getProperty("ga");
	private static final String PLAT01_EWS = Config.properties.getProperty("ews");

	static UserOfAccount userOfAccount = new UserOfAccount(Config.properties.getProperty("user"), Config.properties.getProperty("account"));

	@Autowired
	ConfigurationResolver configurationResolver;

	@BeforeSuite
	public void subscribeUser() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		logger.info("Subscribing user " + userOfAccount.getUsername() + "to push notifications " +
				"Url:" + PLAT01_EWS + userOfAccount.getAccountId() + "/subscriptions");
		given()
				.contentType(ContentType.JSON)
				.body("[\"" + userOfAccount.getUsername() + "\"]")
				.post(PLAT01_EWS + userOfAccount.getAccountId() + "/subscriptions")
				.then()
				.assertThat().statusCode(202);
	}

	@Step
	void assertSlotIsBooked(int index) {
		logger.info("Querying get-availability service for booked slot. Url: " + PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy");

		given()
				.param("start", bounderyStartTime())
				.param("end", bounderyEndTime())
				.param("granularity", "60")
				.get(PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy")
				.then()
				.assertThat().statusCode(200)
				.and()
				.assertThat().contentType(ContentType.JSON)
				.and()
				.assertThat().body("freeBusyTime", isSlotBooked(index));
	}

	void assertSlotIsTentative(int index) {
		logger.info("Querying get-availability service for tentative slot");

		given()
				.param("start", bounderyStartTime())
				.param("end", bounderyEndTime())
				.param("granularity", "60")
				.get(PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy")
				.then()
				.assertThat().statusCode(200)
				.and()
				.assertThat().contentType(ContentType.JSON)
				.and()
				.assertThat().body("freeBusyTime", isSlotTentative(index));
	}

	void assertSlotIsFree(int index) {
		logger.info("Querying get-availability service if appointment is free. Url: " + PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy");

		given()
				.param("start", bounderyStartTime())
				.param("end", bounderyEndTime())
				.param("granularity", "60")
				.get(PLAT01_GA + userOfAccount.getAccountId() + "/" + userOfAccount.getUsername() + "/calendar/free-busy")
				.then()
				.assertThat().statusCode(200)
				.and()
				.assertThat().contentType(ContentType.JSON)
				.and()
				.assertThat().body("freeBusyTime", not(isSlotBooked(index)));
	}

	private String bounderyStartTime() {
		ZonedDateTime nowBeginning = LocalDate.now().atStartOfDay(UTC).plusNanos(1);
		return nowBeginning.toInstant().toString();
	}

	private String bounderyEndTime() {
		ZonedDateTime nowBeginning = LocalDate.now().plusDays(1).atStartOfDay(UTC).plusNanos(-1);
		return nowBeginning.toInstant().toString();
	}

	private static Matcher<String> isSlotBooked(int index) {
		return new TypeSafeMatcher<String>() {
			@Override
			public void describeTo(final Description description) {
				description.appendText("Slot at index of " + index + " should be booked");
			}

			@Override
			protected boolean matchesSafely(final String input) {
				return input.substring(index, index + 1).contains("2");
			}
		};
	}

	private static Matcher<String> isSlotTentative(int index) {
		return new TypeSafeMatcher<String>() {
			@Override
			public void describeTo(final Description description) {
				description.appendText("Slot at index of " + index + " should be booked");
			}

			@Override
			protected boolean matchesSafely(final String input) {
				return input.substring(index, index + 1).contains("1");
			}
		};
	}
}

