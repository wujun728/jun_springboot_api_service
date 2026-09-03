<template>
  <div>
    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="1200px"
        :before-close="handleDialogClose"
        append-to-body
        destroy-on-close
    >
      <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="120px"
          class="resource-form"
      >
        <!-- 基础信息卡片 -->
        <el-card class="form-card" shadow="never">
          <template #header>
            <div class="card-header">
              <svg-icon icon-class="info" class="card-icon"/>
              <span class="card-title">基础信息</span>
            </div>
          </template>

          <el-row :gutter="24">
            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="资源名称" prop="name">
                <el-input
                    v-model="formData.name"
                    placeholder="请输入资源名称"
                    clearable
                />
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="资源类型">
                <el-radio-group v-model="formData.classify" class="resource-type-group">
                  <el-radio
                      v-for="dict in resources_type"
                      :key="dict.value"
                      :label="dict.value"
                      class="resource-type-radio"
                  >
                    {{ dict.label }}
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="父级菜单" prop="parentId">
                <el-tree-select
                    clearable
                    v-model="formData.parentId"
                    :data="dataOptions"
                    :props="defaultProps"
                    check-strictly
                    value-key="id"
                    placeholder="请选择父级菜单"
                    class="tree-select"
                />
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="权限标识">
                <el-input
                    v-model="formData.permission"
                    placeholder="请输入权限标识"
                    clearable
                />
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="状态" prop="status">
                <el-switch
                    v-model="formData.status"
                    active-value="1"
                    inactive-value="0"
                    active-text="启用"
                    inactive-text="禁用"
                />
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item :label="$t('jbx.text.sortIndex')" prop="sortIndex">
                <el-input
                    v-model="formData.sortIndex"
                    placeholder="请输入排序值"
                    type="number"
                    min="0"
                />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
              <!-- 描述信息 -->
              <el-form-item label="描述" prop="description">
                <el-input
                    v-model="formData.description"
                    type="textarea"
                    :rows="1"
                    placeholder="请输入资源描述"
                    maxlength="500"
                    show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 菜单专属配置 -->
        <el-card
            v-if="formData.classify === 'menu'"
            class="form-card"
            shadow="never"
        >
          <template #header>
            <div class="card-header">
              <svg-icon icon-class="menu" class="card-icon"/>
              <span class="card-title">配置信息</span>
            </div>
          </template>

          <el-row :gutter="24">
            <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
              <el-form-item label="外部链接">
                <el-switch
                    v-model="formData.isFrame"
                    active-value="y"
                    inactive-value="n"
                    active-text="是"
                    inactive-text="否"
                />
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
              <el-form-item label="是否缓存">
                <el-switch
                    v-model="formData.isCache"
                    active-value="y"
                    inactive-value="n"
                    active-text="是"
                    inactive-text="否"
                />
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
              <el-form-item label="是否可见">
                <el-switch
                    v-model="formData.isVisible"
                    active-value="y"
                    inactive-value="n"
                    active-text="是"
                    inactive-text="否"
                />
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="资源样式">
                <div class="icon-select-wrapper">
                  <svg-icon style="margin-top: 18px;" :icon-class="formData.resStyle"></svg-icon>
                  <icon-select
                      v-model="formData.resStyle"
                      @selected="(name) => {formData.resStyle = name}"
                      class="icon-select-btn"
                  />
                </div>
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="请求参数">
                <el-input
                    v-model="formData.params"
                    placeholder="请输入请求参数"
                    clearable
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- API配置 -->
        <el-card
            v-if="formData.classify === 'api' || formData.classify === 'openApi' || formData.classify === 'proxy'"
            class="form-card"
            shadow="never"
        >
          <template #header>
            <div class="card-header">
              <svg-icon icon-class="api" class="card-icon"/>
              <span class="card-title">API配置</span>
            </div>
          </template>

          <el-row :gutter="24">
            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="请求地址" prop="path">
                <el-input
                    v-model="formData.path"
                    placeholder="请输入资源路径，如：/users"
                    clearable
                >
                  <template #prepend>{{ props.contextPath }}</template>
                </el-input>
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12"
                    v-if="formData.classify === 'api' || formData.classify === 'openApi' || formData.classify === 'proxy'">
              <el-form-item label="请求方式" prop="method">
                <el-select
                    v-model="formData.method"
                    placeholder="请选择请求方式"
                    clearable
                    class="full-width"
                >
                  <el-option
                      v-for="dict in method_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" v-if="formData.classify === 'api'">
              <el-form-item label="请求参数">
                <el-input
                    v-model="formData.params"
                    placeholder="请输入请求参数"
                    clearable
                />
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" v-if="formData.classify === 'openApi'">
              <el-form-item label="数据源" prop="datasourceId">
                <el-select
                    v-model="formData.datasourceId"
                    placeholder="请选择数据源"
                    :loading="dataSourceLoading"
                    clearable
                    class="full-width"
                >
                  <el-option
                      v-for="ds in dataSourceList"
                      :key="ds.id"
                      :label="ds.name"
                      :value="ds.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" v-if="formData.classify === 'openApi'">
              <el-form-item label="是否开放">
                <el-switch
                    v-model="formData.isOpen"
                    active-value="y"
                    inactive-value="n"
                    active-text="是"
                    inactive-text="否"
                />
              </el-form-item>
            </el-col>

            <!-- API代理专属 -->
            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" v-if="formData.classify === 'proxy'">
              <el-form-item label="认证方式" prop="datasourceId">
                <div style="display: flex;justify-content: space-between;width: 100%">
                  <el-select
                      style="flex: 1"
                      v-model="formData.datasourceId"
                      placeholder="请选择认证方式"
                      clearable
                      class="full-width"
                  >
                    <el-option
                        v-for="dict in proxySourceList"
                        :key="dict.id"
                        :label="dict.alias"
                        :value="dict.id"
                    />

                  </el-select>
                  <el-button @click="drawerVisible = true" style="width:66px">管理</el-button>
                </div>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12" v-if="formData.classify === 'proxy'">
              <el-form-item label="代理地址" prop="params">
                <el-input
                    v-model="formData.params"
                    placeholder="请输入完整的URL"
                    clearable
                />
              </el-form-item>
            </el-col>

          </el-row>
        </el-card>

        <!-- 按钮专属配置 -->
        <el-card
            v-if="formData.classify === 'button'"
            class="form-card"
            shadow="never"
        >
          <template #header>
            <div class="card-header">
              <svg-icon icon-class="button" class="card-icon"/>
              <span class="card-title">按钮配置</span>
            </div>
          </template>

          <el-row :gutter="24">
            <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
              <el-form-item label="操作类型">
                <el-select
                    v-model="formData.actionType"
                    placeholder="请选择操作类型"
                    clearable
                    class="full-width"
                >
                  <el-option
                      v-for="dict in action_type"
                      :key="dict.value"
                      :label="dict.label"
                      :value="dict.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>

    <el-drawer
        :title="drawerTitle"
        v-model="drawerVisible"
        size="1200px"
        :before-close="handleDrawerClose"
        append-to-body
        destroy-on-close
        @close="handleUpdateProxy"
    >
      <template #header>
        <h4>{{ drawerTitle }}</h4>
      </template>
      <proxyRules :app-id="appId"></proxyRules>
    </el-drawer>
  </div>
