/**
 * 流程测试订单API服务
 * 对接后端 FlowTestOrderController
 */
import api from './request'

export const flowTestOrderApi = {
  /**
   * 创建订单
   * @param {Object} order 订单信息
   * @returns {Promise}
   */
  create(order) {
    return api.post('/api/flow-test-order', order)
  },

  /**
   * 根据ID查询订单
   * @param {Number} id 订单ID
   * @returns {Promise}
   */
  getById(id) {
    return api.get(`/api/flow-test-order/${id}`)
  },

  /**
   * 根据订单号查询订单
   * @param {String} orderNo 订单号
   * @returns {Promise}
   */
  getByOrderNo(orderNo) {
    return api.get(`/api/flow-test-order/by-order-no/${orderNo}`)
  },

  /**
   * 更新订单
   * @param {Number} id 订单ID
   * @param {Object} order 订单信息
   * @returns {Promise}
   */
  update(id, order) {
    return api.put(`/api/flow-test-order/${id}`, order)
  },

  /**
   * 删除订单
   * @param {Number} id 订单ID
   * @returns {Promise}
   */
  delete(id) {
    return api.delete(`/api/flow-test-order/${id}`)
  },

  /**
   * 批量删除订单
   * @param {Array} ids 订单ID数组
   * @returns {Promise}
   */
  batchDelete(ids) {
    return api.delete('/api/flow-test-order/batch', { data: ids })
  },

  /**
   * 分页查询订单列表
   * @param {Number} current 页码
   * @param {Number} size 每页大小
   * @param {String} orderName 订单名称（可选，用于模糊查询）
   * @returns {Promise}
   */
  page(current = 1, size = 10, orderName = '') {
    const params = { current, size }
    if (orderName) {
      params.orderName = orderName
    }
    return api.get('/api/flow-test-order/page', { params })
  },

  /**
   * 查询所有订单
   * @returns {Promise}
   */
  getAll() {
    return api.get('/api/flow-test-order/all')
  },

  /**
   * 检查订单号是否存在
   * @param {String} orderNo 订单号
   * @param {Number} excludeId 排除的订单ID（可选）
   * @returns {Promise}
   */
  checkOrderNo(orderNo, excludeId = null) {
    const params = { orderNo }
    if (excludeId) {
      params.excludeId = excludeId
    }
    return api.get('/api/flow-test-order/check-order-no', { params })
  }
}
