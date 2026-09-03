<script setup lang="ts">
import { ref, computed, nextTick, watch, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage } from 'element-plus'
import { useFormDesignerStore } from '@/stores/form/formDesigner'
import FormComponentRenderer from './FormComponentRenderer.vue'

const formDesignerStore = useFormDesignerStore()

// 拖拽相关状态
const dragOverIndex = ref(-1)
const isDragOver = ref(false)
const forceUpdateKey = ref(0)
const draggingIndex = ref<number | null>(null)
const isInternalDragging = ref(false)

// 调整大小相关状态
const resizingIndex = ref<number | null>(null)
const resizeStartX = ref(0)
const resizeStartSpan = ref(12)
const componentsListEl = ref<HTMLElement | null>(null)
const isResizing = ref(false)
const resizeBadgeSpan = ref<number | null>(null)

// 计算属性
const components = computed(() => {
  const comps = formDesignerStore.currentForm.components
  console.log('组件列表更新:', comps.length, comps.map(c => ({ id: c.id, type: c.type, label: c.label })))
  return comps
})
const selectedComponentId = computed(() => formDesignerStore.selectedComponentId)
const labelPosition = computed(() => formDesignerStore.currentForm.labelPosition || 'right')
const labelWidth = computed(() => formDesignerStore.currentForm.labelWidth || '120px')

// 监听组件列表变化
watch(
  () => formDesignerStore.currentForm.components,
  (newComponents, oldComponents) => {
    console.log('检测到组件列表变化:', {
      新组件数: newComponents.length,
      旧组件数: oldComponents?.length || 0,
      新组件: newComponents.map(c => c.id),
      旧组件: oldComponents?.map(c => c.id) || []
    })
    
    // 强制更新组件列表显示
    forceUpdateKey.value++
    
    nextTick(() => {
      console.log('DOM已更新，强制更新键:', forceUpdateKey.value)
    })
  },
  { deep: true, immediate: true }
)

// 计算每列宽度（近似）
const getColumnWidth = (): number => {
  const container = componentsListEl.value
  if (!container) return 1
  const styles = getComputedStyle(container)
  const gap = parseFloat(styles.gap || '0')
  const totalGap = gap * 11
  const width = container.clientWidth - totalGap
  return Math.max(1, width / 12)
}

// 根据鼠标位置计算目标span（相对于组件左边缘）
const calcSpanByPointer = (index: number, clientX: number): number => {
  const container = document.querySelector(
    `.component-wrapper[data-idx='${index}'] .component-container`
  ) as HTMLElement | null
  const perCol = getColumnWidth()
  if (!container || perCol <= 0) return 12
  const rect = container.getBoundingClientRect()
  const relX = clientX - rect.left
  const cols = Math.round(relX / perCol)
  return Math.min(12, Math.max(1, cols))
}

// 开始调整大小
const handleResizeStart = (index: number, event: MouseEvent) => {
  event.stopPropagation()
  resizingIndex.value = index
  resizeStartX.value = event.clientX
  const comp = components.value[index]
  resizeStartSpan.value = (comp.span ?? 12)
  isResizing.value = true
  resizeBadgeSpan.value = comp.span ?? 12
  document.body.style.userSelect = 'none'
  window.addEventListener('mousemove', handleResizing)
  window.addEventListener('mouseup', handleResizeEnd)
}

// 调整过程中
const handleResizing = (event: MouseEvent) => {
  if (resizingIndex.value === null) return
  const index = resizingIndex.value
  const comp = components.value[index]
  const nextSpan = calcSpanByPointer(index, event.clientX)
  if (nextSpan !== (comp.span ?? 12)) {
    formDesignerStore.updateComponent(comp.id, { span: nextSpan } as any)
  }
  resizeBadgeSpan.value = nextSpan
}

// 结束调整
const handleResizeEnd = () => {
  window.removeEventListener('mousemove', handleResizing)
  window.removeEventListener('mouseup', handleResizeEnd)
  resizingIndex.value = null
  isResizing.value = false
  resizeBadgeSpan.value = null
  document.body.style.userSelect = ''
}

