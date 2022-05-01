package com.itboy.model;

import lombok.Data;

/**
 * @ClassName LoginResult
 * @Description 登录BEAN
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/26 0026 16:54
 **/
@Data
public class LoginResult {

    private boolean isLogin = false;
    private String result;
}
