<template>
  <div class="pdf-viewer-example">
    <el-card shadow="hover">
      <template #header>
        <div class="card-header">
          <span>PDF 预览组件使用示例</span>
          <div class="mode-switch">
            <el-radio-group v-model="currentMode" @change="handleModeChange">
              <el-radio-button label="preview">预览模式</el-radio-button>
              <el-radio-button label="stamp">盖章模式</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </template>

      <!-- 文件选择区域 -->
      <div class="file-selection" v-if="!selectedFile">
        <el-upload
          class="upload-demo"
          drag
          :auto-upload="false"
          :on-change="handleFileSelect"
          accept=".pdf"
          :limit="1"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将 PDF 文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              只能上传 PDF 文件
            </div>
          </template>
        </el-upload>

        <el-divider>或选择示例文件</el-divider>

        <div class="example-files">
          <el-button
            v-for="file in exampleFiles"
            :key="file.id"
            @click="selectExampleFile(file)"
            type="primary"
            plain
          >
            {{ file.name }}
          </el-button>
        </div>
      </div>

      <!-- PDF 预览组件 -->
      <div v-else class="pdf-viewer-wrapper">
        <div class="viewer-header">
          <div class="file-info">
            <span class="file-name">{{ selectedFile.name }}</span>
            <span class="file-mode">{{ currentMode === 'preview' ? '预览模式' : '盖章模式' }}</span>
          </div>
          <div class="header-actions">
            <el-button @click="resetFile" size="small">
              <el-icon><Refresh /></el-icon>
              重新选择
            </el-button>
          </div>
        </div>

        <PdfViewer
          :file-url="selectedFile.url"
          :mode="currentMode"
          :contract-id="contractId"
          :file-id="selectedFile.id"
          @stamp-success="handleStampSuccess"
          @stamp-error="handleStampError"
        >
          <template #toolbar-actions>
            <el-button v-if="currentMode === 'stamp'" type="success" size="small">
              <el-icon><Download /></el-icon>
              下载盖章文件
            </el-button>
          </template>
        </PdfViewer>
      </div>
    </el-card>

    <!-- 操作日志 -->
    <el-card shadow="hover" style="margin-top: 20px;" v-if="operationLogs.length > 0">
      <template #header>
        <span>操作日志</span>
        <el-button @click="clearLogs" size="small" type="danger" plain style="float: right;">
          清空日志
        </el-button>
      </template>

      <div class="logs-container">
        <div
          v-for="(log, index) in operationLogs"
          :key="index"
          :class="['log-item', log.type]"
        >
          <span class="log-time">{{ log.time }}</span>
          <span class="log-message">{{ log.message }}</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, Refresh, Download } from '@element-plus/icons-vue'
import PdfViewer from '@/components/PdfViewer.vue'

// 当前模式
const currentMode = ref('preview')

// 选中的文件
const selectedFile = ref(null)

// 合同ID（示例）
const contractId = ref('12345')

// 示例文件列表
const exampleFiles = reactive([
  {
    id: 1,
    name: '合同示例.pdf',
    url: '/zapi-allin-sys/file/download/1'
  },
  {
    id: 2,
    name: '协议示例.pdf',
    url: '/zapi-allin-sys/file/download/2'
  }
])

// 操作日志
const operationLogs = ref([])

// 处理文件选择
const handleFileSelect = (file) => {
  const fileUrl = URL.createObjectURL(file.raw)
  selectedFile.value = {
    id: Date.now(),
    name: file.name,
    url: fileUrl
  }

  addLog('info', `选择文件: ${file.name}`)
}

// 选择示例文件
const selectExampleFile = (file) => {
  selectedFile.value = { ...file }
  addLog('info', `选择示例文件: ${file.name}`)
}

// 重置文件选择
const resetFile = () => {
  if (selectedFile.value && selectedFile.value.url.startsWith('blob:')) {
    URL.revokeObjectURL(selectedFile.value.url)
  }
  selectedFile.value = null
  addLog('info', '重新选择文件')
}

// 处理模式切换
const handleModeChange = (mode) => {
  addLog('info', `切换到${mode === 'preview' ? '预览' : '盖章'}模式`)
}

// 处理盖章成功
const handleStampSuccess = (stampData) => {
  addLog('success', `盖章成功 - 页码: ${stampData.signPage}, 位置: (${Math.round(stampData.pageX)}, ${Math.round(stampData.pageY)})`)
  ElMessage.success('盖章操作成功！')
}

// 处理盖章失败
const handleStampError = (error) => {
  console.error('盖章失败:', error)
  addLog('error', `盖章失败: ${error.message || '未知错误'}`)
  ElMessage.error('盖章操作失败！')
}

// 添加日志
const addLog = (type, message) => {
  const now = new Date()
  const time = now.toLocaleTimeString()

  operationLogs.value.unshift({
    type,
    message,
    time,
    timestamp: now.getTime()
  })

  // 限制日志数量
  if (operationLogs.value.length > 50) {
    operationLogs.value = operationLogs.value.slice(0, 50)
  }
}

// 清空日志
const clearLogs = () => {
  operationLogs.value = []
  ElMessage.success('日志已清空')
}
</script>

<style scoped>
.pdf-viewer-example {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.mode-switch {
  display: flex;
  align-items: center;
}

.file-selection {
  text-align: center;
  padding: 40px 20px;
}

.upload-demo {
  margin-bottom: 30px;
}

.example-files {
  display: flex;
  justify-content: center;
  gap: 15px;
  flex-wrap: wrap;
}

.pdf-viewer-wrapper {
  height: 80vh;
  display: flex;
  flex-direction: column;
}

.viewer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 15px;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.file-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.file-mode {
  padding: 4px 12px;
  background: #ecf5ff;
  color: #409eff;
  border-radius: 12px;
  font-size: 12px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.logs-container {
  max-height: 300px;
  overflow-y: auto;
}

.log-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  margin-bottom: 8px;
  border-radius: 4px;
  font-size: 13px;
}

.log-item.info {
  background: #f4f4f5;
  color: #606266;
}

.log-item.success {
  background: #f0f9ff;
  color: #67c23a;
}

.log-item.error {
  background: #fef0f0;
  color: #f56c6c;
}

.log-time {
  font-family: monospace;
  margin-right: 12px;
  color: #909399;
  min-width: 80px;
}

.log-message {
  flex: 1;
}
</style>
