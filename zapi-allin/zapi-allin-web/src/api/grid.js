import api from './request'

/**
 * 表格API
 */
export const gridApi = {
  /**
   * 获取表格详情
   */
  getGridDetail(gridId) {
    return api.get(`/api/grid/${gridId}`)
  },

  /**
   * 根据应用条目ID获取表格
   */
  getGridByEntryId(entryId) {
    return api.get(`/api/grid/entry/${entryId}`)
  },

  /**
   * 创建表格
   */
  createGrid(data) {
    return api.post('/api/grid', data)
  },

  /**
   * 更新表格
   */
  updateGrid(data) {
    return api.put('/api/grid', data)
  },

  /**
   * 删除表格
   */
  deleteGrid(gridId) {
    return api.delete(`/api/grid/${gridId}`)
  }
}

export default gridApi
