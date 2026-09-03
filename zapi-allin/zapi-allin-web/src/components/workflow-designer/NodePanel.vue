<template>
  <div class="node-panel">
    <div class="node-categories">
      <div
        v-for="category in filteredCategories"
        :key="category.name"
        class="category-section"
      >
        <div class="category-title">
          <el-icon class="category-icon">
            <component :is="category.icon" />
          </el-icon>
          <span>{{ category.name }}</span>
        </div>

        <div class="nodes-grid">
          <div
            v-for="nodeType in category.nodes"
            :key="nodeType.type"
            class="node-card"
            draggable="true"
            @dragstart="handleDragStart($event, nodeType.type)"
            @dragend="handleDragEnd"
          >
            <div class="node-icon">
              <el-icon>
                <component :is="nodeType.icon" />
              </el-icon>
            </div>
            <div class="node-name">{{ nodeType.name }}</div>
            <div class="node-desc">{{ nodeType.description }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, defineProps } from 'vue'
import {
  VideoPlay, Check, Document, Connection, ArrowRight, Timer, User,
  Search, Folder, Bell, Share
} from '@element-plus/icons-vue'

const props = defineProps({
  searchText: {
    type: String,
    default: ''
  }
})

// 节点类型定义
interface NodeType {
  type: string
  name: string
  description: string
  icon: any
}

interface NodeCategory {
  name: string
  icon: any
  nodes: NodeType[]
}

// 响应式数据
const searchText = ref('')

// 标准节点分类（符合后端规范）
const nodeCategories: NodeCategory[] = [
  {
    name: '流程控制',
    icon: Folder,
    nodes: [
      {
        type: 'START',
        name: '开始',
        description: '流程开始节点',
        icon: VideoPlay
      },
      {
        type: 'END',
        name: '结束',
        description: '流程结束节点',
        icon: Check
      }
    ]
  },
  {
    name: '业务节点',
    icon: Document,
    nodes: [
      {
        type: 'APPROVAL',
        name: '审批',
        description: '需要人工审批的节点',
        icon: User
      },
      {
        type: 'CC',
        name: '抄送',
        description: '通知相关人员',
        icon: Bell
      },
      {
        type: 'CONDITIONAL_BRANCH',
        name: '条件分支',
        description: '根据条件进行分支',
        icon: Connection
      },
      {
        type: 'PARALLEL',
        name: '并行',
        description: '并行执行多个分支',
        icon: Share
      }
    ]
  }
]

// 计算属性
const filteredCategories = computed(() => {
  if (!searchText.value.trim()) {
    return nodeCategories
  }

  return nodeCategories.map(category => ({
    ...category,
    nodes: category.nodes.filter(node =>
      node.name.toLowerCase().includes(searchText.value.toLowerCase()) ||
      node.description.toLowerCase().includes(searchText.value.toLowerCase())
    )
  })).filter(category => category.nodes.length > 0)
})

// 方法
const handleDragStart = (event: DragEvent, nodeType: string) => {
  console.log('=== 开始拖拽节点 ===')
  console.log('节点类型:', nodeType)
  console.log('DataTransfer:', event.dataTransfer)

  if (event.dataTransfer) {
    event.dataTransfer.setData('text/plain', nodeType)
    event.dataTransfer.effectAllowed = 'copy'
    console.log('拖拽数据已设置')
  } else {
    console.error('DataTransfer 不可用')
  }
}

const handleDragEnd = (event: DragEvent) => {
  console.log('=== 拖拽结束 ===')
  console.log('拖拽效果:', event.dataTransfer?.dropEffect)
}
</script>

<style scoped lang="scss">
.node-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
}

.panel-header {
  padding: 16px;
  border-bottom: 1px solid var(--border-color, #e5e7eb);

  h3 {
    margin: 0 0 12px 0;
    font-size: 14px;
    color: var(--text-primary, #1f2937);
    font-weight: 600;
    background: transparent;
  }
}

.node-categories {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.category-section {
  margin-bottom: 20px;
}

.category-title {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary, #1f2937);
  background: transparent;
  border-left: 4px solid var(--primary-color, #6366f1);
  border-radius: 0 8px 8px 0;
  margin-bottom: 12px;
  box-shadow: none;

  .category-icon {
    color: var(--primary-color, #6366f1);
  }
}

.nodes-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.node-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 6px;
  background: var(--bg-card, #ffffff);
  border: 1px solid var(--border-color, #e5e7eb);
  border-radius: 8px;
  cursor: grab;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  user-select: none;
  box-shadow: var(--shadow-sm, 0 1px 2px 0 rgba(0, 0, 0, 0.05));

  &:hover {
    border-color: var(--primary-color, #6366f1);
    box-shadow: 0 4px 12px rgba(99, 102, 241, 0.2);
    transform: translateY(-2px);
  }

  &:active {
    cursor: grabbing;
    transform: scale(0.96);
  }
}

.node-icon {
  font-size: 24px;
  color: var(--primary-color, #6366f1);
  margin-bottom: 8px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.node-card:hover .node-icon {
  transform: scale(1.1);
  color: var(--primary-hover, #4f46e5);
}

.node-name {
  font-size: 12px;
  color: var(--text-primary, #1f2937);
  text-align: center;
  line-height: 1.2;
  font-weight: 500;
  padding: 0 4px;
  margin-bottom: 3px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.node-desc {
  font-size: 10px;
  color: var(--text-muted, #9ca3af);
  text-align: center;
  line-height: 1.1;
  padding: 0 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 拖拽状态 */
.node-card.dragging {
  opacity: 0.6;
  transform: rotate(3deg) scale(0.95);
  box-shadow: 0 8px 20px rgba(99, 102, 241, 0.3);
}

/* 滚动条样式 */
.node-categories::-webkit-scrollbar {
  width: 4px;
}

.node-categories::-webkit-scrollbar-track {
  background: transparent;
}

.node-categories::-webkit-scrollbar-thumb {
  background: var(--border-light, #d1d5db);
  border-radius: 2px;
}

.node-categories::-webkit-scrollbar-thumb:hover {
  background: var(--text-muted, #9ca3af);
}
</style>
