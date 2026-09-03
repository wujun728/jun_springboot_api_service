<template>
  <div class="pdf-viewer-container">
    <!-- 工具栏 -->
    <div class="pdf-toolbar">
      <div class="toolbar-left">
        <el-button @click="toggleNavigation" size="small">
          <el-icon class="el-icon--left"><Menu /></el-icon>
          导航
        </el-button>
        <el-button @click="onFirstPage" size="small">首页</el-button>
        <el-button @click="onPrevPage" size="small">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <span class="page-indicator">{{ pageNum }} / {{ pageCount }}</span>
        <el-button @click="onNextPage" size="small">
          <el-icon><ArrowRight /></el-icon>
        </el-button>
        <el-button @click="onLastPage" size="small">末页</el-button>
        <el-divider direction="vertical" />
        <span class="scale-label">缩放</span>
        <el-slider
          v-model="scale"
          @change="changeScale"
          :show-input="true"
          class="scale-slider"
          :min="0.5"
          :max="3"
          :step="0.1"
        />
      </div>
      <div class="toolbar-right">
        <slot name="toolbar-actions"></slot>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="pdf-content">
      <!-- PDF 显示区域 -->
      <div class="pdf-display-area" :class="{ 'with-sidebar': mode === 'stamp' }">
        <div ref="pdfShowRef" class="pdf-container" id="pdf_show">
          <canvas
            ref="pdfCanvasRef"
            id="pdf_canvas"
            @mousedown="handleCanvasClick"
            @mousemove="handleMouseMove"
            :class="{ 'stamping': isStamping }"
          />

          <!-- 印章拖拽元素 -->
          <div
            v-if="mode === 'stamp'"
            ref="sealRef"
            id="seal_drag"
            class="seal-drag"
            :style="sealStyle"
          >
            <img :src="sealImageUrl" class="seal-image" />
          </div>
        </div>
      </div>

      <!-- 盖章操作面板 -->
      <div v-if="mode === 'stamp'" class="stamp-panel">
        <div class="panel-card">
          <div class="panel-header">
            <el-icon class="panel-icon"><Stamp /></el-icon>
            <span class="panel-title">盖章操作</span>
          </div>

          <!-- 盖章控制按钮 -->
          <div class="stamp-actions">
            <el-button
              type="primary"
              @click="beginStamp"
              :disabled="isStamping"
              class="action-btn"
            >
              <el-icon><Edit /></el-icon>
              开始盖章
            </el-button>
            <el-button
              type="danger"
              @click="endStamp"
              :disabled="!isStamping"
              class="action-btn"
            >
              <el-icon><Close /></el-icon>
              取消盖章
            </el-button>
          </div>

          <!-- 印章选择 -->
          <div class="seal-selection">
            <div class="section-label">选择印章</div>
            <div class="seal-list" v-if="sealList.length > 0">
              <div
                v-for="seal in sealList"
                :key="seal.id"
                :class="['seal-item', { active: selectedSeal?.id === seal.id }]"
                @click="selectSeal(seal)"
              >
                <img :src="getSealImageUrl(seal.fileId)" class="seal-item-image" />
                <span class="seal-item-name">{{ seal.sealName }}</span>
              </div>
            </div>
            <el-empty v-else description="暂无印章" :image-size="60" />

            <!-- 当前选中印章预览 -->
            <div v-if="selectedSeal" class="current-seal-preview">
              <img :src="sealImageUrl" class="preview-image" />
            </div>
          </div>

          <el-divider />

          <!-- 证书选择 -->
          <div class="cert-selection">
            <div class="section-label">选择证书</div>
            <div class="cert-list" v-if="certList.length > 0">
              <div
                v-for="cert in certList"
                :key="cert.id"
                :class="['cert-item', { active: selectedCert?.id === cert.id }]"
                @click="selectCert(cert)"
              >
                <div class="cert-item-name">{{ cert.certCn }}</div>
                <div class="cert-item-type">{{ cert.certType?.toUpperCase() }}</div>
              </div>
            </div>
            <el-empty v-else description="暂无证书" :image-size="60" />
          </div>
        </div>
      </div>
    </div>

    <!-- 左侧导航 -->
    <el-drawer v-model="showNavigation" size="20%" direction="ltr" :show-close="false">
      <div class="navigation-wrapper">
        <div class="navigation-content">
          <div class="nav-title">页面导航</div>
          <div class="page-list">
            <div
              v-for="page in pageCount"
              :key="page"
              :class="['page-item', { active: page === pageNum }]"
              @click="goToPage(page)"
            >
              <canvas :id="`nav_canvas_${page}`" class="nav-canvas"></canvas>
              <div class="page-number">第 {{ page }} 页</div>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Stamp, Edit, Close, Menu, ArrowLeft, ArrowRight
} from '@element-plus/icons-vue'
import { sealApi } from '@/api/seal'
import { certApi } from '@/api/cert'
import { configurePDFJS } from '@/utils/pdfConfig'

