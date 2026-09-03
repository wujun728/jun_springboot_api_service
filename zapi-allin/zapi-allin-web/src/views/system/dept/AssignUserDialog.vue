<template>
  <el-dialog
    v-model="dialogVisible"
    title="分配用户"
    width="600px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form :model="form" label-width="80px" v-loading="loading">
      <el-form-item label="部门">
        <el-input v-model="currentDept.deptName" disabled />
      </el-form-item>
      <el-form-item label="选择用户">
        <el-select
          v-model="form.userIds"
          multiple
          filterable
          placeholder="请选择用户"
          style="width: 100%"
        >
          <el-option
            v-for="user in userList"
            :key="user.id"
            :label="`${user.realName} (${user.username})`"
            :value="user.id"
          />
        </el-select>
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { userOrgApi, userApi } from '@/api'
import { useUserStore } from '@/stores/user'

const props = defineProps({
  modelValue: Boolean,
  dept: Object
})

const emit = defineEmits(['update:modelValue', 'success'])

const dialogVisible = ref(false)
const loading = ref(false)
const submitting = ref(false)
const currentDept = ref({})
const userList = ref([])
const form = ref({
  userIds: []
})

// 监听 modelValue 变化
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
  if (val && props.dept) {
    currentDept.value = props.dept
    loadUsers()
    loadDeptUsers()
  }
})

// 监听 dialogVisible 变化
watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 加载所有用户
const userStore = useUserStore()

const loadUsers = async () => {
  try {
    // 使用缓存的用户数据
    userList.value = await userStore.loadEnabledUsers()
  } catch (error) {
    console.error('加载用户列表失败:', error)
    ElMessage.error('加载用户列表失败')
  }
}

// 加载部门已有的用户
const loadDeptUsers = async () => {
  loading.value = true
  try {
    const response = await userOrgApi.getUserIdsByOrgId(props.dept.id)
    if (response.code === 200) {
      form.value.userIds = response.data || []
    }
  } catch (error) {
    console.error('加载部门用户失败:', error)
    ElMessage.error('加载部门用户失败')
  } finally {
    loading.value = false
  }
}

// 提交
const handleSubmit = async () => {
  submitting.value = true
  try {
    const response = await userOrgApi.assignUsersToOrg(props.dept.id, form.value.userIds)
    
    if (response.code === 200) {
      ElMessage.success('分配用户成功')
      emit('success')
      handleClose()
    } else {
      ElMessage.error(response.message || '分配用户失败')
    }
  } catch (error) {
    console.error('分配用户失败:', error)
    ElMessage.error('分配用户失败')
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
  form.value.userIds = []
}
</script>

<style scoped lang="scss">
:deep(.el-select) {
  width: 100%;
}
</style>
