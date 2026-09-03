<template>
  <div class="config-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">参数配置</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          添加参数
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon class="el-icon--left"><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <div class="config-container">
      <!-- 搜索区域 -->
      <div class="search-panel">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="参数名称">
            <el-input
              v-model="searchForm.configName"
              placeholder="请输入参数名称"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="参数键名">
            <el-input
              v-model="searchForm.configKey"
              placeholder="请输入参数键名"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="系统内置">
            <el-select v-model="searchForm.configType" placeholder="请选择" clearable style="width: 120px">
              <el-option label="全部" value="" />
              <el-option label="是" value="Y" />
              <el-option label="否" value="N" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon class="el-icon--left"><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon class="el-icon--left"><RefreshLeft /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 表格区域 -->
      <div class="table-panel">
        <el-table :data="configList" style="width: 100%" v-loading="loading" class="config-table">
          <el-table-column prop="configId" label="参数主键" width="100" />
          <el-table-column prop="configName" label="参数名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="configKey" label="参数键名" min-width="200" show-overflow-tooltip />
          <el-table-column prop="configValue" label="参数键值" min-width="150" show-overflow-tooltip />
          <el-table-column prop="configType" label="系统内置" width="100">
            <template #default="{ row }">
              <el-tag :type="row.configType === 'Y' ? 'danger' : 'info'" size="small">
                {{ row.configType === 'Y' ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" width="170">
            <template #default="{ row }">
              {{ formatDateTime(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button
                size="small"
                type="danger"
                :class="{ 'is-disabled-custom': row.configType === 'Y' }"
                @click="row.configType !== 'Y' && handleDelete(row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-panel">
          <el-pagination
            background
            small
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50, 100]"
            :total="Number(total)"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="550px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="configFormRef"
        :model="configForm"
        :rules="configFormRules"
        label-width="100px"
      >
        <el-form-item label="参数名称" prop="configName">
          <el-input v-model="configForm.configName" placeholder="请输入参数名称" clearable />
        </el-form-item>
        <el-form-item label="参数键名" prop="configKey">
          <el-input v-model="configForm.configKey" placeholder="请输入参数键名" clearable />
        </el-form-item>
        <el-form-item label="参数键值" prop="configValue">
          <el-input v-model="configForm.configValue" placeholder="请输入参数键值" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="系统内置" prop="configType">
          <el-radio-group v-model="configForm.configType">
            <el-radio label="Y">是</el-radio>
            <el-radio label="N">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="configForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search, RefreshLeft } from '@element-plus/icons-vue'
import { sysConfigApi } from '@/api'

// 搜索表单
const searchForm = reactive({
  configName: '',
  configKey: '',
  configType: ''
})

// 表格数据
const loading = ref(false)
const configList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogMode = ref('add')
const submitLoading = ref(false)
const configFormRef = ref(null)
const configForm = ref({
  configId: null,
  configName: '',
  configKey: '',
  configValue: '',
  configType: 'N',
  remark: ''
})

const configFormRules = {
  configName: [
    { required: true, message: '请输入参数名称', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  configKey: [
    { required: true, message: '请输入参数键名', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  configValue: [
    { required: true, message: '请输入参数键值', trigger: 'blur' },
    { min: 1, max: 500, message: '长度在 1 到 500 个字符', trigger: 'blur' }
  ]
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  const date = new Date(dateTime)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 加载配置列表
const loadConfigList = async () => {
  loading.value = true
  try {
    const response = await sysConfigApi.getConfigList({
      current: currentPage.value,
      size: pageSize.value,
      configName: searchForm.configName,
      configKey: searchForm.configKey,
      configType: searchForm.configType
    })
    if (response.code === 200) {
      configList.value = response.data.records || []
      total.value = Number(response.data.total) || 0
    }
  } catch (error) {
    ElMessage.error('加载参数配置列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  loadConfigList()
}

// 重置
const handleReset = () => {
  searchForm.configName = ''
  searchForm.configKey = ''
  searchForm.configType = ''
  currentPage.value = 1
  loadConfigList()
}

// 刷新
const handleRefresh = () => {
  loadConfigList()
}

// 添加
const handleAdd = () => {
  dialogMode.value = 'add'
  dialogTitle.value = '添加参数配置'
  resetForm()
  dialogVisible.value = true
}

// 编辑
const handleEdit = async (row) => {
  dialogMode.value = 'edit'
  dialogTitle.value = '编辑参数配置'
  try {
    const response = await sysConfigApi.getConfig(row.configId)
    if (response.code === 200) {
      configForm.value = {
        configId: response.data.configId,
        configName: response.data.configName,
        configKey: response.data.configKey,
        configValue: response.data.configValue,
        configType: response.data.configType || 'N',
        remark: response.data.remark || ''
      }
      dialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取参数配置信息失败')
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除参数"${row.configName}"吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await sysConfigApi.deleteConfig(row.configId)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadConfigList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!configFormRef.value) return
  await configFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (dialogMode.value === 'add') {
          response = await sysConfigApi.addConfig(configForm.value)
        } else {
          response = await sysConfigApi.updateConfig(configForm.value)
        }
        if (response.code === 200) {
          ElMessage.success(dialogMode.value === 'add' ? '添加成功' : '更新成功')
          dialogVisible.value = false
          loadConfigList()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error) {
        ElMessage.error('操作失败')
      } finally {
        submitLoading.value = false
      }
    }
  })
}

// 对话框关闭
const handleDialogClose = () => {
  resetForm()
}

// 重置表单
const resetForm = () => {
  configForm.value = {
    configId: null,
    configName: '',
    configKey: '',
    configValue: '',
    configType: 'N',
    remark: ''
  }
  if (configFormRef.value) {
    configFormRef.value.clearValidate()
  }
}

// 分页大小改变
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadConfigList()
}

// 当前页改变
const handleCurrentChange = (page) => {
  currentPage.value = page
  loadConfigList()
}

onMounted(() => {
  loadConfigList()
})
</script>

<style lang="scss" scoped>
.config-page {
  animation: fadeIn 0.6s ease-out;
  padding: 20px;
  background: var(--bg-secondary, #f8fafc);
  min-height: calc(100vh - 80px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .page-title {
    font-size: 24px;
    font-weight: 600;
    color: var(--text-primary);
    margin: 0;
  }
}

.config-container {
  background: var(--bg-card, #fff);
  border: 1px solid var(--border-color, #e4e7ed);
  border-radius: 12px;
  overflow: hidden;
}

.search-panel {
  padding: 20px;
  border-bottom: 1px solid var(--border-color, #e4e7ed);
  background: var(--bg-tertiary, #f5f7fa);
  
  .search-form {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    
    :deep(.el-form-item) {
      margin-bottom: 0;
    }
  }
}

.table-panel {
  padding: 20px;
}

.config-table {
  :deep(.el-table__header) {
    .el-table__cell {
      background: var(--bg-tertiary, #f5f7fa);
      font-weight: 500;
    }
  }
  
  // 修复操作列按钮样式
  :deep(.el-table__fixed-right) {
    z-index: 1;
  }
  
  .is-disabled-custom {
    opacity: 0.5;
    cursor: not-allowed;
    pointer-events: none;
    background-color: #fab6b6 !important;
    border-color: #fab6b6 !important;
    color: #fff !important;
  }
}

.pagination-panel {
  padding: 16px 0 0;
  display: flex;
  justify-content: flex-end;
}

// 对话框样式
:deep(.el-dialog) {
  border-radius: 12px;
  
  .el-dialog__header {
    padding: 20px 20px 16px;
    border-bottom: 1px solid var(--border-color);
    
    .el-dialog__title {
      font-size: 16px;
      font-weight: 600;
    }
  }
  
  .el-dialog__body {
    padding: 20px;
  }
  
  .el-dialog__footer {
    padding: 12px 20px;
    border-top: 1px solid var(--border-color);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .search-panel {
    .search-form {
      flex-direction: column;
      
      :deep(.el-form-item) {
        width: 100%;
        
        .el-input,
        .el-select {
          width: 100% !important;
        }
      }
    }
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>