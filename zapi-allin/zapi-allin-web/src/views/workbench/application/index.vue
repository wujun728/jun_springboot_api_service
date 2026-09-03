<template>
  <div class="application-page">
    <!-- 顶部走马灯 -->
    <div class="carousel-section">
      <el-carousel :interval="5000" type="card" height="200px" :autoplay="true">
        <el-carousel-item v-for="item in carouselItems" :key="item.id">
          <div class="carousel-item-content" :style="{ background: item.color }">
            <div class="carousel-text">
              <h3>{{ item.title }}</h3>
              <p>{{ item.desc }}</p>
            </div>
            <div class="carousel-decoration">
              <el-icon :size="100" style="opacity: 0.2"><component :is="item.id === 1 ? 'Setting' : (item.id === 2 ? 'Connection' : 'Lock')" /></el-icon>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 主内容区域 -->
    <div class="main-content-wrapper">
      <!-- 搜索和筛选 -->
      <div class="search-section">
        <div class="search-wrapper">
          <el-input
            v-model="searchQuery"
            placeholder="搜索应用名称..."
            prefix-icon="Search"
            style="width: 400px"
            clearable
            @input="handleSearch"
          />
        </div>
        <div class="header-actions">
          <el-button type="primary" @click="handleAdd">
            <el-icon class="el-icon--left"><Plus /></el-icon>
            添加应用
          </el-button>
          <el-button @click="handleRefresh">
            <el-icon class="el-icon--left"><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>

      <!-- 应用列表 (卡片视图) -->
      <div class="application-content" v-loading="loading">
        <div v-if="applicationList.length > 0" class="app-grid">
          <div
            v-for="(item, index) in applicationList"
            :key="item.id"
            class="app-card"
            @click="handleConfig(item)"
          >
            <div class="card-status">
              <el-tag
                :type="item.status === 1 ? 'success' : 'info'"
                size="small"
                effect="plain"
              >
                {{ item.status === 1 ? '运行中' : '已禁用' }}
              </el-tag>
            </div>
            
            <div class="card-main">
              <div class="app-icon" :style="{ backgroundColor: getIconColor(index) }">
                <img v-if="item.appIcon" :src="item.appIcon" alt="icon" />
                <span v-else>{{ item.appName?.charAt(0)?.toUpperCase() }}</span>
              </div>
              
              <h3 class="app-name">{{ item.appName }}</h3>
            </div>

            <div class="card-actions" @click.stop>
              <el-tooltip content="查看授权" placement="top">
                <el-button size="small" circle @click="handleViewAuth(item)">
                  <el-icon><Key /></el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="编辑" placement="top">
                <el-button size="small" circle @click="handleEdit(item)">
                  <el-icon><Edit /></el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="配置" placement="top">
                <el-button size="small" circle type="primary" @click="handleConfig(item)">
                  <el-icon><Setting /></el-icon>
                </el-button>
              </el-tooltip>
              <el-tooltip content="删除" placement="top">
                <el-button size="small" circle type="danger" plain @click="handleDelete(item)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </el-tooltip>
            </div>
          </div>
        </div>
        <el-empty v-else description="暂无应用" />
      </div>

      <!-- 分页区域 -->
      <div class="pagination-section">
        <el-pagination
          background
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="Number(total)"
          layout="total, prev, pager, next"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="应用名称" prop="appName">
          <el-input
            v-model="formData.appName"
            placeholder="请输入应用名称"
            clearable
            maxlength="100"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="应用图标" prop="appIcon">
          <el-input
            v-model="formData.appIcon"
            placeholder="请输入图标URL（可选）"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="formData.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息（可选）"
            maxlength="255"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 新建成功后显示授权信息对话框 -->
    <el-dialog
      v-model="authInfoDialogVisible"
      title="应用创建成功"
      width="550px"
      :close-on-click-modal="false"
    >
      <el-alert 
        title="请妥善保存以下授权信息，授权密码仅显示一次！" 
        type="warning" 
        :closable="false"
        show-icon
        style="margin-bottom: 20px"
      />
      <el-descriptions :column="1" border>
        <el-descriptions-item label="应用名称">{{ createdApp.appName }}</el-descriptions-item>
        <el-descriptions-item label="授权码">
          <div class="auth-info-row">
            <code>{{ createdApp.authCode }}</code>
            <el-button link size="small" @click="copyToClipboard(createdApp.authCode, '授权码')">
              <el-icon><CopyDocument /></el-icon>
            </el-button>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="授权密码">
          <div class="auth-info-row">
            <code>{{ createdApp.authPsw }}</code>
            <el-button link size="small" @click="copyToClipboard(createdApp.authPsw, '授权密码')">
              <el-icon><CopyDocument /></el-icon>
            </el-button>
          </div>
        </el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button type="primary" @click="authInfoDialogVisible = false">我已保存</el-button>
      </template>
    </el-dialog>

    <!-- 查看授权信息对话框 -->
    <el-dialog
      v-model="viewAuthDialogVisible"
      title="授权信息"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="auth-info-content">
        <div class="auth-info-item">
          <div class="auth-label">应用名称</div>
          <div class="auth-value">{{ viewAuthData.appName }}</div>
        </div>
        
        <div class="auth-info-item">
          <div class="auth-label">授权码</div>
          <div class="auth-value-box">
            <code class="auth-code">{{ viewAuthData.authCode }}</code>
            <el-button 
              link 
              type="primary" 
              @click="copyToClipboard(viewAuthData.authCode, '授权码')"
            >
              <el-icon><CopyDocument /></el-icon>
              复制
            </el-button>
          </div>
        </div>
        
        <div class="auth-info-item">
          <div class="auth-label">授权密码</div>
          <div class="auth-value-box">
            <code class="auth-code">{{ viewAuthData.authPsw }}</code>
            <el-button 
              link 
              type="primary" 
              @click="copyToClipboard(viewAuthData.authPsw, '授权密码')"
            >
              <el-icon><CopyDocument /></el-icon>
              复制
            </el-button>
          </div>
        </div>
      </div>
      
      <template #footer>
        <el-button type="primary" @click="viewAuthDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, CopyDocument, View, Hide, RefreshRight, Setting, Edit, Delete, Connection, Lock, Calendar, MoreFilled, Key } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { wbApplicationApi } from '@/api'
