<template>
  <div class="settings-section">
    <h3>通知设置</h3>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>邮件通知</h4>
        <p>接收系统相关的邮件通知</p>
      </div>
      <el-switch v-model="settings.emailNotifications" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>浏览器通知</h4>
        <p>在浏览器中显示系统通知</p>
      </div>
      <el-switch v-model="settings.browserNotifications" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>通知声音</h4>
        <p>播放通知提示音</p>
      </div>
      <el-switch v-model="settings.notificationSound" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>通知类型</h4>
        <p>选择接收哪些类型的通知</p>
      </div>
      <el-checkbox-group v-model="settings.notificationTypes">
        <el-checkbox label="system">系统通知</el-checkbox>
        <el-checkbox label="security">安全提醒</el-checkbox>
        <el-checkbox label="updates">更新提醒</el-checkbox>
        <el-checkbox label="messages">消息通知</el-checkbox>
      </el-checkbox-group>
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

<style lang="scss" scoped>
.settings-section {
  h3 {
    font-size: 20px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 24px;
  }
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 20px 0;
  border-bottom: 1px solid var(--border-color);
  
  &:last-child {
    border-bottom: none;
  }
}

.setting-info {
  flex: 1;
  margin-right: 24px;
  
  h4 {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 4px;
  }
  
  p {
    color: var(--text-secondary);
    font-size: 14px;
    line-height: 1.5;
  }
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