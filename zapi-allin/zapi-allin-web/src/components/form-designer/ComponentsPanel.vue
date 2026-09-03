<script setup lang="ts">
import { computed, ref } from 'vue'
import { componentCategories } from '@/config/form/components'
import { useFormDesignerStore } from '@/stores/form/formDesigner'
// 导入所有Element Plus图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { Search } from '@element-plus/icons-vue'

interface Props {
  useExternalSearch?: boolean
  externalSearchText?: string
}
const props = withDefaults(defineProps<Props>(), {
  useExternalSearch: false,
  externalSearchText: ''
})

const formDesignerStore = useFormDesignerStore()
const searchText = ref('')

// 过滤后的组件分类
const filteredCategories = computed(() => {
  const keyword = props.useExternalSearch ? (props.externalSearchText || '') : searchText.value
  if (!keyword.trim()) {
    return componentCategories
  }
  
  return componentCategories.map(category => ({
    ...category,
    components: category.components.filter(component => 
      component.label.toLowerCase().includes(keyword.toLowerCase()) ||
      component.type.toLowerCase().includes(keyword.toLowerCase())
    )
  })).filter(category => category.components.length > 0)
})

// 开始拖拽
const handleDragStart = (event: DragEvent, componentType: string) => {
  if (!event.dataTransfer) return
  
  console.log('开始拖拽组件:', componentType)
  
  // 清除之前的数据
  event.dataTransfer.clearData()
  
  // 设置拖拽数据
  event.dataTransfer.setData('componentType', componentType)
  event.dataTransfer.setData('text/plain', componentType) // 兼容性
  event.dataTransfer.effectAllowed = 'copy'
  
  // 设置拖拽状态
  formDesignerStore.setDragState(true)
  
  // 添加拖拽样式
  const target = event.target as HTMLElement
  target.classList.add('dragging')
  
  console.log('拖拽数据已设置:', event.dataTransfer.types)
}

// 拖拽结束
const handleDragEnd = (event: DragEvent) => {
  console.log('拖拽结束')
  
  // 移除拖拽样式
  const target = event.target as HTMLElement
  target.classList.remove('dragging')
  
  // 延迟重置状态，确保放置事件先触发
  setTimeout(() => {
    formDesignerStore.setDragState(false)
    console.log('拖拽状态已重置')
  }, 100)
}

// 获取图标组件
const getIconComponent = (iconName: string) => {
  if (!iconName) {
    console.warn('图标名称为空')
    return null
  }
  
  const iconComponent = ElementPlusIconsVue[iconName as keyof typeof ElementPlusIconsVue]
  if (!iconComponent) {
    console.warn('未找到图标组件:', iconName)
    return null
  }
  
  console.log('成功获取图标组件:', iconName, iconComponent)
  return iconComponent
}
</script>

<template>
  <div class="components-panel">
    <div v-if="!props.useExternalSearch" class="search-box">
      <el-input 
        v-model="searchText" 
        placeholder="搜索组件" 
        size="large"
        clearable
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>
    
    <div class="categories-list">
      <div 
        v-for="category in filteredCategories" 
        :key="category.name"
        class="category-section"
      >
        <div class="category-title">
          <el-icon v-if="getIconComponent(category.icon)">
          <component 
            :is="getIconComponent(category.icon)" 
          />
          </el-icon>
          <span>{{ category.label }}</span>
        </div>
        
        <div class="components-grid">
          <div
            v-for="component in category.components"
            :key="component.type"
            class="component-card"
            draggable="true"
            @dragstart="handleDragStart($event, component.type)"
            @dragend="handleDragEnd"
          >
            <div class="component-icon">
              <el-icon v-if="getIconComponent(component.icon)">
              <component 
                :is="getIconComponent(component.icon)" 
              />
              </el-icon>
              <div v-else class="fallback-icon">{{ component.icon?.substring(0, 2) || '?' }}</div>
            </div>
            <div class="component-name">
              {{ component.label }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.components-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.search-box {
  padding: 16px 12px;
  border-bottom: 1px solid var(--border-color);
  background: var(--bg-card);
}

/* 让输入框更大、更易点按 */
.search-box :deep(.el-input) {
  width: 100%;
}

.search-box :deep(.el-input__wrapper) {
  border-radius: 5px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  padding: 6px 12px;
  min-height: 42px;
}

.search-box :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 6px rgba(99, 102, 241, 0.2);
}

.search-box :deep(.el-input__prefix) {
  padding-left: 12px;
}

.search-box :deep(.el-input__prefix-inner svg) {
  width: 18px;
  height: 18px;
}

.search-box :deep(.el-input__suffix) {
  padding-right: 12px;
}

.search-box :deep(.el-input__inner) {
  padding: 0 4px;
  font-size: 14px;
}

.categories-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.category-section {
  margin-bottom: 20px;
}

.category-title {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary, #1f2937);
  background: transparent;
  border-left: 4px solid var(--primary-color, #6366f1);
  border-radius: 0 8px 8px 0;
  margin-bottom: 12px;
  box-shadow: none;
}

.category-title .el-icon {
  font-size: 16px;
  color: var(--primary-color, #6366f1);
}

.components-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.component-card {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 8px;
  background: var(--bg-card, #ffffff);
  border: 1px solid var(--border-color, #e5e7eb);
  border-radius: 12px;
  cursor: grab;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  user-select: none;
  box-shadow: var(--shadow-sm, 0 1px 2px 0 rgba(0, 0, 0, 0.05));
}

.component-card:hover {
  border-color: var(--primary-color, #6366f1);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.2);
  transform: translateY(-2px);
}

.component-card:active {
  cursor: grabbing;
  transform: scale(0.96);
}

.component-icon {
  font-size: 28px;
  color: var(--primary-color, #6366f1);
  margin-bottom: 10px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 明确图标尺寸，避免被继承字号影响显示异常 */
.component-icon :deep(svg) {
  width: 28px;
  height: 28px;
}

.component-card:hover .component-icon {
  transform: scale(1.1);
  color: var(--primary-hover, #4f46e5);
}

.component-name {
  font-size: 13px;
  color: var(--text-primary, #1f2937);
  text-align: center;
  line-height: 1.3;
  font-weight: 500;
  padding: 0 4px;
}

.fallback-icon {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--primary-light);
  border-radius: 4px;
  font-size: 12px;
  color: var(--primary-color);
}

/* 拖拽状态 */
.component-card.dragging {
  opacity: 0.6;
  transform: rotate(3deg) scale(0.95);
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);
}

/* 滚动条样式 */
.categories-list::-webkit-scrollbar {
  width: 4px;
}

.categories-list::-webkit-scrollbar-track {
  background: transparent;
}

.categories-list::-webkit-scrollbar-thumb {
  background: var(--border-light, #d1d5db);
  border-radius: 2px;
}

.categories-list::-webkit-scrollbar-thumb:hover {
  background: var(--text-muted, #9ca3af);
}
</style>