package com.ruoyi.system.api.factory;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.api.RemoteUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 *
 * @author ruoyi
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {
            @Override
            public AjaxResult getUserInfo(String username, String source) {
                return AjaxResult.error("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public AjaxResult registerUserInfo(SysUser sysUser, String source) {
                return AjaxResult.error("注册用户失败:" + throwable.getMessage());
            }
        };
    }
}
