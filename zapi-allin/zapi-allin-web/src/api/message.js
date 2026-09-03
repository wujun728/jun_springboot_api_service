/**
 * 消息相关API
 */

import api from './request'

export const messageApi = {
  // 发送消息
  sendMessage: (message) => {
    return api.post('/notification/message/send', message)
  },

  // 批量发送消息
  sendBatchMessages: (messages) => {
    return api.post('/notification/message/send/batch', messages)
  },

  // 分页查询接收的消息
  getReceivedMessages: (receiverId, params) => {
    return api.get('/notification/message/received', {
      params: { receiverId, ...params }
    })
  },

  // 分页查询发送的消息
  getSentMessages: (senderId, params) => {
    return api.get('/notification/message/sent', {
      params: { senderId, ...params }
    })
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
      params: { userId },
      data: messageIds
    })
  },

  // 获取消息详情
  getMessage: (messageId) => {
    return api.get(`/notification/message/${messageId}`)
  }
}

export default messageApi 