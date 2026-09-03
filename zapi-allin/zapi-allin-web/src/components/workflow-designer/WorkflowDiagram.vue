<template>
  <div class="workflow-diagram" ref="diagramContainer">
    <div v-if="!process || process.nodes.length === 0" class="empty-diagram">
      <el-empty description="暂无流程图数据" />
    </div>
    <svg 
      v-else
      :width="svgWidth" 
      :height="svgHeight" 
      class="diagram-svg"
      :viewBox="`0 0 ${svgWidth} ${svgHeight}`"
    >
      <!-- 定义渐变和标记 -->
      <defs>
        <!-- 渐变定义 -->
        <linearGradient id="startGradient" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" style="stop-color:#34d399;stop-opacity:1" />
          <stop offset="100%" style="stop-color:#10b981;stop-opacity:1" />
        </linearGradient>
        
        <linearGradient id="endGradient" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" style="stop-color:#f87171;stop-opacity:1" />
          <stop offset="100%" style="stop-color:#ef4444;stop-opacity:1" />
        </linearGradient>
        
        <linearGradient id="approvalGradient" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" style="stop-color:#818cf8;stop-opacity:1" />
          <stop offset="100%" style="stop-color:#6366f1;stop-opacity:1" />
        </linearGradient>
        
        <linearGradient id="branchGradient" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" style="stop-color:#fbbf24;stop-opacity:1" />
          <stop offset="100%" style="stop-color:#f59e0b;stop-opacity:1" />
        </linearGradient>
        
        <linearGradient id="defaultGradient" x1="0%" y1="0%" x2="100%" y2="100%">
          <stop offset="0%" style="stop-color:#9ca3af;stop-opacity:1" />
          <stop offset="100%" style="stop-color:#6b7280;stop-opacity:1" />
        </linearGradient>

        <!-- 阴影滤镜 -->
        <filter id="nodeShadow" x="-20%" y="-20%" width="140%" height="140%">
          <feDropShadow dx="0" dy="4" stdDeviation="4" flood-color="rgba(0,0,0,0.15)" />
        </filter>

        <!-- 箭头标记 -->
        <marker
          id="arrowhead"
          markerWidth="12"
          markerHeight="8"
          refX="11"
          refY="4"
          orient="auto"
          markerUnits="strokeWidth"
        >
          <path
            d="M0,0 L0,8 L12,4 z"
            fill="#6366f1"
            opacity="0.8"
          />
        </marker>
      </defs>

      <!-- 绘制连接线 -->
      <g class="flows-group">
        <g v-for="flow in process.flows" :key="flow.id" class="flow-line">
          <path
            :d="getFlowPath(flow)"
            stroke="#6366f1"
            stroke-width="2.5"
            fill="none"
            marker-end="url(#arrowhead)"
            class="flow-path"
            stroke-dasharray="0"
            stroke-linecap="round"
            opacity="0.8"
          />
          <!-- 连接条件标签 -->
          <g v-if="flow.condition" class="flow-label-group">
            <rect
              :x="getFlowLabelPosition(flow).x - flow.condition.length * 3"
              :y="getFlowLabelPosition(flow).y - 8"
              :width="flow.condition.length * 6 + 8"
              height="16"
              rx="8"
              ry="8"
              fill="white"
              stroke="#e5e7eb"
              stroke-width="1"
              filter="url(#nodeShadow)"
            />
            <text
              :x="getFlowLabelPosition(flow).x"
              :y="getFlowLabelPosition(flow).y"
              class="flow-label"
              text-anchor="middle"
              dominant-baseline="middle"
            >
              {{ flow.condition }}
            </text>
          </g>
        </g>
      </g>

      <!-- 绘制节点 -->
      <g class="nodes-group">
        <g 
          v-for="node in layoutNodes" 
          :key="node.id"
          :transform="`translate(${node.x}, ${node.y})`"
          class="node-group"
        >
          <!-- 节点背景 -->
          <rect
            :width="nodeWidth"
            :height="nodeHeight"
            :rx="12"
            :ry="12"
            :fill="getNodeColor(node.type)"
            :stroke="getNodeBorderColor(node.type)"
            stroke-width="1.5"
            class="node-background"
            filter="url(#nodeShadow)"
          />
          
          <!-- 节点图标背景圆形 -->
          <circle
            :cx="nodeWidth / 2"
            :cy="nodeHeight / 2 - 15"
            r="16"
            fill="rgba(255, 255, 255, 0.95)"
            stroke="rgba(255, 255, 255, 0.3)"
            stroke-width="1"
            class="icon-background"
          />
          
          <!-- 节点图标 -->
          <text
            :x="nodeWidth / 2"
            :y="nodeHeight / 2 - 15"
            text-anchor="middle"
            dominant-baseline="middle"
            class="node-icon"
            :style="`fill: ${getNodeIconColor(node.type)}`"
          >
            {{ getNodeIcon(node.type) }}
          </text>
          
          <!-- 节点名称 -->
          <text
            :x="nodeWidth / 2"
            :y="nodeHeight / 2 + 8"
            text-anchor="middle"
            dominant-baseline="middle"
            class="node-name"
          >
            {{ node.name }}
          </text>
          
          <!-- 节点类型标签背景 -->
          <rect
            :x="nodeWidth / 2 - getNodeTypeText(node.type).length * 3.5"
            :y="nodeHeight - 18"
            :width="getNodeTypeText(node.type).length * 7"
            height="14"
            rx="7"
            ry="7"
            fill="rgba(255, 255, 255, 0.9)"
            class="type-label-bg"
          />
          
          <!-- 节点类型标签 -->
          <text
            :x="nodeWidth / 2"
            :y="nodeHeight - 11"
            text-anchor="middle"
            dominant-baseline="middle"
            class="node-type"
          >
            {{ getNodeTypeText(node.type) }}
          </text>
          
          <!-- 处理人信息 -->
          <g v-if="node.assignee && showDetails" class="assignee-info">
            <rect
              :x="nodeWidth / 2 - getAssigneeDisplayName(node.assignee).length * 3"
              :y="nodeHeight + 8"
              :width="getAssigneeDisplayName(node.assignee).length * 6"
              height="16"
              rx="8"
              ry="8"
              fill="rgba(107, 114, 128, 0.1)"
              stroke="rgba(107, 114, 128, 0.3)"
              stroke-width="1"
            />
            <text
              :x="nodeWidth / 2"
              :y="nodeHeight + 16"
              text-anchor="middle"
              dominant-baseline="middle"
              class="node-assignee"
            >
              {{ getAssigneeDisplayName(node.assignee) }}
            </text>
          </g>
        </g>
      </g>
    </svg>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'

