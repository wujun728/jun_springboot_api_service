<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElConfigProvider, ElMessage } from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { 
  RefreshLeft, Search, Plus, Edit, Delete, View, Download, 
  Refresh, Setting, More 
} from '@element-plus/icons-vue'

// 定义类型
interface ListField {
  id: string
  name: string
  key: string
  visible: boolean
  sortable: boolean
  filterable: boolean
  width?: number
}

interface FilterRule {
  id: string
  field: string
  operator: 'eq' | 'contains' | 'range' | 'gt' | 'lt'
  value: string
  value2?: string
}

interface PaginationConfig {
  pageSize: number
  showSizeChanger: boolean
  showQuickJumper: boolean
  showTotal: boolean
  position: 'top' | 'bottom' | 'both'
  align: 'left' | 'center' | 'right'
}

interface ButtonConfig {
  id: string
  name: string
  color: string
  style: 'filled' | 'outlined' | 'text'
  size: 'large' | 'default' | 'small'
  icon?: string
  visible: boolean
  disabled: boolean
  action: string
}

interface ListConfig {
  name: string
  apiUrl: string
  showIndex: boolean
  showSelection: boolean
  striped: boolean
  style: 'simple' | 'poster' | 'table'
  rowClickAction: 'none' | 'view' | 'edit' | 'custom'
  customRowClickAction?: string
  fields: ListField[]
  filters: FilterRule[]
  pagination: PaginationConfig
  conditionButtons: ButtonConfig[]
  actionButtons: ButtonConfig[]
  showActionColumn: boolean
}

// 响应式数据
const listConfig = ref<ListConfig>({
  name: '未命名列表',
  apiUrl: '',
  showIndex: true,
  showSelection: true,
  striped: true,
  style: 'table',
  rowClickAction: 'none',
  customRowClickAction: '',
  fields: [],
  filters: [],
  pagination: {
    pageSize: 10,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: true,
    position: 'bottom',
    align: 'right'
  },
  conditionButtons: [],
  actionButtons: [],
  showActionColumn: true
})

const sampleData = ref<any[]>([])
const previewFilters = ref<Record<string, string>>({})
const currentPage = ref(1)

// 从 localStorage 加载配置
onMounted(() => {
  const savedConfig = localStorage.getItem('listPreviewConfig')
  const savedData = localStorage.getItem('listPreviewData')
  const savedFilters = localStorage.getItem('listPreviewFilters')
  
  if (savedConfig) {
    listConfig.value = JSON.parse(savedConfig)
  }
  if (savedData) {
    sampleData.value = JSON.parse(savedData)
  }
  if (savedFilters) {
    previewFilters.value = JSON.parse(savedFilters)
  }
})

// 获取筛选设置中配置的字段信息
const configuredFilterFields = computed(() => {
  return listConfig.value.filters
    .filter(filter => filter.field) // 只取已选择字段的筛选条件
    .map(filter => {
      const field = listConfig.value.fields.find(f => f.key === filter.field)
      return {
        ...filter,
        fieldName: field?.name || filter.field,
        fieldKey: filter.field
      }
    })
})

// 筛选逻辑
const filteredData = computed(() => {
  let data = [...sampleData.value]
  
  // 应用预览区域的筛选条件（根据筛选设置中的字段）
  configuredFilterFields.value.forEach(filter => {
    const filterValue = previewFilters.value[filter.fieldKey]
    if (!filterValue) return
    
    data = data.filter(item => {
      const fieldValue = item[filter.fieldKey as keyof typeof item]
      if (fieldValue === undefined) return false
      
      // 根据筛选设置中的操作符进行筛选
      switch (filter.operator) {
        case 'eq':
          return String(fieldValue) === filterValue
        case 'contains':
          return String(fieldValue).toLowerCase().includes(filterValue.toLowerCase())
        case 'gt':
          return Number(fieldValue) > Number(filterValue)
        case 'lt':
          return Number(fieldValue) < Number(filterValue)
        case 'range':
          const num = Number(fieldValue)
          const min = Number(filterValue)
          const max = Number(previewFilters.value[`${filter.fieldKey}_max`] || filterValue)
          return num >= min && num <= max
        default:
          return true
      }
    })
  })
  
  return data
})

// 分页逻辑
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * listConfig.value.pagination.pageSize
  const end = start + listConfig.value.pagination.pageSize
  return filteredData.value.slice(start, end)
})

const totalItems = computed(() => filteredData.value.length)
const totalPages = computed(() => Math.ceil(totalItems.value / listConfig.value.pagination.pageSize))