import { useAppStore } from '@/stores/app'

// 使用 app store 获取主题色
const appStore = useAppStore()

// 响应式数据
const loading = ref(false)
const applicationList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const searchQuery = ref('')
const showPassword = reactive({})

// 根据索引生成单一主题色
const getIconColor = (index) => {
  // 使用固定的颜色数组，确保每个应用有不同的颜色
  const colors = [
    '#818cf8',  // 0: 紫蓝色
    '#34d399',  // 1: 绿色
    '#fbbf24',  // 2: 橙色
    '#60a5fa',  // 3: 蓝色
    '#a78bfa',  // 4: 紫色
    '#fb7185',  // 5: 粉红色
  ]
  const colorIndex = index % colors.length
  console.log(`应用索引: ${index}, 颜色索引: ${colorIndex}, 颜色值: ${colors[colorIndex]}`)
  return colors[colorIndex]
}

// 处理卡片下拉菜单操作
const handleCardAction = (command, item) => {
  switch (command) {
    case 'regenerate':
      handleRegenerate(item)
      break
    case 'delete':
      handleDelete(item)
      break
  }
}

// 系统主题色数组（从 appStore 的 themeConfig 中获取）
const getThemeColors = () => {
  const themeConfig = appStore.currentThemeConfig
  return [
    themeConfig.primary,    // 主色
    themeConfig.success,    // 成功色
    themeConfig.warning,    // 警告色
    themeConfig.info,       // 信息色
    themeConfig.secondary,  // 次要色
    themeConfig.accent,     // 强调色
  ]
}

// 随机选择三个不重复的颜色
const getCarouselColors = () => {
  const themeColors = getThemeColors()
  
  // 去重（防止主题配置中有重复颜色）
  const uniqueColors = [...new Set(themeColors)]
  
  // 如果唯一颜色少于3个，使用默认颜色补充
  const defaultColors = [
    '#818cf8',  // 紫蓝色
    '#34d399',  // 绿色
    '#f59e0b',  // 橙色
    '#60a5fa',  // 蓝色
    '#a78bfa',  // 紫色
    '#fb7185',  // 粉红色
  ]
  
  // 合并并去重
  const allColors = [...new Set([...uniqueColors, ...defaultColors])]
  
  // 随机打乱并选择前3个
  const shuffled = [...allColors].sort(() => 0.5 - Math.random())
  return shuffled.slice(0, 3)
}

const carouselColors = getCarouselColors()

// 走马灯数据
const carouselItems = [
  { id: 1, title: '应用管理中心', desc: '统一管理您的API应用，轻松配置授权与安全设置', color: carouselColors[0] },
  { id: 2, title: '快速接入', desc: '提供标准化的API接口，助力业务快速集成与拓展', color: carouselColors[1] },
  { id: 3, title: '安全可靠', desc: '多重安全机制保障，让您的应用数据更安全', color: carouselColors[2] },
]

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogMode = ref('add')
const submitLoading = ref(false)
const formRef = ref(null)
const formData = ref({
  id: null,
  appName: '',
  appIcon: '',
  authCode: '',
  authPsw: '',
  status: 1,
  remark: ''
})

