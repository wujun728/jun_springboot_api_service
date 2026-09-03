<template>
  <div class="settings-section">
    <h3>个人设置</h3>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>用户名</h4>
        <p>设置您的显示名称</p>
      </div>
      <el-input v-model="settings.username" placeholder="请输入用户名" style="width: 300px;" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>邮箱</h4>
        <p>您的联系邮箱地址</p>
      </div>
      <el-input v-model="settings.email" placeholder="请输入邮箱" style="width: 300px;" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>手机号</h4>
        <p>您的联系电话</p>
      </div>
      <el-input v-model="settings.phone" placeholder="请输入手机号" style="width: 300px;" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>个人简介</h4>
        <p>简短的个人介绍</p>
      </div>
      <el-input 
        v-model="settings.bio" 
        type="textarea" 
        :rows="3"
        placeholder="请输入个人简介"
        style="width: 400px;"
      />
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