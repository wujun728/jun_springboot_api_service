<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh, DocumentAdd, Loading } from '@element-plus/icons-vue'
import { workflowApi, formApi } from '@/api'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import FormPreview from '@/components/form-designer/FormPreview.vue'

const router = useRouter()
const userStore = useUserStore()

// 响应式数据
const loading = ref(false)
const workflowList = ref<any[]>([])
const searchQuery = ref('')
const showLaunchDialog = ref(false)
const launching = ref(false)

// 当前选中的工作流
const selectedWorkflow = ref<any>(null)

// 流程发起表单数据
const launchForm = reactive<{
  businessKey: string
  variables: Record<string, any>
  description: string
}>({
  businessKey: '',
  variables: {},
  description: ''
})

// 业务表单相关
const formConfig = ref<any>(null)
const formConfigLoading = ref(false)
const formPreviewRef = ref()

// 分页数据
const pagination = reactive({
  current: 1,
  size: 12,
  total: 0
})

// 获取已启用的工作流列表
const getEnabledWorkflowList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.size,
      name: searchQuery.value || undefined,
      status: 1 // 只获取已启用的工作流
    }
    const response = await workflowApi.getWorkflowList(params)
    
    // 处理不同的响应数据结构
    if (response.data && response.data.records) {
      workflowList.value = response.data.records
      pagination.total = Number(response.data.total) || 0
    } else if (response.records) {
      workflowList.value = response.records
      pagination.total = Number(response.total) || 0
    } else {
      workflowList.value = []
      pagination.total = 0
    }
  } catch (error) {
    console.error('获取工作流列表失败:', error)
    ElMessage.error('获取工作流列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.current = 1
  getEnabledWorkflowList()
}

// 刷新
const handleRefresh = () => {
  searchQuery.value = ''
  pagination.current = 1
  getEnabledWorkflowList()
}

// 分页处理
const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  getEnabledWorkflowList()
}

const handleCurrentChange = (current: number) => {
  pagination.current = current
  getEnabledWorkflowList()
}

// 打开发起流程对话框
const handleLaunchWorkflow = async (workflow: any) => {
  selectedWorkflow.value = workflow
  
  // 重置表单
  launchForm.businessKey = ''
  launchForm.variables = {}
  launchForm.description = ''
  formConfig.value = null
  
  // 如果是业务流程，加载表单配置
  if (workflow.businessType === 2) {
    await loadBusinessFormConfig(workflow)
  } else {
    // 系统流程：如果工作流有定义的变量，初始化表单
    if (workflow.variables && workflow.variables.length > 0) {
      workflow.variables.forEach((variable: any) => {
        // 根据变量类型设置默认值
        switch (variable.type) {
          case 'string':
            launchForm.variables[variable.name] = ''
            break
          case 'number':
            launchForm.variables[variable.name] = 0
            break
          case 'boolean':
            launchForm.variables[variable.name] = false
            break
          case 'date':
            launchForm.variables[variable.name] = ''
            break
          case 'object':
            launchForm.variables[variable.name] = {}
            break
          default:
            launchForm.variables[variable.name] = ''
        }
      })
    }
  }
  
  showLaunchDialog.value = true
}

// 加载业务表单配置
const loadBusinessFormConfig = async (workflow: any) => {
  if (!workflow.businessView) {
    ElMessage.warning('该流程未配置业务表单')
    return
  }
  
  formConfigLoading.value = true
  try {
    // businessView 中存储的是 formConfig JSON 字符串
    formConfig.value = typeof workflow.businessView === 'string' 
      ? JSON.parse(workflow.businessView) 
      : workflow.businessView
  } catch (error) {
    console.error('解析表单配置失败:', error)
    ElMessage.error('表单配置格式错误')
    formConfig.value = null
  } finally {
    formConfigLoading.value = false
  }
}

// 确认发起流程
const confirmLaunchWorkflow = async () => {
  if (!selectedWorkflow.value) {
    ElMessage.warning('请选择要发起的流程')
    return
  }
  
  // 业务流程：只保存表单数据
  if (selectedWorkflow.value.businessType === 2) {
    if (!formPreviewRef.value) {
      ElMessage.warning('表单预览组件未加载')
      return
    }
    
    if (!selectedWorkflow.value.businessFormId) {
      ElMessage.warning('该流程未配置业务表单ID')
      return
    }
    
    launching.value = true
    try {
      // 先进行表单验证
      const isValid = await formPreviewRef.value.validate()
      if (!isValid) {
        ElMessage.warning('请完善表单信息')
        launching.value = false
        return
      }
      
      // 获取表单数据
      const formData = formPreviewRef.value.getFormData()
      
      // 保存表单数据到业务表，并传递当前用户ID用于发起流程
      const submitData = {
        formId: selectedWorkflow.value.businessFormId,
        data: formData,
        startUser: userStore.userInfo?.id ? String(userStore.userInfo.id) : undefined
      }
      
      const response = await formApi.createFormData(submitData)
      
      if (response && response.code === 200) {
        ElMessage.success('流程发起成功')
        showLaunchDialog.value = false
        
        // 询问是否跳转到我的任务
        ElMessageBox.confirm(
          '流程已成功发起，是否前往我的任务查看？',
          '提示',
          {
            confirmButtonText: '前往查看',
            cancelButtonText: '留在此页',
            type: 'success'
          }
        ).then(() => {
          router.push('/home/taskcenter/all')
        }).catch(() => {
          // 用户选择留在当前页面
        })
      } else {
        ElMessage.error(response?.msg || '保存表单数据失败')
      }
    } catch (error: any) {
      console.error('发起流程失败:', error)
      ElMessage.error(error?.message || '发起流程失败')
    } finally {
      launching.value = false
    }
  } else {
    // 系统流程：验证必填变量（预留功能）
    ElMessage.info('系统流程功能开发中')
  }
}

