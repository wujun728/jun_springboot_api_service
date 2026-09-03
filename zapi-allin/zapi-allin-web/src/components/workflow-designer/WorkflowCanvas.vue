<template>
  <div 
    class="workflow-canvas" 
    ref="canvasContainer"
    @mousedown="handleCanvasMouseDown"
    @mousemove="handleCanvasMouseMove"
    @mouseup="handleCanvasMouseUp"
    @mouseleave="handleCanvasMouseUp"
    :class="{ 'is-panning': isPanning }"
  >
    <div class="canvas-content" @drop="handleDrop" @dragover="handleDragOver">
      <!-- 空状态提示 -->
      <div v-if="workflow.nodes.length === 0" class="empty-canvas">
        <div class="empty-content">
          <el-icon class="empty-icon"><VideoPlay /></el-icon>
          <h3>开始设计您的工作流</h3>
          <p>从左侧拖拽节点到画布开始设计</p>
          <div class="quick-actions">
            <el-button type="primary" @click="addQuickStartNode">
              <el-icon><VideoPlay /></el-icon>
              添加开始节点
            </el-button>
          </div>
        </div>
      </div>
      
      <!-- 连接指示器 -->
      <div v-if="connectionState.isConnecting" class="connection-indicator">
        <div class="indicator-content">
          <el-icon class="indicator-icon"><Connection /></el-icon>
          <p>连接模式已激活</p>
          <p class="indicator-tip">点击目标节点的 <span class="highlight">{{ connectionState.startType === 'output' ? '输入点' : '输出点' }}</span> 完成连接</p>
          <el-button size="small" @click="cancelConnection">取消连接</el-button>
        </div>
      </div>
      
      <!-- 工作流节点 -->
      <div
        v-for="node in workflow.nodes"
        :key="node.id"
        class="workflow-node"
        :class="[`node-${node.type}`, { selected: selectedNodeId === node.id }]"
        :style="{
          left: node.position.x + 'px',
          top: node.position.y + 'px'
        }"
        @click="selectNode(node.id)"
        @mousedown="startDrag($event, node)"
      >
        <div class="node-header">
          <el-icon class="node-icon">
            <component :is="getNodeIcon(node.type)" />
          </el-icon>
          <span class="node-title">{{ node.name || getNodeTypeName(node.type) }}</span>
          <el-button
            size="small"
            type="danger"
            circle
            class="delete-btn"
            @click.stop="deleteNode(node.id)"
          >
            <el-icon><Delete /></el-icon>
          </el-button>
        </div>
        <div class="node-content">
          <div class="node-description">{{ node.description || '暂无描述' }}</div>
        </div>
        <!-- 连接点 -->
        <div class="connection-points">
          <!-- 输入点（开始节点不显示） -->
          <div 
            v-if="node.type !== 'start'"
            class="input-point" 
            :class="{ 
              connecting: connectionState.isConnecting && connectionState.startType === 'output',
              active: connectionState.isConnecting && connectionState.startNode === node.id && connectionState.startType === 'input'
            }"
            @click.stop="startConnection(node.id, 'input')"
            title="输入点"
          ></div>
          <!-- 输出点（结束节点不显示） -->
          <div 
            v-if="node.type !== 'end'"
            class="output-point" 
            :class="{ 
              connecting: connectionState.isConnecting && connectionState.startType === 'input',
              active: connectionState.isConnecting && connectionState.startNode === node.id && connectionState.startType === 'output'
            }"
            @click.stop="startConnection(node.id, 'output')"
            title="输出点"
          ></div>
        </div>
      </div>

      <!-- 连接线 -->
      <svg class="connections-svg" :width="canvasSize.width" :height="canvasSize.height">
        <defs>
          <marker id="arrowhead" markerWidth="6" markerHeight="5" refX="0" refY="2.5" orient="auto">
            <polygon points="0 0, 6 2.5, 0 5" :fill="getVar('--text-secondary', '#6b7280')" />
          </marker>
        </defs>
        <path
          v-for="connection in workflow.connections"
          :key="`${connection.from}-${connection.to}`"
          :d="getConnectionPath(connection)"
          :stroke="selectedConnection?.id === connection.id ? getVar('--primary-color', '#6366f1') : getVar('--text-secondary', '#6b7280')"
          :stroke-width="selectedConnection?.id === connection.id ? 3 : 2"
          fill="none"
          marker-end="url(#arrowhead)"
          class="connection-line"
          @click="selectConnection(connection)"
          @contextmenu.prevent="deleteConnection(connection)"
        />
        <!-- 临时连接线 -->
        <path
          v-if="tempConnection"
          :d="tempConnection.path"
          :stroke="getVar('--primary-color', '#6366f1')"
          stroke-width="2"
          fill="none"
          stroke-dasharray="5,5"
          class="temp-connection"
        />
      </svg>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Delete, VideoPlay, User, Document, Setting, Check, Close, 
  Connection, ArrowRight, Timer 
} from '@element-plus/icons-vue'