// 用户相关类型定义
interface User {
  id: number
  username: string
  realName?: string
  email?: string
}

// Props
interface Props {
  process: any
  showDetails?: boolean
  readonly?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  showDetails: false,
  readonly: false
})

// 响应式数据
const diagramContainer = ref<HTMLElement>()
const userMap = ref<Map<number, User>>(new Map()) // 用户ID到用户信息的映射
const nodeWidth = 140
const nodeHeight = 80
const levelHeight = 120
const nodeSpacing = 180

// 计算SVG尺寸
const svgWidth = computed(() => {
  if (!props.process || !props.process.nodes.length) return 800
  const maxLevel = Math.max(...layoutNodes.value.map(n => n.level))
  return Math.max(800, (maxLevel + 1) * nodeSpacing + 200)
})

const svgHeight = computed(() => {
  if (!props.process || !props.process.nodes.length) return 400
  const levels = groupNodesByLevel()
  const maxNodesInLevel = Math.max(...Object.values(levels).map((nodes: any) => nodes.length))
  return Math.max(400, maxNodesInLevel * levelHeight + 100)
})

// 计算节点布局
const layoutNodes = computed(() => {
  if (!props.process || !props.process.nodes.length) return []
  
  // 按层级分组节点
  const levels = groupNodesByLevel()
  const positioned: any[] = []
  
  Object.entries(levels).forEach(([level, nodes]: [string, any]) => {
    const levelNum = parseInt(level)
    const levelNodes = nodes as any[]
    const startY = (svgHeight.value - levelNodes.length * levelHeight) / 2
    
    levelNodes.forEach((node, index) => {
      positioned.push({
        ...node,
        level: levelNum,
        x: levelNum * nodeSpacing + 100,
        y: startY + index * levelHeight + 50
      })
    })
  })
  
  return positioned
})

