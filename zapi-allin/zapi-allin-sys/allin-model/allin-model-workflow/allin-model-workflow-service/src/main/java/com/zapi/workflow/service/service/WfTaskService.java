package com.zapi.workflow.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workflow.service.model.dto.CompleteTaskDTO;
import com.zapi.workflow.service.model.dto.TaskDisplayDTO;
import com.zapi.workflow.service.model.entity.WfTask;

/**
 * 工作流任务服务接口
 * 负责审批任务的管理，包括任务创建、完成、查询等功能
 * 任务是审批节点产生的待办事项，需要指定人员处理
 */
public interface WfTaskService extends IService<WfTask> {
    
    /**
     * 完成任务
     * 处理审批任务，包括通过、驳回等操作
     * 任务完成后会自动推进流程到下一个节点
     *
     * @param completeTaskDTO 完成任务请求参数，包含任务ID、处理结果、审批意见等
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当任务不存在或状态错误时抛出
     */
    void completeTask(CompleteTaskDTO completeTaskDTO);
    
    /**
     * 查询用户待办任务
     * 分页查询指定用户的待处理任务列表
     * 只返回状态为"待处理"的任务
     *
     * @param page 分页参数
     * @param assignee 任务处理人（用户ID或用户名）
     * @param appId 应用ID（可选，如果传递则查询该应用的记录，否则查询app_id为NULL的通用记录）
     * @return 分页的任务列表
     */
    IPage<WfTask> getUserTasks(Page<WfTask> page, String assignee, String appId);
    
    /**
     * 查询用户待办任务（增强显示信息）
     * 返回包含流程定义名称、表单信息、发起人名称等完整显示信息的DTO
     * 用于待办任务列表展示，减少前端多次接口调用
     *
     * @param page 分页参数
     * @param assignee 任务处理人（用户ID或用户名）
     * @param appId 应用ID（可选，如果传递则查询该应用的记录，否则查询app_id为NULL的通用记录）
     * @return 分页的任务显示DTO列表
     */
    IPage<TaskDisplayDTO> getUserTasksWithDisplayInfo(Page<TaskDisplayDTO> page, String assignee, String appId);

    /**
     * 取消流程实例的所有待办任务
     * 用于流程撤回时取消当前待处理的任务
     *
     * @param instanceId 流程实例ID
     */
    void cancelTasksByInstanceId(String instanceId);
}