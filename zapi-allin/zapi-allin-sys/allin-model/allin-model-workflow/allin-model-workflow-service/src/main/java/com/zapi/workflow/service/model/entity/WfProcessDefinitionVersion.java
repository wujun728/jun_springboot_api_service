package com.zapi.workflow.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程定义版本记录实体类
 * 对应数据库表：wf_process_definition_version
 * 用于记录流程定义的历史版本信息，确保已启动的流程实例不受流程定义修改影响
 * 每次更新流程定义时，将原版本信息保存到此表中
 */
@Data
@TableName("wf_process_definition_version")
public class WfProcessDefinitionVersion {
    
    /**
     * 版本记录ID
     * 主键，UUID格式
     * 每个版本记录的唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 流程定义ID
     * 关联原始流程定义的ID
     * 外键，指向wf_process_definition.id
     */
    @TableField("process_def_id")
    private String processDefId;
    
    /**
     * 流程名称
     * 保存时的流程名称，用于历史记录展示
     */
    @TableField("`name`")
    private String name;
    
    /**
     * 流程标识
     * 保存时的流程标识，用于区分不同流程
     * 例如：LEAVE_APPROVAL、PURCHASE_APPROVAL
     */
    @TableField("`key`")
    private String key;
    
    /**
     * 流程版本号
     * 保存时的流程版本号
     * 与原始流程定义表中的version字段对应
     */
    @TableField("`version`")
    private Integer version;
    
    /**
     * JSON格式的流程定义
     * 保存时的完整流程定义JSON
     * 包含节点(nodes)、连线(flows)等完整的流程结构信息
     * 存储格式：{"nodes": [...], "flows": [...]}
     */
    @TableField("json_def")
    private String jsonDef;
    
    /**
     * 流程状态
     * 保存时的流程状态
     * 1：激活状态，0：禁用状态
     */
    @TableField("`status`")
    private Integer status;
    
    /**
     * 流程描述
     * 保存时的流程描述信息
     */
    @TableField("`description`")
    private String description;
    
    /**
     * 业务类型
     * 1：系统表单，2：业务表单
     * 与原流程定义表中的业务类型保持一致
     */
    @TableField("business_type")
    private Integer businessType;
    
    /**
     * 业务表单ID
     * 业务表单类型时使用
     */
    @TableField("business_form_id")
    private String businessFormId;
    
    /**
     * 业务视图配置
     * 业务表单时存储formConfig的JSON配置，系统表单时存储视图地址
     */
    @TableField("business_view")
    private String businessView;
    
    /**
     * 系统表单标识
     * 系统表单类型时使用
     */
    @TableField("system_form_key")
    private String systemFormKey;
    
    /**
     * 流程优先级
     * 0-普通 1-高 2-中 3-低
     */
    @TableField("`priority`")
    private Integer priority;
    
    /**
     * 创建时间
     * 版本记录创建的时间戳
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     * 版本记录更新的时间戳
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}