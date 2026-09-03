<template>
  <div class="cert-container">
    <!-- 标签页 -->
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="证书管理" name="manage">
        <!-- 证书生成区域 -->
    <el-card class="cert-create-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon><DocumentAdd /></el-icon> 证书生成</span>
          <el-button type="primary" @click="handleGenerate" :loading="generating">
            <el-icon class="el-icon--left"><Plus /></el-icon>
            生成证书
          </el-button>
        </div>
      </template>

      <el-form :model="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证书算法">
              <el-radio-group v-model="form.certType">
                <el-radio-button value="SM2">SM2</el-radio-button>
                <el-radio-button value="RSA">RSA2048</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效期/天">
              <el-input-number v-model="form.certValidTime" :min="1" :max="3650" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="证书密码">
              <el-input v-model="form.certPwd" type="password" show-password placeholder="请输入证书密码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公用名/CN">
              <el-input v-model="form.certCn" placeholder="请输入公用名" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="部门/OU">
              <el-input v-model="form.certOu" placeholder="请输入部门" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="公用信息/O">
              <el-input v-model="form.certO" placeholder="统一社会信用代码/身份证" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="国家/C">
              <el-input v-model="form.certC" placeholder="国家代码" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="省份/ST">
              <el-input v-model="form.certSt" placeholder="省份" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="城市/L">
              <el-input v-model="form.certL" placeholder="城市" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 证书列表区域 -->
    <el-card class="cert-list-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span><el-icon><List /></el-icon> 我的证书</span>
          <div class="header-actions">
            <el-input
              v-model="searchForm.certCn"
              placeholder="请输入CN项搜索"
              clearable
              style="width: 200px; margin-right: 10px"
              @clear="loadData"
            >
              <template #append>
                <el-button icon="Search" @click="handleSearch" />
              </template>
            </el-input>
            <el-select
              v-model="searchForm.certType"
              placeholder="证书类型"
              clearable
              style="width: 120px; margin-right: 10px"
              @change="handleSearch"
            >
              <el-option label="SM2" value="SM2" />
              <el-option label="RSA" value="RSA" />
            </el-select>
            <el-button
              type="danger"
              :disabled="!selectedIds.length"
              @click="handleBatchDelete"
            >
              <el-icon class="el-icon--left"><Delete /></el-icon>
              批量删除
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="certCn" label="CN项" min-width="150" />
        <el-table-column prop="certType" label="证书类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.certType === 'SM2' ? 'success' : 'primary'">
              {{ row.certType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="certO" label="O项" min-width="150" />
        <el-table-column prop="certOu" label="OU项" width="120" />
        <el-table-column prop="certC" label="C项" width="80" />
        <el-table-column prop="certSt" label="ST项" width="100" />
        <el-table-column prop="certL" label="L项" width="100" />
        <el-table-column prop="certValidTime" label="有效期/天" width="100" />
        <el-table-column prop="certNotBefore" label="生成时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.certNotBefore) }}
          </template>
        </el-table-column>
        <el-table-column prop="certNotAfter" label="失效时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.certNotAfter) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleDownload(row)"
              link
            >
              <el-icon class="el-icon--left"><Download /></el-icon>
              下载
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(row)"
              link
            >
              <el-icon class="el-icon--left"><Delete /></el-icon>
              删除
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
      </el-tab-pane>

      <el-tab-pane label="签名验签" name="sign">
        <CertSign />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  DocumentAdd,
  Plus,
  List,
  Search,
  Delete,
  Download
} from '@element-plus/icons-vue'
import { certApi } from '@/api'
import CertSign from './sign.vue'

// 证书生成表单
const form = reactive({
  certType: 'SM2',
  certC: 'CN',
  certCn: 'ZHONLZ',
  certOu: '知录API',
  certO: '4306XXXXXXXX1313',
  certSt: '湖南省',
  certL: '长沙市',
  certValidTime: 365,
  certPwd: '123456'
})

// 搜索表单
const searchForm = reactive({
  certCn: '',
  certType: ''
})

// 标签页
const activeTab = ref('manage')

// 表格数据
const tableData = ref([])
const loading = ref(false)
const generating = ref(false)
const selectedIds = ref([])

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 生成证书
const handleGenerate = async () => {
  if (!form.certCn) {
    ElMessage.warning('请输入公用名/CN')
    return
  }
  if (!form.certPwd) {
    ElMessage.warning('请输入证书密码')
    return
  }

  generating.value = true
  try {
    const res = await certApi.createCert(form)
    if (res.code === 200) {
      ElMessage.success('证书生成成功')
      // 重置部分表单
      form.certCn = ''
      // 刷新列表
      loadData()
    } else {
      ElMessage.error(res.message || '证书生成失败')
    }
  } catch (error) {
    console.error('生成证书失败:', error)
    ElMessage.error('证书生成失败')
  } finally {
    generating.value = false
  }
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size
    }

    // 只有当值存在时才添加查询参数
    if (searchForm.certCn && searchForm.certCn.trim()) {
      params.certCn = searchForm.certCn
    }
    if (searchForm.certType && searchForm.certType.trim()) {
      params.certType = searchForm.certType
    }

    const res = await certApi.pageUserCerts(params)
    if (res.code === 200) {
      tableData.value = res.data.records || []
      pagination.total = Number(res.data.total) || 0
    } else {
      ElMessage.error(res.message || '加载数据失败')
    }
  } catch (error) {
    console.error('加载证书数据失败:', error)
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

// 选择变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 下载证书
const handleDownload = (row) => {
  if (row.certUrl) {
    const downloadUrl = certApi.downloadCert(row.certUrl)
    window.open(downloadUrl, '_blank')
    ElMessage.success('开始下载证书')
  } else {
    ElMessage.error('证书文件不存在')
  }
}

// 删除证书
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除证书 "${row.certCn}" 吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await certApi.deleteCert(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除证书失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  if (selectedIds.value.length === 0) {
    ElMessage.warning('请选择要删除的证书')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要删除选中的 ${selectedIds.value.length} 个证书吗？`,
      '批量删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await certApi.batchDelete(selectedIds.value)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      selectedIds.value = []
      loadData()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除证书失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  loadData()
}

// 当前页变化
const handleCurrentChange = (current) => {
  pagination.current = current
  loadData()
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '--'
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 标签页切换
const handleTabChange = (tabName) => {
  if (tabName === 'manage') {
    loadData()
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.cert-container {
  padding: 20px;
  background: var(--bg-secondary, #f8fafc);
  min-height: calc(100vh - 80px);
}

.cert-create-card,
.cert-list-card {
  margin-bottom: 20px;

  :deep(.el-card__header) {
    padding: 16px 20px;
    border-bottom: 1px solid #ebeef5;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  font-size: 16px;
  color: #303133;

  span {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .header-actions {
    display: flex;
    align-items: center;
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

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-table) {
  .el-button + .el-button {
    margin-left: 8px;
  }

  border: none;
  background: transparent;

  .el-table__header {
    background: transparent;

    .el-table__cell {
      background: transparent;
      border: none;
      color: var(--text-secondary);
      font-weight: 500;
      padding: 16px 0;
    }
  }

  .el-table__row {
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

  .el-table__inner-wrapper {
    border: none;
  }

  .el-table__border-line {
    display: none;
  }
}
</style>

