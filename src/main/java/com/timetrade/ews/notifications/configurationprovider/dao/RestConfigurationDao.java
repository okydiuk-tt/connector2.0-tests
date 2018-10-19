package com.timetrade.ews.notifications.configurationprovider.dao;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.timetrade.ews.notifications.configurationprovider.configuration.ConfigurationProviderProperties;
import com.timetrade.ews.notifications.configurationprovider.model.AccountEwsConfig;
import com.timetrade.ews.notifications.configurationprovider.model.AccountEwsConfigAsResource;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfig;
import com.timetrade.ews.notifications.configurationprovider.model.EwsConfigWithVersion;

public class RestConfigurationDao implements ConfigurationDao {
    private static final Log logger = LogFactory.getLog(RestConfigurationDao.class);

    private RestTemplate restTemplate;
    private ConfigurationProviderProperties properties;

    public RestConfigurationDao(RestTemplate restTemplate, ConfigurationProviderProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public EwsConfigWithVersion get(String accountId, Optional<EwsConfigWithVersion> existing) {
        UriComponents uri = UriComponentsBuilder
                .fromUriString(properties.getSingleConfigUrl())
                .buildAndExpand(Collections.singletonMap("accountId", accountId));

        logger.debug("Going to request configuration service at " + uri.toUriString());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (existing.isPresent()) {
            headers.add(HttpHeaders.IF_MODIFIED_SINCE, existing.get().getVersion());
        }

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri.toUri());

        ResponseEntity<EwsConfig> responseEntity;
        try {
            responseEntity = restTemplate.exchange(requestEntity, EwsConfig.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn(e.getMessage());
                throw new NoConfigAtOriginException("No configuration found at " + uri.toUriString());
            }
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }

        if (responseEntity.getStatusCode() == HttpStatus.NOT_MODIFIED) {
            // We can get NOT_MODIFIED (304) only if existing.isPresent() == true. So isPresent() == false is a coding error.
            logger.info("Content not modified for " + accountId);
            return existing.orElseThrow(() -> new DaoException("Existing configuration expected but not present"));
        }

        List<String> versionHeader = responseEntity.getHeaders().get(HttpHeaders.LAST_MODIFIED);

        return new EwsConfigWithVersion(responseEntity.getBody(), !versionHeader.isEmpty() ? versionHeader.get(0) : null);
    }

    public Set<AccountEwsConfig> getAccountConfigs() {
        logger.debug("Going to request configuration service at " + properties.getAllAccountsConfigUrl());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_UTF8_VALUE);

        RequestEntity<Set<Resource<AccountEwsConfig>>> requestEntity = new RequestEntity<>(headers, HttpMethod.GET,
                URI.create(properties.getAllAccountsConfigUrl()));

        ResponseEntity<AccountEwsConfigAsResource[]> responseEntity;
        try {
            responseEntity = restTemplate.exchange(requestEntity, AccountEwsConfigAsResource[].class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);

        }

        return Arrays.asList(responseEntity.getBody()).stream().collect(Collectors.toSet());
    }

}
