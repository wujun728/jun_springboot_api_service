package com.zapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户证书信息实体类
 */
@Data
@TableName("sys_user_cert_info")
public class UserCertInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 证书地址(关联文件表fileId)
     */
    @TableField("cert_url")
    private Long certUrl;

    /**
     * 证书密码
     */
    @TableField("cert_pwd")
    private String certPwd;

    /**
     * 证书位数
     */
    @TableField("cert_num")
    private Integer certNum;

    /**
     * C项 国家
     */
    @TableField("cert_c")
    private String certC;

    /**
     * ST项 省份
     */
    @TableField("cert_st")
    private String certSt;

    /**
     * L项 城市
     */
    @TableField("cert_l")
    private String certL;

    /**
     * O项 公用信息/O 单位统一社会信用代码、个人身份证
     */
    @TableField("cert_o")
    private String certO;

    /**
     * CN项 公用名/CN
     */
    @TableField("cert_cn")
    private String certCn;

    /**
     * OU项 部门/OU
     */
    @TableField("cert_ou")
    private String certOu;

    /**
     * 证书类型：SM2/RSA
     */
    @TableField("cert_type")
    private String certType;

    /**
     * 公钥
     */
    @TableField("cert_public_key")
    private byte[] certPublicKey;

    /**
     * 证书有效期(单位/天)
     */
    @TableField("cert_valid_time")
    private Integer certValidTime;

    /**
     * 证书生成时间
     */
    @TableField("cert_not_before")
    private LocalDateTime certNotBefore;

    /**
     * 证书失效时间
     */
    @TableField("cert_not_after")
    private LocalDateTime certNotAfter;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

