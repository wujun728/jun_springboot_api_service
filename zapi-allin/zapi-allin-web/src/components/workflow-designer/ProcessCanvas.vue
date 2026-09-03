<template>
  <div class="process-canvas" ref="canvasContainer">
    <div class="canvas-content" @drop="handleDrop" @dragover="handleDragOver">
      <!-- 空状态提示 -->
      <div v-if="currentProcess.nodes.length === 0" class="empty-canvas">
        <div class="empty-content">
          <el-icon class="empty-icon"><VideoPlay /></el-icon>
          <h3>开始设计您的流程</h3>
          <p>从左侧拖拽节点到画布开始设计</p>
          <div class="quick-actions">
            <el-button type="primary" @click="addQuickStartNode">
              <el-icon><VideoPlay /></el-icon>
              添加开始节点
            </el-button>
          </div>
        </div>
      </div>

      <!-- 连接模式提示 - 仅在连接时显示 -->
      <div v-if="connectionState.isConnecting" class="connection-hint">
        <el-icon class="hint-icon"><Connection /></el-icon>
        <div class="hint-info">
          <span class="hint-text">
            {{ getNodeName(connectionState.startNode) }} →
            拖动到目标节点的<strong>输入点</strong>松开
          </span>
          <span class="hint-action">ESC 取消</span>
        </div>
      </div>

      <!-- 流程节点 -->
      <div
        v-for="node in currentProcess.nodes"
        :key="node.id"
        class="process-node"
        :class="[
          `node-${node.type.toLowerCase()}`,
          {
            selected: selectedNodeId === node.id,
            dragging: dragState.isDragging && dragState.dragNode?.id === node.id
          }
        ]"
        :style="{
          left: node.position.x + 'px',
          top: node.position.y + 'px'
        }"
        @click="handleNodeClick(node.id, $event)"
        @mousedown="startDrag($event, node)"
      >
        <div class="node-header">
          <el-icon class="node-icon">
            <component :is="getNodeIcon(node.type)" />
          </el-icon>
          <span class="node-title">{{ node.name }}</span>
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
          <div class="node-info">
            <div v-if="node.type === 'APPROVAL'" class="node-details">
              <div class="detail-item">
                <span class="label">处理人:</span>
                <span class="value">{{ getApprovalDisplayName(node as ApprovalNode) }}</span>
              </div>
            </div>
            <div v-else-if="node.type === 'CC'" class="node-details">
              <div class="detail-item">
                <span class="label">抄送人:</span>
                <span class="value">{{ getCcDisplayName(node as CcNode) }}</span>
              </div>
            </div>
            <div v-else-if="node.type === 'CONDITIONAL_BRANCH'" class="node-details">
              <div class="detail-item">
                <span class="label">分支数:</span>
                <span class="value">{{ getOutgoingFlowCount(node.id) }}</span>
              </div>
            </div>
            <div v-else-if="node.type === 'PARALLEL'" class="node-details">
              <div class="detail-item">
                <span class="label">分支数:</span>
                <span class="value">{{ (node as ParallelNode).branches?.length || 0 }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 连接点 -->
        <div class="connection-points">
          <!-- 输入点（START节点不显示） - 只能接收连线 -->
          <div
            v-if="node.type !== 'START'"
            class="input-point"
            :class="{
              'can-connect': connectionState.isConnecting && connectionState.startNode !== node.id
            }"
            title="输入点"
          >
            <div class="point-label">入</div>
            <div class="point-glow"></div>
          </div>

          <!-- 输出点（END节点不显示） - 可以拖动创建连线 -->
          <div
            v-if="node.type !== 'END'"
            class="output-point"
            :class="{
              active: connectionState.isConnecting && connectionState.startNode === node.id
            }"
            @mousedown.stop="handleAnchorMouseDown($event, node)"
            title="输出点"
          >
            <div class="point-label">出</div>
            <div class="point-glow"></div>
          </div>
        </div>
      </div>

        <!-- 连接线 -->
      <svg
        class="flows-svg"
        :width="canvasSize.width"
        :height="canvasSize.height"
        @mousemove="onCanvasMouseMove"
        @click="onCanvasClick"
      >
        <defs>
          <!-- 箭头标记 - 已更新颜色 v2 -->
          <marker id="process-canvas-arrowhead" markerWidth="7" markerHeight="6" refX="1" refY="3" orient="auto">
            <polygon points="0 0, 7 3, 0 6" fill="#6b7280" stroke="#6b7280" stroke-width="0.5" />
          </marker>
          <!-- 中间状态的箭头（用于动画过渡） -->
          <marker id="process-canvas-arrowhead-transition" markerWidth="7.05" markerHeight="6.01" refX="2" refY="3.005" orient="auto">
            <polygon points="0 0, 7.05 3.005, 0 6.01" fill="#6366f1" stroke="#6366f1" stroke-width="0.5" />
          </marker>
          <marker id="process-canvas-arrowhead-primary" markerWidth="7.1" markerHeight="6.02" refX="3" refY="3.01" orient="auto">
            <polygon points="0 0, 7.1 3.01, 0 6.02" fill="#6366f1" stroke="#6366f1" stroke-width="0.5" />
          </marker>
          <marker id="process-canvas-arrowhead-preview" markerWidth="7" markerHeight="6" refX="1" refY="3" orient="auto">
            <polygon points="0 0, 7 3, 0 6" fill="#10b981" stroke="#10b981" stroke-width="0.5" />
          </marker>
        </defs>

        <!-- 已存在的连接线 -->
        <g v-for="flow in sortedFlows" :key="flow.id">
          <!-- 透明的宽点击区域 - 让连接线更容易选中 -->
          <path
            :d="flowPaths[flow.id] || ''"
            stroke="transparent"
            stroke-width="20"
            fill="none"
            class="flow-hitarea"
            @click="selectFlow(flow.id)"
            @contextmenu.prevent="confirmDeleteFlow(flow.id)"
            @mouseenter="hoverFlowId = flow.id"
            @mouseleave="hoverFlowId = ''"
          />
          <!-- 实际显示的连接线 -->
          <path
            :d="flowPaths[flow.id] || ''"
            :key="`path-${flow.id}-${flowPaths._updateKey || 0}`"
            :stroke="getFlowStroke(flow)"
            :stroke-width="getFlowStrokeWidth(flow)"
            fill="none"
            :marker-end="getFlowMarker(flow)"
            :data-flow-id="flow.id"
            class="flow-line"
            :class="{
              selected: selectedFlowId === flow.id,
              'just-created': connectionState.justCreated === flow.id
            }"
            style="pointer-events: none;"
          />
          <!-- 连接条件标签 -->
          <text
            v-if="flow.condition"
            :x="flowMidpoints[flow.id]?.x || 0"
            :y="(flowMidpoints[flow.id]?.y || 0) - 10"
            class="flow-condition"
            text-anchor="middle"
            @click="selectFlow(flow.id)"
          >
            {{ flow.condition }}
          </text>
          <!-- 删除按钮（仅在选中时显示） -->
          <circle
            v-if="selectedFlowId === flow.id"
            :cx="flowMidpoints[flow.id]?.x || 0"
            :cy="flowMidpoints[flow.id]?.y || 0"
            r="12"
            fill="white"
            stroke="#ef4444"
            stroke-width="2"
            class="flow-delete-btn"
            @click.stop="confirmDeleteFlow(flow.id)"
          />
          <text
            v-if="selectedFlowId === flow.id"
            :x="flowMidpoints[flow.id]?.x || 0"
            :y="(flowMidpoints[flow.id]?.y || 0) + 4"
            text-anchor="middle"
            fill="#ef4444"
            font-size="12"
            font-weight="bold"
            class="flow-delete-icon"
            @click.stop="confirmDeleteFlow(flow.id)"
          >
            ×
          </text>
        </g>

        <!-- 预览连接线（拖拽时显示） -->
        <path
          v-if="connectionState.isConnecting && connectionState.previewPath"
          :d="connectionState.previewPath"
          stroke="#10b981"
          stroke-width="3"
          stroke-dasharray="5,5"
          fill="none"
          marker-end="url(#process-canvas-arrowhead-preview)"
          class="flow-preview"
        >
          <animate attributeName="stroke-dashoffset" values="0;10" dur="0.5s" repeatCount="indefinite" />
        </path>
      </svg>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, onUnmounted, computed, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Delete, VideoPlay, User, Document, Check, Connection, Close, InfoFilled, Right,
  Bell, Share
} from '@element-plus/icons-vue'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'
import type { ProcessDefinition, ProcessNode, ProcessFlow, ApprovalNode, CcNode, ParallelNode } from '@/stores/workflow/processDesigner'

