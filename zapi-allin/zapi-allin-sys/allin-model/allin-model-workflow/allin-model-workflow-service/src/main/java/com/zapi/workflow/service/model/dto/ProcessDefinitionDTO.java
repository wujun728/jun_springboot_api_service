package com.zapi.workflow.service.model.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 流程定义传输对象
 * 用于前端和后端之间传递流程定义的完整结构信息
 * 包含节点、连线、变量等所有流程元素
 */
@Data
public class ProcessDefinitionDTO {
    
    /**
     * 流程定义ID
     * 可选字段，新建时为空，编辑时必填
     * 对应wf_process_definition.id
     */
    private String id;
    
    /**
     * 流程名称
     * 必填字段，用于显示的流程名称
     * 例如：请假审批流程、采购审批流程
     */
    @NotBlank(message = "流程名称不能为空")
    private String name;
    
    /**
     * 流程标识
     * 必填字段，流程的唯一标识符
     * 用于程序中引用，例如：LEAVE_APPROVAL、PURCHASE_APPROVAL
     * 同一个key可以有多个version
     */
    @NotBlank(message = "流程标识不能为空")
    private String key;
    
    /**
     * 流程描述
     * 可选字段，详细描述流程的用途和功能
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
     * 流程变量定义列表
     * 可选字段，定义此流程支持的变量
     * 包含变量名称、类型、默认值等信息
     * 用于流程设计器的变量管理
     */
    private List<VariableDTO> variables;
    
    /**
     * 节点列表
     * 必填字段，流程的所有节点
     * 包含开始节点、审批节点、条件分支节点、结束节点等
     * 至少包含一个开始节点和一个结束节点
     */
    @NotNull(message = "节点列表不能为空")
    private List<NodeDTO> nodes;
    
    /**
     * 连线列表
     * 必填字段，连接各个节点的流向线
     * 定义流程的执行路径和条件
     * 每条连线包含起始节点、目标节点和执行条件
     */
    @NotNull(message = "连线列表不能为空")
    private List<FlowDTO> flows;
    
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
     * 对应form表的主键ID
     */
    private String businessFormId;
    
    /**
     * 系统表单标识
     * 当businessType为1（系统表单）时使用
     * 用于标识系统内置的表单类型
     */
    private String systemFormKey;
    
    /**
     * 业务视图配置
     * 根据 businessType 存储不同内容：
     * - businessType=1（系统表单）：存储视图地址
     * - businessType=2（业务表单）：存储完整的表单配置JSON
     */
    private String businessView;
    
    /**
     * 流程优先级
     * 0：普通，1：高，2：中，3：低
     * 默认值为0（普通）
     */
    private Integer priority;
}
    