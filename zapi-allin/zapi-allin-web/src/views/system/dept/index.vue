<template>
  <div class="dept-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">部门管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          添加部门
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
          placeholder="搜索部门..."
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
          <el-button :type="viewMode === 'tree' ? 'primary' : ''" @click="viewMode = 'tree'">
            <el-icon><Grid /></el-icon>
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 部门列表 -->
    <div class="dept-content">
      <!-- 表格视图 -->
      <div v-if="viewMode === 'table'" class="table-view">
        <el-table 
          :data="filteredDepts" 
          style="width: 100%" 
          class="dept-table"
          v-loading="loading"
          row-key="id"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        >
          <el-table-column prop="deptName" label="部门名称" min-width="200" show-overflow-tooltip />
          <el-table-column prop="deptCode" label="部门编码" min-width="150" show-overflow-tooltip />
          <el-table-column prop="parentName" label="上级部门" min-width="150" show-overflow-tooltip />
          <el-table-column prop="leader" label="负责人" min-width="120" show-overflow-tooltip />
          <el-table-column prop="phone" label="联系电话" min-width="150" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="80" />
          <el-table-column prop="createTime" label="创建时间" min-width="180" show-overflow-tooltip />
          <el-table-column label="操作" width="360" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button size="small" type="info" @click="handleAssignUser(row)">分配用户</el-button>
              <el-button size="small" type="success" @click="handleAddChild(row)">添加子部门</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 树形视图 -->
      <div v-else class="tree-view">
        <div class="dept-tree-container">
          <el-tree
            :data="filteredDepts"
            :props="{ label: 'deptName', children: 'children' }"
            node-key="id"
            default-expand-all
            class="dept-tree"
          >
            <template #default="{ node, data }">
              <div class="tree-node">
                <div class="node-content">
                  <el-icon class="dept-icon">
                    <OfficeBuilding />
                  </el-icon>
                  <span class="node-label">{{ data.deptName }}</span>
                  <el-tag size="small" :type="data.status === 1 ? 'success' : 'danger'" class="node-status">
                    {{ data.status === 1 ? '启用' : '禁用' }}
                  </el-tag>
                </div>
                <div class="node-actions">
                  <el-button size="small" @click.stop="handleEdit(data)">编辑</el-button>
                  <el-button size="small" type="info" @click.stop="handleAssignUser(data)">分配用户</el-button>
                  <el-button size="small" type="success" @click.stop="handleAddChild(data)">添加子部门</el-button>
                  <el-button size="small" type="danger" @click.stop="handleDelete(data)">删除</el-button>
                </div>
              </div>
            </template>
          </el-tree>
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
      :dept="currentDept"
      @success="handleAssignSuccess"
    />

    <!-- 添加/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="deptFormRef"
        :model="deptForm"
        :rules="deptFormRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门名称" prop="deptName">
              <el-input
                v-model="deptForm.deptName"
                placeholder="请输入部门名称"
                clearable
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="部门编码" prop="deptCode">
              <el-input
                v-model="deptForm.deptCode"
                placeholder="请输入部门编码"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="deptForm.parentId"
            :data="parentDeptOptions"
            :props="{ value: 'id', label: 'deptName', children: 'children' }"
            placeholder="请选择上级部门（留空则为顶级部门）"
            clearable
            check-strictly
            :render-after-expand="false"
            style="width: 100%"
          />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="负责人" prop="leader">
              <el-input
                v-model="deptForm.leader"
                placeholder="请输入负责人"
                clearable
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input
                v-model="deptForm.phone"
                placeholder="请输入联系电话"
                clearable
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="deptForm.email"
            placeholder="请输入邮箱"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="部门描述" prop="description">
          <el-input
            v-model="deptForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入部门描述"
            clearable
          />
        </el-form-item>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number
                v-model="deptForm.sort"
                :min="0"
                :max="9999"
                controls-position="right"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="deptForm.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
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
import { Plus, Refresh, List, Grid, OfficeBuilding } from '@element-plus/icons-vue'
import { departmentApi } from '@/api'
import AssignUserDialog from './AssignUserDialog.vue'

// 响应式数据
const loading = ref(false)
const deptList = ref([])
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
const deptFormRef = ref(null)
const parentDeptOptions = ref([])
const deptForm = ref({
  id: null,
  deptName: '',
  deptCode: '',
  parentId: null,
  description: '',
  leader: '',
  phone: '',
  email: '',
  sort: 0,
  status: 1
})

// 分配用户对话框相关
const assignUserDialogVisible = ref(false)
const currentDept = ref(null)

