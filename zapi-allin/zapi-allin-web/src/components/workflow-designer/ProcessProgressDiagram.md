# ProcessProgressDiagram 组件

流程执行进度图组件，用于可视化展示工作流的执行状态和进度。

## 功能特性

- 🎯 **实时进度展示**: 显示流程执行的实时进度百分比
- 🎨 **可视化流程图**: 基于画布的流程节点和连接线展示
- 🖱️ **交互式节点**: 点击节点查看详细执行信息
- 📊 **智能状态识别**: 自动识别节点状态（未开始、进行中、已完成、已拒绝）
- 🔗 **动态连接线**: 根据执行状态动态显示连接线样式
- 📱 **响应式设计**: 支持移动端和桌面端显示

## 使用方法

### 基本用法

```vue
<template>
  <ProcessProgressDiagram
    :process-definition="processDefinition"
    :task-history="taskHistory"
    :current-node-id="currentNodeId"
    :process-status="processStatus"
    :instance-id="instanceId"
  />
</template>

<script setup>
import ProcessProgressDiagram from '@/components/workflow-designer/ProcessProgressDiagram.vue'

const processDefinition = {
  id: 'process-123',
  name: '请假审批流程',
  key: 'leave_approval',
  jsonDef: JSON.stringify({
    nodes: [
      { id: 'start', name: '开始', type: 'START', position: { x: 100, y: 150 } },
      { id: 'apply', name: '申请提交', type: 'APPROVAL', position: { x: 300, y: 150 } },
      { id: 'approve', name: '主管审批', type: 'APPROVAL', position: { x: 500, y: 150 } },
      { id: 'end', name: '结束', type: 'END', position: { x: 700, y: 150 } }
    ],
    flows: [
      { id: 'flow1', from: 'start', to: 'apply' },
      { id: 'flow2', from: 'apply', to: 'approve' },
      { id: 'flow3', from: 'approve', to: 'end' }
    ]
  })
}

const taskHistory = [
  {
    nodeId: 'start',
    nodeName: '开始',
    nodeType: 'START',
    operator: 'user123',
    operatorName: '张三',
    startTime: '2024-01-01T09:00:00',
    endTime: '2024-01-01T09:00:01',
    duration: 1000,
    comment: '流程启动'
  },
  {
    nodeId: 'apply',
    nodeName: '申请提交',
    nodeType: 'APPROVAL',
    operator: 'user123',
    operatorName: '张三',
    startTime: '2024-01-01T09:00:01',
    endTime: '2024-01-01T09:30:00',
    duration: 1800000,
    comment: '提交请假申请'
  }
]

const currentNodeId = 'approve'
const processStatus = 1 // 1: 运行中, 2: 已结束
const instanceId = 'instance-456'
</script>
```

## 属性说明

| 属性名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| `process-definition` | Object | 是 | - | 流程定义对象 |
| `task-history` | Array | 是 | [] | 任务执行历史记录 |
| `current-node-id` | String | 否 | - | 当前执行节点ID |
| `process-status` | Number | 是 | - | 流程状态（1: 运行中, 2: 已结束） |
| `instance-id` | String | 否 | - | 流程实例ID |

### process-definition 对象结构

```typescript
interface ProcessDefinition {
  id: string          // 流程定义ID
  name: string        // 流程名称
  key: string         // 流程标识
  jsonDef: string     // 流程定义JSON字符串
}
```

### jsonDef 结构

```json
{
  "nodes": [
    {
      "id": "node_id",
      "name": "节点名称",
      "type": "START|END|APPROVAL|CONDITIONAL_BRANCH",
      "position": { "x": 100, "y": 150 }
    }
  ],
  "flows": [
    {
      "id": "flow_id",
      "from": "source_node_id",
      "to": "target_node_id",
      "name": "连接名称",
      "condition": "连接条件"
    }
  ]
}
```

### task-history 数组结构

```typescript
interface TaskHistory {
  nodeId: string          // 节点ID
  nodeName?: string       // 节点名称
  nodeType?: string       // 节点类型
  operator?: string       // 操作人ID
  operatorName?: string   // 操作人姓名
  startTime?: string      // 开始时间
  endTime?: string        // 结束时间
  duration?: number       // 执行耗时（毫秒）
  comment?: string        // 操作备注
}
```

## 节点状态说明

组件自动根据任务历史和流程状态计算节点状态：

- **pending** (未开始): 灰色，节点尚未开始执行
- **current** (进行中): 蓝色，节点正在执行中
- **completed** (已完成): 绿色，节点已成功完成
- **rejected** (已拒绝): 红色，节点被拒绝或驳回

## 节点类型支持

- **START**: 开始节点（圆形，绿色渐变）
- **END**: 结束节点（圆形，红色渐变）
- **APPROVAL**: 审批节点（圆角矩形，蓝色渐变）
- **CONDITIONAL_BRANCH**: 条件分支（菱形，橙色渐变）

## 交互功能

### 节点点击

点击任意节点会弹出详情对话框，显示：
- 节点基本信息（名称、ID、类型、状态）
- 执行详情（开始时间、结束时间、耗时、操作人、备注）

### 自适应布局

- 组件会根据节点数量自动调整画布大小
- 支持水平滚动查看大型流程图
- 响应式设计，适配不同屏幕尺寸

## 样式定制

组件使用 SCSS 编写样式，支持通过 CSS 变量进行主题定制：

```css
.process-progress-diagram {
  --primary-color: #409eff;
  --success-color: #67c23a;
  --warning-color: #e6a23c;
  --danger-color: #f56c6c;
  --info-color: #909399;
}
```

## 性能优化

- 使用 `computed` 计算属性缓存节点和连接线数据
- SVG 连接线渲染，性能优异
- 响应式数据最小化，避免不必要的重渲染

## 最佳实践

1. **数据结构**: 确保 `process-definition.jsonDef` 包含正确的节点位置信息
2. **节点匹配**: 任务历史中的 `nodeId` 应与流程定义中的节点ID匹配
3. **性能考虑**: 对于大型流程图（>20个节点），建议使用分页或折叠显示
4. **用户体验**: 提供加载状态和错误处理机制

## 故障排除

### 常见问题

1. **节点不显示**: 检查 `jsonDef` 格式是否正确
2. **状态不正确**: 检查 `task-history` 数据格式和节点ID匹配
3. **样式问题**: 确保引入了必要的 Element Plus 样式

### 调试技巧

```javascript
// 在浏览器控制台查看解析后的数据
console.log('processNodes:', processNodes.value)
console.log('processFlows:', processFlows.value)
```
