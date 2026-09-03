<template>
  <div class="data-source-export-container">
    <div class="export-card">
      <div class="data-source-layout">
        <!-- 左侧数据源列表 -->
        <div class="data-source-list">
          <div class="list-header">
            <span>数据源列表</span>
            <el-button type="primary" size="small" @click="fetchAllDataSources" :icon="Refresh" :loading="loadingDataSources">
              刷新
            </el-button>
          </div>
          <div class="list-content">
            <el-scrollbar>
              <el-menu
                  v-model="activeDataSource"
                  class="data-source-menu"
                  @select="handleDataSourceSelect"
              >
                <el-menu-item
                    v-for="ds in dataSources"
                    :key="ds.id"
                    :index="ds.id"
                    :class="{ 'active-ds': activeDataSource === ds.id }"
                >
                  <div class="ds-item">
                    <span class="ds-name">{{ ds.schemaName }}</span>
                    <span class="ds-type">{{ ds.dbType.toUpperCase() }}</span>
                  </div>
                </el-menu-item>
              </el-menu>
            </el-scrollbar>
          </div>
        </div>

        <!-- 右侧数据源详情和表操作 -->
        <div class="data-source-detail">
          <el-form :model="formInfo" label-width="120px" label-position="left">
            <el-form-item label="数据库类型">
              <el-select v-model="formInfo.dbType" placeholder="请选择数据库类型" style="width: 100%" disabled>
                <el-option label="MySQL" value="mysql"></el-option>
                <el-option label="达梦" value="dm"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="连接URL">
              <el-input
                  v-model="formInfo.url"
                  placeholder="jdbc:mysql://localhost:3306/db"
                  clearable
                  disabled
              >
              </el-input>
            </el-form-item>

            <el-form-item label="用户名">
              <el-input v-model="formInfo.username" clearable disabled></el-input>
            </el-form-item>

            <el-form-item label="密码">
              <el-input v-model="formInfo.password" type="password" clearable disabled></el-input>
            </el-form-item>

            <el-form-item label="Schema">
              <el-input v-model="formInfo.schema" clearable disabled></el-input>
            </el-form-item>

            <el-form-item>
              <el-checkbox v-model="formInfo.exportData">导出数据</el-checkbox>
            </el-form-item>

            <el-divider></el-divider>

            <el-form-item label="选择表">
              <div class="table-selection">
                <div class="action-buttons">
                  <el-button
                      type="primary"
                      @click="fetchTables"
                      :loading="loadingTables"
                      :icon="Refresh"
                  >
                    获取表列表
                  </el-button>
                  <el-button
                      type="primary"
                      @click="handlePreview"
                      :disabled="selectedTables.length === 0"
                      :loading="previewing"
                      :icon="View"
                  >
                    预览SQL
                  </el-button>
                  <el-button
                      type="success"
                      @click="handleExport"
                      :disabled="selectedTables.length === 0 || !previewContent"
                      :loading="exporting"
                      :icon="Download"
                  >
                    导出脚本
                  </el-button>
                  <el-select
                      v-model="exportTargetDbType"
                      style="width: 120px; margin-left: 10px;"
                      placeholder="导出为"
                      :disabled="!previewContent"
                  >
                    <el-option label="原格式" value=""></el-option>
                    <el-option label="MySQL格式" value="mysql"></el-option>
                    <el-option label="达梦格式" value="dm"></el-option>
                  </el-select>
                </div>

                <div class="table-content" v-if="tables.length > 0">
                  <div class="table-list-container">
                    <div class="table-actions">
                      <el-button type="primary" @click="selectAllTables" size="small">全选</el-button>
                      <el-button type="primary" @click="clearSelection" size="small">清空</el-button>
                    </div>
                    <el-checkbox-group v-model="selectedTables" class="checkbox-group">
                      <el-checkbox
                          v-for="table in tables"
                          :key="table"
                          :label="table"
                          class="table-checkbox"
                      >
                        {{ table }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>

                  <div class="sql-preview-container">
                    <div class="sql-preview-header">
                      <span>SQL预览</span>
                      <div class="sql-preview-actions">
                        <el-tooltip content="全屏显示" placement="top">
                          <el-button
                              size="small"
                              :icon="FullScreen"
                              @click="toggleFullScreen"
                              :disabled="!previewContent"
                          >
                          </el-button>
                        </el-tooltip>
                        <el-tooltip content="转换SQL语法" placement="top">
                          <el-button
                              size="small"
                              :icon="MagicStick"
                              @click="handleSqlConversion"
                              :disabled="!previewContent"
                              :loading="converting"
                          >
                          </el-button>
                        </el-tooltip>
                        <el-tooltip content="复制SQL" placement="top">
                          <el-button
                              size="small"
                              :icon="DocumentCopy"
                              @click="copyPreviewContent"
                              :disabled="!previewContent"
                          >
                          </el-button>
                        </el-tooltip>
                        <el-tooltip content="清空预览" placement="top">
                          <el-button
                              size="small"
                              :icon="Delete"
                              @click="previewContent = ''"
                              :disabled="!previewContent"
                          >
                          </el-button>
                        </el-tooltip>
                      </div>
                    </div>
                    <div class="sql-content-wrapper">
                      <pre
                          v-if="previewContent"
                          class="sql-content"
                          :class="{ 'sql-converted': isConverted }"
                      >
                        {{ previewContent }}
                      </pre>
                      <div v-else class="sql-placeholder">
                        <el-icon :size="40"><InfoFilled /></el-icon>
                        <p>点击"预览SQL"按钮查看生成的SQL语句</p>
                      </div>
                    </div>
                  </div>
                </div>
                <el-empty v-else description="请先获取表列表" :image-size="100"></el-empty>
              </div>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Refresh,
  View,
  Download,
  DocumentCopy,
  Delete,
  InfoFilled,
  MagicStick,
  FullScreen
} from '@element-plus/icons-vue'