// 创建成功后显示授权信息
const authInfoDialogVisible = ref(false)
const createdApp = ref({})

// 查看授权信息对话框
const viewAuthDialogVisible = ref(false)
const viewAuthData = ref({})

// 查看授权信息
const handleViewAuth = async (row) => {
  try {
    const response = await wbApplicationApi.getApplication(row.id)
    if (response.code === 200) {
      viewAuthData.value = {
        appName: response.data.appName,
        authCode: response.data.authCode,
        authPsw: response.data.authPsw
      }
      viewAuthDialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取授权信息失败')
    }
  } catch (error) {
    ElMessage.error('获取授权信息失败')
  }
}

// 表单验证规则
const formRules = {
  appName: [
    { required: true, message: '请输入应用名称', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 加载应用列表
const loadApplicationList = async () => {
  loading.value = true
  try {
    const params = {
      current: currentPage.value,
      size: pageSize.value
    }
    if (searchQuery.value) {
      params.appName = searchQuery.value
    }
    
    const response = await wbApplicationApi.getApplicationList(params)
    if (response.code === 200) {
      applicationList.value = response.data.records || []
      total.value = Number(response.data.total) || 0
    }
  } catch (error) {
    ElMessage.error('加载应用列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadApplicationList()
}

// 刷新
const handleRefresh = () => {
  loadApplicationList()
}

// 打开添加对话框
const handleAdd = () => {
  dialogMode.value = 'add'
  dialogTitle.value = '添加应用'
  resetForm()
  dialogVisible.value = true
}

// 打开编辑对话框
const handleEdit = async (row) => {
  dialogMode.value = 'edit'
  dialogTitle.value = '编辑应用'
  
  try {
    const response = await wbApplicationApi.getApplication(row.id)
    if (response.code === 200) {
      formData.value = {
        id: response.data.id,
        appName: response.data.appName,
        appIcon: response.data.appIcon || '',
        authCode: response.data.authCode,
        authPsw: response.data.authPsw,
        status: response.data.status,
        remark: response.data.remark || ''
      }
      dialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取应用信息失败')
    }
  } catch (error) {
    ElMessage.error('获取应用信息失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (dialogMode.value === 'add') {
          response = await wbApplicationApi.addApplication({
            appName: formData.value.appName,
            appIcon: formData.value.appIcon,
            status: formData.value.status,
            remark: formData.value.remark
          })
          
          if (response.code === 200) {
            ElMessage.success('添加成功')
            dialogVisible.value = false
            // 显示授权信息
            createdApp.value = response.data
            authInfoDialogVisible.value = true
            loadApplicationList()
          } else {
            ElMessage.error(response.message || '添加失败')
          }
        } else {
          response = await wbApplicationApi.updateApplication({
            id: formData.value.id,
            appName: formData.value.appName,
            appIcon: formData.value.appIcon,
            status: formData.value.status,
            remark: formData.value.remark
          })
          
          if (response.code === 200) {
            ElMessage.success('更新成功')
            dialogVisible.value = false
            loadApplicationList()
          } else {
            ElMessage.error(response.message || '更新失败')
          }
        }
      } catch (error) {
        ElMessage.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 重新生成授权码和密码
const handleRegenerate = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要重新生成应用"${row.appName}"的授权码和密码吗？原有的授权信息将失效！`,
      '确认重新生成',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await wbApplicationApi.regenerateAuth(row.id)
    if (response.code === 200) {
      ElMessage.success('重新生成成功')
      // 显示新的授权信息
      createdApp.value = response.data
      authInfoDialogVisible.value = true
      loadApplicationList()
    } else {
      ElMessage.error(response.message || '重新生成失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重新生成失败')
    }
  }
}

// 切换状态
const handleStatusChange = async (row, value) => {
  const newStatus = value ? 1 : 0
  try {
    const response = await wbApplicationApi.changeStatus(row.id, newStatus)
    if (response.code === 200) {
      ElMessage.success(newStatus === 1 ? '启用成功' : '禁用成功')
      loadApplicationList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 删除应用
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除应用"${row.appName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await wbApplicationApi.deleteApplication(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadApplicationList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 配置应用
const router = useRouter()
const handleConfig = (row) => {
  // 在新标签页打开应用配置页面
  const routeData = router.resolve({
    path: '/app-config',
    query: { appId: row.id, appName: row.appName }
  })
  window.open(routeData.href, '_blank')
}

// 复制到剪贴板
const copyToClipboard = async (text, label) => {
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success(`${label}已复制到剪贴板`)
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

// 切换密码显示
const togglePassword = (id) => {
  showPassword[id] = !showPassword[id]
}

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
}

// 重置表单
const resetForm = () => {
  formData.value = {
    id: null,
    appName: '',
    appIcon: '',
    authCode: '',
    authPsw: '',
    status: 1,
    remark: ''
  }
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

// 分页
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadApplicationList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadApplicationList()
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.split(' ')[0]
}

onMounted(() => {
  loadApplicationList()
})
</script>

<style lang="scss" scoped>
.application-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 80px);
  padding: 20px;
  background: var(--bg-primary);
  overflow: hidden;
}

.carousel-section {
  margin-bottom: 20px;
  flex-shrink: 0;
  
  .el-carousel__item {
    border-radius: 12px;
  }
}

.carousel-item-content {
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 60px;
  color: #fff;
  position: relative;
  overflow: hidden;
  
  .carousel-text {
    z-index: 1;
    
    h3 {
      font-size: 28px;
      font-weight: 600;
      margin: 0 0 12px 0;
      text-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    
    p {
      font-size: 16px;
      margin: 0;
      opacity: 0.9;
      max-width: 400px;
      line-height: 1.5;
    }
  }
  
  .carousel-decoration {
    position: absolute;
    right: 40px;
    bottom: -20px;
    transform: rotate(-15deg);
  }
}

.main-content-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--bg-card);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: var(--shadow-sm);
}

.search-section {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  background: var(--bg-card);
  position: relative;
  flex-shrink: 0;
}

.search-wrapper {
  display: flex;
  justify-content: center;
  width: 100%;
}

.header-actions {
  display: flex;
  gap: 12px;
  position: absolute;
  right: 20px;
}

.application-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  background: var(--bg-card);
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 16px;
}

.app-card {
  background: var(--bg-card);
  border-radius: 12px;
  border: 1px solid var(--border-color);
  padding: 20px 24px;
  transition: all 0.3s ease;
  position: relative;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 16px;
  
  &:hover {
    box-shadow: var(--shadow-md);
    border-color: var(--primary-color);
  }
}

.card-status {
  position: absolute;
  top: 12px;
  right: 12px;
}

.card-main {
  display: flex;
  align-items: center;
  gap: 16px;
  flex: 1;
  min-width: 0;
  
  .app-icon {
    width: 56px;
    height: 56px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    font-size: 24px;
    font-weight: 600;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    flex-shrink: 0;
    
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      border-radius: 12px;
    }
  }
  
  .app-name {
    margin: 0;
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.card-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  padding-top: 12px;
  border-top: 1px solid var(--border-color);
}

.text-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pagination-section {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 16px 20px;
  background: var(--bg-card);
  border-top: 1px solid var(--border-color);
  flex-shrink: 0;
}

// 对话框样式
:deep(.el-dialog) {
  border-radius: 16px;
  
  .el-dialog__header {
    padding: 24px 24px 16px;
    border-bottom: 1px solid var(--border-color);
    
    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
    }
  }
  
  .el-dialog__body {
    padding: 24px;
  }
  
  .el-dialog__footer {
    padding: 16px 24px;
    border-top: 1px solid var(--border-color);
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.auth-info-row {
  display: flex;
  align-items: center;
  gap: 8px;
  
  code {
    background: var(--bg-secondary);
    padding: 4px 8px;
    border-radius: 4px;
    font-family: monospace;
    word-break: break-all;
  }
}

// 授权信息对话框样式
.auth-info-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
  
  .auth-info-item {
    display: flex;
    flex-direction: column;
    gap: 8px;
    
    .auth-label {
      font-size: 13px;
      color: var(--text-secondary);
      font-weight: 500;
    }
    
    .auth-value {
      font-size: 15px;
      color: var(--text-primary);
      font-weight: 600;
    }
    
    .auth-value-box {
      display: flex;
      align-items: center;
      gap: 12px;
      padding: 12px 16px;
      background: var(--bg-secondary);
      border-radius: 8px;
      border: 1px solid var(--border-color);
      
      .auth-code {
        flex: 1;
        font-family: 'Monaco', 'Consolas', monospace;
        font-size: 13px;
        color: var(--text-primary);
        word-break: break-all;
        background: transparent;
      }
      
      .el-button {
        flex-shrink: 0;
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .search-section {
    flex-direction: column;
    gap: 16px;
  }
  
  .header-actions {
    position: static;
    width: 100%;
    justify-content: center;
  }
  
  .search-wrapper {
    width: 100%;
  }
  
  .app-grid {
    grid-template-columns: 1fr;
  }
}
</style>
