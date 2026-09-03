<template>
  <div class="action-designer">
    <!-- 左侧节点面板 -->
    <aside class="node-panel" :class="{ 'is-collapsed': isNodePanelCollapsed }">
      <div class="panel-header">
        <span class="panel-title" v-if="!isNodePanelCollapsed">节点库</span>
        <el-icon 
          class="collapse-icon" 
          @click="isNodePanelCollapsed = !isNodePanelCollapsed"
          :title="isNodePanelCollapsed ? '展开' : '折叠'"
        >
          <DArrowLeft v-if="!isNodePanelCollapsed" />
          <DArrowRight v-else />
        </el-icon>
      </div>
      <div class="panel-content" v-show="!isNodePanelCollapsed">
        <div class="node-category">
          <div class="category-title">基础节点</div>
          <div class="node-list">
            <div
              v-for="node in basicNodes"
              :key="node.type"
              class="node-item"
              draggable="true"
              @dragstart="handleNodeDragStart($event, node)"
            >
              <el-icon :size="20" :color="node.color">
                <component :is="node.icon" />
              </el-icon>
              <span class="node-name">{{ node.name }}</span>
            </div>
          </div>
        </div>

        <div class="node-category">
          <div class="category-title">执行节点</div>
          <div class="node-list">
            <div
              v-for="node in executeNodes"
              :key="node.type"
              class="node-item"
              draggable="true"
              @dragstart="handleNodeDragStart($event, node)"
            >
              <el-icon :size="20" :color="node.color">
                <component :is="node.icon" />
              </el-icon>
              <span class="node-name">{{ node.name }}</span>
            </div>
          </div>
        </div>
      </div>
    </aside>

    <!-- 中间画布区域 -->
    <div class="canvas-container">
      <div class="canvas-toolbar">
        <el-button-group>
          <el-button size="small" @click="handleZoomIn">
            <el-icon><ZoomIn /></el-icon>
          </el-button>
          <el-button size="small" @click="handleZoomOut">
            <el-icon><ZoomOut /></el-icon>
          </el-button>
          <el-button size="small" @click="handleResetZoom">
            <el-icon><Refresh /></el-icon>
          </el-button>
        </el-button-group>
        <el-button-group>
          <el-button size="small" @click="handleFullscreen" :title="isFullscreen ? '退出全屏' : '全屏编辑'">
            <el-icon><FullScreen /></el-icon>
          </el-button>
          <el-button size="small" @click="handlePreview" title="预览">
            <el-icon><View /></el-icon>
          </el-button>
        </el-button-group>
        <el-button size="small" @click="handleSave" type="primary">
          <el-icon><Select /></el-icon>
          保存
        </el-button>
      </div>

      <div
        ref="canvasRef"
        class="canvas"
        @drop="handleCanvasDrop"
        @dragover.prevent
        @click="handleCanvasClick"
      >
        <div class="canvas-content" :style="{ transform: `scale(${scale})`, transformOrigin: '0 0' }">
          <!-- 渲染节点 -->
          <div
            v-for="node in nodes"
            :key="node.id"
            class="flow-node"
            :class="[
              `node-${node.type}`, 
              { 
                'is-selected': selectedNodeId === node.id,
                'is-dragging': draggingNode && draggingNode.id === node.id
              }
            ]"
            :style="{
              left: node.x + 'px',
              top: node.y + 'px'
            }"
            @click.stop="handleNodeClick(node)"
            @mousedown="handleNodeMouseDown($event, node)"
          >
            <div class="node-header" :style="{ background: node.color }">
              <el-icon :size="16">
                <component :is="node.icon" />
              </el-icon>
              <span class="node-title">{{ node.name }}</span>
              <el-icon class="node-delete" @click.stop="handleDeleteNode(node)">
                <Close />
              </el-icon>
            </div>
            <div class="node-body">
              <div class="node-desc">{{ node.description || '点击配置节点' }}</div>
            </div>
            <!-- 连接点 -->
            <div
              class="node-anchor node-anchor-left"
              data-anchor-type="input"
            ></div>
            <div
              class="node-anchor node-anchor-right"
              data-anchor-type="output"
              @mousedown.stop="handleAnchorMouseDown($event, node, 'output')"
            ></div>
          </div>

          <!-- 渲染连线 -->
          <svg class="connections-layer" :style="{ width: canvasSize.width + 'px', height: canvasSize.height + 'px' }">
            <defs>
              <marker
                id="arrowhead"
                markerWidth="12"
                markerHeight="12"
                refX="11"
                refY="6"
                orient="auto"
              >
                <path d="M2,2 L10,6 L2,10 L4,6 L2,2" fill="#52c41a" />
              </marker>
              <marker
                id="arrowhead-hover"
                markerWidth="12"
                markerHeight="12"
                refX="11"
                refY="6"
                orient="auto"
              >
                <path d="M2,2 L10,6 L2,10 L4,6 L2,2" fill="#73d13d" />
              </marker>
            </defs>
            
            <!-- 连线底层（用于点击检测） -->
            <path
              v-for="conn in connections"
              :key="'hit-' + conn.id"
              :d="getConnectionPath(conn)"
              class="connection-hit-area"
              stroke="transparent"
              stroke-width="20"
              fill="none"
              @mouseenter="handleConnectionHover(conn)"
              @mouseleave="handleConnectionLeave"
            />
            
            <!-- 连线显示层 -->
            <path
              v-for="conn in connections"
              :key="conn.id"
              :d="getConnectionPath(conn)"
              class="connection-line"
              :class="{ 'is-hover': hoveredConnectionId === conn.id }"
              stroke="#52c41a"
              stroke-width="2"
              fill="none"
              :marker-end="hoveredConnectionId === conn.id ? 'url(#arrowhead-hover)' : 'url(#arrowhead)'"
            />
            
            <!-- 连线中点删除按钮 -->
            <g v-if="hoveredConnectionId" :key="'delete-' + hoveredConnectionId">
              <circle
                :cx="hoveredConnectionMidpoint.x"
                :cy="hoveredConnectionMidpoint.y"
                r="12"
                fill="white"
                stroke="#ef4444"
                stroke-width="2"
                class="connection-delete-btn"
                @click.stop="handleDeleteConnection(hoveredConnectionId)"
              />
              <text
                :x="hoveredConnectionMidpoint.x"
                :y="hoveredConnectionMidpoint.y + 4"
                text-anchor="middle"
                fill="#ef4444"
                font-size="12"
                font-weight="bold"
                class="connection-delete-icon"
                @click.stop="handleDeleteConnection(hoveredConnectionId)"
              >
                ×
              </text>
            </g>
            
            <!-- 临时连线 -->
            <path
              v-if="tempConnection"
              :d="tempConnection.path"
              class="connection-line temp-connection"
              stroke="#52c41a"
              stroke-width="2"
              fill="none"
              stroke-dasharray="5,5"
              pointer-events="none"
            />
          </svg>
        </div>
      </div>
    </div>

    <!-- 右侧属性面板 -->
    <aside class="property-panel" :class="{ 'is-collapsed': isPropertyPanelCollapsed }">
      <div class="panel-header">
        <el-icon 
          class="collapse-icon" 
          @click="isPropertyPanelCollapsed = !isPropertyPanelCollapsed"
          :title="isPropertyPanelCollapsed ? '展开' : '折叠'"
        >
          <DArrowRight v-if="!isPropertyPanelCollapsed" />
          <DArrowLeft v-else />
        </el-icon>
        <span class="panel-title" v-if="!isPropertyPanelCollapsed">{{ selectedNode ? '节点配置' : '属性面板' }}</span>
      </div>
      <div class="panel-content" v-show="!isPropertyPanelCollapsed">
        <div v-if="selectedNode" class="node-config">
          <el-form :model="selectedNode" label-width="80px" size="small">
            <el-form-item label="节点名称">
              <el-input v-model="selectedNode.name" />
            </el-form-item>
            <el-form-item label="节点描述">
              <el-input v-model="selectedNode.description" type="textarea" :rows="2" />
            </el-form-item>

            <!-- 开始节点配置 -->
            <template v-if="selectedNode.type === 'start'">
              <el-form-item label="触发方式">
                <el-select v-model="selectedNode.config.triggerType">
                  <el-option label="手动触发" value="manual" />
                  <el-option label="定时触发" value="schedule" />
                  <el-option label="事件触发" value="event" />
                </el-select>
              </el-form-item>
            </template>

            <!-- API执行节点配置 -->
            <template v-if="selectedNode.type === 'api'">
              <el-form-item label="请求方法">
                <el-select v-model="selectedNode.config.method">
                  <el-option label="GET" value="GET" />
                  <el-option label="POST" value="POST" />
                  <el-option label="PUT" value="PUT" />
                  <el-option label="DELETE" value="DELETE" />
                </el-select>
              </el-form-item>
              <el-form-item label="请求URL">
                <el-input v-model="selectedNode.config.url" placeholder="https://api.example.com" />
              </el-form-item>
              <el-form-item label="请求头">
                <el-input v-model="selectedNode.config.headers" type="textarea" :rows="3" placeholder='{"Content-Type": "application/json"}' />
              </el-form-item>
              <el-form-item label="请求体">
                <el-input v-model="selectedNode.config.body" type="textarea" :rows="4" />
              </el-form-item>
            </template>

            <!-- 消息通知节点配置 -->
            <template v-if="selectedNode.type === 'notification'">
              <el-form-item label="通知类型">
                <el-select v-model="selectedNode.config.notifyType">
                  <el-option label="邮件" value="email" />
                  <el-option label="短信" value="sms" />
                  <el-option label="企业微信" value="wechat" />
                  <el-option label="钉钉" value="dingtalk" />
                </el-select>
              </el-form-item>
              <el-form-item label="接收人">
                <el-input v-model="selectedNode.config.receiver" placeholder="多个接收人用逗号分隔" />
              </el-form-item>
              <el-form-item label="消息标题">
                <el-input v-model="selectedNode.config.title" />
              </el-form-item>
              <el-form-item label="消息内容">
                <el-input v-model="selectedNode.config.content" type="textarea" :rows="4" />
              </el-form-item>
            </template>

            <!-- 条件判断节点配置 -->
            <template v-if="selectedNode.type === 'condition'">
              <el-form-item label="条件表达式">
                <el-input v-model="selectedNode.config.expression" type="textarea" :rows="3" placeholder="例如: ${result.code} == 200" />
              </el-form-item>
            </template>

            <!-- 数据转换节点配置 -->
            <template v-if="selectedNode.type === 'transform'">
              <el-form-item label="转换脚本">
                <el-input v-model="selectedNode.config.script" type="textarea" :rows="6" placeholder="JavaScript代码" />
              </el-form-item>
            </template>
          </el-form>
        </div>
        <el-empty v-else description="请选择一个节点" :image-size="100" />
      </div>
    </aside>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  VideoPlay,
  Connection,
  Bell,
  Position,
  Close,
  ZoomIn,
  ZoomOut,
  Refresh,
  Select,
  Operation,
  Share,
  DArrowLeft,
  DArrowRight,
  FullScreen,
  View
} from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: Object,
    default: () => ({ nodes: [], connections: [] })
  }
})

