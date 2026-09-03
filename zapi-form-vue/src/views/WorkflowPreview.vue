<template>
  <div class="workflow-preview">
    <el-container>
      <!-- 预览工具栏 -->
      <el-header class="preview-toolbar">
        <div class="toolbar-left">
          <el-button :icon="ArrowLeft" @click="goBack">返回设计器</el-button>
        </div>
        <div class="toolbar-center">
          <h2>{{ workflowData.name || '流程预览' }}</h2>
        </div>
        <div class="toolbar-right">
          <el-button-group>
            <el-button :icon="Refresh" @click="refreshPreview">刷新</el-button>
            <el-button :icon="FullScreen" @click="toggleFullscreen">{{ isFullscreen ? '退出全屏' : '全屏' }}</el-button>
            <el-button type="primary" :icon="Play" @click="runSimulation">运行模拟</el-button>
          </el-button-group>
        </div>
      </el-header>

      <el-main class="preview-content">
        <!-- 流程图预览区域 -->
        <div ref="previewCanvasRef" class="preview-canvas" :class="{ 'fullscreen': isFullscreen }">
          <!-- 网格背景 -->
          <div class="grid-background"></div>
          
          <!-- 空状态提示 -->
          <div v-if="!workflowData.nodes.length" class="empty-state">
            <el-empty 
              description="暂无流程数据"
              :image-size="120"
            >
              <template #description>
                <p>请先在流程设计器中创建流程</p>
              </template>
              <el-button type="primary" @click="goBack">返回设计器</el-button>
            </el-empty>
          </div>
          
          <!-- 流程节点 -->
          <div
            v-for="node in workflowData.nodes"
            :key="node.id"
            class="preview-node"
            :class="[`node-${node.type}`, { 'active': activeNodes.includes(node.id), 'completed': completedNodes.includes(node.id) }]"
            :style="{ left: (node.x || 0) + 'px', top: (node.y || 0) + 'px' }"
          >
            <div class="node-header">
              <el-icon><component :is="getNodeIcon(node.type)" /></el-icon>
              <span class="node-title">{{ node.title || node.type }}</span>
              <div v-if="activeNodes.includes(node.id)" class="node-status running">
                <el-icon class="rotating"><Loading /></el-icon>
              </div>
              <div v-if="completedNodes.includes(node.id)" class="node-status completed">
                <el-icon><Check /></el-icon>
              </div>
            </div>
            <div class="node-content">
              {{ node.description || '暂无描述' }}
            </div>
            
            <!-- 节点执行信息 -->
            <div v-if="nodeExecutionInfo[node.id]" class="execution-info">
              <el-tag size="small" :type="getExecutionTagType(nodeExecutionInfo[node.id].status)">
                {{ nodeExecutionInfo[node.id].status }}
              </el-tag>
              <div class="execution-time">
                {{ formatExecutionTime(nodeExecutionInfo[node.id].timestamp) }}
              </div>
            </div>
          </div>

          <!-- 连接线 -->
          <svg class="connections-svg">
            <defs>
              <marker id="arrowhead-preview" markerWidth="10" markerHeight="7" 
                      refX="9" refY="3.5" orient="auto">
                <polygon points="0 0, 10 3.5, 0 7" fill="#409eff" />
              </marker>
              <marker id="arrowhead-active" markerWidth="10" markerHeight="7" 
                      refX="9" refY="3.5" orient="auto">
                <polygon points="0 0, 10 3.5, 0 7" fill="#67c23a" />
              </marker>
            </defs>
            <path
              v-for="connection in workflowData.connections"
              :key="connection.id"
              :d="getConnectionPath(connection)"
              :stroke="getConnectionColor(connection)"
              stroke-width="2"
              fill="none"
              :marker-end="getConnectionMarker(connection)"
              class="connection-line"
              :class="{ 'active': activeConnections.includes(connection.id) }"
            />
          </svg>

          <!-- 数据流动动画 -->
          <div
            v-for="flow in dataFlows"
            :key="flow.id"
            class="data-flow"
            :style="getDataFlowStyle(flow)"
          >
            <div class="flow-dot"></div>
          </div>
        </div>

        <!-- 执行日志面板 -->
        <div class="execution-log" v-if="showExecutionLog">
          <div class="log-header">
            <h4>执行日志</h4>
            <el-button size="small" @click="clearLog">清空日志</el-button>
          </div>
          <div class="log-content">
            <div
              v-for="log in executionLogs"
              :key="log.id"
              class="log-item"
              :class="log.type"
            >
              <span class="log-time">{{ formatTime(log.timestamp) }}</span>
              <span class="log-message">{{ log.message }}</span>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>

    <!-- 模拟执行对话框 -->
    <el-dialog
      v-model="simulationDialogVisible"
      title="流程模拟执行"
      width="600px"
    >
      <div class="simulation-config">
        <el-form :model="simulationConfig" label-width="120px">
          <el-form-item label="执行速度">
            <el-slider
              v-model="simulationConfig.speed"
              :min="1"
              :max="10"
              :step="1"
              show-stops
              show-tooltip
            />
            <span class="speed-label">{{ getSpeedLabel(simulationConfig.speed) }}</span>
          </el-form-item>
          <el-form-item label="模拟数据">
            <el-input
              v-model="simulationConfig.inputData"
              type="textarea"
              :rows="4"
              placeholder="请输入JSON格式的模拟数据"
            />
          </el-form-item>
          <el-form-item label="执行模式">
            <el-radio-group v-model="simulationConfig.mode">
              <el-radio label="step">逐步执行</el-radio>
              <el-radio label="auto">自动执行</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="simulationDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="startSimulation">开始模拟</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useWorkflowDesignerStore } from '@/stores/workflowDesigner'
