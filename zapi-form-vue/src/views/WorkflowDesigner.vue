<template>
  <div class="workflow-designer">
    <el-container>
      <!-- 工具栏 -->
      <el-header class="toolbar">
        <div class="toolbar-left">
          <el-button-group>
            <el-tooltip content="添加新节点" placement="bottom">
              <el-button type="primary" :icon="Plus" @click="addNode">添加节点</el-button>
            </el-tooltip>
            <el-tooltip content="从右侧输出点拖拽到左侧输入点创建连线" placement="bottom">
              <el-button :icon="Connection" @click="addConnection">连线帮助</el-button>
            </el-tooltip>
            <el-tooltip content="删除选中的节点或连线" placement="bottom">
              <el-button :icon="Delete" @click="deleteSelected">删除</el-button>
            </el-tooltip>
          </el-button-group>
        </div>
        <div class="toolbar-center">
          <el-input
            v-model="workflowName"
            placeholder="请输入流程名称"
            style="width: 300px"
            clearable
          />
        </div>
        <div class="toolbar-right">
          <el-button-group>
            <el-tooltip content="预览流程图" placement="bottom">
              <el-button :icon="View" @click="preview">预览</el-button>
            </el-tooltip>
            <el-tooltip content="保存流程" placement="bottom">
              <el-button type="success" :icon="Check" @click="save">保存</el-button>
            </el-tooltip>
            <el-tooltip content="导出为JSON文件" placement="bottom">
              <el-button :icon="Download" @click="exportWorkflow">导出</el-button>
            </el-tooltip>
          </el-button-group>
        </div>
      </el-header>

      <el-container>
        <!-- 左侧组件面板 -->
        <el-aside width="250px" class="components-panel">
          <div class="panel-header">
            <h3>流程组件</h3>
            <el-tooltip content="查看使用说明" placement="right">
              <el-button 
                size="small" 
                text 
                :icon="QuestionFilled" 
                @click="showHelpDialog = true"
              />
            </el-tooltip>
          </div>
          <div class="component-list">
            <div class="component-category">
              <h4>基础节点</h4>
              <div 
                v-for="component in basicComponents" 
                :key="component.type"
                class="component-item"
                draggable="true"
                @dragstart="onDragStart($event, component)"
              >
                <el-icon><component :is="component.icon" /></el-icon>
                <span>{{ component.label }}</span>
              </div>
            </div>
            
            <div class="component-category">
              <h4>控制节点</h4>
              <div 
                v-for="component in controlComponents" 
                :key="component.type"
                class="component-item"
                draggable="true"
                @dragstart="onDragStart($event, component)"
              >
                <el-icon><component :is="component.icon" /></el-icon>
                <span>{{ component.label }}</span>
              </div>
            </div>
          </div>
        </el-aside>

        <!-- 中间设计画布 -->
        <el-main class="design-canvas-container">
          <!-- 连线状态提示 -->
          <div v-if="connectionState.isConnecting" class="connection-hint">
            <el-alert
              title="连线模式"
              description="请将鼠标拖拽到目标节点的输入连接点（左侧绿色圆点）完成连接"
              type="success"
              :closable="false"
              show-icon
            />
          </div>
          
          <div 
            ref="canvasRef"
            class="design-canvas"
            :class="{ 'connecting': connectionState.isConnecting }"
            @drop="onDrop"
            @dragover="onDragOver"
            @click="onCanvasClick"
          >
            <!-- 网格背景 -->
            <div class="grid-background"></div>
            
            <!-- 流程节点 -->
            <div
              v-for="node in nodes"
              :key="node.id"
              class="workflow-node"
              :class="{ 'selected': selectedNode === node.id, [`node-${node.type}`]: true }"
              :style="{ left: node.x + 'px', top: node.y + 'px' }"
              @click.stop="selectNode(node.id)"
              @mousedown="startDrag($event, node)"
            >
              <div class="node-header">
                <el-icon><component :is="getNodeIcon(node.type)" /></el-icon>
                <span class="node-title">{{ node.title || node.type }}</span>
              </div>
              <div class="node-content">
                {{ node.description || '点击配置节点属性' }}
              </div>
              <!-- 连接点 -->
              <div 
                class="connection-point input" 
                :class="{ 'highlight': connectionState.targetHighlight === `${node.id}-input` }"
                @mousedown.stop="startConnection($event, node, 'input')"
                @mouseenter="highlightConnectionPoint(`${node.id}-input`)"
                @mouseleave="clearHighlight()"
              ></div>
              <div 
                class="connection-point output" 
                :class="{ 'highlight': connectionState.targetHighlight === `${node.id}-output` }"
                @mousedown.stop="startConnection($event, node, 'output')"
                @mouseenter="highlightConnectionPoint(`${node.id}-output`)"
                @mouseleave="clearHighlight()"
              ></div>
            </div>

            <!-- 连接线 -->
            <svg class="connections-svg">
              <defs>
                <marker id="arrowhead" markerWidth="10" markerHeight="7" 
                        refX="9" refY="3.5" orient="auto">
                  <polygon points="0 0, 10 3.5, 0 7" fill="#409eff" />
                </marker>
                <marker id="arrowhead-preview" markerWidth="10" markerHeight="7" 
                        refX="9" refY="3.5" orient="auto">
                  <polygon points="0 0, 10 3.5, 0 7" fill="#67c23a" />
                </marker>
              </defs>
              <!-- 已有连接线 -->
              <path
                v-for="connection in connections"
                :key="connection.id"
                :d="getConnectionPath(connection)"
                stroke="#409eff"
                stroke-width="2"
                fill="none"
                marker-end="url(#arrowhead)"
                class="connection-line"
                @click="selectConnection(connection.id)"
              />
              <!-- 临时连接线预览 -->
              <path
                v-if="connectionState.isConnecting && connectionState.previewPath"
                :d="connectionState.previewPath"
                stroke="#67c23a"
                stroke-width="2"
                fill="none"
                stroke-dasharray="5,5"
                marker-end="url(#arrowhead-preview)"
                class="connection-preview"
              />
            </svg>
          </div>
        </el-main>

        <!-- 右侧属性面板 -->
        <el-aside width="300px" class="properties-panel">
          <div class="panel-header">
            <h3>属性配置</h3>
          </div>
          <div class="properties-content">
            <div v-if="selectedNode" class="node-properties">
              <el-form :model="selectedNodeData" label-width="80px" size="small">
                <el-form-item label="节点标题">
                  <el-input v-model="selectedNodeData.title" />
                </el-form-item>
                <el-form-item label="节点描述">
                  <el-input v-model="selectedNodeData.description" type="textarea" :rows="3" />
                </el-form-item>
                <el-form-item label="节点类型">
                  <el-tag>{{ selectedNodeData.type }}</el-tag>
                </el-form-item>
                
                <!-- 根据节点类型显示不同的配置项 -->
                <template v-if="selectedNodeData.type === 'approval'">
                  <el-form-item label="审批人">
                    <el-select v-model="selectedNodeData.approver" placeholder="请选择审批人">
                      <el-option label="管理员" value="admin" />
                      <el-option label="部门经理" value="manager" />
                      <el-option label="HR" value="hr" />
                    </el-select>
                  </el-form-item>
                </template>
                
                <template v-if="selectedNodeData.type === 'condition'">
                  <el-form-item label="条件表达式">
                    <el-input v-model="selectedNodeData.condition" placeholder="如: amount > 1000" />
                  </el-form-item>
                </template>
              </el-form>
            </div>
            
            <div v-else-if="selectedConnection" class="connection-properties">
              <el-form :model="selectedConnectionData" label-width="80px" size="small">
                <el-form-item label="连线标签">
                  <el-input v-model="selectedConnectionData.label" />
                </el-form-item>
                <el-form-item label="条件">
                  <el-input v-model="selectedConnectionData.condition" />
                </el-form-item>
              </el-form>
            </div>
            
            <div v-else class="no-selection">
              <el-empty description="请选择节点或连线进行配置" />
            </div>
          </div>
        </el-aside>
      </el-container>
    </el-container>

    <!-- 帮助对话框 -->
    <el-dialog
      v-model="showHelpDialog"
      title="流程设计器使用说明"
      width="600px"
    >
      <div class="help-content">
        <el-steps direction="vertical" :active="4">
          <el-step title="添加节点" description="从左侧组件面板拖拽节点到画布，或点击工具栏的'添加节点'按钮" />
          <el-step title="连接节点" description="按住鼠标左键从节点右侧的蓝色圆点拖拽到目标节点左侧的蓝色圆点" />
          <el-step title="配置属性" description="点击节点选中后，在右侧属性面板中配置节点的详细属性" />
          <el-step title="保存预览" description="完成设计后，点击保存按钮保存流程，点击预览按钮查看效果" />
        </el-steps>
        
        <el-divider />
        
        <div class="shortcut-keys">
          <h4>快捷键</h4>
          <ul>
            <li><kbd>Delete</kbd> / <kbd>Backspace</kbd> - 删除选中的节点或连线</li>
            <li><kbd>Escape</kbd> - 取消当前连线操作</li>
            <li><kbd>Ctrl + S</kbd> - 保存流程</li>
          </ul>
        </div>
        
        <el-divider />
        
        <div class="tips">
          <h4>使用技巧</h4>
          <ul>
            <li>连线时，目标连接点会高亮显示绿色并有脉冲动画</li>
            <li>拖拽节点时可以精确定位，支持网格对齐</li>
            <li>连接线采用贝塞尔曲线，视觉效果更美观</li>
            <li>支持多种节点类型，满足不同的业务需求</li>
          </ul>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="showHelpDialog = false">知道了</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useWorkflowDesignerStore } from '@/stores/workflowDesigner'
