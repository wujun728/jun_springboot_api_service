<template>
  <div class="settings-section">
    <h3>安全设置</h3>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>双因素认证</h4>
        <p>启用双因素认证以提高账户安全性</p>
      </div>
      <el-switch v-model="settings.twoFactorAuth" />
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>会话超时</h4>
        <p>设置用户会话的超时时间</p>
      </div>
      <el-select v-model="settings.sessionTimeout" placeholder="选择超时时间">
        <el-option label="15分钟" value="15" />
        <el-option label="30分钟" value="30" />
        <el-option label="1小时" value="60" />
        <el-option label="4小时" value="240" />
        <el-option label="永不" value="0" />
      </el-select>
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>密码策略</h4>
        <p>设置密码的复杂度要求</p>
      </div>
      <el-checkbox-group v-model="settings.passwordPolicy">
        <el-checkbox label="uppercase">必须包含大写字母</el-checkbox>
        <el-checkbox label="lowercase">必须包含小写字母</el-checkbox>
        <el-checkbox label="numbers">必须包含数字</el-checkbox>
        <el-checkbox label="symbols">必须包含特殊字符</el-checkbox>
        <el-checkbox label="minLength">最小长度8位</el-checkbox>
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