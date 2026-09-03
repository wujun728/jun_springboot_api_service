<template>
  <div class="workflow-designer" :class="{ 'fullscreen-mode': isFullscreen }">
    <!-- 顶部工具栏 -->
    <div class="designer-header">
      <div class="header-left">
        <h2>{{ isEditMode ? '编辑流程' : '新建流程' }}</h2>
        <div class="process-inputs">
          <el-input
            v-model="processStore.currentProcess.name"
            placeholder="请输入流程名称"
            class="process-name-input"
            @blur="updateProcessName"
          />
          <el-input
            v-model="processStore.currentProcess.key"
            placeholder="流程标识（自动生成）"
            class="process-key-input"
            :disabled="isEditMode"
            @blur="updateProcessName"
          />
        </div>
      </div>
      <div class="header-right">
        <el-button @click="validateWorkflow">
          <el-icon class="el-icon--left"><Check /></el-icon>
          验证工作流
        </el-button>
        <el-button type="primary" @click="previewWorkflow">
          <el-icon class="el-icon--left"><View /></el-icon>
          预览工作流
        </el-button>
        <el-button 
          type="success" 
          @click="saveWorkflow"
          :loading="isSaving"
          :disabled="isSaving"
        >
          <el-icon class="el-icon--left" v-if="!isSaving"><Document /></el-icon>
          {{ isSaving ? '保存中...' : (isEditMode ? '更新流程' : '保存流程') }}
        </el-button>
        <el-button @click="clearWorkflow">
          <el-icon class="el-icon--left"><Delete /></el-icon>
          清空画布
        </el-button>
        <el-button @click="toggleFullscreen">
          <el-icon class="el-icon--left"><FullScreen v-if="!isFullscreen" /><Aim v-else /></el-icon>
          {{ isFullscreen ? '退出全屏' : '全屏设计' }}
        </el-button>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="designer-body">
      <!-- 左侧节点面板 -->
      <div class="designer-left" :class="{ collapsed: leftPanelCollapsed }">
        <div class="nodes-panel">
          <div class="panel-header">
            <div class="header-title" v-show="!leftPanelCollapsed">
              <h3>节点库</h3>
              <el-input
                v-model="nodeSearchText"
                placeholder="搜索节点..."
                size="small"
                prefix-icon="Search"
                clearable
                class="search-input"
              />
            </div>
            <el-button 
              :icon="leftPanelCollapsed ? ArrowRight : ArrowLeft"
              size="small"
              circle
              class="collapse-btn"
              @click="toggleLeftPanel"
            />
          </div>
          <div class="panel-content" v-show="!leftPanelCollapsed">
            <NodePanel :search-text="nodeSearchText" />
          </div>
        </div>
      </div>

      <!-- 中间工作流画布 -->
      <div class="designer-center">
        <div class="canvas-container">
          <div class="canvas-header">
            <span>工作流画布</span>
            <div class="canvas-controls">
              <!-- 缩放控制器 -->
              <div class="zoom-controls">
                <el-button-group>
                  <el-button size="small" @click="zoomOut" :disabled="canvasScale <= 0.25">
                    <el-icon><ZoomOut /></el-icon>
                  </el-button>
                  <el-button size="small" @click="resetZoom" title="重置缩放和位置">
                    {{ Math.round(canvasScale * 100) }}%
                  </el-button>
                  <el-button size="small" @click="centerCanvas" title="居中显示">
                    <el-icon><Aim /></el-icon>
                  </el-button>
                  <el-button size="small" @click="zoomIn" :disabled="canvasScale >= 2">
                    <el-icon><ZoomIn /></el-icon>
                  </el-button>
                </el-button-group>
              </div>
              <!-- 画布统计 -->
              <div class="canvas-stats">
                <el-tag v-if="processStore.currentProcess.nodes.length > 0">
                  {{ processStore.currentProcess.nodes.length }} 个节点
                </el-tag>
                <el-tag v-if="processStore.currentProcess.flows.length > 0" type="success">
                  {{ processStore.currentProcess.flows.length }} 个连接
                </el-tag>
                <el-tag v-if="processStore.currentProcess.nodes.length === 0" type="info">
                  空画布
                </el-tag>
              </div>
            </div>
          </div>
          <div 
            class="canvas-wrapper" 
            ref="canvasWrapperRef"
            @wheel="handleCanvasWheel"
            @mousedown="handleCanvasMouseDown"
            @mousemove="handleCanvasMouseMove"
            @mouseup="handleCanvasMouseUp"
            @mouseleave="handleCanvasMouseUp"
            :class="{ 'is-dragging': isDragging }"
          >
            <div 
              class="canvas-content" 
              :style="canvasTransformStyle"
            >
              <ProcessCanvas
                ref="processCanvasRef"
                :current-process="processStore.currentProcess"
                :selected-node-id="processStore.selectedNodeId"
                :selected-flow-id="processStore.selectedFlowId"
                :canvas-scale="canvasScale"
                @node-added="handleNodeAdded"
                @node-deleted="handleNodeDeleted"
                @node-selected="handleNodeSelected"
                @node-updated="handleNodeUpdated"
                @flow-added="handleFlowAdded"
                @flow-deleted="handleFlowDeleted"
                @flow-selected="handleFlowSelected"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧面板 -->
      <div class="designer-right" :class="{ collapsed: rightPanelCollapsed }">
        <div class="right-panel">
          <div class="panel-header">
            <!-- 折叠按钮 -->
            <el-button 
              :icon="rightPanelCollapsed ? ArrowLeft : ArrowRight"
              size="small"
              circle
              class="collapse-btn"
              @click="toggleRightPanel"
            />
            <!-- Tab 切换 -->
            <el-tabs v-model="rightActiveTab" class="right-tabs" v-show="!rightPanelCollapsed">
              <el-tab-pane label="节点配置" name="config" />
              <el-tab-pane label="操作指南" name="guide" />
            </el-tabs>
          </div>
          
          <div class="panel-content" v-show="!rightPanelCollapsed">
            <!-- 操作指南内容 -->
            <div v-show="rightActiveTab === 'guide'" class="guide-content">
              <div class="guide-section">
                <h5>🎯 快速开始</h5>
                <ul>
                  <li>从左侧拖拽节点到画布</li>
                  <li>点击节点进行属性配置</li>
                  <li>拖拽连接节点建立流程</li>
                </ul>
              </div>
              <div class="guide-section">
                <h5>⚡ 快捷键</h5>
                <ul>
                  <li><kbd>Delete</kbd> 删除选中元素</li>
                  <li><kbd>Ctrl+S</kbd> 保存流程</li>
                  <li><kbd>Ctrl+Z</kbd> 撤销操作</li>
                  <li><kbd>Ctrl+滚轮</kbd> 缩放画布</li>
                  <li><kbd>Alt+拖拽</kbd> 平移画布</li>
                  <li><kbd>F11</kbd> 切换全屏模式</li>
                  <li><kbd>Esc</kbd> 退出全屏</li>
                </ul>
              </div>
              <div class="guide-section">
                <h5>💡 提示</h5>
                <ul>
                  <li>开始节点只能有一个</li>
                  <li>每个流程至少需要一个结束节点</li>
                  <li>审批节点必须设置处理人</li>
                  <li>中键或Alt+拖拽可以平移画布</li>
                  <li>Ctrl+滚轮可以缩放画布</li>
                  <li>全屏模式提供更大设计空间</li>
                </ul>
              </div>
            </div>
            
            <!-- 节点配置内容 -->
            <div v-show="rightActiveTab === 'config'" class="config-content">
              <ProcessPropertiesPanel
                v-if="processStore.selectedNode"
                :selected-node="processStore.selectedNode"
                :selected-flow="processStore.selectedFlow"
                :process="processStore.currentProcess"
                @node-updated="handleNodePropertyUpdated"
                @flow-updated="handleFlowUpdated"
                @flow-deleted="handleFlowDeleted"
                @variable-added="handleVariableAdded"
                @variable-deleted="handleVariableDeleted"
                @process-updated="handleProcessUpdated"
              />
              <el-empty 
                v-else 
                description="请选择节点进行配置"
                :image-size="100"
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 预览抽屉 - 全屏展示 -->
    <el-drawer
      v-model="previewDrawerVisible"
      title="工作流预览"
      direction="rtl"
      size="100%"
      :with-header="true"
      class="fullscreen-preview-drawer"
    >
      <div class="workflow-preview">
        <div class="preview-info">
          <div class="preview-title">
            <h3>{{ processStore.currentProcess.name || '未命名流程' }}</h3>
          </div>
        </div>

        <!-- 预览内容 - 左右布局 -->
        <div class="preview-content">
          <!-- 左侧：流程图预览 -->
          <div class="preview-left">
            <div class="preview-diagram">
              <WorkflowCanvas 
                :model-value="previewWorkflowData"
                :readonly="true"
                class="diagram-container"
              />
            </div>
          </div>
          
          <!-- 右侧：节点信息和JSON配置 -->
          <div class="preview-right">
            <div class="info-panel">
              <el-tabs v-model="previewRightTab" class="info-tabs">
                <el-tab-pane label="节点信息" name="nodes">
                  <template #label>
                    <span class="tab-label">
                      <el-icon><Setting /></el-icon>
                      节点信息
                    </span>
                  </template>
                  <div class="panel-content">
                    <div v-if="processStore.currentProcess.nodes.length === 0" class="empty-state">
                      <el-empty description="暂无节点" />
                    </div>
                    <div v-else class="nodes-list">
                      <div 
                        v-for="node in processStore.currentProcess.nodes" 
                        :key="node.id"
                        class="node-item"
                      >
                        <div class="node-header">
                          <div class="node-icon">
                            <el-icon>
                              <VideoPlay v-if="node.type === 'START'" />
                              <User v-else-if="node.type === 'APPROVAL'" />
                              <Bell v-else-if="node.type === 'CC'" />
                              <Setting v-else-if="node.type === 'CONDITIONAL_BRANCH'" />
                              <Share v-else-if="node.type === 'PARALLEL'" />
                              <Connection v-else />
                            </el-icon>
                          </div>
                          <div class="node-info">
                            <h4>{{ node.name }}</h4>
                            <span class="node-type">{{ getNodeTypeText(node.type) }}</span>
                          </div>
                        </div>
                        <div class="node-details">
                          <el-descriptions :column="1" size="small">
                            <el-descriptions-item label="节点ID">{{ node.id }}</el-descriptions-item>
                            <el-descriptions-item label="节点类型">{{ node.type }}</el-descriptions-item>
                            <el-descriptions-item v-if="node.assignee" label="处理人">{{ getUserDisplayName(node.assignee) }}</el-descriptions-item>
                            <el-descriptions-item v-if="node.assigneeType" label="处理人类型">{{ getAssigneeTypeText(node.assigneeType) }}</el-descriptions-item>
                            <el-descriptions-item v-if="node.condition" label="执行条件">{{ node.condition }}</el-descriptions-item>
                            <el-descriptions-item v-if="node.description" label="节点描述">{{ node.description }}</el-descriptions-item>
                          </el-descriptions>
                        </div>
                      </div>
                    </div>
                  </div>
                </el-tab-pane>
                
                <el-tab-pane label="JSON配置" name="json">
                  <template #label>
                    <span class="tab-label">
                      <el-icon><Document /></el-icon>
                      JSON配置
                    </span>
                  </template>
                  <div class="panel-content">
                    <div class="json-header">
                      <h4>工作流配置</h4>
                      <el-button size="small" @click="copyJsonToClipboard">
                        <el-icon class="el-icon--left"><Document /></el-icon>
                        复制JSON
                      </el-button>
                    </div>
                    <el-input
                      :model-value="JSON.stringify(processStore.currentProcess, null, 2)"
                      type="textarea"
                      :rows="20"
                      readonly
                      class="json-textarea"
                    />
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Check, View, Document, Delete, VideoPlay, User, Setting, 
  Connection, ArrowRight, ArrowLeft, ArrowUp, ArrowDown, Close, Timer,
  ZoomIn, ZoomOut, FullScreen, Aim, QuestionFilled, Bell, Share
} from '@element-plus/icons-vue'
import { useProcessDesignerStore } from '@/stores/workflow/processDesigner'
import ProcessCanvas from '@/components/workflow-designer/ProcessCanvas.vue'
import NodePanel from '@/components/workflow-designer/NodePanel.vue'
import ProcessPropertiesPanel from '@/components/workflow-designer/ProcessPropertiesPanel.vue'
import WorkflowCanvas from '@/components/workflow-designer/WorkflowCanvas.vue'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'

