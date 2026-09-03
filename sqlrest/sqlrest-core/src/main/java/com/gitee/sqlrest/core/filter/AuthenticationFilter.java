package com.gitee.sqlrest.core.filter;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.json.JSONUtil;
import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.enums.HttpMethodEnum;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.common.util.TokenUtils;
import com.gitee.sqlrest.core.servlet.ClientTokenService;
import com.gitee.sqlrest.core.util.ServletUtils;
import com.gitee.sqlrest.persistence.dao.ApiAssignmentDao;
import com.gitee.sqlrest.persistence.entity.AccessRecordEntity;
import com.gitee.sqlrest.persistence.entity.ApiAssignmentEntity;
import com.gitee.sqlrest.persistence.mapper.AccessRecordMapper;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationFilter implements Filter {

  @Resource
  private ApiAssignmentDao apiAssignmentDao;
  @Resource
  private FlowControlManger flowControlManger;
  @Resource
  private ClientTokenService clientTokenService;
  @Resource
  private AccessRecordMapper accessRecordMapper;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) resp;
    String path = request.getRequestURI().substring(Constants.API_PATH_PREFIX.length() + 2);
    HttpMethodEnum method = HttpMethodEnum.exists(request.getMethod())
        ? HttpMethodEnum.valueOf(request.getMethod().toUpperCase())
        : HttpMethodEnum.GET;
    ApiAssignmentEntity apiConfigEntity = apiAssignmentDao.getByUk(method, path, false);
    if (null == apiConfigEntity || !apiConfigEntity.getStatus()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      String message = String.format("/%s/%s[%s]", Constants.API_PATH_PREFIX, path, method.name());
      ResultEntity result = ResultEntity.failed(ResponseErrorCode.ERROR_PATH_NOT_EXISTS, message);
      response.getWriter().append(JSONUtil.toJsonStr(result));
      return;
    }

    if (apiConfigEntity.getFlowStatus()) {
      String resourceName = Constants.getResourceName(method.name(), path);
      if (flowControlManger.checkFlowControl(resourceName, response)) {
        doAuthenticationFilter(chain, request, response, apiConfigEntity);
      }
    } else {
      doAuthenticationFilter(chain, request, response, apiConfigEntity);
    }
  }

  private void doAuthenticationFilter(FilterChain chain, HttpServletRequest request, HttpServletResponse response,
      ApiAssignmentEntity apiConfigEntity) throws IOException {
    AccessRecordEntity accessRecordEntity = AccessRecordEntity.builder()
        .path(request.getRequestURI())
        .status(HttpStatus.OK.value())
        .duration(System.currentTimeMillis())
        .ipAddr(ServletUtils.getIpAddr())
        .userAgent(ServletUtils.getUserAgent())
        .apiId(apiConfigEntity.getId())
        .build();

    String path = apiConfigEntity.getPath();
    HttpMethodEnum method = apiConfigEntity.getMethod();

    try {
      if (!apiConfigEntity.getOpen()) {
        String tokenStr = TokenUtils.getRequestToken(request);
        String appKey = clientTokenService.verifyTokenAndGetAppKey(tokenStr);
        accessRecordEntity.setClientKey(appKey);
        if (null == appKey) {
          log.error("Failed get app key from token [{}].", tokenStr);
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          String message = String.format("/%s/%s[%s]", Constants.API_PATH_PREFIX, path, method.name());
          ResultEntity result = ResultEntity.failed(ResponseErrorCode.ERROR_ACCESS_FORBIDDEN, message);
          response.getWriter().append(JSONUtil.toJsonStr(result));
          return;
        } else {
          boolean verify = clientTokenService.verifyAuthGroup(appKey, apiConfigEntity.getGroupId());
          if (!verify) {
            log.error("Failed verify group from token [{}] , app key [{}].", tokenStr, appKey);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String message = String.format("%s[%s]", path, method.name());
            ResultEntity result = ResultEntity.failed(ResponseErrorCode.ERROR_ACCESS_FORBIDDEN, message);
            response.getWriter().append(JSONUtil.toJsonStr(result));
            return;
          }
        }
      }
      chain.doFilter(request, response);
    } catch (Throwable t) {
      String exception = (null != t.getMessage()) ? t.getMessage() : ExceptionUtil.stacktraceToString(t, 100);
      ResultEntity resultEntity = ResultEntity.failed(ResponseErrorCode.ERROR_INTERNAL_ERROR, exception);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().append(JSONUtil.toJsonStr(resultEntity));
      accessRecordEntity.setException(exception);
    } finally {
      accessRecordEntity.setDuration(System.currentTimeMillis() - accessRecordEntity.getDuration());
      CompletableFuture.runAsync(() -> accessRecordMapper.insert(accessRecordEntity));
    }
  }

  @Override
  public void destroy() {

  }
}
