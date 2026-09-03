package com.gitee.sqlrest.common.consts;

public abstract class Constants {

  public static final String API_PATH_PREFIX = "api";

  public static final String MANGER_API_PREFIX = "/sqlrest/manager/api";
  public static final String MANGER_API_V1 = MANGER_API_PREFIX + "/v1";

  public static final String PARAM_PAGE_NUMBER = "apiPageNum";
  public static final String PARAM_PAGE_SIZE = "apiPageSize";

  public static final String GATEWAY_APPLICATION_NAME = "sqlrest-gateway";

  public static final String CACHE_KEY_TOKEN_CLIENT = "token_client";

  public static final Long CLIENT_TOKEN_DURATION_SECONDS = 7200L;

  public static final int SC_TOO_MANY_REQUESTS = 429;

  public static final String API_DOC_PATH_PREFIX = "/apidoc";

  public static final String SYS_PARAM_KEY_API_DOC_OPEN = "apiDocOpen";

  public static final String getResourceName(String method, String path) {
    return String.format("/%s/%s[%s]", Constants.API_PATH_PREFIX, path, method);
  }
}
