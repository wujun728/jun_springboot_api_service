package com.zapi.workflow.service.model.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 流程实例查询传输对象
 * 用于分页查询流程实例的条件参数
 */
@Data
public class ProcessInstanceQueryDTO {
    
    /**
     * 页码
     * 默认第1页，最小值为1
     */
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     * 默认10条，最小1条，最大100条
     */
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer pageSize = 10;
    
    /**
     * 流程定义ID
     * 可选，用于查询特定流程定义的实例
     */
    private String processDefId;
    
    /**
     * 流程标识
     * 可选，用于按流程key模糊查询
     * 例如：LEAVE_APPROVAL、PURCHASE_APPROVAL
     */
    private String processKey;
    
    /**
     * 业务关联ID
     * 可选，用于查询特定业务数据关联的流程实例
     * 例如：订单ID、申请单ID等
     */
    private String businessKey;
    
    /**
     * 流程实例状态
     * 可选，用于按状态过滤
     * 1：运行中，2：已结束，3：已暂停
     */
    private Integer status;
    
    /**
     * 流程发起人
     * 可选，用于查询特定用户发起的流程实例
     */
    private String startUser;
    
    /**
     * 应用ID
     * 可选，用于按应用过滤流程实例
     * 实现多应用隔离
     */
    private String appId;
    
    /**
     * 流程名称
     * 可选，用于按流程名称模糊查询
     * 通过关联流程定义表查询
     */
    private String processName;
}
