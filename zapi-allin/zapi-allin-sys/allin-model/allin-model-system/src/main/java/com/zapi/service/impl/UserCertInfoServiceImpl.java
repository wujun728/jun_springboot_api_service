package com.zapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zapi.entity.UserCertInfo;
import com.zapi.file.entity.FileInfo;
import com.zapi.file.service.FileService;
import com.zapi.mapper.UserCertInfoMapper;
import com.zapi.service.UserCertInfoService;
import com.zapi.util.cert.CertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 用户证书信息Service实现类
 */
@Service
public class UserCertInfoServiceImpl extends ServiceImpl<UserCertInfoMapper, UserCertInfo> implements UserCertInfoService {

    @Autowired
    private FileService fileService;

    @Override
    public IPage<UserCertInfo> pageUserCerts(Page<UserCertInfo> page, Long userId, String certCn, String certType) {
        QueryWrapper<UserCertInfo> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (certCn != null && !certCn.trim().isEmpty()) {
            queryWrapper.like("cert_cn", certCn);
        }
        if (certType != null && !certType.trim().isEmpty()) {
            queryWrapper.eq("cert_type", certType);
        }
        queryWrapper.orderByDesc("create_time");
        return page(page, queryWrapper);
    }

    @Override
    @Transactional
    public UserCertInfo createSM2Cert(Long userId, String certC, String certCn, String certOu, 
                                       String certO, String certSt, String certL, 
                                       Integer certValidTime, String certPwd) {
        try {
            
            // 调用工具类生成证书
            Map<String, byte[]> certData = CertUtils.createSM2CertToOne(
                certC, certCn, certOu, certO, certSt, certL, certValidTime, certPwd
            );
            
            byte[] certBytes = certData.get("certData");
            byte[] publicKey = certData.get("publicKey");
            
            if (certBytes == null) {
                throw new RuntimeException("证书生成失败，未获取到证书数据");
            }
            
            
            // 上传证书文件
            String fileName = certCn + "_SM2_" + System.currentTimeMillis() + ".pfx";
            ByteArrayInputStream is = new ByteArrayInputStream(certBytes);
            
            FileInfo fileInfo = fileService.uploadFile(
                is,
                fileName,
                "cert",
                "SM2证书：" + certCn,
                userId,
                null
            );
            
            if (fileInfo == null) {
                throw new RuntimeException("证书文件上传失败");
            }
            
            
            // 保存证书记录
            UserCertInfo userCertInfo = new UserCertInfo();
            userCertInfo.setUserId(userId);
            userCertInfo.setCertUrl(fileInfo.getId());
            userCertInfo.setCertPwd(certPwd);
            userCertInfo.setCertNum(256); // SM2固定256位
            userCertInfo.setCertC(certC);
            userCertInfo.setCertSt(certSt);
            userCertInfo.setCertL(certL);
            userCertInfo.setCertO(certO);
            userCertInfo.setCertCn(certCn);
            userCertInfo.setCertOu(certOu);
            userCertInfo.setCertType("SM2");
            userCertInfo.setCertPublicKey(publicKey);
            userCertInfo.setCertValidTime(certValidTime);
            userCertInfo.setCertNotBefore(LocalDateTime.now());
            userCertInfo.setCertNotAfter(LocalDateTime.now().plusDays(certValidTime));
            
            boolean saved = save(userCertInfo);
            if (!saved) {
                throw new RuntimeException("保存证书记录失败");
            }
            
            
            return userCertInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建SM2证书失败：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public UserCertInfo createRSACert(Long userId, String certC, String certCn, String certOu, 
                                       String certO, String certSt, String certL, 
                                       Integer certValidTime, String certPwd) {
        try {
            
            // 调用工具类生成证书
            Map<String, byte[]> certData = CertUtils.createRSACertToOne(
                certC, certCn, certOu, certO, certSt, certL, certValidTime, certPwd
            );
            
            byte[] certBytes = certData.get("keyStoreData");
            byte[] publicKey = certData.get("publicKey");
            
            if (certBytes == null) {
                throw new RuntimeException("证书生成失败，未获取到证书数据");
            }
            
            
            // 上传证书文件
            String fileName = certCn + "_RSA2048_" + System.currentTimeMillis() + ".pfx";
            ByteArrayInputStream is = new ByteArrayInputStream(certBytes);
            
            FileInfo fileInfo = fileService.uploadFile(
                is,
                fileName,
                "cert",
                "RSA证书：" + certCn,
                userId,
                null
            );
            
            if (fileInfo == null) {
                throw new RuntimeException("证书文件上传失败");
            }
            
            
            // 保存证书记录
            UserCertInfo userCertInfo = new UserCertInfo();
            userCertInfo.setUserId(userId);
            userCertInfo.setCertUrl(fileInfo.getId());
            userCertInfo.setCertPwd(certPwd);
            userCertInfo.setCertNum(2048); // RSA 2048位
            userCertInfo.setCertC(certC);
            userCertInfo.setCertSt(certSt);
            userCertInfo.setCertL(certL);
            userCertInfo.setCertO(certO);
            userCertInfo.setCertCn(certCn);
            userCertInfo.setCertOu(certOu);
            userCertInfo.setCertType("RSA");
            userCertInfo.setCertPublicKey(publicKey);
            userCertInfo.setCertValidTime(certValidTime);
            userCertInfo.setCertNotBefore(LocalDateTime.now());
            userCertInfo.setCertNotAfter(LocalDateTime.now().plusDays(certValidTime));
            
            boolean saved = save(userCertInfo);
            if (!saved) {
                throw new RuntimeException("保存证书记录失败");
            }
            
            
            return userCertInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建RSA证书失败：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean deleteCert(Long id) {
        UserCertInfo userCertInfo = getById(id);
        if (userCertInfo == null) {
            throw new RuntimeException("证书不存在");
        }
        
        // 删除文件
        if (userCertInfo.getCertUrl() != null) {
            try {
                fileService.deleteFile(userCertInfo.getCertUrl());
            } catch (Exception e) {
            }
        }
        
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        
        // 删除所有相关文件
        List<UserCertInfo> certs = listByIds(ids);
        for (UserCertInfo cert : certs) {
            if (cert.getCertUrl() != null) {
                try {
                    fileService.deleteFile(cert.getCertUrl());
                } catch (Exception e) {
                }
            }
        }
        
        return removeByIds(ids);
    }
}

