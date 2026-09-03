/**
 * 系统配置文件
 */

// API基础配置
export const API_CONFIG = {
  // 后端API基础URL - 使用代理路径
  BASE_URL: '/zapi-allin-sys',
  
  // 文件服务配置
  FILE: {
    // 文件上传接口
    UPLOAD_URL: '/file/upload',
    // 文件下载接口
    DOWNLOAD_URL: '/file/download',
    // 文件删除接口
    DELETE_URL: '/file/delete',
    // 文件列表接口
    LIST_URL: '/file/list'
  }
}

// 获取完整的文件下载URL
export const getFileDownloadUrl = (fileId) => {
  if (!fileId) return ''
  return `${API_CONFIG.BASE_URL}${API_CONFIG.FILE.DOWNLOAD_URL}/${fileId}`
}

// 获取完整的API URL
export const getApiUrl = (path) => {
  return `${API_CONFIG.BASE_URL}${path}`
}

export default {
  API_CONFIG,
  getFileDownloadUrl,
  getApiUrl
}