const processStore = useProcessDesignerStore()

// 用户数据
interface UserInfo {
  id: number
  username: string
  realName?: string
  email?: string
}

const userMap = ref<Map<number | string, UserInfo>>(new Map())

// 响应式数据
const previewDrawerVisible = ref(false)
const previewRightTab = ref('nodes') // 右侧面板的tab切换
const processId = ref<string>('')
const isEditMode = computed(() => !!processId.value)
const processCanvasRef = ref()
const isSaving = ref(false)

// 面板控制
const leftPanelCollapsed = ref(false)
const rightPanelCollapsed = ref(false) // 右侧面板默认展开
const rightActiveTab = ref('guide') // 右侧Tab默认显示操作指南
const nodeSearchText = ref('') // 节点搜索文本

// 画布缩放和平移
const canvasScale = ref(1)
const canvasPanX = ref(0)
const canvasPanY = ref(0)
const isPanning = ref(false)
const isDragging = ref(false) // 普通左键拖拽状态
const panStartX = ref(0)
const panStartY = ref(0)
const canvasWrapperRef = ref<HTMLElement | null>(null)

// 全屏功能
const isFullscreen = ref(false)

// 预览工作流数据转换
const previewWorkflowData = computed(() => {
  // 节点类型映射
  const mapNodeType = (type: string) => {
    const typeMap = {
      'START': 'start',
      'END': 'end', 
      'APPROVAL': 'user',
      'CONDITIONAL_BRANCH': 'decision',
      'TASK': 'task'
    }
    return typeMap[type] || 'task'
  }

  return {
    id: processStore.currentProcess.id || '',
    name: processStore.currentProcess.name || '未命名工作流',
    nodes: (processStore.currentProcess.nodes || []).map(node => ({
      id: node.id,
      type: mapNodeType(node.type),
      name: node.name,
      description: node.description || '',
      position: node.position || { x: 100, y: 100 },
      properties: node.properties || {}
    })),
    connections: (processStore.currentProcess.flows || []).map(flow => ({
      id: flow.id,
      from: flow.from,
      to: flow.to,
      name: flow.name || '',
      condition: flow.condition || null
    }))
  }
})

