package com.zapi.workbench.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workbench.entity.WbApplication;

/**
 * 外部应用Service接口
 */
public interface WbApplicationService extends IService<WbApplication> {

    /**
     * 创建应用（自动生成授权码和密码）
     * @param application 应用信息
     * @return 创建后的应用
     */
    WbApplication createApplication(WbApplication application);

    /**
     * 重新生成授权码和密码
     * @param id 应用ID
     * @return 更新后的应用
     */
    WbApplication regenerateAuth(Long id);

    /**
     * 根据认证码和认证密码获取应用
     * @param authCode 授权码
     * @param authPsw 授权密码
     * @return 应用信息，验证失败返回null
     */
    WbApplication getApplicationByAuth(String authCode, String authPsw);
}