const emit = defineEmits(['update:modelValue', 'save'])

// 节点库定义
const basicNodes = [
  { type: 'start', name: '开始', icon: VideoPlay, color: '#67C23A' },
  { type: 'end', name: '结束', icon: Position, color: '#F56C6C' }
]

const executeNodes = [
  { type: 'api', name: 'API执行', icon: Connection, color: '#409EFF' },
  { type: 'notification', name: '消息通知', icon: Bell, color: '#E6A23C' },
  { type: 'condition', name: '条件判断', icon: Share, color: '#909399' },
  { type: 'transform', name: '数据转换', icon: Operation, color: '#9C27B0' }
]

// 画布相关
const canvasRef = ref(null)
const scale = ref(1)
const nodes = ref([])
const connections = ref([])
const selectedNodeId = ref(null)
const selectedNode = computed(() => nodes.value.find(n => n.id === selectedNodeId.value))

// 面板折叠状态
const isNodePanelCollapsed = ref(false)
const isPropertyPanelCollapsed = ref(false)
const isFullscreen = ref(false)
const isPreviewMode = ref(false)

// 计算画布大小
const canvasSize = computed(() => {
  if (nodes.value.length === 0) {
    return { width: 2000, height: 2000 }
  }
  
  let maxX = 0
  let maxY = 0
  
  nodes.value.forEach(node => {
    maxX = Math.max(maxX, node.x + 200)
    maxY = Math.max(maxY, node.y + 150)
  })
  
  return {
    width: Math.max(2000, maxX + 500),
    height: Math.max(2000, maxY + 500)
  }
})

