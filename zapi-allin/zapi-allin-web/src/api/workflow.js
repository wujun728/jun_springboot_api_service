/**
 * 工作流管理相关API
 */

import api from './request'

export const workflowApi = {
  // 部署流程定义
  deployProcess: (processData) => {
    return api.post('/api/workflow/deploy', processData)
  },

  // 更新流程定义
  updateProcessDefinition: (definitionId, processData) => {
    return api.put(`/api/workflow/definitions/${definitionId}`, processData)
  },

  // 删除流程定义
  deleteProcessDefinition: (definitionId) => {
    return api.delete(`/api/workflow/definitions/${definitionId}`)
  },

  // 获取流程定义详情
  getProcessDefinition: (definitionId) => {
    return api.get(`/api/workflow/definitions/${definitionId}`)
  },

  // 更新流程定义状态
  updateProcessStatus: (definitionId, status) => {
    return api.put(`/api/workflow/definitions/${definitionId}/status`, null, {
      params: { status }
    })
  },

  // 分页查询流程定义列表
  getWorkflowList: (params) => {
    return api.get('/api/workflow/definitions', {
      params: {
        pageNum: params.pageNum || params.current || 1,
        pageSize: params.pageSize || params.size || 10,
        name: params.name || params.workflowName,
        key: params.key,
        status: params.status,
        businessType: params.businessType,
        appId: params.appId // 添加应用ID参数
      }
    })
  },

  // 获取已绑定的业务表单ID列表
  getBoundFormIds: () => {
    return api.get('/api/workflow/definitions/bound-forms')
  },

  // 发布/取消发布工作流
  changeWorkflowStatus: (workflowId, status) => {
    return api.put(`/api/workflow/definitions/${workflowId}/status`, null, {
      params: { status }
    })
  },

  // 删除工作流
  deleteWorkflow: (workflowId) => {
    return api.delete(`/api/workflow/definitions/${workflowId}`)
  },

  // 获取工作流详情
  getWorkflowDetail: (workflowId) => {
    return api.get(`/api/workflow/definitions/${workflowId}`)
  },

  // 完成任务
  completeTask: (taskId, completeTaskDTO) => {
    return api.post(`/api/workflow/tasks/${taskId}/complete`, completeTaskDTO)
  },

  // 获取用户任务列表
  getUserTasks: (params) => {
    return api.get('/api/workflow/tasks', {
      params: {
        pageNum: params.pageNum || params.current || 1,
        pageSize: params.pageSize || params.size || 10,
        assignee: params.assignee
      }
    })
  },

  // 获取用户任务列表（增强显示信息）
  getUserTasksWithDisplayInfo: (params) => {
    return api.get('/api/workflow/tasks/display', {
      params: {
        pageNum: params.pageNum || params.current || 1,
        pageSize: params.pageSize || params.size || 10,
        assignee: params.assignee,
        appId: params.appId // 添加应用ID参数
      }
    })
  },

  // 获取用户抄送记录
  getUserCcRecords: (params) => {
    return api.get('/api/workflow/cc/records', {
      params: {
        pageNum: params.pageNum || params.current || 1,
        pageSize: params.pageSize || params.size || 10,
        ccUser: params.userId || params.ccUser,
        appId: params.appId // 添加应用ID参数
      }
    })
  },

  // 标记抄送记录为已读
  markCcRecordAsRead: (recordId, userId) => {
    return api.post(`/api/workflow/cc/read/${recordId}`, null, {
      params: { userId }
    })
  },

  // 批量标记抄送记录为已读
  batchMarkCcRecordsAsRead: (recordIds, userId) => {
    return api.post('/api/workflow/cc/read/batch', recordIds, {
      params: { userId }
    })
  },

  // 获取未读抄送数量
  getUnreadCcCount: (userId) => {
    return api.get('/api/workflow/cc/unread/count', {
      params: { userId }
    })
  },

  // 获取流程实例的抄送记录
  getCcRecordsByInstance: (instanceId) => {
    return api.get(`/api/workflow/cc/instance/${instanceId}`)
  },

  // 获取用户已办任务
  getUserCompletedTasks: (params) => {
    return api.get('/api/workflow/completed/tasks', {
      params: {
        pageNum: params.pageNum || params.current || 1,
        pageSize: params.pageSize || params.size || 10,
        operator: params.operator,
        appId: params.appId // 添加应用ID参数
      }
    })
  }
}

