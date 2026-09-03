<template>
  <div class="floating-ai-chat">
    <!-- 触发按钮（收起时显示） -->
    <transition name="bounce">
      <div 
        v-show="!isExpanded" 
        class="chat-trigger"
        :style="triggerStyle"
        @click="toggleExpand"
        @mousedown="startDrag"
      >
        <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="badge">
          <el-icon :size="28" class="trigger-icon">
            <ChatDotRound />
          </el-icon>
        </el-badge>
        <div class="trigger-text">AI助手</div>
      </div>
    </transition>

    <!-- 聊天窗口（展开时显示） -->
    <transition name="slide-up">
      <div 
        v-show="isExpanded" 
        class="chat-window"
        :style="windowStyle"
      >
        <!-- 头部 -->
        <div class="chat-header" @mousedown="startDrag">
          <div class="header-left">
            <el-icon class="ai-icon"><MagicStick /></el-icon>
            <span class="header-title"></span>
          </div>
          <div class="header-actions">
            <el-tooltip content="刷新">
              <el-icon class="action-icon" @click="refreshChat"><Refresh /></el-icon>
            </el-tooltip>
            <el-tooltip content="收起">
              <el-icon class="action-icon" @click="toggleExpand"><Minus /></el-icon>
            </el-tooltip>
            <el-tooltip content="关闭">
              <el-icon class="action-icon close" @click="closeChat"><Close /></el-icon>
            </el-tooltip>
          </div>
        </div>

        <!-- 聊天内容区域 -->
        <div class="chat-content">
          <AiChatBox ref="chatBoxRef" />
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { ChatDotRound, MagicStick, Refresh, Minus, Close } from '@element-plus/icons-vue'
import AiChatBox from '@/views/dashboard/components/AiChatBox.vue'

// 状态
const isExpanded = ref(false)
const unreadCount = ref(0)
const isDragging = ref(false)
const triggerPosition = ref({ x: 0, y: 0 }) // 触发按钮位置
const windowPosition = ref({ x: 0, y: 0 })  // 窗口位置
const dragOffset = ref({ x: 0, y: 0 })

// 聊天框引用
const chatBoxRef = ref(null)

// 默认位置（右侧居中）
const initPosition = () => {
  // 触发按钮：右侧居中
  triggerPosition.value = {
    x: window.innerWidth - 80,  // 60px宽度 + 20px边距
    y: (window.innerHeight - 60) / 2  // 垂直居中
  }
  
  // 聊天窗口：右侧居中
  windowPosition.value = {
    x: window.innerWidth - 420, // 400px宽度 + 20px边距
    y: (window.innerHeight - 600) / 2  // 垂直居中
  }
}

// 触发按钮样式
const triggerStyle = computed(() => ({
  left: `${triggerPosition.value.x}px`,
  top: `${triggerPosition.value.y}px`
}))

// 聊天窗口样式
const windowStyle = computed(() => ({
  left: `${windowPosition.value.x}px`,
  top: `${windowPosition.value.y}px`
}))

// 记录点击位置，用于区分点击和拖拽
const clickStartPos = ref({ x: 0, y: 0 })

// 展开/收起
const toggleExpand = (e) => {
  // 如果发生了拖拽（移动超过5px），不触发展开
  const moveDistance = Math.sqrt(
    Math.pow(e.clientX - clickStartPos.value.x, 2) + 
    Math.pow(e.clientY - clickStartPos.value.y, 2)
  )
  
  if (moveDistance > 5) {
    return // 拖拽操作，不展开
  }
  
  isExpanded.value = !isExpanded.value
  if (isExpanded.value) {
    unreadCount.value = 0
  }
}

// 关闭聊天
const closeChat = () => {
  isExpanded.value = false
}

// 刷新聊天
const refreshChat = () => {
  if (chatBoxRef.value && chatBoxRef.value.clearMessages) {
    chatBoxRef.value.clearMessages()
  }
}

// 拖拽相关
const startDrag = (e) => {
  if (e.target.closest('.header-actions') || e.target.closest('.action-icon')) {
    return // 点击操作按钮时不触发拖拽
  }
  
  // 记录点击位置，用于区分点击和拖拽
  clickStartPos.value = {
    x: e.clientX,
    y: e.clientY
  }
  
  isDragging.value = true
  
  if (isExpanded.value) {
    // 计算拖拽偏移量（窗口模式）
    const rect = e.currentTarget.getBoundingClientRect()
    dragOffset.value = {
      x: e.clientX - rect.left,
      y: e.clientY - rect.top
    }
  } else {
    // 计算拖拽偏移量（按钮模式）
    const rect = e.currentTarget.getBoundingClientRect()
    dragOffset.value = {
      x: e.clientX - rect.left,
      y: e.clientY - rect.top
    }
  }
  
  document.addEventListener('mousemove', onDrag)
  document.addEventListener('mouseup', stopDrag)
  e.preventDefault()
}

