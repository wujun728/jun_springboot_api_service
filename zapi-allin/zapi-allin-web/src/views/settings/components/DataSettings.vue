<template>
  <div class="settings-section">
    <h3>数据设置</h3>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>数据备份</h4>
        <p>自动备份系统数据</p>
      </div>
      <el-switch v-model="settings.autoBackup" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>备份频率</h4>
        <p>设置数据备份的频率</p>
      </div>
      <el-select v-model="settings.backupFrequency" placeholder="选择备份频率">
        <el-option label="每日" value="daily" />
        <el-option label="每周" value="weekly" />
        <el-option label="每月" value="monthly" />
      </el-select>
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>数据保留</h4>
        <p>设置备份数据的保留时间</p>
      </div>
      <el-select v-model="settings.dataRetention" placeholder="选择保留时间">
        <el-option label="30天" value="30" />
        <el-option label="90天" value="90" />
        <el-option label="180天" value="180" />
        <el-option label="365天" value="365" />
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