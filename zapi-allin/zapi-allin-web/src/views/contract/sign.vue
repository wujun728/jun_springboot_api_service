<template>
  <div style="background-color: #ffff;">
    <!-- 头部 -->
    <div class="toolbar-header">
      <div class="toolbar-section toolbar-left">
        <el-button @click="handleBack" size="small" plain>
          <el-icon><Back /></el-icon>
          返回
        </el-button>
        <el-divider direction="vertical" />
        <el-button @click="isShow_l" size="small">
          <el-icon><Menu /></el-icon>
          导航
        </el-button>
        <el-button @click="onFirstPage" size="small">首页</el-button>
        <el-button @click="onPrevPage" size="small">
          <el-icon><ArrowLeft /></el-icon>
        </el-button>
        <span class="page-indicator">{{pageNum}} / {{pageCount}}</span>
        <el-button @click="onNextPage" size="small">
          <el-icon><ArrowRight /></el-icon>
        </el-button>
        <el-button @click="onLastPage" size="small">末页</el-button>
        <el-divider direction="vertical" />
        <span class="scale-label">缩放</span>
        <el-slider v-model="scale" @change="changeSuoFang" :show-input="true" class="scale-slider"></el-slider>
      </div>
      <div class="toolbar-section toolbar-right">
        <el-button v-if="hasStamped" @click="downloadPdf" size="small" type="success">
          <el-icon><Download /></el-icon>
          下载盖章后的文件
        </el-button>
      </div>
    </div>

    <!-- 内容部分 -->
    <div class="content">
      <div class="div_book">
        <div ref="pdfShowRef" class="content_read_pdf base_scrollbar" id="pdf_show">
          <canvas ref="pdfCanvasRef" id="pdf_canvas" @mousedown="clickPdfPos" @mousemove="divFlying"></canvas>
        </div>
      </div>
      <div class="div_book_right">
        <div class="right-panel-card">
          <div class="panel-header">
            <el-icon class="panel-icon"><Stamp /></el-icon>
            <span class="panel-title">盖章操作</span>
          </div>

          <div class="stamp-actions">
            <el-button
              type="primary"
              @click="beginSign"
              :disabled="divFlyingUse"
              class="action-btn-inline"
            >
              <el-icon><Edit /></el-icon>
              开始盖章
            </el-button>
            <el-button
              type="danger"
              @click="endSign"
              :disabled="!divFlyingUse"
              class="action-btn-inline"
            >
              <el-icon><Close /></el-icon>
              取消盖章
            </el-button>
          </div>

          <div class="stamp-preview-wrapper">
            <div class="preview-label">选择印章</div>
            <div class="seal-list" v-if="sealList.length > 0">
              <div
                v-for="seal in sealList"
                :key="seal.id"
                :class="['seal-item', { active: selectedSeal?.id === seal.id }]"
                @click="selectSeal(seal)"
              >
                <img :src="getSealImageUrl(seal.fileId)" class="seal-item-image" />
                <div class="seal-item-name">{{ seal.sealName }}</div>
              </div>
            </div>
            <el-empty v-else description="暂无印章" :image-size="60" />
            <div class="preview-label" style="margin-top: 20px;">当前印章</div>
            <div ref="yzRef" id="yz" class="stamp-preview" :style="yzStyle">
              <img :src="yzImgUrl" class="stamp-image" />
            </div>
          </div>

          <el-divider />

          <div class="cert-preview-wrapper">
            <div class="preview-label">选择证书</div>
            <div class="cert-list" v-if="certList.length > 0">
              <div
                v-for="cert in certList"
                :key="cert.id"
                :class="['cert-item', { active: selectedCert?.id === cert.id }]"
                @click="selectCert(cert)"
              >
                <div class="cert-item-name">{{ cert.certCn }}</div>
                <div class="cert-item-type">{{ cert.certType }}</div>
              </div>
            </div>
            <el-empty v-else description="暂无证书" :image-size="60" />
            <div class="preview-label" style="margin-top: 20px;">当前证书</div>
            <div class="current-cert" v-if="selectedCert">
              <div class="cert-info">
                <div><strong>证书名称:</strong> {{ selectedCert.certCn }}</div>
                <div><strong>证书类型:</strong> {{ selectedCert.certType }}</div>
              </div>
            </div>
            <el-empty v-else description="未选择证书" :image-size="60" />
          </div>

          <div v-if="breakFileUid !== null && breakFileUid !== ''" class="download-section">
            <el-divider />
            <el-button
              type="success"
              @click="signBreakFile"
              class="download-btn"
            >
              <el-icon><Download /></el-icon>
              下载盖章文件
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 左侧导航 -->
    <el-drawer v-model="drawer_l" size="20%" direction="ltr" :show-close="false">
      <div class="drawer-wrapper">
        <div class="left_content base_scrollbar">
          <div v-for="i in pageCount" :key="i">
            <div :id="'page_'+i" class="left_pdf_xx" @click="onRomPage(i)">
              <canvas :id="'canvas_'+i"></canvas>
              <div class="page-num">第{{i}}页</div>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Stamp, Edit, Close, Download, Back, Menu,
  ArrowLeft, ArrowRight
} from '@element-plus/icons-vue'
import { sealApi, certApi, contractApi } from '@/api'
import { configurePDFJS } from '@/utils/pdfConfig'

