<template>
  <div class="appearance-drawer-content">
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
        <h4>水印设置</h4>
        <p>为页面添加自定义水印，保护内容安全</p>
      </div>
      <div class="watermark-settings">
        <div class="setting-row">
          <label>启用水印</label>
          <el-switch v-model="watermarkEnabled" @change="handleWatermarkToggle" />
        </div>
        <template v-if="watermarkEnabled">
          <div class="setting-row">
            <label>水印内容</label>
            <el-input
              v-model="watermarkText"
              placeholder="请输入水印内容"
              @input="handleWatermarkChange"
              clearable
            />
          </div>
          <div class="setting-row">
            <label>字体大小</label>
            <el-slider
              v-model="watermarkFontSize"
              :min="12"
              :max="24"
              :step="1"
              show-input
              @change="handleWatermarkChange"
              style="width: 200px;"
            />
          </div>
          <div class="setting-row">
            <label>透明度</label>
            <el-slider
              v-model="watermarkOpacity"
              :min="0.05"
              :max="0.3"
              :step="0.05"
              :format-tooltip="(val) => `${Math.round(val * 100)}%`"
              @change="handleWatermarkChange"
              style="width: 200px;"
            />
          </div>
          <div class="setting-row">
            <label>旋转角度</label>
            <el-slider
              v-model="watermarkRotate"
              :min="-45"
              :max="45"
              :step="5"
              :format-tooltip="(val) => `${val}°`"
              @change="handleWatermarkChange"
              style="width: 200px;"
            />
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useAppStore } from '@/stores/app'
import { Check } from '@element-plus/icons-vue'
import watermark from '@/utils/watermark'

const appStore = useAppStore()

// 水印相关状态
const watermarkEnabled = ref(true)
const watermarkText = ref('知录Admin')
const watermarkFontSize = ref(16)
const watermarkOpacity = ref(0.15)
const watermarkRotate = ref(-20)

// 初始化水印设置
onMounted(() => {
  // 从localStorage读取水印设置
  const savedSettings = localStorage.getItem('watermarkSettings')
  if (savedSettings) {
    try {
      const settings = JSON.parse(savedSettings)
      watermarkEnabled.value = settings.enabled !== undefined ? settings.enabled : true
      watermarkText.value = settings.text || '知录Admin'
      watermarkFontSize.value = settings.fontSize || 16
      watermarkOpacity.value = settings.opacity || 0.15
      watermarkRotate.value = settings.rotate || -20
    } catch (e) {
      console.error('Failed to parse watermark settings:', e)
    }
  }
})

// 处理主题切换
const handleThemeChange = (themeValue) => {
  appStore.setTheme(themeValue)
}

// 处理主色调切换
const handlePrimaryColorChange = (colorValue) => {
  appStore.setPrimaryColor(colorValue)
}

// 处理水印开关
const handleWatermarkToggle = (enabled) => {
  saveWatermarkSettings()
  watermark.update()
}

// 处理水印变化
const handleWatermarkChange = () => {
  saveWatermarkSettings()
  watermark.update()
}

// 保存水印设置
const saveWatermarkSettings = () => {
  const settings = {
    enabled: watermarkEnabled.value,
    text: watermarkText.value,
    fontSize: watermarkFontSize.value,
    opacity: watermarkOpacity.value,
    rotate: watermarkRotate.value
  }
  localStorage.setItem('watermarkSettings', JSON.stringify(settings))
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
.appearance-drawer-content {
  padding: 0 4px;
}

.setting-item {
  padding: 24px 0;
  border-bottom: 1px solid var(--border-color);

  &:last-child {
    border-bottom: none;
  }
}

.setting-info {
  margin-bottom: 20px;

  h4 {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    margin-bottom: 8px;
  }

  p {
    color: var(--text-secondary);
    font-size: 14px;
    line-height: 1.5;
  }
}

// 主题模式选择器
.theme-mode-selector {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 16px;
}

.theme-mode-option {
  background: var(--bg-card);
  border: 2px solid var(--border-color);
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;

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
  height: 100px;
  border-radius: 8px;
  margin-bottom: 12px;
  position: relative;
  overflow: hidden;

  &.dark {
    background: linear-gradient(135deg, #1f1f1f 0%, #0a0a0a 100%);

    .preview-header {
      background: #262626;
      height: 25px;
      border-bottom: 1px solid #333;
    }

    .preview-sidebar {
      background: #1a1a1a;
      width: 35px;
      height: 75px;
      position: absolute;
      left: 0;
      bottom: 0;
      border-right: 1px solid #333;
    }

    .preview-content {
      background: #0f0f0f;
      width: calc(100% - 35px);
      height: 75px;
      position: absolute;
      right: 0;
      bottom: 0;
    }
  }

  &.light {
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);

    .preview-header {
      background: #f1f5f9;
      height: 25px;
      border-bottom: 1px solid #e2e8f0;
    }

    .preview-sidebar {
      background: #ffffff;
      width: 35px;
      height: 75px;
      position: absolute;
      left: 0;
      bottom: 0;
      border-right: 1px solid #e2e8f0;
    }

    .preview-content {
      background: #f8fafc;
      width: calc(100% - 35px);
      height: 75px;
      position: absolute;
      right: 0;
      bottom: 0;
    }
  }

  &.auto {
    background: linear-gradient(135deg, #1f1f1f 0%, #ffffff 100%);

    .preview-header {
      background: linear-gradient(90deg, #262626 0%, #f1f5f9 100%);
      height: 25px;
      border-bottom: 1px solid #666;
    }

    .preview-sidebar {
      background: linear-gradient(180deg, #1a1a1a 0%, #ffffff 100%);
      width: 35px;
      height: 75px;
      position: absolute;
      left: 0;
      bottom: 0;
      border-right: 1px solid #666;
    }

    .preview-content {
      background: linear-gradient(180deg, #0f0f0f 0%, #f8fafc 100%);
      width: calc(100% - 35px);
      height: 75px;
      position: absolute;
      right: 0;
      bottom: 0;
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
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
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
  width: 40px;
  height: 40px;
  border-radius: 8px;
  flex-shrink: 0;
  box-shadow: inset 0 0 0 1px rgba(255, 255, 255, 0.1);
}

.color-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4px;
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

// 水印设置
.watermark-settings {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.setting-row {
  display: flex;
  align-items: center;
  gap: 16px;

  label {
    font-size: 14px;
    font-weight: 500;
    color: var(--text-primary);
    min-width: 80px;
  }

  .el-input {
    flex: 1;
  }
}
</style>
