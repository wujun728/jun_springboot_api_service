package com.gitee.sqlrest.executor.model;

import com.gitee.sqlrest.common.enums.HttpMethodEnum;
import com.gitee.sqlrest.core.servlet.ApiServletService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpApiServlet extends HttpServlet {

  private ApiServletService apiServletService;

  public HttpApiServlet(ApiServletService apiServletService) {
    this.apiServletService = apiServletService;
  }

  private void doHandle(HttpMethodEnum method, HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    apiServletService.process(method, request, response);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doHandle(HttpMethodEnum.GET, req, resp);
  }

  @Override
  protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doHandle(HttpMethodEnum.HEAD, req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doHandle(HttpMethodEnum.POST, req, resp);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doHandle(HttpMethodEnum.PUT, req, resp);
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doHandle(HttpMethodEnum.DELETE, req, resp);
  }
}
