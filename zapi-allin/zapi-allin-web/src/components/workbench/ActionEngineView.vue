<template>
  <div class="action-engine-view">
    <!-- 左侧动作列表 -->
    <aside class="action-sidebar">
      <div class="sidebar-header">
        <span class="sidebar-title">动作列表</span>
        <el-button type="primary" size="small" @click="handleAddAction">
          <el-icon><Plus /></el-icon>
        </el-button>
      </div>
      
      <div class="sidebar-content" v-loading="loading">
        <!-- 搜索框 -->
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索动作"
            clearable
            :prefix-icon="Search"
          />
        </div>

        <!-- 动作列表 -->
        <div class="action-list">
          <div
            v-for="action in filteredActions"
            :key="action.id"
            class="action-item"
            :class="{ 'is-active': selectedActionId === action.id }"
            @click="handleSelectAction(action)"
          >
            <div class="action-icon">
              <el-icon :size="20">
                <Operation />
              </el-icon>
            </div>
            <div class="action-info">
              <div class="action-name">{{ action.actionName }}</div>
              <div class="action-type">{{ action.actionType }}</div>
            </div>
            <div class="action-actions" @click.stop>
              <el-dropdown trigger="click" @command="(cmd) => handleActionCommand(cmd, action)">
                <el-icon class="more-icon"><MoreFilled /></el-icon>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="edit">
                      <el-icon><Edit /></el-icon>
                      编辑
                    </el-dropdown-item>
                    <el-dropdown-item command="copy">
                      <el-icon><DocumentCopy /></el-icon>
                      复制
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

          <!-- 空状态 -->
          <el-empty
            v-if="filteredActions.length === 0"
            description="暂无动作"
            :image-size="100"
          >
            <el-button type="primary" @click="handleAddAction">
              <el-icon><Plus /></el-icon>
              创建动作
            </el-button>
          </el-empty>
        </div>
      </div>
    </aside>

    <!-- 右侧动作设计器 -->
    <div class="action-designer-container">
      <div v-if="selectedAction" class="designer-wrapper">
        <!-- 设计器头部 -->
        <div class="designer-header">
          <div class="header-left">
            <el-input
              v-model="selectedAction.actionName"
              placeholder="动作名称"
              class="action-name-input"
            />
          </div>
          <div class="header-right">
            <el-button @click="handleTest">
              <el-icon><VideoPlay /></el-icon>
              测试
            </el-button>
            <el-button type="primary" @click="handleSave">
              <el-icon><SelectIcon /></el-icon>
              保存
            </el-button>
          </div>
        </div>

        <!-- 动作设计器 -->
        <div class="designer-content">
          <ActionDesigner
            v-model="selectedAction.actionConfig"
            @save="handleDesignerSave"
          />
        </div>
      </div>

      <!-- 未选择动作的空状态 -->
      <el-empty
        v-else
        description="请选择或创建一个动作"
        :image-size="200"
      >
        <el-button type="primary" @click="handleAddAction">
          <el-icon><Plus /></el-icon>
          创建动作
        </el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import {
  Plus,
  Search,
  Operation,
  Edit,
  Delete,
  MoreFilled,
  DocumentCopy,
  VideoPlay,
  Select as SelectIcon
} from '@element-plus/icons-vue'
import ActionDesigner from '../action-designer/ActionDesigner.vue'

const props = defineProps({
  actions: {
    type: Array,
    default: () => []
  },
  loading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['add', 'edit', 'delete', 'copy', 'save', 'test'])

const searchKeyword = ref('')
const selectedActionId = ref(null)
const selectedAction = ref(null)

// 过滤后的动作列表
const filteredActions = computed(() => {
  if (!searchKeyword.value) {
    return props.actions
  }
  return props.actions.filter(action =>
    action.actionName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 添加动作
const handleAddAction = () => {
  emit('add')
}

// 选择动作
const handleSelectAction = (action) => {
  selectedActionId.value = action.id
  // 解析 actionConfig
  let config = action.actionConfig
  if (typeof config === 'string') {
    try {
      config = JSON.parse(config)
    } catch (e) {
      config = { nodes: [], connections: [] }
    }
  }
  selectedAction.value = { 
    ...action,
    actionConfig: config || { nodes: [], connections: [] }
  }
}

// 动作操作
const handleActionCommand = (command, action) => {
  switch (command) {
    case 'edit':
      handleSelectAction(action)
      break
    case 'copy':
      emit('copy', action)
      break
    case 'delete':
      emit('delete', action)
      break
  }
}

// 测试动作
const handleTest = () => {
  emit('test', selectedAction.value)
}

// 保存动作
const handleSave = () => {
  emit('save', selectedAction.value)
}

// 设计器保存
const handleDesignerSave = (data) => {
  if (selectedAction.value) {
    selectedAction.value.actionConfig = JSON.stringify(data)
  }
}
</script>

<style lang="scss" scoped>
.action-engine-view {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.action-sidebar {
  width: 300px;
  background: var(--bg-secondary);
  border-right: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  flex-shrink: 0;

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
    display: flex;
    flex-direction: column;

    .search-box {
      height: 50px;
      display: flex;
      align-items: center;
      padding: 0 16px;
      border-bottom: 1px solid var(--border-color);
      flex-shrink: 0;
    }

    .action-list {
      flex: 1;
      overflow-y: auto;
      padding: 8px;

      .action-item {
        display: flex;
        align-items: center;
        gap: 12px;
        padding: 12px;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.2s;
        margin-bottom: 4px;

        &:hover {
          background: var(--bg-tertiary);

          .action-actions {
            opacity: 1;
          }
        }

        &.is-active {
          background: var(--primary-light);
          border: 1px solid var(--primary-color);
        }

        .action-icon {
          width: 40px;
          height: 40px;
          border-radius: 8px;
          background: var(--primary-light);
          display: flex;
          align-items: center;
          justify-content: center;
          color: var(--primary-color);
          flex-shrink: 0;
        }

        .action-info {
          flex: 1;
          min-width: 0;

          .action-name {
            font-size: 14px;
            font-weight: 500;
            color: var(--text-primary);
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .action-type {
            font-size: 12px;
            color: var(--text-secondary);
            margin-top: 2px;
          }
        }

        .action-actions {
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
      }
    }
  }
}

.action-designer-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--bg-primary);

  .designer-wrapper {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .designer-header {
      height: 60px;
      background: var(--bg-card);
      border-bottom: 1px solid var(--border-color);
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 24px;
      flex-shrink: 0;

      .header-left {
        display: flex;
        align-items: center;
        gap: 12px;

        .action-name-input {
          width: 300px;

          :deep(.el-input__inner) {
            border: 1px dashed var(--border-color);
            background: transparent;
            font-size: 16px;
            font-weight: 500;

            &:hover {
              border-color: var(--primary-color);
            }

            &:focus {
              border-color: var(--primary-color);
              border-style: solid;
            }
          }
        }
      }

      .header-right {
        display: flex;
        gap: 12px;
      }
    }

    .designer-content {
      flex: 1;
      overflow: hidden;
    }
  }
}
</style>