// PDF.js 实例（从 CDN 加载）
let PDFJS = null

// 初始化 PDF.js
const initPDFJS = async () => {
  const config = await configurePDFJS()
  if (config.success) {
    PDFJS = config.pdfjs
    return true
  }
  console.error('PDF.js 初始化失败:', config.error)
  return false
}

const route = useRoute()
const router = useRouter()

// 路由参数
const contractId = ref(route.query.id || null)
const fileId = ref(route.query.fileId || null)

// PDF 相关
const fileUrl = ref('')
const pdfDoc = ref(null)
const pageNum = ref(1)
const pageCount = ref(1)
const pageRendering = ref(false)
const pageNumPending = ref(null)
const scale = ref(50)
const scaleInfo = ref(1.0)
const scaleList = ref(0.5)

// UI 状态
const drawer_l = ref(false)

// 盖章相关
const divFlyingUse = ref(false)
const yz_x = ref('')
const yz_y = ref('')
const yzImgUrl = ref('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGNpcmNsZSBjeD0iNDAiIGN5PSI0MCIgcj0iMzUiIHN0cm9rZT0iI0Y1NjYwMCIgc3Ryb2tlLXdpZHRoPSIzIiBmaWxsPSJub25lIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtc2l6ZT0iMTYiIGZpbGw9IiNGNTY2MDAiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj7lhaznq6A8L3RleHQ+PC9zdmc+')
const yzStyle = ref({ width: "100px", height: "100px" })
const breakFileUid = ref('')
const sealList = ref([])
const selectedSeal = ref(null)
const certList = ref([])
const selectedCert = ref(null)
const hasStamped = ref(false) // 是否已盖章

// DOM refs
const pdfCanvasRef = ref(null)
const yzRef = ref(null)
const pdfShowRef = ref(null)

// 显示pdf信息
const getPdfInfo = (url, scaleInfo) => {

  if (!PDFJS || !PDFJS.getDocument) {
    console.error('PDFJS 未正确加载')
    ElMessage.error('PDF 库加载失败')
    return
  }

  // PDF.js 2.6.347 的 getDocument 返回 PDFDocumentLoadingTask，需要用 .promise
  const loadingTask = PDFJS.getDocument(url)
  loadingTask.promise.then(function(pdfDoc_) {
    pdfDoc.value = pdfDoc_
    pageCount.value = pdfDoc.value.numPages
    ElMessage.success(`PDF加载成功，共${pageCount.value}页`)
    renderPage(pageNum.value, scaleInfo)
  }).catch(function(error) {
    console.error('PDF加载失败:', error)
    ElMessage.error('PDF文件加载失败: ' + (error.message || '文件不存在或格式错误'))

    // 在画布上显示错误信息
    const canvas = pdfCanvasRef.value
    if (canvas) {
      const ctx = canvas.getContext('2d')
      canvas.width = 800
      canvas.height = 400
      ctx.fillStyle = '#f56c6c'
      ctx.font = '16px Arial'
      ctx.fillText('PDF文件加载失败', 50, 50)
      ctx.fillText(error.message || '未知错误', 50, 80)
    }
  })
}

