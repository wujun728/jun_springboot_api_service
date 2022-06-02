package com.soukuan.web;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xiebiao
 * @date  2018-07-11  10:08:27
 * @version v1.0
 * Description
 */
@Builder
@Data
public class ApiResponseEntity<T extends Object> implements Serializable {
    /**
     * 错误消息
     */
    private String errorMsg;
    /**
     * 数据内容
     */
    private T data;
}
