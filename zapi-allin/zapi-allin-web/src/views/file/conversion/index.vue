<template>
  <div class="file-conversion-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">文件格式转换</h2>
    </div>

    <!-- 转换类型选择 -->
    <div class="conversion-type-selector">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon><Operation /></el-icon>
            <span>选择转换类型</span>
          </div>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <div 
              class="conversion-card"
              :class="{ active: conversionType === 'word-to-pdf' }"
              @click="selectConversionType('word-to-pdf')"
            >
              <div class="conversion-icon">
                <el-icon><Document /></el-icon>
                <el-icon class="arrow"><Right /></el-icon>
                <el-icon><Tickets /></el-icon>
              </div>
              <div class="conversion-title">Word 转 PDF</div>
              <div class="conversion-desc">.doc / .docx → .pdf</div>
            </div>
          </el-col>
          
          <el-col :span="12">
            <div 
              class="conversion-card"
              :class="{ active: conversionType === 'pdf-to-word' }"
              @click="selectConversionType('pdf-to-word')"
            >
              <div class="conversion-icon">
                <el-icon><Tickets /></el-icon>
                <el-icon class="arrow"><Right /></el-icon>
                <el-icon><Document /></el-icon>
              </div>
              <div class="conversion-title">PDF 转 Word</div>
              <div class="conversion-desc">.pdf → .docx</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <!-- 文件上传区域 -->
    <div class="upload-area">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon><Upload /></el-icon>
            <span>上传文件</span>
          </div>
        </template>
        
        <el-upload
          ref="uploadRef"
          class="upload-demo"
          drag
          :auto-upload="false"
          :limit="1"
          :on-change="handleFileChange"
          :on-exceed="handleExceed"
          :accept="getAcceptTypes()"
          :file-list="fileList"
        >
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              {{ getUploadTip() }}
            </div>
          </template>
        </el-upload>

        <div class="upload-options">
          <el-checkbox v-model="autoDownload">转换完成后自动下载</el-checkbox>
        </div>
      </el-card>
    </div>

    <!-- 转换按钮 -->
    <div class="action-buttons">
      <el-button 
        type="primary" 
        size="large" 
        :loading="converting"
        :disabled="!conversionType || fileList.length === 0"
        @click="startConversion"
      >
        <el-icon v-if="!converting" class="el-icon--left"><VideoPlay /></el-icon>
        {{ converting ? '转换中...' : '开始转换' }}
      </el-button>
      <el-button 
        size="large" 
        @click="clearFiles"
        :disabled="converting"
      >
        <el-icon class="el-icon--left"><Delete /></el-icon>
        清空文件
      </el-button>
    </div>

    <!-- 转换进度 -->
    <div v-if="conversionProgress.length > 0" class="conversion-progress">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon class="el-icon--left"><Timer /></el-icon>
            <span>转换进度</span>
          </div>
        </template>
        
        <div class="progress-list">
          <div 
            v-for="(item, index) in conversionProgress" 
            :key="index"
            class="progress-item"
          >
            <div class="progress-info">
              <div class="file-name">
                <el-icon class="el-icon--left"><Document /></el-icon>
                {{ item.fileName }}
              </div>
              <div class="progress-status">
                <el-tag 
                  :type="item.status === 'success' ? 'success' : (item.status === 'error' ? 'danger' : 'info')"
                  size="small"
                >
                  {{ item.statusText }}
                </el-tag>
              </div>
            </div>
            
            <el-progress 
              v-if="item.status === 'converting'"
              :percentage="item.progress" 
              :status="item.status === 'error' ? 'exception' : undefined"
            />
            
            <div v-if="item.error" class="error-message">
              <el-text type="danger" size="small">{{ item.error }}</el-text>
            </div>
            
            <div v-if="item.status === 'success'" class="success-actions">
              <el-button 
                type="primary" 
                size="small" 
                @click="downloadFile(item.fileId)"
              >
                <el-icon class="el-icon--left"><Download /></el-icon>
                下载文件
              </el-button>
              <el-text type="success" size="small">
                文件大小: {{ formatFileSize(item.fileSize) }}
              </el-text>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 转换历史记录 -->
    <div v-if="historyList.length > 0" class="conversion-history">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <div class="header-left">
              <el-icon class="el-icon--left"><Clock /></el-icon>
              <span>最近转换</span>
            </div>
            <el-button 
              type="text" 
              size="small" 
              @click="loadHistory"
              :loading="loadingHistory"
            >
              <el-icon class="el-icon--left"><Refresh /></el-icon>
            </el-button>
          </div>
        </template>
        
        <el-table 
          :data="historyList" 
          v-loading="loadingHistory"
          :show-header="true"
        >
          <el-table-column prop="fileName" label="文件名" min-width="200" show-overflow-tooltip />
          <el-table-column prop="fileSize" label="大小" width="100">
            <template #default="{ row }">
              {{ formatFileSize(row.fileSize) }}
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="转换时间" width="160">
            <template #default="{ row }">
              {{ formatTime(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center">
            <template #default="{ row }">
              <el-button 
                type="primary" 
                size="small"
                link
                @click="downloadFile(row.id)"
              >
                <el-icon class="el-icon--left"><Download /></el-icon>
                下载
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Document, Tickets, Right, Upload, UploadFilled, VideoPlay, 
  Delete, Timer, Clock, Refresh, Download, Operation
} from '@element-plus/icons-vue'
import axios from 'axios'
import type { UploadInstance, UploadUserFile } from 'element-plus'

// 响应式数据
const uploadRef = ref<UploadInstance>()
const conversionType = ref<string>('')
const fileList = ref<UploadUserFile[]>([])
const converting = ref(false)
const autoDownload = ref(true)
const conversionProgress = ref<any[]>([])
const historyList = ref<any[]>([])
const loadingHistory = ref(false)

// 选择转换类型
const selectConversionType = (type: string) => {
  conversionType.value = type
  
  // 切换转换类型时清空文件列表
  if (fileList.value.length > 0) {
    ElMessageBox.confirm('切换转换类型将清空已选文件，是否继续？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      clearFiles()
    }).catch(() => {
      // 取消则不切换
    })
  }
}

// 获取接受的文件类型
const getAcceptTypes = () => {
  const typeMap: Record<string, string> = {
    'word-to-pdf': '.doc,.docx',
    'pdf-to-word': '.pdf'
  }
  return typeMap[conversionType.value] || '*'
}

// 获取上传提示
const getUploadTip = () => {
  const tipMap: Record<string, string> = {
    'word-to-pdf': '支持 .doc、.docx 格式，单个文件最大 50MB',
    'pdf-to-word': '支持 .pdf 格式，单个文件最大 50MB'
  }
  return tipMap[conversionType.value] || '请先选择转换类型'
}

// 文件选择变化
const handleFileChange = (file: UploadUserFile, files: UploadUserFile[]) => {
  fileList.value = files
}

// 文件超出限制
const handleExceed = () => {
  ElMessage.warning('一次只能转换一个文件')
}

// 清空文件
const clearFiles = () => {
  uploadRef.value?.clearFiles()
  fileList.value = []
  conversionProgress.value = []
}

// 开始转换
const startConversion = async () => {
  if (!conversionType.value) {
    ElMessage.warning('请选择转换类型')
    return
  }
  
  if (fileList.value.length === 0) {
    ElMessage.warning('请选择要转换的文件')
    return
  }

  converting.value = true
  conversionProgress.value = []

  try {
    // 单个文件转换
    await singleConvert()
  } finally {
    converting.value = false
  }
}

// 单个文件转换
const singleConvert = async () => {
  const file = fileList.value[0]
  
  // 添加进度项
  const progressItem = {
    fileName: file.name,
    progress: 0,
    status: 'converting',
    statusText: '转换中',
    error: null,
    fileId: null,
    fileSize: 0
  }
  conversionProgress.value.push(progressItem)

  try {
    const formData = new FormData()
    formData.append('file', file.raw as File)
    formData.append('uploadUserId', '1') // TODO: 从用户信息中获取
    formData.append('uploadUsername', 'admin')

    // 根据转换类型选择接口
    const apiPath = getApiPath(conversionType.value)
    
    // 模拟进度更新
    const progressInterval = setInterval(() => {
      if (progressItem.progress < 90) {
        progressItem.progress += 10
      }
    }, 200)

    const response = await axios.post(apiPath, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })

    clearInterval(progressInterval)

    if (response.data.code === 200) {
      progressItem.progress = 100
      progressItem.status = 'success'
      progressItem.statusText = '转换成功'
      progressItem.fileId = response.data.data.id
      progressItem.fileSize = response.data.data.fileSize

      ElMessage.success('文件转换成功')

      // 自动下载
      if (autoDownload.value && response.data.data.id) {
        setTimeout(() => {
          downloadFile(response.data.data.id)
        }, 500)  // 延迟500ms确保文件已保存
      }

      // 刷新历史记录
      loadHistory()
    } else {
      throw new Error(response.data.message || '转换失败')
    }
  } catch (error: any) {
    progressItem.progress = 0
    progressItem.status = 'error'
    progressItem.statusText = '转换失败'
    progressItem.error = error.message || '未知错误'
    ElMessage.error('转换失败: ' + (error.message || '未知错误'))
  }
}