</template>

<script setup>
import {ref, reactive, computed, toRefs, defineProps, defineEmits, onMounted} from 'vue'
import * as appResourcesApi from '@/api/app/resources'
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import * as proxy from "@/utils/Dict";
import IconSelect from "@/components/IconSelect/index.vue";
import SvgIcon from "@/components/SvgIcon/index.vue";
import proxyRules from "@/views/app/app-manage/proxy-rules.vue"


const {
  resources_type,
  action_type,
  method_type
} = proxy.useDict("resources_type", "action_type", "method_type");
const {t} = useI18n()

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  formData: {
    type: Object,
    default: () => ({})
  },
  dataOptions: {
    type: Array,
    default: () => []
  },
  dataSourceList: {
    type: Array,
    default: () => []
  },
  proxySourceList: {
    type: Array,
    default: () => []
  },
  appId: {
    type: String,
    default: 'appId'
  },
  contextPath: {
    type: String,
    default: 'contextPath'
  }
});

const contextPath = ref("");

const emit = defineEmits(['update:visible', 'update:formData', 'success', 'close', 'updateProxySource']);

// 响应式数据
const dataSourceLoading = ref(false)
const formRef = ref()
const submitting = ref(false)

const defaultProps = ref({
  children: "children",
  label: "name"
});

// 表单验证规则
const formRules = computed(() => {
  const res = {
    name: [
      {required: true, message: '请输入API名称', trigger: 'blur'}
    ],
    path: [
      {required: true, message: '请输入API路径', trigger: 'blur'}
    ],
    method: [
      {required: true, message: '请选择HTTP方法', trigger: 'change'}
    ],
    datasourceId: [
      {required: true, message: '请选择数据源', trigger: 'change'}
    ]
  }
  if (props.formData.classify === 'proxy') {
    res.datasourceId = [
      {required: true, message: '请选择代理协议', trigger: 'change'}
    ]
    res['params'] = [
      {required: true, message: '请输入代理地址', trigger: 'change'}
    ]
  }
  return res
})
const drawerTitle = ref('代理方法管理');
const drawerVisible = ref(false);
// 计算属性
const dialogTitle = computed(() => props.isEdit ? '编辑资源' : '新增资源')
const dialogVisible = computed({
  get() {
    return props.visible
  },
  set(value) {

    emit('update:visible', value)
  }
})

