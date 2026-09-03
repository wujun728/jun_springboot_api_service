<template>
  <div class="menu-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">菜单管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加菜单
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <div class="search-left">
        <el-input
          v-model="searchQuery"
          placeholder="搜索菜单..."
          prefix-icon="Search"
          style="width: 300px"
          clearable
        />
        <el-select v-model="menuTypeFilter" placeholder="菜单类型" style="width: 150px">
          <el-option label="全部" value="" />
          <el-option label="目录" value="1" />
          <el-option label="菜单" value="2" />
          <el-option label="按钮" value="3" />
        </el-select>
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

    <!-- 菜单列表 -->
    <div class="menu-content">
      <!-- 表格视图 -->
      <div v-if="viewMode === 'table'" class="table-view">
        <el-table
          :data="filteredMenus"
          row-key="id"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          style="width: 100%"
          class="menu-table"
          v-loading="loading"
        >
          <el-table-column prop="menuName" label="菜单名称" min-width="200">
            <template #default="{ row }">
              <el-icon v-if="row.icon" class="menu-icon">
                <component :is="row.icon" />
              </el-icon>
              <span>{{ row.menuName }}</span>
            </template>
          </el-table-column>
          
          <el-table-column prop="menuCode" label="菜单编码" min-width="150" show-overflow-tooltip />
          
          <el-table-column prop="menuType" label="菜单类型" width="100">
            <template #default="{ row }">
              <el-tag :type="getMenuTypeTag(row.menuType)">
                {{ getMenuTypeText(row.menuType) }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="path" label="路由路径" min-width="150" show-overflow-tooltip />
          
          <el-table-column prop="component" label="组件路径" min-width="200" show-overflow-tooltip />
          
          <el-table-column prop="sort" label="排序" width="80" />
          
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column prop="visible" label="可见性" width="80">
            <template #default="{ row }">
              <el-tag :type="row.visible === 1 ? 'success' : 'info'">
                {{ row.visible === 1 ? '显示' : '隐藏' }}
              </el-tag>
            </template>
          </el-table-column>
          
          <el-table-column label="操作" width="280" fixed="right">
            <template #default="{ row }">
              <el-button size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button size="small" type="success" @click="handleAddChild(row)">添加子菜单</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 树形视图 -->
      <div v-else class="tree-view">
        <div class="menu-tree-container">
          <el-tree
            :data="filteredMenus"
            :props="{ label: 'menuName', children: 'children' }"
            node-key="id"
            default-expand-all
            class="menu-tree"
          >
            <template #default="{ node, data }">
              <div class="tree-node">
                <div class="node-content">
                  <el-icon v-if="data.icon" class="menu-icon">
                    <component :is="data.icon" />
                  </el-icon>
                  <span class="node-label">{{ data.menuName }}</span>
                  <el-tag size="small" :type="getMenuTypeTag(data.menuType)" class="node-type">
                    {{ getMenuTypeText(data.menuType) }}
                  </el-tag>
                  <el-tag size="small" :type="data.status === 1 ? 'success' : 'danger'" class="node-status">
                    {{ data.status === 1 ? '启用' : '禁用' }}
                  </el-tag>
                </div>
                <div class="node-actions">
                  <el-button size="small" @click.stop="handleEdit(data)">编辑</el-button>
                  <el-button size="small" type="success" @click.stop="handleAddChild(data)">添加子菜单</el-button>
                  <el-button size="small" type="danger" @click.stop="handleDelete(data)">删除</el-button>
                </div>
              </div>
            </template>
          </el-tree>
        </div>
      </div>
    </div>

    <!-- 菜单编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="menuFormRef"
        :model="menuForm"
        :rules="menuRules"
        label-width="100px"
      >
        <el-form-item label="父级菜单" prop="parentId">
          <el-tree-select
            v-model="menuForm.parentId"
            :data="menuTreeData"
            :props="{
              label: 'menuName',
              value: 'id',
              children: 'children'
            }"
            placeholder="请选择父级菜单"
            clearable
            check-strictly
            :render-after-expand="false"
            :default-expand-all="true"
            popper-class="menu-tree-select-dropdown"
            style="width: 100%"
            class="menu-tree-select"
          />
        </el-form-item>
        
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="menuForm.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        
        <el-form-item label="菜单编码" prop="menuCode">
          <el-input v-model="menuForm.menuCode" placeholder="请输入菜单编码" />
        </el-form-item>
        
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="menuForm.menuType">
            <el-radio :label="1">目录</el-radio>
            <el-radio :label="2">菜单</el-radio>
            <el-radio :label="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="路由路径" prop="path" v-if="menuForm.menuType !== 3">
          <el-input v-model="menuForm.path" placeholder="请输入路由路径" />
        </el-form-item>
        
        <el-form-item label="组件路径" prop="component" v-if="menuForm.menuType === 2">
          <el-input v-model="menuForm.component" placeholder="system/user/index">
            <template #prepend>@/views/</template>
          </el-input>
          <div class="form-item-tip">
            <el-text size="small" type="info">
              <el-icon><InfoFilled /></el-icon>
              示例：system/user/index, dataset/DataSetManage, workflow/index
            </el-text>
          </div>
        </el-form-item>
        
        <el-form-item label="图标" prop="icon" v-if="menuForm.menuType !== 3">
          <IconSelector v-model="menuForm.icon" />
        </el-form-item>
        
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="menuForm.sort" :min="0" :max="999" />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="menuForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="可见性" prop="visible" v-if="menuForm.menuType !== 3">
          <el-radio-group v-model="menuForm.visible">
            <el-radio :label="1">显示</el-radio>
            <el-radio :label="0">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, List, Grid, InfoFilled } from '@element-plus/icons-vue'
