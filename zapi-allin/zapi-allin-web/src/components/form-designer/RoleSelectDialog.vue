<template>
  <div class="role-select-wrapper">
    <!-- 触发按钮 -->
    <div class="select-trigger" @click="openDialog">
      <div class="selected-tags" v-if="selectedRoles.length">
        <el-tag
          v-for="role in selectedRoles"
          :key="role.id"
          :closable="!disabled"
          @close="removeRole(role)"
          size="small"
        >
          {{ role.roleName }}
        </el-tag>
      </div>
      <span v-else class="placeholder">{{ placeholder }}</span>
      <el-icon class="trigger-icon"><UserFilled /></el-icon>
    </div>

    <!-- 选择角色弹窗 -->
    <el-dialog
      title="选择角色"
      v-model="dialogVisible"
      width="500px"
      destroy-on-close
      :close-on-click-modal="false"
      append-to-body
    >
      <!-- 已选摘要 -->
      <div class="selected-summary" v-if="tempSelectedRoles.length">
        <div class="summary-title">已选 ({{ tempSelectedRoles.length }})：</div>
        <div class="summary-chips">
          <el-tag
            v-for="role in tempSelectedRoles"
            :key="role.id"
            type="info"
            closable
            @close="removeTempRole(role)"
            size="small"
          >
            {{ role.roleName }}
          </el-tag>
        </div>
      </div>
      <div class="selected-summary empty" v-else>未选择任何角色</div>

      <div class="selector-body" v-loading="loading">
        <el-input
          v-model="searchKeyword"
          placeholder="搜索角色"
          clearable
          style="margin-bottom: 12px;"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <div class="role-list">
          <el-checkbox-group v-model="tempSelectedIds" v-if="multiple">
            <el-checkbox
              v-for="role in filteredRoleList"
              :key="role.id"
              :label="role.id"
              @change="(checked) => handleRoleCheck(role, checked)"
            >
              <div class="role-item">
                <el-icon class="role-icon"><UserFilled /></el-icon>
                <span class="role-name">{{ role.roleName }}</span>
                <span class="role-code" v-if="role.roleCode">{{ role.roleCode }}</span>
              </div>
            </el-checkbox>
          </el-checkbox-group>
          
          <el-radio-group v-model="singleSelectedId" v-else>
            <el-radio
              v-for="role in filteredRoleList"
              :key="role.id"
              :label="role.id"
              @change="() => handleSingleSelect(role)"
            >
              <div class="role-item">
                <el-icon class="role-icon"><UserFilled /></el-icon>
                <span class="role-name">{{ role.roleName }}</span>
                <span class="role-code" v-if="role.roleCode">{{ role.roleCode }}</span>
              </div>
            </el-radio>
          </el-radio-group>
          
          <div class="empty-tip" v-if="!filteredRoleList.length">暂无角色数据</div>
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
import { UserFilled, Search } from '@element-plus/icons-vue'
import { roleApi } from '@/api/role'

