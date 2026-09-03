<template>
  <div class="file-upload-example">
    <el-card>
      <template #header>
        <h2>文件上传组件使用示例</h2>
      </template>

      <!-- 基本用法 -->
      <div class="example-section">
        <h3>基本用法</h3>
        <FileUpload
          v-model="fileList"
          :accept="'.jpg,.png,.pdf'"
          :max-size="5 * 1024 * 1024"
          @upload-success="onUploadSuccess"
          @upload-error="onUploadError"
        />
        
        <div class="file-info">
          <p><strong>已上传文件数量:</strong> {{ fileList.length }}</p>
          <p><strong>文件ID列表:</strong> {{ fileIds.join(', ') || '暂无' }}</p>
        </div>
      </div>

      <!-- 多文件上传 -->
      <div class="example-section">
        <h3>多文件上传</h3>
        <FileUpload
          v-model="multipleFileList"
          :multiple="true"
          :accept="'.jpg,.png,.pdf,.doc,.docx'"
          :max-size="10 * 1024 * 1024"
          @upload-success="onUploadSuccess"
          @upload-error="onUploadError"
        />
        
        <div class="file-info">
          <p><strong>已上传文件数量:</strong> {{ multipleFileList.length }}</p>
        </div>
      </div>

      <!-- 手动上传 -->
      <div class="example-section">
        <h3>手动上传</h3>
        <FileUpload
          ref="uploadRef"
          v-model="manualFileList"
          :auto-upload="false"
          :multiple="true"
          :accept="'.jpg,.png'"
        />
        
        <div class="actions">
          <el-button type="primary" @click="uploadFiles">开始上传</el-button>
          <el-button @click="clearFiles">清空列表</el-button>
        </div>
        
        <div class="file-info">
          <p><strong>待上传文件数量:</strong> {{ manualFileList.length }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import FileUpload from './index.vue'

// 响应式数据
const fileList = ref([])
const multipleFileList = ref([])
const manualFileList = ref([])
const uploadRef = ref()

// 计算属性
const fileIds = computed(() => {
  return fileList.value.map(file => file.id)
})

// 事件处理
const onUploadSuccess = (fileInfo, file) => {
  ElMessage.success(`文件上传成功: ${fileInfo.fileName}`)
  console.log('上传成功，文件ID:', fileInfo.id)
  console.log('文件信息:', fileInfo)
}

const onUploadError = (error, file) => {
  ElMessage.error(`文件上传失败: ${file.name}`)
  console.error('上传失败:', error)
}

// 手动上传
const uploadFiles = () => {
  if (manualFileList.value.length === 0) {
    ElMessage.warning('请先选择要上传的文件')
    return
  }
  uploadRef.value.upload()
}

// 清空文件列表
const clearFiles = () => {
  uploadRef.value.clear()
  ElMessage.success('文件列表已清空')
}
</script>

<style scoped>
.file-upload-example {
  padding: 20px;
}

.example-section {
  margin-bottom: 30px;
}

.example-section:last-child {
  margin-bottom: 0;
}

.example-section h3 {
  margin-bottom: 16px;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.file-info {
  margin-top: 16px;
  padding: 12px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border-left: 4px solid #409eff;
}

.file-info p {
  margin: 4px 0;
  font-size: 14px;
  color: #606266;
}

.actions {
  margin-top: 16px;
  display: flex;
  gap: 12px;
}
</style> 