package com.zapi.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zapi.common.Result;
import com.zapi.entity.UserCertInfo;
import com.zapi.service.UserCertInfoService;
import com.zapi.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.zapi.util.cert.CertUtils;

import java.util.List;
import java.util.Map;

/**
 * 用户证书信息Controller
 */
@Api(tags = "用户证书管理")
@RestController
@RequestMapping("/system/cert")
public class UserCertInfoController {

    @Autowired
    private UserCertInfoService userCertInfoService;

    /**
     * 分页查询用户证书（只查询当前登录用户的证书）
     */
    @ApiOperation("分页查询用户证书")
    @GetMapping("/page")
    public Result<IPage<UserCertInfo>> pageUserCerts(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("CN项") @RequestParam(required = false) String certCn,
            @ApiParam("证书类型") @RequestParam(required = false) String certType) {
        try {
            // 从系统获取当前登录用户ID
            Long userId = UserContext.getCurrentUserId();
            if (userId == null) {
                return Result.error("未获取到当前登录用户信息，请重新登录");
            }
            
            Page<UserCertInfo> page = new Page<>(current, size);
            IPage<UserCertInfo> result = userCertInfoService.pageUserCerts(page, userId, certCn, certType);
            
            
            return Result.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取证书详情
     */
    @ApiOperation("根据ID获取证书详情")
    @GetMapping("/{id}")
    public Result<UserCertInfo> getCertById(
            @ApiParam("证书ID") @PathVariable Long id) {
        UserCertInfo cert = userCertInfoService.getById(id);
        if (cert == null) {
            return Result.error("证书不存在");
        }
        return Result.success(cert);
    }

    /**
     * 生成证书
     */
    @ApiOperation("生成证书")
    @PostMapping("/create")
    public Result<UserCertInfo> createCert(@RequestBody CreateCertRequest request) {
        try {
            // 从系统上下文获取当前登录用户ID
            Long userId = UserContext.getCurrentUserId();
            if (userId == null) {
                return Result.error("未获取到当前登录用户信息，请重新登录");
            }
            
            UserCertInfo cert;
            if ("SM2".equalsIgnoreCase(request.getCertType())) {
                cert = userCertInfoService.createSM2Cert(
                    userId,
                    request.getCertC(),
                    request.getCertCn(),
                    request.getCertOu(),
                    request.getCertO(),
                    request.getCertSt(),
                    request.getCertL(),
                    request.getCertValidTime(),
                    request.getCertPwd()
                );
            } else if ("RSA".equalsIgnoreCase(request.getCertType())) {
                cert = userCertInfoService.createRSACert(
                    userId,
                    request.getCertC(),
                    request.getCertCn(),
                    request.getCertOu(),
                    request.getCertO(),
                    request.getCertSt(),
                    request.getCertL(),
                    request.getCertValidTime(),
                    request.getCertPwd()
                );
            } else {
                return Result.error("不支持的证书类型：" + request.getCertType());
            }
            
            
            return Result.success("证书创建成功", cert);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建证书失败：" + e.getMessage());
        }
    }

    /**
     * 创建证书请求对象
     */
    public static class CreateCertRequest {
        private String certType;      // 证书类型：SM2/RSA
        private String certC;          // C项 国家
        private String certCn;         // CN项 公用名/CN
        private String certOu;         // OU项 部门/OU
        private String certO;          // O项 公用信息/O
        private String certSt;         // ST项 省份
        private String certL;          // L项 城市
        private Integer certValidTime; // 证书有效期(单位/天)
        private String certPwd;        // 证书密码

        public String getCertType() {
            return certType;
        }

        public void setCertType(String certType) {
            this.certType = certType;
        }

        public String getCertC() {
            return certC;
        }

        public void setCertC(String certC) {
            this.certC = certC;
        }

        public String getCertCn() {
            return certCn;
        }

        public void setCertCn(String certCn) {
            this.certCn = certCn;
        }

        public String getCertOu() {
            return certOu;
        }

        public void setCertOu(String certOu) {
            this.certOu = certOu;
        }

        public String getCertO() {
            return certO;
        }

        public void setCertO(String certO) {
            this.certO = certO;
        }

        public String getCertSt() {
            return certSt;
        }

        public void setCertSt(String certSt) {
            this.certSt = certSt;
        }

        public String getCertL() {
            return certL;
        }

        public void setCertL(String certL) {
            this.certL = certL;
        }

        public Integer getCertValidTime() {
            return certValidTime;
        }

        public void setCertValidTime(Integer certValidTime) {
            this.certValidTime = certValidTime;
        }

        public String getCertPwd() {
            return certPwd;
        }

        public void setCertPwd(String certPwd) {
            this.certPwd = certPwd;
        }
    }

    /**
     * 删除证书
     */
    @ApiOperation("删除证书")
    @DeleteMapping("/{id}")
    public Result<String> deleteCert(
            @ApiParam("证书ID") @PathVariable Long id) {
        try {
            boolean success = userCertInfoService.deleteCert(id);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 批量删除证书
     */
    @ApiOperation("批量删除证书")
    @DeleteMapping("/batch")
    public Result<String> batchDelete(
            @ApiParam("证书ID列表") @RequestBody List<Long> ids) {
        try {
            boolean success = userCertInfoService.batchDelete(ids);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * SM2证书签名
     */
    @ApiOperation("SM2证书签名")
    @PostMapping("/sign/sm2")
    public Result<Map<String, Object>> signSM2Cert(
            @ApiParam("证书文件") @RequestParam("certFile") MultipartFile certFile,
            @ApiParam("签名原文") @RequestParam("originalText") String originalText,
            @ApiParam("证书密码") @RequestParam("certPwd") String certPwd) {
        try {
            if (certFile == null || certFile.isEmpty()) {
                return Result.error("请选择证书文件");
            }
            if (originalText == null || originalText.trim().isEmpty()) {
                return Result.error("请输入签名原文");
            }
            if (certPwd == null || certPwd.trim().isEmpty()) {
                return Result.error("请输入证书密码");
            }
            
            return CertUtils.signSM2Cert(certFile, originalText, certPwd);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("签名失败：" + e.getMessage());
        }
    }

    /**
     * SM2证书验签
     */
    @ApiOperation("SM2证书验签")
    @PostMapping("/verify/sm2")
    public Result<Map<String, Object>> verifySM2Cert(@RequestBody VerifySM2Request request) {
        try {
            if (request.getPublicKey() == null || request.getPublicKey().trim().isEmpty()) {
                return Result.error("请输入公钥");
            }
            if (request.getOriginalText() == null || request.getOriginalText().trim().isEmpty()) {
                return Result.error("请输入签名原文");
            }
            if (request.getSignData() == null || request.getSignData().trim().isEmpty()) {
                return Result.error("请输入签名值");
            }
            
            return CertUtils.verifySM2Cert(request.getPublicKey(), request.getOriginalText(), request.getSignData());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("验签失败：" + e.getMessage());
        }
    }

    /**
     * SM2验签请求对象
     */
    public static class VerifySM2Request {
        private String publicKey;
        private String originalText;
        private String signData;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getOriginalText() {
            return originalText;
        }

        public void setOriginalText(String originalText) {
            this.originalText = originalText;
        }

        public String getSignData() {
            return signData;
        }

        public void setSignData(String signData) {
            this.signData = signData;
        }
    }

    /**
     * RSA证书签名
     */
    @ApiOperation("RSA证书签名")
    @PostMapping("/sign/rsa")
    public Result<Map<String, Object>> signRSACert(
            @ApiParam("证书文件") @RequestParam("certFile") MultipartFile certFile,
            @ApiParam("签名原文") @RequestParam("originalText") String originalText,
            @ApiParam("证书密码") @RequestParam("certPwd") String certPwd) {
        try {
            if (certFile == null || certFile.isEmpty()) {
                return Result.error("请选择证书文件");
            }
            if (originalText == null || originalText.trim().isEmpty()) {
                return Result.error("请输入签名原文");
            }
            if (certPwd == null || certPwd.trim().isEmpty()) {
                return Result.error("请输入证书密码");
            }
            
            return CertUtils.signRSACert(certFile, originalText, certPwd);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("签名失败：" + e.getMessage());
        }
    }

    /**
     * RSA证书验签
     */
    @ApiOperation("RSA证书验签")
    @PostMapping("/verify/rsa")
    public Result<Map<String, Object>> verifyRSACert(
            @ApiParam("证书文件") @RequestParam("certFile") MultipartFile certFile,
            @ApiParam("证书密码") @RequestParam("certPwd") String certPwd,
            @ApiParam("签名原文") @RequestParam("originalText") String originalText,
            @ApiParam("签名值") @RequestParam("signData") String signData) {
        try {
            if (certFile == null || certFile.isEmpty()) {
                return Result.error("请选择证书文件");
            }
            if (certPwd == null || certPwd.trim().isEmpty()) {
                return Result.error("请输入证书密码");
            }
            if (originalText == null || originalText.trim().isEmpty()) {
                return Result.error("请输入签名原文");
            }
            if (signData == null || signData.trim().isEmpty()) {
                return Result.error("请输入签名值");
            }
            
            return CertUtils.verifyRSACert(certFile, certPwd, originalText, signData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("验签失败：" + e.getMessage());
        }
    }
}

