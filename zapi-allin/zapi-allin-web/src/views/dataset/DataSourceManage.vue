<template>
  <div class="data-source-manage-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="page-title-section">
          <h1 class="page-title">数据源管理</h1>
        </div>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="openAddDialog">
          <el-icon><Plus /></el-icon>
          新增数据源
        </el-button>
        <el-button @click="fetchData">
          <el-icon><Refresh /></el-icon>
          刷新数据
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <div class="content-card">
        <!-- 数据表格 -->
        <el-skeleton :loading="loading" animated>
          <el-table
              :data="dataSourceList"
              class="data-source-table"
              empty-text="暂无数据"
              v-loading="loading"
          >
            <el-table-column prop="dbType" label="数据库类型" min-width="120">
              <template #default="{ row }">
                <el-tag :type="getDbTypeTagType(row.dbType)" size="small">
                  {{ row.dbType.toUpperCase() }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="url" label="连接URL" min-width="200" show-overflow-tooltip />
            <el-table-column prop="username" label="用户名" min-width="120" show-overflow-tooltip />
            <el-table-column prop="schemaName" label="Schema名称" min-width="120" show-overflow-tooltip />
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="editDataSource(row)">
                  <el-icon><Edit /></el-icon>
                  编辑
                </el-button>
                <el-button size="small" type="danger" @click="deleteDataSourceClick(row.id)">
                  <el-icon><Delete /></el-icon>
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-skeleton>

        <!-- 分页 -->
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

    <!-- 弹窗表单 -->
    <el-dialog
        :title="isEdit ? '编辑数据源' : '新增数据源'"
        v-model="dialogVisible"
        width="600px"
        center
        :close-on-click-modal="false"
        class="data-source-dialog"
    >
      <el-form ref="formRef" :model="formData" :rules="rules" label-width="120px">
        <el-form-item label="数据库类型" prop="dbType">
          <el-select v-model="formData.dbType" placeholder="请选择数据库类型" style="width: 100%">
            <el-option label="MySQL" value="mysql" />
            <el-option label="PostgreSQL" value="postgresql" />
            <el-option label="Oracle" value="oracle" />
            <el-option label="SQL Server" value="sqlserver" />
            <el-option label="达梦" value="dm" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="连接URL" prop="url">
          <el-input v-model="formData.url" placeholder="jdbc:mysql://localhost:3306/database" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入数据库用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="formData.password" type="password" placeholder="请输入数据库密码" show-password />
        </el-form-item>
        <el-form-item label="Schema名称" prop="schemaName">
          <el-input v-model="formData.schemaName" placeholder="请输入Schema名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" @click="submitForm">提交</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import useDataSource from '../../hooks/useDataSource'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Edit, Delete } from '@element-plus/icons-vue'

const {
  dataSourceList,
  loading,
  total,
  currentPage,
  pageSize,
  fetchData,
  createDataSource,
  updateDataSource,
  deleteDataSource
} = useDataSource()

// 表单相关
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const formData = ref({
  id: undefined,
  dbType: '',
  url: '',
  username: '',
  password: '',
  schemaName: ''
})

const rules = {
  dbType: [{ required: true, message: '请选择数据库类型', trigger: 'change' }],
  url: [{ required: true, message: '请输入数据库连接URL', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  schemaName: [{ required: true, message: '请输入Schema名称', trigger: 'blur' }]
}

// 获取数据库类型标签样式
const getDbTypeTagType = (dbType) => {
  const typeMap = {
    'mysql': 'success',
    'postgresql': 'primary',
    'oracle': 'warning',
    'sqlserver': 'info',
    'dm': 'danger',
    'other': ''
  }
  return typeMap[dbType.toLowerCase()] || ''
}

// 打开新增弹窗
function openAddDialog() {
  isEdit.value = false
  formData.value = {
    dbType: '',
    url: '',
    username: '',
    password: '',
    schemaName: ''
  }
  dialogVisible.value = true
}

// 编辑数据源
function editDataSource(row) {
  isEdit.value = true
  formData.value = { ...row }
  dialogVisible.value = true
}

// 提交表单
function submitForm() {
  formRef.value.validate(valid => {
    if (valid) {
      const apiCall = isEdit.value ? updateDataSource(formData.value) : createDataSource(formData.value)

      apiCall.then(() => {
        ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
        dialogVisible.value = false
        fetchData()
      }).catch(error => {
        ElMessage.error(`操作失败: ${error.message}`)
      })
    } else {
      return false
    }
  })
}

// 删除数据源
function deleteDataSourceClick(id) {
  ElMessageBox.confirm('确定要删除该数据源吗？删除后无法恢复。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    deleteDataSource(id).then(() => {
      ElMessage.success('删除成功')
      fetchData()
    }).catch(error => {
      ElMessage.error(`删除失败: ${error.message}`)
    })
  }).catch(() => {
    // 用户取消删除
  })
}

// 表单重置
function resetForm() {
  formRef.value.resetFields()
}

