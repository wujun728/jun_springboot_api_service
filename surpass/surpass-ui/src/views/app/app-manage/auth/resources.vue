<template>
  <div class="resource-management">
    <!-- 主内容区域 -->
    <div class="main-content">
      <!-- 左侧资源树 -->
      <el-card class="tree-card">
        <div class="tree-container">
          <el-tree
              node-key="id"
              :data="dataOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="tree"
              :default-expanded-keys="treeData"
              @node-click="handleNodeClick"
              highlight-current
              class="modern-tree"
              v-slot="{ node, data }"
          >
                <span class="tree-node">
                  <!-- 资源图标 -->
                  <svg-icon :icon-class="getResourceIcon(data.classify, data.resStyle)" class="resource-icon"/>

                  <!-- 资源名称 -->
                  <span class="tree-label">
                    <el-tooltip
                        :content="node.label"
                        placement="top"
                        :disabled="node.label.length <= 15"
                    >
                      <span>{{ node.label.length > 15 ? node.label.slice(0, 15) + '...' : node.label }}</span>
                    </el-tooltip>
                  </span>

                  <!-- 资源类型标签 -->
                  <dict-tag class="tree-tag" :options="resources_type" :value="data.classify"/>
                </span>
          </el-tree>

          <!-- 空状态 -->
          <div v-if="!dataOptions || dataOptions.length === 0" class="tree-empty">
            <svg-icon icon-class="empty" class="empty-icon"/>
            <p>暂无资源数据</p>
          </div>
        </div>
      </el-card>
      <!-- 右侧资源列表 -->
      <el-card class="list-card">
        <div class="list-header">
          <el-button type="primary" @click="showCreateDialog">
            新增资源
          </el-button>
          <el-button
              type="danger"
              :disabled="ids.length === 0"
              @click="onBatchDelete"
          >
            批量删除
          </el-button>
          <el-button @click="refreshList">
            刷新
          </el-button>
        </div>

        <el-card class="common-card query-box">
          <div class="queryForm">
            <el-form :model="queryParams" ref="queryRef" :inline="true"
                     @submit.native.prevent>
              <el-form-item label="资源名称">
                <el-input
                    v-model="queryParams.name"
                    clearable
                    style="width: 200px"
                    @keyup.enter="loadApis"
                />
              </el-form-item>
              <el-form-item label="资源类型">
                <el-select v-model="queryParams.classify" clearable style="width: 200px" @change="loadApis">
                  <el-option
                      v-for="dict in resources_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button @click="loadApis">{{ t('org.button.query') }}</el-button>
                <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>

        <!-- 资源列表 -->
        <div class="resource-list">
          <el-table
              :data="apiList"
              v-loading="loading"
              style="width: 100%"
              @selection-change="handleSelectionChange"
              class="modern-table"
          >
            <el-table-column type="selection" width="55" align="center" fixed="left"/>
            <el-table-column prop="name" label="资源名称" min-width="180"></el-table-column>
            <el-table-column prop="path" label="请求地址" min-width="200" show-overflow-tooltip/>
            <el-table-column prop="classify" label="类型" width="120">
              <template #default="{ row }">
                <dict-tag :options="resources_type" :value="row.classify"/>
              </template>
            </el-table-column>
            <el-table-column prop="method" label="方法" width="100"
                             v-if="queryParams.classify === 'openApi' || queryParams.classify === 'api'">
              <template #default="{ row }">
                <el-tag :type="getMethodTagType(row.method)" effect="light" class="method-tag">
                  {{ row.method }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="datasourceId" label="数据源" width="150"
                             v-if="queryParams.classify === 'openApi'">
              <template #default="{ row }">
                <el-tag v-if="row.datasourceId" type="info" effect="light">
                  {{ dataSourceList.find(ds => ds.id === row.datasourceId)?.name || '未知' }}
                </el-tag>
                <span v-else class="empty-text">-</span>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.status === '1' ? 'success' : 'danger'" size="small" effect="light">
                  {{ row.status === '1' ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="API管理" width="80" align="center">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-tooltip content="管理" placement="top" v-if="row.classify === 'openApi' || row.classify === 'proxy'">
                    <el-button size="small" type="primary" link @click="viewVersions(row)">
                      <svg-icon icon-class="up-square"/>
                    </el-button>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center">
              <template #default="{ row }">
                <div class="action-buttons">
                  <el-tooltip content="编辑" placement="top">
                    <el-button size="small" link type="primary" @click="editApi(row)">
                      <svg-icon icon-class="edit"/>
                    </el-button>
                  </el-tooltip>
                  <el-tooltip content="删除" placement="top">
                    <el-button size="small" link type="danger" @click="deleteApi(row)">
                      <svg-icon icon-class="delete"/>
                    </el-button>
                  </el-tooltip>
                </div>
              </template>
            </el-table-column>
          </el-table>

          <!-- 分页 -->
          <div class="pagination-wrapper">
            <pagination
                v-show="total > 0"
                :total="total"
                v-model:page="queryParams.pageNumber"
                v-model:limit="queryParams.pageSize"
                :page-sizes="queryParams.pageSizeOptions"
                @pagination="loadApis"
            />
          </div>

          <!-- 空状态 -->
          <div v-if="!loading && apiList.length === 0" class="list-empty">
            <svg-icon icon-class="empty-data" class="empty-data-icon"/>
            <p>暂无资源数据</p>
            <el-button type="primary" plain @click="showCreateDialog">
              创建第一个资源
            </el-button>
          </div>
        </div>
    </el-card>
  </div>
  <!-- 新增/编辑对话框组件 -->
  <ResourceDialog
      v-model:visible="dialogVisible"
      v-model:formData="formData"
      :isEdit="isEdit"
      :dataOptions="dataOptions"
      :dataSourceList="dataSourceList"
      :proxySourceList="proxySourceList"
      :appId="props.appId"
      :contextPath="contextPath"
      @success="handleDialogSuccess"
      @close="handleDialogClose"
      @updateProxySource="getProxyList"
  />
</div>
</template>

<script setup>
import {ref, reactive, computed, toRefs} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import * as dataSourceApi from '@/api/api-service/dataSource'
import * as appResourcesApi from '@/api/app/resources'
import modal from "@/plugins/modal";
import {set2String} from "@/utils/index";
import {useI18n} from "vue-i18n";
import * as proxy from "@/utils/Dict";
import DictTag from "@/components/DictTag/index.vue";
import ResourceDialog from "./components/ResourceDialog.vue";
import {getApp} from "@/api/api-service/apps";
import * as appProxyApis from "@/api/app/app-proxy-rules.ts";

const {resources_type, action_type, method_type, proxy_auth_type} = proxy.useDict("resources_type", "action_type", "method_type", "proxy_auth_type");
const router = useRouter()

const props = defineProps({
  appId: {
    type: String,
    default: undefined
  }
});

// 响应式数据
const loading = ref(false)
const dataSourceLoading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)

const resTreeRef = ref(undefined);
const ids = ref([]);
const selectionlist = ref([]);
const apiList = ref([])
const total = ref(0);
const dataSourceList = ref([])
const proxySourceList = ref([])
const dataOptions = ref([]);
const dataOptionsMenu = ref([]);
const treeData = ref([]);//当前选中节点
const treeDataMenu = ref([]);//当前选中节点菜单
const defaultProps = ref({
  children: "children",
  label: "name"
});

const contextPath = ref("");

const data = reactive({
  queryParams: {
    appId: null,
    name: '',
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {t} = useI18n()

const {queryParams} = toRefs(data);

const formData = reactive({
  id: null,
  name: '',
  classify: 'menu',
  path: '',
  method: 'GET',
  params: '',
  datasourceId: null,
  description: '',
  actionType: 'r',
  permission: '',
  resStyle: '',
  parentId: '',
  isVisible: 'y',
  isCache: 'n',
  isFrame: 'n',
  isOpen: 'y',
  status: '1',
  sortIndex: '1'
})

// 方法
const loadApis = async () => {
  appResourcesApi.pageResources(queryParams.value).then((res) => {
    if (res.code === 0) {
      loading.value = false;
      apiList.value = res.data.rows;
      total.value = res.data.records;
    }
  })

  getApp(props.appId).then((res) => {
    contextPath.value= res.data.contextPath;
    console.log("contextPath "+contextPath.value);
  });
}

const loadDataSources = async () => {
  try {
    dataSourceLoading.value = true
    const response = await dataSourceApi.list()
    dataSourceList.value = response.data || []
  } catch (error) {
    ElMessage.error('加载数据源列表失败')
    console.error('加载数据源列表失败:', error)
  } finally {
    dataSourceLoading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

const editApi = (row) => {
  isEdit.value = true
  Object.assign(formData, {...row})
  dialogVisible.value = true
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    name: '',
    classify: 'menu',
    path: '',
    method: 'GET',
    params: '',
    datasourceId: null,
    description: '',
    actionType: 'r',
    permission: '',
    resStyle: '',
    parentId: '',
    isVisible: 'y',
    isCache: 'n',
    isFrame: 'n',
    isOpen: 'y',
    status: '1',
    sortIndex: '1'
  })
}

const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handleDialogSuccess = () => {
  dialogVisible.value = false
  loadApis()
  loadTree()
}

const viewVersions = (row) => {
  router.push(`/api/Version?apiId=${row.id}&appId=${props.appId}`)
}

const deleteApi = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除资源 "${row.name}" 吗？此操作不可恢复。`,
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await appResourcesApi.deleteData(row.id)
    ElMessage.success('删除成功')
    loadApis()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

/** 多选删除操作*/
function onBatchDelete() {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    let setIds = set2String(ids.value);
    return appResourcesApi.deleteData(setIds);
  }).then((res) => {
    if (res.code === 0) {
      loadApis();
      loadTree();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
}

/** 多选操作*/
function handleSelectionChange(selection) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item) => item.id);
}

const getMethodTagType = (method) => {
  const types = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger',
    'PATCH': 'info'
  }
  return types[method] || 'info'
}

const refreshList = () => {
  loadApis()
}

/**
 * 重置
 */
function resetQuery() {
  queryParams.value.name = undefined;
  queryParams.value.classify = undefined;
  queryParams.value.parentId = undefined;
  loadApis();
}

function loadTree() {
  let params = {
    appId: props.appId,
  }

  appResourcesApi.getTree(params).then(res => {
    // 清空，避免多次调用重复 push
    treeData.value = []
    treeDataMenu.value = []

    collectExpandIds(res.data.resources || [], treeData, 1)
    // collectExpandIds(res.data.resourcesMenu || [], treeDataMenu, 1)

    dataOptions.value = res.data.resources;
    // dataOptionsMenu.value = res.data.resourcesMenu;
  })
}

const collectExpandIds = (nodes, targetRef, maxLevel = 1) => {
  const traverse = (node, level) => {
    if (level <= maxLevel) {
      targetRef.value.push(node.id)
    }
    if (Array.isArray(node.children)) {
      node.children.forEach(child => traverse(child, level + 1))
    }
  }

  nodes.forEach(root => traverse(root, 1))
}

/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.parentId = data.key;
  loadApis();
}

const getResourceIcon = (classify, icon) => {
  const map = {
    menu: 'menu',
    button: 'button',
    api: 'api',
    openApi: 'api'
  }
  return icon || map[classify] || 'resource'
}

const getProxyList = () => {
  appProxyApis.getAllProxyRules({
    appId: props.appId
  }).then(res => {
    proxySourceList.value = res.data
  })
}

watch(
    () => props.appId,
    (val) => {
      if (val != null) {
        queryParams.value.appId = val
        loadApis()
        loadTree();
        loadDataSources()
        getProxyList()
      }
    },
    {immediate: true}
)
</script>

<style scoped lang="scss">
.resource-management {
  display: flex;
  flex-direction: column;

  .main-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    flex: 1;
    min-height: 0;

    .el-row {
      height: 100%;

      .el-col {
        height: 100%;
      }
    }

    .tree-card {
      width: 320px;
      margin-right: 20px;
      height: 100%;
      border-radius: 12px;
      border: 1px solid #e4e7ed;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      display: flex;
      flex-direction: column;

      :deep(.el-card__body) {
        padding: 0 !important;
      }

      :deep(.el-card__header) {
        padding: 8px 20px;
        border-bottom: 1px solid #f0f2f5;
        background: linear-gradient(135deg, #fafbfc 0%, #f5f7fa 100%);

        .tree-header {
          display: flex;
          align-items: center;
          justify-content: space-between;

          .tree-icon {
            width: 18px;
            height: 18px;
            color: #409eff;
            margin-right: 8px;
          }

          .tree-title {
            font-weight: 600;
            flex: 1;
          }

          .refresh-btn {
            padding: 4px;
            border-radius: 4px;

            &:hover {
              background: rgba(64, 158, 255, 0.1);

              .svg-icon {
                color: #409eff;
              }
            }

            .svg-icon {
              width: 16px;
              height: 16px;
              color: #909399;
              transition: color 0.3s;
            }
          }
        }
      }

      .tree-container {
        min-height: 600px;
        max-height: 100%;
        overflow: auto;
        padding-top: 20px;

        :deep(.el-tree-node__content) {
          padding: 20px 12px;
        }

        .modern-tree {
          .tree-node {
            display: flex;
            align-items: center;
            width: 100%;
            gap: 8px;

            .resource-icon {
              width: 16px;
              height: 16px;
              color: #909399;
              flex-shrink: 0;
              transition: all 0.3s;
            }

            .tree-label {
              flex: 1;
              font-size: 14px;
              color: #606266;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
            }

            .tree-tag {
              flex-shrink: 0;
              font-size: 12px;
              padding: 0 6px;
              height: 20px;
              line-height: 18px;
            }
          }
        }

        .tree-empty {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          height: 200px;
          color: #909399;

          .empty-icon {
            width: 60px;
            height: 60px;
            color: #dcdfe6;
            margin-bottom: 12px;
          }

          p {
            margin: 0;
            font-size: 14px;
          }
        }
      }
    }

    .list-card {
      flex: 1;
      height: 100%;
      border-radius: 12px;
      border: 1px solid #e4e7ed;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
      display: flex;
      flex-direction: column;

      .list-header {
        margin-bottom: 20px;
      }

      :deep(.el-card__header) {
        padding: 8px 20px;
        border-bottom: 1px solid #f0f2f5;
        background: linear-gradient(135deg, #fafbfc 0%, #f5f7fa 100%);
      }

      .resource-list {
        height: 100%;
        display: flex;
        flex-direction: column;

        .list-empty {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          height: 300px;
          color: #909399;

          .empty-data-icon {
            width: 80px;
            height: 80px;
            color: #dcdfe6;
            margin-bottom: 16px;
          }

          p {
            margin: 0 0 16px;
            font-size: 14px;
          }

          .el-button {
            padding: 8px 24px;
            border-radius: 6px;
          }
        }
      }
    }
  }
}

.query-card {
  animation-delay: 0.1s;
}

.tree-card {
  animation-delay: 0.2s;
}

.list-card {
  animation-delay: 0.3s;
}
</style>
