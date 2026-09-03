package com.zapi.workflow.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workflow.service.model.dto.ProcessInstanceQueryDTO;
import com.zapi.workflow.service.model.dto.ProcessInstanceDisplayDTO;
import com.zapi.workflow.service.model.dto.ProcessInstanceDetailDTO;
import com.zapi.workflow.service.model.dto.StartProcessDTO;
import com.zapi.workflow.service.model.dto.RevokeProcessDTO;
import com.zapi.workflow.service.model.entity.WfProcessInstance;

import java.util.Map;

/**
 * 流程实例服务接口
 * 负责流程运行时的管理，包括启动、暂停、恢复、结束等生命周期管理
 * 流程实例是流程定义的具体执行，包含运行时状态和变量
 */
public interface WfProcessInstanceService extends IService<WfProcessInstance> {
    
    /**
     * 启动流程实例
     * 根据流程定义创建新的流程实例并开始执行
     * 从开始节点开始，自动推进到第一个需要人工处理的节点
     *
     * @param startProcessDTO 启动流程请求参数，包含流程定义ID、发起人、初始变量等
     * @return 创建的流程实例，包含实例ID、当前节点等信息
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程定义不存在或状态错误时抛出
     */
    WfProcessInstance startProcess(StartProcessDTO startProcessDTO);
    
    /**
     * 结束流程实例
     * 强制结束正在运行的流程实例
     * 通常用于异常处理或管理员干预
     *
     * @param instanceId 流程实例ID
     * @param endNodeId 结束节点ID
     * @param operator 操作人
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程实例不存在时抛出
     */
    void endProcess(String instanceId, String endNodeId, String operator);
    
    /**
     * 暂停流程实例
     * 暂停正在运行的流程实例，暂停后不能继续执行
     * 可以通过activateProcess方法恢复执行
     *
     * @param instanceId 流程实例ID
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程实例不存在时抛出
     */
    void suspendProcess(String instanceId);
    
    /**
     * 激活流程实例
     * 恢复被暂停的流程实例，使其可以继续执行
     *
     * @param instanceId 流程实例ID
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程实例不存在时抛出
     */
    void activateProcess(String instanceId);
    
    /**
     * 更新流程变量
     * 更新流程实例的变量值，新变量会与现有变量合并
     * 更新后的变量可用于后续的条件判断和任务分配
     *
     * @param instanceId 流程实例ID
     * @param variables 要更新的变量键值对
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程实例不存在或变量序列化失败时抛出
     */
    void updateVariables(String instanceId, Map<String, Object> variables);
    
    /**
     * 分页查询流程实例
     * 支持按多种条件组合查询流程实例列表
     * 包含流程定义信息的关联查询
     *
     * @param page 分页参数
     * @param queryDTO 查询条件，包含流程状态、发起人、业务key等
     * @return 分页的流程实例列表
     */
    IPage<WfProcessInstance> pageQuery(Page<WfProcessInstance> page, ProcessInstanceQueryDTO queryDTO);
    
    /**
     * 分页查询流程实例（增强显示信息）
     * 返回包含流程定义名称、表单信息、用户名称等完整显示信息的DTO
     * 用于任务列表展示，减少前端多次接口调用
     *
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页的流程实例显示DTO列表
     */
    IPage<ProcessInstanceDisplayDTO> pageQueryWithDisplayInfo(Page<ProcessInstanceDisplayDTO> page, ProcessInstanceQueryDTO queryDTO);
    
    /**
     * 获取流程实例完整详情
     * 一次性返回详情页面需要的所有数据，包括：
     * - 流程实例基本信息
     * - 流程定义信息（包含变量定义）
     * - 任务历史记录（包含审批记录）
     * - 流程图数据
     * 避免前端多次接口调用，提升用户体验
     *
     * @param instanceId 流程实例ID
     * @return 完整的流程实例详情DTO
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程实例不存在时抛出
     */
    ProcessInstanceDetailDTO getCompleteDetail(String instanceId);
    
    /**
     * 撤回流程实例
     * 发起人可以撤回尚未结束的流程实例
     * 撤回后流程实例状态变为“已撤回”，不能再继续执行
     *
     * @param revokeProcessDTO 撤回请求参数，包含实例ID、撤回原因、操作人
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程实例不存在、已结束或操作人不是发起人时抛出
     */
    void revokeProcess(RevokeProcessDTO revokeProcessDTO);
    
    /**
     * 获取流程实例的版本记录ID
     * 根据流程实例ID获取关联的流程定义版本记录ID
     *
     * @param instanceId 流程实例ID
     * @return 流程定义版本记录ID，如果不存在则返回null
     */
    String getProcessDefVersionId(String instanceId);
}
    