import { 
  Plus, Connection, Delete, View, Check, Download,
  User, Setting, Timer, Share, Document, QuestionFilled,
  VideoPlay, VideoPause
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const store = useWorkflowDesignerStore()

// 响应式数据
const workflowName = ref('新建流程')
const canvasRef = ref<HTMLElement>()
const selectedNode = ref<string | null>(null)
const selectedConnection = ref<string | null>(null)
const draggedComponent = ref<any>(null)
const isDragging = ref(false)
const dragOffset = ref({ x: 0, y: 0 })
const showHelpDialog = ref(false)

// 连接状态管理
const connectionState = reactive({
  isConnecting: false,
  sourceNode: null as any,
  sourcePort: '' as string,
  currentMousePos: { x: 0, y: 0 },
  previewPath: '' as string,
  targetHighlight: '' as string
})

// 组件定义
const basicComponents = [
  { type: 'start', label: '开始', icon: 'VideoPlay' },
  { type: 'end', label: '结束', icon: 'VideoPause' },
  { type: 'task', label: '任务', icon: 'Document' },
  { type: 'approval', label: '审批', icon: 'User' },
]

const controlComponents = [
  { type: 'condition', label: '条件判断', icon: 'Share' },
  { type: 'parallel', label: '并行', icon: 'Connection' },
  { type: 'timer', label: '定时器', icon: 'Timer' },
  { type: 'script', label: '脚本', icon: 'Setting' },
]

// 节点和连接数据
const nodes = ref<any[]>([])
const connections = ref<any[]>([])

// 计算属性
const selectedNodeData = computed(() => {
  if (!selectedNode.value) return null
  return nodes.value.find(node => node.id === selectedNode.value) || null
})

const selectedConnectionData = computed(() => {
  if (!selectedConnection.value) return null
  return connections.value.find(conn => conn.id === selectedConnection.value) || null
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

const onDragStart = (event: DragEvent, component: any) => {
  draggedComponent.value = component
  if (event.dataTransfer) {
    event.dataTransfer.effectAllowed = 'copy'
  }
}

const onDragOver = (event: DragEvent) => {
  event.preventDefault()
  if (event.dataTransfer) {
    event.dataTransfer.dropEffect = 'copy'
  }
}

const onDrop = (event: DragEvent) => {
  event.preventDefault()
  if (!draggedComponent.value || !canvasRef.value) return

  const rect = canvasRef.value.getBoundingClientRect()
  const x = event.clientX - rect.left
  const y = event.clientY - rect.top

  addNodeToCanvas(draggedComponent.value, x, y)
  draggedComponent.value = null
}

const addNodeToCanvas = (component: any, x: number, y: number) => {
  const newNode = {
    id: `node_${Date.now()}`,
    type: component.type,
    title: component.label,
    description: '',
    x: x - 50, // 居中偏移
    y: y - 25,
    width: 100,
    height: 50
  }
  
  nodes.value.push(newNode)
}

const addNode = () => {
  const component = basicComponents[0] // 默认添加开始节点
  addNodeToCanvas(component, 200, 100)
}

const selectNode = (nodeId: string) => {
  selectedNode.value = nodeId
  selectedConnection.value = null
}

const selectConnection = (connectionId: string) => {
  selectedConnection.value = connectionId
  selectedNode.value = null
}

const onCanvasClick = () => {
  selectedNode.value = null
  selectedConnection.value = null
}

const startDrag = (event: MouseEvent, node: any) => {
  if (event.button !== 0) return // 只处理左键
  
  isDragging.value = true
  dragOffset.value = {
    x: event.clientX - node.x,
    y: event.clientY - node.y
  }
  
  const onMouseMove = (e: MouseEvent) => {
    if (!isDragging.value) return
    node.x = e.clientX - dragOffset.value.x
    node.y = e.clientY - dragOffset.value.y
  }
  
  const onMouseUp = () => {
    isDragging.value = false
    document.removeEventListener('mousemove', onMouseMove)
    document.removeEventListener('mouseup', onMouseUp)
  }
  
  document.addEventListener('mousemove', onMouseMove)
  document.addEventListener('mouseup', onMouseUp)
}

// 连接相关方法
const startConnection = (event: MouseEvent, node: any, port: 'input' | 'output') => {
  event.stopPropagation()
  
  // 输入端口不能作为起始点
  if (port === 'input') {
    return
  }
  
  connectionState.isConnecting = true
  connectionState.sourceNode = node
  connectionState.sourcePort = port
  connectionState.currentMousePos = { x: event.clientX, y: event.clientY }
  
  // 添加全局鼠标事件监听
  document.addEventListener('mousemove', onConnectionMouseMove)
  document.addEventListener('mouseup', onConnectionMouseUp)
  
  // 更新预览路径
  updateConnectionPreview(event.clientX, event.clientY)
}

const onConnectionMouseMove = (event: MouseEvent) => {
  if (!connectionState.isConnecting) return
  
  connectionState.currentMousePos = { x: event.clientX, y: event.clientY }
  updateConnectionPreview(event.clientX, event.clientY)
}

const onConnectionMouseUp = (event: MouseEvent) => {
  if (!connectionState.isConnecting) return
  
  // 检查是否在目标连接点上
  const targetElement = event.target as HTMLElement
  if (targetElement && targetElement.classList.contains('connection-point') && 
      targetElement.classList.contains('input')) {
    
    // 找到目标节点
    const targetNodeElement = targetElement.closest('.workflow-node')
    if (targetNodeElement) {
      const targetNodeId = getNodeIdFromElement(targetNodeElement)
      const targetNode = nodes.value.find(n => n.id === targetNodeId)
      
      if (targetNode && targetNode.id !== connectionState.sourceNode.id) {
        createConnection(connectionState.sourceNode, targetNode)
      }
    }
  }
  
  // 清理连接状态
  endConnection()
}

const createConnection = (fromNode: any, toNode: any) => {
  // 检查是否已存在连接
  const existingConnection = connections.value.find(
    conn => conn.fromNodeId === fromNode.id && conn.toNodeId === toNode.id
  )
  
  if (existingConnection) {
    ElMessage.warning('节点间已存在连接')
    return
  }
  
  const newConnection = {
    id: `connection_${Date.now()}`,
    fromNodeId: fromNode.id,
    toNodeId: toNode.id,
    label: '',
    condition: ''
  }
  
  connections.value.push(newConnection)
  ElMessage.success('连接创建成功')
}

const endConnection = () => {
  connectionState.isConnecting = false
  connectionState.sourceNode = null
  connectionState.sourcePort = ''
  connectionState.previewPath = ''
  connectionState.targetHighlight = ''
  
  document.removeEventListener('mousemove', onConnectionMouseMove)
  document.removeEventListener('mouseup', onConnectionMouseUp)
}

const updateConnectionPreview = (mouseX: number, mouseY: number) => {
  if (!connectionState.sourceNode || !canvasRef.value) return
  
  const canvasRect = canvasRef.value.getBoundingClientRect()
  const sourceNode = connectionState.sourceNode
  
  // 计算起始点坐标（输出连接点）
  const startX = sourceNode.x + 120 // 节点宽度
  const startY = sourceNode.y + 30  // 节点高度的一半
  
  // 计算鼠标在画布中的相对坐标
  const endX = mouseX - canvasRect.left
  const endY = mouseY - canvasRect.top
  
  // 创建贝塞尔曲线路径
  const controlPointOffset = Math.min(Math.abs(endX - startX) / 2, 100)
  const path = `M ${startX} ${startY} C ${startX + controlPointOffset} ${startY}, ${endX - controlPointOffset} ${endY}, ${endX} ${endY}`
  
  connectionState.previewPath = path
}

const highlightConnectionPoint = (pointId: string) => {
  if (connectionState.isConnecting) {
    connectionState.targetHighlight = pointId
  }
}

const clearHighlight = () => {
  connectionState.targetHighlight = ''
}

const getNodeIdFromElement = (element: Element): string => {
  const nodeElements = Array.from(canvasRef.value?.querySelectorAll('.workflow-node') || [])
  const index = nodeElements.indexOf(element)
  return index >= 0 ? nodes.value[index]?.id || '' : ''
}

const addConnection = () => {
  ElMessage.info('请从输出连接点（右侧圆点）拖拽到目标节点的输入连接点（左侧圆点）')
}

const deleteSelected = () => {
  if (selectedNode.value) {
    nodes.value = nodes.value.filter(node => node.id !== selectedNode.value)
    selectedNode.value = null
  } else if (selectedConnection.value) {
    connections.value = connections.value.filter(conn => conn.id !== selectedConnection.value)
    selectedConnection.value = null
  }
}

const getConnectionPath = (connection: any) => {
  const fromNode = nodes.value.find(n => n.id === connection.fromNodeId)
  const toNode = nodes.value.find(n => n.id === connection.toNodeId)
  
  if (!fromNode || !toNode) return ''
  
  // 计算起始点和结束点坐标
  const startX = fromNode.x + 120 // 输出连接点位置
  const startY = fromNode.y + 30
  const endX = toNode.x         // 输入连接点位置
  const endY = toNode.y + 30
  
  // 创建贝塞尔曲线路径，使连接线更美观
  const controlPointOffset = Math.min(Math.abs(endX - startX) / 2, 100)
  return `M ${startX} ${startY} C ${startX + controlPointOffset} ${startY}, ${endX - controlPointOffset} ${endY}, ${endX} ${endY}`
}

const preview = () => {
  console.log('预览流程 - 当前数据:', {
    name: workflowName.value,
    nodes: nodes.value,
    connections: connections.value
  })
  
  // 验证数据
  if (!nodes.value.length) {
    ElMessage.warning('请先添加流程节点再预览')
    return
  }
  
  // 保存当前流程到store
  const workflowData = {
    name: workflowName.value,
    nodes: nodes.value,
    connections: connections.value
  }
  
  store.setWorkflow(workflowData)
  
  // 同时保存到localStorage作为备用
  localStorage.setItem('tempWorkflowPreview', JSON.stringify(workflowData))
  
  console.log('store中的数据:', store.currentWorkflow)
  
  router.push('/workflow-preview')
}

const save = () => {
  console.log('保存流程 - 当前数据:', {
    name: workflowName.value,
    nodes: nodes.value,
    connections: connections.value,
    nodeCount: nodes.value.length,
    connectionCount: connections.value.length
  })
  
  // 保存当前流程到store
  store.setWorkflow({
    name: workflowName.value,
    nodes: nodes.value,
    connections: connections.value
  })
  
  console.log('保存后store中的数据:', store.currentWorkflow)
  
  // 同时调用store的保存方法
  store.saveWorkflow().then(success => {
    if (success) {
      ElMessage.success('流程保存成功！')
    } else {
      ElMessage.error('流程保存失败！')
    }
  })
}

const exportWorkflow = () => {
  const workflowData = {
    name: workflowName.value,
    description: '通过流程设计器创建的工作流',
    nodes: nodes.value.map(node => ({
      ...node,
      properties: selectedNodeData.value?.id === node.id ? {
        title: selectedNodeData.value.title,
        description: selectedNodeData.value.description,
        type: selectedNodeData.value.type
      } : {}
    })),
    connections: connections.value,
    metadata: {
      createdBy: '流程设计器',
      version: '1.0.0',
      nodeCount: nodes.value.length,
      connectionCount: connections.value.length
    },
    createdAt: new Date().toISOString()
  }
  
  console.log('导出流程数据:', workflowData)
  
  const blob = new Blob([JSON.stringify(workflowData, null, 2)], { type: 'application/json' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `${workflowName.value || 'workflow'}.json`
  a.click()
  URL.revokeObjectURL(url)
  
  ElMessage.success('流程导出成功！')
}

// 键盘事件处理
const handleKeyDown = (event: KeyboardEvent) => {
  // 删除键
  if (event.key === 'Delete' || event.key === 'Backspace') {
    deleteSelected()
    event.preventDefault()
  }
  
  // Escape键取消连线
  if (event.key === 'Escape' && connectionState.isConnecting) {
    endConnection()
    event.preventDefault()
  }
  
  // Ctrl+S 保存
  if (event.ctrlKey && event.key === 's') {
    save()
    event.preventDefault()
  }
}

// 初始化
onMounted(async () => {
  console.log('WorkflowDesigner 初始化开始')
  
  // 初始化store
  await store.init()
  console.log('store初始化完成:', store.currentWorkflow)
  
  // 添加一个示例开始节点
  addNodeToCanvas({ type: 'start', label: '开始' }, 100, 100)
  console.log('添加示例节点后，当前节点数量:', nodes.value.length)
  
  // 添加键盘事件监听
  document.addEventListener('keydown', handleKeyDown)
})

// 清理
onUnmounted(() => {
  document.removeEventListener('keydown', handleKeyDown)
  endConnection()
})
</script>

<style scoped>
.workflow-designer {
  height: 100vh;
  background: #f5f5f5;
}

.toolbar {
  background: white;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.components-panel {
  background: white;
  border-right: 1px solid #e6e6e6;
  overflow-y: auto;
}

.panel-header {
  padding: 15px;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.component-list {
  padding: 15px;
}

.component-category {
  margin-bottom: 20px;
}

.component-category h4 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #666;
}

.component-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  margin-bottom: 8px;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 4px;
  cursor: grab;
  transition: all 0.2s;
}

.component-item:hover {
  background: #e3f2fd;
  border-color: #409eff;
}

.component-item:active {
  cursor: grabbing;
}

.component-item .el-icon {
  margin-right: 8px;
  color: #409eff;
}

.design-canvas-container {
  position: relative;
  background: #f9f9f9;
  overflow: hidden;
}

.design-canvas {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
  transition: background-color 0.3s ease;
}

.design-canvas.connecting {
  background-color: rgba(103, 194, 58, 0.05);
}

.connection-hint {
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  max-width: 400px;
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
  opacity: 0.5;
}

.workflow-node {
  position: absolute;
  width: 120px;
  min-height: 60px;
  background: white;
  border: 2px solid #e6e6e6;
  border-radius: 8px;
  cursor: move;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;
}

.workflow-node:hover {
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.workflow-node.selected {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.node-header {
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
}

.node-content {
  padding: 8px 12px;
  font-size: 11px;
  color: #666;
  line-height: 1.4;
}

.connection-point {
  position: absolute;
  width: 10px;
  height: 10px;
  background: #409eff;
  border: 2px solid white;
  border-radius: 50%;
  cursor: pointer;
  opacity: 0.7;
  transition: all 0.2s ease;
  z-index: 10;
}

.connection-point:hover {
  opacity: 1;
  transform: scale(1.3);
  box-shadow: 0 0 8px rgba(64, 158, 255, 0.5);
}

.connection-point.highlight {
  opacity: 1;
  background: #67c23a;
  transform: scale(1.4);
  box-shadow: 0 0 12px rgba(103, 194, 58, 0.7);
  animation: pulse 1s infinite;
}

.connection-point.input {
  top: 50%;
  left: -7px;
  transform: translateY(-50%);
}

.connection-point.output {
  top: 50%;
  right: -7px;
  transform: translateY(-50%);
  cursor: crosshair;
}

@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(103, 194, 58, 0.7); }
  70% { box-shadow: 0 0 0 10px rgba(103, 194, 58, 0); }
  100% { box-shadow: 0 0 0 0 rgba(103, 194, 58, 0); }
}

.connections-svg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.connection-line {
  pointer-events: stroke;
  cursor: pointer;
  transition: stroke-width 0.2s ease;
}

.connection-line:hover {
  stroke-width: 3;
}

.connection-preview {
  pointer-events: none;
  animation: dashMove 1s linear infinite;
}

@keyframes dashMove {
  0% { stroke-dashoffset: 0; }
  100% { stroke-dashoffset: 10; }
}

.properties-panel {
  background: white;
  border-left: 1px solid #e6e6e6;
  overflow-y: auto;
}

.properties-content {
  padding: 15px;
}

.no-selection {
  text-align: center;
  padding: 40px 20px;
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

/* 帮助对话框样式 */
.help-content {
  padding: 20px;
}

.shortcut-keys h4,
.tips h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 14px;
}

.shortcut-keys ul,
.tips ul {
  margin: 0;
  padding-left: 20px;
}

.shortcut-keys li,
.tips li {
  margin-bottom: 8px;
  color: #666;
  font-size: 13px;
}

kbd {
  display: inline-block;
  padding: 2px 6px;
  background: #f5f5f5;
  border: 1px solid #ccc;
  border-radius: 3px;
  font-family: monospace;
  font-size: 11px;
  box-shadow: 0 1px 0 rgba(0, 0, 0, 0.1);
}
</style>
