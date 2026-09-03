<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useFormDesignerStore } from '../stores/formDesigner'
import ComponentsPanel from '../components/ComponentsPanel.vue'
import DesignCanvas from '../components/DesignCanvas.vue'
import PropertiesPanel from '../components/PropertiesPanel.vue'
import DrawerCodeEditor from '../components/DrawerCodeEditor.vue'
import FormPreview from './FormPreview.vue'

const router = useRouter()
const formDesignerStore = useFormDesignerStore()

// 抽屉状态
const codeDrawerVisible = ref(false)
const previewDrawerVisible = ref(false)

// 组件库搜索
const componentSearch = ref('')

// 初始化表单
onMounted(() => {
  formDesignerStore.initForm()
})

// 预览表单
const previewForm = () => {
  if (formDesignerStore.currentForm.components.length === 0) {
    ElMessage.warning('请先添加表单组件')
    return
  }
  
  // 打开预览抽屉
  previewDrawerVisible.value = true
  ElMessage.success('打开预览抽屉')
}

// 保存表单
const saveForm = () => {
  if (formDesignerStore.currentForm.components.length === 0) {
    ElMessage.warning('请先添加表单组件')
    return
  }
  
  // 保存到本地存储
  const formData = formDesignerStore.exportForm()
  localStorage.setItem('form_designer_data', JSON.stringify(formData))
  
  ElMessage.success('表单已保存到本地')
  console.log('保存表单', formData)
}

// 清空表单
const clearForm = () => {
  ElMessageBox.confirm('确定要清空表单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    formDesignerStore.clearForm()
    ElMessage.success('表单已清空')
  })
}

// 撤销/重做
const handleUndo = () => {
  formDesignerStore.undo()
}

const handleRedo = () => {
  formDesignerStore.redo()
}

// 返回首页
const goHome = () => {
  if (formDesignerStore.currentForm.components.length > 0) {
    ElMessageBox.confirm('当前表单尚未保存，确定要返回首页吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }).then(() => {
      router.push('/')
    })
  } else {
    router.push('/')
  }
}

// 打开代码编辑器抽屉
const openCodeEditor = () => {
  codeDrawerVisible.value = true
}

// 处理代码更新事件
const handleCodeUpdated = (code: string) => {
  // 由于已移除实时预览功能，这里只做简单的日志记录
  console.log('代码已更新:', code)
}
</script>

