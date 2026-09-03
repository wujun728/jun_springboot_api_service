<template>
  <div class="settings-section">
    <h3>基本设置</h3>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>系统名称</h4>
        <p>设置显示在浏览器标题栏和登录页面的系统名称</p>
      </div>
      <el-input v-model="settings.systemName" placeholder="输入系统名称" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>系统描述</h4>
        <p>系统的简要描述信息</p>
      </div>
      <el-input 
        v-model="settings.systemDescription" 
        type="textarea" 
        :rows="3"
        placeholder="输入系统描述"
      />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>时区设置</h4>
        <p>选择系统使用的时区</p>
      </div>
      <el-select v-model="settings.timezone" placeholder="选择时区">
        <el-option label="UTC+8 (北京时间)" value="UTC+8" />
        <el-option label="UTC+0 (格林威治时间)" value="UTC+0" />
        <el-option label="UTC-5 (东部时间)" value="UTC-5" />
      </el-select>
    </div>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'

const props = defineProps({
  settings: {
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.settings-section {
  h3 {
    font-size: 20px;
    font-weight: 600;
    color: var(--el-text-color-primary);
    margin-bottom: 24px;
  }
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px 0;
  border-bottom: 1px solid var(--el-border-color-lighter);
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-info {
  flex: 1;
  margin-right: 24px;
}

.setting-info h4 {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.setting-info p {
  color: var(--el-text-color-secondary);
  font-size: 14px;
  line-height: 1.5;
}

.setting-item :deep(.el-input__inner),
.setting-item :deep(.el-textarea__inner) {
  border-color: var(--el-border-color-light);
  background-color: var(--el-fill-color-light);
  color: var(--el-text-color-regular);
  border-radius: 6px;
}

.setting-item :deep(.el-input__inner:focus),
.setting-item :deep(.el-textarea__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

.setting-item :deep(.el-select .el-input__inner) {
  border-color: var(--el-border-color-light);
  background-color: var(--el-fill-color-light);
  border-radius: 6px;
}

.setting-item :deep(.el-select .el-input__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

@media (max-width: 768px) {
  .setting-item {
    flex-direction: column;
    gap: 16px;
  }
  
  .setting-info {
    margin-right: 0;
  }
}
</style> 