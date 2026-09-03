<template>
  <div class="data-table-manage-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">数据表管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="addTableOpen">
          <el-icon><Plus /></el-icon>
          新增表
        </el-button>
        <el-button @click="fetchAllDataSources" :loading="loadingDataSources">
          <el-icon><Refresh /></el-icon>
          刷新数据源
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧数据源列表 -->
      <div class="data-source-panel">
        <div class="panel-header">
          <h3 class="panel-title">数据源列表</h3>
          <span class="data-source-count">({{ dataSources.length }})</span>
        </div>
        <div class="data-source-content">
          <el-scrollbar class="data-source-scroll">
            <el-menu
              v-model="activeDataSource"
              class="data-source-menu"
              @select="handleDataSourceSelect"
            >
              <el-menu-item
                v-for="ds in dataSources"
                :key="ds.id"
                :index="ds.id"
                :class="{ 'active-data-source': activeDataSource === ds.id }"
              >
                <div class="data-source-item">
                  <div class="data-source-info">
                    <span class="data-source-name">{{ ds.schemaName }}</span>
                    <el-tag size="small" effect="plain" class="data-source-type">{{ ds.dbType.toUpperCase() }}</el-tag>
                  </div>
                </div>
              </el-menu-item>
            </el-menu>
          </el-scrollbar>
        </div>
      </div>

      <!-- 右侧表格及操作区 -->
      <div class="table-panel">
        <div class="panel-header">
          <h3 class="panel-title">数据表列表</h3>
          <div class="panel-actions">
            <el-button type="primary" size="small" @click="addTableOpen">
              <el-icon><Plus /></el-icon>
              新增表
            </el-button>
          </div>
        </div>
        <div class="table-content">
          <el-table :data="selectedTables" class="data-table" v-loading="loadingTables">
            <el-table-column prop="tableName" label="表名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="tableComment" label="表备注" min-width="200" show-overflow-tooltip />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="editTable(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="deleteTable(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="pagination-wrapper">
            <el-pagination
              layout="total, prev, pager, next, jumper"
              :total="Number(tablePage.total)"
              v-model:current-page="tablePage.pageNum"
              :page-size="tablePage.pageSize"
              background
              @current-change="handlePageChange"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
    <!-- 编辑表弹窗 -->
    <el-dialog
        title="编辑表"
        v-model="dialogVisible"
        width="800px"
        center
    >
      <el-table :data="columnList" >
        <el-table-column
            type="index"
            label="序号"
            width="60"
        />
        <el-table-column
            label="列标识"
            prop="columnName"
        >
          <template #default="{ row }">
            <el-input class="bs-el-input"
                      v-model="row.columnName"
            />
          </template>
        </el-table-column>
        <el-table-column label="列名称">
          <template #default="{ row }">
            <el-input
                class="bs-el-input"
                v-model="row.columnComment"
            />
          </template>
        </el-table-column>
        <el-table-column
            label="列类型"
            prop="columnType"
        >
          <template #default="{ row }">
            <el-select
                popper-class="bs-el-select"
                class="bs-el-select"
                v-model="row.columnType"
            >
              <el-option
                  class=""
                  v-for="item in columnTypeList"
                  :key="item"
                  :label="item"
                  :value="item"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center"
        >
          <template #default="{ row }">
            <el-button
                type="text"
                @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button
          type="primary"
          @click="handleAdd"
      >
        + 增加行
      </el-button>
      <template #footer>
        <div class="dialog-footer">
          <el-button
              class="bs-el-button-default"
              @click="handleClose"
          >
            取消
          </el-button>
          <el-button
              type="primary"
              @click="submitColumn"
          >
            确定
          </el-button>
        </div>
      </template>


    </el-dialog>
    <el-dialog
        title="新增表"
        v-model="addTableVisible"
        width="400px"
        center
    >
      <el-form
          ref="dataForm"
      >
        <el-form-item label="表名称">
          <el-input  v-model="tableForm.tableName" />
        </el-form-item>
        <el-form-item label="表备注">
          <el-input  v-model="tableForm.tableComment" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button
              class="bs-el-button-default"
              @click="tableFormClose"
          >
            取消
          </el-button>
          <el-button
              type="primary"
              @click="submitTable"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
</template>

<script setup>
import {ref, onMounted, reactive} from 'vue'
import { ElMessage } from 'element-plus'
import {
  Plus,
  Refresh,
} from '@element-plus/icons-vue'

import { dataSourceApi, dataTableApi } from '@/api/index.js'

// 数据源列表相关
const dataSources = ref([])
const activeDataSource = ref('')
const loadingDataSources = ref(false)

// 表单数据
const formInfo = ref({
  dbType: '',
  url: '',
  username: '',
  password: '',
  schema: '',
  exportData: true
})

const tablePage = ref({
  pageNum: 1,
  pageSize: 10,
  total : 0
})

// 表相关
const tables = ref([])
const selectedTables = ref([])
const loadingTables = ref(false)
const previewContent = ref('')

// 编辑表弹窗
const dialogVisible = ref(false)
const columnList = ref([])
const columnTypeList = ref([])

// 新增表
const addTableVisible = ref(false)
const tableForm = ref({
  tableName: '',
  tableComment: ''
})


// 获取所有数据源
const fetchAllDataSources = async () => {
  loadingDataSources.value = true
  try {
        const response = await dataSourceApi.getAllDataSources()
    if (response.code === 200) {
      dataSources.value = response.data
      if (response.data.length > 0) {
        // 默认选中第一个数据源
        activeDataSource.value = response.data[0].id
        // 填充表单数据
        updateFormInfo(response.data[0])
      }
    }
  } catch (error) {
    ElMessage.error('获取数据源列表失败: ' + error.message)
  } finally {
    loadingDataSources.value = false
  }
}

// 更新表单信息
const updateFormInfo = (dataSource) => {
  formInfo.value = {
    dbType: dataSource.dbType,
    url: dataSource.url,
    username: dataSource.username,
    password: dataSource.password,
    schema: dataSource.schemaName,
    exportData: true
  }
}

// 处理数据源选择
const handleDataSourceSelect = (index) => {
  const selected = dataSources.value.find(ds => ds.id === index)
  if (selected) {
    updateFormInfo(selected)
    // 清空之前的表和预览
    tables.value = []
    selectedTables.value = []
    previewContent.value = ''
    fetchTables()
    getColumnType()
  }
}

// 分页变化
function handlePageChange(page) {
  tablePage.value.pageNum = page
  fetchTables()
}

// 获取表列表
const fetchTables = async () => {
  loadingTables.value = true
  const config = {
    dbType: formInfo.value.dbType,
    url: formInfo.value.url,
    username: formInfo.value.username,
    password: formInfo.value.password,
    schema: formInfo.value.schema,
    pageNum: tablePage.value.pageNum,
    pageSize: tablePage.value.pageSize
  }

  try {
    const response = await dataTableApi.getTablesAndComment(config)
    if (response.code === 200) {
      const tablesData = Array.isArray(response.data.data) ? response.data.data : []
      tablePage.value.pageNum = response.data.pageNum
      tablePage.value.pageSize = response.data.pageSize
      tablePage.value.total = Number(response.data.totalRecords) || 0
      tables.value = tablesData
      selectedTables.value = [...tablesData]
    } else {
      tables.value = []
      selectedTables.value = []
    }
    // ElMessage.success(`成功获取 ${tablesData.length} 张表`)
  } catch (error) {
    console.error('请求错误:', error)
    const errorMsg = error.response?.data?.message || error.message || '未知错误'
    ElMessage.error(`获取表列表失败: ${errorMsg}`)
  } finally {
    loadingTables.value = false
  }
}

const tableName = ref('')

// 编辑表字段
const editTable = async (row) => {
  tableName.value = row.tableName
  dialogVisible.value = true
  const config = {
    dbType: formInfo.value.dbType,
    url: formInfo.value.url,
    username: formInfo.value.username,
    password: formInfo.value.password,
    schema: formInfo.value.schema,
    tableName: row.tableName
  }
  const res = await dataTableApi.getTableColumnList(config)
  if (res.code === 200) {
    columnList.value = res.data
  } else {
    columnList.value = []
  }
}

// 编辑表时获取字段类型
const getColumnType = async () => {
  const res = await dataTableApi.getColumnType(formInfo.value.dbType)
  if (res.code === 200) {
    columnTypeList.value = res.data
  } else {
    columnTypeList.value = []
  }
}

// 弹窗 新增行按钮
const handleAdd = () => {
  columnList.value.push({ })
}

// 弹窗table中删除按钮
const handleDelete = (row) => {
  // 遍历 columnList 找到对应的行并删除
  const idx = columnList.value.findIndex(item => item === row);
  if (idx !== -1) {
    columnList.value.splice(idx, 1);
  }
}

// 关闭弹窗
const handleClose = () =>{
  dialogVisible.value = false
  columnList.value = []
}

// 字段修改弹窗提交
const submitColumn = async () => {
  // 数据校验
  for (let i = 0; i < columnList.value.length; i++) {
    if (columnList.value[i].columnName == undefined || columnList.value[i].columnName == null || columnList.value[i].columnName == '') {
      ElMessage.warning('表字段第' + (i + 1) + '行，列标识不能为空！')
      return false
    }
    if (columnList.value[i].columnType == undefined || columnList.value[i].columnType == null || columnList.value[i].columnType == '') {
      ElMessage.warning('表字段第' + (i + 1) + '行，列标识不能为空！')
      return false
    }
  }
  const config = {
    dbType: formInfo.value.dbType,
    url: formInfo.value.url,
    username: formInfo.value.username,
    password: formInfo.value.password,
    schema: formInfo.value.schema,
    tableName: tableName.value ,
    fields: columnList.value
  }
  // 提交数据
  const flag = await dataTableApi.updateColumnType(config)
  if (flag.code === 200 && flag.data){
    dialogVisible.value = false
    ElMessage.success('保存成功')
  }else {
    ElMessage.warning('保存失败，请联系管理员')
  }
}

// 新增表
const addTableOpen = () =>{
  if (formInfo.value.url.length<0){
    ElMessage.warning('请先选择数据源')
    return false
  }
  addTableVisible.value = true
}

// 关闭弹窗
const tableFormClose = () => {
  addTableVisible.value = false
  tableForm.value.tableName = ''
  tableForm.value.tableComment = ''
}

// 新增表确认
const submitTable = async () => {

  if (!tableForm.value.tableName.length>0){
    ElMessage.warning('请填写表名称')
  }

  const config = {
    dbType: formInfo.value.dbType,
    url: formInfo.value.url,
    username: formInfo.value.username,
    password: formInfo.value.password,
    schema: formInfo.value.schema,
    tableName: tableForm.value.tableName ,
    tableComment: tableForm.value.tableComment
  }

  const res = await dataTableApi.addTable(config);
  if (res.code === 200 && res.data){
    addTableVisible.value = false
    tableForm.value.tableName = ''
    tableForm.value.tableComment = ''
    ElMessage.success('新增成功')
    await fetchTables()
  }

}

// 删除表
const deleteTable = async (row) => {
  const config = {
    dbType: formInfo.value.dbType,
    url: formInfo.value.url,
    username: formInfo.value.username,
    password: formInfo.value.password,
    schema: formInfo.value.schema,
    tableName: row.tableName
  }
  const res = await dataTableApi.dropTable(config)
  if (res.code === 200 && res.data){
    ElMessage.success('删除成功')
    await fetchTables()
  }
}

onMounted(() => {
  // 页面加载时获取数据源列表
  fetchAllDataSources()
})
</script>

<style scoped>
.data-table-manage-page {
  padding: 24px;
  background: var(--el-bg-color-page);
  min-height: 100vh;
}

/* 页面头部 */

.header-content {
  flex: 1;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* 主要内容区域 */
.main-content {
  display: flex;
  gap: 24px;
  height: calc(100vh - 200px);
}

/* 左侧数据源面板 */
.data-source-panel {
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
  background: transparent;
}

.data-source-count {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  margin-left: 4px;
}

.panel-actions {
  display: flex;
  gap: 8px;
}

.data-source-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 16px;
}

.data-source-scroll {
  flex: 1;
}

.data-source-menu {
  border: none;
  background: transparent;
}

.data-source-menu :deep(.el-menu-item) {
  border-radius: 8px;
  margin-bottom: 8px;
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

.data-source-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.data-source-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.data-source-name {
  font-weight: 500;
  color: var(--el-text-color-primary);
  font-size: 14px;
}

.data-source-type {
  font-size: 11px;
}

/* 右侧表格面板 */
.table-panel {
  flex: 1;
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
  display: flex;
  flex-direction: column;
}

.table-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.data-table {
  flex: 1;
}

.data-table :deep(.el-table) {
  border: none;
  background: transparent;
}

.data-table :deep(.el-table__header) {
  background: transparent;
}

.data-table :deep(.el-table__header .el-table__cell) {
  background: transparent;
  border: none;
  color: var(--text-secondary);
  font-weight: 500;
  padding: 16px 0;
}

.data-table :deep(.el-table__row) {
  background: transparent;
  border: none;
}

.data-table :deep(.el-table__row:hover) {
  background: var(--bg-tertiary);
}

.data-table :deep(.el-table__cell) {
  border: none;
  padding: 16px 0;
  background: transparent;
}

.data-table :deep(.el-table__inner-wrapper) {
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

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }
  
  .data-source-panel {
    flex: none;
    height: 300px;
  }
}
</style>