// 按层级分组节点
const groupNodesByLevel = () => {
  if (!props.process || !props.process.nodes.length) return {}
  
  const levels: Record<number, any[]> = {}
  const visited = new Set<string>()
  const nodeMap = new Map(props.process.nodes.map((n: any) => [n.id, n]))
  
  // 找到开始节点
  const startNodes = props.process.nodes.filter((n: any) => n.type === 'START')
  
  // 使用BFS计算每个节点的层级
  const queue: Array<{node: any, level: number}> = []
  
  startNodes.forEach(startNode => {
    queue.push({ node: startNode, level: 0 })
  })
  
  while (queue.length > 0) {
    const { node, level } = queue.shift()!
    
    if (visited.has(node.id)) continue
    visited.add(node.id)
    
    if (!levels[level]) {
      levels[level] = []
    }
    levels[level].push(node)
    
    // 找到当前节点的下一级节点
    const outgoingFlows = props.process.flows.filter((f: any) => f.from === node.id)
    outgoingFlows.forEach((flow: any) => {
      const nextNode = nodeMap.get(flow.to)
      if (nextNode && !visited.has(nextNode.id)) {
        queue.push({ node: nextNode, level: level + 1 })
      }
    })
  }
  
  // 处理未访问的节点（可能是孤立节点）
  props.process.nodes.forEach((node: any) => {
    if (!visited.has(node.id)) {
      const maxLevel = Math.max(...Object.keys(levels).map(Number)) + 1
      if (!levels[maxLevel]) {
        levels[maxLevel] = []
      }
      levels[maxLevel].push(node)
    }
  })
  
  return levels
}

// 获取连接线路径
const getFlowPath = (flow: any) => {
  const fromNode = layoutNodes.value.find(n => n.id === flow.from)
  const toNode = layoutNodes.value.find(n => n.id === flow.to)
  
  if (!fromNode || !toNode) return ''
  
  const fromX = fromNode.x + nodeWidth
  const fromY = fromNode.y + nodeHeight / 2
  const toX = toNode.x
  const toY = toNode.y + nodeHeight / 2
  
  // 创建贝塞尔曲线路径
  const midX = (fromX + toX) / 2
  const controlPoint1X = fromX + (midX - fromX) * 0.5
  const controlPoint2X = toX - (toX - midX) * 0.5
  
  return `M ${fromX} ${fromY} C ${controlPoint1X} ${fromY}, ${controlPoint2X} ${toY}, ${toX} ${toY}`
}

// 获取连接线标签位置
const getFlowLabelPosition = (flow: any) => {
  const fromNode = layoutNodes.value.find(n => n.id === flow.from)
  const toNode = layoutNodes.value.find(n => n.id === flow.to)
  
  if (!fromNode || !toNode) return { x: 0, y: 0 }
  
  const fromX = fromNode.x + nodeWidth
  const fromY = fromNode.y + nodeHeight / 2
  const toX = toNode.x
  const toY = toNode.y + nodeHeight / 2
  
  return {
    x: (fromX + toX) / 2,
    y: (fromY + toY) / 2 - 10
  }
}

// 获取节点颜色 - 现代化渐变色
const getNodeColor = (type: string) => {
  const colorMap = {
    START: 'url(#startGradient)',
    END: 'url(#endGradient)', 
    APPROVAL: 'url(#approvalGradient)',
    CONDITIONAL_BRANCH: 'url(#branchGradient)'
  }
  return colorMap[type as keyof typeof colorMap] || 'url(#defaultGradient)'
}

// 获取节点边框颜色 - 柔和边框
const getNodeBorderColor = (type: string) => {
  const colorMap = {
    START: '#34d399',
    END: '#f87171',
    APPROVAL: '#818cf8',
    CONDITIONAL_BRANCH: '#fbbf24'
  }
  return colorMap[type as keyof typeof colorMap] || '#9ca3af'
}

