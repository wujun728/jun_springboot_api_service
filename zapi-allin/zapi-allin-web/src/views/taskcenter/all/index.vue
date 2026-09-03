<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Connection, Timer, User,
  Document, CircleCheck, CircleClose, Search, Refresh,
  ArrowRight, RefreshLeft, Warning
} from '@element-plus/icons-vue'
import { workflowInstanceApi } from '@/api/workflow'
import { useUserStore } from '@/stores/user'
import ProcessProgressDiagram from '@/components/workflow-designer/ProcessProgressDiagram.vue'
import FormPreview from '@/components/form-designer/FormPreview.vue'
import ProcessTimeline from '@/components/workflow-designer/ProcessTimeline.vue'

// 获取用户store
const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const detailLoading = ref(false)
const instanceList = ref([])
const total = ref(0)
const activeTab = ref('business') // 当前激活的 Tab
const currentInstance = ref(null as any) // 当前选中的列表项（摘要信息）
const instanceDetail = ref(null as any) // 当前选中项的完整详情（接口获取）
const diagramData = ref(null as any)
const processDefinition = ref(null as any)
const taskHistory = ref([] as any[])

// 分页参数
const pagination = reactive({
  current: 1,
  pageSize: 10
})

// 搜索参数
const searchForm = reactive({
  processKey: '',      // 任务名称
  status: null as number | null,        // 状态
})

// 流程实例状态映射
const statusMap = {
  1: { text: '运行中', type: 'primary', color: '#409EFF', bgColor: '#ECF5FF' },      // 蓝色 - 进行中
  2: { text: '已结束', type: 'success', color: '#67C23A', bgColor: '#F0F9EB' },      // 绿色 - 成功完成
  3: { text: '已暂停', type: 'warning', color: '#E6A23C', bgColor: '#FDF6EC' },      // 橙色 - 暂停/警告
  4: { text: '已撤回', type: 'danger', color: '#F56C6C', bgColor: '#FEF0F0' }        // 红色 - 已取消
}

// 撤回弹窗相关
const revokeDialogVisible = ref(false)
const revokeLoading = ref(false)
const revokeForm = reactive({
  reason: ''
})

// 刷新列表
const refreshList = () => {
  pagination.current = 1
  fetchInstanceList()
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

// 格式化相对时间 (例如: 2小时前)
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

  return formatTime(time).split(' ')[0] // 只显示日期
}

// 获取任务名称
const getTaskName = (row) => {
  return row.taskName || row.processKey || '未知任务'
}

// 获取优先级文本和颜色
const getPriorityInfo = (priority) => {
  const map = {
    1: { text: '高', type: 'danger', color: '#F56C6C' },
    2: { text: '中', type: 'warning', color: '#E6A23C' },
    3: { text: '低', type: 'success', color: '#67C23A' },
    0: { text: '普通', type: 'info', color: '#909399' }
  }
  return map[priority] || map[0]
}

const getTaskPriority = (row) => {
  return row.priority !== undefined ? row.priority : 0
}