// 拖拽相关
const draggedNodeType = ref(null)
const draggingNode = ref(null)
const dragOffset = ref({ x: 0, y: 0 })

// 连线相关
const connectingFrom = ref(null)
const tempConnection = ref(null)
const isConnecting = ref(false)
const hoveredConnectionId = ref(null)
const hoveredConnectionMidpoint = ref({ x: 0, y: 0 })

let nodeIdCounter = 1

// 节点拖拽开始
const handleNodeDragStart = (event, node) => {
  draggedNodeType.value = node
  event.dataTransfer.effectAllowed = 'copy'
}

// 画布放置节点
const handleCanvasDrop = (event) => {
  event.preventDefault()
  if (!draggedNodeType.value) return

  const rect = canvasRef.value.getBoundingClientRect()
  const x = (event.clientX - rect.left) / scale.value - 75
  const y = (event.clientY - rect.top) / scale.value - 40

  const newNode = {
    id: `node_${nodeIdCounter++}`,
    type: draggedNodeType.value.type,
    name: draggedNodeType.value.name,
    icon: draggedNodeType.value.icon,
    color: draggedNodeType.value.color,
    x,
    y,
    description: '',
    config: {}
  }

  nodes.value.push(newNode)
  draggedNodeType.value = null
}

// 节点点击
const handleNodeClick = (node) => {
  selectedNodeId.value = node.id
}

