<template>
  <div class="contract-container">
    <el-card shadow="always">
      <template #header>
        <div class="card-header">
          <span><el-icon><Document /></el-icon> 合同管理</span>
          <div class="header-actions">
            <el-button type="primary" @click="handleCreate">
              <el-icon class="el-icon--left"><Plus /></el-icon>
               新建合同
            </el-button>
            <el-button
              type="danger"
              :disabled="selectedIds.length === 0"
              @click="handleBatchDelete"
            >
              <el-icon class="el-icon--left"><Delete /></el-icon>
               批量删除
            </el-button>
          </div>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="合同名称">
            <el-input
              v-model="searchForm.contractName"
              placeholder="请输入合同名称"
              clearable
              style="width: 200px"
              @clear="loadData"
            />
          </el-form-item>
          <el-form-item label="合同状态">
            <el-select
              v-model="searchForm.contractStatus"
              placeholder="请选择状态"
              clearable
              style="width: 150px"
              @clear="loadData"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="审核中" value="REVIEWING" />
              <el-option label="待签署" value="PENDING_SIGN" />
              <el-option label="签署完成" value="SIGNED" />
              <el-option label="归档" value="ARCHIVED" />
            </el-select>
          </el-form-item>
          <el-form-item label=" ">
            <el-button type="primary" @click="handleSearch">
              <el-icon class="el-icon--left"><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon class="el-icon--left"><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 表格区域 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        stripe
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="contractNo" label="合同编号" width="180" />
        <el-table-column prop="contractName" label="合同名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="contractType" label="合同类型" width="120" />
        <el-table-column prop="fileId" label="合同文件" width="260">
          <template #default="{ row }">
              <div class="file-actions" v-if="row.fileId">
                <el-tag size="small" type="info" >ID: {{ row.fileId }}</el-tag>
                <el-divider direction="vertical" />
                <el-button type="primary" size="small" @click="handlePreviewContract(row)">
                  <el-icon class="el-icon--left"><View /></el-icon>
                  预览
                </el-button>
                <el-button type="success" size="small" @click="previewRowFile(row)">
                  <el-icon class="el-icon--left"><Download /></el-icon>
                  下载
                </el-button>
              </div>

            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="contractStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.contractStatus)">
              {{ getStatusLabel(row.contractStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="contractAmount" label="合同金额" width="120">
          <template #default="{ row }">
            <span v-if="row.contractAmount">
              ¥{{ formatAmount(row.contractAmount) }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="initiatorName" label="发起人" width="120" />
        <el-table-column prop="startDate" label="开始日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.startDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.endDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="row.contractStatus === 'DRAFT'"
              type="primary"
              link
              size="small"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              type="warning"
              link
              size="small"
              @click="handleGoToSign(row)"
            >
              去盖章
            </el-button>
            <el-button
              v-if="row.contractStatus === 'DRAFT'"
              type="danger"
              link
              size="small"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-area">
        <el-pagination
          background
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :total="Number(pagination.total)"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="120px"
        class="contract-form"
      >
        <el-scrollbar class="dialog-scroll">
          <div class="form-section">
            <div class="section-title">
              <el-icon><Document /></el-icon>
              <span>基本信息</span>
            </div>
            <div class="two-columns">
              <el-form-item label="合同名称" prop="contractName">
                <el-input v-model="form.contractName" placeholder="请输入合同名称" />
              </el-form-item>
              <el-form-item label="合同类型" prop="contractType">
                <el-input v-model="form.contractType" placeholder="请输入合同类型" />
              </el-form-item>
              <el-form-item label="合同金额" prop="contractAmount">
                <el-input-number
                  v-model="form.contractAmount"
                  :precision="2"
                  :min="0"
                  :max="999999999"
                  style="width: 100%"
                  placeholder="请输入合同金额"
                />
              </el-form-item>
              <el-form-item class="full-width" label="合同文件" prop="fileId">
                <!-- 已有关联文件信息（放在上传区域上方，确保立刻可见） -->
                <div v-if="form.fileId" class="file-info-card">
                  <div class="file-row file-actions">
                    <el-tag type="success" size="small">
                      <el-icon><Document /></el-icon>
                      文件ID: {{ form.fileId }}
                    </el-tag>
                    <el-divider direction="vertical" />
                    <el-button type="primary" link size="small" @click="previewFile">
                      <el-icon class="el-icon--left"><View /></el-icon>
                      预览/下载
                    </el-button>
                    <el-button type="danger" link size="small" @click="handleFileRemove">
                      <el-icon class="el-icon--left"><Delete /></el-icon>
                      移除
                    </el-button>
                    <span class="file-tip">可重新上传文件替换</span>
                  </div>
                  <div v-if="uploadedFile" class="file-row file-meta-tags">
                    <el-tag type="info" size="small" effect="plain" class="tag name">
                      {{ uploadedFile.name || '未命名文件' }}
                    </el-tag>
                    <el-tag size="small" effect="plain" class="tag">
                      {{ formatFileSize(uploadedFile.size) }}
                    </el-tag>
                    <el-tag v-if="uploadedFile.type" size="small" effect="plain" class="tag">
                      {{ uploadedFile.type }}
                    </el-tag>
                  </div>
                </div>

                <el-upload
                  ref="fileUploadRef"
                  :auto-upload="true"
                  :limit="1"
                  :on-success="handleFileUploadSuccess"
                  :on-remove="handleFileRemove"
                  :on-error="handleFileUploadError"
                  :before-upload="beforeFileUpload"
                  :action="fileUploadAction"
                  :headers="fileUploadHeaders"
                  accept=".pdf"
                  drag
                >
                  <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                  <div class="el-upload__text">
                    将文件拖到此处，或<em>点击上传</em>
                  </div>
                  <template #tip>
                    <div class="el-upload__tip">
                      仅支持 PDF 格式，单个文件不超过 50MB
                    </div>
                  </template>
                </el-upload>
              </el-form-item>
            </div>
          </div>

          <div class="form-section">
            <div class="section-title">
              <el-icon><Calendar /></el-icon>
              <span>时间信息</span>
            </div>
            <div class="two-columns">
              <el-form-item label="开始日期" prop="startDate">
                <el-date-picker
                  v-model="form.startDate"
                  type="date"
                  placeholder="选择开始日期"
                  style="width: 100%"
                  value-format="YYYY-MM-DD"
                />
              </el-form-item>
              <el-form-item label="结束日期" prop="endDate">
                <el-date-picker
                  v-model="form.endDate"
                  type="date"
                  placeholder="选择结束日期"
                  style="width: 100%"
                  value-format="YYYY-MM-DD"
                />
              </el-form-item>
            </div>
          </div>

          <div class="form-section">
            <div class="section-title">
              <el-icon><Edit /></el-icon>
              <span>备注信息</span>
            </div>
            <el-form-item label="备注" prop="remark">
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注"
                maxlength="500"
                show-word-limit
              />
            </el-form-item>
          </div>
        </el-scrollbar>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 提交审核对话框 -->
    <el-dialog
      v-model="submitReviewDialogVisible"
      title="提交审核"
      width="500px"
    >
      <el-form :model="submitReviewForm" label-width="100px">
        <el-form-item label="审核人ID">
          <el-input-number
            v-model="submitReviewForm.reviewerId"
            :min="1"
            style="width: 100%"
            placeholder="请输入审核人ID"
          />
        </el-form-item>
        <el-form-item label="审核人姓名">
          <el-input
            v-model="submitReviewForm.reviewerName"
            placeholder="请输入审核人姓名"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="submitReviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitReviewConfirm">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="reviewDialogVisible"
      title="审核合同"
      width="500px"
    >
      <el-form :model="reviewForm" label-width="100px">
        <el-form-item label="审核结果">
          <el-radio-group v-model="reviewForm.approved">
            <el-radio :label="true">通过</el-radio>
            <el-radio :label="false">不通过</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见">
          <el-input
            v-model="reviewForm.reviewComment"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="reviewDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleReviewConfirm">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 合同预览抽屉 -->
    <el-drawer
      v-model="previewDialogVisible"
      :title="`预览合同 - ${previewFileName}`"
      direction="rtl"
      size="100%"
      :close-on-click-modal="false"
      :close-on-press-escape="true"
      class="preview-drawer"
      @close="handlePreviewClose"
    >
      <template #header="{ close }">
        <div class="drawer-header">
          <div class="header-info">
            <el-icon class="header-icon"><Document /></el-icon>
            <span class="header-title">{{ previewFileName }}</span>
            <el-tag type="info" size="small" style="margin-left: 12px;">预览模式</el-tag>
          </div>
          <div class="header-actions">
            <el-button @click="downloadCurrentFile" size="small" type="primary">
              <el-icon class="el-icon--left"><Download /></el-icon>
              下载文件
            </el-button>
            <el-button @click="printCurrentFile" size="small">
              <el-icon class="el-icon--left"><Printer /></el-icon>
              打印
            </el-button>
            <el-button @click="handleClosePreview" size="small" plain>
              <el-icon class="el-icon--left"><Close /></el-icon>
              关闭
            </el-button>
          </div>
        </div>
      </template>

      <div class="drawer-content">
        <div v-if="previewLoading" class="loading-container">
          <el-icon class="is-loading loading-icon"><Loading /></el-icon>
          <p class="loading-text">正在加载 PDF 文件...</p>
        </div>
        <PdfViewer
          v-else-if="previewFileUrl"
          :file-url="previewFileUrl"
          mode="preview"
        />
        <div v-else class="empty-container">
          <el-icon class="empty-icon"><Document /></el-icon>
          <p class="empty-text">暂无文件内容</p>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Document,
  Plus,
  Delete,
  Search,
  Refresh,
  Edit,
  UploadFilled,
  View,
  Download,
  Printer,
  Close,
  Loading,
  Calendar
} from '@element-plus/icons-vue'
import { contractApi, fileApi } from '@/api'
import { useUserStore } from '@/stores/user'
import PdfViewer from '@/components/PdfViewer.vue'

// 路由实例
const router = useRouter()

// 搜索表单
const searchForm = reactive({
  contractName: '',
  contractStatus: ''
})

// 表格数据
const loading = ref(false)
const tableData = ref([])
const selectedIds = ref([])

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = computed(() => (form.id ? '编辑合同' : '新建合同'))
const submitLoading = ref(false)
const formRef = ref(null)
const fileUploadRef = ref(null)

// 用户信息
const userStore = useUserStore()

// 文件上传配置
const fileUploadAction = '/zapi-allin-sys/file/upload'
const fileUploadHeaders = computed(() => {
  const token = localStorage.getItem('zapi_token')
  return {
    Authorization: token ? `Bearer ${token}` : ''
  }
})

// 表单数据
const form = reactive({
  id: null,
  contractName: '',
  contractType: '',
  fileId: null,
  contractAmount: null,
  startDate: '',
  endDate: '',
  remark: ''
})

// 已上传文件信息（显示用）
const uploadedFile = ref(null)

// 表单验证规则
const formRules = {
  contractName: [
    { required: true, message: '请输入合同名称', trigger: 'blur' }
  ],
  fileId: [
    { required: true, message: '请上传合同文件', trigger: 'change' }
  ]
}

// 提交审核对话框
const submitReviewDialogVisible = ref(false)
const currentSubmitContract = ref(null)
const submitReviewForm = reactive({
  reviewerId: null,
  reviewerName: ''
})

// 审核对话框
const reviewDialogVisible = ref(false)
const currentReviewContract = ref(null)
const reviewForm = reactive({
  approved: true,
  reviewComment: ''
})

// 预览对话框
const previewDialogVisible = ref(false)
const previewFileUrl = ref('')
const previewFileName = ref('')
const previewLoading = ref(false)

// 获取状态标签
const getStatusLabel = (status) => {
  const map = {
    DRAFT: '草稿',
    REVIEWING: '审核中',
    PENDING_SIGN: '待签署',
    SIGNED: '签署完成',
    ARCHIVED: '归档'
  }
  return map[status] || status
}

// 获取状态标签类型
const getStatusTagType = (status) => {
  const map = {
    DRAFT: 'info',
    REVIEWING: 'warning',
    PENDING_SIGN: 'primary',
    SIGNED: 'success',
    ARCHIVED: ''
  }
  return map[status] || ''
}

// 格式化金额
const formatAmount = (amount) => {
  if (!amount) return '0.00'
  return Number(amount).toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  return date
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 格式化文件大小
const formatFileSize = (size) => {
  if (size === null || size === undefined) return '-'
  const units = ['B', 'KB', 'MB', 'GB']
  let idx = 0
  let s = Number(size)
  while (s >= 1024 && idx < units.length - 1) {
    s /= 1024
    idx++
  }
  return `${s.toFixed(2)} ${units[idx]}`
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      // userId由后端从系统获取，不需要前端传递
      contractName: searchForm.contractName || undefined,
      contractStatus: searchForm.contractStatus || undefined
    }

    const res = await contractApi.pageContracts(params)
    if (res.code === 200) {
      tableData.value = res.data.records || []
      pagination.total = Number(res.data.total) || 0
    } else {
      ElMessage.error(res.message || res.msg || '加载数据失败')
    }

    // 调试：输出数据
    if (tableData.value.length > 0) {
    }
  } catch (error) {
    console.error('加载合同数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadData()
}

// 重置
const handleReset = () => {
  searchForm.contractName = ''
  searchForm.contractStatus = ''
  pagination.current = 1
  loadData()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  loadData()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  loadData()
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 新建
const handleCreate = () => {
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  resetForm()
  dialogVisible.value = true

  try {
    // 以ID查询最新详情，确保拿到 fileId 等字段
    const detail = await contractApi.getContractById(row.id)
    if (detail && detail.code === 200 && detail.data) {
      const d = detail.data
      form.id = d.id
      form.contractName = d.contractName || ''
      form.contractType = d.contractType || ''
      form.fileId = d.fileId || null
      form.contractAmount = d.contractAmount || null
      form.startDate = d.startDate || ''
      form.endDate = d.endDate || ''
      form.remark = d.remark || ''
    } else {
      // 回退到列表行数据
      form.id = row.id
      form.contractName = row.contractName || ''
      form.contractType = row.contractType || ''
      form.fileId = row.fileId || null
      form.contractAmount = row.contractAmount || null
      form.startDate = row.startDate || ''
      form.endDate = row.endDate || ''
      form.remark = row.remark || ''
    }

    // 根据 fileId 拉取文件元信息用于回显
    if (form.fileId) {
      fileApi.getFileInfo(form.fileId).then(res => {
        if (res && res.code === 200 && res.data) {
          const info = res.data
          uploadedFile.value = {
            name: info.originalFileName || info.fileName || info.name || '',
            size: info.fileSize || info.size || null,
            type: info.fileType || info.contentType || ''
          }
        }
      }).catch(() => {})
    }
  } catch (e) {
    // 出错时也至少填充基础字段，避免空白
    form.id = row.id
    form.contractName = row.contractName || ''
    form.contractType = row.contractType || ''
    form.fileId = row.fileId || null
    form.contractAmount = row.contractAmount || null
    form.startDate = row.startDate || ''
    form.endDate = row.endDate || ''
    form.remark = row.remark || ''
  }
}

// 查看
const handleView = (row) => {
  ElMessageBox.alert(
    `合同编号：${row.contractNo}<br/>合同名称：${row.contractName}<br/>合同类型：${row.contractType || '-'}<br/>状态：${getStatusLabel(row.contractStatus)}<br/>金额：${row.contractAmount ? '¥' + formatAmount(row.contractAmount) : '-'}<br/>发起人：${row.initiatorName || '-'}<br/>备注：${row.remark || '-'}`,
    '合同详情',
    {
      dangerouslyUseHTMLString: true
    }
  )
}

// 提交
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      if (form.id) {
        // 更新
        const res = await contractApi.updateContract(form.id, form)
        if (res.code === 200) {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          loadData()
        } else {
          ElMessage.error(res.message || '更新失败')
        }
      } else {
        // 创建
        const res = await contractApi.createContract(form)
        if (res.code === 200) {
          ElMessage.success('创建成功')
          dialogVisible.value = false
          loadData()
        } else {
          ElMessage.error(res.message || '创建失败')
        }
      }
    } catch (error) {
      console.error('提交失败:', error)
      ElMessage.error('操作失败')
    } finally {
      submitLoading.value = false
    }
  })
}

// 提交审核
const handleSubmitReview = (row) => {
  currentSubmitContract.value = row
  submitReviewForm.reviewerId = null
  submitReviewForm.reviewerName = ''
  submitReviewDialogVisible.value = true
}

// 确认提交审核
const handleSubmitReviewConfirm = async () => {
  if (!submitReviewForm.reviewerId) {
    ElMessage.warning('请输入审核人ID')
    return
  }
  if (!submitReviewForm.reviewerName) {
    ElMessage.warning('请输入审核人姓名')
    return
  }

  try {
    const res = await contractApi.submitForReview(currentSubmitContract.value.id, submitReviewForm)
    if (res.code === 200) {
      ElMessage.success('提交审核成功')
      submitReviewDialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.message || '提交审核失败')
    }
  } catch (error) {
    console.error('提交审核失败:', error)
    ElMessage.error('提交审核失败')
  }
}

