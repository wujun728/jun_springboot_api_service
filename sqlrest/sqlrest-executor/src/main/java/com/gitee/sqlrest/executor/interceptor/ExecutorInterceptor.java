package com.gitee.sqlrest.executor.interceptor;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.persistence.dao.SystemParamDao;
import com.gitee.sqlrest.persistence.entity.SystemParamEntity;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class ExecutorInterceptor implements HandlerInterceptor {

  private final SystemParamDao systemParamDao;

  public ExecutorInterceptor(SystemParamDao systemParamDao) {
    this.systemParamDao = systemParamDao;
  }

  private boolean isApiDocOpen() {
    SystemParamEntity entity = systemParamDao.getByParamKey(Constants.SYS_PARAM_KEY_API_DOC_OPEN);
    if (null == entity) {
      return true;
    }
    Class<Boolean> clazz = entity.getParamType().getClazz();
    String paramValue = entity.getParamValue();
    return clazz.cast(entity.getParamType().getConverter().apply(paramValue));
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    String path = request.getRequestURI();
    if (path.startsWith("/apidoc")) {
      return isApiDocOpen();
    }
    return true;
  }
}