// 渲染某页pdf内容
const renderPage = (num, scale) => {
  pageRendering.value = true
  let canvas = pdfCanvasRef.value

  if (!canvas) {
    console.error('Canvas元素未找到！')
    return
  }

  if (!pdfDoc.value) {
    console.error('PDF文档未加载！')
    return
  }

  let ctx = canvas.getContext('2d')
  pdfDoc.value.getPage(num).then(function (page) {
    // PDF.js 2.x 需要传入对象参数 { scale: number }
    let viewport = page.getViewport({ scale: scale })
    canvas.height = viewport.height
    canvas.width = viewport.width

    let renderContext = {
      canvasContext: ctx,
      viewport: viewport
    }
    let renderTask = page.render(renderContext)
    renderTask.promise.then(function () {
      pageRendering.value = false
      if (pageNumPending.value !== null) {
        renderPage(pageNumPending.value, scaleInfo.value)
        pageNumPending.value = null
      }
    }).catch(function (error) {
      console.error('渲染任务失败:', error)
      ElMessage.error('渲染任务失败: ' + (error.message || '未知错误'))
    })
  }).catch(function (error) {
    console.error('获取PDF页面失败:', error)
    ElMessage.error('获取PDF页面失败: ' + (error.message || '未知错误'))
  })
}

// 显示pdf左侧导航
const getPdfList = async (url, size) => {
  // 确保 PDF.js 已初始化
  if (!PDFJS || !PDFJS.getDocument) {
    const initialized = await initPDFJS()
    if (!initialized) {
      console.error('PDF.js 未初始化，无法加载导航')
      return
    }
  }

  // PDF.js 2.6.347 的 getDocument 返回 PDFDocumentLoadingTask，需要用 .promise
  const loadingTask = PDFJS.getDocument(url)
  loadingTask.promise.then(function(pdfDoc_) {
    pdfDoc.value = pdfDoc_
    pageCount.value = pdfDoc.value.numPages
    pageRendering.value = true
    let pdfCountPages = pdfDoc_.numPages
    for (let j = 1; j <= pdfCountPages; j++) {
      renderPages(j, scaleList.value)
    }
    pageRendering.value = false
  })
}

// 渲染左侧某页pdf内容
const renderPages = (num, scale) => {
  pageRendering.value = true
  let canvas = document.getElementById('canvas_' + num)
  if (!canvas) return

  let ctx = canvas.getContext('2d')
  pdfDoc.value.getPage(num).then(function (page) {
    // PDF.js 2.x 需要传入对象参数 { scale: number }
    let viewport = page.getViewport({ scale: scale })
    canvas.height = viewport.height
    canvas.width = viewport.width

    let renderContext = {
      canvasContext: ctx,
      viewport: viewport
    }
    let renderTask = page.render(renderContext)
    renderTask.promise.then(function () {
      pageRendering.value = false
      if (pageNumPending.value !== null) {
        renderPage(pageNumPending.value, scaleInfo.value)
        pageNumPending.value = null
      }
    })
  })
}

// 渲染队列
const queueRenderPage = (num) => {
  if (pageRendering.value) {
    pageNumPending.value = num
  } else {
    renderPage(num, scaleInfo.value)
  }
}

// 第一页
const onFirstPage = () => {
  pageNum.value = 1
  queueRenderPage(pageNum.value)
}

// 最后一页
const onLastPage = () => {
  pageNum.value = pdfDoc.value.numPages
  queueRenderPage(pageNum.value)
}

// 上一页
const onPrevPage = () => {
  if (pageNum.value <= 1) {
    ElMessage.warning("已经是第一页")
    return
  }
  pageNum.value--
  queueRenderPage(pageNum.value)
}

