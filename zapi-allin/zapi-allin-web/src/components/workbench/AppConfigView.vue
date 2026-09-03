<template>
  <div class="app-config-view">
    <!-- 左侧应用目录树 -->
    <aside class="config-sidebar">
      <!-- 任务中心部分 -->
      <div class="task-center-section">
        <div class="task-center-header">
          <span class="task-center-title">任务中心</span>
        </div>

        <div class="task-center-menu">
          <div
              v-for="item in taskCenterMenus"
              :key="item.key"
              class="task-menu-item"
              :class="{ active: activeTaskMenu === item.key }"
              @click="handleTaskMenuClick(item)"
          >
            <el-icon class="menu-icon">
              <component :is="item.icon" />
            </el-icon>
            <span class="menu-label">{{ item.label }}</span>
          </div>
        </div>
      </div>

      <!-- 应用目录部分 -->
      <div class="sidebar-section">
        <div class="sidebar-header">
          <span class="sidebar-title">应用目录</span>
          <el-button type="primary" size="small" @click="handleAddEntry">
            <el-icon><Plus /></el-icon>
          </el-button>
        </div>

        <div class="sidebar-content" v-loading="treeLoading">
          <el-tree
            ref="treeRef"
            :data="entryTree"
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
                    <Folder v-if="data.type === 'group'" />
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
      </div>


    </aside>

    <!-- 右侧内容区域 -->
    <div class="config-main">
      <slot></slot>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { Plus, Folder, Document, Grid, Edit, Delete, MoreFilled, EditPen, Clock, CircleCheck, Bell } from '@element-plus/icons-vue'

defineProps({
  appId: {
    type: String,
    required: true
  },
  entryTree: {
    type: Array,
    default: () => []
  },
  treeLoading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits([
  'add-entry',
  'node-click',
  'node-action',
  'node-drop',
  'allow-drop',
  'task-menu-click'
])

const treeRef = ref(null)
const treeProps = {
  children: 'children',
  label: 'name'
}

// 暴露方法给父组件
defineExpose({
  setCurrentKey: (key) => {
    if (treeRef.value) {
      treeRef.value.setCurrentKey(key)
    }
  }
})

// 任务中心菜单
const activeTaskMenu = ref('')
const taskCenterMenus = ref([
  {
    key: 'myApplications',
    label: '我的申请',
    icon: EditPen,
    path: '/home/taskcenter/all'
  },
  {
    key: 'myTasks',
    label: '我的待办',
    icon: Clock,
    path: '/home/taskcenter/mytask'
  },
  {
    key: 'myCompleted',
    label: '我的已办',
    icon: CircleCheck,
    path: '/home/taskcenter/mycompleted'
  },
  {
    key: 'ccMe',
    label: '抄送我的',
    icon: Bell,
    path: '/home/taskcenter/ccmy'
  }
])

const handleAddEntry = () => {
  emit('add-entry', null)
}

const handleNodeClick = (data) => {
  emit('node-click', data)
  // 清除任务中心菜单的选中状态
  activeTaskMenu.value = ''
}

const handleNodeAction = (command, data) => {
  emit('node-action', { command, data })
}

const handleNodeDrop = (draggingNode, dropNode, dropType, event) => {
  emit('node-drop', { draggingNode, dropNode, dropType, event })
}

const handleAllowDrop = (draggingNode, dropNode, type) => {
  if (type === 'inner') {
    return dropNode.data.type === 'group'
  }
  return true
}

// 处理任务中心菜单点击
const handleTaskMenuClick = (item) => {
  activeTaskMenu.value = item.key
  // 触发事件，让父组件处理任务中心视图的显示
  emit('task-menu-click', item)
}
</script>

<style lang="scss" scoped>
.app-config-view {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.config-sidebar {
  width: 260px;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-color);
  overflow: hidden;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;

  // 应用目录部分
  .sidebar-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    border-bottom: 1px solid var(--border-color);
  }

  .sidebar-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 16px;
    border-bottom: 1px solid var(--border-color);
    border-top: 1px solid var(--border-color);
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

  // 任务中心部分
  .task-center-section {
    flex-shrink: 0;
    display: flex;
    flex-direction: column;
    background: var(--bg-secondary);
  }

  .task-center-header {
    height: 60px;
    display: flex;
    align-items: center;
    padding: 0 16px;
    border-bottom: 1px solid var(--border-color);

    .task-center-title {
      font-size: 14px;
      font-weight: 600;
      color: var(--text-primary);
    }
  }

  .task-center-menu {
    padding: 8px;
  }

  .task-menu-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px 12px;
    margin-bottom: 4px;
    color: var(--text-secondary);
    font-size: 14px;
    cursor: pointer;
    border-radius: 6px;
    transition: all 0.2s;
    position: relative;

    &:hover {
      background: var(--bg-tertiary);
      color: var(--text-primary);
    }

    &.active {
      background: var(--primary-light);
      color: var(--primary-color);

      .menu-icon {
        color: var(--primary-color);
      }
    }

    .menu-icon {
      font-size: 18px;
      color: var(--text-secondary);
      flex-shrink: 0;
      transition: color 0.2s;
    }

    .menu-label {
      flex: 1;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    &:last-child {
      margin-bottom: 0;
    }
  }
}

.config-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--bg-primary);
}
</style>
