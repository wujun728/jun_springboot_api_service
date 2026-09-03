<template>
  <div class="grid-preview-section">
    <div v-loading="loading" class="grid-preview-container">
      <!-- 表格为空的提示 -->
      <div v-if="!gridInfo" class="empty-form-tip">
        <el-empty description="表格内容为空">
          <template #description>
            <p class="empty-desc">该表格还没有设计任何内容</p>
            <p class="empty-hint">点击右上角"编辑表格"按钮开始设计表格</p>
          </template>
          <el-button type="primary" @click="$emit('edit')">
            <el-icon><Edit /></el-icon>
            立即设计表格
          </el-button>
        </el-empty>
      </div>

      <!-- 表格内容渲染 -->
      <div v-else class="grid-preview-content">
        <!-- 表格配置为空的提示 -->
        <div v-if="!gridConfig.fields || gridConfig.fields.length === 0" class="empty-form-tip">
          <el-empty description="表格配置为空">
            <template #description>
              <p class="empty-desc">该表格还没有配置任何字段</p>
              <p class="empty-hint">点击右上角"编辑表格"按钮配置表格字段</p>
            </template>
            <el-button type="primary" @click="$emit('edit')">
              <el-icon><Edit /></el-icon>
              立即配置表格
            </el-button>
          </el-empty>
        </div>

        <!-- 表格有配置时显示数据列表 -->
        <template v-else>
          <!-- 筛选条件区域 -->
          <div v-if="gridConfig.filters && gridConfig.filters.length > 0" class="grid-preview-filters">
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
                  @change="handleFilterChange"
                  style="width: 200px;"
                />
              </div>
              <el-button size="small" @click="handleFilterChange" type="primary">
                <el-icon><Search /></el-icon>
                查询
              </el-button>
              <el-button size="small" @click="handleClearFilters">
                <el-icon><RefreshLeft /></el-icon>
                重置
              </el-button>
            </div>
          </div>

          <div class="grid-preview-wrapper">
            <el-table
              :data="gridData"
              v-loading="dataLoading"
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
                <template #default="{ row }">
                  <template v-for="action in enabledActions" :key="action.id">
                    <el-button
                      :type="action.type"
                      size="small"
                      text
                      @click="handleActionClick(action.id, row)"
                    >
                      {{ action.label }}
                    </el-button>
                  </template>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页区域 -->
          <div class="grid-preview-pagination">
            <el-pagination
              v-model:current-page="pagination.current"
              v-model:page-size="pagination.size"
              :page-sizes="[10, 20, 50, 100]"
              :total="pagination.total"
              layout="total, sizes, prev, pager, next, jumper"
              @size-change="$emit('page-change')"
              @current-change="$emit('page-change')"
            />
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Edit, Search, RefreshLeft } from '@element-plus/icons-vue'