// 下一页
const onNextPage = () => {
  if (pageNum.value >= pdfDoc.value.numPages) {
    ElMessage.warning("已经是最后一页")
    return
  }
  pageNum.value++
  queueRenderPage(pageNum.value)
}

// 导航栏点击跳转页数
const onRomPage = (pageNums) => {
  pageNum.value = pageNums
  queueRenderPage(pageNum.value)
}

// 缩放
const changeSuoFang = (val) => {
  let scale = val / 100
  scaleInfo.value = 3 * scale
  queueRenderPage(pageNum.value)
  return scale
}

// 显示左侧导航
const isShow_l = () => {
  if (!drawer_l.value) {
    getPdfList(fileUrl.value, scaleList.value)
    drawer_l.value = true
  } else {
    drawer_l.value = false
  }
}

// 点击pdf获取位置信息
const clickPdfPos = (e) => {
  if (divFlyingUse.value) {
    let canvas = pdfCanvasRef.value
    let mousePos = canvas.getBoundingClientRect()
    let x = e.clientX - mousePos.left * (canvas.width / mousePos.width)
    let y = e.clientY - mousePos.top * (canvas.height / mousePos.height)

    // 验证必要参数
    if (!selectedSeal.value) {
      ElMessage.error('请先选择印章')
      return
    }
    if (!selectedCert.value) {
      ElMessage.error('请先选择证书')
      return
    }

    // 弹出证书密码输入框
    ElMessageBox.prompt('请输入证书密码', '证书密码验证', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputType: 'password',
      inputPlaceholder: '请输入证书密码',
      closeOnClickModal: false
    }).then(({ value: certPwd }) => {
      // 调用后端盖章接口
      stampSeal(x, y, certPwd, canvas)
    }).catch(() => {
      ElMessage.info('已取消盖章')
    })
  }
}

// 调用后端盖章接口
const stampSeal = async (x, y, certPwd, canvas) => {
  try {
    // 获取PDF信息
    const pdfPage = await pdfDoc.value.getPage(pageNum.value)
    const viewport = pdfPage.getViewport({ scale: 1 })

    // 构建请求参数
    const request = {
      signFileId: fileId.value,  // 需盖章签署文件ID
      certId: selectedCert.value.id,  // 证书文件ID
      sealId: selectedSeal.value.id,  // 印章文件ID
      certPwd: certPwd,  // 证书密码
      pdfPageCount: pageCount.value,  // 总页数
      pdfWidth: viewport.width,  // PDF宽度
      pdfHeight: viewport.height,  // PDF高度
      signPage: pageNum.value,  // 盖章文件页码
      pageX: x,  // 盖章位置--x轴
      pageY: y   // 盖章位置--y轴
    }


    // 调用后端接口
    const response = await contractApi.stampSeal(contractId.value, request)

    if (response && response.code === 200) {
      // 结束盖章模式
      endSign()

      // 刷新pdf文件
      fileUrl.value = `/zapi-allin-sys/file/download/${response.data}`
      // 显示pdf
      getPdfInfo(fileUrl.value, scaleInfo.value)

      // 标记已盖章
      hasStamped.value = true
      breakFileUid.value = response.data

      ElMessage.success({
        message: `盖章成功！位置: 第${pageNum.value}页 (${Math.round(x)}, ${Math.round(y)})`,
        type: 'success',
        duration: 2000
      })
    } else {
      ElMessage.error(response?.msg || '盖章失败')
      // 取消盖章模式
      endSign()
    }
  } catch (error) {
    console.error('盖章失败:', error)
    ElMessage.error('盖章失败: ' + (error.message || '未知错误'))
    // 取消盖章模式
    endSign()
  }
}

