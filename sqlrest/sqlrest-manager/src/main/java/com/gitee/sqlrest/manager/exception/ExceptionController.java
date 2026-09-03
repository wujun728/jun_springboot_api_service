package com.gitee.sqlrest.manager.exception;

import com.gitee.sqlrest.common.dto.ResultEntity;
import com.gitee.sqlrest.common.exception.CommonException;
import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ExceptionController {

  @ResponseBody
  @ExceptionHandler(value = Exception.class)
  public ResultEntity errorHandler(Exception e) {
    if (e instanceof CommonException) {
      CommonException ex = (CommonException) e;
      return ResultEntity.failed(ex.getCode(), ex.getMessage());
    }

    log.error("Error:", e);
    if (e instanceof NullPointerException) {
      return ResultEntity.failed(ResponseErrorCode.ERROR_INTERNAL_ERROR, "Null Pointer Exception");
    } else {
      return ResultEntity.failed(ResponseErrorCode.ERROR_INTERNAL_ERROR, e.getMessage());
    }

  }
}
