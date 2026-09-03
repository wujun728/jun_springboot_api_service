import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout/index.vue'
import { generateRoutes, debugRouteGeneration } from '@/utils/routeUtils'

// 静态路由（不需要权限的路由）
const staticRoutes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/app-config',
    name: 'AppConfig',
    component: () => import('@/views/workbench/application/config.vue'),
    meta: { title: '应用配置' }
  },
  {
    path: '/home',
    name: 'Home',
    component: Layout,
    redirect: '/home/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'Odometer' }
      },
      // 表单设计器路由（支持编辑参数）
      {
        path: 'form/design',
        name: 'FormDesign',
        component: () => import('@/views/form/design/index.vue'),
        meta: { title: '表单设计', icon: 'Edit' }
      },
      // 表格设计器路由（原始列表设计器）
      {
        path: 'sheet/design',
        name: 'SheetDesign',
        component: () => import('@/views/sheet/design/index.vue'),
        meta: { title: '表格设计', icon: 'Grid' }
      },
      // 列表预览路由
      {
        path: 'list-preview',
        name: 'ListPreview',
        component: () => import('@/views/sheet/preview/index.vue'),
        meta: { title: '列表预览', icon: 'View' }
      },
      // 文件管理页面
      {
        path: 'file',
        name: 'FileManagement',
        component: () => import('@/views/file/index.vue'),
        meta: { title: '文件管理', icon: 'Files' }
      },

      // 合同预览页面
      {
        path: 'contract/preview',
        name: 'PreviewContract',
        component: () => import('@/views/contract/PreviewContract.vue'),
        meta: { title: '合同预览', hidden: true }
      },
      // 合同盖章页面（静态路由，不需要菜单权限）
      {
        path: 'contract/sign',
        name: 'ContractSign',
        component: () => import('@/views/contract/sign.vue'),
        meta: { title: '合同盖章', icon: 'Stamp' }
      }

    ]
  }
]

// 存储动态路由
let dynamicRoutes = []

const routes = [...staticRoutes]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 动态添加路由的方法
 * @param {Array} menuList 后端返回的菜单数据
 */
export const addDynamicRoutes = (menuList) => {

  try {
    // 清除之前添加的动态路由
    clearDynamicRoutes()

    // 生成动态路由
    const generatedRoutes = debugRouteGeneration(menuList)

    // 将动态路由作为 /home 的子路由添加
    generatedRoutes.forEach(route => {
      router.addRoute('Home', route) // 'Home' 是父路由名称
      dynamicRoutes.push(route) // 保存到数组中以便后续清理
    })

    // 动态路由添加完成后，添加404路由（确保它是最后匹配的）
    const notFoundRoute = {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/error/404.vue'),
      meta: { title: '页面不存在' }
    }

    // 检查是否已经有404路由，如果没有则添加
    if (!router.hasRoute('NotFound')) {
      router.addRoute(notFoundRoute)
    }
  } catch (error) {
    // 错误处理
  }
}

/**
 * 清除动态路由
 */
export const clearDynamicRoutes = () => {

  // 移除之前添加的动态路由
  dynamicRoutes.forEach(route => {
    if (router.hasRoute(route.name)) {
      router.removeRoute(route.name)
    }
  })

  // 移除404路由（在重新加载动态路由时会重新添加）
  if (router.hasRoute('NotFound')) {
    router.removeRoute('NotFound')
  }

  dynamicRoutes = []
}

/**
 * 获取当前动态路由
 */
export const getDynamicRoutes = () => {
  return dynamicRoutes
}

// 路由守卫
router.beforeEach(async (to, from, next) => {

  // 检查用户是否已登录
  const isLoggedIn = localStorage.getItem('zapi_token')

  // 如果访问登录页面，直接放行
  if (to.path === '/login') {
    // 如果已经登录，重定向到首页
    if (isLoggedIn) {
      next('/home')
    } else {
      next()
    }
    return
  }

  // 如果未登录且不是访问登录页面，重定向到登录页面
  if (!isLoggedIn) {
    next('/login')
    return
  }

  // 已登录，允许访问基本路由（dashboard、预览页面和应用配置页面）
  if (to.path === '/home/dashboard' || to.path === '/home' || to.path === '/' || to.path === '/home/list-preview' || to.path === '/app-config') {
    next()
    return
  }

  // 对于其他路由，检查是否需要加载动态路由
  if (isLoggedIn) {
    try {

      // 导入用户store
      const { useUserStore } = await import('@/stores/user')
      const userStore = useUserStore()

      // 如果动态路由未加载（页面刷新后会重置）
      // 增加 !userStore.dynamicRoutesLoaded 判断，防止在动态路由为空时（例如没有权限）陷入无限循环
      if (dynamicRoutes.length === 0 && !userStore.dynamicRoutesLoaded) {

        // 强制重新加载动态路由（页面刷新后需要重新加载）
        const success = await userStore.loadDynamicRoutes(true)

        if (success) {

          // 重新导航到目标路由
          next({ ...to, replace: true })
          return
        } else {

          next('/home')
          return
        }
      } else {

      }
    } catch (error) {

      // 发生错误时，尝试跳转到首页
      next('/home')
      return
    }
  }

  // 继续访问

  next()

})

export default router