import { 
  ArrowLeft, Refresh, FullScreen, VideoPlay as Play, Check, Loading,
  User, Setting, Timer, Share, Document, VideoPlay, VideoPause, Connection
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const store = useWorkflowDesignerStore()

// 响应式数据
const previewCanvasRef = ref<HTMLElement>()
const isFullscreen = ref(false)
const showExecutionLog = ref(true)
const simulationDialogVisible = ref(false)
const activeNodes = ref<string[]>([])
const completedNodes = ref<string[]>([])
const activeConnections = ref<string[]>([])
const dataFlows = ref<any[]>([])
const executionLogs = ref<any[]>([])
const nodeExecutionInfo = ref<Record<string, any>>({})

const simulationConfig = reactive({
  speed: 5,
  inputData: '{\n  "amount": 1500,\n  "applicant": "张三",\n  "department": "销售部"\n}',
  mode: 'auto'
})

// 计算属性
const workflowData = computed(() => {
  // 首先尝试从store获取数据
  if (store.currentWorkflow && store.currentWorkflow.nodes.length > 0) {
    return store.currentWorkflow
  }
  
  // 如果store中没有数据，尝试从localStorage获取
  try {
    const tempData = localStorage.getItem('tempWorkflowPreview')
    if (tempData) {
      const parsed = JSON.parse(tempData)
      console.log('从localStorage加载预览数据:', parsed)
      return parsed
    }
  } catch (error) {
    console.error('解析localStorage数据失败:', error)
  }
  
  // 返回空数据
  return { name: '', nodes: [], connections: [] }
})

// 方法
const getNodeIcon = (type: string) => {
  const iconMap: Record<string, any> = {
    start: VideoPlay,
    end: VideoPause,
    task: Document,
    approval: User,
    condition: Share,
    parallel: Connection,
    timer: Timer,
    script: Setting
  }
  return iconMap[type] || Document
}

const getConnectionPath = (connection: any) => {
  // 根据节点位置计算连接线路径
  const fromNode = workflowData.value.nodes.find(n => n.id === connection.fromNodeId)
  const toNode = workflowData.value.nodes.find(n => n.id === connection.toNodeId)
  
  if (!fromNode || !toNode) return ''
  
  const startX = fromNode.x + 120 // 节点宽度
  const startY = fromNode.y + 30  // 节点高度的一半
  const endX = toNode.x
  const endY = toNode.y + 30
  
  return `M ${startX} ${startY} L ${endX} ${endY}`
}

const getConnectionColor = (connection: any) => {
  return activeConnections.value.includes(connection.id) ? '#67c23a' : '#409eff'
}

const getConnectionMarker = (connection: any) => {
  return activeConnections.value.includes(connection.id) ? 'url(#arrowhead-active)' : 'url(#arrowhead-preview)'
}

const getExecutionTagType = (status: string) => {
  const typeMap: Record<string, string> = {
    'pending': 'info',
    'running': 'warning',
    'completed': 'success',
    'failed': 'danger'
  }
  return typeMap[status] || 'info'
}

const formatExecutionTime = (timestamp: number) => {
  return new Date(timestamp).toLocaleTimeString()
}

const formatTime = (timestamp: number) => {
  return new Date(timestamp).toLocaleTimeString()
}

const getSpeedLabel = (speed: number) => {
  const labels = ['很慢', '慢', '较慢', '正常', '较快', '快', '很快', '极快', '闪电', '瞬间']
  return labels[speed - 1] || '正常'
}

const getDataFlowStyle = (flow: any) => {
  return {
    left: flow.x + 'px',
    top: flow.y + 'px',
    transform: `translate(${flow.offsetX}px, ${flow.offsetY}px)`
  }
}

const goBack = () => {
  // 确保数据保存到store中
  if (workflowData.value.nodes.length > 0) {
    store.setWorkflow(workflowData.value)
  }
  router.push('/workflow-designer')
}

const refreshPreview = () => {
  // 重置状态
  activeNodes.value = []
  completedNodes.value = []
  activeConnections.value = []
  dataFlows.value = []
  executionLogs.value = []
  nodeExecutionInfo.value = {}
  
  ElMessage.success('预览已刷新')
}

const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value
  nextTick(() => {
    if (isFullscreen.value) {
      document.documentElement.requestFullscreen?.()
    } else {
      document.exitFullscreen?.()
    }
  })
}

