package com.zapi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同信息实体类
 */
@Data
@TableName("contract_info")
public class ContractInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 合同编号
     */
    @TableField("contract_no")
    private String contractNo;

    /**
     * 合同名称
     */
    @TableField("contract_name")
    private String contractName;

    /**
     * 合同类型
     */
    @TableField("contract_type")
    private String contractType;

    /**
     * 合同文件ID（关联文件表）
     */
    @TableField("file_id")
    private Long fileId;

    /**
     * 发起人ID（用户ID）
     */
    @TableField("initiator_id")
    private Long initiatorId;

    /**
     * 发起人姓名
     */
    @TableField("initiator_name")
    private String initiatorName;

    /**
     * 合同状态：DRAFT-草稿，REVIEWING-审核中，PENDING_SIGN-待签署，SIGNED-签署完成，ARCHIVED-归档
     */
    @TableField("contract_status")
    private String contractStatus;

    /**
     * 合同金额
     */
    @TableField("contract_amount")
    private BigDecimal contractAmount;

    /**
     * 合同开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 审核人ID
     */
    @TableField("reviewer_id")
    private Long reviewerId;

    /**
     * 审核人姓名
     */
    @TableField("reviewer_name")
    private String reviewerName;

    /**
     * 审核时间
     */
    @TableField("review_time")
    private LocalDateTime reviewTime;

    /**
     * 审核意见
     */
    @TableField("review_comment")
    private String reviewComment;

    /**
     * 签署人ID
     */
    @TableField("signer_id")
    private Long signerId;

    /**
     * 签署人姓名
     */
    @TableField("signer_name")
    private String signerName;

    /**
     * 签署时间
     */
    @TableField("sign_time")
    private LocalDateTime signTime;

    /**
     * 归档时间
     */
    @TableField("archive_time")
    private LocalDateTime archiveTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

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

    /**
     * 合同状态枚举
     */
    public static class Status {
        public static final String DRAFT = "DRAFT";           // 草稿
        public static final String REVIEWING = "REVIEWING";   // 审核中
        public static final String PENDING_SIGN = "PENDING_SIGN"; // 待签署
        public static final String SIGNED = "SIGNED";         // 签署完成
        public static final String ARCHIVED = "ARCHIVED";      // 归档
    }
}

