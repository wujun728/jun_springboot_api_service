
<template>
  <div class="dict-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">字典管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAddType">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          添加字典
        </el-button>
        <el-button @click="handleRefresh">
          <el-icon class="el-icon--left"><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <div class="dict-container">
      <!-- 左侧：字典类型列表 -->
      <div class="dict-type-panel">
        <div class="panel-header">
          <span class="panel-title">字典类型</span>
        </div>
        
        <!-- 字典类型搜索 -->
        <div class="type-search">
          <el-input
            v-model="typeSearchQuery"
            placeholder="搜索字典类型"
            prefix-icon="Search"
            clearable
          />
        </div>
        
        <!-- 字典类型列表 -->
        <div class="type-list" v-loading="typeLoading">
          <div
            v-for="item in filteredDictTypes"
            :key="item.dictId"
            class="type-item"
            :class="{ active: currentDictType && currentDictType.dictId === item.dictId }"
            @click="handleSelectType(item)"
          >
            <div class="type-info">
              <div class="type-name">{{ item.dictName }}</div>
              <div class="type-code">{{ item.dictType }}</div>
            </div>
            <div class="type-actions">
              <el-button size="small" @click.stop="handleEditType(item)">编辑</el-button>
              <el-button size="small" type="danger" @click.stop="handleDeleteType(item)">删除</el-button>
            </div>
          </div>
          <el-empty v-if="filteredDictTypes.length === 0" description="暂无字典类型" :image-size="60" />
        </div>
      </div>

      <!-- 右侧：字典数据列表 -->
      <div class="dict-data-panel">
        <div class="panel-header">
          <span class="panel-title">
            字典数据
            <el-tag v-if="currentDictType" type="info" size="small" class="dict-type-tag">
              {{ currentDictType.dictName }}
            </el-tag>
          </span>
          <el-button type="primary" @click="handleAddData" :disabled="!currentDictType">
            <el-icon class="el-icon--left"><Plus /></el-icon>
            添加数据
          </el-button>
        </div>

        <!-- 字典数据搜索 -->
        <div class="data-search">
          <el-input
            v-model="dataSearchQuery.dictLabel"
            placeholder="搜索字典标签..."
            prefix-icon="Search"
            style="width: 200px"
            clearable
            @keyup.enter="loadDictDataList"
          />
          <el-select v-model="dataSearchQuery.status" placeholder="状态筛选" style="width: 120px" clearable>
            <el-option label="全部" value="" />
            <el-option label="正常" value="0" />
            <el-option label="停用" value="1" />
          </el-select>
          <el-button type="primary" @click="loadDictDataList">
            <el-icon class="el-icon--left"><Search /></el-icon>
            搜索
          </el-button>
        </div>

        <!-- 字典数据表格 -->
        <div class="data-table" v-loading="dataLoading">
          <el-table :data="dictDataList" style="width: 100%" class="dict-table" v-if="currentDictType">
            <el-table-column prop="dictCode" label="字典编码" width="100" />
            <el-table-column prop="dictLabel" label="字典标签" min-width="120" show-overflow-tooltip />
            <el-table-column prop="dictValue" label="字典键值" min-width="100" show-overflow-tooltip />
            <el-table-column prop="dictSort" label="排序" width="70" />
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.status === '0' ? 'success' : 'danger'" size="small">
                  {{ row.status === '0' ? '正常' : '停用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="listClass" label="回显样式" width="100">
              <template #default="{ row }">
                <el-tag v-if="row.listClass" :type="row.listClass" size="small">{{ row.listClass }}</el-tag>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="handleEditData(row)">编辑</el-button>
                <el-button size="small" type="danger" @click="handleDeleteData(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="请先选择左侧字典类型" :image-size="100" />
        </div>

        <!-- 分页 -->
        <div class="data-pagination" v-if="currentDictType">
          <el-pagination
            background
            small
            v-model:current-page="currentPage"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50]"
            :total="Number(total)"
            layout="total, sizes, prev, pager, next"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>

    <!-- 字典类型对话框 -->
    <el-dialog
      v-model="typeDialogVisible"
      :title="typeDialogTitle"
      width="500px"
      :close-on-click-modal="false"
      @close="handleTypeDialogClose"
    >
      <el-form
        ref="dictTypeFormRef"
        :model="dictTypeForm"
        :rules="dictTypeFormRules"
        label-width="90px"
      >
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="dictTypeForm.dictName" placeholder="请输入字典名称" clearable />
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="dictTypeForm.dictType" placeholder="请输入字典类型，如：sys_user_sex" clearable />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dictTypeForm.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dictTypeForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTypeSubmit" :loading="typeSubmitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 字典数据对话框 -->
    <el-dialog
      v-model="dataDialogVisible"
      :title="dataDialogTitle"
      width="500px"
      :close-on-click-modal="false"
      @close="handleDataDialogClose"
    >
      <el-form
        ref="dictDataFormRef"
        :model="dictDataForm"
        :rules="dictDataFormRules"
        label-width="90px"
      >
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="dictDataForm.dictType" disabled />
        </el-form-item>
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input v-model="dictDataForm.dictLabel" placeholder="请输入字典标签" clearable />
        </el-form-item>
        <el-form-item label="字典键值" prop="dictValue">
          <el-input v-model="dictDataForm.dictValue" placeholder="请输入字典键值" clearable />
        </el-form-item>
        <el-form-item label="显示排序" prop="dictSort">
          <el-input-number v-model="dictDataForm.dictSort" :min="0" :max="9999" controls-position="right" style="width: 100%" />
        </el-form-item>
        <el-form-item label="回显样式" prop="listClass">
          <el-select v-model="dictDataForm.listClass" placeholder="请选择回显样式" style="width: 100%" clearable>
            <el-option label="默认" value="" />
            <el-option label="主要(primary)" value="primary" />
            <el-option label="成功(success)" value="success" />
            <el-option label="信息(info)" value="info" />
            <el-option label="警告(warning)" value="warning" />
            <el-option label="危险(danger)" value="danger" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否默认" prop="isDefault">
          <el-radio-group v-model="dictDataForm.isDefault">
            <el-radio label="Y">是</el-radio>
            <el-radio label="N">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="dictDataForm.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dictDataForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dataDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleDataSubmit" :loading="dataSubmitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Search, Edit, Delete } from '@element-plus/icons-vue'
import { dictTypeApi, dictDataApi } from '@/api'

// ==================== 字典类型相关 ====================
const typeLoading = ref(false)
const dictTypeList = ref([])
const currentDictType = ref(null)
const typeSearchQuery = ref('')

// 字典类型对话框
const typeDialogVisible = ref(false)
const typeDialogTitle = ref('')
const typeDialogMode = ref('add')
const typeSubmitLoading = ref(false)
const dictTypeFormRef = ref(null)
const dictTypeForm = ref({
  dictId: null,
  dictName: '',
  dictType: '',
  status: '0',
  remark: ''
})

const dictTypeFormRules = {
  dictName: [
    { required: true, message: '请输入字典名称', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  dictType: [
    { required: true, message: '请输入字典类型', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' },
    { pattern: /^[a-z][a-z0-9_]*$/, message: '字典类型必须以小写字母开头，只能包含小写字母、数字和下划线', trigger: 'blur' }
  ]
}

// 过滤后的字典类型列表
const filteredDictTypes = computed(() => {
  if (!typeSearchQuery.value) return dictTypeList.value
  const query = typeSearchQuery.value.toLowerCase()
  return dictTypeList.value.filter(item =>
    item.dictName.toLowerCase().includes(query) ||
    item.dictType.toLowerCase().includes(query)
  )
})

// 加载字典类型列表
const loadDictTypeList = async () => {
  typeLoading.value = true
  try {
    const response = await dictTypeApi.getAllDictTypes()
    if (response.code === 200) {
      dictTypeList.value = response.data || []
      // 如果有数据且没有选中项，默认选中第一个
      if (dictTypeList.value.length > 0 && !currentDictType.value) {
        handleSelectType(dictTypeList.value[0])
      }
    }
  } catch (error) {
    ElMessage.error('加载字典类型列表失败')
  } finally {
    typeLoading.value = false
  }
}

// 选择字典类型
const handleSelectType = (item) => {
  currentDictType.value = item
  currentPage.value = 1
  loadDictDataList()
}

// 添加字典类型
const handleAddType = () => {
  typeDialogMode.value = 'add'
  typeDialogTitle.value = '添加字典类型'
  resetTypeForm()
  typeDialogVisible.value = true
}

// 编辑字典类型
const handleEditType = async (row) => {
  typeDialogMode.value = 'edit'
  typeDialogTitle.value = '编辑字典类型'
  try {
    const response = await dictTypeApi.getDictType(row.dictId)
    if (response.code === 200) {
      dictTypeForm.value = {
        dictId: response.data.dictId,
        dictName: response.data.dictName,
        dictType: response.data.dictType,
        status: response.data.status || '0',
        remark: response.data.remark || ''
      }
      typeDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取字典类型信息失败')
  }
}

// 删除字典类型
const handleDeleteType = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除字典类型"${row.dictName}"吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await dictTypeApi.deleteDictType(row.dictId)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      // 如果删除的是当前选中的类型，清空选中
      if (currentDictType.value && currentDictType.value.dictId === row.dictId) {
        currentDictType.value = null
        dictDataList.value = []
      }
      loadDictTypeList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交字典类型表单
const handleTypeSubmit = async () => {
  if (!dictTypeFormRef.value) return
  await dictTypeFormRef.value.validate(async (valid) => {
    if (valid) {
      typeSubmitLoading.value = true
      try {
        let response
        if (typeDialogMode.value === 'add') {
          response = await dictTypeApi.addDictType(dictTypeForm.value)
        } else {
          response = await dictTypeApi.updateDictType(dictTypeForm.value)
        }
        if (response.code === 200) {
          ElMessage.success(typeDialogMode.value === 'add' ? '添加成功' : '更新成功')
          typeDialogVisible.value = false
          loadDictTypeList()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error) {
        ElMessage.error('操作失败')
      } finally {
        typeSubmitLoading.value = false
      }
    }
  })
}

const handleTypeDialogClose = () => {
  resetTypeForm()
}

const resetTypeForm = () => {
  dictTypeForm.value = {
    dictId: null,
    dictName: '',
    dictType: '',
    status: '0',
    remark: ''
  }
  if (dictTypeFormRef.value) {
    dictTypeFormRef.value.clearValidate()
  }
}

// ==================== 字典数据相关 ====================
const dataLoading = ref(false)
const dictDataList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const dataSearchQuery = reactive({
  dictLabel: '',
  status: ''
})

// 字典数据对话框
const dataDialogVisible = ref(false)
const dataDialogTitle = ref('')
const dataDialogMode = ref('add')
const dataSubmitLoading = ref(false)
const dictDataFormRef = ref(null)
const dictDataForm = ref({
  dictCode: null,
  dictType: '',
  dictLabel: '',
  dictValue: '',
  dictSort: 0,
  cssClass: '',
  listClass: '',
  isDefault: 'N',
  status: '0',
  remark: ''
})

const dictDataFormRules = {
  dictLabel: [
    { required: true, message: '请输入字典标签', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  dictValue: [
    { required: true, message: '请输入字典键值', trigger: 'blur' },
    { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
  ],
  dictSort: [
    { required: true, message: '请输入显示排序', trigger: 'blur' }
  ]
}

// 加载字典数据列表
const loadDictDataList = async () => {
  if (!currentDictType.value) return
  dataLoading.value = true
  try {
    const response = await dictDataApi.getDictDataList({
      current: currentPage.value,
      size: pageSize.value,
      dictType: currentDictType.value.dictType,
      dictLabel: dataSearchQuery.dictLabel,
      status: dataSearchQuery.status
    })
    if (response.code === 200) {
      dictDataList.value = response.data.records || []
      total.value = Number(response.data.total) || 0
    }
  } catch (error) {
    ElMessage.error('加载字典数据列表失败')
  } finally {
    dataLoading.value = false
  }
}

// 添加字典数据
const handleAddData = () => {
  if (!currentDictType.value) {
    ElMessage.warning('请先选择字典类型')
    return
  }
  dataDialogMode.value = 'add'
  dataDialogTitle.value = '添加字典数据'
  resetDataForm()
  dictDataForm.value.dictType = currentDictType.value.dictType
  dataDialogVisible.value = true
}

// 编辑字典数据
const handleEditData = async (row) => {
  dataDialogMode.value = 'edit'
  dataDialogTitle.value = '编辑字典数据'
  try {
    const response = await dictDataApi.getDictData(row.dictCode)
    if (response.code === 200) {
      dictDataForm.value = {
        dictCode: response.data.dictCode,
        dictType: response.data.dictType,
        dictLabel: response.data.dictLabel,
        dictValue: response.data.dictValue,
        dictSort: response.data.dictSort || 0,
        cssClass: response.data.cssClass || '',
        listClass: response.data.listClass || '',
        isDefault: response.data.isDefault || 'N',
        status: response.data.status || '0',
        remark: response.data.remark || ''
      }
      dataDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取字典数据信息失败')
  }
}

// 删除字典数据
const handleDeleteData = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除字典数据"${row.dictLabel}"吗？`, '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    const response = await dictDataApi.deleteDictData(row.dictCode)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadDictDataList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交字典数据表单
const handleDataSubmit = async () => {
  if (!dictDataFormRef.value) return
  await dictDataFormRef.value.validate(async (valid) => {
    if (valid) {
      dataSubmitLoading.value = true
      try {
        let response
        if (dataDialogMode.value === 'add') {
          response = await dictDataApi.addDictData(dictDataForm.value)
        } else {
          response = await dictDataApi.updateDictData(dictDataForm.value)
        }
        if (response.code === 200) {
          ElMessage.success(dataDialogMode.value === 'add' ? '添加成功' : '更新成功')
          dataDialogVisible.value = false
          loadDictDataList()
        } else {
          ElMessage.error(response.message || '操作失败')
        }
      } catch (error) {
        ElMessage.error('操作失败')
      } finally {
        dataSubmitLoading.value = false
      }
    }
  })
}

const handleDataDialogClose = () => {
  resetDataForm()
}

const resetDataForm = () => {
  dictDataForm.value = {
    dictCode: null,
    dictType: '',
    dictLabel: '',
    dictValue: '',
    dictSort: 0,
    cssClass: '',
    listClass: '',
    isDefault: 'N',
    status: '0',
    remark: ''
  }
  if (dictDataFormRef.value) {
    dictDataFormRef.value.clearValidate()
  }
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadDictDataList()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadDictDataList()
}

// 刷新
const handleRefresh = () => {
  loadDictTypeList()
  if (currentDictType.value) {
    loadDictDataList()
  }
}

onMounted(() => {
  loadDictTypeList()
})
</script>

<style lang="scss" scoped>
.dict-page {
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

.dict-container {
  display: flex;
  gap: 20px;
  height: calc(100vh - 180px);
}

// 左侧字典类型面板
.dict-type-panel {
  width: 320px;
  flex-shrink: 0;
  background: var(--bg-card, #fff);
  border: 1px solid var(--border-color, #e4e7ed);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

// 右侧字典数据面板
.dict-data-panel {
  flex: 1;
  background: var(--bg-card, #fff);
  border: 1px solid var(--border-color, #e4e7ed);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-color, #e4e7ed);
  background: var(--bg-tertiary, #f5f7fa);
  
  .panel-title {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .dict-type-tag {
    font-weight: normal;
  }
}

.type-search {
  padding: 16px;
  border-bottom: 1px solid var(--border-color, #e4e7ed);
}

.type-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.type-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s ease;
  background: var(--bg-card, #fff);
  border-left: 3px solid transparent;
  
  &:hover {
    background: var(--bg-tertiary, #f5f7fa);
    
    .type-actions {
      opacity: 1;
    }
  }
  
  &.active {
    background: var(--bg-card, #fff);
    border-left: 3px solid var(--primary-color, #409eff);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    
    .type-name {
      color: var(--primary-color, #409eff);
    }
  }
  
  .type-info {
    flex: 1;
    min-width: 0;
    
    .type-name {
      font-size: 14px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 4px;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
    
    .type-code {
      font-size: 12px;
      color: var(--text-placeholder, #a8abb2);
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
  
  .type-actions {
    display: flex;
    gap: 4px;
    opacity: 0;
    transition: opacity 0.2s ease;
  }
}

.data-search {
  display: flex;
  gap: 12px;
  padding: 12px 20px;
  border-bottom: 1px solid var(--border-color, #e4e7ed);
  flex-wrap: wrap;
}

.data-table {
  flex: 1;
  padding: 16px 20px;
  overflow: auto;
}

.dict-table {
  :deep(.el-table__header) {
    .el-table__cell {
      background: var(--bg-tertiary, #f5f7fa);
      font-weight: 500;
    }
  }
}

.data-pagination {
  padding: 12px 20px;
  border-top: 1px solid var(--border-color, #e4e7ed);
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
@media (max-width: 1024px) {
  .dict-container {
    flex-direction: column;
    height: auto;
  }
  
  .dict-type-panel {
    width: 100%;
    max-height: 300px;
  }
  
  .dict-data-panel {
    min-height: 400px;
  }
}

// 空状态样式
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
  color: var(--text-secondary);
  
  .el-icon {
    font-size: 48px;
    margin-bottom: 16px;
    color: var(--text-placeholder);
  }
  
  p {
    font-size: 14px;
    margin: 0;
  }
}

// 加载状态
.loading-state {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}
</style>