// 定义接口
interface WorkflowNode {
  id: string
  type: 'start' | 'end' | 'task' | 'decision' | 'parallel' | 'timer' | 'user'
  name: string
  description?: string
  position: { x: number; y: number }
  properties: Record<string, any>
}

interface WorkflowConnection {
  id: string
  from: string
  to: string
  condition?: string
  name?: string
}

interface WorkflowData {
  id: string
  name: string
  nodes: WorkflowNode[]
  connections: WorkflowConnection[]
}

// Props
interface Props {
  modelValue: WorkflowData
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: () => ({
    id: '',
    name: '未命名工作流',
    nodes: [],
    connections: []
  })
})

// Emits
const emit = defineEmits<{
  'update:modelValue': [value: WorkflowData]
  'node-selected': [nodeId: string]
  'connection-selected': [connection: WorkflowConnection]
}>()

// 响应式数据
const canvasContainer = ref<HTMLElement>()
const selectedNodeId = ref<string>('')
const selectedConnection = ref<WorkflowConnection | null>(null)
const dragState = reactive({
  isDragging: false,
  dragNode: null as WorkflowNode | null,
  startPos: { x: 0, y: 0 },
  offset: { x: 0, y: 0 }
})
const connectionState = reactive({
  isConnecting: false,
  startNode: '',
  startType: '' as 'input' | 'output'
})
const tempConnection = ref<{ path: string } | null>(null)
const canvasSize = reactive({ width: 2000, height: 2000 })

// 画布拖拽平移
const isPanning = ref(false)
const panStartX = ref(0)
const panStartY = ref(0)
const scrollStartX = ref(0)
const scrollStartY = ref(0)

// 画布拖拽平移方法
const handleCanvasMouseDown = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  
  // 检查是否点击在节点或连接点上
  const isOnInteractive = target.closest('.workflow-node') || 
                          target.closest('.connection-points') ||
                          target.closest('.delete-btn') ||
                          target.closest('.el-button')
  
  if (isOnInteractive) {
    return
  }
  
  // 左键点击开始拖拽平移
  if (event.button === 0) {
    event.preventDefault()
    isPanning.value = true
    panStartX.value = event.clientX
    panStartY.value = event.clientY
    if (canvasContainer.value) {
      scrollStartX.value = canvasContainer.value.scrollLeft
      scrollStartY.value = canvasContainer.value.scrollTop
    }
  }
}

const handleCanvasMouseMove = (event: MouseEvent) => {
  if (isPanning.value && canvasContainer.value) {
    event.preventDefault()
    const deltaX = event.clientX - panStartX.value
    const deltaY = event.clientY - panStartY.value
    canvasContainer.value.scrollLeft = scrollStartX.value - deltaX
    canvasContainer.value.scrollTop = scrollStartY.value - deltaY
  }
}

const handleCanvasMouseUp = () => {
  isPanning.value = false
}

// 初始化画布位置（滚动到中心）
const initCanvasPosition = (retryCount = 0) => {
  nextTick(() => {
    if (canvasContainer.value) {
      const wrapper = canvasContainer.value
      
      // 如果容器宽度为0，说明DOM还没渲染完，重试
      if (wrapper.clientWidth === 0 && retryCount < 5) {
        setTimeout(() => initCanvasPosition(retryCount + 1), 100)
        return
      }
      
      // 滚动到画布中心位置
      const scrollX = (canvasSize.width - wrapper.clientWidth) / 2
      const scrollY = (canvasSize.height - wrapper.clientHeight) / 2
      
      wrapper.scrollLeft = Math.max(0, scrollX)
      wrapper.scrollTop = Math.max(0, scrollY)
    }
  })
}

onMounted(() => {
  // 初始化画布位置（滚动到中心）
  setTimeout(() => {
    initCanvasPosition(0)
  }, 200)
})

