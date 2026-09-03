<template>
  <div class="editor-page embed">
    <!-- 顶部工具栏（可选） -->
    <div v-if="showToolbar && editorReady" class="toolbar">
      <div class="toolbar-left">
        <div class="document-info">
          <span class="doc-title">文档编辑</span>
          <span v-if="dirty" class="dirty-indicator">● 未保存</span>
        </div>
      </div>
      <div class="toolbar-right">
        <button class="btn btn-save" :disabled="saving" @click="handleSave">
          <span v-if="saving" class="btn-icon">⏳</span>
          <span v-else class="btn-icon">💾</span>
          {{ saving ? '保存中...' : '保存' }}
        </button>
      </div>
    </div>

    <!-- 编辑器容器 -->
    <div class="editor-wrapper">
      <DocumentEditor
        v-if="editorReady"
        id="docEditor"
        :documentServerUrl="documentServerUrl"
        :config="editorConfig"
        @onDocumentReady="onDocumentReady"
        @onSave="onSave"
        @onError="onError"
        @onDocumentStateChange="onDocumentStateChange"
        @onRequestSaveResult="onRequestSaveResult"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { DocumentEditor } from '@onlyoffice/document-editor-vue'
import { onlyofficeApi } from '@/api'

const props = defineProps({
  documentId: { type: [String, Number], required: true },
  showToolbar: { type: Boolean, default: false }
})

const emit = defineEmits(['saveStart', 'saveTriggered', 'saveError', 'saveResult'])

const editorConfig = ref({})
const documentServerUrl = ref('')
const editorReady = ref(false)
const saving = ref(false)
const dirty = ref(false)

const onDocumentReady = () => {
  dirty.value = false
}

const onSave = () => {}

const onError = (error) => {
  console.error('OnlyOffice Error:', error)
}

const onDocumentStateChange = (event) => {
  dirty.value = !!event?.data
}

const onRequestSaveResult = (event) => {
  if (event?.data === 0) dirty.value = false
  saving.value = false
  emit('saveResult', event?.data)
}

const handleSave = async () => {
  try {
    const key = editorConfig.value?.document?.key
    if (!key) return
    saving.value = true
    emit('saveStart')
    await onlyofficeApi.forceSave(key)
    emit('saveTriggered')
  } catch (e) {
    console.error(e)
    emit('saveError', e)
  } finally {
    // 是否在回调中复位由 onRequestSaveResult 负责
  }
}

// 向父组件暴露保存方法
const triggerSave = async () => {
  await handleSave()
}

defineExpose({ triggerSave })

const loadEditorConfig = async (id) => {
  try {
    editorReady.value = false
    const response = await onlyofficeApi.getEditConfig(id)
    // 兼容两种返回：拦截器绕过时为原生 axios 响应，带有 data；后端若自带 {code,data}，此处也能兼容
    const payload = response?.data || response
    const config = payload?.config || payload?.data?.config || payload?.data || payload
    if (!config || typeof config !== 'object') {
      throw new Error('后端未返回 config 或格式不正确')
    }
    const serverUrl = config.documentServerUrl
    if (!serverUrl) {
      throw new Error('config.documentServerUrl 缺失')
    }
    documentServerUrl.value = serverUrl
    editorConfig.value = config
    editorReady.value = true
  } catch (error) {
    console.error('Failed to load editor config:', error)
  }
}

watch(() => props.documentId, (id) => {
  if (id) loadEditorConfig(id)
}, { immediate: true })
</script>

<style scoped>
.editor-page.embed {
  position: relative;
  top: auto; left: auto; right: auto; bottom: auto;
  height: 100%;
  background: #f8fafc;
  display: flex;
  flex-direction: column;
}

/* 保留原有样式 */
.toolbar { display: flex; align-items: center; justify-content: space-between; padding: 12px 16px; background: white; border-bottom: 1px solid #e5e7eb; box-shadow: 0 1px 3px rgba(0,0,0,0.1); flex-shrink: 0; }
.toolbar-left { display: flex; align-items: center; gap: 16px; }
.toolbar-right { display: flex; align-items: center; gap: 8px; }
.document-info { display: flex; align-items: center; gap: 8px; }
.doc-title { font-weight: 600; color: #1f2937; }
.dirty-indicator { color: #f59e0b; font-size: 12px; font-weight: 500; }
.editor-wrapper { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 12px; border: 1px solid #d1d5db; border-radius: 6px; background: white; color: #374151; font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.15s ease; text-decoration: none; }
.btn:hover { background: #f9fafb; border-color: #9ca3af; }
.btn:active { transform: translateY(1px); }
.btn:disabled { opacity: 0.6; cursor: not-allowed; }
.btn-save { background: #446995; border-color: #446995; color: white; }
.btn-save:hover:not(:disabled) { background: #3a5a7a; }
.btn-icon { font-size: 16px; line-height: 1; }
:deep(#docEditor) { width: 100% !important; height: 100% !important; min-height: 0; }
</style>