import { menuApi } from '@/api'
import IconSelector from '@/components/IconSelector/index.vue'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const menuFormRef = ref()
const menuList = ref([])
const menuTreeData = ref([])
const searchQuery = ref('')
const menuTypeFilter = ref('')
const statusFilter = ref('')
const viewMode = ref('table')

// 菜单表单
const menuForm = reactive({
  id: null,
  parentId: 0,
  menuName: '',
  menuCode: '',
  menuType: 1,
  path: '',
  component: '',
  icon: '',
  sort: 0,
  status: 1,
  visible: 1
})

// 表单验证规则
const menuRules = {
  menuName: [
    { required: true, message: '请输入菜单名称', trigger: 'blur' }
  ],
  menuCode: [
    { required: true, message: '请输入菜单编码', trigger: 'blur' }
  ],
  menuType: [
    { required: true, message: '请选择菜单类型', trigger: 'change' }
  ]
}

// 计算属性
const isEdit = computed(() => menuForm.id !== null)

const filteredMenus = computed(() => {
  let filtered = menuList.value

  if (searchQuery.value) {
    filtered = filterMenusByQuery(filtered, searchQuery.value)
  }

  if (menuTypeFilter.value !== '') {
    filtered = filterMenusByType(filtered, parseInt(menuTypeFilter.value))
  }

  if (statusFilter.value !== '') {
    filtered = filterMenusByStatus(filtered, parseInt(statusFilter.value))
  }

  return filtered
})

// 递归过滤菜单
const filterMenusByQuery = (menus, query) => {
  return menus.filter(menu => {
    const matches = menu.menuName.toLowerCase().includes(query.toLowerCase()) ||
                   menu.menuCode.toLowerCase().includes(query.toLowerCase())
    
    if (menu.children && menu.children.length > 0) {
      menu.children = filterMenusByQuery(menu.children, query)
      return matches || menu.children.length > 0
    }
    
    return matches
  })
}

const filterMenusByType = (menus, type) => {
  return menus.filter(menu => {
    const matches = menu.menuType === type
    
    if (menu.children && menu.children.length > 0) {
      menu.children = filterMenusByType(menu.children, type)
      return matches || menu.children.length > 0
    }
    
    return matches
  })
}

const filterMenusByStatus = (menus, status) => {
  return menus.filter(menu => {
    const matches = menu.status === status
    
    if (menu.children && menu.children.length > 0) {
      menu.children = filterMenusByStatus(menu.children, status)
      return matches || menu.children.length > 0
    }
    
    return matches
  })
}

