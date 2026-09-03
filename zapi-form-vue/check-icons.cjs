const icons = require('@element-plus/icons-vue');

// 检查所有组件配置中的图标
const componentConfigs = [
  { type: 'input', icon: 'Edit' },
  { type: 'textarea', icon: 'Document' },
  { type: 'input-number', icon: 'Plus' },
  { type: 'select', icon: 'ArrowDown' },
  { type: 'radio', icon: 'CircleCheck' },
  { type: 'checkbox', icon: 'Checked' },
  { type: 'switch', icon: 'SwitchButton' },
  { type: 'date-picker', icon: 'Calendar' },
  { type: 'time-picker', icon: 'Clock' },
  { type: 'slider', icon: 'Minus' },
  { type: 'rate', icon: 'Star' },
  { type: 'color-picker', icon: 'Brush' },
  { type: 'upload', icon: 'Upload' }
];

console.log('检查组件图标:');
componentConfigs.forEach(config => {
  const exists = !!icons[config.icon];
  console.log(config.type + ' (' + config.icon + '):', exists ? '✓ 存在' : '✗ 不存在');
});