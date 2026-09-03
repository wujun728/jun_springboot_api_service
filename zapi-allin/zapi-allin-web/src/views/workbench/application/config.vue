<template>
  <div class="app-config-layout">
    <!-- 顶部导航栏 -->
    <header class="top-header">
      <div class="header-left">
        <div class="logo">
          <img src="/logo3.png" alt="Logo" class="logo-img">
          <span class="project-name">{{ appName }}</span>
        </div>
      </div>

      <div class="header-center">
        <!-- 图标导航栏 -->
        <div class="icon-nav-bar-horizontal">
          <div
            v-for="item in navItems"
            :key="item.key"
            class="nav-item"
            :class="{ 'is-active': activeNavItem === item.key }"
            @click="handleNavChange(item.key)"
          >
            <el-icon :size="20">
              <component :is="item.icon" />
            </el-icon>
            <span class="nav-label">{{ item.label }}</span>
          </div>
        </div>
      </div>

      <div class="header-right">

        <el-dropdown @command="handleDropdownCommand" class="user-dropdown-wrapper">
          <div class="user-info">
            <div class="user-avatar">
              <el-icon><User /></el-icon>
            </div>
            <span class="user-name">{{ userStore.displayName }}</span>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人资料
              </el-dropdown-item>
              <el-dropdown-item command="appearance">
                <el-icon><Brush /></el-icon>
                外观设置
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>
                系统设置
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon>
                退出
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="config-container">
      <!-- 应用配置视图 -->
      <AppConfigView
        v-if="activeNavItem === 'appConfig'"
        ref="appConfigViewRef"
        :app-id="appId"
        :entry-tree="entryTree"
        :tree-loading="treeLoading"
        @add-entry="handleAddEntry"
        @node-click="handleNodeClick"
        @node-action="handleNodeAction"
        @node-drop="handleNodeDrop"
        @task-menu-click="handleTaskMenuClick"
      >
        <!-- 右侧内容区域 -->
        <!-- 任务中心视图 -->
        <TaskCenterView
          v-if="activeTaskView"
          :active-view="activeTaskView"
          :app-id="appId"
        />

        <!-- 应用配置内容 -->
        <template v-else>
          <ConfigHeader
            :app-name="appName"
            :breadcrumb-path="breadcrumbPath"
            :current-node="currentNode"
            :current-form-info="currentFormInfo"
            :current-grid-info="currentGridInfo"
            @edit-form="handleEditForm"
            @view-form-data="handleViewFormData"
            @form-settings="handleFormSettings"
            @edit-grid="handleEditGrid"
          />

          <div class="config-content">
            <!-- 表单预览 -->
            <FormPreviewSection
              v-if="currentNode && currentNode.type === 'form'"
              ref="formPreviewRef"
              :form-info="currentFormInfo"
              :loading="formInfoLoading"
              :save-loading="saveFormDataLoading"
              :app-id="appId"
              :entry-id="currentNode?.id"
              :has-flow="currentNode?.hasFlow"
              @edit="handleEditForm"
              @save="handleSaveFormData"
              @reset="handleResetFormData"
            />

            <!-- 表格预览 -->
            <GridPreviewSection
              v-else-if="currentNode && currentNode.type === 'grid'"
              :grid-info="currentGridInfo"
              :grid-data="gridPreviewData"
              :pagination="gridPagination"
              :loading="formInfoLoading"
              :data-loading="gridDataLoading"
              @edit="handleEditGrid"
              @view-row="handleViewGridRow"
              @edit-row="handleEditGridRow"
              @delete-row="handleDeleteGridRow"
              @page-change="loadGridPreviewData"
            />

            <!-- 空状态 -->
            <el-empty
              v-else
              description="请选择一个应用条目"
              :image-size="200"
            />
          </div>
        </template>
      </AppConfigView>

      <!-- 动作引擎视图 -->
      <ActionEngineView
        v-else-if="activeNavItem === 'actionEngine'"
        :actions="actionList"
        :loading="actionLoading"
        @add="handleAddAction"
        @edit="handleEditAction"
        @delete="handleDeleteAction"
        @copy="handleCopyAction"
        @save="handleSaveAction"
        @test="handleTestAction"
      />

      <!-- 流程引擎视图 -->
      <WorkflowEngineView
        v-else-if="activeNavItem === 'workflowEngine'"
        :app-id="appId"
      />
    </div>

    <!-- 表单设计器抽屉 -->
    <FormDesignerDrawer
      v-model="showFormDesigner"
      :form-id="designerFormId"
      :form-info="currentFormInfo"
      @saved="handleFormDesignerSaved"
      @closed="handleFormDesignerClosed"
    />

    <!-- 表格设计器抽屉 -->
    <GridDesignerDrawer
      v-model="showGridDesigner"
      :grid-id="designerGridId"
      :app-id="appId"
      :grid-info="currentGridInfo"
      @saved="handleGridDesignerSaved"
      @closed="handleGridDesignerClosed"
    />

    <!-- 表单数据查看抽屉 -->
    <FormDataDrawer
      v-model="showFormDataDrawer"
      :form-components="formPreviewConfig.components"
      :data-list="formDataList"
      :pagination="formDataPagination"
      :loading="formDataLoading"
      @close="handleCloseFormDataDrawer"
      @view-row="handleViewFormDataRow"
      @edit-row="handleEditFormDataRow"
      @delete-row="handleDeleteFormDataRow"
      @page-change="loadFormDataList"
    />

    <!-- 条目对话框 -->
    <EntryDialog
      v-model="showEntryDialog"
      :title="entryDialogTitle"
      :entry-form="entryForm"
      :submit-loading="entrySubmitLoading"
      @submit="handleEntrySubmit"
      @close="handleEntryDialogClose"
    />

    <!-- 外观设置抽屉 -->
    <el-drawer
      v-model="appearanceDrawerVisible"
      title="外观设置"
      direction="rtl"
      size="680px"
      :close-on-click-modal="false"
    >
      <AppearanceDrawer />
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  User,
  Setting,
  SwitchButton,
  ArrowDown,
  Brush,
  Operation,
  Share
} from '@element-plus/icons-vue'
import { wbAppEntryApi, formApi } from '@/api'
import gridApi from '@/api/grid'
import { actionEngineApi } from '@/api/actionEngine'
import AppConfigView from '@/components/workbench/AppConfigView.vue'
import ActionEngineView from '@/components/workbench/ActionEngineView.vue'
import WorkflowEngineView from '@/components/workbench/WorkflowEngineView.vue'
import ConfigHeader from '@/components/workbench/ConfigHeader.vue'
import FormPreviewSection from '@/components/workbench/FormPreviewSection.vue'
import GridPreviewSection from '@/components/workbench/GridPreviewSection.vue'
import FormDesignerDrawer from '@/components/form-designer/FormDesignerDrawer.vue'
import GridDesignerDrawer from '@/components/grid-designer/GridDesignerDrawer.vue'
import FormDataDrawer from '@/components/workbench/FormDataDrawer.vue'
import AppearanceDrawer from '@/components/AppearanceDrawer.vue'
import TaskCenterView from '@/components/workbench/TaskCenterView.vue'
import EntryDialog from '@/components/workbench/EntryDialog.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 基础信息
const appId = ref(route.query.appId || '')
const appName = ref(route.query.appName || '应用配置')

