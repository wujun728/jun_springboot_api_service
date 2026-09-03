<template>
  <div class="dataset-manage-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="page-title-section">
          <h1 class="page-title">数据集管理</h1>
        </div>
        <div class="data-source-list">
          <el-menu
              mode="horizontal"
              v-model="activeDataSource"
              class="data-source-menu"
              @select="handleDataSourceSelect"
              :default-active="activeDataSource"
          >
            <el-menu-item
                v-for="ds in dataSources"
                :key="ds.id"
                :index="ds.id"
                :class="{ 'active-data-source': activeDataSource === ds.id }"
            >
              <span class="data-source-name">{{ ds.schemaName }}</span>
              <el-tag size="small" effect="plain" class="data-source-type">{{ ds.dbType.toUpperCase() }}</el-tag>
            </el-menu-item>
          </el-menu>
        </div>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleCreateDataset" :disabled="!selectedTable">
          <el-icon><Plus /></el-icon>
          创建数据集
        </el-button>
        <el-button @click="fetchDatasets">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧数据表面板 -->
      <div class="table-panel">
        <div class="panel-header">
          <h3 class="panel-title">数据表</h3>
          <span class="table-count">({{ tables.length }})</span>
        </div>
        <div class="table-list">
          <el-scrollbar class="table-scroll" ref="tableScrollbar">
            <div
              v-for="table in tables"
              :key="table.tableName"
              class="table-item"
              :class="{ 'table-item-active': selectedTable && selectedTable.tableName === table.tableName }"
              @click="selectTable(table)"
            >
              <div class="table-item-header">
                <el-icon class="table-icon"><Grid /></el-icon>
                <span class="table-name">{{ table.tableName }}</span>
              </div>
              <div class="table-item-desc">{{ table.tableComment || '暂无描述' }}</div>
            </div>
          </el-scrollbar>
          <div class="table-pager">
            <el-button
              v-if="tables.length < tablePage.total"
              :loading="loadingTables"
              type="primary"
              size="small"
              class="load-more-btn"
              @click="loadNextTablePage"
            >
              加载更多
            </el-button>
            <div v-else-if="tablePage.total > 0" class="load-complete">已加载全部</div>
          </div>
        </div>
      </div>

      <!-- 右侧数据集列表 -->
      <div class="dataset-panel">
        <div class="panel-header">
          <h3 class="panel-title">数据集列表</h3>
          <div class="panel-actions">
            <el-button type="primary" size="small" @click="handleCreateDataset" :disabled="!selectedTable">
              <el-icon><Plus /></el-icon>
              创建数据集
            </el-button>
            <el-button size="small" @click="fetchDatasets">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
        <div class="dataset-content">
          <el-table :data="datasets" class="dataset-table" v-loading="loadingDatasets">
            <el-table-column prop="name" label="数据集名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
            <el-table-column prop="sourceTable" label="来源表" min-width="120" show-overflow-tooltip />
            <el-table-column prop="createTime" label="创建时间" min-width="150" show-overflow-tooltip>
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="160" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="handleEditDataset(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDeleteDataset(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-wrapper">
            <el-pagination
              layout="total, prev, pager, next, jumper"
              :total="Number(datasetPage.total)"
              v-model:current-page="datasetPage.pageNum"
              :page-size="datasetPage.pageSize"
              background
              @current-change="handleDatasetPageChange"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 创建/编辑数据集对话框 -->
    <el-dialog
        :title="datasetDialogTitle"
        v-model="datasetDialogVisible"
        width="420px"
        center
        :close-on-click-modal="false"
        class="dataset-dialog"
    >
      <el-form :model="datasetForm" label-width="80px" class="ds-dialog-form">
        <el-form-item label="名称" required>
          <el-input v-model="datasetForm.name" placeholder="请输入数据集名称" maxlength="32" show-word-limit />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
              v-model="datasetForm.description"
              type="textarea"
              :rows="3"
              placeholder="请输入数据集描述"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="来源表" v-if="selectedTable">
          <el-tag>{{ selectedTable.tableName }}</el-tag>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="ds-dialog-footer">
          <el-button @click="datasetDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitDataset">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Plus, Grid } from '@element-plus/icons-vue'
