<template>
  <div class="dataset-index">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">数据集管理</h1>
      </div>
    </div>

    <div class="dataset-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="dataset-card" @click="navigateTo('/home/dataset/manage')">
            <div class="card-content">
              <el-icon class="card-icon"><DataAnalysis /></el-icon>
              <h3>数据集管理</h3>
              <p>创建和管理数据集，支持数据源连接和表选择</p>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="dataset-card" @click="navigateTo('/home/dataset/table')">
            <div class="card-content">
              <el-icon class="card-icon"><Grid /></el-icon>
              <h3>数据表管理</h3>
              <p>管理数据表结构，编辑表字段和属性</p>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="dataset-card" @click="navigateTo('/home/dataset/source')">
            <div class="card-content">
              <el-icon class="card-icon"><Link /></el-icon>
              <h3>数据源管理</h3>
              <p>配置和管理数据库连接，支持多种数据源</p>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="dataset-card" @click="navigateTo('/home/dataset/export')">
            <div class="card-content">
              <el-icon class="card-icon"><Download /></el-icon>
              <h3>数据源导出</h3>
              <p>导出数据源结构和数据，支持多种格式</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { DataAnalysis, Grid, Link, Download } from '@element-plus/icons-vue'

const router = useRouter()

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.dataset-index {
  padding: 20px;
}

.dataset-cards {
  margin-top: 20px;
}

.dataset-card {
  cursor: pointer;
  transition: all 0.3s ease;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dataset-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.card-content {
  text-align: center;
  padding: 20px;
}

.card-icon {
  font-size: 48px;
  color: var(--el-color-primary);
  margin-bottom: 16px;
}

.card-content h3 {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 12px 0;
}

.card-content p {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin: 0;
  line-height: 1.5;
}
</style> 