// PDF.js 实例
let PDFJS = null

// 异步初始化 PDF.js
const initPDFJS = async () => {
  try {
    console.log('开始初始化 PDF.js...')
    const config = await configurePDFJS()
    console.log('configurePDFJS 返回:', config)
    
    if (config.success) {
      PDFJS = config.pdfjs
      console.log('PDF.js 对象:', PDFJS)
      console.log('PDFJS.getDocument 存在?', typeof PDFJS.getDocument)
      console.log('Worker路径:', config.workerSrc)
      
      // 验证 getDocument 方法
      if (!PDFJS || typeof PDFJS.getDocument !== 'function') {
        console.error('PDFJS.getDocument 不是函数！')
        console.error('PDFJS 的属性:', Object.keys(PDFJS || {}))
        return false
      }
      
      console.log('PDF.js 初始化成功')
      return true
    } else {
      throw new Error(config.error)
    }
  } catch (error) {
    console.error('PDF.js 初始化失败:', error)
    return false
  }
}

// PDF.js 初始化状态
const pdfInitialized = ref(false)

// Props 定义
const props = defineProps({
  // PDF 文件 URL
  fileUrl: {
    type: String,
    required: true
  },
  // 模式：'preview' 预览模式，'stamp' 盖章模式
  mode: {
    type: String,
    default: 'preview',
    validator: (value) => ['preview', 'stamp'].includes(value)
  },
  // 合同ID（盖章模式需要）
  contractId: {
    type: [String, Number],
    default: null
  },
  // 文件ID（盖章模式需要）
  fileId: {
    type: [String, Number],
    default: null
  }
})

// Emits 定义
const emit = defineEmits(['stamp-success', 'stamp-error'])

// DOM refs
const pdfCanvasRef = ref(null)
const pdfShowRef = ref(null)
const sealRef = ref(null)

// PDF 相关状态
const pdfDoc = ref(null)
const pageNum = ref(1)
const pageCount = ref(0)
const scale = ref(1)
const pageRendering = ref(false)
const pageNumPending = ref(null)

// 导航相关
const showNavigation = ref(false)

// 盖章相关状态
const isStamping = ref(false)
const sealList = ref([])
const certList = ref([])
const selectedSeal = ref(null)
const selectedCert = ref(null)
const sealImageUrl = ref('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGNpcmNsZSBjeD0iNDAiIGN5PSI0MCIgcj0iMzUiIHN0cm9rZT0iI0Y1NjYwMCIgc3Ryb2tlLXdpZHRoPSIzIiBmaWxsPSJub25lIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtc2l6ZT0iMTYiIGZpbGw9IiNGNTY2MDAiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj7lhaznq6A8L3RleHQ+PC9zdmc+')
const sealStyle = ref({
  width: "100px",
  height: "100px"
})

// 监听文件 URL 变化
watch(() => props.fileUrl, async (newUrl) => {
  if (newUrl) {
    // 确保在加载 PDF 前 PDF.js 已初始化
    if (!pdfInitialized.value) {
      console.log('文件 URL 变化，需要先初始化 PDF.js')
      const success = await initPDFJS()
      if (success) {
        pdfInitialized.value = true
      }
    }
    loadPdf(newUrl)
  }
}, { immediate: true })

// 监听模式变化
watch(() => props.mode, (newMode) => {
  if (newMode === 'stamp') {
    loadStampData()
  }
}, { immediate: true })

// PDF 加载函数
const loadPdf = async (url) => {
  console.log('开始加载PDF:', url)

  // 确保 PDF.js 已初始化
  if (!pdfInitialized.value) {
    console.log('PDF.js 未初始化，开始初始化...')
    const success = await initPDFJS()
    if (!success) {
      ElMessage.error('PDF 库初始化失败')
      return
    }
    pdfInitialized.value = true
  }

  if (!PDFJS || !PDFJS.getDocument) {
    console.error('PDFJS.getDocument 方法不存在')
    ElMessage.error('PDF 库方法缺失')
    return
  }

  try {
    console.log('调用 PDFJS.getDocument:', url)
    const loadingTask = PDFJS.getDocument(url)
    const pdfDoc_ = await loadingTask.promise

    console.log('PDF加载成功，页数:', pdfDoc_.numPages)
    pdfDoc.value = pdfDoc_
    pageCount.value = pdfDoc.value.numPages
    ElMessage.success(`PDF加载成功，共${pageCount.value}页`)

    // 等待下一个 tick 确保 DOM 更新完成
    await nextTick()
    renderPage(pageNum.value, scale.value)

    // 如果显示导航，渲染导航页面
    if (showNavigation.value) {
      renderNavigationPages()
    }
  } catch (error) {
    console.error('PDF加载失败:', error)
    ElMessage.error('PDF文件加载失败: ' + (error.message || '文件不存在或格式错误'))
  }
}

