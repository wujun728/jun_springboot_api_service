<template>
  <div class="layout">
    <!-- 顶部导航栏 -->
    <header class="top-header">
      <!-- 左侧：Logo + 项目名称 -->
      <div class="header-left">
        <div class="logo" @click="router.push('/home')">
          <img src="/logo3.png" alt="知录Admin" class="logo-img">
          <span class="project-name">知录Admin</span>
        </div>
      </div>

      <!-- 中间：模块菜单 -->
      <nav class="header-nav">
        <div
          v-for="module in topModules"
          :key="module.id"
          class="nav-item"
          :class="{ active: currentModule?.id === module.id }"
          @click="selectModule(module)"
        >
          <el-icon><component :is="module.icon" /></el-icon>
          <span class="nav-text">{{ module.menuName }}</span>
        </div>
      </nav>

      <!-- 右侧：通知 + 用户 -->
      <div class="header-right">
        <!-- 搜索 -->
        <el-button type="text" class="header-action">
          <el-icon><Search /></el-icon>
        </el-button>

        <!-- 通知铃铛 -->
        <el-badge :value="unreadCount" :max="99" :hidden="unreadCount === 0" class="notification-badge">
          <el-button type="text" class="header-action" @click="goToNotifications">
            <el-icon><Bell /></el-icon>
          </el-button>
        </el-badge>

        <!-- 用户下拉菜单 -->
        <el-dropdown @command="handleDropdownCommand" class="user-dropdown-wrapper">
          <div class="user-info">
            <div class="user-avatar">
              <el-icon><User /></el-icon>
            </div>
            <span class="user-name">{{ userStore.displayName }}</span>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人资料
              </el-dropdown-item>
              <el-dropdown-item command="appearance">
                <el-icon><Brush /></el-icon>
                外观设置
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>
                系统设置
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon>
                退出
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="main-container">
      <!-- 左侧：二级菜单 -->
      <aside class="sidebar" :class="{ collapsed: isCollapsed }" v-if="currentModule && hasSubMenus && !isHomePage">
        <div class="sidebar-header">
          <el-icon class="module-icon" @click="isCollapsed && toggleCollapse()"><component :is="currentModule.icon" /></el-icon>
          <span v-show="!isCollapsed" class="module-title">{{ currentModule.menuName }}</span>
          <el-button
            type="text"
            @click="toggleCollapse"
            class="collapse-btn"
            v-show="!isCollapsed"
          >
            <el-icon><Fold /></el-icon>
          </el-button>
        </div>

        <nav class="sidebar-nav">
          <menu-item-recursive
            v-for="menu in currentSubMenus"
            :key="menu.id"
            :menu="menu"
            :level="0"
            :is-collapsed="isCollapsed"
            @navigate="navigateToMenu"
          />
        </nav>

        <!-- 展开/收起按钮（收起状态） -->
        <div class="sidebar-footer" v-if="isCollapsed">
          <el-button type="text" @click="toggleCollapse" class="expand-btn">
            <el-icon><Expand /></el-icon>
          </el-button>
        </div>
      </aside>

      <!-- 主内容区 -->
      <main class="main-content">
        <!-- 面包屑导航 -->
        <div class="breadcrumb-bar" v-if="currentPageTitle && !isHomePage">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item @click="router.push('/home')" style="cursor: pointer;">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <router-view />
      </main>
    </div>

    <!-- 外观设置抽屉 -->
    <el-drawer
      v-model="appearanceDrawerVisible"
      title="外观设置"
      direction="rtl"
      size="680px"
      :close-on-click-modal="false"
    >
      <AppearanceDrawer />
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { ElNotification } from 'element-plus'
import {
  Search,
  Bell,
  User,
  Setting,
  SwitchButton,
  ArrowDown,
  Fold,
  Expand,
  ArrowRight,
  ArrowDown as ArrowDownIcon,
  Brush
} from '@element-plus/icons-vue'
import { messageApi } from '@/api'
import notificationSocket from '@/utils/notificationSocket'
import MenuItemRecursive from './MenuItemRecursive.vue'
import AppearanceDrawer from '@/components/AppearanceDrawer.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const appStore = useAppStore()

