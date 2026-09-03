/**
 * 应用状态管理 Store
 *
 * 功能说明：
 * 1. 应用主题管理
 * 2. 应用配置管理
 * 3. 通知管理
 * 4. 系统状态管理
 * 5. 缓存管理
 *
 * @author ZAPI Team
 * @version 1.0.0
 */

import { defineStore } from "pinia";
import { ref, computed, watch } from "vue";
import { ElMessage } from "element-plus";

export const useAppStore = defineStore("app", () => {
  // ==================== 状态定义 ====================

  // 应用主题
  const theme = ref("light"); // dark, light, auto

  // 主色调
  const primaryColor = ref("#6366f1");

  // 侧边栏状态
  const sidebarCollapsed = ref(false);

  // 侧边栏宽度
  const sidebarWidth = ref(260);

  // 应用配置
  const appConfig = ref({
    // 基本配置
    appName: "知录Admin",
    appVersion: "1.0.0",
    appDescription: "现代化管理系统",

    // 界面配置
    showBreadcrumb: true,
    showTags: true,
    showFooter: true,
    showLogo: true,

    // 功能配置
    enableAnimation: true,
    enableSound: false,
    enableNotification: true,
    enableAutoSave: true,

    // 性能配置
    enableCache: true,
    cacheExpireTime: 30 * 60 * 1000, // 30分钟
    enableLazyLoad: true,
    enableVirtualScroll: true,

    // 安全配置
    enableCaptcha: false,
    enableTwoFactor: false,
    sessionTimeout: 30 * 60 * 1000, // 30分钟
    maxLoginAttempts: 5,

    // 数据配置
    pageSize: 20,
    maxPageSize: 100,
    enableExport: true,
    enableImport: true,

    // 通知配置
    notificationTypes: {
      success: true,
      warning: true,
      error: true,
      info: true,
    },

    // 主题配置
    themeConfig: {
      dark: {
        primary: "#6366f1",
        secondary: "#10b981",
        accent: "#f59e0b",
        danger: "#ef4444",
        success: "#10b981",
        warning: "#f59e0b",
        info: "#3b82f6",
      },
      light: {
        primary: "#6366f1",
        secondary: "#10b981",
        accent: "#f59e0b",
        danger: "#ef4444",
        success: "#10b981",
        warning: "#f59e0b",
        info: "#3b82f6",
      },
    },
  });

  // 通知列表
  const notifications = ref([]);

  // 系统状态
  const systemStatus = ref({
    online: true,
    serverTime: new Date(),
    lastSync: new Date(),
    memoryUsage: 0,
    cpuUsage: 0,
    diskUsage: 0,
  });

  // 缓存数据
  const cache = ref(new Map());

  // 加载状态
  const loadingStates = ref(new Map());

  // 错误状态
  const errorStates = ref(new Map());

  // 全局搜索关键词
  const globalSearchKeyword = ref("");

  // 当前页面标题
  const currentPageTitle = ref("");

  // 页面标签页
  const pageTabs = ref([]);

  // ==================== 计算属性 ====================

  // 当前主题配置
  const currentThemeConfig = computed(() => {
    return (
      appConfig.value.themeConfig[theme.value] ||
      appConfig.value.themeConfig.dark
    );
  });

  // 未读通知数量
  const unreadNotificationCount = computed(() => {
    return notifications.value.filter((notification) => !notification.read)
      .length;
  });

  // 重要通知数量
  const importantNotificationCount = computed(() => {
    return notifications.value.filter(
      (notification) => !notification.read && notification.important
    ).length;
  });

  // 系统是否在线
  const isOnline = computed(() => {
    return systemStatus.value.online;
  });

  // 缓存大小
  const cacheSize = computed(() => {
    return cache.value.size;
  });

  // ==================== 方法定义 ====================

  /**
   * 初始化应用配置
   * @returns {Promise<void>}
   */
  const initAppConfig = async () => {
    try {
      // 从本地存储恢复配置
      const savedConfig = localStorage.getItem("zapi_app_config");
      const savedTheme = localStorage.getItem("zapi_theme");
      const savedSidebarState = localStorage.getItem("zapi_sidebar_state");
      const savedPrimaryColor = localStorage.getItem("zapi_primary_color");

      if (savedConfig) {
        appConfig.value = { ...appConfig.value, ...JSON.parse(savedConfig) };
      }

      if (savedTheme) {
        theme.value = savedTheme;
      }

      if (savedSidebarState) {
        const sidebarState = JSON.parse(savedSidebarState);
        sidebarCollapsed.value = sidebarState.collapsed;
        sidebarWidth.value = sidebarState.width;
      }

      // 设置主题
      setTheme(theme.value);

      // 恢复主色调（在设置主题之后）
      if (savedPrimaryColor) {
        setPrimaryColor(savedPrimaryColor);
      }
    } catch (error) {
      console.error("初始化应用配置失败:", error);
    }
  };

  /**
   * 设置主题
   * @param {string} newTheme - 新主题
   */
  const setTheme = (newTheme) => {
    theme.value = newTheme;

    // 设置 data-theme 属性到 html 元素
    const root = document.documentElement;
    
    // 处理自动主题
    let actualTheme = newTheme;
    if (newTheme === 'auto') {
      // 检测系统主题偏好
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
      actualTheme = prefersDark ? 'dark' : 'light';
    }
    
    root.setAttribute('data-theme', actualTheme);

    // 更新CSS变量
    const themeConfig = currentThemeConfig.value;

    Object.entries(themeConfig).forEach(([key, value]) => {
      root.style.setProperty(`--${key}-color`, value);
    });

    const oldTheme = localStorage.getItem("zapi_theme");
    if (oldTheme !== newTheme) {
      // 保存到本地存储
      localStorage.setItem("zapi_theme", newTheme);
      addNotification({
        type: "info",
        title: "主题切换",
        message: `已切换到${newTheme === "dark" ? "深色" : newTheme === "light" ? "浅色" : "跟随系统"}主题`,
        duration: 3000,
      });
    }
  };

  /**
   * 设置主色调
   * @param {string} color - 主色调颜色值（如 #6366f1）
   */
  const setPrimaryColor = (color) => {
    primaryColor.value = color;

    // 动态更新 Element Plus 的 CSS 变量
    const root = document.documentElement;
    
    // 设置主色
    root.style.setProperty('--el-color-primary', color);
    
    // 计算并设置衍生色
    // light-3: 混入 10% 白色
    const light3 = mixColor('#ffffff', color, 0.1);
    const light5 = mixColor('#ffffff', color, 0.2);
    const light7 = mixColor('#ffffff', color, 0.7);
    const light9 = mixColor('#ffffff', color, 0.9);
    const dark2 = mixColor('#000000', color, 0.08);
    
    root.style.setProperty('--el-color-primary-light-3', light3);
    root.style.setProperty('--el-color-primary-light-5', light5);
    root.style.setProperty('--el-color-primary-light-7', light7);
    root.style.setProperty('--el-color-primary-light-9', light9);
    root.style.setProperty('--el-color-primary-dark-2', dark2);

    // 同时更新自定义 CSS 变量（用于侧边栏和其他自定义组件）
    root.style.setProperty('--primary-color', color);
    root.style.setProperty('--primary-hover', light3);
    root.style.setProperty('--primary-light', light9);
    
    // 更新基于主色的渐变和阴影
    const rgb = hexToRgb(color);
    root.style.setProperty('--gradient-primary', `linear-gradient(135deg, rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.1) 0%, rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.2) 100%)`);
    root.style.setProperty('--shadow-sm', `0 1px 3px 0 rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.08)`);
    root.style.setProperty('--shadow-md', `0 4px 12px -2px rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.1)`);
    root.style.setProperty('--shadow-lg', `0 10px 20px -4px rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.12)`);
    root.style.setProperty('--shadow-xl', `0 20px 30px -6px rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.15)`);
    
    // 更新边框颜色
    root.style.setProperty('--border-color', `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.15)`);
    root.style.setProperty('--border-light', `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.1)`);
    
    // 更新背景颜色
    root.style.setProperty('--bg-tertiary', `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.05)`);
    
    // 更新滚动条颜色
    root.style.setProperty('--scrollbar-thumb-color', `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.2)`);
    root.style.setProperty('--scrollbar-thumb-hover-color', `rgba(${rgb.r}, ${rgb.g}, ${rgb.b}, 0.3)`);

    // 更新 themeConfig 中的 primary 颜色
    appConfig.value.themeConfig.dark.primary = color;
    appConfig.value.themeConfig.light.primary = color;

    // 保存到本地存储
    localStorage.setItem('zapi_primary_color', color);

    addNotification({
      type: 'success',
      title: '主色调切换',
      message: `已切换主色调为 ${color}`,
      duration: 3000,
    });
  };

  /**
   * 颜色混合函数
   * @param {string} mixColor - 要混入的颜色
   * @param {string} baseColor - 基础颜色
   * @param {number} weight - 混入比例 (0-1)
   * @returns {string} 混合后的颜色
   */
  const mixColor = (mixColorHex, baseColorHex, weight) => {
    // 解析颜色
    const mix = hexToRgb(mixColorHex);
    const base = hexToRgb(baseColorHex);
    
    // 混合颜色
    const r = Math.round(mix.r * weight + base.r * (1 - weight));
    const g = Math.round(mix.g * weight + base.g * (1 - weight));
    const b = Math.round(mix.b * weight + base.b * (1 - weight));
    
    return rgbToHex(r, g, b);
  };

  /**
   * 十六进制颜色转 RGB
   * @param {string} hex - 十六进制颜色
   * @returns {object} RGB 对象
   */
  const hexToRgb = (hex) => {
    const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
      r: parseInt(result[1], 16),
      g: parseInt(result[2], 16),
      b: parseInt(result[3], 16)
    } : { r: 0, g: 0, b: 0 };
  };

  /**
   * RGB 转十六进制颜色
   * @param {number} r - 红色值
   * @param {number} g - 绿色值
   * @param {number} b - 蓝色值
   * @returns {string} 十六进制颜色
   */
  const rgbToHex = (r, g, b) => {
    return '#' + [r, g, b].map(x => {
      const hex = x.toString(16);
      return hex.length === 1 ? '0' + hex : hex;
    }).join('');
  };

  /**
   * 切换侧边栏状态
   */
  const toggleSidebar = () => {
    sidebarCollapsed.value = !sidebarCollapsed.value;

    // 保存状态
    localStorage.setItem(
      "zapi_sidebar_state",
      JSON.stringify({
        collapsed: sidebarCollapsed.value,
        width: sidebarWidth.value,
      })
    );
  };

  /**
   * 设置侧边栏宽度
   * @param {number} width - 宽度
   */
  const setSidebarWidth = (width) => {
    sidebarWidth.value = width;

    // 保存状态
    localStorage.setItem(
      "zapi_sidebar_state",
      JSON.stringify({
        collapsed: sidebarCollapsed.value,
        width: sidebarWidth.value,
      })
    );
  };

  /**
   * 更新应用配置
   * @param {Object} newConfig - 新配置
   */
  const updateAppConfig = (newConfig) => {
    appConfig.value = { ...appConfig.value, ...newConfig };

    // 保存到本地存储
    localStorage.setItem("zapi_app_config", JSON.stringify(appConfig.value));

    addNotification({
      type: "success",
      title: "配置更新",
      message: "应用配置已更新",
      duration: 3000,
    });
  };

  /**
   * 添加通知
   * @param {Object} notification - 通知对象
   */
  const addNotification = (notification) => {
    const newNotification = {
      id: Date.now() + Math.random(),
      timestamp: new Date().toISOString(),
      read: false,
      important: false,
      ...notification,
    };

    notifications.value.unshift(newNotification);

    // 只保留最近100条通知
    if (notifications.value.length > 100) {
      notifications.value = notifications.value.slice(0, 100);
    }

    // 保存到本地存储
    saveNotificationsToStorage();

    // 显示消息提示
    if (appConfig.value.enableNotification) {
      ElMessage({
        type: notification.type,
        message: notification.message,
        duration: notification.duration || 3000,
      });
    }
  };

  /**
   * 标记通知为已读
   * @param {number} notificationId - 通知ID
   */
  const markNotificationAsRead = (notificationId) => {
    const notification = notifications.value.find(
      (n) => n.id === notificationId
    );
    if (notification) {
      notification.read = true;
      saveNotificationsToStorage();
    }
  };

  /**
   * 标记所有通知为已读
   */
  const markAllNotificationsAsRead = () => {
    notifications.value.forEach((notification) => {
      notification.read = true;
    });
    saveNotificationsToStorage();
  };

  /**
   * 删除通知
   * @param {number} notificationId - 通知ID
   */
  const removeNotification = (notificationId) => {
    const index = notifications.value.findIndex((n) => n.id === notificationId);
    if (index > -1) {
      notifications.value.splice(index, 1);
      saveNotificationsToStorage();
    }
  };

  /**
   * 清空所有通知
   */
  const clearAllNotifications = () => {
    notifications.value = [];
    saveNotificationsToStorage();
  };

  /**
   * 设置加载状态
   * @param {string} key - 状态键
   * @param {boolean} loading - 加载状态
   */
  const setLoading = (key, loading) => {
    loadingStates.value.set(key, loading);
  };

  /**
   * 获取加载状态
   * @param {string} key - 状态键
   * @returns {boolean} 加载状态
   */
  const getLoading = (key) => {
    return loadingStates.value.get(key) || false;
  };

  /**
   * 设置错误状态
   * @param {string} key - 状态键
   * @param {string} error - 错误信息
   */
  const setError = (key, error) => {
    errorStates.value.set(key, error);
  };

  /**
   * 获取错误状态
   * @param {string} key - 状态键
   * @returns {string} 错误信息
   */
  const getError = (key) => {
    return errorStates.value.get(key) || "";
  };

  /**
   * 清除错误状态
   * @param {string} key - 状态键
   */
  const clearError = (key) => {
    errorStates.value.delete(key);
  };

  /**
   * 设置缓存
   * @param {string} key - 缓存键
   * @param {any} value - 缓存值
   * @param {number} expireTime - 过期时间（毫秒）
   */
  const setCache = (key, value, expireTime = null) => {
    const cacheItem = {
      value,
      timestamp: Date.now(),
      expireTime: expireTime || appConfig.value.cacheExpireTime,
    };

    cache.value.set(key, cacheItem);

    // 清理过期缓存
    cleanExpiredCache();
  };

  /**
   * 获取缓存
   * @param {string} key - 缓存键
   * @returns {any} 缓存值
   */
  const getCache = (key) => {
    const cacheItem = cache.value.get(key);

    if (!cacheItem) return null;

    // 检查是否过期
    if (Date.now() - cacheItem.timestamp > cacheItem.expireTime) {
      cache.value.delete(key);
      return null;
    }

    return cacheItem.value;
  };

  /**
   * 删除缓存
   * @param {string} key - 缓存键
   */
  const removeCache = (key) => {
    cache.value.delete(key);
  };

  /**
   * 清空所有缓存
   */
  const clearCache = () => {
    cache.value.clear();
  };

  /**
   * 清理过期缓存
   */
  const cleanExpiredCache = () => {
    const now = Date.now();

    for (const [key, cacheItem] of cache.value.entries()) {
      if (now - cacheItem.timestamp > cacheItem.expireTime) {
        cache.value.delete(key);
      }
    }
  };

  /**
   * 更新系统状态
   * @param {Object} status - 系统状态
   */
  const updateSystemStatus = (status) => {
    systemStatus.value = { ...systemStatus.value, ...status };
  };

  /**
   * 设置全局搜索关键词
   * @param {string} keyword - 搜索关键词
   */
  const setGlobalSearchKeyword = (keyword) => {
    globalSearchKeyword.value = keyword;
  };

  /**
   * 设置当前页面标题
   * @param {string} title - 页面标题
   */
  const setCurrentPageTitle = (title) => {
    currentPageTitle.value = title;
  };

  /**
   * 添加页面标签页
   * @param {Object} tab - 标签页对象
   */
  const addPageTab = (tab) => {
    const existingTab = pageTabs.value.find((t) => t.path === tab.path);
    if (!existingTab) {
      pageTabs.value.push({
        id: Date.now() + Math.random(),
        timestamp: new Date().toISOString(),
        ...tab,
      });
    }
  };

  /**
   * 移除页面标签页
   * @param {string} path - 页面路径
   */
  const removePageTab = (path) => {
    const index = pageTabs.value.findIndex((tab) => tab.path === path);
    if (index > -1) {
      pageTabs.value.splice(index, 1);
    }
  };

  /**
   * 清空所有页面标签页
   */
  const clearPageTabs = () => {
    pageTabs.value = [];
  };

  /**
   * 保存通知到本地存储
   */
  const saveNotificationsToStorage = () => {
    try {
      localStorage.setItem(
        "zapi_notifications",
        JSON.stringify(notifications.value)
      );
    } catch (error) {
      console.error("保存通知失败:", error);
    }
  };

  /**
   * 从本地存储加载通知
   */
  const loadNotificationsFromStorage = () => {
    try {
      const savedNotifications = localStorage.getItem("zapi_notifications");
      if (savedNotifications) {
        notifications.value = JSON.parse(savedNotifications);
      }
    } catch (error) {
      console.error("加载通知失败:", error);
    }
  };

  // ==================== 监听器 ====================

  // 监听主题变化
  watch(theme, (newTheme) => {
    setTheme(newTheme);
  });

  // 监听通知变化
  watch(
    notifications,
    () => {
      saveNotificationsToStorage();
    },
    { deep: true }
  );

  // ==================== 初始化 ====================

  // 从本地存储加载通知
  loadNotificationsFromStorage();

  // 定期清理过期缓存
  setInterval(cleanExpiredCache, 5 * 60 * 1000); // 每5分钟清理一次

  // ==================== 返回状态和方法 ====================

  return {
    // 状态
    theme,
    primaryColor,
    sidebarCollapsed,
    sidebarWidth,
    appConfig,
    notifications,
    systemStatus,
    cache,
    loadingStates,
    errorStates,
    globalSearchKeyword,
    currentPageTitle,
    pageTabs,

    // 计算属性
    currentThemeConfig,
    unreadNotificationCount,
    importantNotificationCount,
    isOnline,
    cacheSize,

    // 方法
    initAppConfig,
    setTheme,
    setPrimaryColor,
    toggleSidebar,
    setSidebarWidth,
    updateAppConfig,
    addNotification,
    markNotificationAsRead,
    markAllNotificationsAsRead,
    removeNotification,
    clearAllNotifications,
    setLoading,
    getLoading,
    setError,
    getError,
    clearError,
    setCache,
    getCache,
    removeCache,
    clearCache,
    cleanExpiredCache,
    updateSystemStatus,
    setGlobalSearchKeyword,
    setCurrentPageTitle,
    addPageTab,
    removePageTab,
    clearPageTabs,
    saveNotificationsToStorage,
    loadNotificationsFromStorage,
  };
});