// 渲染 PDF 页面
const renderPage = (num, scaleValue) => {
  console.log('开始渲染第', num, '页，缩放:', scaleValue)
  pageRendering.value = true

  const canvas = pdfCanvasRef.value
  if (!canvas) {
    console.error('Canvas元素未找到！')
    return
  }

  if (!pdfDoc.value) {
    console.error('PDF文档未加载！')
    return
  }

  const ctx = canvas.getContext('2d')
  pdfDoc.value.getPage(num).then(function (page) {
    const viewport = page.getViewport({ scale: scaleValue })
    canvas.height = viewport.height
    canvas.width = viewport.width

    const renderContext = {
      canvasContext: ctx,
      viewport: viewport
    }

    const renderTask = page.render(renderContext)
    renderTask.promise.then(function () {
      console.log('PDF页面渲染完成！')
      pageRendering.value = false
      if (pageNumPending.value !== null) {
        renderPage(pageNumPending.value, scale.value)
        pageNumPending.value = null
      }
    }).catch(function(error) {
      console.error('PDF页面渲染失败:', error)
      pageRendering.value = false
      ElMessage.error('PDF页面渲染失败: ' + error.message)
    })
  }).catch(function (error) {
    console.error('获取PDF页面失败:', error)
    pageRendering.value = false
    ElMessage.error('获取PDF页面失败: ' + error.message)
  })
}

// 页面导航函数
const queueRenderPage = (num) => {
  if (pageRendering.value) {
    pageNumPending.value = num
  } else {
    renderPage(num, scale.value)
  }
}

const onFirstPage = () => {
  if (pageNum.value <= 1) return
  pageNum.value = 1
  queueRenderPage(pageNum.value)
}

const onLastPage = () => {
  if (pageNum.value >= pageCount.value) return
  pageNum.value = pageCount.value
  queueRenderPage(pageNum.value)
}

const onPrevPage = () => {
  if (pageNum.value <= 1) return
  pageNum.value--
  queueRenderPage(pageNum.value)
}

const onNextPage = () => {
  if (pageNum.value >= pageCount.value) return
  pageNum.value++
  queueRenderPage(pageNum.value)
}

const goToPage = (page) => {
  if (page < 1 || page > pageCount.value) return
  pageNum.value = page
  queueRenderPage(pageNum.value)
}

const changeScale = (newScale) => {
  scale.value = newScale
  renderPage(pageNum.value, scale.value)
}

// 导航相关
const toggleNavigation = () => {
  showNavigation.value = !showNavigation.value
  if (showNavigation.value) {
    nextTick(() => {
      renderNavigationPages()
    })
  }
}

const renderNavigationPages = () => {
  if (!pdfDoc.value) return

  for (let i = 1; i <= pageCount.value; i++) {
    const canvas = document.getElementById(`nav_canvas_${i}`)
    if (!canvas) continue

    const ctx = canvas.getContext('2d')
    pdfDoc.value.getPage(i).then(function (page) {
      const viewport = page.getViewport({ scale: 0.2 })
      canvas.height = viewport.height
      canvas.width = viewport.width

      const renderContext = {
        canvasContext: ctx,
        viewport: viewport
      }
      page.render(renderContext)
    })
  }
}

// 盖章相关函数
const loadStampData = async () => {
  if (props.mode !== 'stamp') return

  try {
    await Promise.all([
      loadSealList(),
      loadCertList()
    ])
  } catch (error) {
    console.error('加载盖章数据失败:', error)
  }
}

const loadSealList = async () => {
  try {
    const params = {
      current: 1,
      size: 100,
      status: 1
    }

    const response = await sealApi.pageUserSeals(params)
    if (response && response.code === 200) {
      sealList.value = response.data?.records || response.data || []
      if (sealList.value.length > 0) {
        selectSeal(sealList.value[0])
      }
    }
  } catch (error) {
    console.error('获取印章列表失败:', error)
    ElMessage.error('获取印章列表失败')
  }
}

