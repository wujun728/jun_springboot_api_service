package com.ruoyi.api.core.constant;

public interface ApiConstant {

    String PREFIX_API = "/api";
    String PREFIX_REVERSE = "/reverse";
    String PREFIX_MCP = "/mcp";
    String PREFIX_OPENAPI = "/openapi";

    String HEADER_AUTH_TOKEN = "Authorization";
    String HEADER_APP_KEY = "AppKey";
    String HEADER_APP_SECRET = "AppSecret";
    String HEADER_TIMESTAMP = "Timestamp";
    String HEADER_SIGN = "Sign";

    String CACHE_PREFIX_API = "api:";
    String CACHE_PREFIX_DATASOURCE = "ds:";
    String CACHE_PREFIX_PLUGIN = "plugin:";

    int MAX_API_MERGE_COUNT = 10;

    long DEFAULT_CACHE_EXPIRE = 300;

}