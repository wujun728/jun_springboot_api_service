package com.ruoyi.system.api;

import com.ruoyi.common.cloud.constants.ServiceConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.api.factory.RemoteUserFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务远程调用
 *
 * @author ruoyi
 */
@FeignClient(contextId = "remoteUserService", value = ServiceConstants.SYSTEM_SERVICE,
        fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/system/user/info/{username}")
    AjaxResult getUserInfo(@PathVariable("username") String username,
                           @RequestHeader(value = "from-source", required = false) String source);

    /**
     * 注册用户信息
     */
    @PostMapping("/system/user/register")
    AjaxResult registerUserInfo(@RequestBody SysUser sysUser,
                                @RequestHeader(value = "from-source", required = false) String source);
}