onBeforeUnmount(() => {
  window.removeEventListener('mousemove', handleResizing)
  window.removeEventListener('mouseup', handleResizeEnd)
})

// 拖拽进入
const handleDragEnter = (event: DragEvent) => {
  event.preventDefault()
  event.stopPropagation()
  if (isInternalDragging.value) {
    // 内部排序拖拽，不显示外部添加遮罩
    return
  }
  
  // 强制重置状态，防止之前的状态干扰
  formDesignerStore.setDragState(true)
  isDragOver.value = true
  
  console.log('拖拽进入画布')
}

// 拖拽悬停
const handleDragOver = (event: DragEvent) => {
  event.preventDefault()
  event.stopPropagation()
  if (isInternalDragging.value) {
    event.dataTransfer && (event.dataTransfer.dropEffect = 'move')
    return
  }
  if (event.dataTransfer) {
    event.dataTransfer.dropEffect = 'copy'
  }
}

// 拖拽离开
const handleDragLeave = (event: DragEvent) => {
  // 只有当目标元素完全离开当前元素时才重置状态
  if (!event.relatedTarget || !(event.currentTarget as Element).contains(event.relatedTarget as Node)) {
    if (!isInternalDragging.value) {
    isDragOver.value = false
    dragOverIndex.value = -1
    }
    console.log('拖拽离开画布')
  }
}

// 放置处理
const handleDrop = (event: DragEvent, index?: number) => {
  event.preventDefault()
  event.stopPropagation()
  console.log('放置事件触发', { index, isDragOver: isDragOver.value })

  // 优先处理画布内排序
  if (draggingIndex.value !== null) {
    const fromIndex = draggingIndex.value
    // 使用辅助线位置作为插入点；若无则按传入索引或末尾
    const toIndex = (dragOverIndex.value >= 0)
      ? dragOverIndex.value
      : (typeof index === 'number' ? index : components.value.length)
    console.log('内部排序移动(按辅助线)', { fromIndex, toIndex })
    formDesignerStore.moveComponent(fromIndex, toIndex)
    // 重置拖拽状态
    draggingIndex.value = null
    isInternalDragging.value = false
    isDragOver.value = false
    dragOverIndex.value = -1
    formDesignerStore.setDragState(false)
    return
  }
  
  const componentType = event.dataTransfer?.getData('componentType')
  console.log('获取的组件类型:', componentType)
  
  if (componentType) {
    try {
      console.log('添加组件前 - 当前组件数:', formDesignerStore.currentForm.components.length)
      
      const newComponent = formDesignerStore.addComponent(componentType, index)
      console.log('新组件已添加:', newComponent)
      console.log('添加组件后 - 当前组件数:', formDesignerStore.currentForm.components.length)
      
      // 强制更新组件列表
      forceUpdateKey.value++
      
      // 显示成功消息
      ElMessage.success(`已添加 ${newComponent.label}`)
    } catch (error) {
      console.error('添加组件失败:', error)
      ElMessage.error('添加组件失败')
    }
  } else {
    console.warn('未获取到组件类型')
  }
  
  // 确保状态完全重置
  isDragOver.value = false
  dragOverIndex.value = -1
  formDesignerStore.setDragState(false)
  
  console.log('拖拽状态已重置')
}

// 在组件之间拖拽悬停
const handleComponentDragOver = (event: DragEvent, index: number) => {
  event.preventDefault()
  dragOverIndex.value = index
}

// 画布内排序：拖拽开始/结束/放下
const handleItemDragStart = (index: number, event: DragEvent) => {
  draggingIndex.value = index
  isInternalDragging.value = true
  event.stopPropagation()
  event.dataTransfer?.setData('text/plain', `${index}`)
  event.dataTransfer?.setData('internal-reorder', '1')
  event.dataTransfer!.effectAllowed = 'move'
}