const loadCertList = async () => {
  try {
    const params = {
      current: 1,
      size: 100
    }

    const response = await certApi.pageUserCerts(params)
    if (response && response.code === 200) {
      certList.value = response.data?.records || response.data || []
      if (certList.value.length > 0) {
        selectCert(certList.value[0])
      }
    }
  } catch (error) {
    console.error('获取证书列表失败:', error)
    ElMessage.error('获取证书列表失败')
  }
}

const selectSeal = (seal) => {
  selectedSeal.value = seal
  sealImageUrl.value = getSealImageUrl(seal.fileId)
  ElMessage.success(`已选择印章: ${seal.sealName}`)
}

const selectCert = (cert) => {
  selectedCert.value = cert
  ElMessage.success(`已选择证书: ${cert.certCn}`)
}

const getSealImageUrl = (fileId) => {
  if (!fileId) {
    return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGNpcmNsZSBjeD0iNDAiIGN5PSI0MCIgcj0iMzUiIHN0cm9rZT0iI0Y1NjYwMCIgc3Ryb2tlLXdpZHRoPSIzIiBmaWxsPSJub25lIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtc2l6ZT0iMTYiIGZpbGw9IiNGNTY2MDAiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj7lhaznq6A8L3RleHQ+PC9zdmc+'
  }
  return `/zapi-allin-sys/file/download/${fileId}`
}

// 盖章操作
const beginStamp = () => {
  if (!selectedSeal.value) {
    ElMessage.error('请先选择印章')
    return
  }
  if (!selectedCert.value) {
    ElMessage.error('请先选择证书')
    return
  }

  isStamping.value = true
  sealStyle.value = {
    width: "100px",
    height: "100px",
    position: "fixed",
    pointerEvents: "none",
    zIndex: 9999
  }

  ElMessage.success('请在PDF上点击需要盖章的位置')
}

const endStamp = () => {
  isStamping.value = false
  sealStyle.value = {
    width: "100px",
    height: "100px"
  }
}

// 鼠标事件处理
const handleMouseMove = (e) => {
  if (!isStamping.value || props.mode !== 'stamp') return

  const sealElement = sealRef.value
  if (!sealElement) return

  const pdfShow = pdfShowRef.value
  const scrollTop = pdfShow ? pdfShow.scrollTop : 0
  const scrollLeft = pdfShow ? pdfShow.scrollLeft : 0

  const zX = e.clientX
  const zY = e.clientY

  sealElement.style.left = (zX + scrollLeft - 50) + "px"
  sealElement.style.top = (zY + scrollTop - 50) + "px"
}

const handleCanvasClick = async (e) => {
  if (props.mode !== 'stamp' || !isStamping.value) return

  // 验证必要参数
  if (!selectedSeal.value) {
    ElMessage.error('请先选择印章')
    return
  }
  if (!selectedCert.value) {
    ElMessage.error('请先选择证书')
    return
  }

  const canvas = pdfCanvasRef.value
  const rect = canvas.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top

  // 弹出证书密码输入框
  try {
    const { value: certPwd } = await ElMessageBox.prompt('请输入证书密码', '证书密码验证', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password',
      inputPlaceholder: '请输入证书密码',
      closeOnClickModal: false
    })

    await performStamp(x, y, certPwd)
  } catch {
    ElMessage.info('已取消盖章')
  }
}

const performStamp = async (x, y, certPwd) => {
  try {
    // 获取PDF信息
    const pdfPage = await pdfDoc.value.getPage(pageNum.value)
    const viewport = pdfPage.getViewport({ scale: 1 })

    // 构建请求参数
    const stampData = {
      signFileId: props.fileId,
      certId: selectedCert.value.id,
      sealId: selectedSeal.value.id,
      certPwd: certPwd,
      pdfPageCount: pageCount.value,
      pdfWidth: viewport.width,
      pdfHeight: viewport.height,
      signPage: pageNum.value,
      pageX: x,
      pageY: y
    }

    // 触发盖章成功事件
    emit('stamp-success', stampData)

    // 结束盖章模式
    endStamp()

  } catch (error) {
    console.error('盖章失败:', error)
    emit('stamp-error', error)
    endStamp()
  }
}

// 组件挂载
onMounted(async () => {
  console.log('PDF Viewer 组件已挂载')

  // 预初始化 PDF.js（可选，也可以等到需要时再初始化）
  if (!pdfInitialized.value) {
    console.log('预初始化 PDF.js...')
    const success = await initPDFJS()
    if (success) {
      pdfInitialized.value = true
      console.log('PDF.js 预初始化成功')
    } else {
      console.warn('PDF.js 预初始化失败，将在需要时重试')
    }
  }
})
</script>