// 业务类型相关参数
const businessTypeParams = ref({
  businessType: 1,
  systemFormKey: '',
  businessFormId: '',
  businessView: '',
  variables: null as string | null
})

// 获取URL参数
const getUrlParams = () => {
  const urlParams = new URLSearchParams(window.location.search)
  return urlParams.get('id') || ''
}

// 获取业务类型参数
const getBusinessTypeParams = () => {
  const urlParams = new URLSearchParams(window.location.search)
  return {
    businessType: urlParams.get('businessType') ? parseInt(urlParams.get('businessType')!) : 1,
    systemFormKey: urlParams.get('systemFormKey') || '',
    businessFormId: urlParams.get('businessFormId') || '',
    businessView: urlParams.get('businessView') || '', // 获取表单视图配置
    variables: urlParams.get('variables') || null // 获取变量JSON字符串
  }
}

// 将表单组件类型映射为变量类型
const mapComponentTypeToVariableType = (componentType: string): string => {
  const typeMap: Record<string, string> = {
    'input': 'string',
    'textarea': 'string',
    'number': 'number',
    'select': 'string',
    'radio': 'string',
    'checkbox': 'string',
    'date': 'date',
    'datetime': 'date',
    'switch': 'boolean',
    'upload': 'string',
    'user-select': 'user-select',
    'department-select': 'department-select',
    'role-select': 'role-select'
  }
  return typeMap[componentType] || 'string'
}

// 从businessView中更新变量类型
const updateVariableTypesFromBusinessView = (variables: any[], businessView: string): any[] => {
  if (!businessView || !variables || variables.length === 0) {
    return variables
  }
  
  try {
    const formConfig = JSON.parse(businessView)
    if (!formConfig.components || !Array.isArray(formConfig.components)) {
      return variables
    }
    
    // 创建组件名称到类型的映射
    const componentTypeMap: Record<string, string> = {}
    formConfig.components.forEach((comp: any) => {
      const name = comp.name || comp.id
      if (name && comp.type) {
        componentTypeMap[name] = mapComponentTypeToVariableType(comp.type)
      }
    })
    
    // 更新变量类型
    return variables.map(v => ({
      ...v,
      type: componentTypeMap[v.name] || v.type
    }))
  } catch (e) {
    console.error('解析businessView失败:', e)
    return variables
  }
}

// 加载流程数据
const loadProcessData = async (id: string) => {
  try {
    
    // 调用后端API获取流程数据
    const { workflowApi } = await import('@/api')
    const response = await workflowApi.getProcessDefinition(id)
    
    
    // 处理统一响应格式
    let actualData = response
    if (response.data) {
      // 如果响应被Result包装了
      actualData = response.data
    }
    
    // 获取变量，如果有businessView则从中更新变量类型
    let variables = actualData.variables || []
    if (actualData.businessView) {
      variables = updateVariableTypesFromBusinessView(variables, actualData.businessView)
    }
    
    // 解析流程配置 - actualData就是ProcessDefinitionDTO
    let processData = {
      id: actualData.id || id,
      name: actualData.name || '',
      key: actualData.key || `process_${id}`,
      description: actualData.description || '',
      variables: variables,
      nodes: actualData.nodes || [],
      flows: actualData.flows || []
    }
    
    
    processStore.importProcess(processData)
    ElMessage.success('流程加载成功')
  } catch (error) {
    console.error('加载流程失败:', error)
    ElMessage.error('加载流程失败，将使用空白流程')
    
    // 加载失败时创建空白流程
    processStore.initProcess()
  }
}

// 这部分代码已经移动到上面的全屏功能部分

// 事件处理
const handleNodeAdded = (nodeData: any) => {
  
  try {
    const newNode = processStore.addNode(nodeData.type, nodeData.position)
    
    if (newNode) {
      ElMessage.success(`已添加${getNodeTypeText(newNode.type)}`)
    } else {
      console.error('Store返回空节点')
    }
  } catch (error) {
    console.error('添加节点时出错:', error)
    ElMessage.error('添加节点失败')
  }
}

const handleNodeDeleted = (nodeId: string) => {
  processStore.deleteNode(nodeId)
}

const handleNodeSelected = (nodeId: string) => {
  processStore.selectNode(nodeId)
  // 选中节点时自动切换到配置 Tab
  rightActiveTab.value = 'config'
  // 如果面板是折叠的，自动展开
  if (rightPanelCollapsed.value) {
    rightPanelCollapsed.value = false
  }
}

const handleNodeUpdated = (nodeId: string, updates: any) => {
  processStore.updateNode(nodeId, updates)
}

const handleFlowAdded = (flow: any) => {
  
  try {
    const newFlow = processStore.addFlow(flow.from, flow.to, flow.condition)
    
    if (newFlow) {
      
      // 选中新创建的连接
      processStore.selectFlow(newFlow.id)
      
      // 触发视觉反馈
      showFlowCreatedFeedback(newFlow)
    } else {
      console.error('Store返回空连接')
    }
  } catch (error) {
    console.error('添加连接时出错:', error)
    ElMessage.error('添加连接失败')
  }
}

// 显示连接创建成功的视觉反馈
const showFlowCreatedFeedback = (flow: any) => {
  // 显示成功通知
  const fromNodeName = getNodeName(flow.from)
  const toNodeName = getNodeName(flow.to)
  
  ElMessage({
    message: `✅ 连接创建成功：${fromNodeName} → ${toNodeName}`,
    type: 'success',
    duration: 3000,
    showClose: true,
    customClass: 'flow-success-message'
  })
  
  // 添加画布统计更新动画
  animateStatsUpdate()
}

