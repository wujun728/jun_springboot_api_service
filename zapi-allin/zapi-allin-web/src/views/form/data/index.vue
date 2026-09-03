<template>
  <div class="form-data-page">
    <!-- 左侧表单列表导航 -->
    <div class="form-nav">
      <div class="nav-header">
        <h3>表单列表</h3>
        <el-button size="small" @click="refreshFormList">
          <el-icon><Refresh /></el-icon>
        </el-button>
      </div>
      
      <div class="nav-content">
        <div v-if="formListLoading" class="loading-container">
          <el-skeleton :rows="5" animated />
        </div>
        
        <div v-else-if="formList.length === 0" class="empty-container">
          <el-empty description="暂无表单数据" />
        </div>
        
        <div v-else class="form-list">
          <div
            v-for="form in formList"
            :key="form.id"
            :class="['form-item', { active: selectedFormId === form.id }]"
            @click="selectForm(form)"
          >
            <div class="form-info">
              <div class="form-name">{{ form.formName }}</div>
              <div class="form-desc" v-if="form.formDescription">
                {{ form.formDescription }}
              </div>
            </div>
            <div class="form-status">
              <el-tag :type="form.status === 1 ? 'success' : 'info'" size="small">
                {{ form.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧数据列表 -->
    <div class="data-content">
      <div v-if="!selectedFormId" class="no-selection">
        <el-empty description="请选择一个表单查看数据" />
      </div>
      
      <div v-else class="data-panel">
        <!-- 数据列表头部 -->
        <div class="data-header">
          <div class="header-left">
            <h3>{{ selectedForm?.formName }} - 业务数据</h3>
            <el-tag v-if="selectedForm" :type="selectedForm.status === 1 ? 'success' : 'info'">
              {{ selectedForm.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </div>
          <div class="header-right">
            <el-button 
              v-if="selectedRows.length > 0" 
              type="danger" 
              plain
              @click="handleBatchDelete"
            >
              <el-icon><Delete /></el-icon>
              批量删除 ({{ selectedRows.length }})
            </el-button>
            <el-button @click="refreshDataList">
              <el-icon><Refresh /></el-icon>
              刷新数据
            </el-button>
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新增数据
            </el-button>
          </div>
        </div>

        <!-- 数据表格 -->
        <div class="data-table">
          <div class="table-wrapper">
            <el-table
              v-loading="dataListLoading"
              :data="dataList"
              style="width: 100%"
              empty-text="暂无数据"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              
              <!-- 动态渲染业务字段列 -->
              <el-table-column
                v-for="column in businessColumns"
                :key="column.columnName"
                :prop="column.columnName"
                :label="column.columnComment"
                :min-width="getColumnWidth(column)"
                show-overflow-tooltip
              >
                <template #default="{ row }">
                  <span>{{ formatCellValue(row[column.columnName], column) }}</span>
                </template>
              </el-table-column>
              
              <!-- 系统字段 -->
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
              
              <el-table-column label="操作" width="120" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" link size="small" @click="handleEdit(row)">
                    编辑
                  </el-button>
                  <el-button type="danger" link size="small" @click="handleDelete(row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 分页 -->
          <div class="pagination-wrapper">
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
        </div>
      </div>
    </div>

    <!-- 新增数据抽屉 -->
    <el-drawer
      v-model="addDrawerVisible"
      :title="`新增 - ${selectedForm?.formName}`"
      size="50%"
      direction="rtl"
      :before-close="handleDrawerClose"
    >
      <div class="add-form-container">
        <div v-if="!selectedForm" class="no-form">
          <el-empty description="请先选择表单" />
        </div>
        
        <div v-else-if="formConfigLoading" class="loading-container">
          <el-skeleton :rows="8" animated />
        </div>
        
        <div v-else-if="!formConfig" class="no-config">
          <el-empty description="表单配置为空" />
        </div>
        
        <div v-else class="form-content">
          <FormPreview ref="addFormPreviewRef" :form-config="formConfig" />
        </div>
      </div>

      <template #footer>
        <div class="drawer-footer">
          <el-button @click="addDrawerVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmitAdd">
            <el-icon><Document /></el-icon>
            保存
          </el-button>
        </div>
      </template>
    </el-drawer>

    <!-- 编辑数据抽屉 -->
    <el-drawer
      v-model="editDrawerVisible"
      :title="`编辑 - ${selectedForm?.formName}`"
      size="50%"
      direction="rtl"
      :before-close="handleDrawerClose"
    >
      <div class="edit-form-container">
        <div v-if="!selectedForm" class="no-form">
          <el-empty description="请先选择表单" />
        </div>
        
        <div v-else-if="formConfigLoading" class="loading-container">
          <el-skeleton :rows="8" animated />
        </div>
        
        <div v-else-if="!formConfig" class="no-config">
          <el-empty description="表单配置为空" />
        </div>
        
        <div v-else class="form-content">
          <FormPreview ref="editFormPreviewRef" :form-config="formConfig" :initial-data="currentEditData" />
        </div>
      </div>

      <template #footer>
        <div class="drawer-footer">
          <el-button @click="editDrawerVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitLoading" @click="handleSubmitEdit">
            <el-icon><Document /></el-icon>
            更新
          </el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Refresh, Plus, Document, Delete } from '@element-plus/icons-vue'
import { formApi, fileApi } from '@/api'
import { userApi } from '@/api/user'
import { useUserStore } from '@/stores/user'
import { departmentApi } from '@/api/department'
import { roleApi } from '@/api/role'
import FormPreview from '@/components/form-designer/FormPreview.vue'

// 响应式数据
const formListLoading = ref(false)
const dataListLoading = ref(false)
const formList = ref<any[]>([])
const selectedFormId = ref<string>('')
const selectedForm = computed(() => formList.value.find(form => form.id === selectedFormId.value))
const dataList = ref<any[]>([])
const businessColumns = ref<any[]>([]) // 业务字段列信息
const selectedRows = ref<any[]>([])

// 新增功能相关
const addDrawerVisible = ref(false)
const formConfig = ref<any>(null)
const formConfigLoading = ref(false)
const submitLoading = ref(false)
const addFormPreviewRef = ref()

// 编辑功能相关
const editDrawerVisible = ref(false)
const currentEditData = ref<any>(null)
const currentEditRow = ref<any>(null)
const editFormPreviewRef = ref()

// 分页数据
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 业务数据缓存（用于显示名称）
const userMap = ref<Map<string, any>>(new Map())
const departmentMap = ref<Map<string, any>>(new Map())
const roleMap = ref<Map<string, any>>(new Map())

// 加载业务数据缓存
const userStore = useUserStore()

const loadBusinessDataCache = async () => {
  try {
    // 并行加载用户、部门、角色数据
    const [users, deptsRes, rolesRes] = await Promise.all([
      userStore.loadEnabledUsers().catch(() => []),
      departmentApi.getDepartmentTree().catch(() => ({ data: [] })),
      roleApi.getAllRoles().catch(() => ({ data: [] }))
    ])
    
    // 构建用户映射
    users.forEach((user: any) => {
      userMap.value.set(String(user.id), user)
    })
    
    // 构建部门映射（递归展开树结构）
    const flattenDepts = (depts: any[], result: any[] = []) => {
      depts.forEach(dept => {
        result.push(dept)
        if (dept.children && dept.children.length) {
          flattenDepts(dept.children, result)
        }
      })
      return result
    }
    const depts = flattenDepts(deptsRes.data || [])
    depts.forEach((dept: any) => {
      departmentMap.value.set(String(dept.id), dept)
    })
    
    // 构建角色映射
    const roles = rolesRes.data || []
    roles.forEach((role: any) => {
      roleMap.value.set(String(role.id), role)
    })
  } catch (error) {
    console.error('加载业务数据缓存失败:', error)
  }
}

// 获取表单列表
const getFormList = async () => {
  formListLoading.value = true
  try {
    const response = await formApi.getFormList({
      current: 1,
      size: 1000, // 获取所有表单
      status: 1 // 只获取启用的表单
    })
    formList.value = response.data.records || []
    
    // 默认选择第一个表单
    if (formList.value.length > 0 && !selectedFormId.value) {
      selectForm(formList.value[0])
    }
  } catch (error) {
    console.error('获取表单列表失败:', error)
    ElMessage.error('获取表单列表失败')
  } finally {
    formListLoading.value = false
  }
}

// 选择表单
const selectForm = async (form: any) => {
  selectedFormId.value = form.id
  pagination.current = 1 // 重置分页
  await getFormColumns(form.id) // 获取字段信息
  await getFormData(form.id) // 获取数据
}

// 获取表单字段信息
const getFormColumns = async (formId: string) => {
  try {
    const response = await formApi.getTableInfoByFormId(formId)
    const data = response.data
    
    if (data && data.columnInfos && Array.isArray(data.columnInfos)) {
      // 只获取业务字段（非系统字段）
      businessColumns.value = data.columnInfos.filter(column => column.isSysField === 0)
    } else {
      businessColumns.value = []
    }
  } catch (error) {
    console.error('获取表单字段失败:', error)
    businessColumns.value = []
  }
}

// 获取表单数据
const getFormData = async (formId: string) => {
  if (!formId) return
  
  dataListLoading.value = true
  try {
    const response = await formApi.getFormDataPage({
      formId: formId,
      criteria: {}, // 暂时不添加筛选条件
      pageNo: pagination.current,
      pageSize: pagination.size
    })

    const data = response.data
    if (data && data.records) {
      dataList.value = data.records
      pagination.total = Number(data.total) || 0
    } else {
      dataList.value = []
      pagination.total = 0
    }
  } catch (error) {
    console.error('获取表单数据失败:', error)
    ElMessage.error('获取表单数据失败')
    dataList.value = []
    pagination.total = 0
  } finally {
    dataListLoading.value = false
  }
}

// 刷新表单列表
const refreshFormList = () => {
  getFormList()
}

// 刷新数据列表
const refreshDataList = () => {
  if (selectedFormId.value) {
    getFormData(selectedFormId.value)
  }
}

// 分页处理
const handleSizeChange = (val: number) => {
  pagination.size = val
  pagination.current = 1
  if (selectedFormId.value) {
    getFormData(selectedFormId.value)
  }
}

const handleCurrentChange = (val: number) => {
  pagination.current = val
  if (selectedFormId.value) {
    getFormData(selectedFormId.value)
  }
}

// 选择行处理
const handleSelectionChange = (val: any[]) => {
  selectedRows.value = val
}

// 新增数据
const handleAdd = async () => {
  if (!selectedFormId.value) {
    ElMessage.warning('请先选择表单')
    return
  }
  
  // 获取表单配置
  await loadFormConfig(selectedFormId.value)
  addDrawerVisible.value = true
}

// 加载表单配置
const loadFormConfig = async (formId: string) => {
  formConfigLoading.value = true
  try {
    const response = await formApi.getFormDetail(formId)
    const formData = response.data

    if (formData.formConfig) {
      formConfig.value = JSON.parse(formData.formConfig)
    } else {
      formConfig.value = null
      ElMessage.warning('该表单暂无配置信息')
    }
  } catch (error) {
    console.error('加载表单配置失败:', error)
    ElMessage.error('加载表单配置失败')
    formConfig.value = null
  } finally {
    formConfigLoading.value = false
  }
}

// 处理表单数据用于提交：将文件字段转换为ID数组
const processFormDataForSubmit = (formData: any) => {
  if (!formConfig.value || !formConfig.value.components) {
    return formData
  }
  
  const processedData = { ...formData }
  
  // 遍历表单配置，找到所有文件上传组件
  formConfig.value.components.forEach((component: any) => {
    if (component.type === 'upload' && component.name) {
      const fieldName = component.name
      const fieldValue = processedData[fieldName]
      
      // 如果字段有值且是数组，提取ID
      if (fieldValue && Array.isArray(fieldValue)) {
        processedData[fieldName] = fieldValue.map((file: any) => {
          // 如果是对象，提取id；如果已经是字符串/数字，直接使用
          if (typeof file === 'object' && file !== null) {
            return String(file.id || file.fileId || '')
          }
          return String(file)
        }).filter((id: string) => id) // 过滤空值
      } else if (fieldValue === null || fieldValue === undefined) {
        // 空值设置为空数组
        processedData[fieldName] = []
      }
    }
  })
  
  return processedData
}

// 提交新增数据
const handleSubmitAdd = async () => {
  if (!addFormPreviewRef.value) {
    ElMessage.warning('表单预览组件未加载')
    return
  }

  if (!selectedFormId.value) {
    ElMessage.warning('请先选择表单')
    return
  }

  try {
    submitLoading.value = true
    
    // 先进行表单验证
    const isValid = await addFormPreviewRef.value.validate()
    if (!isValid) {
      ElMessage.warning('请完善表单信息')
      submitLoading.value = false
      return
    }
    
    // 获取表单数据
    const formData = addFormPreviewRef.value.getFormData()
    
    // 处理文件字段：转换为ID数组
    const processedData = processFormDataForSubmit(formData)
    
    // 构建提交数据
    const submitData = {
      formId: selectedFormId.value,
      data: processedData
    }


    // 调用后端新增接口
    await formApi.createFormData(submitData)
    
    ElMessage.success('新增成功')
    addDrawerVisible.value = false
    
    // 刷新数据列表
    refreshDataList()
    
  } catch (error) {
    console.error('新增失败:', error)
    ElMessage.error('新增失败')
  } finally {
    submitLoading.value = false
  }
}

// 抽屉关闭前的处理
const handleDrawerClose = (done: () => void) => {
  if (submitLoading.value) {
    ElMessage.warning('正在提交数据，请稍候...')
    return
  }
  // 清空编辑数据
  currentEditData.value = null
  currentEditRow.value = null
  done()
}

// 提交编辑数据
const handleSubmitEdit = async () => {
  if (!editFormPreviewRef.value) {
    ElMessage.warning('表单预览组件未加载')
    return
  }

  if (!selectedFormId.value) {
    ElMessage.warning('请先选择表单')
    return
  }

  if (!currentEditRow.value || !currentEditRow.value.id) {
    ElMessage.warning('缺少数据ID')
    return
  }

  try {
    submitLoading.value = true
    
    // 先进行表单验证
    const isValid = await editFormPreviewRef.value.validate()
    if (!isValid) {
      ElMessage.warning('请完善表单信息')
      submitLoading.value = false
      return
    }
    
    // 获取表单数据
    const formData = editFormPreviewRef.value.getFormData()
    
    // 处理文件字段：转换为ID数组
    const processedData = processFormDataForSubmit(formData)
    
    // 构建提交数据
    const submitData = {
      formId: selectedFormId.value,
      id: currentEditRow.value.id, // 添加数据ID
      data: processedData
    }


    // 调用后端更新接口
    await formApi.updateFormData(submitData)
    
    ElMessage.success('更新成功')
    editDrawerVisible.value = false
    
    // 刷新数据列表
    refreshDataList()
    
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error('更新失败')
  } finally {
    submitLoading.value = false
  }
}

// 编辑数据
const handleEdit = async (row: any) => {
  if (!selectedFormId.value) {
    ElMessage.warning('请先选择表单')
    return
  }
  
  // 保存当前编辑的行数据
  currentEditRow.value = row
  
  // 先加载表单配置（需要用来识别文件字段）
  await loadFormConfig(selectedFormId.value)
  
  // 提取业务数据（排除系统字段）
  const businessData: any = {}
  businessColumns.value.forEach(column => {
    if (row[column.columnName] !== undefined) {
      businessData[column.columnName] = row[column.columnName]
    }
  })
  
  // 处理文件字段：将ID数组转换为文件详情对象数组
  const processedData = await processFormDataForEdit(businessData)
  
  currentEditData.value = processedData
  editDrawerVisible.value = true
}

// 处理表单数据用于编辑：将文件ID数组转换为文件详情对象
const processFormDataForEdit = async (formData: any) => {
  if (!formConfig.value || !formConfig.value.components) {
    return formData
  }
  
  const processedData = { ...formData }
  
  // 找到所有文件上传组件
  const uploadComponents = formConfig.value.components.filter(
    (component: any) => component.type === 'upload' && component.name
  )
  
  // 并行查询所有文件字段的详情
  for (const component of uploadComponents) {
    const fieldName = component.name
    const fieldValue = processedData[fieldName]
    
    // 如果字段有值
    if (fieldValue) {
      let fileIds: string[] = []
      
      // 解析文件ID数组（可能是字符串或数组）
      if (typeof fieldValue === 'string') {
        try {
          const parsed = JSON.parse(fieldValue)
          fileIds = Array.isArray(parsed) ? parsed : [fieldValue]
        } catch {
          fileIds = [fieldValue]
        }
      } else if (Array.isArray(fieldValue)) {
        fileIds = fieldValue.map(id => String(id))
      }
      
      // 根据ID查询文件详情
      if (fileIds.length > 0) {
        const fileDetails = await fetchFileDetailsByIds(fileIds)
        processedData[fieldName] = fileDetails
      } else {
        processedData[fieldName] = []
      }
    } else {
      processedData[fieldName] = []
    }
  }
  
  return processedData
}

// 根据文件ID数组获取文件详情
const fetchFileDetailsByIds = async (fileIds: string[]): Promise<any[]> => {
  const fileDetails: any[] = []
  
  for (const fileId of fileIds) {
    if (!fileId) continue
    
    try {
      const response = await fileApi.getFileInfo(fileId)
      if (response.data) {
        fileDetails.push(response.data)
      }
    } catch (error) {
      console.warn(`获取文件信息失败: fileId=${fileId}`, error)
    }
  }
  
  return fileDetails
}

// 删除数据
const handleDelete = async (row: any) => {
  if (!selectedFormId.value) {
    ElMessage.warning('请先选择表单')
    return
  }

  if (!row.id) {
    ElMessage.warning('数据ID不存在')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除这条数据吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用删除接口
    await formApi.deleteFormData({
      formId: selectedFormId.value,
      id: row.id
    })
    
    ElMessage.success('删除成功')
    refreshDataList()
  } catch (error: any) {
    // 用户取消操作时，error 是 'cancel'
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 批量删除数据
const handleBatchDelete = async () => {
  if (!selectedFormId.value) {
    ElMessage.warning('请先选择表单')
    return
  }

  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择要删除的数据')
    return
  }

  // 提取所有选中行的ID
  const ids = selectedRows.value.map(row => row.id).filter(id => id)
  
  if (ids.length === 0) {
    ElMessage.warning('选中的数据缺少ID')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${ids.length} 条数据吗？此操作不可恢复！`,
      '确认批量删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // 调用批量删除接口
    await formApi.batchDeleteFormData({
      formId: selectedFormId.value,
      ids: ids
    })
    
    ElMessage.success(`成功删除 ${ids.length} 条数据`)
    
    // 清空选中状态
    selectedRows.value = []
    
    // 刷新数据列表
    refreshDataList()
  } catch (error: any) {
    // 用户取消操作时，error 是 'cancel'
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error(error.message || '批量删除失败')
    }
  }
}

// 获取列宽度
const getColumnWidth = (column: any) => {
  const typeWidthMap: Record<string, number> = {
    'varchar': 150,
    'text': 200,
    'int': 100,
    'decimal': 120,
    'date': 120,
    'datetime': 160,
    'tinyint': 80
  }
  return typeWidthMap[column.columnType] || 150
}

// 格式化单元格值
const formatCellValue = (value: any, column: any) => {
  if (value === null || value === undefined) {
    return '-'
  }
  
  // 根据字段类型格式化显示
  switch (column.formType) {
    case 'switch':
      // 处理各种可能的值格式：数字、字符串、布尔值
      if (value === 1 || value === '1' || value === true || value === 'true') {
        return '是'
      }
      return '否'
    case 'checkbox':
      try {
        const arr = JSON.parse(value)
        return Array.isArray(arr) ? arr.join(', ') : value
      } catch {
        return value
      }
    case 'user-select':
      return formatUserValue(value)
    case 'department-select':
      return formatDepartmentValue(value)
    case 'role-select':
      return formatRoleValue(value)
    default:
      return value
  }
}

// 格式化用户值
const formatUserValue = (value: any) => {
  if (!value) return '-'
  
  // 解析值
  let items: any[] = []
  if (typeof value === 'string') {
    try {
      items = JSON.parse(value)
    } catch {
      return value
    }
  } else if (Array.isArray(value)) {
    items = value
  } else {
    return String(value)
  }
  
  // 如果是 [{id, name}] 格式，直接使用 name
  if (items.length > 0 && typeof items[0] === 'object' && items[0].name) {
    return items.map(item => item.name).join(', ')
  }
  
  // 兼容旧格式：纯 ID 数组
  const ids = items.map(String)
  const names = ids.map(id => {
    const user = userMap.value.get(id)
    return user ? (user.realName || user.username) : `用户${id}`
  })
  
  return names.join(', ')
}

// 格式化部门值
const formatDepartmentValue = (value: any) => {
  if (!value) return '-'
  
  // 解析值
  let items: any[] = []
  if (typeof value === 'string') {
    try {
      items = JSON.parse(value)
    } catch {
      return value
    }
  } else if (Array.isArray(value)) {
    items = value
  } else {
    return String(value)
  }
  
  // 如果是 [{id, name}] 格式，直接使用 name
  if (items.length > 0 && typeof items[0] === 'object' && items[0].name) {
    return items.map(item => item.name).join(', ')
  }
  
  // 兼容旧格式：纯 ID 数组
  const ids = items.map(String)
  const names = ids.map(id => {
    const dept = departmentMap.value.get(id)
    return dept ? dept.deptName : `部门${id}`
  })
  
  return names.join(', ')
}

// 格式化角色值
const formatRoleValue = (value: any) => {
  if (!value) return '-'
  
  // 解析值
  let items: any[] = []
  if (typeof value === 'string') {
    try {
      items = JSON.parse(value)
    } catch {
      return value
    }
  } else if (Array.isArray(value)) {
    items = value
  } else {
    return String(value)
  }
  
  // 如果是 [{id, name}] 格式，直接使用 name
  if (items.length > 0 && typeof items[0] === 'object' && items[0].name) {
    return items.map(item => item.name).join(', ')
  }
  
  // 兼容旧格式：纯 ID 数组
  const ids = items.map(String)
  const names = ids.map(id => {
    const role = roleMap.value.get(id)
    return role ? role.roleName : `角色${id}`
  })
  
  return names.join(', ')
}

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 页面挂载时获取数据
onMounted(() => {
  loadBusinessDataCache() // 加载业务数据缓存
  getFormList()
})
</script>

<style scoped lang="scss">
.form-data-page {
  display: flex;
  height: calc(100vh - 60px); // 减去顶部导航条高度
  background: var(--bg-secondary, #f8fafc);
  padding: 16px;
  gap: 16px;
}

// 左侧表单导航
.form-nav {
  width: 280px;
  background: var(--el-fill-color-blank);
  border-radius: 10px;
  box-shadow: var(--el-box-shadow-light);
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .nav-header {
    padding: 14px 20px;
    border-bottom: 1px solid var(--el-border-color-lighter);
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-shrink: 0;
    height: 52px;
    box-sizing: border-box;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: var(--el-text-color-primary);
      background: transparent;
    }
  }

  .nav-content {
    flex: 1;
    overflow-y: auto;
    min-height: 0;

    .loading-container,
    .empty-container {
      padding: 20px;
    }

    .form-list {
      .form-item {
        padding: 14px 18px;
        cursor: pointer;
        transition: all 0.2s ease;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-left: 3px solid transparent;

        // 斑马纹效果，与右侧表格一致
        &:nth-child(odd) {
          background: var(--el-fill-color-lighter);
        }

        &:nth-child(even) {
          background: var(--el-bg-color);
        }

        &:hover {
          background: var(--el-fill-color-light);
        }

        &.active {
          background: var(--el-color-primary-light-9);
          border-left-color: var(--el-color-primary);

          .form-name {
            color: var(--el-color-primary);
            font-weight: 500;
          }
        }

        .form-info {
          flex: 1;
          min-width: 0;

          .form-name {
            font-size: 14px;
            color: var(--el-text-color-primary);
            margin-bottom: 4px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .form-desc {
            font-size: 12px;
            color: var(--el-text-color-secondary);
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }

        .form-status {
          margin-left: 12px;
          flex-shrink: 0;
        }
      }
    }
  }
}

// 右侧数据内容
.data-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  min-width: 0;

  .no-selection {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    background: var(--el-bg-color);
    border-radius: 10px;
    box-shadow: var(--el-box-shadow-light);
  }

  .data-panel {
    flex: 1;
    display: flex;
    flex-direction: column;
    background: var(--el-bg-color);
    border-radius: 10px;
    box-shadow: var(--el-box-shadow-light);
    overflow: hidden;
    min-height: 0;

    .data-header {
      padding: 14px 20px;
      border-bottom: 1px solid var(--el-border-color-lighter);
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-shrink: 0;
      height: 52px;
      box-sizing: border-box;

      .header-left {
        display: flex;
        align-items: center;
        gap: 12px;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 600;
          color: var(--el-text-color-primary);
          background: transparent;
        }
      }

      .header-right {
        display: flex;
        gap: 10px;
      }
    }

    .data-table {
      flex: 1;
      display: flex;
      flex-direction: column;
      padding: 16px;
      overflow: hidden;
      min-height: 0;

      .table-wrapper {
        flex: 1;
        overflow: auto;
        min-height: 0;
        
        :deep(.el-table) {
          height: 100% !important;
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

      .pagination-wrapper {
        flex-shrink: 0;
        display: flex;
        justify-content: center;
        padding: 14px 0 0;
        border-top: 1px solid var(--el-border-color-lighter);
        margin-top: 14px;
      }
    }
  }
}

// 新增/编辑抽屉样式
.add-form-container,
.edit-form-container {
  padding: 20px 0;
  
  .loading-container,
  .no-form,
  .no-config {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 300px;
  }
  
  .form-content {
    :deep(.el-form) {
      max-width: none;
    }
  }
}

.drawer-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 0;
  border-top: 1px solid var(--el-border-color-lighter);
  margin-top: 20px;
}
</style>