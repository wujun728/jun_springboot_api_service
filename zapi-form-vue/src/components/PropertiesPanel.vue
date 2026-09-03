<script setup lang="ts">
import { computed, watch, ref } from 'vue'
import { useFormDesignerStore } from '../stores/formDesigner'
import type { FormComponent, SelectComponent, RadioComponent, CheckboxComponent, DatePickerComponent } from '../types/form'

const formDesignerStore = useFormDesignerStore()

// 当前Tab：form（表单设置）| component（组件属性）
const activeTab = ref<'form' | 'component'>('form')

// 折叠面板活动项（已不再使用，仅保留变量以避免潜在引用报错）
const activeNames = ref(['basic'])

// 计算属性
const selectedComponent = computed(() => formDesignerStore.selectedComponent)
const hasSelection = computed(() => !!selectedComponent.value)

// 更新组件属性
const updateProperty = (property: string, value: any) => {
  if (selectedComponent.value) {
    formDesignerStore.updateComponent(selectedComponent.value.id, {
      [property]: value
    })
  }
}

// 添加选项（用于select、radio、checkbox组件）
const addOption = () => {
  if (!selectedComponent.value) return

  const component = selectedComponent.value as SelectComponent | RadioComponent | CheckboxComponent
  if (!component.options) component.options = []

  const newOption = {
    label: `选项${component.options.length + 1}`,
    value: `option${component.options.length + 1}`,
    disabled: false
  }

  component.options.push(newOption)
  updateProperty('options', component.options)
}

// 删除选项
const removeOption = (index: number) => {
  if (!selectedComponent.value) return

  const component = selectedComponent.value as SelectComponent | RadioComponent | CheckboxComponent
  if (component.options && component.options.length > 1) {
    component.options.splice(index, 1)
    updateProperty('options', component.options)
  }
}

// 更新选项
const updateOption = (index: number, field: string, value: any) => {
  if (!selectedComponent.value) return

  const component = selectedComponent.value as SelectComponent | RadioComponent | CheckboxComponent
  if (component.options && component.options[index]) {
    ;(component.options[index] as any)[field] = value
    updateProperty('options', component.options)
  }
}

// 添加验证规则
const addValidationRule = () => {
  if (!selectedComponent.value) return

  if (!selectedComponent.value.rules) {
    selectedComponent.value.rules = []
  }

  const newRule = {
    required: false,
    message: '',
    trigger: 'blur' as const
  }

  selectedComponent.value.rules.push(newRule)
  updateProperty('rules', selectedComponent.value.rules)
}

// 删除验证规则
const removeValidationRule = (index: number) => {
  if (!selectedComponent.value || !selectedComponent.value.rules) return

  selectedComponent.value.rules.splice(index, 1)
  updateProperty('rules', selectedComponent.value.rules)
}

// 更新验证规则
const updateValidationRule = (index: number, field: string, value: any) => {
  if (!selectedComponent.value || !selectedComponent.value.rules) return

  ;(selectedComponent.value.rules[index] as any)[field] = value
  updateProperty('rules', selectedComponent.value.rules)
}
</script>