// 方法
const loadMenuList = async () => {
  loading.value = true
  try {
    const response = await menuApi.getMenuTree()
    if (response.code === 200) {
      menuList.value = response.data || []
      // 为树形选择器准备数据，添加顶级菜单选项
      const treeData = [
        {
          id: 0,
          menuName: '顶级菜单',
          children: response.data || []
        }
      ]
      menuTreeData.value = treeData
    }
  } catch (error) {
    console.error('加载菜单列表失败:', error)
    ElMessage.error('加载菜单列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增菜单'
  dialogVisible.value = true
}

const handleAddChild = (row) => {
  // 重置表单
  resetForm()
  // 设置父级菜单
  menuForm.parentId = row.id
  dialogTitle.value = '新增子菜单'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  // 重置表单
  resetForm()
  // 复制数据到表单
  Object.assign(menuForm, {
    id: row.id,
    parentId: row.parentId || 0,
    menuName: row.menuName,
    menuCode: row.menuCode,
    menuType: row.menuType,
    path: row.path || '',
    component: row.component || '',
    icon: row.icon || '',
    sort: row.sort || 0,
    status: row.status,
    visible: row.visible
  })
  dialogTitle.value = '编辑菜单'
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除菜单"${row.menuName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await menuApi.deleteMenu(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadMenuList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  if (!menuFormRef.value) return
  
  try {
    await menuFormRef.value.validate()
    submitting.value = true
    
    const api = isEdit.value ? menuApi.updateMenu : menuApi.addMenu
    const response = await api(menuForm)
    
    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      loadMenuList()
    } else {
      ElMessage.error(response.message || (isEdit.value ? '更新失败' : '添加失败'))
    }
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitting.value = false
  }
}

const handleRefresh = () => {
  loadMenuList()
}

const handleDialogClose = () => {
  resetForm()
}

const resetForm = () => {
  // 重置表单数据
  menuForm.id = null
  menuForm.parentId = 0
  menuForm.menuName = ''
  menuForm.menuCode = ''
  menuForm.menuType = 1
  menuForm.path = ''
  menuForm.component = ''
  menuForm.icon = ''
  menuForm.sort = 0
  menuForm.status = 1
  menuForm.visible = 1
  
  // 清除表单验证
  if (menuFormRef.value) {
    menuFormRef.value.clearValidate()
  }
  
}

const getMenuTypeText = (type) => {
  const typeMap = {
    1: '目录',
    2: '菜单',
    3: '按钮'
  }
  return typeMap[type] || '未知'
}

const getMenuTypeTag = (type) => {
  const tagMap = {
    1: 'primary',
    2: 'success',
    3: 'warning'
  }
  return tagMap[type] || 'info'
}

// 生命周期
onMounted(() => {
  loadMenuList()
})
</script>

<style lang="scss" scoped>
.menu-page {
  animation: fadeIn 0.6s ease-out;
  padding: 20px;
  background: var(--bg-secondary, #f8fafc);
  min-height: calc(100vh - 80px);
}

// 表单提示样式
.form-item-tip {
  margin-top: 4px;
  
  .el-text {
    display: flex;
    align-items: center;
    gap: 4px;
    color: var(--el-text-color-secondary);
  }
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

.menu-content {
  margin-bottom: 24px;
}

.table-view {
  background: transparent;
  border: none;
  border-radius: 0;
  overflow: visible;
  box-shadow: none;
}

.menu-table {
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
  
  .menu-icon {
    margin-right: 8px;
    color: var(--text-secondary);
  }
}

.tree-view {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 20px;
  box-shadow: var(--shadow-lg);
}

.menu-tree-container {
  .menu-tree {
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
    
    .menu-icon {
      color: var(--text-secondary);
    }
    
    .node-label {
      font-weight: 500;
      color: var(--text-primary);
    }
    
    .node-type {
      margin-left: 8px;
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

// 树形选择器样式
.menu-tree-select {
  :deep(.el-tree-select) {
    width: 100%;
  }
}
</style> 