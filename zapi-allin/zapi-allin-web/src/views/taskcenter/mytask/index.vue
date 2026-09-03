<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Check, Close, User, CircleCheck, CircleClose, Warning, EditPen,
  Refresh, Search, ArrowRight, Connection, Document, Timer
} from '@element-plus/icons-vue'
import { workflowApi, workflowInstanceApi } from '@/api/workflow'
import { useUserStore } from '@/stores/user'
import ProcessProgressDiagram from '@/components/workflow-designer/ProcessProgressDiagram.vue'
import FormPreview from '@/components/form-designer/FormPreview.vue'
import ProcessTimeline from '@/components/workflow-designer/ProcessTimeline.vue'

// 获取用户store
const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const detailLoading = ref(false)
const taskList = ref([] as any[])
const total = ref(0)
const currentTask = ref(null as any) // 当前选中的任务（列表项）
const instanceDetail = ref(null as any) // 流程实例详情（右侧展示用）
const activeTab = ref('handle') // 当前激活的 Tab
const diagramData = ref(null as any)
const processDefinition = ref(null as any)

// 分页参数
const pagination = reactive({
  current: 1,
  pageSize: 10
})

// 搜索参数
const searchForm = reactive({
  assignee: userStore.userInfo?.id || '1' // 从当前登录用户信息中获取用户ID
})

// 审批表单数据
const approvalForm = reactive({
  action: '',
  comment: ''
})

// 审批状态控制
const approvalDialogVisible = ref(false)

const openApprovalDialog = () => {
  approvalDialogVisible.value = true
}

const closeApprovalDialog = () => {
  approvalDialogVisible.value = false
}

// 任务状态映射
const statusMap = {
  1: { text: '待处理', type: 'warning', color: '#E6A23C', bgColor: '#FDF6EC' },
  2: { text: '已完成', type: 'success', color: '#67C23A', bgColor: '#F0F9EB' },
  3: { text: '已驳回', type: 'danger', color: '#F56C6C', bgColor: '#FEF0F0' }
}

