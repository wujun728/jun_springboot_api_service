/**
 * 部门相关API
 */

import api from './request'

export const departmentApi = {
  // 获取部门列表
  getDepartmentList: (params) => {
    return api.get('/system/department/list', { params })
  },

  // 获取所有部门
  getAllDepartments: () => {
    return api.get('/system/department/all')
  },

  // 获取部门树形结构
  getDepartmentTree: () => {
    return api.get('/system/department/tree')
  },

  // 获取部门详情
  getDepartment: (id) => {
    return api.get(`/system/department/${id}`)
  },

  // 添加部门
  addDepartment: (deptData) => {
    return api.post('/system/department/add', deptData)
  },

  // 更新部门
  updateDepartment: (deptData) => {
    return api.put('/system/department/update', deptData)
  },

  // 删除部门
  deleteDepartment: (id) => {
    return api.delete(`/system/department/delete/${id}`)
  }
}

export default departmentApi 