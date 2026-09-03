<template>
  <div class="department-select-wrapper">
    <!-- 触发按钮 -->
    <div class="select-trigger" @click="openDialog">
      <div class="selected-tags" v-if="selectedDepts.length">
        <el-tag
          v-for="dept in selectedDepts"
          :key="dept.id"
          :closable="!disabled"
          @close="removeDept(dept)"
          size="small"
        >
          {{ dept.deptName }}
        </el-tag>
      </div>
      <span v-else class="placeholder">{{ placeholder }}</span>
      <el-icon class="trigger-icon"><OfficeBuilding /></el-icon>
    </div>

    <!-- 选择部门弹窗 -->
    <el-dialog
      title="选择部门"
      v-model="dialogVisible"
      width="500px"
      destroy-on-close
      :close-on-click-modal="false"
      append-to-body
    >
      <!-- 已选摘要 -->
      <div class="selected-summary" v-if="tempSelectedDepts.length">
        <div class="summary-title">已选 ({{ tempSelectedDepts.length }})：</div>
        <div class="summary-chips">
          <el-tag
            v-for="dept in tempSelectedDepts"
            :key="dept.id"
            type="info"
            closable
            @close="removeTempDept(dept)"
            size="small"
          >
            {{ dept.deptName }}
          </el-tag>
        </div>
      </div>
      <div class="selected-summary empty" v-else>未选择任何部门</div>

      <div class="selector-body" v-loading="loading">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索部门"
          clearable
          style="margin-bottom: 12px;"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <div class="tree-wrapper">
          <el-tree
            ref="treeRef"
            :data="filteredDeptTree"
            node-key="id"
            :props="{ label: 'deptName', children: 'children' }"
            :show-checkbox="multiple"
            :check-strictly="true"
            :default-expand-all="true"
            :highlight-current="!multiple"
            :filter-node-method="filterNode"
            @check="handleCheck"
            @node-click="handleNodeClick"
          />
          <div class="empty-tip" v-if="!deptTree.length">暂无部门数据</div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button @click="clearSelection">清空</el-button>
          <el-button type="primary" @click="confirmSelection">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed, onMounted, nextTick } from 'vue'
import { OfficeBuilding, Search } from '@element-plus/icons-vue'
import { departmentApi } from '@/api/department'

interface DeptItem {
  id: number | string
  deptName: string
  parentId?: number | string
  children?: DeptItem[]
}

const props = defineProps<{
  modelValue?: string | string[]
  multiple?: boolean
  disabled?: boolean
  placeholder?: string
}>()

const emit = defineEmits(['update:modelValue', 'change'])

const dialogVisible = ref(false)
const loading = ref(false)
const searchKeyword = ref('')
const treeRef = ref()

const deptTree = ref<DeptItem[]>([])
const selectedDepts = ref<DeptItem[]>([])
const tempSelectedDepts = ref<DeptItem[]>([])

// 过滤后的部门树
const filteredDeptTree = computed(() => {
  if (!searchKeyword.value) return deptTree.value
  return filterTree(deptTree.value, searchKeyword.value.toLowerCase())
})

// 递归过滤树
const filterTree = (tree: DeptItem[], keyword: string): DeptItem[] => {
  return tree.filter(node => {
    const match = node.deptName.toLowerCase().includes(keyword)
    if (node.children && node.children.length) {
      const filteredChildren = filterTree(node.children, keyword)
      if (filteredChildren.length) {
        return true
      }
    }
    return match
  }).map(node => ({
    ...node,
    children: node.children ? filterTree(node.children, keyword) : undefined
  }))
}

// 过滤节点方法
const filterNode = (value: string, data: DeptItem) => {
  if (!value) return true
  return data.deptName.toLowerCase().includes(value.toLowerCase())
}

// 解析 modelValue
const parseModelValue = (value: any): any[] => {
  if (!value) return []
  if (Array.isArray(value)) {
    // 如果是对象数组 [{id, name}]，提取 id
    if (value.length > 0 && typeof value[0] === 'object') {
      return value.map(v => v.id || v)
    }
    return value
  }
  if (typeof value === 'string') {
    try {
      const parsed = JSON.parse(value)
      if (Array.isArray(parsed)) {
        // 如果是对象数组 [{id, name}]，提取 id
        if (parsed.length > 0 && typeof parsed[0] === 'object') {
          return parsed.map(v => v.id || v)
        }
        return parsed
      }
      return [value]
    } catch {
      return value.split(',').filter(Boolean)
    }
  }
  return [value]
}

// 从树中查找节点
const findNodeInTree = (tree: DeptItem[], id: number | string): DeptItem | null => {
  for (const node of tree) {
    if (String(node.id) === String(id)) return node
    if (node.children) {
      const found = findNodeInTree(node.children, id)
      if (found) return found
    }
  }
  return null
}