// 动态生成分页布局
const paginationLayout = computed(() => {
  const parts: string[] = []
  
  if (listConfig.value.pagination.showTotal) {
    parts.push('total')
  }
  
  if (listConfig.value.pagination.showSizeChanger) {
    parts.push('sizes')
  }
  
  parts.push('prev', 'pager', 'next')
  
  if (listConfig.value.pagination.showQuickJumper) {
    parts.push('jumper')
  }
  
  return parts.join(', ')
})

// 预览筛选操作
const clearPreviewFilters = () => {
  previewFilters.value = {}
}

// 分页操作
const handlePageChange = (page: number) => {
  currentPage.value = page
}

const handlePageSizeChange = (size: number) => {
  listConfig.value.pagination.pageSize = size
  currentPage.value = 1
}

// 生成按钮样式
const getButtonStyle = (button: ButtonConfig) => {
  const baseStyle = {
    borderRadius: '6px',
    fontWeight: '500',
  }

  switch (button.style) {
    case 'filled':
      return {
        ...baseStyle,
        backgroundColor: button.color,
        borderColor: button.color,
        color: '#fff'
      }
    case 'outlined':
      return {
        ...baseStyle,
        backgroundColor: 'transparent',
        borderColor: button.color,
        color: button.color
      }
    case 'text':
      return {
        ...baseStyle,
        backgroundColor: 'transparent',
        borderColor: 'transparent',
        color: button.color
      }
    default:
      return baseStyle
  }
}

// 按钮点击处理
const handleButtonClick = (button: ButtonConfig, row?: any) => {
  let actionName = ''
  switch (button.action) {
    case 'reset':
      clearPreviewFilters()
      actionName = '重置筛选'
      break
    case 'search':
      actionName = '查询数据'
      break
    case 'add':
      actionName = '新增记录'
      break
    case 'edit':
      actionName = `编辑记录${row ? ` - ID: ${row.id}` : ''}`
      break
    case 'delete':
      actionName = `删除记录${row ? ` - ID: ${row.id}` : ''}`
      break
    case 'view':
      actionName = `查看详情${row ? ` - ID: ${row.id}` : ''}`
      break
    case 'export':
      actionName = '导出数据'
      break
    case 'refresh':
      actionName = '刷新列表'
      break
    case 'batch':
      actionName = '批量操作'
      break
    default:
      actionName = button.action || '自定义操作'
  }
  
  ElMessage.info(`执行操作: ${actionName}`)
}

// 行点击事件处理
const handleRowClick = (row: any, column: any, event: Event) => {
  if (listConfig.value.rowClickAction === 'none') return

  let actionName = ''
  switch (listConfig.value.rowClickAction) {
    case 'view':
      actionName = '查看详情'
      break
    case 'edit':
      actionName = '编辑记录'
      break
    case 'custom':
      actionName = listConfig.value.customRowClickAction || '自定义操作'
      break
  }

  ElMessage.info(`执行行点击操作: ${actionName} - ID: ${row.id}`)
}

// 调试信息
const debugInfo = computed(() => {
  return {
    config: listConfig.value,
    totalItems: totalItems.value,
    currentPage: currentPage.value,
    pageSize: listConfig.value.pagination.pageSize,
    filteredCount: filteredData.value.length,
    sampleDataCount: sampleData.value.length,
    filters: configuredFilterFields.value,
    previewFilters: previewFilters.value
  }
})
</script>

