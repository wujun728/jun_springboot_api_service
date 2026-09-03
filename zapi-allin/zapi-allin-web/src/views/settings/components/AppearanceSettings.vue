<template>
  <div class="settings-section">
    <h3>外观设置</h3>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>主题模式</h4>
        <p>选择系统的主题模式</p>
      </div>
      <div class="theme-mode-selector">
        <div 
          v-for="mode in themeModes" 
          :key="mode.value"
          class="theme-mode-option"
          :class="{ active: appStore.theme === mode.value }"
          @click="handleThemeChange(mode.value)"
        >
          <div class="mode-preview" :class="mode.value">
            <div class="preview-header"></div>
            <div class="preview-sidebar"></div>
            <div class="preview-content"></div>
          </div>
          <div class="mode-info">
            <h5>{{ mode.label }}</h5>
            <p>{{ mode.description }}</p>
          </div>
        </div>
      </div>
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>主色调</h4>
        <p>选择系统的主色调，影响按钮、链接等元素的颜色</p>
      </div>
      <div class="color-picker">
        <div 
          v-for="color in themeColors" 
          :key="color.name"
          class="color-option"
          :class="{ active: appStore.primaryColor === color.value }"
          @click="handlePrimaryColorChange(color.value)"
        >
          <div class="color-preview" :style="{ background: color.value }"></div>
          <div class="color-info">
            <span class="color-name">{{ color.name }}</span>
            <span class="color-value">{{ color.value }}</span>
          </div>
          <el-icon v-if="appStore.primaryColor === color.value" class="check-icon"><Check /></el-icon>
        </div>
      </div>
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>侧边栏样式</h4>
        <p>自定义侧边栏的外观和行为</p>
      </div>
      <div class="sidebar-settings">
        <div class="setting-row">
          <label>侧边栏宽度</label>
          <el-slider 
            v-model="settings.sidebarWidth" 
            :min="200" 
            :max="300" 
            :step="10"
            show-input
            style="width: 200px;"
          />
        </div>
        <div class="setting-row">
          <label>侧边栏位置</label>
          <el-radio-group v-model="settings.sidebarPosition">
            <el-radio label="left">左侧</el-radio>
            <el-radio label="right">右侧</el-radio>
          </el-radio-group>
        </div>
        <div class="setting-row">
          <label>侧边栏行为</label>
          <el-checkbox-group v-model="settings.sidebarBehavior">
            <el-checkbox label="collapsible">可折叠</el-checkbox>
            <el-checkbox label="fixed">固定位置</el-checkbox>
            <el-checkbox label="overlay">悬浮显示</el-checkbox>
          </el-checkbox-group>
        </div>
      </div>
    </div>
    
    <div class="setting-item">
      <div class="setting-info">
        <h4>界面元素</h4>
        <p>自定义界面元素的显示效果</p>
      </div>
      <div class="interface-settings">
        <div class="setting-row">
          <label>圆角大小</label>
          <el-slider 
            v-model="settings.borderRadius" 
            :min="0" 
            :max="20" 
            :step="2"
            show-input
            style="width: 200px;"
          />
        </div>
        <div class="setting-row">
          <label>阴影效果</label>
          <el-select v-model="settings.shadowLevel" placeholder="选择阴影级别">
            <el-option label="无阴影" value="none" />
            <el-option label="轻微阴影" value="light" />
            <el-option label="中等阴影" value="medium" />
            <el-option label="强烈阴影" value="heavy" />
          </el-select>
        </div>
        <div class="setting-row">
          <label>动画效果</label>
          <el-switch v-model="settings.animations" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref } from 'vue'
import { useAppStore } from '@/stores/app'

const props = defineProps({
  settings: {
    type: Object,
    required: true
  }
})

// 使用 app store
const appStore = useAppStore()

// 处理主题切换
const handleThemeChange = (themeValue) => {
  appStore.setTheme(themeValue)
  // 同步到 props.settings（保持兼容性）
  props.settings.theme = themeValue
}

// 处理主色调切换
const handlePrimaryColorChange = (colorValue) => {
  appStore.setPrimaryColor(colorValue)
  // 同步到 props.settings（保持兼容性）
  props.settings.primaryColor = colorValue
}

// 主题模式选项
const themeModes = ref([
  { 
    value: 'dark', 
    label: '深色模式', 
    description: '适合夜间使用，减少眼睛疲劳' 
  },
  { 
    value: 'light', 
    label: '浅色模式', 
    description: '经典明亮界面，清晰易读' 
  },
  { 
    value: 'auto', 
    label: '跟随系统', 
    description: '根据系统设置自动切换主题' 
  }
])

// 主题颜色选项
const themeColors = ref([
  { name: '蓝色', value: '#6366f1' },
  { name: '绿色', value: '#10b981' },
  { name: '橙色', value: '#f59e0b' },
  { name: '红色', value: '#ef4444' },
  { name: '紫色', value: '#8b5cf6' },
  { name: '青色', value: '#06b6d4' }
])
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

