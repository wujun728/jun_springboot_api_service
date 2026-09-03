<template>
  <div class="sign-container">
    <!-- 头部工具栏 -->
    <div class="toolbar-header">
      <div class="toolbar-left">
        <el-button @click="handleBack" size="small" plain>
          <el-icon><Back /></el-icon>
          返回
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-button v-if="hasStamped" @click="downloadStampedFile" size="small" type="success">
          <el-icon><Download /></el-icon>
          下载盖章后的文件
        </el-button>
      </div>
    </div>

    <!-- PDF 预览组件 -->
    <div class="pdf-viewer-container">
      <PdfViewer
        :file-url="fileUrl"
        mode="stamp"
        :contract-id="contractId"
        :file-id="fileId"
        @stamp-success="handleStampSuccess"
        @stamp-error="handleStampError"
      >
        <template #toolbar-actions>
          <el-button v-if="hasStamped" @click="downloadStampedFile" size="small" type="success">
            <el-icon><Download /></el-icon>
            下载盖章文件
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
import { Back, Download } from '@element-plus/icons-vue'
import PdfViewer from '@/components/PdfViewer.vue'
import { contractApi } from '@/api/contract'

const route = useRoute()
const router = useRouter()

// 页面参数
const contractId = ref(route.query.contractId)
const fileId = ref(route.query.fileId)
const fileUrl = ref('')

// 盖章状态
const hasStamped = ref(false)
const stampedFileId = ref(null)

// 处理盖章成功
const handleStampSuccess = async (stampData) => {
  try {

    // 调用后端盖章接口
    const response = await contractApi.stampSeal(contractId.value, stampData)

    if (response && response.code === 200) {
      // 更新文件URL为盖章后的文件
      stampedFileId.value = response.data
      fileUrl.value = `/zapi-allin-sys/file/download/${response.data}`
      hasStamped.value = true

      ElMessage.success({
        message: `盖章成功！位置: 第${stampData.signPage}页 (${Math.round(stampData.pageX)}, ${Math.round(stampData.pageY)})`,
        type: 'success',
        duration: 3000
      })
    } else {
      throw new Error(response?.msg || '盖章失败')
    }
  } catch (error) {
    console.error('盖章失败:', error)
    ElMessage.error('盖章失败: ' + (error.message || '未知错误'))
  }
}

// 处理盖章失败
const handleStampError = (error) => {
  console.error('盖章操作失败:', error)
  ElMessage.error('盖章操作失败: ' + (error.message || '未知错误'))
}

// 下载盖章后的文件
const downloadStampedFile = () => {
  if (stampedFileId.value) {
    window.open(`/zapi-allin-sys/file/download/${stampedFileId.value}`)
  } else {
    ElMessage.warning('暂无盖章文件可下载')
  }
}

// 返回列表
const handleBack = () => {
  router.push('/home/contract/index')
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
.sign-container {
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

.pdf-viewer-container {
  flex: 1;
  overflow: hidden;
}
</style>
