/**
 * 调试工具类
 * 用于查看和分析后台返回的数据
 */

/**
 * 打印菜单数据结构
 * @param {Array} menus - 菜单数据
 */
export const debugMenus = (menus) => {
  console.group('🔍 菜单数据结构分析')

  if (!Array.isArray(menus)) {

    console.groupEnd()
    return
  }

  const analyzeMenu = (menu, level = 0) => {
    const indent = '  '.repeat(level)
    `)

    })`)

    if (menu.children && menu.children.length > 0) {

      menu.children.forEach(child => analyzeMenu(child, level + 1))
    }
  }
  
  menus.forEach(menu => analyzeMenu(menu))
  
  // 统计信息
  const stats = {
    total: menus.length,
    directories: menus.filter(m => m.menuType === 1).length,
    menus: menus.filter(m => m.menuType === 2).length,
    buttons: menus.filter(m => m.menuType === 3).length,
    withComponent: menus.filter(m => m.component && m.component.trim()).length,
    withoutComponent: menus.filter(m => !m.component || !m.component.trim()).length
  }

  console.groupEnd()
}

/**
 * 获取菜单类型名称
 * @param {number} type - 菜单类型
 * @returns {string} 类型名称
 */
const getMenuTypeName = (type) => {
  const typeMap = {
    1: '目录',
    2: '菜单',
    3: '按钮'
  }
  return typeMap[type] || '未知'
}

/**
 * 分析组件路径问题
 * @param {Array} menus - 菜单数据
 */
export const analyzeComponentPaths = (menus) => {
  console.group('🔧 组件路径分析')
  
  const menuItems = menus.filter(m => m.menuType === 2)

  const componentPaths = menuItems.map(m => m.component).filter(Boolean)

  const uniquePaths = [...new Set(componentPaths)]

  // 检查哪些路径可能有问题
  const problematicPaths = uniquePaths.filter(path => {
    return !path.includes('/') || path.includes('index') || path.includes('.vue')

  if (problematicPaths.length > 0) {

  }
  
  console.groupEnd()
}

/**
 * 验证路由配置
 * @param {Array} routes - 路由配置
 */
export const validateRoutes = (routes) => {
  console.group('✅ 路由配置验证')

  routes.forEach((route, index) => {

  // 检查重复的路由名称
  const routeNames = routes.map(r => r.name).filter(Boolean)
  const duplicateNames = routeNames.filter((name, index) => routeNames.indexOf(name) !== index)
  
  if (duplicateNames.length > 0) {

  }
  
  // 检查重复的路由路径
  const routePaths = routes.map(r => r.path).filter(Boolean)
  const duplicatePaths = routePaths.filter((path, index) => routePaths.indexOf(path) !== index)
  
  if (duplicatePaths.length > 0) {

  }
  
  console.groupEnd()
}

/**
 * 打印权限数据
 * @param {Array} permissions - 权限数据
 */
export const debugPermissions = (permissions) => {
  console.group('🔐 权限数据结构分析')

  if (!Array.isArray(permissions)) {

    console.groupEnd()
    return
  }

  console.groupEnd()
}

export default {
  debugMenus,
  analyzeComponentPaths,
  validateRoutes,
  debugPermissions
} 