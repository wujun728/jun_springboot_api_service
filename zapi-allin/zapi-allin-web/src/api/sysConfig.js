/**
 * 系统配置管理相关API
 */

import api from './request'

/**
 * 系统配置API
 */
export const sysConfigApi = {
  // 获取参数配置列表（分页）
  getConfigList: (params) => {
    return api.get('/system/config/list', { params })
  },

  // 根据ID获取参数配置详情
  getConfig: (configId) => {
    return api.get(`/system/config/${configId}`)
  },

  // 根据参数键名查询参数配置
  getConfigByKey: (configKey) => {
    return api.get(`/system/config/key/${configKey}`)
  },

  // 根据参数键名获取参数键值
  getConfigValueByKey: (configKey) => {
    return api.get(`/system/config/value/${configKey}`)
  },

  // 新增参数配置
  addConfig: (data) => {
    return api.post('/system/config/add', data)
  },

  // 修改参数配置
  updateConfig: (data) => {
    return api.put('/system/config/update', data)
  },

  // 删除参数配置
  deleteConfig: (configIds) => {
    return api.delete(`/system/config/delete/${configIds}`)
  },

  // 刷新参数缓存
  refreshCache: () => {
    return api.delete('/system/config/refreshCache')
  }
}

export default {
  sysConfigApi
}