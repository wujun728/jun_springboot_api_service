<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage, ElConfigProvider } from 'element-plus'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { useRouter } from 'vue-router'
import { 
  Plus, Edit, Delete, View, Search, RefreshLeft, Download, 
  Refresh, Setting, More, Check, DCaret 
} from '@element-plus/icons-vue'
// @ts-ignore
import { formApi } from '@/api'

const router = useRouter()

// 定义类型
interface ListField {
  id: string
  name: string
  key: string
  visible: boolean
  sortable: boolean
  filterable: boolean
  width?: number
  type?: 'text' | 'number' | 'date' | 'datetime' | 'select' | 'boolean'
  isSystemField?: boolean // 标记是否为系统字段
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
  selectedFormId: string
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
  systemButtons: ButtonConfig[]
  showActionColumn: boolean
}

// 响应式数据
const currentConfig = ref<ListConfig>({
  name: '未命名列表',
  selectedFormId: '',
  showIndex: true,
  showSelection: true,
  striped: true,
  style: 'table',
  rowClickAction: 'none',
  customRowClickAction: '',
  fields: [
    { id: '1', name: '标题', key: 'title', visible: true, sortable: true, filterable: true, type: 'text' },
    { id: '2', name: '描述', key: 'description', visible: true, sortable: false, filterable: true, type: 'text' },
    { id: '3', name: '创建时间', key: 'createTime', visible: true, sortable: true, filterable: true, type: 'datetime' },
    { id: '4', name: '状态', key: 'status', visible: true, sortable: true, filterable: true, type: 'select' }
  ],
  filters: [],
  pagination: {
    pageSize: 10,
    showSizeChanger: true,
    showQuickJumper: true,
    showTotal: true,
    position: 'bottom',
    align: 'right'
  },
  conditionButtons: [
    {
      id: 'reset',
      name: '重置',
      color: '#909399',
      style: 'outlined',
      size: 'default',
      icon: 'RefreshLeft',
      visible: true,
      disabled: false,
      action: 'reset'
    },
    {
      id: 'search',
      name: '查询',
      color: '#409eff',
      style: 'filled',
      size: 'default',
      icon: 'Search',
      visible: true,
      disabled: false,
      action: 'search'
    }
  ],
  actionButtons: [
    {
      id: 'edit',
      name: '编辑',
      color: '#409eff',
      style: 'filled',
      size: 'small',
      icon: 'Edit',
      visible: true,
      disabled: false,
      action: 'edit'
    },
    {
      id: 'delete',
      name: '删除',
      color: '#f56c6c',
      style: 'filled',
      size: 'small',
      icon: 'Delete',
      visible: true,
      disabled: false,
      action: 'delete'
    },
    {
      id: 'view',
      name: '查看',
      color: '#67c23a',
      style: 'outlined',
      size: 'small',
      icon: 'View',
      visible: true,
      disabled: false,
      action: 'view'
    }
  ],
  systemButtons: [
    {
      id: 'add',
      name: '新增',
      color: '#67c23a',
      style: 'filled',
      size: 'default',
      icon: 'Plus',
      visible: true,
      disabled: false,
      action: 'add'
    },
    {
      id: 'import',
      name: '导入',
      color: '#e6a23c',
      style: 'filled',
      size: 'default',
      icon: 'Upload',
      visible: true,
      disabled: false,
      action: 'import'
    },
    {
      id: 'export',
      name: '导出',
      color: '#909399',
      style: 'outlined',
      size: 'default',
      icon: 'Download',
      visible: true,
      disabled: false,
      action: 'export'
    }
  ],
  showActionColumn: true
})

const activeTab = ref('basic')

// 表单列表相关
const formList = ref<any[]>([])
const formListLoading = ref(false)
const fieldsLoading = ref(false)

// 字段选择相关
const availableFields = ref<ListField[]>([]) // 从后端获取的完整字段列表
const fieldSelectionVisible = ref(false) // 字段选择弹框显示状态

// 分页相关
const currentPage = ref(1)
const pageSize = ref(10)

// 实际数据相关
const realData = ref<any[]>([]) // 从后端获取的真实数据
const realDataLoading = ref(false) // 数据加载状态
const realTotal = ref(0) // 真实数据总数

// 预览区域的筛选条件
const previewFilters = ref<Record<string, string>>({})

// 拖拽相关
const draggedFieldIndex = ref<number | null>(null)
const draggedButtonIndex = ref<number | null>(null)
const draggedButtonType = ref<'condition' | 'action' | 'system' | null>(null)

// 按钮编辑弹框状态
const buttonEditVisible = ref(false)
const editingButton = ref<ButtonConfig | null>(null)
const editingButtonType = ref<'condition' | 'action' | 'system'>('condition')
const editingButtonIndex = ref<number>(-1)

// 字段编辑弹框状态
const fieldEditVisible = ref(false)
const editingField = ref<ListField | null>(null)
const editingFieldIndex = ref<number>(-1)

// 操作符选项
const operatorOptions = [
  { label: '等于', value: 'eq' },
  { label: '包含', value: 'contains' },
  { label: '范围', value: 'range' },
  { label: '大于', value: 'gt' },
  { label: '小于', value: 'lt' }
]

// 按钮大小选项
const buttonSizeOptions = [
  { label: '大', value: 'large' },
  { label: '默认', value: 'default' },
  { label: '小', value: 'small' }
]

// 按钮风格选项
const buttonStyleOptions = [
  { label: '填充', value: 'filled' },
  { label: '描边', value: 'outlined' },
  { label: '文本', value: 'text' }
]

// 常用图标选项
const iconOptions = [
  { label: '搜索', value: 'Search' },
  { label: '重置', value: 'RefreshLeft' },
  { label: '编辑', value: 'Edit' },
  { label: '删除', value: 'Delete' },
  { label: '查看', value: 'View' },
  { label: '添加', value: 'Plus' },
  { label: '导出', value: 'Download' },
  { label: '刷新', value: 'Refresh' },
  { label: '设置', value: 'Setting' },
  { label: '更多', value: 'More' },
  { label: '无图标', value: '' }
]

// 条件按钮动作选项
const conditionButtonActionOptions = [
  { label: '重置筛选', value: 'reset' },
  { label: '查询数据', value: 'search' },
  { label: '刷新列表', value: 'refresh' },
  { label: '自定义', value: 'custom' }
]

// 操作按钮动作选项
const actionButtonActionOptions = [
  { label: '编辑记录', value: 'edit' },
  { label: '删除记录', value: 'delete' },
  { label: '查看详情', value: 'view' },
  { label: '批量操作', value: 'batch' },
  { label: '自定义', value: 'custom' }
]

// 系统按钮动作选项
const systemButtonActionOptions = [
  { label: '新增记录', value: 'add' },
  { label: '导入数据', value: 'import' },
  { label: '导出数据', value: 'export' },
  { label: '批量导入', value: 'batch_import' },
  { label: '模板下载', value: 'template_download' },
  { label: '刷新列表', value: 'refresh' },
  { label: '自定义', value: 'custom' }
]

// 模拟数据
const allSampleData = ref([
  {
    id: 1,
    title: '示例标题1',
    description: '这是一个示例描述',
    createTime: '2024-01-01',
    status: '启用',
    cover: 'https://via.placeholder.com/200x120'
  },
  {
    id: 2,
    title: '示例标题2',
    description: '这是另一个示例描述',
    createTime: '2024-01-02',
    status: '禁用',
    cover: 'https://via.placeholder.com/200x120'
  },
  {
    id: 3,
    title: '示例标题3',
    description: '这是第三个示例描述',
    createTime: '2024-01-03',
    status: '启用',
    cover: 'https://via.placeholder.com/200x120'
  },
  {
    id: 4,
    title: '示例标题4',
    description: '这是第四个示例描述',
    createTime: '2024-01-04',
    status: '禁用',
    cover: 'https://via.placeholder.com/200x120'
  },
  {
    id: 5,
    title: '示例标题5',
    description: '这是第五个示例描述',
    createTime: '2024-01-05',
    status: '启用',
    cover: 'https://via.placeholder.com/200x120'
  },
  {
    id: 6,
    title: '示例标题6',
    description: '这是第六个示例描述',
    createTime: '2024-01-06',
    status: '禁用',
    cover: 'https://via.placeholder.com/200x120'
  },
  {
    id: 7,
    title: '示例标题7',
    description: '这是第七个示例描述',
    createTime: '2024-01-07',
    status: '启用',
    cover: 'https://via.placeholder.com/200x120'
  },
  {
    id: 8,
    title: '示例标题8',
    description: '这是第八个示例描述',
    createTime: '2024-01-08',
    status: '禁用',
    cover: 'https://via.placeholder.com/200x120'
  }
])

