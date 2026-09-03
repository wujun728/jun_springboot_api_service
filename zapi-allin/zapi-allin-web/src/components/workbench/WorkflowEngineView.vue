<template>
  <div class="workflow-engine-view">
    <!-- 左侧流程列表 -->
    <aside class="workflow-sidebar">
      <div class="sidebar-header">
        <span class="sidebar-title">流程定义</span>
        <el-button type="primary" size="small" @click="handleCreate">
          <el-icon><Plus /></el-icon>
        </el-button>
      </div>

      <!-- 搜索筛选 -->
      <div class="search-section">
        <el-input
          v-model="searchQuery"
          placeholder="搜索流程..."
          :prefix-icon="Search"
          clearable
          @input="handleSearch"
        />
      </div>

      <!-- 流程列表 -->
      <div class="workflow-list" v-loading="loading">
        <template v-if="workflowList.length > 0">
          <div
            v-for="item in workflowList"
            :key="item.id"
            class="workflow-item"
            :class="{ active: currentWorkflow?.id === item.id }"
          >
            <div class="item-main" @click="handleSelectWorkflow(item)">
              <div class="item-header">
                <span class="workflow-name" :title="item.name">{{ item.name }}</span>
                <el-tag size="small" :type="getStatusTagType(item.status)" effect="plain">
                  {{ getStatusText(item.status) }}
                </el-tag>
              </div>
              <div class="item-footer">
                <span class="time-info">
                  <el-icon><Timer /></el-icon>
                  {{ formatRelativeTime(item.createTime) }}
                </span>
                <span class="version-info">v{{ item.version }}</span>
              </div>
            </div>
            <div class="item-actions" @click.stop>
              <el-dropdown trigger="click" @command="(cmd) => handleItemAction(cmd, item)">
                <el-icon class="more-icon"><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="edit" :icon="Edit">
                      编辑
                    </el-dropdown-item>
                    <el-dropdown-item
                      command="toggle"
                      :icon="Switch"
                    >
                      {{ item.status === 1 ? '禁用' : '激活' }}
                    </el-dropdown-item>
                    <el-dropdown-item command="delete" :icon="Delete" divided>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </template>
        <el-empty v-else description="暂无流程定义" :image-size="60">
          <el-button type="primary" size="small" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            创建流程
          </el-button>
        </el-empty>
      </div>

      <!-- 分页 -->
      <div class="list-pagination" v-if="pagination.total > 0">
        <el-pagination
          small
          background
          layout="prev, pager, next"
          :total="Number(pagination.total)"
          :page-size="pagination.size"
          v-model:current-page="pagination.current"
          @current-change="handlePageChange"
        />
      </div>
    </aside>

    <!-- 右侧详情面板 -->
    <div class="right-panel" v-loading="detailLoading">
      <div v-if="currentWorkflow" class="detail-content">
        <!-- 核心内容区 -->
        <div class="detail-body">
          <div class="tabs-wrapper">
            <el-tabs v-model="activeTab" class="custom-tabs">
              <el-tab-pane name="design">
              <template #label>
                <span class="tab-label">
                  <el-icon><Edit /></el-icon>
                  流程设计
                </span>
              </template>
              <div class="tab-content design-content" :class="{ 'fullscreen-mode': isFullscreen }">
                <!-- 设计器主体 - 三栏布局 -->
                <div class="designer-main">
                  <!-- 左侧节点面板 -->
                  <div class="designer-left" :class="{ collapsed: leftPanelCollapsed }">
                    <div class="panel-header">
                      <h4 v-show="!leftPanelCollapsed">节点库</h4>
                      <el-button
                        :icon="leftPanelCollapsed ? ArrowRight : ArrowLeft"
                        size="small"
                        circle
                        class="collapse-btn"
                        @click="toggleLeftPanel"
                        title="折叠/展开"
                      />
                    </div>
                    <div class="panel-content" v-show="!leftPanelCollapsed">
                      <NodePanel />
                    </div>
                  </div>

                  <!-- 中间画布 -->
                  <div class="designer-center">
                    <div class="canvas-toolbar">
                      <div class="toolbar-left">
                        <el-button size="small" @click="handleResetDesign">
                          <el-icon><RefreshLeft /></el-icon>
                          重置
                        </el-button>
                        <el-button size="small" @click="clearWorkflow">
                          <el-icon><Delete /></el-icon>
                          清空画布
                        </el-button>
                        <el-button size="small" @click="showPreviewDrawer">
                          <el-icon><View /></el-icon>
                          流程预览
                        </el-button>
                      </div>
                      <div class="toolbar-right">
                        <div class="canvas-stats">
                          <el-tag v-if="processStore.currentProcess.nodes.length > 0" size="small">
                            {{ processStore.currentProcess.nodes.length }} 个节点
                          </el-tag>
                          <el-tag v-if="processStore.currentProcess.flows.length > 0" type="success" size="small">
                            {{ processStore.currentProcess.flows.length }} 个连接
                          </el-tag>
                          <el-tag v-if="processStore.currentProcess.nodes.length === 0" type="info" size="small">
                            空画布
                          </el-tag>
                        </div>
                        <el-divider direction="vertical" />
                        <el-button size="small" @click="toggleFullscreen">
                          <el-icon><FullScreen v-if="!isFullscreen" /><Aim v-else /></el-icon>
                          {{ isFullscreen ? '退出全屏' : '全屏设计' }}
                        </el-button>
                      </div>
                    </div>
                    <div class="canvas-wrapper">
                      <ProcessCanvas
                        v-if="currentWorkflow"
                        :key="`canvas-${currentWorkflow.id}`"
                        :current-process="processStore.currentProcess"
                        :selected-node-id="processStore.selectedNodeId"
                        :selected-flow-id="processStore.selectedFlowId"
                        @node-added="handleNodeAdded"
                        @node-deleted="handleNodeDeleted"
                        @node-selected="handleNodeSelected"
                        @node-updated="handleNodeUpdated"
                        @flow-added="handleFlowAdded"
                        @flow-deleted="handleFlowDeleted"
                        @flow-selected="handleFlowSelected"
                      />
                      <div v-else class="canvas-empty">
                        <el-empty description="请先选择一个流程" />
                      </div>
                    </div>
                  </div>

                  <!-- 右侧属性面板 -->
                  <div class="designer-right" :class="{ collapsed: rightPanelCollapsed }">
                    <div class="panel-header">
                      <h4 v-show="!rightPanelCollapsed">{{ processStore.selectedNodeId ? '节点配置' : processStore.selectedFlowId ? '连线配置' : '流程配置' }}</h4>
                      <el-button
                        :icon="rightPanelCollapsed ? ArrowLeft : ArrowRight"
                        size="small"
                        circle
                        class="collapse-btn"
                        @click="toggleRightPanel"
                        title="折叠/展开"
                      />
                    </div>
                    <div class="panel-content" v-show="!rightPanelCollapsed">
                      <ProcessPropertiesPanel
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
                    </div>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane name="info">
              <template #label>
                <span class="tab-label">
                  <el-icon><InfoFilled /></el-icon>
                  基本信息
                </span>
              </template>
              <div class="tab-content scrollable">
                <el-form
                  ref="infoFormRef"
                  :model="infoForm"
                  :rules="infoFormRules"
                  label-width="100px"
                  size="default"
                >
                  <div class="info-section">
                    <div class="section-title">基本信息</div>
                    <el-form-item label="流程名称" prop="name">
                      <el-input
                        v-model="infoForm.name"
                        placeholder="请输入流程名称"
                        clearable
                      />
                    </el-form-item>

                    <el-form-item label="流程标识" prop="key">
                      <el-input
                        v-model="infoForm.key"
                        placeholder="请输入流程标识"
                        clearable
                      />
                      <div class="form-help-text">
                        流程的唯一标识，建议使用英文和下划线
                      </div>
                    </el-form-item>

                    <el-form-item label="流程描述">
                      <el-input
                        v-model="infoForm.description"
                        type="textarea"
                        :rows="3"
                        placeholder="请输入流程描述"
                        clearable
                      />
                    </el-form-item>

                    <el-form-item label="业务表单" prop="businessFormId">
                      <div style="display: flex; gap: 8px;">
                        <el-select
                          v-model="infoForm.businessFormId"
                          placeholder="请选择业务表单"
                          style="flex: 1;"
                          filterable
                          @change="handleBusinessFormChange"
                        >
                          <el-option
                            v-for="form in businessFormList"
                            :key="form.id"
                            :label="form.formName"
                            :value="form.id"
                          >
                            <span>{{ form.formName }}</span>
                          </el-option>
                        </el-select>
                        <el-button
                          v-if="infoForm.businessFormId"
                          @click="handleRefreshVariables"
                          :loading="refreshingVariables"
                          :icon="Refresh"
                        >
                          刷新变量
                        </el-button>
                      </div>
                      <div class="form-help-text">
                        选择当前应用下的业务表单作为流程的数据载体，点击"刷新变量"可从表单字段重新生成流程变量
                      </div>
                    </el-form-item>

                    <!-- 流程变量列表（只读展示） -->
                    <el-form-item label="流程变量">
                      <div class="variables-readonly-list">
                        <div v-if="!processStore.currentProcess.variables || processStore.currentProcess.variables.length === 0" class="no-variables-hint">
                          <el-text type="info" size="small">暂无流程变量</el-text>
                        </div>
                        <div v-else class="variables-grid">
                          <div
                            v-for="variable in processStore.currentProcess.variables"
                            :key="variable.name"
                            class="variable-chip"
                          >
                            <el-tag size="small" effect="plain">
                              {{ variable.label || variable.name }}
                            </el-tag>
                            <el-text size="small" type="info" class="variable-type">{{ variable.type }}</el-text>
                          </div>
                        </div>
                      </div>
                      <div class="form-help-text">
                        流程变量来自业务表单字段，点击上方"刷新变量"按钮可重新生成
                      </div>
                    </el-form-item>
                  </div>

                  <div class="info-section">
                    <div class="section-title">状态信息</div>
                    <div class="info-grid">
                      <div class="info-item">
                        <label>流程ID：</label>
                        <span>{{ currentWorkflow.id }}</span>
                      </div>
                      <div class="info-item">
                        <label>版本号：</label>
                        <span>v{{ currentWorkflow.version }}</span>
                      </div>
                      <div class="info-item">
                        <label>状态：</label>
                        <el-tag :type="getStatusTagType(currentWorkflow.status)">
                          {{ getStatusText(currentWorkflow.status) }}
                        </el-tag>
                      </div>
                      <div class="info-item">
                        <label>业务类型：</label>
                        <el-tag :type="currentWorkflow.businessType === 1 ? 'primary' : 'success'">
                          {{ currentWorkflow.businessType === 1 ? '系统表单' : '业务表单' }}
                        </el-tag>
                      </div>
                    </div>
                  </div>

                  <div class="info-section">
                    <div class="section-title">时间信息</div>
                    <div class="info-grid">
                      <div class="info-item">
                        <label>创建时间：</label>
                        <span>{{ formatTime(currentWorkflow.createTime) }}</span>
                      </div>
                      <div class="info-item">
                        <label>更新时间：</label>
                        <span>{{ formatTime(currentWorkflow.updateTime) }}</span>
                      </div>
                    </div>
                  </div>
                </el-form>
              </div>
            </el-tab-pane>

          </el-tabs>

          <!-- Tab 右侧操作按钮 -->
          <div v-if="activeTab === 'design'" class="tab-actions">
            <el-button size="small" @click="validateWorkflow">
              <el-icon><Check /></el-icon>
              验证工作流
            </el-button>
            <el-button type="primary" size="small" @click="handleDesignSaved">
              <el-icon><Check /></el-icon>
              保存流程
            </el-button>
          </div>
          <div v-if="activeTab === 'info'" class="tab-actions">
            <el-button type="primary" size="small" @click="handleSaveInfo" :loading="savingInfo">
              <el-icon><Check /></el-icon>
              保存基本信息
            </el-button>
          </div>
        </div>
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-state">
        <el-empty description="请在左侧选择一个流程查看详情" />
      </div>
    </div>

    <!-- 创建流程抽屉 -->
    <el-drawer
      v-model="showCreateDrawer"
      title="创建流程"
      direction="rtl"
      size="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="createFormRef"
        :model="createForm"
        :rules="createFormRules"
        label-width="100px"
      >
        <el-form-item label="流程名称" prop="name">
          <el-input
            v-model="createForm.name"
            placeholder="请输入流程名称"
            clearable
          />
        </el-form-item>

        <el-form-item label="流程标识" prop="key">
          <el-input
            v-model="createForm.key"
            placeholder="请输入流程标识，如：leave_process"
            clearable
          />
          <div class="form-help-text">
            流程的唯一标识，建议使用英文和下划线
          </div>
        </el-form-item>

        <el-form-item label="流程描述" prop="description">
          <el-input
            v-model="createForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入流程描述"
            clearable
          />
        </el-form-item>

        <el-form-item label="业务表单" prop="businessFormId">
          <el-select
            v-model="createForm.businessFormId"
            placeholder="请选择业务表单"
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="form in businessFormList"
              :key="form.id"
              :label="form.formName"
              :value="form.id"
            >
              <span>{{ form.formName }}</span>
            </el-option>
          </el-select>
          <div class="form-help-text">
            选择当前应用下的业务表单作为流程的数据载体
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="drawer-footer">
          <el-button @click="showCreateDrawer = false">取消</el-button>
          <el-button type="primary" @click="handleCreateSubmit" :loading="createLoading">
            创建并设计
          </el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 流程预览抽屉 -->
    <el-drawer
      v-model="showPreviewDrawerVisible"
      title="流程预览"
      direction="rtl"
      size="80%"
      :close-on-click-modal="false"
    >
      <WorkflowPreview
        v-if="currentWorkflow && previewProcessData"
        :process-data="previewProcessData"
        :process-info="previewProcessInfo"
        :show-info="true"
        :show-json="true"
        :show-business-data="false"
        :height="previewDrawerHeight"
      />
      <el-empty v-else description="暂无流程数据" :image-size="100" />
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, watch, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Refresh, Search, View, Edit, Switch, Delete,
  Key, Timer, ArrowRight, InfoFilled, Check, RefreshLeft, MoreFilled,
  FullScreen, Aim, ArrowLeft
} from '@element-plus/icons-vue'
import { workflowApi, wbAppEntryApi, formApi } from '@/api'
import { useProcessDesignerStore } from '@/stores/workflow/processDesigner'
import WorkflowPreview from '@/components/workflow-designer/WorkflowPreview.vue'
import ProcessCanvas from '@/components/workflow-designer/ProcessCanvas.vue'
import NodePanel from '@/components/workflow-designer/NodePanel.vue'
import ProcessPropertiesPanel from '@/components/workflow-designer/ProcessPropertiesPanel.vue'