import { dataSourceApi, dataTableApi, datasetApi } from '@/api/index.js'

// 数据源列表相关
const dataSources = ref([])
const activeDataSource = ref('')
const loadingDataSources = ref(false)

// 表相关
const tables = ref([])
const selectedTable = ref(null)
const tablePage = reactive({
  pageNum: 1,
  pageSize: 20,
  total: 0
})
const loadingTables = ref(false)
const tableScrollbar = ref(null)

// 数据集相关
const datasets = ref([])
const datasetPage = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})
const loadingDatasets = ref(false)

// 表单相关
const datasetDialogVisible = ref(false)
const datasetDialogTitle = ref('创建数据集')
const datasetForm = reactive({
  id: null,
  name: '',
  description: '',
  sourceTable: '',
  dataSourceId: ''
})
const isEditDataset = ref(false)

// 获取所有数据源
const fetchAllDataSources = async () => {
  loadingDataSources.value = true
  try {
    const response = await dataSourceApi.getAllDataSources();
    if (response.code === 200) {
      dataSources.value = response.data;
      if (response.data.length > 0) {
        activeDataSource.value = response.data[0].id
        tablePage.pageNum = 1
        await fetchTables(false)
      }
    }
  } catch (error) {
    ElMessage.error('获取数据源列表失败: ' + error.message)
  } finally {
    loadingDataSources.value = false
  }
}

// 处理数据源选择
const handleDataSourceSelect = async (index) => {
  activeDataSource.value = index
  tablePage.pageNum = 1
  await fetchTables(false)
}

// 获取表列表，支持追加
const fetchTables = async (append = false) => {
  const dataSource = dataSources.value.find(ds => ds.id === activeDataSource.value)
  if (!dataSource) return
  const config = {
    dbType: dataSource.dbType,
    url: dataSource.url,
    username: dataSource.username,
    password: dataSource.password,
    schema: dataSource.schemaName,
    pageNum: tablePage.pageNum,
    pageSize: tablePage.pageSize
  }
  loadingTables.value = true
  try {
    const response = await dataTableApi.getTablesAndComment(config)
    let newTables = []
    if (response.code === 200) {
      if (Array.isArray(response.data)) {
        newTables = response.data
        tablePage.total = Number(response.data.length) || 0
      } else if (response.data && Array.isArray(response.data.data)) {
        newTables = response.data.data
        // 兼容常见分页结构
        tablePage.total = Number(response.data.total || response.data.totalRecords || response.data.recordsTotal || response.data.data.length) || 0
      } else {
        newTables = []
        tablePage.total = 0
      }
    } else {
      newTables = []
      tablePage.total = 0
    }
    if (append) {
      tables.value = tables.value.concat(newTables)
    } else {
      tables.value = newTables
    }
    if (!append) selectedTable.value = null
  } catch (error) {
    ElMessage.error(`获取表列表失败: ${error.message}`)
  } finally {
    loadingTables.value = false
  }
}

// 获取数据集列表
const fetchDatasets = async () => {
  loadingDatasets.value = true
  try {
    const response = await datasetApi.getDatasetList({
      pageNum: datasetPage.pageNum,
      pageSize: datasetPage.pageSize
    })
    // 兼容后端分页结构
    if (response.code === 200 && response.data && Array.isArray(response.data.records)) {
      datasets.value = response.data.records
      datasetPage.total = Number(response.data.total) || 0
    } else {
      datasets.value = []
      datasetPage.total = 0
    }
  } catch (error) {
    ElMessage.error('获取数据集列表失败: ' + error.message)
  } finally {
    loadingDatasets.value = false
  }
}

// 数据集分页变化
const handleDatasetPageChange = (page) => {
  datasetPage.pageNum = page
  fetchDatasets()
}

