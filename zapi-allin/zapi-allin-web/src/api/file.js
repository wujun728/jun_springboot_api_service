/**
 * 文件相关API
 */

import api from './request'

export const fileApi = {
  // 上传文件
  uploadFile: (file, options = {}) => {
    const formData = new FormData()
    formData.append('file', file)

    if (options.path) {
      formData.append('path', options.path)
    }
    if (options.description) {
      formData.append('description', options.description)
    }
    if (options.uploadUserId) {
      formData.append('uploadUserId', options.uploadUserId)
    }
    if (options.uploadUsername) {
      formData.append('uploadUsername', options.uploadUsername)
    }

    return api.post('/file/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 下载文件
  downloadFile: (fileId) => {
    return api.get(`/file/download/${fileId}`, {
      responseType: 'blob'
    })
  },

  // 删除文件
  deleteFile: (fileId) => {
    return api.delete(`/file/delete/${fileId}`)
  },

  // 获取文件信息
  getFileInfo: (fileId) => {
    return api.get(`/file/info/${fileId}`)
  },

  // 获取文件列表
  getFileList: (params) => {
    return api.get('/file/list', { params })
  },

  // 复制文件
  copyFile: (sourceFileId, targetPath, options = {}) => {
    const formData = new FormData()
    formData.append('sourceFileId', sourceFileId)
    formData.append('targetPath', targetPath)

    if (options.description) {
      formData.append('description', options.description)
    }
    if (options.uploadUserId) {
      formData.append('uploadUserId', options.uploadUserId)
    }
    if (options.uploadUsername) {
      formData.append('uploadUsername', options.uploadUsername)
    }

    return api.post('/file/copy', formData)
  },

  // 移动文件
  moveFile: (fileId, targetPath) => {
    return api.put('/file/move', null, {
      params: { fileId, targetPath }
    })
  }
}

export default fileApi 