<template>
  <div class="form-designer">
    <!-- 顶部工具栏 -->
    <div class="designer-header">
      <div class="header-left">
        <!-- 返回首页按钮 -->
        <el-button 
          @click="goHome" 
          type="text" 
          class="back-home-btn"
          title="返回首页"
        >
          <el-icon size="18"><ArrowLeft /></el-icon>
          首页
        </el-button>
        
        <el-divider direction="vertical" />
        
        <div class="logo">
          <el-icon size="24" color="#409eff"><Grid /></el-icon>
          <span class="logo-text">表单设计器</span>
        </div>
        <div class="form-title">
          <el-input 
            v-model="formDesignerStore.currentForm.name" 
            placeholder="未命名表单"
            class="title-input"
            @blur="formDesignerStore.saveToHistory()"
          />
        </div>
      </div>
      
      <div class="header-center">
        <el-button-group class="action-buttons">
          <el-button 
            :disabled="!formDesignerStore.canUndo"
            @click="handleUndo"
            title="撤销"
          >
            <el-icon><RefreshLeft /></el-icon>
          </el-button>
          <el-button 
            :disabled="!formDesignerStore.canRedo"
            @click="handleRedo"
            title="重做"
          >
            <el-icon><RefreshRight /></el-icon>
          </el-button>
        </el-button-group>
      </div>
      
      <div class="header-right">
        <el-button-group>
          <el-button @click="previewForm">
            <el-icon><View /></el-icon>
            预览
          </el-button>
          <el-button @click="saveForm" type="primary">
            <el-icon><Document /></el-icon>
            保存
          </el-button>
          <!-- 添加代码编辑按钮，与保存按钮保持一行 -->
          <el-button @click="openCodeEditor">
            <el-icon><EditPen /></el-icon>
            代码
          </el-button>
        </el-button-group>

         
         
        
      </div>
    </div>
    
    <!-- 主体内容 -->
    <div class="designer-body">
      <!-- 左侧组件库 -->
      <div class="left-sidebar">
        <div class="sidebar-header">
          <h3>组件库</h3>
          <div class="header-search">
            <el-input
              v-model="componentSearch"
              placeholder="搜索组件"
              size="small"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
          </div>
        </div>
        <div class="sidebar-content">
          <ComponentsPanel :use-external-search="true" :external-search-text="componentSearch" />
        </div>
      </div>
      
      <!-- 中央设计区域 -->
      <div class="design-area">
        <!-- 设计区顶部工具 -->
        <div class="design-toolbar">
          <div class="toolbar-left">
            <span class="component-count">{{ formDesignerStore.currentForm.components.length }} 个组件</span>
            <span v-if="formDesignerStore.selectedComponent" class="selected-component">
              已选择：{{ formDesignerStore.selectedComponent.label }}
            </span>
          </div>
          <div class="toolbar-right">
            <el-button size="small" text @click="clearForm">
              <el-icon><Delete /></el-icon>
              清空
            </el-button>
          </div>
        </div>
        
        <!-- 设计画布 -->
        <div class="canvas-container">
          <div class="canvas-wrapper">
            <DesignCanvas />
          </div>
        </div>
      </div>
      
      <!-- 右侧属性面板 -->
      <div class="right-sidebar">
        <div class="sidebar-header">
          <h4>属性设置</h4>
        </div>
        <div class="sidebar-content">
          <!-- 只保留属性面板 -->
          <PropertiesPanel />
        </div>
      </div>
    </div>
    
    <!-- 代码编辑器抽屉 -->
    <el-drawer
      v-model="codeDrawerVisible"
      title="代码预览"
      direction="rtl"
      size="100%"
      :before-close="() => { codeDrawerVisible = false }"
    >
      <div class="code-editor-container">
        <DrawerCodeEditor @code-updated="handleCodeUpdated" />
      </div>
    </el-drawer>
    
    <!-- 表单预览抽屉 -->
    <el-drawer
      v-model="previewDrawerVisible"
      title="表单预览"
      direction="rtl"
      size="100%"
      :before-close="() => { previewDrawerVisible = false }"
    >
      <div class="preview-drawer-container">
        <FormPreview />
      </div>
    </el-drawer>
  </div>
</template>

<style scoped>
.form-designer {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  width: 100%;
}

/* 顶部工具栏 */
.designer-header {
  height: 64px;
  background: white;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  z-index: 100;
  flex-shrink: 0;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px; /* 减小间距 */
  flex: 1;
  min-width: 0; /* 允许弹性收缩 */
}

.back-home-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  color: #666;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  padding: 6px 12px;
  transition: all 0.2s ease;
}

.back-home-btn:hover {
  color: #409eff;
  border-color: #409eff;
  background-color: #f0f9ff;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  white-space: nowrap;
}

.logo-text {
  font-size: 18px;
  font-weight: 600;
  color: #409eff;
}

.form-title {
  flex: 1;
}

.form-title .title-input {
  width: 100%;
  max-width: 300px;
}

.form-title .title-input :deep(.el-input__inner) {
  border: none;
  background: transparent;
  font-size: 16px;
  font-weight: 500;
  padding: 8px 12px;
}

.form-title .title-input:hover :deep(.el-input__inner) {
  background: #f5f5f5;
  border-radius: 4px;
}

.header-center {
  flex: 0 0 auto; /* 不增长不收缩 */
  display: flex;
  justify-content: center;
  margin: 0 20px;
}

.action-buttons {
  border-radius: 6px;
}