<template>
  <div class="properties-panel">
    <el-tabs v-model="activeTab" class="prop-tabs" type="border-card">


      <!-- 组件属性 -->
      <el-tab-pane label="组件属性" name="component">
        <!-- 无选择状态 -->
        <div v-if="!hasSelection" class="no-selection">
          <el-empty
            description="请选择组件查看属性"
            :image-size="80"
          >
            <template #image>
              <el-icon size="80" color="#c0c4cc"><Mouse /></el-icon>
            </template>
          </el-empty>
        </div>

        <!-- 属性配置内容（分类标题 + 内容） -->
        <div v-else class="panel-content">
          <!-- 基础属性 -->
          <div class="section">
            <div class="section-title">
              <span>基础属性</span>
            </div>
            <div class="section-body">
              <el-form label-width="80px" size="small">
                <el-form-item label="标签">
                  <el-input
                    :model-value="selectedComponent?.label ?? ''"
                    @input="updateProperty('label', $event)"
                    placeholder="请输入标签"
                  />
                </el-form-item>

                <el-form-item label="字段名">
                  <el-input
                    :model-value="selectedComponent?.name ?? ''"
                    @input="updateProperty('name', $event)"
                    placeholder="请输入字段名"
                  />
                </el-form-item>

                <el-form-item label="占位符">
                  <el-input
                    :model-value="(selectedComponent && selectedComponent.placeholder) ? selectedComponent.placeholder : ''"
                    @input="updateProperty('placeholder', $event)"
                    placeholder="请输入占位符"
                  />
                </el-form-item>

                <el-form-item label="默认值">
                  <template v-if="selectedComponent && selectedComponent.type === 'switch'">
                    <el-switch
                      :model-value="(selectedComponent as any).defaultValue ?? false"
                      @change="updateProperty('defaultValue', $event)"
                    />
                  </template>
                  <template v-else-if="selectedComponent && ['input-number', 'slider', 'rate'].includes(selectedComponent.type)">
                    <el-input-number
                      :model-value="Number((selectedComponent as any).defaultValue ?? 0)"
                      @change="updateProperty('defaultValue', $event)"
                    />
                  </template>
                  <template v-else>
                    <el-input
                      :model-value="String((selectedComponent as any)?.defaultValue ?? '')"
                      @input="updateProperty('defaultValue', $event)"
                    />
                  </template>
                </el-form-item>

                <el-form-item label="必填">
                  <el-switch
                    :model-value="selectedComponent?.required || false"
                    @change="updateProperty('required', $event)"
                  />
                </el-form-item>

                <el-form-item label="禁用">
                  <el-switch
                    :model-value="selectedComponent?.disabled || false"
                    @change="updateProperty('disabled', $event)"
                  />
                </el-form-item>
              </el-form>
            </div>
          </div>

          <!-- 高级属性 -->
          <div class="section">
            <div class="section-title">
              <span>高级属性</span>
            </div>
            <div class="section-body">
              <!-- 输入框特殊属性 -->
              <template v-if="selectedComponent.type === 'input'">
                <el-form label-width="80px" size="small">
                  <el-form-item label="输入类型">
                    <el-select
                      :model-value="(selectedComponent as any).inputType || 'text'"
                      @change="updateProperty('inputType', $event)"
                    >
                      <el-option label="文本" value="text" />
                      <el-option label="密码" value="password" />
                      <el-option label="多行文本" value="textarea" />
                      <el-option label="数字" value="number" />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="最大长度">
                    <el-input-number
                      :model-value="(selectedComponent as any).maxlength"
                      @change="updateProperty('maxlength', $event)"
                      :min="1"
                    />
                  </el-form-item>

                  <el-form-item label="显示清除">
                    <el-switch
                      :model-value="(selectedComponent as any).clearable || false"
                      @change="updateProperty('clearable', $event)"
                    />
                  </el-form-item>

                  <el-form-item v-if="(selectedComponent as any).inputType === 'textarea'" label="行数">
                    <el-input-number
                      :model-value="(selectedComponent as any).rows || 4"
                      @change="updateProperty('rows', $event)"
                      :min="2"
                      :max="10"
                    />
                  </el-form-item>
                </el-form>
              </template>

              <!-- 选择器特殊属性 -->
              <template v-if="selectedComponent.type === 'select'">
                <el-form label-width="80px" size="small">
                  <el-form-item label="多选">
                    <el-switch
                      :model-value="(selectedComponent as SelectComponent).multiple || false"
                      @change="updateProperty('multiple', $event)"
                    />
                  </el-form-item>

                  <el-form-item label="可搜索">
                    <el-switch
                      :model-value="(selectedComponent as SelectComponent).filterable || false"
                      @change="updateProperty('filterable', $event)"
                    />
                  </el-form-item>

                  <el-form-item label="可清空">
                    <el-switch
                      :model-value="(selectedComponent as SelectComponent).clearable || false"
                      @change="updateProperty('clearable', $event)"
                    />
                  </el-form-item>
                </el-form>
              </template>

              <!-- 日期选择器特殊属性 -->
              <template v-if="selectedComponent.type === 'date-picker'">
                <el-form label-width="80px" size="small">
                  <el-form-item label="选择类型">
                    <el-select
                      :model-value="(selectedComponent as DatePickerComponent).pickerType || 'date'"
                      @change="updateProperty('pickerType', $event)"
                    >
                      <el-option label="日期" value="date" />
                      <el-option label="日期时间" value="datetime" />
                      <el-option label="日期范围" value="daterange" />
                      <el-option label="日期时间范围" value="datetimerange" />
                      <el-option label="月份" value="month" />
                      <el-option label="年份" value="year" />
                    </el-select>
                  </el-form-item>

                  <el-form-item label="格式">
                    <el-input
                      :model-value="(selectedComponent as DatePickerComponent).format || 'YYYY-MM-DD'"
                      @input="updateProperty('format', $event)"
                    />
                  </el-form-item>
                </el-form>
              </template>
            </div>
          </div>

          <!-- 布局：组件宽度（span） -->
          <div class="section">
            <div class="section-title">
              <span>布局</span>
            </div>
            <div class="section-body">
              <el-form label-width="80px" size="small">
                <el-form-item label="组件宽度">
                  <div class="layout-row">
                    <!-- <el-slider
                      :model-value="(selectedComponent && selectedComponent.span != null) ? selectedComponent.span : 12"
                      @change="updateProperty('span', $event)"
                      :min="1" :max="12" :step="1"
                      show-stops
                      :marks="{1:'1',6:'6',12:'12'}"
                      style="flex:1"
                    /> -->
                    <el-input-number
                      :model-value="(selectedComponent && selectedComponent.span != null) ? selectedComponent.span : 12"
                      @change="updateProperty('span', $event)"
                      :min="1" :max="12" :step="1"
                      controls-position="right"
                      style="width: 110px; margin-left: 8px;"
                    />
                  </div>
                </el-form-item>
              </el-form>
            </div>
          </div>

          <!-- 选项配置 -->
          <div v-if="['select', 'radio', 'checkbox'].includes(selectedComponent.type)" class="section">
            <div class="section-title with-action">
              <span>选项配置</span>
              <el-button type="primary" size="small" @click="addOption">
                <el-icon><Plus /></el-icon>
                添加选项
              </el-button>
            </div>
            <div class="section-body">
              <div class="options-list">
                <div
                  v-for="(option, index) in (selectedComponent as any).options"
                  :key="index"
                  class="option-item"
                >
                  <el-form label-width="50px" size="small">
                    <el-form-item label="标签">
                      <el-input
                        :model-value="option.label"
                        @input="updateOption(index, 'label', $event)"
                      />
                    </el-form-item>

                    <el-form-item label="值">
                      <el-input
                        :model-value="option.value"
                        @input="updateOption(index, 'value', $event)"
                      />
                    </el-form-item>

                    <el-form-item label="禁用">
                      <div class="option-controls">
                        <el-switch
                          :model-value="option.disabled || false"
                          @change="updateOption(index, 'disabled', $event)"
                        />
                        <el-button
                          type="danger"
                          size="small"
                          text
                          @click="removeOption(index)"
                          :disabled="(selectedComponent as any).options.length <= 1"
                        >
                          <el-icon><Delete /></el-icon>
                        </el-button>
                      </div>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </div>
          </div>

          <!-- 验证规则 -->
          <div class="section">
            <div class="section-title with-action">
              <span>验证规则</span>
              <el-button type="primary" size="small" @click="addValidationRule">
                <el-icon><Plus /></el-icon>
                添加规则
              </el-button>
            </div>
            <div class="section-body">
              <div v-if="!selectedComponent?.rules || selectedComponent?.rules.length === 0" class="no-rules">
                <el-text type="info">暂无验证规则</el-text>
              </div>

              <div v-else class="rules-list">
                <div
                  v-for="(rule, index) in (selectedComponent?.rules || [])"
                  :key="index"
                  class="rule-item"
                >
                  <el-form label-width="80px" size="small">
                    <el-form-item label="必填">
                      <el-switch
                        :model-value="rule.required || false"
                        @change="updateValidationRule(index, 'required', $event)"
                      />
                    </el-form-item>

                    <el-form-item label="错误信息">
                      <el-input
                        :model-value="rule.message || ''"
                        @input="updateValidationRule(index, 'message', $event)"
                        placeholder="请输入错误信息"
                      />
                    </el-form-item>

                    <el-form-item label="触发时机">
                      <div class="rule-controls">
                        <el-select
                          :model-value="rule.trigger || 'blur'"
                          @change="updateValidationRule(index, 'trigger', $event)"
                          style="flex: 1"
                        >
                          <el-option label="失焦时" value="blur" />
                          <el-option label="改变时" value="change" />
                        </el-select>
                        <el-button
                          type="danger"
                          size="small"
                          text
                          @click="removeValidationRule(index)"
                        >
                          <el-icon><Delete /></el-icon>
                        </el-button>
                      </div>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <!-- 表单设置（全局） -->
      <el-tab-pane label="表单设置" name="form">
        <div class="panel-content" style="padding-bottom: 0;">
          <div class="section">
            <div class="section-title">
              <span>表单设置</span>
            </div>
            <div class="section-body">
              <el-form label-width="70px" size="small">
                <el-form-item label="标签位置">
                  <el-radio-group
                    :model-value="formDesignerStore.currentForm.labelPosition || 'right'"
                    @change="formDesignerStore.updateFormConfig({ labelPosition: $event })"
                  >
                    <el-radio-button label="top">上下</el-radio-button>
                    <el-radio-button label="right">右对齐</el-radio-button>
                    <el-radio-button label="left">左对齐</el-radio-button>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="标签宽度">
                  <el-input
                    :model-value="formDesignerStore.currentForm.labelWidth || '120px'"
                    @input="formDesignerStore.updateFormConfig({ labelWidth: $event })"
                    placeholder="如 120px 或 8em"
                  />
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </el-tab-pane>


    </el-tabs>
  </div>
