<template>
  <div class="content-section form-preview-section">
    <div v-loading="loading" class="form-preview-container">
      <!-- 表单预览 -->
      <div v-if="formInfo" class="form-preview-content">
        <!-- 表单为空的提示 -->
        <div v-if="!formConfig.components || formConfig.components.length === 0" class="empty-form-tip">
          <el-empty description="表单内容为空">
            <template #description>
              <p class="empty-desc">该表单还没有设计任何内容</p>
              <p class="empty-hint">点击右上角"设计表单"按钮开始设计表单</p>
            </template>
            <el-button type="primary" @click="$emit('edit')">
              <el-icon><Edit /></el-icon>
              立即设计表单
            </el-button>
          </el-empty>
        </div>
        
        <!-- 表单有内容时显示预览 -->
        <template v-else>
          <div class="form-preview-wrapper">
            <FormPreview ref="formPreviewRef" :form-config="formConfig" />
          </div>
          
          <!-- 表单操作按钮 -->
          <div class="form-preview-actions">
            <el-button @click="handleReset">
              <el-icon><RefreshLeft /></el-icon>
              重置
            </el-button>
            <el-button type="primary" @click="handleSave" :loading="saveLoading">
              <el-icon><Select /></el-icon>
              保存
            </el-button>
          </div>
        </template>
      </div>

      <!-- 未找到表单信息 -->
      <el-empty v-else description="未找到关联的表单信息" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, RefreshLeft, Select } from '@element-plus/icons-vue'
import FormPreview from '@/components/form-designer/FormPreview.vue'
import { formApi } from '@/api'

const props = defineProps({
  formInfo: {
    type: Object,
    default: null
  },
  loading: {
    type: Boolean,
    default: false
  },
  appId: {
    type: String,
    default: null
  },
  entryId: {
    type: [String, Number],
    default: null
  },
  hasFlow: {
    type: Number,
    default: null
  }
})

const emit = defineEmits(['edit'])

const formPreviewRef = ref(null)
const saveLoading = ref(false)

// 表单预览配置
const formConfig = computed(() => {
  if (!props.formInfo || !props.formInfo.formConfig) {
    return {
      labelWidth: '100px',
      labelPosition: 'right',
      size: 'default',
      disabled: false,
      inline: false,
      components: [],
      style: {}
    }
  }
  
  try {
    const config = JSON.parse(props.formInfo.formConfig)
    return {
      labelWidth: config.labelWidth || '100px',
      labelPosition: config.labelPosition || 'right',
      size: config.size || 'default',
      disabled: config.disabled || false,
      inline: config.inline || false,
      components: config.components || [],
      style: config.style || {}
    }
  } catch (error) {
    console.error('解析表单配置失败:', error)
    return {
      labelWidth: '100px',
      labelPosition: 'right',
      size: 'default',
      disabled: false,
      inline: false,
      components: [],
      style: {}
    }
  }
})

// 保存表单数据
const handleSave = async () => {
  if (!formPreviewRef.value) {
    ElMessage.warning('表单未加载')
    return
  }

  const isValid = await formPreviewRef.value.validate()
  if (!isValid) {
    ElMessage.warning('请填写完整的表单信息')
    return
  }

  const formData = formPreviewRef.value.getFormData()
  console.log('准备保存的表单数据:', formData)

  saveLoading.value = true
  try {
    const requestData = {
      formId: props.formInfo.id,
      data: formData
    }
    
    // 如果传递了 appId 和 entryId，则添加到请求中
    if (props.appId) {
      requestData.appId = props.appId
    }
    if (props.entryId) {
      requestData.entryId = props.entryId
    }
    // 如果传递了 hasFlow，则添加到请求中
    if (props.hasFlow !== null && props.hasFlow !== undefined) {
      requestData.hasFlow = props.hasFlow
    }
    
    const response = await formApi.createFormData(requestData)
    
    if (response.code === 200) {
      ElMessage.success('保存成功')
      handleReset()
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    console.error('保存表单数据失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saveLoading.value = false
  }
}

// 重置表单数据
const handleReset = () => {
  if (formPreviewRef.value) {
    formPreviewRef.value.resetForm()
    ElMessage.success('已重置')
  }
}
</script>

<style lang="scss" scoped>
.form-preview-section {
  height: 100%;
  display: flex;
  flex-direction: column;

  .form-preview-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .form-preview-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .empty-form-tip {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: center;
        background: var(--bg-card);
        border: 1px solid var(--border-color);
        border-radius: 12px;

        .empty-desc {
          font-size: 14px;
          color: var(--text-secondary);
          margin: 8px 0 4px 0;
        }

        .empty-hint {
          font-size: 13px;
          color: var(--text-tertiary);
          margin: 0 0 16px 0;
        }
      }

      .form-preview-wrapper {
        flex: 1;
        background: var(--bg-card);
        padding: 24px;
        overflow-y: auto;
      }

      .form-preview-actions {
        background: var(--bg-card);
        border-top: 1px solid var(--border-color);
        padding: 16px 24px;
        display: flex;
        justify-content: flex-end;
        gap: 12px;
        flex-shrink: 0;
      }
    }
  }
}
</style>