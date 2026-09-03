# AvatarUpload 头像上传组件

## 功能特性

- 📸 支持图片文件上传
- 🖼️ 实时头像预览
- 📏 文件大小限制（默认2MB）
- 🗑️ 头像删除功能
- 🔄 上传进度显示
- 📱 响应式设计

## 使用方法

### 基础用法

```vue
<template>
  <AvatarUpload
    v-model="avatarId"
    :max-size="2 * 1024 * 1024"
    @upload-success="handleUploadSuccess"
    @upload-error="handleUploadError"
    @remove="handleRemove"
  />
</template>

<script setup>
import { ref } from 'vue'
import AvatarUpload from '@/components/AvatarUpload/index.vue'

const avatarId = ref('')

const handleUploadSuccess = (fileInfo) => {
  console.log('上传成功:', fileInfo)
}

const handleUploadError = (error) => {
  console.error('上传失败:', error)
}

const handleRemove = () => {
  console.log('头像已删除')
}
</script>
```

## Props

| 参数 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| modelValue | 头像文件ID或URL | String/Number | '' |
| maxSize | 最大文件大小（字节） | Number | 2 * 1024 * 1024 |
| disabled | 是否禁用 | Boolean | false |

## Events

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| update:modelValue | 头像ID更新时触发 | (value: string \| number) |
| upload-success | 上传成功时触发 | (fileInfo: object) |
| upload-error | 上传失败时触发 | (error: Error) |
| remove | 删除头像时触发 | - |

## 数据流

1. 用户选择图片文件
2. 组件验证文件类型和大小
3. 自动上传到服务器
4. 返回文件ID并更新v-model
5. 显示上传的头像（通过文件下载接口）

## 注意事项

- 只支持图片格式文件（image/*）
- 头像预览使用 `/api/file/download/{fileId}` 接口
- 删除头像时会同时删除服务器文件
- 上传路径固定为 'avatar'
