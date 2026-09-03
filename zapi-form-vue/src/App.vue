<script setup lang="ts">
import { RouterView, useRoute } from 'vue-router'
import { ref, computed } from 'vue'

const route = useRoute()
const activeIndex = computed(() => {
  switch (route.name) {
    case 'home': return '/'
    case 'form-designer': return '/designer'
    case 'form-preview': return '/preview'
    case 'list-designer': return '/list-designer'
    case 'list-preview': return '/list-preview'
    case 'workflow-designer': return '/workflow-designer'
    case 'workflow-preview': return '/workflow-preview'
    case 'about': return '/about'
    default: return '/'
  }
})
</script>

<template>
  <el-container class="app-container">
    <el-header class="app-header" v-if="route.name !== 'form-designer'">
      <div class="logo-section">
        <el-icon><Tools /></el-icon>
        <span class="logo-text">ZAPI设计器</span>
      </div>
      <el-menu
        :default-active="activeIndex"
        mode="horizontal"
        router
        class="nav-menu"
      >
        <el-menu-item index="/">首页</el-menu-item>
        <el-menu-item index="/designer">表单设计</el-menu-item>
        <el-menu-item index="/preview">表单预览</el-menu-item>
        <el-menu-item index="/list-designer">列表设计</el-menu-item>
        <el-menu-item index="/list-preview">列表预览</el-menu-item>
        <el-menu-item index="/workflow-designer">流程设计</el-menu-item>
        <el-menu-item index="/workflow-preview">流程预览</el-menu-item>
        <el-menu-item index="/about">关于</el-menu-item>
      </el-menu>
    </el-header>
    
    <el-main class="app-main" :class="{ 'no-header': route.name === 'form-designer' }">
      <RouterView />
    </el-main>
  </el-container>
</template>

<style scoped>
.app-container {
  width: 100vw;
  height: 100vh;
}

.app-header {
  display: flex;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.logo-section {
  display: flex;
  align-items: center;
  margin-right: 40px;
  color: #409eff;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  margin-left: 8px;
}

.nav-menu {
  flex: 1;
  border: none;
}

.app-main {
  padding: 0;
  height: calc(100vh - 60px);
  overflow: auto;
}

.app-main.no-header {
  height: 100vh;
}
</style>
