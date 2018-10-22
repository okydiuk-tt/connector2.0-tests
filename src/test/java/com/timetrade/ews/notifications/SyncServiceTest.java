package com.timetrade.ews.notifications;

import com.timetrade.ews.notifications.configuration.EwsProperties;
import com.timetrade.ews.notifications.configurationprovider.configuration.ConfigurationProviderProperties;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfig.Endpoint;
import com.timetrade.ews.notifications.configurationprovider.service.ConfigurationResolver;
import com.timetrade.ews.notifications.model.UserOfAccount;
import com.timetrade.ews.notifications.service.*;
import com.timetrade.ews.notifications.service.wrappers.TTExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.LegacyFreeBusyStatus;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.calendar.AppointmentType;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import microsoft.exchange.webservices.data.search.CalendarView;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import org.mockito.Mockito.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
//        (classes = {TestConfig.class})
@EnableConfigurationProperties({ConfigurationProviderProperties.class, EwsProperties.class})
@ActiveProfiles("integration")
public class SyncServiceTest {

    private static final String testUsername = "userName";
    private static final String testAccountId = "accountId";


    private UserOfAccount testUserOfAccount = new UserOfAccount(testUsername, testAccountId);

    @Mock
    private EwsProperties.ExchangeCredentials exchangeCreds;

    @Mock
    private ConfigurationResolver mockCR;

    @Mock
    private CacheManager mockCM;

    private Endpoint endpoint = new Endpoint();
    @Mock
    private EwsProperties.KafkaProperties mockKafkaProperties;


    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);

        endpoint.setAutodiscover(false);
        endpoint.setUrl("test/url");

        when(mockCM.getCache(anyString())).thenReturn(null);
        when(mockCR.resolveExchangeVersion()).thenReturn(ExchangeVersion.Exchange2010_SP2);
        when(mockCR.resolveExchangeEndpoint(anyString())).thenReturn(endpoint);
        exchangeCreds = new EwsProperties.ExchangeCredentials("impersonation@ttops.onmicrosoft.com", "T1metradeDEV", EwsProperties.AccessType.IMPERSONATION);
        when(mockCR.resolveMasterUserPasswordAccess("accountId")).thenReturn(exchangeCreds);
        when(exchangeCreds.getExchangePassword()).thenReturn("impersonation@ttops.onmicrosoft.com");
        when(exchangeCreds.getExchangeUsername()).thenReturn("T1metradeDEV");
        TTExchangeService ttExchangeService = new TTExchangeService(testUserOfAccount, mockCR);
        int k = 3;

    }

    @Test
    public void testCreate() {
        Assert.assertNotNull(5 == 3);
    }

}
