/**
 * 用户相关API
 */

import api from './request'

export const userApi = {
  // 用户登录
  login: (username, password, captcha) => {
    return api.post('/system/user/login', null, {
      params: { username, password, captcha }
    })
  },

  // 获取用户信息
  getUserInfo: (userId) => {
    return api.get('/system/user/info', {
      params: { userId }
    })
  },

  // 更新用户信息
  updateUserInfo: (userInfo) => {
    return api.put('/system/user/info', userInfo)
  },

  // 修改密码
  changePassword: (userId, oldPassword, newPassword) => {
    return api.put('/system/user/password', {
      userId,
      oldPassword,
      newPassword
    })
  },

  // 获取用户列表
  getUserList: (params) => {
    return api.get('/system/user/list', { params })
  },

  // 获取用户详情
  getUserDetail: (id) => {
    return api.get(`/system/user/detail/${id}`)
  },

  // 创建用户
  createUser: (userData) => {
    return api.post('/system/user/add', userData)
  },

  // 添加用户（兼容旧方法名）
  addUser: (userData) => {
    return api.post('/system/user/add', userData)
  },

  // 更新用户
  updateUser: (userData) => {
    return api.put('/system/user/update', userData)
  },

  // 更新用户状态
  updateUserStatus: (id, status) => {
    return api.put(`/system/user/updateStatus/${id}`, null, {
      params: { status }
    })
  },

  // 删除用户
  deleteUser: (id) => {
    return api.delete(`/system/user/delete/${id}`)
  },

  // 获取所有启用用户
  getEnabledUsers: () => {
    return api.get('/system/user/enabled')
  },
  
  // 获取验证码（旧版本）
  getCaptcha: () => {
    return api.get('/system/user/captcha')
  },
  
  // 生成登录二维码
  generateQrCode: () => {
    return api.get('/system/user/qrcode')
  },
  
  // 查询验证码状态
  getCaptchaStatus: (sessionId) => {
    return api.get('/system/user/captcha-status', {
      params: { sessionId }
    })
  },
  
  // 扫码确认获取验证码
  scanCaptcha: (sessionId) => {
    return api.get('/system/user/scan-captcha', {
      params: { sessionId }
    })
  }
}

// 用户角色关联 API
export const userRoleApi = {
  // 根据用户ID获取角色ID列表
  getRoleIdsByUserId: (userId) => {
    return api.get(`/system/user-role/roles/${userId}`)
  },

  // 根据角色ID获取用户ID列表
  getUserIdsByRoleId: (roleId) => {
    return api.get(`/system/user-role/users/${roleId}`)
  },

  // 为用户分配角色
  assignRolesToUser: (userId, roleIds) => {
    return api.post(`/system/user-role/assign/${userId}`, roleIds)
  },

  // 删除用户的所有角色
  removeUserRoles: (userId) => {
    return api.delete(`/system/user-role/user/${userId}`)
  },

  // 删除角色的所有用户关联
  removeRoleUsers: (roleId) => {
    return api.delete(`/system/user-role/role/${roleId}`)
  },

  // 获取所有用户角色关联
  list: () => {
    return api.get('/system/user-role/list')
  }
}

// 用户组织关联 API
export const userOrgApi = {
  // 根据用户ID获取组织ID列表
  getOrgIdsByUserId: (userId) => {
    return api.get(`/system/user-org/orgs/${userId}`)
  },

  // 根据组织ID获取用户ID列表
  getUserIdsByOrgId: (orgId) => {
    return api.get(`/system/user-org/users/${orgId}`)
  },

  // 为用户分配组织
  assignOrgsToUser: (userId, orgIds) => {
    return api.post(`/system/user-org/assign-orgs/${userId}`, orgIds)
  },

  // 为组织分配用户
  assignUsersToOrg: (orgId, userIds) => {
    return api.post(`/system/user-org/assign-users/${orgId}`, userIds)
  },

  // 删除用户的所有组织
  removeUserOrgs: (userId) => {
    return api.delete(`/system/user-org/user/${userId}`)
  },

  // 删除组织的所有用户关联
  removeOrgUsers: (orgId) => {
    return api.delete(`/system/user-org/org/${orgId}`)
  },

  // 获取所有用户组织关联
  list: () => {
    return api.get('/system/user-org/list')
  },

  // 根据组织ID获取用户列表（带分页）
  getUsersByOrgId: (orgId, params) => {
    return api.get(`/system/user-org/users-by-org/${orgId}`, { params })
  }
}

export default userApi 