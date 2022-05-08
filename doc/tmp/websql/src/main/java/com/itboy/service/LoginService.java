package com.itboy.service;

import com.itboy.model.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @ClassName LoginService
 * @Description 登录接口
 * @Author 超 boy_0214@sina.com
 * @Date 2019/6/14 0014 10:52
 **/
public interface LoginService {
    SysUser findByUserName(String userName);

    LoginResult login(String userName, String password, HttpServletRequest request);

    void logout();

    Result<SysUserLog> getLogList(SysUserLog model);

    void updateUsers(SysUser sysUser);

    void updateSysSetUp(SysSetup sys);

    void initSystem();
}