// 用户相关类型定义
interface User {
  id: number
  username: string
  realName?: string
  email?: string
}

// Props
interface Props {
  currentProcess: ProcessDefinition
  selectedNodeId: string
  selectedFlowId: string
}

const props = defineProps<Props>()

// Emits
const emit = defineEmits<{
  'node-added': [node: ProcessNode]
  'node-deleted': [nodeId: string]
  'node-selected': [nodeId: string]
  'flow-added': [flow: ProcessFlow]
  'flow-deleted': [flowId: string]
  'flow-selected': [flowId: string]
  'node-updated': [nodeId: string, updates: Partial<ProcessNode>]
}>()

// 响应式数据
const canvasContainer = ref<HTMLElement>()
const userMap = ref<Map<number | string, User>>(new Map()) // 用户ID到用户信息的映射（支持数字和字符串类型）
// 使用 ref 来确保响应式追踪
const dragState = reactive({
  isDragging: false,
  dragNode: null as ProcessNode | null,
  startPos: { x: 0, y: 0 },
  offset: { x: 0, y: 0 },
  tempPosition: { x: 0, y: 0 }, // 拖拽时的临时位置
  updateCounter: 0 // 用于触发连接线实时更新的计数器
})

// 使用独立的 ref 来强制触发更新
const tempPositionX = ref(0)
const tempPositionY = ref(0)
const connectionState = reactive({
  isConnecting: false,
  startNode: '',
  startType: '' as 'input' | 'output',
  previewPath: '',
  mousePosition: { x: 0, y: 0 },
  justCreated: '' // 刚创建的连接ID，用于显示成功动画
})
const canvasSize = reactive({ width: 2000, height: 2000 })
const hoverFlowId = ref<string>('') // 当前鼠标悬停的连接线ID

// 计算属性
const currentProcess = computed(() => props.currentProcess)
const selectedNodeId = computed(() => props.selectedNodeId)
const selectedFlowId = computed(() => props.selectedFlowId)

// 排序连线：选中的排在后面，确保箭头能覆盖普通箭头
const sortedFlows = computed(() => {
  const flows = [...currentProcess.value.flows]
  return flows.sort((a, b) => {
    const aSelected = selectedFlowId.value === a.id
    const bSelected = selectedFlowId.value === b.id
    if (aSelected && !bSelected) return 1 // 选中的排在后面
    if (!aSelected && bSelected) return -1 // 未选中的排在前面
    return 0 // 保持原有顺序
  })
})

// 拖拽更新触发器 - 用于触发连接线实时更新
const dragUpdateTrigger = computed(() => {
  // 当拖拽时，读取 tempPosition 和 updateCounter 来触发更新
  if (dragState.isDragging && dragState.dragNode) {
    // 同时读取 ref 和 reactive 的值，确保响应式追踪
    const tempPos = dragState.tempPosition
    const x = tempPositionX.value // 读取 ref 值
    const y = tempPositionY.value // 读取 ref 值
    return {
      isDragging: true,
      nodeId: dragState.dragNode.id,
      x: x || tempPos.x, // 优先使用 ref 值
      y: y || tempPos.y, // 优先使用 ref 值
      counter: dragState.updateCounter
    }
  }
  return { isDragging: false, nodeId: '', x: 0, y: 0, counter: 0 }
})

// 为所有连接线计算路径的计算属性（确保响应式更新）
const flowPaths = computed(() => {
  // 读取拖拽触发器，确保在拖拽时重新计算
  const trigger = dragUpdateTrigger.value

  // 直接读取 tempPosition（reactive）和 ref，确保响应式追踪
  const _tempX = dragState.tempPosition.x
  const _tempY = dragState.tempPosition.y
  const _refX = tempPositionX.value
  const _refY = tempPositionY.value
  const _counter = dragState.updateCounter

  // 为每个连接线计算路径，使用对象而不是 Map，确保 Vue 能正确追踪
  // 每次都创建全新的对象，确保 Vue 检测到变化
  const paths: Record<string, string> = Object.create(null) // 使用 Object.create(null) 创建纯净对象
  currentProcess.value.flows.forEach(flow => {
    const path = computeFlowPath(flow, trigger)
    paths[flow.id] = path
  })

  // 返回新对象，并添加一个时间戳确保 Vue 检测到变化
  return Object.assign({}, paths, { _updateKey: _counter })
})

// 为所有连接线计算中点的计算属性
const flowMidpoints = computed(() => {
  // 读取拖拽触发器，确保在拖拽时重新计算
  const trigger = dragUpdateTrigger.value

  // 直接读取 tempPosition，确保响应式追踪
  const _tempX = dragState.tempPosition.x
  const _tempY = dragState.tempPosition.y
  const _counter = dragState.updateCounter

  // 为每个连接线计算中点，使用对象而不是 Map，确保 Vue 能正确追踪
  const midpoints: Record<string, { x: number; y: number }> = {}
  currentProcess.value.flows.forEach(flow => {
    midpoints[flow.id] = computeFlowMidpoint(flow, trigger)
  })
  return midpoints
})

