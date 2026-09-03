/**
 * API请求配置
 * 统一处理axios实例和拦截器
 * 使用代理方式请求后端，避免跨域问题
 */

import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const api = axios.create({
  baseURL: '/zapi-allin-sys', // 使用代理路径，由vite代理到后端
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 从localStorage获取token
    const token = localStorage.getItem('zapi_token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    // 如果是FormData，让浏览器自动设置Content-Type（包含boundary）
    if (config.data instanceof FormData) {
      delete config.headers['Content-Type']
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    // 如果是文件下载请求（responseType: 'blob'），直接返回response
    if (response.config.responseType === 'blob') {
      return response
    }

    const { data } = response

    // 检查后端返回的状态码
    if (data.code === 200) {
      return data
    } else {
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
  },
  (error) => {
    if (error.response) {
      const { status, data } = error.response

      switch (status) {
        case 401:
          ElMessage.error('登录已过期，请重新登录')
          // 清除所有缓存和状态
          localStorage.clear()
          sessionStorage.clear()
          // 跳转到登录页
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('没有权限访问该资源，请重新登录')
          // 清除所有缓存和状态
          localStorage.clear()
          sessionStorage.clear()
          // 跳转到登录页
          window.location.href = '/login'
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器内部错误')
          break
        default:
          ElMessage.error(data?.message || '请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络连接失败，请检查网络')
    } else {
      ElMessage.error('请求配置错误')
    }

    return Promise.reject(error)
  }
)

export default api  