// 计算属性
const workflow = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 节点类型映射
const nodeTypeMap = {
  start: { name: '开始', icon: 'Play', color: '#67c23a' },
  end: { name: '结束', icon: 'Check', color: '#f56c6c' },
  task: { name: '任务', icon: 'Document', color: '#409eff' },
  decision: { name: '决策', icon: 'Connection', color: '#e6a23c' },
  parallel: { name: '并行', icon: 'ArrowRight', color: '#909399' },
  timer: { name: '定时器', icon: 'Timer', color: '#f56c6c' },
  user: { name: '用户任务', icon: 'User', color: '#67c23a' }
}

// 方法
const getVar = (cssVar: string, fallback: string) => {
  if (typeof window !== 'undefined') {
    const value = getComputedStyle(document.documentElement).getPropertyValue(cssVar)
    return value || fallback
  }
  return fallback
}

const getNodeIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    start: VideoPlay,
    end: Check,
    task: Document,
    decision: Connection,
    parallel: ArrowRight,
    timer: Timer,
    user: User
  }
  return iconMap[type] || Document
}

const getNodeTypeName = (type: string) => {
  return nodeTypeMap[type as keyof typeof nodeTypeMap]?.name || '未知'
}

const selectNode = (nodeId: string) => {
  selectedNodeId.value = nodeId
  selectedConnection.value = null
  emit('node-selected', nodeId)
}

const selectConnection = (connection: WorkflowConnection) => {
  selectedConnection.value = connection
  selectedNodeId.value = ''
  emit('connection-selected', connection)
}

const deleteConnection = (connection: WorkflowConnection) => {
  const index = workflow.value.connections.findIndex(c => c.id === connection.id)
  if (index > -1) {
    workflow.value.connections.splice(index, 1)
    if (selectedConnection.value?.id === connection.id) {
      selectedConnection.value = null
    }
    ElMessage.success('连接已删除')
  }
}

const deleteNode = (nodeId: string) => {
  const nodeIndex = workflow.value.nodes.findIndex(n => n.id === nodeId)
  if (nodeIndex > -1) {
    workflow.value.nodes.splice(nodeIndex, 1)
    // 删除相关连接
    workflow.value.connections = workflow.value.connections.filter(
      c => c.from !== nodeId && c.to !== nodeId
    )
    if (selectedNodeId.value === nodeId) {
      selectedNodeId.value = ''
    }
  }
}

const handleDrop = (event: DragEvent) => {
  event.preventDefault()
  const nodeType = event.dataTransfer?.getData('text/plain')
  if (nodeType) {
    const rect = canvasContainer.value?.getBoundingClientRect()
    if (rect) {
      const x = event.clientX - rect.left
      const y = event.clientY - rect.top
      addNode(nodeType, { x, y })
    }
  }
}

const handleDragOver = (event: DragEvent) => {
  event.preventDefault()
}

const addNode = (type: string, position: { x: number; y: number }) => {
  // 调整位置，避免节点重叠，并确保在合理位置
  const adjustedPosition = {
    x: Math.max(50, Math.min(position.x - 90, 1600)), // 确保节点在画布范围内
    y: Math.max(50, Math.min(position.y - 40, 1800))
  }
  
  const newNode: WorkflowNode = {
    id: `node_${Date.now()}`,
    type: type as WorkflowNode['type'],
    name: getNodeTypeName(type),
    position: adjustedPosition,
    properties: {}
  }
  workflow.value.nodes.push(newNode)
  selectNode(newNode.id)
}

const startDrag = (event: MouseEvent, node: WorkflowNode) => {
  if (event.button !== 0) return // 只处理左键
  
  dragState.isDragging = true
  dragState.dragNode = node
  dragState.startPos = { x: event.clientX, y: event.clientY }
  dragState.offset = {
    x: event.clientX - node.position.x,
    y: event.clientY - node.position.y
  }

  const handleMouseMove = (e: MouseEvent) => {
    if (dragState.isDragging && dragState.dragNode) {
      dragState.dragNode.position.x = e.clientX - dragState.offset.x
      dragState.dragNode.position.y = e.clientY - dragState.offset.y
    }
  }

  const handleMouseUp = () => {
    dragState.isDragging = false
    dragState.dragNode = null
    document.removeEventListener('mousemove', handleMouseMove)
    document.removeEventListener('mouseup', handleMouseUp)
  }

  document.addEventListener('mousemove', handleMouseMove)
  document.addEventListener('mouseup', handleMouseUp)
}