// 计算单个连接线路径的函数
const computeFlowPath = (flow: ProcessFlow, trigger: any) => {
  // 直接读取 dragState 的属性，确保能追踪到变化
  const isDragging = dragState.isDragging
  const draggingNodeId = dragState.dragNode?.id
  // 优先使用 ref 值，如果没有则使用 reactive 值
  const tempPosX = tempPositionX.value || dragState.tempPosition.x
  const tempPosY = tempPositionY.value || dragState.tempPosition.y

  // 获取起始节点位置
  const fromNode = currentProcess.value.nodes.find(n => n.id === flow.from)
  const toNode = currentProcess.value.nodes.find(n => n.id === flow.to)

  if (!fromNode || !toNode) return ''

  // 检查是否正在拖拽这些节点
  const isDraggingFrom = isDragging && draggingNodeId === flow.from
  const isDraggingTo = isDragging && draggingNodeId === flow.to

  // 获取起始位置（如果正在拖拽该节点，使用临时位置）
  const fromPosition = isDraggingFrom
    ? { x: tempPosX, y: tempPosY }
    : fromNode.position

  // 获取目标位置（如果正在拖拽该节点，使用临时位置）
  const toPosition = isDraggingTo
    ? { x: tempPosX, y: tempPosY }
    : toNode.position

  // 计算连接点位置
  const fromCenterY = fromPosition.y + CANVAS_CONSTANTS.NODE_HEIGHT / 2
  const toCenterY = toPosition.y + CANVAS_CONSTANTS.NODE_HEIGHT / 2

  const fromPos = {
    x: fromPosition.x + CANVAS_CONSTANTS.NODE_WIDTH,
    y: fromCenterY
  }

  const toPos = {
    x: toPosition.x + CANVAS_CONSTANTS.INPUT_POINT_OFFSET, // 连线尾部往后移
    y: toCenterY
  }

  // 使用统一的路径创建函数
  return createConnectionPath(fromPos, toPos, PATH_CONSTANTS.FLOW_CONTROL_DISTANCE_RATIO)
}

// 方法
const getVar = (cssVar: string, fallback: string) => {
  if (typeof window !== 'undefined') {
    const value = getComputedStyle(document.documentElement).getPropertyValue(cssVar)
    return value || fallback
  }
  return fallback
}

// 节点图标映射类型
type NodeIconComponent = typeof VideoPlay | typeof Check | typeof User | typeof Connection | typeof Bell | typeof Share | typeof Document

const getNodeIcon = (type: string): NodeIconComponent => {
  const iconMap: Record<string, NodeIconComponent> = {
    START: VideoPlay,
    END: Check,
    APPROVAL: User,
    CONDITIONAL_BRANCH: Connection,
    CC: Bell,
    PARALLEL: Share
  }
  return iconMap[type] || Document
}

// 缓存节点输出连接数，避免重复计算
const outgoingFlowCountCache = computed(() => {
  const cache = new Map<string, number>()
  currentProcess.value.flows.forEach(flow => {
    cache.set(flow.from, (cache.get(flow.from) || 0) + 1)
  })
  return cache
})

const getOutgoingFlowCount = (nodeId: string) => {
  return outgoingFlowCountCache.value.get(nodeId) || 0
}

// 加载用户数据
const userStore = useUserStore()

const loadUsers = async () => {
  try {
    // 使用缓存的用户数据
    const users = await userStore.loadEnabledUsers()

    // 构建用户ID到用户信息的映射
    // 同时存储数字和字符串类型的key，以兼容不同的数据格式
    const newUserMap = new Map<number | string, User>()
    users.forEach((user: User) => {
      newUserMap.set(user.id, user)
      // 同时也存储字符串类型的key
      newUserMap.set(user.id.toString(), user)
    })
    userMap.value = newUserMap
  } catch (error) {
    console.error('[ProcessCanvas] 加载用户数据失败:', error)
    ElMessage.error('加载用户数据失败，部分功能可能无法正常使用')
  }
}

// 获取处理人显示名称
const getAssigneeDisplayName = (assignee: string | number | undefined): string => {
  if (!assignee) {
    return '未设置'
  }

  // 先尝试直接用原值查询（可能是字符串或数字）
  let user = userMap.value.get(assignee as number | string)

  // 如果直接查询失败，尝试转换类型后查询
  if (!user) {
    const userId = typeof assignee === 'string' ? parseInt(assignee, 10) : assignee
    if (!isNaN(userId as number)) {
      user = userMap.value.get(userId)
    }
  }

  if (user) {
    return user.realName || user.username
  }

  // 如果找不到用户，返回原值
  return assignee.toString()
}

// 获取审批节点显示名称（支持多人）
const getApprovalDisplayName = (node: ApprovalNode): string => {
  // 优先使用 approvalTargets（多人审批）
  if (node.approvalTargets && node.approvalTargets.length > 0) {
    const names = node.approvalTargets.map(target => {
      if (target.name) return target.name
      if (target.type === 'user') return getAssigneeDisplayName(target.id)
      if (target.type === 'dept') return `部门${target.id}`
      if (target.type === 'role') return `角色${target.id}`
      return target.id?.toString() || '未知'
    })
    return names.join('、')
  }
  // 兼容旧的单人审批
  return getAssigneeDisplayName(node.assignee)
}

// 获取抄送节点显示名称（支持多人）
const getCcDisplayName = (node: CcNode): string => {
  // 优先使用 ccTargets（多人抄送）
  if (node.ccTargets && node.ccTargets.length > 0) {
    const names = node.ccTargets.map(target => {
      if (target.name) return target.name
      if (target.type === 'user') return getAssigneeDisplayName(target.id)
      if (target.type === 'dept') return `部门${target.id}`
      if (target.type === 'role') return `角色${target.id}`
      return target.id?.toString() || '未知'
    })
    return names.join('、')
  }
  // 兼容旧的单人抄送
  return getAssigneeDisplayName(node.assignee)
}

// 处理节点点击事件
const handleNodeClick = (nodeId: string, event: MouseEvent) => {
  // 如果正在拖拽，不处理点击事件（避免拖拽结束时触发连接）
  if (dragState.isDragging) {
    return
  }

  // 正常选中节点
  selectNode(nodeId)
}

const selectNode = (nodeId: string) => {
  emit('node-selected', nodeId)
}

const selectFlow = (flowId: string) => {
  emit('flow-selected', flowId)
}

const deleteNode = (nodeId: string) => {
  emit('node-deleted', nodeId)
}

const deleteFlow = (flowId: string) => {
  emit('flow-deleted', flowId)
}

