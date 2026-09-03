/**
 * 系统日志相关API
 */

import api from './request'

export const sysLogApi = {
  // 获取日志列表
  getLogList: (params) => {
    return api.get('/system/syslog/list', { params })
  },

  // 获取日志详情
  getLog: (id) => {
    return api.get(`/system/syslog/${id}`)
  },

  // 清空日志
  clearLogs: () => {
    return api.delete('/system/syslog/clear')
  },

  // 删除日志
  deleteLog: (id) => {
    return api.delete(`/system/syslog/delete/${id}`)
  }
}

export default sysLogApi 