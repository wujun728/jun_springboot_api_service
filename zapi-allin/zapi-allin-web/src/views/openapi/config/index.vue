<template>
  <div class="config-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">接口配置</h1>
        <p class="page-desc">配置对外开放的数据同步接口，支持被动和主动两种调用方式</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          添加配置
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
        <el-select v-model="searchParams.appId" placeholder="选择应用" style="width: 180px" clearable @change="handleSearch">
          <el-option v-for="app in applicationList" :key="app.id" :label="app.appName" :value="app.id" />
        </el-select>
        <el-input v-model="searchParams.configName" placeholder="配置名称..." style="width: 200px" clearable @input="handleSearch" />
        <el-select v-model="searchParams.callType" placeholder="调用类型" style="width: 130px" clearable @change="handleSearch">
          <el-option label="被动" :value="1" />
          <el-option label="主动" :value="2" />
        </el-select>
        <el-select v-model="searchParams.dataType" placeholder="数据类型" style="width: 130px" clearable @change="handleSearch">
          <el-option label="用户" value="user" />
          <el-option label="角色" value="role" />
          <el-option label="部门" value="dept" />
        </el-select>
        <el-select v-model="searchParams.status" placeholder="状态" style="width: 100px" clearable @change="handleSearch">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </div>
    </div>

    <!-- 配置列表 -->
    <div class="config-content">
      <el-table :data="configList" style="width: 100%" v-loading="loading">
        <el-table-column prop="configName" label="配置名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="appName" label="所属应用" min-width="120" show-overflow-tooltip />
        <el-table-column prop="callType" label="调用类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.callType === 1 ? 'success' : 'warning'" size="small">
              {{ row.callType === 1 ? '被动' : '主动' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="dataType" label="数据类型" width="100">
          <template #default="{ row }">
            <span>{{ dataTypeMap[row.dataType] || row.dataType }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="syncMode" label="同步方式" width="100">
          <template #default="{ row }">
            <el-tag :type="row.syncMode === 1 ? 'primary' : 'info'" size="small">
              {{ row.syncMode === 1 ? '全量' : '增量' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch :model-value="row.status === 1" @change="handleStatusChange(row, $event)" inline-prompt active-text="启" inactive-text="停" />
          </template>
        </el-table-column>
        <el-table-column prop="lastSyncTime" label="上次同步" min-width="160" show-overflow-tooltip />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <!-- 启用状态：显示文档和测试 -->
            <template v-if="row.status === 1">
              <el-button size="small" type="info" @click="handleShowDoc(row)">文档</el-button>
              <el-button size="small" type="primary" @click="handleTest(row)">测试</el-button>
            </template>
            <!-- 禁用状态：显示编辑和删除 -->
            <template v-else>
              <el-button size="small" @click="handleEdit(row)">编辑</el-button>
              <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-section">
      <el-pagination
        background
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50]"
        :total="Number(total)"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>


    <!-- 添加/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px" :close-on-click-modal="false" @close="handleDialogClose">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="90px" class="config-form">
        <!-- 基础信息 -->
        <div class="form-section">
          <div class="section-title">基础信息</div>
          <el-form-item label="配置名称" prop="configName">
            <el-input v-model="formData.configName" placeholder="请输入配置名称" maxlength="100" />
          </el-form-item>
          <el-form-item label="所属应用" prop="appId">
            <el-select v-model="formData.appId" placeholder="请选择应用" style="width: 100%">
              <el-option v-for="app in applicationList" :key="app.id" :label="app.appName" :value="app.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="数据类型" prop="dataType">
            <el-select v-model="formData.dataType" placeholder="请选择数据类型" style="width: 100%">
              <el-option label="用户" value="user" />
              <el-option label="角色" value="role" />
              <el-option label="部门" value="dept" />
            </el-select>
          </el-form-item>
          <el-form-item label="调用类型" prop="callType">
            <el-radio-group v-model="formData.callType">
              <el-radio :label="1">被动（提供接口给外部调用）</el-radio>
              <el-radio :label="2">主动（调用外部接口获取数据）</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="同步方式" prop="syncMode">
            <el-radio-group v-model="formData.syncMode">
              <el-radio :label="1">全量同步</el-radio>
              <el-radio :label="2">增量同步</el-radio>
            </el-radio-group>
          </el-form-item>
        </div>

        <!-- 被动模式配置 -->
        <div class="form-section" v-if="formData.callType === 1">
          <div class="section-title">接口配置</div>
          <el-form-item label="接口地址">
            <div class="api-path-display">
              <span class="api-path-text">{{ apiBaseUrl }}{{ generatedApiPath }}</span>
              <el-button type="primary" link size="small" @click="copyApiPath">
                <el-icon><CopyDocument /></el-icon> 复制
              </el-button>
            </div>
            <div class="form-tip">接口路径由系统自动生成，保存后生效</div>
          </el-form-item>
          <el-form-item label="请求方法">
            <el-radio-group v-model="formData.apiMethod" disabled>
              <el-radio label="POST">POST</el-radio>
            </el-radio-group>
            <div class="form-tip">被动模式仅支持POST请求方法</div>
          </el-form-item>
        </div>

        <!-- 主动模式配置 -->
        <div class="form-section" v-if="formData.callType === 2">
          <div class="section-title">远程接口配置</div>
          <el-form-item label="远程地址" prop="remoteUrl">
            <el-input v-model="formData.remoteUrl" placeholder="https://example.com/api/users" />
          </el-form-item>
          <el-form-item label="请求方法">
            <el-radio-group v-model="formData.remoteMethod">
              <el-radio label="GET">GET</el-radio>
              <el-radio label="POST">POST</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="请求头">
            <el-input v-model="formData.requestHeaders" type="textarea" :rows="2" placeholder='{"Content-Type": "application/json", "Custom-Header": "value"}' />
            <div class="form-tip">JSON格式，系统会自动添加App-Id、App-Code、App-Secret鉴权信息</div>
          </el-form-item>
          <el-form-item label="请求参数">
            <el-input v-model="formData.requestParams" type="textarea" :rows="2" placeholder='{"pageSize": 100, "status": 1}' />
            <div class="form-tip">JSON格式，请求时携带的参数</div>
          </el-form-item>
        </div>

        <!-- 增量配置 -->
        <div class="form-section" v-if="formData.syncMode === 2">
          <div class="section-title">增量配置</div>
          <el-form-item label="增量字段">
            <el-input v-model="formData.incrementField" placeholder="update_time" />
            <div class="form-tip">用于判断数据是否更新的字段，通常为更新时间</div>
          </el-form-item>
        </div>

        <!-- 字段映射 -->
        <div class="form-section">
          <div class="section-title">
            <span>字段映射</span>
            <el-button type="primary" size="small" @click="addMappingRow">
              <el-icon class="el-icon--left"><Plus /></el-icon> 添加
            </el-button>
          </div>
          <div class="form-tip" style="margin-bottom: 12px;">配置外部接口字段与本地数据库字段的对应关系</div>
          <el-table :data="fieldMappings" border size="small" class="mapping-table" v-if="fieldMappings.length > 0">
            <el-table-column label="外部字段" min-width="160">
              <template #default="{ row }">
                <el-input v-model="row.remoteField" placeholder="外部字段名" size="small" @change="updateResponseMapping" />
              </template>
            </el-table-column>
            <el-table-column label="" width="50" align="center">
              <template #default>
                <el-icon color="#909399"><Right /></el-icon>
              </template>
            </el-table-column>
            <el-table-column label="本地字段" min-width="180">
              <template #default="{ row }">
                <el-select v-model="row.localField" placeholder="选择本地字段" size="small" style="width: 100%" @change="updateResponseMapping">
                  <el-option v-for="field in currentLocalFields" :key="field.value" :label="field.label" :value="field.value" />
                </el-select>
              </template>
            </el-table-column>
            <el-table-column label="" width="60" align="center">
              <template #default="{ $index }">
                <el-button type="danger" size="small" link @click="removeMappingRow($index)">
                  <el-icon><Delete /></el-icon>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty v-else description="暂无字段映射，点击添加按钮配置" :image-size="60" />
        </div>

        <!-- 其他设置 -->
        <div class="form-section">
          <div class="section-title">其他设置</div>
          <el-form-item label="状态">
            <el-switch v-model="formData.status" :active-value="1" :inactive-value="0" active-text="启用" inactive-text="禁用" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="备注信息（可选）" maxlength="255" show-word-limit />
          </el-form-item>
        </div>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 接口文档对话框 -->
    <el-dialog v-model="docDialogVisible" title="接口文档" width="800px" :close-on-click-modal="false">
      <div class="api-doc-container" v-if="currentDoc">
        <!-- 基本信息 -->
        <div class="doc-section">
          <h3 class="doc-title">基本信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="配置名称">{{ currentDoc.configName }}</el-descriptions-item>
            <el-descriptions-item label="所属应用">{{ currentDoc.appName }}</el-descriptions-item>
            <el-descriptions-item label="调用类型">
              <el-tag :type="currentDoc.callType === 1 ? 'success' : 'warning'" size="small">
                {{ currentDoc.callType === 1 ? '被动模式' : '主动模式' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="数据类型">{{ dataTypeMap[currentDoc.dataType] }}</el-descriptions-item>
            <el-descriptions-item label="同步方式">
              <el-tag :type="currentDoc.syncMode === 1 ? 'primary' : 'info'" size="small">
                {{ currentDoc.syncMode === 1 ? '全量同步' : '增量同步' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="currentDoc.status === 1 ? 'success' : 'danger'" size="small">
                {{ currentDoc.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 被动模式文档 -->
        <div class="doc-section" v-if="currentDoc.callType === 1">
          <h3 class="doc-title">接口信息</h3>
          <div class="code-block">
            <div class="code-header">
              <span>接口地址</span>
              <el-button type="primary" link size="small" @click="copyDocApiPath">
                <el-icon><CopyDocument /></el-icon> 复制
              </el-button>
            </div>
            <pre class="code-content">{{ apiBaseUrl }}{{ currentDoc.apiPath }}</pre>
          </div>
          
          <div class="info-item">
            <span class="info-label">请求方法：</span>
            <el-tag size="small">{{ currentDoc.apiMethod }}</el-tag>
          </div>

          <h4 class="sub-title">请求示例 (curl)</h4>
          <div class="code-block">
            <div class="code-header">
              <span>命令行请求</span>
              <el-button type="primary" link size="small" @click="copyCurlCommand">
                <el-icon><CopyDocument /></el-icon> 复制
              </el-button>
            </div>
            <pre class="code-content">{{ generateCurlCommand() }}</pre>
          </div>

          <h4 class="sub-title">请求示例 (JavaScript)</h4>
          <div class="code-block">
            <pre class="code-content">{{ generateJavaScriptExample() }}</pre>
          </div>

          <h4 class="sub-title">响应示例</h4>
          <div class="code-block">
            <pre class="code-content">{{ generateResponseExample() }}</pre>
          </div>
        </div>

        <!-- 主动模式文档 -->
        <div class="doc-section" v-if="currentDoc.callType === 2">
          <h3 class="doc-title">远程接口配置</h3>
          <div class="info-item">
            <span class="info-label">远程地址：</span>
            <code>{{ currentDoc.remoteUrl }}</code>
          </div>
          <div class="info-item">
            <span class="info-label">请求方法：</span>
            <el-tag size="small">{{ currentDoc.remoteMethod }}</el-tag>
          </div>
          <div class="info-item" v-if="currentDoc.requestHeaders">
            <span class="info-label">请求头：</span>
            <div class="code-block">
              <pre class="code-content">{{ formatJson(currentDoc.requestHeaders) }}</pre>
            </div>
          </div>
          <div class="info-item" v-if="currentDoc.requestParams">
            <span class="info-label">请求参数：</span>
            <div class="code-block">
              <pre class="code-content">{{ formatJson(currentDoc.requestParams) }}</pre>
            </div>
          </div>
        </div>

        <!-- 字段映射 -->
        <div class="doc-section" v-if="currentDoc.responseMapping">
          <h3 class="doc-title">字段映射关系</h3>
          <el-table :data="parseFieldMapping(currentDoc.responseMapping)" border size="small">
            <el-table-column prop="remote" label="外部字段" min-width="150" />
            <el-table-column label="" width="80" align="center">
              <template #default>
                <el-icon><Right /></el-icon>
              </template>
            </el-table-column>
            <el-table-column prop="local" label="本地字段" min-width="150" />
          </el-table>
        </div>

        <!-- 增量配置 -->
        <div class="doc-section" v-if="currentDoc.syncMode === 2 && currentDoc.incrementField">
          <h3 class="doc-title">增量配置</h3>
          <div class="info-item">
            <span class="info-label">增量字段：</span>
            <code>{{ currentDoc.incrementField }}</code>
          </div>
          <div class="form-tip">系统将根据此字段判断数据是否需要更新</div>
        </div>

        <!-- 备注信息 -->
        <div class="doc-section" v-if="currentDoc.remark">
          <h3 class="doc-title">备注</h3>
          <p class="remark-text">{{ currentDoc.remark }}</p>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="docDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>

    <!-- 测试对话框 -->
    <el-dialog v-model="testDialogVisible" title="接口测试" width="900px" :close-on-click-modal="false">
      <div class="test-container" v-if="testConfig">
        <!-- 测试配置信息 -->
        <div class="test-section">
          <h3 class="test-title">测试配置</h3>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="配置名称">{{ testConfig.configName }}</el-descriptions-item>
            <el-descriptions-item label="数据类型">{{ dataTypeMap[testConfig.dataType] }}</el-descriptions-item>
            <el-descriptions-item label="调用类型">
              <el-tag :type="testConfig.callType === 1 ? 'success' : 'warning'" size="small">
                {{ testConfig.callType === 1 ? '被动模式' : '主动模式' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="请求方法">
              <el-tag size="small">{{ testConfig.callType === 1 ? testConfig.apiMethod : testConfig.remoteMethod }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 请求信息 -->
        <div class="test-section">
          <h3 class="test-title">请求信息</h3>
          <div class="info-item">
            <span class="info-label">请求URL：</span>
            <code>{{ testRequestUrl }}</code>
          </div>
          <div class="info-item" v-if="testConfig.callType === 1">
            <span class="info-label">鉴权信息：</span>
            <el-input v-model="testAuthCode" placeholder="请输入X-Auth-Code" style="width: 200px; margin-right: 10px;" size="small" />
            <el-input v-model="testAuthPsw" placeholder="请输入X-Auth-Psw" type="password" style="width: 200px;" size="small" show-password />
          </div>
          <div class="info-item" v-if="testConfig.apiMethod === 'POST' || testConfig.remoteMethod === 'POST'">
            <span class="info-label">请求体：</span>
            <el-input v-model="testRequestBody" type="textarea" :rows="6" placeholder="JSON格式请求体" />
          </div>
          <div class="test-actions">
            <el-button type="primary" @click="executeTest" :loading="testLoading">
              <el-icon class="el-icon--left"><VideoPlay /></el-icon>
              执行测试
            </el-button>
            <el-button @click="resetTestData">
              <el-icon class="el-icon--left"><Refresh /></el-icon>
              重置
            </el-button>
          </div>
        </div>

        <!-- 响应信息 -->
        <div class="test-section" v-if="testResponse">
          <h3 class="test-title">
            响应信息
            <el-tag :type="testResponse.success ? 'success' : 'danger'" size="small" style="margin-left: 10px;">
              {{ testResponse.success ? '成功' : '失败' }}
            </el-tag>
          </h3>
          <div class="info-item">
            <span class="info-label">状态码：</span>
            <el-tag :type="testResponse.status >= 200 && testResponse.status < 300 ? 'success' : 'danger'" size="small">
              {{ testResponse.status }}
            </el-tag>
          </div>
          <div class="info-item">
            <span class="info-label">耗时：</span>
            <span>{{ testResponse.duration }}ms</span>
          </div>
          <div class="info-item">
            <span class="info-label">响应头：</span>
            <div class="code-block">
              <pre class="code-content">{{ formatJson(JSON.stringify(testResponse.headers)) }}</pre>
            </div>
          </div>
          <div class="info-item">
            <span class="info-label">响应体：</span>
            <div class="code-block">
              <pre class="code-content">{{ formatJson(JSON.stringify(testResponse.data)) }}</pre>
            </div>
          </div>
        </div>

        <!-- 错误信息 -->
        <div class="test-section" v-if="testError">
          <h3 class="test-title">
            <el-icon color="#F56C6C"><CircleClose /></el-icon>
            错误信息
          </h3>
          <el-alert :title="testError" type="error" :closable="false" show-icon />
        </div>
      </div>
      
      <template #footer>
        <el-button @click="testDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Refresh, Right, Delete, CopyDocument, VideoPlay, CircleClose } from '@element-plus/icons-vue'
import { apiConfigApi, wbApplicationApi } from '@/api'

// 文档相关状态
const docDialogVisible = ref(false)
const currentDoc = ref(null)

// 测试相关状态
const testDialogVisible = ref(false)
const testConfig = ref(null)
const testAuthCode = ref('')
const testAuthPsw = ref('')
const testRequestBody = ref('')
const testLoading = ref(false)
const testResponse = ref(null)
const testError = ref(null)

const loading = ref(false)
const configList = ref([])
const applicationList = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const searchParams = reactive({
  appId: null,
  configName: '',
  callType: null,
  dataType: null,
  status: null
})

const dataTypeMap = { user: '用户', role: '角色', dept: '部门' }

// 本地字段配置（根据数据类型）
const localFieldsConfig = {
  user: [
    { label: '用户ID (id)', value: 'id' },
    { label: '用户名 (username)', value: 'username' },
    { label: '密码 (password)', value: 'password' },
    { label: '真实姓名 (real_name)', value: 'real_name' },
    { label: '邮箱 (email)', value: 'email' },
    { label: '手机号 (phone)', value: 'phone' },
    { label: '性别 (gender)', value: 'gender' },
    { label: '状态 (status)', value: 'status' },
    { label: '业务ID (business_id)', value: 'business_id' },
    { label: '数据来源 (data_source)', value: 'data_source' },
    { label: '来源应用ID (source_app_id)', value: 'source_app_id' },
    { label: '最后登录时间 (last_login_time)', value: 'last_login_time' },
    { label: '创建时间 (create_time)', value: 'create_time' },
    { label: '更新时间 (update_time)', value: 'update_time' },
    { label: '删除标识 (deleted)', value: 'deleted' }
  ],
  role: [
    { label: '角色ID (id)', value: 'id' },
    { label: '角色名称 (roleName)', value: 'roleName' },
    { label: '角色编码 (roleCode)', value: 'roleCode' },
    { label: '角色描述 (description)', value: 'description' },
    { label: '状态 (status)', value: 'status' },
    { label: '排序 (sort)', value: 'sort' },
    { label: '创建时间 (createTime)', value: 'createTime' },
    { label: '更新时间 (updateTime)', value: 'updateTime' },
    { label: '删除标识 (deleted)', value: 'deleted' }
  ],
  dept: [
    { label: '部门ID (id)', value: 'id' },
    { label: '部门名称 (deptName)', value: 'deptName' },
    { label: '部门编码 (deptCode)', value: 'deptCode' },
    { label: '父部门ID (parentId)', value: 'parentId' },
    { label: '部门描述 (description)', value: 'description' },
    { label: '负责人 (leader)', value: 'leader' },
    { label: '联系电话 (phone)', value: 'phone' },
    { label: '邮箱 (email)', value: 'email' },
    { label: '状态 (status)', value: 'status' },
    { label: '排序 (sort)', value: 'sort' },
    { label: '创建时间 (createTime)', value: 'createTime' },
    { label: '更新时间 (updateTime)', value: 'updateTime' },
    { label: '删除标识 (deleted)', value: 'deleted' }
  ]
}

// 字段映射数据
const fieldMappings = ref([])

// API基础地址（从环境变量或配置获取）
const apiBaseUrl = computed(() => {
  // 后端实际地址：http://localhost:8080/zapi-allin-sys
  return 'http://localhost:8080/zapi-allin-sys'
})

// 自动生成的接口路径
const generatedApiPath = computed(() => {
  if (formData.value.id && formData.value.configUuid) {
    // 编辑模式，使用UUID生成路径
    return `/openapi/sync/${formData.value.dataType}/${formData.value.configUuid}`
  }
  // 新增模式，显示预览路径
  return `/openapi/sync/${formData.value.dataType}/{configUuid}`
})

// 复制完整API路径
const copyApiPath = async () => {
  const fullPath = apiBaseUrl.value + generatedApiPath.value
  try {
    await navigator.clipboard.writeText(fullPath)
    ElMessage.success('接口地址已复制到剪贴板')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

// 当前数据类型对应的本地字段
const currentLocalFields = computed(() => {
  return localFieldsConfig[formData.value.dataType] || []
})

// 监听数据类型变化，清空映射
watch(() => formData.value.dataType, () => {
  if (dialogVisible.value && dialogMode.value === 'add') {
    fieldMappings.value = []
  }
})

// 添加映射行
const addMappingRow = () => {
  fieldMappings.value.push({ remoteField: '', localField: '' })
}

// 删除映射行
const removeMappingRow = (index) => {
  fieldMappings.value.splice(index, 1)
  updateResponseMapping()
}

// 更新responseMapping字段（JSON格式）
const updateResponseMapping = () => {
  const mapping = {}
  fieldMappings.value.forEach(row => {
    if (row.remoteField && row.localField) {
      mapping[row.remoteField] = row.localField
    }
  })
  formData.value.responseMapping = Object.keys(mapping).length > 0 ? JSON.stringify(mapping) : ''
}

// 解析responseMapping到fieldMappings
const parseResponseMapping = () => {
  fieldMappings.value = []
  if (formData.value.responseMapping) {
    try {
      const mapping = JSON.parse(formData.value.responseMapping)
      Object.keys(mapping).forEach(key => {
        fieldMappings.value.push({ remoteField: key, localField: mapping[key] })
      })
    } catch (e) {
      console.error('解析字段映射失败', e)
    }
  }
}

const dialogVisible = ref(false)
const dialogTitle = ref('')
const dialogMode = ref('add')
const submitLoading = ref(false)
const formRef = ref(null)

const formData = ref({
  id: null,
  appId: null,
  configName: '',
  configUuid: '',
  callType: 1,
  dataType: 'user',
  syncMode: 1,
  apiPath: '',
  apiMethod: 'POST',
  remoteUrl: '',
  remoteMethod: 'GET',
  requestHeaders: '',
  requestParams: '',
  responseMapping: '',
  incrementField: '',
  cronExpression: '',
  autoSync: 0,
  status: 1,
  remark: ''
})

const formRules = {
  configName: [{ required: true, message: '请输入配置名称', trigger: 'blur' }],
  appId: [{ required: true, message: '请选择应用', trigger: 'change' }],
  callType: [{ required: true, message: '请选择调用类型', trigger: 'change' }],
  dataType: [{ required: true, message: '请选择数据类型', trigger: 'change' }],
  syncMode: [{ required: true, message: '请选择同步方式', trigger: 'change' }]
}

const loadApplicationList = async () => {
  try {
    const response = await wbApplicationApi.getAllApplications()
    if (response.code === 200) {
      applicationList.value = response.data || []
    }
  } catch (error) {
    console.error('加载应用列表失败', error)
  }
}

const loadConfigList = async () => {
  loading.value = true
  try {
    const params = {
      current: currentPage.value,
      size: pageSize.value,
      ...searchParams
    }
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === '') delete params[key]
    })
    
    const response = await apiConfigApi.getConfigList(params)
    if (response.code === 200) {
      configList.value = response.data.records || []
      total.value = Number(response.data.total) || 0
    }
  } catch (error) {
    ElMessage.error('加载配置列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadConfigList()
}

const handleRefresh = () => loadConfigList()

const handleAdd = () => {
  dialogMode.value = 'add'
  dialogTitle.value = '添加配置'
  resetForm()
  fieldMappings.value = []
  dialogVisible.value = true
}

const handleEdit = async (row) => {
  dialogMode.value = 'edit'
  dialogTitle.value = '编辑配置'
  try {
    const response = await apiConfigApi.getConfig(row.id)
    if (response.code === 200) {
      formData.value = { ...response.data }
      // 被动模式强制使用POST方法
      if (formData.value.callType === 1) {
        formData.value.apiMethod = 'POST'
      }
      parseResponseMapping()
      dialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取配置信息失败')
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  // 提交前更新字段映射
  updateResponseMapping()
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        let response
        if (dialogMode.value === 'add') {
          response = await apiConfigApi.addConfig(formData.value)
        } else {
          response = await apiConfigApi.updateConfig(formData.value)
        }
        if (response.code === 200) {
          ElMessage.success(dialogMode.value === 'add' ? '添加成功' : '更新成功')
          dialogVisible.value = false
          loadConfigList()
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

const handleStatusChange = async (row, value) => {
  const newStatus = value ? 1 : 0
  try {
    const response = await apiConfigApi.changeStatus(row.id, newStatus)
    if (response.code === 200) {
      ElMessage.success(newStatus === 1 ? '启用成功' : '禁用成功')
      loadConfigList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除配置"${row.configName}"吗？`, '确认删除', { type: 'warning' })
    const response = await apiConfigApi.deleteConfig(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadConfigList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

// 打开测试对话框
const handleTest = async (row) => {
  try {
    const response = await apiConfigApi.getConfig(row.id)
    if (response.code === 200) {
      testConfig.value = {
        ...response.data,
        appName: row.appName
      }
      // 被动模式强制使用POST方法
      if (testConfig.value.callType === 1) {
        testConfig.value.apiMethod = 'POST'
      }
      
      // 初始化测试数据
      resetTestData()
      
      testDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取配置信息失败')
  }
}

// 测试请求URL
const testRequestUrl = computed(() => {
  if (!testConfig.value) return ''
  
  if (testConfig.value.callType === 1) {
    // 被动模式 - 使用UUID生成的路径
    return apiBaseUrl.value + testConfig.value.apiPath
  } else {
    // 主动模式 - 使用配置的远程地址
    return testConfig.value.remoteUrl
  }
})

// 重置测试数据
const resetTestData = () => {
  testAuthCode.value = ''
  testAuthPsw.value = ''
  // 使用testConfig而不是currentDoc
  testRequestBody.value = generateTestRequestBody()
  testResponse.value = null
  testError.value = null
}

// 生成测试请求体
const generateTestRequestBody = () => {
  if (!testConfig.value || !testConfig.value.responseMapping) {
    return '{}'
  }
  
  try {
    const mapping = JSON.parse(testConfig.value.responseMapping)
    const body = {}
    
    // 根据字段映射生成示例数据
    Object.keys(mapping).forEach(remoteField => {
      const localField = mapping[remoteField]
      
      // 根据字段名推断示例值
      if (localField.includes('id')) {
        body[remoteField] = 1
      } else if (localField.includes('name') || localField.includes('username')) {
        body[remoteField] = 'zhangsan'
      } else if (localField.includes('phone')) {
        body[remoteField] = '13800138000'
      } else if (localField.includes('email')) {
        body[remoteField] = 'zhangsan@example.com'
      } else if (localField.includes('gender')) {
        body[remoteField] = 1
      } else if (localField.includes('status')) {
        body[remoteField] = 1
      } else if (localField.includes('code')) {
        body[remoteField] = 'CODE001'
      } else if (localField.includes('description')) {
        body[remoteField] = '示例描述'
      } else if (localField.includes('time') || localField.includes('date')) {
        body[remoteField] = '2024-01-01 12:00:00'
      } else if (localField.includes('sort')) {
        body[remoteField] = 1
      } else if (localField.includes('leader')) {
        body[remoteField] = '张三'
      } else if (localField.includes('parent')) {
        body[remoteField] = 0
      } else {
        body[remoteField] = 'example_value'
      }
    })
    
    return JSON.stringify(body, null, 2)
  } catch (e) {
    return '{}'
  }
}

// 执行测试
const executeTest = async () => {
  testLoading.value = true
  testResponse.value = null
  testError.value = null
  
  const startTime = Date.now()
  
  try {
    const url = testRequestUrl.value
    const method = testConfig.value.callType === 1 ? testConfig.value.apiMethod : testConfig.value.remoteMethod
    
    // 构建请求选项
    const options = {
      method: method,
      headers: {
        'Content-Type': 'application/json'
      }
    }
    
    // 添加鉴权头（被动模式）
    if (testConfig.value.callType === 1) {
      if (testAuthCode.value) {
        options.headers['X-Auth-Code'] = testAuthCode.value
      }
      if (testAuthPsw.value) {
        options.headers['X-Auth-Psw'] = testAuthPsw.value
      }
    }
    
    // 添加请求体（POST请求）
    if (method === 'POST' && testRequestBody.value) {
      try {
        // 验证JSON格式
        JSON.parse(testRequestBody.value)
        options.body = testRequestBody.value
      } catch (e) {
        throw new Error('请求体JSON格式错误')
      }
    }
    
    // 发送请求
    const response = await fetch(url, options)
    const duration = Date.now() - startTime
    
    // 获取响应头
    const headers = {}
    response.headers.forEach((value, key) => {
      headers[key] = value
    })
    
    // 获取响应体
    let data
    const contentType = response.headers.get('content-type')
    if (contentType && contentType.includes('application/json')) {
      data = await response.json()
    } else {
      data = await response.text()
    }
    
    testResponse.value = {
      success: response.ok,
      status: response.status,
      statusText: response.statusText,
      headers: headers,
      data: data,
      duration: duration
    }
    
    if (response.ok) {
      ElMessage.success('测试请求执行成功')
    } else {
      ElMessage.warning(`请求返回错误状态: ${response.status}`)
    }
    
  } catch (error) {
    testError.value = error.message || '请求执行失败'
    ElMessage.error('测试请求失败: ' + testError.value)
  } finally {
    testLoading.value = false
  }
}

// 显示接口文档
const handleShowDoc = async (row) => {
  try {
    const response = await apiConfigApi.getConfig(row.id)
    if (response.code === 200) {
      currentDoc.value = {
        ...response.data,
        appName: row.appName
      }
      // 被动模式强制使用POST方法
      if (currentDoc.value.callType === 1) {
        currentDoc.value.apiMethod = 'POST'
      }
      docDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取配置信息失败')
  }
}

// 复制文档中的API路径
const copyDocApiPath = async () => {
  const fullPath = apiBaseUrl.value + currentDoc.value.apiPath
  try {
    await navigator.clipboard.writeText(fullPath)
    ElMessage.success('接口地址已复制')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

// 复制curl命令
const copyCurlCommand = async () => {
  try {
    await navigator.clipboard.writeText(generateCurlCommand())
    ElMessage.success('curl命令已复制')
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

// 生成请求body示例（根据字段映射）
const generateRequestBody = () => {
  if (!currentDoc.value || !currentDoc.value.responseMapping) {
    return '{}'
  }
  
  try {
    const mapping = JSON.parse(currentDoc.value.responseMapping)
    const body = {}
    
    // 根据字段映射生成示例数据
    Object.keys(mapping).forEach(remoteField => {
      const localField = mapping[remoteField]
      
      // 根据字段名推断示例值
      if (localField.includes('id')) {
        body[remoteField] = 1
      } else if (localField.includes('name') || localField.includes('username')) {
        body[remoteField] = 'zhangsan'
      } else if (localField.includes('nickname')) {
        body[remoteField] = '张三'
      } else if (localField.includes('phone')) {
        body[remoteField] = '13800138000'
      } else if (localField.includes('email')) {
        body[remoteField] = 'zhangsan@example.com'
      } else if (localField.includes('gender') || localField.includes('sex')) {
        body[remoteField] = 1
      } else if (localField.includes('status')) {
        body[remoteField] = 1
      } else if (localField.includes('code')) {
        body[remoteField] = 'CODE001'
      } else if (localField.includes('description') || localField.includes('remark')) {
        body[remoteField] = '示例描述'
      } else if (localField.includes('time') || localField.includes('date')) {
        body[remoteField] = '2024-01-01 12:00:00'
      } else if (localField.includes('sort') || localField.includes('order')) {
        body[remoteField] = 1
      } else if (localField.includes('leader')) {
        body[remoteField] = '张三'
      } else if (localField.includes('avatar')) {
        body[remoteField] = 'https://example.com/avatar.jpg'
      } else if (localField.includes('parent')) {
        body[remoteField] = 0
      } else {
        body[remoteField] = 'example_value'
      }
    })
    
    return JSON.stringify(body, null, 2)
  } catch (e) {
    return '{}'
  }
}

// 生成curl命令示例
const generateCurlCommand = () => {
  if (!currentDoc.value) return ''
  const url = apiBaseUrl.value + currentDoc.value.apiPath
  const method = currentDoc.value.apiMethod
  
  if (method === 'GET') {
    // GET请求，如果有字段映射则作为查询参数示例
    let queryParams = ''
    if (currentDoc.value.responseMapping) {
      try {
        const mapping = JSON.parse(currentDoc.value.responseMapping)
        const params = []
        Object.keys(mapping).slice(0, 2).forEach(field => {
          params.push(`${field}=value`)
        })
        if (params.length > 0) {
          queryParams = '?' + params.join('&')
        }
      } catch (e) {
        // 忽略解析错误
      }
    }
    
    return `curl -X GET "${url}${queryParams}" \\
  -H "X-Auth-Code: your-auth-code" \\
  -H "X-Auth-Psw: your-auth-password"`
  } else {
    const bodyExample = generateRequestBody()
    return `curl -X POST "${url}" \\
  -H "Content-Type: application/json" \\
  -H "X-Auth-Code: your-auth-code" \\
  -H "X-Auth-Psw: your-auth-password" \\
  -d '${bodyExample}'`
  }
}

// 生成JavaScript请求示例
const generateJavaScriptExample = () => {
  if (!currentDoc.value) return ''
  const url = apiBaseUrl.value + currentDoc.value.apiPath
  const method = currentDoc.value.apiMethod
  
  if (method === 'GET') {
    let queryParams = ''
    if (currentDoc.value.responseMapping) {
      try {
        const mapping = JSON.parse(currentDoc.value.responseMapping)
        const params = []
        Object.keys(mapping).slice(0, 2).forEach(field => {
          params.push(`${field}=value`)
        })
        if (params.length > 0) {
          queryParams = '?' + params.join('&')
        }
      } catch (e) {
        // 忽略解析错误
      }
    }
    
    return `fetch('${url}${queryParams}', {
  method: 'GET',
  headers: {
    'X-Auth-Code': 'your-auth-code',
    'X-Auth-Psw': 'your-auth-password'
  }
})
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));`
  } else {
    const bodyExample = generateRequestBody()
    return `fetch('${url}', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-Auth-Code': 'your-auth-code',
    'X-Auth-Psw': 'your-auth-password'
  },
  body: JSON.stringify(${bodyExample})
})
  .then(response => response.json())
  .then(data => console.log(data))
  .catch(error => console.error('Error:', error));`
  }
}

// 生成响应示例
const generateResponseExample = () => {
  if (!currentDoc.value) return ''
  const dataType = currentDoc.value.dataType
  
  let dataExample = {}
  if (dataType === 'user') {
    dataExample = {
      id: 1,
      username: 'zhangsan',
      nickname: '张三',
      phone: '13800138000',
      email: 'zhangsan@example.com'
    }
  } else if (dataType === 'role') {
    dataExample = {
      id: 1,
      role_name: '管理员',
      role_code: 'admin',
      description: '系统管理员角色'
    }
  } else if (dataType === 'dept') {
    dataExample = {
      id: 1,
      dept_name: '技术部',
      dept_code: 'TECH',
      leader: '李四'
    }
  }
  
  return JSON.stringify({
    code: 200,
    message: '获取成功',
    data: {
      records: [dataExample],
      total: 1,
      size: 10,
      current: 1
    }
  }, null, 2)
}

// 格式化JSON字符串
const formatJson = (jsonStr) => {
  try {
    return JSON.stringify(JSON.parse(jsonStr), null, 2)
  } catch (e) {
    return jsonStr
  }
}

// 解析字段映射
const parseFieldMapping = (mappingStr) => {
  try {
    const mapping = JSON.parse(mappingStr)
    return Object.keys(mapping).map(key => ({
      remote: key,
      local: mapping[key]
    }))
  } catch (e) {
    return []
  }
}

const handleDialogClose = () => resetForm()

const resetForm = () => {
  formData.value = {
    id: null, appId: null, configName: '', configUuid: '', callType: 1, dataType: 'user', syncMode: 1,
    apiPath: '', apiMethod: 'GET', remoteUrl: '', remoteMethod: 'GET',
    requestHeaders: '', requestParams: '', responseMapping: '', incrementField: '',
    cronExpression: '', autoSync: 0, status: 1, remark: ''
  }
  fieldMappings.value = []
  formRef.value?.clearValidate()
}

const handleSizeChange = (size) => { pageSize.value = size; currentPage.value = 1; loadConfigList() }
const handleCurrentChange = (page) => { currentPage.value = page; loadConfigList() }

onMounted(() => {
  loadApplicationList()
  loadConfigList()
})
</script>

<style lang="scss" scoped>
.config-page {
  padding: 20px;
  background: var(--bg-secondary, #f8fafc);
  min-height: calc(100vh - 80px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  
  .page-title { font-size: 24px; font-weight: 600; margin: 0 0 8px 0; }
  .page-desc { color: var(--text-secondary); font-size: 14px; margin: 0; }
}

.header-actions { display: flex; gap: 12px; }

.search-section {
  display: flex;
  margin-bottom: 24px;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
}

.search-left { display: flex; gap: 12px; flex-wrap: wrap; }

.config-content {
  margin-bottom: 24px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 16px;
}

.pagination-section {
  display: flex;
  justify-content: center;
  padding: 20px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
}

:deep(.el-dialog) {
  border-radius: 16px;
  .el-dialog__header { padding: 20px 24px 16px; border-bottom: 1px solid var(--border-color); }
  .el-dialog__body { padding: 0; max-height: 65vh; overflow-y: auto; }
  .el-dialog__footer { padding: 16px 24px; border-top: 1px solid var(--border-color); }
}

.config-form {
  .form-section {
    padding: 20px 24px;
    border-bottom: 1px solid #f0f0f0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .section-title {
      font-size: 15px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 16px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
  
  .form-tip {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
    line-height: 1.5;
  }
  
  .api-path-display {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 10px 12px;
    background: #f5f7fa;
    border-radius: 6px;
    border: 1px solid #e4e7ed;
    
    .api-path-text {
      flex: 1;
      font-family: monospace;
      font-size: 13px;
      color: #606266;
      word-break: break-all;
    }
  }
  
  .mapping-table {
    :deep(.el-table__header) {
      .el-table__cell {
        background: #fafafa;
        font-weight: 500;
        font-size: 13px;
      }
    }
  }
  
  :deep(.el-empty) {
    padding: 20px 0;
  }
}

.field-mapping-section {
  .mapping-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    
    .mapping-tip {
      font-size: 13px;
      color: var(--text-secondary);
    }
  }
}

// 接口文档样式
.api-doc-container {
  .doc-section {
    margin-bottom: 24px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .doc-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 16px 0;
    padding-bottom: 8px;
    border-bottom: 2px solid #409eff;
  }
  
  .sub-title {
    font-size: 14px;
    font-weight: 600;
    color: #606266;
    margin: 16px 0 12px 0;
  }
  
  .code-block {
    background: #f5f7fa;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    overflow: hidden;
    margin-bottom: 12px;
    
    .code-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 12px;
      background: #e4e7ed;
      border-bottom: 1px solid #dcdfe6;
      font-size: 13px;
      color: #606266;
    }
    
    .code-content {
      padding: 12px;
      margin: 0;
      font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
      font-size: 13px;
      line-height: 1.6;
      color: #2c3e50;
      white-space: pre-wrap;
      word-wrap: break-word;
      overflow-x: auto;
    }
  }
  
  .info-item {
    margin-bottom: 12px;
    line-height: 1.8;
    
    .info-label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }
    
    code {
      background: #f5f7fa;
      padding: 2px 8px;
      border-radius: 4px;
      font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
      font-size: 13px;
      color: #e83e8c;
    }
  }
  
  .remark-text {
    padding: 12px;
    background: #f5f7fa;
    border-left: 4px solid #409eff;
    border-radius: 4px;
    color: #606266;
    line-height: 1.6;
    margin: 0;
  }
  
  :deep(.el-descriptions) {
    .el-descriptions__label {
      font-weight: 600;
      background: #fafafa;
    }
  }
  
  :deep(.el-table) {
    font-size: 13px;
  }
}

// 测试对话框样式
.test-container {
  .test-section {
    margin-bottom: 24px;
    padding: 20px;
    background: #fafafa;
    border-radius: 8px;
    
    &:last-child {
      margin-bottom: 0;
    }
  }
  
  .test-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 16px 0;
    padding-bottom: 8px;
    border-bottom: 2px solid #409eff;
    display: flex;
    align-items: center;
  }
  
  .test-actions {
    margin-top: 16px;
    display: flex;
    gap: 12px;
  }
  
  .info-item {
    margin-bottom: 12px;
    line-height: 1.8;
    
    .info-label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
      display: inline-block;
      min-width: 80px;
    }
    
    code {
      background: #f5f7fa;
      padding: 4px 8px;
      border-radius: 4px;
      font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
      font-size: 13px;
      color: #e83e8c;
      word-break: break-all;
    }
  }
  
  .code-block {
    background: #f5f7fa;
    border: 1px solid #e4e7ed;
    border-radius: 6px;
    overflow: hidden;
    margin-top: 8px;
    
    .code-content {
      padding: 12px;
      margin: 0;
      font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
      font-size: 13px;
      line-height: 1.6;
      color: #2c3e50;
      white-space: pre-wrap;
      word-wrap: break-word;
      overflow-x: auto;
      max-height: 300px;
    }
  }
}
</style>