const handleDrop = (event: DragEvent) => {
  event.preventDefault()

  const nodeType = event.dataTransfer?.getData('text/plain')

  if (nodeType) {
    const container = canvasContainer.value
    if (container) {
      const rect = container.getBoundingClientRect()
      
      // 计算鼠标在画布容器中的位置，并加上滚动偏移量
      const scrollLeft = container.scrollLeft
      const scrollTop = container.scrollTop
      
      const x = event.clientX - rect.left + scrollLeft
      const y = event.clientY - rect.top + scrollTop
      
      addNode(nodeType, { x, y })
    }
  }
}

const handleDragOver = (event: DragEvent) => {
  event.preventDefault()
  event.dataTransfer!.dropEffect = 'copy'
}

// 画布常量
const CANVAS_CONSTANTS = {
  MIN_X: 50,
  MIN_Y: 50,
  MAX_X: 2800,
  MAX_Y: 1800,
  NODE_WIDTH_OFFSET: 90, // 节点宽度的一半，用于居中
  NODE_HEIGHT_OFFSET: 40, // 节点高度的一半，用于居中
  NODE_WIDTH: 180,
  NODE_HEIGHT: 80,
  CONNECTION_POINT_OFFSET: 8, // 连接点偏移量
  INPUT_POINT_OFFSET: -14, // 输入点（连线尾部）向后偏移量
  DEFAULT_START_POSITION: { x: 1400, y: 900 }
} as const

const addNode = (type: string, position: { x: number; y: number }) => {
  // 检查START节点数量限制
  if (type === 'START') {
    const startNodes = currentProcess.value.nodes.filter(n => n.type === 'START')
    if (startNodes.length > 0) {
      ElMessage.warning('流程中只能有一个开始节点')
      return
    }
  }

  // 调整位置，避免节点重叠并限制在画布范围内
  const adjustedPosition = {
    x: Math.max(CANVAS_CONSTANTS.MIN_X, Math.min(position.x - CANVAS_CONSTANTS.NODE_WIDTH_OFFSET, CANVAS_CONSTANTS.MAX_X)),
    y: Math.max(CANVAS_CONSTANTS.MIN_Y, Math.min(position.y - CANVAS_CONSTANTS.NODE_HEIGHT_OFFSET, CANVAS_CONSTANTS.MAX_Y))
  }

  // 通过emit触发父组件添加节点
  emit('node-added', { type, position: adjustedPosition })
}

const startDrag = (event: MouseEvent, node: ProcessNode) => {
  if (event.button !== 0) return // 只处理左键

  event.preventDefault()
  event.stopPropagation()

  dragState.isDragging = true
  dragState.dragNode = node
  dragState.startPos = { x: event.clientX, y: event.clientY }

  // 获取节点DOM元素，用于直接操作transform和获取位置
  const nodeElement = event.currentTarget as HTMLElement

  // 获取画布内容区域（.canvas-content），节点是相对于这个区域定位的
  const canvasContent = canvasContainer.value?.querySelector('.canvas-content') as HTMLElement
  if (!canvasContent) return

  // 获取画布内容区域在视口中的位置
  const initialContentRect = canvasContent.getBoundingClientRect()

  // 获取节点在视口中的实际位置
  const nodeRect = nodeElement.getBoundingClientRect()

  // 计算节点在画布内容区域中的实际位置
  const nodeXInContent = nodeRect.left - initialContentRect.left
  const nodeYInContent = nodeRect.top - initialContentRect.top

  // 计算拖拽偏移：鼠标相对于节点的偏移量（在画布内容区域坐标系中）
  const mouseXInContent = event.clientX - initialContentRect.left
  const mouseYInContent = event.clientY - initialContentRect.top

  dragState.offset = {
    x: mouseXInContent - nodeXInContent,
    y: mouseYInContent - nodeYInContent
  }

  // 保存初始位置（使用节点的实际位置）
  const initialPosition = { x: node.position.x, y: node.position.y }
  // 初始化临时位置为节点的实际位置
  dragState.tempPosition.x = initialPosition.x
  dragState.tempPosition.y = initialPosition.y
  tempPositionX.value = initialPosition.x
  tempPositionY.value = initialPosition.y
  let rafId: number | null = null
  let pathUpdateRafId: number | null = null
  let lastUpdateTime = 0
  const throttleMs = 16 // 约60fps，限制更新频率以优化性能

  const handleMouseMove = (e: MouseEvent) => {
    if (!dragState.isDragging || !dragState.dragNode) {
      // 如果拖拽状态异常，清理事件监听
      document.removeEventListener('mousemove', handleMouseMove)
      document.removeEventListener('mouseup', handleMouseUp)
      return
    }

    // 每次移动时重新获取画布内容区域位置，确保位置计算准确
    const canvasContent = canvasContainer.value?.querySelector('.canvas-content') as HTMLElement
    if (!canvasContent) return

    const contentRect = canvasContent.getBoundingClientRect()

    const now = Date.now()
    if (now - lastUpdateTime < throttleMs) return
    lastUpdateTime = now

    // 计算新位置（相对于画布内容区域）
    // 鼠标在视口中的位置 - 内容区域在视口中的位置 = 鼠标在内容区域中的位置
    // 鼠标在内容区域中的位置 - 拖拽偏移 = 节点的新位置
    const mouseXInContent = e.clientX - contentRect.left
    const mouseYInContent = e.clientY - contentRect.top

    const newX = mouseXInContent - dragState.offset.x
    const newY = mouseYInContent - dragState.offset.y

    // 限制拖拽范围
    const clampedX = Math.max(CANVAS_CONSTANTS.MIN_X, Math.min(newX, CANVAS_CONSTANTS.MAX_X))
    const clampedY = Math.max(CANVAS_CONSTANTS.MIN_Y, Math.min(newY, CANVAS_CONSTANTS.MAX_Y))

    // 立即更新临时位置（用于连接线实时预览）
    // 同时更新 reactive 和 ref，确保响应式追踪
    dragState.tempPosition.x = clampedX
    dragState.tempPosition.y = clampedY
    tempPositionX.value = clampedX
    tempPositionY.value = clampedY
    // 递增更新计数器，触发连接线重新计算
    dragState.updateCounter++

    // 使用 requestAnimationFrame 优化节点移动性能
    if (rafId !== null) {
      cancelAnimationFrame(rafId)
    }

    rafId = requestAnimationFrame(() => {
      // 使用 CSS transform 来移动节点（不触发响应式更新）
      const deltaX = clampedX - initialPosition.x
      const deltaY = clampedY - initialPosition.y
      nodeElement.style.transform = `translate(${deltaX}px, ${deltaY}px)`
      nodeElement.style.transition = 'none'
      nodeElement.style.willChange = 'transform'
    })
  }

  const handleMouseUp = () => {
    if (rafId !== null) {
      cancelAnimationFrame(rafId)
      rafId = null
    }

    if (pathUpdateRafId !== null) {
      cancelAnimationFrame(pathUpdateRafId)
      pathUpdateRafId = null
    }

    if (dragState.isDragging && dragState.dragNode && nodeElement) {
      // 只在拖拽结束时才更新实际位置并emit事件
      const finalPosition = {
        x: dragState.tempPosition.x,
        y: dragState.tempPosition.y
      }

      // 检查位置是否真的改变了
      if (finalPosition.x !== initialPosition.x || finalPosition.y !== initialPosition.y) {
        // 先更新实际位置（这会触发响应式更新）
        dragState.dragNode.position.x = finalPosition.x
        dragState.dragNode.position.y = finalPosition.y

        // 先直接设置内联样式到最终位置，确保节点在正确位置
        nodeElement.style.left = finalPosition.x + 'px'
        nodeElement.style.top = finalPosition.y + 'px'

        // 立即清除 transform，此时节点已经通过内联样式在正确位置了
        nodeElement.style.transform = ''
        nodeElement.style.transition = ''
        nodeElement.style.willChange = ''

        // 不要移除内联样式，让它们一直存在
        // 因为内联样式的优先级高于 :style 绑定，这样可以避免抖动
        // 当节点位置再次更新时，内联样式会被新的值覆盖

        // 只在最后emit一次事件，保存历史记录
        emit('node-updated', dragState.dragNode.id, { position: finalPosition })
      } else {
        // 如果位置没有改变，直接恢复样式
        nodeElement.style.transform = ''
        nodeElement.style.transition = ''
        nodeElement.style.willChange = ''
      }
    }

    dragState.isDragging = false
    dragState.dragNode = null
    dragState.tempPosition = { x: 0, y: 0 }
    tempPositionX.value = 0
    tempPositionY.value = 0
    dragState.updateCounter = 0 // 重置更新计数器
    document.removeEventListener('mousemove', handleMouseMove)
    document.removeEventListener('mouseup', handleMouseUp)
  }

  document.addEventListener('mousemove', handleMouseMove, { passive: true })
  document.addEventListener('mouseup', handleMouseUp)
}