const router = useRouter()
const processStore = useProcessDesignerStore()

const props = defineProps({
  appId: {
    type: String,
    required: true
  }
})

// 计算属性：将workflow数据转换为WorkflowPreview需要的格式
const previewProcessData = computed(() => {
  if (!currentWorkflow.value) {
    console.log('currentWorkflow.value 为空，返回 null')
    return null
  }

  console.log('=== 开始转换预览数据 ===')
  console.log('currentWorkflow.value:', currentWorkflow.value)
  console.log('currentWorkflow.value.nodes:', currentWorkflow.value.nodes)
  console.log('currentWorkflow.value.flows:', currentWorkflow.value.flows)

  const result = {
    id: currentWorkflow.value.id,
    name: currentWorkflow.value.name,
    key: currentWorkflow.value.key,
    description: currentWorkflow.value.description,
    nodes: currentWorkflow.value.nodes || [],
    flows: currentWorkflow.value.flows || [],
    variables: currentWorkflow.value.variables || []
  }

  console.log('result:', result)
  console.log('result.nodes 数量:', result.nodes?.length || 0)
  console.log('result.flows 数量:', result.flows?.length || 0)
  console.log('=== 转换预览数据完成 ===')

  return result
})

const previewProcessInfo = computed(() => {
  if (!currentWorkflow.value) return null

  return {
    name: currentWorkflow.value.name,
    key: currentWorkflow.value.key,
    id: currentWorkflow.value.id,
    description: currentWorkflow.value.description,
    businessType: currentWorkflow.value.businessType,
    systemFormKey: currentWorkflow.value.systemFormKey,
    businessFormId: currentWorkflow.value.businessFormId
  }
});

