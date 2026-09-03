<template>
  <div class="notifications-page">
    <div class="page-header">
      <div class="header-content">
          <h1 class="page-title">消息模板</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openCreate">
          <el-icon><Plus /></el-icon>
          新增模板
        </el-button>
        <el-button @click="fetchTemplates">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <div class="main-content">
      <div class="content-card">
        <div class="template-list">
          <el-table :data="messageTemplates" class="template-table" v-loading="loading">
            <el-table-column prop="templateName" label="模板名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="templateCode" label="模板编码" min-width="120" show-overflow-tooltip />
            <el-table-column prop="templateContent" label="模板内容" min-width="300" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 'ENABLED' ? 'success' : 'danger'" size="small">
                  {{ row.status === 'ENABLED' ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180">
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="250" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="editTemplate(row)">
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button size="small" type="success" @click="testTemplate(row)">
                  测试
                </el-button>
                <el-button
                  v-if="row.status === 'DISABLED'"
                  size="small"
                  type="warning"
                  @click="enableTemplate(row.id)"
                >
                  启用
                </el-button>
                <el-button
                  v-else
                  size="small"
                  type="info"
                  @click="disableTemplate(row.id)"
                >
                  禁用
                </el-button>
                <el-button size="small" type="danger" @click="deleteTemplate(row)">
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-wrapper">
          <el-pagination
            layout="total, prev, pager, next, jumper"
            :total="Number(total)"
            v-model:current-page="currentPage"
            :page-size="pageSize"
            background
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>

    <!-- 模板编辑对话框 -->
    <el-dialog
      title="消息模板"
      v-model="showTemplateDialog"
      width="700px"
      center
      :close-on-click-modal="false"
    >
      <el-form :model="templateForm" :rules="templateFormRules" ref="templateFormRef" label-width="100px">
        <el-form-item label="模板编码" prop="templateCode" required>
          <el-input v-model="templateForm.templateCode" placeholder="请输入模板编码" />
        </el-form-item>
        <el-form-item label="模板名称" prop="templateName" required>
          <el-input v-model="templateForm.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板标题" prop="templateTitle" required>
          <el-input v-model="templateForm.templateTitle" placeholder="请输入模板标题" />
        </el-form-item>
        <el-form-item label="模板内容" prop="templateContent" required>
          <el-input
            v-model="templateForm.templateContent"
            type="textarea"
            :rows="6"
            placeholder="请输入模板内容，支持变量占位符如 {name}、{time} 等"
          />
        </el-form-item>
        <el-form-item label="模板描述" prop="description">
          <el-input
            v-model="templateForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入模板描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="templateForm.status" :active-value="1" :inactive-value="0" />
          <span style="margin-left: 8px; color: var(--el-text-color-secondary);">
            {{ templateForm.status === 1 ? '启用' : '禁用' }}
          </span>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showTemplateDialog = false">取消</el-button>
          <el-button type="primary" @click="submitTemplate" :loading="templateSaving">
            {{ isEditTemplate ? '更新' : '创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 模板测试对话框 -->
    <el-dialog
      title="测试模板"
      v-model="testTemplateDialogVisible"
      width="500px"
      center
    >
      <el-form :model="testForm" label-width="100px">
        <el-form-item label="测试参数">
          <el-input
            v-model="testForm.params"
            type="textarea"
            :rows="4"
            placeholder="请输入JSON格式的测试参数，如：{'name': '张三', 'time': '2024-01-01'}"
          />
        </el-form-item>
      </el-form>
      <div v-if="testResult" class="test-result">
        <h4>渲染结果：</h4>
        <div class="result-content">{{ testResult }}</div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="testTemplateDialogVisible = false">关闭</el-button>
          <el-button type="primary" @click="renderTemplate">渲染测试</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { messageTemplateApi } from '@/api/index.js'

const loading = ref(false)
const templateSaving = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const showTemplateDialog = ref(false)
const isEditTemplate = ref(false)
const templateForm = reactive({
  id: null,
  templateCode: '',
  templateName: '',
  templateTitle: '',
  templateContent: '',
  description: '',
  status: 1
})

const templateFormRules = {
  templateCode: [{ required: true, message: '请输入模板编码', trigger: 'blur' }],
  templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  templateTitle: [{ required: true, message: '请输入模板标题', trigger: 'blur' }],
  templateContent: [{ required: true, message: '请输入模板内容', trigger: 'blur' }]
}

const templateFormRef = ref()
const messageTemplates = ref([])
const testTemplateDialogVisible = ref(false)
const currentTestTemplate = ref(null)
const testForm = reactive({
  params: ''
})
const testResult = ref('')

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const fetchTemplates = async () => {
  loading.value = true
  try {
    const response = await messageTemplateApi.getTemplates({
      current: currentPage.value,
      size: pageSize.value
    })

    if (response && response.data) {
      messageTemplates.value = response.data.records || response.data.list || []
      total.value = Number(response.data.total) || 0
    } else {
      messageTemplates.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取模板列表失败:', error)
    ElMessage.error('获取模板列表失败: ' + (error.message || '未知错误'))
    messageTemplates.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const editTemplate = (template) => {
  isEditTemplate.value = true
  Object.assign(templateForm, template)
  showTemplateDialog.value = true
}

const testTemplate = (template) => {
  currentTestTemplate.value = template
  testForm.params = '{"name": "测试用户", "time": "2024-01-01"}'
  testResult.value = ''
  testTemplateDialogVisible.value = true
}

const renderTemplate = async () => {
  if (!currentTestTemplate.value) return

  try {
    let params = {}
    if (testForm.params) {
      try {
        params = JSON.parse(testForm.params)
      } catch (e) {
        ElMessage.error('参数格式错误，请输入有效的JSON格式')
        return
      }
    }

    const response = await messageTemplateApi.renderTemplate(currentTestTemplate.value.templateCode, params)
    if (response && response.data) {
      testResult.value = response.data
    } else {
      ElMessage.error('渲染失败')
    }
  } catch (error) {
    console.error('渲染模板失败:', error)
    ElMessage.error('渲染失败: ' + (error.message || '未知错误'))
  }
}

const enableTemplate = async (templateId) => {
  try {
    const response = await messageTemplateApi.enableTemplate(templateId)
    if (response && (response.data === true || response.code === 200)) {
      ElMessage.success('启用成功')
      fetchTemplates()
    } else {
      ElMessage.error(response?.message || '启用失败')
    }
  } catch (error) {
    console.error('启用模板失败:', error)
    ElMessage.error('启用失败: ' + (error.message || '未知错误'))
  }
}

const disableTemplate = async (templateId) => {
  try {
    const response = await messageTemplateApi.disableTemplate(templateId)
    if (response && (response.data === true || response.code === 200)) {
      ElMessage.success('禁用成功')
      fetchTemplates()
    } else {
      ElMessage.error(response?.message || '禁用失败')
    }
  } catch (error) {
    console.error('禁用模板失败:', error)
    ElMessage.error('禁用失败: ' + (error.message || '未知错误'))
  }
}

const deleteTemplate = async (template) => {
  try {
    await ElMessageBox.confirm(`确定要删除模板 "${template.templateName}" 吗?`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const response = await messageTemplateApi.deleteTemplate(template.id)
    if (response && (response.data === true || response.code === 200)) {
      ElMessage.success('删除成功')
      fetchTemplates()
    } else {
      ElMessage.error(response?.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除模板失败:', error)
      ElMessage.error('删除失败: ' + (error.message || '未知错误'))
    }
  }
}

const submitTemplate = async () => {
  try {
    await templateFormRef.value.validate()

    templateSaving.value = true
    let response

    const templateData = {
      id: templateForm.id,
      templateCode: templateForm.templateCode,
      templateName: templateForm.templateName,
      templateTitle: templateForm.templateTitle,
      templateContent: templateForm.templateContent,
      description: templateForm.description,
      status: templateForm.status
    }

    if (isEditTemplate.value) {
      response = await messageTemplateApi.updateTemplate(templateData)
    } else {
      response = await messageTemplateApi.createTemplate(templateData)
    }

    if (response && (response.data === true || response.code === 200)) {
      ElMessage.success(isEditTemplate.value ? '更新成功' : '创建成功')
      showTemplateDialog.value = false
      resetTemplateForm()
      fetchTemplates()
    } else {
      ElMessage.error(response?.message || (isEditTemplate.value ? '更新失败' : '创建失败'))
    }
  } catch (error) {
    if (error !== false) {
      console.error('提交模板失败:', error)
      ElMessage.error((isEditTemplate.value ? '更新失败' : '创建失败') + ': ' + (error.message || '未知错误'))
    }
  } finally {
    templateSaving.value = false
  }
}

const resetTemplateForm = () => {
  Object.assign(templateForm, {
    id: null,
    templateCode: '',
    templateName: '',
    templateTitle: '',
    templateContent: '',
    description: '',
    status: 1
  })
  isEditTemplate.value = false
  templateFormRef.value?.resetFields()
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchTemplates()
}

const openCreate = () => {
  resetTemplateForm()
  showTemplateDialog.value = true
}

onMounted(() => {
  fetchTemplates()
})
</script>

<style scoped>
@import '../index.scss';

.test-result {
  margin-top: 20px;
  padding: 16px;
  background: var(--el-fill-color-light);
  border-radius: 8px;
  border: 1px solid var(--el-border-color-lighter);
}

.test-result h4 {
  margin: 0 0 12px 0;
  color: var(--el-text-color-primary);
  font-size: 14px;
  font-weight: 600;
}

.result-content {
  color: var(--el-text-color-regular);
  font-size: 14px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}
</style>

