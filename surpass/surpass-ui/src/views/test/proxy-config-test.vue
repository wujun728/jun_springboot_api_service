<template>
  <div class="test-container">
    <h2>代理配置组件测试</h2>
    
    <div class="test-section">
      <h3>选择协议类型：</h3>
      <el-select v-model="testProtocol" style="width: 200px; margin-bottom: 20px;">
        <el-option label="API Key" value="api_key" />
        <el-option label="OAuth2" value="oauth2" />
        <el-option label="JWT" value="jwt" />
        <el-option label="Basic Auth" value="basic_auth" />
        <el-option label="Bearer Token" value="bearer_token" />
        <el-option label="Digest Auth" value="digest_auth" />
        <el-option label="自定义" value="custom" />
      </el-select>
    </div>
    
    <div class="test-section">
      <h3>配置组件：</h3>
      <ProxyConfig
        v-model:config="testConfig"
        :protocol-type="testProtocol"
      />
    </div>
    
    <div class="test-section">
      <h3>生成的JSON配置：</h3>
      <el-input
        type="textarea"
        v-model="testConfig"
        :rows="8"
        readonly
      />
    </div>
    
    <div class="test-section">
      <h3>解析后的配置对象：</h3>
      <pre>{{ parsedConfig }}</pre>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import ProxyConfig from '@/components/ProxyConfig/index.vue'

const testProtocol = ref('api_key')
const testConfig = ref('')

const parsedConfig = computed(() => {
  try {
    return JSON.parse(testConfig.value || '{}')
  } catch (e) {
    return { error: '无效的JSON' }
  }
})
</script>

<style scoped>
.test-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.test-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  background: #fff;
}

h2 {
  margin-bottom: 30px;
  color: #303133;
}

h3 {
  margin-bottom: 15px;
  color: #606266;
}

pre {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  overflow: auto;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
}
</style>