const isCollapsed = ref(false)
const unreadCount = ref(0)
const currentModule = ref(null)
const appearanceDrawerVisible = ref(false)
let unsubscribeSocket = null

// 处理WebSocket消息
const handleSocketMessage = (event) => {
  const payload = event?.payload
  if (!payload) return

  if (route.path !== '/home/notifications') {
    ElNotification({
      title: payload.title || '系统通知',
      message: payload.content || '您收到一条新的通知',
      type: 'info',
      duration: 5000
    })
  }

  window.dispatchEvent(new CustomEvent('notification:incoming', { detail: payload }))
  refreshUnreadCount()
}

// 刷新未读通知数量
const refreshUnreadCount = async () => {
  const userId = userStore.userInfo?.id
  if (!userId) {
    unreadCount.value = 0
    return
  }
  try {
    const response = await messageApi.getUnreadCount(userId)
    unreadCount.value = Number(response?.data || 0)
  } catch (error) {
    console.error('获取未读通知数量失败', error)
  }
}

const handleUnreadRefresh = () => {
  refreshUnreadCount()
}

// 计算当前页面标题
const currentPageTitle = computed(() => {
  const currentRoute = router.currentRoute.value
  return currentRoute.meta?.title || ''
})

// 判断是否为首页
const isHomePage = computed(() => {
  return route.path === '/home/dashboard' || route.path === '/home'
})

// 计算属性
const userMenus = computed(() => {
  return JSON.parse(JSON.stringify(userStore.userMenus || []))
})

// 过滤可见菜单
const visibleMenus = computed(() => {
  return filterVisibleMenus(userMenus.value)
})

// 顶部模块菜单（一级目录）
const topModules = computed(() => {
  return visibleMenus.value.filter(menu => menu.menuType === 1)
})

// 当前模块的子菜单
const currentSubMenus = computed(() => {
  if (!currentModule.value) return []
  return currentModule.value.children || []
})

// 是否有子菜单
const hasSubMenus = computed(() => {
  return currentSubMenus.value.length > 0
})

// 过滤可见菜单的方法
const filterVisibleMenus = (menus) => {
  if (!menus || !Array.isArray(menus)) {
    return []
  }

  return menus
    .filter(menu => menu.status === 1 && menu.visible === 1)
    .map(menu => {
      if (menu.children && menu.children.length > 0) {
        const visibleChildren = filterVisibleMenus(menu.children)
        return {
          ...menu,
          children: visibleChildren
        }
      }
      return menu
    })
    .filter(menu => {
      if (menu.menuType === 1) {
        return menu.children && menu.children.length > 0
      }
      return true
    })
}

// 切换侧边栏
const toggleCollapse = () => {
  isCollapsed.value = !isCollapsed.value
}

// 递归查找第一个真正的菜单项（非目录类型）
const findFirstRealMenu = (menu) => {
  // 如果有子菜单，说明是目录类型，需要继续递归查找
  if (menu.children && menu.children.length > 0) {
    for (const child of menu.children) {
      const found = findFirstRealMenu(child)
      if (found) return found
    }
    return null
  }
  // 如果没有子菜单且有path，说明是真正的菜单项（叶子节点）
  if (menu.path && menu.path !== '') {
    return menu
  }
  return null
}

// 选择模块
const selectModule = (module) => {
  currentModule.value = module
  // 如果有子菜单，递归查找第一个真正的菜单项（叶子节点）
  if (module.children && module.children.length > 0) {
    const firstRealMenu = findFirstRealMenu(module.children[0])
    if (firstRealMenu) {
      navigateToMenu(firstRealMenu)
    } else {
      // 如果找不到真正的菜单项，尝试导航到第一个子菜单
      navigateToMenu(module.children[0])
    }
  }
}

// 导航到菜单
const navigateToMenu = (menu) => {
  const path = getMenuPath(menu.path)
  router.push(path)
}

// 处理菜单路径
const getMenuPath = (path) => {
  if (path === '/home' || path === '') {
    return '/home'
  }
  if (path.startsWith('/')) {
    return `/home${path}`
  }
  return `/home/${path}`
}