</template>

<style scoped>
.properties-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
}

.prop-tabs {
  flex: 1;
  display: flex;
  flex-direction: column;
}

:deep(.el-tabs__content) {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.no-selection {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
}

.no-selection .el-empty {
  padding: 20px;
}

.no-selection .el-empty :deep(.el-empty__description) {
  color: #999;
  font-size: 13px;
}

.panel-content {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
}

.section {
  margin-bottom: 16px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
}

.section-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  background: #fafafa;
  border-bottom: 1px solid #f0f0f0;
}

.section-title span {
  font-size: 13px;
  font-weight: 500;
  color: #333;
}

.section-title.with-action span {
  display: inline-flex;
  align-items: center;
}

.section-body {
  padding: 5px;
}

:deep(.el-form-item) {
  margin-bottom: 16px;
}

:deep(.el-form-item__label) {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  line-height: 32px;
}

:deep(.el-input), :deep(.el-select), :deep(.el-switch) {
  width: 100%;
}

:deep(.el-input__inner) {
  border-radius: 6px;
  border-color: #e8e8e8;
  font-size: 13px;
}

:deep(.el-input__inner:focus) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
}

.options-list, .rules-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.option-item, .rule-item {
  padding: 12px;
  background: #fafafa;
  border-radius: 6px;
  border: 1px solid #f0f0f0;
  transition: all 0.2s ease;
}

.option-item:hover, .rule-item:hover {
  border-color: #e0e0e0;
  background: #f5f5f5;
}

.option-controls, .rule-controls {
  display: flex;
  align-items: center;
  gap: 8px;
}

.rule-controls :deep(.el-select) {
  flex: 1;
}

.no-rules {
  text-align: center;
  padding: 24px;
  color: #999;
  font-size: 13px;
}

/* 滚动条样式 */
.panel-content::-webkit-scrollbar {
  width: 4px;
}

.panel-content::-webkit-scrollbar-track {
  background: transparent;
}

.panel-content::-webkit-scrollbar-thumb {
  background: #ddd;
  border-radius: 2px;
}

.panel-content::-webkit-scrollbar-thumb:hover {
  background: #ccc;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .panel-content {
    padding: 12px;
  }

  .option-item, .rule-item {
    padding: 12px;
  }
}
</style>
