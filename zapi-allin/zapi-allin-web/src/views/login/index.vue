<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>知录Admin</h1>
        <p>现代化管理系统</p>
      </div>

      <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" placeholder="用户名" size="large">
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="密码" size="large" show-password>
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="captcha">
          <el-input v-model="loginForm.captcha" placeholder="请扫描下方二维码获取验证码" size="large" maxlength="4">
            <template #prefix>
              <el-icon><Key /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="handleLogin">
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-tips">
        <p>测试账号：admin / 123456</p>
      </div>

      <div class="qrcode-section">
        <div class="qrcode-divider">
          <span>关注公众号</span>
        </div>
        <div class="qrcode-wrapper">
          <img src="/gzh.jpg" alt="公众号二维码" class="qrcode-img" />
          <p class="qrcode-tip">扫码关注获取更多信息</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { Key } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: 'admin',
  password: '123456',
  captcha: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请扫描下方二维码获取验证码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    await loginFormRef.value.validate()
    loading.value = true

    const success = await userStore.login({
      username: loginForm.username,
      password: loginForm.password,
      captcha: loginForm.captcha
    })

    if (success) {
      router.push('/home')
    }
  } catch (error) {
    // 表单验证失败，不需要额外提示
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: var(--el-bg-color-page);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-card {
  background: var(--el-bg-color);
  border: 1px solid var(--el-border-color);
  border-radius: 16px;
  padding: 40px;
  width: 100%;
  max-width: 400px;
  box-shadow: var(--el-box-shadow);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;

  h1 {
    font-size: 28px;
    font-weight: 700;
    margin-bottom: 8px;
    color: var(--el-color-primary);
    display: inline-block;
  }

  p {
    color: var(--el-text-color-secondary);
    font-size: 16px;
  }
}

.login-form {
  margin-bottom: 24px;
}

.login-btn {
  width: 100%;
  height: 48px;
  border: none;
  font-size: 16px;
  font-weight: 600;
}

.login-tips {
  text-align: center;

  p {
    color: var(--el-text-color-placeholder);
    font-size: 14px;
    font-family: 'Monaco', 'Menlo', monospace;
  }
}

.qrcode-section {
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid var(--el-border-color);
}

.qrcode-divider {
  text-align: center;
  margin-bottom: 16px;

  span {
    color: var(--el-text-color-secondary);
    font-size: 14px;
    background: var(--el-bg-color);
    padding: 0 12px;
    position: relative;
  }
}

.qrcode-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.qrcode-img {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;

  &:hover {
    transform: scale(1.05);
  }
}

.qrcode-tip {
  margin-top: 8px;
  color: var(--el-text-color-placeholder);
  font-size: 12px;
}

@media (max-width: 480px) {
  .login-card {
    padding: 24px;
  }

  .login-header h1 {
    font-size: 24px;
  }
}
</style>
