<template>
  <div class="config-header">
    <div class="header-info">
      <!-- 面包屑导航 -->
      <el-breadcrumb separator="/" class="header-breadcrumb">
        <el-breadcrumb-item>{{ appName }}</el-breadcrumb-item>
        <el-breadcrumb-item
          v-for="node in breadcrumbPath"
          :key="node.id"
        >
          {{ node.name }}
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="header-actions">
      <!-- 表单操作按钮 -->
      <template v-if="currentNode && currentNode.type === 'form'">
        <el-button type="primary" @click="$emit('edit-form')">
          <el-icon><Edit /></el-icon>
          设计表单
        </el-button>
        <el-button @click="$emit('view-form-data')">
          <el-icon><Document /></el-icon>
          查看数据
        </el-button>
        <el-button @click="$emit('form-settings')">
          <el-icon><Setting /></el-icon>
          表单设置
        </el-button>
      </template>
      <!-- 表格操作按钮 -->
      <template v-else-if="currentNode && currentNode.type === 'grid'">
        <el-button type="primary" @click="$emit('edit-grid')">
          <el-icon><Edit /></el-icon>
          表格设计
        </el-button>
      </template>
      <!-- 其他页面的保存按钮 -->
      <template v-else>
        <el-button v-if="showSaveButton" type="primary" @click="$emit('save')">
          <el-icon class="el-icon--left"><Select /></el-icon>
          保存
        </el-button>
      </template>
    </div>
  </div>
</template>

<script setup>
import { Edit, Document, Setting, Select } from '@element-plus/icons-vue'

defineProps({
  appName: {
    type: String,
    default: ''
  },
  breadcrumbPath: {
    type: Array,
    default: () => []
  },
  currentNode: {
    type: Object,
    default: null
  },
  showSaveButton: {
    type: Boolean,
    default: false
  }
})

defineEmits(['edit-form', 'view-form-data', 'form-settings', 'edit-grid', 'save'])
</script>

<style lang="scss" scoped>
.config-header {
  height: 60px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  flex-shrink: 0;

  .header-info {
    flex: 1;

    .header-breadcrumb {
      font-size: 14px;

      :deep(.el-breadcrumb__item) {
        .el-breadcrumb__inner {
          color: var(--text-secondary);
          font-weight: 400;

          &:hover {
            color: var(--primary-color);
          }
        }

        &:last-child .el-breadcrumb__inner {
          color: var(--text-primary);
          font-weight: 500;
        }
      }
    }
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }
}
</style>
