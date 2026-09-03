package com.zapi.workflow.service.model.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * 启动流程请求DTO
 * 用于启动新的流程实例时传递的参数信息
 */
@Data
public class StartProcessDTO {
    
    /**
     * 流程定义ID
     * 必填字段，对应wf_process_definition.id
     * 标识要启动的流程定义
     */
    @NotBlank(message = "流程定义ID不能为空")
    private String processDefinitionId;
    
    /**
     * 业务关联键
     * 可选字段，关联具体的业务数据
     * 例如：订单号、申请单号等
     * 便于在业务系统中查找对应的流程实例
     */
    private String businessKey;
    
    /**
     * 流程发起人
     * 必填字段，启动此流程的用户ID或用户名
     * 用于权限控制和流程追溯
     */
    @NotBlank(message = "流程发起人不能为空")
    private String startUser;
    
    /**
     * 初始流程变量
     * 可选字段，流程启动时的初始变量
     * 键值对形式，例如：{"amount": 5000, "applicant": "张三", "department": "财务部"}
     * 这些变量可用于后续的条件判断和任务分配
     */
    private Map<String, Object> variables;
}
    