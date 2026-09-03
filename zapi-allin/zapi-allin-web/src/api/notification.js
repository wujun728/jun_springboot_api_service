/**
 * 通知管理相关API
 */

import api from './request'

// 消息管理API
export const messageApi = {
  // 发送消息
  sendMessage: (data) => {
    return api.post('/notification/message/send', data)
  },

  // 批量发送消息
  sendBatchMessages: (data) => {
    return api.post('/notification/message/send/batch', data)
  },

  // 分页查询接收的消息
  getReceivedMessages: (params) => {
    return api.get('/notification/message/received', { params })
  },

  // 分页查询发送的消息
  getSentMessages: (params) => {
    return api.get('/notification/message/sent', { params })
  },

  // 标记消息为已读
  markAsRead: (messageId, receiverId) => {
    return api.put(`/notification/message/read/${messageId}`, null, {
      params: { receiverId }
    })
  },

  // 批量标记消息为已读
  markBatchAsRead: (messageIds, receiverId) => {
    return api.put('/notification/message/read/batch', messageIds, {
      params: { receiverId }
    })
  },

  // 获取未读消息数量
  getUnreadCount: (receiverId) => {
    return api.get('/notification/message/unread/count', {
      params: { receiverId }
    })
  },

  // 删除消息
  deleteMessage: (messageId, userId) => {
    return api.delete(`/notification/message/${messageId}`, {
      params: { userId }
    })
  },

  // 批量删除消息
  deleteBatchMessages: (messageIds, userId) => {
    return api.delete('/notification/message/batch', {
      data: messageIds,
      params: { userId }
    })
  },

  // 获取消息详情
  getMessage: (messageId) => {
    return api.get(`/notification/message/${messageId}`)
  }
}

// 消息模板API
export const messageTemplateApi = {
  // 获取消息模板列表
  getTemplates: (params) => {
    return api.get('/notification/message-template/list', { params })
  },

  // 创建消息模板
  createTemplate: (data) => {
    return api.post('/notification/message-template/add', data)
  },

  // 更新消息模板
  updateTemplate: (data) => {
    return api.put('/notification/message-template/update', data)
  },

  // 删除消息模板
  deleteTemplate: (id) => {
    return api.delete(`/notification/message-template/${id}`)
  },

  // 获取消息模板详情
  getTemplate: (id) => {
    return api.get(`/notification/message-template/${id}`)
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

// 默认导出所有API
export default {
  messageApi,
  messageTemplateApi
} 