import { dataSourceApi, dataSourceExportApi } from '@/api/index.js'

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

// 表相关
const tables = ref([])
const selectedTables = ref([])
const loadingTables = ref(false)
const exporting = ref(false)
const previewing = ref(false)
const converting = ref(false)
const previewContent = ref('')
const isConverted = ref(false)
const exportTargetDbType = ref('') // 导出目标数据库类型
const isFullScreen = ref(false)

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
  }
}

// 获取表列表
const fetchTables = async () => {
  loadingTables.value = true
  const config = {
    dbType: formInfo.value.dbType,
    url: formInfo.value.url,
    username: formInfo.value.username,
    password: formInfo.value.password,
    schema: formInfo.value.schema
  }

  try {
    const response = await dataSourceExportApi.getTableList(config)
    if (response.code === 200) {
      const tablesData = Array.isArray(response.data) ? response.data : []
      tables.value = tablesData
      selectedTables.value = [...tablesData]
      ElMessage.success(`成功获取 ${tablesData.length} 张表`)
    } else {
      tables.value = []
      selectedTables.value = []
    }
  } catch (error) {
    console.error('请求错误:', error)
    const errorMsg = error.response?.data?.message || error.message || '未知错误'
    ElMessage.error(`获取表列表失败: ${errorMsg}`)
  } finally {
    loadingTables.value = false
  }
}

const handlePreview = async () => {
  isConverted.value = false
  previewing.value = true
  const request = {
    dbType: formInfo.value.dbType,
    dataSourceConfig: {
      dbType: formInfo.value.dbType,
      url: formInfo.value.url,
      username: formInfo.value.username,
      password: formInfo.value.password,
      schema: formInfo.value.schema
    },
    exportData: formInfo.value.exportData,
    selectedTables: selectedTables.value
  }

  try {
    const response = await dataSourceExportApi.previewSql(request)
    if (response.code === 200) {
      previewContent.value = response.data
    } else {
      previewContent.value = ''
    }
  } catch (error) {
    ElMessage.error('预览失败: ' + error.message)
  } finally {
    previewing.value = false
  }
}

const copyPreviewContent = () => {
  if (!previewContent.value) return

  navigator.clipboard.writeText(previewContent.value)
      .then(() => {
        ElMessage.success('SQL已复制到剪贴板')
      })
      .catch((err) => {
        ElMessage.error('复制失败: ' + err)
      })
}

const handleExport = async () => {
  exporting.value = true
  const request = {
    dbType: formInfo.value.dbType,
    targetDbType: exportTargetDbType.value || undefined,
    dataSourceConfig: {
      dbType: formInfo.value.dbType,
      url: formInfo.value.url,
      username: formInfo.value.username,
      password: formInfo.value.password,
      schema: formInfo.value.schema
    },
    exportData: formInfo.value.exportData,
    selectedTables: selectedTables.value
  }

  try {
    const response = await dataSourceExportApi.generateScript(request)
    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', `export_${getFormattedTime()}.sql`)
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败: ' + error.message)
  } finally {
    exporting.value = false
  }
}

const selectAllTables = () => {
  selectedTables.value = [...tables.value]
}

const clearSelection = () => {
  selectedTables.value = []
}

const getFormattedTime = () => {
  const date = new Date()
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')

  return `${year}${month}${day}${hours}${minutes}${seconds}`
}

