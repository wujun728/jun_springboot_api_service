/**
 * 合同管理相关API
 */

import api from './request'

export const contractApi = {
  // 分页查询合同信息
  pageContracts: (params) => {
    return api.get('/contract/info/page', { params })
  },

  // 根据ID获取合同详情
  getContractById: (id) => {
    return api.get(`/contract/info/${id}`)
  },

  // 创建合同
  createContract: (data) => {
    return api.post('/contract/info/create', data)
  },

  // 更新合同
  updateContract: (id, data) => {
    return api.put(`/contract/info/${id}`, data)
  },

  // 提交审核
  submitForReview: (id, data) => {
    return api.post(`/contract/info/${id}/submit`, data)
  },

  // 审核合同
  reviewContract: (id, data) => {
    return api.post(`/contract/info/${id}/review`, data)
  },

  // 签署合同
  signContract: (id) => {
    return api.post(`/contract/info/${id}/sign`)
  },

  // 归档合同
  archiveContract: (id) => {
    return api.post(`/contract/info/${id}/archive`)
  },

  // 删除合同
  deleteContract: (id) => {
    return api.delete(`/contract/info/${id}`)
  },

  // 批量删除合同
  batchDelete: (ids) => {
    return api.delete('/contract/info/batch', { data: ids })
  },

  // 盖章签名
  stampSeal: (contractId, data) => {
    return api.post(`/contract/seal-sign/${contractId}/stamp-seal`, data)
  }
}

export default contractApi
