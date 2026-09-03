<template>
  <div class="workflow-preview">
    <!-- 基础信息 -->
    <div v-if="showInfo && processInfo" class="preview-info">
      <el-descriptions :column="4" border size="small">
        <el-descriptions-item label="流程名称">{{ processInfo.name || '未命名工作流' }}</el-descriptions-item>
        <el-descriptions-item label="流程标识">{{ processInfo.key || processInfo.id }}</el-descriptions-item>
        <el-descriptions-item label="节点数量">{{ processData.nodes.length }}</el-descriptions-item>
        <el-descriptions-item label="连接数量">{{ processData.flows.length }}</el-descriptions-item>
        <el-descriptions-item label="业务类型">
          <el-tag v-if="processInfo.businessType === 1" type="success">系统表单 ({{ processInfo.systemFormKey }})</el-tag>
          <el-tag v-else-if="processInfo.businessType === 2" type="warning">业务表单</el-tag>
          <el-tag v-else type="info">未设置</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">
          {{ processInfo.description || '暂无描述' }}
        </el-descriptions-item>
      </el-descriptions>
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
      
      <!-- 右侧：信息面板 -->
      <div class="preview-right">
        <div class="info-panel">
          <el-tabs v-model="rightTab" class="info-tabs">
            <el-tab-pane label="节点信息" name="nodes">
              <template #label>
                <span class="tab-label">
                  <el-icon><Setting /></el-icon>
                  节点信息
                </span>
              </template>
              <div class="panel-content">
                <div v-if="processData.nodes.length === 0" class="empty-state">
                  <el-empty description="暂无节点" />
                </div>
                <div v-else class="nodes-list">
                  <div 
                    v-for="node in processData.nodes" 
                    :key="node.id"
                    class="node-item"
                  >
                    <div class="node-header">
                      <div class="node-icon">
                        <el-icon>
                          <VideoPlay v-if="node.type === 'START'" />
                          <User v-else-if="node.type === 'APPROVAL'" />
                          <Setting v-else-if="node.type === 'CONDITIONAL_BRANCH'" />
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
                        <!-- 审批节点显示处理人和审批方式 -->
                        <template v-if="node.type === 'APPROVAL'">
                          <el-descriptions-item label="处理人">{{ getNodeAssigneeDisplay(node) }}</el-descriptions-item>
                          <el-descriptions-item label="审批方式">
                            <el-tag :type="node.approvalMode === 'ALL' ? 'warning' : 'success'" size="small">
                              {{ node.approvalMode === 'ALL' ? '会签' : '或签' }}
                            </el-tag>
                          </el-descriptions-item>
                        </template>
                        <!-- 抄送节点显示抄送人 -->
                        <template v-else-if="node.type === 'CC'">
                          <el-descriptions-item label="抄送人">{{ getNodeCcDisplay(node) }}</el-descriptions-item>
                        </template>
                        <!-- 其他节点兼容旧的 assignee -->
                        <el-descriptions-item v-else-if="node.assignee" label="处理人">{{ node.assignee }}</el-descriptions-item>
                        <el-descriptions-item v-if="node.assigneeType" label="处理人类型">{{ getAssigneeTypeText(node.assigneeType) }}</el-descriptions-item>
                        <el-descriptions-item v-if="node.condition" label="执行条件">{{ node.condition }}</el-descriptions-item>
                        <el-descriptions-item v-if="node.description" label="节点描述">{{ node.description }}</el-descriptions-item>
                      </el-descriptions>
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane v-if="showBusinessData && instanceData?.variables" label="业务数据" name="business">
              <template #label>
                <span class="tab-label">
                  <el-icon><Document /></el-icon>
                  业务数据
                </span>
              </template>
              <div class="panel-content">
                <div class="business-data-content">
                  <el-descriptions 
                    :column="1" 
                    border 
                    class="business-descriptions"
                  >
                    <el-descriptions-item 
                      v-for="(row, index) in getFormattedBusinessData(instanceData.variables, processData)" 
                      :key="index"
                      :label="row.fieldLabel" 
                      label-class-name="business-label"
                    >
                      <div class="business-value">
                        <span class="value-text">{{ row.fieldValue }}</span>
                        <el-text class="field-code" size="small">{{ row.fieldName }}</el-text>
                      </div>
                    </el-descriptions-item>
                  </el-descriptions>
                </div>
              </div>
            </el-tab-pane>
            
            <el-tab-pane v-if="showJson" label="JSON配置" name="json">
              <template #label>
                <span class="tab-label">
                  <el-icon><Setting /></el-icon>
                  JSON配置
                </span>
              </template>
              <div class="panel-content">
                <div class="json-header">
                  <h4>工作流配置</h4>
                  <el-button size="small" @click="copyJsonToClipboard">
                    <el-icon><Document /></el-icon>
                    复制JSON
                  </el-button>
                </div>
                <el-input
                  :model-value="JSON.stringify(processData, null, 2)"
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
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Setting, Document, VideoPlay, User, Connection 
} from '@element-plus/icons-vue'
import WorkflowCanvas from './WorkflowCanvas.vue'

