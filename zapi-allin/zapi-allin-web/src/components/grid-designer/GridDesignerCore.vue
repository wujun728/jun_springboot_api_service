<template>
  <div class="grid-designer-core">
    <!-- 左侧预览区域 (70%) -->
    <div class="preview-area">
      <div class="preview-header">
        <h3>列表预览</h3>
        <el-button 
          v-if="gridConfig.fields && gridConfig.fields.length > 0" 
          size="small" 
          @click="loadPreviewData"
          :loading="previewLoading"
        >
          <el-icon><Refresh /></el-icon>
          刷新预览
        </el-button>
      </div>
      <div class="preview-content">
        <!-- 有字段时显示表格预览 -->
        <template v-if="gridConfig.fields && gridConfig.fields.length > 0">
          <!-- 筛选条件区域 -->
          <div v-if="gridConfig.filters && gridConfig.filters.length > 0" class="preview-filters">
            <div class="filter-row">
              <div 
                v-for="(filter, index) in gridConfig.filters" 
                :key="index"
                class="filter-item-inline"
              >
                <span class="filter-label">{{ getFieldLabel(filter.key) }}:</span>
                <el-input
                  v-model="filter.value"
                  :placeholder="`请输入${getFieldLabel(filter.key)}`"
                  size="small"
                  clearable
                  @change="loadPreviewData"
                  style="width: 200px;"
                />
              </div>
              <el-button size="small" @click="loadPreviewData" type="primary">
                <el-icon><Search /></el-icon>
                查询
              </el-button>
              <el-button size="small" @click="handleClearFilters">
                <el-icon><RefreshLeft /></el-icon>
                重置
              </el-button>
            </div>
          </div>
          
          <div class="preview-table-wrapper">
            <el-table
              :data="previewData"
              v-loading="previewLoading"
              :stripe="gridConfig.striped"
              :show-overflow-tooltip="true"
              :border="false"
              style="width: 100%"
              class="simple-table"
            >
              <!-- 序号列 -->
              <el-table-column
                v-if="gridConfig.showIndex"
                type="index"
                label="序号"
                width="60"
                align="center"
              />
              
              <!-- 勾选框 -->
              <el-table-column
                v-if="gridConfig.showSelection"
                type="selection"
                width="50"
                align="center"
              />
              
              <!-- 动态字段列 -->
              <el-table-column
                v-for="field in visibleFields"
                :key="field.key"
                :prop="field.key"
                :label="field.name"
                :sortable="field.sortable"
                min-width="120"
              >
                <template #default="{ row }">
                  <span>{{ formatFieldValue(row[field.key], field.type) }}</span>
                </template>
              </el-table-column>
              
              <!-- 操作列 -->
              <el-table-column
                v-if="gridConfig.showActionColumn"
                label="操作"
                :width="calculateActionColumnWidth"
                align="center"
                fixed="right"
              >
                <template #default>
                  <template v-for="action in enabledActions" :key="action.id">
                    <el-button 
                      :type="action.type" 
                      size="small" 
                      text
                    >
                      {{ action.label }}
                    </el-button>
                  </template>
                </template>
              </el-table-column>
            </el-table>
          </div>
          
          <!-- 分页 -->
          <div class="preview-pagination">
            <el-pagination
              v-model:current-page="previewPagination.current"
              v-model:page-size="previewPagination.size"
              :page-sizes="[10, 20, 50, 100]"
              :total="previewPagination.total"
              :background="true"
              :layout="getPaginationLayout()"
              @size-change="handlePageSizeChange"
              @current-change="loadPreviewData"
            />
          </div>
        </template>
        
        <!-- 空状态 -->
        <div v-else class="preview-placeholder">
          <el-empty description="表格预览区域">
            <template #description>
              <p>请先选择表单并添加字段</p>
              <p>添加字段后将显示数据预览</p>
            </template>
          </el-empty>
        </div>
      </div>
    </div>

    <!-- 右侧配置区域 (30%) -->
    <div class="config-area">
      <div class="config-nav">
        <div 
          v-for="tab in configTabs"
          :key="tab.key"
          :class="['nav-item', { active: activeTab === tab.key }]"
          @click="activeTab = tab.key"
        >
          {{ tab.label }}
        </div>
      </div>

      <div class="config-content">
        <!-- 基础设置 -->
        <div v-if="activeTab === 'basic'" class="config-panel">
          <h4>基础设置</h4>
          <div class="form-item">
            <label>表单选择</label>
            <el-select
              v-model="gridConfig.selectedFormId"
              placeholder="请选择表单"
              :loading="formListLoading"
              clearable
              filterable
              style="width: 100%"
            >
              <el-option
                v-for="entry in formEntryList"
                :key="entry.id"
                :label="entry.name"
                :value="entry.entryId"
              />
            </el-select>
            <div v-if="formEntryList.length === 0 && !formListLoading" class="form-hint">
              <span style="color: var(--text-tertiary); font-size: 12px;">当前应用下暂无表单类型条目</span>
            </div>
          </div>
          <div class="form-item">
            <el-checkbox v-model="gridConfig.showIndex">显示序号</el-checkbox>
          </div>
          <div class="form-item">
            <el-checkbox v-model="gridConfig.showSelection">显示勾选框</el-checkbox>
          </div>
          <div class="form-item">
            <el-checkbox v-model="gridConfig.striped">隔行变色</el-checkbox>
          </div>
        </div>

        <!-- 显示设置 -->
        <div v-if="activeTab === 'display'" class="config-panel">
          <h4>显示设置</h4>
          <div class="section">
            <div class="section-header">
              <h5>字段管理</h5>
              <el-button size="small" type="primary" @click="handleAddField">
                <el-icon><Plus /></el-icon>
                添加字段
              </el-button>
            </div>
            
            <!-- 已添加的字段列表 -->
            <draggable
              v-if="gridConfig.fields && gridConfig.fields.length > 0"
              v-model="gridConfig.fields"
              item-key="id"
              class="field-list-container"
              handle=".drag-handle"
              animation="200"
            >
              <template #item="{ element: field, index }">
                <div class="field-item">
                  <div class="field-drag">
                    <el-icon class="drag-handle"><Rank /></el-icon>
                  </div>
                  <div class="field-content">
                    <div class="field-info">
                      <span class="field-name">{{ field.name }}</span>
                    </div>
                  </div>
                  <div class="field-settings">
                    <el-tooltip content="是否在列表中显示" placement="top">
                      <el-checkbox v-model="field.visible" size="small" label="显示" />
                    </el-tooltip>
                    <el-tooltip content="是否允许排序" placement="top">
                      <el-checkbox v-model="field.sortable" size="small" label="排序" />
                    </el-tooltip>
                    <el-tooltip content="是否允许筛选" placement="top">
                      <el-checkbox v-model="field.filterable" size="small" label="筛选" />
                    </el-tooltip>
                  </div>
                  <div class="field-actions">
                    <el-button
                      size="small"
                      type="danger"
                      link
                      @click="handleRemoveField(index)"
                    >
                      <el-icon><Delete /></el-icon>
                    </el-button>
                  </div>
                </div>
              </template>
            </draggable>
            
            <!-- 空状态 -->
            <div v-else class="empty-fields">
              <p>暂无字段，请先选择表单或手动添加字段</p>
            </div>
          </div>
        </div>

        <!-- 筛选设置 -->
        <div v-if="activeTab === 'filter'" class="config-panel">
          <h4>筛选设置</h4>
          <div class="filter-toolbar">
            <el-button size="small" type="primary" @click="handleAddFilter">
              <el-icon><Plus /></el-icon>
              添加筛选条件
            </el-button>
          </div>
          
          <div v-if="gridConfig.filters && gridConfig.filters.length > 0" class="filter-list">
            <div v-for="(filter, index) in gridConfig.filters" :key="index" class="filter-item">
              <div class="filter-row">
                <el-select
                  v-model="filter.key"
                  placeholder="选择字段"
                  size="small"
                  style="width: 120px"
                  filterable
                >
                  <el-option
                    v-for="field in fieldList"
                    :key="field.key"
                    :label="field.name"
                    :value="field.key"
                  />
                </el-select>
                
                <el-select v-model="filter.operator" placeholder="操作符" size="small" style="width: 90px">
                  <el-option label="等于" value="eq" />
                  <el-option label="不等于" value="ne" />
                  <el-option label="包含" value="like" />
                  <el-option label="大于" value="gt" />
                  <el-option label="小于" value="lt" />
                  <el-option label="大于等于" value="ge" />
                  <el-option label="小于等于" value="le" />
                </el-select>

                <el-input
                  v-model="filter.value"
                  placeholder="值"
                  size="small"
                  style="flex: 1"
                />

                <el-button
                  type="danger"
                  link
                  size="small"
                  @click="handleRemoveFilter(index)"
                >
                  <el-icon><Delete /></el-icon>
                </el-button>
              </div>
            </div>
          </div>

          <div v-else class="empty-filters">
            <p>暂无筛选条件，点击"添加筛选条件"开始设置</p>
          </div>
        </div>

        <!-- 分页设置 -->
        <div v-if="activeTab === 'pagination'" class="config-panel">
          <h4>分页设置</h4>
          <div class="form-item">
            <label>每页条数</label>
            <el-input-number 
              v-model="gridConfig.pageSize" 
              :min="1" 
              :max="100"
            />
          </div>
          <div class="form-item">
            <el-checkbox v-model="gridConfig.showSizeChanger">
              显示每页条数选择器
            </el-checkbox>
          </div>
          <div class="form-item">
            <el-checkbox v-model="gridConfig.showQuickJumper">
              显示快速跳转
            </el-checkbox>
          </div>
        </div>

        <!-- 风格设置 -->
        <div v-if="activeTab === 'style'" class="config-panel">
          <h4>风格设置</h4>
          <div class="form-item">
            <label>列表风格</label>
            <el-radio-group v-model="gridConfig.style">
              <el-radio label="simple">简约风格</el-radio>
              <el-radio label="poster">海报风格</el-radio>
              <el-radio label="table">表格列表</el-radio>
            </el-radio-group>
          </div>
        </div>

        <!-- 操作设置 -->
        <div v-if="activeTab === 'actions'" class="config-panel">
          <h4>操作设置</h4>
          
          <!-- 显示操作列开关 -->
          <div class="form-item">
            <el-checkbox v-model="gridConfig.showActionColumn">显示操作列</el-checkbox>
          </div>
          
          <!-- 操作按钮管理 -->
          <div v-if="gridConfig.showActionColumn" class="section">
            <div class="section-header">
              <h5>操作按钮</h5>
            </div>
            
            <div class="action-list">
              <div
                v-for="action in gridConfig.actions"
                :key="action.id"
                class="action-item"
              >
                <el-checkbox v-model="action.enabled" class="action-checkbox">
                  {{ action.label }}
                </el-checkbox>
                <el-tag :type="action.type" size="small" effect="plain">
                  {{ action.type === 'primary' ? '主要' : action.type === 'danger' ? '危险' : '默认' }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 字段选择对话框 -->
    <el-dialog
      v-model="fieldDialogVisible"
      title="选择字段"
      width="600px"
      :close-on-click-modal="false"
    >
      <div class="field-selection" v-loading="fieldLoading">
        <el-alert
          v-if="currentTableName"
          :title="`物理表名: ${currentTableName}`"
          type="success"
          :closable="false"
          show-icon
          style="margin-bottom: 16px;"
        />
        
        <div v-if="fieldList.length > 0" class="field-list">
          <div class="field-list-header">
            <span>共 {{ fieldList.length }} 个字段</span>
            <el-button size="small" @click="selectAllFields">全选</el-button>
          </div>
          
          <el-checkbox-group v-model="selectedFields" class="field-checkbox-group">
            <div
              v-for="field in fieldList"
              :key="field.key"
              class="field-checkbox-item"
            >
              <el-checkbox :label="field.key">
                <span class="field-name">{{ field.name }}</span>
                <span class="field-key">({{ field.key }})</span>
                <el-tag v-if="field.isSystemField" size="small" type="info">系统</el-tag>
                <el-tag size="small">{{ field.type }}</el-tag>
              </el-checkbox>
            </div>
          </el-checkbox-group>
        </div>
        
        <el-empty v-else description="暂无可选字段" />
      </div>
      
      <template #footer>
        <el-button @click="fieldDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmAddFields" :disabled="selectedFields.length === 0">
          确定 ({{ selectedFields.length }})
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, computed } from 'vue'
import { Plus, Delete, Rank, Refresh, Search, RefreshLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { wbAppEntryApi, formApi } from '@/api'
import draggable from 'vuedraggable'

const props = defineProps({
  gridId: {
    type: String,
    default: ''
  },
  appId: {
    type: [String, Number],
    default: ''
  },
  initialConfig: {
    type: Object,
    default: null
  }
})

const activeTab = ref('basic')

const configTabs = [
  { key: 'basic', label: '基础设置' },
  { key: 'display', label: '显示设置' },
  { key: 'filter', label: '筛选设置' },
  { key: 'pagination', label: '分页设置' },
  { key: 'style', label: '风格设置' },
  { key: 'actions', label: '操作设置' }
]

const gridConfig = ref({
  selectedFormId: '',
  showIndex: true,
  showSelection: true,
  striped: true,
  showActionColumn: true,
  pageSize: 10,
  showSizeChanger: true,
  showQuickJumper: true,
  style: 'table',
  fields: [], // 添加字段列表
  filters: [], // 筛选条件列表
  tableName: '', // 添加物理表名
  actions: [ // 操作按钮列表
    { id: 'view', label: '查看', type: 'primary', enabled: true },
    { id: 'edit', label: '编辑', type: 'primary', enabled: true },
    { id: 'delete', label: '删除', type: 'danger', enabled: true }
  ]
})

// 表单列表
const formEntryList = ref([])
const formListLoading = ref(false)

// 字段列表
const fieldList = ref([])
const fieldDialogVisible = ref(false)
const selectedFields = ref([])
const fieldLoading = ref(false)
const currentTableName = ref('') // 当前表单的物理表名
const currentFormId = ref('') // 当前表单ID

// 预览数据
const previewData = ref([])
const previewLoading = ref(false)
const previewPagination = ref({
  current: 1,
  size: 10,
  total: 0
})

// 监听 gridConfig.pageSize 变化，同步到预览分页
watch(() => gridConfig.value.pageSize, (newSize) => {
  if (newSize && newSize > 0) {
    previewPagination.value.size = newSize
    // 重置到第一页并刷新数据
    previewPagination.value.current = 1
    if (!isInitializing.value && currentFormId.value && gridConfig.value.fields?.length > 0) {
      loadPreviewData()
    }
  }
})

// 是否正在初始化配置（避免触发不必要的watch）
const isInitializing = ref(false)

// 可见字段（用于表格显示）
const visibleFields = computed(() => {
  return gridConfig.value.fields?.filter(f => f.visible) || []
})

// 启用的操作按钮
const enabledActions = computed(() => {
  return gridConfig.value.actions?.filter(a => a.enabled) || []
})

// 计算操作列宽度
const calculateActionColumnWidth = computed(() => {
  const count = enabledActions.value.length
  if (count === 0) return 100
  // 每个按钮大约需要 60-70px
  return Math.max(100, count * 70)
})

// 监听表单选择变化，自动加载字段
watch(() => gridConfig.value.selectedFormId, async (newFormId) => {
  // 如果正在初始化，跳过
  if (isInitializing.value) {
    return
  }
  
  if (newFormId) {
    await loadFormFields(newFormId)
  } else {
    fieldList.value = []
    currentTableName.value = ''
    currentFormId.value = ''
    previewData.value = []
  }
})

// 监听字段变化，自动刷新预览
watch(() => gridConfig.value.fields, async (newFields) => {
  // 如果正在初始化，跳过
  if (isInitializing.value) {
    return
  }
  
  if (newFields && newFields.length > 0 && currentFormId.value) {
    await loadPreviewData()
  } else {
    previewData.value = []
  }
}, { deep: true })

// 监听筛选条件变化，自动刷新预览
watch(() => gridConfig.value.filters, async () => {
  if (isInitializing.value) return
  
  if (currentFormId.value && gridConfig.value.fields && gridConfig.value.fields.length > 0) {
    await loadPreviewData()
  }
}, { deep: true })

// 加载表单字段
const loadFormFields = async (entryId) => {
  fieldLoading.value = true
  try {
    // 先根据 entryId 获取表单信息
    const formResponse = await formApi.getFormByEntryId(entryId)
    if (formResponse.code !== 200 || !formResponse.data) {
      ElMessage.error('未找到对应的表单')
      return
    }
    
    const formId = formResponse.data.id
    currentFormId.value = formId
    
    // 根据 formId 获取表信息和字段列表
    const tableInfoResponse = await formApi.getTableInfoByFormId(formId)
    if (tableInfoResponse.code === 200 && tableInfoResponse.data) {
      const data = tableInfoResponse.data
      
      // 保存物理表名
      currentTableName.value = data.tableName || ''
      
      // 解析字段信息
      if (data.columnInfos && Array.isArray(data.columnInfos)) {
        fieldList.value = data.columnInfos.map((column, index) => ({
          id: `field_${Date.now()}_${index}`,
          name: column.columnComment || column.columnName,
          key: column.columnName,
          type: mapColumnType(column.columnType, column.formType),
          isSystemField: column.isSysField === 1,
          visible: true,
          sortable: true,
          filterable: true
        }))
        
        console.log('加载字段成功:', fieldList.value)
        console.log('物理表名:', currentTableName.value)
      }
    }
  } catch (error) {
    console.error('加载表单字段失败:', error)
    ElMessage.error('加载表单字段失败')
  } finally {
    fieldLoading.value = false
  }
}

// 映射字段类型
const mapColumnType = (columnType, formType) => {
  if (formType) {
    const type = formType.toLowerCase()
    if (type.includes('number') || type.includes('inputnumber')) return 'number'
    if (type.includes('date')) return type.includes('time') ? 'datetime' : 'date'
    if (type.includes('select') || type.includes('radio') || type.includes('checkbox')) return 'select'
    if (type.includes('switch')) return 'boolean'
  }
  
  if (columnType) {
    const type = columnType.toLowerCase()
    if (type.includes('int') || type.includes('decimal') || type.includes('float')) return 'number'
    if (type.includes('datetime') || type.includes('timestamp')) return 'datetime'
    if (type.includes('date')) return 'date'
    if (type.includes('bool') || type.includes('tinyint(1)')) return 'boolean'
  }
  
  return 'text'
}

// 添加字段
const handleAddField = () => {
  if (!gridConfig.value.selectedFormId) {
    ElMessage.warning('请先选择表单')
    return
  }
  
  if (fieldList.value.length === 0) {
    ElMessage.warning('该表单暂无可用字段')
    return
  }
  
  fieldDialogVisible.value = true
}

// 确认添加字段
const confirmAddFields = () => {
  if (selectedFields.value.length === 0) {
    ElMessage.warning('请至少选择一个字段')
    return
  }
  
  // 将选中的字段添加到配置中
  const fieldsToAdd = fieldList.value.filter(field => 
    selectedFields.value.includes(field.key)
  )
  
  if (!gridConfig.value.fields) {
    gridConfig.value.fields = []
  }
  
  // 避免重复添加
  fieldsToAdd.forEach(field => {
    const exists = gridConfig.value.fields.some(f => f.key === field.key)
    if (!exists) {
      gridConfig.value.fields.push({ ...field })
    }
  })
  
  // 保存物理表名
  gridConfig.value.tableName = currentTableName.value
  
  ElMessage.success(`已添加 ${fieldsToAdd.length} 个字段`)
  fieldDialogVisible.value = false
  selectedFields.value = []
}

// 全选字段
const selectAllFields = () => {
  if (selectedFields.value.length === fieldList.value.length) {
    selectedFields.value = []
  } else {
    selectedFields.value = fieldList.value.map(f => f.key)
  }
}

// 获取当前应用下的表单类型条目
const getFormEntries = async () => {
  if (!props.appId) {
    console.warn('appId 为空，无法获取表单列表')
    return
  }

  formListLoading.value = true
  try {
    const response = await wbAppEntryApi.getAppEntryTree(props.appId)
    if (response.code === 200) {
      // 筛选出表单类型的条目
      const allEntries = response.data || []
      formEntryList.value = filterFormEntries(allEntries)
      console.log('获取到的表单条目:', formEntryList.value)
    } else {
      ElMessage.error(response.message || '获取表单列表失败')
    }
  } catch (error) {
    console.error('获取表单列表失败:', error)
    ElMessage.error('获取表单列表失败')
  } finally {
    formListLoading.value = false
  }
}

// 递归筛选表单类型的条目
const filterFormEntries = (entries) => {
  const formEntries = []
  
  const traverse = (items) => {
    items.forEach(item => {
      if (item.type === 'form') {
        formEntries.push({
          id: item.id,
          name: item.name,
          entryId: item.id
        })
      }
      if (item.children && item.children.length > 0) {
        traverse(item.children)
      }
    })
  }
  
  traverse(entries)
  return formEntries
}

// 移除字段
const handleRemoveField = (index) => {
  gridConfig.value.fields.splice(index, 1)
  ElMessage.success('已移除字段')
}

// 添加筛选条件
const handleAddFilter = () => {
  if (!gridConfig.value.filters) {
    gridConfig.value.filters = []
  }
  gridConfig.value.filters.push({
    key: '',
    operator: 'eq',
    value: ''
  })
}

// 移除筛选条件
const handleRemoveFilter = (index) => {
  gridConfig.value.filters.splice(index, 1)
}

// 加载预览数据
const loadPreviewData = async () => {
  if (!currentFormId.value || !gridConfig.value.fields || gridConfig.value.fields.length === 0) {
    previewData.value = []
    return
  }
  
  previewLoading.value = true
  try {
    // 构建查询条件
    const criteria = {}
    if (gridConfig.value.filters && gridConfig.value.filters.length > 0) {
      gridConfig.value.filters.forEach(filter => {
        if (filter.key && filter.value) {
          // 这里假设后端支持这种格式的查询条件，具体格式可能需要根据后端API调整
          // 简单起见，这里只处理等于的情况，或者根据operator构建
          // 如果后端支持复杂查询，可能需要构建更复杂的criteria对象
          // 目前假设 criteria 是简单的 key-value 对，用于精确匹配
          // 或者 criteria 可以包含操作符信息
          
          // 尝试构建带操作符的查询条件
          // 假设后端支持 { key: value } 形式的精确查询
          // 或者 { key: { operator: 'eq', value: 'val' } } 形式
          
          // 暂时使用简单的 key-value 形式，仅支持等于
          // 如果需要支持其他操作符，需要确认后端API格式
          // 这里先尝试将操作符和值组合，或者只传递值
          
          // 假设后端接受 { fieldName: value } 进行精确匹配
          // 对于其他操作符，可能需要特殊处理
          
          // 为了演示效果，这里先只处理 'eq' (等于) 和 'like' (包含)
          if (filter.operator === 'eq') {
            criteria[filter.key] = filter.value
          } else if (filter.operator === 'like') {
            // 假设后端支持模糊查询的字段命名约定，例如 nameLike
            // 或者后端能自动识别
            criteria[filter.key] = filter.value
          } else {
             // 其他操作符暂时也直接传值
             criteria[filter.key] = filter.value
          }
        }
      })
    }

    const response = await formApi.getFormDataPage({
      formId: currentFormId.value,
      criteria: criteria,
      pageNo: previewPagination.value.current,
      pageSize: previewPagination.value.size
    })
    
    if (response.code === 200 && response.data) {
      previewData.value = response.data.records || []
      
      // 确保 total 是数字类型
      previewPagination.value.total = Number(response.data.total) || 0
      
      console.log('预览数据加载成功:', {
        records: previewData.value.length,
        total: previewPagination.value.total
      })
    } else {
      ElMessage.error(response.message || '加载预览数据失败')
      previewData.value = []
      previewPagination.value.total = 0
    }
  } catch (error) {
    console.error('加载预览数据失败:', error)
    ElMessage.error('加载预览数据失败')
    previewData.value = []
  } finally {
    previewLoading.value = false
  }
}

// 格式化字段值
const formatFieldValue = (value, type) => {
  if (value === null || value === undefined) {
    return '-'
  }
  
  switch (type) {
    case 'date':
      return value ? new Date(value).toLocaleDateString('zh-CN') : '-'
    case 'datetime':
      return value ? new Date(value).toLocaleString('zh-CN') : '-'
    case 'boolean':
      return value ? '是' : '否'
    case 'number':
      return typeof value === 'number' ? value.toLocaleString() : value
    default:
      return value
  }
}

// 获取字段标签
const getFieldLabel = (fieldKey) => {
  const field = gridConfig.value.fields?.find(f => f.key === fieldKey)
  return field ? field.name : fieldKey
}

// 获取操作符标签
const getOperatorLabel = (operator) => {
  const operatorMap = {
    'eq': '等于',
    'ne': '不等于',
    'like': '包含',
    'gt': '大于',
    'lt': '小于',
    'ge': '大于等于',
    'le': '小于等于'
  }
  return operatorMap[operator] || operator
}

// 清空筛选条件
const handleClearFilters = () => {
  if (gridConfig.value.filters) {
    gridConfig.value.filters.forEach(filter => {
      filter.value = ''
    })
    loadPreviewData()
  }
}

// 获取分页布局配置
const getPaginationLayout = () => {
  const layouts = ['total']
  
  if (gridConfig.value.showSizeChanger) {
    layouts.push('sizes')
  }
  
  layouts.push('prev', 'pager', 'next')
  
  if (gridConfig.value.showQuickJumper) {
    layouts.push('jumper')
  }
  
  return layouts.join(', ')
}

// 处理每页条数变化
const handlePageSizeChange = (newSize) => {
  // 同步更新配置中的 pageSize
  gridConfig.value.pageSize = newSize
  previewPagination.value.current = 1
  loadPreviewData()
}

// 暴露配置给父组件
defineExpose({
  getConfig: () => gridConfig.value
})

// 组件挂载时获取表单列表和加载已保存的配置
onMounted(async () => {
  isInitializing.value = true
  
  await getFormEntries()
  
  // 如果有初始配置，加载它
  if (props.initialConfig) {
    try {
      const config = typeof props.initialConfig === 'string' 
        ? JSON.parse(props.initialConfig) 
        : props.initialConfig
      
      // 恢复配置
      Object.assign(gridConfig.value, config)
      
      // 同步 pageSize 到预览分页
      if (gridConfig.value.pageSize) {
        previewPagination.value.size = gridConfig.value.pageSize
      }
      
      console.log('加载已保存的配置:', gridConfig.value)
      
      // 如果有选中的表单，加载字段信息
      if (gridConfig.value.selectedFormId) {
        await loadFormFields(gridConfig.value.selectedFormId)
        
        // 如果有字段配置，加载预览数据
        if (gridConfig.value.fields && gridConfig.value.fields.length > 0) {
          await loadPreviewData()
        }
      }
    } catch (error) {
      console.error('加载配置失败:', error)
    }
  } else {
    // 如果没有初始配置，也要同步默认的 pageSize
    previewPagination.value.size = gridConfig.value.pageSize
  }
  
  isInitializing.value = false
})
</script>

<style lang="scss" scoped>
.grid-designer-core {
  display: flex;
  gap: 16px;
  height: 100%;
  background: var(--bg-page);
  padding: 16px;
}

.preview-area {
  flex: 0 0 70%;
  background: var(--bg-card);
  border-radius: 8px;
  box-shadow: var(--shadow-sm);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.preview-header {
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
  
  h3 {
    margin: 0;
    font-size: 16px;
    color: var(--text-primary);
  }
}

.preview-content {
  flex: 1;
  padding: 20px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  
  .preview-filters {
    background: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 4px;
    padding: 16px;
    margin-bottom: 16px;
    
    .filter-row {
      display: flex;
      align-items: center;
      gap: 16px;
      flex-wrap: wrap;
      
      .filter-item-inline {
        display: flex;
        align-items: center;
        gap: 8px;
        
        .filter-label {
          font-size: 14px;
          color: var(--text-primary);
          white-space: nowrap;
        }
      }
    }
  }
  
  .preview-table-wrapper {
    flex: 1;
    overflow: auto;
    background: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 4px;
    margin-bottom: 16px;
    min-height: 0;
    
    // 简洁表格样式
    .simple-table {
      :deep(.el-table__header-wrapper) {
        .el-table__header {
          th {
            background: transparent;
            border: none;
            border-bottom: 1px solid var(--border-color);
            color: var(--text-secondary);
            font-weight: 500;
            font-size: 13px;
            padding: 12px 0;
          }
        }
      }
      
      :deep(.el-table__body-wrapper) {
        .el-table__body {
          tr {
            border: none;
            
            td {
              border: none;
              border-bottom: 1px solid var(--border-color-light);
              padding: 16px 0;
              
              .cell {
                color: var(--text-primary);
                font-size: 14px;
              }
            }
            
            &:hover {
              td {
                background: var(--bg-hover);
              }
            }
            
            &:last-child {
              td {
                border-bottom: none;
              }
            }
          }
        }
      }
      
      // 隔行变色
      :deep(.el-table__row--striped) {
        td {
          background: transparent;
        }
      }
      
      // 去掉表格外边框
      :deep(.el-table__inner-wrapper) {
        &::before {
          display: none;
        }
      }
    }
  }
  
  .preview-pagination {
    padding: 16px;
    background: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 4px;
    display: flex;
    justify-content: flex-end;
    align-items: center;
    flex-shrink: 0;
    min-height: 56px;
    
    .el-pagination {
      display: flex;
      align-items: center;
    }
  }
  
  .preview-placeholder {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.config-area {
  flex: 0 0 30%;
  display: flex;
  background: var(--bg-card);
  border-radius: 8px;
  box-shadow: var(--shadow-sm);
  overflow: hidden;
}

.config-nav {
  width: 120px;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-color);
}

.nav-item {
  padding: 16px 12px;
  cursor: pointer;
  border-bottom: 1px solid var(--border-color);
  font-size: 14px;
  color: var(--text-secondary);
  transition: all 0.2s;
  
  &:hover {
    background: var(--bg-hover);
    color: var(--text-primary);
  }
  
  &.active {
    background: var(--primary-color);
    color: #fff;
  }
}

.config-content {
  flex: 1;
  padding: 20px;
  overflow: auto;
}

.config-panel {
  h4 {
    margin: 0 0 16px 0;
    font-size: 16px;
    color: var(--text-primary);
  }
}

.form-item {
  margin-bottom: 16px;
  
  label {
    display: block;
    margin-bottom: 8px;
    font-size: 14px;
    color: var(--text-secondary);
  }
}

.section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  
  h5 {
    margin: 0;
    font-size: 14px;
    color: var(--text-primary);
    font-weight: 600;
  }
}

.empty-fields,
.empty-filters {
  text-align: center;
  color: var(--text-tertiary);
  font-size: 12px;
  padding: 20px;
  background: var(--bg-secondary);
  border-radius: 6px;
  border: 1px dashed var(--border-color);
}

.filter-toolbar {
  margin-bottom: 16px;
}

.form-hint {
  margin-top: 4px;
}

.field-list-container {
  max-height: 500px;
  overflow-y: auto;
  background: transparent;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 2px;
}

.field-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  transition: all 0.2s;
  background: var(--bg-card);
  cursor: move;
  
  &:hover {
    border-color: var(--primary-color);
    box-shadow: var(--shadow-sm);
    transform: translateY(-1px);
    
    .field-drag .drag-handle {
      opacity: 1;
      color: var(--primary-color);
    }
  }
  
  // 拖拽时的样式
  &.sortable-ghost {
    opacity: 0.4;
    background: var(--bg-secondary);
  }
  
  &.sortable-drag {
    opacity: 0.8;
    box-shadow: var(--shadow-md);
    transform: rotate(2deg);
  }
  
  .field-drag {
    margin-right: 12px;
    display: flex;
    align-items: center;
    
    .drag-handle {
      cursor: move;
      color: var(--text-tertiary);
      font-size: 18px;
      opacity: 0.5;
      transition: all 0.2s;
    }
  }
  
  .field-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 6px;
    min-width: 0;
    margin-right: 16px;
    
    .field-info {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .field-name {
        font-weight: 600;
        color: var(--text-primary);
        font-size: 14px;
      }
      
      .field-key {
        color: var(--text-secondary);
        font-size: 12px;
        font-family: monospace;
        background: var(--bg-secondary);
        padding: 2px 6px;
        border-radius: 4px;
      }
    }
    
    .field-tags {
      display: flex;
      gap: 6px;
    }
  }
  
  .field-settings {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-right: 16px;
    background: var(--bg-secondary);
    padding: 4px 12px;
    border-radius: 6px;
    
    .el-checkbox {
      margin-right: 0;
      height: 24px;
      
      :deep(.el-checkbox__label) {
        font-size: 12px;
        padding-left: 4px;
        color: var(--text-secondary);
      }
    }
  }
  
  .field-actions {
    display: flex;
    align-items: center;
  }
}

