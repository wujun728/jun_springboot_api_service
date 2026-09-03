<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import loader from '@monaco-editor/loader'

interface Props {
  modelValue: string
  language?: string
  theme?: string
  height?: string
  readonly?: boolean
  options?: any
}

interface Emits {
  (e: 'update:modelValue', value: string): void
  (e: 'change', value: string): void
  (e: 'blur'): void
  (e: 'focus'): void
}

const props = withDefaults(defineProps<Props>(), {
  language: 'javascript',
  theme: 'vs-dark',
  height: '400px',
  readonly: false,
  options: () => ({})
})

const emit = defineEmits<Emits>()

const editorContainer = ref<HTMLElement>()
let editor: any = null

// 初始化编辑器
const initEditor = async () => {
  if (!editorContainer.value) return

  try {
    const monaco = await loader.init()
    
    const defaultOptions = {
      value: props.modelValue,
      language: props.language,
      theme: props.theme,
      readOnly: props.readonly,
      automaticLayout: true,
      minimap: { enabled: true },
      scrollBeyondLastLine: false,
      wordWrap: 'on',
      fontSize: 14,
      lineNumbers: 'on',
      folding: true,
      bracketMatching: 'always',
      ...props.options
    }

    editor = monaco.editor.create(editorContainer.value, defaultOptions)

    // 监听内容变化
    editor.onDidChangeModelContent(() => {
      const value = editor.getValue()
      emit('update:modelValue', value)
      emit('change', value)
    })

    // 监听焦点事件
    editor.onDidFocusEditorText(() => {
      emit('focus')
    })

    editor.onDidBlurEditorText(() => {
      emit('blur')
    })

  } catch (error) {
    console.error('Monaco Editor initialization failed:', error)
  }
}

// 更新编辑器内容
const updateContent = (newValue: string) => {
  if (editor && editor.getValue() !== newValue) {
    editor.setValue(newValue)
  }
}

// 更新编辑器语言
const updateLanguage = (language: string) => {
  if (editor) {
    const model = editor.getModel()
    if (model) {
      loader.init().then(monaco => {
        monaco.editor.setModelLanguage(model, language)
      })
    }
  }
}

// 更新编辑器主题
const updateTheme = (theme: string) => {
  if (editor) {
    loader.init().then(monaco => {
      monaco.editor.setTheme(theme)
    })
  }
}

// 格式化代码
const formatCode = () => {
  if (editor) {
    editor.getAction('editor.action.formatDocument').run()
  }
}

// 获取编辑器内容
const getValue = () => {
  return editor?.getValue() || ''
}

// 设置编辑器内容
const setValue = (value: string) => {
  if (editor) {
    editor.setValue(value)
  }
}

// 监听props变化
watch(() => props.modelValue, updateContent)
watch(() => props.language, updateLanguage)
watch(() => props.theme, updateTheme)

onMounted(async () => {
  await nextTick()
  initEditor()
})

onUnmounted(() => {
  if (editor) {
    editor.dispose()
  }
})

// 暴露方法
defineExpose({
  formatCode,
  getValue,
  setValue
})
</script>

<template>
  <div class="monaco-editor-wrapper" :style="{ height }">
    <div ref="editorContainer" class="editor-container" />
  </div>
</template>

<style scoped>
.monaco-editor-wrapper {
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  overflow: hidden;
  width: 100%;
  flex: 1;
  min-height: 200px;
}

.editor-container {
  width: 100%;
  height: 100%;
}
</style>