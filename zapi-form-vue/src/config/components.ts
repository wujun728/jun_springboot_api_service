import type { ComponentCategory, ComponentType } from '../types/form'

// 组件默认配置
export const componentDefaults: Record<string, Partial<any>> = {
  input: {
    type: 'input',
    label: '输入框',
    name: '',
    placeholder: '请输入内容',
    required: false,
    disabled: false,
    clearable: true,
    inputType: 'text',
    maxlength: undefined,
    showWordLimit: false,
    icon: 'Edit',
    defaultValue: '',
    span: 12
  },
  
  textarea: {
    type: 'textarea',
    label: '多行文本',
    name: '',
    placeholder: '请输入内容',
    required: false,
    disabled: false,
    inputType: 'textarea',
    rows: 4,
    icon: 'Document',
    defaultValue: '',
    span: 12
  },
  
  select: {
    type: 'select',
    label: '下拉选择',
    name: '',
    placeholder: '请选择',
    required: false,
    disabled: false,
    clearable: true,
    filterable: false,
    multiple: false,
    options: [
      { label: '选项1', value: '1' },
      { label: '选项2', value: '2' }
    ],
    icon: 'ArrowDown',
    defaultValue: '',
    span: 12
  },
  
  radio: {
    type: 'radio',
    label: '单选框',
    name: '',
    required: false,
    disabled: false,
    buttonStyle: false,
    options: [
      { label: '选项1', value: '1' },
      { label: '选项2', value: '2' }
    ],
    icon: 'CircleCheck',
    defaultValue: '',
    span: 12
  },
  
  checkbox: {
    type: 'checkbox',
    label: '多选框',
    name: '',
    required: false,
    disabled: false,
    options: [
      { label: '选项1', value: '1' },
      { label: '选项2', value: '2' }
    ],
    icon: 'Select',
    defaultValue: [],
    span: 12
  },
  
  'date-picker': {
    type: 'date-picker',
    label: '日期选择',
    name: '',
    placeholder: '请选择日期',
    required: false,
    disabled: false,
    clearable: true,
    editable: true,
    pickerType: 'date',
    format: 'YYYY-MM-DD',
    valueFormat: 'YYYY-MM-DD',
    icon: 'Calendar',
    defaultValue: '',
    span: 12
  },
  
  'time-picker': {
    type: 'time-picker',
    label: '时间选择',
    name: '',
    placeholder: '请选择时间',
    required: false,
    disabled: false,
    clearable: true,
    editable: true,
    format: 'HH:mm:ss',
    valueFormat: 'HH:mm:ss',
    icon: 'Clock',
    defaultValue: '',
    span: 12
  },
  
  'input-number': {
    type: 'input-number',
    label: '数字输入',
    name: '',
    required: false,
    disabled: false,
    controls: true,
    min: undefined,
    max: undefined,
    step: 1,
    precision: undefined,
    icon: 'Plus',
    defaultValue: 0,
    span: 12
  },
  
  slider: {
    type: 'slider',
    label: '滑块',
    name: '',
    required: false,
    disabled: false,
    min: 0,
    max: 100,
    step: 1,
    showInput: false,
    showStops: false,
    range: false,
    icon: 'Minus',
    defaultValue: 0,
    span: 12
  },
  
  switch: {
    type: 'switch',
    label: '开关',
    name: '',
    required: false,
    disabled: false,
    activeText: '',
    inactiveText: '',
    activeValue: true,
    inactiveValue: false,
    icon: 'SwitchButton',
    defaultValue: false,
    span: 12
  },
  
  rate: {
    type: 'rate',
    label: '评分',
    name: '',
    required: false,
    disabled: false,
    max: 5,
    allowHalf: false,
    showText: false,
    showScore: false,
    icon: 'Star',
    defaultValue: 0,
    span: 12
  },
  
  upload: {
    type: 'upload',
    label: '文件上传',
    name: '',
    required: false,
    disabled: false,
    multiple: false,
    limit: 1,
    accept: '',
    listType: 'text',
    autoUpload: true,
    showFileList: true,
    icon: 'Upload',
    defaultValue: [],
    span: 12
  },
  
  'color-picker': {
    type: 'color-picker',
    label: '颜色选择',
    name: '',
    required: false,
    disabled: false,
    showAlpha: false,
    colorFormat: 'hex',
    icon: 'Brush',
    defaultValue: '',
    span: 12
  }
}

// 组件分类配置
export const componentCategories: ComponentCategory[] = [
  {
    name: 'basic',
    label: '基础组件',
    icon: 'Grid',
    components: [
      {
        type: 'input',
        label: '输入框',
        icon: 'Edit',
        defaultProps: componentDefaults.input
      },
      {
        type: 'textarea',
        label: '多行文本',
        icon: 'Document',
        defaultProps: componentDefaults.textarea
      },
      {
        type: 'input-number',
        label: '数字输入',
        icon: 'Plus',
        defaultProps: componentDefaults['input-number']
  },
      {
        type: 'select',
        label: '下拉选择',
        icon: 'ArrowDown',
        defaultProps: componentDefaults.select
      },
      {
        type: 'radio',
        label: '单选框',
        icon: 'CircleCheck',
        defaultProps: componentDefaults.radio
      },
      {
        type: 'checkbox',
        label: '多选框',
        icon: 'Select',
        defaultProps: componentDefaults.checkbox
      },
      {
        type: 'switch',
        label: '开关',
        icon: 'SwitchButton',
        defaultProps: componentDefaults.switch
  },
      {
        type: 'date-picker',
        label: '日期选择',
        icon: 'Calendar',
        defaultProps: componentDefaults['date-picker']
      },
      {
        type: 'time-picker',
        label: '时间选择',
        icon: 'Clock',
        defaultProps: componentDefaults['time-picker']
      }
    ]
  },
  {
    name: 'advanced',
    label: '高级组件',
    icon: 'Setting',
    components: [
      {
        type: 'slider',
        label: '滑块',
        icon: 'Minus',
        defaultProps: componentDefaults.slider
      },
      {
        type: 'rate',
        label: '评分',
        icon: 'Star',
        defaultProps: componentDefaults.rate
      },
      {
        type: 'color-picker',
        label: '颜色选择',
        icon: 'Brush',
        defaultProps: componentDefaults['color-picker']
      },
      {
        type: 'upload',
        label: '文件上传',
        icon: 'Upload',
        defaultProps: componentDefaults.upload
      }
    ]
  }
]

// 获取组件默认配置
export const getComponentDefault = (type: string) => {
  console.log('正在创建组件:', type)
  
  const defaults = componentDefaults[type]
  if (!defaults) {
    console.error(`未知的组件类型: ${type}`)
    throw new Error(`Unknown component type: ${type}`)
  }
  
  const timestamp = Date.now()
  const random = Math.random().toString(36).substr(2, 9)
  
  const newComponent = {
    ...JSON.parse(JSON.stringify(defaults)), // 深度克隆防止引用问题
    id: `component_${timestamp}_${random}`,
    name: `field_${type}_${random}`, // 使用random而不是timestamp确保唯一性
    label: `${defaults.label}_${Math.floor(Math.random() * 1000)}`
  }
  
  console.log('组件创建成功:', newComponent)
  return newComponent
}

// 获取所有组件类型
export const getAllComponentTypes = (): ComponentType[] => {
  return componentCategories.reduce((acc, category) => {
    return acc.concat(category.components)
  }, [] as ComponentType[])
}

// 根据类型获取组件信息
export const getComponentTypeInfo = (type: string): ComponentType | undefined => {
  const allTypes = getAllComponentTypes()
  return allTypes.find(item => item.type === type)
}