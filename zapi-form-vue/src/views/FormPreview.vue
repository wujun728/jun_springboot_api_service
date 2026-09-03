<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useFormDesignerStore } from '../stores/formDesigner'
import FormComponentRenderer from '../components/FormComponentRenderer.vue'
import type { FormComponent } from '../types/form'

const route = useRoute()
const formDesignerStore = useFormDesignerStore()

const formData = ref<Record<string, any>>({})
const previewMode = ref('desktop') // desktop | tablet | mobile
const formRef = ref()
const isDev = ref(import.meta.env.DEV)

// 计算属性
const currentForm = computed(() => formDesignerStore.currentForm)
const components = computed(() => currentForm.value.components)

// 初始化表单数据
const initFormData = () => {
  const data: Record<string, any> = {}
  components.value.forEach((component: FormComponent) => {
    if (component.name) {
      // 根据组件类型设置初始值
      switch (component.type) {
        case 'checkbox':
          data[component.name] = []
          break
        case 'switch':
          data[component.name] = false
          break
        case 'rate':
          data[component.name] = 0
          break
        case 'slider':
          data[component.name] = 0
          break
        case 'input-number':
          data[component.name] = undefined
          break
        default:
          data[component.name] = ''
      }
    }
  })
  formData.value = data
}

// 表单提交
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    console.log('表单提交成功:', formData.value)
    ElMessage.success('表单提交成功')
  } catch (error) {
    console.error('表单验证失败:', error)
    ElMessage.error('请检查表单输入')
  }
}

// 重置表单
const handleReset = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  initFormData()
  ElMessage.info('表单已重置')
}

// 生成表单验证规则
const generateRules = () => {
  const rules: Record<string, any[]> = {}
  
  components.value.forEach((component: FormComponent) => {
    if (component.name && component.rules && component.rules.length > 0) {
      rules[component.name] = component.rules
    } else if (component.required) {
      // 如果组件是必填的但没有验证规则，添加默认规则
      rules[component.name] = [
        { required: true, message: `请输入${component.label}`, trigger: 'blur' }
      ]
    }
  })
  
  return rules
}

const rules = computed(() => generateRules())

// 组件挂载时初始化
onMounted(() => {
  initFormData()
})
</script>

<template>
  <div class="preview-container">
    <el-card>
      <template #header>
        <div class="preview-header">
          <div class="header-left">
            <span class="form-title">{{ currentForm.name || '表单预览' }}</span>
            <el-tag v-if="currentForm.description" type="info" size="small">
              {{ currentForm.description }}
            </el-tag>
          </div>
          <div class="preview-controls">
            <el-radio-group v-model="previewMode" size="small">
              <el-radio-button label="desktop">
                <el-icon><Monitor /></el-icon>
                桌面端
              </el-radio-button>
              <el-radio-button label="tablet">
                <el-icon><Iphone /></el-icon>
                平板端
              </el-radio-button>
              <el-radio-button label="mobile">
                <el-icon><Cellphone /></el-icon>
                移动端
              </el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </template>
      
      <div class="preview-content" :class="previewMode">
        <div class="form-preview">
          <!-- 空状态 -->
          <div v-if="components.length === 0" class="empty-form">
            <el-empty description="暂无表单内容">
              <el-button type="primary" @click="$router.push('/designer')">
                去设计表单
              </el-button>
            </el-empty>
          </div>
          
          <!-- 表单内容 -->
          <el-form
            v-else
            ref="formRef"
            :model="formData"
            :rules="rules"
            :label-width="currentForm.labelWidth || '120px'"
            :label-position="currentForm.labelPosition || 'right'"
            :size="currentForm.size || 'default'"
            :disabled="currentForm.disabled"
            :inline="currentForm.inline"
          >
            <!-- 动态渲染表单项 -->
            <el-form-item
              v-for="component in components"
              :key="component.id"
              :label="component.label"
              :prop="component.name"
              :required="component.required"
            >
              <FormComponentRenderer
                :component="component"
                v-model="formData[component.name]"
              />
            </el-form-item>
            
            <!-- 表单操作按钮 -->
            <el-form-item v-if="components.length > 0">
              <el-button type="primary" @click="handleSubmit">
                <el-icon><Check /></el-icon>
                提交表单
              </el-button>
              <el-button @click="handleReset">
                <el-icon><RefreshRight /></el-icon>
                重置表单
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      
      <!-- 调试信息（开发环境） -->
      <div v-if="isDev">
        <el-divider />
            <div class="debug-info">
          <h4>调试信息</h4>
              <h4>表单数据:</h4>
              <pre>{{ JSON.stringify(formData, null, 2) }}</pre>
              <h4>表单配置:</h4>
              <pre>{{ JSON.stringify(currentForm, null, 2) }}</pre>
            </div>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.preview-container {
  padding: 20px;
}

.preview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.form-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.preview-content {
  display: flex;
  justify-content: center;
  padding: 20px;
  transition: all 0.3s ease;
}

.form-preview {
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  min-height: 300px;
}

/* 响应式预览模式 */
.preview-content.desktop .form-preview {
  width: 800px;
  max-width: 100%;
}

.preview-content.tablet .form-preview {
  width: 600px;
  max-width: 100%;
}

.preview-content.mobile .form-preview {
  width: 375px;
  max-width: 100%;
}

.empty-form {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 200px;
}

.debug-info {
  background-color: #f8f9fa;
  padding: 16px;
  border-radius: 4px;
}

.debug-info h4 {
  margin: 0 0 8px 0;
  color: #666;
}

.debug-info pre {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 12px;
  font-size: 12px;
  overflow: auto;
  max-height: 200px;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .preview-content.desktop .form-preview,
  .preview-content.tablet .form-preview {
    width: 100%;
    padding: 20px;
  }
  
  .preview-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
  
  .preview-controls {
    width: 100%;
  }
  
  .preview-controls :deep(.el-radio-group) {
    width: 100%;
  }
  
  .preview-controls :deep(.el-radio-button) {
    flex: 1;
  }
}
</style>