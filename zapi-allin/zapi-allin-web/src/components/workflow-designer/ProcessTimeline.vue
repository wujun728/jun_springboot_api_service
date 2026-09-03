<template>
  <div class="process-timeline" v-if="filteredTaskHistory && filteredTaskHistory.length > 0">
    <h4 class="timeline-title">流程历史</h4>
    <el-timeline>
      <el-timeline-item
        v-for="(task, index) in filteredTaskHistory"
        :key="index"
        :timestamp="formatTime(task.startTime)"
        placement="top"
        :type="getTimelineType(task)"
        :icon="getTimelineIcon(task)"
      >
        <div class="timeline-content">
          <div class="timeline-header">
            <!-- 抄送节点不显示操作人，直接显示动作 -->
            <template v-if="task.nodeType === 'CC'">
              <span class="timeline-action">{{ getTimelineAction(task) }}</span>
            </template>
            <!-- 审批节点进行中时，不显示操作人，只显示动作（动作中已包含审批人名称） -->
            <template v-else-if="task.nodeType === 'APPROVAL' && !task.endTime">
              <span class="timeline-action">{{ getTimelineAction(task) }}</span>
            </template>
            <template v-else>
              <span class="timeline-user">{{ getOperatorDisplayName(task) }}</span>
              <span class="timeline-action">{{ getTimelineAction(task) }}</span>
            </template>
          </div>
          <div class="timeline-node">
            节点：{{ task.nodeName || task.nodeId }}
            <!-- 审批节点显示审批方式标签 -->
            <el-tag 
              v-if="task.nodeType === 'APPROVAL'" 
              :type="getApprovalModeType(task)" 
              size="small"
              class="approval-mode-tag"
            >
              {{ getApprovalModeText(task) }}
            </el-tag>
          </div>
          <div v-if="task.comment && task.nodeType !== 'CC'" class="timeline-comment">
            <span class="comment-label">备注：</span>
            <span class="comment-text">{{ task.comment }}</span>
          </div>
          <div v-if="task.duration" class="timeline-duration">
            耗时：{{ formatDuration(task.duration) }}
          </div>
        </div>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { VideoPlay, Check, Close, Loading, Bell, Document } from '@element-plus/icons-vue'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'

// Props
interface Props {
  taskHistory: any[]
  processKey?: string
  processDefinition?: any
}

const props = defineProps<Props>()

// 过滤后的任务历史（排除"审批节点完成"的汇总记录）
const filteredTaskHistory = computed(() => {
  if (!props.taskHistory || props.taskHistory.length === 0) {
    return []
  }
  
  // 过滤掉"审批节点完成"的汇总记录（这是节点完成时的标记记录，不是实际审批人的记录）
  return props.taskHistory.filter(task => 
    !(task.comment && task.comment.includes('审批节点完成'))
  )
})

// 用户数据映射
const userMap = ref(new Map())
const userStore = useUserStore()

// 加载用户数据
const loadUsers = async () => {
  try {
    // 使用缓存的用户数据
    const users = await userStore.loadEnabledUsers()
    
    // 构建用户ID到用户信息的映射
    const newUserMap = new Map()
    users.forEach((user) => {
      newUserMap.set(user.id, user)
      newUserMap.set(user.id.toString(), user)
    })
    userMap.value = newUserMap
  } catch (error) {
    console.error('[ProcessTimeline] 加载用户数据失败:', error)
  }
}

// 获取用户显示名称
const getUserDisplayName = (userId: any): string => {
  if (!userId) return '未知'
  
  let user = userMap.value.get(userId)
  
  if (!user) {
    const numUserId = typeof userId === 'string' ? parseInt(userId) : userId
    if (!isNaN(numUserId)) {
      user = userMap.value.get(numUserId)
    }
  }
  
  if (user) {
    return user.realName || user.username
  }
  
  return userId.toString()
}

