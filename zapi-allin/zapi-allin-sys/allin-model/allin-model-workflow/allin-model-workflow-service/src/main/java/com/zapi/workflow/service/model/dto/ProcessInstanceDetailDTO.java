package com.zapi.workflow.service.model.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程实例完整详情DTO - 包含所有详情页面需要的数据
 * 避免前端多次调用接口
 */
@Data
public class ProcessInstanceDetailDTO {
    
    // ==================== 基本实例信息 ====================
    private String id;
    private String processDefId;
    private String processDefVersionId; // 流程定义版本记录ID
    private String processKey;
    private String businessKey;
    private Integer status; // 0-运行中, 1-挂起, 2-完成, 3-终止
    private String startUser;
    private String startUserName; // 发起人姓名
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String variables; // 业务变量JSON字符串
    
    // ==================== 流程定义信息 ====================
    private ProcessDefinitionInfo processDefinition;
    
    // ==================== 任务历史记录 ====================
    private List<TaskHistoryInfo> taskHistory;
    
    // ==================== 流程图数据 ====================
    private ProcessDiagramInfo diagramData;
    
    /**
     * 流程定义信息
     */
    @Data
    public static class ProcessDefinitionInfo {
        private String id;
        private String name;
        private String key;
        private String description;
        private Integer businessType; // 1-系统表单, 2-业务表单
        private String systemFormKey;
        private String businessFormId;
        private String businessView; // 业务视图配置：系统表单时为视图地址，业务表单时为formConfig JSON
        private Integer priority;
        private String jsonDef; // 流程定义JSON
        private List<VariableInfo> variables; // 变量定义
    }
    
    /**
     * 变量定义信息
     */
    @Data
    public static class VariableInfo {
        private String name;
        private String type;
        private String label;
        private String defaultValue;
        private Boolean required;
    }
    
    /**
     * 任务历史记录信息
     */
    @Data
    public static class TaskHistoryInfo {
        private String id;
        private String taskId;
        private String nodeId;
        private String nodeName;
        private String nodeType; // START, END, APPROVAL, TASK, etc.
        private String assignee;
        private String assigneeName; // 处理人姓名
        private String operator;
        private String operatorName; // 实际操作人姓名
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Long duration; // 耗时（毫秒）
        private String comment; // 处理意见
        private Integer status; // 0-待处理, 1-已完成, 2-已取消
        private String approvalUserNames; // 审批人名称列表（多人审批时使用，如"董事长、张三、李四"）
        private String approvalMode; // 审批方式：ANY-或签，ALL-会签
    }
    
    /**
     * 流程图数据信息
     */
    @Data
    public static class ProcessDiagramInfo {
        private String processDefId;
        private String processDefName;
        private String jsonDef; // 流程定义JSON
        private Integer status; // 实例状态
        private String currentNodeId; // 当前节点ID
        private List<TaskHistoryInfo> taskHistory; // 执行历史（用于流程图状态显示）
        private ProcessDefinitionInfo processDefinition; // 流程定义信息
    }
}