// 跳转到通知页面
const goToNotifications = () => {
  router.push('/home/notifications/message/index')
}

// 处理用户下拉菜单命令
const handleDropdownCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/home/system/settings?tab=personal')
      break
    case 'appearance':
      appearanceDrawerVisible.value = true
      break
    case 'settings':
      router.push('/home/system/settings?tab=general')
      break
    case 'logout':
      userStore.logout()
      router.push('/login')
      break
  }
}

// 根据当前路由自动选择模块
const autoSelectModule = () => {
  const currentPath = route.path

  // 遍历所有模块，找到匹配的
  for (const module of topModules.value) {
    if (module.children) {
      for (const child of module.children) {
        const childPath = getMenuPath(child.path)
        if (currentPath === childPath || currentPath.startsWith(childPath + '/')) {
          currentModule.value = module
          return
        }
      }
    }
  }

  // 如果没有匹配，默认选择第一个模块
  if (topModules.value.length > 0 && !currentModule.value) {
    currentModule.value = topModules.value[0]
  }
}

// 监听路由变化
watch(() => route.path, () => {
  autoSelectModule()
}, { immediate: true })

// 监听用户信息变化
watch(
  () => userStore.userInfo?.id,
  (id) => {
    if (id) {
      notificationSocket.connect(id)
      if (!unsubscribeSocket) {
        unsubscribeSocket = notificationSocket.subscribe(handleSocketMessage)
      }
      refreshUnreadCount()
    } else {
      if (unsubscribeSocket) {
        unsubscribeSocket()
        unsubscribeSocket = null
      }
      notificationSocket.disconnect()
      unreadCount.value = 0
    }
  },
  { immediate: true }
)

// 生命周期
onMounted(() => {
  window.addEventListener('notification:refresh-unread', handleUnreadRefresh)
  autoSelectModule()
})

onBeforeUnmount(() => {
  if (unsubscribeSocket) {
    unsubscribeSocket()
    unsubscribeSocket = null
  }
  notificationSocket.disconnect()
  window.removeEventListener('notification:refresh-unread', handleUnreadRefresh)
})
</script>

<style lang="scss" scoped>
.layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: var(--bg-primary);
}

