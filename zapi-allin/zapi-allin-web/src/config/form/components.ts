import type { ComponentCategory, ComponentType } from '@/types/form'

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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
  },
  
  'number': {
    type: 'number',
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
  },
  
  switch: {
    type: 'switch',
    label: '开关',
    name: '',
    required: false,
    disabled: false,
    activeText: '',
    inactiveText: '',
    activeValue: 1,
    inactiveValue: 0,
    icon: 'SwitchButton',
    defaultValue: 0,
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
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
    span: 12  // 12列系统，渲染时乘以2变成24（满宽）
  },
  
  // 业务组件
  'user-select': {
    type: 'user-select',
    label: '人员选择',
    name: '',
    placeholder: '请选择人员',
    required: false,
    disabled: false,
    clearable: true,
    filterable: true,
    multiple: false,
    icon: 'User',
    defaultValue: '',
    span: 12
  },
  
  'department-select': {
    type: 'department-select',
    label: '部门选择',
    name: '',
    placeholder: '请选择部门',
    required: false,
    disabled: false,
    clearable: true,
    filterable: true,
    multiple: false,
    checkStrictly: true,  // 是否可以选择任意级别
    icon: 'OfficeBuilding',
    defaultValue: '',
    span: 12
  },
  
  'role-select': {
    type: 'role-select',
    label: '角色选择',
    name: '',
    placeholder: '请选择角色',
    required: false,
    disabled: false,
    clearable: true,
    filterable: true,
    multiple: false,
    icon: 'UserFilled',
    defaultValue: '',
    span: 12
  }
}

// 组件分类配置
export const componentCategories: ComponentCategory[] = [
  {
    name: 'business',
    label: '业务组件',
    icon: 'Briefcase',
    components: [
      {
        type: 'user-select',
        label: '人员选择',
        icon: 'User',
        defaultProps: componentDefaults['user-select']
      },
      {
        type: 'department-select',
        label: '部门选择',
        icon: 'OfficeBuilding',
        defaultProps: componentDefaults['department-select']
      },
      {
        type: 'role-select',
        label: '角色选择',
        icon: 'UserFilled',
        defaultProps: componentDefaults['role-select']
      }
    ]
  },
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
        type: 'number',
        label: '数字输入',
        icon: 'Plus',
        defaultProps: componentDefaults['number']
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

  const defaults = componentDefaults[type]
  if (!defaults) {

    throw new Error(`Unknown component type: ${type}`)
  }
  
  const timestamp = Date.now()
  const random = Math.random().toString(36).substr(2, 5) // 缩短随机后缀到5位
  
  // 字段名中的 - 转换为 _，确保字段名合法
  const safeType = type.replace(/-/g, '_')
  
  // 字段名控制在25个字符以内，格式: {type}_{random}
  const fieldName = `${safeType}_${random}`.substring(0, 25)
  
  const newComponent = {
    ...JSON.parse(JSON.stringify(defaults)), // 深度克隆防止引用问题
    id: `component_${timestamp}_${random}`,
    name: fieldName, // 简短字段名，最多15字符
    label: `${defaults.label}_${Math.floor(Math.random() * 1000)}`
  }

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