.field-selection {
  .field-list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    padding-bottom: 8px;
    border-bottom: 1px solid var(--border-color);
    
    span {
      font-size: 14px;
      color: var(--text-secondary);
    }
  }
  
  .field-checkbox-group {
    max-height: 400px;
    overflow-y: auto;
  }
  
  .field-checkbox-item {
    padding: 8px;
    border-bottom: 1px solid var(--border-color-light);
    
    &:hover {
      background: var(--bg-hover);
    }
    
    .field-name {
      font-weight: 500;
      margin-right: 8px;
    }
    
    .field-key {
      color: var(--text-tertiary);
      font-size: 12px;
      margin-right: 8px;
    }
    
    .el-tag {
      margin-left: 8px;
    }
  }
}

.filter-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.filter-item {
  background: var(--bg-secondary);
  padding: 12px;
  border-radius: 6px;
  border: 1px solid var(--border-color);
  
  .filter-row {
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

.action-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 8px 0;
}

.action-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: var(--bg-secondary);
  border-radius: 6px;
  border: 1px solid var(--border-color);
  transition: all 0.2s;
  
  &:hover {
    border-color: var(--primary-color);
    background: var(--bg-card);
  }
  
  .action-checkbox {
    flex: 1;
    
    :deep(.el-checkbox__label) {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-primary);
    }
  }
}
</style>
