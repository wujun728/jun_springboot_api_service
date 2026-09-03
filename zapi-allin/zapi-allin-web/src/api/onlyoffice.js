import axios from 'axios'
import api from './request'

// 创建一个不带响应拦截器的原始实例，用于获取OnlyOffice配置（有些服务不会返回 code=200 包装）
const rawApi = axios.create({
  baseURL: api.defaults.baseURL,
  timeout: api.defaults.timeout
})

export const onlyofficeApi = {
  // 获取文档编辑配置（使用 rawApi，直接透传服务端返回的数据）
  getEditConfig: (documentId) => rawApi.get(`/onlyoffice/edit-config/${documentId}`),

  // 触发强制保存（Docs CommandService forcesave）
  forceSave: (key) => rawApi.get(`/onlyoffice/force-save/${key}`)
}

export default onlyofficeApi