// 获取节点图标
const getNodeIcon = (type: string) => {
  const iconMap = {
    START: '🚀',
    END: '🎯',
    APPROVAL: '👤',
    CONDITIONAL_BRANCH: '🔀'
  }
  return iconMap[type as keyof typeof iconMap] || '⚪'
}

// 获取节点图标颜色
const getNodeIconColor = (type: string) => {
  const colorMap = {
    START: '#059669',
    END: '#dc2626',
    APPROVAL: '#4f46e5',
    CONDITIONAL_BRANCH: '#d97706'
  }
  return colorMap[type as keyof typeof colorMap] || '#6b7280'
}

// 获取节点类型文本
const getNodeTypeText = (type: string) => {
  const typeMap = {
    START: '开始',
    END: '结束',
    APPROVAL: '审批',
    CONDITIONAL_BRANCH: '条件'
  }
  return typeMap[type as keyof typeof typeMap] || '未知'
}

// 加载用户数据
const userStore = useUserStore()

const loadUsers = async () => {
  try {
    // 使用缓存的用户数据
    const users = await userStore.loadEnabledUsers()
    
    // 构建用户ID到用户信息的映射
    const newUserMap = new Map<number, User>()
    users.forEach((user: User) => {
      newUserMap.set(user.id, user)
    })
    userMap.value = newUserMap
  } catch (error) {
    console.error('加载用户数据失败:', error)
  }
}

// 获取处理人显示名称
const getAssigneeDisplayName = (assignee: string | number | undefined): string => {
  if (!assignee) {
    return '未设置'
  }
  
  // 如果assignee是数字（用户ID）
  const userId = typeof assignee === 'string' ? parseInt(assignee) : assignee
  if (!isNaN(userId)) {
    const user = userMap.value.get(userId)
    if (user) {
      return user.realName || user.username
    }
  }
  
  // 如果找不到用户或者assignee不是数字，直接返回原值
  return assignee.toString()
}

// 组件挂载时加载用户数据
onMounted(() => {
  loadUsers()
})
</script>

<style scoped lang="scss">
.workflow-diagram {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-card, #ffffff);
  border-radius: 8px;
  overflow: auto;
}

.empty-diagram {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  min-height: 300px;
}

.diagram-svg {
  max-width: 100%;
  max-height: 100%;
  
  .flow-path {
    transition: all 0.3s ease;
    cursor: pointer;
    
    &:hover {
      stroke-width: 3.5 !important;
      opacity: 1 !important;
      stroke: #4f46e5;
    }
  }
  
  .flow-label-group {
    .flow-label {
      font-size: 11px;
      font-weight: 500;
      fill: #374151;
      user-select: none;
    }
  }
  
  .node-group {
    cursor: pointer;
    
    .node-background {
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }
    
    .icon-background {
      transition: all 0.3s ease;
    }
    
    .node-icon {
      font-size: 16px;
      font-weight: normal;
      transition: all 0.3s ease;
      user-select: none;
    }
    
    .node-name {
      font-size: 13px;
      font-weight: 600;
      fill: white;
      text-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
      user-select: none;
      transition: all 0.3s ease;
    }
    
    .type-label-bg {
      transition: all 0.3s ease;
    }
    
    .node-type {
      font-size: 10px;
      font-weight: 600;
      fill: #374151;
      user-select: none;
      transition: all 0.3s ease;
    }
    
    .assignee-info {
      .node-assignee {
        font-size: 10px;
        font-weight: 500;
        fill: #6b7280;
        user-select: none;
      }
    }
    
    // 悬停效果 - 移除transform避免跳动
    &:hover {
      .node-background {
        stroke-width: 2.5;
        filter: url(#nodeShadow) brightness(1.05);
      }
      
      .icon-background {
        fill: white;
        stroke: rgba(255, 255, 255, 0.6);
        r: 18;
      }
      
      .node-icon {
        font-size: 18px;
      }
      
      .node-name {
        font-size: 14px;
      }
      
      .type-label-bg {
        fill: white;
      }
    }
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .diagram-svg {
    .node-name {
      font-size: 11px;
    }
    
    .node-type {
      font-size: 9px;
    }
    
    .flow-label {
      font-size: 10px;
    }
  }
}
</style>
