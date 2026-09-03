# 表单设计器

## 概述

本表单设计器是从 `zapi-form-vue` 项目迁移而来的功能完整的可视化表单设计工具。

## 目录结构

```
src/
├── components/form-designer/     # 表单设计器组件
│   ├── ComponentsPanel.vue      # 组件面板
│   ├── DesignCanvas.vue         # 设计画布
│   ├── PropertiesPanel.vue      # 属性面板
│   ├── FormComponentRenderer.vue # 表单组件渲染器
│   ├── FormPreview.vue          # 表单预览
│   ├── DrawerCodeEditor.vue     # 代码编辑器抽屉
│   └── MonacoEditor.vue         # Monaco代码编辑器
├── config/form/                 # 表单配置
│   └── components.ts            # 组件配置定义
├── stores/form/                 # 表单状态管理
│   └── formDesigner.ts          # 表单设计器Store
├── types/form/                  # 类型定义
│   └── index.ts                 # 表单相关类型
└── views/form/design/           # 表单设计页面
    └── index.vue                # 主页面
```

## 主要功能

### 1. 组件库
- 输入框 (input)
- 多行文本 (textarea)
- 下拉选择 (select)
- 单选框 (radio)
- 多选框 (checkbox)
- 日期选择 (date-picker)
- 时间选择 (time-picker)
- 数字输入 (input-number)
- 滑块 (slider)
- 开关 (switch)
- 评分 (rate)
- 上传 (upload)
- 颜色选择 (color-picker)

### 2. 设计画布
- 拖拽添加组件
- 组件排序
- 组件选中/删除
- 实时预览

### 3. 属性面板
- 表单全局设置
- 组件属性配置
- 实时属性更新

### 4. 功能操作
- 表单预览
- 表单保存
- 清空画布
- 查看代码

## 使用方法

### 1. 基础使用

访问 `/form/design` 路由即可进入表单设计器。

### 2. 保存表单

目前表单数据保存到本地存储，可以扩展为调用后端API：

```javascript
// 在 saveForm 方法中
const formData = formDesignerStore.exportForm()
// 调用后端API保存
// await formApi.createForm({ 
//   formName: formData.name,
//   formConfig: JSON.stringify(formData)
// })
```

### 3. 加载表单

可以从后端加载已保存的表单：

```javascript
// 加载表单配置
const loadForm = async (formId) => {
  const response = await formApi.getFormDetail(formId)
  const formConfig = JSON.parse(response.data.formConfig)
  formDesignerStore.loadForm(formConfig)
}
```

## 依赖包

新安装的依赖包：
- `vuedraggable@2.24.3` - 拖拽功能
- `@monaco-editor/loader@1.5.0` - Monaco编辑器加载器
- `monaco-editor@0.53.0` - Monaco代码编辑器

## 扩展开发

### 1. 添加新组件

1. 在 `config/form/components.ts` 中添加组件配置
2. 在 `types/form/index.ts` 中添加组件类型定义
3. 在 `FormComponentRenderer.vue` 中添加组件渲染逻辑

### 2. 自定义属性面板

在 `PropertiesPanel.vue` 中添加组件特定的属性配置界面。

### 3. 表单验证

组件支持标准的表单验证规则，可在属性面板中配置。

## 注意事项

1. 确保项目已安装所有必要的依赖
2. Monaco编辑器需要网络访问或本地CDN
3. 拖拽功能在移动端可能需要额外适配
4. 表单数据的保存和加载需要根据实际后端API调整

## 技术栈

- Vue 3
- TypeScript
- Element Plus
- Pinia
- Monaco Editor
- Vue Draggable
