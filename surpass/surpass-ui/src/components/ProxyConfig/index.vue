<template>
  <div class="proxy-config-container">
    <!-- 目标 URL 配置 (所有协议通用) -->
    <div class="config-form">
      <el-form :model="configData" label-width="120px">
        <el-form-item label="目标 URL" required>
          <el-input
            v-model="configData.targetUrl"
            placeholder="请输入目标 URL，如：https://api.example.com"
          />
        </el-form-item>
      </el-form>
    </div>

    <!-- API Key 配置 -->
    <div v-if="protocolType === 'api_key'" class="config-form">
      <el-form :model="configData" label-width="120px">
        <el-form-item label="API Key" required>
          <el-input
            v-model="configData.apiKey"
            placeholder="请输入API Key"
            show-password
            autocomplete="off"
          />
        </el-form-item>
        
        <el-form-item label="Key位置">
          <el-radio-group v-model="configData.keyLocation">
            <el-radio label="header">请求头</el-radio>
            <el-radio label="query">查询参数</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="Key名称" v-if="configData.keyLocation">
          <el-input
            v-model="configData.keyName"
            :placeholder="configData.keyLocation === 'header' ? 'X-API-Key' : 'api_key'"
          />
        </el-form-item>
        
        <el-form-item label="前缀">
          <el-input
            v-model="configData.prefix"
            placeholder="如：Bearer "
          />
        </el-form-item>
      </el-form>
    </div>
    
    <!-- OAuth2 配置 -->
    <div v-else-if="protocolType === 'oauth2'" class="config-form">
      <el-form :model="configData" label-width="120px">
        <el-form-item label="客户端ID" required>
          <el-input
            v-model="configData.clientId"
            placeholder="请输入Client ID"
          />
        </el-form-item>
        
        <el-form-item label="客户端密钥" required>
          <el-input
            v-model="configData.clientSecret"
            placeholder="请输入Client Secret"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="授权类型">
          <el-select v-model="configData.grantType" style="width: 100%">
            <el-option label="客户端凭证" value="client_credentials" />
            <el-option label="授权码" value="authorization_code" />
            <el-option label="密码模式" value="password" />
            <el-option label="刷新令牌" value="refresh_token" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="令牌端点" required>
          <el-input
            v-model="configData.tokenUrl"
            placeholder="https://example.com/oauth/token"
          />
        </el-form-item>
        
        <el-form-item label="授权端点" v-if="configData.grantType === 'authorization_code'">
          <el-input
            v-model="configData.authorizationUrl"
            placeholder="https://example.com/oauth/authorize"
          />
        </el-form-item>
        
        <el-form-item label="作用域">
          <el-input
            v-model="configData.scope"
            placeholder="如：read write"
          />
        </el-form-item>
        
        <el-form-item label="令牌位置">
          <el-radio-group v-model="configData.tokenLocation">
            <el-radio label="header">请求头</el-radio>
            <el-radio label="query">查询参数</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="令牌前缀">
          <el-input
            v-model="configData.tokenPrefix"
            placeholder="如：Bearer "
          />
        </el-form-item>
      </el-form>
    </div>
    
    <!-- JWT 配置 -->
    <div v-else-if="protocolType === 'jwt'" class="config-form">
      <el-form :model="configData" label-width="120px">
        <el-form-item label="JWT令牌" required>
          <el-input
            v-model="configData.token"
            type="textarea"
            :rows="3"
            placeholder="请输入JWT令牌"
          />
        </el-form-item>
        
        <el-form-item label="令牌位置">
          <el-radio-group v-model="configData.tokenLocation">
            <el-radio label="header">请求头</el-radio>
            <el-radio label="query">查询参数</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="令牌名称" v-if="configData.tokenLocation">
          <el-input
            v-model="configData.tokenName"
            :placeholder="configData.tokenLocation === 'header' ? 'Authorization' : 'token'"
          />
        </el-form-item>
        
        <el-form-item label="令牌前缀">
          <el-input
            v-model="configData.tokenPrefix"
            placeholder="如：Bearer "
          />
        </el-form-item>
        
        <el-form-item label="密钥" v-if="!configData.token">
          <el-input
            v-model="configData.secret"
            placeholder="用于生成JWT的密钥"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="算法">
          <el-select v-model="configData.algorithm" style="width: 100%">
            <el-option label="HS256" value="HS256" />
            <el-option label="HS384" value="HS384" />
            <el-option label="HS512" value="HS512" />
            <el-option label="RS256" value="RS256" />
            <el-option label="RS384" value="RS384" />
            <el-option label="RS512" value="RS512" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="过期时间(秒)">
          <el-input-number
            v-model="configData.expiresIn"
            :min="60"
            :step="60"
            placeholder="3600"
          />
        </el-form-item>
      </el-form>
    </div>
    
    <!-- Basic Auth 配置 -->
    <div v-else-if="protocolType === 'basic_auth'" class="config-form">
      <el-form :model="configData" label-width="120px">
        <el-form-item label="用户名" required>
          <el-input
            v-model="configData.username"
            placeholder="请输入用户名"
          />
        </el-form-item>
        
        <el-form-item label="密码" required>
          <el-input
            v-model="configData.password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="编码方式">
          <el-radio-group v-model="configData.encoding">
            <el-radio label="base64">Base64</el-radio>
            <el-radio label="plain">明文</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- Bearer Token 配置 -->
    <div v-else-if="protocolType === 'bearer_token'" class="config-form">
      <el-form :model="configData" label-width="120px">
        <el-form-item label="Bearer令牌" required>
          <el-input
            v-model="configData.token"
            type="textarea"
            :rows="3"
            placeholder="请输入Bearer令牌"
          />
        </el-form-item>
        
        <el-form-item label="令牌位置">
          <el-radio-group v-model="configData.tokenLocation">
            <el-radio label="header">请求头</el-radio>
            <el-radio label="query">查询参数</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="令牌名称" v-if="configData.tokenLocation">
          <el-input
            v-model="configData.tokenName"
            :placeholder="configData.tokenLocation === 'header' ? 'Authorization' : 'token'"
          />
        </el-form-item>
      </el-form>
    </div>
    
    <!-- Digest Auth 配置 -->
    <div v-else-if="protocolType === 'digest_auth'" class="config-form">
      <el-form :model="configData" label-width="120px">
        <el-form-item label="用户名" required>
          <el-input
            v-model="configData.username"
            placeholder="请输入用户名"
          />
        </el-form-item>
        
        <el-form-item label="密码" required>
          <el-input
            v-model="configData.password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
        
        <el-form-item label="领域(Realm)">
          <el-input
            v-model="configData.realm"
            placeholder="认证领域"
          />
        </el-form-item>
        
        <el-form-item label="算法">
          <el-select v-model="configData.algorithm" style="width: 100%">
            <el-option label="MD5" value="MD5" />
            <el-option label="SHA-256" value="SHA-256" />
            <el-option label="SHA-512" value="SHA-512" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="QOP">
          <el-select v-model="configData.qop" style="width: 100%">
            <el-option label="auth" value="auth" />
            <el-option label="auth-int" value="auth-int" />
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 自定义配置 -->
    <div v-else class="config-form" style="width: 100%">
      <el-input
          type="textarea"
          v-model="jsonConfig"
          :rows="6"
          placeholder='自定义协议支持：{"key":"value"}'
          @input="handleJsonInput"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'