<template>
  <el-config-provider :locale="zhCn">
    <div class="list-preview">
      <!-- 头部 -->
      <div class="preview-header">
        <h2>{{ listConfig.name || '列表预览' }}</h2>
        <div class="preview-stats">
          <span>共 {{ totalItems }} 条数据，当前第 {{ currentPage }} 页</span>
        </div>
      </div>

      <!-- 筛选条件区域 -->
      <div v-if="configuredFilterFields.length > 0" class="filter-section">
        <div class="filter-row">
          <div 
            v-for="filter in configuredFilterFields" 
            :key="filter.id"
            class="filter-item"
          >
            <label class="filter-label">{{ filter.fieldName }}:</label>
            <el-input 
              v-model="previewFilters[filter.fieldKey]"
              :placeholder="`请输入${filter.fieldName}`"
              class="filter-input"
              clearable
            />
            <!-- 范围筛选的最大值输入框 -->
            <el-input 
              v-if="filter.operator === 'range'"
              v-model="previewFilters[`${filter.fieldKey}_max`]"
              placeholder="最大值"
              class="filter-input range-input"
              clearable
            />
          </div>
          <div class="filter-actions">
            <el-button
              v-for="button in listConfig.conditionButtons.filter(b => b.visible)"
              :key="button.id"
              :size="button.size"
              :disabled="button.disabled"
              :style="getButtonStyle(button)"
              @click="handleButtonClick(button)"
            >
              <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
              {{ button.name }}
            </el-button>
          </div>
        </div>
      </div>

      <!-- 分页器（顶部） -->
      <div 
        v-if="listConfig.pagination.position === 'top' || listConfig.pagination.position === 'both'"
        :class="['pagination-top', `pagination-${listConfig.pagination.align}`]"
      >
        <el-pagination
          background
          v-model:current-page="currentPage"
          v-model:page-size="listConfig.pagination.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="totalItems"
          :layout="paginationLayout"
          @size-change="handlePageSizeChange"
          @current-change="handlePageChange"
        />
      </div>

      <!-- 列表内容 -->
      <div class="list-content">
        <!-- 简易列表 -->
        <div v-if="listConfig.style === 'simple'" class="simple-list">
          <div 
            v-for="item in paginatedData" 
            :key="item.id" 
            class="simple-item"
          >
            <div class="item-header">
              <h4 class="item-title">{{ item.title }}</h4>
              <div class="item-actions">
                <el-button
                  v-for="button in listConfig.actionButtons.filter(b => b.visible)"
                  :key="button.id"
                  :size="button.size"
                  :disabled="button.disabled"
                  :style="getButtonStyle(button)"
                  class="action-btn"
                  @click="handleButtonClick(button, item)"
                >
                  <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                  {{ button.name }}
                </el-button>
              </div>
            </div>
            <p class="item-desc">{{ item.description }}</p>
            <div class="item-meta">
              <span>创建时间: {{ item.createTime }}</span>
              <span class="item-status" :class="item.status === '启用' ? 'active' : 'inactive'">
                {{ item.status }}
              </span>
            </div>
          </div>
        </div>

        <!-- 海报列表 -->
        <div v-else-if="listConfig.style === 'poster'" class="poster-list">
          <div 
            v-for="item in paginatedData" 
            :key="item.id" 
            class="poster-item"
          >
            <img :src="item.cover" alt="封面" class="poster-cover" />
            <div class="poster-content">
              <div class="poster-header">
                <h4 class="poster-title">{{ item.title }}</h4>
                <div class="poster-actions">
                  <el-button
                    v-for="button in listConfig.actionButtons.filter(b => b.visible)"
                    :key="button.id"
                    :size="button.size"
                    :disabled="button.disabled"
                    :style="getButtonStyle(button)"
                    class="action-btn"
                    @click="handleButtonClick(button, item)"
                  >
                    <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                    {{ button.name }}
                  </el-button>
                </div>
              </div>
              <p class="poster-desc">{{ item.description }}</p>
              <div class="poster-meta">
                <span>创建时间: {{ item.createTime }}</span>
                <span class="poster-status" :class="item.status === '启用' ? 'active' : 'inactive'">
                  {{ item.status }}
                </span>
              </div>
            </div>
          </div>
        </div>

        <!-- 表格列表 -->
        <div v-else-if="listConfig.style === 'table'" class="table-list">
          <el-table 
            :data="paginatedData" 
            :stripe="listConfig.striped"
            style="width: 100%"
            @row-click="handleRowClick"
            :row-style="listConfig.rowClickAction !== 'none' ? 'cursor: pointer' : ''"
          >
            <el-table-column 
              v-if="listConfig.showIndex"
              type="index" 
              label="序号" 
              width="60"
              :index="(index: number) => (currentPage - 1) * listConfig.pagination.pageSize + index + 1"
            />
            <el-table-column 
              v-if="listConfig.showSelection"
              type="selection" 
              width="55"
            />
            <el-table-column 
              v-for="field in listConfig.fields.filter(f => f.visible)"
              :key="field.id"
              :prop="field.key"
              :label="field.name"
              :width="field.width"
              :sortable="field.sortable"
            />
            <el-table-column
              v-if="listConfig.showActionColumn && listConfig.actionButtons.some(b => b.visible)"
              label="操作"
              width="160"
              fixed="right"
              align="center"
            >
              <template #default="{ row }">
                <div class="table-actions">
                  <el-button
                    v-for="button in listConfig.actionButtons.filter(b => b.visible)"
                    :key="button.id"
                    :size="button.size"
                    :disabled="button.disabled"
                    :style="getButtonStyle(button)"
                    class="action-btn"
                    @click="handleButtonClick(button, row)"
                  >
                    <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                    {{ button.name }}
                  </el-button>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <!-- 分页器（底部） -->
      <div 
        v-if="listConfig.pagination.position === 'bottom' || listConfig.pagination.position === 'both'"
        :class="['pagination-bottom', `pagination-${listConfig.pagination.align}`]"
      >
        <el-pagination
          background
          v-model:current-page="currentPage"
          v-model:page-size="listConfig.pagination.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="totalItems"
          :layout="paginationLayout"
          @size-change="handlePageSizeChange"
          @current-change="handlePageChange"
        />
      </div>

      <!-- 调试信息 -->
      <div class="debug-section">
        <h3>调试信息</h3>
        <div class="debug-content">
          <div class="debug-item">
            <label>配置信息:</label>
            <pre>{{ JSON.stringify(debugInfo.config, null, 2) }}</pre>
          </div>
          <div class="debug-item">
            <label>数据统计:</label>
            <pre>{{ JSON.stringify({
              totalItems: debugInfo.totalItems,
              currentPage: debugInfo.currentPage,
              pageSize: debugInfo.pageSize,
              filteredCount: debugInfo.filteredCount,
              sampleDataCount: debugInfo.sampleDataCount
            }, null, 2) }}</pre>
          </div>
          <div class="debug-item">
            <label>筛选条件:</label>
            <pre>{{ JSON.stringify(debugInfo.filters, null, 2) }}</pre>
          </div>
          <div class="debug-item">
            <label>预览筛选值:</label>
            <pre>{{ JSON.stringify(debugInfo.previewFilters, null, 2) }}</pre>
          </div>
        </div>
      </div>
    </div>
  </el-config-provider>