// 导航状态
const activeNavItem = ref('appConfig')

// 导航项配置
const navItems = [
  {
    key: 'appConfig',
    label: '应用配置',
    icon: Setting
  },
  {
    key: 'workflowEngine',
    label: '流程引擎',
    icon: Share
  },
  {
    key: 'actionEngine',
    label: '动作引擎',
    icon: Operation
  }
]

// 应用配置相关
const entryTree = ref([])
const treeLoading = ref(false)
const currentNode = ref(null)
const currentFormInfo = ref(null)
const currentGridInfo = ref(null)
const formInfoLoading = ref(false)
const gridPreviewData = ref([])
const gridDataLoading = ref(false)
const gridPagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 表单设计器状态
const showFormDesigner = ref(false)
const designerFormId = ref(null)

// 表格设计器状态
const showGridDesigner = ref(false)
const designerGridId = ref(null)

// 表单数据查看抽屉状态
const showFormDataDrawer = ref(false)
const formDataList = ref([])
const formDataLoading = ref(false)
const formDataPagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 表单预览引用
const formPreviewRef = ref(null)
const saveFormDataLoading = ref(false)

// AppConfigView 引用
const appConfigViewRef = ref(null)

// 动作引擎相关
const actionList = ref([])
const actionLoading = ref(false)

