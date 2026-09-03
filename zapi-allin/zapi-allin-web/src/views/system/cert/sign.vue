<template>
  <div class="cert-sign-container">
    <!-- 证书签名区域 -->
    <el-card class="sign-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon><EditPen /></el-icon> 证书签名</span>
        </div>
      </template>

      <el-form :model="signForm" label-width="120px">
        <el-form-item label="证书类型">
          <el-radio-group v-model="signForm.certType">
            <el-radio-button value="SM2">SM2</el-radio-button>
            <el-radio-button value="RSA">RSA</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="证书文件">
          <el-upload
            ref="uploadRef"
            :auto-upload="false"
            :limit="1"
            :on-change="handleFileChange"
            :on-remove="handleFileRemove"
            accept=".pfx,.p12"
            drag
          >
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
              将证书文件拖到此处，或<em>点击上传</em>
            </div>
            <template #tip>
              <div class="el-upload__tip">
                只能上传 .pfx 或 .p12 格式的证书文件
              </div>
            </template>
          </el-upload>
        </el-form-item>

        <el-form-item label="证书密码">
          <el-input
            v-model="signForm.certPwd"
            type="password"
            show-password
            placeholder="请输入证书密码"
            style="width: 100%"
          />
        </el-form-item>

        <el-form-item label="签名原文">
          <el-input
            v-model="signForm.originalText"
            type="textarea"
            :rows="6"
            placeholder="请输入要签名的原文内容"
            maxlength="5000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSign" :loading="signing">
            <el-icon><EditPen /></el-icon>
            执行签名
          </el-button>
          <el-button @click="handleResetSign">重置</el-button>
        </el-form-item>

        <!-- 签名结果 -->
        <el-form-item v-if="signResult" label="签名结果">
          <el-card shadow="never" style="background: #f5f7fa">
            <div class="result-content">
              <div class="result-item">
                <span class="result-label">签名值：</span>
                <el-input
                  v-model="signResult.signData"
                  type="textarea"
                  :rows="4"
                  readonly
                  style="margin-top: 8px"
                />
                <el-button
                  type="text"
                  @click="copyToClipboard(signResult.signData)"
                  style="margin-top: 8px"
                >
                  <el-icon><DocumentCopy /></el-icon>
                  复制签名值
                </el-button>
              </div>
              <div class="result-item" v-if="signResult.publicKey">
                <span class="result-label">公钥：</span>
                <el-input
                  v-model="signResult.publicKey"
                  type="textarea"
                  :rows="6"
                  readonly
                  style="margin-top: 8px"
                />
                <el-button
                  type="text"
                  @click="copyToClipboard(signResult.publicKey)"
                  style="margin-top: 8px"
                >
                  <el-icon><DocumentCopy /></el-icon>
                  复制公钥
                </el-button>
              </div>
            </div>
          </el-card>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 证书验签区域 -->
    <el-card class="verify-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon><CircleCheck /></el-icon> 证书验签</span>
        </div>
      </template>

      <el-tabs v-model="verifyTab" @tab-change="handleTabChange">
        <!-- SM2验签 -->
        <el-tab-pane label="SM2验签" name="sm2">
          <el-form :model="verifySM2Form" label-width="120px">
            <el-form-item label="公钥">
              <el-input
                v-model="verifySM2Form.publicKey"
                type="textarea"
                :rows="6"
                placeholder="请输入SM2公钥（PEM格式）"
              />
            </el-form-item>

            <el-form-item label="签名原文">
              <el-input
                v-model="verifySM2Form.originalText"
                type="textarea"
                :rows="4"
                placeholder="请输入签名原文"
              />
            </el-form-item>

            <el-form-item label="签名值">
              <el-input
                v-model="verifySM2Form.signData"
                type="textarea"
                :rows="4"
                placeholder="请输入签名值（Base64编码）"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleVerifySM2" :loading="verifying">
                <el-icon><CircleCheck /></el-icon>
                执行验签
              </el-button>
              <el-button @click="handleResetVerifySM2">重置</el-button>
            </el-form-item>

            <!-- 验签结果 -->
            <el-form-item v-if="verifySM2Result !== null" label="验签结果">
              <el-alert
                :type="verifySM2Result ? 'success' : 'error'"
                :title="verifySM2Result ? '验签成功' : '验签失败'"
                :description="verifySM2Result ? '签名验证通过' : '签名验证失败，请检查公钥、原文和签名值是否正确'"
                show-icon
                :closable="false"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- RSA验签 -->
        <el-tab-pane label="RSA验签" name="rsa">
          <el-form :model="verifyRSAForm" label-width="120px">
            <el-form-item label="证书文件">
              <el-upload
                ref="verifyUploadRef"
                :auto-upload="false"
                :limit="1"
                :on-change="handleVerifyFileChange"
                :on-remove="handleVerifyFileRemove"
                accept=".pfx,.p12"
                drag
              >
                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                <div class="el-upload__text">
                  将证书文件拖到此处，或<em>点击上传</em>
                </div>
                <template #tip>
                  <div class="el-upload__tip">
                    只能上传 .pfx 或 .p12 格式的证书文件
                  </div>
                </template>
              </el-upload>
            </el-form-item>

            <el-form-item label="证书密码">
              <el-input
                v-model="verifyRSAForm.certPwd"
                type="password"
                show-password
                placeholder="请输入证书密码"
                style="width: 100%"
              />
            </el-form-item>

            <el-form-item label="签名原文">
              <el-input
                v-model="verifyRSAForm.originalText"
                type="textarea"
                :rows="4"
                placeholder="请输入签名原文"
              />
            </el-form-item>

            <el-form-item label="签名值">
              <el-input
                v-model="verifyRSAForm.signData"
                type="textarea"
                :rows="4"
                placeholder="请输入签名值（Base64编码）"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" @click="handleVerifyRSA" :loading="verifying">
                <el-icon><CircleCheck /></el-icon>
                执行验签
              </el-button>
              <el-button @click="handleResetVerifyRSA">重置</el-button>
            </el-form-item>

            <!-- 验签结果 -->
            <el-form-item v-if="verifyRSAResult !== null" label="验签结果">
              <el-alert
                :type="verifyRSAResult ? 'success' : 'error'"
                :title="verifyRSAResult ? '验签成功' : '验签失败'"
                :description="verifyRSAResult ? '签名验证通过' : '签名验证失败，请检查证书文件、密码、原文和签名值是否正确'"
                show-icon
                :closable="false"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import {
  EditPen,
  CircleCheck,
  UploadFilled,
  DocumentCopy
} from '@element-plus/icons-vue'
import { certApi } from '@/api'

