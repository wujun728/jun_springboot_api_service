package com.gitee.sqlrest.core.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public interface FlowControlManger {

  boolean checkFlowControl(String resourceName, HttpServletResponse response) throws IOException;
}
