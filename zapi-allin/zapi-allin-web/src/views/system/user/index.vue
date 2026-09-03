<template>
  <div class="users-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">用户管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAddUser">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          添加用户
        </el-button>
        <el-button>
          <el-icon class="el-icon--left"><Download /></el-icon>
          导出数据
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <div class="search-left">
        <el-input
          v-model="searchQuery"
          placeholder="搜索用户名、姓名、邮箱..."
          prefix-icon="Search"
          style="width: 300px"
          clearable
          @input="handleSearch"
          @clear="handleSearch"
        />
        <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 150px" @change="handleSearch">
          <el-option label="全部" value="" />
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
        <el-button type="primary" @click="handleSearch">
          <el-icon class="el-icon--left"><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon class="el-icon--left"><Refresh /></el-icon>
          刷新
        </el-button>
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

    <!-- 主内容区域：左侧部门树 + 右侧用户列表 -->
    <div class="main-content">
      <!-- 左侧部门树 -->
      <div class="dept-tree-panel">
        <div class="panel-body">
          <el-tree
            :data="deptTree"
            :props="{ label: 'deptName', children: 'children' }"
            node-key="id"
            default-expand-all
            highlight-current
            :expand-on-click-node="false"
            @node-click="handleDeptClick"
            class="dept-tree"
          >
            <template #default="{ node, data }">
              <div class="tree-node-content">
