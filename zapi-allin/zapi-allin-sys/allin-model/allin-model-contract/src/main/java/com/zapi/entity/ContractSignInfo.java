package com.zapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 合同签署信息实体类
 * 用于存储合同签署的详细信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("contract_sign_info")
public class ContractSignInfo {
    
    /**
     * 唯一标识
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 签署前的文件（关联文件uid）
     */
    private Long signFrontFileId;
    
    /**
     * 签署后的文件（关联文件uid）
     */
    private Long signBackFileId;
    
    /**
     * 印章id（关联印章信息表id）
     */
    private Long sealId;
    
    /**
     * 证书id（关联证书信息表id）
     */
    private Long certId;
    
    /**
     * 签署签名算法（常见的RSA，国办SM2）
     */
    private String signAlgorithm;
    
    /**
     * pdf签署页码
     */
    private Integer signPage;
    
    /**
     * pdf总页数
     */
    private Integer pdfPageCount;
    
    /**
     * pdf宽度
     */
    private Integer pdfWidth;
    
    /**
     * pdf高度
     */
    private Integer pdfHeight;
    
    /**
     * 坐标x
     */
    private Integer coordX;
    
    /**
     * 坐标y
     */
    private Integer coordY;
    
    /**
     * 像素大小（数值表示例如：100，120，表示宽100px，高120）
     */
    private String pixelSize;
    
    /**
     * 签署时间
     */
    private Date signTime;
    
    /**
     * 创建时间
     */
    private Date createTime;
}
