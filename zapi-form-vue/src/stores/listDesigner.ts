import { defineStore } from 'pinia'
import { reactive } from 'vue'

export type ListStyle = 'simple' | 'poster' | 'table'

export interface ListField {
  id: string
  name: string
  key: string
  visible: boolean
  sortable: boolean
  filterable: boolean
  width?: number
}

export interface FilterRule {
  id: string
  field: string
  operator: 'eq' | 'contains' | 'range' | 'gt' | 'lt'
  value: string
  value2?: string
}

export interface PaginationConfig {
  pageSize: number
  showSizeChanger: boolean
  showQuickJumper: boolean
  showTotal: boolean
  position: 'top' | 'bottom' | 'both'
}

export interface ListConfig {
  name: string
  apiUrl: string
  showIndex: boolean
  showSelection: boolean
  striped: boolean
  style: ListStyle
  fields: ListField[]
  filters: FilterRule[]
  pagination: PaginationConfig
}

export const useListDesignerStore = defineStore('listDesigner', () => {
  const currentList = reactive<ListConfig>({
    name: '未命名列表',
    apiUrl: '',
    showIndex: true,
    showSelection: true,
    striped: true,
    style: 'simple',
    fields: [
      { id: '1', name: '标题', key: 'title', visible: true, sortable: true, filterable: true },
      { id: '2', name: '描述', key: 'description', visible: true, sortable: false, filterable: true },
      { id: '3', name: '创建时间', key: 'createTime', visible: true, sortable: true, filterable: true },
      { id: '4', name: '状态', key: 'status', visible: true, sortable: true, filterable: true }
    ],
    filters: [],
    pagination: {
      pageSize: 10,
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: true,
      position: 'bottom'
    }
  })

  const addField = () => {
    const id = Date.now().toString()
    currentList.fields.push({
      id,
      name: '新字段',
      key: `field_${id}`,
      visible: true,
      sortable: false,
      filterable: false
    })
  }

  const removeField = (id: string) => {
    const index = currentList.fields.findIndex(f => f.id === id)
    if (index > -1) {
      currentList.fields.splice(index, 1)
    }
  }

  const updateField = (id: string, updates: Partial<ListField>) => {
    const field = currentList.fields.find(f => f.id === id)
    if (field) {
      Object.assign(field, updates)
    }
  }

  const moveField = (fromIndex: number, toIndex: number) => {
    const field = currentList.fields.splice(fromIndex, 1)[0]
    currentList.fields.splice(toIndex, 0, field)
  }

  const addFilter = () => {
    const id = Date.now().toString()
    currentList.filters.push({
      id,
      field: '',
      operator: 'eq',
      value: ''
    })
  }

  const removeFilter = (id: string) => {
    const index = currentList.filters.findIndex(f => f.id === id)
    if (index > -1) {
      currentList.filters.splice(index, 1)
    }
  }

  const setStyle = (style: ListStyle) => {
    currentList.style = style
  }

  const updateConfig = (updates: Partial<ListConfig>) => {
    Object.assign(currentList, updates)
  }

  return {
    currentList,
    addField,
    removeField,
    updateField,
    moveField,
    addFilter,
    removeFilter,
    setStyle,
    updateConfig
  }
})
