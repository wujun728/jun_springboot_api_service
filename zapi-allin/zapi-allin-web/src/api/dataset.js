/**
 * 数据集管理相关API
 */

import api from './request'

// 数据集管理API
export const datasetApi = {
  // 获取数据集列表
  getDatasetList: (params) => {
    return api.get('/dataset/dataConnection/list', { params })
  },

  // 创建数据集
  createDataset: (data) => {
    return api.post('/dataset/dataConnection/create', data)
  },

  // 根据ID获取数据集信息
  getDatasetInfoById: (data) => {
    return api.post('/dataset/dataConnection/getDataSetInfoById', data)
  },

  // 更新数据集
  updateDataset: (data) => {
    return api.put('/dataset/dataConnection/update', data)
  },

  // 删除数据集
  deleteDataset: (id) => {
    return api.delete(`/dataset/dataConnection/delete/${id}`)
  },

  // 获取数据集字段
  getDatasetFields: (datasetId) => {
    return api.get('/dataset/dataConnection/fields', {
      params: { datasetId }
    })
  }
}

// 数据表管理API
export const dataTableApi = {
  // 获取表和表注释
  getTablesAndComment: (config) => {
    return api.post('/dataset/dataTableManage/tables', config)
  },

  // 获取指定表的字段信息
  getTableColumnList: (config) => {
    return api.post('/dataset/dataTableManage/getTableColumnList', config)
  },

  // 获取列类型
  getColumnType: (type) => {
    return api.get(`/dataset/dataTableManage/getColumnType/${type}`)
  },

  // 更新表字段
  updateColumnType: (config) => {
    return api.post('/dataset/dataTableManage/updateColumnType', config)
  },

  // 新增表
  addTable: (config) => {
    return api.post('/dataset/dataTableManage/addTable', config)
  },

  // 删除表
  dropTable: (config) => {
    return api.post('/dataset/dataTableManage/dropTable', config)
  }
}

// 数据源管理API
export const dataSourceApi = {
  // 获取所有数据源
  getAllDataSources: () => {
    return api.get('/dataset/dataSource/getAll')
  },

  // 根据ID获取数据源信息
  getDataSourceById: (id) => {
    return api.get(`/dataset/dataSource/getInfoById/${id}`)
  },

  // 创建数据源
  createDataSource: (data) => {
    return api.post('/dataset/dataSource/create', data)
  },

  // 更新数据源
  updateDataSource: (data) => {
    return api.put('/dataset/dataSource/update', data)
  },

  // 删除数据源
  deleteDataSource: (id) => {
    return api.delete(`/dataset/dataSource/delete/${id}`)
  }
}

// 数据源导出API
export const dataSourceExportApi = {
  // 获取表列表
  getTableList: (config) => {
    return api.post('/dataset/dataSourceExport/tables', config)
  },

  // 生成脚本（预览）
  previewSql: (request) => {
    return api.post('/dataset/dataSourceExport/previewSql', request)
  },

  // 生成脚本（下载）
  generateScript: (request) => {
    return api.post('/dataset/dataSourceExport/generate', request, {
      params: { download: true },
      responseType: 'blob'
    })
  },

  // SQL转换
  convertSql: (request) => {
    return api.post('/dataset/dataSourceExport/convertSql', request)
  }
}

// 默认导出所有API
export default {
  datasetApi,
  dataTableApi,
  dataSourceApi,
  dataSourceExportApi
} 