<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import type { FormComponent, InputComponent, SelectComponent, RadioComponent, CheckboxComponent, DatePickerComponent, TimePickerComponent, InputNumberComponent, SliderComponent, SwitchComponent, RateComponent, UploadComponent, ColorPickerComponent } from '../types/form'

// Props定义
interface Props {
  component: FormComponent
  modelValue?: any
  disabled?: boolean
}

// Emits定义
interface Emits {
  (e: 'update:modelValue', value: any): void
  (e: 'change', value: any): void
  (e: 'blur', event: Event): void
  (e: 'focus', event: Event): void
}

const props = withDefaults(defineProps<Props>(), {
  disabled: false
})

const emit = defineEmits<Emits>()

// 内部值：优先使用 modelValue，否则退回到组件的 defaultValue
const internalValue = ref(props.modelValue !== undefined ? props.modelValue : (props.component as any).defaultValue)

// 监听外部值变化
watch(() => props.modelValue, (newValue) => {
  internalValue.value = newValue !== undefined ? newValue : (props.component as any).defaultValue
}, { immediate: true })

// 当组件默认值变化时（且外部未显式绑定 modelValue），同步到内部值
watch(() => (props.component as any).defaultValue, (val) => {
  if (props.modelValue === undefined) {
    internalValue.value = val
  }
})

// 监听内部值变化
watch(internalValue, (newValue) => {
  emit('update:modelValue', newValue)
  emit('change', newValue)
}, { deep: true })

// 计算属性：获取组件配置
const componentConfig = computed(() => props.component)

// 事件处理
const handleBlur = (event: Event) => {
  emit('blur', event)
}

const handleFocus = (event: Event) => {
  emit('focus', event)
}

// 上传相关方法
const handleUploadSuccess = (response: any, file: any, fileList: any[]) => {
  internalValue.value = fileList
}

const handleUploadRemove = (file: any, fileList: any[]) => {
  internalValue.value = fileList
}

const beforeUpload = (file: any) => {
  // 可以在这里添加上传前的验证逻辑
  return true
}
</script>

