<template>
  <el-dialog
    v-model="dialogVisible"
    title="分配角色"
    width="500px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form :model="form" label-width="80px" v-loading="loading">
      <el-form-item label="用户">
        <el-input v-model="currentUser.realName" disabled />
      </el-form-item>
      <el-form-item label="选择角色">
        <el-checkbox-group v-model="form.roleIds">
          <el-checkbox
            v-for="role in roleList"
            :key="role.id"
            :label="role.id"
          >
            {{ role.roleName }}
          </el-checkbox>
        </el-checkbox-group>
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
import { userRoleApi } from '@/api'
import { roleApi } from '@/api'

const props = defineProps({
  modelValue: Boolean,
  user: Object
})

const emit = defineEmits(['update:modelValue', 'success'])

const dialogVisible = ref(false)
const loading = ref(false)
const submitting = ref(false)
const currentUser = ref({})
const roleList = ref([])
const form = ref({
  roleIds: []
})

// 监听 modelValue 变化
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
  if (val && props.user) {
    currentUser.value = props.user
    loadRoles()
    loadUserRoles()
  }
})

// 监听 dialogVisible 变化
watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 加载所有角色
const loadRoles = async () => {
  try {
    const response = await roleApi.getRoleList({ pageNum: 1, pageSize: 100 })
    if (response.code === 200) {
      roleList.value = response.data.records || response.data || []
    }
  } catch (error) {
    console.error('加载角色列表失败:', error)
    ElMessage.error('加载角色列表失败')
  }
}

// 加载用户已有的角色
const loadUserRoles = async () => {
  loading.value = true
  try {
    const response = await userRoleApi.getRoleIdsByUserId(props.user.id)
    if (response.code === 200) {
      form.value.roleIds = response.data || []
    }
  } catch (error) {
    console.error('加载用户角色失败:', error)
    ElMessage.error('加载用户角色失败')
  } finally {
    loading.value = false
  }
}

// 提交
const handleSubmit = async () => {
  submitting.value = true
  try {
    const response = await userRoleApi.assignRolesToUser(
      props.user.id,
      form.value.roleIds
    )
    if (response.code === 200) {
      ElMessage.success('分配角色成功')
      emit('success')
      handleClose()
    } else {
      ElMessage.error(response.message || '分配角色失败')
    }
  } catch (error) {
    console.error('分配角色失败:', error)
    ElMessage.error('分配角色失败')
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
  form.value.roleIds = []
}
</script>

<style scoped lang="scss">
:deep(.el-checkbox-group) {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
</style>