// 表单验证规则
const deptFormRules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  deptCode: [
    { required: true, message: '请输入部门编码', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '部门编码必须以字母开头，只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  leader: [
    { max: 50, message: '长度不能超过 50 个字符', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
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
const filteredDepts = computed(() => {
  let filtered = deptList.value

  if (searchQuery.value) {
    filtered = filterDeptsByQuery(filtered, searchQuery.value)
  }

  if (statusFilter.value !== '') {
    filtered = filterDeptsByStatus(filtered, parseInt(statusFilter.value))
  }

  return filtered
})

// 递归过滤部门
const filterDeptsByQuery = (depts, query) => {
  return depts.filter(dept => {
    const matches = dept.deptName.toLowerCase().includes(query.toLowerCase()) ||
                   dept.deptCode.toLowerCase().includes(query.toLowerCase()) ||
                   (dept.leader && dept.leader.toLowerCase().includes(query.toLowerCase()))
    
    if (dept.children && dept.children.length > 0) {
      dept.children = filterDeptsByQuery(dept.children, query)
      return matches || dept.children.length > 0
    }
    
    return matches
  })
}

const filterDeptsByStatus = (depts, status) => {
  return depts.filter(dept => {
    const matches = dept.status === status
    
    if (dept.children && dept.children.length > 0) {
      dept.children = filterDeptsByStatus(dept.children, status)
      return matches || dept.children.length > 0
    }
    
    return matches
  })
}

const loadDeptList = async () => {
  loading.value = true
  try {
    const response = await departmentApi.getDepartmentTree()
    if (response.code === 200) {
      deptList.value = response.data || []
      total.value = countTreeNodes(response.data || [])
    }
  } catch (error) {
    ElMessage.error('加载部门列表失败')
  } finally {
    loading.value = false
  }
}

// 计算树形结构中的节点总数
const countTreeNodes = (nodes) => {
  let count = 0
  nodes.forEach(node => {
    count++
    if (node.children && node.children.length > 0) {
      count += countTreeNodes(node.children)
    }
  })
  return count
}

// 加载父部门选项
const loadParentDeptOptions = async (excludeId = null) => {
  try {
    const response = await departmentApi.getDepartmentTree()
    if (response.code === 200) {
      parentDeptOptions.value = response.data || []
      // 如果是编辑模式，需要排除自己和子部门
      if (excludeId) {
        parentDeptOptions.value = filterDeptOptions(parentDeptOptions.value, excludeId)
      }
    }
  } catch (error) {
    ElMessage.error('加载父部门选项失败')
  }
}

// 过滤部门选项（排除自己和子部门）
const filterDeptOptions = (depts, excludeId) => {
  return depts.filter(dept => {
    if (dept.id === excludeId) {
      return false
    }
    if (dept.children && dept.children.length > 0) {
      dept.children = filterDeptOptions(dept.children, excludeId)
    }
    return true
  })
}

// 打开添加对话框
const handleAdd = async () => {
  dialogMode.value = 'add'
  dialogTitle.value = '添加部门'
  resetForm()
  await loadParentDeptOptions()
  dialogVisible.value = true
}

// 打开添加子部门对话框
const handleAddChild = async (row) => {
  dialogMode.value = 'add'
  dialogTitle.value = '添加子部门'
  resetForm()
  deptForm.value.parentId = row.id
  await loadParentDeptOptions()
  dialogVisible.value = true
}

// 打开编辑对话框
const handleEdit = async (row) => {
  dialogMode.value = 'edit'
  dialogTitle.value = '编辑部门'
  
  try {
    const response = await departmentApi.getDepartment(row.id)
    if (response.code === 200) {
      deptForm.value = {
        id: response.data.id,
        deptName: response.data.deptName,
        deptCode: response.data.deptCode,
        parentId: response.data.parentId || null,
        description: response.data.description || '',
        leader: response.data.leader || '',
        phone: response.data.phone || '',
        email: response.data.email || '',
        sort: response.data.sort || 0,
        status: response.data.status
      }
      await loadParentDeptOptions(row.id)
      dialogVisible.value = true
    } else {
      ElMessage.error(response.message || '获取部门信息失败')
    }
  } catch (error) {
    ElMessage.error('获取部门信息失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!deptFormRef.value) return
  
  await deptFormRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (dialogMode.value === 'add') {
          response = await departmentApi.addDepartment(deptForm.value)
        } else {
          response = await departmentApi.updateDepartment(deptForm.value)
        }
        
        if (response.code === 200) {
          ElMessage.success(dialogMode.value === 'add' ? '添加成功' : '更新成功')
          dialogVisible.value = false
          loadDeptList()
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
  deptForm.value = {
    id: null,
    deptName: '',
    deptCode: '',
    parentId: null,
    description: '',
    leader: '',
    phone: '',
    email: '',
    sort: 0,
    status: 1
  }
  if (deptFormRef.value) {
    deptFormRef.value.clearValidate()
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除部门"${row.deptName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await departmentApi.deleteDepartment(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadDeptList()
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
  loadDeptList()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadDeptList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadDeptList()
}

// 分配用户
const handleAssignUser = (row) => {
  currentDept.value = row
  assignUserDialogVisible.value = true
}

// 分配用户成功回调
const handleAssignSuccess = () => {
  ElMessage.success('分配用户成功')
  loadDeptList()
}

onMounted(() => {
  loadDeptList()
})
</script>

<style lang="scss" scoped>
.dept-page {
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

.dept-content {
  margin-bottom: 24px;
}

.table-view {
  background: transparent;
  border: none;
  border-radius: 0;
  overflow: visible;
  box-shadow: none;
}

.dept-table {
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

.tree-view {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 20px;
  box-shadow: var(--shadow-lg);
}

.dept-tree-container {
  .dept-tree {
    :deep(.el-tree-node__content) {
      height: auto;
      padding: 8px 0;
      
      &:hover {
        background: var(--bg-tertiary);
      }
    }
    
    :deep(.el-tree-node__label) {
      flex: 1;
    }
  }
}

.tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 8px 0;
  
  .node-content {
    display: flex;
    align-items: center;
    gap: 8px;
    flex: 1;
    
    .dept-icon {
      color: var(--text-secondary);
    }
    
    .node-label {
      font-weight: 500;
      color: var(--text-primary);
    }
    
    .node-status {
      margin-left: 8px;
    }
  }
  
  .node-actions {
    display: flex;
    gap: 8px;
    opacity: 0;
    transition: opacity 0.3s ease;
  }
  
  &:hover .node-actions {
    opacity: 1;
  }
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
  
  .tree-node {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
    
    .node-actions {
      opacity: 1;
      width: 100%;
      justify-content: flex-end;
    }
  }
}
</style> 