// 关闭发起对话框
const closeLaunchDialog = () => {
  showLaunchDialog.value = false
  selectedWorkflow.value = null
}

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 获取变量类型显示文本
const getVariableTypeText = (type: string) => {
  const typeMap: Record<string, string> = {
    'string': '字符串',
    'number': '数值',
    'boolean': '布尔',
    'date': '日期',
    'object': '对象'
  }
  return typeMap[type] || type
}

// 页面加载时获取数据
onMounted(() => {
  getEnabledWorkflowList()
})
</script>

<template>
  <div class="launch-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">流程发起</h1>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <div class="search-left">
        <el-input
          v-model="searchQuery"
          placeholder="搜索工作流名称..."
          prefix-icon="Search"
          style="width: 300px"
          clearable
          @input="handleSearch"
        />
      </div>
      <div class="search-right">
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 工作流卡片列表 -->
    <div class="workflow-content">
      <el-empty 
        v-if="!loading && workflowList.length === 0" 
        description="暂无可发起的工作流"
        :image-size="200"
      />
      
      <div v-else v-loading="loading" class="workflow-grid">
        <div 
          v-for="workflow in workflowList" 
          :key="workflow.id" 
          class="workflow-card"
          @click="handleLaunchWorkflow(workflow)"
        >
          <div class="card-header">
            <div class="workflow-icon">
              <el-icon :size="32"><DocumentAdd /></el-icon>
            </div>
            <div class="workflow-info">
              <h3 class="workflow-name">{{ workflow.name }}</h3>
              <p class="workflow-key">{{ workflow.key }}</p>
            </div>
          </div>
          
          <div class="card-body">
            <p class="workflow-description">
              {{ workflow.description || '暂无描述' }}
            </p>
            
            <div class="workflow-meta">
              <div class="meta-item">
                <span class="meta-label">版本:</span>
                <span class="meta-value">v{{ workflow.version }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">类型:</span>
                <el-tag :type="workflow.businessType === 1 ? 'primary' : 'success'" size="small">
                  {{ workflow.businessType === 1 ? '系统表单' : '业务表单' }}
                </el-tag>
              </div>
            </div>
          </div>
          
          <div class="card-footer">
            <el-button type="primary" size="small" :icon="Plus">
              发起流程
            </el-button>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          background
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[12, 24, 36, 48]"
          :total="Number(pagination.total)"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 发起流程对话框 -->
    <el-dialog
      v-model="showLaunchDialog"
      :title="`发起流程：${selectedWorkflow?.name || ''}`"
      width="700px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedWorkflow" class="launch-form">
        <!-- 业务数据表单 -->
        <div class="variables-section">
          <el-form label-width="120px">

            <!-- 业务流程：使用 FormPreview 组件渲染表单 -->
            <template v-if="selectedWorkflow.businessType === 2">
              <div v-if="formConfigLoading" class="loading-container">
                <el-skeleton :rows="6" animated />
              </div>
              
              <div v-else-if="!formConfig" class="no-config">
                <el-alert
                  title="该流程未配置业务表单"
                  type="warning"
                  :closable="false"
                  show-icon
                />
              </div>
              
              <div v-else class="form-content">
                <FormPreview ref="formPreviewRef" :form-config="formConfig" />
              </div>
            </template>

            <!-- 系统流程：动态变量表单（预留） -->
            <template v-else>
              <el-alert
                title="系统流程发起说明"
                type="info"
                :closable="false"
                show-icon
              >
                <template #default>
                  <div style="margin: 8px 0 0 0; font-size: 13px; color: var(--text-secondary); line-height: 1.8;">
                    <p style="margin: 0 0 8px 0;">系统流程需要从对应的业务系统页面发起：</p>
                    <ul style="margin: 0; padding-left: 20px;">
                      <li>请前往 <strong>系统管理 → 流程测试订单</strong> 页面</li>
                      <li>在订单列表中点击 <strong>"新增数据"</strong> 按钮</li>
                      <li>填写订单信息后，系统会自动发起对应的流程</li>
                    </ul>
                    <p style="margin: 12px 0 0 0; color: var(--text-muted);">
                      💡 提示：系统流程与业务数据绑定，需要通过业务系统操作来触发流程实例。
                    </p>
                  </div>
                </template>
              </el-alert>
              
              <!-- 预留：系统流程变量输入 -->
              <!-- 
              <template v-if="selectedWorkflow.variables && selectedWorkflow.variables.length > 0">
                <el-form-item
                  v-for="variable in selectedWorkflow.variables"
                  :key="variable.name"
                  :label="variable.label || variable.name"
                  :required="variable.required"
                >
                  <el-input
                    v-if="variable.type === 'string'"
                    v-model="launchForm.variables[variable.name]"
                    :placeholder="`请输入${variable.label || variable.name}`"
                    clearable
                  />
                  <el-input-number
                    v-else-if="variable.type === 'number'"
                    v-model="launchForm.variables[variable.name]"
                    style="width: 100%"
                  />
                  <el-switch
                    v-else-if="variable.type === 'boolean'"
                    v-model="launchForm.variables[variable.name]"
                    active-text="是"
                    inactive-text="否"
                  />
                  <el-date-picker
                    v-else-if="variable.type === 'date'"
                    v-model="launchForm.variables[variable.name]"
                    type="datetime"
                    style="width: 100%"
                    value-format="YYYY-MM-DD HH:mm:ss"
                  />
                  <el-input
                    v-else
                    v-model="launchForm.variables[variable.name]"
                    clearable
                  />
                  <div class="form-help-text">
                    类型: {{ getVariableTypeText(variable.type) }}
                    {{ variable.required ? '（必填）' : '（选填）' }}
                  </div>
                </el-form-item>
              </template>
              -->
            </template>
          </el-form>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeLaunchDialog">取消</el-button>
          <el-button type="primary" @click="confirmLaunchWorkflow" :loading="launching">
            <el-icon v-if="!launching"><DocumentAdd /></el-icon>
            {{ launching ? '发起中...' : '确认发起' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.launch-page {
  padding: 24px;
  background-color: var(--bg-secondary, #f8fafc);
  min-height: calc(100vh - 60px);
}

.search-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 16px 20px;
  background: var(--bg-card);
  border-radius: 8px;
  box-shadow: var(--shadow-sm);
  
  .search-left {
    display: flex;
    gap: 12px;
  }
  
  .search-right {
    display: flex;
    gap: 12px;
  }
}

.workflow-content {
  .workflow-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 16px;
    margin-bottom: 16px;
  }
  
  .workflow-card {
    background: var(--bg-card);
    border-radius: 8px;
    padding: 20px;
    box-shadow: var(--shadow-sm);
    transition: all 0.2s ease;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    height: 100%;
    border: 1px solid transparent;
    
    &:hover {
      border-color: var(--border-color);
      box-shadow: var(--shadow-md);
    }
    
    .card-header {
      display: flex;
      align-items: center;
      margin-bottom: 16px;
      
      .workflow-icon {
        width: 48px;
        height: 48px;
        border-radius: 8px;
        background: var(--primary-light);
        display: flex;
        align-items: center;
        justify-content: center;
        color: var(--primary-color);
        margin-right: 12px;
        flex-shrink: 0;
      }
      
      .workflow-info {
        flex: 1;
        min-width: 0;
        
        .workflow-name {
          font-size: 16px;
          font-weight: 600;
          color: var(--text-primary);
          margin: 0 0 4px 0;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        
        .workflow-key {
          font-size: 12px;
          color: var(--text-muted);
          margin: 0;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }
    }
    
    .card-body {
      flex: 1;
      margin-bottom: 16px;
      
      .workflow-description {
        font-size: 14px;
        color: var(--text-secondary);
        line-height: 1.6;
        margin: 0 0 12px 0;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
        min-height: 44px;
      }
      
      .workflow-meta {
        display: flex;
        gap: 16px;
        
        .meta-item {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 13px;
          
          .meta-label {
            color: var(--text-muted);
          }
          
          .meta-value {
            color: var(--text-secondary);
            font-weight: 500;
          }
        }
      }
    }
    
    .card-footer {
      border-top: 1px solid var(--border-light);
      padding-top: 12px;
      display: flex;
      justify-content: flex-end;
    }
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
}

// 发起流程对话框样式
.launch-form {
  .form-help-text {
    font-size: 12px;
    color: var(--text-muted);
    margin-top: 4px;
    line-height: 1.5;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

// 表单加载和配置样式
.loading-container,
.no-config {
  padding: 20px 0;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.form-content {
  padding: 0;
  
  :deep(.el-form) {
    max-width: none;
  }
}
</style>
