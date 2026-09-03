<template>
  <div class="form-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">表单管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="goToFormDesigner">
          <el-icon><Plus /></el-icon>
          创建表单
        </el-button>
        <el-button>
          <el-icon><Download /></el-icon>
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <div class="search-left">
        <el-input
          v-model="searchQuery"
          placeholder="搜索表单名称..."
          prefix-icon="Search"
          style="width: 300px"
          clearable
          @input="handleSearch"
        />
        <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 150px" @change="handleSearch">
          <el-option label="全部" :value="null" />
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </div>
      <div class="search-right">
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 表单列表 -->
    <div class="forms-content">
      <el-table
        v-loading="loading"
        :data="formList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="formName" label="表单名称" min-width="150">
          <template #default="{ row }">
            <div class="form-name">
              <span class="name">{{ row.formName }}</span>
              <span class="description" v-if="row.formDescription">{{ row.formDescription }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.statusDesc }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createBy" label="创建者" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.updateTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <div class="table-actions">
              <!-- 禁用状态显示编辑按钮 -->
              <el-button 
                v-if="row.status === 0" 
                type="primary" 
                link 
                @click="handleEdit(row)"
              >
                <el-icon class="el-icon--left"><Edit /></el-icon>
                编辑
              </el-button>
              <!-- 启用状态显示预览按钮 -->
              <el-button 
                v-if="row.status === 1" 
                type="primary" 
                link 
                @click="handlePreview(row)"
              >
                <el-icon class="el-icon--left"><View /></el-icon>
                预览
              </el-button>
              <el-button
                :type="row.status === 1 ? 'warning' : 'success'"
                link
                @click="handleToggleStatus(row)"
              >
                <el-icon class="el-icon--left"><Switch /></el-icon>
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button 
                v-if="row.status === 0"
                type="danger" 
                link 
                @click="handleDelete(row)"
              >
                <el-icon class="el-icon--left"><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-section">
      <el-pagination
        background
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="Number(pagination.total)"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 表单预览抽屉 -->
    <el-drawer
      v-model="previewDrawerVisible"
      :title="currentForm?.formName || '表单预览'"
      direction="rtl"
      size="60%"
      :close-on-click-modal="false"
    >
      <div class="preview-container">
        <div class="preview-content">
          <FormPreview v-if="formConfig" :formConfig="formConfig" />
          <div v-else class="no-config">
            <el-empty description="暂无表单配置" />
          </div>
        </div>
      </div>
    </el-drawer>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Search, Refresh, Edit, Switch, Delete, View } from '@element-plus/icons-vue'
import { formApi } from '@/api'
import FormPreview from '@/components/form-designer/FormPreview.vue'

// 响应式数据
const loading = ref(false)
const formList = ref<any[]>([])
const selectedForms = ref<any[]>([])
const searchQuery = ref('')
const statusFilter = ref<number | null>(null)

// 预览相关
const previewDrawerVisible = ref(false)
const currentForm = ref<any>(null)
const formConfig = ref<any>(null)

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})


// 获取表单列表
const getFormList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      formName: searchQuery.value || undefined,
      status: statusFilter.value
    }
    const response = await formApi.getFormList(params)
    // 响应拦截器已经处理了状态码，直接使用返回的数据
    formList.value = response.data.records
    pagination.total = Number(response.data.total) || 0
  } catch (error) {
    console.error('获取表单列表失败:', error)
    ElMessage.error('获取表单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.current = 1
  getFormList()
}

// 刷新
const handleRefresh = () => {
  searchQuery.value = ''
  statusFilter.value = null
  pagination.current = 1
  getFormList()
}

// 跳转到表单设计器
const goToFormDesigner = () => {
  window.location.href = '/home/form/design'
}

// 分页处理
const handleSizeChange = (val: number) => {
  pagination.size = val
  getFormList()
}

const handleCurrentChange = (val: number) => {
  pagination.current = val
  getFormList()
}

// 选择处理
const handleSelectionChange = (val: any[]) => {
  selectedForms.value = val
}


// 编辑表单 - 跳转到表单设计器并传递表单ID
const handleEdit = (row: any) => {
  window.location.href = `/home/form/design?id=${row.id}`
}

// 预览表单
const handlePreview = async (row: any) => {
  try {
    loading.value = true
    currentForm.value = row
    
    // 获取表单详情
    const response = await formApi.getFormDetail(row.id)
    
    if (response.data && response.data.formConfig) {
      // 解析表单配置
      try {
        const parsedConfig = typeof response.data.formConfig === 'string' 
          ? JSON.parse(response.data.formConfig) 
          : response.data.formConfig
        
        console.log('解析后的表单配置:', parsedConfig)
        
        // 确保配置格式正确
        if (parsedConfig && parsedConfig.components) {
          formConfig.value = parsedConfig
          previewDrawerVisible.value = true
        } else {
          ElMessage.warning('表单配置格式不正确')
          console.error('表单配置缺少components字段:', parsedConfig)
        }
      } catch (e) {
        console.error('解析表单配置失败:', e)
        ElMessage.error('表单配置格式错误')
      }
    } else {
      ElMessage.warning('该表单暂无配置信息')
    }
  } catch (error) {
    console.error('获取表单详情失败:', error)
    ElMessage.error('获取表单详情失败')
  } finally {
    loading.value = false
  }
}

// 切换状态
const handleToggleStatus = async (row: any) => {
  const newStatus = row.status === 1 ? 0 : 1
  const statusText = newStatus === 1 ? '启用' : '禁用'
  
  try {
    await ElMessageBox.confirm(
      `确定要${statusText}表单"${row.formName}"吗？`,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await formApi.changeFormStatus(row.id, newStatus)
    // 响应拦截器已经处理了状态码，直接显示成功信息
    ElMessage.success(`${statusText}成功`)
    getFormList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(`${statusText}表单失败:`, error)
      ElMessage.error(`${statusText}失败`)
    }
  }
}

// 删除表单
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除表单"${row.formName}"吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await formApi.deleteForm(row.id)
    // 响应拦截器已经处理了状态码，直接显示成功信息
    ElMessage.success('删除成功')
    getFormList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除表单失败:', error)
      ElMessage.error('删除失败')
    }
  }
}



// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 页面挂载时获取数据
onMounted(() => {
  getFormList()
})
</script>

<style scoped lang="scss">
.form-page {
  padding: 24px;
  background: var(--bg-secondary, #f5f7fa);
  min-height: calc(100vh - 80px);
  animation: fadeIn 0.6s ease-out;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  .header-content {
    .page-title {
      margin: 0;
      font-size: 24px;
      font-weight: 600;
      color: var(--text-primary);
      background: transparent;
    }
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }
}

.search-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 20px;
  background: var(--bg-card, #ffffff);
  border: 1px solid var(--border-color, #e4e7ed);
  border-radius: 12px;
  box-shadow: var(--shadow-sm, 0 1px 2px rgba(0, 0, 0, 0.05));

  .search-left {
    display: flex;
    gap: 16px;
    align-items: center;
    flex-wrap: wrap;
  }

  .search-right {
    display: flex;
    gap: 12px;
  }
}

.forms-content {
  background: var(--bg-card, #ffffff);
  border-radius: 12px;
  box-shadow: var(--shadow-lg, 0 2px 12px 0 rgba(0, 0, 0, 0.1));
  padding: 24px;
  margin-bottom: 24px;

  .form-name {
    .name {
      font-weight: 500;
      color: var(--text-primary, #303133);
      font-size: 14px;
    }

    .description {
      display: block;
      font-size: 12px;
      color: var(--text-secondary, #909399);
      margin-top: 4px;
      line-height: 1.4;
    }
  }

  .table-actions {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    align-items: center;
  }

  :deep(.el-table) {
    border: none;
    background: transparent;

    .el-table__header {
      background: transparent;
      
      .el-table__cell {
        background: transparent;
        border: none;
        color: var(--text-secondary, #909399);
        font-weight: 500;
        padding: 16px 0;
      }
    }
    
    .el-table__row {
      background: transparent;
      border: none;
      
      &:hover {
        background: var(--bg-tertiary, #f5f7fa);
      }
      
      .el-table__cell {
        border: none;
        padding: 16px 0;
        background: transparent;
      }
    }
    
    .el-table__inner-wrapper {
      border: none;
    }
    
    .el-table__border-line {
      display: none;
    }
  }

}

.pagination-section {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: var(--bg-card, #ffffff);
  border: 1px solid var(--border-color, #e4e7ed);
  border-radius: 12px;
  box-shadow: var(--shadow-lg, 0 2px 12px 0 rgba(0, 0, 0, 0.1));
}

// 预览抽屉样式
.preview-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  
  .preview-content {
    flex: 1;
    overflow-y: auto;
    background: var(--bg-secondary, #f5f7fa);
    
    .no-config {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 300px;
    }
  }
}

:deep(.el-drawer) {
  .el-drawer__header {
    padding: 20px;
    margin-bottom: 0;
    border-bottom: 1px solid var(--border-color, #e4e7ed);
    
    .el-drawer__title {
      font-size: 18px;
      font-weight: 600;
      color: var(--text-primary, #303133);
    }
  }
  
  .el-drawer__body {
    padding: 0;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .form-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }

  .search-section {
    flex-direction: column;
    gap: 16px;
  }

  .search-left {
    width: 100%;
    flex-direction: column;
    
    .el-input,
    .el-select {
      width: 100% !important;
    }
  }

  .table-actions {
    flex-direction: column;
    width: 100%;
    
    .el-button {
      width: 100%;
    }
  }
  
  :deep(.el-drawer) {
    width: 90% !important;
  }
}
</style>