// 主题模式选择器
.theme-mode-selector {
  display: flex;
  gap: 16px;
  margin-top: 16px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.theme-mode-option {
  background: var(--bg-card);
  border: 2px solid var(--border-color);
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 200px;
  flex-shrink: 0;
  
  &:hover {
    border-color: var(--border-light);
    transform: translateY(-2px);
    box-shadow: var(--shadow-lg);
  }
  
  &.active {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 3px var(--primary-light);
  }
}

.mode-preview {
  width: 100%;
  height: 80px;
  border-radius: 8px;
  margin-bottom: 12px;
  position: relative;
  overflow: hidden;
  
  &.dark {
    background: linear-gradient(135deg, #1f1f1f 0%, #0a0a0a 100%);
    
    .preview-header {
      background: #262626;
      height: 20px;
      border-bottom: 1px solid #333;
    }
    
    .preview-sidebar {
      background: #1a1a1a;
      width: 30px;
      height: 60px;
      position: absolute;
      left: 0;
      bottom: 0;
      border-right: 1px solid #333;
    }
    
    .preview-content {
      background: #0f0f0f;
      width: calc(100% - 30px);
      height: 60px;
      position: absolute;
      right: 0;
      bottom: 0;
    }
  }
  
  &.light {
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    
    .preview-header {
      background: #f1f5f9;
      height: 20px;
      border-bottom: 1px solid #e2e8f0;
    }
    
    .preview-sidebar {
      background: #ffffff;
      width: 30px;
      height: 60px;
      position: absolute;
      left: 0;
      bottom: 0;
      border-right: 1px solid #e2e8f0;
    }
    
    .preview-content {
      background: #f8fafc;
      width: calc(100% - 30px);
      height: 60px;
      position: absolute;
      right: 0;
      bottom: 0;
    }
  }
  
  &.auto {
    background: linear-gradient(135deg, #1f1f1f 0%, #ffffff 100%);
    
    .preview-header {
      background: #262626;
      height: 20px;
      border-bottom: 1px solid #333;
    }
    
    .preview-sidebar {
      background: #1a1a1a;
      width: 30px;
      height: 30px;
      position: absolute;
      left: 0;
      bottom: 30px;
      border-right: 1px solid #333;
    }
    
    .preview-content {
      background: #0f0f0f;
      width: calc(100% - 30px);
      height: 30px;
      position: absolute;
      right: 0;
      bottom: 30px;
    }
    
    &::after {
      content: '';
      position: absolute;
      left: 0;
      bottom: 0;
      width: 100%;
      height: 30px;
      background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
      
      .preview-sidebar {
        background: #ffffff;
        border-right: 1px solid #e2e8f0;
      }
      
      .preview-content {
        background: #f8fafc;
      }
    }
  }
}

.mode-info {
  h5 {
    font-size: 14px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 4px;
  }
  
  p {
    font-size: 12px;
    color: var(--text-secondary);
    line-height: 1.4;
  }
}

// 颜色选择器
.color-picker {
  display: flex;
  gap: 12px;
  margin-top: 16px;
  overflow-x: auto;
  padding-bottom: 8px;
}

.color-option {
  background: var(--bg-card);
  border: 2px solid var(--border-color);
  border-radius: 12px;
  padding: 12px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 12px;
  transition: all 0.3s ease;
  position: relative;
  min-width: 180px;
  flex-shrink: 0;
  
  &:hover {
    border-color: var(--border-light);
    transform: translateY(-2px);
    box-shadow: var(--shadow-lg);
  }
  
  &.active {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 3px var(--primary-light);
    
    .check-icon {
      color: white;
      font-size: 16px;
    }
  }
}

.color-preview {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  flex-shrink: 0;
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.1);
}

.color-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.color-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
}

.color-value {
  font-size: 12px;
  color: var(--text-secondary);
  font-family: 'Monaco', 'Menlo', monospace;
}

.check-icon {
  position: absolute;
  top: 8px;
  right: 8px;
  background: var(--primary-color);
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
}

// 侧边栏设置
.sidebar-settings {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 16px;
}

.setting-row {
  display: flex;
  align-items: center;
  gap: 16px;
  
  label {
    font-size: 14px;
    font-weight: 500;
    color: var(--text-primary);
    min-width: 100px;
  }
}

// 界面设置
.interface-settings {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 16px;
}

@media (max-width: 768px) {
  .setting-item {
    flex-direction: column;
    gap: 16px;
  }
  
  .setting-info {
    margin-right: 0;
  }
  
  .theme-mode-option {
    padding: 12px;
    min-width: 160px;
  }
  
  .mode-preview {
    height: 60px;
  }
  
  .color-option {
    padding: 8px;
    min-width: 140px;
  }
  
  .color-preview {
    width: 24px;
    height: 24px;
  }
  
  .setting-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
    
    label {
      min-width: auto;
    }
  }
}

@media (max-width: 480px) {
  .theme-mode-option {
    min-width: 140px;
  }
  
  .color-option {
    min-width: 120px;
  }
  
  .theme-mode-selector,
  .color-picker {
    gap: 8px;
  }
}
</style> 