// 创建数据集
const handleCreateDataset = () => {
  if (!selectedTable.value) {
    ElMessage.warning('请先选择一张表')
    return
  }
  datasetDialogTitle.value = '创建数据集'
  isEditDataset.value = false
  datasetForm.id = null
  datasetForm.name = ''
  datasetForm.description = ''
  datasetForm.sourceTable = selectedTable.value.tableName
  datasetForm.dataSourceId = activeDataSource.value
  datasetDialogVisible.value = true
}

// 编辑数据集
const handleEditDataset = (dataset) => {
  datasetDialogTitle.value = '编辑数据集'
  isEditDataset.value = true
  Object.assign(datasetForm, dataset)
  datasetDialogVisible.value = true
}

// 删除数据集
const handleDeleteDataset = async (dataset) => {
  try {
    await ElMessageBox.confirm(`确定要删除数据集 "${dataset.name}" 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await datasetApi.deleteDataset(dataset.id)
    ElMessage.success('删除成功')
    fetchDatasets()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败: ' + error.message)
    }
  }
}

// 提交数据集表单
const submitDataset = async () => {
  if (!datasetForm.name) {
    ElMessage.warning('请输入数据集名称')
    return
  }
  try {
    if (isEditDataset.value) {
      await datasetApi.updateDataset(datasetForm)
      ElMessage.success('更新成功')
    } else {
      await datasetApi.createDataset({
        ...datasetForm,
        tableName: selectedTable.value.tableName,
        dataSourceId: activeDataSource.value
      })
      ElMessage.success('创建成功')
    }
    datasetDialogVisible.value = false
    datasetPage.pageNum = 1
    fetchDatasets()
  } catch (error) {
    ElMessage.error(`操作失败: ${error.message}`)
  }
}

// 新增selectTable方法，替换handleTableSelect
const selectTable = (table) => {
  selectedTable.value = table
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 移除滚动事件，新增loadNextTablePage方法
const loadNextTablePage = () => {
  if (tables.value.length < tablePage.total && !loadingTables.value) {
    tablePage.pageNum += 1
    fetchTables(true)
  }
}

onMounted(() => {
  fetchAllDataSources()
  fetchDatasets()
})
</script>

<style scoped>
.dataset-manage-page {
  padding: 24px;
  background: var(--el-bg-color-page);
  min-height: 100vh;
}

/* 页面头部 */

.header-content {
  flex: 1;
}

.page-title-section {
  margin-bottom: 16px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* 数据源选择区 */
.data-source-section {
  margin-bottom: 24px;
  padding: 20px;
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0;
}

.data-source-list {
  width: 100%;
}

.data-source-menu {
  border: none;
  background: transparent;
}

.data-source-menu :deep(.el-menu-item) {
  border-radius: 8px;
  margin-right: 12px;
  transition: all 0.3s ease;
}

.data-source-menu :deep(.el-menu-item:hover) {
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}

.data-source-menu :deep(.el-menu-item.is-active) {
  background: var(--el-color-primary-light-8);
  color: var(--el-color-primary);
}

.data-source-name {
  font-weight: 500;
  margin-right: 8px;
}

.data-source-type {
  font-size: 11px;
}

/* 主要内容区域 */
.main-content {
  display: flex;
  gap: 24px;
  height: calc(100vh - 280px);
}

/* 左侧数据表面板 */
.table-panel {
  flex: 0 0 320px;
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
  display: flex;
  flex-direction: column;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 20px 16px 20px;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0;
}

.table-count {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  margin-left: 4px;
}

.panel-actions {
  display: flex;
  gap: 8px;
}

.table-list {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 16px;
}

.table-scroll {
  flex: 1;
  margin-bottom: 16px;
}

.table-item {
  background: var(--el-bg-color-page);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.table-item:hover {
  border-color: var(--el-color-primary);
  box-shadow: var(--el-box-shadow-light);
  transform: translateY(-1px);
}

.table-item-active {
  background: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary);
  box-shadow: var(--el-box-shadow-light);
}

.table-item-header {
  display: flex;
  align-items: center;
  margin-bottom: 6px;
}

.table-icon {
  color: var(--el-color-primary);
  margin-right: 8px;
  font-size: 16px;
}

.table-name {
  font-weight: 500;
  color: var(--el-text-color-primary);
  font-size: 14px;
}

.table-item-desc {
  color: var(--el-text-color-secondary);
  font-size: 12px;
  line-height: 1.4;
  margin-left: 24px;
}

.table-pager {
  margin-top: auto;
  text-align: center;
}

.load-more-btn {
  width: 100%;
}

.load-complete {
  color: var(--el-text-color-secondary);
  font-size: 12px;
  padding: 8px;
}

/* 右侧数据集面板 */
.dataset-panel {
  flex: 1;
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
  display: flex;
  flex-direction: column;
}

.dataset-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.dataset-table {
  flex: 1;
}

.dataset-table :deep(.el-table) {
  border: none;
  background: transparent;
}

.dataset-table :deep(.el-table__header) {
  background: transparent;
}

.dataset-table :deep(.el-table__header .el-table__cell) {
  background: transparent;
  border: none;
  color: var(--el-text-color-secondary);
  font-weight: 500;
  padding: 16px 0;
}

.dataset-table :deep(.el-table__row) {
  background: transparent;
  border: none;
}

.dataset-table :deep(.el-table__row:hover) {
  background: var(--bg-tertiary);
}

.dataset-table :deep(.el-table__cell) {
  border: none;
  padding: 16px 0;
  background: transparent;
}

.dataset-table :deep(.el-table__inner-wrapper) {
  border: none;
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

/* 创建/编辑数据集对话框 */
.dataset-dialog :deep(.el-dialog__header) {
  background: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.dataset-dialog :deep(.el-dialog__title) {
  color: var(--el-text-color-primary);
  font-weight: 600;
}

.dataset-dialog :deep(.el-dialog__body) {
  padding: 20px 20px 10px 20px;
}

.ds-dialog-form :deep(.el-form-item__label) {
  color: var(--el-text-color-primary);
  font-weight: 500;
}

.ds-dialog-form :deep(.el-form-item__content) {
  color: var(--el-text-color-regular);
}

.ds-dialog-form :deep(.el-input__inner) {
  border-color: var(--el-border-color-light);
  background-color: var(--el-fill-color-light);
}

.ds-dialog-form :deep(.el-input__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

.ds-dialog-form :deep(.el-textarea__inner) {
  border-color: var(--el-border-color-light);
  background-color: var(--el-fill-color-light);
}

.ds-dialog-form :deep(.el-textarea__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

.ds-dialog-form :deep(.el-tag) {
  background-color: var(--el-fill-color-light);
  border-color: var(--el-border-color-light);
  color: var(--el-text-color-regular);
}

.ds-dialog-footer {
  padding: 10px 20px 20px 20px;
  border-top: 1px solid var(--el-border-color-lighter);
  text-align: right;
}

.ds-dialog-footer :deep(.el-button) {
  border-radius: 8px;
  padding: 8px 16px;
  font-size: 14px;
}

.ds-dialog-footer :deep(.el-button--primary) {
  background-color: var(--el-color-primary);
  border-color: var(--el-color-primary);
}

.ds-dialog-footer :deep(.el-button--primary:hover) {
  background-color: var(--el-color-primary-dark-2);
  border-color: var(--el-color-primary-dark-2);
}

.ds-dialog-footer :deep(.el-button--primary:focus) {
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

.ds-dialog-footer :deep(.el-button--danger) {
  background-color: var(--el-color-danger);
  border-color: var(--el-color-danger);
}

.ds-dialog-footer :deep(.el-button--danger:hover) {
  background-color: var(--el-color-danger-light-2);
  border-color: var(--el-color-danger-light-2);
}

.ds-dialog-footer :deep(.el-button--danger:focus) {
  box-shadow: 0 0 0 2px var(--el-color-danger-light-8);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }

  .table-panel {
    flex: none;
    height: 300px;
  }
}
</style>
