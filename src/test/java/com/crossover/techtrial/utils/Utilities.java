package com.crossover.techtrial.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class Utilities {

    public static String dummyString() {
        return UUID.randomUUID().
                toString().
                replace("-", "").
                replaceAll("\\d", "");
    }

    public static HttpEntity<Object> getHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
