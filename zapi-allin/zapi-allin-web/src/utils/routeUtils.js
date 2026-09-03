/**
 * 动态路由工具函数
 */

import Layout from '@/layout/index.vue'

/**
 * 使用 Vite 的 import.meta.glob 预加载所有 views 下的 Vue 组件
 * 这样可以避免动态导入时的路径解析问题
 */
const modules = import.meta.glob('@/views/**/*.vue')
  
/**
 * 智能匹配模块路径
 * 因为 import.meta.glob 返回的路径格式可能是 /src/views/... 或 @/views/...
 * @param {String} componentPath 组件路径
 * @returns {Function|null} 组件加载函数
 */
function findModuleByPath(componentPath) {
  const cleanPath = componentPath.trim().replace(/^\/+|\/+$/g, '')
  
  // 尝试多种路径格式
  const patterns = [
    `@/views/${cleanPath}.vue`,
    `/src/views/${cleanPath}.vue`,
    `./src/views/${cleanPath}.vue`,
    `../views/${cleanPath}.vue`,
  ]
  
  // 遍历所有 modules，尝试匹配
  for (const [modulePath, moduleLoader] of Object.entries(modules)) {
    // 精确匹配
    if (patterns.includes(modulePath)) {
      return moduleLoader
    }
    
    // 模糊匹配：路径末尾相同
    if (modulePath.endsWith(`/${cleanPath}.vue`) || modulePath.endsWith(`${cleanPath}.vue`)) {
      return moduleLoader
    }
  }
  
  return null
}

/**
 * 根据后端菜单数据生成路由配置
 * @param {Array} menuList 后端返回的菜单数据
 * @returns {Array} 路由配置数组
 */
export function generateRoutes(menuList) {
  const routes = []

  menuList.forEach(menu => {
    // 只检查菜单状态，不检查visible字段
    // 这样隐藏的菜单仍然可以通过直接URL访问
    if (menu.status !== 1) {
      return
    }

    // 根据菜单类型处理不同的路由配置
    if (menu.menuType === 1) {
      // 目录类型 - 处理子菜单，不创建顶级路由
      if (menu.children && menu.children.length > 0) {
        // 递归处理子菜单，将子菜单作为平级路由添加
        const childRoutes = generateRoutes(menu.children)
        routes.push(...childRoutes)
      }
    } else if (menu.menuType === 2) {
      // 菜单类型 - 创建实际的路由
      const route = {
        path: convertToChildPath(menu.path), // 转换为子路由路径
        name: generateRouteName(menu.menuCode),
        component: loadComponent(menu.component),
        meta: {
          title: menu.menuName,
          icon: menu.icon,
          menuCode: menu.menuCode,
          menuType: menu.menuType,
          sort: menu.sort,
          fullPath: menu.path // 保存原始完整路径
        }
      }
      routes.push(route)
    }
  })

  return routes
}

/**
 * 将完整路径转换为子路由路径
 * @param {String} fullPath 完整路径，如 /system/user
 * @returns {String} 子路由路径，如 system/user
 */
function convertToChildPath(fullPath) {
  if (!fullPath) return ''

  // 移除开头的 /
  let path = fullPath.startsWith('/') ? fullPath.slice(1) : fullPath

  return path
}

/**
 * 获取实际的文件路径
 * @param {String} componentPath 后端返回的组件路径
 * @returns {String} 实际的文件路径
 */
function getActualFilePath(componentPath) {
  if (!componentPath || componentPath === 'Layout') {
    return componentPath
  }

  // 清理路径：去除前后空格和斜杠
  let path = componentPath.trim().replace(/^\/+|\/+$/g, '')

  // 如果已经是完整的@/views/路径，确保有.vue后缀
  if (path.startsWith('@/views/')) {
    return path.endsWith('.vue') ? path : `${path}.vue`
  }

  // 如果是@/开头但不是@/views/，也加上.vue后缀
  if (path.startsWith('@/')) {
    return path.endsWith('.vue') ? path : `${path}.vue`
  }

  // 默认情况：添加@/views/前缀和.vue后缀
  // 后端返回的路径格式如：dataset/DatasetHomeIndex、form/list/index 等
  return `@/views/${path}.vue`
}

