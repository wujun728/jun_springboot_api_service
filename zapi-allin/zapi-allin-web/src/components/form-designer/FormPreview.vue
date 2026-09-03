<template>
  <div class="form-preview">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      :label-width="currentFormConfig.labelWidth || '100px'"
      :label-position="currentFormConfig.labelPosition || 'right'"
      :size="currentFormConfig.size || 'default'"
      :disabled="currentFormConfig.disabled || false"
      :inline="currentFormConfig.inline || false"
    >
      <el-row :gutter="20">
        <el-col
          v-for="component in currentFormConfig.components"
          :key="component.id"
          :span="(component.span || 12) * 2"
        >
          <el-form-item
            :label="component.label"
            :prop="component.name"
            :required="component.required"
          >
            <FormComponentRenderer
              :component="component"
              v-model="formData[component.name]"
              :preview="true"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch } from 'vue'
import { useFormDesignerStore } from '@/stores/form/formDesigner'
import FormComponentRenderer from './FormComponentRenderer.vue'
import type { FormInstance, FormRules } from 'element-plus'

// 定义props
interface Props {
  formConfig?: any
  initialData?: Record<string, any> // 用于数据回显的初始数据
}

const props = withDefaults(defineProps<Props>(), {
  formConfig: undefined,
  initialData: () => ({})
})

const formDesignerStore = useFormDesignerStore()

// 表单引用
const formRef = ref<FormInstance>()

// 当前表单配置 - 优先使用props，否则使用store
const currentFormConfig = computed(() => {
  if (props.formConfig) {
    return props.formConfig
  }
  return formDesignerStore.currentForm
})

// 表单数据
const formData = reactive<Record<string, any>>({})

// 动态生成表单验证规则
const formRules = computed<FormRules>(() => {
  const rules: FormRules = {}
  const components = currentFormConfig.value.components || []
  
  components.forEach(component => {
    if (!component.name) return
    
    const fieldRules: any[] = []
    
    // 必填验证
    if (component.required) {
      fieldRules.push({
        required: true,
        message: `${component.label || component.name}不能为空`,
        trigger: ['blur', 'change']
      })
    }
    
    // 根据组件类型添加特定验证
    if (component.type === 'input' || component.type === 'textarea') {
      // 最小长度验证
      if (component.minLength) {
        fieldRules.push({
          min: component.minLength,
          message: `${component.label || component.name}长度不能少于${component.minLength}个字符`,
          trigger: 'blur'
        })
      }
      
      // 最大长度验证
      if (component.maxLength) {
        fieldRules.push({
          max: component.maxLength,
          message: `${component.label || component.name}长度不能超过${component.maxLength}个字符`,
          trigger: 'blur'
        })
      }
      
      // 正则验证
      if (component.pattern) {
        fieldRules.push({
          pattern: new RegExp(component.pattern),
          message: component.patternMessage || `${component.label || component.name}格式不正确`,
          trigger: 'blur'
        })
      }
    }
    
    // 数字类型验证
    if (component.type === 'number') {
      // 最小值验证
      if (component.min !== undefined && component.min !== null) {
        fieldRules.push({
          type: 'number',
          min: component.min,
          message: `${component.label || component.name}不能小于${component.min}`,
          trigger: 'blur'
        })
      }
      
      // 最大值验证
      if (component.max !== undefined && component.max !== null) {
        fieldRules.push({
          type: 'number',
          max: component.max,
          message: `${component.label || component.name}不能大于${component.max}`,
          trigger: 'blur'
        })
      }
    }
    
    // 邮箱验证
    if (component.type === 'input' && component.inputType === 'email') {
      fieldRules.push({
        type: 'email',
        message: '请输入正确的邮箱地址',
        trigger: 'blur'
      })
    }
    
    // URL验证
    if (component.type === 'input' && component.inputType === 'url') {
      fieldRules.push({
        type: 'url',
        message: '请输入正确的URL地址',
        trigger: 'blur'
      })
    }
    
    if (fieldRules.length > 0) {
      rules[component.name] = fieldRules
    }
  })
  
  return rules
})

// 初始化表单数据
const initFormData = () => {
  // 先清空现有数据
  Object.keys(formData).forEach(key => {
    delete formData[key]
  })
  
  const components = currentFormConfig.value.components || []
  components.forEach(component => {
    if (component.name) {
      // 优先使用传入的初始数据，否则使用默认值
      if (props.initialData && props.initialData[component.name] !== undefined) {
        formData[component.name] = props.initialData[component.name]
      } else {
        formData[component.name] = (component as any).defaultValue || ''
      }
    }
  })
  
  console.log('表单数据初始化完成:', formData)
}

// 监听组件变化和初始数据变化，重新初始化表单数据
watch([currentFormConfig, () => props.initialData], initFormData, { immediate: true, deep: true })

// 暴露获取表单数据的方法给父组件
const getFormData = () => {
  return { ...formData }
}

// 暴露表单验证方法给父组件
const validate = async (): Promise<boolean> => {
  if (!formRef.value) {
    console.warn('表单引用未初始化')
    return false
  }
  
  try {
    await formRef.value.validate()
    return true
  } catch (error) {
    console.error('表单验证失败:', error)
    return false
  }
}

// 重置表单验证
const resetValidation = () => {
  formRef.value?.clearValidate()
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
}

// 暴露方法给父组件
defineExpose({
  getFormData,
  validate,
  resetValidation,
  resetForm
})
</script>

<style scoped lang="scss">
.form-preview {
  padding: 20px;
  background: var(--bg-card);
  border-radius: 4px;
  
  .el-form {
    max-width: none;
  }
}
</style>
