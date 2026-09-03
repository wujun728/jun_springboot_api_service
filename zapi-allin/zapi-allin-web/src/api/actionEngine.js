/**
 * 动作执行引擎相关API
 */

import api from './request'

export const actionEngineApi = {
  // 测试接口
  test: () => {
    return api.get('/api/action-engine/test')
  },

  // 创建动作
  createAction: (actionData) => {
    return api.post('/api/action-engine', actionData)
  },

  // 更新动作
  updateAction: (actionData) => {
    return api.put('/api/action-engine', actionData)
  },

  // 删除动作
  deleteAction: (actionId) => {
    return api.delete(`/api/action-engine/${actionId}`)
  },

  // 获取动作详情
  getActionDetail: (actionId) => {
    return api.get(`/api/action-engine/${actionId}`)
  },

  // 分页查询动作列表
  getActionList: (params) => {
    return api.get('/api/action-engine/list', {
      params: {
        appId: params.appId,
        current: params.current || 1,
        size: params.size || 10,
        actionName: params.actionName,
        actionType: params.actionType,
        status: params.status
      }
    })
  },

  // 启用/禁用动作
  changeActionStatus: (actionId, status) => {
    return api.put(`/api/action-engine/${actionId}/status`, null, {
      params: { status }
    })
  },

  // 执行动作
  executeAction: (actionId, params) => {
    return api.post(`/api/action-engine/${actionId}/execute`, params)
  }
}

export default actionEngineApi