const runSimulation = () => {
  simulationDialogVisible.value = true
}

const startSimulation = async () => {
  simulationDialogVisible.value = false
  
  // 重置状态
  refreshPreview()
  
  addLog('info', '开始流程模拟执行')
  
  try {
    const inputData = JSON.parse(simulationConfig.inputData)
    addLog('success', `输入数据: ${JSON.stringify(inputData)}`)
  } catch (error) {
    addLog('error', '输入数据格式错误，使用默认数据')
  }
  
  // 模拟执行流程
  await simulateWorkflowExecution()
}

const simulateWorkflowExecution = async () => {
  const nodes = workflowData.value.nodes
  const startNode = nodes.find(node => node.type === 'start')
  
  if (!startNode) {
    addLog('error', '未找到开始节点')
    return
  }
  
  // 执行节点
  await executeNode(startNode)
}

const executeNode = async (node: any) => {
  // 标记节点为活跃状态
  activeNodes.value.push(node.id)
  nodeExecutionInfo.value[node.id] = {
    status: 'running',
    timestamp: Date.now()
  }
  
  addLog('info', `执行节点: ${node.title || node.type}`)
  
  // 模拟执行时间
  const executionTime = (11 - simulationConfig.speed) * 200 // 200ms 到 2000ms
  await new Promise(resolve => setTimeout(resolve, executionTime))
  
  // 标记节点为完成状态
  activeNodes.value = activeNodes.value.filter(id => id !== node.id)
  completedNodes.value.push(node.id)
  nodeExecutionInfo.value[node.id] = {
    status: 'completed',
    timestamp: Date.now()
  }
  
  addLog('success', `节点执行完成: ${node.title || node.type}`)
  
  // 查找下一个节点
  const nextNodes = findNextNodes(node.id)
  for (const nextNode of nextNodes) {
    await executeNode(nextNode)
  }
}

const findNextNodes = (nodeId: string) => {
  const connections = workflowData.value.connections.filter(conn => conn.fromNodeId === nodeId)
  return connections.map(conn => 
    workflowData.value.nodes.find(node => node.id === conn.toNodeId)
  ).filter(Boolean)
}

const addLog = (type: string, message: string) => {
  executionLogs.value.push({
    id: Date.now(),
    type,
    message,
    timestamp: Date.now()
  })
}

const clearLog = () => {
  executionLogs.value = []
}

// 初始化
onMounted(async () => {
  console.log('预览页面初始化开始')
  
  // 初始化store
  await store.init()
  
  console.log('预览页面初始化 - store数据:', store.currentWorkflow)
  console.log('预览页面初始化 - workflowData:', workflowData.value)
  
  if (!workflowData.value.nodes.length) {
    ElMessage.warning('暂无流程数据，请先在设计器中创建流程')
    console.log('警告：没有找到流程节点数据')
  } else {
    console.log(`成功加载流程数据，包含 ${workflowData.value.nodes.length} 个节点，${workflowData.value.connections.length} 个连接`)
  }
})
</script>

