<template>
  <div class="file-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">文件管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleUpload">
          <el-icon><Upload /></el-icon>
          上传文件
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="search-section">
      <div class="search-left">
        <el-input
          v-model="searchQuery"
          placeholder="搜索文件名..."
          clearable
          style="width: 300px"
          @input="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <!-- <el-select
          v-model="fileTypeFilter"
          placeholder="文件类型"
          clearable
          style="width: 150px"
          @change="handleSearch"
        >
          <el-option label="全部" value="" />
          <el-option label="📷 图片" value="image" />
          <el-option label="📄 PDF文档" value="pdf" />
          <el-option label="📝 Office文档" value="office" />
          <el-option label="📋 文本文件" value="text" />
          <el-option label="🎬 视频" value="video" />
          <el-option label="🎵 音频" value="audio" />
          <el-option label="📦 其他" value="other" />
        </el-select> -->

        <el-select
          v-model="statusFilter"
          placeholder="状态"
          clearable
          style="width: 120px"
          @change="handleSearch"
        >
          <el-option label="全部" value="" />
          <el-option label="正常" value="1" />
          <el-option label="已删除" value="0" />
        </el-select>
      </div>

      <div class="search-right">
        <el-radio-group v-model="viewMode" size="small">
          <el-radio-button label="table">
            <el-icon><List /></el-icon>
          </el-radio-button>
          <el-radio-button label="grid">
            <el-icon><Grid /></el-icon>
          </el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 文件内容区域 -->
    <div class="file-content">
      <!-- 表格视图 -->
      <div v-if="viewMode === 'table'" class="table-view">
        <el-table
          v-loading="loading"
          :data="filteredFiles"
          class="file-table"
          style="width: 100%"
        >
          <el-table-column prop="originalFileName" label="文件名" min-width="180">
            <template #default="{ row }">
              <div class="file-name-cell">
                <el-icon class="file-icon">
                  <component :is="getFileIconByName(row.originalFileName || row.fileName)" />
                </el-icon>
                <span class="file-name">{{ row.originalFileName || row.fileName }}</span>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="fileSize" label="大小" >
            <template #default="{ row }">
              {{ formatFileSize(row.fileSize) }}
            </template>
          </el-table-column>

          <el-table-column label="类型" width="100">
            <template #default="{ row }">
              <el-tag size="small" :type="getFileTypeTagByName(row.originalFileName || row.fileName)">
                {{ getFileTypeByName(row.originalFileName || row.fileName) }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="createTime" label="上传时间" >
            <template #default="{ row }">
              {{ formatDate(row.createTime) }}
            </template>
          </el-table-column>

          <el-table-column prop="status" label="状态" >
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
                {{ row.status === 1 ? '正常' : '已删除' }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="操作" fixed="right" align="center" width="280">
            <template #default="{ row }">
              <div class="action-buttons">
                <el-button size="small" @click="downloadFile(row)">
                  <el-icon class="el-icon--left"><Download /></el-icon>
                  下载
                </el-button>

                <el-button size="small" @click="previewFile(row)">
                  <el-icon class="el-icon--left"><View /></el-icon>
                  预览
                </el-button>

                <el-button size="small" @click="deleteFile(row)">
                  <el-icon class="el-icon--left"><Delete /></el-icon>
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 网格视图 -->
      <div v-else class="grid-view">
        <div class="file-grid">
          <div
            v-for="file in filteredFiles"
            :key="file.id"
            class="file-card"
            @click="previewFile(file)"
          >
            <div class="file-preview">
              <img
                v-if="isImageFile(file)"
                :src="getFilePreviewUrl(file)"
                :alt="file.originalFileName || file.fileName"
                class="file-image"
                @error="handleImageError"
              />
              <div v-else class="file-placeholder">
                <el-icon class="file-icon-large">
                  <component :is="getFileIconByName(file.originalFileName || file.fileName)" />
                </el-icon>
              </div>
            </div>

            <div class="file-info">
              <div class="file-name" :title="file.originalFileName || file.fileName">
                {{ file.originalFileName || file.fileName }}
              </div>
              <div class="file-meta">
                <span class="file-size">{{ formatFileSize(file.fileSize) }}</span>
                <el-tag size="small" :type="getFileTypeTagByName(file.originalFileName || file.fileName)">
                  {{ getFileTypeByName(file.originalFileName || file.fileName) }}
                </el-tag>
              </div>
            </div>

            <div class="file-actions">
              <el-button
                type="primary"
                size="small"
                text
                @click.stop="downloadFile(file)"
              >
                <el-icon><Download /></el-icon>
              </el-button>
              <el-button
                type="danger"
                size="small"
                text
                @click.stop="deleteFile(file)"
              >
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          background
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="totalFiles"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 文件上传对话框 -->
    <el-dialog
      v-model="uploadDialogVisible"
      title="上传文件"
      width="600px"
      @close="handleUploadDialogClose"
    >
      <FileUpload
        v-model="uploadFileList"
        :multiple="true"
        :accept="'.jpg,.jpeg,.png,.gif,.pdf,.doc,.docx,.xls,.xlsx,.txt'"
        :max-size="50 * 1024 * 1024"
        :description="'选择要上传的文件'"
        @upload-start="handleUploadStart"
        @upload-success="handleUploadSuccess"
        @upload-error="handleUploadError"
      />

      <template #footer>
        <div class="upload-footer">
          <el-button @click="uploadDialogVisible = false">关闭</el-button>
          <span class="upload-tip-text">文件上传成功后将自动关闭</span>
        </div>
      </template>
    </el-dialog>

    <!-- 文件预览全屏抽屉 -->
    <el-drawer
      v-model="previewDrawerVisible"
      :title="getPreviewTitle()"
      size="100%"
      direction="rtl"
      @close="handlePreviewDrawerClose"
      class="file-preview-drawer"
    >
      <template #header>
        <div class="preview-header">
          <div class="preview-title">
            <el-icon class="preview-icon">
              <component :is="getFileIconByName(currentPreviewFile?.originalFileName || currentPreviewFile?.fileName)" />
            </el-icon>
            <span>{{ currentPreviewFile?.originalFileName || currentPreviewFile?.fileName }}</span>
          </div>
          <div class="preview-actions">
            <el-button @click="downloadFile(currentPreviewFile)" size="small">
              <el-icon><Download /></el-icon>
              下载
            </el-button>
            <el-button @click="handlePreviewDrawerClose" size="small">
              <el-icon><Close /></el-icon>
              关闭
            </el-button>
          </div>
        </div>
      </template>

      <div v-if="currentPreviewFile" class="preview-content">

        <!-- 图片预览 -->
        <div v-if="isImageFile(currentPreviewFile)" class="image-preview">
          <div v-if="previewLoading" class="loading-container">
            <el-icon class="loading-icon is-loading"><Loading /></el-icon>
            <p class="loading-text">正在加载图片...</p>
          </div>
          <img
            v-else
            :src="getFilePreviewUrl(currentPreviewFile)"
            :alt="currentPreviewFile.originalFileName || currentPreviewFile.fileName"
            class="preview-image"
            @error="handleImageError"
            @load="handleImageLoad"
          />
        </div>

        <!-- PDF 预览 -->
        <div v-else-if="isPdfFile(currentPreviewFile)" class="pdf-preview">
          <div v-if="previewLoading" class="loading-container">
            <el-icon class="loading-icon is-loading"><Loading /></el-icon>
            <p class="loading-text">正在加载 PDF 文件...</p>
          </div>
          <PdfViewer
            v-else-if="pdfPreviewUrl"
            :file-url="pdfPreviewUrl"
            mode="preview"
            class="pdf-viewer-container"
          />
          <div v-else class="empty-container">
            <el-icon class="empty-icon"><Document /></el-icon>
            <p class="empty-text">PDF 文件加载失败</p>
          </div>
        </div>

        <!-- Office 文档预览 (Word, Excel, PowerPoint) -->
        <div v-else-if="isOfficeFile(currentPreviewFile)" class="office-preview">
          <div class="office-info-container">
            <div class="office-icon">
              <el-icon class="office-icon-large"><Document /></el-icon>
            </div>
            <div class="office-details">
              <h3>{{ currentPreviewFile.originalFileName || currentPreviewFile.fileName }}</h3>
              <p class="office-type">{{ getFileTypeByName(currentPreviewFile?.originalFileName || currentPreviewFile?.fileName) }} 文档</p>
              <p class="office-size">文件大小: {{ formatFileSize(currentPreviewFile.fileSize) }}</p>
              <div class="office-actions">
                <el-button type="primary" @click="downloadFile(currentPreviewFile)">
                  <el-icon><Download /></el-icon>
                  下载文件
                </el-button>
              </div>
              <div class="office-note">
                <p><el-icon><InfoFilled /></el-icon> Office 文档在线预览功能需要配置 OnlyOffice 服务</p>
                <p>您可以下载文件后使用本地软件打开</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 文本文件预览 -->
        <div v-else-if="isTextFile(currentPreviewFile)" class="text-preview">
          <div v-if="previewLoading" class="loading-container">
            <el-icon class="loading-icon is-loading"><Loading /></el-icon>
            <p class="loading-text">正在加载文本内容...</p>
          </div>
          <pre v-else-if="textContent" class="text-content">{{ textContent }}</pre>
          <div v-else class="empty-container">
            <el-icon class="empty-icon"><Document /></el-icon>
            <p class="empty-text">文本文件加载失败</p>
          </div>
        </div>

        <!-- 视频预览 -->
        <div v-else-if="isVideoFile(currentPreviewFile)" class="video-preview">
          <video
            :src="getFilePreviewUrl(currentPreviewFile)"
            controls
            class="preview-video"
            @error="handleVideoError"
          >
            您的浏览器不支持视频播放
          </video>
        </div>

        <!-- 音频预览 -->
        <div v-else-if="isAudioFile(currentPreviewFile)" class="audio-preview">
          <div class="audio-container">
            <div class="audio-info">
              <el-icon class="audio-icon"><Headset /></el-icon>
              <div class="audio-details">
                <h3>{{ currentPreviewFile.originalFileName || currentPreviewFile.fileName }}</h3>
                <p>文件大小: {{ formatFileSize(currentPreviewFile.fileSize) }}</p>
              </div>
            </div>
            <audio
              :src="getFilePreviewUrl(currentPreviewFile)"
              controls
              class="preview-audio"
              @error="handleAudioError"
            >
              您的浏览器不支持音频播放
            </audio>
          </div>
        </div>

        <!-- 其他文件类型 -->
        <div v-else class="file-info-preview">
          <div class="file-info-container">
            <div class="file-icon-large">
              <el-icon><component :is="getFileIconByName(currentPreviewFile?.originalFileName || currentPreviewFile?.fileName)" /></el-icon>
            </div>
            <div class="file-details">
              <h3>{{ currentPreviewFile.originalFileName || currentPreviewFile.fileName }}</h3>
              <div class="file-meta-grid">
                <div class="meta-item">
                  <span class="meta-label">文件大小:</span>
                  <span class="meta-value">{{ formatFileSize(currentPreviewFile.fileSize) }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">文件类型:</span>
                  <span class="meta-value">{{ getFileTypeByName(currentPreviewFile?.originalFileName || currentPreviewFile?.fileName) }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">文件扩展名:</span>
                  <span class="meta-value">{{ getFileExtension(currentPreviewFile?.originalFileName || currentPreviewFile?.fileName)?.toUpperCase() || '无' }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">上传时间:</span>
                  <span class="meta-value">{{ formatDate(currentPreviewFile.createTime) }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">上传用户:</span>
                  <span class="meta-value">{{ currentPreviewFile.uploadUsername || '未知' }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">存储类型:</span>
                  <span class="meta-value">{{ currentPreviewFile.storageType || '本地' }}</span>
                </div>
              </div>
              <div class="file-actions">
                <el-button type="primary" @click="downloadFile(currentPreviewFile)">
                  <el-icon><Download /></el-icon>
                  下载文件
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>

    <!-- 全屏编辑抽屉 -->
    <el-drawer
      v-model="editDrawerVisible"
      title="在线编辑"
      :size="'100%'"
      direction="rtl"
      :before-close="handleEditDrawerClose"
      class="edit-drawer"
    >
      <template #header>
        <div class="drawer-header">
          <span class="drawer-title">在线编辑 - {{ currentEditFile?.originalFileName }}</span>
          <div style="display:flex; gap:8px; align-items:center;">
            <el-button type="primary" @click="onEditorSaveClick" :disabled="!currentEditFile">
              <el-icon><Edit /></el-icon>
              保存
            </el-button>
<!--            <el-button type="primary" @click="handleEditDrawerClose">-->
<!--              <el-icon><Close /></el-icon>-->
<!--              关闭-->
<!--            </el-button>-->
          </div>
        </div>
      </template>
      <div class="edit-container">
        <OnlyEditor
          v-if="currentEditFile"
          ref="onlyEditorRef"
          :documentId="currentEditFile.id"
          :showToolbar="false"
          @saveStart="onChildSaveStart"
          @saveTriggered="onChildSaveTriggered"
          @saveError="onChildSaveError"
          @saveResult="onChildSaveResult"
        />
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Upload, Refresh, Search, List, Grid, View, Download, Delete, Picture, Document, VideoPlay, Headset, Files, Edit, Close, Loading, InfoFilled, FolderOpened, Folder, Reading, Notebook, DataBoard, Monitor } from '@element-plus/icons-vue'
import FileUpload from '@/components/FileUpload/index.vue'
import PdfViewer from '@/components/PdfViewer.vue'
import { fileApi } from '@/api'
import OnlyEditor from './onlyeditor.vue'
import { getFileDownloadUrl } from '@/config'

// 响应式数据
const loading = ref(false)
const searchQuery = ref('')
const fileTypeFilter = ref('')
const statusFilter = ref('')
const viewMode = ref('grid')
const currentPage = ref(1)
const pageSize = ref(10)
const totalFiles = ref(0)
const uploadDialogVisible = ref(false)
const previewDrawerVisible = ref(false)
const editDrawerVisible = ref(false)
const uploadFileList = ref([])
const currentPreviewFile = ref(null)
const currentEditFile = ref(null)
const onlyEditorRef = ref(null)
const savingFromHeader = ref(false)
const previewLoading = ref(false)

// 预览相关状态
const pdfPreviewUrl = ref('')
const officePreviewUrl = ref('')
const textContent = ref('')

// 文件列表数据
const fileList = ref([])

// 计算属性
const filteredFiles = computed(() => {
  let filtered = fileList.value

  if (searchQuery.value) {
    filtered = filtered.filter(file => {
      const searchTerm = searchQuery.value.toLowerCase()
      const fileName = (file.originalFileName || file.fileName || '').toLowerCase()
      return fileName.includes(searchTerm)
    })
  }

  if (fileTypeFilter.value) {
    filtered = filtered.filter(file => {
      switch (fileTypeFilter.value) {
        case 'image':
          return isImageFile(file)
        case 'pdf':
          return isPdfFile(file)
        case 'office':
          return isOfficeFile(file)
        case 'text':
          return isTextFile(file)
        case 'video':
          return isVideoFile(file)
        case 'audio':
          return isAudioFile(file)
        case 'other':
          return !isImageFile(file) && !isPdfFile(file) && !isOfficeFile(file) &&
                 !isTextFile(file) && !isVideoFile(file) && !isAudioFile(file)
        default:
          return true
      }
    })
  }

  if (statusFilter.value !== '') {
    filtered = filtered.filter(file => file.status === parseInt(statusFilter.value))
  }

  return filtered
})

// 方法
const loadFileList = async () => {
  loading.value = true
  try {

    // 原始API调用代码
    const response = await fileApi.getFileList({
      current: currentPage.value,
      size: pageSize.value,
      fileName: searchQuery.value || undefined,
      fileType: fileTypeFilter.value || undefined,
      storageType: undefined,
      uploadUserId: undefined
    })

    if (response.code === 200) {
      fileList.value = response.data.records || response.data.list || []
      totalFiles.value = Number(response.data.total) || 0
    } else {
      ElMessage.error(response.message || '加载文件列表失败')
    }
  } catch (error) {
    console.error('加载文件列表失败:', error)
    ElMessage.error('加载文件列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadFileList()
}

const handleRefresh = () => {
  loadFileList()
}

const handleUpload = () => {
  uploadDialogVisible.value = true
}

// 用于跟踪上传状态
const uploadCount = ref(0)
const totalUploadCount = ref(0)

const handleUploadStart = (fileCount) => {
  // 设置总文件数量
  totalUploadCount.value = fileCount
  uploadCount.value = 0
}

const handleUploadSuccess = (file) => {
  // 不再显示重复的成功消息，FileUpload组件已经显示过了
  uploadCount.value++

  // 刷新文件列表
  loadFileList()

  // 如果所有文件都上传完成，延迟关闭弹窗
  if (uploadCount.value >= totalUploadCount.value) {
    setTimeout(() => {
      uploadDialogVisible.value = false
      uploadFileList.value = []
      // 重置计数器
      uploadCount.value = 0
      totalUploadCount.value = 0
    }, 1500) // 增加延迟时间，让用户看到最终状态
  }
}

const handleUploadError = (error) => {
  uploadCount.value++
  ElMessage.error(`上传失败: ${error.message}`)

  // 检查是否所有文件都处理完成（包括失败的）
  if (uploadCount.value >= totalUploadCount.value) {
    setTimeout(() => {
      uploadDialogVisible.value = false
      uploadFileList.value = []
      // 重置计数器
      uploadCount.value = 0
      totalUploadCount.value = 0
    }, 2000) // 错误情况下延迟更长时间
  }
}

const confirmUpload = () => {
  // 现在这个方法主要用于手动关闭，大多数情况下会自动关闭
  uploadDialogVisible.value = false
  uploadFileList.value = []
  // 重置计数器
  uploadCount.value = 0
  totalUploadCount.value = 0
}

const handleUploadDialogClose = () => {
  uploadFileList.value = []
}

const previewFile = async (file) => {
  // 如果是 Office 文档，直接打开编辑器
  if (isOfficeFile(file)) {
    editFile(file)
    return
  }

  currentPreviewFile.value = file
  previewLoading.value = true
  previewDrawerVisible.value = true

  // 清空之前的预览数据
  pdfPreviewUrl.value = ''
  officePreviewUrl.value = ''
  textContent.value = ''

  try {
    if (isPdfFile(file)) {
      // PDF 文件预览
      pdfPreviewUrl.value = `/zapi-allin-sys/file/download/${file.id}`
      previewLoading.value = false
    } else if (isTextFile(file)) {
      // 文本文件预览
      await loadTextContent(file)
    } else if (isImageFile(file) || isVideoFile(file) || isAudioFile(file)) {
      // 图片、视频、音频直接使用 URL
      previewLoading.value = false
    } else {
      // 其他文件类型
      previewLoading.value = false
    }
  } catch (error) {
    console.error('预览文件失败:', error)
    ElMessage.error('文件预览失败')
    previewLoading.value = false
  }
}

const handlePreviewDrawerClose = () => {
  currentPreviewFile.value = null
  previewLoading.value = false
  pdfPreviewUrl.value = ''
  officePreviewUrl.value = ''
  textContent.value = ''
}

// 处理图片加载完成
const handleImageLoad = () => {
  previewLoading.value = false
}

// 获取预览标题
const getPreviewTitle = () => {
  if (!currentPreviewFile.value) return '文件预览'
  return `${currentPreviewFile.value.originalFileName || currentPreviewFile.value.fileName} - 预览`
}

const downloadFile = async (file) => {
  try {
    ElMessage.info('正在下载文件...')

    const response = await fileApi.downloadFile(file.id)

    // 检查响应状态
    if (response.status === 200) {
      const blob = response.data

      // 验证blob数据
      if (!blob || blob.size === 0) {
        ElMessage.error('文件内容为空')
        return
      }

      // 获取文件名，优先使用originalFileName
      const fileName = file.originalFileName || file.fileName || `download_${file.id}`

      // 创建下载链接
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = fileName
      link.style.display = 'none'

      // 添加到DOM，触发下载，然后清理
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      window.URL.revokeObjectURL(url)

      ElMessage.success(`文件下载成功: ${fileName}`)
    } else {
      ElMessage.error('文件下载失败：服务器响应异常')
    }
  } catch (error) {
    console.error('下载文件失败:', error)

    // 更详细的错误信息
    if (error.response) {
      const status = error.response.status
      if (status === 404) {
        ElMessage.error('文件不存在或已被删除')
      } else if (status === 403) {
        ElMessage.error('没有权限下载该文件')
      } else {
        ElMessage.error(`下载失败: HTTP ${status}`)
      }
    } else if (error.request) {
      ElMessage.error('网络连接失败，请检查网络')
    } else {
      ElMessage.error(`下载失败: ${error.message || '未知错误'}`)
    }
  }
}

const editFile = (file) => {
  currentEditFile.value = file
  editDrawerVisible.value = true
}

const handleEditDrawerClose = () => {
  editDrawerVisible.value = false
  currentEditFile.value = null
}

const handleEditorReady = () => {
  ElMessage.success('编辑器加载完成')
}

const handleEditorError = (error) => {
  ElMessage.error('编辑器加载失败: ' + (error.message || '未知错误'))
}

const handleEditorSave = () => {
  ElMessage.success('文档保存成功')
  // 可以在这里刷新文件列表
  loadFileList()
}

const deleteFile = async (file) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除文件"${file.fileName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await fileApi.deleteFile(file.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadFileList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除文件失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadFileList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadFileList()
}

// 工具方法
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB', 'TB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const formatDate = (dateString) => {
  if (!dateString) return '未知时间'

  try {
    const date = new Date(dateString)
    if (isNaN(date.getTime())) {
      return '无效时间'
    }
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  } catch (error) {
    console.error('日期格式化错误:', error, dateString)
    return '时间格式错误'
  }
}

const isImage = (fileType) => {
  const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'ico']
  return imageTypes.includes(fileType?.toLowerCase())
}

const isImageFile = (file) => {
  if (!file) return false
  const extension = getExtension(file)
  const imageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'ico', 'tiff', 'tif']
  return imageTypes.includes(extension)
}

// 统一的扩展名获取函数
const getExtension = (file) => {
  if (!file) return ''

  let extension = file.fileExtension?.toLowerCase()

  // 如果 fileExtension 带点号，去掉点号
  if (extension && extension.startsWith('.')) {
    extension = extension.substring(1)
  }

  // 如果没有 fileExtension，从文件名中提取
  if (!extension) {
    extension = file.originalFileName?.split('.').pop()?.toLowerCase() ||
                file.fileName?.split('.').pop()?.toLowerCase()
  }

  return extension || ''
}

const isPdfFile = (file) => {
  if (!file) return false
  const extension = getExtension(file)
  return extension === 'pdf'
}

const isOfficeFile = (file) => {
  if (!file) return false
  const extension = getExtension(file)
  const officeTypes = ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx', 'odt', 'ods', 'odp']
  return officeTypes.includes(extension)
}

const isTextFile = (file) => {
  if (!file) return false
  const extension = getExtension(file)
  const textTypes = ['txt', 'md', 'json', 'xml', 'html', 'css', 'js', 'ts', 'vue', 'java', 'py', 'php', 'cpp', 'c', 'h', 'sql', 'log', 'ini', 'conf', 'yaml', 'yml']
  return textTypes.includes(extension)
}

const isVideoFile = (file) => {
  if (!file) return false
  const extension = getExtension(file)
  const videoTypes = ['mp4', 'avi', 'mov', 'wmv', 'flv', 'webm', 'mkv', '3gp', 'rm', 'rmvb']
  return videoTypes.includes(extension)
}

const isAudioFile = (file) => {
  if (!file) return false
  const extension = getExtension(file)
  const audioTypes = ['mp3', 'wav', 'flac', 'aac', 'ogg', 'wma', 'm4a', 'ape']
  return audioTypes.includes(extension)
}

// 获取 OnlyOffice 预览 URL
const getOnlyOfficePreviewUrl = (file) => {
  // 这里需要根据你的 OnlyOffice 配置来生成预览 URL
  // 示例：返回编辑器 URL，但设置为只读模式
  const baseUrl = window.location.origin
  return `${baseUrl}/onlyoffice/editor?fileId=${file.id}&mode=view`
}

// 加载文本文件内容
const loadTextContent = async (file) => {
  try {
    const response = await fileApi.downloadFile(file.id)
    if (response.status === 200) {
      const blob = response.data
      const text = await blob.text()
      textContent.value = text
      previewLoading.value = false
    } else {
      throw new Error('文件下载失败')
    }
  } catch (error) {
    console.error('加载文本内容失败:', error)
    ElMessage.error('文本文件加载失败')
    previewLoading.value = false
  }
}

// 处理各种预览错误
const handleVideoError = () => {
  ElMessage.error('视频文件加载失败')
}

const handleAudioError = () => {
  ElMessage.error('音频文件加载失败')
}

const handleOfficeLoad = () => {
  previewLoading.value = false
}

const handleOfficeError = () => {
  ElMessage.error('Office 文档预览失败')
  previewLoading.value = false
}

const isEditableFile = (fileType) => {
  const type = fileType?.toLowerCase()
  return ['doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx'].includes(type)
}

const getDocumentType = (fileType) => {
  const type = fileType?.toLowerCase()
  if (['doc', 'docx'].includes(type)) return 'word'
  if (['xls', 'xlsx'].includes(type)) return 'cell'
  if (['ppt', 'pptx'].includes(type)) return 'slide'
  return 'word'
}

// 从文件名获取扩展名
const getFileExtension = (fileName) => {
  if (!fileName) return ''
  return fileName.split('.').pop()?.toLowerCase() || ''
}

// 根据文件名获取文件类型显示名称
const getFileTypeByName = (fileName) => {
  const ext = getFileExtension(fileName)
  if (!ext) return '未知'

  // 图片类型
  if (['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'ico', 'tiff', 'tif'].includes(ext)) {
    return ext.toUpperCase()
  }

  // 文档类型
  if (['pdf'].includes(ext)) return 'PDF'
  if (['doc', 'docx'].includes(ext)) return 'Word'
  if (['xls', 'xlsx'].includes(ext)) return 'Excel'
  if (['ppt', 'pptx'].includes(ext)) return 'PPT'
  if (['txt', 'md'].includes(ext)) return '文本'

  // 代码文件
  if (['js', 'ts', 'vue', 'html', 'css', 'json', 'xml'].includes(ext)) return '代码'
  if (['java', 'py', 'php', 'cpp', 'c', 'h'].includes(ext)) return '代码'

  // 媒体文件
  if (['mp4', 'avi', 'mov', 'wmv', 'flv', 'webm', 'mkv'].includes(ext)) return '视频'
  if (['mp3', 'wav', 'flac', 'aac', 'ogg', 'wma'].includes(ext)) return '音频'

  // 压缩文件
  if (['zip', 'rar', '7z', 'tar', 'gz'].includes(ext)) return '压缩包'

  return ext.toUpperCase()
}

// 根据文件名获取类型标签颜色
const getFileTypeTagByName = (fileName) => {
  const ext = getFileExtension(fileName)

  // 图片 - 绿色
  if (['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'ico', 'tiff', 'tif'].includes(ext)) {
    return 'success'
  }

  // 文档 - 蓝色
  if (['pdf', 'doc', 'docx', 'xls', 'xlsx', 'ppt', 'pptx'].includes(ext)) {
    return 'primary'
  }

  // 代码 - 紫色
  if (['js', 'ts', 'vue', 'html', 'css', 'json', 'xml', 'java', 'py', 'php', 'cpp', 'c', 'h', 'txt', 'md'].includes(ext)) {
    return 'info'
  }

  // 媒体 - 橙色
  if (['mp4', 'avi', 'mov', 'wmv', 'flv', 'webm', 'mkv', 'mp3', 'wav', 'flac', 'aac', 'ogg', 'wma'].includes(ext)) {
    return 'warning'
  }

  // 压缩包 - 红色
  if (['zip', 'rar', '7z', 'tar', 'gz'].includes(ext)) {
    return 'danger'
  }

  return 'default'
}

// 根据文件名获取图标
const getFileIconByName = (fileName) => {
  const ext = getFileExtension(fileName)

  // 图片文件
  if (['jpg', 'jpeg', 'png', 'gif', 'bmp', 'webp', 'svg', 'ico', 'tiff', 'tif'].includes(ext)) {
    return Picture
  }

  // PDF文档
  if (ext === 'pdf') {
    return Reading
  }

  // Word文档
  if (['doc', 'docx'].includes(ext)) {
    return Document
  }

  // Excel表格
  if (['xls', 'xlsx'].includes(ext)) {
    return DataBoard
  }

  // PowerPoint演示文稿
  if (['ppt', 'pptx'].includes(ext)) {
    return Monitor
  }

  // 文本文件
  if (['txt', 'md', 'readme'].includes(ext)) {
    return Notebook
  }

  // 代码文件
  if (['js', 'ts', 'vue', 'html', 'css', 'json', 'xml', 'java', 'py', 'php', 'cpp', 'c', 'h', 'sql'].includes(ext)) {
    return Files
  }

  // 压缩文件
  if (['zip', 'rar', '7z', 'tar', 'gz'].includes(ext)) {
    return FolderOpened
  }

  // 视频文件
  if (['mp4', 'avi', 'mov', 'wmv', 'flv', 'webm', 'mkv', '3gp', 'rm', 'rmvb'].includes(ext)) {
    return VideoPlay
  }

  // 音频文件
  if (['mp3', 'wav', 'flac', 'aac', 'ogg', 'wma', 'm4a', 'ape'].includes(ext)) {
    return Headset
  }

  // 默认文档图标
  return Document
}

// 保留原有函数以兼容旧代码
const getFileIcon = (fileType) => {
  const type = fileType?.toLowerCase()
  if (isImage(type)) return Picture
  if (['pdf', 'doc', 'docx', 'txt'].includes(type)) return Document
  if (['mp4', 'avi', 'mov'].includes(type)) return VideoPlay
  if (['mp3', 'wav', 'flac'].includes(type)) return Headset
  return Files
}

const getFileTypeTag = (fileType) => {
  const type = fileType?.toLowerCase()
  if (isImage(type)) return 'success'
  if (['pdf', 'doc', 'docx', 'txt'].includes(type)) return 'primary'
  if (['mp4', 'avi', 'mov'].includes(type)) return 'warning'
  if (['mp3', 'wav', 'flac'].includes(type)) return 'info'
  return 'default'
}

// 获取文件预览URL
const getFilePreviewUrl = (file) => {
  if (!file || !file.id) return ''

  // 使用下载接口作为预览URL
  return getFileDownloadUrl(file.id)
}

// 处理图片加载错误
const handleImageError = (event) => {
  console.error('图片加载失败:', event)
  // 替换为默认图标
  const img = event.target
  img.style.display = 'none'

  // 创建错误提示元素
  const errorDiv = document.createElement('div')
  errorDiv.className = 'image-error'
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
      <span>图片加载失败</span>
      <span style="font-size: 12px; margin-top: 4px;">文件可能已损坏或不存在</span>
    </div>
  `

  // 替换图片元素
  img.parentNode.replaceChild(errorDiv, img)
}

const onEditorSaveClick = async () => {
  try {
    savingFromHeader.value = true
    await onlyEditorRef.value?.triggerSave?.()
  } catch (e) {
    console.error(e)
  }
}

const onChildSaveStart = () => {
  savingFromHeader.value = true
  ElMessage.info('正在触发保存...')
}

const onChildSaveTriggered = () => {
  ElMessage.success('已发起保存，请稍候')
}

const onChildSaveError = (e) => {
  savingFromHeader.value = false
  ElMessage.error('触发保存失败')
}

const onChildSaveResult = (code) => {
  savingFromHeader.value = false
  if (code === 0) {
    ElMessage.success('文档保存成功')
    // 刷新文件列表以更新修改时间等信息
    loadFileList()
  } else {
    ElMessage.warning('文档保存结果未知或失败')
  }
}

// 生命周期
onMounted(() => {
  loadFileList()
})
</script>

<style lang="scss" scoped>
.file-page {
  animation: fadeIn 0.6s ease-out;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.search-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
}

.search-left {
  display: flex;
  gap: 16px;
}

.file-content {
  margin-bottom: 24px;
}

.table-view {
  background: transparent;
  border: none;
  border-radius: 0;
  overflow: visible;
  box-shadow: none;
}

.file-table {
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

.file-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;

  .file-icon {
    color: var(--text-secondary);
  }

  .file-name {
    font-weight: 500;
    color: var(--text-primary);
  }
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  padding: 6px 4px;
}

:deep(.action-buttons .el-button) {
  padding: 6px 10px;
}

:deep(.btn-danger-text.is-text) {
  color: var(--el-color-danger);
}


.grid-view {
  .file-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
    gap: 20px;
  }
}

.file-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-lg);
    border-color: var(--color-primary);
  }

  .file-preview {
    height: 160px;
    border-radius: 8px;
    overflow: hidden;
    margin-bottom: 12px;

    .file-image {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .file-placeholder {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      background: var(--bg-tertiary);

      .file-icon-large {
        font-size: 48px;
        color: var(--text-secondary);
      }
    }
  }

  .file-info {
    margin-bottom: 12px;

    .file-name {
      font-weight: 500;
      color: var(--text-primary);
      margin-bottom: 8px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }

    .file-meta {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .file-size {
        color: var(--text-secondary);
        font-size: 12px;
      }
    }
  }

  .file-actions {
    display: flex;
    justify-content: flex-end;
    gap: 8px;
  }
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
}

.file-preview-content {
  text-align: center;

  .image-preview-container {
    position: relative;
    display: inline-block;
  }

  img {
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    max-height: 60vh;
    object-fit: contain;
  }

  .image-loading {
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

  .file-info-preview {
    text-align: left;
    background: var(--bg-tertiary);
    padding: 20px;
    border-radius: 8px;
    margin-top: 16px;

    h3 {
      margin-bottom: 16px;
      color: var(--text-primary);
      font-size: 18px;
      font-weight: 600;
    }

    p {
      margin-bottom: 12px;
      color: var(--text-secondary);
      line-height: 1.6;

      strong {
        color: var(--text-primary);
        font-weight: 500;
      }
    }

    .preview-actions {
      margin-top: 20px;
      padding-top: 16px;
      border-top: 1px solid var(--border-color);
      text-align: center;
    }
  }
}

// 编辑抽屉样式
  :deep(.el-drawer__header){
    margin-bottom: 0 !important;
  }
.edit-drawer {
  :deep(.el-drawer__header) {
    margin-bottom: 0px !important;
    padding: 16px 24px;
    border-bottom: 1px solid var(--border-color);
  }

  :deep(.el-drawer__body) {
    padding: 0;
    height: calc(100vh - 60px);
  }
}

.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0px;
  width: 100%;
  .drawer-title {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.edit-container {
  width: 100%;
  height: 100%;
  background: #f5f5f5;
}

// 上传对话框底部样式
.upload-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;

  .upload-tip-text {
    color: var(--text-secondary);
    font-size: 14px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }

  .search-section {
    flex-direction: column;
    gap: 16px;
  }

  .search-left {
    width: 100%;
    flex-direction: column;
  }

  .file-grid {
    grid-template-columns: 1fr;
  }

  .upload-footer {
    flex-direction: column;
    gap: 12px;

    .upload-tip-text {
      font-size: 12px;
    }
  }
}

// 文件预览抽屉样式
.file-preview-drawer {
  :deep(.el-drawer__header) {
    padding: 0;
    margin-bottom: 0;
  }

  :deep(.el-drawer__body) {
    padding: 0;
    height: 100%;
    overflow: hidden;
  }

  .preview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 24px;
    border-bottom: 1px solid var(--border-color);
    background: var(--bg-card);

    .preview-title {
      display: flex;
      align-items: center;
      gap: 12px;
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);

      .preview-icon {
        font-size: 20px;
        color: var(--color-primary);
      }
    }

    .preview-actions {
      display: flex;
      gap: 8px;
    }
  }

  .preview-content {
    height: calc(100vh - 80px);
    overflow: hidden;
    position: relative;
  }

  // 图片预览样式
  .image-preview {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f5f5;

    .preview-image {
      max-width: 100%;
      max-height: 100%;
      object-fit: contain;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }
  }

  // PDF 预览样式
  .pdf-preview {
    height: 100%;

    .pdf-viewer-container {
      height: 100%;
    }
  }

  // Office 文档预览样式
  .office-preview {
    height: 100%;

    .office-iframe {
      width: 100%;
      height: 100%;
      border: none;
    }
  }

  // 文本预览样式
  .text-preview {
    height: 100%;
    overflow: auto;
    padding: 20px;
    background: #fafafa;

    .text-content {
      font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
      font-size: 14px;
      line-height: 1.6;
      color: var(--text-primary);
      background: white;
      padding: 20px;
      border-radius: 8px;
      border: 1px solid var(--border-color);
      white-space: pre-wrap;
      word-wrap: break-word;
    }
  }

  // 视频预览样式
  .video-preview {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #000;

    .preview-video {
      max-width: 100%;
      max-height: 100%;
      border-radius: 8px;
    }
  }

  // 音频预览样式
  .audio-preview {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--bg-secondary);

    .audio-container {
      text-align: center;
      padding: 40px;
      background: white;
      border-radius: 16px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
      max-width: 500px;
      width: 100%;

      .audio-info {
        display: flex;
        align-items: center;
        gap: 16px;
        margin-bottom: 24px;

        .audio-icon {
          font-size: 48px;
          color: var(--color-primary);
        }

        .audio-details {
          text-align: left;
          flex: 1;

          h3 {
            margin: 0 0 8px 0;
            font-size: 18px;
            color: var(--text-primary);
          }

          p {
            margin: 0;
            color: var(--text-secondary);
            font-size: 14px;
          }
        }
      }

      .preview-audio {
        width: 100%;
        border-radius: 8px;
      }
    }
  }

  // 文件信息预览样式
  .file-info-preview {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 40px;
    background: var(--bg-secondary);

    .file-info-container {
      background: white;
      border-radius: 16px;
      padding: 40px;
      box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
      max-width: 600px;
      width: 100%;
      text-align: center;

      .file-icon-large {
        font-size: 64px;
        color: var(--color-primary);
        margin-bottom: 24px;
      }

      .file-details {
        h3 {
          margin: 0 0 24px 0;
          font-size: 20px;
          color: var(--text-primary);
          word-break: break-all;
        }

        .file-meta-grid {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 16px;
          margin-bottom: 32px;
          text-align: left;

          .meta-item {
            display: flex;
            flex-direction: column;
            gap: 4px;

            .meta-label {
              font-size: 12px;
              color: var(--text-secondary);
              font-weight: 500;
            }

            .meta-value {
              font-size: 14px;
              color: var(--text-primary);
              word-break: break-all;
            }
          }
        }

        .file-actions {
          display: flex;
          gap: 12px;
          justify-content: center;
        }
      }
    }
  }

  // 加载和空状态样式
  .loading-container,
  .empty-container {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    color: var(--text-secondary);

    .loading-icon,
    .empty-icon {
      font-size: 48px;
      margin-bottom: 16px;
    }

    .loading-icon {
      color: var(--color-primary);
    }

    .empty-icon {
      color: var(--text-placeholder);
    }

    .loading-text,
    .empty-text {
      font-size: 16px;
      margin: 0;
    }

    .empty-subtitle {
      font-size: 14px;
      margin: 8px 0 0 0;
      color: var(--text-placeholder);
    }
  }
}

// Office 文档预览样式
.office-preview {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  padding: 40px;

  .office-info-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    max-width: 500px;

    .office-icon {
      margin-bottom: 24px;

      .office-icon-large {
        font-size: 80px;
        color: var(--primary-color);
      }
    }

    .office-details {
      h3 {
        font-size: 20px;
        font-weight: 600;
        color: var(--text-primary);
        margin: 0 0 12px 0;
        word-break: break-all;
      }

      .office-type {
        font-size: 16px;
        color: var(--primary-color);
        font-weight: 500;
        margin: 0 0 8px 0;
      }

      .office-size {
        font-size: 14px;
        color: var(--text-secondary);
        margin: 0 0 24px 0;
      }

      .office-actions {
        display: flex;
        gap: 12px;
        justify-content: center;
        margin-bottom: 24px;

        .el-button {
          padding: 12px 24px;
        }
      }

      .office-note {
        background: var(--bg-secondary);
        border: 1px solid var(--border-light);
        border-radius: 8px;
        padding: 16px;

        p {
          margin: 0 0 8px 0;
          font-size: 14px;
          color: var(--text-secondary);
          display: flex;
          align-items: center;
          gap: 8px;

          &:last-child {
            margin-bottom: 0;
          }

          .el-icon {
            color: var(--warning-color);
            flex-shrink: 0;
          }
        }
      }
    }
  }
}
</style>