// 获取API路径
const getApiPath = (type: string): string => {
  const pathMap: Record<string, string> = {
    'word-to-pdf': '/zapi-allin-sys/file/conversion/word-to-pdf',
    'pdf-to-word': '/zapi-allin-sys/file/conversion/pdf-to-word'
  }
  return pathMap[type] || ''
}

// 下载文件
const downloadFile = (fileId: number) => {
  
  if (!fileId) {
    ElMessage.error('文件ID无效')
    return
  }
  
  try {
    const downloadUrl = `/zapi-allin-sys/file/conversion/download/${fileId}`
    
    // 使用axios检查文件是否存在
    axios.get(`/zapi-allin-sys/file/info/${fileId}`).then(response => {
      if (response.data.code === 200) {
        // 文件存在，开始下载
        window.open(downloadUrl, '_blank')
      } else {
        ElMessage.error('文件不存在: ' + response.data.message)
      }
    }).catch(error => {
      console.error('获取文件信息失败:', error)
      ElMessage.error('获取文件信息失败，但仍尝试下载')
      // 即使检查失败也尝试下载
      window.open(downloadUrl, '_blank')
    })
  } catch (error: any) {
    console.error('下载文件失败:', error)
    ElMessage.error('下载文件失败: ' + (error.message || '未知错误'))
  }
}

