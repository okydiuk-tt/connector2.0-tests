package com.timetrade.connector2.qa.service.wrappers;

import com.timetrade.connector2.qa.configuration.EwsProperties;
import com.timetrade.connector2.qa.configurationprovider.model.EwsConfig;
import com.timetrade.connector2.qa.configurationprovider.model.EwsConfig.Endpoint;
import com.timetrade.connector2.qa.model.UserOfAccount;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ConnectingIdType;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.misc.ImpersonatedUserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * Created by oleksandr.romakh on 5/24/2017.
 */
public class TTExchangeService extends ExchangeService {
    private static final Logger logger = LoggerFactory.getLogger(TTExchangeService.class);

    private UserOfAccount userOfAccount;
    private EwsProperties.ExchangeCredentials exchangeCredentials;
    private EwsConfig.Endpoint endpoint;


    public TTExchangeService(UserOfAccount userOfAccount) {
        super(ExchangeVersion.Exchange2007_SP1);

        this.userOfAccount = userOfAccount;
        this.exchangeCredentials = new EwsProperties.ExchangeCredentials("impersonation@ttops.onmicrosoft.com", "T1metradeDEV", EwsProperties.AccessType.IMPERSONATION);

        endpoint = new Endpoint();
        endpoint.setAutodiscover(false);
        endpoint.setUrl("https://outlook.office365.com/EWS/Exchange.asmx");

        if (endpoint.getAutodiscover()) {
            try {
                this.autodiscoverUrl(userOfAccount.getUsername());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        } else {
            this.setUrl(URI.create(endpoint.getUrl()));
        }

        setCredentials(userOfAccount, this, exchangeCredentials);
    }

    private static EwsProperties.ExchangeCredentials setCredentials(UserOfAccount userOfAccount, ExchangeService exchangeService,
                                                                    EwsProperties.ExchangeCredentials masterUserPasswordAccess) {

        exchangeService.setCredentials(
                new WebCredentials(masterUserPasswordAccess.getExchangeUsername(), masterUserPasswordAccess.getExchangePassword()));

        if (EwsProperties.AccessType.IMPERSONATION == masterUserPasswordAccess.getAccessType()) {
            logger.debug("Impersonation is active: {} will act on behalf of {}", masterUserPasswordAccess.getExchangeUsername(),
                    userOfAccount.getUsername());
            exchangeService.setImpersonatedUserId(new ImpersonatedUserId(ConnectingIdType.SmtpAddress, userOfAccount.getUsername()));
        } else {
            logger.debug("Delegation is active: {} will act on behalf of {}", masterUserPasswordAccess.getExchangeUsername(),
                    userOfAccount.getUsername());
        }

        return masterUserPasswordAccess;
    }
}