<!--                <el-icon class="dept-icon">-->
<!--                  <OfficeBuilding />-->
<!--                </el-icon>-->
                <span class="node-label">{{ data.deptName }}</span>
              </div>
            </template>
          </el-tree>
        </div>
      </div>

      <!-- 右侧用户列表 -->
      <div class="users-content">
      <!-- 表格视图 -->
      <div v-if="viewMode === 'table'" class="table-view">
        <el-table :data="users" style="width: 100%" class="users-table" v-loading="loading">
          <el-table-column prop="realName" label="姓名" min-width="120" show-overflow-tooltip />
          <el-table-column prop="email" label="邮箱" min-width="200" show-overflow-tooltip />
          <el-table-column prop="username" label="用户名" min-width="120" show-overflow-tooltip />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '活跃' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="lastLoginTime" label="最后登录" min-width="150" show-overflow-tooltip>
            <template #default="{ row }">
              {{ formatDate(row.lastLoginTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="400" fixed="right">
            <template #default="{ row }">
              <el-button size="small" type="primary" link @click="editUser(row)">
                <el-icon class="el-icon--left"><Edit /></el-icon>
                编辑
              </el-button>
              <el-button size="small" type="info" link @click="handleAssignRole(row)">
                <el-icon class="el-icon--left"><UserFilled /></el-icon>
                分配角色
              </el-button>
              <el-button size="small" type="success" link @click="handleAssignDept(row)">
                <el-icon class="el-icon--left"><OfficeBuilding /></el-icon>
                分配部门
              </el-button>
              <el-button
                size="small"
                :type="row.status === 1 ? 'warning' : 'success'"
                link
                @click="toggleUserStatus(row)"
              >
                <el-icon class="el-icon--left"><Switch /></el-icon>
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button
                v-if="row.status === 0"
                size="small"
                type="danger"
                link
                @click="deleteUser(row)"
              >
                <el-icon class="el-icon--left"><Delete /></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 网格视图 -->
      <div v-else class="grid-view">
        <div class="users-grid">
          <div v-for="user in filteredUsers" :key="user.id" class="user-card">
            <div class="user-header">
              <div class="user-status" :class="user.status === 1 ? 'active' : 'inactive'"></div>
            </div>
            <div class="user-info">
              <h3 class="user-name">{{ user.realName || user.username }}</h3>
              <p class="user-email">{{ user.email }}</p>
            </div>
            <div class="user-actions">
              <el-button size="small" type="primary" @click="editUser(user)">编辑</el-button>
              <el-button
                size="small"
                :type="user.status === 1 ? 'warning' : 'success'"
                @click="toggleUserStatus(user)"
              >
                {{ user.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button
                v-if="user.status === 0"
                size="small"
                type="danger"
                @click="deleteUser(user)"
              >
                删除
              </el-button>
            </div>
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
        :total="totalUsers"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 新增/编辑用户对话框 -->
    <el-dialog
      v-model="showUserDialog"
      :title="isEditMode ? '编辑用户' : '新增用户'"
      width="600px"
      @close="resetUserForm"
    >
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="userForm.username"
                placeholder="请输入用户名"
                :disabled="isEditMode"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="真实姓名" prop="realName">
              <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
        <el-form-item label="邮箱" prop="email">
              <el-input v-model="userForm.email" placeholder="请输入邮箱地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="userForm.phone" placeholder="请输入手机号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20" v-if="!isEditMode">
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input
                v-model="userForm.password"
                type="password"
                placeholder="请输入密码"
                show-password
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="userForm.confirmPassword"
                type="password"
                placeholder="请再次输入密码"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="userForm.status">
                <el-radio :label="1">启用</el-radio>
                <el-radio :label="0">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="userForm.gender">
                <el-radio :label="1">男</el-radio>
                <el-radio :label="2">女</el-radio>
                <el-radio :label="0">未知</el-radio>
          </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <el-button @click="showUserDialog = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleSubmitUser"
          :loading="submitting"
        >
          {{ isEditMode ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>

    <!-- 分配角色对话框 -->
    <AssignRoleDialog
      v-model="assignRoleDialogVisible"
      :user="currentUserForRole"
      @success="handleAssignRoleSuccess"
    />

    <!-- 分配部门对话框 -->
    <el-dialog
      v-model="assignDeptDialogVisible"
      title="分配部门"
      width="500px"
      @close="handleAssignDeptClose"
    >
      <div class="assign-dept-content">
        <div class="user-info-section">
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="用户名">{{ currentUserForDept?.username }}</el-descriptions-item>
            <el-descriptions-item label="姓名">{{ currentUserForDept?.realName }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <el-divider content-position="left">选择部门</el-divider>
        
        <el-tree
          ref="deptTreeRef"
          :data="deptTreeForAssign"
          :props="{ label: 'deptName', children: 'children' }"
          node-key="id"
          show-checkbox
          default-expand-all
          :check-strictly="false"
          class="dept-select-tree"
        >
          <template #default="{ node, data }">
            <div class="tree-node-content">
              <el-icon class="dept-icon"><OfficeBuilding /></el-icon>
              <span class="node-label">{{ data.deptName }}</span>
            </div>
          </template>
        </el-tree>
      </div>

      <template #footer>
        <el-button @click="assignDeptDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitAssignDept" :loading="assignDeptLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, List, Grid, Search, Edit, Delete, Switch, Refresh, UserFilled, OfficeBuilding } from '@element-plus/icons-vue'
import { userApi, userOrgApi, departmentApi } from '@/api'
import AssignRoleDialog from './AssignRoleDialog.vue'

// 响应式数据
const searchQuery = ref('')
const statusFilter = ref('')
const viewMode = ref('table')
const currentPage = ref(1)
const pageSize = ref(20)
const showUserDialog = ref(false)
const isEditMode = ref(false)
const submitting = ref(false)
const userFormRef = ref()

// 部门树相关
const deptTree = ref([])
const selectedDeptId = ref(null)

// 角色分配相关
const assignRoleDialogVisible = ref(false)
const currentUserForRole = ref(null)

// 部门分配相关
const assignDeptDialogVisible = ref(false)
const currentUserForDept = ref(null)
const deptTreeRef = ref(null)
const deptTreeForAssign = ref([])
const assignDeptLoading = ref(false)

// 用户表单
const userForm = ref({
  id: '',
  username: '',
  realName: '',
  email: '',
  phone: '',
  password: '',
  confirmPassword: '',
  status: 1,
  gender: 0
})

// 自定义验证规则
const validatePassword = (rule, value, callback) => {
  if (!isEditMode.value) {
    if (!value) {
      callback(new Error('请输入密码'))
    } else if (value.length < 6) {
      callback(new Error('密码长度至少6位'))
    } else {
      callback()
    }
  } else {
    callback()
  }
}

const validateConfirmPassword = (rule, value, callback) => {
  if (!isEditMode.value) {
    if (!value) {
      callback(new Error('请再次输入密码'))
    } else if (value !== userForm.value.password) {
      callback(new Error('两次输入的密码不一致'))
    } else {
      callback()
    }
  } else {
    callback()
  }
}

const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3到20个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2到20个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  password: [{ validator: validatePassword, trigger: 'blur' }],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
}

// 用户数据
const users = ref([])
const loading = ref(false)
const totalUsers = ref(0)

// 计算属性
const filteredUsers = computed(() => {
  let filtered = users.value

  if (searchQuery.value) {
    filtered = filtered.filter(user =>
      user.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.value.toLowerCase())
    )
  }

  if (statusFilter.value) {
    filtered = filtered.filter(user => user.status === statusFilter.value)
  }

  return filtered
})



// 加载用户列表
const loadUserList = async () => {
  loading.value = true
  try {
    const params = {
      current: currentPage.value,
      size: pageSize.value
    }

    // 添加搜索条件
    if (searchQuery.value) {
      params.keyword = searchQuery.value
    }

    // 添加状态筛选
    if (statusFilter.value !== '') {
      params.status = parseInt(statusFilter.value)
    }

    // 根据是否选中部门，调用不同的API
    let response
    if (selectedDeptId.value) {
      // 如果选中了部门，获取该部门下的用户
      response = await userOrgApi.getUsersByOrgId(selectedDeptId.value, params)
    } else {
      // 否则获取所有用户
      response = await userApi.getUserList(params)
    }

    if (response.code === 200) {
      users.value = response.data.records || []
      totalUsers.value = Number(response.data.total) || 0
    } else {
      ElMessage.error(response.message || '加载用户列表失败')
    }
  } catch (error) {
    console.error('加载用户列表失败:', error)
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

// 新增用户
const handleAddUser = () => {
  isEditMode.value = false
  resetUserForm()
  showUserDialog.value = true
}

// 编辑用户
const editUser = async (user) => {
  try {
    isEditMode.value = true

    // 获取用户详细信息
    const response = await userApi.getUserDetail(user.id)
    if (response.code === 200) {
      const userData = response.data

      // 填充表单数据
      userForm.value = {
        id: userData.id,
        username: userData.username,
        realName: userData.realName,
        email: userData.email,
        phone: userData.phone || '',
        password: '',
        confirmPassword: '',
        status: userData.status,
        gender: userData.gender || 0
      }

      showUserDialog.value = true
    } else {
      ElMessage.error(response.message || '获取用户信息失败')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 删除用户
const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${user.realName || user.username}" 吗？此操作不可恢复！`,
      '确认删除',
      {
        type: 'warning',
        confirmButtonText: '确定删除',
        cancelButtonText: '取消'
      }
    )

    const response = await userApi.deleteUser(user.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadUserList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除用户失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 打开分配角色对话框
const handleAssignRole = (user) => {
  currentUserForRole.value = user
  assignRoleDialogVisible.value = true
}

// 分配角色成功回调
const handleAssignRoleSuccess = () => {
  ElMessage.success('角色分配成功')
  // 可以选择刷新列表或不刷新
}

// 打开分配部门对话框
const handleAssignDept = async (user) => {
  currentUserForDept.value = user
  assignDeptDialogVisible.value = true
  
  // 加载部门树（不包含"全部"节点）
  try {
    const response = await departmentApi.getDepartmentTree()
    if (response.code === 200) {
      deptTreeForAssign.value = response.data || []
    }
  } catch (error) {
    console.error('加载部门树失败:', error)
    ElMessage.error('加载部门树失败')
  }
  
  // 加载用户已分配的部门
  try {
    const response = await userOrgApi.getOrgIdsByUserId(user.id)
    if (response.code === 200) {
      const orgIds = response.data || []
      // 等待树渲染完成后设置选中
      setTimeout(() => {
        if (deptTreeRef.value) {
          deptTreeRef.value.setCheckedKeys(orgIds)
        }
      }, 100)
    }
  } catch (error) {
    console.error('加载用户部门失败:', error)
  }
}

// 提交分配部门
const handleSubmitAssignDept = async () => {
  if (!currentUserForDept.value) return
  
  try {
    assignDeptLoading.value = true
    
    // 获取选中的部门ID
    const checkedKeys = deptTreeRef.value.getCheckedKeys()
    
    // 调用API分配部门
    const response = await userOrgApi.assignOrgsToUser(currentUserForDept.value.id, checkedKeys)
    
    if (response.code === 200) {
      ElMessage.success('部门分配成功')
      assignDeptDialogVisible.value = false
      // 如果当前在某个部门下查看，刷新列表
      if (selectedDeptId.value) {
        loadUserList()
      }
    } else {
      ElMessage.error(response.message || '部门分配失败')
    }
  } catch (error) {
    console.error('分配部门失败:', error)
    ElMessage.error('部门分配失败')
  } finally {
    assignDeptLoading.value = false
  }
}

// 关闭分配部门对话框
const handleAssignDeptClose = () => {
  currentUserForDept.value = null
  if (deptTreeRef.value) {
    deptTreeRef.value.setCheckedKeys([])
  }
}

// 提交用户表单
const handleSubmitUser = async () => {
  if (!userFormRef.value) return

  try {
    // 表单验证
    await userFormRef.value.validate()

    submitting.value = true

    // 准备提交数据
    const submitData = {
      username: userForm.value.username,
      realName: userForm.value.realName,
      email: userForm.value.email,
      phone: userForm.value.phone,
      status: userForm.value.status,
      gender: userForm.value.gender
    }

    let response
    if (isEditMode.value) {
      // 编辑模式
      submitData.id = userForm.value.id
      response = await userApi.updateUser(submitData)
    } else {
      // 新增模式
      submitData.password = userForm.value.password
      response = await userApi.createUser(submitData)
    }

    if (response.code === 200) {
      ElMessage.success(isEditMode.value ? '更新成功' : '创建成功')
      showUserDialog.value = false
      loadUserList()
    } else {
      ElMessage.error(response.message || (isEditMode.value ? '更新失败' : '创建失败'))
    }
  } catch (error) {
    if (error && typeof error === 'object' && error.message) {
      // 表单验证错误
      return
    }
    console.error('提交用户失败:', error)
    ElMessage.error(isEditMode.value ? '更新失败' : '创建失败')
  } finally {
    submitting.value = false
  }
}

// 重置用户表单
const resetUserForm = () => {
  if (userFormRef.value) {
    userFormRef.value.resetFields()
  }
  userForm.value = {
    id: '',
    username: '',
    realName: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
    status: 1,
    gender: 0
  }
}

// 切换用户状态
const toggleUserStatus = async (user) => {
  try {
    const newStatus = user.status === 1 ? 0 : 1
    const statusText = newStatus === 1 ? '启用' : '禁用'

    await ElMessageBox.confirm(
      `确定要${statusText}用户 "${user.realName || user.username}" 吗？`,
      '确认操作',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )

    const response = await userApi.updateUserStatus(user.id, newStatus)
    if (response.code === 200) {
      ElMessage.success(`${statusText}成功`)
      loadUserList()
    } else {
      ElMessage.error(response.message || `${statusText}失败`)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('切换用户状态失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadUserList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadUserList()
}

// 搜索处理
const handleSearch = () => {
  currentPage.value = 1
  loadUserList()
}

// 刷新数据
const handleRefresh = () => {
  searchQuery.value = ''
  statusFilter.value = ''
  currentPage.value = 1
  loadUserList()
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return '从未登录'

  try {
    const date = new Date(dateString)
    if (isNaN(date.getTime())) {
      return '无效时间'
    }
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    })
  } catch (error) {
    console.error('日期格式化错误:', error, dateString)
    return '时间格式错误'
  }
}

// 加载部门树
const loadDeptTree = async () => {
  try {
    const response = await departmentApi.getDepartmentTree()
    if (response.code === 200) {
      // 在树的最前面添加"全部"节点
      deptTree.value = [
        {
          id: null,
          deptName: '全部',
          children: []
        },
        ...(response.data || [])
      ]
    }
  } catch (error) {
    console.error('加载部门树失败:', error)
    ElMessage.error('加载部门树失败')
  }
}

// 处理部门点击
const handleDeptClick = (data) => {
  selectedDeptId.value = data.id
  currentPage.value = 1
  loadUserList()
}

// 生命周期
onMounted(() => {
  loadDeptTree()
  loadUserList()
})
</script>

<style lang="scss" scoped>
.users-page {
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
  align-items: center;
  flex-wrap: wrap;
}

// 主内容区域：左右布局
.main-content {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
}

// 左侧部门树面板
.dept-tree-panel {
  width: 280px;
  flex-shrink: 0;
  background: var(--bg-card);
  border: 0 px solid var(--border-color);
  // box-shadow: var(--shadow-lg);
  overflow: hidden;

  .panel-header {
    padding: 16px 20px;
    border-bottom: 1px solid var(--border-color);
    background: var(--bg-secondary);

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: var(--text-primary);
    }
  }

  .panel-body {
    padding: 12px;
    max-height: calc(100vh - 400px);
    overflow-y: auto;
  }
}

.dept-tree {
  :deep(.el-tree-node__content) {
    height: auto;
    padding: 8px 0;
    border-radius: 8px;

    &:hover {
      background: var(--bg-tertiary);
    }
  }

  :deep(.el-tree-node.is-current > .el-tree-node__content) {
    background: var(--primary-color);
    color: white;

    .dept-icon {
      color: white;
    }
  }
}

.tree-node-content {
  display: flex;
  align-items: center;
  gap: 8px;

  .dept-icon {
    color: var(--text-secondary);
    font-size: 16px;
  }

  .node-label {
    font-size: 14px;
  }
}

// 右侧用户列表
.users-content {
  flex: 1;
  min-width: 0;
}

.table-view {
  background: transparent;
  border: none;
  border-radius: 0;
  overflow: visible;
  box-shadow: none;
}

.users-table {
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
  .users-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 24px;
  }
}

.user-card {
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

.user-header {
  position: relative;
  margin-bottom: 16px;
  display: flex;
  justify-content: center;

  .user-status {
    width: 16px;
    height: 16px;
    border-radius: 50%;
    border: 3px solid var(--bg-card);

    &.active {
      background: var(--success-color);
    }

    &.inactive {
      background: var(--danger-color);
    }
  }
}

.user-info {
  margin-bottom: 20px;

  .user-name {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin-bottom: 8px;
  }

  .user-email {
    color: var(--text-secondary);
    font-size: 14px;
    margin-bottom: 4px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .user-role {
    color: var(--text-muted);
    font-size: 12px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
}

.user-actions {
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

    .el-input,
    .el-select {
      width: 100% !important;
    }
  }

  .search-right {
    width: 100%;
    justify-content: center;
  }

  .users-grid {
    grid-template-columns: 1fr;
  }

  .user-actions {
    flex-direction: column;
    gap: 8px;

    .el-button {
      width: 100%;
    }
  }
}

// 分配部门对话框样式
.assign-dept-content {
  .user-info-section {
    margin-bottom: 20px;
  }
  
  .dept-select-tree {
    max-height: 400px;
    overflow-y: auto;
    border: 1px solid var(--border-color);
    border-radius: 8px;
    padding: 12px;
    
    :deep(.el-tree-node__content) {
      height: auto;
      padding: 8px 0;
      border-radius: 6px;
      
      &:hover {
        background: var(--bg-tertiary);
      }
    }
  }
  
  .tree-node-content {
    display: flex;
    align-items: center;
    gap: 8px;
    
    .dept-icon {
      color: var(--text-secondary);
      font-size: 16px;
    }
    
    .node-label {
      font-size: 14px;
    }
  }
}
</style>