// 拖拽式连接处理
const handleAnchorMouseDown = (event: MouseEvent, node: ProcessNode) => {
  event.preventDefault()
  event.stopPropagation()

  connectionState.isConnecting = true
  connectionState.startNode = node.id
  connectionState.startType = 'output'
  document.body.style.cursor = 'crosshair'
  document.body.style.userSelect = 'none'

  // 立即显示初始连线
  const canvas = canvasContainer.value
  if (!canvas) return

  const rect = canvas.getBoundingClientRect()
  const scrollLeft = canvas.scrollLeft
  const scrollTop = canvas.scrollTop

  const startPos = getNodeConnectionPoint(node.id, 'output')
  const endX = (event.clientX - rect.left + scrollLeft)
  const endY = (event.clientY - rect.top + scrollTop)

  connectionState.previewPath = createConnectionPath(startPos, { x: endX, y: endY }, PATH_CONSTANTS.PREVIEW_CONTROL_DISTANCE_RATIO)

  document.addEventListener('mousemove', handleConnectionMouseMove, { passive: false })
  document.addEventListener('mouseup', handleConnectionMouseUp)
}

const handleConnectionMouseMove = (event: MouseEvent) => {
  if (!connectionState.isConnecting) return

  event.preventDefault()

  const canvas = canvasContainer.value
  if (!canvas) return

  const rect = canvas.getBoundingClientRect()
  const scrollLeft = canvas.scrollLeft
  const scrollTop = canvas.scrollTop

  // 计算起点（输出锚点位置）
  const startPos = getNodeConnectionPoint(connectionState.startNode, 'output')

  // 计算终点（鼠标位置，考虑滚动）
  const endX = (event.clientX - rect.left + scrollLeft)
  const endY = (event.clientY - rect.top + scrollTop)

  // 直接更新预览路径
  connectionState.previewPath = createConnectionPath(startPos, { x: endX, y: endY }, PATH_CONSTANTS.PREVIEW_CONTROL_DISTANCE_RATIO)
}

const handleConnectionMouseUp = (event: MouseEvent) => {
  if (!connectionState.isConnecting) return

  event.preventDefault()

  // 保存起始节点
  const fromNode = connectionState.startNode

  // 立即清除连接状态
  connectionState.isConnecting = false
  connectionState.startNode = ''
  connectionState.startType = ''
  connectionState.previewPath = ''
  document.body.style.cursor = ''
  document.body.style.userSelect = ''
  document.removeEventListener('mousemove', handleConnectionMouseMove)
  document.removeEventListener('mouseup', handleConnectionMouseUp)

  // 查找鼠标位置下的节点
  const canvas = canvasContainer.value
  if (!canvas) return

  const rect = canvas.getBoundingClientRect()
  const scrollLeft = canvas.scrollLeft
  const scrollTop = canvas.scrollTop

  const mouseX = (event.clientX - rect.left + scrollLeft)
  const mouseY = (event.clientY - rect.top + scrollTop)

  // 检查是否在某个节点的输入点范围内
  const targetNode = currentProcess.value.nodes.find(n => {
    if (n.id === fromNode) return false
    if (n.type === 'START') return false // START节点不能有输入

    // 输入锚点在节点左侧中间
    const inputPos = getNodeConnectionPoint(n.id, 'input')

    // 检查鼠标是否在输入点附近（扩大检测范围到40px）
    const distance = Math.sqrt(
      Math.pow(mouseX - inputPos.x, 2) +
      Math.pow(mouseY - inputPos.y, 2)
    )

    return distance < 40
  })

  if (targetNode) {
    // 业务逻辑验证
    const fromNodeData = currentProcess.value.nodes.find(n => n.id === fromNode)

    if (fromNodeData?.type === 'END') {
      ElMessage.warning('结束节点不能有输出连接')
      return
    }

    // 检查是否已存在连接
    const existingFlow = currentProcess.value.flows.find(
      f => f.from === fromNode && f.to === targetNode.id
    )

    if (existingFlow) {
      ElMessage.warning('连接已存在')
      return
    }

    // 创建新连接
    const flowData = {
      from: fromNode,
      to: targetNode.id,
      condition: null,
      name: ''
    }

    emit('flow-added', flowData)
    ElMessage.success('连接成功')
  }
}

const cancelConnection = () => {
  connectionState.isConnecting = false
  connectionState.startNode = ''
  connectionState.startType = ''
  connectionState.previewPath = ''
  connectionState.mousePosition = { x: 0, y: 0 }
  document.body.style.cursor = ''
  document.body.style.userSelect = ''
  document.removeEventListener('mousemove', handleConnectionMouseMove)
  document.removeEventListener('mouseup', handleConnectionMouseUp)
}

// 获取节点名称的辅助函数
const getNodeName = (nodeId: string) => {
  const node = currentProcess.value.nodes.find(n => n.id === nodeId)
  return node ? node.name : '未知节点'
}

