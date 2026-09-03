<template>
  <div class="proxy-rule-container">
    <!-- 操作栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">
        新增规则
      </el-button>

      <el-button
          type="danger"
          :disabled="ids.length === 0"
          @click="handleBatchDelete"
      >
        批量删除
      </el-button>

      <el-button @click="loadData">
        刷新
      </el-button>
    </div>

    <!-- 查询区 -->
    <el-card class="query-card">
      <el-form
          :model="queryParams"
          inline
          ref="queryRef"
      >

        <el-form-item label="协议类型">
          <el-select
              v-model="queryParams.protocolType"
              clearable
              style="width: 200px"
          >
            <el-option
                v-for="item in proxy_auth_type"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
          <el-select
              v-model="queryParams.status"
              clearable
              style="width: 150px"
          >
            <el-option label="启用" value="1"/>
            <el-option label="禁用" value="0"/>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="loadData">
            查询
          </el-button>

          <el-button @click="resetQuery">
            重置
          </el-button>
        </el-form-item>

      </el-form>


    </el-card>

    <!-- 表格 -->
    <div class="table-card">

      <el-table
          :data="tableData"
          v-loading="loading"
          @selection-change="handleSelectionChange"
          border
      >

        <el-table-column
            type="selection"
            width="55"
            align="center"
        />
        <el-table-column prop="alias" label="别名"></el-table-column>
        <el-table-column
            prop="protocolType"
            label="协议类型"
        >
          <template #default="{ row }">
            <el-tag>
              {{ formatProtocol(row.protocolType) }}
            </el-tag>
          </template>
        </el-table-column>

        <!--        <el-table-column-->
        <!--            prop="proxyRuleId"-->
        <!--            label="关联资源"-->
        <!--            min-width="180"-->
        <!--            show-overflow-tooltip-->
        <!--        />-->

        <el-table-column
            prop="status"
            label="状态"
        >
          <template #default="{ row }">
            <el-tag
                :type="row.status === '1' ? 'success' : 'danger'"
            >
              {{ row.status === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column
            prop="createdDate"
            label="创建时间"
        />

        <el-table-column
            label="操作"
            width="120"
            fixed="right"
            align="center"
        >
          <template #default="{ row }">
            <el-tooltip content="编辑">
              <el-button type="primary" link icon="Edit" @click="handleEdit(row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除">
              <el-button type="danger" icon="Delete" link @click="handleDelete(row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>

      </el-table>

      <!-- 分页 -->
      <div class="pagination">

        <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNumber"
            v-model:limit="queryParams.pageSize"
            @pagination="loadData"
        />

      </div>

    </div>

    <!-- 新增/编辑弹窗 -->
    <el-drawer
        :title="isEdit ? '编辑规则' : '新增规则'"
        v-model="dialogVisible"
        size="600px"
    >
      <template #header>
        <h4>{{ isEdit ? '编辑规则' : '新增规则' }}</h4>
      </template>
      <el-form
          :model="formData"
          label-width="100px"
          ref="formRef"
      >

        <el-form-item label="协议类型" required>
          <el-select
              v-model="formData.protocolType"
              style="width: 100%"
          >
            <el-option
                v-for="item in proxy_auth_type"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="别名">
          <el-input
              v-model="formData.alias"
              placeholder="请输入"
              autocomplete="off"
          />
        </el-form-item>

        <el-form-item label="配置" required>
          <ProxyConfig
              v-model:config="formData.config"
              :protocol-type="formData.protocolType"
          />
        </el-form-item>

        <el-form-item label="状态">
          <el-switch
              v-model="formData.status"
              active-value="1"
              inactive-value="0"
          />
        </el-form-item>

      </el-form>

      <template #footer>
        <div style="flex: auto">
          <el-button @click="dialogVisible = false">
            取消
          </el-button>
          <el-button
              type="primary"
              :loading="submitting"
              @click="submitForm"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup>
import {ref, reactive, watch, computed} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import * as proxyRuleApi from '@/api/app/app-proxy-rules.ts'
import ProxyConfig from '@/components/ProxyConfig/index.vue'
import * as proxy from "@/utils/Dict.ts";

/* Props */
const props = defineProps({
  appId: {
    type: String,
    required: true
  }
})
const {proxy_auth_type} = proxy.useDict("proxy_auth_type");

/* 状态 */
const loading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)

/* 表格 */
const tableData = ref([])
const total = ref(0)
const ids = ref([])

/* 查询 */
const queryParams = reactive({
  appId: '',
  alias: '',
  protocolType: '',
  status: '',
  pageNumber: 1,
  pageSize: 10
})

/* 表单 */
const formData = reactive({
  id: null,
  appId: '',
  alias: '',
  proxyRuleId: '',
  protocolType: '',
  config: '',
  status: '1'
})

function formatProtocol(val) {
  const item = proxy_auth_type.value.find(i => i.value === val)
  return (item && item.label) || val
}

/* 加载数据 */
async function loadData() {
  loading.value = true
  try {
    const res = await proxyRuleApi.pageProxyRules(queryParams)

    if (res.code === 0) {
      tableData.value = res.data.rows
      total.value = res.data.records
    }

  } finally {
    loading.value = false
  }
}

/* 新增 */
function handleAdd() {
  isEdit.value = false

  Object.assign(formData, {
    id: null,
    appId: props.appId,
    alias: '',
    proxyRuleId: '',
    protocolType: 'api_key',
    config: '',
    status: '1'
  })

  dialogVisible.value = true
}

/* 编辑 */
function handleEdit(row) {
  isEdit.value = true

  Object.assign(formData, {
    id: row.id,
    appId: row.appId,
    alias: row.alias,
    proxyRuleId: row.proxyRuleId,
    protocolType: row.protocolType,
    config: row.config || '',
    status: row.status || '1'
  })

  dialogVisible.value = true
}

/* 删除 */
async function handleDelete(row) {
  await ElMessageBox.confirm(
      `确认删除该规则？`,
      '提示',
      {type: 'warning'}
  )

  await proxyRuleApi.deleteData(row.id)

  ElMessage.success('删除成功')

  loadData()
}

/* 批量删除 */
async function handleBatchDelete() {
  await ElMessageBox.confirm(
      '确认批量删除？',
      '提示',
      {type: 'warning'}
  )

  await proxyRuleApi.deleteData(ids.value.join(','))

  ElMessage.success('删除成功')

  loadData()
}

/* 提交 */
async function submitForm() {
  submitting.value = true
  try {
    // 验证必填字段
    if (!formData.protocolType) {
      ElMessage.error('请选择协议类型')
      return
    }

    // 尝试解析配置JSON
    let configObj = {}
    try {
      configObj = JSON.parse(formData.config || '{}')
    } catch (e) {
      ElMessage.error('配置必须是合法JSON')
      return
    }

    // 根据协议类型验证必填字段
    const validationError = validateConfig(formData.protocolType, configObj)
    if (validationError) {
      ElMessage.error(validationError)
      return
    }

    let res

    if (isEdit.value) {
      res = await proxyRuleApi.update(formData)
    } else {
      res = await proxyRuleApi.create(formData)
    }

    if (res.code === 0) {
      ElMessage.success('保存成功')

      dialogVisible.value = false
      loadData()
    }

  } catch (e) {
    console.error('保存失败:', e)
    ElMessage.error('保存失败：' + (e.message || '未知错误'))
  } finally {
    submitting.value = false
  }
}

/* 配置验证 */
function validateConfig(protocolType, config) {
  // 验证目标 URL
  if (!config.targetUrl) {
    return '目标 URL 不能为空'
  }

  switch (protocolType) {
    case 'api_key':
      if (!config.apiKey) return 'API Key 不能为空'
      break
    case 'oauth2':
      if (!config.clientId) return '客户端 ID 不能为空'
      if (!config.clientSecret) return '客户端密钥不能为空'
      if (!config.tokenUrl) return '令牌端点不能为空'
      break
    case 'jwt':
      if (!config.token && !config.secret) return 'JWT 令牌或密钥不能为空'
      break
    case 'basic_auth':
      if (!config.username) return '用户名不能为空'
      if (!config.password) return '密码不能为空'
      break
    case 'bearer_token':
      if (!config.token) return 'Bearer 令牌不能为空'
      break
    case 'digest_auth':
      if (!config.username) return '用户名不能为空'
      if (!config.password) return '密码不能为空'
      break
    case 'custom':
      if (!formData.config || formData.config.trim() === '') return '自定义配置不能为空'
      break
  }
  return null
}

/* 选择 */
function handleSelectionChange(list) {
  ids.value = list.map(i => i.id)
}

/* 重置 */
function resetQuery() {
  queryParams.protocolType = ''
  queryParams.status = ''

  loadData()
}

/* 监听 appId */
watch(
    () => props.appId,
    val => {
      if (val) {
        queryParams.appId = val
        formData.appId = val
        loadData()
      }
    },
    {immediate: true}
)
</script>

<style scoped lang="scss">

.proxy-rule-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.toolbar {
  display: flex;
  gap: 12px;
}

.query-card {
  padding: 10px;
}

.table-card {
  flex: 1;
}

.pagination {
  margin-top: 16px;
  text-align: right;
}

</style>
