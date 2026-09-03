/**
 * 工作台-应用管理API
 */

import api from './request'

export const wbApplicationApi = {
  // 获取应用列表（分页）
  getApplicationList: (params) => {
    return api.get('/api/workbench/application/list', { params })
  },

  // 获取所有启用的应用
  getAllApplications: () => {
    return api.get('/api/workbench/application/all')
  },

  // 获取应用详情
  getApplication: (id) => {
    return api.get(`/api/workbench/application/${id}`)
  },

  // 添加应用
  addApplication: (data) => {
    return api.post('/api/workbench/application/add', data)
  },

  // 更新应用
  updateApplication: (data) => {
    return api.put('/api/workbench/application/update', data)
  },

  // 删除应用
  deleteApplication: (id) => {
    return api.delete(`/api/workbench/application/delete/${id}`)
  },

  // 重新生成授权码和密码
  regenerateAuth: (id) => {
    return api.post(`/api/workbench/application/regenerate/${id}`)
  },

  // 切换应用状态
  changeStatus: (id, status) => {
    return api.put(`/api/workbench/application/status/${id}`, null, { params: { status } })
  }
}

export default wbApplicationApi