// 画布统计更新动画
const animateStatsUpdate = () => {
  // 简单的数字更新动画效果
  const statsElements = document.querySelectorAll('.canvas-stats .el-tag')
  statsElements.forEach(el => {
    el.classList.add('stats-update-animation')
    setTimeout(() => {
      el.classList.remove('stats-update-animation')
    }, 600)
  })
}

// 获取节点名称
const getNodeName = (nodeId: string) => {
  const node = processStore.currentProcess.nodes.find(n => n.id === nodeId)
  return node ? node.name : '未知节点'
}

const handleFlowDeleted = (flowId: string) => {
  processStore.deleteFlow(flowId)
}

const handleFlowSelected = (flowId: string) => {
  processStore.selectFlow(flowId)
}

const handleNodePropertyUpdated = (nodeId: string, updates: any) => {
  processStore.updateNode(nodeId, updates)
}

const handleFlowUpdated = (flowId: string, updates: any) => {
  const flow = processStore.currentProcess.flows.find(f => f.id === flowId)
  if (flow) {
    Object.assign(flow, updates)
    processStore.saveToHistory()
  }
}

const handleVariableAdded = (variable: any) => {
  processStore.addVariable(variable)
}

const handleVariableDeleted = (variableName: string) => {
  processStore.deleteVariable(variableName)
}

const handleProcessUpdated = (updates: any) => {
  Object.assign(processStore.currentProcess, updates)
  processStore.saveToHistory()
}

const updateProcessName = () => {
  if (processStore.currentProcess.name.trim()) {
    // 自动生成key（仅在新建模式且key为空时）
    if (!isEditMode.value && !processStore.currentProcess.key.trim()) {
      processStore.currentProcess.key = processStore.currentProcess.name
        .toLowerCase()
        .replace(/\s+/g, '_')
        .replace(/[^\w_]/g, '')
        .replace(/^[^a-z]/, 'process_') // 确保以字母开头
    }
    processStore.saveToHistory()
  }
}

// 验证流程
const validateWorkflow = () => {
  const errors = processStore.validateProcess()
  if (errors.length === 0) {
    ElMessage.success('流程验证通过')
  } else {
    ElMessage.error(`流程验证失败：${errors[0]}`)
  }
}

// 预览流程
const previewWorkflow = () => {
  if (processStore.currentProcess.nodes.length === 0) {
    ElMessage.warning('请先添加流程节点')
    return
  }
  
  previewDrawerVisible.value = true
}

