package com.ruoyi.auth.controller;

import com.ruoyi.auth.service.AuthService;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证服务控制器
 * 微服务模式下的独立登录/登出接口
 *
 * @author ruoyi
 */
@RestController
public class TokenController {

    @Autowired
    private AuthService authService;

    /**
     * 登录接口
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        // 用户登录
        String token = authService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("token", token);
        return ajax;
    }

    /**
     * 登出接口
     */
    @DeleteMapping("/logout")
    public AjaxResult logout(@RequestHeader(value = "Authorization", required = false) String token) {
        authService.logout(token);
        return AjaxResult.success();
    }

    /**
     * 刷新令牌
     */
    @PostMapping("/refresh")
    public AjaxResult refresh(@RequestHeader(value = "Authorization") String token) {
        authService.refreshToken(token);
        return AjaxResult.success();
    }
}