// 签名表单
const signForm = reactive({
  certType: 'SM2',
  certFile: null,
  certPwd: '',
  originalText: ''
})

// 验签表单 - SM2
const verifySM2Form = reactive({
  publicKey: '',
  originalText: '',
  signData: ''
})

// 验签表单 - RSA
const verifyRSAForm = reactive({
  certFile: null,
  certPwd: '',
  originalText: '',
  signData: ''
})

// 状态
const signing = ref(false)
const verifying = ref(false)
const signResult = ref(null)
const verifySM2Result = ref(null)
const verifyRSAResult = ref(null)
const verifyTab = ref('sm2')

// 上传组件引用
const uploadRef = ref(null)
const verifyUploadRef = ref(null)

// 文件变化处理
const handleFileChange = (file) => {
  signForm.certFile = file.raw
}

const handleFileRemove = () => {
  signForm.certFile = null
}

const handleVerifyFileChange = (file) => {
  verifyRSAForm.certFile = file.raw
}

const handleVerifyFileRemove = () => {
  verifyRSAForm.certFile = null
}

// 执行签名
const handleSign = async () => {
  if (!signForm.certFile) {
    ElMessage.warning('请选择证书文件')
    return
  }
  if (!signForm.certPwd) {
    ElMessage.warning('请输入证书密码')
    return
  }
  if (!signForm.originalText || !signForm.originalText.trim()) {
    ElMessage.warning('请输入签名原文')
    return
  }

  signing.value = true
  try {
    const formData = new FormData()
    formData.append('certFile', signForm.certFile)
    formData.append('originalText', signForm.originalText)
    formData.append('certPwd', signForm.certPwd)

    const apiMethod = signForm.certType === 'SM2' 
      ? certApi.signSM2Cert 
      : certApi.signRSACert

    const res = await apiMethod(formData)
    
    if (res.code === 200) {
      signResult.value = res.data
      ElMessage.success('签名成功')
    } else {
      ElMessage.error(res.message || '签名失败')
    }
  } catch (error) {
    console.error('签名失败:', error)
    ElMessage.error('签名失败')
  } finally {
    signing.value = false
  }
}

// 重置签名表单
const handleResetSign = () => {
  signForm.certFile = null
  signForm.certPwd = ''
  signForm.originalText = ''
  signResult.value = null
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}