const onDrag = (e) => {
  if (!isDragging.value) return
  
  if (isExpanded.value) {
    // 窗口模式拖拽
    let newX = e.clientX - dragOffset.value.x
    let newY = e.clientY - dragOffset.value.y
    
    // 边界检测
    const maxX = window.innerWidth - 400
    const maxY = window.innerHeight - 600
    
    newX = Math.max(0, Math.min(newX, maxX))
    newY = Math.max(0, Math.min(newY, maxY))
    
    windowPosition.value = { x: newX, y: newY }
  } else {
    // 按钮模式拖拽
    let newX = e.clientX - dragOffset.value.x
    let newY = e.clientY - dragOffset.value.y
    
    // 边界检测
    const maxX = window.innerWidth - 60
    const maxY = window.innerHeight - 60
    
    newX = Math.max(0, Math.min(newX, maxX))
    newY = Math.max(0, Math.min(newY, maxY))
    
    triggerPosition.value = { x: newX, y: newY }
  }
}

const stopDrag = () => {
  isDragging.value = false
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
}

// 暴露方法给父组件
const sendMessage = (message) => {
  if (chatBoxRef.value && chatBoxRef.value.sendMessageFromParent) {
    isExpanded.value = true
    chatBoxRef.value.sendMessageFromParent(message)
  }
}

defineExpose({
  sendMessage,
  toggleExpand
})

// 生命周期
onMounted(() => {
  initPosition()
  
  // 监听窗口大小变化
  window.addEventListener('resize', initPosition)
})

onUnmounted(() => {
  window.removeEventListener('resize', initPosition)
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
})
</script>

<style scoped lang="scss">
.floating-ai-chat {
  position: fixed;
  z-index: 9999;
}

// 触发按钮
.chat-trigger {
  position: fixed;
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: move;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.4);
  transition: transform 0.3s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  user-select: none;
  
  &:hover {
    transform: scale(1.1);
    box-shadow: 0 6px 30px rgba(102, 126, 234, 0.6);
  }
  
  &:active {
    transform: scale(0.95);
  }
  
  .badge {
    :deep(.el-badge__content) {
      background-color: #f56c6c;
      border: 2px solid #fff;
    }
  }
  
  .trigger-icon {
    color: white;
    margin-bottom: 2px;
  }
  
  .trigger-text {
    color: white;
    font-size: 11px;
    font-weight: 500;
    margin-top: 2px;
  }
}

// 聊天窗口
.chat-window {
  position: fixed;
  width: 400px;
  height: 600px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 8px 40px rgba(0, 0, 0, 0.12);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  
  .chat-header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 16px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: move;
    user-select: none;
    flex-shrink: 0;
    
    .header-left {
      display: flex;
      align-items: center;
      gap: 8px;
      
      .ai-icon {
        font-size: 20px;
      }
      
      .header-title {
        font-size: 16px;
        font-weight: 600;
      }
    }
    
    .header-actions {
      display: flex;
      gap: 12px;
      align-items: center;
      
      .action-icon {
        font-size: 18px;
        cursor: pointer;
        transition: all 0.2s;
        padding: 4px;
        border-radius: 4px;
        
        &:hover {
          background: rgba(255, 255, 255, 0.2);
          transform: scale(1.1);
        }
        
        &:active {
          transform: scale(0.95);
        }
        
        &.close:hover {
          background: rgba(245, 108, 108, 0.3);
        }
      }
    }
  }
  
  .chat-content {
    flex: 1;
    overflow: hidden;
    display: flex;
    flex-direction: column;
  }
}

// 动画
.bounce-enter-active {
  animation: bounce-in 0.5s;
}

.bounce-leave-active {
  animation: bounce-out 0.3s;
}

@keyframes bounce-in {
  0% {
    transform: scale(0);
    opacity: 0;
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
    opacity: 1;
  }
}

@keyframes bounce-out {
  0% {
    transform: scale(1);
    opacity: 1;
  }
  100% {
    transform: scale(0);
    opacity: 0;
  }
}

.slide-up-enter-active {
  animation: slide-up-in 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-up-leave-active {
  animation: slide-up-out 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes slide-up-in {
  0% {
    transform: translateY(20px) scale(0.95);
    opacity: 0;
  }
  100% {
    transform: translateY(0) scale(1);
    opacity: 1;
  }
}

@keyframes slide-up-out {
  0% {
    transform: translateY(0) scale(1);
    opacity: 1;
  }
  100% {
    transform: translateY(20px) scale(0.95);
    opacity: 0;
  }
}

// 响应式
@media (max-width: 768px) {
  .chat-window {
    width: calc(100vw - 20px) !important;
    height: calc(100vh - 20px) !important;
    left: 10px !important;
    top: 10px !important;
    border-radius: 12px;
  }
  
  .chat-trigger {
    width: 56px;
    height: 56px;
    
    .trigger-icon {
      font-size: 24px;
    }
    
    .trigger-text {
      font-size: 10px;
    }
  }
}
</style>

