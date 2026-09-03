<template>
  <div class="login-container">
    <!-- 左侧装饰区域 -->
    <div class="login-decoration">
      <div class="decoration-content">
        <div class="brand-info">
          <img :src="staticAppInfo.logo" alt="Logo" class="brand-logo"/>
          <h1 class="brand-title">{{ t('appTitle') }}</h1>
          <p class="brand-slogan">安全 · 高效 · 可靠</p>
        </div>
        <div class="decoration-elements">
          <div class="element element-1"></div>
          <div class="element element-2"></div>
          <div class="element element-3"></div>
        </div>
      </div>
    </div>

    <!-- 右侧登录表单区域 -->
    <div class="login-main">
      <div class="login-card">
        <div class="card-header">
          <h2 class="login-title">欢迎登录</h2>
          <p class="login-subtitle">请输入您的账户信息</p>
        </div>

        <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
          <el-form-item prop="username">
            <div class="input-wrapper">
              <el-icon class="input-icon">
                <UserFilled/>
              </el-icon>
              <el-input
                  v-model="loginForm.username"
                  type="text"
                  auto-complete="off"
                  :placeholder="t('login.textUsername')"
                  class="custom-input"
              />
            </div>
          </el-form-item>

          <el-form-item prop="password">
            <div class="input-wrapper">
              <svg-icon icon-class="password2" class="input-icon"/>
              <el-input
                  v-model="loginForm.password"
                  type="password"
                  auto-complete="off"
                  show-password
                  :placeholder="t('login.textPassword')"
                  @keyup.enter="handleLogin"
                  class="custom-input"
              />
            </div>
          </el-form-item>

          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="captcha-wrapper">
              <div class="input-wrapper captcha-input">
                <svg-icon icon-class="validCode2" class="input-icon"/>
                <el-input
                    v-model="loginForm.captcha"
                    auto-complete="off"
                    :placeholder="t('login.textCaptcha')"
                    @keyup.enter="handleLogin"
                    class="custom-input"
                />
              </div>
              <div class="captcha-image">
                <img :src="codeUrl" @click="getCode" class="captcha-img" alt="验证码"/>
              </div>
            </div>
          </el-form-item>

          <el-form-item class="submit-item">
            <el-button
                class="login-button"
                :loading="loading"
                @click.prevent="handleLogin">
              <span v-if="!loading">{{ t("login.login") }}</span>
              <span v-else>{{ t("login.logging") }}</span>
            </el-button>
          </el-form-item>

          <el-form-item v-if="others.length > 0" class="social-login">
            <div class="divider">
              <span class="divider-text">其他登录方式</span>
            </div>
            <div class="social-icons">
              <div
                  v-for="other in others"
                  :key="other.id"
                  class="social-icon"
                  @click="openOtherLogin(other.id)"
                  :title="other.name"
              >
                <img :src="privateImage(other.icon)" :alt="other.name"/>
              </div>
            </div>
          </el-form-item>
<!--          <div class="form-footer">-->
<!--            <router-link to="/register" class="register-link">-->
<!--              注册-->
<!--            </router-link>-->
<!--          </div>-->
        </el-form>
      </div>

      <!-- 底部 -->
      <Footer class="login-footer"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, watch} from "vue";
import {getCodeImg, loginPreGet, getThirdById} from "@/api/login";
import {privateImage} from "@/utils/Surpass";
import {ElMessage} from 'element-plus'
import Cookies from "js-cookie";
import {decrypt} from "@/utils/Jsencrypt";
import useUserStore from '@/store/modules/user'
import appStore from '@/store/modules/app'
import {useI18n} from 'vue-i18n'
import logoUrl from '@/assets/logo/logo.png'
import modal from "@/plugins/modal"

const {t} = useI18n()
import {getToken} from '@/utils/Auth'
import Footer from "@/components/Footer/index.vue"
import {useRoute, useRouter} from "vue-router";
import type {FormInstance, FormRules} from 'element-plus';
import {UserFilled} from "@element-plus/icons-vue";