// 印章跟随鼠标移动
const divFlying = (e) => {
  if (divFlyingUse.value) {
    let div = yzRef.value
    if (!div) return

    // 获取 PDF 显示容器的滚动位置
    let pdfShow = pdfShowRef.value
    let scrollTop = pdfShow ? pdfShow.scrollTop : 0
    let scrollLeft = pdfShow ? pdfShow.scrollLeft : 0

    // 获取鼠标相对于视口的位置
    let zX = e.clientX
    let zY = e.clientY

    // 设置印章位置（偏移一点，避免遮挡鼠标）
    div.style.left = (zX + scrollLeft - 40) + "px"
    div.style.top = (zY + scrollTop - 40) + "px"
  }
}

// 开始盖章
const beginSign = () => {
  divFlyingUse.value = true
  yzStyle.value = {
    width: "100px",
    height: "100px",
    position: "fixed"
  }

  // 添加 CSS 类
  const canvas = pdfCanvasRef.value
  const yz = yzRef.value
  if (canvas) canvas.classList.add('stamping')
  if (yz) yz.classList.add('dragging')

  ElMessage.success({
    message: '请在PDF上点击需要盖章的位置',
    type: 'success',
    duration: 2000
  })
}

// 取消盖章
const endSign = () => {
  let div = yzRef.value
  if (!div) return

  div.style.left = yz_x.value + "px"
  div.style.top = yz_y.value + "px"
  div.style.position = "relative"
  divFlyingUse.value = false

  // 移除 CSS 类
  const canvas = pdfCanvasRef.value
  if (canvas) canvas.classList.remove('stamping')
  if (div) div.classList.remove('dragging')

  yzStyle.value = {
    width: "100px",
    height: "100px"
  }
}

// 下载盖章后的文件
const signBreakFile = () => {
  window.open('/zapi-allin-sys/file/download/' + breakFileUid.value)
}

// 下载PDF
const downloadPdf = () => {
  if (fileUrl.value) {
    window.open(fileUrl.value)
  }
}

// 返回列表
const handleBack = () => {
  router.push('/home/contract/index')
}

// 获取印章列表
const loadSealList = async () => {
  try {
    const params = {
      current: 1,
      size: 100,
      status: 1 // 1表示启用状态
    }

    const response = await sealApi.pageUserSeals(params)

    if (response && response.code === 200) {
      sealList.value = response.data?.records || response.data || []

      // 如果有印章，默认选中第一个
      if (sealList.value.length > 0) {
        selectSeal(sealList.value[0])
      } else {
        ElMessage.info('暂无可用印章')
        console.warn("暂无可用印章")
      }
    } else {
      console.error('获取印章列表失败:', response)
      ElMessage.error(response?.msg || '获取印章列表失败')
    }
  } catch (error) {
    console.error("获取印章列表异常:", error)
    ElMessage.error('获取印章列表失败: ' + (error.message || '未知错误'))
  }
}

// 选择印章
const selectSeal = (seal) => {
  selectedSeal.value = seal
  yzImgUrl.value = getSealImageUrl(seal.fileId)
  ElMessage.success(`已选择印章: ${seal.sealName}`)
}

// 获取印章图片URL
const getSealImageUrl = (fileId) => {
  if (!fileId) {
    console.warn('印章文件ID为空')
    return 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGNpcmNsZSBjeD0iNDAiIGN5PSI0MCIgcj0iMzUiIHN0cm9rZT0iI0Y1NjYwMCIgc3Ryb2tlLXdpZHRoPSIzIiBmaWxsPSJub25lIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtc2l6ZT0iMTYiIGZpbGw9IiNGNTY2MDAiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGR5PSIuM2VtIj7lhaznq6A8L3RleHQ+PC9zdmc+'
  }
  return `/zapi-allin-sys/file/download/${fileId}`
}

// 获取证书列表
const loadCertList = async () => {
  try {
    const params = {
      current: 1,
      size: 100
    }

    const response = await certApi.pageUserCerts(params)

    if (response && response.code === 200) {
      certList.value = response.data?.records || response.data || []

      // 如果有证书，默认选中第一个
      if (certList.value.length > 0) {
        selectCert(certList.value[0])
      } else {
        console.warn('暂无可用证书')
        ElMessage.info('暂无可用证书')
      }
    } else {
      console.error('获取证书列表失败，响应:', response)
      ElMessage.error(response?.msg || '获取证书列表失败')
    }
  } catch (error) {
    console.error('获取证书列表异常:', error)
    ElMessage.error('获取证书列表失败: ' + (error.message || '未知错误'))
  }
}