// 画布点击
const handleCanvasClick = () => {
  selectedNodeId.value = null
  exitPreview()
}

// 节点拖动
const handleNodeMouseDown = (event, node) => {
  if (event.button !== 0) return
  event.stopPropagation()
  
  draggingNode.value = node
  const rect = canvasRef.value.getBoundingClientRect()
  dragOffset.value = {
    x: (event.clientX - rect.left) / scale.value - node.x,
    y: (event.clientY - rect.top) / scale.value - node.y
  }
  
  // 添加拖拽样式
  document.body.style.cursor = 'grabbing'
  document.body.style.userSelect = 'none'
  
  // 使用passive: false以允许preventDefault
  document.addEventListener('mousemove', handleNodeMouseMove, { passive: false })
  document.addEventListener('mouseup', handleNodeMouseUp)
}

const handleNodeMouseMove = (event) => {
  if (!draggingNode.value) return
  
  event.preventDefault()
  
  const rect = canvasRef.value.getBoundingClientRect()
  const newX = (event.clientX - rect.left) / scale.value - dragOffset.value.x
  const newY = (event.clientY - rect.top) / scale.value - dragOffset.value.y
  
  // 直接更新位置，让节点可以自由移动
  draggingNode.value.x = newX
  draggingNode.value.y = newY
}

const handleNodeMouseUp = () => {
  draggingNode.value = null
  
  // 恢复样式
  document.body.style.cursor = ''
  document.body.style.userSelect = ''
  
  document.removeEventListener('mousemove', handleNodeMouseMove)
  document.removeEventListener('mouseup', handleNodeMouseUp)
}

// 连接点拖动
const handleAnchorMouseDown = (event, node, type) => {
  event.preventDefault()
  event.stopPropagation()
  
  if (type === 'output') {
    connectingFrom.value = node
    isConnecting.value = true
    document.body.style.cursor = 'crosshair'
    document.body.style.userSelect = 'none'
    
    // 立即显示初始连线
    const canvas = canvasRef.value
    const rect = canvas.getBoundingClientRect()
    const scrollLeft = canvas.scrollLeft
    const scrollTop = canvas.scrollTop
    
    const startX = node.x + 150
    const startY = node.y + 40
    const endX = (event.clientX - rect.left + scrollLeft) / scale.value
    const endY = (event.clientY - rect.top + scrollTop) / scale.value
    
    tempConnection.value = {
      path: createCurvePath(startX, startY, endX, endY)
    }
    
    document.addEventListener('mousemove', handleConnectionMouseMove, { passive: false })
    document.addEventListener('mouseup', handleConnectionMouseUp)
  }
}