// mysql转换
const handleSqlConversion = async () => {
  if (!previewContent.value) {
    ElMessage.warning('请先预览SQL再进行转换')
    return
  }

  converting.value = true
  try {
    const response = await dataSourceExportApi.convertSql({
      sourceDbType: formInfo.value.dbType,
      targetDbType: formInfo.value.dbType === 'mysql' ? 'dm' : 'mysql',
      sqlContent: previewContent.value
    })
    if (response.code === 200) {
      previewContent.value = response.data
      isConverted.value = true
      ElMessage.success('SQL语法转换成功')
    } else {
      ElMessage.error('转换失败: ' + response.message)
    }
  } catch (error) {
    ElMessage.error('转换失败: ' + error.message)
  } finally {
    converting.value = false
  }
}

// 切换全屏
const toggleFullScreen = () => {
  const previewContainer = document.querySelector('.sql-preview-container')
  if (!previewContainer) return

  if (!isFullScreen.value) {
    if (previewContainer.requestFullscreen) {
      previewContainer.requestFullscreen()
    } else if (previewContainer.webkitRequestFullscreen) {
      /* Safari */
      previewContainer.webkitRequestFullscreen()
    } else if (previewContainer.msRequestFullscreen) {
      /* IE11 */
      previewContainer.msRequestFullscreen()
    }
  } else {
    if (document.exitFullscreen) {
      document.exitFullscreen()
    } else if (document.webkitExitFullscreen) {
      /* Safari */
      document.webkitExitFullscreen()
    } else if (document.msExitFullscreen) {
      /* IE11 */
      document.msExitFullscreen()
    }
  }

  isFullScreen.value = !isFullScreen.value
}

// 监听全屏变化
const handleFullscreenChange = () => {
  isFullScreen.value = !!document.fullscreenElement
}

onMounted(() => {
  document.addEventListener('fullscreenchange', handleFullscreenChange)
  document.addEventListener('webkitfullscreenchange', handleFullscreenChange)
  document.addEventListener('msfullscreenchange', handleFullscreenChange)

  // 页面加载时获取数据源列表
  fetchAllDataSources()
})
</script>

<style scoped>
.data-source-layout {
  display: flex;
  height: 100%;
}

.data-source-list {
  width: 280px;
  border-right: 1px solid #ebeef5;
  padding-right: 15px;
  display: flex;
  flex-direction: column;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding: 0 10px;
}

.list-header span {
  font-weight: bold;
  font-size: 16px;
}

.list-content {
  flex: 1;
  overflow: hidden;
}

.data-source-menu {
  border-right: none;
}

.ds-item {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

.ds-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.ds-type {
  color: #909399;
  font-size: 12px;
  margin-left: 10px;
}

.active-ds {
  background-color: #ecf5ff;
  color: #409eff;
}

.data-source-detail {
  flex: 1;
  padding-left: 20px;
  overflow: auto;
}

.table-selection {
  width: 100%;
}
.table-actions {
  margin-bottom: 10px;
}

.checkbox-group {
  display: flex;
  flex-wrap: wrap;
}

.table-checkbox {
  width: 250px;
  margin-right: 10px;
  margin-bottom: 10px;
}

.sql-preview-container {
  margin-top: 15px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 500px;
  transition: all 0.3s ease;
}
.sql-preview-container:fullscreen {
  width: 100vw;
  height: 100vh;
  padding: 20px;
  background: white;
  z-index: 9999;
}

.sql-preview-container:-webkit-full-screen {
  width: 100vw;
  height: 100vh;
  padding: 20px;
  background: white;
  z-index: 9999;
}

.sql-preview-container:-ms-fullscreen {
  width: 100vw;
  height: 100vh;
  padding: 20px;
  background: white;
  z-index: 9999;
}

.sql-content-wrapper:fullscreen {
  height: calc(100vh - 60px);
}

.sql-content-wrapper:-webkit-full-screen {
  height: calc(100vh - 60px);
}

.sql-content-wrapper:-ms-fullscreen {
  height: calc(100vh - 60px);
}

.sql-preview-header {
  padding: 10px 15px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f5f7fa;
}

.sql-preview-header span {
  font-weight: bold;
  color: #606266;
}

.sql-preview-actions button {
  margin-left: 5px;
  padding: 2px;
}

.sql-content-wrapper {
  flex: 1;
  overflow: auto;
  position: relative;
  transition: all 0.3s;
}

.sql-content {
  margin: 0;
  padding: 15px;
  white-space: pre-wrap;
  font-family: 'Courier New', Courier, monospace;
  font-size: 13px;
  line-height: 1.5;
  color: #333;
  tab-size: 4;
  background-color: #fafafa;
}

.sql-placeholder {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: #909399;
}

.sql-placeholder p {
  margin: 0;
  font-size: 14px;
}

/* 代码高亮效果 */
.sql-converted {
  border-left: 3px solid #409eff;
  padding-left: 10px;
  background-color: #f0f7ff;
}
</style>