// 获取筛选设置中配置的字段信息
const configuredFilterFields = computed(() => {
  return currentConfig.value.filters
    .filter(filter => filter.field)
    .map(filter => {
      const field = currentConfig.value.fields.find(f => f.key === filter.field)
      return {
        ...filter,
        fieldName: field?.name || filter.field,
        fieldKey: filter.field,
        fieldType: field?.type || 'text'
      }
    })
})

// 显示数据逻辑 - 优先使用真实数据，否则使用模拟数据
const displayData = computed(() => {
  // 如果有表单选择且加载完成，使用真实数据
  if (currentConfig.value.selectedFormId && !realDataLoading.value) {
    return realData.value
  }
  // 否则使用模拟数据进行预览
  return allSampleData.value
})

// 筛选逻辑（仅用于模拟数据）
const filteredData = computed(() => {
  // 如果使用真实数据，筛选在后端完成
  if (currentConfig.value.selectedFormId && !realDataLoading.value) {
    return realData.value
  }
  
  // 模拟数据的前端筛选逻辑
  let data = [...allSampleData.value]
  
  configuredFilterFields.value.forEach(filter => {
    const filterValue = previewFilters.value[filter.fieldKey]
    if (!filterValue) return
    
    data = data.filter(item => {
      const fieldValue = item[filter.fieldKey as keyof typeof item]
      if (fieldValue === undefined) return false
      
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
  // 如果使用真实数据，分页在后端完成
  if (currentConfig.value.selectedFormId && !realDataLoading.value) {
    return realData.value
  }
  
  // 模拟数据的前端分页
  const start = (currentPage.value - 1) * currentConfig.value.pagination.pageSize
  const end = start + currentConfig.value.pagination.pageSize
  return filteredData.value.slice(start, end)
})

const totalItems = computed(() => {
  // 如果使用真实数据，使用后端返回的总数
  if (currentConfig.value.selectedFormId && !realDataLoading.value) {
    return realTotal.value
  }
  // 模拟数据使用前端筛选后的数量
  return filteredData.value.length
})

const totalPages = computed(() => Math.ceil(totalItems.value / currentConfig.value.pagination.pageSize))

// 获取可以添加的字段（排除已经添加的字段）
const addableFields = computed(() => {
  const existingKeys = new Set(currentConfig.value.fields.map(f => f.key))
  return availableFields.value.filter(field => !existingKeys.has(field.key))
})

// 获取可以添加的用户字段
const addableUserFields = computed(() => {
  return addableFields.value.filter(field => !field.isSystemField)
})

// 获取可以添加的系统字段
const addableSystemFields = computed(() => {
  return addableFields.value.filter(field => field.isSystemField)
})

// 动态生成分页布局
const paginationLayout = computed(() => {
  const parts: string[] = []
  
  if (currentConfig.value.pagination.showTotal) {
    parts.push('total')
  }
  
  if (currentConfig.value.pagination.showSizeChanger) {
    parts.push('sizes')
  }
  
  parts.push('prev', 'pager', 'next')
  
  if (currentConfig.value.pagination.showQuickJumper) {
    parts.push('jumper')
  }
  
  return parts.join(', ')
})

// 字段操作
const addField = () => {
  if (addableFields.value.length === 0) {
    ElMessage.warning('没有可添加的字段，所有字段都已添加')
    return
  }
  fieldSelectionVisible.value = true
}

// 从可选字段中添加字段
const addSelectedField = (field: ListField) => {
  const newField = {
    ...field,
    id: `${field.key}_${Date.now()}` // 生成新的唯一ID
  }
  currentConfig.value.fields.push(newField)
  fieldSelectionVisible.value = false
  ElMessage.success(`已添加字段：${field.name}`)
}

const editField = (field: ListField, index: number) => {
  editingField.value = { ...field }
  editingFieldIndex.value = index
  fieldEditVisible.value = true
}

const saveFieldEdit = () => {
  if (!editingField.value) return

  currentConfig.value.fields[editingFieldIndex.value] = { ...editingField.value }
  fieldEditVisible.value = false
}

const cancelFieldEdit = () => {
  fieldEditVisible.value = false
  editingField.value = null
}

const removeField = (id: string) => {
  const index = currentConfig.value.fields.findIndex(f => f.id === id)
  if (index > -1) {
    currentConfig.value.fields.splice(index, 1)
  }
}

// 字段拖拽
const handleFieldDragStart = (index: number) => {
  draggedFieldIndex.value = index
}

const handleFieldDragOver = (e: DragEvent) => {
  e.preventDefault()
}

const handleFieldDrop = (targetIndex: number) => {
  if (draggedFieldIndex.value === null || draggedFieldIndex.value === targetIndex) return
  
  const fields = currentConfig.value.fields
  const draggedField = fields[draggedFieldIndex.value]
  fields.splice(draggedFieldIndex.value, 1)
  fields.splice(targetIndex, 0, draggedField)
  
  draggedFieldIndex.value = null
}

// 筛选规则操作
const addFilter = () => {
  const id = Date.now().toString()
  currentConfig.value.filters.push({
    id,
    field: '',
    operator: 'eq',
    value: ''
  })
}

const removeFilter = (id: string) => {
  const index = currentConfig.value.filters.findIndex(f => f.id === id)
  if (index > -1) {
    currentConfig.value.filters.splice(index, 1)
    const filter = currentConfig.value.filters[index]
    if (filter) {
      delete previewFilters.value[filter.field]
      delete previewFilters.value[`${filter.field}_max`]
    }
  }
}

// 预览筛选操作
const clearPreviewFilters = () => {
  previewFilters.value = {}
  // 如果有表单选择，重新获取数据
  if (currentConfig.value.selectedFormId) {
    getRealFormData()
  }
}

// 执行搜索
const handleSearch = () => {
  // 如果有表单选择，重新获取数据
  if (currentConfig.value.selectedFormId) {
    currentPage.value = 1 // 重置到第一页
    getRealFormData()
  }
}

// 分页操作
const handlePageChange = (page: number) => {
  currentPage.value = page
  // 如果有表单选择，重新获取数据
  if (currentConfig.value.selectedFormId) {
    getRealFormData()
  }
}

const handlePageSizeChange = (size: number) => {
  currentConfig.value.pagination.pageSize = size
  currentPage.value = 1
  // 如果有表单选择，重新获取数据
  if (currentConfig.value.selectedFormId) {
    getRealFormData()
  }
}

// 条件按钮操作
const addConditionButton = () => {
  const newButton = {
    id: Date.now().toString(),
    name: '新按钮',
    color: '#409eff',
    style: 'filled' as const,
    size: 'default' as const,
    icon: '',
    visible: true,
    disabled: false,
    action: 'custom'
  }

  currentConfig.value.conditionButtons.push(newButton)
  editButton(newButton, 'condition', currentConfig.value.conditionButtons.length - 1)
}

const editButton = (button: ButtonConfig, type: 'condition' | 'action' | 'system', index: number) => {
  editingButton.value = { ...button }
  editingButtonType.value = type
  editingButtonIndex.value = index
  buttonEditVisible.value = true
}

const saveButtonEdit = () => {
  if (!editingButton.value) return

  let buttons
  if (editingButtonType.value === 'condition') {
    buttons = currentConfig.value.conditionButtons
  } else if (editingButtonType.value === 'action') {
    buttons = currentConfig.value.actionButtons
  } else {
    buttons = currentConfig.value.systemButtons
  }

  buttons[editingButtonIndex.value] = { ...editingButton.value }
  buttonEditVisible.value = false
}

const cancelButtonEdit = () => {
  buttonEditVisible.value = false
  editingButton.value = null
}

const removeConditionButton = (id: string) => {
  const index = currentConfig.value.conditionButtons.findIndex(b => b.id === id)
  if (index > -1) {
    currentConfig.value.conditionButtons.splice(index, 1)
  }
}

// 操作按钮操作
const addActionButton = () => {
  const newButton = {
    id: Date.now().toString(),
    name: '新按钮',
    color: '#409eff',
    style: 'filled' as const,
    size: 'small' as const,
    icon: '',
    visible: true,
    disabled: false,
    action: 'custom'
  }

  currentConfig.value.actionButtons.push(newButton)
  editButton(newButton, 'action', currentConfig.value.actionButtons.length - 1)
}

const removeActionButton = (id: string) => {
  const index = currentConfig.value.actionButtons.findIndex(b => b.id === id)
  if (index > -1) {
    currentConfig.value.actionButtons.splice(index, 1)
  }
}

// 系统按钮操作
const addSystemButton = () => {
  const newButton = {
    id: Date.now().toString(),
    name: '新按钮',
    color: '#67c23a',
    style: 'filled' as const,
    size: 'default' as const,
    icon: '',
    visible: true,
    disabled: false,
    action: 'custom'
  }

  currentConfig.value.systemButtons.push(newButton)
  editButton(newButton, 'system', currentConfig.value.systemButtons.length - 1)
}

const removeSystemButton = (id: string) => {
  const index = currentConfig.value.systemButtons.findIndex(b => b.id === id)
  if (index > -1) {
    currentConfig.value.systemButtons.splice(index, 1)
  }
}

// 按钮拖拽
const handleButtonDragStart = (index: number, type: 'condition' | 'action' | 'system') => {
  draggedButtonIndex.value = index
  draggedButtonType.value = type
}

const handleButtonDragOver = (e: DragEvent) => {
  e.preventDefault()
}

const handleButtonDrop = (targetIndex: number, type: 'condition' | 'action' | 'system') => {
  if (draggedButtonIndex.value === null || draggedButtonType.value !== type || draggedButtonIndex.value === targetIndex) return
  
  let buttons
  if (type === 'condition') {
    buttons = currentConfig.value.conditionButtons
  } else if (type === 'action') {
    buttons = currentConfig.value.actionButtons
  } else {
    buttons = currentConfig.value.systemButtons
  }
  
  const draggedButton = buttons[draggedButtonIndex.value]
  buttons.splice(draggedButtonIndex.value, 1)
  buttons.splice(targetIndex, 0, draggedButton)
  
  draggedButtonIndex.value = null
  draggedButtonType.value = null
}

// 保存配置
const saveConfig = () => {
  ElMessage.success('配置已保存')
}

// 预览配置
const previewConfig = () => {
  
  try {
    // 保存配置到localStorage
    localStorage.setItem('listPreviewConfig', JSON.stringify(currentConfig.value))
    localStorage.setItem('listPreviewData', JSON.stringify(allSampleData.value))
    localStorage.setItem('listPreviewFilters', JSON.stringify(previewFilters.value))

    ElMessage.success('正在跳转到预览页面...')
    
    // 直接使用window.location.href确保跳转成功
    setTimeout(() => {
      window.location.href = '/home/list-preview'
    }, 500)
    
  } catch (error) {
    console.error('预览配置保存失败:', error)
    ElMessage.error('预览配置保存失败')
  }
}

// 行点击事件处理
const handleRowClick = (row: any, column: any, event: Event) => {
  if (currentConfig.value.rowClickAction === 'none') return

  let actionName = ''
  switch (currentConfig.value.rowClickAction) {
    case 'view':
      actionName = '查看详情'
      break
    case 'edit':
      actionName = '编辑记录'
      break
    case 'custom':
      actionName = currentConfig.value.customRowClickAction || '自定义操作'
      break
  }

  ElMessage.info(`执行行点击操作: ${actionName} - ID: ${row.id}`)
}

// 系统按钮点击事件处理
const handleSystemButtonClick = (button: ButtonConfig) => {
  let actionName = ''
  switch (button.action) {
    case 'add':
      actionName = '新增记录'
      break
    case 'import':
      actionName = '导入数据'
      break
    case 'export':
      actionName = '导出数据'
      break
    case 'batch_import':
      actionName = '批量导入'
      break
    case 'template_download':
      actionName = '模板下载'
      break
    case 'refresh':
      actionName = '刷新列表'
      // 如果有表单选择，重新获取数据
      if (currentConfig.value.selectedFormId) {
        getRealFormData()
      }
      break
    default:
      actionName = button.name
      break
  }

  ElMessage.success(`执行系统操作: ${actionName}`)
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

// 获取字段类型标签
const getFieldTypeLabel = (type?: string) => {
  const typeMap = {
    text: '文本',
    number: '数字',
    date: '日期',
    datetime: '日期时间',
    select: '选择',
    boolean: '布尔'
  }
  return typeMap[type as keyof typeof typeMap] || '文本'
}

// 根据按钮类型获取相应的动作选项
const getButtonActionOptions = () => {
  switch (editingButtonType.value) {
    case 'condition':
      return conditionButtonActionOptions
    case 'action':
      return actionButtonActionOptions
    case 'system':
      return systemButtonActionOptions
    default:
      return []
  }
}

// 获取启用的表单列表
const getEnabledFormList = async () => {
  formListLoading.value = true
  try {
    const response = await formApi.getFormList({
      current: 1,
      size: 1000, // 获取所有启用的表单
      status: 1 // 只获取启用状态的表单
    })
    formList.value = response.data.records || []
  } catch (error) {
    console.error('获取表单列表失败:', error)
    ElMessage.error('获取表单列表失败')
    formList.value = []
  } finally {
    formListLoading.value = false
  }
}

// 根据表单ID获取字段信息
const getFormFieldsByFormId = async (formId: string) => {
  if (!formId) return
  
  fieldsLoading.value = true
  try {
    const response = await formApi.getTableInfoByFormId(formId)
    
    // 请求拦截器已经处理了状态码检查，直接使用 response.data
    const data = response.data
    
    
    // 解析字段信息并转换为前端格式
    if (data && data.columnInfos && Array.isArray(data.columnInfos)) {
      // 包含所有字段（系统字段和用户自定义字段）
      const allFields = data.columnInfos.map((column: any, index: number) => ({
        id: `field_${Date.now()}_${index}`,
        name: column.columnComment || column.columnName, // 使用注释作为显示名称，否则使用列名
        key: column.columnName,
        visible: true,
        sortable: true,
        filterable: true,
        type: mapFormTypeToFieldType(column.formType, column.columnType),
        width: undefined,
        isSystemField: column.isSysField === 1 // 标记是否为系统字段
      }))
      
      // 保存完整的可用字段列表
      availableFields.value = allFields
      
      // 默认只添加用户自定义字段到显示列表中
      const userFields = allFields.filter(field => !field.isSystemField)
      currentConfig.value.fields = [...userFields]
      
      const userFieldCount = userFields.length
      const systemFieldCount = allFields.length - userFieldCount
      ElMessage.success(`成功获取到 ${userFieldCount} 个用户字段，${systemFieldCount} 个系统字段`)
    } else {
      console.warn('数据格式不正确:', data)
      ElMessage.warning('未获取到有效的字段信息')
    }
  } catch (error) {
    console.error('获取表单字段失败:', error)
    console.error('错误类型:', typeof error)
    console.error('错误详情:', JSON.stringify(error, null, 2))
    
    // 更详细的错误信息
    let errorMessage = '获取表单字段失败'
    if (error && typeof error === 'object') {
      if (error.message) {
        errorMessage = error.message
      } else if (error.response && error.response.data && error.response.data.message) {
        errorMessage = error.response.data.message
      } else if (typeof error === 'string') {
        errorMessage = error
      }
    }
    
    ElMessage.error(errorMessage)
  } finally {
    fieldsLoading.value = false
  }
}

// 将表单字段类型映射为前端字段类型
const mapFormTypeToFieldType = (formType: string, columnType: string): 'text' | 'number' | 'date' | 'datetime' | 'select' | 'boolean' => {
  if (!formType) return 'text'
  
  // 优先使用 formType 进行映射
  switch (formType.toLowerCase()) {
    case 'number':
    case 'inputnumber':
      return 'number'
    case 'date':
      return 'date'
    case 'datetime':
    case 'datetimerange':
      return 'datetime'
    case 'select':
    case 'radio':
    case 'checkbox':
      return 'select'
    case 'switch':
      return 'boolean'
    case 'input':
    case 'textarea':
    default:
      return 'text'
  }
}

// 将数据库字段类型映射为前端字段类型（备用方法）
const mapColumnTypeToFieldType = (dataType: string): 'text' | 'number' | 'date' | 'datetime' | 'select' | 'boolean' => {
  if (!dataType) return 'text'
  
  const type = dataType.toLowerCase()
  
  if (type.includes('int') || type.includes('decimal') || type.includes('float') || type.includes('double')) {
    return 'number'
  } else if (type.includes('date')) {
    return type.includes('datetime') || type.includes('timestamp') ? 'datetime' : 'date'
  } else if (type.includes('bool') || type.includes('tinyint(1)')) {
    return 'boolean'
  } else if (type.includes('enum') || type.includes('set')) {
    return 'select'
  } else {
    return 'text'
  }
}

// 监听表单选择变化
watch(() => currentConfig.value.selectedFormId, async (newFormId) => {
  if (newFormId) {
    // 获取字段信息
    await getFormFieldsByFormId(newFormId)
    // 获取数据
    await getRealFormData()
  } else {
    // 清空字段列表和数据
    currentConfig.value.fields = []
    availableFields.value = []
    realData.value = []
    realTotal.value = 0
  }
})

// 获取实际的分页数据
const getRealFormData = async () => {
  if (!currentConfig.value.selectedFormId) {
    realData.value = []
    realTotal.value = 0
    return
  }

  realDataLoading.value = true
  try {
    // 构建筛选条件
    const criteria: Record<string, any> = {}
    
    // 将预览筛选条件转换为后端需要的格式
    configuredFilterFields.value.forEach(filter => {
      const filterValue = previewFilters.value[filter.fieldKey]
      if (filterValue) {
        switch (filter.operator) {
          case 'eq':
            criteria[filter.fieldKey] = filterValue
            break
          case 'contains':
            criteria[`${filter.fieldKey}_like`] = filterValue
            break
          case 'gt':
            criteria[`${filter.fieldKey}_gt`] = filterValue
            break
          case 'lt':
            criteria[`${filter.fieldKey}_lt`] = filterValue
            break
          case 'range':
            criteria[`${filter.fieldKey}_gte`] = filterValue
            const maxValue = previewFilters.value[`${filter.fieldKey}_max`]
            if (maxValue) {
              criteria[`${filter.fieldKey}_lte`] = maxValue
            }
            break
        }
      }
    })

    const response = await formApi.getFormDataPage({
      formId: currentConfig.value.selectedFormId,
      criteria: criteria,
      pageNo: currentPage.value,
      pageSize: currentConfig.value.pagination.pageSize
    })

    const data = response.data

    if (data && data.records) {
      realData.value = data.records
      // 确保 total 是数字类型
      realTotal.value = Number(data.total) || 0
    } else {
      realData.value = []
      realTotal.value = 0
    }
  } catch (error) {
    console.error('获取分页数据失败:', error)
    ElMessage.error('获取数据失败')
    realData.value = []
    realTotal.value = 0
  } finally {
    realDataLoading.value = false
  }
}

// 页面挂载时获取表单列表
onMounted(() => {
  getEnabledFormList()
})
</script>

<template>
  <el-config-provider :locale="zhCn">
    <div class="list-designer">
      <!-- 头部 -->
      <div class="designer-header">
        <div class="left">
          <el-input 
            v-model="currentConfig.name" 
            placeholder="未命名列表" 
            style="width: 300px" 
          />
        </div>
        <div class="right">
          <el-button @click="previewConfig">预览</el-button>
          <el-button type="primary" @click="saveConfig">保存</el-button>
        </div>
      </div>

      <!-- 主体内容 -->
      <div class="designer-body">
        <!-- 左侧预览区域 (70%) -->
        <div class="preview-area">
          <div class="preview-header">
            <h3>列表预览</h3>
          </div>
          <div class="preview-content">
            <!-- 系统按钮区域 -->
            <div v-if="currentConfig.systemButtons.some(b => b.visible)" class="system-buttons-section">
              <div class="system-buttons">
                <el-button
                  v-for="button in currentConfig.systemButtons.filter(b => b.visible)"
                  :key="button.id"
                  :size="button.size"
                  :disabled="button.disabled"
                  :style="getButtonStyle(button)"
                  @click="handleSystemButtonClick(button)"
                >
                  <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                  {{ button.name }}
                </el-button>
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
                  <!-- 日期类型字段 -->
                  <el-date-picker
                    v-if="filter.fieldType === 'date'"
                    v-model="previewFilters[filter.fieldKey]"
                    type="date"
                    :placeholder="`请选择${filter.fieldName}`"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    class="filter-input"
                    clearable
                  />
                  <!-- 日期时间类型字段 -->
                  <el-date-picker
                    v-else-if="filter.fieldType === 'datetime'"
                    v-model="previewFilters[filter.fieldKey]"
                    type="datetime"
                    :placeholder="`请选择${filter.fieldName}`"
                    format="YYYY-MM-DD HH:mm:ss"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    class="filter-input"
                    clearable
                  />
                  <!-- 范围类型的第二个日期时间选择器 -->
                  <el-date-picker
                    v-if="filter.operator === 'range' && (filter.fieldType === 'date' || filter.fieldType === 'datetime')"
                    v-model="previewFilters[`${filter.fieldKey}_max`]"
                    :type="filter.fieldType === 'date' ? 'date' : 'datetime'"
                    placeholder="结束时间"
                    :format="filter.fieldType === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm:ss'"
                    :value-format="filter.fieldType === 'date' ? 'YYYY-MM-DD' : 'YYYY-MM-DD HH:mm:ss'"
                    class="filter-input range-input"
                    clearable
                  />
                  <!-- 数字类型字段 -->
                  <el-input-number
                    v-else-if="filter.fieldType === 'number' && filter.operator !== 'range'"
                    v-model="previewFilters[filter.fieldKey]"
                    :placeholder="`请输入${filter.fieldName}`"
                    class="filter-input"
                    clearable
                  />
                  <!-- 数字范围类型 -->
                  <template v-else-if="filter.fieldType === 'number' && filter.operator === 'range'">
                    <el-input-number
                      v-model="previewFilters[filter.fieldKey]"
                      placeholder="最小值"
                      class="filter-input range-input"
                      clearable
                    />
                    <el-input-number
                      v-model="previewFilters[`${filter.fieldKey}_max`]"
                      placeholder="最大值"
                      class="filter-input range-input"
                      clearable
                    />
</template>
                  <!-- 布尔类型字段 -->
                  <el-select
                    v-else-if="filter.fieldType === 'boolean'"
                    v-model="previewFilters[filter.fieldKey]"
                    :placeholder="`请选择${filter.fieldName}`"
                    class="filter-input"
                    clearable
                  >
                    <el-option label="是" value="true" />
                    <el-option label="否" value="false" />
                  </el-select>
                  <!-- 选择类型字段 -->
                  <el-select
                    v-else-if="filter.fieldType === 'select'"
                    v-model="previewFilters[filter.fieldKey]"
                    :placeholder="`请选择${filter.fieldName}`"
                    class="filter-input"
                    clearable
                  >
                    <el-option label="启用" value="启用" />
                    <el-option label="禁用" value="禁用" />
                  </el-select>
                  <!-- 默认文本输入框 -->
                  <el-input
                    v-else
                    v-model="previewFilters[filter.fieldKey]"
                    :placeholder="`请输入${filter.fieldName}`"
                    class="filter-input"
                    clearable
                  />
                  <!-- 文本范围的第二个输入框 -->
                  <el-input
                    v-if="filter.operator === 'range' && (filter.fieldType === 'text' || !filter.fieldType)"
                    v-model="previewFilters[`${filter.fieldKey}_max`]"
                    placeholder="最大值"
                    class="filter-input range-input"
                    clearable
                  />
                </div>
                <div class="filter-actions">
                  <el-button
                    v-for="button in currentConfig.conditionButtons.filter(b => b.visible)"
                    :key="button.id"
                    :size="button.size"
                    :disabled="button.disabled"
                    :style="getButtonStyle(button)"
                    @click="button.action === 'reset' ? clearPreviewFilters() : handleSearch()"
                  >
                    <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                    {{ button.name }}
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 分页器（顶部） -->
            <div 
              v-if="currentConfig.pagination.position === 'top' || currentConfig.pagination.position === 'both'"
              :class="['pagination-top', `pagination-${currentConfig.pagination.align}`]"
            >
              <el-pagination
                background
                v-model:current-page="currentPage"
                v-model:page-size="currentConfig.pagination.pageSize"
                :page-sizes="[5, 10, 20, 50]"
                :total="totalItems"
                :layout="paginationLayout"
                @size-change="handlePageSizeChange"
                @current-change="handlePageChange"
              />
            </div>

            <!-- 简易列表 -->
            <div v-if="currentConfig.style === 'simple'" class="simple-list">
              <div 
                v-for="item in paginatedData" 
                :key="item.id" 
                class="simple-item"
              >
                <div class="item-header">
                  <h4 class="item-title">{{ item.title }}</h4>
                  <div class="item-actions">
                    <el-button
                      v-for="button in currentConfig.actionButtons.filter(b => b.visible)"
                      :key="button.id"
                      :size="button.size"
                      :disabled="button.disabled"
                      :style="getButtonStyle(button)"
                      class="action-btn"
                    >
                      <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                      {{ button.name }}
                    </el-button>
                  </div>
                </div>
                <p class="item-desc">{{ item.description }}</p>
                <div class="item-meta">
                  <span>创建时间: {{ item.createTime }}</span>
                </div>
              </div>
            </div>

            <!-- 海报列表 -->
            <div v-else-if="currentConfig.style === 'poster'" class="poster-list">
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
                        v-for="button in currentConfig.actionButtons.filter(b => b.visible)"
                        :key="button.id"
                        :size="button.size"
                        :disabled="button.disabled"
                        :style="getButtonStyle(button)"
                        class="action-btn"
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
            <div v-else-if="currentConfig.style === 'table'" class="table-list">
              <el-table
                v-loading="realDataLoading"
                :data="paginatedData"
                :stripe="currentConfig.striped"
                style="width: 100%"
                @row-click="handleRowClick"
                :row-style="currentConfig.rowClickAction !== 'none' ? { cursor: 'pointer' } : {}"
              >
                <el-table-column 
                  v-if="currentConfig.showIndex"
                  type="index" 
                  label="序号" 
                  width="60"
                  :index="(index: number) => (currentPage - 1) * currentConfig.pagination.pageSize + index + 1"
                />
                <el-table-column 
                  v-if="currentConfig.showSelection"
                  type="selection" 
                  width="55"
                />
                <el-table-column 
                  v-for="field in currentConfig.fields.filter(f => f.visible)"
                  :key="field.id"
                  :prop="field.key"
                  :label="field.name"
                  :width="field.width"
                  :sortable="field.sortable"
                />
                <el-table-column
                  v-if="currentConfig.showActionColumn && currentConfig.actionButtons.some(b => b.visible)"
                  label="操作"
                  width="160"
                  fixed="right"
                  align="center"
                >
                  <template #default="{ row }">
                    <div class="table-actions">
                      <el-button
                        v-for="button in currentConfig.actionButtons.filter(b => b.visible)"
                        :key="button.id"
                        :size="button.size"
                        :disabled="button.disabled"
                        :style="getButtonStyle(button)"
                        class="action-btn"
                      >
                        <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                        {{ button.name }}
                      </el-button>
                    </div>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 分页器（底部） -->
            <div 
              v-if="currentConfig.pagination.position === 'bottom' || currentConfig.pagination.position === 'both'"
              :class="['pagination-bottom', `pagination-${currentConfig.pagination.align}`]"
            >
              <el-pagination
                background
                v-model:current-page="currentPage"
                v-model:page-size="currentConfig.pagination.pageSize"
                :page-sizes="[5, 10, 20, 50]"
                :total="totalItems"
                :layout="paginationLayout"
                @size-change="handlePageSizeChange"
                @current-change="handlePageChange"
              />
            </div>
          </div>
        </div>

        <!-- 右侧配置区域 (30%) -->
        <div class="config-area">
          <div class="config-nav">
            <div 
              v-for="tab in [
                { key: 'basic', label: '基础设置' },
                { key: 'display', label: '显示设置' },
                { key: 'filter', label: '筛选设置' },
                { key: 'pagination', label: '分页设置' },
                { key: 'style', label: '风格设置' },
                { key: 'buttons', label: '按钮设置' }
              ]"
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
                  v-model="currentConfig.selectedFormId"
                  placeholder="请选择表单"
                  :loading="formListLoading"
                  clearable
                  filterable
                  style="width: 100%"
                >
                  <el-option
                    v-for="form in formList"
                    :key="form.id"
                    :label="form.formName"
                    :value="form.id"
                  >
                    <span style="float: left">{{ form.formName }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">
                      {{ form.formDescription || '无描述' }}
                    </span>
                  </el-option>
                </el-select>
              </div>
              <div class="form-item">
                <el-checkbox v-model="currentConfig.showIndex">显示序号</el-checkbox>
              </div>
              <div class="form-item">
                <el-checkbox v-model="currentConfig.showSelection">显示勾选框</el-checkbox>
              </div>
              <div class="form-item">
                <el-checkbox v-model="currentConfig.striped">隔行变色</el-checkbox>
              </div>
              <div class="form-item">
                <label>行点击事件</label>
                <el-radio-group v-model="currentConfig.rowClickAction">
                  <el-radio label="none">无操作</el-radio>
                  <el-radio label="view">查看详情</el-radio>
                  <el-radio label="edit">编辑记录</el-radio>
                  <el-radio label="custom">自定义</el-radio>
                </el-radio-group>
                <el-input
                  v-if="currentConfig.rowClickAction === 'custom'"
                  v-model="currentConfig.customRowClickAction"
                  placeholder="请输入自定义动作名称"
                  style="margin-top: 8px"
                  size="small"
                />
              </div>
            </div>

            <!-- 显示设置 -->
            <div v-if="activeTab === 'display'" class="config-panel">
              <h4>显示设置</h4>
              <div class="form-item">
                <el-checkbox v-model="currentConfig.showActionColumn">显示操作列</el-checkbox>
              </div>
              
              <!-- 字段管理 -->
              <div class="section">
                <div class="section-header">
                  <h5>字段管理</h5>
                  <div class="header-actions">
                    <span v-if="fieldsLoading" class="loading-text">正在获取字段...</span>
                    <el-button size="small" type="primary" @click="addField" :disabled="fieldsLoading">
                      <el-icon><Plus /></el-icon>
                      添加字段
                    </el-button>
                  </div>
                </div>
                <div class="field-list" v-loading="fieldsLoading">
                  <div v-if="currentConfig.fields.length === 0 && !fieldsLoading" class="empty-fields">
                    <p>暂无字段，请先选择表单或手动添加字段</p>
                  </div>
                  <div
                    v-for="(field, index) in currentConfig.fields"
                    :key="field.id"
                    class="field-item"
                    :class="{ disabled: !field.visible }"
                    draggable="true"
                    @dragstart="handleFieldDragStart(index)"
                    @dragover="handleFieldDragOver"
                    @drop="handleFieldDrop(index)"
                  >
                    <div class="drag-handle">
                      <el-icon><DCaret /></el-icon>
                    </div>
                    <div class="field-content">
                      <span class="field-name">{{ field.name }}</span>
                      <span class="field-separator">--</span>
                      <span class="field-type">{{ getFieldTypeLabel(field.type) }}</span>
                      <span v-if="field.isSystemField" class="system-badge">系统</span>
                    </div>
                    <div class="field-actions">
                      <el-button
                        size="small"
                        type="primary"
                        circle
                        @click.stop="editField(field, index)"
                      >
                        <el-icon><Edit /></el-icon>
                      </el-button>
                      <el-button
                        size="small"
                        type="danger"
                        circle
                        @click.stop="removeField(field.id)"
                      >
                        <el-icon><Delete /></el-icon>
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 筛选设置 -->
            <div v-if="activeTab === 'filter'" class="config-panel">
              <h4>筛选设置</h4>
              <div class="filter-toolbar">
                <el-button size="small" @click="addFilter">添加筛选条件</el-button>
              </div>
              <div class="filter-list">
                <div 
                  v-for="filter in currentConfig.filters" 
                  :key="filter.id"
                  class="filter-item"
                >
                  <el-select 
                    v-model="filter.field" 
                    placeholder="选择字段"
                    size="small"
                    style="width: 120px"
                  >
                    <el-option 
                      v-for="field in currentConfig.fields.filter(f => f.filterable)"
                      :key="field.id"
                      :label="field.name"
                      :value="field.key"
                    />
                  </el-select>
                  <el-select 
                    v-model="filter.operator" 
                    placeholder="条件"
                    size="small"
                    style="width: 100px"
                  >
                    <el-option 
                      v-for="op in operatorOptions"
                      :key="op.value"
                      :label="op.label"
                      :value="op.value"
                    />
                  </el-select>
                  <el-input 
                    v-model="filter.value" 
                    placeholder="值"
                    size="small"
                    style="width: 100px"
                  />
                  <el-input 
                    v-if="filter.operator === 'range'"
                    v-model="filter.value2" 
                    placeholder="最大值"
                    size="small"
                    style="width: 100px"
                  />
                  <el-button 
                    size="small" 
                    type="danger" 
                    @click="removeFilter(filter.id)"
                  >
                    删除
                  </el-button>
                </div>
              </div>
              <div v-if="currentConfig.filters.length === 0" class="empty-filters">
                <p>暂无筛选条件，点击"添加筛选条件"开始设置</p>
              </div>
            </div>

            <!-- 分页设置 -->
            <div v-if="activeTab === 'pagination'" class="config-panel">
              <h4>分页设置</h4>
              <div class="form-item">
                <label>每页条数</label>
                <el-input-number 
                  v-model="currentConfig.pagination.pageSize" 
                  :min="1" 
                  :max="100"
                  @change="handlePageSizeChange"
                />
              </div>
              <div class="form-item">
                <el-checkbox v-model="currentConfig.pagination.showSizeChanger">
                  显示每页条数选择器
                </el-checkbox>
              </div>
              <div class="form-item">
                <el-checkbox v-model="currentConfig.pagination.showQuickJumper">
                  显示快速跳转
                </el-checkbox>
              </div>
              <div class="form-item">
                <el-checkbox v-model="currentConfig.pagination.showTotal">
                  显示总数
                </el-checkbox>
              </div>
              <div class="form-item">
                <label>分页位置</label>
                <el-radio-group v-model="currentConfig.pagination.position">
                  <el-radio label="top">顶部</el-radio>
                  <el-radio label="bottom">底部</el-radio>
                  <el-radio label="both">顶部+底部</el-radio>
                </el-radio-group>
              </div>
              <div class="form-item">
                <label>分页对齐</label>
                <el-radio-group v-model="currentConfig.pagination.align">
                  <el-radio label="left">左侧</el-radio>
                  <el-radio label="center">中间</el-radio>
                  <el-radio label="right">右侧</el-radio>
                </el-radio-group>
              </div>
            </div>

            <!-- 风格设置 -->
            <div v-if="activeTab === 'style'" class="config-panel">
              <h4>风格设置</h4>
              <div class="form-item">
                <label>列表风格</label>
                <div class="style-selector">
                  <div
                    v-for="styleOption in [
                      {
                        value: 'simple',
                        name: '简约风格',
                        icon: '📋',
                        description: '纵向卡片布局，简洁明了',
                        preview: '简约清爽的卡片式布局'
                      },
                      {
                        value: 'poster',
                        name: '海报风格',
                        icon: '🖼️',
                        description: '横向图文布局，视觉丰富',
                        preview: '图片与内容并排的海报式布局'
                      },
                      {
                        value: 'table',
                        name: '表格列表',
                        icon: '📊',
                        description: '传统表格布局，信息密集',
                        preview: '经典的表格数据展示方式'
                      }
                    ]"
                    :key="styleOption.value"
                    :class="['style-option', { active: currentConfig.style === styleOption.value }]"
                    @click="currentConfig.style = styleOption.value as 'simple' | 'poster' | 'table'"
                  >
                    <div class="style-icon">{{ styleOption.icon }}</div>
                    <div class="style-content">
                      <div class="style-name">{{ styleOption.name }}</div>
                      <div class="style-description">{{ styleOption.description }}</div>
                      <div class="style-preview">{{ styleOption.preview }}</div>
                    </div>
                    <div class="style-indicator">
                      <el-icon v-if="currentConfig.style === styleOption.value" class="check-icon">
                        <Check />
                      </el-icon>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 按钮设置 -->
            <div v-if="activeTab === 'buttons'" class="config-panel">
              <h4>按钮设置</h4>

              <!-- 系统按钮 -->
              <div class="section">
                <div class="section-header">
                  <h5>系统按钮</h5>
                  <el-button size="small" type="success" @click="addSystemButton">
                    <el-icon><Plus /></el-icon>
                    添加按钮
                  </el-button>
                </div>
                <div class="button-cards">
                  <div
                    v-for="(button, index) in currentConfig.systemButtons"
                    :key="button.id"
                    class="button-card"
                    :class="{ disabled: !button.visible }"
                  >
                    <div class="button-display">
                      <el-button
                        :size="button.size"
                        :style="getButtonStyle(button)"
                        :disabled="button.disabled || !button.visible"
                      >
                        <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                        {{ button.name }}
                      </el-button>
                    </div>
                    <div class="button-actions">
                      <el-button
                        size="small"
                        @click="editButton(button, 'system', index)"
                      >
                        <el-icon><Edit /></el-icon>
                        编辑
                      </el-button>
                      <el-button
                        size="small"
                        @click="removeSystemButton(button.id)"
                      >
                        <el-icon><Delete /></el-icon>
                        删除
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 条件按钮 -->
              <div class="section">
                <div class="section-header">
                  <h5>条件按钮</h5>
                  <el-button size="small" type="primary" @click="addConditionButton">
                    <el-icon><Plus /></el-icon>
                    添加按钮
                  </el-button>
                </div>
                <div class="button-cards">
                  <div
                    v-for="(button, index) in currentConfig.conditionButtons"
                    :key="button.id"
                    class="button-card"
                    :class="{ disabled: !button.visible }"
                  >
                    <div class="button-display">
                      <el-button
                        :size="button.size"
                        :style="getButtonStyle(button)"
                        :disabled="button.disabled || !button.visible"
                      >
                        <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                        {{ button.name }}
                      </el-button>
                    </div>
                    <div class="button-actions">
                      <el-button
                        size="small"
                        @click="editButton(button, 'condition', index)"
                      >
                        <el-icon><Edit /></el-icon>
                        编辑
                      </el-button>
                      <el-button
                        size="small"
                        @click="removeConditionButton(button.id)"
                      >
                        <el-icon><Delete /></el-icon>
                        删除
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 操作按钮 -->
              <div class="section">
                <div class="section-header">
                  <h5>操作按钮</h5>
                  <el-button size="small" type="primary" @click="addActionButton">
                    <el-icon><Plus /></el-icon>
                    添加按钮
                  </el-button>
                </div>
                <div class="button-cards">
                  <div
                    v-for="(button, index) in currentConfig.actionButtons"
                    :key="button.id"
                    class="button-card"
                    :class="{ disabled: !button.visible }"
                  >
                    <div class="button-display">
                      <el-button
                        :size="button.size"
                        :style="getButtonStyle(button)"
                        :disabled="button.disabled || !button.visible"
                      >
                        <el-icon v-if="button.icon"><component :is="button.icon" /></el-icon>
                        {{ button.name }}
                      </el-button>
                    </div>
                    <div class="button-actions">
                      <el-button
                        size="small"
                        @click="editButton(button, 'action', index)"
                      >
                        <el-icon><Edit /></el-icon>
                        编辑
                      </el-button>
                      <el-button
                        size="small"
                        @click="removeActionButton(button.id)"
                      >
                        <el-icon><Delete /></el-icon>
                        删除
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 按钮编辑弹出框 -->
              <el-dialog
                v-model="buttonEditVisible"
                :title="`编辑${editingButtonType === 'condition' ? '条件' : editingButtonType === 'action' ? '操作' : '系统'}按钮`"
                width="500px"
                destroy-on-close
              >
                <div v-if="editingButton" class="button-edit-form">
                  <!-- 预览区域 -->
                  <div class="edit-preview">
                    <label>预览效果</label>
                    <div class="preview-area">
                      <el-button
                        :size="editingButton.size"
                        :style="getButtonStyle(editingButton)"
                        :disabled="editingButton.disabled"
                      >
                        <el-icon v-if="editingButton.icon"><component :is="editingButton.icon" /></el-icon>
                        {{ editingButton.name }}
                      </el-button>
                    </div>
                  </div>

                  <!-- 基础设置 -->
                  <div class="form-group">
                    <label>按钮名称</label>
                    <el-input
                      v-model="editingButton.name"
                      placeholder="请输入按钮名称"
                    />
                  </div>

                  <div class="form-row">
                    <div class="form-group">
                      <label>按钮颜色</label>
                      <el-color-picker
                        v-model="editingButton.color"
                        show-alpha
                        color-format="hex"
                      />
                    </div>
                    <div class="form-group">
                      <label>按钮风格</label>
                      <el-select v-model="editingButton.style">
                        <el-option
                          v-for="style in buttonStyleOptions"
                          :key="style.value"
                          :label="style.label"
                          :value="style.value"
                        />
                      </el-select>
                    </div>
                    <div class="form-group">
                      <label>按钮大小</label>
                      <el-select v-model="editingButton.size">
                        <el-option
                          v-for="size in buttonSizeOptions"
                          :key="size.value"
                          :label="size.label"
                          :value="size.value"
                        />
                      </el-select>
                    </div>
                  </div>

                  <div class="form-group">
                    <label>按钮图标</label>
                    <el-select
                      v-model="editingButton.icon"
                      placeholder="选择图标"
                      clearable
                    >
                      <el-option
                        v-for="icon in iconOptions"
                        :key="icon.value"
                        :label="icon.label"
                        :value="icon.value"
                      >
                        <span style="display: flex; align-items: center; gap: 8px;">
                          <el-icon v-if="icon.value"><component :is="icon.value" /></el-icon>
                          {{ icon.label }}
                        </span>
                      </el-option>
                    </el-select>
                  </div>

                  <div class="form-group">
                    <label>按钮动作</label>
                    <el-select
                      v-model="editingButton.action"
                      placeholder="选择动作"
                    >
                      <el-option
                        v-for="action in getButtonActionOptions()"
                        :key="action.value"
                        :label="action.label"
                        :value="action.value"
                      />
                    </el-select>
                  </div>

                  <div class="form-row">
                    <div class="form-group">
                      <el-checkbox v-model="editingButton.visible">显示按钮</el-checkbox>
                    </div>
                    <div class="form-group">
                      <el-checkbox v-model="editingButton.disabled">禁用状态</el-checkbox>
                    </div>
                  </div>
                </div>

                <template #footer>
                  <div class="dialog-footer">
                    <el-button @click="cancelButtonEdit">取消</el-button>
                    <el-button type="primary" @click="saveButtonEdit">保存</el-button>
                  </div>
                </template>
              </el-dialog>

            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 字段编辑弹出框 -->
    <el-dialog
      v-model="fieldEditVisible"
      title="编辑字段"
      width="500px"
      destroy-on-close
    >
      <div v-if="editingField" class="field-edit-form">
        <!-- 预览区域 -->
        <div class="edit-preview">
          <label>字段预览</label>
          <div class="preview-area">
            <div class="field-preview-card">
              <div class="field-name">{{ editingField.name }}</div>
              <div class="field-key">{{ editingField.key }}</div>
              <div class="field-meta">
                <span class="field-type">{{ getFieldTypeLabel(editingField.type) }}</span>
                <span v-if="editingField.sortable" class="field-tag">可排序</span>
                <span v-if="editingField.filterable" class="field-tag">可筛选</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 基础设置 -->
        <div class="form-group">
          <label>字段名称</label>
          <el-input
            v-model="editingField.name"
            placeholder="请输入字段名称"
          />
        </div>

        <div class="form-group">
          <label>字段Key</label>
          <el-input
            v-model="editingField.key"
            placeholder="请输入字段Key"
          />
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>字段类型</label>
            <el-select v-model="editingField.type">
              <el-option label="文本" value="text" />
              <el-option label="数字" value="number" />
              <el-option label="日期" value="date" />
              <el-option label="日期时间" value="datetime" />
              <el-option label="选择" value="select" />
              <el-option label="布尔" value="boolean" />
            </el-select>
          </div>
          <div class="form-group">
            <label>字段宽度</label>
            <el-input-number
              v-model="editingField.width"
              :min="50"
              :max="500"
              placeholder="自动"
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <el-checkbox v-model="editingField.visible">显示字段</el-checkbox>
          </div>
          <div class="form-group">
            <el-checkbox v-model="editingField.sortable">可排序</el-checkbox>
          </div>
          <div class="form-group">
            <el-checkbox v-model="editingField.filterable">可筛选</el-checkbox>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelFieldEdit">取消</el-button>
          <el-button type="primary" @click="saveFieldEdit">保存</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 字段选择弹出框 -->
    <el-dialog
      v-model="fieldSelectionVisible"
      title="选择要添加的字段"
      width="500px"
      destroy-on-close
    >
      <div class="field-selection">
        <div v-if="addableFields.length === 0" class="no-fields">
          <el-empty description="没有可添加的字段" />
        </div>
        <div v-else class="field-selection-list">
          <!-- 用户字段 -->
          <div v-if="addableUserFields.length > 0" class="field-section">
            <h4 class="section-title">用户字段</h4>
            <div
              v-for="field in addableUserFields"
              :key="field.key"
              class="field-selection-item"
              @click="addSelectedField(field)"
            >
              <div class="field-info">
                <div class="field-name">{{ field.name }}</div>
                <div class="field-key">{{ field.key }}</div>
              </div>
              <div class="field-type-badge user-field">
                {{ getFieldTypeLabel(field.type) }}
              </div>
              <el-icon class="add-icon"><Plus /></el-icon>
            </div>
          </div>
          
          <!-- 系统字段 -->
          <div v-if="addableSystemFields.length > 0" class="field-section">
            <h4 class="section-title">系统字段</h4>
            <div
              v-for="field in addableSystemFields"
              :key="field.key"
              class="field-selection-item system-field"
              @click="addSelectedField(field)"
            >
              <div class="field-info">
                <div class="field-name">{{ field.name }}</div>
                <div class="field-key">{{ field.key }}</div>
              </div>
              <div class="field-type-badge system-field">
                {{ getFieldTypeLabel(field.type) }}
              </div>
              <el-icon class="add-icon"><Plus /></el-icon>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="fieldSelectionVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
  </el-config-provider>
</template>

<style scoped>
.list-designer {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.designer-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.designer-body {
  flex: 1;
  display: flex;
  gap: 16px;
  padding: 16px;
  overflow: hidden;
  min-height: 0;
}

.preview-area {
  flex: 0 0 70%;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  display: flex;
  flex-direction: column;
  width: 70%;
  min-width: 0;
}

.preview-header {
  padding: 16px 20px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.preview-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.preview-content {
  flex: 1;
  padding: 20px;
  overflow: auto;
  min-width: 0;
}

/* 系统按钮区域 */
.system-buttons-section {
  margin-bottom: 20px;
  background: #fff;
  padding: 16px 0;
  border-bottom: 1px solid #e0e0e0;
}

.system-buttons {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

/* 筛选条件区域 */
.filter-section {
  margin-bottom: 20px;
  background: #fff;
  padding: 16px 0;
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

.config-area {
  flex: 0 0 30%;
  display: flex;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  overflow: hidden;
  width: 30%;
  min-width: 0;
}

.config-nav {
  width: 120px;
  background: var(--bg-tertiary, #f1f5f9);
  border-right: 1px solid var(--border-color, #e5e7eb);
}

.nav-item {
  padding: 16px 12px;
  cursor: pointer;
  border-bottom: 1px solid var(--border-color, #e5e7eb);
  font-size: 14px;
  color: var(--text-secondary, #6b7280);
  transition: all 0.2s;
}

.nav-item:hover {
  background: var(--bg-secondary, #f8fafc);
  color: var(--text-primary, #1f2937);
}

.nav-item.active {
  background: var(--primary-color, #6366f1);
  color: #fff;
}

.config-content {
  flex: 1;
  padding: 20px;
  overflow: auto;
}

.config-panel h4 {
  margin: 0 0 16px 0;
  font-size: 16px;
  color: #333;
}

.form-item {
  margin-bottom: 16px;
}

.form-item label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: #666;
}

/* 新的简约布局 */
.section {
  margin-bottom: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.section-header h5 {
  margin: 0;
  font-size: 14px;
  color: #333;
  font-weight: 600;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.loading-text {
  font-size: 12px;
  color: #409eff;
}

.empty-fields {
  text-align: center;
  color: #999;
  font-size: 12px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
  border: 1px dashed #ddd;
}

.drag-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.drag-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
  background: var(--bg-tertiary, #f1f5f9);
  border: 1px solid var(--border-color, #e5e7eb);
  border-radius: 6px;
  cursor: move;
  transition: all 0.2s;
}

.drag-item:hover {
  background: var(--bg-secondary, #f8fafc);
  border-color: var(--primary-color, #6366f1);
}

.drag-item:active {
  transform: scale(0.98);
}

.drag-handle {
  color: #999;
  cursor: grab;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
}

.drag-handle:active {
  cursor: grabbing;
}

.item-content {
  display: flex;
  align-items: center;
  gap: 6px;
  flex: 1;
}

.item-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}

.filter-toolbar {
  margin-bottom: 16px;
}

.filter-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.filter-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px;
}

.empty-filters {
  text-align: center;
  color: #999;
  font-size: 12px;
  padding: 20px;
}

/* 预览样式 */
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

.item-desc {
  margin: 0 0 8px 0;
  color: #666;
  font-size: 14px;
}

.item-meta {
  color: #999;
  font-size: 12px;
}

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

.table-list {
  width: 100%;
  overflow-x: auto;
}

.table-list .el-table {
  width: 100% !important;
  table-layout: fixed;
}

/* 风格选择器样式 */
.style-selector {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-top: 8px;
}

.style-option {
  display: flex;
  align-items: center;
  padding: 16px;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #fff;
  position: relative;
  overflow: hidden;
}

.style-option:hover {
  border-color: var(--primary-color, #6366f1);
  background: rgba(99, 102, 241, 0.05);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.15);
}

.style-option.active {
  border-color: var(--primary-color, #6366f1);
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.08) 0%, rgba(99, 102, 241, 0.05) 100%);
  box-shadow: 0 4px 16px rgba(99, 102, 241, 0.2);
}

.style-option.active::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color, #6366f1), var(--primary-hover, #4f46e5));
}

.style-icon {
  font-size: 32px;
  margin-right: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
  border-radius: 10px;
  border: 1px solid #dee2e6;
}

.style-option.active .style-icon {
  background: linear-gradient(135deg, var(--primary-color, #6366f1), var(--primary-hover, #4f46e5));
  border-color: var(--primary-color, #6366f1);
  transform: scale(1.1);
}

.style-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.style-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.style-option.active .style-name {
  color: var(--primary-color, #6366f1);
}

.style-description {
  font-size: 14px;
  color: #666;
  line-height: 1.4;
}

.style-preview {
  font-size: 12px;
  color: #999;
  font-style: italic;
  margin-top: 2px;
}

.style-option.active .style-preview {
  color: var(--primary-hover, #4f46e5);
}

.style-indicator {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.check-icon {
  color: var(--primary-color, #6366f1);
  font-size: 18px;
  animation: checkIn 0.3s ease;
}

@keyframes checkIn {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 操作按钮样式优化 */
.table-actions,
.item-actions,
.poster-actions {
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

.table-actions {
  justify-content: center;
  max-width: 160px;
}

.item-actions {
  justify-content: flex-end;
}

.poster-actions {
  justify-content: flex-end;
}

/* 当按钮过多时的处理 */
.table-actions .action-btn {
  flex-shrink: 1;
  min-width: 40px;
}

/* 针对不同按钮类型的间距优化 */
.action-btn + .action-btn {
  margin-left: 0 !important;
}

/* 表格操作列的响应式处理 */
@media (max-width: 1200px) {
  .table-actions {
    gap: 4px;
  }

  .table-actions .action-btn {
    min-width: 50px;
    font-size: 12px;
    padding: 4px 8px;
  }
}

/* 筛选控件样式优化 */
.filter-input.el-date-editor {
  width: 180px !important;
}

.filter-input.el-input-number {
  width: 180px !important;
}

.filter-input.el-select {
  width: 180px !important;
}

.range-input.el-date-editor {
  width: 160px !important;
}

.range-input.el-input-number {
  width: 120px !important;
}

/* 按钮配置样式 */
.button-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 16px;
  margin-top: 12px;
}

.button-card {
  background: #fff;
  border: 2px solid #e0e0e0;
  border-radius: 12px;
  padding: 16px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}


.button-card.disabled {
  opacity: 0.6;
  background: #f5f7fa;
}

.button-card.disabled::before {
  content: '已隐藏';
  position: absolute;
  top: 8px;
  right: 8px;
  background: #f56c6c;
  color: #fff;
  padding: 2px 8px;
  border-radius: 10px;
  font-size: 11px;
  z-index: 1;
}

.button-display {
  text-align: center;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  min-height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.button-actions {
  display: flex;
  justify-content: center;
  gap: 8px;
  opacity: 0;
  transform: translateY(-100%);
  transition: all 0.3s ease;
  pointer-events: none;
  position: absolute;
  bottom: 16px;
  left: 16px;
  right: 16px;
  height: 0;
  overflow: hidden;
}

.button-card:hover {
  border-color: var(--primary-color, #6366f1);
  box-shadow: 0 4px 16px rgba(99, 102, 241, 0.15);
  transform: translateY(-2px);
  padding-bottom: 56px; /* 为操作按钮留出空间 */
}

.button-card:hover .button-actions {
  opacity: 1;
  transform: translateY(0);
  pointer-events: auto;
  height: 32px;
}

/* 弹框编辑样式 */
.button-edit-form {
  padding: 8px 0;
}

.edit-preview {
  margin-bottom: 24px;
  text-align: center;
}

.edit-preview label {
  display: block;
  margin-bottom: 12px;
  font-weight: 600;
  color: #333;
}

.edit-preview .preview-area {
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 2px dashed #ddd;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #333;
  font-size: 14px;
}

.form-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.form-row .form-group {
  margin-bottom: 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

/* 字段列表样式 */
.field-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-top: 12px;
}

.field-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  transition: all 0.2s ease;
  cursor: move;
}

.field-item:hover {
  border-color: var(--primary-color, #6366f1);
  background: rgba(99, 102, 241, 0.05);
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.1);
}

.field-item.disabled {
  opacity: 0.6;
  background: #f5f7fa;
}

.field-item.disabled::after {
  content: '已隐藏';
  margin-left: auto;
  padding: 2px 8px;
  background: #f56c6c;
  color: #fff;
  border-radius: 10px;
  font-size: 11px;
}

.field-item .drag-handle {
  color: #999;
  cursor: grab;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  flex-shrink: 0;
}

.field-item .drag-handle:active {
  cursor: grabbing;
}

.field-content {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
}

.field-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.field-separator {
  color: #999;
  font-size: 12px;
}

.field-type {
  font-size: 12px;
  color: #666;
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 12px;
}

.field-actions {
  display: flex;
  gap: 4px;
  flex-shrink: 0;
}

/* 字段预览卡片 */
.field-preview-card {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e0e0e0;
}

.field-edit-form .field-preview-card .field-name {
  font-size: 18px;
  margin-bottom: 6px;
}

.field-edit-form .field-preview-card .field-key {
  margin-bottom: 12px;
  font-family: monospace;
  color: #666;
  font-size: 14px;
}

.field-meta {
  display: flex;
  gap: 8px;
  align-items: center;
}

.field-tag {
  padding: 2px 6px;
  background: var(--primary-color, #6366f1);
  color: #fff;
  border-radius: 10px;
  font-size: 11px;
}

/* 字段选择弹框样式 */
.field-selection {
  padding: 8px 0;
}

.no-fields {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
}

.field-selection-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-height: 400px;
  overflow-y: auto;
}

.field-selection-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  background: #f8f9fa;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
}

.field-selection-item:hover {
  background: rgba(99, 102, 241, 0.05);
  border-color: var(--primary-color, #6366f1);
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.1);
}

.field-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.field-selection-item .field-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.field-selection-item .field-key {
  font-size: 12px;
  color: #666;
  font-family: monospace;
}

.field-type-badge {
  padding: 4px 8px;
  background: var(--primary-color, #6366f1);
  color: #fff;
  border-radius: 12px;
  font-size: 11px;
  margin-right: 12px;
}

.add-icon {
  color: var(--primary-color, #6366f1);
  font-size: 18px;
  opacity: 0.6;
  transition: opacity 0.2s ease;
}

.field-selection-item:hover .add-icon {
  opacity: 1;
}

/* 字段分组样式 */
.field-section {
  margin-bottom: 20px;
}

.section-title {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  padding-bottom: 8px;
  border-bottom: 1px solid #e0e0e0;
}

/* 系统字段样式 */
.field-selection-item.system-field {
  background: #f8f8ff;
  border-color: #d4d4ff;
}

.field-selection-item.system-field:hover {
  background: rgba(75, 85, 255, 0.05);
  border-color: #4b55ff;
}

.field-type-badge.system-field {
  background: #6c757d;
}

.field-type-badge.user-field {
  background: var(--primary-color, #6366f1);
}

/* 字段管理列表中的系统字段标识 */
.system-badge {
  padding: 2px 6px;
  background: #6c757d;
  color: #fff;
  border-radius: 10px;
  font-size: 10px;
  margin-left: 8px;
}
</style>