const userStore: any = useUserStore();
const route: any = useRoute();
const router: any = useRouter();
const {proxy} = getCurrentInstance()!;
const loginForm: any = ref({
  username: "",
  password: "",
  rememberMe: false,
  captcha: "",
  state: "",
  authType: 'normal'
});

const loginRules: any = reactive<FormRules>({
  username: [{required: true, trigger: "blur", message: t('login.textUsernameNotice')}],
  password: [{required: true, trigger: "blur", message: t('login.textPwdNotice')}],
  captcha: [{required: true, trigger: "change", message: t('login.textCodeNotice')}]
});
const state: any = ref("");
const staticAppInfo: any = ref({
  logo: logoUrl,
  consoleTitle: ""
});
const codeUrl: any = ref("");
const loading: any = ref(false);
// 验证码开关
const captchaEnabled: any = ref(false);
// 注册开关
const register: any = ref(false);
const redirect: any = ref("");

//其他登录方式
interface OtherItem {
  id: any;
  name: any;
  icon: any;
}

const others: any = ref<OtherItem[]>([]);

watch(route, (newRoute: any) => {
  redirect.value = newRoute.query && newRoute.query.redirect;
}, {immediate: true});

const loginRef: any = ref<FormInstance>();

function handleLogin(): any {
  if (!loginRef.value) return;
  loginRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      loginForm.value.state = state.value;

      // 调用action的登录方法
      userStore.login(loginForm.value).then((res: any) => {
        const query: any = route.query;
        const otherQueryParams: any = Object.keys(query).reduce((acc: any, cur: any) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur];
          }
          return acc;
        }, {});
        if (redirect.value && redirect.value?.startsWith("http")) {
          location.replace(redirect.value)
        } else if (redirect.value) {
          router.push({path: redirect.value, query: otherQueryParams});
        } else {
          window.location.reload()
        }
      }).catch((err: any) => {
        console.error(err)
        ElMessage.error(err.message || "登录失败")
        loading.value = false;
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode();
        }
      }).finally(() => {
        loading.value = false
      });
    }
  });
}

function getCode(): any {
  getCodeImg(state.value, captchaEnabled.value).then((res: any) => {
    if (captchaEnabled.value) {
      codeUrl.value = res.data.image;
      state.value = res.data.state
    }
  });
}

function getCookie(): any {
  const username: any = Cookies.get("username");
  const password: any = Cookies.get("password");
  const rememberMe: any = Cookies.get("rememberMe");
  loginForm.value = {
    authType: "", captcha: "", state: "",
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  };
}

function getState(): any {
  loginPreGet().then((res: any) => {
    if (res.code === 0) {
      //staticAppInfo.value = res.data.inst
      staticAppInfo.value.logo = logoUrl
      // import(`/${res.data.inst.logo}`).then((logo: any) =>  {
      //   staticAppInfo.value.logo = logo.default
      //   console.log(staticAppInfo.value)
      // })

      appStore().setAppInfo(staticAppInfo.value)
      state.value = res.data.state
      captchaEnabled.value = res.data.captcha
      if (captchaEnabled.value) {
        getCode();
      }
      // others.value = res.data.auths;
    }
  });

  // openFuncList().then((res: any) =>  {
  //   others.value = res.data.auths;
  // })
}

function openOtherLogin(id: any): any {
  getThirdById(id).then((res: any) => {
    if (res.code === 200) {
      const flag: any = window.open(res.data, '', "height=800, width=1400, top=100, left=100,toolbar=no, menubar=no, scrollbars=no, resizable=no, loca tion=no, status=no");
      const loop: any = setInterval(function () {
        if (flag?.closed) {
          clearInterval(loop);
          if (getToken()) {
            const query: any = route.query;
            const otherQueryParams: any = Object.keys(query).reduce((acc: any, cur: any) => {
              if (cur !== "redirect") {
                acc[cur] = query[cur];
              }
              return acc;
            }, {});
            router.push({path: redirect.value || "/", query: otherQueryParams});
          }
        }
      }, 3);
    } else {
      modal.msgError(res.message);
    }
  })
}