// 任务中心相关
const activeTaskView = ref('')

// 条目对话框相关
const showEntryDialog = ref(false)
const entryDialogTitle = ref('添加条目')
const entrySubmitLoading = ref(false)
const entryForm = ref({
  id: null,
  name: '',
  type: 'group',
  parentId: null,
  appId: appId.value,
  icon: '',
  color: '',
  sort: 0,
  hasFlow: 0
})

// 面包屑路径
const breadcrumbPath = computed(() => {
  if (!currentNode.value) return []
  const path = []
  let node = currentNode.value
  while (node) {
    path.unshift(node)
    if (node.parentId) {
      node = findNodeById(entryTree.value, node.parentId)
    } else {
      node = null
    }
  }
  return path
})

// 表单预览配置
const formPreviewConfig = computed(() => {
  if (!currentFormInfo.value || !currentFormInfo.value.formConfig) {
    return {
      labelWidth: '100px',
      labelPosition: 'right',
      size: 'default',
      disabled: false,
      inline: false,
      components: [],
      style: {}
    }
  }

  try {
    const config = JSON.parse(currentFormInfo.value.formConfig)
    return {
      labelWidth: config.labelWidth || '100px',
      labelPosition: config.labelPosition || 'right',
      size: config.size || 'default',
      disabled: config.disabled || false,
      inline: config.inline || false,
      components: config.components || [],
      style: config.style || {}
    }
  } catch (error) {
    console.error('解析表单配置失败:', error)
    return {
      labelWidth: '100px',
      labelPosition: 'right',
      size: 'default',
      disabled: false,
      inline: false,
      components: [],
      style: {}
    }
  }
})

// 查找节点
const findNodeById = (nodes, id) => {
  for (const node of nodes) {
    if (node.id === id) return node
    if (node.children) {
      const found = findNodeById(node.children, id)
      if (found) return found
    }
  }
  return null
}

// 导航切换
const handleNavChange = (key) => {
  console.log('切换导航:', key)
  activeNavItem.value = key
  if (key === 'actionEngine') {
    loadActionList()
  }
}

// 查找第一个表单或表格节点
const findFirstFormOrGridNode = (nodes) => {
  for (const node of nodes) {
    // 如果当前节点是表单或表格，返回它
    if (node.type === 'form' || node.type === 'grid') {
      return node
    }
    // 如果有子节点，递归查找
    if (node.children && node.children.length > 0) {
      const found = findFirstFormOrGridNode(node.children)
      if (found) return found
    }
  }
  return null
}

// 加载应用条目树
const loadEntryTree = async () => {
  if (!appId.value) return
  treeLoading.value = true
  try {
    const response = await wbAppEntryApi.getAppEntryTree(appId.value)
    if (response.code === 200) {
      entryTree.value = buildTree(response.data || [])

      // 使用 nextTick 确保树已经渲染完成
      await new Promise(resolve => setTimeout(resolve, 100))

      // 自动选中第一个表单或表格节点
      const firstNode = findFirstFormOrGridNode(entryTree.value)
      if (firstNode) {
        console.log('自动选中第一个表单/表格节点:', firstNode)
        // 设置树的当前选中节点
        if (appConfigViewRef.value) {
          appConfigViewRef.value.setCurrentKey(firstNode.id)
        }
        // 触发节点点击事件，加载对应内容
        await handleNodeClick(firstNode)
      }
    }
  } catch (error) {
    ElMessage.error('加载应用条目失败')
  } finally {
    treeLoading.value = false
  }
}

// 构建树
const buildTree = (list) => {
  const map = {}
  const roots = []
  list.forEach(item => {
    map[item.id] = { ...item, children: [] }
  })
  list.forEach(item => {
    if (item.parentId && map[item.parentId]) {
      map[item.parentId].children.push(map[item.id])
    } else {
      roots.push(map[item.id])
    }
  })
  return roots
}

