package com.zapi.workflow.service.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程定义实体类
 * 对应数据库表：wf_process_definition
 * 存储JSON格式的流程定义信息
 */
@Data
@TableName("wf_process_definition")
public class WfProcessDefinition {
    
    /**
     * 流程定义ID
     * 主键，UUID格式
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    /**
     * 流程名称
     * 用于显示的流程名称，例如：请假审批流程
     */
    @TableField("`name`")
    private String name;
    
    /**
     * 流程标识
     * 用于区分不同版本的流程，例如：LEAVE_APPROVAL
     * 同一个key可以有多个version
     */
    @TableField("`key`")
    private String key;
    
    /**
     * 流程版本号
     * 从1开始递增，默认值为1
     * 用于流程定义的版本管理
     */
    @TableField("`version`")
    private Integer version;
    
    /**
     * JSON格式的流程定义
     * 包含节点(nodes)、连线(flows)等完整的流程结构信息
     * 存储格式：{"nodes": [...], "flows": [...]}
     */
    private String jsonDef;
    
    /**
     * 流程状态
     * 1：激活状态，可以启动新的流程实例
     * 0：禁用状态，不允许启动新的流程实例
     */
    @TableField("`status`")
    private Integer status;
    
    /**
     * 流程描述
     * 可选字段，用于描述流程的用途和功能
     * 最大长度512字符
     */
    private String description;
    
    /**
     * 应用ID
     * 流程所属的应用ID
     * 用于区分不同应用的流程定义
     */
    private String appId;
    
    /**
     * 业务类型
     * 1：系统表单
     * 2：业务表单
     * 默认值为1
     */
    private Integer businessType;
    
    /**
     * 业务表单ID
     * 当businessType为2（业务表单）时使用
     * 最大长度64字符
     */
    private String businessFormId;
    
    /**
     * 系统表单标识
     * 当businessType为1（系统表单）时使用
     * 最大长度100字符
     */
    private String systemFormKey;
    
    /**
     * 业务视图配置
     * 根据 businessType 存储不同内容：
     * - businessType=1（系统表单）：存储视图地址，如：/views/system/leave-form
     * - businessType=2（业务表单）：存储完整的表单配置JSON（formConfig）
     * 示例（业务表单）：{"labelWidth":"120px","labelPosition":"right","components":[...]}
     */
    private String businessView;
    
    /**
     * 流程优先级
     * 0：普通，1：高，2：中，3：低
     * 默认值为0（普通）
     */
    private Integer priority;
    
    /**
     * 创建时间
     * 流程定义创建的时间戳
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     * 流程定义最后修改的时间戳
     * 自动更新
     */
    private LocalDateTime updateTime;
}
    