interface RoleItem {
  id: number | string
  roleName: string
  roleCode?: string
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

const roleList = ref<RoleItem[]>([])
const selectedRoles = ref<RoleItem[]>([])
const tempSelectedRoles = ref<RoleItem[]>([])
const tempSelectedIds = ref<(number | string)[]>([])
const singleSelectedId = ref<number | string>('')

// 过滤后的角色列表
const filteredRoleList = computed(() => {
  if (!searchKeyword.value) return roleList.value
  const keyword = searchKeyword.value.toLowerCase()
  return roleList.value.filter(role => 
    role.roleName.toLowerCase().includes(keyword) ||
    (role.roleCode && role.roleCode.toLowerCase().includes(keyword))
  )
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

// 初始化已选角色
const initSelectedRoles = () => {
  const ids = parseModelValue(props.modelValue)
  if (ids.length === 0 || roleList.value.length === 0) {
    selectedRoles.value = []
    return
  }
  
  selectedRoles.value = roleList.value.filter(role => 
    ids.includes(String(role.id)) || ids.includes(role.id)
  )
}

// 监听 modelValue 变化
watch(() => props.modelValue, () => {
  initSelectedRoles()
})

// 监听角色列表加载完成
watch(() => roleList.value, () => {
  initSelectedRoles()
})

// 打开弹窗
const openDialog = async () => {
  if (props.disabled) return
  dialogVisible.value = true
  searchKeyword.value = ''
  tempSelectedRoles.value = [...selectedRoles.value]
  tempSelectedIds.value = tempSelectedRoles.value.map(r => r.id)
  
  if (!props.multiple && tempSelectedRoles.value.length > 0) {
    singleSelectedId.value = tempSelectedRoles.value[0].id
  } else {
    singleSelectedId.value = ''
  }
  
  if (roleList.value.length === 0) {
    await loadRoleList()
  }
}

// 加载角色列表
const loadRoleList = async () => {
  loading.value = true
  try {
    const res = await roleApi.getAllRoles()
    roleList.value = res.data || []
  } catch (error) {
    console.error('加载角色列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 多选模式：勾选变化
const handleRoleCheck = (role: RoleItem, checked: boolean) => {
  if (checked) {
    if (!tempSelectedRoles.value.find(r => r.id === role.id)) {
      tempSelectedRoles.value.push(role)
    }
  } else {
    tempSelectedRoles.value = tempSelectedRoles.value.filter(r => r.id !== role.id)
    tempSelectedIds.value = tempSelectedIds.value.filter(id => id !== role.id)
  }
}

// 单选模式：选择角色
const handleSingleSelect = (role: RoleItem) => {
  tempSelectedRoles.value = [role]
}

// 移除临时选中
const removeTempRole = (role: RoleItem) => {
  tempSelectedRoles.value = tempSelectedRoles.value.filter(r => r.id !== role.id)
  tempSelectedIds.value = tempSelectedIds.value.filter(id => id !== role.id)
  if (!props.multiple && singleSelectedId.value === role.id) {
    singleSelectedId.value = ''
  }
}

// 移除已选角色
const removeRole = (role: RoleItem) => {
  selectedRoles.value = selectedRoles.value.filter(r => r.id !== role.id)
  emitValue()
}

// 清空选择
const clearSelection = () => {
  tempSelectedRoles.value = []
  tempSelectedIds.value = []
  singleSelectedId.value = ''
}

// 确认选择
const confirmSelection = () => {
  selectedRoles.value = [...tempSelectedRoles.value]
  emitValue()
  dialogVisible.value = false
}

// 发送值
const emitValue = () => {
  // 构建包含 id 和 name 的对象数组
  const valueArray = selectedRoles.value.map(r => ({
    id: String(r.id),
    name: r.roleName
  }))
  emit('update:modelValue', valueArray)
  emit('change', valueArray)
}

onMounted(() => {
  loadRoleList()
})
</script>

<style scoped lang="scss">
.role-select-wrapper {
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
  height: 350px;
  display: flex;
  flex-direction: column;
}

.role-list {
  flex: 1;
  overflow: auto;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 8px;
  padding: 12px;
  
  :deep(.el-checkbox),
  :deep(.el-radio) {
    display: flex;
    align-items: center;
    margin: 0;
    padding: 10px 12px;
    border-radius: 6px;
    width: 100%;
    
    &:hover {
      background: var(--el-fill-color-light);
    }
  }
  
  :deep(.el-checkbox-group),
  :deep(.el-radio-group) {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }
  
  .role-item {
    display: flex;
    align-items: center;
    gap: 8px;
    
    .role-icon {
      color: var(--el-color-primary);
    }
    
    .role-name {
      font-weight: 500;
    }
    
    .role-code {
      color: var(--el-text-color-secondary);
      font-size: 12px;
      margin-left: auto;
    }
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
