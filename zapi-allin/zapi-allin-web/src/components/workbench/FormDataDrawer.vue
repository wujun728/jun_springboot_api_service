<template>
  <el-drawer
    :model-value="modelValue"
    title="表单数据列表"
    size="80%"
    :close-on-click-modal="false"
    :destroy-on-close="true"
    @close="handleClose"
  >
    <div class="form-data-container" v-loading="loading">
      <!-- 数据表格 -->
      <el-table
        :data="dataList"
        :border="false"
        :stripe="true"
        style="width: 100%"
        class="simple-table"
      >
        <!-- 动态列 - 根据表单配置显示 -->
        <el-table-column
          v-for="component in formComponents"
          :key="component.name"
          :prop="component.name"
          :label="component.label"
          min-width="120"
        >
          <template #default="{ row }">
            <span>{{ row[component.name] || '-' }}</span>
          </template>
        </el-table-column>
        
        <!-- 操作列 -->
        <el-table-column
          label="操作"
          width="200"
          align="center"
          fixed="right"
        >
          <template #default="{ row }">
            <el-button type="primary" size="small" text @click="$emit('view-row', row)">查看</el-button>
            <el-button type="primary" size="small" text @click="$emit('edit-row', row)">编辑</el-button>
            <el-button type="danger" size="small" text @click="$emit('delete-row', row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="form-data-pagination">
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
    </div>
  </el-drawer>
</template>

<script setup>
const props = defineProps({
  // v-model 绑定
  modelValue: {
    type: Boolean,
    default: false
  },
  // 表单组件列表
  formComponents: {
    type: Array,
    default: () => []
  },
  // 数据列表
  dataList: {
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
  }
})

const emit = defineEmits(['update:modelValue', 'close', 'view-row', 'edit-row', 'delete-row', 'page-change'])

// 处理关闭事件
const handleClose = () => {
  emit('update:modelValue', false)
  emit('close')
}
</script>

<style lang="scss" scoped>
.form-data-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 20px;
  
  .simple-table {
    flex: 1;
    margin-bottom: 20px;
    
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
    
    :deep(.el-table__row--striped) {
      td {
        background: transparent;
      }
    }
    
    :deep(.el-table__inner-wrapper) {
      &::before {
        display: none;
      }
    }
  }
  
  .form-data-pagination {
    display: flex;
    justify-content: flex-end;
    padding: 16px 0;
    border-top: 1px solid #e5e7eb;
  }
}
</style>