<style scoped>
.workflow-preview {
  height: 100vh;
  background: #f5f5f5;
}

.preview-toolbar {
  background: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar-center h2 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.preview-content {
  position: relative;
  padding: 0;
  display: flex;
  flex-direction: column;
}

.preview-canvas {
  position: relative;
  flex: 1;
  background: #f9f9f9;
  overflow: auto;
  min-height: 600px;
}

.preview-canvas.fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 9999;
  background: white;
}

.grid-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(to right, #e0e0e0 1px, transparent 1px),
    linear-gradient(to bottom, #e0e0e0 1px, transparent 1px);
  background-size: 20px 20px;
  opacity: 0.3;
}

.empty-state {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  z-index: 10;
}

.preview-node {
  position: absolute;
  width: 120px;
  min-height: 60px;
  background: white;
  border: 2px solid #e6e6e6;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.preview-node.active {
  border-color: #e6a23c;
  box-shadow: 0 0 20px rgba(230, 162, 60, 0.3);
  transform: scale(1.05);
}

.preview-node.completed {
  border-color: #67c23a;
  box-shadow: 0 0 15px rgba(103, 194, 58, 0.2);
}

.node-header {
  position: relative;
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background: #f8f9fa;
  border-bottom: 1px solid #e6e6e6;
  border-radius: 6px 6px 0 0;
}

.node-header .el-icon {
  margin-right: 6px;
  color: #409eff;
}

.node-title {
  font-size: 12px;
  font-weight: 500;
  color: #333;
  flex: 1;
}

.node-status {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
}

.node-status.running .el-icon {
  color: #e6a23c;
}

.node-status.completed .el-icon {
  color: #67c23a;
}

.rotating {
  animation: rotate 1s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.node-content {
  padding: 8px 12px;
  font-size: 11px;
  color: #666;
  line-height: 1.4;
}

.execution-info {
  padding: 4px 12px;
  border-top: 1px solid #f0f0f0;
  background: #fafafa;
}

.execution-time {
  font-size: 10px;
  color: #999;
  margin-top: 2px;
}

.connections-svg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.connection-line.active {
  stroke-width: 3;
  animation: flow 2s linear infinite;
}

@keyframes flow {
  0% { stroke-dasharray: 0 10; }
  100% { stroke-dasharray: 10 0; }
}

.data-flow {
  position: absolute;
  pointer-events: none;
}

.flow-dot {
  width: 8px;
  height: 8px;
  background: #67c23a;
  border-radius: 50%;
  animation: flowMove 2s ease-in-out infinite;
}

@keyframes flowMove {
  0% { opacity: 0; transform: scale(0); }
  50% { opacity: 1; transform: scale(1); }
  100% { opacity: 0; transform: scale(0); }
}

.execution-log {
  height: 200px;
  background: white;
  border-top: 1px solid #e6e6e6;
  display: flex;
  flex-direction: column;
}

.log-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px;
  border-bottom: 1px solid #e6e6e6;
  background: #f8f9fa;
}

.log-header h4 {
  margin: 0;
  font-size: 14px;
  color: #333;
}

.log-content {
  flex: 1;
  overflow-y: auto;
  padding: 10px 15px;
}

.log-item {
  display: flex;
  margin-bottom: 8px;
  font-size: 12px;
  line-height: 1.4;
}

.log-time {
  color: #999;
  margin-right: 10px;
  min-width: 80px;
}

.log-message {
  flex: 1;
}

.log-item.info .log-message {
  color: #333;
}

.log-item.success .log-message {
  color: #67c23a;
}

.log-item.error .log-message {
  color: #f56c6c;
}

.simulation-config {
  padding: 20px;
}

.speed-label {
  margin-left: 10px;
  font-size: 12px;
  color: #666;
}

/* 节点类型样式 */
.node-start .node-header {
  background: linear-gradient(135deg, #67c23a, #85ce61);
  color: white;
}

.node-end .node-header {
  background: linear-gradient(135deg, #f56c6c, #f78989);
  color: white;
}

.node-approval .node-header {
  background: linear-gradient(135deg, #409eff, #66b1ff);
  color: white;
}

.node-condition .node-header {
  background: linear-gradient(135deg, #e6a23c, #ebb563);
  color: white;
}
</style>