// 保存流程
const saveWorkflow = async () => {
  if (isSaving.value) {
    return // 防止重复提交
  }
  
  if (!processStore.currentProcess.name.trim()) {
    ElMessage.warning('请输入流程名称')
    return
  }
  
  // 自动生成key（如果为空）
  if (!processStore.currentProcess.key.trim()) {
    processStore.currentProcess.key = processStore.currentProcess.name
      .toLowerCase()
      .replace(/\s+/g, '_')
      .replace(/[^\w_]/g, '')
      .replace(/^[^a-z]/, 'process_') // 确保以字母开头
    
    if (!processStore.currentProcess.key.trim()) {
      ElMessage.warning('无法生成有效的流程标识，请手动输入')
      return
    }
  }
  
  if (processStore.currentProcess.nodes.length === 0) {
    ElMessage.warning('流程内容不能为空')
    return
  }
  
  // 验证流程
  const errors = processStore.validateProcess()
  if (errors.length > 0) {
    ElMessage.error(`流程验证失败：${errors[0]}`)
    return
  }
  
  try {
    const actionText = isEditMode.value ? '更新' : '保存'
    await ElMessageBox.confirm(`确定${actionText}流程吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    
    // 开始保存状态
    isSaving.value = true
    
    // 构造符合后端规范的流程定义数据
    const processDefinitionData = {
      name: processStore.currentProcess.name,
      key: processStore.currentProcess.key,
      description: processStore.currentProcess.description || '',
      variables: processStore.currentProcess.variables || [],
      nodes: processStore.currentProcess.nodes,
      flows: processStore.currentProcess.flows,
      // 添加业务类型相关字段
      businessType: businessTypeParams.value.businessType,
      systemFormKey: businessTypeParams.value.systemFormKey || null,
      businessFormId: businessTypeParams.value.businessFormId || null,
      businessView: businessTypeParams.value.businessView || null
    }
    
    
    // 调用后端API保存
    const { workflowApi } = await import('@/api')
    let response
    
    if (isEditMode.value) {
      // 更新模式：调用更新接口
      response = await workflowApi.updateProcessDefinition(processId.value, processDefinitionData)
    } else {
      // 新建模式：调用部署接口
      response = await workflowApi.deployProcess(processDefinitionData)
      
      // 保存返回的ID用于后续编辑
      if (response.id) {
        processId.value = response.id
        // 更新URL，但不刷新页面
        const newUrl = `${window.location.pathname}?id=${response.id}`
        window.history.replaceState({}, '', newUrl)
      }
    }
    
    ElMessage.success(`流程${actionText}成功，即将跳转到列表页面...`)
    
    // 保存成功后跳转回流程列表
    setTimeout(() => {
       window.location.href = '/home/workflow/list'
    }, 1000)
    
  } catch (error) {
    console.error('保存流程失败:', error)
    
    if (error && typeof error === 'object' && 'response' in error) {
      // 处理HTTP错误响应
      const errorResponse = error.response
      if (errorResponse && errorResponse.data && errorResponse.data.message) {
        ElMessage.error(`保存失败：${errorResponse.data.message}`)
      } else {
        ElMessage.error(`保存失败：HTTP ${errorResponse?.status || '未知错误'}`)
      }
    } else if (error && typeof error === 'object' && 'message' in error) {
      ElMessage.error('保存失败：' + error.message)
    } else {
      const actionText = isEditMode.value ? '更新' : '保存'
      ElMessage.error(`${actionText}流程失败`)
    }
  } finally {
    // 重置保存状态
    isSaving.value = false
  }
}

// 清空流程
const clearWorkflow = async () => {
  if (processStore.currentProcess.nodes.length === 0) {
    ElMessage.warning('画布已经是空的')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      '确定要清空画布吗？此操作不可恢复！',
      '确认清空',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    processStore.clearProcess()
    ElMessage.success('画布已清空')
  } catch (error) {
    // 用户取消操作
  }
}

// 获取节点类型文本
const getNodeTypeText = (type: string) => {
  const typeMap = {
    START: '开始节点',
    END: '结束节点',
    APPROVAL: '审批节点',
    CC: '抄送节点',
    CONDITIONAL_BRANCH: '条件分支',
    PARALLEL: '并行节点'
  }
  return typeMap[type as keyof typeof typeMap] || '未知节点'
}

// 获取处理人类型文本
const getAssigneeTypeText = (type: string) => {
  const typeMap = {
    FIXED: '固定用户',
    DYNAMIC: '动态人员',
    USER: '指定用户',
    ROLE: '指定角色',
    DEPT: '指定部门',
    EXPRESSION: '表达式'
  }
  return typeMap[type as keyof typeof typeMap] || '未知类型'
}

// 加载用户数据
const userStore = useUserStore()

const loadUsers = async () => {
  try {
    // 使用缓存的用户数据
    const users: UserInfo[] = await userStore.loadEnabledUsers()
    
    // 构建用户ID到用户信息的映射
    // 同时存储数字和字符串类型的key，以兼容不同的数据格式
    const newUserMap = new Map<number | string, UserInfo>()
    users.forEach((user: UserInfo) => {
      newUserMap.set(user.id, user)
      // 同时也存储字符串类型的key
      newUserMap.set(user.id.toString(), user)
    })
    userMap.value = newUserMap
  } catch (error) {
    console.error('加载用户数据失败:', error)
  }
}

// 获取用户显示名称
const getUserDisplayName = (userId: string | number | undefined): string => {
  if (!userId) {
    return '未指定'
  }
  
  // 先尝试直接用原值查询（可能是字符串或数字）
  let user = userMap.value.get(userId)
  
  // 如果直接查询失败，尝试转换类型后查询
  if (!user) {
    const numUserId = typeof userId === 'string' ? parseInt(userId) : userId
    if (!isNaN(numUserId)) {
      user = userMap.value.get(numUserId)
    }
  }
  
  if (user) {
    return user.realName || user.username
  }
  
  // 如果找不到用户，返回原值
  return userId.toString()
}

// 复制JSON到剪贴板
const copyJsonToClipboard = async () => {
  try {
    const jsonText = JSON.stringify(processStore.currentProcess, null, 2)
    await navigator.clipboard.writeText(jsonText)
    ElMessage.success('JSON配置已复制到剪贴板')
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败，请手动选择复制')
  }
}

// 面板控制方法
const toggleLeftPanel = () => {
  leftPanelCollapsed.value = !leftPanelCollapsed.value
}

const toggleRightPanel = () => {
  rightPanelCollapsed.value = !rightPanelCollapsed.value
}

// 右侧面板标题
const rightPanelTitle = computed(() => {
  if (rightActiveTab.value === 'config' && processStore.selectedNode) {
    return `节点配置 - ${processStore.selectedNode.name || '未命名节点'}`
  }
  if (rightActiveTab.value === 'config') {
    return '节点配置'
  }
  return '操作指南'
})

// 画布变换样式 - 只用于缩放
const canvasTransformStyle = computed(() => ({
  transform: `scale(${canvasScale.value})`,
  transformOrigin: '0 0',
  transition: 'transform 0.2s ease'
}))

// 缩放控制方法
const zoomIn = () => {
  if (canvasScale.value < 2) {
    canvasScale.value = Math.min(2, canvasScale.value + 0.25)
  }
}

const zoomOut = () => {
  if (canvasScale.value > 0.25) {
    canvasScale.value = Math.max(0.25, canvasScale.value - 0.25)
  }
}

const resetZoom = () => {
  canvasScale.value = 1
  canvasPanX.value = 0
  canvasPanY.value = 0
}

// 鼠标滚轮缩放
const handleCanvasWheel = (event: WheelEvent) => {
  if (event.ctrlKey || event.metaKey) {
    event.preventDefault()
    
    const delta = -event.deltaY / 1000
    const newScale = Math.max(0.25, Math.min(2, canvasScale.value + delta))
    
    if (newScale !== canvasScale.value) {
      canvasScale.value = newScale
    }
  }
}

// 画布平移 - 通过修改滚动位置实现
const handleCanvasMouseDown = (event: MouseEvent) => {
  const target = event.target as HTMLElement
  
  // 检查是否点击在节点或连接点上（如果是，不进行平移）
  const isOnInteractive = target.closest('.process-node') || 
                          target.closest('.connection-point') ||
                          target.closest('.input-point') ||
                          target.closest('.output-point') ||
                          target.closest('.flow-line') ||
                          target.closest('.flow-hitarea') ||
                          target.closest('.delete-btn') ||
                          target.closest('.el-button')
  
  if (isOnInteractive) {
    return // 点击在交互元素上，不处理平移
  }
  
  // 左键点击开始拖拽平移
  if (event.button === 0) {
    event.preventDefault()
    isDragging.value = true
    // 记录起始位置和当前滚动位置
    panStartX.value = event.clientX
    panStartY.value = event.clientY
    if (canvasWrapperRef.value) {
      canvasPanX.value = canvasWrapperRef.value.scrollLeft
      canvasPanY.value = canvasWrapperRef.value.scrollTop
    }
  }
}

const handleCanvasMouseMove = (event: MouseEvent) => {
  if (isDragging.value && canvasWrapperRef.value) {
    event.preventDefault()
    // 计算移动距离，更新滚动位置
    const deltaX = event.clientX - panStartX.value
    const deltaY = event.clientY - panStartY.value
    canvasWrapperRef.value.scrollLeft = canvasPanX.value - deltaX
    canvasWrapperRef.value.scrollTop = canvasPanY.value - deltaY
  }
}

const handleCanvasMouseUp = () => {
  isDragging.value = false
}

// 画布居中显示
const centerCanvas = () => {
  if (canvasWrapperRef.value) {
    const wrapper = canvasWrapperRef.value
    // 滚动到画布中心位置
    const scrollX = (3000 - wrapper.clientWidth) / 2
    const scrollY = (2000 - wrapper.clientHeight) / 2
    wrapper.scrollTo({
      left: scrollX,
      top: scrollY,
      behavior: 'smooth'
    })
  }
  canvasScale.value = 1
  ElMessage.success('画布已居中')
}

// 初始化画布位置（滚动到中心）
const initCanvasPosition = () => {
  if (canvasWrapperRef.value) {
    const wrapper = canvasWrapperRef.value
    // 滚动到画布中心位置
    const scrollX = (3000 - wrapper.clientWidth) / 2
    const scrollY = (2000 - wrapper.clientHeight) / 2
    wrapper.scrollLeft = scrollX
    wrapper.scrollTop = scrollY
  }
}

// 全屏功能
const toggleFullscreen = async () => {
  try {
    if (!isFullscreen.value) {
      // 进入全屏
      const element = document.documentElement
      if (element.requestFullscreen) {
        await element.requestFullscreen()
      } else if (element.webkitRequestFullscreen) {
        await element.webkitRequestFullscreen()
      } else if (element.mozRequestFullScreen) {
        await element.mozRequestFullScreen()
      } else if (element.msRequestFullscreen) {
        await element.msRequestFullscreen()
      }
    } else {
      // 退出全屏
      if (document.exitFullscreen) {
        await document.exitFullscreen()
      } else if (document.webkitExitFullscreen) {
        await document.webkitExitFullscreen()
      } else if (document.mozCancelFullScreen) {
        await document.mozCancelFullScreen()
      } else if (document.msExitFullscreen) {
        await document.msExitFullscreen()
      }
    }
  } catch (error) {
    console.error('全屏操作失败:', error)
    ElMessage.error('全屏操作失败')
  }
}

// 监听全屏状态变化
const handleFullscreenChange = () => {
  isFullscreen.value = !!(
    document.fullscreenElement ||
    document.webkitFullscreenElement ||
    document.mozFullScreenElement ||
    document.msFullscreenElement
  )
}

// 页面挂载时添加全屏监听
onMounted(async () => {
  processId.value = getUrlParams()
  businessTypeParams.value = getBusinessTypeParams()
  
  // 加载用户数据
  await loadUsers()
  
  // 添加全屏状态监听
  document.addEventListener('fullscreenchange', handleFullscreenChange)
  document.addEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.addEventListener('mozfullscreenchange', handleFullscreenChange)
  document.addEventListener('msfullscreenchange', handleFullscreenChange)
  
  // 添加键盘快捷键监听
  document.addEventListener('keydown', handleKeyDown)
  
  if (isEditMode.value) {
    // 编辑模式：加载现有流程数据
    await loadProcessData(processId.value)
    
    // 编辑模式下也需要处理从列表页面传递的变量（用于更新变量配置）
    if (businessTypeParams.value.variables) {
      try {
        const variables = JSON.parse(businessTypeParams.value.variables)
        
        // 将变量更新到流程定义中（覆盖从后端加载的变量）
        if (Array.isArray(variables) && variables.length > 0) {
          processStore.currentProcess.variables = variables.map(v => ({
            name: v.name,
            type: v.type,
            label: v.label,
            defaultValue: v.defaultValue || ''
          }))
          ElMessage.success(`已更新 ${variables.length} 个流程变量配置`)
        }
      } catch (error) {
        console.error('编辑模式 - 解析变量参数失败:', error)
        ElMessage.warning('变量参数解析失败，使用原有配置')
      }
    }
  } else {
    // 新增模式：初始化空流程
    processStore.initProcess()
    
    // 处理从列表页面传递的变量
    if (businessTypeParams.value.variables) {
      try {
        const variables = JSON.parse(businessTypeParams.value.variables)
        
        // 将变量添加到流程定义中
        if (Array.isArray(variables) && variables.length > 0) {
          processStore.currentProcess.variables = variables.map(v => ({
            name: v.name,
            type: v.type,
            label: v.label,
            defaultValue: v.defaultValue || ''
          }))
          ElMessage.success(`已导入 ${variables.length} 个流程变量`)
        }
      } catch (error) {
        console.error('解析变量参数失败:', error)
        ElMessage.warning('变量参数解析失败，请重新配置')
      }
    }
    
    // 如果是新建模式，显示业务类型信息
    if (businessTypeParams.value.businessType) {
      const typeText = businessTypeParams.value.businessType === 1 ? '系统表单' : '业务表单'
      const formInfo = businessTypeParams.value.businessType === 1 
        ? businessTypeParams.value.systemFormKey 
        : businessTypeParams.value.businessFormId
      
      ElMessage.success(`已选择${typeText}类型的工作流`)
    }
  }
  
  // 初始化画布位置（滚动到中心）
  setTimeout(() => {
    initCanvasPosition()
  }, 100)
})

// 键盘快捷键处理
const handleKeyDown = (event: KeyboardEvent) => {
  // F11 切换全屏
  if (event.key === 'F11') {
    event.preventDefault()
    toggleFullscreen()
  }
  // Esc 退出全屏
  else if (event.key === 'Escape' && isFullscreen.value) {
    event.preventDefault()
    toggleFullscreen()
  }
}

// 组件卸载时移除监听
onUnmounted(() => {
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
  document.removeEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.removeEventListener('mozfullscreenchange', handleFullscreenChange)
  document.removeEventListener('msfullscreenchange', handleFullscreenChange)
  document.removeEventListener('keydown', handleKeyDown)
})
</script>

<style scoped lang="scss">
.workflow-designer {
  height: calc(100vh - 50px); // 减去顶部导航栏高度
  max-height: calc(100vh - 50px);
  min-width: 1200px;
  display: flex;
  flex-direction: column;
  background: #ffffff;
  overflow: hidden; // 防止内容溢出
  
  &.fullscreen-mode {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 9999;
    min-width: 100vw;
    width: 100vw;
    height: 100vh;
    
    .designer-header {
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
      border-bottom: 1px solid rgba(229, 231, 235, 0.8);
      
      .header-right {
        .el-button {
          &:last-child {
            background: var(--primary-color, #6366f1);
            color: white;
            border-color: var(--primary-color, #6366f1);
            
            &:hover {
              background: var(--primary-color-hover, #5855eb);
              border-color: var(--primary-color-hover, #5855eb);
            }
          }
        }
      }
    }
    
    .guide-panel {
      background: rgba(255, 255, 255, 0.95);
      backdrop-filter: blur(10px);
      border: 1px solid rgba(229, 231, 235, 0.8);
    }
  }
}

.designer-header {
  height: 60px;
  background: var(--bg-card, #ffffff);
  border-bottom: 1px solid var(--border-color, #e5e7eb);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: var(--shadow-sm, 0 1px 2px 0 rgba(0, 0, 0, 0.05));

  .header-left {
    display: flex;
    align-items: center;
    gap: 20px;

    h2 {
      margin: 0;
      font-size: 18px;
      color: var(--text-primary, #1f2937);
      background: transparent;
    }

    .process-inputs {
      display: flex;
      gap: 12px;
      
      .process-name-input,
      .process-key-input {
        width: 200px;
        
        :deep(.el-input__inner) {
          border: 1px dashed var(--border-color, #e5e7eb);
          background: transparent;
          font-size: 14px;
          font-weight: 500;
          
          &:hover {
            border-color: var(--primary-color, #6366f1);
          }
          
          &:focus {
            border-color: var(--primary-color, #6366f1);
            border-style: solid;
          }
        }
        
        &.is-disabled :deep(.el-input__inner) {
          background: var(--bg-tertiary, #f1f5f9);
          color: var(--text-muted, #9ca3af);
          border-style: solid;
        }
      }
      
      .process-key-input {
        font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
        
        :deep(.el-input__inner) {
          font-size: 13px;
        }
      }
    }
  }

  .header-right {
    display: flex;
    gap: 12px;
  }
}

.designer-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.designer-left {
  width: 280px;
  min-width: 280px;
  background: var(--bg-card, #ffffff);
  border-right: 1px solid var(--border-color, #e5e7eb);
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  position: relative;

  &.collapsed {
    width: 50px;
    min-width: 50px;
  }

  .nodes-panel {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .panel-header {
      padding: 0 16px;
      height: 52px;
      border-bottom: 1px solid var(--border-color, #e5e7eb);
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-shrink: 0;
      background: var(--bg-card, #ffffff);

      .header-title {
        flex: 1;
        display: flex;
        align-items: center;
        gap: 12px;
        margin-right: 12px;
        overflow: hidden;

        h3 {
          margin: 0;
          font-size: 14px;
          font-weight: 600;
          color: var(--text-primary, #1f2937);
          white-space: nowrap;
        }
        z-index: 10;
      }
    }

    .panel-content {
      flex: 1;
      overflow-y: auto;
    }
  }
}

.designer-center {
  flex: 1;
  min-width: 600px;
  display: flex;
  flex-direction: column;
  margin: 0 2px;
  position: relative;

  .canvas-container {
    flex: 1;
    min-height: 0; // 重要：确保 flex 子元素能正确计算高度
    overflow: hidden; // 防止内容溢出
    background: var(--bg-card, #ffffff);
    display: flex;
    flex-direction: column;

    .canvas-header {
      height: 52px;
      padding: 0 24px;
      border-bottom: 1px solid var(--border-color, #e5e7eb);
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: #ffffff;

      span {
        font-weight: 500;
        color: var(--text-primary, #1f2937);
      }
      
      .canvas-controls {
        display: flex;
        align-items: center;
        gap: 16px;
        
        .zoom-controls {
          .el-button-group {
            .el-button {
              padding: 4px 8px;
              font-size: 12px;
              
              &:nth-child(2) {
                min-width: 60px;
                font-weight: 600;
                color: var(--primary-color, #6366f1);
              }
            }
          }
        }
        
        .canvas-stats {
          display: flex;
          gap: 8px;
          
          .el-tag.stats-update-animation {
            animation: stats-bounce 0.6s ease-out;
            transform-origin: center;
          }
        }
      }
    }
    
    .canvas-wrapper {
      flex: 1;
      min-height: 0; // 重要：限制高度，让滚动条在可视区域内
      position: relative;
      overflow: auto;
      background: #f8fafc;
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
      // 设置大尺寸以支持扩展
      width: 3000px;
      height: 2000px;
      min-width: 3000px;
      min-height: 2000px;
      position: relative;
      background: #ffffff;
      background-image: 
        linear-gradient(rgba(99, 102, 241, 0.05) 1px, transparent 1px),
        linear-gradient(90deg, rgba(99, 102, 241, 0.05) 1px, transparent 1px);
      background-size: 20px 20px;
    }
  }
}

.designer-right {
  width: 350px;
  min-width: 350px;
  background: var(--bg-card, #ffffff);
  border-left: 1px solid var(--border-color, #e5e7eb);
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;

  &.collapsed {
    width: 50px;
    min-width: 50px;
  }

  .right-panel {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .panel-header {
      padding: 0;
      height: 52px; // 与左侧面板头部高度一致
      border-bottom: 1px solid var(--border-color, #e5e7eb);
      display: flex;
      align-items: center;
      flex-shrink: 0;
      position: relative;

      .collapse-btn {
        position: absolute;
        left: 12px;
        z-index: 10;
      }

      .right-tabs {
        flex: 1;
        height: 100%;
        
        :deep(.el-tabs__header) {
          margin: 0;
          height: 52px;
          border-bottom: none;
          background: transparent;
          padding-left: 48px; // 为折叠按钮留出空间
        }

        :deep(.el-tabs__nav-wrap) {
          height: 52px;
          line-height: 52px;
          
          &::after {
            display: none; // 移除底部线条
          }
        }

        :deep(.el-tabs__nav) {
          height: 52px;
          display: flex;
          align-items: center;
        }

        :deep(.el-tabs__item) {
          height: 52px;
          line-height: 52px;
          padding: 0 20px;
          font-size: 14px;
          font-weight: 500;
          color: var(--text-secondary, #6b7280);
          display: flex;
          align-items: center;
          
          &.is-active {
            color: var(--primary-color, #6366f1);
            font-weight: 600;
          }
        }
        
        :deep(.el-tabs__content) {
          display: none; // 隐藏Tab组件默认的内容区域，我们使用自定义的内容区域
        }
      }
    }

    .panel-content {
      flex: 1;
      overflow-y: auto;
      padding: 16px;
      background: var(--bg-card, #ffffff);

      .guide-content {
        .guide-section {
          margin-bottom: 20px;

          &:last-child {
            margin-bottom: 0;
          }

          h5 {
            margin: 0 0 12px 0;
            font-size: 13px;
            font-weight: 600;
            color: var(--text-primary, #1f2937);
            display: flex;
            align-items: center;
            gap: 6px;
          }

          ul {
            margin: 0;
            padding-left: 20px;
            list-style: none;

            li {
              position: relative;
              margin-bottom: 8px;
              font-size: 12px;
              color: var(--text-secondary, #6b7280);
              line-height: 1.6;

              &:before {
                content: '•';
                position: absolute;
                left: -14px;
                color: var(--primary-color, #6366f1);
                font-weight: bold;
              }

              kbd {
                display: inline-block;
                padding: 2px 6px;
                font-size: 11px;
                color: var(--text-primary, #1f2937);
                background: var(--bg-tertiary, #f1f5f9);
                border: 1px solid var(--border-color, #e5e7eb);
                border-radius: 4px;
                font-family: 'Courier New', monospace;
                font-weight: 500;
              }
            }
          }
        }
      }

      .config-content {
        height: 100%;
        display: flex;
        flex-direction: column;
      }
    }
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 1;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.9;
  }
}

.workflow-preview {
  padding: 20px;
  height: 100%;
  display: flex;
  flex-direction: column;
  
  .preview-info {
    margin-bottom: 20px;
    flex-shrink: 0;
    
    .preview-title {
      h3 {
        margin: 0;
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        padding-bottom: 12px;
        border-bottom: 2px solid #e5e7eb;
        background: transparent;
      }
    }
  }
  
  .preview-content {
    flex: 1;
    display: flex;
    gap: 16px;
    min-height: 0;
  }
  
  .preview-left {
    flex: 3;
    min-width: 0;
    display: flex;
    flex-direction: column;
    
    .preview-diagram {
      flex: 1;
      min-height: 0;
      border: 1px solid var(--border-color, #e5e7eb);
      border-radius: 8px;
      background: var(--bg-card, #ffffff);
      
      .diagram-container {
        width: 100%;
        height: 100%;
      }
    }
  }
  
  .preview-right {
    flex: 2;
    min-width: 400px;
    max-width: 500px;
    display: flex;
    flex-direction: column;
    
    .info-panel {
      flex: 1;
      min-height: 0;
      border: 1px solid var(--border-color, #e5e7eb);
      border-radius: 8px;
      background: var(--bg-card, #ffffff);
      display: flex;
      flex-direction: column;
      
      .info-tabs {
        height: 100%;
        display: flex;
        flex-direction: column;
        
        :deep(.el-tabs__header) {
          margin: 0;
          padding: 0 16px;
          border-bottom: 1px solid var(--border-color, #e5e7eb);
          background: var(--bg-gray-50, #f9fafb);
          border-radius: 8px 8px 0 0;
        }
        
        :deep(.el-tabs__content) {
          flex: 1;
          padding: 0;
          overflow: hidden;
        }
        
        :deep(.el-tab-pane) {
          height: 100%;
          display: flex;
          flex-direction: column;
        }
        
        .tab-label {
          display: flex;
          align-items: center;
          gap: 6px;
          font-size: 14px;
          
          .el-icon {
            color: var(--primary-color, #6366f1);
          }
        }
      }
      
      .panel-content {
        flex: 1;
        overflow: auto;
        padding: 12px;
        
        // 滚动条样式
        &::-webkit-scrollbar {
          width: 8px;
          height: 8px;
        }
        
        &::-webkit-scrollbar-track {
          background: var(--bg-tertiary, #f1f5f9);
          border-radius: 4px;
        }
        
        &::-webkit-scrollbar-thumb {
          background: var(--scrollbar-thumb-color, rgba(99, 102, 241, 0.2));
          border-radius: 4px;
          border: 2px solid var(--bg-tertiary, #f1f5f9);
          
          &:hover {
            background: var(--scrollbar-thumb-hover-color, rgba(99, 102, 241, 0.3));
          }
        }
        
        &::-webkit-scrollbar-corner {
          background: var(--bg-tertiary, #f1f5f9);
        }
        
        .empty-state {
          text-align: center;
          padding: 40px 0;
        }
        
        .nodes-list {
          display: flex;
          flex-direction: column;
          gap: 8px;
        }
        
        .json-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 16px;
          
          h4 {
            margin: 0;
            font-size: 16px;
            font-weight: 600;
            color: var(--text-primary, #1f2937);
          }
        }
        
        .json-textarea {
          :deep(.el-textarea__inner) {
            font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
            font-size: 12px;
            line-height: 1.5;
            background: var(--bg-tertiary, #f1f5f9);
            border: 1px solid var(--border-color, #e5e7eb);
          }
        }
      }
    }
  }
  
  // 响应式设计
  @media (max-width: 1200px) {
    .preview-content {
      flex-direction: column;
      
      .preview-left {
        flex: none;
        
        .preview-diagram {
          height: 500px;
        }
      }
      
      .preview-right {
        flex: none;
        min-width: auto;
        max-width: none;
        
        .info-panel {
          height: 400px;
        }
      }
    }
  }
    
    .node-item {
      border: 1px solid var(--border-color, #e5e7eb);
      border-radius: 6px;
      padding: 12px;
      background: var(--bg-card, #ffffff);
      
      .node-header {
        display: flex;
        align-items: center;
        gap: 10px;
        margin-bottom: 8px;
        
        .node-icon {
          width: 32px;
          height: 32px;
          border-radius: 6px;
          background: var(--primary-color, #6366f1);
          color: white;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 16px;
        }
        
        .node-info {
          flex: 1;
          
          h4 {
            margin: 0 0 4px 0;
            font-size: 16px;
            color: var(--text-primary, #1f2937);
          }
          
          .node-type {
            font-size: 12px;
            color: var(--text-muted, #9ca3af);
            background: var(--bg-tertiary, #f1f5f9);
            padding: 2px 6px;
            border-radius: 4px;
          }
        }
      }
      
      .node-details {
        :deep(.el-descriptions__label) {
          font-weight: 500;
        }
      }
    }
  }
  
  .preview-flows {
    .empty-state {
      text-align: center;
      padding: 40px 0;
    }
    
    .flows-list {
      display: grid;
      gap: 16px;
    }
    
    .flow-item {
      border: 1px solid var(--border-color, #e5e7eb);
      border-radius: 8px;
      padding: 16px;
      background: var(--bg-card, #ffffff);
      
      .flow-path {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 12px;
        padding: 8px 12px;
        background: var(--bg-tertiary, #f1f5f9);
        border-radius: 6px;
        
        .from-node, .to-node {
          font-weight: 500;
          color: var(--text-primary, #1f2937);
          padding: 4px 8px;
          background: var(--bg-card, #ffffff);
          border-radius: 4px;
          border: 1px solid var(--border-color, #e5e7eb);
        }
        
        .arrow {
          color: var(--primary-color, #6366f1);
          font-size: 16px;
        }
      }
      
      .flow-details {
        :deep(.el-descriptions__label) {
          font-weight: 500;
        }
      }
    }
  }
  
  .preview-json {
    .json-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;
      
      h4 {
        margin: 0;
        color: var(--text-primary, #1f2937);
      }
    }
    
    .json-textarea {
      :deep(.el-textarea__inner) {
        font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
        font-size: 12px;
        line-height: 1.5;
        background: var(--bg-tertiary, #f1f5f9);
        border: 1px solid var(--border-color, #e5e7eb);
      }
    }
  }

/* 响应式设计 */
@media (max-width: 1400px) {
  .designer-left {
    width: 250px;
    min-width: 250px;
    
    &.collapsed {
      width: 45px;
      min-width: 45px;
    }
  }
  
  .designer-right {
    width: 320px;
    min-width: 320px;
  }
}

@media (max-width: 1200px) {
  .workflow-designer {
    min-width: 1000px;
  }
  
  .designer-left {
    width: 220px;
    min-width: 220px;
    
    &.collapsed {
      width: 40px;
      min-width: 40px;
    }
  }
  
  .designer-right {
    width: 280px;
    min-width: 280px;
  }
  
  .designer-center {
    min-width: 500px;
  }
}

@keyframes stats-bounce {
  0% {
    transform: scale(1);
  }
  25% {
    transform: scale(1.15);
    background: var(--success-color, #10b981);
    color: white;
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

/* 成功消息样式 */
:global(.flow-success-message) {
  background: linear-gradient(135deg, var(--success-color, #10b981), #34d399) !important;
  border: none !important;
  color: white !important;
  font-weight: 500 !important;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3) !important;
}
</style>
