<template>
  <div class="settings-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
          <h1 class="page-title">系统设置</h1>
      </div>
      <div class="header-actions">
        <el-button @click="resetSettings">
          <el-icon><Refresh /></el-icon>
          重置设置
        </el-button>
        <el-button type="primary" @click="saveSettings">
          <el-icon><Check /></el-icon>
          保存设置
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <div class="settings-content">
        <!-- 设置导航 -->
        <div class="settings-nav">
          <div class="nav-tabs">
            <div
              v-for="tab in settingsTabs"
              :key="tab.key"
              class="nav-item"
              :class="{ active: activeTab === tab.key }"
              @click="handleTabChange(tab.key)"
            >
              <el-icon><component :is="tab.icon" /></el-icon>
              <span>{{ tab.title }}</span>
            </div>
          </div>
        </div>

        <!-- 设置内容 -->
        <div class="settings-panel">
          <!-- 个人设置 -->
          <PersonalSettings v-if="activeTab === 'personal'" :settings="settings" />

          <!-- 基本设置 -->
          <GeneralSettings v-if="activeTab === 'general'" :settings="settings" />

          <!-- 通知设置 -->
          <NotificationSettings v-if="activeTab === 'notifications'" :settings="settings" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Refresh, Check } from '@element-plus/icons-vue'
import PersonalSettings from './components/PersonalSettings.vue'
import GeneralSettings from './components/GeneralSettings.vue'
import NotificationSettings from './components/NotificationSettings.vue'
import { userApi } from '@/api'
import { useAppStore } from '@/stores/app'

const route = useRoute()
const router = useRouter()

// 使用 app store
const appStore = useAppStore()

// 当前激活的标签页
const activeTab = ref('personal')

// 根据URL参数设置初始标签页
onMounted(() => {
  const tabParam = route.query.tab
  if (tabParam && ['personal', 'general', 'notifications'].includes(tabParam)) {
    activeTab.value = tabParam
  } else {
    // 如果没有URL参数，默认显示个人设置
    activeTab.value = 'personal'
  }

  // 加载当前用户信息
  loadCurrentUserInfo()
})

// 监听URL参数变化
watch(() => route.query.tab, (newTab) => {
  if (newTab && ['personal', 'general', 'notifications'].includes(newTab)) {
    activeTab.value = newTab
  } else if (!newTab) {
    // 如果URL参数被清除，默认显示个人设置
    activeTab.value = 'personal'
  }
})

// 处理标签页切换
const handleTabChange = (tab) => {
  activeTab.value = tab
  // 更新URL参数
  router.push({
    path: '/home/system/settings',
    query: { tab }
  })
}

// 设置标签页
const settingsTabs = ref([
  { key: 'personal', title: '个人中心', icon: 'User' },
  { key: 'general', title: '基本设置', icon: 'Setting' },
  { key: 'notifications', title: '通知设置', icon: 'Bell' }
])

// 设置数据
const settings = reactive({
  // 个人设置
  username: '',
  email: '',
  phone: '',
  bio: '',
  language: 'zh-CN',
  timezone: 'UTC+8',

  // 基本设置
  systemName: '知录Admin',
  systemDescription: '现代化管理系统',

  // 外观设置
  theme: 'light',
  primaryColor: '#6366f1',
  sidebarWidth: 260,
  sidebarPosition: 'left',
  sidebarBehavior: ['collapsible', 'fixed'],
  borderRadius: 12,
  shadowLevel: 'medium',
  animations: true,

  // 通知设置
  emailNotifications: true,
  browserNotifications: true,
  notificationSound: false,
  notificationTypes: ['system', 'security']
})