const handleItemDrop = (toIndex: number, event: DragEvent) => {
  event.preventDefault()
  event.stopPropagation()
  if (draggingIndex.value === null) return
  const fromIndex = draggingIndex.value
  // 如果存在辅助线位置，则以辅助线为准
  const finalToIndex = dragOverIndex.value >= 0 ? dragOverIndex.value : toIndex
  if (fromIndex === finalToIndex) return
  formDesignerStore.moveComponent(fromIndex, finalToIndex)
  draggingIndex.value = null
  isInternalDragging.value = false
  dragOverIndex.value = -1
}

const handleItemDragOver = (event: DragEvent, index?: number) => {
  event.preventDefault()
  event.stopPropagation()
  event.dataTransfer && (event.dataTransfer.dropEffect = 'move')
  // 根据鼠标位置决定插入到该项之前还是之后
  if (typeof index === 'number') {
    const target = event.currentTarget as HTMLElement
    const rect = target.getBoundingClientRect()
    const offsetY = event.clientY - rect.top
    const before = offsetY < rect.height / 2
    dragOverIndex.value = before ? index : index + 1
  }
}

// 选择组件
const selectComponent = (componentId: string, event?: Event) => {
  event?.stopPropagation()
  formDesignerStore.selectComponent(componentId)
}

// 删除组件
const removeComponent = (componentId: string, event: Event) => {
  event.stopPropagation()
  formDesignerStore.removeComponent(componentId)
}

// 复制组件
const duplicateComponent = (componentId: string, event: Event) => {
  event.stopPropagation()
  formDesignerStore.duplicateComponent(componentId)
}

// 清空选择
const clearSelection = () => {
  formDesignerStore.clearSelection()
}

// 键盘事件处理
const handleKeydown = (event: KeyboardEvent) => {
  if (selectedComponentId.value) {
    if (event.key === 'Delete' || event.key === 'Backspace') {
      formDesignerStore.removeComponent(selectedComponentId.value)
    } else if (event.key === 'd' && (event.ctrlKey || event.metaKey)) {
      event.preventDefault()
      formDesignerStore.duplicateComponent(selectedComponentId.value)
    }
  }
}

// 调整宽度（span: 1-12）
const increaseSpan = (componentId: string) => {
  const comp = formDesignerStore.currentForm.components.find(c => c.id === componentId)
  if (!comp) return
  const current = comp.span ?? 12
  if (current < 12) {
    formDesignerStore.updateComponent(componentId, { span: Math.min(12, current + 1) } as any)
  }
}

const decreaseSpan = (componentId: string) => {
  const comp = formDesignerStore.currentForm.components.find(c => c.id === componentId)
  if (!comp) return
  const current = comp.span ?? 12
  if (current > 1) {
    formDesignerStore.updateComponent(componentId, { span: Math.max(1, current - 1) } as any)
  }
}
</script>