// 选择证书
const selectCert = (cert) => {
  selectedCert.value = cert
  ElMessage.success(`已选择证书: ${cert.certCn}`)
}

// 页面加载
onMounted(async () => {
  console.log('=== 盖章页面 onMounted 开始 ===')
  console.log('路由参数 - contractId:', contractId.value)
  console.log('路由参数 - fileId:', fileId.value)
  console.log('window.pdfjsLib 是否存在:', typeof window.pdfjsLib !== 'undefined')

  if (!fileId.value) {
    ElMessage.warning('缺少文件ID')
    return
  }

  try {
    // 先初始化 PDF.js
    console.log('开始初始化 PDF.js...')
    const pdfInitialized = await initPDFJS()
    console.log('PDF.js 初始化结果:', pdfInitialized)
    console.log('PDFJS 对象:', PDFJS)
    
    if (!pdfInitialized) {
      ElMessage.error('PDF.js 初始化失败，请刷新页面重试')
      return
    }

    // 构建文件URL
    fileUrl.value = `/zapi-allin-sys/file/download/${fileId.value}`
    console.log('PDF 文件 URL:', fileUrl.value)

    // 显示pdf
    getPdfInfo(fileUrl.value, scaleInfo.value)

    // 加载印章列表
    loadSealList()

    // 加载证书列表
    loadCertList()

    // 获取印章位置
    setTimeout(() => {
      let dragImg = yzRef.value
      if (dragImg) {
        yz_x.value = dragImg.offsetLeft
        yz_y.value = dragImg.offsetTop
      }
    }, 100)
  } catch (error) {
    console.error('页面加载异常:', error)
    ElMessage.error('页面加载失败: ' + (error.message || '未知错误'))
  }
})
</script>

<style scoped>
.toolbar-header {
  width: 100%;
  background: linear-gradient(to bottom, #ffffff, #f8f9fa);
  display: flex;
  align-items: center;
  height: 56px;
  padding: 0 20px;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
}

.toolbar-section {
  display: flex;
  align-items: center;
  gap: 8px;
}

.toolbar-left {
  flex: 1;
  justify-content: flex-start;
  flex-wrap: nowrap;
}

.toolbar-right {
  flex: 0 0 auto;
  justify-content: flex-end;
}

.page-indicator {
  display: inline-flex;
  align-items: center;
  padding: 4px 12px;
  background: var(--bg-secondary);
  border-radius: 4px;
  font-size: 13px;
  font-weight: 500;
  color: var(--text-primary);
  min-width: 60px;
  justify-content: center;
}

.scale-label {
  font-size: 13px;
  color: #606266;
  margin-left: 4px;
  font-weight: 500;
}

.scale-slider {
  width: 280px;
  margin-left: 8px;
}

.content {
  display: flex;
  padding-left: 20px;
  padding-bottom: 20px;
  padding-right: 20px;
}

.div_book {
  margin-top: 20px;
  height: 900px;
  width: 78%;
  overflow: auto;
  margin-right: 20px;
  background-color: #EEEEEE;
  box-shadow: 5px 5px 5px 0 rgba(0, 0, 0, .05);
  -webkit-box-shadow: #c5caca 0px 0px 10px;
  -moz-box-shadow: #c5caca 0px 0px 10px;
}

.div_book_right {
  width: 22%;
  height: 100%;
  margin-top: 20px;
}

.right-panel-card {
  background: var(--bg-card);
  border-radius: 12px;
  padding: 20px;
  box-shadow: var(--shadow-md);
  height: fit-content;
}

.panel-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f0f0;
}

.panel-icon {
  font-size: 20px;
  color: #409eff;
  margin-right: 8px;
}

