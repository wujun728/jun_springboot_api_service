<template>
  <div class="role-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">角色管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          添加角色
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon class="el-icon--left"><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <div class="search-left">
        <el-input
          v-model="searchQuery"
          placeholder="搜索角色..."
          prefix-icon="Search"
          style="width: 300px"
          clearable
        />
        <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 150px">
          <el-option label="全部" value="" />
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
      </div>
      <div class="search-right">
        <el-button-group>
          <el-button :type="viewMode === 'table' ? 'primary' : ''" @click="viewMode = 'table'">
            <el-icon><List /></el-icon>
          </el-button>
          <el-button :type="viewMode === 'grid' ? 'primary' : ''" @click="viewMode = 'grid'">
            <el-icon><Grid /></el-icon>
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 角色列表 -->
    <div class="role-content">
      <!-- 表格视图 -->
      <div v-if="viewMode === 'table'" class="table-view">
        <el-table 
          :data="filteredRoles" 
          style="width: 100%" 
          class="role-table"
          v-loading="loading"
        >
          <el-table-column prop="roleName" label="角色名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="roleCode" label="角色编码" min-width="150" show-overflow-tooltip />
          <el-table-column prop="description" label="角色描述" min-width="200" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="80" />
          <el-table-column prop="createTime" label="创建时间" min-width="180" show-overflow-tooltip />
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button size="small" type="info" @click="handleAssignUser(row)">分配用户</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 网格视图 -->
      <div v-else class="grid-view">
        <div class="role-grid">
          <div v-for="role in filteredRoles" :key="role.id" class="role-card">
            <div class="role-header">
              <div class="role-icon">
                <el-icon><UserFilled /></el-icon>
              </div>
              <div class="role-status" :class="{ active: role.status === 1 }"></div>
            </div>
            <div class="role-info">
              <h3 class="role-name">{{ role.roleName }}</h3>
              <p class="role-code">{{ role.roleCode }}</p>
              <p class="role-description">{{ role.description }}</p>
            </div>
            <div class="role-actions">
              <el-button size="small" @click="handleEdit(role)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(role)">删除</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 分页 -->
    <div class="pagination-section">
      <el-pagination
        background
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="Number(total)"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 分配用户对话框 -->
    <AssignUserDialog
      v-model="assignUserDialogVisible"
      :role="currentRole"
      @success="handleAssignSuccess"
    />

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleFormRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input
            v-model="roleForm.roleName"
            placeholder="请输入角色名称"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="角色编码" prop="roleCode">
          <el-input
            v-model="roleForm.roleCode"
            placeholder="请输入角色编码，如：admin、user"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="角色描述" prop="description">
          <el-input
            v-model="roleForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入角色描述"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="排序" prop="sort">
          <el-input-number
            v-model="roleForm.sort"
            :min="0"
            :max="9999"
            controls-position="right"
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, List, Grid, UserFilled } from '@element-plus/icons-vue'
import { roleApi } from '@/api'
import AssignUserDialog from './AssignUserDialog.vue'

// 响应式数据
const loading = ref(false)
const roleList = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)
const searchQuery = ref('')
const statusFilter = ref('')
const viewMode = ref('table')

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogMode = ref('add') // add 或 edit
const submitLoading = ref(false)
const roleFormRef = ref(null)
const roleForm = ref({
  id: null,
  roleName: '',
  roleCode: '',
  description: '',
  sort: 0,
  status: 1
})

// 分配用户对话框相关
const assignUserDialogVisible = ref(false)
const currentRole = ref(null)