// 审核
const handleReview = (row) => {
  currentReviewContract.value = row
  reviewForm.approved = true
  reviewForm.reviewComment = ''
  reviewDialogVisible.value = true
}

// 确认审核
const handleReviewConfirm = async () => {
  try {
    const res = await contractApi.reviewContract(currentReviewContract.value.id, reviewForm)
    if (res.code === 200) {
      ElMessage.success('审核成功')
      reviewDialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(res.message || '审核失败')
    }
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败')
  }
}

// 签署
const handleSign = async (row) => {
  try {
    await ElMessageBox.confirm('确认签署该合同？', '提示', {
      type: 'warning'
    })

    const res = await contractApi.signContract(row.id)
    if (res.code === 200) {
      ElMessage.success('签署成功')
      loadData()
    } else {
      ElMessage.error(res.message || '签署失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('签署失败:', error)
      ElMessage.error('签署失败')
    }
  }
}

// 去盖章
const handleGoToSign = (row) => {
  router.push({
    path: '/home/contract/sign',
    query: {
      id: row.id,
      fileId: row.fileId
    }
  })
}

// 归档
const handleArchive = async (row) => {
  try {
    await ElMessageBox.confirm('确认归档该合同？', '提示', {
      type: 'warning'
    })

    const res = await contractApi.archiveContract(row.id)
    if (res.code === 200) {
      ElMessage.success('归档成功')
      loadData()
    } else {
      ElMessage.error(res.message || '归档失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('归档失败:', error)
      ElMessage.error('归档失败')
    }
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该合同？', '提示', {
      type: 'warning'
    })

    const res = await contractApi.deleteContract(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的合同')
    return
  }

  try {
    await ElMessageBox.confirm(`确认删除选中的 ${selectedIds.value.length} 个合同？`, '提示', {
      type: 'warning'
    })

    const res = await contractApi.batchDelete(selectedIds.value)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      selectedIds.value = []
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 文件上传前验证
const beforeFileUpload = (file) => {
  const isValidType = file.name.toLowerCase().endsWith('.pdf')
  const isLt50M = file.size / 1024 / 1024 < 50

  if (!isValidType) {
    ElMessage.error('只能上传 PDF 格式的文件!')
    return false
  }
  if (!isLt50M) {
    ElMessage.error('文件大小不能超过 50MB!')
    return false
  }
  return true
}

// 文件上传成功
const handleFileUploadSuccess = (response, file) => {
  // 文件上传接口可能直接返回data，也可能包装在Result中
  if (response.code === 200 && response.data) {
    // 如果返回的是Result格式
    form.fileId = response.data.id || response.data.fileId || response.data
    ElMessage.success('文件上传成功')
    // 触发表单验证
    if (formRef.value) {
      formRef.value.validateField('fileId')
    }
    uploadedFile.value = {
      name: response.data.originalFileName || response.data.fileName || file.name,
      size: response.data.fileSize || file.size,
      type: response.data.fileType || file.type
    }
  } else if (response.id || response.fileId) {
    // 如果直接返回文件信息
    form.fileId = response.id || response.fileId
    ElMessage.success('文件上传成功')
    // 触发表单验证
    if (formRef.value) {
      formRef.value.validateField('fileId')
    }
    uploadedFile.value = {
      name: response.originalFileName || response.fileName || file.name,
      size: response.fileSize || file.size,
      type: response.fileType || file.type
    }
  } else {
    ElMessage.error(response.message || response.msg || '文件上传失败')
  }
}

// 文件上传失败
const handleFileUploadError = (error) => {
  console.error('文件上传失败:', error)
  ElMessage.error('文件上传失败，请重试')
}

// 文件移除
const handleFileRemove = () => {
  form.fileId = null
  if (fileUploadRef.value) {
    fileUploadRef.value.clearFiles()
  }
  // 触发表单验证
  if (formRef.value) {
    formRef.value.validateField('fileId')
  }
  uploadedFile.value = null
}

// 预览文件
const previewFile = () => {
  if (form.fileId) {
    // 通过接口以blob方式下载，保证鉴权头携带
    fileApi.downloadFile(form.fileId).then((res) => {
      const contentType = res.headers?.['content-type'] || 'application/octet-stream'
      const blob = new Blob([res.data], { type: contentType })
      const url = URL.createObjectURL(blob)
      window.open(url)
      setTimeout(() => URL.revokeObjectURL(url), 10000)
    })
  }
}

// 预览合同
const handlePreviewContract = async (row) => {
  if (!row.fileId) {
    ElMessage.warning('该合同暂无文件')
    return
  }

  // 先清空之前的 URL，避免缓存问题
  previewFileUrl.value = ''
  previewFileName.value = row.contractName || '合同文件'
  previewLoading.value = true
  previewDialogVisible.value = true

  // 等待抽屉完全打开后再设置 PDF URL
  await nextTick()
  setTimeout(() => {
    previewFileUrl.value = `/zapi-allin-sys/file/download/${row.fileId}`
    previewLoading.value = false
  }, 200)
}

// 下载当前预览文件
const downloadCurrentFile = () => {
  if (previewFileUrl.value) {
    window.open(previewFileUrl.value)
  } else {
    ElMessage.warning('文件URL不存在')
  }
}

// 打印当前预览文件
const printCurrentFile = () => {
  if (previewFileUrl.value) {
    const printWindow = window.open(previewFileUrl.value)
    if (printWindow) {
      printWindow.onload = () => {
        printWindow.print()
      }
    }
  } else {
    ElMessage.warning('文件URL不存在')
  }
}

// 手动关闭预览抽屉
const handleClosePreview = () => {
  previewDialogVisible.value = false
}

// 关闭预览抽屉时的清理
const handlePreviewClose = () => {
  // 延迟清理，确保抽屉关闭动画完成
  setTimeout(() => {
    previewFileUrl.value = ''
    previewFileName.value = ''
    previewLoading.value = false
  }, 300)
}

// 表格行文件预览/下载
const previewRowFile = (row) => {
  if (!row.fileId) return
  fileApi.downloadFile(row.fileId).then((res) => {
    const contentType = res.headers?.['content-type'] || 'application/octet-stream'
    const blob = new Blob([res.data], { type: contentType })
    const url = URL.createObjectURL(blob)
    window.open(url)
    setTimeout(() => URL.revokeObjectURL(url), 10000)
  })
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.contractName = ''
  form.contractType = ''
  form.fileId = null
  form.contractAmount = null
  form.startDate = ''
  form.endDate = ''
  form.remark = ''
  if (formRef.value) {
    formRef.value.clearValidate()
  }
  if (fileUploadRef.value) {
    fileUploadRef.value.clearFiles()
  }
  uploadedFile.value = null
}

// 页面加载时获取数据
onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.contract-container {
  padding: 20px;
  background: var(--el-bg-color-page);
  min-height: calc(100vh - 80px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
  color: var(--el-text-color-primary);

  span {
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

.header-actions {
  display: flex;
  gap: 10px;
}

.search-area {
  margin-bottom: 20px;
  padding: 20px;
  background: var(--el-fill-color-lighter);
  border-radius: 4px;
}

.pagination-area {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table) {
  .el-table__cell {
    padding: 12px 0;
  }
}

.contract-form {
  padding-right: 8px;
}

.dialog-scroll {
  max-height: 420px;
  padding-right: 8px;
}

.form-section {
  background: var(--el-fill-color-lighter);
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 10px;
  padding: 16px;
  margin-bottom: 16px;
}

/* 文件操作按钮样式 */
.file-actions {
  display: flex;
  gap: 8px;
  align-items: center;
  .el-button {
    margin: 0;
  }
}

/* 预览抽屉样式 */
:deep(.preview-drawer) {
  .el-drawer__body {
    padding: 0;
    display: flex;
    flex-direction: column;
    height: 100%;
    background: var(--el-bg-color-page);
  }
}

.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color);
  box-shadow: var(--el-box-shadow-lighter);
}

.header-info {
  display: flex;
  align-items: center;
  flex: 1;
}

.header-icon {
  font-size: 18px;
  color: var(--el-color-primary);
  margin-right: 8px;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

.drawer-content {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  background: var(--el-bg-color-page);
}

.loading-container,
.empty-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: var(--el-text-color-secondary);
}

.loading-icon {
  font-size: 32px;
  color: var(--el-color-primary);
  margin-bottom: 16px;
}

.loading-text {
  font-size: 14px;
  margin: 0;
  color: var(--el-text-color-regular);
}

.empty-icon {
  font-size: 48px;
  color: var(--el-text-color-placeholder);
  margin-bottom: 16px;
}

.empty-text {
  font-size: 14px;
  margin: 0;
  color: var(--el-text-color-secondary);
}

.section-title {
  display: flex;
  align-items: center;
  font-size: 15px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin-bottom: 14px;

  .el-icon {
    margin-right: 6px;
    color: var(--el-color-primary);
  }
}

.two-columns {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 16px;
}

.two-columns :deep(.el-form-item) {
  margin-right: 0;
}

.two-columns :deep(.full-width) {
  grid-column: 1 / -1;
}

.file-info {
  margin-top: 10px;
  display: flex;
  align-items: center;
}

.file-info-card {
  margin-bottom: 10px;
  padding: 8px 12px;
  border: 1px solid rgba(99, 102, 241, 0.2);
  background: rgba(99, 102, 241, 0.05);
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.file-row { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }

.file-tip {
  margin-left: 10px;
  color: var(--el-text-color-placeholder);
  font-size: 12px;
}

.file-meta {
  margin-left: 12px;
  color: var(--el-text-color-secondary);
  font-size: 12px;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.file-meta-tags { align-items: center; gap: 8px; }

.file-meta-tags .tag :deep(.el-tag__content) {
  max-width: 220px;
  display: inline-block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-meta-tags .tag.name :deep(.el-tag__content) { max-width: 320px; }

:deep(.el-upload) {
  width: 100%;
}

:deep(.el-upload-dragger) {
  width: 100%;
}
</style>

