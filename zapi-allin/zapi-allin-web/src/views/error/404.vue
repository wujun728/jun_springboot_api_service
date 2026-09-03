<template>
  <div class="error-page">
    <div class="error-content">
      <div class="error-icon">
        <el-icon size="120" color="#909399">
          <Warning />
        </el-icon>
      </div>
      <h1 class="error-title">404</h1>
      <p class="error-message">抱歉，您访问的页面不存在</p>
      <div class="error-actions">
        <el-button type="primary" @click="goHome">
          <el-icon><House /></el-icon>
          返回首页
        </el-button>
        <el-button @click="goBack">
          <el-icon><Back /></el-icon>
          返回上页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { Warning, House, Back } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/home')
}

const goBack = () => {
  router.go(-1)
}
</script>

<style scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: var(--el-bg-color-page);
}

.error-content {
  text-align: center;
  padding: 40px;
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
  max-width: 500px;
  width: 100%;
}

.error-icon {
  margin-bottom: 24px;
}

.error-title {
  font-size: 72px;
  font-weight: 700;
  color: var(--el-text-color-primary);
  margin: 0 0 16px 0;
  line-height: 1;
}

.error-message {
  font-size: 18px;
  color: var(--el-text-color-regular);
  margin: 0 0 32px 0;
  line-height: 1.5;
}

.error-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.error-actions .el-button {
  padding: 12px 24px;
  font-size: 16px;
  border-radius: 8px;
}

.error-actions .el-button .el-icon {
  margin-right: 8px;
}
</style> 