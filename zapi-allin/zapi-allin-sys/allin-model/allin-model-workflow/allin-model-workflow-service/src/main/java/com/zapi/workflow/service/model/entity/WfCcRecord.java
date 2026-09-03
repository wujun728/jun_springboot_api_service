package com.zapi.workflow.service.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程抄送节点记录实体类
 * 对应数据库表：wf_cc_record
 * 
 * 设计思路：一个抄送节点执行一次 = 一条记录
 * 抄送目标（用户/部门/角色）以JSON格式存储，不展开为多条记录
 */
@Data
@TableName("wf_cc_record")
public class WfCcRecord {
    
    /**
     * 抄送记录ID（主键，UUID格式）
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
     * 抄送节点ID
     */
    private String nodeId;
    
    /**
     * 抄送节点名称
     */
    private String nodeName;
    
    /**
     * 抄送目标配置（JSON格式）
     * 格式: [{"id":1,"type":"user","name":"张三"},{"id":2,"type":"dept","name":"研发部"},{"id":3,"type":"role","name":"管理员"}]
     */
    private String ccTargets;
    
    /**
     * 展开后的实际抄送用户ID列表（JSON数组）
     * 格式: [1,2,3,4,5]
     * 用于快速查询"抄送我的"
     */
    private String ccUserIds;
    
    /**
     * 展开后的实际抄送用户姓名（逗号分隔，用于显示）
     * 格式: "张三、李四、王五"
     */
    private String ccUserNames;
    
    /**
     * 通知方式：SYSTEM/EMAIL/SMS/ALL
     */
    private String notifyType;
    
    /**
     * 是否需要确认阅读
     */
    private Boolean requireRead;
    
    /**
     * 抄送时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime ccTime;
    
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