/**
 * 动态加载Vue组件
 * @param {String} componentPath 组件路径（后端返回的component字段）
 * @returns {Function} 组件加载函数
 * 
 * 路径转换规则：
 * - 后端返回: dataset/DatasetHomeIndex -> 前端: @/views/dataset/DatasetHomeIndex.vue
 * - 后端返回: form/list/index -> 前端: @/views/form/list/index.vue
 * - 后端返回: Layout -> 前端: Layout组件（特殊处理）
 */
function loadComponent(componentPath) {
  if (!componentPath) {

    return () => import('@/views/error/404.vue')
  }

  // 特殊处理：Layout组件（不在views目录下）
  if (componentPath === 'Layout') {

    return Layout
  }

  // 使用智能匹配函数查找组件
  const moduleLoader = findModuleByPath(componentPath)
  
  if (moduleLoader) {

    return moduleLoader
  }

  // 如果智能匹配失败，显示详细的调试信息

  // 显示所有包含该组件名的路径，帮助调试
  const parts = componentPath.split('/')
  const matchingPaths = Object.keys(modules).filter(k => {
    return parts.some(part => part && k.toLowerCase().includes(part.toLowerCase()))
  })

  if (matchingPaths.length > 0) {
    console.warn(`组件 ${componentPath} 未找到，可能的匹配路径:`, matchingPaths)
  } else {
    console.warn(`组件 ${componentPath} 未找到，没有匹配的路径`)
  }
  
  return () => import('@/views/error/404.vue')
}

/**
 * 根据menuCode生成路由名称
 * @param {String} menuCode 菜单编码
 * @returns {String} 路由名称
 */
function generateRouteName(menuCode) {
  if (!menuCode) return 'Unknown'

  // 将menuCode转换为PascalCase格式
  return menuCode
    .split(/[:\-_]/)
    .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join('')
}

/**
 * 将嵌套路由结构扁平化
 * @param {Array} routes 路由数组
 * @param {String} parentPath 父路径
 * @returns {Array} 扁平化的路由数组
 */
export function flattenRoutes(routes, parentPath = '') {
  const flattened = []

  routes.forEach(route => {
    const fullPath = parentPath ? `${parentPath}/${route.path}`.replace(/\/+/g, '/') : route.path

    flattened.push({
      ...route,
      path: fullPath,
      children: undefined
    })

    if (route.children && route.children.length > 0) {
      flattened.push(...flattenRoutes(route.children, fullPath))
    }
  })

  return flattened
}

/**
 * 过滤出有效的菜单项（用于侧边栏显示）
 * @param {Array} menuList 菜单数据
 * @returns {Array} 过滤后的菜单数据
 */
export function filterValidMenus(menuList) {
  return menuList.filter(menu => {
    // 过滤条件：状态正常、可见、有权限
    if (menu.status !== 1 || menu.visible !== 1) {
      return false
    }

    // 如果有子菜单，递归过滤子菜单
    if (menu.children && menu.children.length > 0) {
      menu.children = filterValidMenus(menu.children)
    }

    return true
  })
}

/**
 * 根据路由路径查找对应的菜单项
 * @param {Array} menuList 菜单数据
 * @param {String} path 路由路径
 * @returns {Object|null} 找到的菜单项
 */
export function findMenuByPath(menuList, path) {
  for (const menu of menuList) {
    if (menu.path === path) {
      return menu
    }

    if (menu.children && menu.children.length > 0) {
      const found = findMenuByPath(menu.children, path)
      if (found) {
        return found
      }
    }
  }

  return null
}

/**
 * 获取组件映射表（已废弃，保留用于兼容性）
 * 新版本完全依赖动态导入，不再使用硬编码的组件映射
 * @deprecated 不再需要硬编码组件路径，所有路径都通过后端数据动态生成
 */
function getComponentMap() {
  // 返回空对象，保持向后兼容
  return {}
}

/**
 * 调试函数：打印路由生成详情
 * @param {Array} menuList 菜单数据
 * @returns {Array} 生成的路由配置
 */
export function debugRouteGeneration(menuList) {
  const routes = generateRoutes(menuList)

  routes.forEach((route, index) => {
    // console.log(`路由 ${index + 1}:`, route)
  })

  return routes
}
