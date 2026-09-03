<template>
  <el-dialog
    :model-value="modelValue"
    :title="title"
    width="500px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
    >
      <el-form-item label="名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入名称" />
      </el-form-item>
      
      <el-form-item label="类型" prop="type">
        <el-select v-model="formData.type" placeholder="请选择类型" style="width: 100%">
          <el-option label="分组" value="group" />
          <el-option label="表单" value="form" />
          <el-option label="表格" value="grid" />
        </el-select>
      </el-form-item>

      <el-form-item label="图标" prop="icon">
        <IconSelector v-model="formData.icon" />
      </el-form-item>

      <el-form-item label="颜色" prop="color">
        <el-color-picker v-model="formData.color" />
      </el-form-item>

      <el-form-item label="流程表单" prop="hasFlow" v-if="formData.type === 'form'">
        <el-switch v-model="formData.hasFlow" :active-value="1" :inactive-value="0" />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
        确定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import IconSelector from '@/components/IconSelector/index.vue'

const props = defineProps({
  // v-model 绑定
  modelValue: {
    type: Boolean,
    default: false
  },
  // 对话框标题
  title: {
    type: String,
    default: '添加条目'
  },
  // 表单数据
  entryForm: {
    type: Object,
    default: () => ({
      id: null,
      name: '',
      type: 'group',
      parentId: null,
      appId: '',
      icon: '',
      color: '',
      sort: 0,
      hasFlow: 0
    })
  },
  // 提交加载状态
  submitLoading: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue', 'submit', 'close'])

// 表单引用
const formRef = ref(null)

// 表单数据（内部管理）
const formData = ref({
  id: null,
  name: '',
  type: 'group',
  parentId: null,
  appId: '',
  icon: '',
  color: '',
  sort: 0,
  hasFlow: 0
})

// 表单验证规则
const formRules = {
  name: [
    { required: true, message: '请输入名称', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择类型', trigger: 'change' }
  ]
}

// 监听 entryForm 变化，同步到内部 formData
watch(() => props.entryForm, (newVal) => {
  if (newVal) {
    formData.value = { ...newVal }
  }
}, { immediate: true, deep: true })

// 处理提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      emit('submit', formData.value)
    }
  })
}

// 处理关闭
const handleClose = () => {
  emit('update:modelValue', false)
  emit('close')
}
</script>

<style lang="scss" scoped>
// 可以在这里添加特定于对话框的样式
</style>