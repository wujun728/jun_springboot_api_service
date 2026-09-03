import request from './request'

/**
 * AI聊天助手API
 */
export const aiApi = {
  /**
   * 发送消息（AI响应可能需要较长时间，设置3分钟超时）
   */
  sendMessage(data) {
    return request({
      url: '/api/ai/chat/send',
      method: 'post',
      data,
      timeout: 180000 // 3分钟超时
    })
  },

  /**
   * 清除会话
   */
  clearSession(sessionId) {
    return request({
      url: `/api/ai/chat/session/${sessionId}`,
      method: 'delete'
    })
  },

  /**
   * 健康检查
   */
  health() {
    return request({
      url: '/api/ai/chat/health',
      method: 'get'
    })
  },

  /**
   * 获取用户聊天历史
   */
  getUserHistory(userId = 'default_user', limit = 50) {
    return request({
      url: '/api/ai/chat/history',
      method: 'get',
      params: { userId, limit }
    })
  },

  /**
   * 获取指定会话历史
   */
  getSessionHistory(sessionId) {
    return request({
      url: `/api/ai/chat/session/${sessionId}`,
      method: 'get'
    })
  }
}