const startConnection = (nodeId: string, type: 'input' | 'output') => {
  const node = workflow.value.nodes.find(n => n.id === nodeId)
  if (!node) return
  
  // 验证节点类型限制
  if (type === 'input' && node.type === 'start') {
    ElMessage.warning('开始节点不能有输入连接')
    return
  }
  if (type === 'output' && node.type === 'end') {
    ElMessage.warning('结束节点不能有输出连接')
    return
  }
  
  if (connectionState.isConnecting) {
    // 完成连接
    if (connectionState.startNode !== nodeId) {
      let fromNode = ''
      let toNode = ''
      
      if (connectionState.startType === 'output' && type === 'input') {
        // 从输出点连到输入点
        fromNode = connectionState.startNode
        toNode = nodeId
      } else if (connectionState.startType === 'input' && type === 'output') {
        // 从输入点连到输出点
        fromNode = nodeId
        toNode = connectionState.startNode
      } else {
        ElMessage.warning('连接方向不正确：请从输出点连接到输入点')
        connectionState.isConnecting = false
        connectionState.startNode = ''
        tempConnection.value = null
        return
      }
      
      // 验证目标节点类型
      const fromNodeData = workflow.value.nodes.find(n => n.id === fromNode)
      const toNodeData = workflow.value.nodes.find(n => n.id === toNode)
      
      if (fromNodeData?.type === 'end') {
        ElMessage.warning('结束节点不能有输出连接')
        connectionState.isConnecting = false
        connectionState.startNode = ''
        tempConnection.value = null
        return
      }
      
      if (toNodeData?.type === 'start') {
        ElMessage.warning('开始节点不能有输入连接')
        connectionState.isConnecting = false
        connectionState.startNode = ''
        tempConnection.value = null
        return
      }
      
      // 检查是否已存在连接
      const existingConnection = workflow.value.connections.find(
        c => c.from === fromNode && c.to === toNode
      )
      
      if (existingConnection) {
        ElMessage.warning('该连接已存在')
      } else {
        const connection: WorkflowConnection = {
          id: `conn_${Date.now()}`,
          from: fromNode,
          to: toNode,
          name: ''
        }
        workflow.value.connections.push(connection)
        ElMessage.success('连接创建成功')
      }
    }
    connectionState.isConnecting = false
    connectionState.startNode = ''
    tempConnection.value = null
  } else {
    // 开始连接
    connectionState.isConnecting = true
    connectionState.startNode = nodeId
    connectionState.startType = type
    ElMessage.info(`开始连接，请点击目标节点的${type === 'output' ? '输入' : '输出'}点`)
  }
}

// 画布常量
const CANVAS_CONSTANTS = {
  NODE_WIDTH: 180,
  NODE_HEIGHT: 80,
  INPUT_POINT_OFFSET: -14 // 输入点（连线尾部）向后偏移量（负值表示向左，正值表示向右）
} as const

const getNodeConnectionPoint = (nodeId: string, type: 'input' | 'output') => {
  const node = workflow.value.nodes.find(n => n.id === nodeId)
  if (!node) return { x: 0, y: 0 }
  
  const centerY = node.position.y + CANVAS_CONSTANTS.NODE_HEIGHT / 2 // 节点高度的一半
  
  if (type === 'output') {
    return {
      x: node.position.x + CANVAS_CONSTANTS.NODE_WIDTH, // 节点右边
      y: centerY
    }
  } else {
    return {
      x: node.position.x + CANVAS_CONSTANTS.INPUT_POINT_OFFSET, // 节点左边，连线尾部往后移
      y: centerY
    }
  }
}

const getConnectionPath = (connection: WorkflowConnection) => {
  const fromPos = getNodeConnectionPoint(connection.from, 'output')
  const toPos = getNodeConnectionPoint(connection.to, 'input')
  
  const dx = toPos.x - fromPos.x
  const dy = toPos.y - fromPos.y
  
  // 创建更自然的贝塞尔曲线
  const controlDistance = Math.max(Math.abs(dx) * 0.3, 50)
  const cp1x = fromPos.x + controlDistance
  const cp1y = fromPos.y
  const cp2x = toPos.x - controlDistance
  const cp2y = toPos.y
  
  return `M ${fromPos.x} ${fromPos.y} C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${toPos.x} ${toPos.y}`
}

// 生命周期
onMounted(() => {
  // 初始化画布
  if (canvasContainer.value) {
    const rect = canvasContainer.value.getBoundingClientRect()
    canvasSize.width = Math.max(rect.width, 2000)
    canvasSize.height = Math.max(rect.height, 2000)
  }
})

