<template>
  <nav class="icon-nav-bar">
    <div 
      v-for="item in navItems" 
      :key="item.key"
      class="nav-item"
      :class="{ 'is-active': modelValue === item.key }"
      @click="handleClick(item.key)"
    >
      <el-icon :size="24">
        <component :is="item.icon" />
      </el-icon>
      <div class="nav-tooltip">{{ item.label }}</div>
    </div>
  </nav>
</template>

<script setup>
import { Setting, Operation, Share } from '@element-plus/icons-vue'

defineProps({
  modelValue: {
    type: String,
    default: 'appConfig'
  }
})

const emit = defineEmits(['update:modelValue', 'change'])

const navItems = [
  {
    key: 'appConfig',
    label: '应用配置',
    icon: Setting
  },
  {
    key: 'actionEngine',
    label: '动作引擎',
    icon: Operation
  },
  {
    key: 'workflowEngine',
    label: '流程引擎',
    icon: Share
  }
]

const handleClick = (key) => {
  emit('update:modelValue', key)
  emit('change', key)
}
</script>

<style lang="scss" scoped>
.icon-nav-bar {
  width: 56px;
  background: var(--bg-tertiary, #f5f5f5);
  border-right: 1px solid var(--border-color);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 0;
  gap: 4px;
  flex-shrink: 0;

  .nav-item {
    width: 44px;
    height: 44px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s;
    color: var(--text-secondary, #909399);
    position: relative;

    &:hover {
      background: var(--bg-hover, rgba(0, 0, 0, 0.05));
      color: var(--text-primary, #303133);

      .nav-tooltip {
        opacity: 1;
        visibility: visible;
        transform: translateX(0);
      }
    }

    &.is-active {
      background: var(--primary-light, rgba(64, 158, 255, 0.1));
      color: var(--primary-color, #409eff);

      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 3px;
        height: 20px;
        background: var(--primary-color, #409eff);
        border-radius: 0 2px 2px 0;
      }
    }

    .el-icon {
      font-size: 20px;
    }

    .nav-tooltip {
      position: absolute;
      left: 100%;
      margin-left: 10px;
      padding: 6px 12px;
      background: var(--bg-card, #ffffff);
      color: var(--text-primary, #303133);
      font-size: 13px;
      white-space: nowrap;
      border-radius: 6px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
      opacity: 0;
      visibility: hidden;
      transform: translateX(-8px);
      transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
      pointer-events: none;
      z-index: 1000;

      &::before {
        content: '';
        position: absolute;
        right: 100%;
        top: 50%;
        transform: translateY(-50%);
        border: 5px solid transparent;
        border-right-color: var(--bg-card, #ffffff);
      }
    }
  }
}
</style>
