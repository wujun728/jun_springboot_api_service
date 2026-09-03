<template>
  <el-drawer
    v-model="visible"
    :show-close="false"
    :with-header="false"
    size="100%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :destroy-on-close="true"
    @close="handleClose"
  >
    <div class="form-designer-container">
      <!-- 顶部工具栏 -->
      <div class="designer-toolbar">
        <div class="toolbar-left">
          <el-input
            v-model="formDesignerStore.currentForm.name"
            placeholder="请输入表单名称"
            class="form-name-input"
            style="width: 300px"
          />
        </div>
        <div class="toolbar-right">
          <el-button @click="handleClose">
            <el-icon><Close /></el-icon>
            关闭
          </el-button>
          <el-button type="primary" @click="handleSave">
            <el-icon><Document /></el-icon>
            保存表单
          </el-button>
        </div>
      </div>

      <!-- 设计器主体 -->
      <div class="designer-main">
        <!-- 左侧组件面板 -->
        <div class="designer-left">
          <div class="panel-header">
            <h3>组件库</h3>
          </div>
          <div class="panel-content">
            <ComponentsPanel />
          </div>
        </div>

        <!-- 中间设计画布 -->
        <div class="designer-center">
          <div class="canvas-header">
            <span>设计画布</span>
            <el-tag v-if="formDesignerStore.currentForm.components.length > 0">
              {{ formDesignerStore.currentForm.components.length }} 个组件
            </el-tag>
            <el-tag v-else type="info">空画布</el-tag>
          </div>
          <DesignCanvas />
        </div>

        <!-- 右侧属性面板 -->
        <div class="designer-right">
          <div class="panel-header">
            <h3>属性配置</h3>
          </div>
          <div class="panel-content">
            <PropertiesPanel />
          </div>
        </div>
      </div>
    </div>
  </el-drawer>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Close, Document } from '@element-plus/icons-vue'
import { useFormDesignerStore } from '@/stores/form/formDesigner'
import ComponentsPanel from './ComponentsPanel.vue'
import DesignCanvas from './DesignCanvas.vue'
import PropertiesPanel from './PropertiesPanel.vue'
import { formApi } from '@/api'

const formDesignerStore = useFormDesignerStore()

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  formId: {
    type: [String, Number],
    default: null
  },
  formInfo: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'saved', 'closed'])

const visible = ref(props.modelValue)

// 监听 modelValue 变化
watch(() => props.modelValue, (newVal) => {
  visible.value = newVal
  if (newVal && props.formInfo) {
    loadFormConfig()
  }
})

// 监听 visible 变化，同步到父组件
watch(visible, (newVal) => {
  emit('update:modelValue', newVal)
})

// 加载表单配置
const loadFormConfig = () => {
  try {
    // 先清空设计器状态
    formDesignerStore.clearForm()
    
    if (props.formInfo.formConfig) {
      const formConfig = JSON.parse(props.formInfo.formConfig)
      formDesignerStore.importForm({
        id: props.formInfo.id,
        name: props.formInfo.formName,
        description: props.formInfo.formDescription,
        ...formConfig
      })
    } else {
      // 如果没有配置，只设置基本信息
      formDesignerStore.updateFormConfig({
        id: props.formInfo.id,
        name: props.formInfo.formName,
        description: props.formInfo.formDescription
      })
    }
  } catch (error) {
    console.error('加载表单配置失败:', error)
    ElMessage.error('加载表单配置失败')
  }
}

// 保存表单设计
const handleSave = async () => {
  if (!formDesignerStore.currentForm.name.trim()) {
    ElMessage.warning('请输入表单名称')
    return
  }

  if (formDesignerStore.currentForm.components.length === 0) {
    ElMessage.warning('表单内容不能为空')
    return
  }

  try {
    await ElMessageBox.confirm('确定保存表单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })

    // 构造保存数据
    const formData = {
      id: props.formId,
      formName: formDesignerStore.currentForm.name,
      formDescription: formDesignerStore.currentForm.description || '',
      formConfig: JSON.stringify({
        labelWidth: formDesignerStore.currentForm.labelWidth,
        labelPosition: formDesignerStore.currentForm.labelPosition,
        size: formDesignerStore.currentForm.size,
        disabled: formDesignerStore.currentForm.disabled,
        inline: formDesignerStore.currentForm.inline,
        components: formDesignerStore.currentForm.components,
        style: formDesignerStore.currentForm.style
      }),
      status: 1
    }

    // 调用更新接口
    const response = await formApi.updateForm(formData)
    if (response.code === 200) {
      ElMessage.success('表单保存成功')
      emit('saved', formData)
      handleClose()
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('保存表单失败:', error)
      ElMessage.error('保存表单失败')
    }
  }
}

// 关闭抽屉
const handleClose = () => {
  visible.value = false
  // 清空设计器状态
  formDesignerStore.clearForm()
  emit('closed')
}
</script>

<style lang="scss" scoped>
// 表单设计器容器
.form-designer-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: var(--bg-secondary);

  .designer-toolbar {
    height: 56px;
    background: var(--bg-card);
    border-bottom: 1px solid var(--border-color);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    flex-shrink: 0;

    .toolbar-left {
      display: flex;
      align-items: center;
      gap: 16px;

      .form-name-input {
        :deep(.el-input__inner) {
          border: 1px dashed var(--border-color);
          background: transparent;
          font-size: 16px;
          font-weight: 500;

          &:hover {
            border-color: var(--primary-color);
          }

          &:focus {
            border-color: var(--primary-color);
            border-style: solid;
          }
        }
      }
    }

    .toolbar-right {
      display: flex;
      gap: 12px;
    }
  }

  .designer-main {
    flex: 1;
    display: flex;
    overflow: hidden;

    .designer-left {
      width: 260px;
      background: var(--bg-card);
      border-right: 1px solid var(--border-color);
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .panel-header {
        padding: 16px;
        border-bottom: 1px solid var(--border-color);
        flex-shrink: 0;

        h3 {
          margin: 0;
          font-size: 14px;
          color: var(--text-primary);
        }
      }

      .panel-content {
        flex: 1;
        overflow-y: auto;
      }
    }

    .designer-center {
      flex: 1;
      background: var(--bg-card);
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .canvas-header {
        height: 50px;
        padding: 0 20px;
        border-bottom: 1px solid var(--border-color);
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex-shrink: 0;

        span {
          font-weight: 500;
          color: var(--text-primary);
        }
      }
    }

    .designer-right {
      width: 300px;
      background: var(--bg-card);
      border-left: 1px solid var(--border-color);
      display: flex;
      flex-direction: column;
      overflow: hidden;

      .panel-header {
        padding: 16px;
        border-bottom: 1px solid var(--border-color);
        flex-shrink: 0;

        h3 {
          margin: 0;
          font-size: 14px;
          color: var(--text-primary);
        }
      }

      .panel-content {
        flex: 1;
        overflow-y: auto;
      }
    }
  }
}

// 覆盖抽屉默认样式，移除内边距
:deep(.el-drawer__body) {
  padding: 0;
  overflow: hidden;
}
</style>