const handleConnectionMouseMove = (event) => {
  if (!connectingFrom.value || !isConnecting.value) return
  
  event.preventDefault()
  
  const canvas = canvasRef.value
  const rect = canvas.getBoundingClientRect()
  const scrollLeft = canvas.scrollLeft
  const scrollTop = canvas.scrollTop
  
  // 计算起点（输出锚点位置）
  const startX = connectingFrom.value.x + 150
  const startY = connectingFrom.value.y + 40
  
  // 计算终点（鼠标位置，考虑缩放和滚动）
  const endX = (event.clientX - rect.left + scrollLeft) / scale.value
  const endY = (event.clientY - rect.top + scrollTop) / scale.value
  
  // 直接更新，不使用requestAnimationFrame（更流畅）
  tempConnection.value = {
    path: createCurvePath(startX, startY, endX, endY)
  }
}

const handleConnectionMouseUp = (event) => {
  if (!connectingFrom.value) return
  
  event.preventDefault()
  
  // 立即清除临时连线和状态
  const fromNode = connectingFrom.value
  connectingFrom.value = null
  tempConnection.value = null
  isConnecting.value = false
  document.body.style.cursor = ''
  document.body.style.userSelect = ''
  document.removeEventListener('mousemove', handleConnectionMouseMove)
  document.removeEventListener('mouseup', handleConnectionMouseUp)
  
  // 查找鼠标位置下的节点
  const canvas = canvasRef.value
  const rect = canvas.getBoundingClientRect()
  const scrollLeft = canvas.scrollLeft
  const scrollTop = canvas.scrollTop
  
  const mouseX = (event.clientX - rect.left + scrollLeft) / scale.value
  const mouseY = (event.clientY - rect.top + scrollTop) / scale.value
  
  // 检查是否在某个节点的输入点范围内
  const targetNode = nodes.value.find(n => {
    if (n.id === fromNode.id) return false
    
    // 输入锚点在节点左侧中间
    const anchorX = n.x
    const anchorY = n.y + 40
    
    // 检查鼠标是否在输入点附近（扩大检测范围到40px）
    const distance = Math.sqrt(
      Math.pow(mouseX - anchorX, 2) + 
      Math.pow(mouseY - anchorY, 2)
    )
    
    return distance < 40
  })
  
  if (targetNode) {
    // 检查是否已存在相同的连接
    const existingConnection = connections.value.find(
      c => c.from === fromNode.id && c.to === targetNode.id
    )
    
    if (!existingConnection) {
      connections.value.push({
        id: `conn_${Date.now()}`,
        from: fromNode.id,
        to: targetNode.id
      })
      ElMessage.success('连接成功')
    } else {
      ElMessage.warning('连接已存在')
    }
  }
}

// 获取连线路径
const getConnectionPath = (conn) => {
  const fromNode = nodes.value.find(n => n.id === conn.from)
  const toNode = nodes.value.find(n => n.id === conn.to)
  
  if (!fromNode || !toNode) return ''
  
  const startX = fromNode.x + 150
  const startY = fromNode.y + 40
  const endX = toNode.x
  const endY = toNode.y + 40
  
  return createCurvePath(startX, startY, endX, endY)
}

// 创建曲线路径
const createCurvePath = (x1, y1, x2, y2) => {
  const dx = x2 - x1
  const offsetX = Math.abs(dx) * 0.5
  
  return `M ${x1},${y1} C ${x1 + offsetX},${y1} ${x2 - offsetX},${y2} ${x2},${y2}`
}

// 删除节点
const handleDeleteNode = (node) => {
  nodes.value = nodes.value.filter(n => n.id !== node.id)
  connections.value = connections.value.filter(c => c.from !== node.id && c.to !== node.id)
  if (selectedNodeId.value === node.id) {
    selectedNodeId.value = null
  }
}

// 连线hover处理
const handleConnectionHover = (conn) => {
  hoveredConnectionId.value = conn.id
  
  // 计算连线中点
  const fromNode = nodes.value.find(n => n.id === conn.from)
  const toNode = nodes.value.find(n => n.id === conn.to)
  
  if (fromNode && toNode) {
    const startX = fromNode.x + 150
    const startY = fromNode.y + 40
    const endX = toNode.x
    const endY = toNode.y + 40
    
    hoveredConnectionMidpoint.value = {
      x: (startX + endX) / 2,
      y: (startY + endY) / 2
    }
  }
}