// 分页变化
function handlePageChange(page) {
  currentPage.value = page
  fetchData()
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.data-source-manage-page {
  padding: 24px;
  background: var(--el-bg-color-page);
  min-height: 100vh;
}

/* 页面头部 */

.header-content {
  flex: 1;
}

.page-title-section {
  margin-bottom: 16px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.header-actions :deep(.el-button) {
  border-radius: 8px;
  border: none;
  font-weight: 500;
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.header-actions :deep(.el-button--primary) {
  background: var(--el-color-primary);
  color: white;
}

.header-actions :deep(.el-button--primary:hover) {
  background: var(--el-color-primary-dark-2);
}

.header-actions :deep(.el-button .el-icon) {
  font-size: 16px;
}

/* 主要内容区域 */
.main-content {
  background: var(--el-bg-color);
  border-radius: 12px;
  box-shadow: var(--el-box-shadow-light);
  overflow: hidden;
}

.content-card {
  padding: 24px;
}

/* 表格样式 */
.data-source-table {
  margin-bottom: 24px;
}

.data-source-table :deep(.el-table) {
  border: none;
  background: transparent;
}

.data-source-table :deep(.el-table__header) {
  background: transparent;
}

.data-source-table :deep(.el-table__header .el-table__cell) {
  background: transparent;
  border: none;
  color: var(--text-secondary);
  font-weight: 500;
  padding: 16px 0;
}

.data-source-table :deep(.el-table__row) {
  background: transparent;
  border: none;
}

.data-source-table :deep(.el-table__row:hover) {
  background: var(--bg-tertiary);
}

.data-source-table :deep(.el-table__cell) {
  border: none;
  padding: 16px 0;
  background: transparent;
}

.data-source-table :deep(.el-table__inner-wrapper) {
  border: none;
}

.data-source-table :deep(.el-table__border-line) {
  display: none;
}

.data-source-table :deep(.el-table__border-line--left),
.data-source-table :deep(.el-table__border-line--right),
.data-source-table :deep(.el-table__border-line--top),
.data-source-table :deep(.el-table__border-line--bottom) {
  display: none;
}

.data-source-table :deep(.el-button) {
  border-radius: 6px;
  padding: 6px 12px;
  font-size: 12px;
  border: none;
  margin-right: 8px;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.data-source-table :deep(.el-button--primary) {
  background: var(--el-color-primary);
  color: white;
}

.data-source-table :deep(.el-button--primary:hover) {
  background: var(--el-color-primary-dark-2);
}

.data-source-table :deep(.el-button--danger) {
  background: var(--el-color-danger);
  color: white;
}

.data-source-table :deep(.el-button--danger:hover) {
  background: var(--el-color-danger-dark-2);
}

.data-source-table :deep(.el-button .el-icon) {
  font-size: 14px;
}

.data-source-table :deep(.el-tag) {
  border-radius: 4px;
  border: none;
  font-weight: 500;
}

/* 分页样式 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
}

/* 对话框样式 */
.data-source-dialog :deep(.el-dialog__header) {
  background: var(--el-bg-color);
  border-bottom: 1px solid var(--el-border-color-lighter);
  padding: 20px 20px 16px 20px;
}

.data-source-dialog :deep(.el-dialog__title) {
  color: var(--el-text-color-primary);
  font-weight: 600;
  font-size: 16px;
}

.data-source-dialog :deep(.el-dialog__body) {
  padding: 20px;
  background: var(--el-bg-color);
}

.data-source-dialog :deep(.el-form-item__label) {
  color: var(--el-text-color-primary);
  font-weight: 500;
}

.data-source-dialog :deep(.el-input__inner),
.data-source-dialog :deep(.el-textarea__inner) {
  border-color: var(--el-border-color-light);
  background-color: var(--el-fill-color-light);
  color: var(--el-text-color-regular);
  border-radius: 6px;
}

.data-source-dialog :deep(.el-input__inner:focus),
.data-source-dialog :deep(.el-textarea__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

.data-source-dialog :deep(.el-select .el-input__inner) {
  border-color: var(--el-border-color-light);
  background-color: var(--el-fill-color-light);
  border-radius: 6px;
}

.data-source-dialog :deep(.el-select .el-input__inner:focus) {
  border-color: var(--el-color-primary);
  box-shadow: 0 0 0 2px var(--el-color-primary-light-8);
}

.dialog-footer {
  padding: 16px 20px 20px 20px;
  border-top: 1px solid var(--el-border-color-lighter);
  text-align: right;
  background: var(--el-bg-color);
}

.dialog-footer :deep(.el-button) {
  border-radius: 8px;
  padding: 8px 16px;
  font-size: 14px;
  margin-left: 12px;
  border: none;
}

.dialog-footer :deep(.el-button--primary) {
  background-color: var(--el-color-primary);
  border-color: var(--el-color-primary);
}

.dialog-footer :deep(.el-button--primary:hover) {
  background-color: var(--el-color-primary-dark-2);
  border-color: var(--el-color-primary-dark-2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 16px;
  }

  .header-actions {
    width: 100%;
    justify-content: flex-start;
  }

  .content-card {
    padding: 16px;
  }
}
</style>