getState();
// getCookie();
</script>

<style lang='scss' scoped>
@use "sass:color";

// 高级素雅风配色方案
$primary-color: #2c3e50; // 深蓝灰色主色调
$secondary-color: #34495e; // 辅助深色
$accent-color: #3498db; // 亮蓝色点缀
$text-primary: #2c3e50; // 主要文字色
$text-secondary: #7f8c8d; // 次要文字色
$background-light: #ffffff; // 浅色背景
$background-dark: #f8f9fa; // 浅灰背景
$border-color: #e9ecef; // 边框色
$shadow-color: rgba(44, 62, 80, 0.1); // 阴影色
$success-color: #27ae60; // 成功色
$error-color: #e74c3c; // 错误色

.login-container {
  position: relative;
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  background-image: url('../assets/images/login-bg.jpg');
  background-size: cover;
  background-position: center;
  color: $text-primary;
  font-family: 'Helvetica Neue', Arial, sans-serif;
}

// 左侧装饰区域
.login-decoration {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, rgba(44, 62, 80, 0.85) 0%, rgba(52, 73, 94, 0.9) 100%);
  position: relative;
  overflow: hidden;

  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(52, 152, 219, 0.1) 0%, transparent 70%);
    animation: rotate 20s linear infinite;
  }

  .decoration-content {
    position: relative;
    z-index: 2;
    text-align: center;
    max-width: 500px;
    padding: 2rem;
  }

  .brand-info {
    margin-bottom: 3rem;

    .brand-logo {
      width: 80px;
      height: 80px;
      margin-bottom: 1.5rem;
      filter: drop-shadow(0 4px 12px rgba(0, 0, 0, 0.2));
    }

    .brand-title {
      font-size: 2.5rem;
      font-weight: 300;
      color: white;
      margin: 0 0 1rem 0;
      letter-spacing: 1px;
    }

    .brand-slogan {
      font-size: 1.2rem;
      color: rgba(255, 255, 255, 0.85);
      font-weight: 300;
      margin: 0;
    }
  }

  .decoration-elements {
    .element {
      position: absolute;
      border-radius: 50%;
      background: rgba(52, 152, 219, 0.2);

      &.element-1 {
        width: 120px;
        height: 120px;
        top: 20%;
        left: 10%;
        animation: float 6s ease-in-out infinite;
      }

      &.element-2 {
        width: 80px;
        height: 80px;
        bottom: 25%;
        right: 15%;
        animation: float 8s ease-in-out infinite 1s;
      }

      &.element-3 {
        width: 60px;
        height: 60px;
        top: 60%;
        left: 20%;
        animation: float 7s ease-in-out infinite 0.5s;
      }
    }
  }
}

// 右侧登录区域
.login-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.92);
  backdrop-filter: blur(10px);
  padding: 2rem;
  position: relative;

  .login-card {
    width: 100%;
    max-width: 420px;
    background: white;
    border-radius: 16px;
    padding: 2.5rem;
    box-shadow: 0 10px 30px rgba(44, 62, 80, 0.15);
    border: 1px solid rgba(233, 236, 239, 0.8);

    .card-header {
      text-align: center;
      margin-bottom: 2rem;

      .login-title {
        font-size: 1.8rem;
        font-weight: 400;
        color: $primary-color;
        margin: 0 0 0.5rem 0;
      }

      .login-subtitle {
        font-size: 0.95rem;
        color: $text-secondary;
        margin: 0;
        font-weight: 300;
      }
    }

    .login-form {
      .el-form-item {
        margin-bottom: 1.5rem;
      }

      ::v-deep(.el-form-item__error) {
        color: $error-color;
        font-size: 0.85rem;
        padding-top: 0.25rem;
      }
    }
  }

  .login-footer {
    position: absolute;
    bottom: 1rem;
  }
}

