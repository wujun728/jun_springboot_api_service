<template>
  <div class="workflow-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">工作流管理</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="goToWorkflowDesigner">
          <el-icon class="el-icon--left"><Plus /></el-icon>
          创建工作流
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
          placeholder="搜索工作流名称..."
          prefix-icon="Search"
          style="width: 300px"
          clearable
          @input="handleSearch"
        />
        <el-select v-model="statusFilter" placeholder="状态筛选" style="width: 150px" @change="handleSearch">
          <el-option label="全部" :value="null" />
          <el-option label="激活" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
        <el-select v-model="businessTypeFilter" placeholder="业务类型" style="width: 150px" @change="handleSearch">
          <el-option label="全部" :value="null" />
          <el-option label="系统表单" :value="1" />
          <el-option label="业务表单" :value="2" />
        </el-select>
      </div>
      <div class="search-right">
        <el-button @click="handleRefresh">
          <el-icon class="el-icon--left"><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <!-- 工作流列表 -->
    <div class="workflow-content">
      <el-table
        v-loading="loading"
        :data="workflowList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="流程名称" min-width="150">
          <template #default="{ row }">
            <div class="workflow-name">
              <span class="name">{{ row.name }}</span>
              <span class="description" v-if="row.description">{{ row.description }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="key" label="流程标识" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="businessType" label="业务类型" width="120">
          <template #default="{ row }">
            <el-tag :type="row.businessType === 1 ? 'primary' : 'success'">
              {{ row.businessType === 1 ? '系统表单' : (row.businessType === 2 ? '业务表单' : '未知') }}
            </el-tag>
          </template>
        </el-table-column>
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
        <el-table-column label="操作" width="300" fixed="right" align="center">
          <template #default="{ row }">
            <div class="table-action-group">
              <el-button size="small" type="primary" link @click="handleView(row)">
                <el-icon class="el-icon--left"><View /></el-icon>
                查看
              </el-button>
              <el-button size="small" type="primary" link @click="handleEdit(row)">
                <el-icon class="el-icon--left"><Edit /></el-icon>
                编辑
              </el-button>
              <el-button
                size="small"
                :type="row.status === 1 ? 'warning' : 'success'"
                link
                @click="handleToggleStatus(row)"
              >
                <el-icon class="el-icon--left"><Switch /></el-icon>
                {{ row.status === 1 ? '禁用' : '激活' }}
              </el-button>
              <el-button size="small" type="danger" link @click="handleDelete(row)">
                <el-icon class="el-icon--left"><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

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

    <!-- 创建工作流抽屉 -->
    <el-drawer
      v-model="showCreateDrawer"
      title="创建工作流"
      direction="rtl"
      size="60%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="create-workflow-form">
        <el-form
          ref="createFormRef"
          :model="createForm"
          :rules="createFormRules"
          label-width="100px"
        >
          <el-form-item label="工作流类型" prop="businessType">
            <el-radio-group v-model="createForm.businessType">
              <el-radio :label="1">系统表单</el-radio>
              <el-radio :label="2">业务表单</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item
            v-if="createForm.businessType === 1"
            label="表单标识"
            prop="systemFormKey"
          >
            <el-input
              v-model="createForm.systemFormKey"
              placeholder="请输入系统表单标识"
              clearable
            />
            <div class="form-help-text">
              用于标识系统内置的表单类型，如：leave_form、purchase_form
            </div>
          </el-form-item>

          <el-form-item
            v-if="createForm.businessType === 2"
            label="业务表单"
            prop="businessFormId"
          >
            <el-select
              v-model="createForm.businessFormId"
              placeholder="请选择业务表单"
              style="width: 100%"
              filterable
              :loading="formListLoading"
              @change="handleBusinessFormChange"
            >
              <el-option
                v-for="form in businessFormList"
                :key="form.id"
                :label="form.formName"
                :value="form.id"
                :disabled="boundFormIds.includes(form.id)"
              >
                <span>{{ form.formName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">
                  {{ form.formDescription || '无描述' }}
                </span>
              </el-option>
            </el-select>
            <div class="form-help-text">
              选择已创建的业务表单作为流程的数据载体
            </div>
          </el-form-item>

          <!-- 表单视图配置 -->
          <el-form-item
            label="表单视图"
            class="business-view-form-item"
          >
            <div class="business-view-config">
              <div class="config-header">
                <span class="config-title">
                  {{ createForm.businessType === 1 ? '视图地址配置' : '表单配置' }}
                </span>
                <el-tag :type="createForm.businessType === 1 ? 'primary' : 'success'" size="small">
                  {{ createForm.businessType === 1 ? '手动配置' : '自动带入' }}
                </el-tag>
                 <el-button
                      type="primary"
                      size="small"
                      :icon="Refresh"
                      @click="refreshEditBusinessView"
                      :loading="refreshingEditForm"
                    >
                      刷新配置
                  </el-button>
              </div>

              <!-- 系统表单：手动输入视图地址 -->
              <div v-if="createForm.businessType === 1" class="system-view-config">
                <el-input
                  v-model="createForm.businessView"
                  placeholder="请输入视图地址，如：/views/system/leave-form"
                  clearable
                >
                  <template #prepend>视图地址</template>
                </el-input>
                <div class="form-help-text">
                  配置系统表单的视图路径，用于在任务中心展示业务数据
                </div>
              </div>

              <!-- 业务表单：显示自动带入的formConfig -->
              <div v-else class="business-form-config">
                <el-alert
                  v-if="!createForm.businessFormId"
                  title="请先选择业务表单"
                  type="info"
                  :closable="false"
                  show-icon
                />
                <div v-else-if="!createForm.businessView" class="loading-config">
                  <el-icon class="is-loading"><Loading /></el-icon>
                  <span>正在加载表单配置...</span>
                </div>
                <div v-else class="config-preview">
                  <div class="preview-header">
                    <div class="header-left">
                      <el-icon><Document /></el-icon>
                      <span>表单配置已自动加载</span>
                      <el-tag type="success" size="small">{{ getFormConfigComponentCount() }} 个组件</el-tag>
                    </div>

                  </div>
                  <el-input
                    :model-value="formatBusinessView()"
                    type="textarea"
                    :rows="6"
                    readonly
                    placeholder="表单配置JSON"
                  />
                </div>
                <div class="form-help-text">
                  选择业务表单后，会自动加载表单的 formConfig 配置，用于在任务中心以表单形式展示业务数据
                </div>
              </div>
            </div>
          </el-form-item>

          <!-- 流程变量管理 - 系统表单和业务表单都显示 -->
          <el-form-item
            label="流程变量"
            class="variables-form-item"
          >
            <div class="variables-management">
              <div class="variables-header">
                <span class="variables-title">
                  {{ createForm.businessType === 1 ? '变量定义' : '表单字段变量' }}
                </span>
                <el-button
                  v-if="createForm.businessType === 1"
                  type="primary"
                  size="small"
                  @click="addVariable"
                  :icon="'Plus'"
                >
                  添加变量
                </el-button>
                <el-tag v-else type="info" size="small">
                  自动从表单带出
                </el-tag>
              </div>

              <!-- 表头 - 始终显示 -->
              <div class="variables-table-header">
                <div class="header-cell name-col">变量名称 *</div>
                <div class="header-cell type-col">数据类型</div>
                <div class="header-cell label-col">显示标签</div>
                <div class="header-cell required-col">必填</div>
                <div class="header-cell action-col">操作</div>
              </div>

              <div v-if="createForm.variables.length === 0" class="empty-variables">
                <el-empty
                  description="暂无变量定义"
                  :image-size="60"
                />
              </div>

              <div v-else class="variables-list">

                <!-- 变量列表 -->
                <div class="variables-table-body">
                  <div
                    v-for="(variable, index) in createForm.variables"
                    :key="index"
                    class="variable-row"
                    :class="{
                      'has-error': variable.nameError,
                      'auto-generated': createForm.businessType === 2
                    }"
                  >
                    <div class="row-content">
                      <div class="cell name-col">
                        <el-input
                          v-model="variable.name"
                          placeholder="如：userName"
                          @blur="validateVariableName(variable, index)"
                          :class="{ 'is-error': variable.nameError }"
                          :disabled="createForm.businessType === 2"
                        />
                        <div v-if="variable.nameError" class="field-error">
                          <el-icon><WarningFilled /></el-icon>
                          {{ variable.nameError }}
                        </div>
                      </div>

                      <div class="cell type-col">
                        <el-select
                          v-model="variable.type"
                          placeholder="选择类型"
                          style="width: 100%"
                          :disabled="createForm.businessType === 2"
                        >
                          <el-option label="字符串" value="string">
                            <span>字符串</span>
                            <span class="option-desc">文本数据</span>
                          </el-option>
                          <el-option label="数值" value="number">
                            <span>数值</span>
                            <span class="option-desc">整数或小数</span>
                          </el-option>
                          <el-option label="布尔" value="boolean">
                            <span>布尔</span>
                            <span class="option-desc">是/否</span>
                          </el-option>
                          <el-option label="日期" value="date">
                            <span>日期</span>
                            <span class="option-desc">时间数据</span>
                          </el-option>
                          <el-option label="对象" value="object">
                            <span>对象</span>
                            <span class="option-desc">JSON格式</span>
                          </el-option>
                          <el-option label="人员选择" value="user-select">
                            <span>人员选择</span>
                            <span class="option-desc">选择用户</span>
                          </el-option>
                          <el-option label="部门选择" value="department-select">
                            <span>部门选择</span>
                            <span class="option-desc">选择部门</span>
                          </el-option>
                          <el-option label="角色选择" value="role-select">
                            <span>角色选择</span>
                            <span class="option-desc">选择角色</span>
                          </el-option>
                        </el-select>
                      </div>

                      <div class="cell label-col">
                        <el-input
                          v-model="variable.label"
                          placeholder="如：用户名称"
                          :disabled="createForm.businessType === 2"
                        />
                      </div>

                      <div class="cell required-col">
                        <el-switch
                          v-model="variable.required"
                          active-text="是"
                          inactive-text="否"
                          inline-prompt
                          :disabled="createForm.businessType === 2"
                        />
                      </div>

                      <div class="cell action-col">
                        <el-tooltip content="删除变量" placement="top">
                          <el-button
                            type="danger"
                            text
                            @click="removeVariable(index)"
                            :icon="'Delete'"
                            :disabled="createForm.businessType === 2"
                          />
                        </el-tooltip>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="form-help-text">
              <span v-if="createForm.businessType === 1">
                定义流程中使用的变量，用于流程流转时的数据传递和条件判断
              </span>
              <span v-else>
                显示业务表单的字段信息，这些字段将作为流程变量使用。选择业务表单后会自动带出字段信息。
              </span>
            </div>
          </el-form-item>
        </el-form>

        <div class="drawer-footer">
          <el-button @click="closeCreateDrawer">取消</el-button>
          <el-button type="primary" @click="confirmCreateWorkflow" :loading="creating">
            {{ creating ? '跳转中...' : '进入设计器' }}
          </el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 编辑工作流抽屉 -->
    <el-drawer
      v-model="showEditDrawer"
      title="编辑工作流"
      direction="rtl"
      size="60%"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <div class="create-workflow-form">
        <el-form
          ref="editFormRef"
          :model="editForm"
          :rules="editFormRules"
          label-width="100px"
        >
          <el-form-item label="流程名称" prop="name">
            <el-input
              v-model="editForm.name"
              placeholder="请输入流程名称"
              clearable
            />
          </el-form-item>

          <el-form-item label="流程标识" prop="key">
            <el-input
              v-model="editForm.key"
              placeholder="请输入流程标识"
              clearable
            />
            <div class="form-help-text">
              流程的唯一标识符，如：leave_approval、purchase_approval
            </div>
          </el-form-item>

          <el-form-item label="流程描述">
            <el-input
              v-model="editForm.description"
              type="textarea"
              :rows="3"
              placeholder="请输入流程描述（可选）"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>

          <el-form-item label="工作流类型" prop="businessType">
            <el-radio-group v-model="editForm.businessType">
              <el-radio :label="1">系统表单</el-radio>
              <el-radio :label="2">业务表单</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item
            v-if="editForm.businessType === 1"
            label="表单标识"
            prop="systemFormKey"
          >
            <el-input
              v-model="editForm.systemFormKey"
              placeholder="请输入系统表单标识"
              clearable
            />
            <div class="form-help-text">
              用于标识系统内置的表单类型，如：leave_form、purchase_form
            </div>
          </el-form-item>

          <el-form-item
            v-if="editForm.businessType === 2"
            label="业务表单"
            prop="businessFormId"
          >
            <el-select
              v-model="editForm.businessFormId"
              placeholder="请选择业务表单"
              style="width: 100%"
              filterable
              :loading="formListLoading"
              @change="handleEditBusinessFormChange"
            >
              <el-option
                v-for="form in businessFormList"
                :key="form.id"
                :label="form.formName"
                :value="form.id"
                :disabled="boundFormIds.includes(form.id) && form.id !== currentEditWorkflow?.businessFormId"
              >
                <span>{{ form.formName }}</span>
                <span style="float: right; color: #8492a6; font-size: 13px">
                  {{ form.formDescription || '无描述' }}
                </span>
              </el-option>
            </el-select>
            <div class="form-help-text">
              选择已创建的业务表单作为流程的数据载体
            </div>
          </el-form-item>

          <!-- 表单视图配置 -->
          <el-form-item
            label="表单视图"
            class="business-view-form-item"
          >
            <div class="business-view-config">
              <div class="config-header">
                <span class="config-title">
                  {{ editForm.businessType === 1 ? '视图地址配置' : '表单配置' }}
                </span>
                <el-tag :type="editForm.businessType === 1 ? 'primary' : 'success'" size="small">
                  {{ editForm.businessType === 1 ? '手动配置' : '自动带入' }}
                </el-tag>
                 <el-button
                 v-if="editForm.businessType === 2"
                      type="primary"
                      size="small"
                      :icon="Refresh"
                      @click="refreshEditBusinessView"
                      :loading="refreshingEditForm"
                    >
                      刷新配置
                    </el-button>
              </div>

              <!-- 系统表单：手动输入视图地址 -->
              <div v-if="editForm.businessType === 1" class="system-view-config">
                <el-input
                  v-model="editForm.businessView"
                  placeholder="请输入视图地址，如：/views/system/leave-form"
                  clearable
                >
                  <template #prepend>视图地址</template>
                </el-input>
                <div class="form-help-text">
                  配置系统表单的视图路径，用于在任务中心展示业务数据
                </div>
              </div>

              <!-- 业务表单：显示自动带入的formConfig -->
              <div v-else class="business-form-config">
                <el-alert
                  v-if="!editForm.businessFormId"
                  title="请先选择业务表单"
                  type="info"
                  :closable="false"
                  show-icon
                />
                <div v-else-if="editForm.businessView" class="config-preview">
                  <div class="preview-header">
                    <div class="header-left">
                      <el-icon><Document /></el-icon>
                      <span>表单配置已加载</span>
                      <el-tag type="success" size="small">{{ getEditFormConfigComponentCount() }} 个组件</el-tag>
                    </div>
                  </div>
                  <el-input
                    :model-value="formatEditBusinessView()"
                    type="textarea"
                    :rows="6"
                    readonly
                    placeholder="表单配置JSON"
                  />
                </div>
                <el-alert
                  v-else
                  title="该业务表单暂无配置信息"
                  type="warning"
                  :closable="false"
                  show-icon
                />
                <div class="form-help-text">
                  业务表单的 formConfig 配置，用于在任务中心以表单形式展示业务数据
                </div>
              </div>
            </div>
          </el-form-item>

          <!-- 流程变量管理 - 系统表单和业务表单都显示 -->
          <el-form-item
            label="流程变量"
            class="variables-form-item"
          >
            <div class="variables-management">
              <div class="variables-header">
                <span class="variables-title">
                  {{ editForm.businessType === 1 ? '变量定义' : '表单字段变量' }}
                </span>
                <el-button
                  v-if="editForm.businessType === 1"
                  type="primary"
                  size="small"
                  @click="addEditVariable"
                  :icon="'Plus'"
                >
                  添加变量
                </el-button>
                <el-tag v-else type="info" size="small">
                  自动从表单带出
                </el-tag>
              </div>

              <!-- 表头 - 始终显示 -->
              <div class="variables-table-header">
                <div class="header-cell name-col">变量名称 *</div>
                <div class="header-cell type-col">数据类型</div>
                <div class="header-cell label-col">显示标签</div>
                <div class="header-cell required-col">必填</div>
                <div class="header-cell action-col">操作</div>
              </div>

              <div v-if="editForm.variables.length === 0" class="empty-variables">
                <el-empty
                  description="暂无变量定义"
                  :image-size="60"
                />
              </div>

              <div v-else class="variables-list">

                <!-- 变量列表 -->
                <div class="variables-table-body">
                  <div
                    v-for="(variable, index) in editForm.variables"
                    :key="index"
                    class="variable-row"
                    :class="{
                      'has-error': variable.nameError,
                      'auto-generated': editForm.businessType === 2
                    }"
                  >
                    <div class="row-content">
                      <div class="cell name-col">
                        <el-input
                          v-model="variable.name"
                          placeholder="如：userName"
                          @blur="validateEditVariableName(variable, index)"
                          :class="{ 'is-error': variable.nameError }"
                          :disabled="editForm.businessType === 2"
                        />
                        <div v-if="variable.nameError" class="field-error">
                          <el-icon><WarningFilled /></el-icon>
                          {{ variable.nameError }}
                        </div>
                      </div>

                      <div class="cell type-col">
                        <el-select
                          v-model="variable.type"
                          placeholder="选择类型"
                          style="width: 100%"
                          :disabled="editForm.businessType === 2"
                        >
                          <el-option label="字符串" value="string">
                            <span>字符串</span>
                            <span class="option-desc">文本数据</span>
                          </el-option>
                          <el-option label="数值" value="number">
                            <span>数值</span>
                            <span class="option-desc">整数或小数</span>
                          </el-option>
                          <el-option label="布尔" value="boolean">
                            <span>布尔</span>
                            <span class="option-desc">是/否</span>
                          </el-option>
                          <el-option label="日期" value="date">
                            <span>日期</span>
                            <span class="option-desc">时间数据</span>
                          </el-option>
                          <el-option label="对象" value="object">
                            <span>对象</span>
                            <span class="option-desc">JSON格式</span>
                          </el-option>
                          <el-option label="人员选择" value="user-select">
                            <span>人员选择</span>
                            <span class="option-desc">选择用户</span>
                          </el-option>
                          <el-option label="部门选择" value="department-select">
                            <span>部门选择</span>
                            <span class="option-desc">选择部门</span>
                          </el-option>
                          <el-option label="角色选择" value="role-select">
                            <span>角色选择</span>
                            <span class="option-desc">选择角色</span>
                          </el-option>
                        </el-select>
                      </div>

                      <div class="cell label-col">
                        <el-input
                          v-model="variable.label"
                          placeholder="如：用户名称"
                          :disabled="editForm.businessType === 2"
                        />
                      </div>

                      <div class="cell required-col">
                        <el-switch
                          v-model="variable.required"
                          active-text="是"
                          inactive-text="否"
                          inline-prompt
                          :disabled="editForm.businessType === 2"
                        />
                      </div>

                      <div class="cell action-col">
                        <el-tooltip content="删除变量" placement="top">
                          <el-button
                            type="danger"
                            text
                            @click="removeEditVariable(index)"
                            :icon="'Delete'"
                            :disabled="editForm.businessType === 2"
                          />
                        </el-tooltip>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="form-help-text">
              <span v-if="editForm.businessType === 1">
                定义流程中使用的变量，用于流程流转时的数据传递和条件判断
              </span>
              <span v-else>
                显示业务表单的字段信息，这些字段将作为流程变量使用。选择业务表单后会自动带出字段信息。
              </span>
            </div>
          </el-form-item>
        </el-form>

        <div class="drawer-footer">
          <el-button @click="closeEditDrawer">取消</el-button>
          <el-button type="primary" @click="confirmUpdateWorkflow" :loading="editing">
            {{ editing ? '跳转中...' : '进入设计器' }}
          </el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 查看工作流详情对话框 -->
    <el-dialog
      v-model="showViewDialog"
      title="工作流详情"
      width="900px"
      top="5vh"
    >
      <div v-if="currentWorkflow" class="workflow-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="流程名称">{{ currentWorkflow.name }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(currentWorkflow.status)">
              {{ getStatusText(currentWorkflow.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="版本">v{{ currentWorkflow.version }}</el-descriptions-item>
          <el-descriptions-item label="流程标识">{{ currentWorkflow.key }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDateTime(currentWorkflow.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ formatDateTime(currentWorkflow.updateTime) }}</el-descriptions-item>
          <el-descriptions-item label="业务类型" :span="2">
            <el-tag :type="currentWorkflow.businessType === 1 ? 'primary' : 'success'">
              {{ currentWorkflow.businessType === 1 ? '系统表单' : (currentWorkflow.businessType === 2 ? '业务表单' : '未知') }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="流程描述" :span="2">{{ currentWorkflow.description }}</el-descriptions-item>
        </el-descriptions>
        <div class="config-section">
          <h4>流程定义JSON</h4>
          <div class="workflow-config-container">
            <el-input
              :model-value="currentWorkflow.jsonDef"
              type="textarea"
              :rows="15"
              readonly
            />
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download, Search, Refresh, View, Edit, Switch, Delete, Setting, WarningFilled, Loading, Document } from '@element-plus/icons-vue'
import { workflowApi, formApi } from '@/api'

// 类型定义
interface VariableItem {
  name: string
  type: string
  label: string
  required: boolean
  nameError?: string
}

// 响应式数据
const loading = ref(false)
const workflowList = ref<any[]>([])
const selectedWorkflows = ref<any[]>([])
const showViewDialog = ref(false)
const currentWorkflow = ref<any>(null)
const searchQuery = ref('')
const statusFilter = ref<number | null>(null)
const businessTypeFilter = ref<number | null>(null)

// 创建工作流相关
const showCreateDrawer = ref(false)
const creating = ref(false)
const createFormRef = ref()

// 编辑工作流相关
const showEditDrawer = ref(false)
const editing = ref(false)
const editFormRef = ref()
const currentEditWorkflow = ref(null)
const createForm = reactive<{
  businessType: number
  systemFormKey: string
  businessFormId: string
  businessView: string
  variables: VariableItem[]
}>({
  businessType: 1, // 默认系统表单
  systemFormKey: '',
  businessFormId: '',
  businessView: '', // 表单视图配置
  variables: [] // 流程变量列表
})

// 编辑表单数据
const editForm = reactive<{
  id: string
  name: string
  key: string
  description: string
  businessType: number
  systemFormKey: string
  businessFormId: string
  businessView: string
  variables: VariableItem[]
}>({
  id: '',
  name: '',
  key: '',
  description: '',
  businessType: 1,
  systemFormKey: '',
  businessFormId: '',
  businessView: '', // 表单视图配置
  variables: []
})

// 表单验证规则
const createFormRules = {
  businessType: [
    { required: true, message: '请选择工作流类型', trigger: 'change' }
  ],
  systemFormKey: [
    { required: true, message: '请输入系统表单标识', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  businessFormId: [
    { required: true, message: '请选择业务表单', trigger: 'change' }
  ]
}

// 编辑表单验证规则
const editFormRules = {
  name: [
    { required: true, message: '请输入流程名称', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  key: [
    { required: true, message: '请输入流程标识', trigger: 'blur' },
    { pattern: /^[a-zA-Z][a-zA-Z0-9_]*$/, message: '流程标识必须以字母开头，只能包含字母、数字、下划线', trigger: 'blur' }
  ],
  businessType: [
    { required: true, message: '请选择工作流类型', trigger: 'change' }
  ],
  systemFormKey: [
    { required: true, message: '请输入系统表单标识', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  businessFormId: [
    { required: true, message: '请选择业务表单', trigger: 'change' }
  ]
}

// 业务表单列表
const businessFormList = ref<any[]>([])
const boundFormIds = ref<string[]>([])
const formListLoading = ref(false)

// 刷新业务视图的加载状态
const refreshingCreateForm = ref(false)
const refreshingEditForm = ref(false)

// 分页数据
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 获取工作流列表
const getWorkflowList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.current,
      pageSize: pagination.size,
      name: searchQuery.value || undefined,
      status: statusFilter.value,
      businessType: businessTypeFilter.value
    }
    const response = await workflowApi.getWorkflowList(params)

    // 处理不同的响应数据结构
    if (response.data && response.data.records) {
      // 如果响应被包装了一层 data
      workflowList.value = response.data.records
      pagination.total = Number(response.data.total) || 0
    } else if (response.records) {
      // 如果直接返回分页对象
      workflowList.value = response.records
      pagination.total = Number(response.total) || 0
    } else {
      // 兜底处理
      console.warn('未知的响应数据结构:', response)
      workflowList.value = []
      pagination.total = 0
    }
  } catch (error) {
    console.error('获取工作流列表失败:', error)
    ElMessage.error('获取工作流列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索处理
const handleSearch = () => {
  pagination.current = 1
  getWorkflowList()
}

// 刷新
const handleRefresh = () => {
  searchQuery.value = ''
  statusFilter.value = null
  businessTypeFilter.value = null
  pagination.current = 1
  getWorkflowList()
}

// 打开创建工作流抽屉
const goToWorkflowDesigner = () => {
  showCreateDrawer.value = true
  // 加载业务表单列表
  loadBusinessFormList()
}

// 加载业务表单列表
const loadBusinessFormList = async () => {
  formListLoading.value = true
  try {
    const [formResponse, boundResponse] = await Promise.all([
      formApi.getFormList({
        current: 1,
        size: 1000, // 获取所有启用的表单
        status: 1 // 只获取启用状态的表单
      }),
      workflowApi.getBoundFormIds()
    ])

    if (formResponse.data && formResponse.data.records) {
      businessFormList.value = formResponse.data.records
    } else if (formResponse.records) {
      businessFormList.value = formResponse.records
    } else {
      businessFormList.value = []
    }

    if (boundResponse.data) {
      boundFormIds.value = boundResponse.data
    }
  } catch (error) {
    console.error('加载业务表单列表失败:', error)
    ElMessage.error('加载业务表单列表失败')
    businessFormList.value = []
  } finally {
    formListLoading.value = false
  }
}

// ==================== 变量管理方法 ====================

// 添加变量
const addVariable = () => {
  createForm.variables.push({
    name: '',
    type: 'string',
    label: '',
    required: false,
    nameError: '' // 用于显示名称验证错误
  } as VariableItem)
}

// 删除变量
const removeVariable = (index) => {
  createForm.variables.splice(index, 1)
}

// 验证变量名称
const validateVariableName = (variable: VariableItem, index: number) => {
  // 清除之前的错误
  variable.nameError = ''

  if (!variable.name) {
    variable.nameError = '变量名称不能为空'
    return false
  }

  // 检查变量名称格式（字母开头，可包含字母、数字、下划线）
  const namePattern = /^[a-zA-Z][a-zA-Z0-9_]*$/
  if (!namePattern.test(variable.name)) {
    variable.nameError = '变量名称必须以字母开头，只能包含字母、数字、下划线'
    return false
  }

  // 检查变量名称是否重复
  const duplicateIndex = createForm.variables.findIndex((v, i) =>
    i !== index && v.name === variable.name
  )
  if (duplicateIndex !== -1) {
    variable.nameError = '变量名称不能重复'
    return false
  }


  return true
}

// 验证所有变量
const validateAllVariables = () => {
  let isValid = true

  createForm.variables.forEach((variable, index) => {
    if (!validateVariableName(variable, index)) {
      isValid = false
    }
  })

  return isValid
}

// 重置变量列表
const resetVariables = () => {
  createForm.variables = []
}

// 重置创建表单
const resetCreateForm = () => {
  createForm.businessType = 1
  createForm.systemFormKey = ''
  createForm.businessFormId = ''
  createForm.businessView = ''
  createForm.variables = []

  // 清除表单验证状态
  nextTick(() => {
    createFormRef.value?.clearValidate()
  })
}

// 处理业务表单选择变化 - 自动加载formConfig
const handleBusinessFormChange = async (formId: string) => {
  if (!formId) {
    createForm.businessView = ''
    createForm.variables = []
    return
  }

  try {
    // 获取表单详情
    const response = await formApi.getFormDetail(formId)
    if (response && response.data && response.data.formConfig) {
      // 将formConfig保存到businessView（formConfig已经是字符串格式）
      createForm.businessView = response.data.formConfig

      // 解析formConfig，提取组件作为变量
      try {
        const formConfig = JSON.parse(response.data.formConfig)
        console.log('解析的formConfig:', formConfig)

        if (formConfig.components && Array.isArray(formConfig.components)) {
          console.log('组件列表:', formConfig.components.map((c: any) => ({ name: c.name, type: c.type })))
          createForm.variables = formConfig.components.map((comp: any) => {
            const mappedType = mapComponentTypeToVariableType(comp.type)
            console.log(`组件 ${comp.name} 类型映射: ${comp.type} -> ${mappedType}`)
            return {
              name: comp.name || comp.id,
              type: mappedType,
              label: comp.label || comp.name,
              required: comp.required || false,
              nameError: ''
            }
          })
          console.log('最终变量列表:', createForm.variables)
        }
      } catch (e) {
        console.error('解析formConfig失败:', e)
      }
    }
  } catch (error) {
    console.error('加载表单配置失败:', error)
    ElMessage.error('加载表单配置失败')
  }
}

// 将表单组件类型映射为变量类型
const mapComponentTypeToVariableType = (componentType: string): string => {
  const typeMap: Record<string, string> = {
    'input': 'string',
    'textarea': 'string',
    'number': 'number',
    'select': 'string',
    'radio': 'string',
    'checkbox': 'string',
    'date': 'date',
    'datetime': 'date',
    'switch': 'boolean',
    'upload': 'string',
    'user-select': 'user-select',
    'department-select': 'department-select',
    'role-select': 'role-select'
  }
  return typeMap[componentType] || 'string'
}

// 格式化businessView用于显示（创建抽屉）
const formatBusinessView = (): string => {
  if (!createForm.businessView) return ''
  try {
    const config = typeof createForm.businessView === 'string'
      ? JSON.parse(createForm.businessView)
      : createForm.businessView
    return JSON.stringify(config, null, 2)
  } catch (e) {
    return createForm.businessView
  }
}

// 获取formConfig中的组件数量（创建抽屉）
const getFormConfigComponentCount = (): number => {
  if (!createForm.businessView) return 0
  try {
    const config = typeof createForm.businessView === 'string'
      ? JSON.parse(createForm.businessView)
      : createForm.businessView
    return config.components?.length || 0
  } catch (e) {
    return 0
  }
}

// 关闭创建抽屉
const closeCreateDrawer = () => {
  showCreateDrawer.value = false
  resetCreateForm()
}

// ==================== 编辑相关方法 ====================

// 为编辑表单添加变量
const addEditVariable = () => {
  editForm.variables.push({
    name: '',
    type: 'string',
    label: '',
    required: false,
    nameError: ''
  } as VariableItem)
}

// 删除编辑表单中的变量
const removeEditVariable = (index: number) => {
  editForm.variables.splice(index, 1)
}

// 验证编辑表单中的变量名称
const validateEditVariableName = (variable: VariableItem, index: number) => {
  // 使用相同的验证逻辑，但作用于editForm
  variable.nameError = ''

  if (!variable.name) {
    variable.nameError = '变量名称不能为空'
    return false
  }

  const namePattern = /^[a-zA-Z][a-zA-Z0-9_]*$/
  if (!namePattern.test(variable.name)) {
    variable.nameError = '变量名称必须以字母开头，只能包含字母、数字、下划线'
    return false
  }

  const duplicateIndex = editForm.variables.findIndex((v, i) =>
    i !== index && v.name === variable.name
  )
  if (duplicateIndex !== -1) {
    variable.nameError = '变量名称不能重复'
    return false
  }


  return true
}

// 验证编辑表单中的所有变量
const validateAllEditVariables = () => {
  let isValid = true

  editForm.variables.forEach((variable, index) => {
    if (!validateEditVariableName(variable, index)) {
      isValid = false
    }
  })

  return isValid
}

// 重置编辑表单
const resetEditForm = () => {
  editForm.id = ''
  editForm.name = ''
  editForm.key = ''
  editForm.description = ''
  editForm.businessType = 1
  editForm.systemFormKey = ''
  editForm.businessFormId = ''
  editForm.businessView = ''
  editForm.variables = []

  nextTick(() => {
    editFormRef.value?.clearValidate()
  })
}

// 刷新创建表单的业务视图配置
const refreshCreateBusinessView = async () => {
  if (!createForm.businessFormId) {
    ElMessage.warning('请先选择业务表单')
    return
  }

  refreshingCreateForm.value = true
  try {
    const response = await formApi.getFormDetail(createForm.businessFormId)
    if (response && response.data && response.data.formConfig) {
      createForm.businessView = response.data.formConfig

      // 重新解析变量
      try {
        const formConfig = JSON.parse(response.data.formConfig)
        if (formConfig.components && Array.isArray(formConfig.components)) {
          createForm.variables = formConfig.components.map((comp: any) => ({
            name: comp.name || comp.id,
            type: mapComponentTypeToVariableType(comp.type),
            label: comp.label || comp.name,
            required: comp.required || false,
            nameError: ''
          }))
        }
      } catch (e) {
        console.error('解析formConfig失败:', e)
      }

      ElMessage.success('表单配置已刷新')
    } else {
      ElMessage.warning('该表单暂无配置信息')
    }
  } catch (error) {
    console.error('刷新表单配置失败:', error)
    ElMessage.error('刷新表单配置失败')
  } finally {
    refreshingCreateForm.value = false
  }
}

// 处理业务表单选择变化 - 编辑抽屉
const handleEditBusinessFormChange = async (formId: string) => {
  if (!formId) {
    editForm.businessView = ''
    editForm.variables = []
    return
  }

  try {
    const response = await formApi.getFormDetail(formId)
    if (response && response.data && response.data.formConfig) {
      // 将formConfig保存到businessView（formConfig已经是字符串格式）
      editForm.businessView = response.data.formConfig

      // 解析formConfig，提取组件作为变量
      try {
        const formConfig = JSON.parse(response.data.formConfig)

        if (formConfig.components && Array.isArray(formConfig.components)) {
          editForm.variables = formConfig.components.map((comp: any) => ({
            name: comp.name || comp.id,
            type: mapComponentTypeToVariableType(comp.type),
            label: comp.label || comp.name,
            required: comp.required || false,
            nameError: ''
          }))
        }
      } catch (e) {
        console.error('解析formConfig失败:', e)
      }
    }
  } catch (error) {
    console.error('加载表单配置失败:', error)
    ElMessage.error('加载表单配置失败')
  }
}

// 刷新编辑表单的业务视图配置
const refreshEditBusinessView = async () => {
  if (!editForm.businessFormId) {
    ElMessage.warning('请先选择业务表单')
    return
  }

  refreshingEditForm.value = true
  try {
    const response = await formApi.getFormDetail(editForm.businessFormId)
    if (response && response.data && response.data.formConfig) {
      editForm.businessView = response.data.formConfig

      // 重新解析变量
      try {
        const formConfig = JSON.parse(response.data.formConfig)
        if (formConfig.components && Array.isArray(formConfig.components)) {
          editForm.variables = formConfig.components.map((comp: any) => ({
            name: comp.name || comp.id,
            type: mapComponentTypeToVariableType(comp.type),
            label: comp.label || comp.name,
            required: comp.required || false,
            nameError: ''
          }))
        }
      } catch (e) {
        console.error('解析formConfig失败:', e)
      }

      ElMessage.success('表单配置已刷新')
    } else {
      ElMessage.warning('该表单暂无配置信息')
    }
  } catch (error) {
    console.error('刷新表单配置失败:', error)
    ElMessage.error('刷新表单配置失败')
  } finally {
    refreshingEditForm.value = false
  }
}

// 格式化businessView用于显示（编辑抽屉）
const formatEditBusinessView = (): string => {
  if (!editForm.businessView) return ''
  try {
    const config = typeof editForm.businessView === 'string'
      ? JSON.parse(editForm.businessView)
      : editForm.businessView
    return JSON.stringify(config, null, 2)
  } catch (e) {
    return editForm.businessView
  }
}

// 获取formConfig中的组件数量（编辑抽屉）
const getEditFormConfigComponentCount = (): number => {
  if (!editForm.businessView) return 0
  try {
    const config = typeof editForm.businessView === 'string'
      ? JSON.parse(editForm.businessView)
      : editForm.businessView
    return config.components?.length || 0
  } catch (e) {
    return 0
  }
}

// 关闭编辑抽屉
const closeEditDrawer = () => {
  showEditDrawer.value = false
  resetEditForm()
}

// 确认编辑工作流（跳转到设计器）
const confirmUpdateWorkflow = async () => {
  try {
    // 表单验证
    await editFormRef.value?.validate()

    // 验证变量定义
    if (!validateAllEditVariables()) {
      ElMessage.error('请检查变量定义中的错误')
      return
    }

    editing.value = true

    // 构造URL参数，传递编辑后的数据给设计器
    const params = new URLSearchParams({
      id: editForm.id,
      name: editForm.name,
      key: editForm.key,
      description: editForm.description,
      businessType: editForm.businessType.toString(),
      isEdit: 'true' // 标识这是编辑模式
    })

    if (editForm.businessType === 1) {
      if (editForm.systemFormKey) {
        params.append('systemFormKey', editForm.systemFormKey)
      }
    } else if (editForm.businessType === 2 && editForm.businessFormId) {
      params.append('businessFormId', editForm.businessFormId)
    }

    // 无论是系统表单还是业务表单，都需要传递变量定义到设计器
    if (editForm.variables.length > 0) {
      const cleanVariables = editForm.variables.map((v: VariableItem) => ({
        name: v.name,
        type: v.type,
        label: v.label,
        required: v.required
      }))
      params.append('variables', JSON.stringify(cleanVariables))
    }

    // 传递 businessView
    if (editForm.businessView) {
      params.append('businessView', editForm.businessView)
    }

    // 关闭抽屉并重置表单
    closeEditDrawer()

    // 跳转到设计器页面并传递参数
    const url = `/home/workflow/design?${params.toString()}`
    window.location.href = url

  } catch (error) {
    console.error('编辑工作流参数验证失败:', error)
    ElMessage.error('请检查表单输入')
  } finally {
    editing.value = false
  }
}

// 确认创建工作流（跳转到设计器）
const confirmCreateWorkflow = async () => {
  try {
    // 表单验证
    await createFormRef.value?.validate()

    // 验证变量定义
    if (!validateAllVariables()) {
      ElMessage.error('请检查变量定义中的错误')
      return
    }

    creating.value = true

    // 构造URL参数
    const params = new URLSearchParams({
      businessType: createForm.businessType.toString()
    })

    if (createForm.businessType === 1 && createForm.systemFormKey) {
      params.append('systemFormKey', createForm.systemFormKey)
    } else if (createForm.businessType === 2 && createForm.businessFormId) {
      params.append('businessFormId', createForm.businessFormId)
    }

    // 传递 businessView
    if (createForm.businessView) {
      params.append('businessView', createForm.businessView)
    }

    // 如果有变量定义，将其作为JSON字符串传递
    if (createForm.variables.length > 0) {
      // 清理变量数据，移除错误信息等UI相关字段
      const cleanVariables = createForm.variables.map((v: VariableItem) => ({
        name: v.name,
        type: v.type,
        label: v.label,
        required: v.required
      }))
      params.append('variables', JSON.stringify(cleanVariables))
    }

    // 关闭抽屉并重置表单
    closeCreateDrawer()

    // 跳转到设计器页面并传递参数
    const url = `/home/workflow/design?${params.toString()}`
    window.location.href = url

  } catch (error) {
    console.error('创建工作流参数验证失败:', error)
  } finally {
    creating.value = false
  }
}

// 分页处理
const handleSizeChange = (val: number) => {
  pagination.size = val
  getWorkflowList()
}

const handleCurrentChange = (val: number) => {
  pagination.current = val
  getWorkflowList()
}

// 选择处理
const handleSelectionChange = (val: any[]) => {
  selectedWorkflows.value = val
}

// 查看工作流
const handleView = async (row: any) => {
  try {
    const response = await workflowApi.getWorkflowDetail(row.id)
    currentWorkflow.value = response.data
    showViewDialog.value = true
  } catch (error) {
    console.error('获取工作流详情失败:', error)
    ElMessage.error('获取工作流详情失败')
  }
}

// 编辑工作流
const handleEdit = async (row: any) => {
  try {
    // 获取流程定义详情
    const response = await workflowApi.getWorkflowDetail(row.id)
    const workflowDetail = response.data

    // 填充编辑表单
    editForm.id = workflowDetail.id
    editForm.name = workflowDetail.name
    editForm.key = workflowDetail.key
    editForm.description = workflowDetail.description || ''
    editForm.businessType = workflowDetail.businessType || 1
    editForm.systemFormKey = workflowDetail.systemFormKey || ''
    editForm.businessFormId = workflowDetail.businessFormId || ''
    editForm.businessView = workflowDetail.businessView || ''

    // 如果有变量定义，解析并填充
    if (workflowDetail.variables && Array.isArray(workflowDetail.variables)) {
      let variables = workflowDetail.variables.map((v: any) => ({
        name: v.name || '',
        type: v.type || 'string',
        label: v.label || '',
        required: v.required || false,
        nameError: ''
      }))
      
      // 如果有businessView，从中更新变量类型
      if (workflowDetail.businessView && workflowDetail.businessType === 2) {
        try {
          const formConfig = JSON.parse(workflowDetail.businessView)
          if (formConfig.components && Array.isArray(formConfig.components)) {
            // 创建组件名称到类型的映射
            const componentTypeMap: Record<string, string> = {}
            formConfig.components.forEach((comp: any) => {
              const name = comp.name || comp.id
              if (name && comp.type) {
                componentTypeMap[name] = mapComponentTypeToVariableType(comp.type)
              }
            })
            // 更新变量类型
            variables = variables.map((v: any) => ({
              ...v,
              type: componentTypeMap[v.name] || v.type
            }))
          }
        } catch (e) {
          console.error('解析businessView更新变量类型失败:', e)
        }
      }
      
      editForm.variables = variables
    } else {
      editForm.variables = []
    }

    currentEditWorkflow.value = workflowDetail
    showEditDrawer.value = true

    // 如果是业务表单类型，加载业务表单列表
    if (editForm.businessType === 2) {
      loadBusinessFormList()
    }
  } catch (error) {
    console.error('获取工作流详情失败:', error)
    ElMessage.error('获取工作流详情失败')
  }
}

// 切换状态
const handleToggleStatus = async (row: any) => {
  const newStatus = row.status === 1 ? 0 : 1
  const statusText = newStatus === 1 ? '激活' : '禁用'

  try {
    await ElMessageBox.confirm(
      `确定要${statusText}流程定义"${row.name}"吗？`,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await workflowApi.changeWorkflowStatus(row.id, newStatus)
    ElMessage.success(`${statusText}成功`)
    getWorkflowList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(`${statusText}流程定义失败:`, error)
      ElMessage.error(`${statusText}失败`)
    }
  }
}

// 删除工作流
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除流程定义"${row.name}"吗？此操作不可恢复！`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await workflowApi.deleteWorkflow(row.id)
    ElMessage.success('删除成功')
    getWorkflowList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除流程定义失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 获取状态文本
const getStatusText = (status: number) => {
  const statusMap = {
    1: '激活',
    0: '禁用'
  }
  return statusMap[status as keyof typeof statusMap] || '未知'
}

// 获取状态标签类型
const getStatusTagType = (status: number) => {
  const typeMap = {
    1: 'success',
    0: 'info'
  }
  return typeMap[status as keyof typeof typeMap] || 'info'
}

// 格式化日期时间
const formatDateTime = (dateTime: string) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 监听业务表单选择变化 - 创建模式
watch(() => createForm.businessFormId, async (newFormId) => {
  if (newFormId && createForm.businessType === 2) {
    await loadFormFieldsToVariables(newFormId, 'create')
  }
})

// 监听工作流类型变化 - 创建模式
watch(() => createForm.businessType, (newType) => {
  if (newType === 2) {
    loadBusinessFormList()
  }
})

// 监听工作流类型变化 - 编辑模式
watch(() => editForm.businessType, (newType) => {
  if (newType === 2) {
    loadBusinessFormList()
  }
})

// 监听业务表单选择变化 - 编辑模式
watch(() => editForm.businessFormId, async (newFormId) => {
  if (newFormId && editForm.businessType === 2) {
    await loadFormFieldsToVariables(newFormId, 'edit')
  }
})

// 根据业务表单ID加载字段信息并转换为流程变量
const loadFormFieldsToVariables = async (formId: string, mode: 'create' | 'edit') => {
  try {
    const response = await formApi.getTableInfoByFormId(formId)
    const data = response.data

    if (data && data.columnInfos && Array.isArray(data.columnInfos)) {
      // 过滤出业务字段（非系统字段）
      const businessFields = data.columnInfos.filter(column => column.isSysField === 0)

      // 将字段信息转换为流程变量
      const variables = businessFields.map(column => ({
        name: column.columnName,
        type: mapColumnTypeToVariableType(column.formType, column.columnType),
        label: column.columnComment || column.columnName,
        required: !column.isNullable,
        nameError: ''
      }))

      // 根据模式更新对应的变量列表
      if (mode === 'create') {
        createForm.variables = variables
      } else {
        editForm.variables = variables
      }

    } else {
      ElMessage.warning('该表单暂无字段信息')
      // 清空变量列表
      if (mode === 'create') {
        createForm.variables = []
      } else {
        editForm.variables = []
      }
    }
  } catch (error) {
    console.error('加载表单字段失败:', error)
    ElMessage.error('加载表单字段失败')
    // 清空变量列表
    if (mode === 'create') {
      createForm.variables = []
    } else {
      editForm.variables = []
    }
  }
}

// 将数据库字段类型映射为流程变量类型
const mapColumnTypeToVariableType = (formType: string, columnType: string): string => {
  // 优先使用表单类型进行映射
  switch (formType) {
    case 'number':
    case 'slider':
    case 'rate':
      return 'number'
    case 'switch':
      return 'boolean'
    case 'date':
    case 'datetime':
    case 'time':
    case 'date-picker':
    case 'time-picker':
      return 'date'
    case 'checkbox':
    case 'upload':
      return 'object' // 数组类型使用object
    // 业务组件类型
    case 'user-select':
      return 'user-select'
    case 'department-select':
      return 'department-select'
    case 'role-select':
      return 'role-select'
    default:
      return 'string'
  }
}

// 页面挂载时获取数据
onMounted(() => {
  getWorkflowList()
})
</script>

<style scoped lang="scss">
.workflow-page {
  padding: 20px;
  background: var(--bg-secondary, #f8fafc);
  min-height: 100vh;
}

.page-header {

  .header-actions {
    display: flex;
    gap: 12px;
  }
}

.search-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 16px 20px;
  background: var(--bg-card, #ffffff);
  border-radius: 8px;
  box-shadow: var(--shadow-sm, 0 1px 2px 0 rgba(0, 0, 0, 0.05));

  .search-left {
    display: flex;
    gap: 12px;
    align-items: center;
  }

  .search-right {
    display: flex;
    gap: 12px;
  }
}

.workflow-content {
  background: var(--bg-card, #ffffff);
  border-radius: 8px;
  box-shadow: var(--shadow-sm, 0 1px 2px 0 rgba(0, 0, 0, 0.05));
  padding: 20px;

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

  .workflow-name {
    .name {
      font-weight: 500;
      color: var(--text-primary, #1f2937);
    }

    .description {
      display: block;
      font-size: 12px;
      color: var(--text-muted, #9ca3af);
      margin-top: 4px;
    }
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    padding: 20px;
    background: var(--bg-card);
    border: 1px solid var(--border-color);
    border-radius: 12px;
    box-shadow: var(--shadow-lg);
  }
}

.workflow-detail {
  .config-section {
    margin-top: 20px;

    h4 {
      margin-bottom: 10px;
      color: var(--text-primary, #1f2937);
    }

    .workflow-config-container {
      border: 1px solid var(--border-color, #e5e7eb);
      border-radius: 4px;
      padding: 20px;
      background: var(--bg-tertiary, #f1f5f9);
      max-height: 500px;
      overflow-y: auto;
    }
  }
}

.create-workflow-form {
  padding: 24px;
  height: 100%;
  display: flex;
  flex-direction: column;

  .el-form {
    flex: 1;
    overflow-y: auto;
    padding-right: 8px;

    .el-form-item {
      margin-bottom: 28px;

      &:last-child {
        margin-bottom: 0;
      }
    }

    :deep(.el-form-item__label) {
      font-weight: 600;
      color: var(--el-text-color-primary);
      font-size: 14px;
    }
  }

  .form-help-text {
    font-size: 12px;
    color: var(--el-text-color-secondary);
    margin-top: 8px;
    line-height: 1.5;
    padding-left: 2px;
  }

  // 表单视图配置样式
  .business-view-config {
    background: var(--el-fill-color-lighter);
    border-radius: 10px;
    padding: 16px;
    
    .config-header {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 14px;
      
      .config-title {
        font-size: 14px;
        font-weight: 500;
        color: var(--el-text-color-primary);
      }
    }
    
    .system-view-config {
      :deep(.el-input-group__prepend) {
        background: var(--el-bg-color);
      }
    }
  }

  // 业务表单配置样式
  .business-form-config {
    .loading-config {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      padding: 24px;
      color: var(--el-text-color-secondary);
      font-size: 14px;
      background: var(--el-bg-color);
      border-radius: 8px;
    }

    .config-preview {
      .preview-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 16px;
        background: var(--el-bg-color);
        border-radius: 8px 8px 0 0;

        .header-left {
          display: flex;
          align-items: center;
          gap: 8px;
          flex: 1;

          .el-icon {
            font-size: 16px;
            color: var(--el-color-primary);
          }

          span {
            font-size: 14px;
            font-weight: 500;
            color: var(--el-text-color-primary);
          }

          .el-tag {
            margin-left: 4px;
          }
        }
      }

      :deep(.el-textarea) {
        .el-textarea__inner {
          border-radius: 0 0 8px 8px;
          font-family: 'Monaco', 'Menlo', monospace;
          font-size: 12px;
          border: none;
          background: var(--el-bg-color);
        }
      }
    }
  }

  // 变量管理样式优化 - 简洁清新风格
  .variables-form-item {
    :deep(.el-form-item__content) {
      line-height: normal;
    }
  }

  .variables-management {
    background: var(--el-fill-color-lighter);
    border-radius: 10px;
    overflow: hidden;

    .variables-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 14px 18px;
      background: transparent;

      .variables-title {
        font-size: 14px;
        font-weight: 500;
        color: var(--el-text-color-primary);
        margin: 0;
      }
    }

    .variables-table-header {
      display: flex;
      align-items: center;
      padding: 10px 18px;
      background: rgba(99, 102, 241, 0.06);
      font-weight: 500;
      font-size: 13px;
      color: var(--el-text-color-secondary);

      .header-cell {
        display: flex;
        align-items: center;

        &.name-col { flex: 0 0 180px; padding-right: 12px; }
        &.type-col { flex: 0 0 140px; padding-right: 12px; }
        &.label-col { flex: 1; min-width: 140px; padding-right: 12px; }
        &.required-col { flex: 0 0 70px; justify-content: center; }
        &.action-col { flex: 0 0 60px; justify-content: center; }
      }
    }

    .empty-variables {
      padding: 32px 20px;
      text-align: center;
      background: var(--el-bg-color);
      margin: 0 12px 12px;
      border-radius: 8px;
    }

    .variables-list {
      padding: 0 12px 12px;

      .variables-table-body {
        background: var(--el-bg-color);
        border-radius: 8px;
        overflow: hidden;
        
        .variable-row {
          transition: background 0.15s ease;

          &:hover {
            background: var(--el-fill-color-lighter);
          }

          &.has-error {
            background: var(--el-color-error-light-9);
          }

          &.auto-generated {
            .row-content::after {
              content: '自动';
              background: var(--el-color-info-light-3);
              color: white;
              font-size: 10px;
              padding: 2px 6px;
              border-radius: 4px;
              margin-left: 8px;
              flex-shrink: 0;
            }
          }

          .row-content {
            display: flex;
            align-items: center;
            padding: 12px 18px;

            .cell {
              display: flex;
              flex-direction: column;

              &.name-col {
                flex: 0 0 180px;
                padding-right: 12px;

                .field-error {
                  display: flex;
                  align-items: center;
                  gap: 4px;
                  color: var(--el-color-error);
                  font-size: 11px;
                  margin-top: 4px;

                  .el-icon {
                    font-size: 12px;
                  }
                }
              }

              &.type-col {
                flex: 0 0 140px;
                padding-right: 12px;
              }

              &.label-col { 
                flex: 1; 
                min-width: 140px; 
                padding-right: 12px;
              }

              &.required-col {
                flex: 0 0 70px;
                align-items: center;
                justify-content: center;
              }

              &.action-col {
                flex: 0 0 60px;
                align-items: center;
                justify-content: center;
                
                .el-button {
                  padding: 6px;
                  
                  &:hover {
                    background: var(--el-color-danger-light-9);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  .drawer-footer {
    position: sticky;
    bottom: 0;
    margin: 0 -24px -24px -24px;
    padding: 16px 24px;
    border-top: 1px solid var(--el-border-color-lighter);
    background: var(--el-bg-color);
    display: flex;
    justify-content: flex-end;
    gap: 12px;

    .el-button {
      min-width: 88px;
      border-radius: 8px;
    }
  }
}

// 响应式优化
@media (max-width: 1400px) {
  :deep(.el-drawer) {
    width: 70% !important;
  }
}

@media (max-width: 1200px) {
  :deep(.el-drawer) {
    width: 80% !important;
  }

  .create-workflow-form {
    .variables-management {
      .variables-table-header,
      .variables-list .variable-row .row-content {
        .name-col { flex: 0 0 150px; }
        .type-col { flex: 0 0 120px; }
        .label-col { flex: 1; min-width: 120px; }
        .required-col { flex: 0 0 60px; }
        .action-col { flex: 0 0 50px; }
      }
    }
  }
}

@media (max-width: 768px) {
  :deep(.el-drawer) {
    width: 95% !important;
  }

  .create-workflow-form {
    padding: 16px;

    .variables-management {
      .variables-header {
        flex-direction: column;
        align-items: stretch;
        gap: 12px;
        padding: 12px 14px;
      }

      .variables-table-header {
        display: none;
      }

      .variables-list {
        padding: 0 8px 8px;
        
        .variables-table-body {
          .variable-row {
            .row-content {
              flex-direction: column;
              gap: 12px;
              padding: 14px;

              .cell {
                flex: 1 1 auto !important;
                width: 100%;
                padding-right: 0 !important;

                &::before {
                  content: attr(data-label);
                  font-weight: 500;
                  color: var(--el-text-color-secondary);
                  font-size: 12px;
                  margin-bottom: 6px;
                  display: block;
                }
              }
            }
          }
        }
      }
    }
    
    .drawer-footer {
      margin: 0 -16px -16px -16px;
      padding: 14px 16px;
    }
  }
}
</style>

