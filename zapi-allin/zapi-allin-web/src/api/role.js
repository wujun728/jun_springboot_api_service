/**
 * 角色相关API
 */

import api from './request'

export const roleApi = {
  // 获取角色列表
  getRoleList: (params) => {
    return api.get('/system/role/list', { params })
  },

  // 获取所有角色
  getAllRoles: () => {
    return api.get('/system/role/all')
  },

  // 获取角色详情
  getRole: (id) => {
    return api.get(`/system/role/${id}`)
  },

  // 添加角色
  addRole: (roleData) => {
    return api.post('/system/role/add', roleData)
  },

  // 更新角色
  updateRole: (roleData) => {
    return api.put('/system/role/update', roleData)
  },

  // 删除角色
  deleteRole: (id) => {
    return api.delete(`/system/role/delete/${id}`)
  }
}

export default roleApi 