// 获取流程实例列表
const fetchInstanceList = async (keepCurrent = false) => {
  try {
    loading.value = true

    // 确保用户信息已加载
    if (!userStore.userInfo?.id) {
      await userStore.getUserInfo()
    }

    const params = {
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      startUser: userStore.userInfo?.id ? String(userStore.userInfo.id) : '', // 传入当前用户ID（转为字符串）
      ...searchForm
    }

    const response = await workflowInstanceApi.getProcessInstancesWithDisplayInfo(params)

    if (response && response.data) {
      instanceList.value = response.data.records || []
      total.value = Number(response.data.total) || 0

      // 如果列表不为空，且没有选中项（或者不保持当前选中），默认选中第一条
      if (instanceList.value.length > 0) {
        if (!keepCurrent || !currentInstance.value) {
          handleSelectInstance(instanceList.value[0])
        }
      } else {
        currentInstance.value = null
        instanceDetail.value = null
      }
    }
  } catch (error) {
    console.error('获取流程实例列表失败:', error)
    ElMessage.error('获取流程实例列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  fetchInstanceList()
}

// 重置搜索
const handleReset = () => {
  searchForm.processKey = ''
  searchForm.status = null
  pagination.current = 1
  fetchInstanceList()
}

// 分页变化
const handleCurrentChange = (page) => {
  pagination.current = page
  fetchInstanceList()
}

// 选中任务实例，加载详情
const handleSelectInstance = async (instance) => {
  if (currentInstance.value?.id === instance.id && instanceDetail.value) return

  currentInstance.value = instance
  detailLoading.value = true

  try {
    // 获取完整详情
    const response = await workflowInstanceApi.getProcessInstanceCompleteDetail(instance.id)

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

      // 默认切回流程进度Tab
      // activeTab.value = 'process'
    }
  } catch (error) {
    console.error('获取任务详情失败:', error)
    ElMessage.error('获取任务详情失败')
  } finally {
    detailLoading.value = false
  }
}

// 解析业务变量
const parseVariables = (variablesStr) => {
  if (!variablesStr) return {}
  try {
    return JSON.parse(variablesStr)
  } catch (error) {
    console.error('解析变量数据失败:', error)
    return {}
  }
}

// 获取字段显示名称
const getFieldDisplayName = (fieldName, processDefinition) => {
  if (!processDefinition || !processDefinition.variables) return fieldName
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
      let formConfig = null
      if (typeof businessView === 'string') {
        formConfig = JSON.parse(businessView)
      } else {
        formConfig = businessView
      }
      
      formConfig.components.forEach(component => {
        // 添加提示信息：该表单已结束，无法修改
        if (component.type === 'upload') {
          component.disabled = true
        }
      })
      return formConfig
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
  if (!variables || Object.keys(variables).length === 0) return []

  const businessFields = Object.entries(variables).filter(([key]) =>
    !['action', 'comment', 'formConfig', 'formData'].includes(key)
  )

  return businessFields.map(([key, value]) => ({
    fieldName: key,
    fieldLabel: getFieldDisplayName(key, processDefinition),
    fieldValue: typeof value === 'object' ? JSON.stringify(value, null, 2) : value
  }))
}

// 获取审批信息
const currentApprovalInfo = computed(() => {
  if (!instanceDetail.value) return null
  return getApprovalInfo(instanceDetail.value.variables, instanceDetail.value.taskHistory)
})

const getApprovalInfo = (variablesStr, taskHistory) => {
  const variables = parseVariables(variablesStr)

  if (variables && variables.action) {
    return createApprovalResult(variables.action, variables.comment, null)
  }

  if (taskHistory && taskHistory.length > 0) {
    // 查找审批节点记录
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
      return createApprovalResult(isApproved ? 'approve' : 'reject', comment, approvalHistory)
    }
  }
  return null
}

const createApprovalResult = (action, comment, history) => {
  const isApproved = action === 'approve' || action === true
  return {
    action: isApproved ? 'approve' : 'reject',
    comment: comment || '无备注',
    operator: history?.operator || '系统用户',
    operatorName: history?.operatorName || history?.operator || '系统用户',
    endTime: history?.endTime,
    duration: history?.duration,
    isApproved: isApproved,
    status: isApproved ? 'success' : 'danger',
    statusText: isApproved ? '审批通过' : '审批驳回',
    icon: isApproved ? CircleCheck : CircleClose,
    color: isApproved ? '#67C23A' : '#F56C6C'
  }
}

// 初始化
onMounted(() => {
  fetchInstanceList()
})

// 打开撤回弹窗
const handleShowRevokeDialog = () => {
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

    await workflowInstanceApi.revokeProcessInstance(instanceDetail.value.id, data)

    ElMessage.success('流程撤回成功')
    revokeDialogVisible.value = false

    // 刷新列表和详情
    currentInstance.value = null
    instanceDetail.value = null
    await fetchInstanceList()
  } catch (error) {
    console.error('撤回流程失败:', error)
    ElMessage.error(error.response?.data?.message || '撤回流程失败')
  } finally {
    revokeLoading.value = false
  }
}
</script>