<template>
  <div 
    class="design-canvas"
    :class="{ 'drag-over': isDragOver }"
    @dragenter="handleDragEnter"
    @dragover="handleDragOver"
    @dragleave="handleDragLeave"
    @drop="handleDrop"
    @click="clearSelection"
    @keydown="handleKeydown"
    tabindex="0"
  >
    <!-- 空状态 -->
    <div v-if="components.length === 0" class="empty-canvas">
      <el-empty 
        description="拖拽左侧组件到此处开始设计表单"
        :image-size="120"
      >
        <template #image>
          <el-icon size="120" color="#c0c4cc"><Grid /></el-icon>
        </template>
      </el-empty>
    </div>
    
    <!-- 表单组件列表 -->
    <div v-else class="components-list" :key="forceUpdateKey" ref="componentsListEl">
      <div class="grid-guides" v-if="isResizing" />
      <el-form :label-position="labelPosition" :label-width="labelWidth" style="width:100%">
      <!-- 组件间的拖拽指示器 -->
      <div
        v-if="dragOverIndex === 0"
        class="drop-indicator"
        @dragover="handleComponentDragOver($event, 0)"
        @drop="handleDrop($event, 0)"
      />
      
      <el-row :gutter="20">
      <el-col
        v-for="(component, index) in components"
        :key="`${component.id}-${forceUpdateKey}`"
        :span="(component.span || 12) * 2"
        class="component-wrapper"
        :data-idx="index"
        :class="{
          'selected': component.id === selectedComponentId,
          'drag-over': dragOverIndex === index + 1
        }"
        @dragover="handleItemDragOver($event, index)"
        @drop="handleItemDrop(index, $event)"
      >
        <!-- 组件容器 -->
        <div
          class="component-container"
          @click="selectComponent(component.id, $event)"
           draggable="true"
           @dragstart.stop="handleItemDragStart(index, $event)"
           @dragend.stop="draggingIndex = null; isInternalDragging = false"
        >
          <!-- 右下角悬浮操作栏（选中时显示） -->
          <div v-if="component.id === selectedComponentId" class="action-bar">
            <el-button 
              class="fab-btn"
              size="small"
              circle
              draggable="true"
              @dragstart.stop="handleItemDragStart(index, $event)"
              @dragend.stop="draggingIndex = null; isInternalDragging = false"
              @click.stop
            >
              <el-icon><Rank /></el-icon>
            </el-button>
              <el-button 
              class="fab-btn"
                size="small" 
                type="primary" 
              circle
                @click="duplicateComponent(component.id, $event)"
              >
                <el-icon><CopyDocument /></el-icon>
              </el-button>
              <el-button 
              class="fab-btn"
                size="small" 
                type="danger" 
              circle
                @click="removeComponent(component.id, $event)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>

          <!-- 调整大小手柄（右侧居中） -->
          <div class="resize-handle" @mousedown="handleResizeStart(index, $event)" />
          <div v-if="isResizing && resizeBadgeSpan !== null && index === resizingIndex" class="resize-badge">
            {{ resizeBadgeSpan }}/12
          </div>
          
          <!-- 表单项 -->
          <el-form-item 
            :label="component.label"
            :required="component.required"
            class="form-item-preview"
          >
            <FormComponentRenderer 
              :component="component"
              :disabled="false"
            />
          </el-form-item>
        </div>
        
        <!-- 组件间的拖拽指示器 -->
        <div
          v-if="dragOverIndex === index + 1"
          class="drop-indicator"
          @dragover="handleComponentDragOver($event, index + 1)"
          @drop="handleDrop($event, index + 1)"
        />
      </el-col>
      </el-row>
      </el-form>
    </div>
    
    <!-- 拖拽遮罩 -->
    <div v-if="isDragOver" class="drag-overlay">
      <div class="drag-hint">
        <el-icon size="48"><Plus /></el-icon>
        <span>松开鼠标添加组件</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
.design-canvas {
  height: 100%;
  background: var(--bg-card);
  border-radius: 8px;
  position: relative;
  outline: none;
  transition: all 0.2s ease;
  overflow-y: auto;
}

.design-canvas:focus {
  box-shadow: 0 0 0 2px var(--primary-light);
}

.design-canvas.drag-over {
  background: var(--primary-light);
  border: 2px dashed var(--primary-color);
}

.empty-canvas {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
}

.empty-canvas .el-empty {
  padding: 60px 40px;
}

.empty-canvas .el-empty :deep(.el-empty__description) {
  color: var(--text-secondary);
  font-size: 14px;
}

.components-list {
  padding: 16px;
  /* 使用 el-row/el-col 布局，不再需要 Grid */
}

/* 网格引导线 */
.grid-guides {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: none;
  background-image: repeating-linear-gradient(
    to right,
    rgba(64,158,255,0.08) 0,
    rgba(64,158,255,0.08) 1px,
    transparent 1px,
    transparent calc((100% - 11 * 8px) / 12)
  );
  background-size: calc((100% - 11 * 8px) / 12 + 8px) 100%;
}

