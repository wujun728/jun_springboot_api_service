/**
 * 用户状态管理 Store
 * 
 * 功能说明：
 * 1. 用户登录状态管理
 * 2. 用户信息管理
 * 3. 权限管理
 * 4. 用户操作历史记录
 * 
 * @author ZAPI Team
 * @version 1.0.0
 */

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { userApi, menuApi } from '@/api'

export const useUserStore = defineStore('user', () => {
  // ==================== 状态定义 ====================
  
  // 用户登录状态
  const isLoggedIn = ref(false)
  
  // 用户信息
  const userInfo = ref({
    id: null,
    username: '',
    realName: '',
    email: '',
    avatar: '',
    role: '',
    permissions: [],
    lastLoginTime: null,
    loginCount: 0,
    status: 'active', // active, inactive, banned
    department: '',
    position: '',
    phone: '',
    createTime: null,
    updateTime: null
  })
  
  // 用户令牌
  const token = ref('')
  
  // 刷新令牌
  const refreshToken = ref('')
  
  // 登录历史记录
  const loginHistory = ref([])
  
  // 用户操作日志
  const userActions = ref([])
  
  // 权限缓存
  const permissionCache = ref(new Map())
  
  // 用户菜单列表
  const userMenus = ref([])
  
  // 用户权限列表
  const userPermissions = ref([])
  
  // 动态路由是否已加载
  const dynamicRoutesLoaded = ref(false)
  
  // 启用用户列表缓存
  const enabledUsers = ref([])
  const enabledUsersLoaded = ref(false)
  const enabledUsersLoadTime = ref(null)
  const enabledUsersLoading = ref(false) // 正在加载标志，防止并发请求
  const enabledUsersLoadPromise = ref(null) // 保存正在进行的加载Promise
  
  // ==================== 计算属性 ====================
  
  // 用户显示名称（优先显示真实姓名）
  const displayName = computed(() => {
    return userInfo.value.realName || userInfo.value.username || userInfo.value.email || '未知用户'
  })

  // 用户角色显示名称
  const roleDisplayName = computed(() => {
    const roleMap = {
      'admin': '超级管理员',
      'manager': '管理员',
      'user': '普通用户',
      'guest': '访客'
    }
    return roleMap[userInfo.value.role] || userInfo.value.role
  })

  // 用户权限列表（从用户信息中获取）
  const userPermissionsFromInfo = computed(() => {
    return userInfo.value.permissions || []
  })

  // 是否为管理员
  const isAdmin = computed(() => {
    return userInfo.value.role === 'admin' || userInfo.value.role === 'manager'
  })

  // 用户状态显示
  const statusDisplay = computed(() => {
    const statusMap = {
      'active': '活跃',
      'inactive': '禁用',
      'banned': '封禁'
    }
    return statusMap[userInfo.value.status] || userInfo.value.status
  })

  // ==================== 方法定义 ====================
  
  /**
   * 用户登录
   * @param {Object} loginData - 登录数据
   * @param {string} loginData.username - 用户名
   * @param {string} loginData.password - 密码
   * @param {boolean} loginData.rememberMe - 记住我
   * @returns {Promise<boolean>} 登录结果
   */
  const login = async (loginData) => {
    try {

      // 调用后端登录API
      const response = await userApi.login(loginData.username, loginData.password, loginData.captcha)
      
      if (response.code === 200 && response.data) {
        const { token: apiToken, user } = response.data
        
        // 更新用户信息
        userInfo.value = {
          id: user.id,
          username: user.username,
          realName: user.realName || '',
          email: user.email || '',
          avatar: user.avatar || `https://avatars.githubusercontent.com/u/${Math.floor(Math.random() * 100)}?v=4`,
          role: user.roleId === 1 ? 'admin' : 'user', // 根据角色ID判断
          permissions: ['dashboard', 'users', 'analytics', 'settings'], // 默认权限
          lastLoginTime: user.lastLoginTime,
          loginCount: 1, // 后端没有这个字段，设为默认值
          status: user.status === 1 ? 'active' : 'inactive',
          department: user.deptId ? `部门${user.deptId}` : '',
          position: '开发工程师', // 后端没有这个字段，设为默认值
          phone: user.phone || '',
          createTime: user.createTime,
          updateTime: user.updateTime
        }
        
        // 保存令牌
        token.value = apiToken
        refreshToken.value = '' // 后端没有刷新令牌，设为空
        isLoggedIn.value = true
        
        // 先保存到本地存储，确保token可用
        saveToLocalStorage()
        
        // 获取用户菜单和权限
        await loadUserMenusAndPermissions()

        // 添加动态路由
        if (userMenus.value.length > 0) {
          const { addDynamicRoutes } = await import('@/router')
          addDynamicRoutes(userMenus.value)
          dynamicRoutesLoaded.value = true
        }
        
        // 预加载启用用户列表（后台加载，不阻塞登录流程）
        loadEnabledUsers().catch(error => {
          console.warn('[UserStore] 预加载启用用户列表失败:', error)
        })
        
        // 记录登录历史
        addLoginHistory({
          time: new Date().toISOString(),
          ip: '192.168.1.1',
          device: 'Chrome/120.0.0.0',
          location: '北京市'
        })

        // 记录用户操作
        addUserAction({
          type: 'login',
          description: '用户登录',
          timestamp: new Date().toISOString()
        })

        ElMessage.success('登录成功！')
        return true
      } else {
        throw new Error(response.message || '登录失败')
      }
      
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
      return false
    }
  }
  
  /**
   * 加载启用用户列表（带缓存和防并发）
   * @param {boolean} forceRefresh - 是否强制刷新
   * @returns {Promise<Array>} 用户列表
   */
  const loadEnabledUsers = async (forceRefresh = false) => {
    try {
      // 检查缓存是否有效（5分钟内）
      const cacheExpired = !enabledUsersLoadTime.value || 
        (Date.now() - enabledUsersLoadTime.value > 5 * 60 * 1000)
      
      // 如果缓存有效且不强制刷新，直接返回缓存数据
      if (!forceRefresh && enabledUsersLoaded.value && !cacheExpired) {
        console.log('[UserStore] 使用缓存的启用用户列表，数量:', enabledUsers.value.length)
        return enabledUsers.value
      }
      
      // 如果正在加载中，返回正在进行的Promise（防止并发请求）
      if (enabledUsersLoading.value && enabledUsersLoadPromise.value) {
        console.log('[UserStore] 已有请求正在进行，等待该请求完成...')
        return enabledUsersLoadPromise.value
      }
      
      // 标记为正在加载
      enabledUsersLoading.value = true
      
      // 创建加载Promise
      enabledUsersLoadPromise.value = (async () => {
        try {
          console.log('[UserStore] 从服务器加载启用用户列表...')
          const result = await userApi.getEnabledUsers()
          
          if (result.code === 200 || result.data) {
            enabledUsers.value = result.data || []
            enabledUsersLoaded.value = true
            enabledUsersLoadTime.value = Date.now()
            console.log('[UserStore] 启用用户列表加载成功，数量:', enabledUsers.value.length)
            return enabledUsers.value
          } else {
            console.warn('[UserStore] 加载启用用户列表失败:', result.message)
            return []
          }
        } finally {
          // 加载完成，清除标志
          enabledUsersLoading.value = false
          enabledUsersLoadPromise.value = null
        }
      })()
      
      return enabledUsersLoadPromise.value
      
    } catch (error) {
      console.error('[UserStore] 加载启用用户列表异常:', error)
      // 清除加载标志
      enabledUsersLoading.value = false
      enabledUsersLoadPromise.value = null
      
      // 如果加载失败但有缓存，返回缓存数据
      if (enabledUsers.value.length > 0) {
        console.log('[UserStore] 加载失败，使用缓存数据')
        return enabledUsers.value
      }
      return []
    }
  }
  
  /**
   * 清除启用用户缓存
   */
  const clearEnabledUsersCache = () => {
    enabledUsers.value = []
    enabledUsersLoaded.value = false
    enabledUsersLoadTime.value = null
    enabledUsersLoading.value = false
    enabledUsersLoadPromise.value = null
    console.log('[UserStore] 已清除启用用户缓存')
  }
  
  /**
   * 用户登出
   * @returns {Promise<void>}
   */
  const logout = async () => {
    try {
      // 记录登出操作
      addUserAction({
        type: 'logout',
        description: '用户登出',
        timestamp: new Date().toISOString()
      })

      // 清除状态
      isLoggedIn.value = false
      userInfo.value = {
        id: null,
        username: '',
        realName: '',
        email: '',
        avatar: '',
        role: '',
        permissions: [],
        lastLoginTime: null,
        loginCount: 0,
        status: 'active',
        department: '',
        position: '',
        phone: '',
        createTime: null,
        updateTime: null
      }
      token.value = ''
      refreshToken.value = ''
      userMenus.value = []
      userPermissions.value = []
      dynamicRoutesLoaded.value = false
      
      // 清除启用用户缓存
      clearEnabledUsersCache()
      
      // 清除动态路由
      try {
        const { clearDynamicRoutes } = await import('@/router')
        clearDynamicRoutes()
      } catch (routeError) {
        console.error('清除动态路由失败:', routeError)
      }
      
      // 清除本地存储
      clearLocalStorage()
      
      // 清除权限缓存
      permissionCache.value.clear()
      
      ElMessage.success('已安全登出')
      
    } catch (error) {

      ElMessage.error('登出失败')
    }
  }
  
  /**
   * 检查用户权限
   * @param {string} permission - 权限标识
   * @returns {boolean} 是否有权限
   */
  const hasPermission = (permission) => {
    // 管理员拥有所有权限
    if (isAdmin.value) return true
    
    // 检查缓存
    if (permissionCache.value.has(permission)) {
      return permissionCache.value.get(permission)
    }
    
    // 检查用户权限（优先使用从后端获取的权限列表）
    const permissions = userPermissions.value.length > 0 ? userPermissions.value : userPermissionsFromInfo.value
    const hasPerm = permissions.includes(permission)
    
    // 缓存结果
    permissionCache.value.set(permission, hasPerm)
    
    return hasPerm
  }
  
  /**
   * 检查多个权限（任一满足即可）
   * @param {string[]} permissions - 权限标识数组
   * @returns {boolean} 是否有权限
   */
  const hasAnyPermission = (permissions) => {
    return permissions.some(permission => hasPermission(permission))
  }
  
  /**
   * 检查多个权限（全部满足）
   * @param {string[]} permissions - 权限标识数组
   * @returns {boolean} 是否有权限
   */
  const hasAllPermissions = (permissions) => {
    return permissions.every(permission => hasPermission(permission))
  }
  
  /**
   * 更新用户信息
   * @param {Object} newInfo - 新的用户信息
   */
  const updateUserInfo = (newInfo) => {
    userInfo.value = { ...userInfo.value, ...newInfo }
    saveToLocalStorage()
    
    // 记录操作
    addUserAction({
      type: 'update_profile',
      description: '更新个人信息',
      timestamp: new Date().toISOString()
    })
  }
  
  /**
   * 加载用户菜单和权限
   */
  const loadUserMenusAndPermissions = async () => {
    try {
      // 调用后端API获取用户菜单
      const menuResponse = await menuApi.getUserMenus()
      if (menuResponse.code === 200) {
        userMenus.value = menuResponse.data || []
      } else {
        console.warn('获取用户菜单失败:', menuResponse.message)
        userMenus.value = []
      }

      // 权限系统已移除，使用默认权限
      userPermissions.value = []
    } catch (error) {

      // 如果API调用失败，使用默认的空数据
      userMenus.value = []
      userPermissions.value = []
    }
  }

  /**
   * 加载动态路由
   */
  const loadDynamicRoutes = async (forceReload = false) => {
    try {
      // 如果已经加载过且不是强制重新加载，跳过
      if (dynamicRoutesLoaded.value && !forceReload) {
        console.log('动态路由已加载，跳过')
        return true
      }

      // 获取用户菜单
      await loadUserMenusAndPermissions()
      
      if (userMenus.value.length > 0) {
        try {
          // 导入动态路由添加函数
          const { addDynamicRoutes } = await import('@/router')
          
          // 添加动态路由
          addDynamicRoutes(userMenus.value)
          dynamicRoutesLoaded.value = true
          console.log('动态路由加载成功')
          return true
        } catch (routeError) {
          console.error('添加动态路由失败:', routeError)
          // 即使路由添加失败，也标记为已尝试加载，避免无限重试
          dynamicRoutesLoaded.value = true
          return false
        }
      } else {
        console.warn('没有用户菜单数据')
        // 即使没有菜单数据，也标记为已加载，避免重复尝试
        dynamicRoutesLoaded.value = true
        return true
      }
    } catch (error) {

      // 标记为已尝试，避免无限重试
      dynamicRoutesLoaded.value = true
      return false
    }
  }

  /**
   * 检查认证状态
   * @returns {Promise<boolean>} 是否已认证
   */
  const checkAuthStatus = async () => {
    try {
      // 从本地存储恢复状态
      const savedToken = localStorage.getItem('zapi_token')
      const savedUserInfo = localStorage.getItem('zapi_user_info')
      
      if (savedToken && savedUserInfo) {
        token.value = savedToken
        userInfo.value = JSON.parse(savedUserInfo)
        isLoggedIn.value = true
        
        // 加载用户菜单和权限
        await loadUserMenusAndPermissions()
        
        // 预加载启用用户列表（后台加载，不阻塞页面加载）
        loadEnabledUsers().catch(error => {
          console.warn('[UserStore] 预加载启用用户列表失败:', error)
        })
        
        // 验证令牌有效性（这里可以调用API验证）
        // const isValid = await validateToken(savedToken)
        // if (!isValid) {
        //   await logout()
        //   return false
        // }
        
        return true
      }
      
      return false
    } catch (error) {

      return false
    }
  }
  
  /**
   * 刷新令牌
   * @returns {Promise<boolean>} 刷新结果
   */
  const refreshUserToken = async () => {
    try {
      if (!refreshToken.value) {
        throw new Error('没有刷新令牌')
      }
      
      // 模拟刷新令牌API调用
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 生成新令牌
      const newToken = `token_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
      const newRefreshToken = `refresh_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
      
      token.value = newToken
      refreshToken.value = newRefreshToken
      
      saveToLocalStorage()
      
      return true
    } catch (error) {

      await logout()
      return false
    }
  }
  
  /**
   * 添加登录历史记录
   * @param {Object} history - 历史记录
   */
  const addLoginHistory = (history) => {
    loginHistory.value.unshift(history)
    
    // 只保留最近50条记录
    if (loginHistory.value.length > 50) {
      loginHistory.value = loginHistory.value.slice(0, 50)
    }
  }
  
  /**
   * 添加用户操作记录
   * @param {Object} action - 操作记录
   */
  const addUserAction = (action) => {
    userActions.value.unshift({
      id: Date.now(),
      userId: userInfo.value.id,
      ...action
    })

    // 只保留最近100条记录
    if (userActions.value.length > 100) {
      userActions.value = userActions.value.slice(0, 100)
    }
  }
  
  /**
   * 保存到本地存储
   */
  const saveToLocalStorage = () => {
    try {
      localStorage.setItem('zapi_token', token.value)
      localStorage.setItem('zapi_refresh_token', refreshToken.value)
      localStorage.setItem('zapi_user_info', JSON.stringify(userInfo.value))
      localStorage.setItem('zapi_login_history', JSON.stringify(loginHistory.value))
      localStorage.setItem('zapi_user_actions', JSON.stringify(userActions.value))
    } catch (error) {
      console.error('保存到本地存储失败:', error)
    }
  }
  
  /**
   * 从本地存储恢复
   */
  const restoreFromLocalStorage = () => {
    try {
      const savedToken = localStorage.getItem('zapi_token')
      const savedRefreshToken = localStorage.getItem('zapi_refresh_token')
      const savedUserInfo = localStorage.getItem('zapi_user_info')
      const savedLoginHistory = localStorage.getItem('zapi_login_history')
      const savedUserActions = localStorage.getItem('zapi_user_actions')
      
      if (savedToken) token.value = savedToken
      if (savedRefreshToken) refreshToken.value = savedRefreshToken
      if (savedUserInfo) userInfo.value = JSON.parse(savedUserInfo)
      if (savedLoginHistory) loginHistory.value = JSON.parse(savedLoginHistory)
      if (savedUserActions) userActions.value = JSON.parse(savedUserActions)
      
      isLoggedIn.value = !!savedToken
    } catch (error) {
      console.error('从本地存储恢复失败:', error)
    }
  }
  
  /**
   * 清除本地存储
   */
  const clearLocalStorage = () => {
    try {
      localStorage.removeItem('zapi_token')
      localStorage.removeItem('zapi_refresh_token')
      localStorage.removeItem('zapi_user_info')
      localStorage.removeItem('zapi_login_history')
      localStorage.removeItem('zapi_user_actions')
    } catch (error) {
      console.error('清除本地存储失败:', error)
    }
  }
  
  // ==================== 初始化 ====================
  
  // 从本地存储恢复状态
  restoreFromLocalStorage()
  
  // ==================== 返回状态和方法 ====================
  
  return {
    // 状态
    isLoggedIn,
    userInfo,
    token,
    refreshToken,
    loginHistory,
    userActions,
    userMenus,
    userPermissions,
    dynamicRoutesLoaded,
    enabledUsers,
    enabledUsersLoaded,
    
    // 计算属性
    displayName,
    roleDisplayName,
    userPermissionsFromInfo,
    isAdmin,
    statusDisplay,
    
    // 方法
    login,
    logout,
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    updateUserInfo,
    checkAuthStatus,
    refreshUserToken,
    loadUserMenusAndPermissions,
    loadDynamicRoutes,
    loadEnabledUsers,
    clearEnabledUsersCache,
    addLoginHistory,
    addUserAction,
    saveToLocalStorage,
    restoreFromLocalStorage,
    clearLocalStorage
  }
})