const handleConnectionLeave = () => {
  hoveredConnectionId.value = null
}

// 删除连线
const handleDeleteConnection = (connId) => {
  connections.value = connections.value.filter(c => c.id !== connId)
  hoveredConnectionId.value = null
  ElMessage.success('删除成功')
}

// 缩放控制
const handleZoomIn = () => {
  scale.value = Math.min(scale.value + 0.1, 2)
}

const handleZoomOut = () => {
  scale.value = Math.max(scale.value - 0.1, 0.5)
}

const handleResetZoom = () => {
  scale.value = 1
}

// 保存
const handleSave = () => {
  const data = {
    nodes: nodes.value,
    connections: connections.value
  }
  emit('update:modelValue', data)
  emit('save', data)
}

// 全屏编辑
const handleFullscreen = () => {
  const designerEl = document.querySelector('.action-designer')
  if (!designerEl) return
  
  if (!isFullscreen.value) {
    if (designerEl.requestFullscreen) {
      designerEl.requestFullscreen()
    }
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
    }
  }
}

// 监听全屏状态变化
const handleFullscreenChange = () => {
  isFullscreen.value = !!document.fullscreenElement
}

// 预览
const handlePreview = () => {
  isPreviewMode.value = true
  isNodePanelCollapsed.value = true
  isPropertyPanelCollapsed.value = true
  ElMessage.success('预览模式已开启，点击画布退出预览')
}

// 退出预览
const exitPreview = () => {
  if (isPreviewMode.value) {
    isPreviewMode.value = false
    isNodePanelCollapsed.value = false
    isPropertyPanelCollapsed.value = false
    ElMessage.info('已退出预览模式')
  }
}

// 初始化
onMounted(() => {
  if (props.modelValue.nodes) {
    nodes.value = props.modelValue.nodes
    connections.value = props.modelValue.connections || []
  }
  
  // 监听全屏状态变化
  document.addEventListener('fullscreenchange', handleFullscreenChange)
})

onUnmounted(() => {
  document.removeEventListener('mousemove', handleNodeMouseMove)
  document.removeEventListener('mouseup', handleNodeMouseUp)
  document.removeEventListener('mousemove', handleConnectionMouseMove)
  document.removeEventListener('mouseup', handleConnectionMouseUp)
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})
</script>

<style lang="scss" scoped>
.action-designer {
  display: flex;
  height: 100%;
  background: var(--bg-primary);
  position: relative;
  
  &:fullscreen {
    background: var(--bg-primary);
    
    .canvas-container {
      .canvas {
        background: #fafafa;
      }
    }
  }
}

.node-panel,
.property-panel {
  width: 260px;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  transition: all 0.3s ease;

  &.is-collapsed {
    width: 40px;
    
    .panel-content {
      display: none;
    }
  }

  .panel-header {
    height: 50px;
    display: flex;
    align-items: center;
    padding: 0 16px;
    border-bottom: 1px solid var(--border-color);
    position: relative;

    .panel-title {
      font-size: 14px;
      font-weight: 600;
      color: var(--text-primary);
      flex: 1;
    }
    
    .collapse-icon {
      font-size: 16px;
      color: var(--text-secondary);
      cursor: pointer;
      padding: 4px;
      border-radius: 4px;
      transition: all 0.2s;
      
      &:hover {
        background: var(--bg-hover);
        color: var(--primary-color);
      }
    }
  }

  .panel-content {
    flex: 1;
    overflow-y: auto;
    padding: 16px;
  }
}

.property-panel {
  border-right: none;
  border-left: 1px solid var(--border-color);
  
  .panel-header {
    justify-content: flex-start;
    gap: 8px;
  }
}

.node-category {
  margin-bottom: 20px;

  .category-title {
    font-size: 13px;
    font-weight: 600;
    color: var(--text-secondary);
    margin-bottom: 12px;
  }

  .node-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .node-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px 12px;
    background: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 6px;
    cursor: move;
    transition: all 0.2s;

    &:hover {
      border-color: var(--primary-color);
      box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
    }

    .node-name {
      font-size: 13px;
      color: var(--text-primary);
    }
  }
}