// Props
interface Props {
  processData: {
    id?: string
    name?: string
    key?: string
    description?: string
    nodes: any[]
    flows: any[]
    variables?: any[]
  }
  processInfo?: {
    name?: string
    key?: string
    id?: string
    description?: string
    businessType?: number
    systemFormKey?: string
    businessFormId?: string
  }
  instanceData?: {
    id?: string
    currentNodeId?: string
    status?: number
    variables?: any
    taskHistory?: any[]
  }
  showInfo?: boolean
  showJson?: boolean
  showBusinessData?: boolean
  height?: number
}

const props = withDefaults(defineProps<Props>(), {
  showInfo: true,
  showJson: true,
  showBusinessData: true,
  height: 600
})

// 响应式数据
const rightTab = ref('nodes')

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
    id: props.processData.id || '',
    name: props.processData.name || '未命名工作流',
    nodes: (props.processData.nodes || []).map(node => {
      const nodeStatus = getNodeStatus(node.id)
      const executionInfo = getNodeExecutionInfo(node.id)
      
      return {
        id: node.id,
        type: mapNodeType(node.type),
        name: node.name,
        description: node.description || '',
        position: node.position || { x: 100, y: 100 },
        properties: {
          ...node.properties || {},
          // 添加状态信息
          status: nodeStatus,
          executionInfo: executionInfo
        }
      }
    }),
    connections: (props.processData.flows || []).map(flow => ({
      id: flow.id,
      from: flow.from,
      to: flow.to,
      name: flow.name || '',
      condition: flow.condition || null
    }))
  }
})

// 工具方法
const getNodeTypeText = (type: string) => {
  const typeMap = {
    START: '开始节点',
    END: '结束节点',
    APPROVAL: '审批节点',
    CONDITIONAL_BRANCH: '条件分支',
    TASK: '任务节点'
  }
  return typeMap[type as keyof typeof typeMap] || '未知节点'
}

const getAssigneeTypeText = (type: string) => {
  const typeMap = {
    user: '指定用户',
    role: '指定角色',
    dept: '指定部门',
    starter: '流程发起人',
    manager: '上级领导'
  }
  return typeMap[type as keyof typeof typeMap] || '未知类型'
}

// 获取审批节点处理人显示（支持多人）
const getNodeAssigneeDisplay = (node: any): string => {
  // 优先使用 approvalTargets（多人审批）
  if (node.approvalTargets && node.approvalTargets.length > 0) {
    return node.approvalTargets.map((t: any) => t.name || t.id).join('、')
  }
  // 兼容旧的单人审批
  return node.assignee || '未设置'
}

