<template>
  <div class="file-upload-demo">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">文件上传</h1>
      </div>
    </div>

    <div class="demo-content">

      <!-- 单文件上传 -->
      <el-card class="demo-section" shadow="never">
        <template #header>
          <h3>1. 单文件上传</h3>
        </template>
        <FileUpload
          v-model="singleFileList"
          :accept="'.jpg,.png,.pdf'"
          :max-size="5 * 1024 * 1024"
          :description="'单文件上传演示'"
          @upload-success="handleSingleUploadSuccess"
          @upload-error="handleUploadError"
        />
        <div class="result-info">
          <p><strong>文件ID:</strong> {{ singleFileId || '暂无' }}</p>
          <p><strong>文件信息:</strong></p>
          <pre>{{ JSON.stringify(singleFileList, null, 2) }}</pre>
        </div>
      </el-card>

      <!-- 多文件上传 -->
      <el-card class="demo-section" shadow="never">
        <template #header>
          <h3>2. 多文件上传</h3>
        </template>
        <FileUpload
          v-model="multipleFileList"
          :multiple="true"
          :accept="'.jpg,.png,.pdf,.doc,.docx'"
          :max-size="10 * 1024 * 1024"
          :path="'/uploads/demo'"
          :description="'多文件上传演示'"
          @upload-success="handleMultipleUploadSuccess"
          @upload-error="handleUploadError"
        />
        <div class="result-info">
          <p><strong>文件数量:</strong> {{ multipleFileList.length }}</p>
          <p><strong>文件ID列表:</strong> {{ multipleFileIds.join(', ') || '暂无' }}</p>
        </div>
      </el-card>

      <!-- 手动上传 -->
      <el-card class="demo-section" shadow="never">
        <template #header>
          <h3>3. 手动上传模式</h3>
        </template>
        <FileUpload
          ref="manualUploadRef"
          v-model="manualFileList"
          :auto-upload="false"
          :multiple="true"
          :accept="'.jpg,.png'"
          :description="'手动上传演示'"
        />
        <div class="action-buttons">
          <el-button type="primary" @click="handleManualUpload">开始上传</el-button>
          <el-button @click="handleClearManual">清空列表</el-button>
        </div>
        <div class="result-info">
          <p><strong>待上传文件:</strong> {{ manualFileList.length }} 个</p>
        </div>
      </el-card>

      <!-- 图片上传 -->
      <el-card class="demo-section" shadow="never">
        <template #header>
          <h3>4. 图片上传（支持预览）</h3>
        </template>
        <FileUpload
          v-model="imageFileList"
          :accept="'.jpg,.jpeg,.png,.gif,.webp'"
          :max-size="2 * 1024 * 1024"
          :path="'/uploads/images'"
          :description="'图片上传演示'"
          @upload-success="handleImageUploadSuccess"
          @upload-error="handleUploadError"
          @file-preview="handleFilePreview"
        />
        <div class="result-info">
          <p><strong>图片数量:</strong> {{ imageFileList.length }}</p>
        </div>
      </el-card>

      <!-- 文档上传 -->
      <el-card class="demo-section" shadow="never">
        <template #header>
          <h3>5. 文档上传</h3>
        </template>
        <FileUpload
          v-model="documentFileList"
          :multiple="true"
          :accept="'.pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt'"
          :max-size="20 * 1024 * 1024"
          :path="'/uploads/documents'"
          :description="'文档上传演示'"
          @upload-success="handleDocumentUploadSuccess"
          @upload-error="handleUploadError"
          @file-download="handleFileDownload"
        />
        <div class="result-info">
          <p><strong>文档数量:</strong> {{ documentFileList.length }}</p>
        </div>
      </el-card>

      <!-- 禁用状态 -->
      <el-card class="demo-section" shadow="never">
        <template #header>
          <h3>6. 禁用状态</h3>
        </template>
        <FileUpload
          v-model="disabledFileList"
          :disabled="true"
          :accept="'.jpg,.png'"
          :description="'禁用状态演示'"
        />
        <div class="result-info">
          <p><em>此上传组件已被禁用，无法进行文件上传操作</em></p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import FileUpload from '@/components/FileUpload/index.vue'

// 单文件上传
const singleFileList = ref([])
const singleFileId = computed(() => {
  return singleFileList.value.length > 0 ? singleFileList.value[0].id : null
})

// 多文件上传
const multipleFileList = ref([])
const multipleFileIds = computed(() => {
  return multipleFileList.value.map(file => file.id)
})

// 手动上传
const manualUploadRef = ref()
const manualFileList = ref([])

// 图片上传
const imageFileList = ref([])

// 文档上传
const documentFileList = ref([])

// 禁用状态
const disabledFileList = ref([])

// 事件处理
const handleSingleUploadSuccess = (fileInfo, file) => {
  ElMessage.success(`单文件上传成功: ${fileInfo.fileName}`)
}

const handleMultipleUploadSuccess = (fileInfo, file) => {
  ElMessage.success(`多文件上传成功: ${fileInfo.fileName}`)
}

const handleImageUploadSuccess = (fileInfo, file) => {
  ElMessage.success(`图片上传成功: ${fileInfo.fileName}`)
}

const handleDocumentUploadSuccess = (fileInfo, file) => {
  ElMessage.success(`文档上传成功: ${fileInfo.fileName}`)
}

const handleUploadError = (error, file) => {
  ElMessage.error(`文件上传失败: ${file.name}`)
  console.error('上传失败:', error)
}

const handleFilePreview = (file) => {
}

const handleFileDownload = (file) => {
  ElMessage.success(`开始下载文件: ${file.fileName}`)
}

const handleManualUpload = () => {
  if (manualFileList.value.length === 0) {
    ElMessage.warning('请先选择要上传的文件')
    return
  }
  
  manualUploadRef.value.upload()
}

const handleClearManual = () => {
  manualUploadRef.value.clear()
  ElMessage.success('文件列表已清空')
}
</script>

<style lang="scss" scoped>
.file-upload-demo {
  animation: fadeIn 0.6s ease-out;
}

.demo-content {
  max-width: 1200px;
  margin: 0 auto;
}

.demo-section {
  margin-bottom: 24px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}

.demo-section:last-child {
  margin-bottom: 0;
}

.demo-section :deep(.el-card__header) {
  background: var(--bg-tertiary);
  border-bottom: 1px solid var(--border-color);
  padding: 16px 20px;
}

.demo-section h3 {
  margin: 0;
  color: var(--text-primary);
  font-size: 16px;
  font-weight: 600;
}

.demo-section :deep(.el-card__body) {
  padding: 20px;
}

.result-info {
  margin-top: 16px;
  padding: 16px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border-left: 4px solid var(--color-primary);
}

.result-info p {
  margin: 4px 0;
  font-size: 14px;
  color: var(--text-secondary);
}

.result-info pre {
  margin: 8px 0 0 0;
  padding: 12px;
  background: var(--bg-card);
  border-radius: 6px;
  font-size: 12px;
  color: var(--text-primary);
  overflow-x: auto;
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid var(--border-color);
}

.action-buttons {
  margin-top: 16px;
  display: flex;
  gap: 12px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }
  
  .demo-content {
    margin: 0;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .action-buttons .el-button {
    width: 100%;
  }
}
</style> 