<template>
  <div class="menu-item-wrapper">
    <!-- 菜单项 -->
    <div
      class="menu-item"
      :class="{ 
        active: isActive, 
        'has-children': hasChildren,
        expanded: isExpanded
      }"
      :style="{ paddingLeft: isCollapsed ? '0' : `${16 + level * 16}px` }"
      @click="handleMenuClick"
      :title="isCollapsed ? menu.menuName : ''"
    >
      <!-- 展开/收起图标 -->
      <el-icon 
        v-if="hasChildren && !isCollapsed" 
        class="expand-icon"
        @click.stop="toggleExpand"
      >
        <component :is="isExpanded ? ArrowDownIcon : ArrowRight" />
      </el-icon>
      
      <!-- 菜单图标 -->
      <el-icon class="menu-icon">
        <component :is="menu.icon" />
      </el-icon>
      
      <!-- 菜单名称 -->
      <span v-show="!isCollapsed" class="menu-name">{{ menu.menuName }}</span>
      
      <!-- 子菜单数量标识（可选） -->
      <span v-if="hasChildren && !isCollapsed && !isExpanded" class="children-count">
        {{ menu.children.length }}
      </span>
    </div>

    <!-- 递归渲染子菜单 -->
    <transition name="slide-fade">
      <div v-show="isExpanded && !isCollapsed" class="submenu-container">
        <menu-item-recursive
          v-for="child in menu.children"
          :key="child.id"
          :menu="child"
          :level="level + 1"
          :is-collapsed="isCollapsed"
          @navigate="$emit('navigate', $event)"
        />
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ArrowRight, ArrowDown as ArrowDownIcon } from '@element-plus/icons-vue'

const props = defineProps({
  menu: {
    type: Object,
    required: true
  },
  level: {
    type: Number,
    default: 0
  },
  isCollapsed: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['navigate'])

const route = useRoute()
const isExpanded = ref(false)

// 是否有子菜单
const hasChildren = computed(() => {
  return props.menu.children && props.menu.children.length > 0
})

// 判断当前菜单是否激活
const isActive = computed(() => {
  const menuPath = getMenuPath(props.menu.path)
  return route.path === menuPath || route.path.startsWith(menuPath + '/')
})

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

// 处理菜单点击
const handleMenuClick = () => {
  if (hasChildren.value && !props.isCollapsed) {
    // 如果有子菜单且未收起，则切换展开状态
    toggleExpand()
  } else if (!hasChildren.value) {
    // 如果没有子菜单，则导航
    emit('navigate', props.menu)
  } else if (props.isCollapsed && hasChildren.value) {
    // 收起状态下，如果有子菜单，递归查找第一个真正的菜单项
    const firstRealMenu = findFirstRealMenu(props.menu.children[0])
    if (firstRealMenu) {
      emit('navigate', firstRealMenu)
    } else {
      // 如果找不到真正的菜单项，尝试导航到第一个子菜单
      emit('navigate', props.menu.children[0])
    }
  }
}

// 切换展开/收起
const toggleExpand = () => {
  isExpanded.value = !isExpanded.value
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

// 监听路由变化，自动展开当前激活菜单的父级
const checkAndExpandActive = () => {
  if (hasChildren.value && !props.isCollapsed) {
    // 检查子菜单中是否有激活的
    const hasActiveChild = props.menu.children.some(child => {
      const childPath = getMenuPath(child.path)
      return route.path === childPath || route.path.startsWith(childPath + '/')
    })
    
    if (hasActiveChild) {
      isExpanded.value = true
    }
  }
}

// 初始化时检查
checkAndExpandActive()
</script>

<script>
export default {
  name: 'MenuItemRecursive'
}
</script>

<style lang="scss" scoped>
.menu-item-wrapper {
  .menu-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 16px;
    margin: 2px 8px;
    color: var(--text-secondary);
    font-size: 14px;
    cursor: pointer;
    border-radius: 6px;
    transition: all 0.2s;
    white-space: nowrap;
    position: relative;

    &:hover {
      background: var(--bg-tertiary);
      color: var(--text-primary);
    }

    &.active {
      background: var(--primary-light);
      color: var(--primary-color);
      font-weight: 500;
    }

    &.has-children {
      .menu-name {
        flex: 1;
      }
    }

    .expand-icon {
      font-size: 14px;
      flex-shrink: 0;
      transition: transform 0.2s;
      color: var(--text-tertiary);

      &:hover {
        color: var(--text-primary);
      }
    }

    .menu-icon {
      font-size: 18px;
      flex-shrink: 0;
    }

    .menu-name {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .children-count {
      font-size: 12px;
      background: var(--bg-tertiary);
      color: var(--text-tertiary);
      padding: 2px 6px;
      border-radius: 10px;
      min-width: 18px;
      text-align: center;
      flex-shrink: 0;
    }
  }

  .submenu-container {
    overflow: hidden;
  }
}

// 过渡动画
.slide-fade-enter-active,
.slide-fade-leave-active {
  transition: all 0.3s ease;
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  opacity: 0;
  max-height: 0;
}

.slide-fade-enter-to,
.slide-fade-leave-from {
  opacity: 1;
  max-height: 1000px;
}

// 收起状态样式
.menu-item-wrapper {
  &:has(.menu-item[style*="padding-left: 0"]) {
    .menu-item {
      justify-content: center !important;
      padding: 12px 0 !important;
      margin: 2px 8px !important;
      
      .menu-icon {
        margin: 0 !important;
      }
    }
  }
}
</style>