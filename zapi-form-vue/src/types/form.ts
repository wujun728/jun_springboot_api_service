// 表单组件基础类型定义
export interface BaseFormComponent {
  id: string
  type: string
  label: string
  name: string
  required?: boolean
  placeholder?: string
  disabled?: boolean
  rules?: FormValidationRule[]
  style?: ComponentStyle
  // 栅格宽度（1-12），用于在设计画布上控制组件占位宽度
  span?: number
}

// 表单验证规则
export interface FormValidationRule {
  required?: boolean
  message?: string
  trigger?: 'blur' | 'change'
  type?: 'string' | 'number' | 'boolean' | 'method' | 'regexp' | 'integer' | 'float' | 'array' | 'object' | 'enum' | 'date' | 'url' | 'hex' | 'email'
  min?: number
  max?: number
  pattern?: RegExp
  validator?: (rule: any, value: any, callback: any) => void
}

// 组件样式
export interface ComponentStyle {
  width?: string
  height?: string
  margin?: string
  padding?: string
  fontSize?: string
  color?: string
  backgroundColor?: string
  border?: string
  borderRadius?: string
}

// 输入框组件
export interface InputComponent extends BaseFormComponent {
  type: 'input'
  inputType?: 'text' | 'password' | 'textarea' | 'number'
  maxlength?: number
  showWordLimit?: boolean
  clearable?: boolean
  showPassword?: boolean
  prefixIcon?: string
  suffixIcon?: string
  rows?: number
}

// 选择器组件
export interface SelectComponent extends BaseFormComponent {
  type: 'select'
  multiple?: boolean
  clearable?: boolean
  filterable?: boolean
  options: SelectOption[]
  remote?: boolean
  remoteMethod?: string
}

// 选择器选项
export interface SelectOption {
  label: string
  value: string | number
  disabled?: boolean
}

// 单选框组件
export interface RadioComponent extends BaseFormComponent {
  type: 'radio'
  options: RadioOption[]
  buttonStyle?: boolean
}

// 单选框选项
export interface RadioOption {
  label: string
  value: string | number
  disabled?: boolean
}

// 多选框组件
export interface CheckboxComponent extends BaseFormComponent {
  type: 'checkbox'
  options: CheckboxOption[]
  min?: number
  max?: number
}

// 多选框选项
export interface CheckboxOption {
  label: string
  value: string | number
  disabled?: boolean
}

// 日期选择器组件
export interface DatePickerComponent extends BaseFormComponent {
  type: 'date-picker'
  pickerType?: 'date' | 'datetime' | 'daterange' | 'datetimerange' | 'month' | 'year'
  format?: string
  valueFormat?: string
  clearable?: boolean
  editable?: boolean
}

// 时间选择器组件
export interface TimePickerComponent extends BaseFormComponent {
  type: 'time-picker'
  format?: string
  valueFormat?: string
  clearable?: boolean
  editable?: boolean
  isRange?: boolean
}

// 数字输入框组件
export interface InputNumberComponent extends BaseFormComponent {
  type: 'input-number'
  min?: number
  max?: number
  step?: number
  precision?: number
  controls?: boolean
  controlsPosition?: 'right' | ''
}

// 滑块组件
export interface SliderComponent extends BaseFormComponent {
  type: 'slider'
  min?: number
  max?: number
  step?: number
  showInput?: boolean
  showStops?: boolean
  range?: boolean
  marks?: Record<number, string>
}

// 开关组件
export interface SwitchComponent extends BaseFormComponent {
  type: 'switch'
  activeText?: string
  inactiveText?: string
  activeColor?: string
  inactiveColor?: string
  activeValue?: string | number | boolean
  inactiveValue?: string | number | boolean
}

// 评分组件
export interface RateComponent extends BaseFormComponent {
  type: 'rate'
  max?: number
  allowHalf?: boolean
  lowThreshold?: number
  highThreshold?: number
  colors?: string[]
  voidColor?: string
  disabledVoidColor?: string
  iconClasses?: string[]
  voidIconClass?: string
  disabledVoidIconClass?: string
  showText?: boolean
  showScore?: boolean
  textColor?: string
  texts?: string[]
}

// 上传组件
export interface UploadComponent extends BaseFormComponent {
  type: 'upload'
  action?: string
  multiple?: boolean
  limit?: number
  fileList?: any[]
  accept?: string
  listType?: 'text' | 'picture' | 'picture-card'
  autoUpload?: boolean
  showFileList?: boolean
}

// 颜色选择器组件
export interface ColorPickerComponent extends BaseFormComponent {
  type: 'color-picker'
  showAlpha?: boolean
  colorFormat?: 'hsl' | 'hsv' | 'hex' | 'rgb'
  predefine?: string[]
}

// 联合类型：所有表单组件
export type FormComponent = 
  | InputComponent 
  | SelectComponent 
  | RadioComponent 
  | CheckboxComponent 
  | DatePickerComponent 
  | TimePickerComponent 
  | InputNumberComponent 
  | SliderComponent 
  | SwitchComponent 
  | RateComponent 
  | UploadComponent 
  | ColorPickerComponent

// 表单配置
export interface FormConfig {
  id: string
  name: string
  description?: string
  labelWidth?: string
  labelPosition?: 'left' | 'right' | 'top'
  size?: 'large' | 'default' | 'small'
  disabled?: boolean
  inline?: boolean
  components: FormComponent[]
  style?: ComponentStyle
}

// 组件分类
export interface ComponentCategory {
  name: string
  label: string
  icon: string
  components: ComponentType[]
}

// 组件类型信息
export interface ComponentType {
  type: string
  label: string
  icon: string
  defaultProps: Partial<FormComponent>
}