<template>
  <div class="notifications-page">
    <div class="page-header">
      <div class="header-content">
          <h1 class="page-title">通知设置</h1>
      </div>
      <div class="header-actions">
        <el-button @click="resetSettings">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
        <el-button type="primary" @click="saveSettings">
          <el-icon><Check /></el-icon>
          保存
        </el-button>
      </div>
    </div>

    <div class="main-content">
      <div class="content-card">
        <div class="settings-content">
          <el-form :model="notificationSettings" label-width="120px" class="settings-form">
            <el-form-item label="系统通知">
              <el-switch v-model="notificationSettings.systemNotification" />
            </el-form-item>
            <el-form-item label="邮件通知">
              <el-switch v-model="notificationSettings.emailNotification" />
            </el-form-item>
            <el-form-item label="短信通知">
              <el-switch v-model="notificationSettings.smsNotification" />
            </el-form-item>
            <el-form-item label="桌面通知">
              <el-switch v-model="notificationSettings.desktopNotification" />
            </el-form-item>
            <el-form-item label="通知频率">
              <el-select v-model="notificationSettings.frequency" placeholder="请选择通知频率">
                <el-option label="实时" value="realtime" />
                <el-option label="每小时" value="hourly" />
                <el-option label="每天" value="daily" />
                <el-option label="每周" value="weekly" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveSettings">保存设置</el-button>
              <el-button @click="resetSettings">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { Refresh, Check } from '@element-plus/icons-vue'

const notificationSettings = reactive({
  systemNotification: true,
  emailNotification: false,
  smsNotification: false,
  desktopNotification: true,
  frequency: 'realtime'
})

const saveSettings = () => {
  ElMessage.success('设置保存成功')
}

const resetSettings = () => {
  notificationSettings.systemNotification = true
  notificationSettings.emailNotification = false
  notificationSettings.smsNotification = false
  notificationSettings.desktopNotification = true
  notificationSettings.frequency = 'realtime'
  ElMessage.info('设置已重置')
}
</script>

<style scoped>
@import '../index.scss';

.settings-content {
  padding: 20px 0;
}

.settings-form {
  max-width: 500px;
}

.settings-form :deep(.el-form-item__label) {
  color: var(--el-text-color-primary);
  font-weight: 500;
}

.settings-form :deep(.el-input__inner),
.settings-form :deep(.el-textarea__inner) {
  border-color: var(--el-border-color-light);
  background-color: var(--el-fill-color-light);
  color: var(--el-text-color-regular);
  border-radius: 6px;
}

.settings-form :deep(.el-input__inner:focus),
.settings-form :deep(.el-textarea__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

.settings-form :deep(.el-select .el-input__inner) {
  border-color: var(--el-border-color-light);
  background-color: var(--el-fill-color-light);
  border-radius: 6px;
}

.settings-form :deep(.el-select .el-input__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}
</style>