// 获取连接线样式（合并三个函数，减少重复逻辑）
const getFlowStyle = (flow: ProcessFlow) => {
  const isJustCreated = connectionState.justCreated === flow.id
  const isSelected = selectedFlowId.value === flow.id
  const isHovered = hoverFlowId.value === flow.id

  if (isJustCreated) {
    return {
      stroke: '#10b981',
      strokeWidth: 4,
      marker: 'url(#process-canvas-arrowhead-preview)'
    }
  } else if (isSelected) {
    return {
      stroke: '#6366f1',
      strokeWidth: 3,
      marker: 'url(#process-canvas-arrowhead-primary)'
    }
  } else if (isHovered) {
    // hover 时使用后移的箭头
    return {
      stroke: '#6366f1',
      strokeWidth: 3,
      marker: 'url(#process-canvas-arrowhead-primary)'
    }
  } else {
    return {
      stroke: '#6b7280',
      strokeWidth: 2,
      marker: 'url(#process-canvas-arrowhead)'
    }
  }
}

// 保持向后兼容的独立函数
const getFlowStroke = (flow: ProcessFlow) => getFlowStyle(flow).stroke
const getFlowStrokeWidth = (flow: ProcessFlow) => getFlowStyle(flow).strokeWidth

// 获取箭头标记（带动画过渡）
const getFlowMarker = (flow: ProcessFlow) => {
  const style = getFlowStyle(flow)
  const marker = style.marker
  
  // 使用 nextTick 确保 DOM 更新后再触发动画
  nextTick(() => {
    const flowLine = document.querySelector(`[data-flow-id="${flow.id}"]`) as SVGPathElement
    if (flowLine) {
      // 添加动画类，触发 CSS 动画
      flowLine.classList.add('arrowhead-transitioning')
      // 动画结束后移除类
      setTimeout(() => {
        flowLine.classList.remove('arrowhead-transitioning')
      }, 300)
    }
  })
  
  return marker
}

// 画布点击（取消连接）
const onCanvasClick = (event: MouseEvent) => {
  if (connectionState.isConnecting) {
    // 点击空白处取消连接
    if (event.target === event.currentTarget) {
      cancelConnection()
      ElMessage.info('连接已取消')
    }
  }
}

// 画布鼠标移动（保留用于其他功能）
const onCanvasMouseMove = (event: MouseEvent) => {
  // 预留给其他功能使用
}

// 连接路径计算常量
const PATH_CONSTANTS = {
  CONTROL_DISTANCE_RATIO: 0.4, // 控制点距离比例
  MIN_CONTROL_DISTANCE: 80, // 最小控制点距离
  PREVIEW_CONTROL_DISTANCE_RATIO: 0.4, // 预览连接线控制点距离比例
  FLOW_CONTROL_DISTANCE_RATIO: 0.3 // 实际连接线控制点距离比例
} as const

// 创建连接路径
const createConnectionPath = (from: { x: number; y: number }, to: { x: number; y: number }, controlRatio: number = PATH_CONSTANTS.CONTROL_DISTANCE_RATIO) => {
  const dx = to.x - from.x

  // 智能贝塞尔曲线
  const controlDistance = Math.max(Math.abs(dx) * controlRatio, PATH_CONSTANTS.MIN_CONTROL_DISTANCE)
  const cp1x = from.x + controlDistance
  const cp1y = from.y
  const cp2x = to.x - controlDistance
  const cp2y = to.y

  return `M ${from.x} ${from.y} C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${to.x} ${to.y}`
}

