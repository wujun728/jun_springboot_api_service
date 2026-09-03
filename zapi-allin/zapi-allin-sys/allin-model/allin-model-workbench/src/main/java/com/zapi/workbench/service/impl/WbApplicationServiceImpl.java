package com.zapi.workbench.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.util.UserContext;
import com.zapi.workbench.entity.WbApplication;
import com.zapi.workbench.mapper.WbApplicationMapper;
import com.zapi.workbench.service.WbApplicationService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

/**
 * 外部应用Service实现类
 */
@Service
public class WbApplicationServiceImpl extends ServiceImpl<WbApplicationMapper, WbApplication> 
        implements WbApplicationService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public WbApplication createApplication(WbApplication application) {
        // 自动生成授权码和密码
        application.setAuthCode(generateAuthCode());
        application.setAuthPsw(generateAuthPassword());
        
        // 设置默认状态
        if (application.getStatus() == null) {
            application.setStatus(1);
        }
        
        // 设置创建信息
        application.setCreateTime(LocalDateTime.now());
        application.setUpdateTime(LocalDateTime.now());
        
        // 获取当前用户
        try {
            Long userId = UserContext.getCurrentUserId();
            if (userId != null) {
                application.setCreator(String.valueOf(userId));
            }
        } catch (Exception e) {
            // 忽略获取用户失败的情况
        }
        
        save(application);
        return application;
    }

    @Override
    public WbApplication regenerateAuth(Long id) {
        WbApplication application = getById(id);
        if (application == null) {
            throw new RuntimeException("应用不存在");
        }
        
        // 重新生成授权码和密码
        application.setAuthCode(generateAuthCode());
        application.setAuthPsw(generateAuthPassword());
        application.setUpdateTime(LocalDateTime.now());
        
        // 获取当前用户
        try {
            Long userId = UserContext.getCurrentUserId();
            if (userId != null) {
                application.setUpdater(String.valueOf(userId));
            }
        } catch (Exception e) {
            // 忽略获取用户失败的情况
        }
        
        updateById(application);
        return application;
    }

    @Override
    public WbApplication getApplicationByAuth(String authCode, String authPsw) {
        if (authCode == null || authPsw == null) {
            return null;
        }
        
        QueryWrapper<WbApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("auth_code", authCode)
                   .eq("auth_psw", authPsw)
                   .eq("status", 1);
        
        return getOne(queryWrapper);
    }

    /**
     * 生成授权码（32位UUID）
     */
    private String generateAuthCode() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成授权密码（32位随机字符串）
     */
    private String generateAuthPassword() {
        byte[] bytes = new byte[24];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