// 方法
const handleDialogClose = () => {
  emit('close')
}
const handleDrawerClose = () => {
  drawerVisible.value = false
};
const handleUpdateProxy = () => {
  emit('updateProxySource')
};
const handleSubmit = async () => {
  const handleResponse = (res, successMessage) => {
    if (res.code === 0) {
      emit('success')
      modal.msgSuccess("操作成功");
    } else {
      modal.msgError(res.message);
    }
    submitting.value = false
  };

  formRef?.value?.validate((valid) => {
    if (valid) {
      submitting.value = true
      const formDataWithAppId = {
        ...props.formData,
        appId: props.appId
      };

      const operation = props.isEdit ? appResourcesApi.update : appResourcesApi.create;
      const successMessage = props.isEdit
          ? t('org.success.update')
          : t('org.success.add');
      operation(formDataWithAppId).then((res) => handleResponse(res, successMessage));
    }
  });
}

</script>

<style scoped lang="scss">
.resource-form {
  max-height: 70vh;
  overflow-y: auto;
  padding-right: 8px;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  &::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;

    &:hover {
      background: #a8a8a8;
    }
  }
}

.form-card {
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  transition: all 0.3s ease;

  &:hover {
    border-color: #409eff;
    box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.1);
  }

  :deep(.el-card__header) {
    padding: 16px 20px;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-bottom: 1px solid #e4e7ed;
    border-radius: 8px 8px 0 0;
  }

  :deep(.el-card__body) {
    padding: 20px;
  }
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;

  .card-icon {
    width: 18px;
    height: 18px;
    color: #409eff;
  }

  .card-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
  }
}

.resource-type-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;

  .resource-type-radio {
    margin-right: 0;

    :deep(.el-radio__label) {
      font-size: 14px;
    }
  }
}

.icon-select-wrapper {
  display: flex;
  justify-content: space-between;
  width: 100%;
  align-items: flex-start;

  .icon-input {
    flex: 1;
  }

  .icon-select-btn {
    flex-shrink: 0;
  }
}

.tree-select {
  width: 100%;
}

.full-width {
  width: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
  margin-top: 10px;

  .el-button {
    min-width: 80px;
    border-radius: 6px;
    padding: 10px 24px;
    font-weight: 500;

    &:first-child {
      border-color: #dcdfe6;
      color: #606266;

      &:hover {
        border-color: #c0c4cc;
        background-color: #f5f7fa;
      }
    }

    &:last-child {
      background: linear-gradient(135deg, #409eff 0%, #337ecc 100%);
      border: none;

      &:hover {
        background: linear-gradient(135deg, #66b1ff 0%, #409eff 100%);
        box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
      }
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  :deep(.el-dialog) {
    width: 90% !important;
    max-width: 900px;
  }
}

@media (max-width: 768px) {
  .resource-form {
    max-height: 60vh;
  }

  .form-card {
    margin-bottom: 16px;

    :deep(.el-card__header) {
      padding: 12px 16px;
    }

    :deep(.el-card__body) {
      padding: 16px;
    }
  }

  .card-header {
    .card-title {
      font-size: 15px;
    }
  }

  .el-col {
    margin-bottom: 8px;
  }

  .resource-type-group {
    gap: 8px;

    .resource-type-radio {
      :deep(.el-radio__label) {
        font-size: 13px;
      }
    }
  }

  .dialog-footer {
    flex-direction: column;
    gap: 8px;

    .el-button {
      width: 100%;
    }
  }
}

// 表单项美化
:deep(.el-form-item) {
  margin-bottom: 20px;

  .el-form-item__label {
    font-weight: 500;
    color: #606266;
    padding-right: 20px;
  }

  .el-input,
  .el-select,
  .el-textarea {
    .el-input__wrapper {
      border-radius: 6px;
      transition: all 0.3s ease;

      &:hover {
        border-color: #c0c4cc;
      }

      &.is-focus {
        border-color: #409eff;
        box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
      }
    }
  }

  .el-switch {
    height: 32px;

    .el-switch__core {
      border-radius: 16px;
    }

    .el-switch__label {
      font-weight: 500;

      &.is-active {
        color: #409eff;
      }
    }
  }
}

// 动画效果
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.form-card {
  animation: fadeIn 0.3s ease-out;

  &:nth-child(2) {
    animation-delay: 0.1s;
  }

  &:nth-child(3) {
    animation-delay: 0.2s;
  }

  &:nth-child(4) {
    animation-delay: 0.3s;
  }

  &:nth-child(5) {
    animation-delay: 0.4s;
  }
}
</style>