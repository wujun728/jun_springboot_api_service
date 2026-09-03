<template>
  <div class="icon-selector">
    <el-popover
      v-model:visible="visible"
      placement="bottom-start"
      :width="400"
      trigger="manual"
      popper-class="icon-selector-popover"
      :hide-after="0"
      :show-arrow="false"
    >
      <template #reference>
        <div class="icon-selector-trigger" @click="toggleVisible">
          <el-icon v-if="modelValue" class="selected-icon">
            <component :is="modelValue" />
          </el-icon>
          <span v-else class="placeholder">请选择图标</span>
          <el-icon class="arrow-icon" :class="{ 'is-reverse': visible }">
            <ArrowDown />
          </el-icon>
        </div>
      </template>
      
      <div class="icon-selector-content">
        <div class="icon-header">
          <div class="icon-search">
            <el-input
              v-model="searchKeyword"
              placeholder="搜索图标..."
              prefix-icon="Search"
              clearable
              size="small"
            />
          </div>
          <div class="icon-categories">
            <el-radio-group v-model="selectedCategory" size="small">
              <el-radio-button label="all">全部</el-radio-button>
              <el-radio-button label="common">常用</el-radio-button>
              <el-radio-button label="navigation">导航</el-radio-button>
              <el-radio-button label="action">操作</el-radio-button>
            </el-radio-group>
          </div>
        </div>
        
        <div class="icon-list">
          <div
            v-for="icon in filteredIcons"
            :key="icon.name"
            class="icon-item"
            :class="{ active: tempSelectedIcon === icon.name }"
            @click.stop="tempSelectIcon(icon.name)"
          >
            <el-icon class="icon">
              <component :is="icon.component" />
            </el-icon>
            <span class="icon-name">{{ icon.name }}</span>
          </div>
        </div>
        
        <div class="icon-footer">
          <el-button size="small" @click.stop="clearIcon">清除</el-button>
          <el-button size="small" @click.stop="cancelSelect">取消</el-button>
          <el-button size="small" type="primary" @click.stop="confirmSelect">确定</el-button>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { ArrowDown } from '@element-plus/icons-vue'
import * as ElementPlusIcons from '@element-plus/icons-vue'

// 定义props
const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

// 定义emits
const emit = defineEmits(['update:modelValue'])

// 响应式数据
const visible = ref(false)
const searchKeyword = ref('')
const selectedCategory = ref('all')
const tempSelectedIcon = ref('')

// 图标列表
const iconList = computed(() => {
  const icons = []
  for (const [name, component] of Object.entries(ElementPlusIcons)) {
    icons.push({
      name,
      component
    })
  }
  return icons.sort((a, b) => a.name.localeCompare(b.name))
})

// 常用图标列表
const commonIcons = [
  'Home', 'Setting', 'User', 'UserFilled', 'Menu', 'Document', 'Folder', 'FolderOpened',
  'Plus', 'Edit', 'Delete', 'Search', 'Refresh', 'Download', 'Upload', 'View',
  'Star', 'StarFilled', 'Heart', 'HeartFilled', 'Share', 'Link', 'Lock', 'Unlock',
  'Bell', 'Message', 'Phone', 'Location', 'Calendar', 'Clock', 'Timer', 'Warning',
  'Success', 'Error', 'Info', 'Question', 'Close', 'Check', 'CircleCheck', 'CircleClose'
]

// 导航图标列表
const navigationIcons = [
  'Home', 'Menu', 'Grid', 'List', 'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
  'Back', 'Forward', 'Top', 'Bottom', 'Expand', 'Fold', 'Sort', 'Rank'
]

// 操作图标列表
const actionIcons = [
  'Plus', 'Edit', 'Delete', 'Search', 'Refresh', 'Download', 'Upload', 'View',
  'Share', 'Link', 'Copy', 'Paste', 'Cut', 'Save', 'Close', 'Check', 'CircleCheck',
  'CircleClose', 'Warning', 'Success', 'Error', 'Info', 'Question'
]

// 过滤后的图标列表
const filteredIcons = computed(() => {
  let icons = iconList.value
  
  // 按分类过滤
  if (selectedCategory.value !== 'all') {
    let categoryIcons = []
    switch (selectedCategory.value) {
      case 'common':
        categoryIcons = commonIcons
        break
      case 'navigation':
        categoryIcons = navigationIcons
        break
      case 'action':
        categoryIcons = actionIcons
        break
    }
    icons = icons.filter(icon => categoryIcons.includes(icon.name))
  }
  
  // 按搜索关键词过滤
  if (searchKeyword.value) {
    icons = icons.filter(icon => 
      icon.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
    )
  }
  
  return icons
})

