<template>
  <div class="home-container">
    <!-- Hero 区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-badge">
          <span class="badge-icon">✨</span>
          <span>低代码工作流引擎</span>
        </div>
        <h1 class="hero-title">知录Admin</h1>
        <p class="hero-subtitle">超级简单的自研工作流引擎，告别繁琐的 BPMN 配置</p>
        <p class="hero-desc">
          采用 JSON 交互方式实现流程定义，支持审批节点、条件分支节点等流程元素，
          打造现代化低代码或零代码平台的基础模型
        </p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="goToDemo" class="btn-primary">
            <el-icon><VideoPlay /></el-icon>
            <span>开始体验</span>
          </el-button>
          <el-button size="large" @click="goToDoc" class="btn-secondary">
            <el-icon><Document /></el-icon>
            <span>低代码引擎</span>
          </el-button>
        </div>
      </div>
      <div class="hero-visual">
        <el-carousel :interval="4000" height="350px" :autoplay="true" arrow="hover">
          <el-carousel-item v-for="(image, index) in carouselImages" :key="index">
            <div class="carousel-image-wrapper">
              <img :src="image.src" :alt="image.alt" class="carousel-img" />
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </section>

    <!-- 核心功能区域 -->
    <section class="features-section">
      <div class="section-header">
        <h2 class="section-title">核心功能</h2>
        <p class="section-desc">一站式低代码开发平台，助力企业数字化转型</p>
      </div>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon workflow">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
            </svg>
          </div>
          <h3 class="feature-title">工作流引擎</h3>
          <p class="feature-desc">支持审批节点、条件分支、会签或签，JSON 交互告别 BPMN 繁琐操作</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon form">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="18" height="18" rx="2"/>
              <path d="M9 9h6M9 13h6M9 17h4"/>
            </svg>
          </div>
          <h3 class="feature-title">表单设计器</h3>
          <p class="feature-desc">可视化拖拽设计表单，丰富的组件库，支持自定义表单对接工作流</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon ai">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="3"/>
              <path d="M12 1v4M12 19v4M4.22 4.22l2.83 2.83M16.95 16.95l2.83 2.83M1 12h4M19 12h4M4.22 19.78l2.83-2.83M16.95 7.05l2.83-2.83"/>
            </svg>
          </div>
          <h3 class="feature-title">动作执行引擎</h3>
          <p class="feature-desc">可视化编排事件执行对接表单，表格</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon sign">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
              <path d="M9 12l2 2 4-4"/>
            </svg>
          </div>
          <h3 class="feature-title">电子签章</h3>
          <p class="feature-desc">支持 SM2/RSA 数字证书，PDF 电子盖章，完整的签署记录与验证</p>
        </div>

      </div>
    </section>

    <!-- 系统统计区域 -->
    <section class="stats-section">
      <div class="section-header">
        <h2 class="section-title">系统数据</h2>
        <p class="section-desc">实时统计，见证平台成长</p>
      </div>
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon primary">
            <el-icon><DataAnalysis /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ totalStats.loginCount }}</div>
            <div class="stat-label">累计登录次数</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon success">
            <el-icon><Mouse /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ totalStats.clickCount }}</div>
            <div class="stat-label">累计点击次数</div>
          </div>
        </div>
      </div>
      <!-- 趋势图表 -->
      <div class="chart-card">
        <h3 class="chart-title">最近7天数据趋势</h3>
        <div ref="chartRef" class="chart-container"></div>
      </div>
    </section>

    <!-- 联系我们区域 -->
    <section class="contact-section">
      <div class="section-header">
        <h2 class="section-title">加入我们</h2>
        <p class="section-desc">关注公众号获取最新动态，加作者好友拉你进群聊一起交流学习</p>
      </div>
      <div class="contact-grid">
        <div class="contact-card">
          <div class="qr-wrapper">
            <el-image
              src="/gzh.jpg"
              alt="公众号二维码"
              class="qr-img"
              :preview-src-list="['/gzh.jpg']"
              fit="contain"
              preview-teleported
            />
          </div>
          <div class="contact-info">
            <h3 class="contact-title">关注公众号</h3>
            <p class="contact-desc">扫码关注，获取最新技术文章和产品动态</p>
          </div>
        </div>
        <div class="contact-card">
          <div class="qr-wrapper">
            <el-image
              src="/vx/vx1.jpg"
              alt="加我拉你进群"
              class="qr-img"
              :preview-src-list="['/vx/vx1.jpg']"
              fit="contain"
              preview-teleported
            />
          </div>
          <div class="contact-info">
            <h3 class="contact-title">作者微信</h3>
            <p class="contact-desc">加我好友，拉你进群</p>
          </div>
        </div>
        <div class="contact-card link-card">
          <div class="link-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"/>
            </svg>
          </div>
          <div class="contact-info">
            <h3 class="contact-title">建议反馈</h3>
            <p class="contact-desc">有任何建议或问题，欢迎提交反馈</p>
            <a href="https://www.kdocs.cn/l/ciigs3muafAP" target="_blank" class="contact-link">
              点击提交建议
              <el-icon><ArrowRight /></el-icon>
            </a>
          </div>
        </div>
      </div>
    </section>

    <!-- 技术栈区域 -->
    <section class="tech-section">
      <div class="section-header">
        <h2 class="section-title">技术架构</h2>
        <p class="section-desc">现代化技术栈，稳定可靠</p>
      </div>
      <div class="tech-grid">
        <div class="tech-item vue">
          <div class="tech-icon">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M2,3H5.5L12,15L18.5,3H22L12,21L2,3M6.5,3H9.5L12,7.58L14.5,3H17.5L12,13.08L6.5,3Z"/>
            </svg>
          </div>
          <span class="tech-name">Vue 3</span>
          <span class="tech-desc">前端框架</span>
        </div>
        <div class="tech-item spring">
          <div class="tech-icon">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M20.205 16.392c-2.469 3.289-7.741 2.179-11.122 2.338 0 0-.599.034-1.201.133 0 0 .228-.097.519-.198 2.374-.821 3.496-.986 4.939-1.727 2.71-1.388 5.408-4.413 5.957-7.555-1.032 3.022-4.17 5.623-7.027 6.679-1.955.722-5.492 1.424-5.493 1.424a5.28 5.28 0 0 1-.143-.076c-2.405-1.17-2.475-6.38 1.894-8.059 1.916-.736 3.747-.332 5.818-.825 2.208-.525 4.766-2.18 5.805-4.344 1.165 3.458 2.565 8.866.054 12.21zm.042-13.28a9.212 9.212 0 0 1-1.065 1.89 9.982 9.982 0 0 0-7.167-3.031C6.492 1.971 2 6.463 2 11.985a9.983 9.983 0 0 0 3.205 7.334l.22.194a.856.856 0 1 1 .001.001l.149.132A9.96 9.96 0 0 0 12.015 22c5.278 0 9.613-4.108 9.984-9.292.274-2.539-.476-5.763-1.752-9.596"/>
            </svg>
          </div>
          <span class="tech-name">Spring Boot</span>
          <span class="tech-desc">后端框架</span>
        </div>
        <div class="tech-item mybatis">
          <div class="tech-icon">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M12,2C6.48,2 2,6.48 2,12C2,17.52 6.48,22 12,22C17.52,22 22,17.52 22,12C22,6.48 17.52,2 12,2M12,20C7.59,20 4,16.41 4,12C4,7.59 7.59,4 12,4C16.41,4 20,7.59 20,12C20,16.41 16.41,20 12,20M16.59,7.58L10,14.17L7.41,11.59L6,13L10,17L18,9L16.59,7.58Z"/>
            </svg>
          </div>
          <span class="tech-name">MyBatis Plus</span>
          <span class="tech-desc">ORM 框架</span>
        </div>
        <div class="tech-item mysql">
          <div class="tech-icon">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M5.5,2C4.67,2 4,2.67 4,3.5V20.5C4,21.33 4.67,22 5.5,22H18.5C19.33,22 20,21.33 20,20.5V8L14,2H5.5M5.5,4H13V9H18V20H5.5V4M7,13V15H17V13H7M7,17V19H14V17H7Z"/>
            </svg>
          </div>
          <span class="tech-name">MySQL 8</span>
          <span class="tech-desc">数据库</span>
        </div>
        <div class="tech-item element">
          <div class="tech-icon">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M12,2L2,7V17L12,22L22,17V7M12,4.18L19.09,8L12,11.82L4.91,8M4,10.18L11,14V19.82L4,16M20,10.18V16L13,19.82V14"/>
            </svg>
          </div>
          <span class="tech-name">Element Plus</span>
          <span class="tech-desc">UI 组件库</span>
        </div>
        <div class="tech-item echarts">
          <div class="tech-icon">
            <svg viewBox="0 0 24 24" fill="currentColor">
              <path d="M3,3V21H21V19H5V3M9,17H7V10H9M13,17H11V7H13M17,17H15V13H17M21,17H19V4H21V17Z"/>
            </svg>
          </div>
          <span class="tech-name">ECharts</span>
          <span class="tech-desc">图表库</span>
        </div>
      </div>
    </section>

    <!-- Footer -->
    <footer class="footer-section">
      <p class="footer-text">知录Admin © 2025 | MIT License</p>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { VideoPlay, Document, DataAnalysis, Mouse, ArrowRight } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { statisticsApi } from '@/api'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'