// 计算预览区域高度（窗口高度 - 顶部导航 - Tab header - padding）
const previewHeight = computed(() => {
  // 假设顶部导航 50px，Tab header 60px，其他边距 40px
  return window.innerHeight - 150;
});

// 计算预览抽屉高度
const previewDrawerHeight = computed(() => {
  // 抽屉标题 60px，padding 40px
  return window.innerHeight - 100;
});

// 显示流程预览抽屉
const showPreviewDrawer = () => {
  showPreviewDrawerVisible.value = true
}

// 初始化基本信息表单
const initInfoForm = (workflow) => {
  infoForm.name = workflow.name || ''
  infoForm.key = workflow.key || ''
  infoForm.description = workflow.description || ''
  infoForm.businessFormId = workflow.businessFormId || ''
}

// 保存基本信息
const handleSaveInfo = async () => {
  if (!infoFormRef.value) return

  try {
    await infoFormRef.value.validate()

    savingInfo.value = true

    const processData = {
      id: currentWorkflow.value.id,
      name: infoForm.name,
      key: infoForm.key,
      description: infoForm.description,
      businessType: 2, // 固定为业务表单
      businessFormId: infoForm.businessFormId,
      businessView: processStore.currentProcess.businessView || '', // 使用 store 中的值
      appId: props.appId, // 关联应用ID
      nodes: processStore.currentProcess.nodes,
      flows: processStore.currentProcess.flows,
      variables: processStore.currentProcess.variables,
      priority: currentWorkflow.value.priority || 0
    }

    const response = await workflowApi.updateProcessDefinition(processData.id, processData)

    if (response.code === 200) {
      ElMessage.success('基本信息保存成功')
      // 刷新列表以更新流程信息
      await loadWorkflowList()
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('保存基本信息失败:', error)
      ElMessage.error('保存失败')
    }
  } finally {
    savingInfo.value = false
  }
}

// 业务表单变更处理
const handleBusinessFormChange = async (formId) => {
  if (!formId) return

  try {
    // 询问是否刷新流程变量
    await ElMessageBox.confirm(
      '更换业务表单后，是否从新表单重新生成流程变量？',
      '提示',
      {
        confirmButtonText: '是',
        cancelButtonText: '否',
        type: 'warning'
      }
    )

    // 用户确认，重新加载表单字段
    await refreshVariablesFromForm(formId)
  } catch (error) {
    // 用户取消或出错，不做处理
  }
}