// 表单验证规则
const roleFormRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  roleCode: [
    { required: true, message: '请输入角色编码', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '角色编码必须以字母开头，只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  description: [
    { max: 200, message: '长度不能超过 200 个字符', trigger: 'blur' }
  ],
  sort: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 计算属性
const filteredRoles = computed(() => {
  let filtered = roleList.value

  if (searchQuery.value) {
    filtered = filtered.filter(role =>
      role.roleName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      role.roleCode.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      (role.description && role.description.toLowerCase().includes(searchQuery.value.toLowerCase()))
    )
  }

  if (statusFilter.value !== '') {
    filtered = filtered.filter(role => role.status.toString() === statusFilter.value)
  }

  return filtered
})

const loadRoleList = async () => {
  loading.value = true
  try {
    const response = await roleApi.getRoleList({
      current: currentPage.value,
      size: pageSize.value
    })
    if (response.code === 200) {
      roleList.value = response.data.records || []
      total.value = Number(response.data.total) || 0
    }
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  } finally {
    loading.value = false
  }
}

// 打开添加对话框
const handleAdd = () => {
  dialogMode.value = 'add'
  dialogTitle.value = '添加角色'
  resetForm()
  dialogVisible.value = true
}

// 打开编辑对话框
const handleEdit = async (row) => {
  dialogMode.value = 'edit'
  dialogTitle.value = '编辑角色'
  
  try {
    const response = await roleApi.getRole(row.id)
    if (response.code === 200) {
      roleForm.value = {
        id: response.data.id,
        roleName: response.data.roleName,
        roleCode: response.data.roleCode,
        description: response.data.description || '',
        sort: response.data.sort || 0,
        status: response.data.status
      }
      dialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取角色信息失败')
    }
  } catch (error) {
    ElMessage.error('获取角色信息失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!roleFormRef.value) return
  
  await roleFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (dialogMode.value === 'add') {
          response = await roleApi.addRole(roleForm.value)
        } else {
          response = await roleApi.updateRole(roleForm.value)
        }
        
        if (response.code === 200) {
          ElMessage.success(dialogMode.value === 'add' ? '添加成功' : '更新成功')
          dialogVisible.value = false
          loadRoleList()
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

// 关闭对话框
const handleDialogClose = () => {
  resetForm()
}

// 重置表单
const resetForm = () => {
  roleForm.value = {
    id: null,
    roleName: '',
    roleCode: '',
    description: '',
    sort: 0,
    status: 1
  }
  if (roleFormRef.value) {
    roleFormRef.value.clearValidate()
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除角色"${row.roleName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await roleApi.deleteRole(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadRoleList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleRefresh = () => {
  loadRoleList()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadRoleList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadRoleList()
}

// 分配用户
const handleAssignUser = (row) => {
  currentRole.value = row
  assignUserDialogVisible.value = true
}

// 分配用户成功回调
const handleAssignSuccess = () => {
  ElMessage.success('分配用户成功')
  loadRoleList()
}

onMounted(() => {
  loadRoleList()
})
</script>

<style lang="scss" scoped>
.role-page {
  animation: fadeIn 0.6s ease-out;
  padding: 20px;
  background: var(--bg-secondary, #f8fafc);
  min-height: calc(100vh - 80px);
}

.header-actions {
  display: flex;
  gap: 12px;
}

.search-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
}

.search-left {
  display: flex;
  gap: 16px;
}

.role-content {
  margin-bottom: 24px;
}

.table-view {
  background: transparent;
  border: none;
  border-radius: 0;
  overflow: visible;
  box-shadow: none;
}

.role-table {
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

.grid-view {
  .role-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 24px;
  }
}

.role-card {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 16px;
  padding: 24px;
  text-align: center;
  transition: all 0.3s ease;
  box-shadow: var(--shadow-lg);
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-xl);
    border-color: var(--border-light);
  }
}

.role-header {
  position: relative;
  margin-bottom: 16px;
  display: flex;
  justify-content: center;
  
  .role-icon {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background: var(--primary-color);
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 24px;
  }
  
  .role-status {
    position: absolute;
    top: 0;
    right: 0;
    width: 16px;
    height: 16px;
    border-radius: 50%;
    border: 3px solid var(--bg-card);
    background: var(--danger-color);
    
    &.active {
      background: var(--success-color);
    }
  }
}

.role-info {
  margin-bottom: 20px;
  
  .role-name {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin-bottom: 8px;
  }
  
  .role-code {
    color: var(--text-secondary);
    font-size: 14px;
    margin-bottom: 4px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
  
  .role-description {
    color: var(--text-muted);
    font-size: 12px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.role-actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.pagination-section {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  box-shadow: var(--shadow-lg);
}

// 对话框样式
:deep(.el-dialog) {
  border-radius: 16px;
  box-shadow: var(--shadow-xl);
  
  .el-dialog__header {
    padding: 24px 24px 16px;
    border-bottom: 1px solid var(--border-color);
    
    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
      color: var(--text-primary);
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

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-input-number) {
  width: 100%;
}

// 响应式设计
@media (max-width: 768px) {
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
  }
  
  .role-grid {
    grid-template-columns: 1fr;
  }
}
</style>