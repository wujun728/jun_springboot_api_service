package com.gameloft9.demo.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 注销请求
 * Created by gameloft9 on 2018/8/9.
 */
@Data
public class ApiUnregisterRequest {

    @NotBlank
    private String api;
    @NotBlank
    private String index;
    @NotBlank
    private String requestMethod;
}