const props = defineProps({
  // 表格信息
  gridInfo: {
    type: Object,
    default: null
  },
  // 表格数据
  gridData: {
    type: Array,
    default: () => []
  },
  // 分页信息
  pagination: {
    type: Object,
    default: () => ({
      current: 1,
      size: 10,
      total: 0
    })
  },
  // 加载状态
  loading: {
    type: Boolean,
    default: false
  },
  // 数据加载状态
  dataLoading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['edit', 'view-row', 'edit-row', 'delete-row', 'page-change', 'filter-change'])

// 表格配置
const gridConfig = computed(() => {
  if (!props.gridInfo || !props.gridInfo.gridConfig) {
    return {
      selectedFormId: '',
      showIndex: true,
      showSelection: true,
      striped: true,
      showActionColumn: true,
      pageSize: 10,
      fields: [],
      filters: [],
      actions: [
        { id: 'view', label: '查看', type: 'primary', enabled: true },
        { id: 'edit', label: '编辑', type: 'primary', enabled: true },
        { id: 'delete', label: '删除', type: 'danger', enabled: true }
      ],
      tableName: ''
    }
  }

  try {
    const config = typeof props.gridInfo.gridConfig === 'string'
      ? JSON.parse(props.gridInfo.gridConfig)
      : props.gridInfo.gridConfig

    return {
      selectedFormId: config.selectedFormId || '',
      showIndex: config.showIndex !== false,
      showSelection: config.showSelection !== false,
      striped: config.striped !== false,
      showActionColumn: config.showActionColumn !== false,
      pageSize: config.pageSize || 10,
      fields: config.fields || [],
      filters: config.filters || [],
      actions: config.actions || [
        { id: 'view', label: '查看', type: 'primary', enabled: true },
        { id: 'edit', label: '编辑', type: 'primary', enabled: true },
        { id: 'delete', label: '删除', type: 'danger', enabled: true }
      ],
      tableName: config.tableName || ''
    }
  } catch (error) {
    console.error('解析表格配置失败:', error)
    return {
      selectedFormId: '',
      showIndex: true,
      showSelection: true,
      striped: true,
      showActionColumn: true,
      pageSize: 10,
      fields: [],
      filters: [],
      actions: [
        { id: 'view', label: '查看', type: 'primary', enabled: true },
        { id: 'edit', label: '编辑', type: 'primary', enabled: true },
        { id: 'delete', label: '删除', type: 'danger', enabled: true }
      ],
      tableName: ''
    }
  }
})

// 表格可见字段
const visibleFields = computed(() => {
  return gridConfig.value.fields?.filter(f => f.visible) || []
})

// 格式化表格字段值
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

// 启用的操作按钮
const enabledActions = computed(() => {
  return gridConfig.value.actions?.filter(a => a.enabled) || []
})

// 计算操作列宽度
const calculateActionColumnWidth = computed(() => {
  const count = enabledActions.value.length
  if (count === 0) return 100
  return Math.max(100, count * 70)
})

// 获取字段标签
const getFieldLabel = (fieldKey) => {
  const field = gridConfig.value.fields?.find(f => f.key === fieldKey)
  return field ? field.name : fieldKey
}

// 处理筛选条件变化
const handleFilterChange = () => {
  emit('filter-change', gridConfig.value.filters)
}

// 清空筛选条件
const handleClearFilters = () => {
  if (gridConfig.value.filters) {
    gridConfig.value.filters.forEach(filter => {
      filter.value = ''
    })
    emit('filter-change', gridConfig.value.filters)
  }
}

// 处理操作按钮点击
const handleActionClick = (actionId, row) => {
  switch (actionId) {
    case 'view':
      emit('view-row', row)
      break
    case 'edit':
      emit('edit-row', row)
      break
    case 'delete':
      emit('delete-row', row)
      break
    default:
      console.log('未知操作:', actionId)
  }
}
</script>

<style lang="scss" scoped>
.grid-preview-section {
  height: 100%;
  display: flex;
  flex-direction: column;

  .grid-preview-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .grid-preview-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .grid-preview-filters {
        background: var(--bg-card);
        border: 1px solid var(--border-color);
        border-radius: 4px;
        padding: 16px;
        margin: 0 24px 16px 24px;

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

      .empty-form-tip {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        background: var(--bg-card);
        border: 1px solid var(--border-color);
        border-radius: 12px;

        .empty-desc {
          font-size: 14px;
          color: var(--text-secondary);
          margin: 8px 0 4px 0;
        }

        .empty-hint {
          font-size: 13px;
          color: var(--text-tertiary);
          margin: 0 0 16px 0;
        }
      }

      .grid-preview-wrapper {
        flex: 1;
        background: var(--bg-card);
        padding: 24px;
        overflow-y: auto;

        // 简洁表格样式
        .simple-table {
          :deep(.el-table__header-wrapper) {
            .el-table__header {
              th {
                background: transparent;
                border: none;
                border-bottom: 1px solid #e5e7eb;
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
                  border-bottom: 1px solid #f0f0f0;
                  padding: 16px 0;

                  .cell {
                    color: var(--text-primary);
                    font-size: 14px;
                  }
                }

                &:hover {
                  td {
                    background: #f8f9fa;
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

      .grid-preview-pagination {
        background: var(--bg-card);
        border-top: 1px solid var(--border-color);
        padding: 16px 24px;
        display: flex;
        justify-content: flex-end;
        align-items: center;
        flex-shrink: 0;
        min-height: 60px;
      }
    }
  }
}
</style>
