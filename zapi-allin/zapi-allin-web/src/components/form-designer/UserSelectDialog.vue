<template>
  <div class="user-select-wrapper">
    <!-- 触发按钮 -->
    <div class="select-trigger" @click="openDialog">
      <div class="selected-tags" v-if="selectedUsers.length">
        <el-tag
          v-for="user in selectedUsers"
          :key="user.id"
          :closable="!disabled"
          @close="removeUser(user)"
          size="small"
        >
          {{ user.realName || user.username }}
        </el-tag>
      </div>
      <span v-else class="placeholder">{{ placeholder }}</span>
      <el-icon class="trigger-icon"><User /></el-icon>
    </div>

    <!-- 选人弹窗 -->
    <el-dialog
      title="选择人员"
      v-model="dialogVisible"
      width="720px"
      destroy-on-close
      :close-on-click-modal="false"
      append-to-body
    >
      <!-- 已选摘要 -->
      <div class="selected-summary" v-if="tempSelectedUsers.length">
        <div class="summary-title">已选 ({{ tempSelectedUsers.length }})：</div>
        <div class="summary-chips">
          <el-tag
            v-for="user in tempSelectedUsers"
            :key="user.id"
            type="info"
            closable
            @close="removeTempUser(user)"
            size="small"
          >
            {{ user.realName || user.username }}
          </el-tag>
        </div>
      </div>
      <div class="selected-summary empty" v-else>未选择任何人员</div>

      <div class="selector-body">
        <!-- 左侧部门树 -->
        <div class="selector-left">
          <div class="section-title">部门</div>
          <el-tree
            :data="deptTree"
            node-key="id"
            :props="{ label: 'deptName', children: 'children' }"
            highlight-current
            default-expand-all
            @node-click="handleDeptClick"
          />
          <div class="empty-tip" v-if="!deptTree.length">暂无部门数据</div>
        </div>

        <!-- 右侧用户列表 -->
        <div class="selector-right">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索用户（姓名/账号）"
            clearable
            @input="handleSearch"
            style="margin-bottom: 12px;"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>

          <div class="user-list" v-loading="loading">
            <el-checkbox-group v-model="tempSelectedIds">
              <el-checkbox
                v-for="user in userList"
                :key="user.id"
                :label="user.id"
                @change="(checked) => handleUserCheck(user, checked)"
              >
                <div class="user-item">
                  <el-avatar :size="28" :src="user.avatar">
                    {{ (user.realName || user.username || '').charAt(0) }}
                  </el-avatar>
                  <span class="user-name">{{ user.realName || user.username }}</span>
                  <span class="user-dept" v-if="user.deptName">{{ user.deptName }}</span>
                </div>
              </el-checkbox>
            </el-checkbox-group>
            <div class="empty-tip" v-if="!userList.length && !loading">暂无用户数据</div>
          </div>

          <!-- 分页 -->
          <div class="pagination-wrapper">
            <el-pagination
              layout="prev, pager, next"
              :current-page="pagination.current"
              :page-size="pagination.size"
              :total="pagination.total"
              small
              @current-change="handlePageChange"
            />
          </div>
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
import { ref, watch, computed, onMounted } from 'vue'
import { User, Search } from '@element-plus/icons-vue'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { departmentApi } from '@/api/department'

