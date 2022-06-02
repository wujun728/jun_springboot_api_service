package com.gameloft9.demo.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 提交api申请bean
 * Created by gameloft9 on 2018/8/8.
 */
@Data
public class ApiRegisterRequest {
    /**处理的方法序号*/
    @NotBlank
    private String index;

    @NotBlank
    private String api;

    private String requestMethod;

    private String msg;
}
