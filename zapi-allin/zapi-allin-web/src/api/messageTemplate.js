/**
 * 消息模板相关API
 */

import api from './request'

export const messageTemplateApi = {
  // 分页查询消息模板
  getTemplates: (params) => {
    return api.get('/notification/message-template/list', { params })
  },

  // 获取所有模板
  getAllTemplates: () => {
    return api.get('/notification/message-template/all')
  },

  // 获取模板详情
  getTemplate: (id) => {
    return api.get(`/notification/message-template/${id}`)
  },

  // 添加模板
  addTemplate: (templateData) => {
    return api.post('/notification/message-template/add', templateData)
  },

  // 更新模板
  updateTemplate: (templateData) => {
    return api.put('/notification/message-template/update', templateData)
  },

  // 删除模板
  deleteTemplate: (id) => {
    return api.delete(`/notification/message-template/${id}`)
  },

  // 启用模板
  enableTemplate: (id) => {
    return api.put(`/notification/message-template/enable/${id}`)
  },

  // 禁用模板
  disableTemplate: (id) => {
    return api.put(`/notification/message-template/disable/${id}`)
  },

  // 渲染模板
  renderTemplate: (templateCode, params) => {
    return api.post(`/notification/message-template/render/${templateCode}`, params)
  }
}

export default messageTemplateApi 