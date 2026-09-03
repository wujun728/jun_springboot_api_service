/**
 * 表单管理相关API
 */

import api from './request'

export const formApi = {
  // 创建表单
  createForm: (formData) => {
    return api.post('/api/form', formData)
  },

  // 更新表单
  updateForm: (formData) => {
    return api.put('/api/form', formData)
  },

  // 删除表单
  deleteForm: (formId) => {
    return api.delete(`/api/form/${formId}`)
  },

  // 获取表单详情
  getFormDetail: (formId) => {
    return api.get(`/api/form/${formId}`)
  },

  // 根据应用条目ID获取表单
  getFormByEntryId: (entryId) => {
    return api.get(`/api/form/entry/${entryId}`)
  },

  // 分页查询表单列表
  getFormList: (params) => {
    return api.get('/api/form/list', {
      params: {
        current: params.current || 1,
        size: params.size || 10,
        formName: params.formName,
        status: params.status
      }
    })
  },

  // 启用/禁用表单
  changeFormStatus: (formId, status) => {
    return api.put(`/api/form/${formId}/status`, null, {
      params: { status }
    })
  },

  // 根据表单ID获取表信息和字段列表
  getTableInfoByFormId: (formId) => {
    return api.get(`/api/form-data/table-info/${formId}`)
  },

  // 根据表单ID分页查询业务数据
  getFormDataPage: (params) => {
    return api.post('/api/form-data/page', {
      formId: params.formId,
      criteria: params.criteria || {},
      pageNo: params.pageNo || 1,
      pageSize: params.pageSize || 10
    })
  },

  // 新增表单业务数据
  createFormData: (params) => {
    return api.post('/api/form-data/create', {
      formId: params.formId,
      data: params.data,
      startUser: params.startUser, // 流程发起人ID
      appId: params.appId, // 应用ID（用于判断是否需要发起流程）
      entryId: params.entryId, // 目录ID（用于查询目录详情判断是否为流程表单）
      hasFlow: params.hasFlow // 是否为流程表单（0=普通表单，1=流程表单）
    })
  },

  // 更新表单业务数据
  updateFormData: (params) => {
    return api.post('/api/form-data/update', {
      formId: params.formId,
      id: params.id,
      data: params.data
    })
  },

  // 删除表单业务数据
  deleteFormData: (params) => {
    return api.post('/api/form-data/delete', {
      formId: params.formId,
      id: params.id
    })
  },

  // 批量删除表单业务数据
  batchDeleteFormData: (params) => {
    return api.post('/api/form-data/batch-delete', {
      formId: params.formId,
      ids: params.ids
    })
  }
}

export default formApi