// 确认删除连接
const confirmDeleteFlow = async (flowId: string) => {
  const flow = currentProcess.value.flows.find(f => f.id === flowId)
  if (!flow) return

  const fromNodeName = getNodeName(flow.from)
  const toNodeName = getNodeName(flow.to)

  try {
    await ElMessageBox.confirm(
      `确定要删除连接"${fromNodeName} → ${toNodeName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    emit('flow-deleted', flowId)
    ElMessage.success('连接已删除')
  } catch (error) {
    // 用户取消删除
  }
}

const getNodeConnectionPoint = (nodeId: string, type: 'input' | 'output') => {
  // 读取拖拽更新触发器，确保建立响应式依赖
  // 这样当拖拽时，这个函数会被重新执行
  // eslint-disable-next-line @typescript-eslint/no-unused-expressions
  dragUpdateTrigger.value

  const node = currentProcess.value.nodes.find(n => n.id === nodeId)
  if (!node) return { x: 0, y: 0 }

  // 如果正在拖拽该节点，使用临时位置
  // 直接读取响应式数据，确保 Vue 能够追踪
  const isDraggingThisNode = dragState.isDragging && dragState.dragNode?.id === nodeId
  let position = node.position

  if (isDraggingThisNode) {
    // 优先使用 ref 值，如果没有则使用 reactive 值
    position = {
      x: tempPositionX.value || dragState.tempPosition.x,
      y: tempPositionY.value || dragState.tempPosition.y
    }
  }

  const centerY = position.y + CANVAS_CONSTANTS.NODE_HEIGHT / 2 // 节点高度的一半

  if (type === 'output') {
    return {
      x: position.x + CANVAS_CONSTANTS.NODE_WIDTH, // 节点右边
      y: centerY
    }
  } else {
    return {
      x: position.x + CANVAS_CONSTANTS.INPUT_POINT_OFFSET, // 节点左边，连线尾部往后移
      y: centerY
    }
  }
}

// 注意：getFlowPath 函数已改为使用计算属性 flowPaths，见下方定义

// 计算单个连接线中点的函数
const computeFlowMidpoint = (flow: ProcessFlow, trigger: any) => {
  // 直接读取 dragState 的属性，确保能追踪到变化
  const isDragging = dragState.isDragging
  const draggingNodeId = dragState.dragNode?.id
  // 优先使用 ref 值，如果没有则使用 reactive 值
  const tempPosX = tempPositionX.value || dragState.tempPosition.x
  const tempPosY = tempPositionY.value || dragState.tempPosition.y

  // 获取起始节点位置
  const fromNode = currentProcess.value.nodes.find(n => n.id === flow.from)
  const toNode = currentProcess.value.nodes.find(n => n.id === flow.to)

  if (!fromNode || !toNode) return { x: 0, y: 0 }

  // 检查是否正在拖拽这些节点
  const isDraggingFrom = isDragging && draggingNodeId === flow.from
  const isDraggingTo = isDragging && draggingNodeId === flow.to

  // 获取位置（如果正在拖拽该节点，使用临时位置）
  const fromPosition = isDraggingFrom
    ? { x: tempPosX, y: tempPosY }
    : fromNode.position

  const toPosition = isDraggingTo
    ? { x: tempPosX, y: tempPosY }
    : toNode.position

  // 计算连接点位置
  const fromCenterY = fromPosition.y + CANVAS_CONSTANTS.NODE_HEIGHT / 2
  const toCenterY = toPosition.y + CANVAS_CONSTANTS.NODE_HEIGHT / 2

  const fromPos = {
    x: fromPosition.x + CANVAS_CONSTANTS.NODE_WIDTH,
    y: fromCenterY
  }

  const toPos = {
    x: toPosition.x + CANVAS_CONSTANTS.INPUT_POINT_OFFSET, // 连线尾部往后移
    y: toCenterY
  }

  return {
    x: (fromPos.x + toPos.x) / 2,
    y: (fromPos.y + toPos.y) / 2
  }
}

// 保持向后兼容的函数（用于其他地方可能还在使用）
const getFlowPath = (flow: ProcessFlow) => {
  return flowPaths.value[flow.id] || ''
}

const getFlowMidpoint = (flow: ProcessFlow) => {
  return flowMidpoints.value[flow.id] || { x: 0, y: 0 }
}

// 快速添加开始节点
const addQuickStartNode = () => {
  emit('node-added', { type: 'START', position: CANVAS_CONSTANTS.DEFAULT_START_POSITION })
}

// 键盘事件处理
const handleKeyDown = (event: KeyboardEvent) => {
  if (event.key === 'Escape' && connectionState.isConnecting) {
    cancelConnection()
    ElMessage.info('连接已取消')
  }
}

// 生命周期
onMounted(() => {
  // 清理状态，确保普通箭头能正常显示
  connectionState.justCreated = ''
  hoverFlowId.value = ''

  // 初始化画布
  if (canvasContainer.value) {
    const rect = canvasContainer.value.getBoundingClientRect()
    canvasSize.width = Math.max(rect.width, 2000)
    canvasSize.height = Math.max(rect.height, 2000)

    // 将滚动条居中显示
    // 画布内容大小是 3000x2000，将滚动条设置到中心位置
    nextTick(() => {
      if (canvasContainer.value) {
        const canvasContent = canvasContainer.value.querySelector('.canvas-content') as HTMLElement
        if (canvasContent) {
          // 计算居中位置
          // 内容宽度 3000，可视区域宽度 rect.width，居中位置 = (3000 - rect.width) / 2
          // 内容高度 2000，可视区域高度 rect.height，居中位置 = (2000 - rect.height) / 2
          const scrollLeft = (3000 - rect.width) / 2
          const scrollTop = (2000 - rect.height) / 2

          canvasContainer.value.scrollLeft = scrollLeft
          canvasContainer.value.scrollTop = scrollTop
        }
      }
    })
  }

  // 添加键盘事件监听
  document.addEventListener('keydown', handleKeyDown)

  // 加载用户数据
  loadUsers()
})

onUnmounted(() => {
  // 清理键盘事件监听
  document.removeEventListener('keydown', handleKeyDown)
})

// 监听流程变化，清理状态确保箭头正常显示
watch(() => currentProcess.value.flows, () => {
  // 当流程变化时，清理 justCreated 状态，避免影响普通箭头显示
  connectionState.justCreated = ''
}, { deep: true })

// 暴露方法给父组件
defineExpose({
  addNode,
  addQuickStartNode
})
</script>

<style scoped lang="scss">
.process-canvas {
  width: 100%;
  height: 100%;
  position: relative;
  overflow: auto;
  background: #ffffff;
}

.canvas-content {
  width: 3000px;
  height: 2000px;
  min-width: 3000px;
  min-height: 2000px;
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

// 连接模式提示 - 紧凑设计
.connection-hint {
  position: fixed;
  top: 20px;
  right: 380px;
  z-index: 999;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(99, 102, 241, 0.95);
  color: white;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.3);
  backdrop-filter: blur(4px);
  font-size: 11px;
  animation: slideInRight 0.2s ease;

  .hint-icon {
    font-size: 14px;
    animation: rotate360 2s linear infinite;
    flex-shrink: 0;
  }

  .hint-info {
    display: flex;
    flex-direction: column;
    gap: 2px;

    .hint-text {
      font-size: 11px;
      line-height: 1.3;

      strong {
        font-weight: 700;
        color: #fbbf24;
        padding: 0 2px;
      }
    }

    .hint-action {
      font-size: 10px;
      opacity: 0.8;
    }
  }
}

@keyframes slideInRight {
  from {
    transform: translateX(20px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes rotate360 {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.process-node {
  position: absolute;
  width: 180px;
  min-height: 80px;
  background: var(--bg-card, #ffffff);
  border: 2px solid var(--border-color, #e5e7eb);
  border-radius: 8px;
  box-shadow: var(--shadow-md, 0 4px 6px -1px rgba(0, 0, 0, 0.1));
  cursor: move;
  z-index: 10; // 确保节点在SVG连接线之上
  transition: all 0.3s ease;
  // 优化拖拽性能 - 移除 paint 以允许连接点显示在节点外部
  contain: layout style;
  overflow: visible; // 确保连接点可以显示在节点外部
  backface-visibility: hidden;
  transform: translateZ(0); // 启用硬件加速

  &:hover {
    box-shadow: var(--shadow-lg, 0 10px 15px -3px rgba(0, 0, 0, 0.1));
    border-color: var(--primary-color, #6366f1);
  }

  &.selected {
    border-color: var(--primary-color, #6366f1);
    box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
  }

  // 拖拽时禁用过渡动画，提升性能
  &.dragging {
    transition: none !important;
    will-change: transform;
    z-index: 20; // 拖拽时提升层级
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

.process-node:hover .delete-btn {
  opacity: 1;
}

.node-content {
  padding: 8px 12px;
}

.node-details {
  .detail-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 4px;

    .label {
      font-size: 11px;
      color: var(--text-muted, #9ca3af);
    }

    .value {
      font-size: 11px;
      color: var(--text-secondary, #6b7280);
      font-weight: 500;
    }
  }
}

.connection-points {
  .input-point,
  .output-point {
    position: absolute;
    width: 16px;
    height: 16px;
    background: var(--primary-color, #6366f1);
    border: 2px solid white;
    border-radius: 50%;
    cursor: pointer;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: 10;
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transform: scale(0.8);

    .point-label {
      font-size: 7px;
      color: white;
      font-weight: bold;
      opacity: 0;
      transition: opacity 0.2s ease;
    }

    .point-glow {
      position: absolute;
      width: 24px;
      height: 24px;
      border-radius: 50%;
      background: radial-gradient(circle, rgba(99, 102, 241, 0.3) 0%, transparent 70%);
      opacity: 0;
      transition: opacity 0.3s ease;
      z-index: -1;
    }
  }

  .input-point {
    left: -12px; // 连接点宽度16px + 边框2px = 18px，偏移12px确保完全显示
    top: 50%;
    transform: translateY(-50%) scale(0.8);
  }

  .output-point {
    right: -12px; // 连接点宽度16px + 边框2px = 18px，偏移12px确保完全显示
    top: 50%;
    transform: translateY(-50%) scale(0.8);
    cursor: crosshair;

    &:hover {
      transform: translateY(-50%) scale(1.2);
      background: var(--success-color, #10b981);
      box-shadow: 0 0 16px rgba(16, 185, 129, 0.6);

      .point-glow {
        opacity: 1;
        background: radial-gradient(circle, rgba(16, 185, 129, 0.4) 0%, transparent 70%);
      }
    }
  }

  // 连接状态样式
  .input-point.can-connect,
  .output-point.can-connect {
    opacity: 1;
    transform: translateY(-50%) scale(1.2);
    background: var(--success-color, #10b981);
    box-shadow: 0 0 16px rgba(16, 185, 129, 0.6);
    animation: gentle-pulse 2s infinite;

    .point-glow {
      opacity: 1;
      background: radial-gradient(circle, rgba(16, 185, 129, 0.4) 0%, transparent 70%);
    }

    .point-label {
      opacity: 1;
    }
  }

  .input-point.active,
  .output-point.active {
    opacity: 1;
    transform: translateY(-50%) scale(1.3);
    background: var(--warning-color, #f59e0b);
    box-shadow: 0 0 20px rgba(245, 158, 11, 0.8);

    .point-glow {
      opacity: 1;
      background: radial-gradient(circle, rgba(245, 158, 11, 0.5) 0%, transparent 70%);
    }

    .point-label {
      opacity: 1;
    }
  }

  .input-point.disabled,
  .output-point.disabled {
    opacity: 0.3;
    background: var(--text-muted, #9ca3af);
    cursor: not-allowed;

    &:hover {
      transform: translateY(-50%) scale(0.8);
    }
  }

  .input-point.connecting,
  .output-point.connecting {
    opacity: 1;
    transform: translateY(-50%) scale(1.1);
    background: var(--info-color, #3b82f6);

    .point-glow {
      opacity: 1;
      background: radial-gradient(circle, rgba(59, 130, 246, 0.4) 0%, transparent 70%);
    }
  }
}

// 节点悬停时显示连接点
.process-node:hover .connection-points {
  .input-point:not(.disabled),
  .output-point:not(.disabled) {
    opacity: 1;
    transform: translateY(-50%) scale(1);

    .point-label {
      opacity: 1;
    }

    &:hover {
      transform: translateY(-50%) scale(1.2);

      .point-glow {
        opacity: 1;
      }
    }
  }
}

// 连接模式下始终显示所有连接点
.process-canvas:has(.connection-hint) .connection-points {
  .input-point,
  .output-point {
    opacity: 1;
    transform: translateY(-50%) scale(1);

    .point-label {
      opacity: 1;
    }
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
}

@keyframes gentle-pulse {
  0%, 100% {
    transform: translateY(-50%) scale(1.2);
    box-shadow: 0 0 16px rgba(16, 185, 129, 0.6);
  }
  50% {
    transform: translateY(-50%) scale(1.3);
    box-shadow: 0 0 24px rgba(16, 185, 129, 0.8);
  }
}

@keyframes flow-created {
  0% {
    stroke-width: 6;
    filter: drop-shadow(0 2px 20px rgba(16, 185, 129, 0.8));
  }
  50% {
    stroke-width: 5;
    filter: drop-shadow(0 2px 18px rgba(16, 185, 129, 0.7));
  }
  100% {
    stroke-width: 3;
    filter: drop-shadow(0 2px 12px rgba(99, 102, 241, 0.5));
  }
}

// 箭头过渡动画 - 平滑的放大和后移效果
@keyframes arrowhead-transition {
  0% {
    opacity: 0.95;
    filter: drop-shadow(0 0 2px rgba(99, 102, 241, 0.2));
  }
  50% {
    opacity: 1;
    filter: drop-shadow(0 1px 6px rgba(99, 102, 241, 0.4));
  }
  100% {
    opacity: 1;
    filter: drop-shadow(0 2px 10px rgba(99, 102, 241, 0.5));
  }
}

.flows-svg {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1;
  // 优化SVG渲染性能
  will-change: contents;
  contain: layout style paint;

  // 透明点击区域 - 让连接线更容易选中（20px宽的点击区）
  .flow-hitarea {
    pointer-events: stroke;
    cursor: pointer;
    stroke-linecap: round;

    &:hover + .flow-line:not(.selected):not(.just-created) {
      stroke: var(--primary-color, #6366f1) !important;
      stroke-width: 3 !important;
      filter: drop-shadow(0 2px 8px rgba(99, 102, 241, 0.3));
    }
  }

  .flow-line {
    pointer-events: none; // 点击事件由hitarea处理
    transition: stroke 0.3s cubic-bezier(0.4, 0, 0.2, 1),
                stroke-width 0.3s cubic-bezier(0.4, 0, 0.2, 1),
                filter 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    stroke-linecap: round;

    // 箭头过渡动画类
    &.arrowhead-transitioning {
      animation: arrowhead-transition 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }

    &.selected {
      stroke-width: 3 !important;
      filter: drop-shadow(0 2px 12px rgba(99, 102, 241, 0.5));
    }

    &.just-created {
      filter: drop-shadow(0 2px 16px rgba(16, 185, 129, 0.6));
      animation: flow-created 2s ease-out forwards;
    }
  }

  // hover 时也添加箭头动画
  .flow-hitarea:hover + .flow-line:not(.selected):not(.just-created) {
    animation: arrowhead-transition 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  }

  .flow-preview {
    pointer-events: none;
    filter: drop-shadow(0 2px 8px rgba(16, 185, 129, 0.4));
  }

  .flow-condition {
    pointer-events: auto; // 条件标签可以点击选中
    cursor: pointer;
    font-size: 11px;
    fill: var(--primary-color, #6366f1);
    font-weight: 500;
    text-shadow: 0 0 3px white, 0 0 6px white; // 让文字更清晰

    &:hover {
      fill: var(--primary-hover, #4f46e5);
      font-weight: 600;
    }
  }

  .flow-delete-btn {
    pointer-events: auto;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
      r: 14;
      stroke-width: 3;
      filter: drop-shadow(0 2px 8px rgba(239, 68, 68, 0.4));
    }
  }

  .flow-delete-icon {
    cursor: pointer;
    pointer-events: auto;
    user-select: none;

    &:hover {
      font-size: 14px;
    }
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

.node-approval .node-header {
  background: var(--gradient-primary, linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%));
  color: white;

  .node-icon {
    color: white;
  }
}

.node-conditional_branch .node-header {
  background: linear-gradient(135deg, var(--warning-color, #f59e0b), #fbbf24);
  color: white;

  .node-icon {
    color: white;
  }
}

.node-cc .node-header {
  background: linear-gradient(135deg, var(--info-color, #3b82f6), #60a5fa);
  color: white;

  .node-icon {
    color: white;
  }
}

.node-parallel .node-header {
  background: linear-gradient(135deg, #8b5cf6, #a78bfa);
  color: white;

  .node-icon {
    color: white;
  }
}
</style>
