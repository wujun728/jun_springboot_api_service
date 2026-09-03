<template>
  <div class="avatar-upload">
    <!-- 头像显示区域 -->
    <div class="avatar-container">
      <div
        class="avatar-display"
        :class="{ 'avatar-uploading': isUploading }"
        @click="handleClick"
      >
        <!-- 已有头像 -->
        <img
          v-if="avatarUrl && !isUploading"
          :src="avatarUrl"
          alt="头像"
          class="avatar-image"
          @error="handleAvatarDisplayError"
        />
        
        <!-- 上传中 -->
        <div v-else-if="isUploading" class="upload-progress">
          <el-progress
            type="circle"
            :percentage="uploadProgress"
            :width="60"
            :stroke-width="4"
          />
          <div class="progress-text">上传中...</div>
        </div>
        
        <!-- 默认头像 -->
        <div v-else class="default-avatar">
          <el-icon class="avatar-icon"><User /></el-icon>
          <div class="upload-text">点击上传头像</div>
        </div>
        
        <!-- 遮罩层 -->
        <div v-if="!isUploading" class="avatar-overlay">
          <el-icon class="upload-icon"><Plus /></el-icon>
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div v-if="avatarUrl && !isUploading" class="avatar-actions">
        <el-button size="small" @click="previewAvatar">
          <el-icon class="el-icon--left"><View /></el-icon>
          预览
        </el-button>
        <el-button size="small" type="danger" @click="removeAvatar">
          <el-icon class="el-icon--left"><Delete /></el-icon>
          删除
        </el-button>
      </div>
    </div>

    <!-- 隐藏的文件输入框 -->
    <input
      ref="fileInput"
      type="file"
      accept="image/*"
      style="display: none"
      @change="handleFileChange"
    />

    <!-- 头像预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      title="头像预览"
      width="500px"
      center
    >
      <div class="preview-container">
        <div v-if="avatarUrl" class="image-preview-wrapper">
          <img
            :src="avatarUrl"
            alt="头像预览"
            class="preview-image"
            @error="handlePreviewImageError"
            @load="handlePreviewImageLoad"
          />
          <div v-if="previewLoading" class="preview-loading">
            <el-icon class="is-loading"><Loading /></el-icon>
            <span>加载中...</span>
          </div>
        </div>
        <div v-else class="no-avatar">
          <el-icon class="no-avatar-icon"><User /></el-icon>
          <span>暂无头像</span>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, Plus, View, Delete, Loading } from '@element-plus/icons-vue'
import { fileApi } from '@/api'
import { getFileDownloadUrl } from '@/config'

// 定义组件属性
const props = defineProps({
  // 头像文件ID或URL
  modelValue: {
    type: [String, Number],
    default: ''
  },
  // 最大文件大小（字节）
  maxSize: {
    type: Number,
    default: 2 * 1024 * 1024 // 2MB
  },
  // 是否禁用
  disabled: {
    type: Boolean,
    default: false
  }
})

// 定义事件
const emit = defineEmits([
  'update:modelValue',
  'upload-success',
  'upload-error',
  'remove'
])

// 响应式数据
const fileInput = ref(null)
const isUploading = ref(false)
const uploadProgress = ref(0)
const previewVisible = ref(false)
const previewLoading = ref(false)

// 计算头像URL（统一使用文件ID，通过 /file/download/{fileId} 接口获取图片流）
const avatarUrl = computed(() => {
  if (!props.modelValue) return ''
  
  const valueStr = String(props.modelValue)
  
  // 1. 数字ID格式（推荐）→ 使用 /file/download/{fileId} 接口
  if (typeof props.modelValue === 'number' || /^\d+$/.test(valueStr)) {
    return getFileDownloadUrl(props.modelValue)
  }
  
  // 2. 相对路径格式（兼容旧数据）→ 添加API前缀
  if (valueStr.startsWith('/file/download/')) {
    return `/zapi-allin-sys${valueStr}`
  }
  
  // 3. 完整URL格式 → 直接使用
  if (valueStr.startsWith('http://') || valueStr.startsWith('https://')) {
    return valueStr
  }
  
  // 4. 其他格式 → 添加API前缀
  return `/zapi-allin-sys${valueStr}`
})

