<template>
  <div class="properties-panel">
    <div class="panel-header">
      <h3>属性配置</h3>
    </div>
    
    <div class="panel-content">
      <!-- 节点属性配置 -->
      <div v-if="selectedNode" class="node-properties">
        <div class="property-section">
          <h4>基础属性</h4>
          
          <div class="form-item">
            <label>节点名称</label>
            <el-input
              v-model="selectedNode.name"
              placeholder="请输入节点名称"
              @input="updateNode"
            />
          </div>
          
          <div class="form-item">
            <label>节点描述</label>
            <el-input
              v-model="selectedNode.description"
              type="textarea"
              :rows="3"
              placeholder="请输入节点描述"
              @input="updateNode"
            />
          </div>
        </div>
        
        <!-- 特殊节点属性 -->
        <div v-if="selectedNode.type === 'task'" class="property-section">
          <h4>任务属性</h4>
          
          <div class="form-item">
            <label>执行人</label>
            <el-select
              v-model="selectedNode.properties.assignee"
              placeholder="请选择执行人"
              @change="updateNode"
            >
              <el-option label="系统自动" value="system" />
              <el-option label="发起人" value="initiator" />
              <el-option label="部门负责人" value="dept_manager" />
              <el-option label="指定用户" value="specific_user" />
            </el-select>
          </div>
          
          <div class="form-item">
            <label>任务类型</label>
            <el-select
              v-model="selectedNode.properties.taskType"
              placeholder="请选择任务类型"
              @change="updateNode"
            >
              <el-option label="审批任务" value="approval" />
              <el-option label="填写任务" value="form" />
              <el-option label="通知任务" value="notification" />
              <el-option label="脚本任务" value="script" />
            </el-select>
          </div>
          
          <div class="form-item">
            <label>超时时间（小时）</label>
            <el-input-number
              v-model="selectedNode.properties.timeout"
              :min="1"
              :max="720"
              placeholder="24"
              @change="updateNode"
            />
          </div>
        </div>
        
        <div v-if="selectedNode.type === 'decision'" class="property-section">
          <h4>决策属性</h4>
          
          <div class="form-item">
            <label>判断条件</label>
            <el-input
              v-model="selectedNode.properties.condition"
              type="textarea"
              :rows="3"
              placeholder="请输入判断条件表达式"
              @input="updateNode"
            />
          </div>
          
          <div class="form-item">
            <label>条件类型</label>
            <el-select
              v-model="selectedNode.properties.conditionType"
              placeholder="请选择条件类型"
              @change="updateNode"
            >
              <el-option label="表达式" value="expression" />
              <el-option label="用户选择" value="user_choice" />
              <el-option label="数据比较" value="data_compare" />
            </el-select>
          </div>
        </div>
        
        <div v-if="selectedNode.type === 'user'" class="property-section">
          <h4>用户任务属性</h4>
          
          <div class="form-item">
            <label>表单配置</label>
            <el-select
              v-model="selectedNode.properties.formId"
              placeholder="请选择关联表单"
              @change="updateNode"
            >
              <el-option label="用户信息表单" value="form_1" />
              <el-option label="审批表单" value="form_2" />
              <el-option label="反馈表单" value="form_3" />
            </el-select>
          </div>
          
          <div class="form-item">
            <label>候选用户</label>
            <el-select
              v-model="selectedNode.properties.candidateUsers"
              multiple
              placeholder="请选择候选用户"
              @change="updateNode"
            >
              <el-option label="张三" value="user_1" />
              <el-option label="李四" value="user_2" />
              <el-option label="王五" value="user_3" />
            </el-select>
          </div>
        </div>
        
        <div v-if="selectedNode.type === 'timer'" class="property-section">
          <h4>定时器属性</h4>
          
          <div class="form-item">
            <label>定时类型</label>
            <el-radio-group
              v-model="selectedNode.properties.timerType"
              @change="updateNode"
            >
              <el-radio label="duration">持续时间</el-radio>
              <el-radio label="date">指定时间</el-radio>
              <el-radio label="cycle">循环执行</el-radio>
            </el-radio-group>
          </div>
          
          <div v-if="selectedNode.properties.timerType === 'duration'" class="form-item">
            <label>等待时间（分钟）</label>
            <el-input-number
              v-model="selectedNode.properties.duration"
              :min="1"
              :max="10080"
              @change="updateNode"
            />
          </div>
          
          <div v-if="selectedNode.properties.timerType === 'date'" class="form-item">
            <label>执行时间</label>
            <el-date-picker
              v-model="selectedNode.properties.executeTime"
              type="datetime"
              placeholder="选择执行时间"
              @change="updateNode"
            />
          </div>
        </div>
      </div>
      
      <!-- 连接属性配置 -->
      <div v-else-if="selectedConnection" class="connection-properties">
        <div class="property-section">
          <h4>连接属性</h4>
          
          <div class="form-item">
            <label>连接名称</label>
            <el-input
              v-model="selectedConnection.name"
              placeholder="请输入连接名称"
              @input="updateConnection"
            />
          </div>
          
          <div class="form-item">
            <label>条件表达式</label>
            <el-input
              v-model="selectedConnection.condition"
              type="textarea"
              :rows="3"
              placeholder="请输入条件表达式（可选）"
              @input="updateConnection"
            />
          </div>
          
          <div class="form-item">
            <el-button type="danger" @click="deleteSelectedConnection" style="width: 100%;">
              <el-icon><Delete /></el-icon>
              删除连接
            </el-button>
          </div>
          
          <div class="connection-help">
            <el-alert
              title="操作提示"
              type="info"
              :closable="false"
              show-icon
            >
              <template #default>
                <ul style="margin: 0; padding-left: 16px; font-size: 12px;">
                  <li>左键点击连接线可选中</li>
                  <li>右键点击连接线可直接删除</li>
                  <li>拖拽节点的输出点到输入点创建连接</li>
                </ul>
              </template>
            </el-alert>
          </div>
        </div>
      </div>
      
      <!-- 空状态 -->
      <div v-else class="empty-state">
        <el-empty description="请选择节点或连接进行配置" />
        
        <!-- 操作说明 -->
        <div class="operation-guide">
          <el-card shadow="never">
            <template #header>
              <span style="font-weight: 600;">操作指南</span>
            </template>
            
            <div class="guide-section">
              <h5>📦 添加节点</h5>
              <p>从左侧拖拽节点到画布</p>
            </div>
            
            <div class="guide-section">
              <h5>🔗 创建连接</h5>
              <p>1. 点击源节点的<span class="guide-highlight">输出点</span>（右侧圆点）</p>
              <p>2. 点击目标节点的<span class="guide-highlight">输入点</span>（左侧圆点）</p>
            </div>
            
            <div class="guide-section">
              <h5>⚙️ 配置属性</h5>
              <p>点击节点或连接线进行配置</p>
            </div>
            
            <div class="guide-section">
              <h5>🗑️ 删除操作</h5>
              <p>• 节点：点击节点右上角删除按钮</p>
              <p>• 连接：右键点击连接线</p>
            </div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Delete } from '@element-plus/icons-vue'

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