const router = useRouter()
const chartRef = ref(null)
let chartInstance = null

// 轮播图图片列表
const carouselImages = ref([
  { src: '/home/img_1.png', alt: '系统功能展示1' },
  { src: '/home/img_2.png', alt: '系统功能展示2' },
  { src: '/home/img_3.png', alt: '系统功能展示3' },
  { src: '/home/img_4.png', alt: '系统功能展示4' }
])

// 统计数据
const totalStats = ref({
  loginCount: 0,
  clickCount: 0
})

// 跳转方法
const goToDemo = () => {
  router.push('/home/taskcenter/launch')
}

const goToDoc = () => {
  router.push('/home/app/workbench/application')
 // window.open('https://www.kdocs.cn/l/ciigs3muafAP', '_blank')
}

// 加载统计数据
const loadStatistics = async () => {
  try {
    const totalRes = await statisticsApi.getTotalStatistics()
    if (totalRes.code === 200) {
      totalStats.value = totalRes.data || { loginCount: 0, clickCount: 0 }
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 加载最近7天的数据
const loadRecentData = async () => {
  try {
    const dates = []
    const loginData = []
    const clickData = []

    for (let i = 6; i >= 0; i--) {
      const date = new Date()
      date.setDate(date.getDate() - i)
      const dateStr = date.toISOString().split('T')[0]
      dates.push(`${date.getMonth() + 1}/${date.getDate()}`)

      const res = await statisticsApi.getStatisticsByDate(dateStr)
      if (res.code === 200 && res.data) {
        loginData.push(res.data.loginCount || 0)
        clickData.push(res.data.clickCount || 0)
      } else {
        loginData.push(0)
        clickData.push(0)
      }
    }
    initChart(dates, loginData, clickData)
  } catch (error) {
    console.error('加载趋势数据失败:', error)
  }
}

// 初始化图表
const initChart = (dates, loginData, clickData) => {
  if (!chartRef.value) return
  if (chartInstance) chartInstance.dispose()

  chartInstance = echarts.init(chartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      backgroundColor: 'rgba(255, 255, 255, 0.95)',
      borderColor: '#e4e7ed',
      borderWidth: 1,
      textStyle: { color: '#606266' }
    },
    legend: {
      data: ['登录次数', '点击次数'],
      top: 10,
      textStyle: { color: '#606266' }
    },
    grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLine: { lineStyle: { color: '#e4e7ed' } },
      axisLabel: { color: '#909399' }
    },
    yAxis: {
      type: 'value',
      axisLine: { show: false },
      axisTick: { show: false },
      axisLabel: { color: '#909399' },
      splitLine: { lineStyle: { color: '#f0f0f0', type: 'dashed' } }
    },
    series: [
      {
        name: '登录次数',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 6,
        data: loginData,
        lineStyle: { width: 3, color: '#2563EB' },
        itemStyle: { color: '#2563EB', borderWidth: 2, borderColor: '#fff' },
        areaStyle: {
          color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [{ offset: 0, color: 'rgba(37, 99, 235, 0.3)' }, { offset: 1, color: 'rgba(37, 99, 235, 0.05)' }]
          }
        }
      },
      {
        name: '点击次数',
        type: 'line',
        smooth: true,
        symbol: 'circle',
        symbolSize: 6,
        data: clickData,
        lineStyle: { width: 3, color: '#10B981' },
        itemStyle: { color: '#10B981', borderWidth: 2, borderColor: '#fff' },
        areaStyle: {
          color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
            colorStops: [{ offset: 0, color: 'rgba(16, 185, 129, 0.3)' }, { offset: 1, color: 'rgba(16, 185, 129, 0.05)' }]
          }
        }
      }
    ]
  }
  chartInstance.setOption(option)
  window.addEventListener('resize', handleResize)
}