.canvas-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: relative;

  .canvas-toolbar {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    border-bottom: 1px solid var(--border-color);
    background: var(--bg-secondary);
    gap: 12px;
  }

  .canvas {
    flex: 1;
    position: relative;
    background: #f5f5f5;
    overflow: auto;
    will-change: scroll-position;
    scrollbar-width: thin;

    &::-webkit-scrollbar {
      width: 8px;
      height: 8px;
    }

    &::-webkit-scrollbar-track {
      background: #f1f1f1;
    }

    &::-webkit-scrollbar-thumb {
      background: #888;
      border-radius: 4px;

      &:hover {
        background: #555;
      }
    }

    .canvas-content {
      position: relative;
      min-width: 100%;
      min-height: 100%;
    }
  }
}

.flow-node {
  position: absolute;
  width: 150px;
  background: white;
  border: 2px solid #e8e8e8;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  cursor: grab;
  user-select: none;
  will-change: transform;

  &.is-selected {
    box-shadow: 0 4px 16px rgba(64, 158, 255, 0.3);
    border-color: var(--primary-color);
  }

  &.is-dragging {
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
    opacity: 0.95;
    cursor: grabbing;
    z-index: 1000;
  }

  &:hover:not(.is-dragging) {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
  }

  &:active {
    cursor: grabbing;
  }

  .node-header {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 8px 12px;
    color: white;
    border-radius: 2px 2px 0 0;
    font-size: 13px;
    font-weight: 500;

    .node-title {
      flex: 1;
    }

    .node-delete {
      cursor: pointer;
      opacity: 0.7;

      &:hover {
        opacity: 1;
      }
    }
  }

  .node-body {
    padding: 12px;
    background: white;
    border-radius: 0 0 2px 2px;

    .node-desc {
      font-size: 12px;
      color: var(--text-secondary);
      line-height: 1.5;
    }
  }

  .node-anchor {
    position: absolute;
    width: 10px;
    height: 10px;
    background: white;
    border: 2px solid #52c41a;
    border-radius: 50%;
    z-index: 10;
    transition: all 0.2s;

    &.node-anchor-left {
      left: -6px;
      top: 50%;
      transform: translateY(-50%);
      cursor: default;
      
      &:hover {
        transform: translateY(-50%) scale(1.5);
        background: #52c41a;
        box-shadow: 0 0 8px rgba(82, 196, 26, 0.6);
      }
    }

    &.node-anchor-right {
      right: -6px;
      top: 50%;
      transform: translateY(-50%);
      cursor: crosshair;

      &:hover {
        transform: translateY(-50%) scale(1.5);
        background: #52c41a;
        box-shadow: 0 0 8px rgba(82, 196, 26, 0.6);
      }
    }
  }
}

.connections-layer {
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
  z-index: 1;
  will-change: contents;

  .connection-hit-area {
    pointer-events: stroke;
    cursor: pointer;
    stroke-linecap: round;
  }

  .connection-line {
    pointer-events: none;
    transition: all 0.2s ease;
    stroke-linecap: round;

    &.is-hover {
      stroke: #73d13d !important;
      stroke-width: 3;
      filter: drop-shadow(0 0 4px rgba(82, 196, 26, 0.6));
    }

    &.temp-connection {
      opacity: 0.7;
      pointer-events: none;
      transition: none;
      stroke-dasharray: 8,4;
      animation: dash 0.5s linear infinite;
    }
  }
  
  .connection-delete-btn {
    pointer-events: auto;
    cursor: pointer;
    transition: all 0.2s ease;
    
    &:hover {
      r: 14;
      stroke-width: 3;
      filter: drop-shadow(0 2px 8px rgba(239, 68, 68, 0.4));
    }
  }
  
  .connection-delete-icon {
    cursor: pointer;
    pointer-events: auto;
    user-select: none;
    
    &:hover {
      font-size: 14px;
    }
  }
}

@keyframes dash {
  to {
    stroke-dashoffset: -12;
  }
}

.node-config {
  :deep(.el-form-item) {
    margin-bottom: 16px;
  }
}
</style>
