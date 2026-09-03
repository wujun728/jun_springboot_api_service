<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  User, Timer, ArrowRight, Refresh, Connection, Document,
  CircleCheck, CircleClose
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
const completedList = ref([] as any[])
const total = ref(0)
const currentTask = ref(null as any) // 当前选中的已办任务
const instanceDetail = ref(null as any) // 流程实例详情
const activeTab = ref('business') // 当前激活的 Tab
const processDefinition = ref(null as any)

// 分页参数
const pagination = reactive({
  current: 1,
  pageSize: 10
})

// 搜索参数
const searchForm = reactive({
  operator: userStore.userInfo?.id || '1' // 从当前登录用户信息中获取用户ID
})

// 流程状态映射
const statusMap = {
  1: { text: '运行中', type: 'warning', color: '#E6A23C', bgColor: '#FDF6EC' },
  2: { text: '已结束', type: 'success', color: '#67C23A', bgColor: '#F0F9EB' },
  3: { text: '已暂停', type: 'info', color: '#909399', bgColor: '#F4F4F5' }
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

// 格式化时长
const formatDuration = (duration) => {
  if (!duration) return '-'
  const seconds = Math.floor(duration / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)

  if (days > 0) return `${days}天${hours % 24}小时`
  if (hours > 0) return `${hours}小时${minutes % 60}分钟`
  if (minutes > 0) return `${minutes}分钟`
  return `${seconds}秒`
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

// 检查是否为业务表单
const isBusinessForm = () => {
  return instanceDetail.value?.processDefinition?.businessType === 2
}

// 获取表单配置
const getFormConfig = (variablesStr) => {
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

// 获取已办任务列表
const fetchCompletedList = async (keepCurrent = false) => {
  try {
    loading.value = true
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
      operator: searchForm.operator
    }

    const response = await workflowApi.getUserCompletedTasks(params)

    if (response && response.data) {
      completedList.value = response.data.records || []
      total.value = Number(response.data.total) || 0

      // 默认选中第一条
      if (completedList.value.length > 0) {
        if (!keepCurrent || !currentTask.value) {
          handleSelectTask(completedList.value[0])
        }
      } else {
        currentTask.value = null
        instanceDetail.value = null
      }
    }
  } catch (error) {
    console.error('获取已办任务列表失败:', error)
    ElMessage.error('获取已办任务列表失败')
  } finally {
    loading.value = false
  }
}

// 选中任务
const handleSelectTask = async (task) => {
  if (currentTask.value?.id === task.id && instanceDetail.value) return

  currentTask.value = task
  detailLoading.value = true

  try {
    // 获取完整详情
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

      // 默认切回业务数据 Tab
      activeTab.value = 'business'
    }
  } catch (error) {
    console.error('获取流程详情失败:', error)
    ElMessage.error('获取流程详情失败')
  } finally {
    detailLoading.value = false
  }
}

// 分页变化
const handleCurrentChange = (page) => {
  pagination.current = page
  fetchCompletedList()
}

// 刷新列表
const refreshList = () => {
  pagination.current = 1
  fetchCompletedList()
}

// 组件挂载
onMounted(async () => {
  if (!userStore.userInfo?.id) {
    try {
      await userStore.getUserInfo()
      if (userStore.userInfo?.id) {
        searchForm.operator = userStore.userInfo.id.toString()
      }
    } catch (error) {
      console.warn('获取用户信息失败:', error)
    }
  }
  fetchCompletedList()
})
</script>

<template>
  <div class="completed-manager-container">
    <!-- 左侧列表区域 -->
    <div class="left-panel">
      <div class="panel-header">
        <h3>我的已办</h3>
        <el-button link :icon="Refresh" @click="refreshList" :loading="loading"></el-button>
      </div>

      <!-- 列表内容 -->
      <div class="task-list" v-loading="loading">
        <template v-if="completedList.length > 0">
          <div
            v-for="item in completedList"
            :key="item.id"
            class="task-item"
            :class="{ active: currentTask?.id === item.id }"
            @click="handleSelectTask(item)"
          >
            <div class="item-main">
              <div class="item-header">
                <span class="task-name" :title="item.processName || item.nodeName">
                  {{ item.processName || item.nodeName }}
                </span>
                <el-tag size="small" :type="statusMap[item.processStatus]?.type" effect="plain" round>
                  {{ statusMap[item.processStatus]?.text }}
                </el-tag>
              </div>
              <div class="item-node">
                <el-icon><CircleCheck /></el-icon>
                <span>{{ item.nodeName }}</span>
              </div>
              <div class="item-meta">
                <span class="meta-info">
                  <el-icon><User /></el-icon> {{ item.startUserName || item.startUser || '未知' }}
                </span>
                <span class="meta-info">
                  <el-icon><Timer /></el-icon> {{ formatRelativeTime(item.endTime) }}
                </span>
              </div>
              <div class="item-duration" v-if="item.duration">
                <span class="duration-label">处理时长：</span>
                <span class="duration-value">{{ formatDuration(item.duration) }}</span>
              </div>
            </div>
            <div class="item-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </template>
        <el-empty v-else description="暂无已办任务" :image-size="60"></el-empty>
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
               <h2>{{ currentTask.processName || currentTask.nodeName }}</h2>
               <el-tag type="success" effect="dark">已完成</el-tag>
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
                <label>完成时间：</label>
                <span>{{ formatTime(currentTask.endTime) }}</span>
              </div>
              <div class="meta-item" v-if="currentTask.duration">
                <label>处理时长：</label>
                <span>{{ formatDuration(currentTask.duration) }}</span>
              </div>
            </div>
            <!-- 审批结果提示 -->
            <div class="approval-info-banner" v-if="currentTask.comment">
              <el-icon class="banner-icon" :class="currentTask.comment.includes('驳回') || currentTask.comment.includes('拒绝') ? 'reject' : 'approve'">
                <component :is="currentTask.comment.includes('驳回') || currentTask.comment.includes('拒绝') ? CircleClose : CircleCheck" />
              </el-icon>
              <div class="banner-content">
                <div class="banner-title">{{ currentTask.comment.includes('驳回') || currentTask.comment.includes('拒绝') ? '已驳回' : '已通过' }}</div>
                <div class="banner-desc">{{ currentTask.comment }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 核心内容区 Tabs -->
        <div class="detail-body">
          <el-tabs v-model="activeTab" class="custom-tabs">
            <el-tab-pane name="business">
              <template #label>
                <span class="tab-label"><el-icon><Document /></el-icon> 业务数据</span>
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
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <el-empty description="请在左侧选择一条已办任务查看详情" />
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.completed-manager-container {
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
          background: var(--bg-secondary);
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

          .item-node {
            display: flex;
            align-items: center;
            gap: 4px;
            font-size: 12px;
            color: var(--success-color);
            margin-bottom: 6px;

            .el-icon {
              font-size: 14px;
            }
          }

          .item-meta {
            display: flex;
            gap: 12px;
            font-size: 12px;
            color: var(--text-secondary);
            margin-bottom: 4px;

            .meta-info {
              display: flex;
              align-items: center;
              gap: 4px;
            }
          }

          .item-duration {
            font-size: 12px;
            color: var(--text-secondary);

            .duration-label {
              color: var(--text-muted);
            }

            .duration-value {
              font-weight: 500;
              color: var(--primary-color);
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
            margin-bottom: 16px;
            flex-wrap: wrap;

            .meta-item {
              label {
                color: var(--text-muted);
                margin-right: 4px;
              }
            }
          }

          .approval-info-banner {
            display: flex;
            align-items: flex-start;
            gap: 12px;
            padding: 12px 16px;
            background: var(--primary-light);
            border: 1px solid var(--primary-color);
            border-radius: 6px;

            &.reject-banner {
              background: var(--danger-light);
              border-color: var(--danger-color);
            }

            .banner-icon {
              font-size: 20px;
              margin-top: 2px;

              &.approve {
                color: var(--success-color);
              }

              &.reject {
                color: var(--danger-color);
              }
            }

            .banner-content {
              flex: 1;

              .banner-title {
                font-size: 14px;
                font-weight: 500;
                color: var(--text-primary);
                margin-bottom: 4px;
              }

              .banner-desc {
                font-size: 13px;
                color: var(--text-secondary);
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
            padding-bottom: 60px; /* 为底部留出空间 */
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
  }
}

.business-fields {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;

  .field-item {
    background: var(--bg-secondary);
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

@media (max-width: 768px) {
  .completed-manager-container {
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