// 验证文件
const validateFile = (file) => {
  // 检查文件类型
  if (!file.type.startsWith('image/')) {
    ElMessage.error('只能上传图片文件')
    return false
  }
  
  // 检查文件大小
  if (file.size > props.maxSize) {
    ElMessage.error(`图片大小不能超过 ${formatFileSize(props.maxSize)}`)
    return false
  }
  
  return true
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 处理文件选择
const handleFileChange = (event) => {
  const file = event.target.files[0]
  if (!file) return
  
  if (!validateFile(file)) {
    event.target.value = ''
    return
  }
  
  uploadFile(file)
  event.target.value = ''
}

// 上传文件
const uploadFile = async (file) => {
  isUploading.value = true
  uploadProgress.value = 0
  
  try {
    const options = {
      path: 'avatar', // 头像专用路径
      description: '用户头像',
      uploadUserId: null,
      uploadUsername: ''
    }
    
    const response = await fileApi.uploadFile(file, options)
    
    if (response.code === 200) {
      const fileInfo = response.data
      uploadProgress.value = 100
      
      // 更新头像文件ID（统一使用文件ID，前端会通过 /file/download/{fileId} 接口显示）
      emit('update:modelValue', fileInfo.id)
      emit('upload-success', fileInfo)
      
      ElMessage.success('头像上传成功')
    } else {
      throw new Error(response.message || '上传失败')
    }
  } catch (error) {
    console.error('头像上传失败:', error)
    emit('upload-error', error)
    ElMessage.error(`头像上传失败：${error.message}`)
  } finally {
    isUploading.value = false
    uploadProgress.value = 0
  }
}

// 点击上传
const handleClick = () => {
  if (!props.disabled && !isUploading.value) {
    fileInput.value?.click()
  }
}

// 预览头像
const previewAvatar = () => {
  if (avatarUrl.value) {
    previewLoading.value = true
    previewVisible.value = true
  } else {
    ElMessage.warning('暂无头像可预览')
  }
}

// 处理预览图片加载完成
const handlePreviewImageLoad = () => {
  previewLoading.value = false
}

// 处理预览图片加载错误
const handlePreviewImageError = (event) => {
  previewLoading.value = false
  console.error('头像预览加载失败:', event)
  ElMessage.error('头像预览加载失败，请检查文件是否存在')
  
  // 替换为错误提示
  const img = event.target
  img.style.display = 'none'
  
  const errorDiv = document.createElement('div')
  errorDiv.className = 'preview-error'
  errorDiv.innerHTML = `
    <div style="
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      min-height: 200px;
      color: #909399;
      background: #f5f7fa;
      border-radius: 8px;
      border: 1px dashed #d9d9d9;
    ">
      <i class="el-icon-picture" style="font-size: 48px; margin-bottom: 16px;"></i>
      <span>头像加载失败</span>
      <span style="font-size: 12px; margin-top: 4px;">文件可能已损坏或不存在</span>
    </div>
  `
  
  img.parentNode.replaceChild(errorDiv, img)
}

// 处理头像显示错误
const handleAvatarDisplayError = (event) => {
  console.error('头像显示失败:', event)
  // 头像显示失败时，不显示错误消息，只是隐藏图片让默认头像显示
  const img = event.target
  img.style.display = 'none'
}

// 删除头像
const removeAvatar = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要删除当前头像吗？',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 如果是文件ID，尝试删除服务器文件
    if (props.modelValue && (typeof props.modelValue === 'number' || /^\d+$/.test(props.modelValue))) {
      try {
        await fileApi.deleteFile(props.modelValue)
      } catch (error) {
        console.error('删除服务器文件失败:', error)
        // 不阻止删除操作，继续清除本地引用
      }
    }
    
    emit('update:modelValue', '')
    emit('remove')
    ElMessage.success('头像删除成功')
  } catch (error) {
    // 用户取消删除
  }
}
</script>

<style scoped lang="scss">
.avatar-upload {
  display: inline-block;
}

.avatar-container {
  text-align: center;
}

.avatar-display {
  position: relative;
  width: 120px;
  height: 120px;
  border: 2px dashed #d9d9d9;
  border-radius: 50%;
  cursor: pointer;
  overflow: hidden;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #fafafa;
  margin: 0 auto;

  &:hover {
    border-color: #409eff;
    
    .avatar-overlay {
      opacity: 1;
    }
  }

  &.avatar-uploading {
    cursor: not-allowed;
    border-color: #409eff;
  }
}

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 50%;
}

.default-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #c0c4cc;
  
  .avatar-icon {
    font-size: 40px;
    margin-bottom: 8px;
  }
  
  .upload-text {
    font-size: 12px;
    color: #909399;
  }
}

.upload-progress {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  
  .progress-text {
    margin-top: 8px;
    font-size: 12px;
    color: #409eff;
  }
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
  border-radius: 50%;
  
  .upload-icon {
    color: white;
    font-size: 24px;
  }
}

.avatar-actions {
  margin-top: 12px;
  display: flex;
  gap: 8px;
  justify-content: center;
}

.preview-container {
  text-align: center;
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.image-preview-wrapper {
  position: relative;
  display: inline-block;
}

.preview-image {
  max-width: 100%;
  max-height: 400px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  object-fit: contain;
}

.preview-loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: var(--text-secondary);
  background: rgba(255, 255, 255, 0.9);
  padding: 20px;
  border-radius: 8px;
  backdrop-filter: blur(4px);

  .el-icon {
    font-size: 24px;
  }

  span {
    font-size: 14px;
  }
}

.no-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: var(--text-secondary);
  min-height: 200px;

  .no-avatar-icon {
    font-size: 48px;
    margin-bottom: 16px;
    color: #c0c4cc;
  }

  span {
    font-size: 14px;
  }
}
</style>
