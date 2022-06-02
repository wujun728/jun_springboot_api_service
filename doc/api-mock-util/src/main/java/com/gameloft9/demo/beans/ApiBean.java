package com.gameloft9.demo.beans;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口注册信息
 * Created by gameloft9 on 2018/8/10.
 */
@Data
public class ApiBean implements Serializable{

    private String api;

    private String requestMethod;

    private String msg;

}
