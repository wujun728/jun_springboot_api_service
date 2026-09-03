<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Download, DocumentCopy } from '@element-plus/icons-vue'
import { useFormDesignerStore } from '@/stores/form/formDesigner'
// import MonacoEditor from './MonacoEditor.vue'

const formDesignerStore = useFormDesignerStore()

// 编辑器状态
const editorContent = ref('')
const language = ref('vue')
const theme = ref('vs-dark')
const editorRef = ref()

// 计算属性
const currentForm = computed(() => formDesignerStore.currentForm)

// 初始化编辑器内容
const initializeEditor = () => {
  // 如果有表单配置，生成对应的Vue组件代码
  if (currentForm.value.components.length > 0) {
    editorContent.value = formDesignerStore.generateVueCode()
  } else {
    // 默认模板
    editorContent.value = `<!-- Vue组件模板示例 -->
<template>
  <el-form :model="form" label-width="120px">
    <el-form-item label="用户名">
      <el-input v-model="form.username" placeholder="请输入用户名" />
    </el-form-item>
    
    <el-form-item label="邮箱">
      <el-input v-model="form.email" type="email" placeholder="请输入邮箱" />
    </el-form-item>
    
    <el-form-item>
      <el-button type="primary" @click="onSubmit">提交</el-button>
    </el-form-item>
  </el-form>
</template>

<!--
<script setup>
import { ref } from 'vue'

const form = ref({
  username: '',
  email: ''
})

const onSubmit = () => {
  console.log('表单数据:', form.value)
}
<\/script>
-->

<style scoped>
/* 表单样式 */
.el-form {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}
</style>`
  }
}

// 保存代码
const saveCode = () => {
  const blob = new Blob([editorContent.value], { type: 'text/plain' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url
  a.download = `form-component.${language.value === 'vue' ? 'vue' : language.value}`
  document.body.appendChild(a)
  a.click()
  document.body.removeChild(a)
  URL.revokeObjectURL(url)
  ElMessage.success('代码已保存到本地')
}

// 复制代码到剪贴板
const copyCode = async () => {
  try {
    await navigator.clipboard.writeText(editorContent.value)
    ElMessage.success('代码已复制到剪贴板')
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败，请手动选择复制')
  }
}

// 格式化代码
const formatCode = () => {
  // 临时禁用格式化功能
  ElMessage.info('格式化功能暂时不可用')
}

// 从表单设计器同步代码
const syncFromDesigner = () => {
  if (currentForm.value.components.length > 0) {
    editorContent.value = formDesignerStore.generateVueCode()
    ElMessage.success('已同步表单设计器的代码')
  } else {
    ElMessage.warning('表单设计器中暂无内容')
  }
}

// 组件挂载时初始化编辑器
onMounted(() => {
  // initializeEditor() // 临时禁用Monaco编辑器初始化
})
</script>

<template>
  <div class="drawer-code-editor">
    <div class="editor-toolbar">
      <div class="toolbar-left">
        <el-select 
          v-model="language" 
          size="small" 
          style="width: 100px"
        >
          <el-option label="Vue" value="vue" />
          <el-option label="JavaScript" value="javascript" />
          <el-option label="TypeScript" value="typescript" />
          <el-option label="HTML" value="html" />
          <el-option label="CSS" value="css" />
          <el-option label="JSON" value="json" />
        </el-select>
        
        <el-select 
          v-model="theme" 
          size="small" 
          style="width: 100px; margin-left: 8px"
        >
          <el-option label="暗色主题" value="vs-dark" />
          <el-option label="亮色主题" value="vs" />
          <el-option label="高对比度" value="hc-black" />
        </el-select>
      </div>
      
      <div class="toolbar-right">
        <el-button-group>
          <el-button size="small" @click="syncFromDesigner" title="同步设计器">
            <el-icon><Refresh /></el-icon>
          </el-button>
          <el-button size="small" @click="formatCode" title="格式化代码">
            <el-icon><Refresh /></el-icon>
          </el-button>
          <el-button size="small" @click="copyCode" title="复制代码">
            <el-icon><DocumentCopy /></el-icon>
          </el-button>
          <el-button size="small" @click="saveCode" title="保存代码">
            <el-icon><Download /></el-icon>
          </el-button>
        </el-button-group>
      </div>
    </div>
    
    <div class="editor-wrapper">
      <el-input
        v-model="editorContent"
        type="textarea"
        :rows="30"
        placeholder="生成的代码将显示在这里..."
        class="code-textarea"
        readonly
      />
    </div>
  </div>
</template>

<style scoped>
.drawer-code-editor {
  height: 100%;
  display: flex;
  flex-direction: column;
  width: 100%;
}

.editor-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e6e6e6;
  flex-shrink: 0;
  width: 100%;
}

.toolbar-left {
  display: flex;
  gap: 8px;
}

.toolbar-right {
  display: flex;
  gap: 4px;
}

.editor-wrapper {
  flex: 1;
  overflow: hidden;
  display: flex;
  width: 100%;
}

.code-textarea {
  height: 100%;
}

.code-textarea :deep(.el-textarea__inner) {
  height: 100% !important;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 13px;
  line-height: 1.5;
  background-color: #1e1e1e;
  color: #d4d4d4;
  border: none;
  resize: none;
}
</style>