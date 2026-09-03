<template>
  <div class="icon-test">
    <h3>图标测试</h3>
    <div class="icon-list">
      <div class="icon-item" v-for="iconName in testIcons" :key="iconName">
        <component :is="getIconComponent(iconName)" />
        <span>{{ iconName }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const testIcons = [
  'Edit',
  'Document',
  'Plus',
  'ArrowDown',
  'CircleCheck',
  'Checked',
  'SwitchButton',
  'Calendar',
  'Clock',
  'Minus',
  'Star',
  'Brush',
  'Upload'
]

const getIconComponent = (iconName: string) => {
  return ElementPlusIconsVue[iconName as keyof typeof ElementPlusIconsVue]
}
</script>

<style scoped>
.icon-test {
  padding: 20px;
}

.icon-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.icon-item svg {
  width: 24px;
  height: 24px;
  margin-bottom: 8px;
}
</style>