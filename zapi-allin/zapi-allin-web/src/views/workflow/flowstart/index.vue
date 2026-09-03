<script setup lang="ts">
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import { flowTestOrderApi } from '@/api/flowTestOrder'

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const selectedRows = ref([])

// 分页参数
const pagination = reactive({
  current: 1,
  size: 10
})

// 搜索参数
const searchForm = reactive({
  orderName: ''
})

// 对话框状态
const dialogVisible = ref(false)
const dialogTitle = ref('新增订单')
const isEdit = ref(false)

// 表单数据
const formData = reactive({
  id: null,
  orderNo: '',
  orderName: '',
  orderCount: 1
})

// 表单验证规则
const formRules = {
  orderNo: [
    { required: true, message: '请输入订单号', trigger: 'blur' },
    { min: 3, max: 50, message: '订单号长度在 3 到 50 个字符', trigger: 'blur' }
  ],
  orderName: [
    { required: true, message: '请输入订单名称', trigger: 'blur' },
    { min: 2, max: 100, message: '订单名称长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  orderCount: [
    { required: true, message: '请输入订单数量', trigger: 'blur' },
    { type: 'number', min: 1, message: '订单数量必须大于0', trigger: 'blur' }
  ]
}

// 表单引用
const formRef = ref(null)

// 加载订单列表
const loadOrderList = async () => {
  loading.value = true
  try {
    const response = await flowTestOrderApi.page(
      pagination.current,
      pagination.size,
      searchForm.orderName
    )
    
    if (response.code === 200) {
      tableData.value = response.data.records || []
      total.value = Number(response.data.total) || 0
    } else {
      ElMessage.error(response.message || '获取订单列表失败')
    }
  } catch (error) {
    console.error('加载订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadOrderList()
}

// 重置搜索
const handleReset = () => {
  searchForm.orderName = ''
  pagination.current = 1
  loadOrderList()
}

// 分页变化
const handlePageChange = (page: number) => {
  pagination.current = page
  loadOrderList()
}

const handleSizeChange = (size: number) => {
  pagination.size = size
  pagination.current = 1
  loadOrderList()
}

// 表格选择变化
const handleSelectionChange = (selection: any[]) => {
  selectedRows.value = selection
}

// 打开新增对话框
const handleAdd = () => {
  dialogTitle.value = '新增订单'
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 打开编辑对话框
const handleEdit = (row: any) => {
  dialogTitle.value = '编辑订单'
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

// 删除单个订单
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除订单 "${row.orderName}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await flowTestOrderApi.delete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadOrderList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除订单失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的订单')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedRows.value.length} 个订单吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const ids = selectedRows.value.map(row => row.id)
    const response = await flowTestOrderApi.batchDelete(ids)
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      selectedRows.value = []
      loadOrderList()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    // 检查订单号是否已存在
    const checkResponse = await flowTestOrderApi.checkOrderNo(
      formData.orderNo,
      isEdit.value ? formData.id : null
    )
    
    if (checkResponse.code === 200 && checkResponse.data) {
      ElMessage.error('订单号已存在，请使用其他订单号')
      return
    }
    
    let response
    if (isEdit.value) {
      response = await flowTestOrderApi.update(formData.id, formData)
    } else {
      response = await flowTestOrderApi.create(formData)
    }
    
    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      dialogVisible.value = false
      loadOrderList()
    } else {
      ElMessage.error(response.message || (isEdit.value ? '更新失败' : '创建失败'))
    }
  } catch (error) {
    console.error('提交表单失败:', error)
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(formData, {
    id: null,
    orderNo: '',
    orderName: '',
    orderCount: 1
  })
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
  dialogVisible.value = false
}

// 生成订单号
const generateOrderNo = () => {
  const now = new Date()
  const dateStr = now.toISOString().slice(0, 10).replace(/-/g, '')
  const timeStr = now.getTime().toString().slice(-6)
  formData.orderNo = `ORD${dateStr}${timeStr}`
}

// 页面挂载时加载数据
onMounted(() => {
  loadOrderList()
})
</script>

<template>
  <div class="flow-test-order-container">

    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" inline>
        <el-form-item label="订单名称">
          <el-input
            v-model="searchForm.orderName"
            placeholder="请输入订单名称"
            clearable
            @keyup.enter="handleSearch"
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">
            搜索
          </el-button>
          <el-button :icon="Refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          新增订单
        </el-button>
        <el-button 
          type="danger" 
          :icon="Delete" 
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-button :icon="Refresh" @click="loadOrderList" circle />
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        height="400"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="orderNo" label="订单号" min-width="120" />
        <el-table-column prop="orderName" label="订单名称" min-width="150" />
        <el-table-column prop="orderCount" label="订单数量" width="100" align="center">
          <template #default="{ row }">
            <el-tag type="success">{{ row.orderCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <div class="table-action-group">
              <el-button size="small" type="primary" link @click="handleEdit(row)">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button size="small" type="danger" link @click="handleDelete(row)">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        background
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :page-sizes="[10, 20, 50, 100]"
        :total="Number(total)"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="订单号" prop="orderNo">
          <el-input
            v-model="formData.orderNo"
            placeholder="请输入订单号"
            clearable
          >
            <template #append>
              <el-button @click="generateOrderNo" :icon="Refresh">
                生成
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="订单名称" prop="orderName">
          <el-input
            v-model="formData.orderName"
            placeholder="请输入订单名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="订单数量" prop="orderCount">
          <el-input-number
            v-model="formData.orderCount"
            :min="1"
            :max="999999"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.flow-test-order-container {
  padding: 20px;
  background: var(--bg-secondary, #f8fafc);
  min-height: 100vh;

  .page-header {
    margin-bottom: 16px;
    
    .page-title {
      margin: 0;
      font-size: 20px;
      font-weight: 600;
      color: var(--text-primary);
    }
  }
}

.search-container {
  margin-bottom: 16px;
  padding: 16px;
  background: var(--bg-card);
  border-radius: 8px;
  box-shadow: var(--shadow-sm);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding: 16px;
  background: var(--bg-card);
  border-radius: 8px;
  box-shadow: var(--shadow-sm);

  .toolbar-left {
    display: flex;
    gap: 12px;
  }
}

.table-container {
  margin-bottom: 16px;
  background: transparent;
  border-radius: 0;
  box-shadow: none;
  overflow: visible;

  :deep(.el-table) {
    border: none;
    background: transparent;
  }

  :deep(.el-table__header) {
    background: transparent;

    .el-table__cell {
      background: transparent;
      border: none;
      color: var(--text-secondary);
      font-weight: 500;
      padding: 16px 0;
    }
  }

  :deep(.el-table__row) {
    background: transparent;
    border: none;

    &:hover {
      background: var(--bg-tertiary);
    }

    .el-table__cell {
      border: none;
      padding: 16px 0;
      background: transparent;
    }
  }

  :deep(.el-table__inner-wrapper) {
    border: none;
  }

  :deep(.el-table__border-line) {
    display: none;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
}

.dialog-footer {
  text-align: right;
}

// 响应式设计
@media (max-width: 768px) {
  .flow-test-order-container {
    padding: 10px;
  }
  
  .toolbar {
    flex-direction: column;
    gap: 12px;
    
    .toolbar-left {
      width: 100%;
      justify-content: center;
    }
  }
  
  .table-container {
    :deep(.el-table) {
      font-size: 12px;
    }
  }
}
</style>
