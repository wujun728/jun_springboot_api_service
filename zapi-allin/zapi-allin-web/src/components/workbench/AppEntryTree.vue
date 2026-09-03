<template>
  <aside class="config-sidebar">
    <div class="sidebar-header">
      <span class="sidebar-title">应用目录</span>
      <el-button type="primary" size="small" @click="handleAdd">
        <el-icon><Plus /></el-icon>
      </el-button>
    </div>

    <div class="sidebar-content" v-loading="loading">
      <el-tree
        ref="treeRef"
        :data="treeData"
        :props="treeProps"
        node-key="id"
        :default-expand-all="true"
        :expand-on-click-node="false"
        :highlight-current="true"
        :draggable="true"
        :allow-drop="handleAllowDrop"
        @node-click="handleNodeClick"
        @node-drop="handleNodeDrop"
        class="entry-tree"
      >
        <template #default="{ node, data }">
          <div class="tree-node">
            <div class="node-content">
              <el-icon class="node-icon" :style="{ color: data.color || '' }">
                <component :is="getNodeIcon(data)" v-if="data.icon" />
                <Folder v-else-if="data.type === 'group'" />
                <Document v-else-if="data.type === 'form'" />
                <Grid v-else-if="data.type === 'grid'" />
              </el-icon>
              <span class="node-label">{{ node.label }}</span>
            </div>
            <div class="node-actions" @click.stop>
              <el-dropdown trigger="click" @command="(cmd) => handleNodeAction(cmd, data)">
                <el-icon class="more-icon"><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="add" v-if="data.type === 'group'">
                      <el-icon><Plus /></el-icon>
                      添加子项
                    </el-dropdown-item>
                    <el-dropdown-item command="edit">
                      <el-icon><Edit /></el-icon>
                      编辑
                    </el-dropdown-item>
                    <el-dropdown-item command="delete" divided>
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </template>
      </el-tree>
    </div>
  </aside>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import { Plus, Folder, Document, Grid, Edit, Delete, MoreFilled } from '@element-plus/icons-vue'
import * as ElementPlusIcons from '@element-plus/icons-vue'

const props = defineProps({
  treeData: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits([
  'node-click',
  'node-drop',
  'add-entry',
  'edit-entry',
  'delete-entry'
])

const treeRef = ref(null)
const treeProps = {
  children: 'children',
  label: 'name'
}

// 获取节点图标组件
const getNodeIcon = (data) => {
  if (data.icon && ElementPlusIcons[data.icon]) {
    return ElementPlusIcons[data.icon]
  }
  return null
}

// 判断是否允许放置节点
const handleAllowDrop = (draggingNode, dropNode, type) => {
  if (type === 'inner') {
    return dropNode.data.type === 'group'
  }
  return true
}

// 节点点击
const handleNodeClick = (data) => {
  emit('node-click', data)
}

// 节点拖拽
const handleNodeDrop = (draggingNode, dropNode, dropType, event) => {
  emit('node-drop', { draggingNode, dropNode, dropType, event })
}

// 添加条目
const handleAdd = () => {
  emit('add-entry', null)
}

// 节点操作
const handleNodeAction = (command, data) => {
  switch (command) {
    case 'add':
      emit('add-entry', data)
      break
    case 'edit':
      emit('edit-entry', data)
      break
    case 'delete':
      emit('delete-entry', data)
      break
  }
}

// 自动选中第一个非分组节点
const autoSelectFirstNode = () => {
  if (!treeRef.value || props.treeData.length === 0) return

  const findFirstNonGroupNode = (nodes) => {
    for (const node of nodes) {
      if (node.type === 'form' || node.type === 'grid') {
        return node
      }
      if (node.type === 'group' && node.children && node.children.length > 0) {
        const found = findFirstNonGroupNode(node.children)
        if (found) return found
      }
    }
    return null
  }

  const firstNode = findFirstNonGroupNode(props.treeData)
  if (firstNode) {
    treeRef.value.setCurrentKey(firstNode.id)
    handleNodeClick(firstNode)
  }
}

// 监听树数据变化，自动选中第一个节点
watch(() => props.treeData, async (newVal) => {
  if (newVal && newVal.length > 0) {
    await nextTick()
    autoSelectFirstNode()
  }
}, { immediate: true })

// 暴露方法供父组件调用
defineExpose({
  setCurrentKey: (key) => treeRef.value?.setCurrentKey(key)
})
</script>

<style lang="scss" scoped>
.config-sidebar {
  width: 260px;
  background: var(--bg-card);
  border-right: 1px solid var(--border-color);
  overflow: hidden;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;

  .sidebar-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    border-bottom: 1px solid var(--border-color);
    flex-shrink: 0;

    .sidebar-title {
      font-size: 15px;
      font-weight: 600;
      color: var(--text-primary);
    }
  }

  .sidebar-content {
    flex: 1;
    overflow-y: auto;
    padding: 8px;
  }

  .entry-tree {
    background: transparent;

    :deep(.el-tree-node) {
      margin-bottom: 2px;

      .el-tree-node__content {
        height: 36px;
        border-radius: 6px;
        padding: 0 8px;
        transition: all 0.2s;

        &:hover {
          background: var(--bg-tertiary);
        }
      }

      &.is-current > .el-tree-node__content {
        background: var(--primary-light);
        color: var(--primary-color);
      }

      &.is-drop-inner > .el-tree-node__content {
        background: var(--primary-light);
        border: 2px dashed var(--primary-color);
      }
    }

    :deep(.el-tree__drop-indicator) {
      background-color: var(--primary-color);
      height: 2px;
    }

    .tree-node {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding-right: 8px;

      .node-content {
        display: flex;
        align-items: center;
        gap: 8px;
        flex: 1;
        min-width: 0;

        .node-icon {
          font-size: 16px;
          color: var(--text-secondary);
          flex-shrink: 0;
        }

        .node-label {
          font-size: 14px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      .node-actions {
        opacity: 0;
        transition: opacity 0.2s;

        .more-icon {
          font-size: 16px;
          color: var(--text-secondary);
          cursor: pointer;
          padding: 4px;
          border-radius: 4px;

          &:hover {
            background: var(--bg-card);
            color: var(--primary-color);
          }
        }
      }

      &:hover .node-actions {
        opacity: 1;
      }
    }
  }
}
</style>