<template>
  <div class="task-manager-container">
    <!-- 左侧列表区域 -->
    <div class="left-panel">
      <div class="panel-header">
        <h3>我的申请</h3>
        <el-button link :icon="Refresh" @click="refreshList" :loading="loading"></el-button>
      </div>

      <!-- 搜索区域 -->
      <div class="search-box">
        <el-input v-model="searchForm.processKey" placeholder="搜索任务名称" clearable :prefix-icon="Search"
          @keyup.enter="handleSearch" @clear="handleSearch">
        </el-input>
        <div class="filter-tags">
          <span class="filter-tag" :class="{ active: searchForm.status === null }"
            @click="() => { searchForm.status = null; handleSearch() }">全部</span>
          <span class="filter-tag" :class="{ active: searchForm.status === 1 }"
            @click="() => { searchForm.status = 1; handleSearch() }">运行中</span>
          <span class="filter-tag" :class="{ active: searchForm.status === 2 }"
            @click="() => { searchForm.status = 2; handleSearch() }">已结束</span>
        </div>
      </div>

      <!-- 列表内容 -->
      <div class="task-list" v-loading="loading">
        <template v-if="instanceList.length > 0">
          <div v-for="item in instanceList" :key="item.id" class="task-item"
            :class="{ active: currentInstance?.id === item.id }" @click="handleSelectInstance(item)">
            <div class="item-main">
              <div class="item-header">
                <span class="task-name" :title="getTaskName(item)">{{ getTaskName(item) }}</span>
                <el-tag size="small" :type="statusMap[item.status]?.type" effect="plain" round>
                  {{ statusMap[item.status]?.text }}
                </el-tag>
              </div>
              <div class="item-meta">
                <span class="meta-info">
                  <el-icon>
                    <User />
                  </el-icon> {{ item.startUserName || item.startUser || '未知' }}
                </span>
                <span class="meta-info">
                  <el-icon>
                    <Timer />
                  </el-icon> {{ formatRelativeTime(item.startTime) }}
                </span>
              </div>
            </div>
            <div class="item-arrow">
              <el-icon>
                <ArrowRight />
              </el-icon>
            </div>
          </div>
        </template>
        <el-empty v-else description="暂无任务" :image-size="60"></el-empty>
      </div>

      <!-- 简单分页 -->
      <div class="list-pagination">
        <el-pagination small background layout="prev, pager, next" :total="Number(total)"
          :page-size="pagination.pageSize" v-model:current-page="pagination.current"
          @current-change="handleCurrentChange" />
      </div>
    </div>

    <!-- 右侧详情区域 -->
    <div class="right-panel" v-loading="detailLoading">
      <div v-if="instanceDetail" class="detail-content">
        <!-- 顶部概览信息 -->
        <div class="detail-header">
          <div class="header-main">
            <div class="title-row">
              <h2>{{ instanceDetail.processKey }}</h2>
              <el-tag :type="statusMap[instanceDetail.status]?.type" effect="dark">
                {{ statusMap[instanceDetail.status]?.text }}
              </el-tag>
            </div>
            <div class="meta-row">
              <div class="meta-item">
                <label>发起人：</label>
                <span>{{ instanceDetail.startUserName || instanceDetail.startUser }}</span>
              </div>
              <div class="meta-item">
                <label>启动时间：</label>
                <span>{{ formatTime(instanceDetail.startTime) }}</span>
              </div>
              <div class="meta-item" v-if="instanceDetail.endTime">
                <label>结束时间：</label>
                <span>{{ formatTime(instanceDetail.endTime) }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 核心内容区 Tabs -->
        <div class="detail-body">
          <el-tabs v-model="activeTab" class="custom-tabs">
            <el-tab-pane name="business">
              <template #label>
                <span class="tab-label"><el-icon>
                    <Document />
                  </el-icon> 业务数据</span>
              </template>
              <div class="tab-content scrollable">
                <!-- 表单预览（业务表单类型：businessType === 2） -->
                <div v-if="isBusinessForm()" class="form-preview-container">
                  <FormPreview :form-config="getFormConfig(instanceDetail.variables)"
                    :initial-data="getFormData(instanceDetail.variables)" />
                </div>

                <!-- 业务字段列表（系统表单类型：businessType === 1） -->
                <div v-else class="business-fields">
                  <div
                    v-for="(row, index) in getFormattedBusinessData(instanceDetail.variables, instanceDetail.processDefinition)"
                    :key="index" class="field-item">
                    <span class="field-label">{{ row.fieldLabel }}</span>
                    <span class="field-value">{{ row.fieldValue }}</span>
                  </div>
                  <el-empty
                    v-if="getFormattedBusinessData(instanceDetail.variables, instanceDetail.processDefinition).length === 0"
                    description="暂无业务数据" :image-size="60" />
                </div>

                <!-- 流程时间线 -->
                <ProcessTimeline :task-history="instanceDetail.taskHistory || []"
                  :process-key="instanceDetail.processKey" :process-definition="instanceDetail.processDefinition" />
              </div>
            </el-tab-pane>

            <el-tab-pane name="process">
              <template #label>
                <span class="tab-label"><el-icon>
                    <Connection />
                  </el-icon> 流程进度</span>
              </template>
              <div class="tab-content">
                <ProcessProgressDiagram v-if="instanceDetail.processDefinition"
                  :process-definition="instanceDetail.processDefinition"
                  :task-history="instanceDetail.taskHistory || []" :current-node-id="instanceDetail.currentNodeId"
                  :process-status="instanceDetail.status" :instance-id="instanceDetail.id" />
              </div>
            </el-tab-pane>
          </el-tabs>

          <!-- 底部操作栏 - 撤回按钮 -->
          <div v-if="instanceDetail.status === 1" class="action-footer">
            <el-button type="danger" size="large" @click="handleShowRevokeDialog" class="action-btn">
              <el-icon style="margin-right: 8px">
                <RefreshLeft />
              </el-icon>
              撤回
            </el-button>
          </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <el-empty description="请在左侧选择一个任务查看详情" />
      </div>
    </div>

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
            <el-input v-model="revokeForm.reason" type="textarea" :rows="3" placeholder="请输入撤回原因" maxlength="200"
              show-word-limit />
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

<style scoped lang="scss">
.task-manager-container {
  display: flex;
  height: calc(100vh - 84px);
  /* 减去顶部导航高度 */
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

    .search-box {
      padding: 12px 16px;
      border-bottom: 1px solid var(--border-color);

      .filter-tags {
        display: flex;
        gap: 8px;
        margin-top: 12px;

        .filter-tag {
          font-size: 12px;
          color: var(--text-secondary);
          cursor: pointer;
          padding: 2px 8px;
          border-radius: 4px;
          transition: all 0.2s;

          &:hover {
            color: var(--primary-color);
            background: var(--primary-light);
          }

          &.active {
            color: white;
            background: var(--primary-color);
          }
        }
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
    /* 防止flex子项溢出 */

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

      .detail-body {
        flex: 1;
        overflow: hidden;
        display: flex;
        flex-direction: column;
        position: relative;
        /* 为底部固定定位做准备 */

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
            overflow-y: auto;
            /* 允许滚动 */
          }

          .tab-content {
            padding: 24px;
            padding-bottom: 100px;
            /* 为底部操作栏留出空间 */
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

.approval-result-card {
  padding: 16px;
  border-radius: 8px;
  display: flex;
  gap: 16px;
  margin-bottom: 24px;

  &.success {
    background: var(--success-light);
    border: 1px solid var(--success-light-2);

    .result-icon {
      color: var(--success-color);
    }

    .result-title {
      color: var(--success-color);
    }
  }

  &.danger {
    background: var(--danger-light);
    border: 1px solid var(--danger-light-2);

    .result-icon {
      color: var(--danger-color);
    }

    .result-title {
      color: var(--danger-color);
    }
  }

  .result-icon {
    font-size: 24px;
    display: flex;
    align-items: flex-start;
    padding-top: 2px;
  }

  .result-info {
    flex: 1;

    .result-title {
      font-weight: 600;
      font-size: 16px;
      margin-bottom: 4px;
    }

    .result-desc {
      font-size: 13px;
      color: var(--text-muted);
      margin-bottom: 8px;

      .operator {
        margin-right: 12px;
      }
    }

    .result-comment {
      font-size: 14px;
      color: var(--text-secondary);
      font-style: italic;
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

/* 响应式调整 */
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

/* 撤回弹窗样式 */
.revoke-dialog-content {
  .revoke-tip {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 16px;
    background: var(--warning-light);
    border-radius: 6px;
    margin-bottom: 16px;
    color: var(--warning-color);
    font-size: 14px;
  }
}

.page-header {
  margin-bottom: 16px;
  
  .page-title {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
    color: var(--text-primary);
  }
}
</style>
