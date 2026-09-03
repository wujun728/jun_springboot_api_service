/**
 * 用户印章相关API
 */

import api from './request'

export const sealApi = {
  // 分页查询用户印章
  pageUserSeals: (params) => {
    return api.get('/system/seal/page', { params })
  },

  // 获取用户所有印章
  getUserSeals: (userId) => {
    return api.get(`/system/seal/user/${userId}`)
  },

  // 获取用户启用的印章
  getEnabledSeals: (userId) => {
    return api.get(`/system/seal/user/${userId}/enabled`)
  },

  // 根据ID获取印章详情
  getSealById: (id) => {
    return api.get(`/system/seal/${id}`)
  },

  // 创建印章
  createSeal: (data) => {
    return api.post('/system/seal/create', data)
  },

  // 更新印章信息
  updateSeal: (id, data) => {
    return api.put(`/system/seal/${id}`, data)
  },

  // 更新印章状态
  updateStatus: (id, status) => {
    return api.put(`/system/seal/${id}/status`, null, { params: { status } })
  },

  // 更新印章排序
  updateSortOrder: (id, sortOrder) => {
    return api.put(`/system/seal/${id}/sort`, null, { params: { sortOrder } })
  },

  // 删除印章
  deleteSeal: (id) => {
    return api.delete(`/system/seal/${id}`)
  },

  // 批量删除印章
  batchDelete: (ids) => {
    return api.delete('/system/seal/batch', { data: ids })
  }
}

export default sealApi

