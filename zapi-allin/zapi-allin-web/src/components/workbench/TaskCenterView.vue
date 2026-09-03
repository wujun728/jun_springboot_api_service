<template>
  <div class="task-center-view">
    <!-- 左侧任务列表 -->
    <div class="left-panel">
      <div class="panel-header">
        <h3>{{ getViewTitle() }}</h3>
        <el-button link :icon="Refresh" @click="refreshList" :loading="loading"></el-button>
      </div>

      <div class="task-list" v-loading="loading">
        <template v-if="taskList.length > 0">
          <div
            v-for="item in taskList"
            :key="item.id"
            class="task-item"
            :class="{ active: currentTask?.id === item.id, unread: !item.isRead }"
            @click="handleTaskClick(item)"
          >
            <div class="item-main">
              <div class="item-header">
                <span class="task-name" :title="item.processName || item.taskName || item.nodeName || item.name">
                  {{ item.processName || item.taskName || item.nodeName || item.name }}
                </span>
                <el-tag size="small" :type="getStatusType(item)" effect="plain" round>
                  {{ getStatusText(item) }}
                </el-tag>
              </div>
              <div class="item-meta">
                <span class="meta-info" v-if="item.startUserName || item.startUser">
                  <el-icon><User /></el-icon> {{ item.startUserName || item.startUser || '未知' }}
                </span>
                <span class="meta-info">
                  <el-icon><Timer /></el-icon> {{ formatRelativeTime(item.createTime || item.startTime || item.ccTime) }}
                </span>
              </div>
            </div>
            <div class="item-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </template>
        <el-empty v-else :description="getEmptyText()" :image-size="60"></el-empty>
      </div>

      <div class="list-pagination" v-if="total > 0">
        <el-pagination
          small
          background
          layout="prev, pager, next"
          :total="Number(total)"
          :page-size="pagination.pageSize"
          v-model:current-page="pagination.current"
          @current-change="handlePageChange"
        />
      </div>
    </div>

    <!-- 右侧详情面板 -->
    <div class="right-panel" v-loading="detailLoading">
      <div v-if="currentTask && instanceDetail" class="detail-content">
        <!-- 顶部概览信息 -->
        <div class="detail-header">
          <div class="header-main">
            <div class="title-row">
              <h2>{{ currentTask.processName || currentTask.taskName || currentTask.nodeName || currentTask.name || '未命名任务' }}</h2>
              <el-tag effect="dark" :type="getStatusType(currentTask)">
                {{ getStatusText(currentTask) }}
              </el-tag>
            </div>
            <div class="meta-row">
              <div class="meta-item" v-if="instanceDetail.processKey || instanceDetail.processName">
                <label>流程：</label>
                <span>{{ instanceDetail.processKey || instanceDetail.processName || '-' }}</span>
              </div>
              <div class="meta-item" v-if="instanceDetail.startUserName || instanceDetail.startUser">
                <label>发起人：</label>
                <span>{{ instanceDetail.startUserName || instanceDetail.startUser || '-' }}</span>
              </div>
              <div class="meta-item">
                <label>时间：</label>
                <span>{{ formatTime(currentTask.createTime || currentTask.startTime || currentTask.ccTime) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 核心内容区 Tabs -->
        <div class="detail-body">
          <el-tabs v-model="activeTab" class="custom-tabs">
            <el-tab-pane name="handle">
              <template #label>
                <span class="tab-label">
                  <el-icon><Document /></el-icon>
                  {{ activeView === 'myTasks' ? '任务处理' : '详细信息' }}
                </span>
              </template>
              <div class="tab-content scrollable">
                <div class="content-wrapper">
                  <!-- 业务数据 -->
                  <div class="section-block">
                    <div class="section-title">业务数据</div>

                    <!-- 表单预览（业务表单类型：businessType === 2） -->
                    <div v-if="isBusinessForm()" class="form-preview-container">
                      <FormPreview
                        :form-config="getFormConfig()"
                        :initial-data="getFormData()"
                      />
                    </div>

                    <!-- 业务字段列表（系统表单类型：businessType === 1） -->
                    <div v-else class="business-fields">
                      <template v-if="getFormattedBusinessData().length > 0">
                        <div
                          v-for="(row, index) in getFormattedBusinessData()"
                          :key="index"
                          class="field-item"
                        >
                          <span class="field-label">{{ row.fieldLabel }}</span>
                          <span class="field-value">{{ row.fieldValue }}</span>
                        </div>
                      </template>
                      <el-empty
                        v-else
                        description="暂无业务数据"
                        :image-size="60"
                      />
                    </div>
                  </div>

                  <!-- 流程时间线 -->
                  <div class="timeline-section" v-if="instanceDetail && instanceDetail.taskHistory && instanceDetail.taskHistory.length > 0">
                    <div class="timeline-wrapper">
                      <ProcessTimeline
                        :task-history="instanceDetail.taskHistory"
                        :process-key="instanceDetail.processKey"
                        :process-definition="instanceDetail.processDefinition"
                      />
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane name="process">
              <template #label>
                <span class="tab-label">
                  <el-icon><Connection /></el-icon> 流程进度
                </span>
              </template>
              <div class="tab-content">
                <!-- 调试信息 -->
                <div v-if="!processDefinition" style="padding: 20px;">
                  <el-alert type="warning" :closable="false" show-icon>
                    <template #title>流程图数据检查</template>
                    <div style="margin-top: 10px; font-size: 13px;">
                      <p>processDefinition: {{ processDefinition ? '存在' : '不存在' }}</p>
                      <p v-if="processDefinition">jsonDef: {{ processDefinition.jsonDef ? '存在' : '不存在' }}</p>
                      <p>请查看控制台日志获取详细信息</p>
                    </div>
                  </el-alert>
                </div>

                <template v-if="processDefinition && processDefinition.jsonDef">
                  <ProcessProgressDiagram
                    :process-definition="processDefinition"
                    :task-history="instanceDetail.taskHistory || []"
                    :current-node-id="instanceDetail.currentNodeId"
                    :process-status="instanceDetail.status"
                    :instance-id="instanceDetail.id"
                  />
                </template>
                <el-empty v-else-if="processDefinition && !processDefinition.jsonDef" description="流程定义缺少 jsonDef 数据" :image-size="60" />
                <el-empty v-else description="暂无流程图数据" :image-size="60" />
              </div>
            </el-tab-pane>
          </el-tabs>

          <!-- 底部操作栏 -->
          <div class="action-footer" v-if="activeView === 'myTasks' || activeView === 'myApplications'">
            <!-- 我的待办：显示审批按钮 -->
            <template v-if="activeView === 'myTasks'">
              <el-button type="primary" size="large" @click="handleApproval" class="action-btn">
                <el-icon style="margin-right: 8px"><EditPen /></el-icon>
                审批
              </el-button>
            </template>
            
            <!-- 我的申请：显示撤回按钮（仅进行中的流程） -->
            <template v-if="activeView === 'myApplications' && instanceDetail && instanceDetail.status === 1">
              <el-button type="danger" size="large" @click="handleWithdraw" class="action-btn">
                <el-icon style="margin-right: 8px"><RefreshLeft /></el-icon>
                撤回
              </el-button>
            </template>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <el-empty description="请在左侧选择一个任务查看详情" />
      </div>
    </div>

    <!-- 审批弹窗 -->
    <el-dialog
      v-model="approvalDialogVisible"
      title="审批操作"
      width="500px"
      :close-on-click-modal="false"
      append-to-body
    >
      <el-form :model="approvalForm" label-position="top" class="approval-form-dialog">
        <el-form-item label="审批结果" required>
          <el-radio-group v-model="approvalForm.action" class="approval-radio-group">
            <el-radio value="approve" size="large" border>
              <div class="radio-content">
                <el-icon class="approve-icon"><Check /></el-icon>
                <span>通过</span>
              </div>
            </el-radio>
            <el-radio value="reject" size="large" border>
              <div class="radio-content">
                <el-icon class="reject-icon"><Close /></el-icon>
                <span>驳回</span>
              </div>
            </el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审批意见" required>
          <el-input
            v-model="approvalForm.comment"
            type="textarea"
            :rows="4"
            placeholder="请填写审批意见..."
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="approvalDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitApproval" :loading="detailLoading">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 撤回弹窗 -->
    <el-dialog v-model="revokeDialogVisible" title="撤回流程" width="450px" :close-on-click-modal="false">
      <div class="revoke-dialog-content">
        <p class="revoke-tip">
          <el-icon color="#E6A23C" :size="18">
            <Warning />
          </el-icon>
          <span>撤回后流程将终止，无法恢复，请确认！</span>
        </p>
        <el-form label-position="top">
          <el-form-item label="撤回原因" required>
            <el-input
              v-model="revokeForm.reason"
              type="textarea"
              :rows="3"
              placeholder="请输入撤回原因"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <el-button @click="revokeDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="handleConfirmRevoke" :loading="revokeLoading">
          确认撤回
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Timer, User, ArrowRight, Document, Connection, EditPen, RefreshLeft, Check, Close, Warning } from '@element-plus/icons-vue'
import { workflowApi, workflowInstanceApi } from '@/api/workflow'
import { useUserStore } from '@/stores/user'
import ProcessProgressDiagram from '@/components/workflow-designer/ProcessProgressDiagram.vue'
import ProcessTimeline from '@/components/workflow-designer/ProcessTimeline.vue'
import FormPreview from '@/components/form-designer/FormPreview.vue'

const userStore = useUserStore()

const props = defineProps({
  activeView: {
    type: String,
    required: true
  },
  appId: {
    type: String,
    required: false,
    default: null
  }
})

const loading = ref(false)
const detailLoading = ref(false)
const taskList = ref([])
const total = ref(0)
const currentTask = ref(null)
const instanceDetail = ref(null)
const processDefinition = ref(null)
const activeTab = ref('handle')

const pagination = reactive({
  current: 1,
  pageSize: 10
})

// 审批表单数据
const approvalForm = reactive({
  action: '',
  comment: ''
})

// 审批对话框状态
const approvalDialogVisible = ref(false)

// 撤回弹窗相关
const revokeDialogVisible = ref(false)
const revokeLoading = ref(false)
const revokeForm = reactive({
  reason: ''
})

// 获取视图标题
const getViewTitle = () => {
  const map = {
    myApplications: '我的申请',
    myTasks: '我的待办',
    myCompleted: '我的已办',
    ccMe: '抄送我的'
  }
  return map[props.activeView] || '任务中心'
}

// 获取空状态文本
const getEmptyText = () => {
  const map = {
    myApplications: '暂无申请记录',
    myTasks: '暂无待办任务',
    myCompleted: '暂无已办任务',
    ccMe: '暂无抄送记录'
  }
  return map[props.activeView] || '暂无数据'
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 格式化相对时间
const formatRelativeTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now.getTime() - date.getTime()

  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour

  if (diff < minute) return '刚刚'
  if (diff < hour) return `${Math.floor(diff / minute)}分钟前`
  if (diff < day) return `${Math.floor(diff / hour)}小时前`
  if (diff < day * 7) return `${Math.floor(diff / day)}天前`

  return formatTime(time).split(' ')[0]
}

// 获取状态类型
const getStatusType = (item) => {
  if (props.activeView === 'myTasks') return 'warning'
  if (props.activeView === 'myCompleted') return 'success'
  if (props.activeView === 'ccMe') return item.isRead ? 'info' : 'warning'

  const map = {
    1: 'warning',
    2: 'success',
    3: 'danger',
    4: 'info'
  }
  return map[item.status] || 'info'
}

// 获取状态文本
const getStatusText = (item) => {
  if (props.activeView === 'myTasks') return '待处理'
  if (props.activeView === 'myCompleted') return '已完成'
  if (props.activeView === 'ccMe') return item.isRead ? '已读' : '未读'

  const map = {
    1: '进行中',
    2: '已完成',
    3: '已驳回',
    4: '已撤回'
  }
  return map[item.status] || '未知'
}

// 解析变量字符串
const parseVariables = (variablesStr) => {
  if (!variablesStr) return {}
  try {
    return typeof variablesStr === 'string' ? JSON.parse(variablesStr) : variablesStr
  } catch (e) {
    console.error('解析变量失败:', e)
    return {}
  }
}

// 获取字段显示名称
const getFieldDisplayName = (fieldName) => {
  if (!instanceDetail.value || !instanceDetail.value.processDefinition || !instanceDetail.value.processDefinition.variables) {
    return fieldName
  }

  const variable = instanceDetail.value.processDefinition.variables.find(v => v.name === fieldName)
  return variable && variable.label ? variable.label : fieldName
}

// 检查是否为业务表单
const isBusinessForm = () => {
  // businessType: 1-系统表单, 2-业务表单
  return instanceDetail.value?.processDefinition?.businessType === 2
}

// 获取表单配置
const getFormConfig = () => {
  if (!instanceDetail.value) return null

  // 优先从流程定义的 businessView 中获取表单配置
  if (instanceDetail.value.processDefinition?.businessView) {
    try {
      const businessView = instanceDetail.value.processDefinition.businessView
      return typeof businessView === 'string'
        ? JSON.parse(businessView)
        : businessView
    } catch (e) {
      console.error('解析 businessView 失败:', e)
    }
  }

  // 兼容旧方式：从 variables 中获取 formConfig
  const variables = parseVariables(instanceDetail.value.variables)
  if (variables && variables.formConfig) {
    try {
      return typeof variables.formConfig === 'string'
        ? JSON.parse(variables.formConfig)
        : variables.formConfig
    } catch (e) {
      console.error('解析 formConfig 失败:', e)
    }
  }

  return null
}

// 获取表单数据
const getFormData = () => {
  console.log('=== getFormData 开始 ===')
  console.log('instanceDetail.value:', instanceDetail.value)
  
  if (!instanceDetail.value) {
    console.log('instanceDetail.value 为空')
    return {}
  }

  console.log('instanceDetail.value.variables:', instanceDetail.value.variables)
  console.log('variables 类型:', typeof instanceDetail.value.variables)
  
  const variables = parseVariables(instanceDetail.value.variables)
  console.log('解析后的 variables:', variables)
  
  if (!variables) {
    console.log('variables 解析后为空')
    return {}
  }

  // 如果有 formData 字段，使用它
  if (variables.formData) {
    console.log('找到 formData 字段:', variables.formData)
    try {
      const result = typeof variables.formData === 'string'
        ? JSON.parse(variables.formData)
        : variables.formData
      console.log('返回 formData:', result)
      return result
    } catch (e) {
      console.error('解析表单数据失败:', e)
      return {}
    }
  }

  // 否则过滤掉系统字段，返回业务字段
  const businessFields = {}
  Object.entries(variables).forEach(([key, value]) => {
    if (!['action', 'comment', 'formConfig', 'formData', 'approvalHistory'].includes(key)) {
      businessFields[key] = value
    }
  })

  console.log('返回业务字段:', businessFields)
  console.log('=== getFormData 结束 ===')
  return businessFields
}

// 获取格式化的业务数据
const getFormattedBusinessData = () => {
  if (!instanceDetail.value || !instanceDetail.value.variables) {
    console.log('没有实例详情或变量数据')
    return []
  }

  const variables = parseVariables(instanceDetail.value.variables)
  console.log('解析后的变量:', variables)

  if (!variables || Object.keys(variables).length === 0) {
    console.log('变量为空')
    return []
  }

  // 排除审批相关字段和表单字段
  const excludeKeys = ['action', 'comment', 'formConfig', 'formData', 'approvalHistory']
  const businessFields = Object.entries(variables).filter(([key]) =>
    !excludeKeys.includes(key)
  )

  console.log('业务字段:', businessFields)

  const result = businessFields.map(([key, value]) => {
    // 使用流程定义中的字段标签
    const fieldLabel = getFieldDisplayName(key)

    // 格式化值
    let displayValue = value
    if (value === null || value === undefined) {
      displayValue = '-'
    } else if (typeof value === 'object') {
      try {
        displayValue = JSON.stringify(value, null, 2)
      } catch (e) {
        displayValue = String(value)
      }
    } else if (typeof value === 'boolean') {
      displayValue = value ? '是' : '否'
    } else {
      displayValue = String(value)
    }

    return {
      fieldName: key,
      fieldLabel: fieldLabel,
      fieldValue: displayValue,
      originalValue: value
    }
  })

  console.log('格式化后的业务数据:', result)
  return result
}

// 加载我的申请
const loadMyApplications = async () => {
  loading.value = true
  try {
    const userId = userStore.userInfo?.id
    if (!userId) return

    const response = await workflowInstanceApi.getProcessInstancesWithDisplayInfo({
      startUser: userId,
      current: pagination.current,
      size: pagination.pageSize,
      appId: props.appId // 添加应用ID参数
    })

    if (response.code === 200 && response.data) {
      taskList.value = response.data.records || []
      total.value = response.data.total || 0

      // 自动选中第一条记录
      if (taskList.value.length > 0 && !currentTask.value) {
        await handleTaskClick(taskList.value[0])
      }
    } else {
      ElMessage.error(response.message || '加载失败')
    }
  } catch (error) {
    console.error('加载我的申请失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 加载我的待办
const loadMyTasks = async () => {
  loading.value = true
  try {
    const userId = userStore.userInfo?.id
    if (!userId) return

    const response = await workflowApi.getUserTasksWithDisplayInfo({
      assignee: userId,
      current: pagination.current,
      size: pagination.pageSize,
      appId: props.appId // 添加应用ID参数
    })

    if (response.code === 200 && response.data) {
      taskList.value = response.data.records || []
      total.value = response.data.total || 0

      // 自动选中第一条记录
      if (taskList.value.length > 0 && !currentTask.value) {
        await handleTaskClick(taskList.value[0])
      }
    } else {
      ElMessage.error(response.message || '加载失败')
    }
  } catch (error) {
    console.error('加载我的待办失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 加载我的已办
const loadMyCompleted = async () => {
  loading.value = true
  try {
    const userId = userStore.userInfo?.id
    if (!userId) return

    const response = await workflowApi.getUserCompletedTasks({
      operator: userId,
      current: pagination.current,
      size: pagination.pageSize,
      appId: props.appId // 添加应用ID参数
    })

    if (response.code === 200 && response.data) {
      taskList.value = response.data.records || []
      total.value = response.data.total || 0

      // 自动选中第一条记录
      if (taskList.value.length > 0 && !currentTask.value) {
        await handleTaskClick(taskList.value[0])
      }
    } else {
      ElMessage.error(response.message || '加载失败')
    }
  } catch (error) {
    console.error('加载我的已办失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 加载抄送我的
const loadCcMe = async () => {
  loading.value = true
  try {
    const userId = userStore.userInfo?.id
    if (!userId) return

    const response = await workflowApi.getUserCcRecords({
      userId: userId,
      current: pagination.current,
      size: pagination.pageSize,
      appId: props.appId // 添加应用ID参数
    })

    if (response.code === 200 && response.data) {
      taskList.value = response.data.records || []
      total.value = response.data.total || 0

      // 自动选中第一条记录
      if (taskList.value.length > 0 && !currentTask.value) {
        await handleTaskClick(taskList.value[0])
      }
    } else {
      ElMessage.error(response.message || '加载失败')
    }
  } catch (error) {
    console.error('加载抄送我的失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 加载任务详情
const loadTaskDetail = async (task) => {
  detailLoading.value = true
  try {
    // 获取流程实例ID
    let instanceId = null

    // 根据不同视图获取实例ID
    if (props.activeView === 'myApplications') {
      // 我的申请：直接使用任务ID作为实例ID
      instanceId = task.id
    } else if (props.activeView === 'myTasks') {
      // 我的待办：使用 processInstanceId
      instanceId = task.processInstanceId || task.instanceId
    } else if (props.activeView === 'myCompleted') {
      // 我的已办：使用 processInstanceId
      instanceId = task.processInstanceId || task.instanceId
    } else if (props.activeView === 'ccMe') {
      // 抄送我的：使用 instanceId
      instanceId = task.instanceId || task.processInstanceId
    }

    if (!instanceId) {
      console.error('无法获取流程实例ID', task)
      ElMessage.error('无法获取流程实例ID')
      return
    }

    console.log('加载任务详情，实例ID:', instanceId)

    // 使用完整详情接口获取流程实例详情（包含 jsonDef）
    const response = await workflowInstanceApi.getProcessInstanceCompleteDetail(instanceId)

    if (response.code === 200 && response.data) {
      const detail = response.data

      // 设置实例详情
      instanceDetail.value = {
        id: detail.id,
        processDefId: detail.processDefId,
        processDefVersionId: detail.processDefVersionId,
        processKey: detail.processKey,
        processName: detail.processName,
        businessKey: detail.businessKey,
        status: detail.status,
        startUser: detail.startUser,
        startUserName: detail.startUserName,
        startTime: detail.startTime,
        endTime: detail.endTime,
        variables: detail.variables,
        taskHistory: detail.taskHistory || [],
        processDefinition: detail.processDefinition,
        currentNodeId: detail.currentNodeId
      }

      console.log('实例详情加载成功:', instanceDetail.value)

      // 设置流程定义（用于流程图）
      if (detail.processDefinition) {
        const def = detail.processDefinition
        console.log('原始流程定义:', def)
        console.log('def.jsonDef:', def.jsonDef)

        // 直接使用后端返回的 jsonDef
        if (def.jsonDef) {
          processDefinition.value = {
            id: def.id,
            name: def.name,
            key: def.key,
            version: def.version,
            jsonDef: def.jsonDef, // 直接使用后端返回的 jsonDef
            businessType: def.businessType,
            businessView: def.businessView,
            variables: def.variables || []
          }
          console.log('✅ 流程定义加载成功:', processDefinition.value)
        } else {
          console.warn('❌ 流程定义缺少 jsonDef 字段')
          console.log('尝试从 processConfig 解析...')

          // 如果没有 jsonDef，尝试从 processConfig 解析
          let processData = null
          if (def.processConfig) {
            try {
              console.log('processConfig 类型:', typeof def.processConfig)

              const config = typeof def.processConfig === 'string'
                ? JSON.parse(def.processConfig)
                : def.processConfig

              console.log('解析后的 config:', config)

              // processConfig 可能直接包含流程定义，或者在 jsonDef 字段中
              if (config.nodes) {
                processData = config
                console.log('从 config.nodes 获取流程数据')
              } else if (config.jsonDef) {
                processData = typeof config.jsonDef === 'string'
                  ? JSON.parse(config.jsonDef)
                  : config.jsonDef
                console.log('从 config.jsonDef 获取流程数据')
              }

              if (processData && processData.nodes && Array.isArray(processData.nodes) && processData.nodes.length > 0) {
                processDefinition.value = {
                  id: def.id,
                  name: def.name,
                  key: def.key,
                  version: def.version,
                  jsonDef: JSON.stringify(processData),
                  businessType: def.businessType,
                  businessView: def.businessView,
                  variables: def.variables || []
                }
                console.log('✅ 从 processConfig 解析流程定义成功')
              } else {
                console.error('❌ processConfig 中没有有效的流程数据')
                processDefinition.value = null
              }
            } catch (e) {
              console.error('解析 processConfig 失败:', e)
              processDefinition.value = null
            }
          } else {
            console.error('❌ processConfig 也为空')
            processDefinition.value = null
          }
        }
      } else {
        console.warn('❌ 流程定义为空')
        processDefinition.value = null
      }
    } else {
      console.error('加载详情失败:', response)
      ElMessage.error(response.message || '加载详情失败')
      instanceDetail.value = null
      processDefinition.value = null
    }
  } catch (error) {
    console.error('加载任务详情失败:', error)
    ElMessage.error('加载详情失败')
    instanceDetail.value = null
    processDefinition.value = null
  } finally {
    detailLoading.value = false
  }
}

// 处理任务点击
const handleTaskClick = async (task) => {
  currentTask.value = task
  activeTab.value = 'handle'
  await loadTaskDetail(task)
}

// 打开审批对话框
const handleApproval = () => {
  // 重置表单
  approvalForm.action = ''
  approvalForm.comment = ''
  approvalDialogVisible.value = true
}

// 提交审批
const submitApproval = async () => {
  if (!currentTask.value) {
    ElMessage.error('任务信息无效')
    return
  }

  if (!approvalForm.action) {
    ElMessage.warning('请选择审批结果')
    return
  }

  if (!approvalForm.comment.trim()) {
    ElMessage.warning('请填写审批意见')
    return
  }

  try {
    detailLoading.value = true
    
    const completeTaskDTO = {
      taskId: currentTask.value.id,
      operator: userStore.userInfo?.id || '1',
      comment: approvalForm.comment.trim(),
      approved: approvalForm.action === 'approve',
      variables: {
        action: approvalForm.action,
        comment: approvalForm.comment.trim()
      }
    }

    console.log('提交审批:', completeTaskDTO)

    const response = await workflowApi.completeTask(currentTask.value.id, completeTaskDTO)
    
    if (response.code === 200) {
      ElMessage.success(`任务${approvalForm.action === 'approve' ? '通过' : '驳回'}成功`)
      approvalDialogVisible.value = false
      // 刷新列表
      refreshList()
    } else {
      ElMessage.error(response.message || '处理任务失败')
      detailLoading.value = false
    }
  } catch (error) {
    console.error('处理任务失败:', error)
    const errorMsg = error.response?.data?.message || error.message || '处理任务失败'
    ElMessage.error(errorMsg)
    detailLoading.value = false
  }
}

// 打开撤回弹窗
const handleWithdraw = () => {
  revokeForm.reason = ''
  revokeDialogVisible.value = true
}

// 确认撤回
const handleConfirmRevoke = async () => {
  if (!revokeForm.reason.trim()) {
    ElMessage.warning('请输入撤回原因')
    return
  }

  try {
    revokeLoading.value = true

    const data = {
      reason: revokeForm.reason,
      operator: String(userStore.userInfo?.id || '')
    }

    console.log('撤回流程，实例ID:', instanceDetail.value.id)
    console.log('撤回数据:', data)

    await workflowInstanceApi.revokeProcessInstance(instanceDetail.value.id, data)

    ElMessage.success('流程撤回成功')
    revokeDialogVisible.value = false

    // 刷新列表
    refreshList()
  } catch (error) {
    console.error('撤回流程失败:', error)
    ElMessage.error(error.response?.data?.message || '撤回流程失败')
  } finally {
    revokeLoading.value = false
  }
}

// 刷新列表
const refreshList = () => {
  pagination.current = 1
  currentTask.value = null
  instanceDetail.value = null
  processDefinition.value = null
  loadData()
}

// 处理分页变化
const handlePageChange = () => {
  loadData()
}

// 加载数据
const loadData = () => {
  switch (props.activeView) {
    case 'myApplications':
      loadMyApplications()
      break
    case 'myTasks':
      loadMyTasks()
      break
    case 'myCompleted':
      loadMyCompleted()
      break
    case 'ccMe':
      loadCcMe()
      break
  }
}

// 监听视图切换
watch(() => props.activeView, () => {
  // 重置状态
  pagination.current = 1
  currentTask.value = null
  instanceDetail.value = null
  processDefinition.value = null
  activeTab.value = 'handle'

  // 加载对应数据
  loadData()
}, { immediate: true })
</script>

<style lang="scss" scoped>
.task-center-view {
  flex: 1;
  display: flex;
  overflow: hidden;
  background: var(--bg-primary);
}

// 左侧面板
.left-panel {
  width: 360px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-color);
}

.panel-header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  border-bottom: 1px solid var(--border-color);
  flex-shrink: 0;

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    margin: 0;
  }
}

.task-list {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
}

.task-item {
  display: flex;
  align-items: center;
  gap: 12px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 14px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    border-color: var(--primary-color);
    box-shadow: var(--shadow-sm);
  }

  &.active {
    border-color: var(--primary-color);
    background: var(--primary-light);
  }

  &.unread {
    position: relative;

    &::before {
      content: '';
      position: absolute;
      left: 0;
      top: 0;
      bottom: 0;
      width: 3px;
      background: var(--primary-color);
      border-radius: 8px 0 0 8px;
    }
  }

  &:last-child {
    margin-bottom: 0;
  }

  .item-main {
    flex: 1;
    min-width: 0;
  }

  .item-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 8px;
    gap: 8px;

    .task-name {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-primary);
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
  }

  .item-meta {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 12px;
    color: var(--text-secondary);

    .meta-info {
      display: flex;
      align-items: center;
      gap: 4px;

      .el-icon {
        font-size: 13px;
      }
    }
  }

  .item-arrow {
    flex-shrink: 0;
    color: var(--text-tertiary);
    font-size: 14px;
  }
}

.list-pagination {
  padding: 12px;
  border-top: 1px solid var(--border-color);
  display: flex;
  justify-content: center;
  flex-shrink: 0;
}

// 右侧面板
.right-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--bg-primary);
}

.detail-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.detail-header {
  flex-shrink: 0;
  padding: 24px;
  border-bottom: 1px solid var(--border-color);
  background: var(--bg-card);

  .header-main {
    .title-row {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 16px;

      h2 {
        font-size: 20px;
        font-weight: 600;
        color: var(--text-primary);
        margin: 0;
        flex: 1;
      }
    }

    .meta-row {
      display: flex;
      align-items: center;
      gap: 24px;
      flex-wrap: wrap;

      .meta-item {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 14px;

        label {
          color: var(--text-secondary);
          font-weight: 500;
        }

        span {
          color: var(--text-primary);
        }
      }
    }
  }
}

.detail-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .custom-tabs {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    :deep(.el-tabs__header) {
      margin: 0;
      padding: 0 24px;
      border-bottom: 1px solid var(--border-color);
      background: var(--bg-card);
    }

    :deep(.el-tabs__content) {
      flex: 1;
      overflow: hidden;
    }

    .tab-label {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 14px;
    }
  }

  .tab-content {
    height: 100%;
    overflow-y: auto;
    padding: 24px;

    &.scrollable {
      overflow-y: auto;
    }

    .content-wrapper {
      max-width: 1000px;
      margin: 0 auto;
    }
  }

  .section-block {
    background: var(--bg-card);
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;

    &:last-child {
      margin-bottom: 0;
    }

    .section-title {
      font-size: 15px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 16px;
      padding-bottom: 12px;
      border-bottom: 1px solid var(--border-color);
    }
  }

  .form-preview-container {
    :deep(.form-preview) {
      background: transparent;
      padding: 0;
    }
  }

  .business-fields {
    .field-item {
      display: flex;
      align-items: flex-start;
      padding: 12px 0;
      border-bottom: 1px solid var(--border-color);

      &:last-child {
        border-bottom: none;
      }

      .field-label {
        width: 140px;
        flex-shrink: 0;
        font-size: 14px;
        font-weight: 500;
        color: var(--text-secondary);
        padding-right: 16px;
      }

      .field-value {
        flex: 1;
        font-size: 14px;
        color: var(--text-primary);
        word-break: break-all;
      }
    }
  }

  // 流程历史区域
  .timeline-section {
    margin-top: 20px;

    .timeline-wrapper {
      max-height: 400px;
      overflow-y: auto;
      overflow-x: hidden;
      padding-right: 4px;
      
      // 自定义滚动条样式
      &::-webkit-scrollbar {
        width: 6px;
      }
      
      &::-webkit-scrollbar-track {
        background: var(--bg-tertiary);
        border-radius: 3px;
      }
      
      &::-webkit-scrollbar-thumb {
        background: var(--border-color);
        border-radius: 3px;
        
        &:hover {
          background: var(--text-tertiary);
        }
      }
    }
  }

  .action-footer {
    padding: 16px 24px;
    border-top: 1px solid var(--border-color);
    background: var(--bg-card);
    display: flex;
    justify-content: center;
    gap: 12px;
    flex-shrink: 0;

    .action-btn {
      min-width: 120px;
    }
  }
}

.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

// 审批弹窗样式
.approval-form-dialog {
  .approval-radio-group {
    display: flex;
    gap: 20px;
    width: 100%;

    .el-radio {
      flex: 1;
      margin-right: 0;

      :deep(.el-radio__input) {
        display: none;
      }

      :deep(.el-radio__label) {
        padding: 0;
        width: 100%;
      }

      .radio-content {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 6px;
        padding: 4px 0;

        .approve-icon {
          color: var(--el-color-success);
          font-size: 18px;
        }
        
        .reject-icon {
          color: var(--el-color-danger);
          font-size: 18px;
        }
      }

      &.is-checked {
        background: var(--el-color-primary-light-9);
        border-color: var(--el-color-primary);

        .radio-content {
          color: var(--el-color-primary);
          font-weight: 600;
        }
      }
    }
  }
}

// 撤回弹窗样式
.revoke-dialog-content {
  .revoke-tip {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 16px;
    background: var(--el-color-warning-light-9);
    border-radius: 6px;
    margin-bottom: 16px;
    color: var(--el-color-warning);
    font-size: 14px;
  }
}
</style>