// 切换显示状态
const toggleVisible = () => {
  visible.value = !visible.value
  if (visible.value) {
    // 打开时，将当前值设为临时选择
    tempSelectedIcon.value = props.modelValue
  }
}

// 临时选择图标
const tempSelectIcon = (iconName) => {
  console.log('选择图标:', iconName)
  tempSelectedIcon.value = iconName
}

// 确认选择
const confirmSelect = () => {
  console.log('确认选择:', tempSelectedIcon.value)
  emit('update:modelValue', tempSelectedIcon.value)
  visible.value = false
}

// 取消选择
const cancelSelect = () => {
  tempSelectedIcon.value = props.modelValue
  visible.value = false
}

// 清除图标
const clearIcon = () => {
  tempSelectedIcon.value = ''
  emit('update:modelValue', '')
  visible.value = false
}

// 监听搜索关键词变化
watch(searchKeyword, () => {
  // 搜索时自动展开
  if (searchKeyword.value) {
    visible.value = true
  }
})

// 监听modelValue变化，同步临时选择
watch(() => props.modelValue, (newValue) => {
  if (!visible.value) {
    tempSelectedIcon.value = newValue
  }
}, { immediate: true })

// 监听ESC键关闭
const handleKeydown = (event) => {
  if (event.key === 'Escape' && visible.value) {
    visible.value = false
  }
}

onMounted(() => {
  document.addEventListener('keydown', handleKeydown)
})

onUnmounted(() => {
  document.removeEventListener('keydown', handleKeydown)
})
</script>

<style scoped>
.icon-selector {
  width: 100%;
}

.icon-selector-trigger {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  cursor: pointer;
  background-color: var(--el-bg-color);
  transition: border-color 0.2s;
}

.icon-selector-trigger:hover {
  border-color: var(--el-border-color-hover);
}

.icon-selector-trigger:focus-within {
  border-color: var(--el-color-primary);
}

.selected-icon {
  font-size: 16px;
  color: var(--el-text-color-regular);
}

.placeholder {
  color: var(--el-text-color-placeholder);
  font-size: 14px;
}

.arrow-icon {
  font-size: 12px;
  color: var(--el-text-color-placeholder);
  transition: transform 0.2s;
}

.arrow-icon.is-reverse {
  transform: rotate(180deg);
}

.icon-selector-content {
  padding: 0;
}

.icon-header {
  border-bottom: 1px solid var(--el-border-color-light);
  background-color: var(--el-fill-color-lighter);
}

.icon-search {
  padding: 12px 12px 8px 12px;
}

.icon-categories {
  padding: 0 12px 12px 12px;
}

.icon-list {
  padding: 8px;
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 8px;
  max-height: 240px;
  overflow-y: auto;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px 4px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
  border: 1px solid transparent;
  min-height: 60px;
  justify-content: center;
}

.icon-item:hover {
  background-color: var(--el-fill-color-light);
  border-color: var(--el-border-color);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.icon-item.active {
  background-color: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.2);
}

.icon-item.active .icon {
  color: var(--el-color-primary);
}

.icon-item .icon {
  font-size: 18px;
  margin-bottom: 4px;
  color: var(--el-text-color-regular);
}

.icon-item .icon-name {
  font-size: 10px;
  text-align: center;
  word-break: break-all;
  line-height: 1.2;
  color: var(--el-text-color-secondary);
}

.icon-footer {
  padding: 12px;
  border-top: 1px solid var(--el-border-color-light);
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  background-color: var(--el-fill-color-lighter);
}

/* 滚动条样式 */
.icon-list::-webkit-scrollbar {
  width: 6px;
}

.icon-list::-webkit-scrollbar-track {
  background: var(--el-fill-color-lighter);
  border-radius: 3px;
}

.icon-list::-webkit-scrollbar-thumb {
  background: var(--el-border-color);
  border-radius: 3px;
}

.icon-list::-webkit-scrollbar-thumb:hover {
  background: var(--el-border-color-darker);
}
</style>

<style>
/* 全局样式，用于弹出层 */
.icon-selector-popover {
  padding: 0 !important;
  border-radius: 8px !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
}

.icon-selector-popover .el-popover__title {
  margin: 0;
  padding: 12px;
  border-bottom: 1px solid var(--el-border-color-light);
  font-weight: 600;
}

/* 优化按钮组样式 */
.icon-selector-popover .el-radio-group .el-radio-button__inner {
  border-radius: 4px;
  margin-right: 4px;
}

.icon-selector-popover .el-radio-group .el-radio-button:first-child .el-radio-button__inner {
  border-radius: 4px;
}

.icon-selector-popover .el-radio-group .el-radio-button:last-child .el-radio-button__inner {
  border-radius: 4px;
}
</style> 