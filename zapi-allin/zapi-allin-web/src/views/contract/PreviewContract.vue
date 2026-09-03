<template>
  <div class="preview-container">
    <!-- 头部工具栏 -->
    <div class="toolbar-header">
      <div class="toolbar-left">
        <el-button @click="handleBack" size="small" plain>
          <el-icon><Back /></el-icon>
          返回
        </el-button>
        <el-divider direction="vertical" />
        <span class="title">合同预览</span>
      </div>
      <div class="toolbar-right">
        <el-button @click="downloadFile" size="small" type="primary">
          <el-icon><Download /></el-icon>
          下载文件
        </el-button>
        <el-button @click="printFile" size="small">
          <el-icon><Printer /></el-icon>
          打印
        </el-button>
      </div>
    </div>

    <!-- PDF 预览组件 -->
    <div class="pdf-viewer-container">
      <PdfViewer
        :file-url="fileUrl"
        mode="preview"
      >
        <template #toolbar-actions>
          <el-button @click="downloadFile" size="small" type="primary">
            <el-icon><Download /></el-icon>
            下载
          </el-button>
          <el-button @click="printFile" size="small">
            <el-icon><Printer /></el-icon>
            打印
          </el-button>
        </template>
      </PdfViewer>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Back, Download, Printer } from '@element-plus/icons-vue'
import PdfViewer from '@/components/PdfViewer.vue'

const route = useRoute()
const router = useRouter()

// 页面参数
const fileId = ref(route.query.fileId)
const fileName = ref(route.query.fileName || '合同文件')
const fileUrl = ref('')

// 下载文件
const downloadFile = () => {
  if (fileUrl.value) {
    window.open(fileUrl.value)
  } else {
    ElMessage.warning('文件URL不存在')
  }
}

// 打印文件
const printFile = () => {
  if (fileUrl.value) {
    const printWindow = window.open(fileUrl.value)
    printWindow.onload = () => {
      printWindow.print()
    }
  } else {
    ElMessage.warning('文件URL不存在')
  }
}

// 返回
const handleBack = () => {
  router.go(-1)
}

// 页面初始化
onMounted(() => {

  if (!fileId.value) {
    ElMessage.warning('缺少文件ID')
    return
  }

  // 构建文件URL
  fileUrl.value = `/zapi-allin-sys/file/download/${fileId.value}`
})
</script>

<style scoped>
.preview-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #fff;
}

.toolbar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px;
  background: linear-gradient(to bottom, #ffffff, #f8f9fa);
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.pdf-viewer-container {
  flex: 1;
  overflow: hidden;
}
</style>