<template>
  <div class="form-component-renderer">
    <!-- 输入框 -->
    <template v-if="componentConfig.type === 'input'">
      <el-input
        v-model="internalValue"
        :type="(componentConfig as InputComponent).inputType || 'text'"
        :placeholder="componentConfig.placeholder"
        :disabled="disabled || componentConfig.disabled"
        :clearable="(componentConfig as InputComponent).clearable"
        :show-password="(componentConfig as InputComponent).showPassword"
        :maxlength="(componentConfig as InputComponent).maxlength"
        :show-word-limit="(componentConfig as InputComponent).showWordLimit"
        :rows="(componentConfig as InputComponent).rows"
        :prefix-icon="(componentConfig as InputComponent).prefixIcon"
        :suffix-icon="(componentConfig as InputComponent).suffixIcon"
        @blur="handleBlur"
        @focus="handleFocus"
      />
    </template>

    <!-- 多行文本 -->
    <template v-else-if="componentConfig.type === 'textarea'">
      <el-input
        v-model="internalValue"
        type="textarea"
        :placeholder="componentConfig.placeholder"
        :disabled="disabled || componentConfig.disabled"
        :rows="(componentConfig as any).rows || 4"
        :maxlength="(componentConfig as any).maxlength"
        :show-word-limit="(componentConfig as any).showWordLimit"
        @blur="handleBlur"
        @focus="handleFocus"
      />
    </template>

    <!-- 下拉选择 -->
    <template v-else-if="componentConfig.type === 'select'">
      <el-select
        v-model="internalValue"
        :placeholder="componentConfig.placeholder"
        :disabled="disabled || componentConfig.disabled"
        :clearable="(componentConfig as SelectComponent).clearable"
        :filterable="(componentConfig as SelectComponent).filterable"
        :multiple="(componentConfig as SelectComponent).multiple"
        style="width: 100%"
        @blur="handleBlur"
        @focus="handleFocus"
      >
        <el-option
          v-for="option in (componentConfig as SelectComponent).options"
          :key="option.value"
          :label="option.label"
          :value="option.value"
          :disabled="option.disabled"
        />
      </el-select>
    </template>

    <!-- 单选框 -->
    <template v-else-if="componentConfig.type === 'radio'">
      <el-radio-group
        v-model="internalValue"
        :disabled="disabled || componentConfig.disabled"
        @change="emit('change', internalValue)"
      >
        <template v-if="(componentConfig as RadioComponent).buttonStyle">
          <el-radio-button
            v-for="option in (componentConfig as RadioComponent).options"
            :key="option.value"
            :label="option.value"
            :disabled="option.disabled"
          >
            {{ option.label }}
          </el-radio-button>
        </template>
        <template v-else>
          <el-radio
            v-for="option in (componentConfig as RadioComponent).options"
            :key="option.value"
            :label="option.value"
            :disabled="option.disabled"
          >
            {{ option.label }}
          </el-radio>
        </template>
      </el-radio-group>
    </template>

    <!-- 多选框 -->
    <template v-else-if="componentConfig.type === 'checkbox'">
      <el-checkbox-group
        v-model="internalValue"
        :disabled="disabled || componentConfig.disabled"
        :min="(componentConfig as CheckboxComponent).min"
        :max="(componentConfig as CheckboxComponent).max"
        @change="emit('change', internalValue)"
      >
        <el-checkbox
          v-for="option in (componentConfig as CheckboxComponent).options"
          :key="option.value"
          :label="option.value"
          :disabled="option.disabled"
        >
          {{ option.label }}
        </el-checkbox>
      </el-checkbox-group>
    </template>

    <!-- 日期选择器 -->
    <template v-else-if="componentConfig.type === 'date-picker'">
      <el-date-picker
        v-model="internalValue"
        :type="(componentConfig as DatePickerComponent).pickerType || 'date'"
        :placeholder="componentConfig.placeholder"
        :disabled="disabled || componentConfig.disabled"
        :clearable="(componentConfig as DatePickerComponent).clearable"
        :editable="(componentConfig as DatePickerComponent).editable"
        :format="(componentConfig as DatePickerComponent).format"
        :value-format="(componentConfig as DatePickerComponent).valueFormat"
        style="width: 100%"
        @blur="handleBlur"
        @focus="handleFocus"
      />
    </template>

    <!-- 时间选择器 -->
    <template v-else-if="componentConfig.type === 'time-picker'">
      <el-time-picker
        v-model="internalValue"
        :placeholder="componentConfig.placeholder"
        :disabled="disabled || componentConfig.disabled"
        :clearable="(componentConfig as TimePickerComponent).clearable"
        :editable="(componentConfig as TimePickerComponent).editable"
        :format="(componentConfig as TimePickerComponent).format"
        :value-format="(componentConfig as TimePickerComponent).valueFormat"
        :is-range="(componentConfig as TimePickerComponent).isRange"
        style="width: 100%"
        @blur="handleBlur"
        @focus="handleFocus"
      />
    </template>

    <!-- 数字输入框 -->
    <template v-else-if="componentConfig.type === 'input-number'">
      <el-input-number
        v-model="internalValue"
        :disabled="disabled || componentConfig.disabled"
        :min="(componentConfig as InputNumberComponent).min"
        :max="(componentConfig as InputNumberComponent).max"
        :step="(componentConfig as InputNumberComponent).step"
        :precision="(componentConfig as InputNumberComponent).precision"
        :controls="(componentConfig as InputNumberComponent).controls"
        :controls-position="(componentConfig as InputNumberComponent).controlsPosition"
        style="width: 100%"
        @blur="handleBlur"
        @focus="handleFocus"
      />
    </template>

    <!-- 滑块 -->
    <template v-else-if="componentConfig.type === 'slider'">
      <el-slider
        v-model="internalValue"
        :disabled="disabled || componentConfig.disabled"
        :min="(componentConfig as SliderComponent).min"
        :max="(componentConfig as SliderComponent).max"
        :step="(componentConfig as SliderComponent).step"
        :show-input="(componentConfig as SliderComponent).showInput"
        :show-stops="(componentConfig as SliderComponent).showStops"
        :range="(componentConfig as SliderComponent).range"
        :marks="(componentConfig as SliderComponent).marks"
        @change="emit('change', internalValue)"
      />
    </template>

    <!-- 开关 -->
    <template v-else-if="componentConfig.type === 'switch'">
      <el-switch
        v-model="internalValue"
        :disabled="disabled || componentConfig.disabled"
        :active-text="(componentConfig as SwitchComponent).activeText"
        :inactive-text="(componentConfig as SwitchComponent).inactiveText"
        :active-color="(componentConfig as SwitchComponent).activeColor"
        :inactive-color="(componentConfig as SwitchComponent).inactiveColor"
        :active-value="(componentConfig as SwitchComponent).activeValue"
        :inactive-value="(componentConfig as SwitchComponent).inactiveValue"
        @change="emit('change', internalValue)"
      />
    </template>

    <!-- 评分 -->
    <template v-else-if="componentConfig.type === 'rate'">
      <el-rate
        v-model="internalValue"
        :disabled="disabled || componentConfig.disabled"
        :max="(componentConfig as RateComponent).max"
        :allow-half="(componentConfig as RateComponent).allowHalf"
        :low-threshold="(componentConfig as RateComponent).lowThreshold"
        :high-threshold="(componentConfig as RateComponent).highThreshold"
        :colors="(componentConfig as RateComponent).colors"
        :void-color="(componentConfig as RateComponent).voidColor"
        :disabled-void-color="(componentConfig as RateComponent).disabledVoidColor"
        :show-text="(componentConfig as RateComponent).showText"
        :show-score="(componentConfig as RateComponent).showScore"
        :text-color="(componentConfig as RateComponent).textColor"
        :texts="(componentConfig as RateComponent).texts"
        @change="emit('change', internalValue)"
      />
    </template>

    <!-- 文件上传 -->
    <template v-else-if="componentConfig.type === 'upload'">
      <el-upload
        :action="(componentConfig as UploadComponent).action || '#'"
        :disabled="disabled || componentConfig.disabled"
        :multiple="(componentConfig as UploadComponent).multiple"
        :limit="(componentConfig as UploadComponent).limit"
        :file-list="internalValue || []"
        :accept="(componentConfig as UploadComponent).accept"
        :list-type="(componentConfig as UploadComponent).listType"
        :auto-upload="(componentConfig as UploadComponent).autoUpload"
        :show-file-list="(componentConfig as UploadComponent).showFileList"
        :before-upload="beforeUpload"
        @success="handleUploadSuccess"
        @remove="handleUploadRemove"
      >
        <template v-if="(componentConfig as UploadComponent).listType === 'picture-card'">
          <el-icon><Plus /></el-icon>
        </template>
        <template v-else>
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            点击上传
          </el-button>
        </template>
      </el-upload>
    </template>

    <!-- 颜色选择器 -->
    <template v-else-if="componentConfig.type === 'color-picker'">
      <el-color-picker
        v-model="internalValue"
        :disabled="disabled || componentConfig.disabled"
        :show-alpha="(componentConfig as ColorPickerComponent).showAlpha"
        :color-format="(componentConfig as ColorPickerComponent).colorFormat"
        :predefine="(componentConfig as ColorPickerComponent).predefine"
        @change="emit('change', internalValue)"
      />
    </template>

    <!-- 未知组件类型 -->
    <template v-else>
      <el-alert
        :title="`未知组件类型: ${componentConfig.type}`"
        type="error"
        show-icon
        :closable="false"
      />
    </template>
  </div>
</template>

<style scoped>
.form-component-renderer {
  width: 100%;
}
</style>