// 工作流实例相关API
export const workflowInstanceApi = {
  // 分页查询流程实例列表
  getProcessInstances: (params) => {
    return api.get('/api/workflow/instances', {
      params: {
        pageNum: params.pageNum || params.current || 1,
        pageSize: params.pageSize || params.size || 10,
        processDefId: params.processDefId,
        processKey: params.processKey,
        businessKey: params.businessKey,
        status: params.status,
        startUser: params.startUser,
        processName: params.processName
      }
    })
  },

  // 分页查询流程实例列表（增强显示信息）
  getProcessInstancesWithDisplayInfo: (params) => {
    return api.get('/api/workflow/instances/display', {
      params: {
        pageNum: params.pageNum || params.current || 1,
        pageSize: params.pageSize || params.size || 10,
        processDefId: params.processDefId,
        processKey: params.processKey,
        businessKey: params.businessKey,
        status: params.status,
        startUser: params.startUser,
        currentNodeId: params.currentNodeId,
        priority: params.priority,
        appId: params.appId // 添加应用ID参数
      }
    })
  },

  // 获取流程实例详情
  getProcessInstanceDetail: (instanceId) => {
    return api.get(`/api/workflow/instances/${instanceId}`)
  },

  // 获取流程图数据（包含流程定义和执行状态）
  getProcessDiagram: (instanceId) => {
    return api.get(`/api/workflow/instances/${instanceId}/diagram`)
  },

  // 获取流程实例历史活动
  getInstanceActivities: (instanceId) => {
    return api.get(`/api/workflow/instances/${instanceId}/activities`)
  },

  // 获取流程实例完整详情（一次性获取所有数据）
  getProcessInstanceCompleteDetail: (instanceId) => {
    return api.get(`/api/workflow/instances/${instanceId}/complete-detail`)
  },

  // 启动流程实例
  startProcessInstance: (definitionId, data) => {
    return api.post(`/api/workflow/processes/${definitionId}/start`, data)
  },

  // 终止流程实例（暂未实现后端接口）
  terminateProcessInstance: (instanceId) => {
    return api.delete(`/api/workflow/instances/${instanceId}`)
  },

  // 暂停流程实例（暂未实现后端接口）
  suspendProcessInstance: (instanceId) => {
    return api.put(`/api/workflow/instances/${instanceId}/suspend`)
  },

  // 恢复流程实例（暂未实现后端接口）
  resumeProcessInstance: (instanceId) => {
    return api.put(`/api/workflow/instances/${instanceId}/resume`)
  },
  
  // 撤回流程实例
  revokeProcessInstance: (instanceId, data) => {
    return api.post(`/api/workflow/instances/${instanceId}/revoke`, data)
  }
}

// 工作流模板相关API
export const workflowTemplateApi = {
  // 获取工作流模板列表
  getWorkflowTemplates: (params) => {
    return api.get('/api/workflow/templates', {
      params: {
        current: params.current || 1,
        size: params.size || 10,
        templateName: params.templateName,
        category: params.category
      }
    })
  },

  // 获取工作流模板详情
  getWorkflowTemplateDetail: (templateId) => {
    return api.get(`/api/workflow/templates/${templateId}`)
  },

  // 创建工作流模板
  createWorkflowTemplate: (templateData) => {
    return api.post('/api/workflow/templates', templateData)
  },

  // 更新工作流模板
  updateWorkflowTemplate: (templateData) => {
    return api.put('/api/workflow/templates', templateData)
  },

  // 删除工作流模板
  deleteWorkflowTemplate: (templateId) => {
    return api.delete(`/api/workflow/templates/${templateId}`)
  }
}

export default workflowApi
