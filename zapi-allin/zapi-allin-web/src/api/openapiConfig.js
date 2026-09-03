/**
 * 开放接口-接口配置API
 */

import api from './request'

export const apiConfigApi = {
  // 获取配置列表（分页）
  getConfigList: (params) => {
    return api.get('/openapi/config/list', { params })
  },

  // 根据应用ID获取配置列表
  getConfigsByAppId: (appId) => {
    return api.get(`/openapi/config/byApp/${appId}`)
  },

  // 获取配置详情
  getConfig: (id) => {
    return api.get(`/openapi/config/${id}`)
  },

  // 添加配置
  addConfig: (data) => {
    return api.post('/openapi/config/add', data)
  },

  // 更新配置
  updateConfig: (data) => {
    return api.put('/openapi/config/update', data)
  },

  // 删除配置
  deleteConfig: (id) => {
    return api.delete(`/openapi/config/delete/${id}`)
  },

  // 切换配置状态
  changeStatus: (id, status) => {
    return api.put(`/openapi/config/status/${id}`, null, { params: { status } })
  },

  // 测试配置
  testConfig: (id) => {
    return api.post(`/openapi/sync/test/${id}`)
  },

  // 主动同步 - 手动触发
  pullSync: (id) => {
    return api.post(`/openapi/sync/pull/${id}`)
  },

  // 主动同步 - 通过UUID触发
  pullSyncByUuid: (uuid) => {
    return api.post(`/openapi/sync/pull/uuid/${uuid}`)
  }
}

export const apiLogApi = {
  // 获取日志列表（分页）
  getLogList: (params) => {
    return api.get('/openapi/log/list', { params })
  },

  // 获取日志详情
  getLog: (id) => {
    return api.get(`/openapi/log/${id}`)
  },

  // 清理历史日志
  cleanLogs: (days) => {
    return api.delete('/openapi/log/clean', { params: { days } })
  }
}

export default { apiConfigApi, apiLogApi }
