package com.zapi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.entity.UserCertInfo;

import java.util.Map;

/**
 * 用户证书信息Service接口
 */
public interface UserCertInfoService extends IService<UserCertInfo> {

    /**
     * 分页查询用户证书
     */
    IPage<UserCertInfo> pageUserCerts(Page<UserCertInfo> page, Long userId, String certCn, String certType);

    /**
     * 生成SM2证书
     */
    UserCertInfo createSM2Cert(Long userId, String certC, String certCn, String certOu, 
                                String certO, String certSt, String certL, 
                                Integer certValidTime, String certPwd);

    /**
     * 生成RSA证书
     */
    UserCertInfo createRSACert(Long userId, String certC, String certCn, String certOu, 
                                String certO, String certSt, String certL, 
                                Integer certValidTime, String certPwd);

    /**
     * 删除证书
     */
    boolean deleteCert(Long id);

    /**
     * 批量删除证书
     */
    boolean batchDelete(java.util.List<Long> ids);
}

