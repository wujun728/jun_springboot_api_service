/**
 * 菜单相关API
 */

import api from './request'

export const menuApi = {
  // 获取当前用户菜单列表
  getUserMenus: () => {
    return api.get('/system/menu/user')
  },

  // 获取菜单列表
  getMenuList: (params) => {
    return api.get('/system/menu/list', { params })
  },

  // 获取菜单树形结构
  getMenuTree: () => {
    return api.get('/system/menu/tree')
  },

  // 获取菜单详情
  getMenu: (id) => {
    return api.get(`/system/menu/${id}`)
  },

  // 添加菜单
  addMenu: (menuData) => {
    return api.post('/system/menu/add', menuData)
  },

  // 更新菜单
  updateMenu: (menuData) => {
    return api.put('/system/menu/update', menuData)
  },

  // 删除菜单
  deleteMenu: (id) => {
    return api.delete(`/system/menu/delete/${id}`)
  }
}

export default menuApi 