// 加载当前用户信息
const loadCurrentUserInfo = async () => {
  try {
    // 从localStorage获取当前用户信息（正确的key是 zapi_user_info）
    const userInfoStr = localStorage.getItem('zapi_user_info')

    if (userInfoStr) {
      const userInfo = JSON.parse(userInfoStr)

      // 如果有用户ID，获取完整用户信息（字段名是 id 不是 userId）
      if (userInfo.id) {
        const response = await userApi.getUserDetail(userInfo.id)

        if (response.code === 200) {
          const user = response.data
          settings.username = user.realName || user.username
          settings.email = user.email || ''
          settings.phone = user.phone || ''
          settings.bio = user.bio || '系统管理员，负责系统的日常维护和管理工作。'
        } else {
          console.error('获取用户详情失败:', response.message)
          ElMessage.error('获取用户详情失败：' + response.message)
        }
      } else {
        console.warn('localStorage中的用户信息没有id字段，使用基本信息')
        // 如果没有用户ID，使用基本信息
        settings.username = userInfo.username || '管理员'
        settings.email = userInfo.email || ''
        settings.phone = userInfo.phone || ''
      }
    } else {
      console.warn('localStorage中没有找到 zapi_user_info')
      ElMessage.warning('未找到用户登录信息，请重新登录')
    }
  } catch (error) {
    console.error('加载用户信息失败:', error)
    ElMessage.error('加载用户信息失败：' + error.message)
    // 使用默认值
    settings.username = '管理员'
    settings.email = 'admin@example.com'
  }
}

// 保存设置
const saveSettings = () => {
  // 同步外观设置到 app store
  appStore.setTheme(settings.theme)
  appStore.setPrimaryColor(settings.primaryColor)
  appStore.setSidebarWidth(settings.sidebarWidth)

  // 这里可以调用API保存其他设置到后端
  ElMessage.success('设置保存成功！')
}

// 重置设置
const resetSettings = () => {
  // 重置为默认设置
  const defaultPrimaryColor = '#6366f1'
  const defaultTheme = 'light'
  const defaultSidebarWidth = 260

  Object.assign(settings, {
    // 个人设置
    username: '管理员',
    email: 'admin@example.com',
    phone: '138****8888',
    bio: '系统管理员，负责系统的日常维护和管理工作。',
    language: 'zh-CN',
    timezone: 'UTC+8',

    // 基本设置
    systemName: '知录Admin',
    systemDescription: '现代化管理系统',
    theme: defaultTheme,
    primaryColor: defaultPrimaryColor,
    sidebarWidth: defaultSidebarWidth,
    sidebarPosition: 'left',
    sidebarBehavior: ['collapsible', 'fixed'],
    borderRadius: 12,
    shadowLevel: 'medium',
    animations: true,
    emailNotifications: true,
    browserNotifications: true,
    notificationSound: false,
    notificationTypes: ['system', 'security']
  })

  // 同步到 app store
  appStore.setTheme(defaultTheme)
  appStore.setPrimaryColor(defaultPrimaryColor)
  appStore.setSidebarWidth(defaultSidebarWidth)

  ElMessage.info('设置已重置为默认值')
}
</script>

<style scoped>
.settings-page {
  padding: 24px;
  background: var(--el-bg-color-page);
  min-height: 100vh;
}

/* 页面头部 */
.header-content {
  flex: 1;
}

.page-title-section {
  margin-bottom: 16px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.header-actions :deep(.el-button) {
  border-radius: 8px;
  border: none;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.header-actions :deep(.el-button--primary) {
  background: var(--el-color-primary);
  color: white;
}

.header-actions :deep(.el-button--primary:hover) {
  background: var(--el-color-primary-dark-2);
}

.header-actions :deep(.el-button .el-icon) {
  font-size: 16px;
}

/* 主要内容区域 */
.main-content {
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
  overflow: hidden;
}

.settings-content {
  padding: 24px;
}

/* 设置导航 */
.settings-nav {
  margin-bottom: 24px;
  padding: 16px;
  background: var(--el-fill-color-light);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-lighter);
}

.nav-tabs {
  display: flex;
  gap: 8px;
  overflow-x: auto;
  flex-wrap: wrap;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  color: var(--el-text-color-regular);
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.3s ease;
  white-space: nowrap;
  flex-shrink: 0;
  border: 1px solid transparent;
}

.nav-item:hover {
  background: var(--el-fill-color-lighter);
  color: var(--el-color-primary);
  border-color: var(--el-color-primary-light-7);
}

.nav-item.active {
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
  border-color: var(--el-color-primary);
}

.nav-item .el-icon {
  font-size: 16px;
}

/* 设置面板 */
.settings-panel {
  background: var(--el-bg-color);
  border-radius: 8px;
  padding: 24px;
  min-height: 500px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 16px;
  }

  .header-actions {
    width: 100%;
    justify-content: flex-start;
  }

  .nav-tabs {
    flex-direction: column;
  }

  .nav-item {
    width: 100%;
    justify-content: flex-start;
  }

  .settings-content {
    padding: 16px;
  }
}
</style>
