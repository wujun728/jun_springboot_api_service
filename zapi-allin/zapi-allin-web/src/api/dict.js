/**
 * 字典管理相关API
 */

import api from './request'

/**
 * 字典类型API
 */
export const dictTypeApi = {
  // 获取字典类型列表（分页）
  getDictTypeList: (params) => {
    return api.get('/system/dict/type/list', { params })
  },

  // 获取所有字典类型
  getAllDictTypes: () => {
    return api.get('/system/dict/type/all')
  },

  // 根据ID获取字典类型详情
  getDictType: (dictId) => {
    return api.get(`/system/dict/type/${dictId}`)
  },

  // 根据字典类型获取字典类型信息
  getDictTypeByType: (dictType) => {
    return api.get(`/system/dict/type/type/${dictType}`)
  },

  // 新增字典类型
  addDictType: (data) => {
    return api.post('/system/dict/type/add', data)
  },

  // 修改字典类型
  updateDictType: (data) => {
    return api.put('/system/dict/type/update', data)
  },

  // 删除字典类型
  deleteDictType: (dictIds) => {
    return api.delete(`/system/dict/type/delete/${dictIds}`)
  },

  // 刷新字典缓存
  refreshCache: () => {
    return api.delete('/system/dict/type/refreshCache')
  }
}

/**
 * 字典数据API
 */
export const dictDataApi = {
  // 获取字典数据列表（分页）
  getDictDataList: (params) => {
    return api.get('/system/dict/data/list', { params })
  },

  // 根据字典类型查询字典数据
  getDictDataByType: (dictType) => {
    return api.get(`/system/dict/data/type/${dictType}`)
  },

  // 根据ID获取字典数据详情
  getDictData: (dictCode) => {
    return api.get(`/system/dict/data/${dictCode}`)
  },

  // 根据字典类型和字典键值查询字典标签
  getDictLabel: (dictType, dictValue) => {
    return api.get('/system/dict/data/label', { params: { dictType, dictValue } })
  },

  // 新增字典数据
  addDictData: (data) => {
    return api.post('/system/dict/data/add', data)
  },

  // 修改字典数据
  updateDictData: (data) => {
    return api.put('/system/dict/data/update', data)
  },

  // 删除字典数据
  deleteDictData: (dictCodes) => {
    return api.delete(`/system/dict/data/delete/${dictCodes}`)
  }
}

export default {
  dictTypeApi,
  dictDataApi
}