.panel-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.stamp-actions {
  display: flex;
  flex-direction: row;
  gap: 10px;
  margin-bottom: 24px;
}

.action-btn-inline {
  flex: 1;
  height: 40px;
  font-size: 14px;
  border-radius: 8px;
  transition: all 0.3s;
}

.action-btn-inline:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.stamp-preview-wrapper {
  margin-bottom: 20px;
}

.preview-label {
  font-size: 13px;
  color: #606266;
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
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

.seal-item.active {
  border-color: var(--primary-color);
  background: var(--primary-light);
  box-shadow: var(--shadow-sm);
}

.seal-item-image {
  width: 60px;
  height: 60px;
  object-fit: contain;
  margin-bottom: 8px;
}

.seal-item-name {
  font-size: 12px;
  color: #606266;
  text-align: center;
  word-break: break-all;
  max-width: 100%;
}

.cert-preview-wrapper {
  margin-bottom: 20px;
}

.cert-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  margin-bottom: 20px;
  max-height: 300px;
  overflow-y: auto;
}

.cert-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  background: var(--bg-card);
  min-height: 80px;
}

.cert-item:hover {
  border-color: var(--primary-color);
  background: var(--primary-light);
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

.cert-item.active {
  border-color: var(--primary-color);
  background: var(--primary-light);
  box-shadow: var(--shadow-sm);
}

.cert-item-name {
  font-size: 13px;
  font-weight: 600;
  color: #303133;
  text-align: center;
  margin-bottom: 4px;
  word-break: break-all;
}

.cert-item-type {
  font-size: 11px;
  color: #909399;
  text-align: center;
}

.current-cert {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 20px;
}

.cert-info {
  font-size: 13px;
  color: #606266;
  line-height: 1.8;
}

.cert-info div {
  margin-bottom: 6px;
}

.cert-info strong {
  color: #303133;
}

.stamp-preview {
  display: flex;
  justify-content: center;
  align-items: center;
  background: transparent;
  border-radius: 8px;
  padding: 15px;
  transition: all 0.3s;
}

.stamp-preview:hover {
  background: var(--bg-secondary);
}

.stamp-image {
  width: 80px;
  height: 80px;
  object-fit: contain;
  pointer-events: none;
}

/* 印章拖动时的样式 */
#yz {
  transition: none !important;
  pointer-events: none;
  z-index: 9999;
}

#yz.dragging {
  opacity: 0.9;
  cursor: none !important;
}

/* PDF Canvas 拖动时的鼠标样式 */
#pdf_canvas.stamping {
  cursor: none !important;
}

.download-section {
  margin-top: 20px;
}

.download-btn {
  width: 100%;
  height: 40px;
  font-size: 14px;
  border-radius: 8px;
}

.drawer-wrapper {
  height: 100%;
  overflow: hidden;
}

.left_content {
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 15px 10px;
}

.content_read_pdf {
  margin: auto;
  padding-bottom: 30px;
  padding-top: 20px;
  overflow: auto;
  text-align: center;
  white-space: nowrap;
}

.base_div {
  line-height: 50px;
}

.left_pdf_xx {
  border: 1px dashed #CCCCCC;
  width: 100%;
  margin-bottom: 15px;
  padding: 8px;
  cursor: pointer;
  transition: all 0.3s;
  border-radius: 4px;
}

.left_pdf_xx:hover {
  border: 1px solid var(--primary-color);
  background: var(--primary-light);
  transform: translateY(-2px);
  box-shadow: var(--shadow-sm);
}

.left_pdf_xx canvas {
  width: 100%;
  height: auto;
  display: block;
}

.page-num {
  text-align: center;
  margin-top: 8px;
  font-size: 13px;
  color: #606266;
}

.base_scrollbar::-webkit-scrollbar {
  width: 5px;
  height: 5px;
}

.base_scrollbar::-webkit-scrollbar-thumb {
  border-radius: 10px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0);
  background: var(--primary-color);
}

.base_scrollbar::-webkit-scrollbar-track {
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.01);
  border-radius: 10px;
  background: var(--bg-card);
}
</style>
