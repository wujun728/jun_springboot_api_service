<template>
  <div class="form-designer">
    <!-- 顶部工具栏 -->
    <div class="designer-header">
      <div class="header-left">
        <h2>{{ isEditMode ? '编辑表单' : '新建表单' }}</h2>
        <el-input
          v-model="formDesignerStore.currentForm.name"
          placeholder="请输入表单名称"
          class="form-name-input"
          @blur="updateFormName"
        />
      </div>
      <div class="header-right">
        <el-button type="primary" @click="previewForm">
          <el-icon><View /></el-icon>
          预览表单
        </el-button>
        <el-button type="success" @click="saveForm">
          <el-icon><Document /></el-icon>
          {{ isEditMode ? '更新表单' : '保存表单' }}
        </el-button>
        <el-button @click="clearForm">
          <el-icon><Delete /></el-icon>
          清空画布
        </el-button>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="designer-body">
      <!-- 左侧组件面板 -->
      <div class="designer-left">
        <div class="components-panel">
          <div class="panel-header">
            <h3>组件库</h3>
            <el-input
              v-model="componentSearch"
              placeholder="搜索组件..."
              prefix-icon="Search"
              size="small"
              clearable
            />
          </div>
          <div class="panel-content">
            <ComponentsPanel
              :use-external-search="true"
              :external-search-text="componentSearch"
            />
          </div>
        </div>
      </div>

      <!-- 中间设计画布 -->
      <div class="designer-center">
        <div class="canvas-container">
          <div class="canvas-header">
            <span>设计画布</span>
            <el-tag v-if="formDesignerStore.currentForm.components.length > 0">
              {{ formDesignerStore.currentForm.components.length }} 个组件
            </el-tag>
            <el-tag v-else type="info">空画布</el-tag>
          </div>
          <DesignCanvas />
        </div>
      </div>

      <!-- 右侧属性面板 -->
      <div class="designer-right">
        <div class="properties-panel">
          <div class="panel-header">
            <h3>属性配置</h3>
          </div>
          <div class="panel-content">
            <PropertiesPanel />
          </div>
        </div>
      </div>
    </div>

    <!-- 预览抽屉 -->
    <el-drawer
      v-model="previewDrawerVisible"
      title="表单预览"
      size="60%"
      direction="rtl"
    >
      <FormPreview ref="formPreviewRef" />
      <template #footer>
        <div class="preview-footer">
          <el-button @click="previewDrawerVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitForm">
            <el-icon><Document /></el-icon>
            提交表单
          </el-button>
        </div>
      </template>
    </el-drawer>

  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { View, Document, Delete, Edit } from '@element-plus/icons-vue'
import { useFormDesignerStore } from '@/stores/form/formDesigner'
import ComponentsPanel from '@/components/form-designer/ComponentsPanel.vue'
import DesignCanvas from '@/components/form-designer/DesignCanvas.vue'
import PropertiesPanel from '@/components/form-designer/PropertiesPanel.vue'
import FormPreview from '@/components/form-designer/FormPreview.vue'


const formDesignerStore = useFormDesignerStore()

// 抽屉状态
const previewDrawerVisible = ref(false)

// 组件库搜索
const componentSearch = ref('')

// 表单预览组件引用
const formPreviewRef = ref()

// 表单ID（从URL参数获取）
const formId = ref<string>('')
const isEditMode = computed(() => !!formId.value)

// 获取URL参数
const getUrlParams = () => {
  const urlParams = new URLSearchParams(window.location.search)
  return urlParams.get('id') || ''
}

// 加载表单数据
const loadFormData = async (id: string) => {
  try {
    const { formApi } = await import('@/api')
    const response = await formApi.getFormDetail(id)
    const formData = response.data

    // 解析表单配置并导入到设计器
    if (formData.formConfig) {
      const formConfig = JSON.parse(formData.formConfig)
      formDesignerStore.importForm({
        id: formData.id,
        name: formData.formName,
        description: formData.formDescription,
        ...formConfig
      })
    } else {
      // 如果没有配置，只设置基本信息
      formDesignerStore.updateFormConfig({
        id: formData.id,
        name: formData.formName,
        description: formData.formDescription
      })
    }

    ElMessage.success('表单加载成功')
  } catch (error) {
    ElMessage.error('加载表单失败')
  }
}

// 初始化表单
onMounted(async () => {
  formId.value = getUrlParams()

  if (isEditMode.value) {
    // 编辑模式：加载现有表单数据
    await loadFormData(formId.value)
  } else {
    // 新增模式：初始化空表单
    formDesignerStore.initForm()
  }
})

// 预览表单
const previewForm = () => {
  if (formDesignerStore.currentForm.components.length === 0) {
    ElMessage.warning('请先添加表单组件')
    return
  }

  previewDrawerVisible.value = true
}