<style scoped>
.pdf-viewer-container {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: var(--bg-card);
}

.pdf-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px;
  background: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color);
  box-shadow: var(--el-box-shadow-lighter);
}

.toolbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
}

.toolbar-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.page-indicator {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  background: var(--el-fill-color-light);
  border-radius: 4px;
  font-size: 13px;
  font-weight: 500;
  color: var(--el-text-color-primary);
  min-width: 60px;
  justify-content: center;
}

.scale-label {
  font-size: 13px;
  color: var(--el-text-color-regular);
  margin-left: 4px;
  font-weight: 500;
}

.scale-slider {
  width: 200px;
  margin-left: 8px;
}

.pdf-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.pdf-display-area {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.pdf-display-area.with-sidebar {
  width: 78%;
}

.pdf-container {
  flex: 1;
  overflow: auto;
  background-color: var(--el-bg-color-page);
  padding: 20px;
  text-align: center;
  position: relative;
}

#pdf_canvas {
  box-shadow: var(--shadow-md);
  border-radius: 4px;
  background: var(--bg-card);
}

#pdf_canvas.stamping {
  cursor: none !important;
}

.seal-drag {
  position: absolute;
  pointer-events: none;
  z-index: 1000;
  transition: none !important;
}

.seal-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.stamp-panel {
  width: 22%;
  padding: 20px;
  background: var(--bg-secondary);
  border-left: 1px solid var(--border-color);
  overflow-y: auto;
}

.panel-card {
  background: var(--bg-card);
  border-radius: 12px;
  padding: 20px;
  box-shadow: var(--shadow-md);
}

.panel-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid var(--el-border-color-lighter);
}

.panel-icon {
  font-size: 20px;
  color: var(--el-color-primary);
  margin-right: 8px;
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
}

.stamp-actions {
  display: flex;
  gap: 10px;
  margin-bottom: 24px;
}

.action-btn {
  flex: 1;
  height: 40px;
  font-size: 14px;
  border-radius: 8px;
}

.section-label {
  font-size: 13px;
  color: var(--el-text-color-regular);
  margin-bottom: 12px;
  font-weight: 500;
}

.seal-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin-bottom: 20px;
  max-height: 300px;
  overflow-y: auto;
}

.seal-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  background: var(--bg-card);
}

.seal-item:hover {
  border-color: var(--primary-color);
  background: var(--primary-light);
}

.seal-item.active {
  border-color: var(--primary-color);
  background: var(--primary-light);
  box-shadow: var(--shadow-sm);
}

.seal-item-image {
  width: 50px;
  height: 50px;
  object-fit: contain;
  margin-bottom: 8px;
}

.seal-item-name {
  font-size: 12px;
  color: var(--el-text-color-regular);
  text-align: center;
  word-break: break-all;
}

.current-seal-preview {
  display: flex;
  justify-content: center;
  padding: 15px;
  background: var(--bg-secondary);
  border-radius: 8px;
  margin-top: 15px;
}

.preview-image {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.cert-list {
  display: grid;
  grid-template-columns: 1fr;
  gap: 10px;
  max-height: 200px;
  overflow-y: auto;
}

.cert-item {
  padding: 12px;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  background: var(--bg-card);
}

.cert-item:hover {
  border-color: var(--primary-color);
  background: var(--primary-light);
}

.cert-item.active {
  border-color: var(--primary-color);
  background: var(--primary-light);
  box-shadow: var(--shadow-sm);
}

.cert-item-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 4px;
}

.cert-item-type {
  font-size: 11px;
  color: var(--el-text-color-secondary);
}

.navigation-wrapper {
  height: 100%;
  overflow: hidden;
}

.navigation-content {
  height: 100%;
  overflow-y: auto;
  padding: 15px 10px;
}

.nav-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 20px;
  text-align: center;
}

.page-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.page-item {
  border: 1px dashed var(--el-border-color);
  border-radius: 4px;
  padding: 8px;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
}

.page-item:hover {
  border-color: var(--primary-color);
  background: var(--primary-light);
}

.page-item.active {
  border-color: var(--primary-color);
  background: var(--primary-light);
  box-shadow: var(--shadow-sm);
}

.nav-canvas {
  width: 100%;
  height: auto;
  display: block;
  margin-bottom: 8px;
}

.page-number {
  font-size: 12px;
  color: var(--el-text-color-regular);
}
</style>
