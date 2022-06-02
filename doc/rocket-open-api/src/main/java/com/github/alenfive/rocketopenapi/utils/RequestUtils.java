package com.github.alenfive.rocketopenapi.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 请求工具类
 */
@Component
public class RequestUtils {

    private List<String> bodyMethods = Arrays.asList("POST", "PUT", "PATCH");

    public Map<String, String> buildHeaderParams(HttpServletRequest request) {
        Enumeration<String> headerKeys = request.getHeaderNames();
        Map<String, String> result = new HashMap<>();
        while (headerKeys.hasMoreElements()) {
            String key = headerKeys.nextElement();
            String value = request.getHeader(key);
            result.put(key, value);
        }
        return result;
    }

    public static Map<String, String> buildHeaderParams(ResponseEntity<String> responseEntity) {
        Map<String, String> header = new HashMap<>();
        Set<String> headerKeys = responseEntity.getHeaders().keySet();
        for (String headerKey : headerKeys) {
            header.put(headerKey, responseEntity.getHeaders().get(headerKey).get(0));
        }
        return header;
    }

}
