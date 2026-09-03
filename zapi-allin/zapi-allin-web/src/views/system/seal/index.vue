<template>
  <div class="seal-management-container">
    <el-card class="filter-card">
      <div class="filter-section">
        <el-input
          v-model="searchForm.sealName"
          placeholder="请输入印章名称"
          clearable
          style="width: 240px; margin-right: 10px"
          @clear="handleSearch"
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>

        <el-select
          v-model="searchForm.status"
          placeholder="印章状态"
          clearable
          style="width: 160px; margin-right: 10px"
          @change="handleSearch"
        >
          <el-option label="全部" :value="null" />
          <el-option label="启用" :value="1" />
          <el-option label="停用" :value="0" />
        </el-select>

        <el-button type="primary" @click="handleSearch">
          <el-icon class="el-icon--left"><Search /></el-icon>
          <span>搜索</span>
        </el-button>
        <el-button @click="handleReset">
          <el-icon class="el-icon--left"><Refresh /></el-icon>
          <span>重置</span>
        </el-button>
      </div>

      <div class="action-section">
        <el-button type="primary" @click="handleCreate">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          <span>新建印章</span>
        </el-button>
        <el-button
          type="danger"
          :disabled="selectedIds.length === 0"
          @click="handleBatchDelete"
        >
          <el-icon class="el-icon--left"><Delete /></el-icon>
          <span>批量删除</span>
        </el-button>
      </div>
    </el-card>

    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="sealName" label="印章名称" min-width="120" />
        <el-table-column prop="sealType" label="印章类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="getSealTypeTag(row.sealType)">
              {{ getSealTypeLabel(row.sealType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileId" label="印章预览" width="150" align="center">
          <template #default="{ row }">
            <el-image
              v-if="row.fileId"
              :src="getImageUrl(row.fileId)"
              :preview-src-list="[getImageUrl(row.fileId)]"
              fit="contain"
              style="width: 80px; height: 80px; cursor: pointer"
              :preview-teleported="true"
            >
              <template #error>
                <div class="image-error">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
            </el-image>
            <span v-else>--</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="sortOrder" label="排序" width="100" align="center" />
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon class="el-icon--left"><Edit /></el-icon>
              <span>编辑</span>
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              <el-icon class="el-icon--left"><Delete /></el-icon>
              <span>删除</span>
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
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

    <!-- 新建/编辑印章对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item label="印章名称" prop="sealName">
          <el-input
            v-model="form.sealName"
            placeholder="请输入印章名称（1-4个字符）"
            maxlength="20"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="印章类型" prop="sealType">
          <el-radio-group v-model="form.sealType">
            <el-radio value="SQUARE_BORDER">方章有边框</el-radio>
            <el-radio value="SQUARE_NO_BORDER">方章无边框</el-radio>
            <el-radio value="SQUARE_3">方章正</el-radio>
            <el-radio value="ELLIPSE">椭圆章</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item
          v-if="form.sealType === 'ELLIPSE'"
          label="中心文字"
          prop="centerText"
        >
          <el-input
            v-model="form.centerText"
            placeholder="请输入中心文字（可选）"
            maxlength="10"
            show-word-limit
          />
          <div class="form-tip">
            椭圆章中心文字，可选
          </div>
        </el-form-item>

        <el-form-item label="排序" prop="sortOrder">
          <el-input-number
            v-model="form.sortOrder"
            :min="0"
            :max="9999"
            controls-position="right"
          />
        </el-form-item>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入备注"
            maxlength="500"
            show-word-limit
            :rows="3"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  Refresh,
  Plus,
  Delete,
  Edit,
  Picture
} from '@element-plus/icons-vue'
import { sealApi } from '@/api'

// 搜索表单
const searchForm = reactive({
  sealName: '',
  status: null
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
const dialogTitle = computed(() => (form.id ? '编辑印章' : '新建印章'))
const submitLoading = ref(false)
const formRef = ref(null)

// 表单数据
const form = reactive({
  id: null,
  sealName: '',
  sealType: 'SQUARE',
  centerText: '',
  sortOrder: 0,
  remark: ''
})

// 表单验证规则
const formRules = {
  sealName: [
    { required: true, message: '请输入印章名称', trigger: 'blur' },
    { min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
  ],
  sealType: [
    { required: true, message: '请选择印章类型', trigger: 'change' }
  ]
}

// 获取印章类型标签
const getSealTypeLabel = (type) => {
  const map = {
    SQUARE_BORDER: '方章有边框',
    SQUARE_NO_BORDER: '方章无边框',
    SQUARE_3: '方章正',
    ELLIPSE: '椭圆章'
  }
  return map[type] || type
}

// 获取印章类型标签颜色
const getSealTypeTag = (type) => {
  const map = {
    SQUARE_BORDER: 'primary',
    SQUARE_NO_BORDER: 'success',
    SQUARE_3: 'warning',
    ELLIPSE: 'info'
  }
  return map[type] || ''
}

// 获取图片URL (通过文件ID)
const getImageUrl = (fileId) => {
  if (!fileId) return ''
  // 使用文件ID下载
  return `/zapi-allin-sys/file/download/${fileId}`
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      // userId由后端从系统获取，不需要前端传递
      sealName: searchForm.sealName || undefined,
      status: searchForm.status !== null ? searchForm.status : undefined
    }

    const res = await sealApi.pageUserSeals(params)
    if (res.code === 200) {
      tableData.value = res.data.records || []
      pagination.total = Number(res.data.total) || 0
      
      // 调试：输出第一条数据
      if (tableData.value.length > 0) {
      }
    } else {
      ElMessage.error(res.msg || '加载数据失败')
    }
  } catch (error) {
    console.error('加载印章数据失败:', error)
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
  searchForm.sealName = ''
  searchForm.status = null
  pagination.current = 1
  loadData()
}

// 表格选择
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 分页
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  loadData()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  loadData()
}

// 新建印章
const handleCreate = () => {
  form.id = null
  form.sealName = ''
  form.sealType = 'SQUARE_BORDER'  // 默认为方章有边框
  form.centerText = ''
  form.sortOrder = 0
  form.remark = ''
  dialogVisible.value = true
}

// 编辑印章
const handleEdit = (row) => {
  form.id = row.id
  form.sealName = row.sealName
  form.sealType = row.sealType
  form.centerText = ''
  form.sortOrder = row.sortOrder
  form.remark = row.remark || ''
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    submitLoading.value = true
    try {
      if (form.id) {
        // 编辑印章
        const res = await sealApi.updateSeal(form.id, {
          sealName: form.sealName,
          sortOrder: form.sortOrder,
          remark: form.remark
        })

        if (res.code === 200) {
          ElMessage.success('更新成功')
          dialogVisible.value = false
          loadData()
        } else {
          ElMessage.error(res.msg || '更新失败')
        }
      } else {
        // 新建印章（不需要传递userId，由后端从系统获取）
        const params = {
          sealName: form.sealName,
          sealType: form.sealType,
          centerText: form.centerText || null,
          sortOrder: form.sortOrder || 0,
          remark: form.remark || null
        }

        const res = await sealApi.createSeal(params)

        if (res.code === 200) {
          ElMessage.success('创建成功')
          dialogVisible.value = false
          loadData()
        } else {
          ElMessage.error(res.msg || '创建失败')
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

// 更新状态
const handleStatusChange = async (row) => {
  try {
    const res = await sealApi.updateStatus(row.id, row.status)
    if (res.code === 200) {
      ElMessage.success('状态更新成功')
    } else {
      ElMessage.error(res.msg || '状态更新失败')
      // 恢复原状态
      row.status = row.status === 1 ? 0 : 1
    }
  } catch (error) {
    console.error('更新状态失败:', error)
    ElMessage.error('状态更新失败')
    // 恢复原状态
    row.status = row.status === 1 ? 0 : 1
  }
}

// 删除印章
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除印章"${row.sealName}"吗？删除后将无法恢复。`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await sealApi.deleteSeal(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.msg || '删除失败')
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
    ElMessage.warning('请选择要删除的印章')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个印章吗？删除后将无法恢复。`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await sealApi.batchDelete(selectedIds.value)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      selectedIds.value = []
      loadData()
    } else {
      ElMessage.error(res.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.seal-management-container {
  padding: 20px;
  background: var(--bg-secondary, #f8fafc);
  min-height: calc(100vh - 80px);
}

.filter-card {
  margin-bottom: 20px;

  .filter-section {
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 15px;
  }

  .action-section {
    display: flex;
    align-items: center;
    gap: 10px;
  }
}

.table-card {
  .el-table {
    margin-bottom: 20px;
  }

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
  margin-top: 20px;
}

.image-error {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 24px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>