// 节点点击
const handleNodeClick = async (data) => {
  console.log('点击节点:', data)
  currentNode.value = data

  // 清除任务中心视图
  activeTaskView.value = ''

  // 根据节点类型显示不同的内容
  if (data.type === 'form') {
    // 表单类型：加载表单信息
    await loadFormInfo(data.id)
    currentGridInfo.value = null
  } else if (data.type === 'grid') {
    // 表格类型：加载表格信息
    await loadGridInfo(data.id)
    currentFormInfo.value = null
  } else {
    // 分组类型：清空表单和表格信息
    currentFormInfo.value = null
    currentGridInfo.value = null
  }
}

// 添加条目
const handleAddEntry = (parentNode) => {
  entryDialogTitle.value = parentNode ? '添加子项' : '添加条目'
  entryForm.value = {
    id: null,
    name: '',
    type: 'group',
    parentId: parentNode ? parentNode.id : null,
    appId: appId.value,
    icon: '',
    color: '',
    sort: 0,
    hasFlow: 0
  }
  showEntryDialog.value = true
}

// 节点操作
const handleNodeAction = async ({ command, data }) => {
  console.log('节点操作:', command, data)

  switch (command) {
    case 'add':
      // 添加子项
      handleAddEntry(data)
      break
    case 'edit':
      // 编辑条目
      handleEditEntry(data)
      break
    case 'delete':
      // 删除条目
      await handleDeleteEntry(data)
      break
  }
}

// 编辑条目
const handleEditEntry = (node) => {
  entryDialogTitle.value = '编辑条目'
  entryForm.value = {
    id: node.id,
    name: node.name,
    type: node.type,
    parentId: node.parentId,
    appId: node.appId,
    icon: node.icon || '',
    color: node.color || '',
    sort: node.sort || 0,
    hasFlow: node.hasFlow || 0
  }
  showEntryDialog.value = true
}

