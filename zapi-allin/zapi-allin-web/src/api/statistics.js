/**
 * 系统统计相关API
 */

import api from './request'

export const statisticsApi = {
  // 获取今日统计
  getTodayStatistics: () => {
    return api.get('/system/statistics/today')
  },

  // 获取指定日期的统计数据
  getStatisticsByDate: (date) => {
    return api.get(`/system/statistics/date/${date}`)
  },

  // 获取最近N天的统计数据
  getRecentStatistics: (days) => {
    return api.get(`/system/statistics/recent/${days}`)
  },

  // 获取总的统计数据
  getTotalStatistics: () => {
    return api.get('/system/statistics/total')
  }
}

export default statisticsApi

