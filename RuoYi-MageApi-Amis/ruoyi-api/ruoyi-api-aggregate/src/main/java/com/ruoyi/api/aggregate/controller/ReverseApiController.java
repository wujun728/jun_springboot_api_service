package com.ruoyi.api.aggregate.controller;

import com.ruoyi.api.aggregate.service.ReverseApiService;
import com.ruoyi.api.aggregate.service.ResultMerger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Supplier;

@RestController
@RequestMapping("/reverse")
public class ReverseApiController {

    private final ReverseApiService reverseApiService;
    private final ResultMerger resultMerger;

    public ReverseApiController(ReverseApiService reverseApiService, ResultMerger resultMerger) {
        this.reverseApiService = reverseApiService;
        this.resultMerger = resultMerger;
    }

    @GetMapping("/{prefix}/{name}")
    public Map<String, Object> executeGet(@PathVariable String prefix, @PathVariable String name, HttpServletRequest request) {
        return execute(prefix, name, getParams(request), getAuthHeaders(request));
    }

    @PostMapping("/{prefix}/{name}")
    public Map<String, Object> executePost(@PathVariable String prefix, @PathVariable String name, @RequestBody(required = false) Map<String, Object> body, HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        if (body != null) {
            params.putAll(body);
        }
        return execute(prefix, name, params, getAuthHeaders(request));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> execute(String prefix, String name, Map<String, Object> params, Map<String, String> authHeaders) {
        String[] names = name.split(",");
        if (names.length > 10) {
            throw new IllegalArgumentException("Merge count exceeds maximum: 10");
        }
        if (names.length == 1) {
            Object result = reverseApiService.execute(prefix, name, params, authHeaders);
            Map<String, Object> map = new HashMap<>();
            map.put("result", result);
            return map;
        }
        List<Supplier<Object>> tasks = new ArrayList<>();
        for (String n : names) {
            String finalName = n.trim();
            tasks.add(() -> reverseApiService.execute(prefix, finalName, params, authHeaders));
        }
        return resultMerger.merge(tasks);
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) {
                params.put(k, v[0]);
            }
        });
        return params;
    }

    private Map<String, String> getAuthHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        String auth = request.getHeader("Authorization");
        if (auth != null) {
            headers.put("Authorization", auth);
        }
        String clientId = request.getHeader("clientId");
        if (clientId != null) {
            headers.put("clientId", clientId);
        }
        String clientSecret = request.getHeader("clientSecret");
        if (clientSecret != null) {
            headers.put("clientSecret", clientSecret);
        }
        return headers;
    }
}