// 顶部导航栏
.top-header {
  height: 56px;
  background: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: var(--shadow-md);
  z-index: 100;

  .header-left {
    display: flex;
    align-items: center;

    .logo {
      display: flex;
      align-items: center;
      gap: 12px;
      cursor: pointer;
      transition: opacity 0.2s;

      &:hover {
        opacity: 0.9;
      }

      .logo-img {
        height: 32px;
        width: auto;
      }

      .project-name {
        font-size: 18px;
        font-weight: 600;
        color: #ffffff;
        white-space: nowrap;
      }
    }
  }

  .header-nav {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 1;
    justify-content: center;
    max-width: 800px;
    margin: 0 auto;

    .nav-item {
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 8px 20px;
      color: rgba(255, 255, 255, 0.85);
      font-size: 14px;
      cursor: pointer;
      border-radius: 4px;
      transition: all 0.2s;
      user-select: none;
      white-space: nowrap;

      &:hover {
        background: rgba(255, 255, 255, 0.1);
        color: #ffffff;
      }

      &.active {
        background: rgba(255, 255, 255, 0.15);
        color: #ffffff;
        font-weight: 500;
      }

      .el-icon {
        font-size: 16px;
        flex-shrink: 0;
      }

      .nav-text {
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 12px;

    .header-action {
      color: #ffffff !important;
      font-size: 18px;
      padding: 8px;
      border-radius: 4px;
      background-color: transparent !important;
      border-color: transparent !important;
      transition: all 0.2s;

      &:hover {
        background-color: rgba(255, 255, 255, 0.1) !important;
        border-color: transparent !important;
      }

      // 确保图标颜色也是白色
      :deep(.el-icon) {
        color: #ffffff !important;
      }
    }

    .notification-badge {
      :deep(.el-badge__content) {
        background-color: #ff4d4f;
        border: 2px solid var(--primary-color);
      }
    }

    .user-dropdown-wrapper {
      .user-info {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 4px 12px;
        border-radius: 4px;
        cursor: pointer;
        transition: all 0.2s;

        &:hover {
          background: rgba(255, 255, 255, 0.1);
        }

        .user-avatar {
          width: 28px;
          height: 28px;
          border-radius: 50%;
          background: rgba(255, 255, 255, 0.2);
          display: flex;
          align-items: center;
          justify-content: center;
          color: #ffffff;
        }

        .user-name {
          color: #ffffff;
          font-size: 14px;
          font-weight: 500;
        }

        .dropdown-icon {
          color: rgba(255, 255, 255, 0.85);
          font-size: 12px;
        }
      }
    }
  }
}

// 主容器
.main-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

// 侧边栏
.sidebar {
  width: 200px;
  background: var(--bg-card);
  border-right: 1px solid var(--border-color);
  transition: width 0.3s ease;
  display: flex;
  flex-direction: column;

  &.collapsed {
    width: 60px;
  }

  .sidebar-header {
    height: 48px;
    display: flex;
    align-items: center;
    padding: 0 16px;
    border-bottom: 1px solid var(--border-color);
    gap: 8px;
    flex-shrink: 0;

    .module-icon {
      font-size: 20px;
      color: var(--primary-color);
      flex-shrink: 0;
      cursor: pointer;
    }

    .module-title {
      flex: 1;
      font-size: 15px;
      font-weight: 600;
      color: var(--text-primary);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .collapse-btn {
      color: var(--text-secondary);
      padding: 4px;
      flex-shrink: 0;

      &:hover {
        color: var(--primary-color);
      }
    }
  }

  .sidebar-nav {
    flex: 1;
    overflow-y: auto;
    padding: 8px 0;

    .menu-item {
      display: flex;
      align-items: center;
      gap: 10px;
      padding: 12px 16px;
      margin: 2px 8px;
      color: var(--text-secondary);
      font-size: 14px;
      cursor: pointer;
      border-radius: 6px;
      transition: all 0.2s;
      white-space: nowrap;

      &:hover {
        background: var(--bg-tertiary);
        color: var(--text-primary);
      }

      &.active {
        background: var(--primary-light);
        color: var(--primary-color);
        font-weight: 500;
      }

      .el-icon {
        font-size: 18px;
      }
    }
  }

  .sidebar-footer {
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-top: 1px solid var(--border-color);

    .expand-btn {
      color: var(--text-secondary);
      font-size: 16px;

      &:hover {
        color: var(--primary-color);
      }
    }
  }

  &.collapsed {
    .sidebar-header {
      justify-content: center;
      padding: 0;

      .module-title,
      .collapse-btn {
        display: none;
      }

      .module-icon {
        cursor: pointer;
      }
    }

    .sidebar-nav {
      .menu-item {
        justify-content: center;
        padding: 12px 0;
        margin: 2px 8px;
      }
    }
  }
}

// 主内容区
.main-content {
  flex: 1;
  overflow-y: auto;
  background: var(--bg-primary);
  display: flex;
  flex-direction: column;
}

// 面包屑导航栏
.breadcrumb-bar {
  height: 48px;
  display: flex;
  align-items: center;
  padding: 0 24px;
  background: var(--bg-primary);
  border-bottom: 1px solid var(--border-color);
  flex-shrink: 0;

  :deep(.el-breadcrumb__item) {
    .el-breadcrumb__inner {
      color: var(--text-secondary);

      &.is-link:hover {
        color: var(--primary-color);
      }
    }

    &:last-child .el-breadcrumb__inner {
      color: var(--text-primary);
    }
  }
}

// 响应式
@media (max-width: 768px) {
  .top-header {
    padding: 0 16px;

    .header-nav {
      display: none;
    }

    .header-left .logo .project-name {
      display: none;
    }
  }

  .sidebar {
    position: fixed;
    left: 0;
    top: 56px;
    height: calc(100vh - 56px);
    z-index: 99;
    transform: translateX(-100%);

    &.show {
      transform: translateX(0);
    }
  }
}
</style>
