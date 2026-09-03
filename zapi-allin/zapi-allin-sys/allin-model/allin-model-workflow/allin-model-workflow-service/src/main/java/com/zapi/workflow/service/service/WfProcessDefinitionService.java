package com.zapi.workflow.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zapi.workflow.service.model.dto.ProcessDefinitionDTO;
import com.zapi.workflow.service.model.dto.ProcessDefinitionQueryDTO;
import com.zapi.workflow.service.model.entity.WfProcessDefinition;

/**
 * 流程定义服务接口
 * 负责流程模板的管理，包括部署、查询、状态控制等功能
 * 流程定义是流程实例的模板，定义了流程的结构和规则
 */
public interface WfProcessDefinitionService extends IService<WfProcessDefinition> {
    
    /**
     * 部署流程定义
     * 将流程设计器生成的JSON格式流程定义保存到数据库
     * 如果是新流程，version从1开始；如果是已存在流程的新版本，version递增
     *
     * @param definitionDTO 流程定义传输对象，包含完整的流程结构
     * @return 保存后的流程定义实体，包含生成的ID和版本号
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程定义格式错误时抛出
     */
    WfProcessDefinition deploy(ProcessDefinitionDTO definitionDTO);
    
    /**
     * 根据ID获取流程定义DTO
     * 将数据库中的JSON格式流程定义转换为DTO对象
     * 用于流程设计器加载和流程引擎执行
     *
     * @param id 流程定义ID
     * @return 流程定义DTO，包含节点、连线等完整信息
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程定义不存在或JSON解析失败时抛出
     */
    ProcessDefinitionDTO getDefinitionDTOById(String id);
    
    /**
     * 更新流程定义
     * 更新现有流程定义的完整信息，包括流程结构和基本信息
     * 会校验流程定义的合法性，并更新版本号
     *
     * @param definitionDTO 更新的流程定义数据
     * @return 更新后的流程定义实体
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程定义不存在或格式错误时抛出
     */
    WfProcessDefinition updateDefinition(ProcessDefinitionDTO definitionDTO);
    
    /**
     * 更新流程定义状态
     * 控制流程定义的启用/禁用状态
     * 禁用的流程定义不能启动新的流程实例
     *
     * @param id 流程定义ID
     * @param status 新状态值（1-激活，0-禁用）
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当流程定义不存在时抛出
     */
    void updateStatus(String id, Integer status);
    
    /**
     * 分页查询流程定义
     * 支持按名称、标识、状态、业务类型等条件进行筛选
     * 结果按创建时间倒序排列
     *
     * @param page 分页参数
     * @param queryDTO 查询条件
     * @return 分页结果，包含流程定义列表和分页信息
     */
    IPage<WfProcessDefinition> pageQuery(Page<WfProcessDefinition> page, ProcessDefinitionQueryDTO queryDTO);
    
    /**
     * 根据业务类型和表单标识查询流程定义
     * 支持系统表单和业务表单两种查询方式，可选择性地根据流程状态进行过滤
     * 
     * @param businessType 业务表单类型（1-系统表单，2-业务表单）
     * @param systemFormKey 系统表单标识（当businessType为1时使用）
     * @param businessFormId 业务表单ID（当businessType为2时使用）
     * @param status 流程状态（可选，1-激活，0-禁用，null-不限制）
     * @return 匹配条件的流程定义，如果没有找到则返回null
     * @throws com.zapi.workflow.service.common.exception.WorkflowException 当参数不合法时抛出
     */
    WfProcessDefinition getByBusinessTypeAndForm(Integer businessType, String systemFormKey, String businessFormId, Integer status);
    
    /**
     * 获取所有已绑定的业务表单ID列表
     * 返回所有被业务表单类型的流程定义所使用的表单ID
     * 
     * @return 已绑定的业务表单ID列表
     */
    java.util.List<String> getBoundFormIds();
}
    