package com.ruoyi.api.core.model;

import java.util.HashMap;
import java.util.Map;

public class ApiRequest {

    private String path;
    private String method;
    private Map<String, Object> headers = new HashMap<>();
    private Map<String, Object> queryParams = new HashMap<>();
    private Map<String, Object> bodyParams = new HashMap<>();

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Map<String, Object> getHeaders() { return headers; }
    public void setHeaders(Map<String, Object> headers) { this.headers = headers; }

    public Map<String, Object> getQueryParams() { return queryParams; }
    public void setQueryParams(Map<String, Object> queryParams) { this.queryParams = queryParams; }

    public Map<String, Object> getBodyParams() { return bodyParams; }
    public void setBodyParams(Map<String, Object> bodyParams) { this.bodyParams = bodyParams; }

    public Object getParam(String name) {
        if (queryParams.containsKey(name)) return queryParams.get(name);
        if (bodyParams.containsKey(name)) return bodyParams.get(name);
        return headers.get(name);
    }

}