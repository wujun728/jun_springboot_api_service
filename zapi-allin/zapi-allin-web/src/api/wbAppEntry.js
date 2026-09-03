import api from './request'

/**
 * 应用条目API
 */
export const wbAppEntryApi = {
  /**
   * 分页查询应用条目
   */
  getAppEntryPage(params) {
    return api.get('/api/workbench/appEntry/page', { params })
  },

  /**
   * 获取应用条目树
   */
  getAppEntryTree(appId) {
    return api.get('/api/workbench/appEntry/tree', { params: { appId } })
  },

  /**
   * 获取子条目列表
   */
  getAppEntryChildren(appId, parentId) {
    return api.get('/api/workbench/appEntry/children', { params: { appId, parentId } })
  },

  /**
   * 查询应用条目详情
   */
  getAppEntry(id) {
    return api.get(`/api/workbench/appEntry/${id}`)
  },

  /**
   * 添加应用条目
   */
  addAppEntry(data) {
    return api.post('/api/workbench/appEntry', data)
  },

  /**
   * 更新应用条目
   */
  updateAppEntry(data) {
    return api.put('/api/workbench/appEntry', data)
  },

  /**
   * 删除应用条目
   */
  deleteAppEntry(id) {
    return api.delete(`/api/workbench/appEntry/${id}`)
  },

  /**
   * 批量删除应用条目
   */
  batchDeleteAppEntry(ids) {
    return api.delete('/api/workbench/appEntry/batch', { data: ids })
  },

  /**
   * 拖拽排序
   * 
   * 参数说明：
   * - entryId: 被拖拽的条目ID（必填）
   * - parentId: 新的父级ID（可选，如果跨分组拖拽则需要传入）
   * - beforeEntryId: 前驱节点ID（必填，0表示放在第一位）
   * 
   * 使用示例：
   * 1. 拖拽到首位：{ entryId: 123, parentId: 456, beforeEntryId: 0 }
   * 2. 拖拽到中间：{ entryId: 123, parentId: 456, beforeEntryId: 789 }
   * 3. 拖拽到末尾：{ entryId: 123, parentId: 456, beforeEntryId: 最后一个节点的ID }
   */
  dragSort(data) {
    return api.post('/api/workbench/appEntry/dragSort', data)
  }
}

export default wbAppEntryApi
