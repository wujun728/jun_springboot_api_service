package com.gitee.sqlrest.core.servlet;

import com.gitee.sqlrest.common.consts.Constants;
import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.enums.HttpMethodEnum;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import com.gitee.sqlrest.core.exec.ApiExecuteService;
import com.gitee.sqlrest.core.util.JacksonUtils;
import com.gitee.sqlrest.persistence.dao.ApiAssignmentDao;
import com.gitee.sqlrest.persistence.entity.ApiAssignmentEntity;
import com.google.common.base.Charsets;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApiServletService {

  @Resource
  private ApiAssignmentDao apiAssignmentDao;
  @Resource
  private ApiExecuteService apiExecuteService;

  public void process(HttpMethodEnum method, HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(Charsets.UTF_8.name());
    String path = request.getRequestURI().substring(Constants.API_PATH_PREFIX.length() + 2);
    ResultEntity result = ResultEntity.success();
    ApiAssignmentEntity apiConfigEntity = apiAssignmentDao.getByUk(method, path);
    if (null == apiConfigEntity || !apiConfigEntity.getStatus()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      String message = String.format("/%s/%s[%s]", Constants.API_PATH_PREFIX, path, method.name());
      result = ResultEntity.failed(ResponseErrorCode.ERROR_PATH_NOT_EXISTS, message);
    } else {
      result = apiExecuteService.execute(apiConfigEntity, request);
    }
    if (0 != result.getCode()) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
    String json = JacksonUtils.toJsonStr(result, apiConfigEntity.getResponseFormat());
    response.getWriter().append(json);
  }

}