// Props
interface Props {
  selectedNodeId?: string
  selectedConnection?: WorkflowConnection | null
  nodes: WorkflowNode[]
}

const props = withDefaults(defineProps<Props>(), {
  selectedNodeId: '',
  selectedConnection: null,
  nodes: () => []
})

// Emits
const emit = defineEmits<{
  'node-updated': [node: WorkflowNode]
  'connection-updated': [connection: WorkflowConnection]
  'connection-deleted': [connection: WorkflowConnection]
}>()

// 计算属性
const selectedNode = computed(() => {
  return props.nodes.find(n => n.id === props.selectedNodeId) || null
})

// 方法
const updateNode = () => {
  if (selectedNode.value) {
    emit('node-updated', selectedNode.value)
  }
}

const updateConnection = () => {
  if (props.selectedConnection) {
    emit('connection-updated', props.selectedConnection)
  }
}

const deleteSelectedConnection = () => {
  if (props.selectedConnection) {
    emit('connection-deleted', props.selectedConnection)
  }
}
</script>

<style scoped lang="scss">
.properties-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
}

.panel-header {
  padding: 16px;
  border-bottom: 1px solid var(--border-color, #e5e7eb);
  
  h3 {
    margin: 0;
    font-size: 14px;
    color: var(--text-primary, #1f2937);
    font-weight: 600;
  }
}

.panel-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.property-section {
  margin-bottom: 24px;
  
  h4 {
    margin: 0 0 16px 0;
    font-size: 13px;
    color: var(--text-primary, #1f2937);
    font-weight: 600;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color, #e5e7eb);
  }
}

.form-item {
  margin-bottom: 18px;
  
  label {
    display: block;
    margin-bottom: 8px;
    font-size: 13px;
    color: var(--text-secondary, #6b7280);
    font-weight: 500;
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.operation-guide {
  margin-top: 20px;
  width: 100%;
  
  .guide-section {
    margin-bottom: 16px;
    
    h5 {
      margin: 0 0 6px 0;
      font-size: 13px;
      color: var(--text-primary, #1f2937);
      font-weight: 600;
    }
    
    p {
      margin: 2px 0;
      font-size: 12px;
      color: var(--text-secondary, #6b7280);
      line-height: 1.4;
      
      .guide-highlight {
        background: rgba(99, 102, 241, 0.1);
        color: var(--primary-color, #6366f1);
        padding: 1px 4px;
        border-radius: 3px;
        font-weight: 500;
      }
    }
  }
}

/* 表单控件样式调整 */
:deep(.el-input__wrapper) {
  border-radius: 6px;
}

:deep(.el-textarea__inner) {
  border-radius: 6px;
}

:deep(.el-select) {
  width: 100%;
}

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-date-editor) {
  width: 100%;
}
</style>
