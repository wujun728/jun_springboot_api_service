# IconSelector 图标选择器组件

## 功能特性

- 🎨 **可视化选择**：提供Element Plus所有图标的可视化选择界面
- 🔍 **搜索功能**：支持按图标名称搜索
- 📂 **分类筛选**：提供常用、导航、操作等分类快速筛选
- 🎯 **实时预览**：选择后立即显示图标效果
- 🌙 **主题适配**：支持明暗主题切换
- 📱 **响应式设计**：适配不同屏幕尺寸

## 使用方法

### 基础用法

```vue
<template>
  <IconSelector v-model="selectedIcon" />
</template>

<script setup>
import { ref } from 'vue'
import IconSelector from '@/components/IconSelector/index.vue'

const selectedIcon = ref('')
</script>
```

### 在表单中使用

```vue
<template>
  <el-form-item label="图标" prop="icon">
    <IconSelector v-model="form.icon" />
  </el-form-item>
</template>

<script setup>
import { ref, reactive } from 'vue'
import IconSelector from '@/components/IconSelector/index.vue'

const form = reactive({
  icon: 'Setting'
})
</script>
```

## API

### Props

| 参数 | 说明 | 类型 | 默认值 |
|------|------|------|--------|
| modelValue | 选中的图标名称 | String | '' |

### Events

| 事件名 | 说明 | 回调参数 |
|--------|------|----------|
| update:modelValue | 图标选择变化时触发 | (iconName: string) |

## 图标分类

### 常用图标
包含最常用的图标，如：Home、Setting、User、Menu、Document、Folder等

### 导航图标
包含导航相关的图标，如：ArrowLeft、ArrowRight、Expand、Fold等

### 操作图标
包含操作相关的图标，如：Plus、Edit、Delete、Search、Refresh等

## 注意事项

1. 组件会自动导入Element Plus的所有图标
2. 图标名称与Element Plus图标组件名称一致
3. 支持搜索和分类筛选，方便快速找到所需图标
4. 选择器支持键盘操作，可通过Tab键导航
5. 组件会自动适配当前主题（明暗模式）

## 示例

```vue
<template>
  <div>
    <h3>当前选择的图标：{{ selectedIcon || '未选择' }}</h3>
    <IconSelector v-model="selectedIcon" />
    
    <!-- 显示选中的图标 -->
    <div v-if="selectedIcon" style="margin-top: 20px;">
      <el-icon style="font-size: 24px;">
        <component :is="selectedIcon" />
      </el-icon>
      <span style="margin-left: 8px;">{{ selectedIcon }}</span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import IconSelector from '@/components/IconSelector/index.vue'

const selectedIcon = ref('Setting')
</script>
``` 