</template>

<style scoped>
.list-preview {
  padding: 20px;
  background: #f5f5f5;
  min-height: 100vh;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.preview-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.preview-stats {
  font-size: 14px;
  color: #666;
}

/* 筛选条件区域 */
.filter-section {
  margin-bottom: 20px;
  background: #fff;
  padding: 16px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  border-bottom: 1px solid #e0e0e0;
}

.filter-row {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-wrap: wrap;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-label {
  font-size: 14px;
  color: #333;
  white-space: nowrap;
  min-width: fit-content;
}

.filter-input {
  width: 180px;
}

.range-input {
  width: 120px;
}

.filter-actions {
  display: flex;
  gap: 8px;
  margin-left: auto;
}

/* 分页器位置控制 */
.pagination-top,
.pagination-bottom {
  margin: 16px 0;
  display: flex;
}

.pagination-left {
  justify-content: flex-start;
}

.pagination-center {
  justify-content: center;
}

.pagination-right {
  justify-content: flex-end;
}

/* 列表内容 */
.list-content {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 20px;
  margin-bottom: 20px;
}

/* 简易列表样式 */
.simple-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.simple-item {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.item-title {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.item-actions {
  display: flex;
  gap: 8px;
}

.item-status {
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
}

.item-status.active {
  background: #e1f5fe;
  color: #0277bd;
}

.item-status.inactive {
  background: #ffebee;
  color: #c62828;
}

.item-desc {
  margin: 0 0 8px 0;
  color: #666;
  font-size: 14px;
}

.item-meta {
  color: #999;
  font-size: 12px;
}

/* 海报列表样式 */
.poster-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.poster-item {
  display: flex;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.poster-cover {
  width: 120px;
  height: 80px;
  object-fit: cover;
}

.poster-content {
  flex: 1;
  padding: 12px 16px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.poster-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.poster-title {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.poster-actions {
  display: flex;
  gap: 8px;
}

.poster-desc {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.poster-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #999;
  font-size: 12px;
}

.poster-status {
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 11px;
}

.poster-status.active {
  background: #e1f5fe;
  color: #0277bd;
}

.poster-status.inactive {
  background: #ffebee;
  color: #c62828;
}

/* 表格列表样式 */
.table-list {
  width: 100%;
}

.table-actions {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-wrap: wrap;
  justify-content: center;
}

.action-btn {
  margin: 0 !important;
  min-width: 50px;
  white-space: nowrap;
  font-size: 12px;
  padding: 4px 8px;
}

/* 调试信息样式 */
.debug-section {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 20px;
}

.debug-section h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  color: #333;
  border-bottom: 1px solid #e0e0e0;
  padding-bottom: 8px;
}

.debug-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.debug-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.debug-item label {
  font-weight: 600;
  color: #666;
  font-size: 14px;
}

.debug-item pre {
  background: #f8f9fa;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  padding: 12px;
  margin: 0;
  font-size: 12px;
  color: #333;
  overflow-x: auto;
  max-height: 200px;
  overflow-y: auto;
}
</style>