interface UserItem {
  id: number | string
  username?: string
  realName?: string
  avatar?: string
  deptName?: string
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
const currentDeptId = ref<number | null>(null)

const deptTree = ref<any[]>([])
const userList = ref<UserItem[]>([])
const selectedUsers = ref<UserItem[]>([])
const tempSelectedUsers = ref<UserItem[]>([])
const tempSelectedIds = ref<(number | string)[]>([])

const pagination = ref({
  current: 1,
  size: 20,
  total: 0
})

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

// 初始化已选用户
const userStore = useUserStore()

const initSelectedUsers = async () => {
  const ids = parseModelValue(props.modelValue)
  if (ids.length === 0) {
    selectedUsers.value = []
    return
  }
  
  // 获取用户详情
  try {
    // 使用缓存的用户数据
    const allUsers = await userStore.loadEnabledUsers()
    selectedUsers.value = allUsers.filter((u: UserItem) => ids.includes(String(u.id)) || ids.includes(u.id))
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}

// 监听 modelValue 变化
watch(() => props.modelValue, () => {
  initSelectedUsers()
}, { immediate: true })

// 打开弹窗
const openDialog = () => {
  if (props.disabled) return
  dialogVisible.value = true
  tempSelectedUsers.value = [...selectedUsers.value]
  tempSelectedIds.value = tempSelectedUsers.value.map(u => u.id)
  loadDeptTree()
  loadUserList()
}

// 加载部门树
const loadDeptTree = async () => {
  try {
    const res = await departmentApi.getDepartmentTree()
    deptTree.value = res.data || []
  } catch (error) {
    console.error('加载部门树失败:', error)
  }
}

// 加载用户列表
const loadUserList = async () => {
  loading.value = true
  try {
    const params: any = {
      current: pagination.value.current,
      size: pagination.value.size
    }
    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }
    if (currentDeptId.value) {
      params.orgId = currentDeptId.value
    }
    
    const res = await userApi.getUserList(params)
    userList.value = res.data?.records || []
    pagination.value.total = res.data?.total || 0
  } catch (error) {
    console.error('加载用户列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 点击部门
const handleDeptClick = (node: any) => {
  currentDeptId.value = node.id
  pagination.value.current = 1
  loadUserList()
}

// 搜索
const handleSearch = () => {
  pagination.value.current = 1
  loadUserList()
}

// 分页
const handlePageChange = (page: number) => {
  pagination.value.current = page
  loadUserList()
}

// 选中/取消用户
const handleUserCheck = (user: UserItem, checked: boolean) => {
  if (checked) {
    if (!props.multiple) {
      tempSelectedUsers.value = [user]
      tempSelectedIds.value = [user.id]
    } else {
      if (!tempSelectedUsers.value.find(u => u.id === user.id)) {
        tempSelectedUsers.value.push(user)
      }
    }
  } else {
    tempSelectedUsers.value = tempSelectedUsers.value.filter(u => u.id !== user.id)
    tempSelectedIds.value = tempSelectedIds.value.filter(id => id !== user.id)
  }
}

// 移除临时选中
const removeTempUser = (user: UserItem) => {
  tempSelectedUsers.value = tempSelectedUsers.value.filter(u => u.id !== user.id)
  tempSelectedIds.value = tempSelectedIds.value.filter(id => id !== user.id)
}

// 移除已选用户
const removeUser = (user: UserItem) => {
  selectedUsers.value = selectedUsers.value.filter(u => u.id !== user.id)
  emitValue()
}

// 清空选择
const clearSelection = () => {
  tempSelectedUsers.value = []
  tempSelectedIds.value = []
}

// 确认选择
const confirmSelection = () => {
  selectedUsers.value = [...tempSelectedUsers.value]
  emitValue()
  dialogVisible.value = false
}

// 发送值
const emitValue = () => {
  // 构建包含 id 和 name 的对象数组
  const valueArray = selectedUsers.value.map(u => ({
    id: String(u.id),
    name: u.realName || u.username
  }))
  emit('update:modelValue', valueArray)
  emit('change', valueArray)
}

onMounted(() => {
  initSelectedUsers()
})
</script>

<style scoped lang="scss">
.user-select-wrapper {
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
  display: flex;
  gap: 16px;
  height: 400px;
}

.selector-left {
  width: 200px;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 8px;
  padding: 12px;
  overflow: auto;
  
  .section-title {
    font-weight: 500;
    margin-bottom: 12px;
    color: var(--el-text-color-primary);
  }
}

.selector-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 8px;
  padding: 12px;
  
  .user-list {
    flex: 1;
    overflow: auto;
    
    :deep(.el-checkbox) {
      display: flex;
      align-items: center;
      margin: 0;
      padding: 8px;
      border-radius: 6px;
      
      &:hover {
        background: var(--el-fill-color-light);
      }
    }
  }
  
  .user-item {
    display: flex;
    align-items: center;
    gap: 8px;
    
    .user-name {
      font-weight: 500;
    }
    
    .user-dept {
      color: var(--el-text-color-secondary);
      font-size: 12px;
    }
  }
  
  .pagination-wrapper {
    padding-top: 12px;
    border-top: 1px solid var(--el-border-color-lighter);
    display: flex;
    justify-content: center;
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
