import { ref, reactive, computed } from 'vue'
import { defineStore } from 'pinia'
import type { FormComponent, FormConfig } from '../types/form'
import { getComponentDefault } from '../config/components'

export const useFormDesignerStore = defineStore('formDesigner', () => {
  // 当前表单配置
  const currentForm = reactive<FormConfig>({
    id: '',
    name: '未命名表单',
    description: '',
    labelWidth: '120px',
    labelPosition: 'right',
    size: 'default',
    disabled: false,
    inline: false,
    components: [],
    style: {}
  })

  // 选中的组件ID
  const selectedComponentId = ref<string>('')

  // 拖拽状态
  const dragState = reactive({
    isDragging: false,
    dragComponent: null as FormComponent | null,
    dragOverIndex: -1
  })

  // 历史记录
  const history = reactive({
    undoStack: [] as string[],
    redoStack: [] as string[],
    maxHistorySize: 50
  })

  // 计算属性
  const selectedComponent = computed(() => {
    return currentForm.components.find(comp => comp.id === selectedComponentId.value) || null
  })

  const hasComponents = computed(() => {
    return currentForm.components.length > 0
  })

  const canUndo = computed(() => {
    return history.undoStack.length > 0
  })

  const canRedo = computed(() => {
    return history.redoStack.length > 0
  })

  // 方法
  
  // 添加组件
  const addComponent = (componentType: string, index?: number) => {
    console.log('开始添加组件:', { componentType, index, 当前组件数: currentForm.components.length })
    
    try {
      const newComponent = getComponentDefault(componentType) as FormComponent
      console.log('新组件已创建:', newComponent)
      
      // 确保组件数组是响应式的
      if (typeof index === 'number' && index >= 0 && index <= currentForm.components.length) {
        currentForm.components.splice(index, 0, newComponent)
        console.log(`组件已插入到位置 ${index}`)
      } else {
        currentForm.components.push(newComponent)
        console.log('组件已添加到末尾')
      }
      
      // 验证添加是否成功
      const finalCount = currentForm.components.length
      const addedComponent = currentForm.components.find(c => c.id === newComponent.id)
      console.log('添加验证:', { 组件数: finalCount, 找到新组件: !!addedComponent })
      
      if (!addedComponent) {
        throw new Error('组件添加失败：未找到新添加的组件')
      }
      
      selectComponent(newComponent.id)
      saveToHistory()
      
      console.log('组件添加完成，当前组件数:', currentForm.components.length)
      console.log('当前组件ID列表:', currentForm.components.map(c => c.id))
      
      return newComponent
    } catch (error) {
      console.error('添加组件失败:', error)
      throw error
    }
  }

  // 删除组件
  const removeComponent = (componentId: string) => {
    const index = currentForm.components.findIndex(comp => comp.id === componentId)
    if (index > -1) {
      currentForm.components.splice(index, 1)
      if (selectedComponentId.value === componentId) {
        selectedComponentId.value = ''
      }
      saveToHistory()
    }
  }

  // 复制组件
  const duplicateComponent = (componentId: string) => {
    const component = currentForm.components.find(comp => comp.id === componentId)
    if (component) {
      const newComponent = {
        ...JSON.parse(JSON.stringify(component)),
        id: `component_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
        name: `${component.name}_copy`
      }
      
      const index = currentForm.components.findIndex(comp => comp.id === componentId)
      currentForm.components.splice(index + 1, 0, newComponent)
      selectComponent(newComponent.id)
      saveToHistory()
      return newComponent
    }
  }

  // 移动组件
  const moveComponent = (fromIndex: number, toIndex: number) => {
    if (fromIndex < 0 || fromIndex >= currentForm.components.length) {
      return
    }
    // 先取出元素
    const item = currentForm.components.splice(fromIndex, 1)[0]
    // 如果从前往后移动，移除后目标索引需要 -1
    let insertIndex = toIndex
    if (fromIndex < toIndex) insertIndex = toIndex - 1
    // 允许插入到末尾（等于当前长度）
    insertIndex = Math.max(0, Math.min(insertIndex, currentForm.components.length))
    currentForm.components.splice(insertIndex, 0, item)
    saveToHistory()
  }

  // 更新组件属性
  const updateComponent = (componentId: string, updates: Partial<FormComponent>) => {
    const component = currentForm.components.find(comp => comp.id === componentId)
    if (component) {
      Object.assign(component, updates)
      saveToHistory()
    }
  }

  // 选中组件
  const selectComponent = (componentId: string) => {
    selectedComponentId.value = componentId
  }

  // 清空选择
  const clearSelection = () => {
    selectedComponentId.value = ''
  }

  // 清空表单
  const clearForm = () => {
    currentForm.components = []
    selectedComponentId.value = ''
    saveToHistory()
  }

  // 更新表单配置
  const updateFormConfig = (updates: Partial<FormConfig>) => {
    Object.assign(currentForm, updates)
    saveToHistory()
  }

  // 设置拖拽状态
  const setDragState = (isDragging: boolean, component?: FormComponent | null, overIndex?: number) => {
    dragState.isDragging = isDragging
    dragState.dragComponent = component || null
    dragState.dragOverIndex = overIndex ?? -1
  }

  // 保存到历史记录
  const saveToHistory = () => {
    const currentState = JSON.stringify({
      form: currentForm,
      selectedComponentId: selectedComponentId.value
    })
    
    history.undoStack.push(currentState)
    
    // 限制历史记录大小
    if (history.undoStack.length > history.maxHistorySize) {
      history.undoStack.shift()
    }
    
    // 清空重做栈
    history.redoStack = []
  }

  // 撤销
  const undo = () => {
    if (history.undoStack.length > 0) {
      // 保存当前状态到重做栈
      const currentState = JSON.stringify({
        form: currentForm,
        selectedComponentId: selectedComponentId.value
      })
      history.redoStack.push(currentState)
      
      // 恢复上一个状态
      const previousState = history.undoStack.pop()!
      const parsed = JSON.parse(previousState)
      
      Object.assign(currentForm, parsed.form)
      selectedComponentId.value = parsed.selectedComponentId
    }
  }

  // 重做
  const redo = () => {
    if (history.redoStack.length > 0) {
      // 保存当前状态到撤销栈
      const currentState = JSON.stringify({
        form: currentForm,
        selectedComponentId: selectedComponentId.value
      })
      history.undoStack.push(currentState)
      
      // 恢复重做状态
      const nextState = history.redoStack.pop()!
      const parsed = JSON.parse(nextState)
      
      Object.assign(currentForm, parsed.form)
      selectedComponentId.value = parsed.selectedComponentId
    }
  }

  // 导入表单配置
  const importForm = (formConfig: FormConfig) => {
    Object.assign(currentForm, formConfig)
    selectedComponentId.value = ''
    saveToHistory()
  }

  // 导出表单配置
  const exportForm = (): FormConfig => {
    return JSON.parse(JSON.stringify(currentForm))
  }

  // 生成表单JSON
  const generateFormJSON = () => {
    return JSON.stringify(currentForm, null, 2)
  }

  // 生成Vue组件代码
  const generateVueCode = () => {
    const template = generateTemplate()
    const script = generateScript()
    const style = generateStyle()
    
    return `<template>
${template}
</template>

<script setup lang="ts">
${script}
</script>

<style scoped>
${style}
</style>`
  }

  // 生成模板代码
  const generateTemplate = () => {
    const formProps = [
      `:model="form"`,
      `label-width="${currentForm.labelWidth}"`,
      `label-position="${currentForm.labelPosition}"`,
      `size="${currentForm.size}"`,
      currentForm.disabled ? 'disabled' : '',
      currentForm.inline ? 'inline' : ''
    ].filter(Boolean).join(' ')

    let template = `  <el-form ${formProps}>\n`
    
    currentForm.components.forEach(component => {
      const formItemProps = [
        `label="${component.label}"`,
        `prop="${component.name}"`,
        component.required ? 'required' : ''
      ].filter(Boolean).join(' ')
      
      template += `    <el-form-item ${formItemProps}>\n`
      template += generateComponentTemplate(component)
      template += `    </el-form-item>\n`
    })
    
    template += `    <el-form-item>\n`
    template += `      <el-button type="primary" @click="onSubmit">提交</el-button>\n`
    template += `      <el-button @click="onReset">重置</el-button>\n`
    template += `    </el-form-item>\n`
    template += `  </el-form>`
    
    return template
  }

  // 生成组件模板
  const generateComponentTemplate = (component: FormComponent): string => {
    switch (component.type) {
      case 'input':
        const inputProps = [
          `v-model="form.${component.name}"`,
          component.placeholder ? `placeholder="${component.placeholder}"` : '',
          component.disabled ? 'disabled' : ''
        ].filter(Boolean).join(' ')
        return `      <el-input ${inputProps} />\n`
      
      case 'select':
        const selectProps = [
          `v-model="form.${component.name}"`,
          component.placeholder ? `placeholder="${component.placeholder}"` : '',
          component.disabled ? 'disabled' : ''
        ].filter(Boolean).join(' ')
        return `      <el-select ${selectProps}>\n        <!-- 选项需要根据配置添加 -->\n      </el-select>\n`
      
      default:
        return `      <!-- ${component.type} 组件 -->\n`
    }
  }

  // 生成脚本代码
  const generateScript = () => {
    const formFields = currentForm.components.reduce((acc, comp) => {
      acc[comp.name] = ''
      return acc
    }, {} as Record<string, any>)

    return `import { ref } from 'vue'

const form = ref(${JSON.stringify(formFields, null, 2)})

const onSubmit = () => {
  console.log('表单数据:', form.value)
}

const onReset = () => {
  form.value = ${JSON.stringify(formFields, null, 2)}
}`
  }

  // 生成样式代码
  const generateStyle = () => {
    return `/* 表单样式 */`
  }

  // 初始化表单
  const initForm = () => {
    currentForm.id = `form_${Date.now()}`
    saveToHistory()
  }

  return {
    // 状态
    currentForm,
    selectedComponentId,
    dragState,
    history,
    
    // 计算属性
    selectedComponent,
    hasComponents,
    canUndo,
    canRedo,
    
    // 方法
    addComponent,
    removeComponent,
    duplicateComponent,
    moveComponent,
    updateComponent,
    selectComponent,
    clearSelection,
    clearForm,
    updateFormConfig,
    setDragState,
    saveToHistory,
    undo,
    redo,
    importForm,
    exportForm,
    generateFormJSON,
    generateVueCode,
    initForm
  }
})