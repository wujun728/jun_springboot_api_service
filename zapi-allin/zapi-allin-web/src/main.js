/**
 * 知录Admin 管理系统 - 主入口文件
 *
 * 功能说明：
 * 1. 创建 Vue 应用实例
 * 2. 注册全局组件和插件
 * 3. 配置全局错误处理
 * 4. 初始化应用状态
 *
 * @author zhonglz
 * @version 1.0.0
 */

// 必须在所有其他导入之前加载 polyfills，修复浏览器环境兼容性问题
import './utils/polyfills'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import watermark from '@/utils/watermark'
import App from './App.vue'
import router from './router'
// 先引入 Element Plus 的 CSS
import 'element-plus/dist/index.css'
// 再引入我们的自定义样式，确保覆盖 Element Plus 默认样式
import './styles/index.scss'
// 强制覆盖日期控件样式
import './styles/date-picker-override.scss'

// 创建 Vue 应用实例
const app = createApp(App)

// 注册所有 Element Plus 图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}


// 全局错误处理
app.config.errorHandler = (err, vm, info) => {

  // 可以在这里添加错误上报逻辑
  // reportError(err, info, vm)
}

// 全局警告处理
app.config.warnHandler = (msg, vm, trace) => {

}

// 注册全局属性
app.config.globalProperties.$formatDate = (date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return ''
  const d = new Date(date)
    return d.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'

  })
}

app.config.globalProperties.$formatNumber = (num) => {
  if (typeof num !== 'number') return num
  return num.toLocaleString('zh-CN')
}

// 注册全局方法
app.config.globalProperties.$showMessage = (message, type = 'info') => {
  // 这里可以集成消息提示组件

}

// 使用插件
app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn,
  size: 'default',
  zIndex: 3000
})

// 确保 DOM 完全加载后再挂载应用
const mountApp = () => {
  try {
    // 添加额外的延迟，确保所有资源都加载完成
    setTimeout(() => {
      app.mount('#app')
      console.log('应用已成功挂载')
      
      // 初始化水印
      setTimeout(() => {
        watermark.init()
      }, 500)
    }, 100)
  } catch (error) {
    console.error('应用挂载失败:', error)
  }
}

// 如果 DOM 已经加载完成，直接挂载
if (document.readyState === 'loading') {
  document.addEventListener('DOMContentLoaded', mountApp)
} else {
  // 即使 DOM 已加载，也等待一下
  if (document.readyState === 'complete') {
    mountApp()
  } else {
    window.addEventListener('load', mountApp)
  }
}

// 初始化应用状态（异步，不阻塞应用启动）
const initApp = async () => {
  try {

    const userStore = useUserStore()
    const appStore = useAppStore()

    // 初始化应用配置（不依赖用户状态）
    try {
      await appStore.initAppConfig()
      // 设置主题
      appStore.setTheme(appStore.theme)
    } catch (appError) {

    }

    // 检查用户登录状态（可选）
    try {
      await userStore.checkAuthStatus()
    } catch (authError) {

    }

  } catch (error) {

  }
}

// 非阻塞方式初始化应用
setTimeout(initApp, 0)

// 开发环境下的调试信息
if (import.meta.env.DEV) {

}