const handleResize = () => {
  if (chartInstance) chartInstance.resize()
}

onMounted(async () => {
  loadStatistics()
  setInterval(loadStatistics, 30000)
  await nextTick()
  loadRecentData()
})

onUnmounted(() => {
  if (chartInstance) {
    window.removeEventListener('resize', handleResize)
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>

<style lang="scss" scoped>
.home-container {
  min-height: 100vh;
  background: var(--bg-primary);
}

// Hero 区域
.hero-section {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 60px;
  padding: 60px 48px;
  max-width: 1400px;
  margin: 0 auto;

  @media (max-width: 1024px) {
    flex-direction: column;
    text-align: center;
    padding: 40px 24px;
    gap: 40px;
  }
}

.hero-content {
  flex: 1;
  max-width: 560px;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 16px;
  background: var(--primary-color-light);
  border-radius: 20px;
  font-size: 14px;
  color: var(--primary-color);
  margin-bottom: 20px;
}

.hero-title {
  font-size: 48px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 16px 0;
  line-height: 1.2;
}

.hero-subtitle {
  font-size: 20px;
  color: var(--text-secondary);
  margin: 0 0 12px 0;
  font-weight: 500;
}

.hero-desc {
  font-size: 16px;
  color: var(--text-tertiary);
  line-height: 1.7;
  margin: 0 0 32px 0;
}

.hero-actions {
  display: flex;
  gap: 16px;

  @media (max-width: 1024px) {
    justify-content: center;
  }

  .btn-primary {
    background: var(--primary-color);
    border-color: var(--primary-color);
    padding: 12px 28px;
    height: auto;
    font-size: 16px;
    border-radius: 8px;

    &:hover {
      background: var(--primary-color-hover);
      border-color: var(--primary-color-hover);
    }
  }

  .btn-secondary {
    padding: 12px 28px;
    height: auto;
    font-size: 16px;
    border-radius: 8px;
    border-color: var(--border-color);
    color: var(--text-secondary);

    &:hover {
      border-color: var(--primary-color);
      color: var(--primary-color);
    }
  }
}

.hero-visual {
  flex: 1;
  max-width: 700px;

  :deep(.el-carousel) {
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 8px 24px rgba(37, 99, 235, 0.12);
  }

  :deep(.el-carousel__item) {
    border-radius: 16px;
    overflow: hidden;
  }

  :deep(.el-carousel__indicators) {
    bottom: 20px;
  }

  :deep(.el-carousel__indicator) {
    button {
      width: 30px;
      height: 4px;
      border-radius: 2px;
      background-color: rgba(255, 255, 255, 0.4);
      transition: all 0.3s;
    }

    &.is-active button {
      width: 40px;
      background-color: #fff;
    }
  }

  :deep(.el-carousel__arrow) {
    width: 40px;
    height: 40px;
    background-color: rgba(255, 255, 255, 0.9);
    color: #2563EB;
    font-size: 16px;

    &:hover {
      background-color: #fff;
    }
  }
}

.carousel-image-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-card);
  padding: 20px;
}

.carousel-img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  border-radius: 8px;
}

