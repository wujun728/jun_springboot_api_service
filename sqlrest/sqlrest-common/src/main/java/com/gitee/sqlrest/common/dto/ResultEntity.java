package com.gitee.sqlrest.common.dto;

import com.gitee.sqlrest.common.exception.ResponseErrorCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@ApiModel(description = "响应结果")
@AllArgsConstructor
@Data
public class ResultEntity<T> {

  private static final String SUCCESS = "success";

  @ApiModelProperty("状态码")
  private Integer code;

  @ApiModelProperty("状态描述")
  private String message;

  @ApiModelProperty("数据")
  private T data;

  public static <T> ResultEntity success() {
    return new ResultEntity(0, SUCCESS, null);
  }

  public static <T> ResultEntity success(T data) {
    return new ResultEntity<>(0, SUCCESS, data);
  }

  public static ResultEntity failed(ResponseErrorCode code) {
    return new ResultEntity(code.getCode(), code.getMessage(), null);
  }

  public static ResultEntity failed(ResponseErrorCode code, String message) {
    return new ResultEntity(code.getCode(), code.getMessage() + ":" + message, null);
  }

}