// 初始化已选部门
const initSelectedDepts = () => {
  const ids = parseModelValue(props.modelValue)
  if (ids.length === 0 || deptTree.value.length === 0) {
    selectedDepts.value = []
    return
  }
  
  selectedDepts.value = ids
    .map(id => findNodeInTree(deptTree.value, id))
    .filter(Boolean) as DeptItem[]
}

// 监听 modelValue 变化
watch(() => props.modelValue, () => {
  initSelectedDepts()
})

// 监听部门树加载完成
watch(() => deptTree.value, () => {
  initSelectedDepts()
})

// 打开弹窗
const openDialog = async () => {
  if (props.disabled) return
  dialogVisible.value = true
  searchKeyword.value = ''
  tempSelectedDepts.value = [...selectedDepts.value]
  
  if (deptTree.value.length === 0) {
    await loadDeptTree()
  }
  
  // 设置树的选中状态
  nextTick(() => {
    if (props.multiple && treeRef.value) {
      treeRef.value.setCheckedKeys(tempSelectedDepts.value.map(d => d.id))
    }
  })
}

// 加载部门树
const loadDeptTree = async () => {
  loading.value = true
  try {
    const res = await departmentApi.getDepartmentTree()
    deptTree.value = res.data || []
  } catch (error) {
    console.error('加载部门树失败:', error)
  } finally {
    loading.value = false
  }
}

// 多选模式：勾选变化
const handleCheck = (node: DeptItem, { checkedNodes }: any) => {
  tempSelectedDepts.value = checkedNodes
}

// 单选模式：点击节点
const handleNodeClick = (node: DeptItem) => {
  if (props.multiple) return
  tempSelectedDepts.value = [node]
}

// 移除临时选中
const removeTempDept = (dept: DeptItem) => {
  tempSelectedDepts.value = tempSelectedDepts.value.filter(d => d.id !== dept.id)
  if (props.multiple && treeRef.value) {
    treeRef.value.setCheckedKeys(tempSelectedDepts.value.map(d => d.id))
  }
}

// 移除已选部门
const removeDept = (dept: DeptItem) => {
  selectedDepts.value = selectedDepts.value.filter(d => d.id !== dept.id)
  emitValue()
}

// 清空选择
const clearSelection = () => {
  tempSelectedDepts.value = []
  if (props.multiple && treeRef.value) {
    treeRef.value.setCheckedKeys([])
  }
}

// 确认选择
const confirmSelection = () => {
  selectedDepts.value = [...tempSelectedDepts.value]
  emitValue()
  dialogVisible.value = false
}

// 发送值
const emitValue = () => {
  // 构建包含 id 和 name 的对象数组
  const valueArray = selectedDepts.value.map(d => ({
    id: String(d.id),
    name: d.deptName
  }))
  emit('update:modelValue', valueArray)
  emit('change', valueArray)
}

onMounted(() => {
  loadDeptTree()
})
</script>

<style scoped lang="scss">
.department-select-wrapper {
  width: 100%;
}

.select-trigger {
  display: flex;
  align-items: center;
  min-height: 32px;
  padding: 4px 8px;
  border: 1px solid var(--el-border-color);
  border-radius: 4px;
  cursor: pointer;
  background: var(--el-fill-color-blank);
  
  &:hover {
    border-color: var(--el-color-primary);
  }
  
  .selected-tags {
    flex: 1;
    display: flex;
    flex-wrap: wrap;
    gap: 4px;
  }
  
  .placeholder {
    flex: 1;
    color: var(--el-text-color-placeholder);
  }
  
  .trigger-icon {
    margin-left: 8px;
    color: var(--el-text-color-secondary);
  }
}

.selected-summary {
  padding: 12px;
  background: var(--el-fill-color-light);
  border-radius: 8px;
  margin-bottom: 16px;
  
  &.empty {
    color: var(--el-text-color-secondary);
    text-align: center;
  }
  
  .summary-title {
    font-weight: 500;
    margin-bottom: 8px;
  }
  
  .summary-chips {
    display: flex;
    flex-wrap: wrap;
    gap: 6px;
  }
}

.selector-body {
  height: 400px;
  display: flex;
  flex-direction: column;
}

.tree-wrapper {
  flex: 1;
  overflow: auto;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 8px;
  padding: 12px;
  
  :deep(.el-tree-node__content) {
    height: 36px;
    border-radius: 6px;
    
    &:hover {
      background: var(--el-fill-color-light);
    }
  }
  
  :deep(.el-tree-node.is-current > .el-tree-node__content) {
    background: var(--el-color-primary-light-9);
  }
}

.empty-tip {
  text-align: center;
  color: var(--el-text-color-secondary);
  padding: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>