// 通用 Section 样式
.section-header {
  text-align: center;
  margin-bottom: 48px;
}

.section-title {
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 12px 0;
}

.section-desc {
  font-size: 16px;
  color: var(--text-tertiary);
  margin: 0;
}

// 功能区域
.features-section {
  padding: 80px 48px;
  max-width: 1400px;
  margin: 0 auto;

  @media (max-width: 768px) {
    padding: 60px 24px;
  }
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;

  @media (max-width: 1200px) {
    grid-template-columns: repeat(2, 1fr);
  }

  @media (max-width: 640px) {
    grid-template-columns: 1fr;
  }
}

.feature-card {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 32px 24px;
  text-align: center;
  transition: all 0.3s ease;
  border: 2px solid var(--border-color);
  cursor: pointer;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, transparent, var(--primary-color), transparent);
    transform: translateX(-100%);
    transition: transform 0.6s ease;
  }

  &:hover {
    transform: translateY(-8px);
    box-shadow: var(--shadow-lg);
    border-color: var(--primary-color);

    &::before {
      transform: translateX(100%);
    }

    .feature-icon {
      transform: scale(1.1) rotate(5deg);
    }
  }
}

.feature-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

  svg {
    width: 32px;
    height: 32px;
  }

  &.workflow {
    background: linear-gradient(135deg, #EEF2FF 0%, #E0E7FF 100%);
    color: #4F46E5;
  }

  &.form {
    background: linear-gradient(135deg, #ECFDF5 0%, #D1FAE5 100%);
    color: #059669;
  }

  &.sign {
    background: linear-gradient(135deg, #FEF3C7 0%, #FDE68A 100%);
    color: #D97706;
  }

  &.ai {
    background: linear-gradient(135deg, #FCE7F3 0%, #FBCFE8 100%);
    color: #DB2777;
  }
}

.feature-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 12px 0;
}

.feature-desc {
  font-size: 14px;
  color: var(--text-tertiary);
  line-height: 1.6;
  margin: 0;
}

// 统计区域
.stats-section {
  padding: 80px 48px;
  max-width: 1400px;
  margin: 0 auto;
  background: var(--bg-card);
  border-radius: 24px;

  @media (max-width: 768px) {
    padding: 60px 24px;
    margin-left: 24px;
    margin-right: 24px;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 24px;
  margin-bottom: 40px;

  @media (max-width: 640px) {
    grid-template-columns: 1fr;
  }
}

.stat-card {
  background: var(--bg-secondary);
  border-radius: 16px;
  padding: 28px;
  display: flex;
  align-items: center;
  gap: 20px;
  border: 1px solid var(--border-color);
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;

  &.primary {
    background: var(--primary-color-light);
    color: var(--primary-color);
  }

  &.success {
    background: var(--success-color-light);
    color: var(--success-color);
  }
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 36px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: var(--text-tertiary);
}

.chart-card {
  background: var(--bg-secondary);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid var(--border-color);
}

.chart-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 20px 0;
}

.chart-container {
  width: 100%;
  height: 300px;
}

// 联系区域
.contact-section {
  padding: 80px 48px;
  max-width: 1400px;
  margin: 0 auto;

  @media (max-width: 768px) {
    padding: 60px 24px;
  }
}

.contact-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;

  @media (max-width: 900px) {
    grid-template-columns: 1fr;
  }
}

.contact-card {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 32px;
  text-align: center;
  border: 2px solid var(--border-color);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, transparent, var(--primary-color), transparent);
    transform: translateX(-100%);
    transition: transform 0.6s ease;
  }

  &:hover {
    transform: translateY(-8px);
    box-shadow: var(--shadow-lg);
    border-color: var(--primary-color);

    &::before {
      transform: translateX(100%);
    }

    .qr-wrapper {
      transform: scale(1.05);
    }

    .link-icon {
      transform: scale(1.1) rotate(5deg);
    }
  }

  &.link-card {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
}