.component-wrapper {
  position: relative;
  margin-bottom: 0;
  group: component;
}

.component-container {
  background: var(--bg-card);
  border: 2px solid transparent;
  border-radius: 8px;
  padding: 12px;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.component-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: transparent;
  border-radius: 6px;
  transition: background 0.15s ease;
  pointer-events: none;
}

.component-container:hover {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-sm);
}

.component-container:hover::before {
  background: transparent;
}

.component-wrapper.selected .component-container {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px var(--primary-light);
}

.component-wrapper.selected .component-container::before {
  background: transparent;
}

/* 深色模式下选中组件的特殊处理 */
[data-theme='dark'] .component-wrapper.selected .component-container {
  background: rgba(99, 102, 241, 0.15);
}

[data-theme='dark'] .component-wrapper.selected .component-container::before {
  background: transparent;
}

/* 右下角悬浮操作栏（选中时显示） */
.action-bar {
  position: absolute;
  right: 8px;
  bottom: 8px;
  display: flex;
  gap: 6px;
  z-index: 20;
}

.fab-btn {
  box-shadow: 0 4px 12px rgba(0,0,0,0.12);
}

.form-item-preview {
  margin-bottom: 0;
}

.form-item-preview :deep(.el-form-item__label) {
  color: var(--text-primary) !important;
  font-weight: 500;
}

.form-item-preview :deep(.el-form-item__content) {
  pointer-events: none;
  opacity: 0.8;
}

.drop-indicator {
  height: 3px;
  background: var(--primary-color);
  border-radius: 2px;
  margin: 6px 0;
  opacity: 0;
  transition: opacity 0.12s ease;
  position: relative;
}

.drop-indicator::before {
  content: '';
  position: absolute;
  left: -6px;
  top: -3px;
  width: 9px;
  height: 9px;
  background: var(--primary-color);
  border-radius: 50%;
  box-shadow: 0 0 0 2px var(--bg-card);
}

.component-wrapper.drag-over .drop-indicator,
.drop-indicator:hover {
  opacity: 1;
}

/* 拖拽经过时，目标项轻微高亮 */
.component-wrapper.drag-over .component-container {
  box-shadow: 0 0 0 2px var(--primary-light);
}

.drag-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--primary-light);
  backdrop-filter: blur(1px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  border-radius: 8px;
}

.drag-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 32px;
  background: var(--bg-card);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
  border: 2px dashed var(--primary-color);
}

.drag-hint .el-icon {
  color: var(--primary-color);
  margin-bottom: 12px;
}

.drag-hint span {
  color: var(--primary-color);
  font-size: 16px;
  font-weight: 500;
}

/* 动画效果 */
@keyframes componentEnter {
  from {
    opacity: 0.4;
    transform: translateY(6px) scale(0.99);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

.component-wrapper {
  animation: componentEnter 0.16s ease;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .components-list {
    padding: 12px;
  }
  
  .component-container {
    padding: 10px;
  }
}

.resize-handle {
  position: absolute;
  top: 0;
  right: -6px;
  width: 12px;
  height: 100%;
  cursor: ew-resize;
  z-index: 15;
  opacity: 0;
  transition: opacity 0.12s ease;
}

.component-container:hover .resize-handle {
  opacity: 1;
}

/* 提示更易抓取 */
.resize-handle::before {
  content: '';
  position: absolute;
  top: 50%;
  right: 4px;
  transform: translateY(-50%);
  width: 3px;
  height: 24px;
  background: var(--primary-color);
  border-radius: 2px;
  opacity: 0.6;
}

.resize-badge {
  position: absolute;
  top: 6px;
  right: 6px;
  background: var(--primary-color);
  color: #fff;
  font-size: 12px;
  line-height: 18px;
  padding: 0 6px;
  border-radius: 9px;
  z-index: 16;
  opacity: 0.9;
}
</style>