// 删除条目
const handleDeleteEntry = async (node) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除"${node.name}"吗？删除后无法恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await wbAppEntryApi.deleteAppEntry(node.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await loadEntryTree()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除条目失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交条目表单
const handleEntrySubmit = async (formData) => {
  entrySubmitLoading.value = true
  try {
    let response
    if (formData.id) {
      // 更新
      response = await wbAppEntryApi.updateAppEntry(formData)
    } else {
      // 添加
      response = await wbAppEntryApi.addAppEntry(formData)
    }

    if (response.code === 200) {
      ElMessage.success(formData.id ? '更新成功' : '添加成功')
      showEntryDialog.value = false
      await loadEntryTree()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('提交条目失败:', error)
    ElMessage.error('操作失败')
  } finally {
    entrySubmitLoading.value = false
  }
}

// 关闭条目对话框
const handleEntryDialogClose = () => {
  showEntryDialog.value = false
  entryForm.value = {
    id: null,
    name: '',
    type: 'group',
    parentId: null,
    appId: appId.value,
    icon: '',
    color: '',
    sort: 0,
    hasFlow: 0
  }
}

// 节点拖拽
const handleNodeDrop = (params) => {
  console.log('节点拖拽:', params)
}

// 编辑表单
const handleEditForm = () => {
  if (currentFormInfo.value) {
    designerFormId.value = currentFormInfo.value.id
    showFormDesigner.value = true
  }
}

// 表单设计器保存后的回调
const handleFormDesignerSaved = () => {
  if (currentNode.value && currentNode.value.type === 'form') {
    loadFormInfo(currentNode.value.id)
  }
}

// 表单设计器关闭后的回调
const handleFormDesignerClosed = () => {
  designerFormId.value = null
}

// 查看表单数据
const handleViewFormData = async () => {
  if (currentFormInfo.value) {
    showFormDataDrawer.value = true
    await loadFormDataList()
  }
}

// 表单设置
const handleFormSettings = () => {
  if (currentFormInfo.value) {
    ElMessage.info('打开表单设置')
  }
}

// 编辑表格
const handleEditGrid = () => {
  if (currentGridInfo.value) {
    designerGridId.value = currentGridInfo.value.id
    showGridDesigner.value = true
  }
}

// 表格设计器保存后的回调
const handleGridDesignerSaved = () => {
  if (currentNode.value && currentNode.value.type === 'grid') {
    loadGridInfo(currentNode.value.id)
  }
}

// 表格设计器关闭后的回调
const handleGridDesignerClosed = () => {
  designerGridId.value = null
}

// 保存表单数据
const handleSaveFormData = async (formData) => {
  saveFormDataLoading.value = true
  try {
    const response = await formApi.createFormData({
      formId: currentFormInfo.value.id,
      data: formData
    })

    if (response.code === 200) {
      ElMessage.success('保存成功')
      if (formPreviewRef.value) {
        formPreviewRef.value.resetForm()
      }
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存表单数据失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saveFormDataLoading.value = false
  }
}

// 重置表单数据
const handleResetFormData = () => {
  // 由 FormPreviewSection 组件内部处理
}

// 加载表单数据列表
const loadFormDataList = async () => {
  if (!currentFormInfo.value) return

  formDataLoading.value = true
  try {
    const response = await formApi.getFormDataPage({
      formId: currentFormInfo.value.id,
      criteria: {},
      pageNo: formDataPagination.value.current,
      pageSize: formDataPagination.value.size
    })

    if (response.code === 200 && response.data) {
      formDataList.value = response.data.records || []
      formDataPagination.value.total = Number(response.data.total) || 0
    } else {
      ElMessage.error(response.message || '加载数据失败')
      formDataList.value = []
      formDataPagination.value.total = 0
    }
  } catch (error) {
    console.error('加载表单数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    formDataLoading.value = false
  }
}

// 关闭表单数据抽屉
const handleCloseFormDataDrawer = () => {
  showFormDataDrawer.value = false
  formDataList.value = []
  formDataPagination.value = {
    current: 1,
    size: 10,
    total: 0
  }
}

// 查看表单数据行
const handleViewFormDataRow = (row) => {
  console.log('查看数据:', row)
  ElMessage.info('查看功能开发中')
}

// 编辑表单数据行
const handleEditFormDataRow = (row) => {
  console.log('编辑数据:', row)
  ElMessage.info('编辑功能开发中')
}

// 删除表单数据行
const handleDeleteFormDataRow = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条数据吗？', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await formApi.deleteFormData({
      formId: currentFormInfo.value.id,
      id: row.id
    })

    if (response.code === 200) {
      ElMessage.success('删除成功')
      await loadFormDataList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除数据失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 加载表单信息
const loadFormInfo = async (entryId) => {
  formInfoLoading.value = true
  try {
    const response = await formApi.getFormByEntryId(entryId)
    if (response.code === 200 && response.data) {
      currentFormInfo.value = response.data
    } else {
      currentFormInfo.value = null
      ElMessage.warning('未找到关联的表单信息')
    }
  } catch (error) {
    console.error('加载表单信息失败:', error)
    currentFormInfo.value = null
    ElMessage.error('加载表单信息失败')
  } finally {
    formInfoLoading.value = false
  }
}

// 加载表格信息
const loadGridInfo = async (entryId) => {
  formInfoLoading.value = true
  try {
    const response = await gridApi.getGridByEntryId(entryId)
    if (response.code === 200 && response.data) {
      currentGridInfo.value = response.data
      // 加载表格数据
      await loadGridPreviewData()
    } else {
      currentGridInfo.value = null
      ElMessage.warning('未找到关联的表格信息')
    }
  } catch (error) {
    console.error('加载表格信息失败:', error)
    currentGridInfo.value = null
    ElMessage.error('加载表格信息失败')
  } finally {
    formInfoLoading.value = false
  }
}

// 加载表格预览数据
const loadGridPreviewData = async () => {
  console.log('开始加载表格预览数据')
  console.log('当前表格配置:', currentGridInfo.value)

  // 检查是否有表格配置
  if (!currentGridInfo.value || !currentGridInfo.value.gridConfig) {
    console.log('表格配置不完整，跳过数据加载')
    gridPreviewData.value = []
    return
  }

  // 解析表格配置
  let gridConfig
  try {
    gridConfig = typeof currentGridInfo.value.gridConfig === 'string'
      ? JSON.parse(currentGridInfo.value.gridConfig)
      : currentGridInfo.value.gridConfig
  } catch (error) {
    console.error('解析表格配置失败:', error)
    gridPreviewData.value = []
    return
  }

  // 检查是否有表单ID和字段配置
  if (!gridConfig.selectedFormId || !gridConfig.fields || gridConfig.fields.length === 0) {
    console.log('表格配置不完整，跳过数据加载')
    gridPreviewData.value = []
    return
  }

  gridDataLoading.value = true
  try {
    // selectedFormId 实际上是 entryId，需要先获取对应的 formId
    const selectedFormEntryId = gridConfig.selectedFormId
    console.log('选中的表单 entryId:', selectedFormEntryId)

    // 根据 entryId 获取表单信息
    const formResponse = await formApi.getFormByEntryId(selectedFormEntryId)
    if (formResponse.code !== 200 || !formResponse.data) {
      ElMessage.error('未找到对应的表单')
      gridPreviewData.value = []
      return
    }

    const formId = formResponse.data.id
    console.log('获取到的 formId:', formId)

    // 构建查询条件
    const criteria = {}
    if (gridConfig.filters && gridConfig.filters.length > 0) {
      gridConfig.filters.forEach(filter => {
        if (filter.key && filter.value) {
          criteria[filter.key] = filter.value
        }
      })
    }

    // 使用 formId 查询数据
    const response = await formApi.getFormDataPage({
      formId: formId,
      criteria: criteria,
      pageNo: gridPagination.value.current,
      pageSize: gridPagination.value.size
    })

    if (response.code === 200 && response.data) {
      gridPreviewData.value = response.data.records || []
      gridPagination.value.total = Number(response.data.total) || 0
      console.log('表格数据加载成功，共 ' + gridPagination.value.total + ' 条记录')
    } else {
      ElMessage.error(response.message || '加载表格数据失败')
      gridPreviewData.value = []
      gridPagination.value.total = 0
    }
  } catch (error) {
    console.error('加载表格数据失败:', error)
    ElMessage.error('加载表格数据失败')
    gridPreviewData.value = []
  } finally {
    gridDataLoading.value = false
  }
}

// 查看表格行
const handleViewGridRow = (row) => {
  console.log('查看行:', row)
  ElMessage.info('查看功能开发中')
}

// 编辑表格行
const handleEditGridRow = (row) => {
  console.log('编辑行:', row)
  ElMessage.info('编辑功能开发中')
}

// 删除表格行
const handleDeleteGridRow = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条数据吗？', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    // TODO: 调用删除API
    ElMessage.success('删除成功')
    await loadGridPreviewData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 加载动作列表
const loadActionList = async () => {
  actionLoading.value = true
  try {
    const response = await actionEngineApi.getActionList({
      appId: appId.value,
      current: 1,
      size: 100
    })
    if (response.code === 200) {
      actionList.value = response.data?.records || []
    } else {
      ElMessage.error(response.message || '加载动作列表失败')
    }
  } catch (error) {
    console.error('加载动作列表失败:', error)
    ElMessage.error('加载动作列表失败')
  } finally {
    actionLoading.value = false
  }
}

// 添加动作
const handleAddAction = () => {
  ElMessageBox.prompt('请输入动作名称', '创建动作', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /\S+/,
    inputErrorMessage: '动作名称不能为空'
  }).then(async ({ value }) => {
    try {
      const response = await actionEngineApi.createAction({
        appId: appId.value,
        actionName: value,
        actionType: '',
        actionDescription: '',
        actionConfig: '{}',
        status: 1
      })

      if (response.code === 200) {
        ElMessage.success('创建成功')
        await loadActionList()
      } else {
        ElMessage.error(response.message || '创建失败')
      }
    } catch (error) {
      console.error('创建动作失败:', error)
      ElMessage.error('创建失败')
    }
  }).catch(() => {
    // 取消操作
  })
}

// 编辑动作
const handleEditAction = (action) => {
  console.log('编辑动作:', action)
}

// 删除动作
const handleDeleteAction = async (action) => {
  try {
    await ElMessageBox.confirm('确定要删除这个动作吗？', '确认删除', {
      type: 'warning'
    })

    const response = await actionEngineApi.deleteAction(action.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await loadActionList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除动作失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 复制动作
const handleCopyAction = async (action) => {
  try {
    const response = await actionEngineApi.createAction({
      appId: appId.value,
      actionName: action.actionName + ' - 副本',
      actionType: action.actionType,
      actionDescription: action.actionDescription,
      actionConfig: action.actionConfig,
      status: action.status
    })

    if (response.code === 200) {
      ElMessage.success('复制成功')
      await loadActionList()
    } else {
      ElMessage.error(response.message || '复制失败')
    }
  } catch (error) {
    console.error('复制动作失败:', error)
    ElMessage.error('复制失败')
  }
}

// 保存动作
const handleSaveAction = async (action) => {
  try {
    const response = await actionEngineApi.updateAction(action)
    if (response.code === 200) {
      ElMessage.success('保存成功')
      await loadActionList()
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存动作失败:', error)
    ElMessage.error('保存失败')
  }
}

// 测试动作
const handleTestAction = (action) => {
  ElMessage.info('测试动作功能开发中')
}

// 处理任务中心菜单点击
const handleTaskMenuClick = (item) => {
  console.log('任务中心菜单点击:', item)
  // 清除当前选中的节点
  currentNode.value = null
  currentFormInfo.value = null
  currentGridInfo.value = null
  // 设置任务中心视图
  activeTaskView.value = item.key
}

// 用户下拉菜单
const appearanceDrawerVisible = ref(false)

const handleDropdownCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/home/system/settings?tab=personal')
      break
    case 'appearance':
      appearanceDrawerVisible.value = true
      break
    case 'settings':
      router.push('/home/system/settings?tab=general')
      break
    case 'logout':
      userStore.logout()
      router.push('/login')
      break
  }
}

onMounted(() => {
  loadEntryTree()
})
</script>

<style lang="scss" scoped>
.app-config-layout {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: var(--bg-primary);
}

.top-header {
  height: 56px;
  background: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: var(--shadow-md);
  z-index: 100;
  flex-shrink: 0;

  .header-left {
    display: flex;
    align-items: center;
    flex: 1;

    .logo {
      display: flex;
      align-items: center;
      gap: 12px;
      cursor: pointer;
      transition: opacity 0.2s;

      &:hover {
        opacity: 0.9;
      }

      .logo-img {
        height: 32px;
        width: auto;
      }

      .project-name {
        font-size: 18px;
        font-weight: 600;
        color: #ffffff;
        white-space: nowrap;
      }
    }
  }

  .header-center {
    flex: 2;
    display: flex;
    justify-content: center;
    align-items: center;

    .icon-nav-bar-horizontal {
      display: flex;
      align-items: center;
      gap: 8px;
      background: rgba(255, 255, 255, 0.1);
      padding: 4px;
      border-radius: 8px;

      .nav-item {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 8px 16px;
        border-radius: 6px;
        cursor: pointer;
        transition: all 0.2s;
        color: rgba(255, 255, 255, 0.85);
        font-size: 14px;
        white-space: nowrap;

        &:hover {
          background: rgba(255, 255, 255, 0.15);
          color: #ffffff;
        }

        &.is-active {
          background: rgba(255, 255, 255, 0.25);
          color: #ffffff;
          font-weight: 500;
        }

        .el-icon {
          font-size: 18px;
        }

        .nav-label {
          font-size: 14px;
        }
      }
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 12px;
    flex: 1;
    justify-content: flex-end;

    .user-dropdown-wrapper {
      .user-info {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 4px 12px;
        border-radius: 4px;
        cursor: pointer;
        transition: all 0.2s;

        &:hover {
          background: rgba(255, 255, 255, 0.1);
        }

        .user-avatar {
          width: 28px;
          height: 28px;
          border-radius: 50%;
          background: rgba(255, 255, 255, 0.2);
          display: flex;
          align-items: center;
          justify-content: center;
          color: #ffffff;
        }

        .user-name {
          color: #ffffff;
          font-size: 14px;
          font-weight: 500;
        }

        .dropdown-icon {
          color: rgba(255, 255, 255, 0.85);
          font-size: 12px;
        }
      }
    }
  }
}

.config-container {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.config-content {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}

.workflow-view {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-primary);
}
</style>