.header-right {
  display: flex;
  gap: 12px;
  flex: 0 0 auto; /* 不增长不收缩 */
  align-items: center;
}

.nav-menu-btn {
  padding: 8px;
  border-radius: 6px;
  color: #666;
  transition: all 0.2s ease;
}

.nav-menu-btn:hover {
  background-color: #f5f5f5;
  color: #409eff;
}

/* 主体内容 */
.designer-body {
  flex: 1;
  display: flex;
  height: calc(100vh - 64px);
  min-height: 600px;
}

/* 侧边栏通用样式 */
.left-sidebar, .right-sidebar {
  width: 18%; /* 减少宽度，减少留白 */
  min-width: 260px;
  max-width: 320px;
  background: white;
  border-right: 1px solid #e8e8e8;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
}

.right-sidebar {
  border-right: none;
  border-left: 1px solid #e8e8e8;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  height: 56px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  border-bottom: 1px solid #f0f0f0;
  background: #fafafa;
  flex-shrink: 0;
  gap: 10px;
}

.sidebar-header h3 {
  margin: 0;
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

/* 左侧标题行的搜索框 */
.header-search {
  flex: 1;
}

.header-search :deep(.el-input__wrapper) {
  border-radius: 6px;
  height: 32px;
}

.sidebar-content {
  flex: 1;
  overflow-y: auto;
  overflow-x: hidden;
  height: calc(100% - 56px);
}

/* 设计区域 */
.design-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: #f8f9fa;
  min-width: 400px; /* 保持最小可用宽度 */
  min-height: calc(100vh - 64px);
}

.design-toolbar {
  height: 56px;
  background: white;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 16px;
  flex-shrink: 0;
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 13px;
  color: #666;
}

.component-count {
  color: #999;
  white-space: nowrap;
}

.selected-component {
  color: #409eff;
  font-weight: 500;
  white-space: nowrap;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.toolbar-right {
  display: flex;
  gap: 8px;
}

/* 画布容器 */
.canvas-container {
  flex: 1;
  padding: 24px;
  overflow: auto;
  background: #f8f9fa;
  min-height: 0; /* 避免因子元素撑高导致溢出 */
}

.canvas-wrapper {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  min-height: calc(100vh - 180px);
  height: 100%;
  border: 1px solid #e8e8e8;
  width: 100%;
  max-width: none;
  margin: 0 auto;
}

/* 抽屉中的代码编辑器容器 */
.code-editor-container {
  height: 100%;
  overflow: hidden;
  display: flex;
}

/* 让抽屉内容充满高度并参与flex布局 */
:deep(.el-drawer__body) {
  height: calc(100% - var(--el-drawer-header-height, 54px));
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 0; /* 去除内边距，避免出现滚动条 */
}

/* 预览抽屉容器 */
.preview-drawer-container {
  height: 100%;
  overflow: auto;
}

.preview-drawer-container :deep(.preview-container) {
  padding: 0;
}

.preview-drawer-container :deep(.el-card) {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.preview-drawer-container :deep(.el-card__body) {
  flex: 1;
  overflow: auto;
}

/* 响应式设计 - 灵活适应屏幕 */
@media (min-width: 1600px) {
  .left-sidebar, .right-sidebar {
    width: 20%; /* 减少大屏幕上的宽度 */
    min-width: 300px;
  }
}

@media (min-width: 1200px) and (max-width: 1599px) {
  .left-sidebar, .right-sidebar {
    width: 18%;
    min-width: 280px;
  }
}

@media (max-width: 1199px) {
  .left-sidebar, .right-sidebar {
    width: 16%; /* 进一步减少 */
    min-width: 240px;
  }
  
  .canvas-container {
    padding: 16px;
  }
}

@media (max-width: 768px) {
  .left-sidebar, .right-sidebar {
    width: 15%;
    min-width: 200px;
  }
  
  .designer-header {
    padding: 0 12px;
  }
  
  .canvas-container {
    padding: 12px;
  }
}
</style>