package com.zapi.workflow.service.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程审批节点记录实体类
 * 对应数据库表：wf_approval_record
 * 
 * 设计思路：一个审批节点执行一次 = 一条记录
 * 审批目标（用户/部门/角色）以JSON格式存储，不展开为多条记录
 */
@Data
@TableName("wf_approval_record")
public class WfApprovalRecord {
    
    /**
     * 审批记录ID（主键，UUID格式）
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    
    /**
     * 流程定义ID
     */
    private String processDefinitionId;
    
    /**
     * 审批节点ID
     */
    private String nodeId;
    
    /**
     * 审批节点名称
     */
    private String nodeName;
    
    /**
     * 审批目标配置（JSON格式）
     * 格式: [{"id":1,"type":"user","name":"张三"},{"id":2,"type":"dept","name":"研发部"}]
     */
    private String approvalTargets;
    
    /**
     * 展开后的实际审批用户ID列表（JSON数组）
     * 格式: ["1","2","3"]
     */
    private String approvalUserIds;
    
    /**
     * 展开后的实际审批用户姓名（顿号分隔，用于显示）
     * 格式: "张三、李四、王五"
     */
    private String approvalUserNames;
    
    /**
     * 审批方式：ANY-任意一人审批/ALL-所有人审批/SEQUENTIAL-依次审批
     */
    private String approvalType;
    
    /**
     * 审批状态：1-进行中 2-已通过 3-已驳回
     */
    private Integer status;
    
    /**
     * 总审批人数
     */
    private Integer totalCount;
    
    /**
     * 已通过审批人数
     */
    private Integer approvedCount;
    
    /**
     * 已驳回审批人数
     */
    private Integer rejectedCount;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除标识
     */
    @TableLogic
    private Integer deleted;
}