.qr-wrapper {
  width: 160px;
  height: 160px;
  margin: 0 auto 20px;
  border-radius: 12px;
  overflow: hidden;
  background: var(--bg-secondary);
  padding: 8px;
  cursor: pointer;
  transition: all 0.3s ease;

  :deep(.el-image) {
    width: 100%;
    height: 100%;
    border-radius: 8px;
  }
}

.qr-img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.link-icon {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  background: var(--primary-color-light);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);

  svg {
    width: 40px;
    height: 40px;
    color: var(--primary-color);
  }
}

.contact-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0 0 8px 0;
}

.contact-desc {
  font-size: 14px;
  color: var(--text-tertiary);
  margin: 0;
  line-height: 1.6;
}

.contact-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-top: 16px;
  color: var(--primary-color);
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  transition: all 0.2s;

  &:hover {
    color: var(--primary-color-hover);
    gap: 8px;
  }
}

// 技术栈区域
.tech-section {
  padding: 80px 48px;
  max-width: 1400px;
  margin: 0 auto;

  @media (max-width: 768px) {
    padding: 60px 24px;
  }
}

.tech-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
}

.tech-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  width: 180px;
  height: 180px;
  padding: 24px 20px;
  background: var(--bg-card);
  border-radius: 16px;
  border: 2px solid var(--border-color);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  flex-shrink: 0;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, transparent, var(--primary-color), transparent);
    transform: translateX(-100%);
    transition: transform 0.6s ease;
  }

  &:hover {
    transform: translateY(-8px);
    border-color: var(--primary-color);
    box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);

    &::before {
      transform: translateX(100%);
    }

    .tech-icon {
      transform: scale(1.1) rotate(5deg);
    }
  }

  // 不同技术栈的主题色
  &.vue {
    .tech-icon {
      background: linear-gradient(135deg, #42b883 0%, #35495e 100%);
    }
  }

  &.spring {
    .tech-icon {
      background: linear-gradient(135deg, #6db33f 0%, #4e8b2f 100%);
    }
  }

  &.mybatis {
    .tech-icon {
      background: linear-gradient(135deg, #e74c3c 0%, #c0392b 100%);
    }
  }

  &.mysql {
    .tech-icon {
      background: linear-gradient(135deg, #00758f 0%, #005a6f 100%);
    }
  }

  &.element {
    .tech-icon {
      background: linear-gradient(135deg, #409eff 0%, #2b7fd6 100%);
    }
  }

  &.echarts {
    .tech-icon {
      background: linear-gradient(135deg, #5470c6 0%, #3d5aa8 100%);
    }
  }
}

.tech-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);

  svg {
    width: 32px;
    height: 32px;
  }
}

.tech-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0;
  text-align: center;
}

.tech-desc {
  font-size: 13px;
  color: var(--text-tertiary);
  text-align: center;
}

// Footer
.footer-section {
  padding: 32px 48px;
  text-align: center;
  border-top: 1px solid var(--border-color);
  background: var(--bg-card);
}

.footer-text {
  font-size: 14px;
  color: var(--text-tertiary);
  margin: 0;
}
</style>
