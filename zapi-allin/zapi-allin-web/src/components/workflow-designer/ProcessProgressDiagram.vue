<template>
  <div class="process-progress-diagram" ref="diagramContainer">
    <div class="diagram-header">
      <div class="header-info">
        <h4 class="diagram-title">
          <el-icon><Connection /></el-icon>
          流程执行进度图
        </h4>
        <div class="progress-summary">
          <span class="progress-text">进度: {{ progressPercentage }}%</span>
          <el-progress
            :percentage="progressPercentage"
            :color="progressColor"
            :stroke-width="6"
            style="width: 200px;"
          />
        </div>
      </div>

      <!-- 工具栏 -->
      <div class="diagram-toolbar">
        <el-button
          size="small"
          type="primary"
          :icon="FullScreen"
          @click="toggleFullscreen"
        >
          {{ isFullscreen ? '退出全屏' : '全屏查看' }}
        </el-button>
        <el-button
          size="small"
          :icon="Refresh"
          @click="refreshDiagram"
        >
          刷新
        </el-button>
      </div>

      <!-- 图例 -->
      <div class="diagram-legend">
        <div class="legend-item">
          <div class="legend-color pending"></div>
          <span>未开始</span>
        </div>
        <div class="legend-item">
          <div class="legend-color current"></div>
          <span>进行中</span>
        </div>
        <div class="legend-item">
          <div class="legend-color completed"></div>
          <span>已完成</span>
        </div>
        <div class="legend-item">
          <div class="legend-color rejected"></div>
          <span>已拒绝</span>
        </div>
      </div>
    </div>

    <div
      class="diagram-content"
      ref="diagramContentRef"
      @mousedown="handleMouseDown"
      @mousemove="handleMouseMove"
      @mouseup="handleMouseUp"
      @mouseleave="handleMouseUp"
      :class="{ 'is-dragging': isDragging }"
    >
      <div class="canvas-content" :style="canvasContentStyle">
        <!-- 流程节点 -->
        <div
          v-for="node in processNodes"
          :key="node.id"
          class="progress-node"
          :class="[
            `node-${node.type.toLowerCase()}`,
            `status-${node.status}`,
            { 'has-execution-info': node.executionInfo }
          ]"
          :style="{
            left: node.position.x + 'px',
            top: node.position.y + 'px'
          }"
          @click="handleNodeClick(node)"
        >
          <div class="node-header">
            <div class="node-icon-wrapper">
              <el-icon class="node-icon">
                <component :is="getNodeIcon(node.type)" />
              </el-icon>
              <!-- 状态指示器 -->
              <div class="status-indicator">
                <el-icon v-if="node.status === 'completed'" class="status-icon completed">
                  <Check />
                </el-icon>
                <el-icon v-else-if="node.status === 'rejected'" class="status-icon rejected">
                  <Close />
                </el-icon>
                <el-icon v-else-if="node.status === 'current'" class="status-icon current">
                  <Loading />
                </el-icon>
                <el-icon v-else class="status-icon pending">
                  <Clock />
                </el-icon>
              </div>
            </div>
            <div class="node-info">
              <span class="node-title">{{ node.name }}</span>
              <!-- 显示审批节点的处理人 -->
              <span v-if="node.type === 'APPROVAL' && node.assigneeDisplayName" class="node-assignee">
                {{ node.assigneeDisplayName }}
              </span>
            </div>
          </div>

          <!-- 执行信息 -->
          <div v-if="node.executionInfo && node.executionInfo.endTime" class="execution-info">
            <!-- 只有已完成的节点才显示执行信息 -->
            <div class="execution-row">
              <el-icon><Timer /></el-icon>
              <span>{{ formatDuration(node.executionInfo.duration) }}</span>
            </div>
            <div v-if="node.executionInfo.operator" class="execution-row">
              <el-icon><User /></el-icon>
              <span>{{ node.executionInfo.operatorName || getOperatorDisplayName(node.executionInfo.operator) }}</span>
            </div>
          </div>

          <!-- 审批节点的待审批人员信息（当节点进行中时显示审批人） -->
          <div v-if="node.type === 'APPROVAL' && node.status === 'current' && node.assigneeDisplayName" class="assignee-info">
            <div class="assignee-row">
              <el-icon><User /></el-icon>
              <span>{{ node.assigneeDisplayName }}</span>
            </div>
          </div>

          <!-- 节点状态标签 -->
          <div class="node-status-badge">
            <el-tag
              :type="getStatusTagType(node.status)"
              size="small"
              class="status-tag"
            >
              {{ getStatusText(node.status) }}
            </el-tag>
          </div>
        </div>

        <!-- 连接线 -->
        <svg
          class="flows-svg"
          :width="canvasSize.width"
          :height="canvasSize.height"
        >
          <defs>
            <marker id="arrowhead" markerWidth="10" markerHeight="10" refX="0" refY="5" orient="auto" markerUnits="userSpaceOnUse">
              <path d="M 0,0 L 0,10 L 9,5 Z" fill="#d1d5db" />
            </marker>
            <marker id="arrowhead-completed" markerWidth="10" markerHeight="10" refX="0" refY="5" orient="auto" markerUnits="userSpaceOnUse">
              <path d="M 0,0 L 0,10 L 9,5 Z" fill="#10b981" />
            </marker>
            <marker id="arrowhead-current" markerWidth="10" markerHeight="10" refX="0" refY="5" orient="auto" markerUnits="userSpaceOnUse">
              <path d="M 0,0 L 0,10 L 9,5 Z" fill="#3b82f6" />
            </marker>
          </defs>

          <!-- 流程连接线 -->
          <path
            v-for="flow in processFlows"
            :key="flow.id"
            :d="getFlowPath(flow)"
            :stroke="getFlowColor(flow)"
            :stroke-width="getFlowWidth(flow)"
            fill="none"
            :marker-end="getFlowMarker(flow)"
            class="flow-line"
            :class="flow.status"
          />
        </svg>
      </div>
    </div>

    <!-- 节点详情弹窗 -->
    <el-dialog
      v-model="showNodeDetail"
      :title="`节点执行详情 - ${selectedNode?.name}`"
      width="600px"
      @close="closeNodeDetail"
    >
      <div v-if="selectedNode" class="node-detail-content">
        <!-- 节点基本信息 -->
        <div class="detail-section">
          <h4>基本信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="节点名称">
              {{ selectedNode.name }}
            </el-descriptions-item>
            <el-descriptions-item label="节点ID">
              {{ selectedNode.id }}
            </el-descriptions-item>
            <el-descriptions-item label="节点类型">
              {{ getNodeTypeName(selectedNode.type) }}
            </el-descriptions-item>
            <el-descriptions-item label="执行状态">
              <el-tag :type="getStatusTagType(selectedNode.status)">
                {{ getStatusText(selectedNode.status) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 审批节点显示审批方式和所有审批记录 -->
        <div v-if="selectedNode.type === 'APPROVAL'" class="detail-section">
          <h4>
            审批详情
            <el-tag
              :type="getNodeApprovalModeType(selectedNode.id)"
              size="small"
              style="margin-left: 8px;"
            >
              {{ getNodeApprovalModeText(selectedNode.id) }}
            </el-tag>
          </h4>

          <!-- 显示所有审批记录 -->
          <div v-if="getNodeAllExecutionInfos(selectedNode.id).length > 0" class="approval-records">
            <div
              v-for="(record, index) in getNodeAllExecutionInfos(selectedNode.id)"
              :key="index"
              class="approval-record-item"
            >
              <el-descriptions :column="1" border size="small">
                <el-descriptions-item label="审批人">
                  {{ record.operatorName || record.operator || '-' }}
                </el-descriptions-item>
                <el-descriptions-item label="审批时间">
                  {{ formatTime(record.endTime) || formatTime(record.startTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="审批结果">
                  <el-tag :type="getApprovalResultType(record.comment)" size="small">
                    {{ getApprovalResultText(record.comment) }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="审批意见">
                  <div class="comment-content">
                    {{ extractApprovalComment(record.comment) || '-' }}
                  </div>
                </el-descriptions-item>
              </el-descriptions>
            </div>
          </div>
          <div v-else class="no-execution-info">
            <el-empty description="暂无审批记录" />
          </div>
        </div>

        <!-- 非审批节点显示单条执行详情 -->
        <div v-else-if="selectedNode.executionInfo" class="detail-section">
          <h4>执行详情</h4>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="开始时间">
              {{ formatTime(selectedNode.executionInfo.startTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="结束时间">
              {{ formatTime(selectedNode.executionInfo.endTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="执行耗时">
              {{ formatDuration(selectedNode.executionInfo.duration) }}
            </el-descriptions-item>
            <el-descriptions-item label="操作人">
              {{ selectedNode.executionInfo.operatorName || selectedNode.executionInfo.operator || '-' }}
            </el-descriptions-item>
            <el-descriptions-item label="操作备注">
              <div class="comment-content">
                {{ selectedNode.executionInfo.comment || '-' }}
              </div>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 无执行记录 -->
        <div v-else class="no-execution-info">
          <el-empty description="暂无执行记录" />
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted, nextTick, watch } from 'vue'
import {
  Check, Close, Loading, Clock, Timer, User, Connection,
  VideoPlay, Document, FullScreen, Refresh
} from '@element-plus/icons-vue'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'

// 用户相关类型定义
interface User {
  id: number
  username: string
  realName?: string
  email?: string
}

// Props 定义
interface ProcessNode {
  id: string
  name: string
  type: string
  position: { x: number; y: number }
  status: 'pending' | 'current' | 'completed' | 'rejected'
  assignee?: string | number
  approvalTargets?: Array<{ id: number; type: string; name?: string }>
  assigneeDisplayName?: string
  executionInfo?: {
    startTime?: string
    endTime?: string
    duration?: number
    operator?: string
    operatorName?: string
    comment?: string
    nodeName?: string
    nodeType?: string
  }
}

interface ProcessFlow {
  id: string
  from: string
  to: string
  name?: string
  condition?: string
  status: 'pending' | 'active' | 'completed'
}

interface Props {
  processDefinition: {
    id: string
    name: string
    key: string
    jsonDef: string
  }
  taskHistory: any[]
  currentNodeId?: string
  processStatus: number
  instanceId?: string
}

const props = defineProps<Props>()

// 响应式数据
const diagramContainer = ref<HTMLElement>()
const diagramContentRef = ref<HTMLElement>()
const showNodeDetail = ref(false)
const selectedNode = ref<ProcessNode | null>(null)
const diagramHeight = ref(500)
const canvasSize = reactive({ width: 3000, height: 2000 })
const isFullscreen = ref(false)
const userMap = ref<Map<number, User>>(new Map())
const usersLoaded = ref(false)

// 拖拽平移相关
const isDragging = ref(false)
const dragStartX = ref(0)
const dragStartY = ref(0)
const scrollStartX = ref(0)
const scrollStartY = ref(0)

// 计算属性
const processNodes = computed(() => {
  // 确保用户数据已加载，这样computed会在usersLoaded变化时重新计算
  // usersLoaded的变化会触发重新计算，从而正确显示用户名
  const _ = usersLoaded.value
  console.log(`[processNodes] 计算节点列表，usersLoaded=${usersLoaded.value}, userMap.size=${userMap.value.size}`)

  try {
    // 检查 jsonDef 是否存在
    if (!props.processDefinition || !props.processDefinition.jsonDef) {
      console.warn('[processNodes] processDefinition 或 jsonDef 不存在')
      return []
    }

    const definition = JSON.parse(props.processDefinition.jsonDef)
    const nodes = definition.nodes || []

    // 构建节点ID到审批人名称的映射（从taskHistory中获取）
    const nodeApprovalUserNamesMap: Record<string, string> = {}
    if (props.taskHistory) {
      props.taskHistory.forEach((task: any) => {
        if (task.nodeType === 'APPROVAL' && task.approvalUserNames) {
          nodeApprovalUserNamesMap[task.nodeId] = task.approvalUserNames
        }
      })
    }

    return nodes.map((node: any) => {
      const nodeStatus = getNodeStatus(node.id)
      const executionInfo = getNodeExecutionInfo(node.id)

      console.log(`[processNodes] 节点${node.id}, executionInfo:`, executionInfo)

      // 处理审批节点的assignee显示（支持多人审批）
      let assigneeDisplayName: string | null = null
      if (node.type === 'APPROVAL') {
        // 优先使用后端返回的 approvalUserNames（从审批记录表获取）
        if (nodeApprovalUserNamesMap[node.id]) {
          assigneeDisplayName = nodeApprovalUserNamesMap[node.id]
        }
        // 其次使用 approvalTargets（多人审批）
        else if (node.approvalTargets && node.approvalTargets.length > 0) {
          assigneeDisplayName = getApprovalTargetsDisplayName(node.approvalTargets)
        } else if (node.assignee) {
          // 兼容旧的单人审批
          assigneeDisplayName = getOperatorDisplayName(node.assignee)
        }
      }

      return {
        id: node.id,
        name: node.name || node.id,
        type: node.type,
        position: calculateNodePosition(node, nodes),
        status: nodeStatus,
        executionInfo,
        assignee: node.assignee,
        approvalTargets: node.approvalTargets,
        assigneeDisplayName
      }
    })
  } catch (error) {
    console.error('解析流程定义失败:', error)
    return []
  }
})

const processFlows = computed(() => {
  try {
    // 检查 jsonDef 是否存在
    if (!props.processDefinition || !props.processDefinition.jsonDef) {
      console.warn('[processFlows] processDefinition 或 jsonDef 不存在')
      return []
    }

    const definition = JSON.parse(props.processDefinition.jsonDef)
    const flows = definition.flows || []

    return flows.map((flow: any) => ({
      id: flow.id || `${flow.from}-${flow.to}`,
      from: flow.from,
      to: flow.to,
      name: flow.name,
      condition: flow.condition,
      status: getFlowStatus(flow)
    }))
  } catch (error) {
    console.error('解析流程连接失败:', error)
    return []
  }
})

const progressPercentage = computed(() => {
  const totalNodes = processNodes.value.length
  if (totalNodes === 0) return 0

  const completedNodes = processNodes.value.filter(node =>
    node.status === 'completed'
  ).length

  return Math.round((completedNodes / totalNodes) * 100)
})

const progressColor = computed(() => {
  if (props.processStatus === 2) return '#67c23a' // 已完成 - 绿色
  if (progressPercentage.value >= 80) return '#409eff' // 进行中高进度 - 蓝色
  if (progressPercentage.value >= 50) return '#e6a23c' // 进行中中进度 - 橙色
  return '#f56c6c' // 进行中低进度 - 红色
})

// 画布内容样式
const canvasContentStyle = computed(() => ({
  width: canvasSize.width + 'px',
  height: canvasSize.height + 'px',
  minWidth: canvasSize.width + 'px',
  minHeight: canvasSize.height + 'px'
}))

// 拖拽平移方法
const handleMouseDown = (event: MouseEvent) => {
  const target = event.target as HTMLElement

  // 检查是否点击在节点上
  const isOnNode = target.closest('.progress-node') ||
                   target.closest('.el-button') ||
                   target.closest('.node-status-badge')

  if (isOnNode) {
    return
  }

  if (event.button === 0) {
    event.preventDefault()
    isDragging.value = true
    dragStartX.value = event.clientX
    dragStartY.value = event.clientY
    if (diagramContentRef.value) {
      scrollStartX.value = diagramContentRef.value.scrollLeft
      scrollStartY.value = diagramContentRef.value.scrollTop
    }
  }
}

const handleMouseMove = (event: MouseEvent) => {
  if (isDragging.value && diagramContentRef.value) {
    event.preventDefault()
    const deltaX = event.clientX - dragStartX.value
    const deltaY = event.clientY - dragStartY.value
    diagramContentRef.value.scrollLeft = scrollStartX.value - deltaX
    diagramContentRef.value.scrollTop = scrollStartY.value - deltaY
  }
}

const handleMouseUp = () => {
  isDragging.value = false
}

// 初始化画布位置（滚动到画布中心）
const initCanvasPosition = (retryCount = 0) => {
  nextTick(() => {
    if (diagramContentRef.value) {
      const wrapper = diagramContentRef.value

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

// 方法
const getNodeStatus = (nodeId: string) => {
  if (!props.taskHistory || props.taskHistory.length === 0) {
    // 根据流程状态和当前节点推断
    if (props.processStatus === 2) {
      return 'completed'
    } else if (nodeId === props.currentNodeId) {
      return 'current'
    }
    return 'pending'
  }

  // 获取该节点的所有历史记录
  let nodeActivities = props.taskHistory.filter(activity => activity.nodeId === nodeId)

  // 仅对特殊节点进行模糊匹配（START和END节点）
  if (nodeActivities.length === 0) {
    if (nodeId === 'end' || (nodeId.startsWith('end_') && nodeId.includes('end'))) {
      nodeActivities = props.taskHistory.filter(activity =>
        activity.nodeId === 'end' ||
        activity.nodeType === 'END'
      )
    } else if (nodeId === 'start' || (nodeId.startsWith('start_') && nodeId.includes('start'))) {
      nodeActivities = props.taskHistory.filter(activity =>
        activity.nodeId === 'start' ||
        activity.nodeType === 'START'
      )
    }
  }

  if (nodeActivities.length > 0) {
    // 对于审批节点，优先查找"节点完成"记录（由后端在会签/或签完成时创建）
    const nodeCompleteActivity = nodeActivities.find(activity =>
      activity.comment && activity.comment.includes('审批节点完成')
    )

    if (nodeCompleteActivity) {
      // 节点已完成，检查是通过还是驳回
      if (nodeCompleteActivity.comment.includes('驳回')) {
        return 'rejected'
      }
      return 'completed'
    }

    // 检查是否有驳回记录（兼容旧数据和或签场景）
    const rejectedActivity = nodeActivities.find(activity =>
      activity.endTime && activity.comment &&
      (activity.comment.includes('驳回') ||
       activity.comment.includes('拒绝') ||
       activity.comment.includes('reject'))
    )
    if (rejectedActivity) {
      return 'rejected'
    }

    // 检查是否有已完成的记录（有 endTime，用于兼容旧数据）
    const completedActivity = nodeActivities.find(activity => activity.endTime)
    if (completedActivity) {
      return 'completed'
    }

    // 有开始记录但没有完成记录，说明进行中
    const startedActivity = nodeActivities.find(activity => activity.startTime)
    if (startedActivity) {
      return 'current'
    }
  }

  // 没有找到对应的历史记录，说明该节点未被执行
  return 'pending'
}

const getNodeExecutionInfo = (nodeId: string) => {
  if (!props.taskHistory || props.taskHistory.length === 0) {
    return null
  }

  // 精确匹配（优先使用，避免误匹配）
  let nodeActivity = props.taskHistory.find(activity => activity.nodeId === nodeId)

  // 仅对特殊节点进行模糊匹配（START和END节点）
  // 注意：审批节点不进行模糊匹配，避免将未执行的审批节点误判为已完成
  if (!nodeActivity) {
    if (nodeId === 'end' || (nodeId.startsWith('end_') && nodeId.includes('end'))) {
      nodeActivity = props.taskHistory.find(activity =>
        activity.nodeId === 'end' ||
        activity.nodeType === 'END'
      )
    } else if (nodeId === 'start' || (nodeId.startsWith('start_') && nodeId.includes('start'))) {
      nodeActivity = props.taskHistory.find(activity =>
        activity.nodeId === 'start' ||
        activity.nodeType === 'START'
      )
    }
    // 对于其他类型的节点（如审批节点、条件分支节点），不进行模糊匹配
    // 必须精确匹配nodeId，这样可以确保只显示真正执行过的节点的执行信息
  }

  if (!nodeActivity) return null

  // 获取操作人信息
  let operator = nodeActivity.operator
  let operatorName = nodeActivity.operatorName

  console.log(`[getNodeExecutionInfo] 节点ID=${nodeId}, 节点类型=${nodeActivity.nodeType}, operator=${operator}, operatorName=${operatorName}`)

  // 对于抄送节点，从comment中提取抄送接收人作为操作人
  if (nodeActivity.nodeType === 'CC' && nodeActivity.comment) {
    // 从comment中提取抄送接收人: "抄送通知，方式: SYSTEM，接收人: 张三、李四、王五"
    const ccUserMatch = nodeActivity.comment.match(/接收人:\s*(.+?)(?:，|$)/)
    if (ccUserMatch && ccUserMatch[1]) {
      // 后端已经返回了姓名，直接使用
      operatorName = ccUserMatch[1].trim()
      operator = operatorName // 保持一致
      console.log(`[getNodeExecutionInfo] 抄送节点，提取到接收人: operatorName=${operatorName}`)
    }
  }

  // 统一处理所有节点类型的操作人显示
  // 无论operatorName是什么值，都尝试从userMap获取真实姓名
  // 这样可以确保即使后端返回了"操作员1"这样的值，也能被替换为真实姓名
  const newOperatorName = getOperatorDisplayName(operator)
  console.log(`[getNodeExecutionInfo] 从userMap获取操作人名称: operator=${operator}, operatorName=${operatorName} -> ${newOperatorName}`)

  // 只有当从userMap成功获取到用户名时才使用，否则保留原有的operatorName
  if (newOperatorName !== operator?.toString() && newOperatorName !== '系统') {
    operatorName = newOperatorName
  } else if (!operatorName) {
    // 如果原本就没有operatorName，使用新获取的值
    operatorName = newOperatorName
  }

  console.log(`[getNodeExecutionInfo] 最终结果: operator=${operator}, operatorName=${operatorName}`)

  return {
    startTime: nodeActivity.startTime,
    endTime: nodeActivity.endTime,
    duration: nodeActivity.duration,
    operator: operator,
    operatorName: operatorName,
    comment: nodeActivity.comment,
    nodeName: nodeActivity.nodeName,
    nodeType: nodeActivity.nodeType
  }
}

// 获取节点的所有执行记录（用于会签场景显示多条审批记录）
const getNodeAllExecutionInfos = (nodeId: string) => {
  if (!props.taskHistory || props.taskHistory.length === 0) {
    return []
  }

  // 获取该节点的所有执行记录
  // 1. 过滤掉没有 endTime 的记录（节点开始记录，不是实际审批记录）
  // 2. 过滤掉"审批节点完成"的汇总记录（这是节点完成时的标记记录，不是实际审批人的记录）
  const nodeActivities = props.taskHistory.filter(activity =>
    activity.nodeId === nodeId &&
    activity.endTime &&
    !(activity.comment && activity.comment.includes('审批节点完成'))
  )

  return nodeActivities.map(activity => {
    let operatorName = activity.operatorName
    if (!operatorName && activity.operator) {
      operatorName = getOperatorDisplayName(activity.operator)
    }
    return {
      startTime: activity.startTime,
      endTime: activity.endTime,
      duration: activity.duration,
      operator: activity.operator,
      operatorName: operatorName,
      comment: activity.comment,
      nodeName: activity.nodeName,
      nodeType: activity.nodeType
    }
  })
}

// 获取节点的审批方式文本
const getNodeApprovalModeText = (nodeId: string): string => {
  // 从 taskHistory 中获取
  const activity = props.taskHistory?.find(a => a.nodeId === nodeId)
  if (activity?.approvalMode) {
    return activity.approvalMode === 'ALL' ? '会签' : '或签'
  }

  // 从流程定义中获取
  try {
    if (props.processDefinition && props.processDefinition.jsonDef) {
      const definition = JSON.parse(props.processDefinition.jsonDef)
      const node = definition.nodes?.find((n: any) => n.id === nodeId)
      if (node?.approvalMode) {
        return node.approvalMode === 'ALL' ? '会签' : '或签'
      }
    }
  } catch (error) {
    // ignore
  }

  return '或签'
}

// 获取节点的审批方式标签类型
const getNodeApprovalModeType = (nodeId: string): string => {
  const activity = props.taskHistory?.find(a => a.nodeId === nodeId)
  if (activity?.approvalMode) {
    return activity.approvalMode === 'ALL' ? 'warning' : 'success'
  }

  try {
    if (props.processDefinition && props.processDefinition.jsonDef) {
      const definition = JSON.parse(props.processDefinition.jsonDef)
      const node = definition.nodes?.find((n: any) => n.id === nodeId)
      if (node?.approvalMode) {
        return node.approvalMode === 'ALL' ? 'warning' : 'success'
      }
    }
  } catch (error) {
    // ignore
  }

  return 'success'
}

// 获取审批结果文本
const getApprovalResultText = (comment: string | undefined): string => {
  if (!comment) return '待审批'
  if (comment.includes('驳回') || comment.includes('拒绝')) return '已驳回'
  if (comment.includes('通过')) return '已通过'
  return '已处理'
}

// 获取审批结果标签类型
const getApprovalResultType = (comment: string | undefined): string => {
  if (!comment) return 'info'
  if (comment.includes('驳回') || comment.includes('拒绝')) return 'danger'
  if (comment.includes('通过')) return 'success'
  return 'info'
}

// 提取审批意见（去掉前缀）
const extractApprovalComment = (comment: string | undefined): string => {
  if (!comment) return ''
  // 去掉 "审批通过：" 或 "审批驳回：" 前缀
  return comment.replace(/^审批(通过|驳回)：/, '').trim()
}

const getFlowStatus = (flow: any) => {
  const fromNode = processNodes.value.find(n => n.id === flow.from)
  const toNode = processNodes.value.find(n => n.id === flow.to)

  // 对于条件分支节点，需要特殊处理
  // 如果起点是条件分支节点且已完成，只有目标节点有执行记录的连线才是激活的
  if (fromNode?.type === 'CONDITIONAL_BRANCH' && fromNode?.status === 'completed') {
    // 检查目标节点是否有执行记录（即是否真正走了这条分支）
    if (toNode?.status === 'completed' || toNode?.status === 'current' || toNode?.status === 'rejected') {
      // 目标节点有执行记录，说明走了这条分支
      if (toNode?.status === 'completed' || toNode?.status === 'rejected') {
        return 'completed'
      } else if (toNode?.status === 'current') {
        return 'active'
      }
    }
    // 目标节点是pending状态，说明没走这条分支
    return 'pending'
  }

  // 常规节点的连线状态判断
  if (fromNode?.status === 'completed' && toNode?.status === 'completed') {
    return 'completed'
  } else if (fromNode?.status === 'completed' && toNode?.status === 'rejected') {
    return 'completed'
  } else if (fromNode?.status === 'completed' && toNode?.status === 'current') {
    return 'active'
  } else if (fromNode?.status === 'current') {
    return 'pending'
  }

  return 'pending'
}

const calculateNodePosition = (node: any, allNodes: any[]) => {
  // 优先使用流程定义中保存的position，保持和设计器一致
  if (node.position) {
    return {
      x: node.position.x,
      y: node.position.y
    }
  }

  // 如果没有保存位置，使用自动布局作为fallback
  const index = allNodes.findIndex(n => n.id === node.id)
  const spacing = 280
  const startX = 100
  const startY = 200

  if (node.type === 'START') {
    return { x: startX, y: startY }
  } else if (node.type === 'END') {
    return { x: startX + (allNodes.length - 1) * spacing, y: startY }
  } else {
    return { x: startX + index * spacing, y: startY }
  }
}

const getNodeIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    START: VideoPlay,
    END: Check,
    APPROVAL: User,
    CONDITIONAL_BRANCH: Connection
  }
  return iconMap[type] || Document
}

const getNodeTypeName = (type: string) => {
  const typeMap: Record<string, string> = {
    START: '开始节点',
    END: '结束节点',
    APPROVAL: '审批节点',
    CONDITIONAL_BRANCH: '条件分支'
  }
  return typeMap[type] || type
}

const getStatusText = (status: string) => {
  const statusMap: Record<string, string> = {
    pending: '未开始',
    current: '进行中',
    completed: '已完成',
    rejected: '已拒绝'
  }
  return statusMap[status] || '未知'
}

const getStatusTagType = (status: string) => {
  const typeMap: Record<string, string> = {
    pending: 'info',
    current: 'warning',
    completed: 'success',
    rejected: 'danger'
  }
  return typeMap[status] || 'info'
}

const getFlowColor = (flow: ProcessFlow) => {
  const colorMap: Record<string, string> = {
    completed: '#10b981',
    active: '#3b82f6',
    pending: '#d1d5db'
  }
  return colorMap[flow.status] || '#d1d5db'
}

const getFlowWidth = (flow: ProcessFlow) => {
  return flow.status === 'completed' || flow.status === 'active' ? 3 : 2
}

const getFlowMarker = (flow: ProcessFlow) => {
  if (flow.status === 'completed') return 'url(#arrowhead-completed)'
  if (flow.status === 'active') return 'url(#arrowhead-current)'
  return 'url(#arrowhead)'
}

const getFlowPath = (flow: ProcessFlow) => {
  const fromNode = processNodes.value.find(n => n.id === flow.from)
  const toNode = processNodes.value.find(n => n.id === flow.to)

  if (!fromNode || !toNode) return ''

  // 箭头长度，用于调整路径终点，让箭头正好指向节点边缘
  const arrowLength = 10

  const fromPos = {
    x: fromNode.position.x + 180, // 节点右边
    y: fromNode.position.y + 40   // 节点中间
  }
  const targetPos = {
    x: toNode.position.x,         // 节点左边
    y: toNode.position.y + 40     // 节点中间
  }

  const dx = targetPos.x - fromPos.x
  const dy = targetPos.y - fromPos.y

  // 创建贝塞尔曲线
  const controlDistance = Math.max(Math.abs(dx) * 0.3, 50)
  const cp1x = fromPos.x + controlDistance
  const cp1y = fromPos.y
  const cp2x = targetPos.x - controlDistance
  const cp2y = targetPos.y

  // 计算贝塞尔曲线终点的切线方向
  // 对于三次贝塞尔曲线 C(cp1, cp2, end)，终点处的切线方向是 cp2 指向 end 的方向
  const tangentDx = targetPos.x - cp2x
  const tangentDy = targetPos.y - cp2y
  const tangentLength = Math.sqrt(tangentDx * tangentDx + tangentDy * tangentDy)

  // 如果切线长度为0（控制点和终点重合），使用直线方向
  let toPos
  if (tangentLength < 0.1) {
    const lineLength = Math.sqrt(dx * dx + dy * dy)
    if (lineLength < 0.1) {
      toPos = targetPos
    } else {
      toPos = {
        x: targetPos.x - (dx / lineLength) * arrowLength,
        y: targetPos.y - (dy / lineLength) * arrowLength
      }
    }
  } else {
    // 路径终点提前箭头长度，让箭头正好指向节点边缘
    // orient="auto" 会自动根据曲线终点的切线方向旋转箭头
    toPos = {
      x: targetPos.x - (tangentDx / tangentLength) * arrowLength,
      y: targetPos.y - (tangentDy / tangentLength) * arrowLength
    }
  }

  return `M ${fromPos.x} ${fromPos.y} C ${cp1x} ${cp1y}, ${cp2x} ${cp2y}, ${toPos.x} ${toPos.y}`
}

const handleNodeClick = (node: ProcessNode) => {
  selectedNode.value = node
  showNodeDetail.value = true
}

const closeNodeDetail = () => {
  showNodeDetail.value = false
  selectedNode.value = null
}

const toggleFullscreen = () => {
  if (!isFullscreen.value) {
    // 进入全屏模式
    if (diagramContainer.value?.requestFullscreen) {
      diagramContainer.value.requestFullscreen()
      isFullscreen.value = true
      diagramHeight.value = window.innerHeight - 200
    }
  } else {
    // 退出全屏模式
    if (document.exitFullscreen) {
      document.exitFullscreen()
      isFullscreen.value = false
      diagramHeight.value = 500
    }
  }
}

const refreshDiagram = () => {
  // 根据节点的实际位置计算画布大小
  const nodes = processNodes.value
  if (nodes.length > 0) {
    // 找到最右边和最下边的节点位置
    const maxX = Math.max(...nodes.map(n => n.position.x)) + 250 // 节点宽度180 + 余量
    const maxY = Math.max(...nodes.map(n => n.position.y)) + 150 // 节点高度80 + 余量

    canvasSize.width = Math.max(1200, maxX)
    canvasSize.height = Math.max(500, maxY)
    diagramHeight.value = isFullscreen.value ? window.innerHeight - 200 : Math.min(600, canvasSize.height)
  }
}

const formatTime = (time?: string) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const formatDuration = (duration?: number) => {
  if (!duration || duration === 0) return '瞬时'

  const seconds = Math.floor(duration / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)

  if (days > 0) {
    return `${days}天${hours % 24}小时`
  } else if (hours > 0) {
    return `${hours}小时${minutes % 60}分钟`
  } else if (minutes > 0) {
    return `${minutes}分钟${seconds % 60}秒`
  } else {
    return `${seconds}秒`
  }
}

// 加载用户数据
const userStore = useUserStore()

const loadUsers = async () => {
  try {
    console.log('[流程进度图] 开始加载用户数据...')
    // 使用缓存的用户数据
    const users = await userStore.loadEnabledUsers()
    console.log('[流程进度图] 解析后的用户数据:', users)

    // 构建用户ID到用户信息的映射
    // 同时存储数字和字符串类型的key，以兼容不同的数据格式
    const newUserMap = new Map<number, User>()
    users.forEach((user: User) => {
      newUserMap.set(user.id, user)
      // 同时也存储字符串类型的key
      newUserMap.set(user.id.toString() as any, user)
      console.log(`[流程进度图] 添加用户到Map: ID=${user.id}, realName=${user.realName}, username=${user.username}`)
    })
    userMap.value = newUserMap
    usersLoaded.value = true
    console.log('[流程进度图] 用户Map构建完成，总数:', userMap.value.size)
  } catch (error) {
    console.error('加载用户数据失败:', error)
    usersLoaded.value = true // 即使失败也标记为已加载，避免阻塞
  }
}

// 获取操作员显示名称
const getOperatorDisplayName = (operator: string | number | undefined): string => {
  console.log(`[getOperatorDisplayName] 输入: operator=${operator} (类型: ${typeof operator}), userMap.size=${userMap.value.size}`)

  // 如果operator为空，返回"系统"
  if (!operator) {
    console.log(`[getOperatorDisplayName] operator为空，返回"系统"`)
    return '系统'
  }

  // 先尝试直接用原值查询（可能是字符串或数字）
  let user = userMap.value.get(operator as any)
  console.log(`[getOperatorDisplayName] 直接查询operator=${operator}, 结果:`, user)

  // 如果直接查询失败，尝试转换类型后查询
  if (!user) {
    const userId = typeof operator === 'string' ? parseInt(operator) : operator
    console.log(`[getOperatorDisplayName] 转换后查询userId=${userId}, isNaN=${isNaN(userId)}`)

    if (!isNaN(userId)) {
      user = userMap.value.get(userId)
      console.log(`[getOperatorDisplayName] 转换后查询结果:`, user)
    }
  }

  if (user) {
    // 优先使用真实姓名，其次使用用户名
    const displayName = user.realName || user.username
    console.log(`[getOperatorDisplayName] 返回用户名: ${displayName}`)
    return displayName
  }

  // 如果找不到用户，返回原值
  console.log(`[getOperatorDisplayName] 找不到用户，返回原值: ${operator.toString()}`)
  console.log(`[getOperatorDisplayName] userMap所有keys:`, Array.from(userMap.value.keys()))
  return operator.toString()
}

// 获取审批目标的显示名称（支持多人/部门/角色）
const getApprovalTargetsDisplayName = (approvalTargets: any[]): string => {
  if (!approvalTargets || approvalTargets.length === 0) return '未知'

  const names = approvalTargets.map(target => {
    // 如果有name字段，直接使用
    if (target.name) return target.name

    // 否则根据类型查询
    if (target.type === 'user') {
      return getOperatorDisplayName(target.id)
    } else if (target.type === 'dept') {
      return `部门${target.id}`
    } else if (target.type === 'role') {
      return `角色${target.id}`
    }
    return target.id?.toString() || '未知'
  })

  return names.join('、')
}

// 监听组件可见性
let observer: IntersectionObserver | null = null

// 暴露方法给父组件
defineExpose({
  centerCanvas: () => initCanvasPosition(0)
})

// 生命周期
onMounted(async () => {
  // 加载用户数据
  await loadUsers()

  // 使用 IntersectionObserver 监听组件可见性
  if (diagramContentRef.value) {
    observer = new IntersectionObserver((entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          // 组件变为可见时触发居中
          setTimeout(() => initCanvasPosition(0), 100)
        }
      })
    }, { threshold: 0.1 })

    observer.observe(diagramContentRef.value)
  }

  // 初始化画布位置（滚动到中心）
  setTimeout(() => {
    initCanvasPosition(0)
  }, 200)
})

onUnmounted(() => {
  if (observer) {
    observer.disconnect()
    observer = null
  }
})
</script>

<style scoped lang="scss">
.process-progress-diagram {
  width: 100%;
  background: var(--bg-card);
  border-radius: 0;
  border: none;
  box-shadow: none;
  overflow: hidden;

  &:fullscreen {
    border-radius: 0;
    border: none;
    box-shadow: none;
    height: 100vh;
    background: var(--bg-card);

    .diagram-content {
      height: calc(100vh - 120px) !important;
    }

    .diagram-header {
      padding: 16px 24px;

      .header-info .diagram-title {
        font-size: 18px;
      }

      .diagram-toolbar .el-button {
        font-size: 14px;
      }
    }
  }
}

.diagram-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: var(--bg-card);

  .header-info {
    display: flex;
    align-items: center;
    gap: 24px;

    .diagram-title {
      display: flex;
      align-items: center;
      gap: 8px;
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);

      .el-icon {
        color: var(--el-color-primary);
        font-size: 18px;
      }
    }

    .progress-summary {
      display: flex;
      align-items: center;
      gap: 12px;

      .progress-text {
        font-size: 14px;
        color: var(--text-secondary);
        font-weight: 500;
        white-space: nowrap;
      }
    }
  }

  .diagram-toolbar {
    display: flex;
    gap: 8px;

    .el-button {
      font-size: 12px;
    }
  }

  .diagram-legend {
    display: flex;
    gap: 20px;

    .legend-item {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 12px;
      color: var(--text-secondary);
      font-weight: 500;

      .legend-color {
        width: 12px;
        height: 12px;
        border-radius: 2px;

        &.pending {
          background: var(--border-color);
        }
        &.current {
          background: var(--el-color-primary);
        }
        &.completed {
          background: var(--el-color-success);
        }
        &.rejected {
          background: var(--el-color-danger);
        }
      }
    }
  }
}

.diagram-content {
  height: 600px;
  position: relative;
  overflow: auto;
  background: var(--bg-secondary);
  cursor: grab;

  &.is-dragging {
    cursor: grabbing;
    user-select: none;
  }

  // 滚动条样式
  &::-webkit-scrollbar {
    width: 10px;
    height: 10px;
  }

  &::-webkit-scrollbar-track {
    background: var(--bg-tertiary);
    border-radius: 5px;
  }

  &::-webkit-scrollbar-thumb {
    background: var(--scrollbar-thumb-color);
    border-radius: 5px;
    border: 2px solid var(--bg-tertiary);

    &:hover {
      background: var(--scrollbar-thumb-hover-color);
    }
  }

  &::-webkit-scrollbar-corner {
    background: var(--bg-tertiary);
  }
}

.canvas-content {
  position: relative;
  background: var(--bg-card);
  background-image:
    linear-gradient(rgba(99, 102, 241, 0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(99, 102, 241, 0.05) 1px, transparent 1px);
  background-size: 20px 20px;
}

.progress-node {
  position: absolute;
  width: 180px;
  min-height: 80px;
  background: var(--bg-card);
  border: 2px solid var(--border-color);
  border-radius: 12px;
  box-shadow: var(--shadow-sm);
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;

  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-md);
  }

  // 状态样式
  &.status-pending {
    border-color: var(--border-color);

    .node-header {
      background: linear-gradient(135deg, var(--bg-secondary) 0%, var(--border-light) 100%);
      color: var(--text-primary);
    }
  }

  &.status-current {
    border-color: var(--el-color-primary);
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);

    .node-header {
      background: transparent;
      color: var(--el-color-primary);
      border-left: 4px solid var(--el-color-primary);
    }
  }

  &.status-completed {
    border-color: var(--el-color-success);
    box-shadow: 0 4px 12px rgba(103, 194, 58, 0.3);

    .node-header {
      background: transparent;
      color: var(--el-color-success);
      border-left: 4px solid var(--el-color-success);
    }
  }

  &.status-rejected {
    border-color: var(--el-color-danger);
    box-shadow: 0 4px 12px rgba(245, 108, 108, 0.3);

    .node-header {
      background: transparent;
      color: var(--el-color-danger);
      border-left: 4px solid var(--el-color-danger);
    }
  }
}

.node-header {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid var(--border-color);
  color: var(--text-primary);

  .node-icon-wrapper {
    position: relative;
    margin-right: 12px;

    .node-icon {
      font-size: 18px;
    }

    .status-indicator {
      position: absolute;
      top: -6px;
      right: -6px;
      width: 16px;
      height: 16px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;

      .status-icon {
        font-size: 10px;

        &.completed {
          color: white;
          background: #67c23a;
          border-radius: 50%;
          padding: 2px;
        }

        &.rejected {
          color: white;
          background: #f56c6c;
          border-radius: 50%;
          padding: 2px;
        }

        &.current {
          color: #409eff;
          animation: spin 1s linear infinite;
        }

        &.pending {
          color: #909399;
        }
      }
    }
  }

  .node-info {
    flex: 1;
    min-width: 0;

    .node-title {
      display: block;
      font-weight: 600;
      font-size: 13px;
      line-height: 1.2;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
      margin-bottom: 2px;
    }

    .node-id {
      display: block;
      font-size: 10px;
      opacity: 0.8;
      font-family: 'Monaco', 'Consolas', monospace;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .node-assignee {
      display: block;
      font-size: 11px;
      color: #409eff;
      font-weight: 500;
      margin-top: 2px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}

.execution-info {
  padding: 8px 16px;
  background: var(--bg-tertiary);

  .execution-row {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 4px;
    font-size: 11px;
    color: var(--text-secondary);

    &:last-child {
      margin-bottom: 0;
    }

    .el-icon {
      font-size: 12px;
      color: var(--text-muted);
    }

    span {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}

.assignee-info {
  padding: 8px 16px;
  background: var(--primary-light);
  border-top: 1px solid var(--border-light);

  .assignee-row {
    display: flex;
    align-items: center;
    gap: 6px;
    font-size: 11px;
    color: var(--el-color-primary);
    font-weight: 500;

    .el-icon {
      font-size: 12px;
      color: var(--el-color-primary);
    }

    span {
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }
}

.node-status-badge {
  position: absolute;
  top: 8px;
  right: 8px;

  .status-tag {
    font-size: 10px;
    padding: 2px 6px;
    border-radius: 10px;
    font-weight: 600;
  }
}

.flows-svg {
  position: absolute;
  top: 0;
  left: 0;
  pointer-events: none;
  z-index: 1;

  .flow-line {
    stroke-linecap: round;
    transition: all 0.3s ease;

    &.completed {
      stroke-dasharray: none;
    }

    &.active {
      stroke-dasharray: 8,4;
      animation: flow-animation 1s linear infinite;
    }

    &.pending {
      opacity: 0.4;
    }
  }
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes flow-animation {
  from { stroke-dashoffset: 12; }
  to { stroke-dashoffset: 0; }
}

// 节点详情弹窗样式
.node-detail-content {
  .detail-section {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: var(--text-primary);
      font-size: 16px;
      font-weight: 600;
      border-bottom: 1px solid var(--border-color);
      padding-bottom: 8px;
      display: flex;
      align-items: center;
    }
  }

  .approval-records {
    .approval-record-item {
      margin-bottom: 16px;
      padding: 12px;
      background: var(--bg-tertiary);
      border-radius: 8px;
      border: 1px solid var(--border-color);

      &:last-child {
        margin-bottom: 0;
      }
    }
  }

  .comment-content {
    max-height: 100px;
    overflow-y: auto;
    white-space: pre-wrap;
    word-break: break-all;
    color: var(--text-secondary);
    line-height: 1.4;
    background: var(--bg-secondary);
    padding: 8px 12px;
    border-radius: 6px;
    border-left: 3px solid var(--el-color-primary);
  }

  .no-execution-info {
    text-align: center;
    padding: 40px 0;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .diagram-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;

    .header-info {
      flex-direction: column;
      align-items: flex-start;
      gap: 12px;
    }

    .diagram-legend {
      flex-wrap: wrap;
      gap: 12px;
    }
  }

  .progress-node {
    width: 160px;

    .node-header {
      padding: 10px 12px;

      .node-info .node-title {
        font-size: 12px;
      }
    }
  }
}
</style>