// 保存表单
const saveForm = async () => {
  if (!formDesignerStore.currentForm.name.trim()) {
    ElMessage.warning('请输入表单名称')
    return
  }

  if (formDesignerStore.currentForm.components.length === 0) {
    ElMessage.warning('表单内容不能为空')
    return
  }

  try {
    const actionText = isEditMode.value ? '更新' : '保存'
    await ElMessageBox.confirm(`确定${actionText}表单吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })

    // 构造保存数据
    const formData = {
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
      })
    }

    // 根据是否有表单ID决定调用新增还是更新接口
    const { formApi } = await import('@/api')
    if (isEditMode.value) {
      // 编辑模式：调用更新接口
      await formApi.updateForm({
        id: formId.value,
        ...formData,
        status: 1 // 编辑时保持原状态，这里默认设为启用
      })
      ElMessage.success('表单更新成功，即将跳转到列表页面...')
    } else {
      // 新增模式：调用创建接口
      await formApi.createForm({
        ...formData,
        status: 0 // 新建表单默认未启用
      })
      ElMessage.success('表单创建成功，即将跳转到列表页面...')
    }

    // 保存成功后跳转回表单列表
    // setTimeout(() => {
    //   window.location.href = '/home/form/list'
    // }, 100)

  } catch (error) {
    if (error && typeof error === 'object' && 'message' in error) {
      ElMessage.error('保存失败：' + error.message)
    } else {
      const actionText = isEditMode.value ? '更新' : '保存'
      ElMessage.error(`${actionText}表单失败`)
    }
  }
}

// 更新表单名称
const updateFormName = () => {
  if (formDesignerStore.currentForm.name.trim()) {
    formDesignerStore.saveToHistory()
  }
}

// 清空画布
const clearForm = async () => {
  if (formDesignerStore.currentForm.components.length === 0) {
    ElMessage.warning('画布已经是空的')
    return
  }

  try {
    await ElMessageBox.confirm(
      '确定要清空画布吗？此操作不可恢复！',
      '确认清空',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    formDesignerStore.clearForm()
    ElMessage.success('画布已清空')
  } catch (error) {
    // 用户取消操作
  }
}

// 处理表单提交
const handleSubmitForm = () => {
  if (!formPreviewRef.value) {
    ElMessage.warning('表单预览组件未加载')
    return
  }

  try {
    // 获取表单数据
    const formData = formPreviewRef.value.getFormData()

    // 显示成功消息
    ElMessage.success('表单数据获取成功')

  } catch (error) {
    ElMessage.error('获取表单数据失败')
  }
}


</script>

<style scoped lang="scss">
.form-designer {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-secondary);
}

.designer-header {
  height: 60px;
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: var(--shadow-sm);

  .header-left {
    display: flex;
    align-items: center;
    gap: 20px;

    h2 {
      margin: 0;
      font-size: 18px;
      color: var(--text-primary);
    }

    .form-name-input {
      width: 300px;

      :deep(.el-input__inner) {
        border: 1px dashed var(--border-color);
        background: transparent;
        font-size: 16px;
        font-weight: 500;
        color: var(--text-primary);

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

  .header-right {
    display: flex;
    gap: 12px;
  }
}

.designer-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.designer-left {
  width: 260px;
  background: var(--bg-card);
  border-right: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 关键：限制高度 */

  .components-panel {
    flex: 1;
    display: flex;
    flex-direction: column;
    min-height: 0; /* 关键：让flex子元素可以正确收缩 */

    .panel-header {
      padding: 16px;
      border-bottom: 1px solid var(--border-color);
      flex-shrink: 0; /* 头部不压缩 */

      h3 {
        margin: 0 0 12px 0;
        font-size: 14px;
        color: var(--text-primary);
        background: transparent;
      }
    }

    .panel-content {
      flex: 1;
      overflow-y: auto;
      min-height: 0; /* 关键：让滚动容器可以正确计算高度 */
    }
  }
}

.designer-center {
  flex: 1;
  display: flex;
  flex-direction: column;
  margin: 0 1px;
  overflow: hidden; /* 关键：限制高度 */

  .canvas-container {
    flex: 1;
    background: var(--bg-card);
    display: flex;
    flex-direction: column;
    min-height: 0; /* 关键：让flex子元素可以正确收缩 */
    overflow: hidden; /* 关键：限制高度 */

    .canvas-header {
      height: 50px;
      padding: 0 20px;
      border-bottom: 1px solid var(--border-color);
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: transparent;
      flex-shrink: 0; /* 头部不压缩 */

      span {
        font-weight: 500;
        color: var(--text-primary);
      }
    }

    // 画布内容区域 - 让 DesignCanvas 可以滚动
    :deep(.design-canvas) {
      flex: 1;
      min-height: 0; /* 关键：让滚动生效 */
      overflow-y: auto;
    }
  }
}

.designer-right {
  width: 300px;
  background: var(--bg-card);
  border-left: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 关键：限制高度，让内部滚动生效 */

  .properties-panel {
    flex: 1;
    display: flex;
    flex-direction: column;
    min-height: 0; /* 关键：让flex子元素可以正确收缩 */

    .panel-header {
      padding: 16px;
      border-bottom: 1px solid var(--border-color);
      flex-shrink: 0; /* 头部不压缩 */

      h3 {
        margin: 0;
        font-size: 14px;
        color: var(--text-primary);
        background: transparent;
      }
    }

    .panel-content {
      flex: 1;
      overflow-y: auto;
      min-height: 0; /* 关键：让滚动容器可以正确计算高度 */
    }
  }
}

// 预览抽屉底部样式
.preview-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 0;
  border-top: 1px solid var(--border-color);
  margin-top: 20px;
}
</style>
