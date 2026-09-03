<template>
  <div class="ai-chat-box">
    <!-- 聊天头部 -->
    <div class="chat-box-header">
      <div class="header-title">
        <el-icon><ChatDotRound /></el-icon>
        <span>AI 智能助手</span>
      </div>
      <el-button text circle @click="clearChat" title="清空对话">
        <el-icon><Delete /></el-icon>
      </el-button>
    </div>

    <!-- 消息区域 -->
    <div class="chat-box-messages" ref="messagesContainer">
      <!-- 空状态 -->
      <div v-if="messages.length === 0" class="empty-state">
        <el-icon class="empty-icon"><ChatDotRound /></el-icon>
        <p>试试对我说：</p>
        <div class="quick-actions">
          <el-button size="small" @click="sendMessage('查看所有用户')">查看所有用户</el-button>
          <el-button size="small" @click="sendMessage('添加用户测试')">添加用户</el-button>
        </div>
      </div>

      <!-- 消息列表 -->
      <div
        v-for="(msg, index) in messages"
        :key="index"
        class="message-item"
        :class="msg.role"
      >
        <div class="message-content">
          <div v-if="msg.content" class="message-text" v-html="formatMessage(msg.content)"></div>
          <span class="message-time">{{ formatTime(msg.timestamp) }}</span>
        </div>
      </div>

      <!-- AI思考中 -->
      <div v-if="loading" class="message-item assistant">
        <div class="message-content thinking">
          <div class="typing-dots">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-box-input">
      <el-input
        v-model="userInput"
        type="textarea"
        :rows="1"
        :autosize="{ minRows: 2, maxRows: 4 }"
        placeholder="输入消息...（回车发送，Shift+回车换行）"
        :disabled="loading"
        @keydown.enter.exact.prevent="sendMessageFromInput"
        @keydown.shift.enter.exact="handleShiftEnter"
      />
      <el-button 
        type="primary"
        circle
        @click="sendMessageFromInput"
        :loading="loading"
        :disabled="!userInput.trim()"
      >
        <el-icon><Promotion /></el-icon>
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ChatDotRound, Delete, Promotion } from '@element-plus/icons-vue'
import { aiApi } from '@/api/ai'

const userInput = ref('')
const messages = ref([])
const loading = ref(false)
const messagesContainer = ref(null)
const sessionId = ref('')

// 格式化时间
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