// 获取抄送节点抄送人显示（支持多人）
const getNodeCcDisplay = (node: any): string => {
  // 优先使用 ccTargets（多人抄送）
  if (node.ccTargets && node.ccTargets.length > 0) {
    return node.ccTargets.map((t: any) => t.name || t.id).join('、')
  }
  // 兼容旧的单人抄送
  return node.assignee || '未设置'
}

// 业务数据格式化
const getFormattedBusinessData = (variables: any, processDefinition: any) => {
  if (!variables || typeof variables !== 'object') {
    return []
  }

  const result = []
  const variableDefinitions = processDefinition?.variables || []
  
  for (const [fieldName, fieldValue] of Object.entries(variables)) {
    // 查找对应的变量定义
    const variableDef = variableDefinitions.find(v => v.name === fieldName)
    const fieldLabel = variableDef?.label || fieldName
    
    result.push({
      fieldName,
      fieldValue: fieldValue || '-',
      fieldLabel
    })
  }
  
  return result
}

// 获取节点状态
const getNodeStatus = (nodeId: string) => {
  if (!props.instanceData?.taskHistory) {
    return 'pending'
  }
  
  const nodeHistory = props.instanceData.taskHistory.find(h => h.nodeId === nodeId)
  if (nodeHistory) {
    if (nodeHistory.status === 1) return 'completed'
    if (nodeHistory.status === 2) return 'rejected'
  }
  
  if (nodeId === props.instanceData.currentNodeId) {
    return 'current'
  }
  
  return 'pending'
}

// 获取节点执行信息
const getNodeExecutionInfo = (nodeId: string) => {
  if (!props.instanceData?.taskHistory) {
    return null
  }
  
  const nodeHistory = props.instanceData.taskHistory.find(h => h.nodeId === nodeId)
  if (nodeHistory) {
    return {
      duration: nodeHistory.duration || 0,
      operator: nodeHistory.operator || nodeHistory.assignee,
      startTime: nodeHistory.startTime,
      endTime: nodeHistory.endTime
    }
  }
  
  return null
}

// 格式化耗时
const formatDuration = (duration: number) => {
  if (!duration) return '-'
  
  const minutes = Math.floor(duration / 60000)
  const seconds = Math.floor((duration % 60000) / 1000)
  
  if (minutes > 0) {
    return `${minutes}分${seconds}秒`
  } else {
    return `${seconds}秒`
  }
}

const copyJsonToClipboard = async () => {
  try {
    await navigator.clipboard.writeText(JSON.stringify(props.processData, null, 2))
    ElMessage.success('JSON配置已复制到剪贴板')
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败')
  }
}
</script>

<style lang="scss" scoped>
.workflow-preview {
  display: flex;
  flex-direction: column;
  height: 100%;
  
  .preview-info {
    margin-bottom: 12px;
    flex-shrink: 0;
    
    :deep(.el-descriptions) {
      margin: 0;
    }
    
    :deep(.el-descriptions__body) {
      background: var(--bg-card, #ffffff);
    }
    
    :deep(.el-descriptions-item__cell) {
      padding: 8px 12px;
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
    
    .preview-diagram {
      height: v-bind('height + "px"');
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
    
    .info-panel {
      height: v-bind('height + "px"');
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
        background: var(--bg-primary, #6366f1);
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 16px;
        flex-shrink: 0;
      }
      
      .node-info {
        flex: 1;
        
        h4 {
          margin: 0 0 4px 0;
          font-size: 16px;
          font-weight: 600;
          color: var(--text-primary, #1f2937);
        }
        
        .node-type {
          font-size: 12px;
          color: var(--text-secondary, #6b7280);
          background: var(--bg-gray-100, #f3f4f6);
          padding: 2px 8px;
          border-radius: 4px;
        }
      }
    }
    
    .node-details {
      :deep(.el-descriptions-item__label) {
        font-weight: 500;
        color: var(--text-secondary, #6b7280);
        width: 80px;
      }
      
      :deep(.el-descriptions-item__content) {
        color: var(--text-primary, #1f2937);
      }
    }
  }
}
</style>