// 输入框样式
.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  border: 1px solid $border-color;
  border-radius: 8px;
  transition: all 0.3s ease;
  background: white;

  &:focus-within {
    border-color: $accent-color;
    box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
  }

  .input-icon {
    margin: 0 10px;
    color: $text-secondary;
    font-size: 1.1rem;
  }

  .custom-input {
    flex: 1;

    :deep(.el-input__wrapper) {
      border: none !important;
      border-left: 1px solid $border-color !important;

      &.is-focus {
        box-shadow: none !important;
      }
    }

    ::v-deep(.el-input__inner) {
      border: none !important;
      outline: none !important;
      box-shadow: none !important;
      padding: 0.8rem 1rem;
      font-size: 0.95rem;
      color: $text-primary;
      background: transparent;

      &:focus {
        border: none !important;
      }

      &::placeholder {
        color: $text-secondary;
        font-weight: 300;
      }
    }

    ::v-deep(.el-input__suffix) {
      padding-right: 1rem;

      .el-input__icon {
        color: $text-secondary;
        cursor: pointer;
        transition: color 0.2s ease;

        &:hover {
          color: $accent-color;
        }
      }
    }
  }
}

// 验证码区域
.captcha-wrapper {
  display: flex;
  gap: 1rem;

  .captcha-input {
    flex: 1;
  }

  .captcha-image {
    flex-shrink: 0;

    .captcha-img {
      height: 42px;
      border-radius: 6px;
      cursor: pointer;
      border: 1px solid $border-color;
      transition: border-color 0.3s ease;

      &:hover {
        border-color: $accent-color;
      }
    }
  }
}

// 登录按钮
.submit-item {
  margin-top: 2rem !important;
  margin-bottom: 1rem !important;

  .login-button {
    width: 100%;
    height: 46px;
    background: linear-gradient(135deg, $accent-color 0%, #2980b9 100%);
    border: none;
    border-radius: 8px;
    color: white;
    font-size: 1rem;
    font-weight: 500;
    letter-spacing: 0.5px;
    transition: all 0.3s ease;
    box-shadow: 0 4px 15px rgba(52, 152, 219, 0.3);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(52, 152, 219, 0.4);
    }

    &:active {
      transform: translateY(0);
    }

    ::v-deep(.el-loading-spinner) {
      .circular {
        width: 20px;
        height: 20px;
      }
    }
  }
}

// 社交登录
.social-login {
  .divider {
    display: flex;
    align-items: center;
    margin: 1.5rem 0;

    &::before,
    &::after {
      content: '';
      flex: 1;
      height: 1px;
      background: $border-color;
    }

    .divider-text {
      padding: 0 1rem;
      color: $text-secondary;
      font-size: 0.85rem;
      font-weight: 300;
    }
  }

  .social-icons {
    display: flex;
    justify-content: center;
    gap: 1rem;

    .social-icon {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background: $background-dark;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 1px solid $border-color;

      &:hover {
        transform: translateY(-3px);
        box-shadow: 0 5px 15px rgba(44, 62, 80, 0.15);
        border-color: $accent-color;
      }

      img {
        width: 20px;
        height: 20px;
      }
    }
  }
}

// 动画效果
@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

// 响应式设计
@media (max-width: 992px) {
  .login-container {
    flex-direction: column;
  }

  .login-decoration {
    padding: 2rem 1rem;

    .brand-title {
      font-size: 2rem;
    }
  }

  .login-main {
    padding: 1rem;
  }
}

@media (max-width: 576px) {
  .login-decoration {
    .brand-title {
      font-size: 1.5rem;
    }

    .brand-slogan {
      font-size: 1rem;
    }
  }

  .login-main {
    .login-card {
      padding: 1.5rem;
      margin: 1rem;
    }
  }

  .captcha-wrapper {
    flex-direction: column;
    gap: 0.5rem;

    .captcha-image {
      .captcha-img {
        width: 100%;
        height: auto;
      }
    }
  }
}

.form-footer {
  text-align: center;
}
</style>