// 格式化消息（支持Markdown）
const formatMessage = (content) => {
  if (!content) return ''
  
  let formatted = content
  
  // 处理代码块
  formatted = formatted.replace(/```(\w+)?\n([\s\S]*?)```/g, (match, lang, code) => {
    const escapedCode = code.replace(/</g, '&lt;').replace(/>/g, '&gt;')
    return `<pre class="code-block"><code>${escapedCode}</code></pre>`
  })
  
  // 处理行内代码
  formatted = formatted.replace(/`([^`]+)`/g, '<code class="inline-code">$1</code>')
  
  // 处理换行
  formatted = formatted.replace(/\n/g, '<br/>')
  
  // 处理粗体
  formatted = formatted.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
  
  // 处理标题
  formatted = formatted.replace(/###\s+(.*?)(<br\/>|$)/g, '<h4>$1</h4>')
  formatted = formatted.replace(/##\s+(.*?)(<br\/>|$)/g, '<h3>$1</h3>')
  
  return formatted
}

// 处理Shift+回车（换行）
const handleShiftEnter = (event) => {
  const textarea = event.target
  const start = textarea.selectionStart
  const end = textarea.selectionEnd
  
  userInput.value = userInput.value.substring(0, start) + '\n' + userInput.value.substring(end)
  
  nextTick(() => {
    textarea.selectionStart = textarea.selectionEnd = start + 1
  })
}

// 从输入框发送消息
const sendMessageFromInput = () => {
  if (!userInput.value.trim() || loading.value) return
  sendMessage(userInput.value.trim())
}

// 发送消息（可从父组件调用）
const sendMessage = async (messageText) => {
  if (!messageText || loading.value) return

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: messageText,
    timestamp: Date.now()
  })
  
  userInput.value = ''
  scrollToBottom()
  
  try {
    loading.value = true
    
    const response = await aiApi.sendMessage({
      message: messageText,
      sessionId: sessionId.value,
      history: messages.value.slice(-10)
    })
    
    if (response.data && response.data.success) {
      if (response.data.sessionId) {
        sessionId.value = response.data.sessionId
      }
      
      messages.value.push({
        role: 'assistant',
        content: response.data.message,
        timestamp: Date.now()
      })
      
      scrollToBottom()
    } else {
      ElMessage.error(response.data.error || 'AI回复失败')
    }
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送消息失败')
    
    messages.value.push({
      role: 'assistant',
      content: '抱歉，我遇到了一些问题。请稍后再试。',
      timestamp: Date.now()
    })
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

// 从父组件发送消息
const sendMessageFromParent = (message) => {
  sendMessage(message)
}

// 清空对话
const clearChat = async () => {
  try {
    if (sessionId.value) {
      await aiApi.clearSession(sessionId.value)
    }
    messages.value = []
    sessionId.value = ''
    ElMessage.success('对话已清空')
  } catch (error) {
    console.error('清空对话失败:', error)
    messages.value = []
    sessionId.value = ''
  }
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 加载历史记录
const loadHistory = async () => {
  try {
    const response = await aiApi.getUserHistory('default_user', 50)
    if (response.data && Array.isArray(response.data)) {
      messages.value = response.data
      scrollToBottom()
    }
  } catch (error) {
    console.error('加载历史记录失败:', error)
  }
}

// 暴露方法给父组件
defineExpose({
  sendMessageFromParent
})

onMounted(() => {
  loadHistory()
})
</script>

<style scoped lang="scss">
.ai-chat-box {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
}

.chat-box-header {
  padding: 16px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #e5e7eb;
  flex-shrink: 0;
  
  .header-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    
    .el-icon {
      font-size: 20px;
      color: #409eff;
    }
  }
}

.chat-box-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  
  &::-webkit-scrollbar {
    width: 6px;
  }
  
  &::-webkit-scrollbar-thumb {
    background: #dcdfe6;
    border-radius: 3px;
  }
}

.empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  
  .empty-icon {
    font-size: 48px;
    margin-bottom: 16px;
    opacity: 0.5;
  }
  
  p {
    margin-bottom: 16px;
    font-size: 14px;
  }
  
  .quick-actions {
    display: flex;
    gap: 8px;
  }
}

.message-item {
  display: flex;
  animation: fadeIn 0.3s ease;
  
  &.user {
    justify-content: flex-end;
    
    .message-content {
      background: #409eff;
      color: white;
      border-radius: 16px 16px 4px 16px;
      max-width: 75%;
    }
  }
  
  &.assistant {
    justify-content: flex-start;
    
    .message-content {
      background: #f5f7fa;
      color: #303133;
      border-radius: 16px 16px 16px 4px;
      max-width: 85%;
      
      &.thinking {
        padding: 12px 16px;
      }
    }
  }
}

.message-content {
  padding: 10px 14px;
  
  .message-text {
    font-size: 14px;
    line-height: 1.6;
    word-wrap: break-word;
    
    :deep(.code-block) {
      background: #282c34;
      border-radius: 6px;
      padding: 12px;
      margin: 8px 0;
      overflow-x: auto;
      
      code {
        font-family: 'Menlo', 'Monaco', monospace;
        font-size: 12px;
        color: #abb2bf;
        display: block;
        white-space: pre;
      }
    }
    
    :deep(.inline-code) {
      background: rgba(0, 0, 0, 0.08);
      padding: 2px 6px;
      border-radius: 4px;
      font-family: 'Menlo', 'Monaco', monospace;
      font-size: 13px;
      color: #e83e8c;
    }
    
    :deep(strong) {
      font-weight: 600;
    }
    
    :deep(h3), :deep(h4) {
      margin: 12px 0 8px 0;
      font-weight: 600;
    }
  }
  
  .message-time {
    display: block;
    font-size: 11px;
    margin-top: 6px;
    opacity: 0.6;
  }
}

.typing-dots {
  display: flex;
  gap: 6px;
  padding: 4px 0;
  
  span {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: #909399;
    animation: bounce 1.4s infinite;
    
    &:nth-child(1) { animation-delay: 0s; }
    &:nth-child(2) { animation-delay: 0.2s; }
    &:nth-child(3) { animation-delay: 0.4s; }
  }
}

.chat-box-input {
  padding: 16px;
  border-top: 1px solid #e5e7eb;
  display: flex;
  gap: 12px;
  align-items: flex-end;
  flex-shrink: 0;
  
  .el-input {
    flex: 1;
    
    :deep(.el-textarea__inner) {
      border: 1px solid #e5e7eb;
      border-radius: 18px;
      padding: 10px 16px;
      font-size: 14px;
      resize: none;
      
      &:focus {
        border-color: #409eff;
      }
    }
  }
  
  .el-button {
    width: 40px;
    height: 40px;
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(8px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes bounce {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-8px);
  }
}
</style>