// 获取操作人显示名称
const getOperatorDisplayName = (task: any): string => {
  // 对于审批节点且未完成的情况，显示审批人而不是操作人
  if (task.nodeType === 'APPROVAL' && !task.endTime) {
    // 优先使用后端返回的 approvalUserNames（从审批记录表获取）
    if (task.approvalUserNames) {
      return task.approvalUserNames
    }
    
    // 其次尝试从流程定义中获取
    if (props.processDefinition) {
      try {
        const definition = JSON.parse(props.processDefinition.jsonDef)
        const node = definition.nodes?.find((n: any) => n.id === task.nodeId)
        
        // 优先使用 approvalTargets（多人审批）
        if (node && node.approvalTargets && node.approvalTargets.length > 0) {
          return getApprovalTargetsDisplayName(node.approvalTargets)
        }
        
        // 兼容旧的单人审批
        if (node && node.assignee) {
          return getUserDisplayName(node.assignee)
        }
      } catch (error) {
        console.error('[ProcessTimeline] 解析流程定义失败:', error)
      }
    }
  }
  
  // 优先使用 operatorName，如果没有或者是ID，则尝试从userMap获取
  if (task.operatorName && task.operatorName !== task.operator?.toString()) {
    return task.operatorName
  }
  
  // 如果有 operator，尝试从userMap获取用户名
  if (task.operator) {
    return getUserDisplayName(task.operator)
  }
  
  return '系统'
}

// 获取审批目标的显示名称（支持多人/部门/角色）
const getApprovalTargetsDisplayName = (approvalTargets: any[]): string => {
  if (!approvalTargets || approvalTargets.length === 0) return '未知'
  
  const names = approvalTargets.map(target => {
    // 如果有name字段，直接使用
    if (target.name) return target.name
    
    // 否则根据类型查询
    if (target.type === 'user') {
      return getUserDisplayName(target.id)
    } else if (target.type === 'dept') {
      return `部门${target.id}`
    } else if (target.type === 'role') {
      return `角色${target.id}`
    }
    return target.id?.toString() || '未知'
  })
  
  return names.join('、')
}

// 获取抄送人员名称
const getCcUserName = (task: any): string => {
  if (task.comment) {
    // 匹配 "接收人: 张三、李四、王五" 格式，提取所有人名
    const ccUserMatch = task.comment.match(/接收人:\s*(.+?)(?:，|$)/)
    if (ccUserMatch && ccUserMatch[1]) {
      // 直接返回抄送人名称（后端已经返回了姓名，不需要再查询）
      return ccUserMatch[1].trim()
    }
  }
  return '未知'
}

// 获取时间线类型
const getTimelineType = (task: any): string => {
  if (task.nodeType === 'START') return 'primary'
  if (task.nodeType === 'END') return 'success'
  if (task.nodeType === 'APPROVAL') {
    if (!task.endTime) return 'warning'
    if (task.comment && (task.comment.includes('驳回') || task.comment.includes('拒绝') || task.comment.includes('reject'))) {
      return 'danger'
    }
    return 'success'
  }
  return 'info'
}

// 获取时间线图标
const getTimelineIcon = (task: any): string => {
  if (task.nodeType === 'START') return 'VideoPlay'
  if (task.nodeType === 'END') return 'Check'
  if (task.nodeType === 'APPROVAL') {
    if (!task.endTime) return 'Loading'
    if (task.comment && (task.comment.includes('驳回') || task.comment.includes('拒绝') || task.comment.includes('reject'))) {
      return 'Close'
    }
    return 'Check'
  }
  if (task.nodeType === 'CC') return 'Bell'
  return 'Document'
}

// 获取时间线操作描述
const getTimelineAction = (task: any): string => {
  if (task.nodeType === 'START') {
    return '发起了流程'
  }
  if (task.nodeType === 'END') {
    return '流程结束'
  }
  if (task.nodeType === 'APPROVAL') {
    if (!task.endTime) {
      // 获取待审批人员名称
      const approverNames = getApprovalPendingNames(task)
      if (approverNames) {
        return `等待 ${approverNames} 审批`
      }
      return '审批进行中'
    }
    if (task.comment && (task.comment.includes('驳回') || task.comment.includes('拒绝') || task.comment.includes('reject'))) {
      return '驳回了审批'
    }
    return '通过了审批'
  }
  if (task.nodeType === 'CC') {
    const ccUserName = getCcUserName(task)
    return `抄送给 ${ccUserName}`
  }
  if (task.nodeType === 'CONDITIONAL_BRANCH') {
    return '执行了条件分支'
  }
  return '执行了节点'
}