// 刷新流程变量
const handleRefreshVariables = async () => {
  if (!infoForm.businessFormId) {
    ElMessage.warning('请先选择业务表单')
    return
  }

  try {
    await ElMessageBox.confirm(
      '刷新后将从业务表单重新生成流程变量，是否继续？',
      '确认刷新',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await refreshVariablesFromForm(infoForm.businessFormId)
  } catch (error) {
    // 用户取消
  }
}

// 从业务表单刷新流程变量
const refreshVariablesFromForm = async (formId) => {
  refreshingVariables.value = true

  try {
    // 1. 获取表单字段信息（用于生成流程变量）
    const tableInfoResponse = await formApi.getTableInfoByFormId(formId)
    const tableData = tableInfoResponse.data

    let variables = []
    if (tableData && tableData.columnInfos && Array.isArray(tableData.columnInfos)) {
      // 过滤出业务字段（非系统字段）
      const businessFields = tableData.columnInfos.filter(column => column.isSysField === 0)

      // 将字段信息转换为流程变量
      variables = businessFields.map(column => ({
        name: column.columnName,
        type: mapColumnTypeToVariableType(column.formType, column.columnType),
        label: column.columnComment || column.columnName,
        required: !column.isNullable
      }))
    }

    // 2. 获取表单配置（用于更新 businessView）
    const formDetailResponse = await formApi.getFormDetail(formId)
    let businessView = ''
    
    if (formDetailResponse.code === 200 && formDetailResponse.data) {
      const formConfig = formDetailResponse.data.formConfig
      if (formConfig) {
        // 确保 businessView 是字符串格式
        businessView = typeof formConfig === 'string' ? formConfig : JSON.stringify(formConfig)
      }
    }

    // 3. 更新到 store
    processStore.currentProcess.variables = variables
    processStore.currentProcess.businessView = businessView
    
    ElMessage.success(`已刷新 ${variables.length} 个流程变量和表单配置`)
  } catch (error) {
    console.error('刷新流程变量失败:', error)
    ElMessage.error('刷新失败')
  } finally {
    refreshingVariables.value = false
  }
}

const loading = ref(false)
const detailLoading = ref(false)
const createLoading = ref(false)
const workflowList = ref([])
const currentWorkflow = ref(null)
const searchQuery = ref('')
const statusFilter = ref(null)
const businessTypeFilter = ref(null)
const activeTab = ref('design')
const showCreateDrawer = ref(false)
const businessFormList = ref([])
const createFormRef = ref(null)
const showPreviewDrawerVisible = ref(false)

// 基本信息表单
const infoFormRef = ref(null)
const savingInfo = ref(false)
const refreshingVariables = ref(false)
const savingVariables = ref(false)
const infoForm = reactive({
  name: '',
  key: '',
  description: '',
  businessFormId: ''
})

const infoFormRules = {
  name: [
    { required: true, message: '请输入流程名称', trigger: 'blur' }
  ],
  key: [
    { required: true, message: '请输入流程标识', trigger: 'blur' },
    { pattern: /^[a-zA-Z_][a-zA-Z0-9_]*$/, message: '流程标识只能包含字母、数字和下划线，且不能以数字开头', trigger: 'blur' }
  ],
  businessFormId: [
    { required: true, message: '请选择业务表单', trigger: 'change' }
  ]
}

// 面板折叠状态
const leftPanelCollapsed = ref(false)
const rightPanelCollapsed = ref(false)
const isFullscreen = ref(false)

// 切换右侧面板
const toggleRightPanel = () => {
  rightPanelCollapsed.value = !rightPanelCollapsed.value
}

// 切换左侧面板
const toggleLeftPanel = () => {
  leftPanelCollapsed.value = !leftPanelCollapsed.value
}

// 全屏切换
const toggleFullscreen = async () => {
  const designContent = document.querySelector('.design-content')

  if (!isFullscreen.value) {
    // 进入全屏
    try {
      if (designContent) {
        await designContent.requestFullscreen()
        isFullscreen.value = true
        ElMessage.success('已进入全屏设计模式，按 ESC 退出')
      }
    } catch (error) {
      console.error('进入全屏失败:', error)
      ElMessage.error('您的浏览器不支持全屏功能')
    }
  } else {
    // 退出全屏
    try {
      await document.exitFullscreen()
      isFullscreen.value = false
      ElMessage.info('已退出全屏模式')
    } catch (error) {
      console.error('退出全屏失败:', error)
    }
  }
}

// 监听全屏变化事件
const handleFullscreenChange = () => {
  // 如果用户通过 ESC 退出全屏，同步状态
  if (!document.fullscreenElement) {
    isFullscreen.value = false
  }
}

const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

const createForm = reactive({
  name: '',
  key: '',
  description: '',
  businessType: 2, // 固定为业务表单
  businessFormId: ''
})

const createFormRules = {
  name: [
    { required: true, message: '请输入流程名称', trigger: 'blur' }
  ],
  key: [
    { required: true, message: '请输入流程标识', trigger: 'blur' },
    { pattern: /^[a-zA-Z_][a-zA-Z0-9_]*$/, message: '流程标识只能包含字母、数字和下划线，且不能以数字开头', trigger: 'blur' }
  ],
  businessFormId: [
    { required: true, message: '请选择业务表单', trigger: 'change' }
  ]
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

// 获取状态标签类型
const getStatusTagType = (status) => {
  return status === 1 ? 'success' : 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  return status === 1 ? '激活' : '禁用'
}

// 加载流程列表
const loadWorkflowList = async () => {
  loading.value = true
  try {
    const response = await workflowApi.getWorkflowList({
      pageNum: pagination.current,
      pageSize: pagination.size,
      name: searchQuery.value || undefined,
      status: statusFilter.value,
      businessType: 2, // 只查询业务表单类型的流程
      appId: props.appId // 直接按应用ID查询
    })

    if (response.code === 200 && response.data) {
      workflowList.value = response.data.records || []
      pagination.total = response.data.total || 0

      // 自动选中第一条记录
      if (workflowList.value.length > 0 && !currentWorkflow.value) {
        await handleSelectWorkflow(workflowList.value[0])
      } else if (currentWorkflow.value) {
        // 如果当前有选中的流程，刷新后重新选中它
        const updatedWorkflow = workflowList.value.find(w => w.id === currentWorkflow.value.id)
        if (updatedWorkflow) {
          await handleSelectWorkflow(updatedWorkflow)
        }
      }
    } else {
      ElMessage.error(response.message || '加载失败')
    }
  } catch (error) {
    console.error('加载流程列表失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

// 选中流程
const handleSelectWorkflow = async (workflow) => {
  try {
    detailLoading.value = true

    console.log('=== 选中流程 ===')
    console.log('workflow参数:', workflow)

    // 获取完整的流程定义详情（确保获取最新数据）
    const response = await workflowApi.getProcessDefinition(workflow.id)

    console.log('=== API响应 ===')
    console.log('response:', response)
    console.log('response.data:', response.data)
    console.log('response.data.nodes:', response.data?.nodes)
    console.log('response.data.flows:', response.data?.flows)
    console.log('nodes 数量:', response.data?.nodes?.length || 0)
    console.log('flows 数量:', response.data?.flows?.length || 0)

    if (response.code === 200 && response.data) {
      // 直接赋值，保持原始数据结构
      currentWorkflow.value = response.data

      console.log('=== 赋值后 ===')
      console.log('currentWorkflow.value:', currentWorkflow.value)
      console.log('currentWorkflow.value.nodes:', currentWorkflow.value.nodes)
      console.log('currentWorkflow.value.flows:', currentWorkflow.value.flows)

      // 初始化基本信息表单
      initInfoForm(response.data)

      // 如果是业务表单类型，需要加载表单字段信息
      if (response.data.businessType === 2 && response.data.businessFormId) {
        console.log('业务表单类型，加载表单字段...')
        // 确保业务表单列表已加载
        if (businessFormList.value.length === 0) {
          await loadBusinessFormList()
        }
        await loadBusinessFormFields(response.data.businessFormId, response.data)
      } else {
        console.log('系统表单或无表单，直接加载到 store...')
        // 系统表单或没有表单，直接加载到 store
        await loadWorkflowToStore(response.data)
      }

      console.log('流程选中成功')
    } else {
      console.error('API返回错误:', response)
      ElMessage.error(response.message || '加载流程详情失败')
      currentWorkflow.value = workflow // 降级使用列表中的数据
    }
  } catch (error) {
    console.error('加载流程详情失败:', error)
    ElMessage.error('加载流程详情失败')
    currentWorkflow.value = workflow // 降级使用列表中的数据
  } finally {
    detailLoading.value = false
  }
}

// 加载业务表单字段信息
const loadBusinessFormFields = async (formId, workflowData) => {
  try {
    console.log('=== 加载业务表单字段 ===')
    console.log('formId:', formId)
    console.log('workflowData 传入时:', workflowData)
    console.log('workflowData.nodes:', workflowData.nodes)
    console.log('workflowData.flows:', workflowData.flows)

    const response = await formApi.getTableInfoByFormId(formId)
    const data = response.data

    if (data && data.columnInfos && Array.isArray(data.columnInfos)) {
      // 过滤出业务字段（非系统字段）
      const businessFields = data.columnInfos.filter(column => column.isSysField === 0)

      // 将字段信息转换为流程变量
      const variables = businessFields.map(column => ({
        name: column.columnName,
        type: mapColumnTypeToVariableType(column.formType, column.columnType),
        label: column.columnComment || column.columnName,
        required: !column.isNullable
      }))

      // 如果流程定义中已有变量，使用流程定义中的变量（可能已经被修改过）
      // 否则使用从表单字段生成的变量
      const finalVariables = workflowData.variables && workflowData.variables.length > 0
        ? workflowData.variables
        : variables

      // 更新工作流数据的变量（不要修改原始的 nodes 和 flows）
      workflowData.variables = finalVariables

      console.log('加载表单字段后，workflowData.nodes:', workflowData.nodes)
      console.log('加载表单字段后，workflowData.flows:', workflowData.flows)
    }

    // 无论是否成功加载字段，都要加载到 store
    await loadWorkflowToStore(workflowData)
  } catch (error) {
    console.error('加载业务表单字段失败:', error)
    // 加载失败，仍然要加载流程数据到 store
    await loadWorkflowToStore(workflowData)
  }
}

// 映射数据库列类型到流程变量类型
const mapColumnTypeToVariableType = (formType, columnType) => {
  // 优先使用 formType（表单组件类型）
  if (formType) {
    const formTypeMap = {
      'input': 'string',
      'textarea': 'string',
      'number': 'number',
      'select': 'string',
      'radio': 'string',
      'checkbox': 'string',
      'date': 'date',
      'datetime': 'date',
      'time': 'date',
      'switch': 'boolean',
      'slider': 'number',
      'rate': 'number',
      'upload': 'string',
      'user-select': 'user-select',
      'department-select': 'department-select',
      'role-select': 'role-select'
    }
    if (formTypeMap[formType]) {
      return formTypeMap[formType]
    }
  }

  // 降级使用 columnType（数据库列类型）
  if (columnType) {
    const lowerType = columnType.toLowerCase()
    if (lowerType.includes('int') || lowerType.includes('decimal') || lowerType.includes('float') || lowerType.includes('double')) {
      return 'number'
    }
    if (lowerType.includes('date') || lowerType.includes('time')) {
      return 'date'
    }
    if (lowerType.includes('bool') || lowerType.includes('bit')) {
      return 'boolean'
    }
    if (lowerType.includes('json') || lowerType.includes('text')) {
      return 'object'
    }
  }

  // 默认返回字符串类型
  return 'string'
}

// 加载流程定义到 processDesigner store
const loadWorkflowToStore = async (workflow) => {
  try {
    console.log('=== 开始加载流程到store ===')
    console.log('workflow:', workflow)
    console.log('workflow.nodes:', workflow.nodes)
    console.log('workflow.flows:', workflow.flows)
    console.log('workflow.nodes 数量:', workflow.nodes?.length || 0)
    console.log('workflow.flows 数量:', workflow.flows?.length || 0)

    // 更新 store - 直接赋值数组，确保响应式更新
    processStore.currentProcess.id = workflow.id
    processStore.currentProcess.name = workflow.name
    processStore.currentProcess.key = workflow.key
    processStore.currentProcess.description = workflow.description || ''
    processStore.currentProcess.businessType = workflow.businessType
    processStore.currentProcess.businessFormId = workflow.businessFormId || ''
    processStore.currentProcess.systemFormKey = workflow.systemFormKey || ''
    processStore.currentProcess.businessView = workflow.businessView || ''
    processStore.currentProcess.nodes = workflow.nodes || []
    processStore.currentProcess.flows = workflow.flows || []
    processStore.currentProcess.variables = workflow.variables || []

    console.log('流程加载到store成功')
    console.log('store.currentProcess:', processStore.currentProcess)
    console.log('store.currentProcess.nodes:', processStore.currentProcess.nodes)
    console.log('store.currentProcess.flows:', processStore.currentProcess.flows)
    console.log('store.nodes 数量:', processStore.currentProcess.nodes.length)
    console.log('store.flows 数量:', processStore.currentProcess.flows.length)
    console.log('=== 加载流程到store完成 ===')

    // 清除选中状态
    processStore.selectedNodeId = ''
    processStore.selectedFlowId = ''
  } catch (error) {
    console.error('加载流程定义到store失败:', error)
    ElMessage.error('加载流程定义失败')
  }
}

// 设计器保存回调
const handleDesignSaved = async () => {
  try {
    // 从 store 获取当前流程定义
    const processData = {
      id: processStore.currentProcess.id,
      name: processStore.currentProcess.name,
      key: processStore.currentProcess.key,
      description: processStore.currentProcess.description,
      businessType: processStore.currentProcess.businessType,
      businessFormId: processStore.currentProcess.businessFormId,
      systemFormKey: processStore.currentProcess.systemFormKey,
      businessView: processStore.currentProcess.businessView,
      appId: props.appId, // 关联应用ID
      // 直接传递 nodes 和 flows 数组，而不是 jsonDef 字符串
      nodes: processStore.currentProcess.nodes,
      flows: processStore.currentProcess.flows,
      variables: processStore.currentProcess.variables,
      priority: processStore.currentProcess.priority || 0
    }

    const response = await workflowApi.updateProcessDefinition(processData.id, processData)

    if (response.code === 200) {
      ElMessage.success('保存成功')
      // 刷新列表以更新流程信息
      await loadWorkflowList()
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存流程失败:', error)
    ElMessage.error('保存失败')
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  currentWorkflow.value = null
  loadWorkflowList()
}

// 创建流程
const handleCreate = () => {
  // 重置表单
  Object.assign(createForm, {
    name: '',
    key: '',
    description: '',
    businessType: 2, // 固定为业务表单
    businessFormId: ''
  })

  // 加载当前应用的业务表单列表
  loadBusinessFormList()

  showCreateDrawer.value = true
}

// 加载业务表单列表（只加载当前应用的表单）
const loadBusinessFormList = async () => {
  try {
    // 通过应用条目获取当前应用下的表单
    const response = await wbAppEntryApi.getAppEntryTree(props.appId)

    if (response.code === 200 && response.data) {
      // 递归函数：从树结构中提取所有类型为 form 的条目
      const extractFormEntries = (nodes) => {
        let formEntries = []
        for (const node of nodes) {
          if (node.type === 'form') {
            formEntries.push(node)
          }
          // 如果有子节点，递归提取
          if (node.children && node.children.length > 0) {
            formEntries = formEntries.concat(extractFormEntries(node.children))
          }
        }
        return formEntries
      }

      // 筛选出类型为 form 的条目（包括嵌套的）
      const formEntries = extractFormEntries(response.data)

      if (formEntries.length === 0) {
        ElMessage.warning('当前应用下没有可用的表单')
        businessFormList.value = []
        return
      }

      // 获取每个表单条目对应的表单信息
      const formPromises = formEntries.map(async (entry) => {
        try {
          const formResponse = await formApi.getFormByEntryId(entry.id)
          if (formResponse.code === 200 && formResponse.data) {
            return {
              ...formResponse.data,
              entryId: entry.id,
              entryName: entry.name
            }
          }
        } catch (error) {
          console.error(`加载表单 ${entry.name} 失败:`, error)
        }
        return null
      })

      const forms = await Promise.all(formPromises)
      businessFormList.value = forms.filter(f => f !== null)

      console.log('加载到的业务表单列表:', businessFormList.value)
    }
  } catch (error) {
    console.error('加载业务表单列表失败:', error)
    ElMessage.error('加载表单列表失败')
  }
}

// 提交创建流程
const handleCreateSubmit = async () => {
  if (!createFormRef.value) return

  try {
    await createFormRef.value.validate()

    createLoading.value = true

    // 先获取业务表单的字段信息（如果是业务表单）
    let variables = []
    let businessView = ''

    if (createForm.businessType === 2 && createForm.businessFormId) {
      try {
        const response = await formApi.getTableInfoByFormId(createForm.businessFormId)
        const data = response.data

        if (data && data.columnInfos && Array.isArray(data.columnInfos)) {
          // 过滤出业务字段（非系统字段）
          const businessFields = data.columnInfos.filter(column => column.isSysField === 0)

          // 将字段信息转换为流程变量
          variables = businessFields.map(column => ({
            name: column.columnName,
            type: mapColumnTypeToVariableType(column.formType, column.columnType),
            label: column.columnComment || column.columnName,
            required: !column.isNullable
          }))
        }

        // 获取表单配置作为 businessView
        const formResponse = await formApi.getFormDetail(createForm.businessFormId)
        if (formResponse.code === 200 && formResponse.data && formResponse.data.formConfig) {
          businessView = formResponse.data.formConfig
        }
      } catch (error) {
        console.error('加载业务表单字段失败:', error)
      }
    }

    // 构建流程定义数据
    const processData = {
      name: createForm.name,
      key: createForm.key,
      description: createForm.description || '',
      businessType: 2, // 固定为业务表单
      businessFormId: createForm.businessFormId || '',
      businessView: businessView || '', // 表单视图配置
      appId: props.appId, // 关联应用ID
      nodes: [], // 空的节点数组，允许用户在设计器中添加
      flows: [], // 空的连接数组
      variables: variables || [], // 从表单字段生成的变量
      priority: 0 // 普通优先级
    }

    console.log('创建流程数据:', processData)

    const response = await workflowApi.deployProcess(processData)

    if (response.code === 200 && response.data) {
      ElMessage.success('流程创建成功');
      showCreateDrawer.value = false;

      // 刷新列表
      await loadWorkflowList();

      // 选中新创建的流程并进入设计模式
      const newWorkflow = response.data;

      // 确保新流程有 nodes 和 flows 字段
      if (!newWorkflow.nodes) {
        newWorkflow.nodes = [];
      }
      if (!newWorkflow.flows) {
        newWorkflow.flows = [];
      }
      if (!newWorkflow.variables) {
        newWorkflow.variables = variables;
      }

      currentWorkflow.value = newWorkflow;

      // 加载到 store（包含变量信息）
      await loadWorkflowToStore(newWorkflow);

      // 切换到设计Tab
      activeTab.value = 'design';
    } else {
      ElMessage.error(response.message || '创建失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('创建流程失败:', error)
      ElMessage.error('创建失败')
    }
  } finally {
    createLoading.value = false
  }
}

// 编辑流程
const handleEdit = (row) => {
  // 切换到设计Tab
  activeTab.value = 'design';
  ElMessage.info('已切换到流程设计模式');
};

// 处理列表项操作
const handleItemAction = (command, item) => {
  switch (command) {
    case 'edit':
      handleEdit(item);
      break;
    case 'toggle':
      handleToggleStatus(item);
      break;
    case 'delete':
      handleDelete(item);
      break;
  }
};

// 切换状态
const handleToggleStatus = async (row) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    const action = newStatus === 1 ? '激活' : '禁用'

    await ElMessageBox.confirm(`确定要${action}该流程吗？`, '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await workflowApi.changeWorkflowStatus(row.id, newStatus)

    if (response.code === 200) {
      ElMessage.success(`${action}成功`)
      await loadWorkflowList()
    } else {
      ElMessage.error(response.message || `${action}失败`)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('切换状态失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 删除流程
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该流程吗？删除后无法恢复。', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await workflowApi.deleteWorkflow(row.id)

    if (response.code === 200) {
      ElMessage.success('删除成功')
      // 如果删除的是当前选中的流程，清空选中状态
      if (currentWorkflow.value && currentWorkflow.value.id === row.id) {
        currentWorkflow.value = null
      }
      await loadWorkflowList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除流程失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 分页变化
const handlePageChange = () => {
  loadWorkflowList()
}

// ProcessCanvas 事件处理
const handleNodeAdded = (nodeData) => {
  processStore.addNode(nodeData.type, nodeData.position)
}

const handleNodeDeleted = (nodeId) => {
  processStore.deleteNode(nodeId)
}

const handleNodeSelected = (nodeId) => {
  processStore.selectNode(nodeId)
}

const handleNodeUpdated = (nodeId, updates) => {
  processStore.updateNode(nodeId, updates)
}

const handleFlowAdded = (flowData) => {
  processStore.addFlow(flowData.from, flowData.to, flowData.condition)
}

const handleFlowDeleted = (flowId) => {
  processStore.deleteFlow(flowId)
}

const handleFlowSelected = (flowId) => {
  processStore.selectFlow(flowId)
}

// ProcessPropertiesPanel 事件处理
const handleNodePropertyUpdated = (nodeId, updates) => {
  processStore.updateNode(nodeId, updates)
}

const handleFlowUpdated = (flowId, updates) => {
  const flow = processStore.currentProcess.flows.find(f => f.id === flowId)
  if (flow) {
    Object.assign(flow, updates)
    processStore.saveToHistory()
  }
}

const handleVariableAdded = (variable) => {
  processStore.addVariable(variable)
}

const handleVariableDeleted = (variableName) => {
  processStore.deleteVariable(variableName)
}

const handleProcessUpdated = (updates) => {
  Object.assign(processStore.currentProcess, updates)
  processStore.saveToHistory()
}

// 重置设计
const handleResetDesign = async () => {
  try {
    await ElMessageBox.confirm('确定要重置流程设计吗？所有未保存的更改将丢失。', '确认重置', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    // 重新加载流程定义
    if (currentWorkflow.value) {
      await loadWorkflowToStore(currentWorkflow.value)
      ElMessage.success('已重置')
    }
  } catch (error) {
    // 用户取消
  }
}

// 验证工作流
const validateWorkflow = () => {
  const errors = processStore.validateProcess()

  if (errors.length === 0) {
    ElMessage.success('工作流验证通过！')
  } else {
    ElMessageBox.alert(
      `<div style="max-height: 400px; overflow-y: auto;">
        <ul style="margin: 0; padding-left: 20px;">
          ${errors.map(err => `<li style="margin: 8px 0;">${err}</li>`).join('')}
        </ul>
      </div>`,
      '工作流验证失败',
      {
        dangerouslyUseHTMLString: true,
        type: 'error',
        confirmButtonText: '知道了'
      }
    )
  }
}

// 清空画布
const clearWorkflow = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空画布吗？所有节点和连接将被删除，此操作不可恢复。',
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
    // 用户取消
  }
}

// 调试函数
const debugProcessCanvas = () => {
  console.log('=== ProcessCanvas 调试信息 ===')
  console.log('currentWorkflow.value:', currentWorkflow.value)
  console.log('activeTab.value:', activeTab.value)
  console.log('processStore.currentProcess:', processStore.currentProcess)
  console.log('processStore.currentProcess.nodes:', processStore.currentProcess.nodes)
  console.log('processStore.currentProcess.flows:', processStore.currentProcess.flows)
  console.log('processStore.currentProcess.nodes.length:', processStore.currentProcess.nodes.length)
  console.log('processStore.currentProcess.flows.length:', processStore.currentProcess.flows.length)
  console.log('v-if 条件:', !!currentWorkflow.value && processStore.currentProcess.nodes.length > 0)
}

onMounted(() => {
  loadWorkflowList()
  // 监听全屏变化
  document.addEventListener('fullscreenchange', handleFullscreenChange)
})

onUnmounted(() => {
  // 清理全屏监听
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})

// 监听 activeTab 变化，当切换到设计Tab时确保数据已加载
watch(activeTab, async (newTab) => {
  if (newTab === 'design' && currentWorkflow.value) {
    console.log('=== 切换到设计Tab ===')
    console.log('currentWorkflow.value:', currentWorkflow.value)
    console.log('processStore.currentProcess:', processStore.currentProcess)
    console.log('processStore.currentProcess.nodes:', processStore.currentProcess.nodes)
    console.log('processStore.currentProcess.flows:', processStore.currentProcess.flows)

    // 如果 store 中没有数据，重新加载
    if (processStore.currentProcess.nodes.length === 0 && currentWorkflow.value.nodes && currentWorkflow.value.nodes.length > 0) {
      console.log('检测到 store 中没有数据，重新加载...')
      await loadWorkflowToStore(currentWorkflow.value)
    }

    // 等待 DOM 更新后，将画布滚动条居中
    await nextTick();
    setTimeout(() => {
      const canvasElement = document.querySelector('.process-canvas');
      if (canvasElement) {
        const rect = canvasElement.getBoundingClientRect();
        const scrollLeft = (3000 - rect.width) / 2;
        const scrollTop = (2000 - rect.height) / 2;
        canvasElement.scrollLeft = scrollLeft;
        canvasElement.scrollTop = scrollTop;
        console.log('画布滚动条已居中', { scrollLeft, scrollTop });
      }
    }, 100);
  }
});
</script>

<style lang="scss" scoped>
.workflow-engine-view {
  flex: 1;
  display: flex;
  overflow: hidden;
  background: var(--bg-primary);
}

// 左侧流程列表
.workflow-sidebar {
  width: 320px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-color);
  overflow: hidden;

  .sidebar-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    border-bottom: 1px solid var(--border-color);
    flex-shrink: 0;

    .sidebar-title {
      font-size: 15px;
      font-weight: 600;
      color: var(--text-primary);
    }
  }

  .search-section {
    height: 60px;
    display: flex;
    align-items: center;
    padding: 0 16px;
    border-bottom: 1px solid var(--border-color);
    flex-shrink: 0;
  }

  .workflow-list {
    flex: 1;
    overflow-y: auto;
    padding: 8px;
  }

  .workflow-item {
    display: flex;
    align-items: center;
    gap: 8px;
    background: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 8px;
    padding: 12px;
    margin-bottom: 8px;
    transition: all 0.2s;
    cursor: pointer;

    &:hover {
      border-color: var(--primary-color);
      box-shadow: var(--shadow-sm);

      .item-actions {
        opacity: 1;
      }
    }

    &.active {
      border-color: var(--primary-color);
      background: var(--primary-light);
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

      .workflow-name {
        font-size: 14px;
        font-weight: 500;
        color: var(--text-primary);
        flex: 1;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }

    .item-footer {
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 12px;
      color: var(--text-tertiary);

      .time-info {
        display: flex;
        align-items: center;
        gap: 4px;

        .el-icon {
          font-size: 12px;
        }
      }

      .version-info {
        font-weight: 500;
      }
    }

    .item-actions {
      flex-shrink: 0;
      opacity: 0;
      transition: opacity 0.2s;

      .more-icon {
        font-size: 16px;
        color: var(--text-secondary);
        cursor: pointer;
        padding: 4px;
        border-radius: 4px;

        &:hover {
          background: var(--bg-card);
          color: var(--primary-color);
        }
      }
    }
  }

  .list-pagination {
    padding: 12px 16px;
    border-top: 1px solid var(--border-color);
    display: flex;
    justify-content: center;
    flex-shrink: 0;
  }
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

.detail-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-height: 0;
  position: relative;

  .tabs-wrapper {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    min-height: 0;
    position: relative;
  }

  .tab-actions {
    position: absolute;
    top: 0;
    right: 24px;
    height: 60px;
    display: flex;
    align-items: center;
    gap: 8px;
    z-index: 10;
    pointer-events: auto;
  }

  .custom-tabs {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    min-height: 0;

    :deep(.el-tabs__header) {
      height: 60px;
      min-height: 60px;
      max-height: 60px;
      margin: 0;
      padding: 0 24px;
      padding-right: 280px; // 调整为两个按钮的宽度
      border-bottom: 1px solid var(--border-color);
      background: var(--bg-card);
      flex-shrink: 0;
      display: flex;
      align-items: center;
      box-sizing: border-box;
    }

    :deep(.el-tabs__nav-wrap) {
      height: 100%;
      display: flex;
      align-items: center;
      flex: 0 0 auto;
    }

    :deep(.el-tabs__content) {
      flex: 1;
      overflow: hidden;
      min-height: 0;
      display: flex;
      flex-direction: column;
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
    }
  }

  .tab-content {
    flex: 1;
    overflow-y: auto;
    padding: 24px;
    min-height: 0;

    &.scrollable {
      overflow-y: auto;
    }

    &.design-content {
      padding: 0;
      overflow: hidden;
      display: flex;
      flex-direction: column;

      &.fullscreen-mode {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 9999;
        background: var(--bg-primary);

        .designer-main {
          height: 100vh;
        }
      }

      .designer-main {
        flex: 1;
        display: flex;
        overflow: hidden;
        min-height: 0;

        // 左侧节点面板
        .designer-left {
          width: 280px;
          flex-shrink: 0;
          background: var(--bg-secondary);
          border-right: 1px solid var(--border-color);
          display: flex;
          flex-direction: column;
          overflow: hidden;
          transition: width 0.3s ease;

          &.collapsed {
            width: 48px;

            .panel-content {
              display: none;
            }
          }

          .panel-header {
            height: 60px;
            padding: 0 16px;
            border-bottom: 1px solid var(--border-color);
            flex-shrink: 0;
            display: flex;
            align-items: center;
            justify-content: space-between;

            h4 {
              margin: 0;
              font-size: 14px;
              font-weight: 600;
              color: var(--text-primary);
              flex: 1;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }

            .collapse-btn {
              flex-shrink: 0;
              margin-left: 8px;
            }
          }

          .panel-content {
            flex: 1;
            overflow-y: auto;
            padding: 12px;
          }
        }

        // 中间画布
        .designer-center {
          flex: 1;
          display: flex;
          flex-direction: column;
          overflow: hidden;
          min-width: 0;
          background: var(--bg-primary);

          .canvas-toolbar {
            height: 60px;
            flex-shrink: 0;
            padding: 0 16px;
            background: var(--bg-card);
            border-bottom: 1px solid var(--border-color);
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;

            .toolbar-left {
              display: flex;
              align-items: center;
              gap: 8px;
            }

            .toolbar-right {
              display: flex;
              align-items: center;
              gap: 12px;

              .canvas-stats {
                display: flex;
                align-items: center;
                gap: 8px;
              }
            }
          }

          .canvas-wrapper {
            flex: 1;
            overflow: hidden;
            position: relative;
            min-height: 0;

            .canvas-empty {
              width: 100%;
              height: 100%;
              display: flex;
              align-items: center;
              justify-content: center;
            }
          }
        }

        // 右侧属性面板
        .designer-right {
          width: 360px;
          flex-shrink: 0;
          background: var(--bg-secondary);
          border-left: 1px solid var(--border-color);
          display: flex;
          flex-direction: column;
          overflow: hidden;
          transition: width 0.3s ease;

          &.collapsed {
            width: 48px;

            .panel-content {
              display: none;
            }
          }

          .panel-header {
            height: 56px;
            padding: 0 16px;
            border-bottom: 1px solid var(--border-color);
            flex-shrink: 0;
            display: flex;
            align-items: center;
            justify-content: space-between;

            h4 {
              margin: 0;
              font-size: 14px;
              font-weight: 600;
              color: var(--text-primary);
              flex: 1;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }

            .collapse-btn {
              flex-shrink: 0;
              margin-left: 8px;
            }
          }

          .panel-content {
            flex: 1;
            overflow-y: auto;
            padding: 16px;
          }
        }
      }
    }
  }

  .info-section {
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

    .info-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 16px;

      .info-item {
        display: flex;
        gap: 12px;
        font-size: 14px;

        &.full-width {
          grid-column: 1 / -1;
        }

        label {
          color: var(--text-secondary);
          font-weight: 500;
          flex-shrink: 0;
          min-width: 100px;
        }

        span {
          color: var(--text-primary);
          flex: 1;
          word-break: break-all;
        }
      }
    }
  }
}

.empty-state {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.form-help-text {
  font-size: 12px;
  color: var(--text-secondary);
  margin-top: 4px;
  line-height: 1.5;
}

// 流程变量只读展示样式
.variables-readonly-list {
  width: 100%;

  .no-variables-hint {
    padding: 8px 12px;
    background: var(--bg-tertiary);
    border-radius: 6px;
    text-align: center;
  }

  .variables-grid {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    padding: 8px 12px;
    background: var(--bg-tertiary);
    border-radius: 6px;
    max-height: 200px;
    overflow-y: auto;

    .variable-chip {
      display: flex;
      align-items: center;
      gap: 6px;
      padding: 4px 8px;
      background: var(--bg-card);
      border-radius: 4px;
      border: 1px solid var(--border-color);

      .variable-type {
        font-size: 11px;
      }
    }
  }
}

.form-help-text {
  font-size: 12px;
  color: var(--text-secondary);
  margin-top: 4px;
  line-height: 1.5;
}
</style>