const props = defineProps({
  protocolType: {
    type: String,
    required: true
  },
  config: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:config'])

// 各协议的默认配置
const defaultConfigs = {
  api_key: {
    targetUrl: '',
    apiKey: '',
    keyLocation: 'header',
    keyName: '',
    prefix: ''
  },
  oauth2: {
    targetUrl: '',
    clientId: '',
    clientSecret: '',
    grantType: 'client_credentials',
    tokenUrl: '',
    authorizationUrl: '',
    scope: '',
    tokenLocation: 'header',
    tokenPrefix: 'Bearer '
  },
  jwt: {
    targetUrl: '',
    token: '',
    tokenLocation: 'header',
    tokenName: '',
    tokenPrefix: 'Bearer ',
    secret: '',
    algorithm: 'HS256',
    expiresIn: 3600
  },
  basic_auth: {
    targetUrl: '',
    username: '',
    password: '',
    encoding: 'base64'
  },
  bearer_token: {
    targetUrl: '',
    token: '',
    tokenLocation: 'header',
    tokenName: ''
  },
  digest_auth: {
    targetUrl: '',
    username: '',
    password: '',
    realm: '',
    algorithm: 'MD5',
    qop: 'auth'
  }
}

// 当前配置数据
const configData = ref({})
const jsonConfig = ref('')

// 初始化配置
function initConfig() {
  try {
    if (props.config && props.config.trim() !== '') {
      const parsed = JSON.parse(props.config)
      // 合并默认配置和解析的配置
      const defaultConfig = defaultConfigs[props.protocolType] || {}
      configData.value = { ...defaultConfig, ...parsed }
    } else {
      configData.value = { ...defaultConfigs[props.protocolType] || {} }
    }
  } catch (e) {
    console.warn('解析配置失败:', e)
    configData.value = { ...defaultConfigs[props.protocolType] || {} }
  }
}

// 监听协议类型变化
watch(() => props.protocolType, () => {
  initConfig()
}, { immediate: true })

// 监听外部配置变化
watch(() => props.config, () => {
  initConfig()
})

// 监听配置数据变化，生成JSON
watch(configData, (newVal) => {
  if (props.protocolType && defaultConfigs[props.protocolType]) {
    const json = JSON.stringify(newVal, null, 2)
    emit('update:config', json)
  }
}, { deep: true, immediate: true })

// 处理JSON输入
function handleJsonInput() {
  if (!defaultConfigs[props.protocolType]) {
    emit('update:config', jsonConfig.value)
  }
}

// 监听外部配置变化（自定义协议）
watch(() => props.config, (newVal) => {
  if (!defaultConfigs[props.protocolType] && newVal !== jsonConfig.value) {
    jsonConfig.value = newVal || ''
  }
}, { immediate: true })

// 计算属性：是否为自定义协议
const isCustomProtocol = computed(() => {
  return !defaultConfigs[props.protocolType]
})
</script>

<style scoped>
.proxy-config-container {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
  width: 100%;
}

.config-form {
  max-width: 100%;
}

.el-form-item {
  margin-bottom: 18px;
}

.el-form-item:last-child {
  margin-bottom: 0;
}

.el-input,
.el-select,
.el-textarea {
  width: 100%;
}

.el-radio-group {
  display: flex;
  gap: 20px;
}

.el-alert {
  margin-bottom: 16px;
}
</style>