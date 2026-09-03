package com.zapi.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * 印章盖章请求实体对象
 * 用于封装盖章操作所需的所有信息
 */
@Data
public class StampSealRequest {
    
    // ========== 基础签署信息 ==========
    
    /**
     * 需盖章签署文件ID
     */
    private Long signFileId;

    /***
     *  证书文件ID
     */
    private Long certId;


    /***
     *  印章文件ID
     */
    private Long sealId;

    /**
     * 证书密码
     */
    private String certPwd;

    /**
     * 总页数
     */
    private Integer pdfPageCount;
    
    /**
     * PDF宽度
     */
    private BigDecimal pdfWidth;
    
    /**
     * PDF高度
     */
    private BigDecimal pdfHeight;

    
    /**
     * 盖章文件页码
     */
    private Integer signPage;
    
    /**
     * 盖章位置--x轴
     */
    private BigDecimal pageX;
    
    /**
     * 盖章位置--y轴
     */
    private BigDecimal pageY;


    // ========== 后台查询处理的数据 ==========

    /**
     * 证书信息
     */
    private UserCertInfo userCertInfo;


    /**
     * 签署信息
     */
    private ContractSignInfo contractSignInfo;
    
    /**
     * 证书文件
     */
    private byte[] certFile;
    
    /**
     * 签名私钥
     */
    private PrivateKey privateKey;
    
    /**
     * 证书链
     */
    private Certificate[] chain;
    
    /**
     * 印章文件
     */
    private byte[] sealFile;
    
    /**
     * 签署前文件
     */
    private byte[] signFrontFile;
    
    /**
     * 签署后文件Uid
     */
    private Long signBackFileUid;
    
    /**
     * 签署后文件路径
     */
    private String signBackFile;
    
    
}