// 取消连接
const cancelConnection = () => {
  connectionState.isConnecting = false
  connectionState.startNode = ''
  tempConnection.value = null
  ElMessage.info('已取消连接操作')
}

// 快速添加开始节点
const addQuickStartNode = () => {
  addNode('start', { x: 400, y: 200 })
}

// 暴露方法给父组件
defineExpose({
  addNode,
  selectNode,
  deleteNode,
  deleteConnection,
  addQuickStartNode,
  centerCanvas: () => initCanvasPosition(0)
})
</script>

<style scoped lang="scss">
.workflow-canvas {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: auto;
  background: #ffffff;
  cursor: grab;
  
  &.is-panning {
    cursor: grabbing;
    user-select: none;
  }
  
  // 滚动条样式
  &::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }
  
  &::-webkit-scrollbar-track {
    background: var(--bg-tertiary, #f1f5f9);
    border-radius: 5px;
  }
  
  &::-webkit-scrollbar-thumb {
    background: var(--scrollbar-thumb-color, rgba(99, 102, 241, 0.2));
    border-radius: 5px;
    border: 2px solid var(--bg-tertiary, #f1f5f9);
    
    &:hover {
      background: var(--scrollbar-thumb-hover-color, rgba(99, 102, 241, 0.3));
    }
  }
  
  &::-webkit-scrollbar-corner {
    background: var(--bg-tertiary, #f1f5f9);
  }
}

.canvas-content {
  width: 2000px;
  height: 2000px;
  position: relative;
}

.empty-canvas {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  z-index: 10;
}

.empty-content {
  padding: 40px;
  background: var(--bg-card, #ffffff);
  border: 2px dashed var(--border-color, #e5e7eb);
  border-radius: 12px;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
  
  .empty-icon {
    font-size: 48px;
    color: var(--primary-color, #6366f1);
    margin-bottom: 16px;
  }
  
  h3 {
    margin: 0 0 8px 0;
    font-size: 18px;
    color: var(--text-primary, #1f2937);
    font-weight: 600;
  }
  
  p {
    margin: 0 0 20px 0;
    color: var(--text-secondary, #6b7280);
    font-size: 14px;
  }
  
  .quick-actions {
    display: flex;
    justify-content: center;
    gap: 12px;
  }
}

.connection-indicator {
  position: fixed;
  top: 120px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
}

.indicator-content {
  padding: 16px 20px;
  background: var(--primary-color, #6366f1);
  color: white;
  border-radius: 8px;
  box-shadow: var(--shadow-lg, 0 10px 15px -3px rgba(0, 0, 0, 0.1));
  text-align: center;
  animation: slideDown 0.3s ease;
  
  .indicator-icon {
    font-size: 24px;
    margin-bottom: 8px;
  }
  
  p {
    margin: 4px 0;
    font-size: 14px;
    
    &.indicator-tip {
      font-size: 12px;
      opacity: 0.9;
      
      .highlight {
        background: rgba(255, 255, 255, 0.2);
        padding: 2px 6px;
        border-radius: 4px;
        font-weight: bold;
      }
    }
  }
  
  .el-button {
    margin-top: 8px;
  }
}

@keyframes slideDown {
  from {
    transform: translateX(-50%) translateY(-20px);
    opacity: 0;
  }
  to {
    transform: translateX(-50%) translateY(0);
    opacity: 1;
  }
}

.workflow-node {
  position: absolute;
  width: 180px;
  min-height: 80px;
  background: var(--bg-card, #ffffff);
  border: 2px solid var(--border-color, #e5e7eb);
  border-radius: 8px;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
  cursor: move;
  transition: all 0.3s ease;
  
  &:hover {
    box-shadow: var(--shadow-lg, 0 10px 15px -3px rgba(0, 0, 0, 0.1));
    border-color: var(--primary-color, #6366f1);
  }
  
  &.selected {
    border-color: var(--primary-color, #6366f1);
    box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
  }
}

.node-header {
  display: flex;
  align-items: center;
  padding: 10px 12px;
  border-bottom: 1px solid var(--border-color, #e5e7eb);
  background: var(--bg-tertiary, #f1f5f9);
  border-radius: 6px 6px 0 0;
  position: relative;
  
  .node-icon {
    margin-right: 8px;
    color: var(--primary-color, #6366f1);
  }
  
  .node-title {
    flex: 1;
    font-weight: 500;
    color: var(--text-primary, #1f2937);
    font-size: 13px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  
  .delete-btn {
    opacity: 0;
    transition: opacity 0.3s ease;
  }
}

.workflow-node:hover .delete-btn {
  opacity: 1;
}

.node-content {
  padding: 8px 12px;
  
  .node-description {
    font-size: 11px;
    color: var(--text-muted, #9ca3af);
    line-height: 1.3;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

.connection-points {
  .input-point,
  .output-point {
    position: absolute;
    width: 16px;
    height: 16px;
    background: var(--primary-color, #6366f1);
    border: 3px solid white;
    border-radius: 50%;
    cursor: pointer;
    opacity: 0.8;
    transition: all 0.3s ease;
    z-index: 10;
    
    &:hover {
      transform: scale(1.4);
      opacity: 1 !important;
      box-shadow: 0 0 12px rgba(99, 102, 241, 0.6);
      background: var(--primary-hover, #4f46e5);
    }
    
    &:active {
      transform: scale(1.2);
    }
  }
  
  .input-point {
    left: -8px;
    top: 50%;
    transform: translateY(-50%);
    
    &::before {
      content: '入';
      position: absolute;
      left: -25px;
      top: 50%;
      transform: translateY(-50%);
      font-size: 10px;
      color: var(--text-muted, #9ca3af);
      background: rgba(255, 255, 255, 0.9);
      padding: 2px 4px;
      border-radius: 3px;
      opacity: 0;
      transition: opacity 0.3s ease;
      pointer-events: none;
    }
    
    &:hover::before {
      opacity: 1;
    }
  }
  
  .output-point {
    right: -8px;
    top: 50%;
    transform: translateY(-50%);
    
    &::before {
      content: '出';
      position: absolute;
      right: -25px;
      top: 50%;
      transform: translateY(-50%);
      font-size: 10px;
      color: var(--text-muted, #9ca3af);
      background: rgba(255, 255, 255, 0.9);
      padding: 2px 4px;
      border-radius: 3px;
      opacity: 0;
      transition: opacity 0.3s ease;
      pointer-events: none;
    }
    
    &:hover::before {
      opacity: 1;
    }
  }
}

.workflow-node .connection-points {
  .input-point,
  .output-point {
    opacity: 1; /* 始终显示连接点 */
  }
}

.workflow-node:hover .connection-points {
  .input-point {
    opacity: 1;
    transform: translateY(-50%) scale(1.1); /* 悬停时稍微放大 */
  }
  
  .output-point {
    opacity: 1;
    transform: translateY(-50%) scale(1.1); /* 悬停时稍微放大 */
  }
}

.connection-points {
  .input-point.connecting {
    opacity: 1 !important;
    background: var(--success-color, #10b981) !important;
    animation: pulse 1.5s infinite;
  }
  
  .output-point.connecting {
    opacity: 1 !important;
    background: var(--success-color, #10b981) !important;
    animation: pulse 1.5s infinite;
  }
  
  .input-point.active,
  .output-point.active {
    opacity: 1 !important;
    background: var(--warning-color, #f59e0b) !important;
    transform: scale(1.5);
    box-shadow: 0 0 12px rgba(245, 158, 11, 0.6);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.2);
    opacity: 0.8;
  }
}

.connections-svg {
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
  z-index: 1;
  
  .connection-line {
    pointer-events: stroke;
    cursor: pointer;
    transition: all 0.3s ease;
    
    &:hover {
      stroke: var(--primary-color, #6366f1) !important;
      stroke-width: 3 !important;
      filter: drop-shadow(0 0 4px rgba(99, 102, 241, 0.4));
    }
  }
  
  .temp-connection {
    pointer-events: none;
  }
}

// 节点类型样式
.node-start .node-header {
  background: linear-gradient(135deg, var(--success-color, #10b981), #34d399);
  color: white;
  
  .node-icon {
    color: white;
  }
}

.node-end .node-header {
  background: linear-gradient(135deg, var(--danger-color, #ef4444), #f87171);
  color: white;
  
  .node-icon {
    color: white;
  }
}

.node-task .node-header {
  background: var(--gradient-primary, linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%));
  color: white;
  
  .node-icon {
    color: white;
  }
}

.node-decision .node-header {
  background: linear-gradient(135deg, var(--warning-color, #f59e0b), #fbbf24);
  color: white;
  
  .node-icon {
    color: white;
  }
}

.node-user .node-header {
  background: var(--gradient-secondary, linear-gradient(135deg, #10b981 0%, #059669 100%));
  color: white;
  
  .node-icon {
    color: white;
  }
}
</style>