// 优先级映射
const priorityMap = {
  1: { text: '高', type: 'danger' },
  2: { text: '中', type: 'primary' },
  3: { text: '低', type: 'info' },
  0: { text: '普通', type: 'info' }
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
const getFieldDisplayName = (fieldName, processDefinition) => {
  if (!processDefinition || !processDefinition.variables) {
    return fieldName
  }

  const variable = processDefinition.variables.find(v => v.name === fieldName)
  return variable && variable.label ? variable.label : fieldName
}

// 检查是否为业务表单（基于流程定义的 businessType）
const isBusinessForm = () => {
  // businessType: 1-系统表单, 2-业务表单
  return instanceDetail.value?.processDefinition?.businessType === 2
}

// 获取表单配置
const getFormConfig = (variablesStr) => {
  // 优先从流程定义的 businessView 中获取表单配置
  if (instanceDetail.value?.processDefinition?.businessView) {
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
  const variables = parseVariables(variablesStr)
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
const getFormData = (variablesStr) => {
  const variables = parseVariables(variablesStr)
  if (!variables) return {}

  // 如果有 formData 字段，使用它
  if (variables.formData) {
    try {
      return typeof variables.formData === 'string'
        ? JSON.parse(variables.formData)
        : variables.formData
    } catch (e) {
      console.error('解析表单数据失败:', e)
      return {}
    }
  }

  // 否则过滤掉系统字段，返回业务字段
  const businessFields = {}
  Object.entries(variables).forEach(([key, value]) => {
    if (!['action', 'comment', 'formConfig', 'formData'].includes(key)) {
      businessFields[key] = value
    }
  })

  return businessFields
}

// 获取格式化的业务数据
const getFormattedBusinessData = (variablesStr, processDefinition) => {
  const variables = parseVariables(variablesStr)

  if (!variables || Object.keys(variables).length === 0) {
    return []
  }

  // 排除审批相关字段和表单字段
  const businessFields = Object.entries(variables).filter(([key]) =>
    !['action', 'comment', 'formConfig', 'formData'].includes(key)
  )

  return businessFields.map(([key, value]) => ({
    fieldName: key,
    fieldLabel: getFieldDisplayName(key, processDefinition),
    fieldValue: typeof value === 'object' ? JSON.stringify(value, null, 2) : value,
    originalValue: value
  }))
}

// 获取当前任务的审批信息
const currentTaskApprovalInfo = computed(() => {
  if (!instanceDetail.value) return null
  return getApprovalInfo(instanceDetail.value.variables, instanceDetail.value.taskHistory)
})

// 获取审批记录信息
const getApprovalInfo = (variablesStr, taskHistory) => {
  const variables = parseVariables(variablesStr)

  if (variables && variables.action) {
    return {
      action: variables.action,
      comment: variables.comment || '无备注',
      isApproved: variables.action === 'approve',
      status: variables.action === 'approve' ? 'success' : 'danger',
      statusText: variables.action === 'approve' ? '审批通过' : '审批驳回',
      icon: variables.action === 'approve' ? CircleCheck : CircleClose,
      color: variables.action === 'approve' ? '#67C23A' : '#F56C6C'
    }
  }

  if (taskHistory && taskHistory.length > 0) {
    let approvalHistory = taskHistory.find(task =>
      task.nodeType === 'APPROVAL' && task.comment && task.comment.trim() !== ''
    )

    if (!approvalHistory) {
      approvalHistory = taskHistory.find(task =>
        task.comment &&
        (task.comment.includes('审批') || task.comment.includes('拒绝') || task.comment.includes('通过') || task.comment.includes('驳回'))
      )
    }

    if (!approvalHistory) {
      approvalHistory = taskHistory.filter(task => task.comment && task.comment.trim() !== '').pop()
    }

    if (approvalHistory) {
      const comment = approvalHistory.comment || ''
      const isApproved = !comment.includes('拒绝') && !comment.includes('驳回') && !comment.includes('reject')

      return {
        action: isApproved ? 'approve' : 'reject',
        comment: comment || '无备注',
        operator: approvalHistory.operator || '系统用户',
        operatorName: approvalHistory.operatorName || approvalHistory.operator || '系统用户',
        endTime: approvalHistory.endTime,
        duration: approvalHistory.duration,
        isApproved: isApproved,
        status: isApproved ? 'success' : 'danger',
        statusText: isApproved ? '审批通过' : '审批驳回',
        icon: isApproved ? CircleCheck : CircleClose,
        color: isApproved ? '#67C23A' : '#F56C6C'
      }
    }
  }
  return null
}

// 获取任务列表
const fetchTaskList = async (keepCurrent = false) => {
  try {
    loading.value = true
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      assignee: searchForm.assignee
    }

    const response = await workflowApi.getUserTasksWithDisplayInfo(params)

    if (response && response.data) {
      taskList.value = response.data.records || []
      total.value = Number(response.data.total) || 0

      // 默认选中第一条
      if (taskList.value.length > 0) {
        if (!keepCurrent || !currentTask.value) {
          handleSelectTask(taskList.value[0])
        }
      } else {
        currentTask.value = null
        instanceDetail.value = null
      }
    }
  } catch (error) {
    console.error('获取任务列表失败:', error)
    ElMessage.error('获取任务列表失败')
  } finally {
    loading.value = false
  }
}

// 选中任务
const handleSelectTask = async (task) => {
  if (currentTask.value?.id === task.id && instanceDetail.value) return

  currentTask.value = task
  detailLoading.value = true

  // 重置审批表单
  approvalForm.action = ''
  approvalForm.comment = ''
  approvalDialogVisible.value = false

  try {
    // 获取完整详情（包含流程图数据和业务数据）
    // 注意：这里使用任务关联的流程实例ID
    const response = await workflowInstanceApi.getProcessInstanceCompleteDetail(task.processInstanceId)

    if (response && response.data) {
      const completeDetail = response.data

      // 设置实例详情
      instanceDetail.value = {
        id: completeDetail.id,
        processDefId: completeDetail.processDefId,
        processDefVersionId: completeDetail.processDefVersionId,
        processKey: completeDetail.processKey,
        businessKey: completeDetail.businessKey,
        status: completeDetail.status,
        startUser: completeDetail.startUser,
        startUserName: completeDetail.startUserName,
        startTime: completeDetail.startTime,
        endTime: completeDetail.endTime,
        variables: completeDetail.variables,
        taskHistory: completeDetail.taskHistory,
        processDefinition: completeDetail.processDefinition,
        currentNodeId: completeDetail.currentNodeId
      }

      // 设置流程图数据
      if (completeDetail.diagramData) {
        diagramData.value = completeDetail.diagramData
      }

      // 设置流程定义
      if (completeDetail.processDefinition) {
        const def = completeDetail.processDefinition
        processDefinition.value = {
          id: def.id,
          name: def.name,
          key: def.key,
          variables: def.variables || [],
          jsonDef: def.jsonDef
        }
      }

      // 默认切回处理 Tab
      activeTab.value = 'handle'
    }
  } catch (error) {
    console.error('获取任务详情失败:', error)
    ElMessage.error('获取任务详情失败')
  } finally {
    detailLoading.value = false
  }
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

    await workflowApi.completeTask(currentTask.value.id, completeTaskDTO)
    ElMessage.success(`任务${approvalForm.action === 'approve' ? '通过' : '驳回'}成功`)

    // 关闭弹窗并刷新列表
    closeApprovalDialog()
    fetchTaskList()
  } catch (error) {
    console.error('处理任务失败:', error)
    ElMessage.error('处理任务失败')
    detailLoading.value = false
  }
}

// 分页变化
const handleCurrentChange = (page) => {
  pagination.current = page
  fetchTaskList()
}

// 刷新列表
const refreshList = () => {
  pagination.current = 1
  fetchTaskList()
}

// 组件挂载
onMounted(async () => {
  if (!userStore.userInfo?.id) {
    try {
      await userStore.getUserInfo()
      if (userStore.userInfo?.id) {
        searchForm.assignee = userStore.userInfo.id.toString()
      }
    } catch (error) {
      console.warn('获取用户信息失败:', error)
    }
  }
  fetchTaskList()
})
</script>

<template>
  <div class="task-manager-container">
    <!-- 左侧列表区域 -->
    <div class="left-panel">
      <div class="panel-header">
        <h3>我的待办</h3>
        <el-button link :icon="Refresh" @click="refreshList" :loading="loading"></el-button>
      </div>

      <!-- 列表内容 -->
      <div class="task-list" v-loading="loading">
        <template v-if="taskList.length > 0">
          <div
            v-for="item in taskList"
            :key="item.id"
            class="task-item"
            :class="{ active: currentTask?.id === item.id }"
            @click="handleSelectTask(item)"
          >
            <div class="item-main">
              <div class="item-header">
                <span class="task-name" :title="item.taskName || item.nodeName">
                  {{ item.taskName || item.nodeName }}
                </span>
                <el-tag size="small" :type="priorityMap[item.priority]?.type" effect="plain" round>
                  {{ priorityMap[item.priority]?.text || '普通' }}
                </el-tag>
              </div>
              <div class="item-meta">
                <span class="meta-info">
                  <el-icon><User /></el-icon> {{ item.startUserName || item.startUser || '未知' }}
                </span>
                <span class="meta-info">
                  <el-icon><Timer /></el-icon> {{ formatRelativeTime(item.createTime) }}
                </span>
              </div>
            </div>
            <div class="item-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </template>
        <el-empty v-else description="暂无待办任务" :image-size="60"></el-empty>
      </div>

      <!-- 简单分页 -->
      <div class="list-pagination">
        <el-pagination
          small
          background
          layout="prev, pager, next"
          :total="Number(total)"
          :page-size="pagination.pageSize"
          v-model:current-page="pagination.current"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 右侧详情区域 -->
    <div class="right-panel" v-loading="detailLoading">
      <div v-if="currentTask && instanceDetail" class="detail-content">
        <!-- 顶部概览信息 -->
        <div class="detail-header">
          <div class="header-main">
            <div class="title-row">
               <h2>{{ currentTask.taskName || currentTask.nodeName }}</h2>
               <el-tag effect="dark" type="warning">待处理</el-tag>
            </div>
            <div class="meta-row">
              <div class="meta-item">
                <label>流程：</label>
                <span>{{ instanceDetail.processKey }}</span>
              </div>
              <div class="meta-item">
                <label>发起人：</label>
                <span>{{ instanceDetail.startUserName || instanceDetail.startUser }}</span>
              </div>
              <div class="meta-item">
                <label>接收时间：</label>
                <span>{{ formatTime(currentTask.createTime) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 核心内容区 Tabs -->
        <div class="detail-body">
          <el-tabs v-model="activeTab" class="custom-tabs">
            <el-tab-pane name="handle">
              <template #label>
                <span class="tab-label"><el-icon><Check /></el-icon> 任务处理</span>
              </template>
              <div class="tab-content scrollable">
                <div class="content-wrapper">
                  <!-- 业务数据 -->
                  <div class="section-block">
                    <!-- 表单预览（业务表单类型：businessType === 2） -->
                    <div v-if="isBusinessForm()" class="form-preview-container">
                      <FormPreview
                        :form-config="getFormConfig(instanceDetail.variables)"
                        :initial-data="getFormData(instanceDetail.variables)"
                      />
                    </div>

                    <!-- 业务字段列表（系统表单类型：businessType === 1） -->
                    <div v-else class="business-fields">
                      <div
                        v-for="(row, index) in getFormattedBusinessData(instanceDetail.variables, instanceDetail.processDefinition)"
                        :key="index"
                        class="field-item"
                      >
                        <span class="field-label">{{ row.fieldLabel }}</span>
                        <span class="field-value">{{ row.fieldValue }}</span>
                      </div>
                      <el-empty
                        v-if="getFormattedBusinessData(instanceDetail.variables, instanceDetail.processDefinition).length === 0"
                        description="暂无业务数据"
                        :image-size="60"
                      />
                    </div>
                  </div>

                  <!-- 流程时间线 -->
                  <ProcessTimeline
                    :task-history="instanceDetail.taskHistory || []"
                    :process-key="instanceDetail.processKey"
                    :process-definition="instanceDetail.processDefinition"
                  />
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane name="process">
              <template #label>
                <span class="tab-label"><el-icon><Connection /></el-icon> 流程进度</span>
              </template>
              <div class="tab-content">
                <ProcessProgressDiagram
                  v-if="processDefinition"
                  :process-definition="processDefinition"
                  :task-history="instanceDetail.taskHistory || []"
                  :current-node-id="instanceDetail.currentNodeId"
                  :process-status="instanceDetail.status"
                  :instance-id="instanceDetail.id"
                />
              </div>
            </el-tab-pane>
          </el-tabs>

          <!-- 底部操作栏 - 移到 tabs 外面 -->
          <div class="action-footer">
            <el-button type="primary" size="large" @click="openApprovalDialog" class="action-btn">
              <el-icon style="margin-right: 8px"><EditPen /></el-icon>
              审批
            </el-button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <el-empty description="请在左侧选择一个任务进行处理" />
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
          <el-button @click="closeApprovalDialog">取消</el-button>
          <el-button type="primary" @click="submitApproval" :loading="detailLoading">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.task-manager-container {
  display: flex;
  height: calc(100vh - 84px);
  background: var(--bg-secondary, #f8fafc);
  gap: 16px;
  padding: 16px;
  overflow: hidden;

  .left-panel {
    width: 320px;
    background: var(--bg-card);
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    box-shadow: var(--shadow-sm);

    .panel-header {
      padding: 16px;
      border-bottom: 1px solid var(--border-color);
      display: flex;
      justify-content: space-between;
      align-items: center;

      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
        color: var(--text-primary);
        background: transparent;
      }
    }

    .task-list {
      flex: 1;
      overflow-y: auto;
      padding: 8px;

      .task-item {
        padding: 12px;
        border-radius: 6px;
        cursor: pointer;
        display: flex;
        align-items: center;
        transition: all 0.2s;
        margin-bottom: 4px;
        border: 1px solid transparent;

        &:hover {
          background: var(--bg-tertiary);
        }

        &.active {
          background: var(--primary-light);
          border-color: var(--primary-color);

          .task-name {
            color: var(--primary-color) !important;
          }

          .item-arrow {
            opacity: 1;
            color: var(--primary-color);
          }
        }

        .item-main {
          flex: 1;
          min-width: 0;

          .item-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 6px;

            .task-name {
              font-weight: 500;
              color: var(--text-primary);
              font-size: 14px;
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
              margin-right: 8px;
            }
          }

          .item-meta {
            display: flex;
            gap: 12px;
            font-size: 12px;
            color: var(--text-secondary);

            .meta-info {
              display: flex;
              align-items: center;
              gap: 4px;
            }
          }
        }

        .item-arrow {
          opacity: 0;
          margin-left: 8px;
          transition: all 0.2s;
        }
      }
    }

    .list-pagination {
      padding: 12px;
      border-top: 1px solid var(--border-color);
      display: flex;
      justify-content: center;
    }
  }

  .right-panel {
    flex: 1;
    background: var(--bg-card);
    border-radius: 8px;
    box-shadow: var(--shadow-sm);
    overflow: hidden;
    display: flex;
    flex-direction: column;
    min-width: 0;

    .empty-state {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .detail-content {
      display: flex;
      flex-direction: column;
      height: 100%;

      .detail-header {
        padding: 20px 24px;
        border-bottom: 1px solid var(--border-color);
        background: var(--bg-card);

        .header-main {
          .title-row {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 12px;

            h2 {
              margin: 0;
              font-size: 20px;
              color: var(--text-primary);
              background: transparent;
            }
          }

          .meta-row {
            display: flex;
            gap: 24px;
            color: var(--text-secondary);
            font-size: 14px;

            .meta-item {
              label {
                color: var(--text-muted);
                margin-right: 4px;
              }
            }
          }
        }
      }

      .detail-body {
        flex: 1;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        position: relative; /* 为底部固定定位做准备 */

        .custom-tabs {
          height: 100%;
          display: flex;
          flex-direction: column;

          :deep(.el-tabs__header) {
            margin: 0;
            padding: 0 24px;
            border-bottom: 1px solid var(--border-color);
          }

          :deep(.el-tabs__content) {
            flex: 1;
            overflow: hidden;
            padding: 0;
          }

          :deep(.el-tab-pane) {
            height: 100%;
            overflow-y: auto; /* 允许滚动 */
          }

          .tab-content {
            padding: 24px;
            padding-bottom: 100px; /* 为底部操作栏留出空间 */
          }
        }

        .action-footer {
          position: absolute;
          bottom: 0;
          right: 0;
          left: 0;
          padding: 16px 24px;
          background: var(--bg-card);
          border-top: 1px solid var(--border-color);
          display: flex;
          justify-content: flex-end;
          gap: 12px;
          box-shadow: var(--shadow-md);
          z-index: 10;

          .action-btn {
            min-width: 100px;
          }
        }
      }
    }
  }
}

.content-wrapper {
  max-width: auto;
  margin: 0 auto;

  .section-block {
    margin-bottom: 32px;

    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);
      margin: 0;
      padding-left: 12px;
      border-left: 4px solid var(--primary-color);
    }
  }

  .section-header-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    .section-title {
      margin: 0;
    }
  }
}

.business-fields {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;

  .field-item {
    background: var(--bg-tertiary);
    padding: 16px;
    border-radius: 6px;
    border: 1px solid var(--border-color);

    .field-label {
      display: block;
      font-size: 13px;
      color: var(--text-secondary);
      margin-bottom: 8px;
    }

    .field-value {
      display: block;
      font-size: 14px;
      color: var(--text-primary);
      font-weight: 500;
      word-break: break-all;
      white-space: pre-wrap;
    }
  }
}

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

        .approve-icon { color: var(--success-color); }
        .reject-icon { color: var(--danger-color); }
      }

      &.is-checked {
        background: var(--primary-light);
        border-color: var(--primary-color);

        .radio-content {
          color: var(--primary-color);
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .task-manager-container {
    flex-direction: column;

    .left-panel {
      width: 100%;
      height: 300px;
    }

    .right-panel {
      height: 500px;
    }

    .business-fields {
      grid-template-columns: 1fr;
    }
  }
}
</style>
