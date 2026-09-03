/**
 * 用户证书相关API
 */

import api from './request'

export const certApi = {
  // 分页查询用户证书
  pageUserCerts: (params) => {
    return api.get('/system/cert/page', { params })
  },

  // 根据ID获取证书详情
  getCertById: (id) => {
    return api.get(`/system/cert/${id}`)
  },

  // 生成证书
  createCert: (data) => {
    return api.post('/system/cert/create', data)
  },

  // 删除证书
  deleteCert: (id) => {
    return api.delete(`/system/cert/${id}`)
  },

  // 批量删除证书
  batchDelete: (ids) => {
    return api.delete('/system/cert/batch', { data: ids })
  },

  // 下载证书
  downloadCert: (fileId) => {
    return `/zapi-allin-sys/file/download/${fileId}`
  },

  // SM2证书签名
  signSM2Cert: (formData) => {
    return api.post('/system/cert/sign/sm2', formData)
  },

  // SM2证书验签
  verifySM2Cert: (data) => {
    return api.post('/system/cert/verify/sm2', data)
  },

  // RSA证书签名
  signRSACert: (formData) => {
    return api.post('/system/cert/sign/rsa', formData)
  },

  // RSA证书验签
  verifyRSACert: (formData) => {
    return api.post('/system/cert/verify/rsa', formData)
  }
}

export default certApi