// 格式化文件大小
const formatFileSize = (size: number): string => {
  if (!size) return '-'
  if (size < 1024) return size + ' B'
  if (size < 1024 * 1024) return (size / 1024).toFixed(1) + ' KB'
  if (size < 1024 * 1024 * 1024) return (size / (1024 * 1024)).toFixed(1) + ' MB'
  return (size / (1024 * 1024 * 1024)).toFixed(1) + ' GB'
}

// 格式化时间
const formatTime = (time: string): string => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

// 加载转换历史
const loadHistory = async () => {
  try {
    loadingHistory.value = true
    const response = await axios.get('/zapi-allin-sys/file/list', {
      params: {
        current: 1,
        size: 10,
        path: 'conversion'
      }
    })

    if (response.data.code === 200) {
      historyList.value = response.data.data.records || []
    }
  } catch (error) {
    console.error('加载历史记录失败:', error)
  } finally {
    loadingHistory.value = false
  }
}

// 组件挂载时加载历史记录
onMounted(() => {
  loadHistory()
})
</script>

<style scoped lang="scss">
.file-conversion-container {
  padding: 20px;
  background: #f5f5f5;
  min-height: calc(100vh - 60px);

  .conversion-type-selector {
    margin-bottom: 16px;

    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 15px;
      font-weight: 600;
    }

    .conversion-card {
      padding: 24px 20px;
      text-align: center;
      border: 2px solid #e4e7ed;
      border-radius: 8px;
      cursor: pointer;
      transition: all 0.25s;
      background: white;

      &:hover {
        border-color: #409eff;
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
        transform: translateY(-2px);
      }

      &.active {
        border-color: #409eff;
        background: #f0f9ff;
        box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
      }

      .conversion-icon {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 8px;
        margin-bottom: 12px;
        font-size: 32px;
        color: #409eff;

        .arrow {
          font-size: 20px;
          color: #909399;
        }
      }

      .conversion-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 8px;
      }

      .conversion-desc {
        font-size: 12px;
        color: #909399;
      }
    }
  }

  .upload-area {
    margin-bottom: 16px;

    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 15px;
      font-weight: 600;
    }

    .upload-demo {
      margin-bottom: 16px;
    }

    .upload-options {
      display: flex;
      gap: 16px;
      padding: 10px 14px;
      background: #f8f9fa;
      border-radius: 6px;
    }
  }

  .action-buttons {
    margin-bottom: 16px;
    text-align: center;
    display: flex;
    justify-content: center;
    gap: 12px;
  }

  .conversion-progress {
    margin-bottom: 16px;

    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      font-size: 15px;
      font-weight: 600;
    }

    .progress-list {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .progress-item {
      padding: 14px;
      border: 1px solid #e4e7ed;
      border-radius: 6px;
      background: #fafafa;

      .progress-info {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 12px;

        .file-name {
          display: flex;
          align-items: center;
          gap: 8px;
          font-weight: 500;
          color: #303133;
        }
      }

      .error-message {
        margin-top: 8px;
      }

      .success-actions {
        margin-top: 12px;
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
    }
  }

  .conversion-history {
    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 16px;
      font-weight: 600;

      .header-left {
        display: flex;
        align-items: center;
        gap: 8px;
      }
    }

    :deep(.el-table) {
      border: none;
      background: transparent;
    }

    :deep(.el-table__header) {
      background: transparent;

      .el-table__cell {
        background: transparent;
        border: none;
        color: var(--text-secondary);
        font-weight: 500;
        padding: 16px 0;
      }
    }

    :deep(.el-table__row) {
      background: transparent;
      border: none;

      &:hover {
        background: var(--bg-tertiary);
      }

      .el-table__cell {
        border: none;
        padding: 16px 0;
        background: transparent;
      }
    }

    :deep(.el-table__inner-wrapper) {
      border: none;
    }

    :deep(.el-table__border-line) {
      display: none;
    }
  }
}
</style>