// SM2验签
const handleVerifySM2 = async () => {
  if (!verifySM2Form.publicKey || !verifySM2Form.publicKey.trim()) {
    ElMessage.warning('请输入公钥')
    return
  }
  if (!verifySM2Form.originalText || !verifySM2Form.originalText.trim()) {
    ElMessage.warning('请输入签名原文')
    return
  }
  if (!verifySM2Form.signData || !verifySM2Form.signData.trim()) {
    ElMessage.warning('请输入签名值')
    return
  }

  verifying.value = true
  try {
    const res = await certApi.verifySM2Cert(verifySM2Form)
    
    if (res.code === 200) {
      verifySM2Result.value = res.data?.verifyResult || false
      if (verifySM2Result.value) {
        ElMessage.success('验签成功')
      } else {
        ElMessage.error('验签失败')
      }
    } else {
      ElMessage.error(res.message || '验签失败')
      verifySM2Result.value = false
    }
  } catch (error) {
    console.error('SM2验签失败:', error)
    ElMessage.error('验签失败')
    verifySM2Result.value = false
  } finally {
    verifying.value = false
  }
}

// RSA验签
const handleVerifyRSA = async () => {
  if (!verifyRSAForm.certFile) {
    ElMessage.warning('请选择证书文件')
    return
  }
  if (!verifyRSAForm.certPwd) {
    ElMessage.warning('请输入证书密码')
    return
  }
  if (!verifyRSAForm.originalText || !verifyRSAForm.originalText.trim()) {
    ElMessage.warning('请输入签名原文')
    return
  }
  if (!verifyRSAForm.signData || !verifyRSAForm.signData.trim()) {
    ElMessage.warning('请输入签名值')
    return
  }

  verifying.value = true
  try {
    const formData = new FormData()
    formData.append('certFile', verifyRSAForm.certFile)
    formData.append('certPwd', verifyRSAForm.certPwd)
    formData.append('originalText', verifyRSAForm.originalText)
    formData.append('signData', verifyRSAForm.signData)

    const res = await certApi.verifyRSACert(formData)
    
    if (res.code === 200) {
      verifyRSAResult.value = res.data?.verifyResult || false
      if (verifyRSAResult.value) {
        ElMessage.success('验签成功')
      } else {
        ElMessage.error('验签失败')
      }
    } else {
      ElMessage.error(res.message || '验签失败')
      verifyRSAResult.value = false
    }
  } catch (error) {
    console.error('RSA验签失败:', error)
    ElMessage.error('验签失败')
    verifyRSAResult.value = false
  } finally {
    verifying.value = false
  }
}

// 重置SM2验签表单
const handleResetVerifySM2 = () => {
  verifySM2Form.publicKey = ''
  verifySM2Form.originalText = ''
  verifySM2Form.signData = ''
  verifySM2Result.value = null
}

// 重置RSA验签表单
const handleResetVerifyRSA = () => {
  verifyRSAForm.certFile = null
  verifyRSAForm.certPwd = ''
  verifyRSAForm.originalText = ''
  verifyRSAForm.signData = ''
  verifyRSAResult.value = null
  if (verifyUploadRef.value) {
    verifyUploadRef.value.clearFiles()
  }
}

// Tab切换
const handleTabChange = (tabName) => {
  if (tabName === 'sm2') {
    verifySM2Result.value = null
  } else {
    verifyRSAResult.value = null
  }
}

// 复制到剪贴板
const copyToClipboard = (text) => {
  if (!text) return
  
  navigator.clipboard.writeText(text).then(() => {
    ElMessage.success('已复制到剪贴板')
  }).catch(() => {
    // 降级方案
    const textarea = document.createElement('textarea')
    textarea.value = text
    document.body.appendChild(textarea)
    textarea.select()
    document.execCommand('copy')
    document.body.removeChild(textarea)
    ElMessage.success('已复制到剪贴板')
  })
}
</script>

<style lang="scss" scoped>
.cert-sign-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 80px);
}

.sign-card,
.verify-card {
  margin-bottom: 20px;
  
  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom: 1px solid #ebeef5;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
  color: #303133;
  
  span {
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

.result-content {
  .result-item {
    margin-bottom: 20px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .result-label {
    font-weight: 600;
    color: #303133;
    display: block;
    margin-bottom: 8px;
  }
}

:deep(.el-upload) {
  width: 100%;
}

:deep(.el-upload-dragger) {
  width: 100%;
}

:deep(.el-textarea__inner) {
  font-family: 'Courier New', monospace;
}
</style>

