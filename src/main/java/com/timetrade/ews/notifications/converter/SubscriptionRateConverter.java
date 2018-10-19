package com.timetrade.ews.notifications.converter;

import java.util.concurrent.TimeUnit;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.timetrade.ews.notifications.configuration.EwsProperties.SubscriptionsProperties.SubscriptionRate;

public class SubscriptionRateConverter implements Converter<String, SubscriptionRate> {

    public SubscriptionRate convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }

        String[] ratePropSplit = source.split("/");
        return new SubscriptionRate(
                Integer.parseInt(ratePropSplit[0]),
                TimeUnit.SECONDS.toMillis(Integer.parseInt(ratePropSplit[1])));
    }

}
