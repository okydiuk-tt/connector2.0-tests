package com.timetrade.ews.notifications.converter;

import java.net.URI;

import org.springframework.core.convert.converter.Converter;

public class UriConverter implements Converter<String, URI> {

    public URI convert(String source) {
        return source != null && !source.isEmpty() ? URI.create(source) : null;
    }

}