// 获取审批节点待审批人员名称
const getApprovalPendingNames = (task: any): string | null => {
  // 优先使用后端返回的 approvalUserNames（从审批记录表获取）
  if (task.approvalUserNames) {
    return task.approvalUserNames
  }
  
  // 其次尝试从流程定义中获取
  if (!props.processDefinition) return null
  
  try {
    const definition = JSON.parse(props.processDefinition.jsonDef)
    const node = definition.nodes?.find((n: any) => n.id === task.nodeId)
    
    // 优先使用 approvalTargets（多人审批）
    if (node && node.approvalTargets && node.approvalTargets.length > 0) {
      return getApprovalTargetsDisplayName(node.approvalTargets)
    }
    
    // 兼容旧的单人审批
    if (node && node.assignee) {
      return getUserDisplayName(node.assignee)
    }
  } catch (error) {
    console.error('[ProcessTimeline] 解析流程定义失败:', error)
  }
  
  return null
}

// 获取审批方式文本
const getApprovalModeText = (task: any): string => {
  // 优先从 task 中获取（后端返回）
  if (task.approvalMode) {
    return task.approvalMode === 'ALL' ? '会签' : '或签'
  }
  
  // 其次从流程定义中获取
  if (props.processDefinition) {
    try {
      const definition = JSON.parse(props.processDefinition.jsonDef)
      const node = definition.nodes?.find((n: any) => n.id === task.nodeId)
      if (node && node.approvalMode) {
        return node.approvalMode === 'ALL' ? '会签' : '或签'
      }
    } catch (error) {
      // ignore
    }
  }
  
  return '或签' // 默认或签
}

// 获取审批方式标签类型
const getApprovalModeType = (task: any): string => {
  // 优先从 task 中获取
  if (task.approvalMode) {
    return task.approvalMode === 'ALL' ? 'warning' : 'success'
  }
  
  // 其次从流程定义中获取
  if (props.processDefinition) {
    try {
      const definition = JSON.parse(props.processDefinition.jsonDef)
      const node = definition.nodes?.find((n: any) => n.id === task.nodeId)
      if (node && node.approvalMode) {
        return node.approvalMode === 'ALL' ? 'warning' : 'success'
      }
    } catch (error) {
      // ignore
    }
  }
  
  return 'success' // 默认或签
}

// 格式化时间
const formatTime = (time: string): string => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 格式化时长
const formatDuration = (duration: number): string => {
  if (!duration || duration === 0) return '瞬时'
  
  const seconds = Math.floor(duration / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)
  
  if (days > 0) {
    return `${days}天${hours % 24}小时`
  } else if (hours > 0) {
    return `${hours}小时${minutes % 60}分钟`
  } else if (minutes > 0) {
    return `${minutes}分钟${seconds % 60}秒`
  } else {
    return `${seconds}秒`
  }
}

// 组件挂载时加载用户数据
onMounted(() => {
  loadUsers()
})
</script>

<style scoped lang="scss">
.process-timeline {
  margin-top: 32px;
  padding-top: 24px;
  padding-bottom: 40px;
  border-top: 1px solid var(--border-color);
  
  .timeline-title {
    margin: 0 0 20px 0;
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    background: transparent;
  }
  
  .timeline-content {
    .timeline-header {
      margin-bottom: 8px;
      
      .timeline-user {
        font-weight: 600;
        color: var(--text-primary);
        margin-right: 8px;
      }
      
      .timeline-action {
        color: var(--text-secondary);
      }
    }
    
    .timeline-node {
      font-size: 13px;
      color: var(--text-muted);
      margin-bottom: 6px;
      display: flex;
      align-items: center;
      gap: 8px;
      
      .approval-mode-tag {
        margin-left: 4px;
      }
    }
    
    .timeline-comment {
      background: var(--bg-tertiary);
      padding: 8px 12px;
      border-radius: 4px;
      margin-bottom: 6px;
      border-left: 3px solid var(--el-color-primary);
      
      .comment-label {
        font-weight: 500;
        color: var(--text-secondary);
        margin-right: 4px;
      }
      
      .comment-text {
        color: var(--text-primary);
      }
    }
    
    .timeline-duration {
      font-size: 12px;
      color: var(--text-muted);
    }
  }
}
</style>
