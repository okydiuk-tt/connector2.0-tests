package com.timetrade.ews.notifications;

import com.timetrade.ews.notifications.config.Config;
import com.timetrade.ews.notifications.configuration.EwsProperties;
import com.timetrade.ews.notifications.configuration.EwsProperties.ExchangeCredentials;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfig.Endpoint;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.model.UserOfAccount;
import io.restassured.http.ContentType;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import ru.yandex.qatools.allure.annotations.Step;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by oleksandr.kydiuk on Oct, 2018
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class BaseTest {

    private static final Log logger = LogFactory.getLog(BaseTest.class);
    private static final ZoneId UTC = ZoneId.of("UTC");
    private static final String PLAT01_GA = Config.properties.getProperty("ga");
    private static final String PLAT01_EWS = Config.properties.getProperty("ews");

    private static final String testUsername = Config.properties.getProperty("user");
    private static final String testAccountId = Config.properties.getProperty("account");

    UserOfAccount userOfAccount = new UserOfAccount(testUsername, testAccountId);

    @Mock
    private ExchangeCredentials exchangeCreds;

    @Mock
    ConfigurationResolver mockCR;

    @Mock
    private CacheManager mockCM;

    private Endpoint endpoint = new Endpoint();

    @BeforeSuite
    public void subscribeUser() throws InterruptedException {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        logger.info("Subscribing user " + userOfAccount.getUsername() + "to push notifications " +
                "Url:" + PLAT01_EWS + userOfAccount.getAccountId() + "/subscriptions");
        given()
                .contentType(ContentType.JSON)
                .body("[\"" + userOfAccount.getUsername() + "\"]")
                .post(PLAT01_EWS + userOfAccount.getAccountId() + "/subscriptions")
                .then()
                .assertThat().statusCode(202);
        Thread.sleep(5000);
    }

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        endpoint.setAutodiscover(false);
        endpoint.setUrl("https://outlook.office365.com/EWS/Exchange.asmx");

        when(mockCM.getCache(anyString())).thenReturn(null);
        when(mockCR.resolveExchangeVersion()).thenReturn(ExchangeVersion.Exchange2010_SP2);
        when(mockCR.resolveExchangeEndpoint(anyString())).thenReturn(endpoint);
        exchangeCreds = new EwsProperties.ExchangeCredentials("impersonation@ttops.onmicrosoft.com", "T1metradeDEV", EwsProperties.AccessType.IMPERSONATION);
        when(mockCR.resolveMasterUserPasswordAccess("app2devauto")).thenReturn(exchangeCreds);

        System.out.println("*");
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

