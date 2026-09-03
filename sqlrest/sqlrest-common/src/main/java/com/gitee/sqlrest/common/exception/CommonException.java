package com.gitee.sqlrest.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonException extends RuntimeException {

  private ResponseErrorCode code;
  private String message;

}
