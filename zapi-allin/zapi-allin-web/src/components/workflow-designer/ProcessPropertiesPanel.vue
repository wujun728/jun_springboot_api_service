<template>
  <div class="process-properties-panel">
    <!-- 节点属性配置 -->
    <div v-if="selectedNode" class="node-properties">
      <div class="property-section">
        <h4>基本信息</h4>
        <el-form label-width="70px" size="small">
          <el-form-item label="节点名称">
            <el-input
              :model-value="selectedNode.name"
              @input="updateNodeProperty('name', $event)"
              placeholder="请输入节点名称"
            />
          </el-form-item>

          <el-form-item label="节点ID">
            <el-input
              :model-value="selectedNode.id"
              disabled
              placeholder="系统自动生成"
            />
          </el-form-item>

          <el-form-item label="节点类型">
            <el-tag :type="getNodeTypeTagType(selectedNode.type)">
              {{ getNodeTypeText(selectedNode.type) }}
            </el-tag>
          </el-form-item>

          <el-form-item label="节点描述">
            <el-input
              :model-value="selectedNode.description || ''"
              @input="updateNodeProperty('description', $event)"
              type="textarea"
              :rows="2"
              placeholder="请输入节点描述（可选）"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 审批节点特有配置 -->
      <div v-if="selectedNode.type === 'APPROVAL'" class="property-section">
        <h4>审批配置</h4>
        <el-form label-width="90px" size="small">
          <el-form-item label="处理人类型">
            <el-select
              :model-value="(selectedNode as ApprovalNode).assigneeType || 'FIXED'"
              @change="handleAssigneeTypeChange"
              style="width: 100%"
            >
              <el-option label="固定人员" value="FIXED">
                <div class="assignee-type-option">
                  <span>固定人员</span>
                  <el-text size="small" type="info">选择用户/部门/角色</el-text>
                </div>
              </el-option>
              <el-option label="动态人员" value="DYNAMIC_USER">
                <div class="assignee-type-option">
                  <span>动态人员</span>
                  <el-text size="small" type="info">从人员选择组件获取</el-text>
                </div>
              </el-option>
              <el-option label="动态部门" value="DYNAMIC_DEPT">
                <div class="assignee-type-option">
                  <span>动态部门</span>
                  <el-text size="small" type="info">从部门选择组件获取</el-text>
                </div>
              </el-option>
              <el-option label="动态角色" value="DYNAMIC_ROLE">
                <div class="assignee-type-option">
                  <span>动态角色</span>
                  <el-text size="small" type="info">从角色选择组件获取</el-text>
                </div>
              </el-option>
            </el-select>
          </el-form-item>

          <!-- 固定人员模式 - 支持多选用户/部门/角色 -->
          <el-form-item
            v-if="(selectedNode as ApprovalNode).assigneeType === 'FIXED' || !(selectedNode as ApprovalNode).assigneeType"
            label="审批人"
          >
            <el-input
              :model-value="getApprovalAssigneeDisplayText()"
              placeholder="请选择审批人"
              readonly
              @click="openApprovalReceiverSelector"
            >
              <template #suffix>
                <el-icon class="el-input__icon" style="cursor: pointer;"><User /></el-icon>
              </template>
            </el-input>
            <!-- 已选择的审批人标签展示 -->
            <div v-if="getApprovalSelectedItems().length > 0" class="cc-selected-tags">
              <el-tag
                v-for="item in getApprovalSelectedItems()"
                :key="`${item.type}-${item.id}`"
                size="small"
                closable
                @close="removeApprovalSelectedItem(item)"
                style="margin: 4px 4px 0 0;"
              >
                <span class="tag-type">[{{ item.type === 'dept' ? '部门' : item.type === 'role' ? '角色' : '用户' }}]</span>
                {{ item.label }}
              </el-tag>
            </div>
          </el-form-item>

          <!-- 动态人员模式 - 从人员选择组件获取 -->
          <el-form-item
            v-if="(selectedNode as ApprovalNode).assigneeType === 'DYNAMIC_USER'"
            label="人员变量"
          >
            <el-select
              :model-value="(selectedNode as ApprovalNode).assignee"
              @change="updateNodeProperty('assignee', $event)"
              placeholder="选择人员选择组件"
              style="width: 100%"
              filterable
              clearable
            >
              <el-option
                v-for="variable in getUserSelectVariables()"
                :key="variable.name"
                :label="variable.label || variable.name"
                :value="variable.name"
              >
                <div class="variable-option">
                  <span class="variable-name">{{ variable.label || variable.name }}</span>
                  <el-tag size="small" type="success">人员</el-tag>
                </div>
              </el-option>
            </el-select>
            <el-text size="small" type="info" style="margin-top: 4px; display: block;">
              选择表单中的人员选择组件
            </el-text>
          </el-form-item>

          <!-- 动态部门模式 - 从部门选择组件获取 -->
          <el-form-item
            v-if="(selectedNode as ApprovalNode).assigneeType === 'DYNAMIC_DEPT'"
            label="部门变量"
          >
            <el-select
              :model-value="(selectedNode as ApprovalNode).assignee"
              @change="updateNodeProperty('assignee', $event)"
              placeholder="选择部门选择组件"
              style="width: 100%"
              filterable
              clearable
            >
              <el-option
                v-for="variable in getDeptSelectVariables()"
                :key="variable.name"
                :label="variable.label || variable.name"
                :value="variable.name"
              >
                <div class="variable-option">
                  <span class="variable-name">{{ variable.label || variable.name }}</span>
                  <el-tag size="small" type="warning">部门</el-tag>
                </div>
              </el-option>
            </el-select>
            <el-text size="small" type="info" style="margin-top: 4px; display: block;">
              选择表单中的部门选择组件，将通知该部门下所有人员
            </el-text>
          </el-form-item>

          <!-- 动态角色模式 - 从角色选择组件获取 -->
          <el-form-item
            v-if="(selectedNode as ApprovalNode).assigneeType === 'DYNAMIC_ROLE'"
            label="角色变量"
          >
            <el-select
              :model-value="(selectedNode as ApprovalNode).assignee"
              @change="updateNodeProperty('assignee', $event)"
              placeholder="选择角色选择组件"
              style="width: 100%"
              filterable
              clearable
            >
              <el-option
                v-for="variable in getRoleSelectVariables()"
                :key="variable.name"
                :label="variable.label || variable.name"
                :value="variable.name"
              >
                <div class="variable-option">
                  <span class="variable-name">{{ variable.label || variable.name }}</span>
                  <el-tag size="small" type="danger">角色</el-tag>
                </div>
              </el-option>
            </el-select>
            <el-text size="small" type="info" style="margin-top: 4px; display: block;">
              选择表单中的角色选择组件，将通知该角色下所有人员
            </el-text>
          </el-form-item>

          <el-form-item label="允许驳回">
            <el-switch
              :model-value="(selectedNode as ApprovalNode).allowReject"
              @change="updateNodeProperty('allowReject', $event)"
            />
          </el-form-item>

          <!-- 多人审批方式配置 -->
          <el-form-item label="审批方式">
            <el-radio-group
              :model-value="(selectedNode as ApprovalNode).approvalMode || 'ANY'"
              @change="updateNodeProperty('approvalMode', $event)"
            >
              <el-radio value="ANY">
                <span class="approval-mode-label">或签</span>
                <el-tooltip content="任一人通过即可" placement="top">
                  <el-icon class="info-icon"><InfoFilled /></el-icon>
                </el-tooltip>
              </el-radio>
              <el-radio value="ALL">
                <span class="approval-mode-label">会签</span>
                <el-tooltip content="所有人都通过才行" placement="top">
                  <el-icon class="info-icon"><InfoFilled /></el-icon>
                </el-tooltip>
              </el-radio>
            </el-radio-group>
            <div class="approval-mode-hint">
              <el-text size="small" type="info">
                {{ (selectedNode as ApprovalNode).approvalMode === 'ALL'
                   ? '会签：全部通过才继续，任一驳回则结束'
                   : '或签：任一通过即继续，任一驳回则结束' }}
              </el-text>
            </div>
          </el-form-item>
        </el-form>
      </div>

      <!-- 审批节点消息发送配置 -->
      <div v-if="selectedNode.type === 'APPROVAL'" class="property-section">
        <h4>消息发送配置</h4>

        <el-tabs v-model="activeMessageTab" class="message-tabs" type="card">
          <el-tab-pane label="待审批" name="onPending">
            <div class="timing-config">
              <el-form label-width="50px" size="small">
                <el-form-item label="启用">
                  <el-switch
                    :model-value="getMessageTimingEnabled('onPending')"
                    @change="toggleMessageTiming('onPending', $event)"
                    size="small"
                  />
                </el-form-item>
                <template v-if="getMessageTimingEnabled('onPending')">
                  <el-form-item label="标题">
                    <el-input
                      :model-value="getMessageTimingField('onPending', 'title')"
                      @input="updateMessageTimingField('onPending', 'title', $event)"
                      placeholder="支持${xxx}变量"
                    />
                  </el-form-item>
                  <el-form-item label="内容">
                    <el-input
                      :model-value="getMessageTimingField('onPending', 'content')"
                      @input="updateMessageTimingField('onPending', 'content', $event)"
                      type="textarea"
                      :rows="2"
                      placeholder="支持${xxx}变量"
                    />
                  </el-form-item>
                  <el-form-item label="接收人">
                    <el-tag size="small" type="info">当前审批人</el-tag>
                  </el-form-item>
                </template>
              </el-form>
            </div>
          </el-tab-pane>

          <el-tab-pane label="通过" name="onApprove">
            <div class="timing-config">
              <el-form label-width="50px" size="small">
                <el-form-item label="启用">
                  <el-switch
                    :model-value="getMessageTimingEnabled('onApprove')"
                    @change="toggleMessageTiming('onApprove', $event)"
                    size="small"
                  />
                </el-form-item>
                <template v-if="getMessageTimingEnabled('onApprove')">
                  <el-form-item label="标题">
                    <el-input
                      :model-value="getMessageTimingField('onApprove', 'title')"
                      @input="updateMessageTimingField('onApprove', 'title', $event)"
                      placeholder="支持${xxx}变量"
                    />
                  </el-form-item>
                  <el-form-item label="内容">
                    <el-input
                      :model-value="getMessageTimingField('onApprove', 'content')"
                      @input="updateMessageTimingField('onApprove', 'content', $event)"
                      type="textarea"
                      :rows="2"
                      placeholder="支持${xxx}变量"
                    />
                  </el-form-item>
                  <el-form-item label="接收人">
                    <el-tag size="small" type="success">流程发起人</el-tag>
                  </el-form-item>
                </template>
              </el-form>
            </div>
          </el-tab-pane>

          <el-tab-pane label="驳回" name="onReject">
            <div class="timing-config">
              <el-form label-width="50px" size="small">
                <el-form-item label="启用">
                  <el-switch
                    :model-value="getMessageTimingEnabled('onReject')"
                    @change="toggleMessageTiming('onReject', $event)"
                    size="small"
                  />
                </el-form-item>
                <template v-if="getMessageTimingEnabled('onReject')">
                  <el-form-item label="标题">
                    <el-input
                      :model-value="getMessageTimingField('onReject', 'title')"
                      @input="updateMessageTimingField('onReject', 'title', $event)"
                      placeholder="支持${xxx}变量"
                    />
                  </el-form-item>
                  <el-form-item label="内容">
                    <el-input
                      :model-value="getMessageTimingField('onReject', 'content')"
                      @input="updateMessageTimingField('onReject', 'content', $event)"
                      type="textarea"
                      :rows="2"
                      placeholder="支持${xxx}变量"
                    />
                  </el-form-item>
                  <el-form-item label="接收人">
                    <el-tag size="small" type="danger">流程发起人</el-tag>
                  </el-form-item>
                </template>
              </el-form>
            </div>
          </el-tab-pane>

          <el-tab-pane label="完成" name="onComplete">
            <div class="timing-config">
              <el-form label-width="50px" size="small">
                <el-form-item label="启用">
                  <el-switch
                    :model-value="getMessageTimingEnabled('onComplete')"
                    @change="toggleMessageTiming('onComplete', $event)"
                    size="small"
                  />
                </el-form-item>
                <template v-if="getMessageTimingEnabled('onComplete')">
                  <el-form-item label="标题">
                    <el-input
                      :model-value="getMessageTimingField('onComplete', 'title')"
                      @input="updateMessageTimingField('onComplete', 'title', $event)"
                      placeholder="支持${xxx}变量"
                    />
                  </el-form-item>
                  <el-form-item label="内容">
                    <el-input
                      :model-value="getMessageTimingField('onComplete', 'content')"
                      @input="updateMessageTimingField('onComplete', 'content', $event)"
                      type="textarea"
                      :rows="2"
                      placeholder="支持${xxx}变量"
                    />
                  </el-form-item>
                  <el-form-item label="接收人">
                    <el-tag size="small" type="warning">流程发起人</el-tag>
                  </el-form-item>
                </template>
              </el-form>
            </div>
          </el-tab-pane>
        </el-tabs>

        <!-- 参数配置（共享） -->
        <div v-if="templateParams.length > 0" class="params-section">
          <div class="params-header">参数配置</div>
          <div class="params-config">
            <div
              v-for="param in templateParams"
              :key="param"
              class="param-item"
            >
              <div class="param-header">
                <span class="param-name">${{ param }}</span>
              </div>
              <div class="param-body">
                <el-select
                  :model-value="getParamSourceType(param)"
                  @change="updateParamSourceType(param, $event)"
                  size="small"
                  style="width: 80px; margin-right: 6px;"
                >
                  <el-option label="固定值" value="FIXED" />
                  <el-option label="变量" value="VARIABLE" />
                  <el-option label="字段" value="FORM_FIELD" />
                </el-select>
                <el-input
                  v-if="getParamSourceType(param) === 'FIXED'"
                  :model-value="getParamValue(param)"
                  @input="updateParamValue(param, $event)"
                  placeholder="固定值"
                  size="small"
                  style="flex: 1;"
                />
                <el-select
                  v-else-if="getParamSourceType(param) === 'VARIABLE'"
                  :model-value="getParamValue(param)"
                  @change="updateParamValue(param, $event)"
                  placeholder="选择变量"
                  size="small"
                  style="flex: 1;"
                  filterable
                  clearable
                >
                  <el-option
                    v-for="variable in process.variables || []"
                    :key="variable.name"
                    :label="variable.label || variable.name"
                    :value="variable.name"
                  />
                </el-select>
                <el-input
                  v-else
                  :model-value="getParamValue(param)"
                  @input="updateParamValue(param, $event)"
                  placeholder="字段名"
                  size="small"
                  style="flex: 1;"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 条件分支节点特有配置 -->
      <div v-if="selectedNode.type === 'CONDITIONAL_BRANCH'" class="property-section">
        <h4>分支配置</h4>
        <el-form label-width="80px" size="small">
          <el-form-item label="默认分支">
            <el-select
              :model-value="(selectedNode as ConditionalBranchNode).defaultFlow || ''"
              @change="updateNodeProperty('defaultFlow', $event)"
              placeholder="选择默认分支"
              style="width: 100%"
              clearable
            >
              <el-option
                v-for="flow in getOutgoingFlows(selectedNode.id)"
                :key="flow.id"
                :label="flow.name || `连接到${getTargetNodeName(flow.to)}`"
                :value="flow.id"
              />
            </el-select>
            <el-text size="small" type="info" style="margin-top: 4px; display: block;">
              条件都不满足时执行
            </el-text>
          </el-form-item>

          <el-form-item label="节点描述">
            <el-input
              :model-value="(selectedNode as ConditionalBranchNode).description || ''"
              @input="updateNodeProperty('description', $event)"
              type="textarea"
              :rows="2"
              placeholder="请输入节点描述（可选）"
            />
          </el-form-item>
        </el-form>

        <div class="flows-config">
          <h5>输出连接配置</h5>
          <div v-if="getOutgoingFlows(selectedNode.id).length === 0" class="no-flows">
            <el-empty description="暂无输出连接" :image-size="60" />
          </div>
          <div v-else>
            <div
              v-for="flow in getOutgoingFlows(selectedNode.id)"
              :key="flow.id"
              class="flow-config-item"
            >
              <div class="flow-info">
                <span class="flow-label">→ {{ getTargetNodeName(flow.to) }}</span>
                <el-button
                  size="small"
                  type="danger"
                  text
                  @click="deleteFlow(flow.id)"
                >
                  删除
                </el-button>
              </div>
              <ConditionBuilder
                :model-value="flow.condition || ''"
                @update:model-value="updateFlowProperty(flow.id, 'condition', $event)"
                :process-variables="process.variables || []"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 抄送节点特有配置 -->
      <div v-if="selectedNode.type === 'CC'" class="property-section">
        <h4>抄送配置</h4>
        <el-form label-width="90px" size="small">
          <el-form-item label="抄送人类型">
            <el-select
              :model-value="(selectedNode as any).assigneeType || 'FIXED'"
              @change="handleCcAssigneeTypeChange"
              style="width: 100%"
            >
              <el-option label="固定人员" value="FIXED">
                <div class="assignee-type-option">
                  <span>固定人员</span>
                  <el-text size="small" type="info">选择用户/部门/角色</el-text>
                </div>
              </el-option>
              <el-option label="动态人员" value="DYNAMIC_USER">
                <div class="assignee-type-option">
                  <span>动态人员</span>
                  <el-text size="small" type="info">从人员选择组件获取</el-text>
                </div>
              </el-option>
              <el-option label="动态部门" value="DYNAMIC_DEPT">
                <div class="assignee-type-option">
                  <span>动态部门</span>
                  <el-text size="small" type="info">从部门选择组件获取</el-text>
                </div>
              </el-option>
              <el-option label="动态角色" value="DYNAMIC_ROLE">
                <div class="assignee-type-option">
                  <span>动态角色</span>
                  <el-text size="small" type="info">从角色选择组件获取</el-text>
                </div>
              </el-option>
            </el-select>
          </el-form-item>

          <!-- 固定人员模式 - 支持多选用户/部门/角色 -->
          <el-form-item
            v-if="(selectedNode as any).assigneeType === 'FIXED' || !(selectedNode as any).assigneeType"
            label="抄送人"
          >
            <el-input
              :model-value="getCcAssigneeDisplayText()"
              placeholder="请选择抄送人"
              readonly
              @click="openCcReceiverSelector"
            >
              <template #suffix>
                <el-icon class="el-input__icon" style="cursor: pointer;"><User /></el-icon>
              </template>
            </el-input>
            <!-- 已选择的接收人标签展示 -->
            <div v-if="getCcSelectedItems().length > 0" class="cc-selected-tags">
              <el-tag
                v-for="item in getCcSelectedItems()"
                :key="`${item.type}-${item.id}`"
                size="small"
                closable
                @close="removeCcSelectedItem(item)"
                style="margin: 4px 4px 0 0;"
              >
                <span class="tag-type">[{{ item.type === 'dept' ? '部门' : item.type === 'role' ? '角色' : '用户' }}]</span>
                {{ item.label }}
              </el-tag>
            </div>
          </el-form-item>

          <!-- 动态人员模式 -->
          <el-form-item
            v-if="(selectedNode as any).assigneeType === 'DYNAMIC_USER'"
            label="人员变量"
          >
            <el-select
              :model-value="(selectedNode as any).assignee"
              @change="updateNodeProperty('assignee', $event)"
              placeholder="选择人员选择组件"
              style="width: 100%"
              filterable
              clearable
            >
              <el-option
                v-for="variable in getUserSelectVariables()"
                :key="variable.name"
                :label="variable.label || variable.name"
                :value="variable.name"
              >
                <div class="variable-option">
                  <span class="variable-name">{{ variable.label || variable.name }}</span>
                  <el-tag size="small" type="success">人员</el-tag>
                </div>
              </el-option>
            </el-select>
            <el-text size="small" type="info" style="margin-top: 4px; display: block;">
              选择表单中的人员选择组件
            </el-text>
          </el-form-item>

          <!-- 动态部门模式 -->
          <el-form-item
            v-if="(selectedNode as any).assigneeType === 'DYNAMIC_DEPT'"
            label="部门变量"
          >
            <el-select
              :model-value="(selectedNode as any).assignee"
              @change="updateNodeProperty('assignee', $event)"
              placeholder="选择部门选择组件"
              style="width: 100%"
              filterable
              clearable
            >
              <el-option
                v-for="variable in getDeptSelectVariables()"
                :key="variable.name"
                :label="variable.label || variable.name"
                :value="variable.name"
              >
                <div class="variable-option">
                  <span class="variable-name">{{ variable.label || variable.name }}</span>
                  <el-tag size="small" type="warning">部门</el-tag>
                </div>
              </el-option>
            </el-select>
            <el-text size="small" type="info" style="margin-top: 4px; display: block;">
              选择表单中的部门选择组件，将通知该部门下所有人员
            </el-text>
          </el-form-item>

          <!-- 动态角色模式 -->
          <el-form-item
            v-if="(selectedNode as any).assigneeType === 'DYNAMIC_ROLE'"
            label="角色变量"
          >
            <el-select
              :model-value="(selectedNode as any).assignee"
              @change="updateNodeProperty('assignee', $event)"
              placeholder="选择角色选择组件"
              style="width: 100%"
              filterable
              clearable
            >
              <el-option
                v-for="variable in getRoleSelectVariables()"
                :key="variable.name"
                :label="variable.label || variable.name"
                :value="variable.name"
              >
                <div class="variable-option">
                  <span class="variable-name">{{ variable.label || variable.name }}</span>
                  <el-tag size="small" type="danger">角色</el-tag>
                </div>
              </el-option>
            </el-select>
            <el-text size="small" type="info" style="margin-top: 4px; display: block;">
              选择表单中的角色选择组件，将通知该角色下所有人员
            </el-text>
          </el-form-item>

          <el-form-item label="通知方式">
            <el-select
              :model-value="(selectedNode as any).notifyType || 'SYSTEM'"
              @change="updateNodeProperty('notifyType', $event)"
              style="width: 100%"
            >
              <el-option label="系统通知" value="SYSTEM" />
              <el-option label="邮件通知" value="EMAIL" />
              <el-option label="短信通知" value="SMS" />
              <el-option label="全部通知" value="ALL" />
            </el-select>
          </el-form-item>

          <el-form-item label="必须查看">
            <el-switch
              :model-value="(selectedNode as any).requireRead || false"
              @change="updateNodeProperty('requireRead', $event)"
            />
          </el-form-item>

          <el-form-item label="节点描述">
            <el-input
              :model-value="(selectedNode as any).description || ''"
              @input="updateNodeProperty('description', $event)"
              type="textarea"
              :rows="2"
              placeholder="请输入节点描述（可选）"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 抄送节点消息发送配置 -->
      <div v-if="selectedNode.type === 'CC'" class="property-section">
        <h4>消息发送配置</h4>

        <el-form label-width="50px" size="small">
          <el-form-item label="启用">
            <el-switch
              :model-value="getCcMessageEnabled()"
              @change="toggleCcMessageEnabled($event)"
              size="small"
            />
          </el-form-item>

          <template v-if="getCcMessageEnabled()">
            <el-form-item label="标题">
              <el-input
                :model-value="getCcMessageField('title')"
                @input="updateCcMessageField('title', $event)"
                placeholder="支持${xxx}变量"
              />
            </el-form-item>
            <el-form-item label="内容">
              <el-input
                :model-value="getCcMessageField('content')"
                @input="updateCcMessageField('content', $event)"
                type="textarea"
                :rows="2"
                placeholder="支持${xxx}变量"
              />
            </el-form-item>
            <el-form-item label="接收人">
              <el-tag size="small" type="info">抄送人</el-tag>
            </el-form-item>
          </template>
        </el-form>

        <!-- 参数配置 -->
        <div v-if="getCcMessageEnabled() && ccTemplateParams.length > 0" class="params-section">
          <div class="params-header">参数配置</div>
          <div class="params-config">
            <div
              v-for="param in ccTemplateParams"
              :key="param"
              class="param-item"
            >
              <div class="param-header">
                <span class="param-name">${{ param }}</span>
              </div>
              <div class="param-body">
                <el-select
                  :model-value="getCcParamSourceType(param)"
                  @change="updateCcParamSourceType(param, $event)"
                  size="small"
                  style="width: 80px; margin-right: 6px;"
                >
                  <el-option label="固定值" value="FIXED" />
                  <el-option label="变量" value="VARIABLE" />
                  <el-option label="字段" value="FORM_FIELD" />
                </el-select>
                <el-input
                  v-if="getCcParamSourceType(param) === 'FIXED'"
                  :model-value="getCcParamValue(param)"
                  @input="updateCcParamValue(param, $event)"
                  placeholder="固定值"
                  size="small"
                  style="flex: 1;"
                />
                <el-select
                  v-else-if="getCcParamSourceType(param) === 'VARIABLE'"
                  :model-value="getCcParamValue(param)"
                  @change="updateCcParamValue(param, $event)"
                  placeholder="选择变量"
                  size="small"
                  style="flex: 1;"
                  filterable
                  clearable
                >
                  <el-option
                    v-for="variable in process.variables || []"
                    :key="variable.name"
                    :label="variable.label || variable.name"
                    :value="variable.name"
                  />
                </el-select>
                <el-input
                  v-else
                  :model-value="getCcParamValue(param)"
                  @input="updateCcParamValue(param, $event)"
                  placeholder="字段名"
                  size="small"
                  style="flex: 1;"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 并行节点特有配置 -->
      <div v-if="selectedNode.type === 'PARALLEL'" class="property-section">
        <h4>并行配置</h4>
        <el-form label-width="90px" size="small">
          <el-form-item label="执行策略">
            <el-select
              :model-value="(selectedNode as any).strategy || 'ALL'"
              @change="updateNodeProperty('strategy', $event)"
              style="width: 100%"
            >
              <el-option label="全部完成" value="ALL">
                <div class="assignee-type-option">
                  <span>全部完成</span>
                  <el-text size="small" type="info">所有分支都完成后才继续</el-text>
                </div>
              </el-option>
              <el-option label="任意完成" value="ANY">
                <div class="assignee-type-option">
                  <span>任意完成</span>
                  <el-text size="small" type="info">任意一个分支完成即可继续</el-text>
                </div>
              </el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="节点描述">
            <el-input
              :model-value="(selectedNode as any).description || ''"
              @input="updateNodeProperty('description', $event)"
              type="textarea"
              :rows="2"
              placeholder="请输入节点描述（可选）"
            />
          </el-form-item>
        </el-form>

        <div class="flows-config">
          <h5>输出连接配置</h5>
          <div v-if="getOutgoingFlows(selectedNode.id).length === 0" class="no-flows">
            <el-empty description="暂无输出连接" :image-size="60" />
          </div>
          <div v-else>
            <div
              v-for="(flow, index) in getOutgoingFlows(selectedNode.id)"
              :key="flow.id"
              class="flow-config-item"
            >
              <div class="flow-info">
                <span class="flow-label">分支 {{ index + 1 }} → {{ getTargetNodeName(flow.to) }}</span>
                <el-button
                  size="small"
                  type="danger"
                  text
                  @click="deleteFlow(flow.id)"
                >
                  删除
                </el-button>
              </div>
              <el-form label-width="70px" size="small" style="margin-top: 6px;">
                <el-form-item label="分支名称">
                  <el-input
                    :model-value="flow.name || ''"
                    @input="updateFlowProperty(flow.id, 'name', $event)"
                    placeholder="请输入分支名称"
                    size="small"
                  />
                </el-form-item>
                <el-form-item label="执行条件">
                  <ConditionBuilder
                    :model-value="flow.condition || ''"
                    @update:model-value="updateFlowProperty(flow.id, 'condition', $event)"
                    :process-variables="process.variables || []"
                  />
                </el-form-item>
              </el-form>
            </div>
          </div>
        </div>
      </div>

      <!-- 位置信息 -->
      <div class="property-section">
        <h4>位置信息</h4>
        <el-form label-width="80px" size="small">
          <el-form-item label="X坐标">
            <el-input-number
              :model-value="selectedNode.position.x"
              @change="updateNodePosition('x', $event)"
              :min="50"
              :max="1600"
              size="small"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="Y坐标">
            <el-input-number
              :model-value="selectedNode.position.y"
              @change="updateNodePosition('y', $event)"
              :min="50"
              :max="1800"
              size="small"
              style="width: 100%"
            />
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 流程基本信息和变量配置 -->
    <div v-else class="process-config">
      <!-- 流程基本信息 -->
      <div class="property-section">
        <h4>流程信息</h4>
        <el-form label-width="80px" size="small">
          <el-form-item label="流程名称">
            <el-input
              :model-value="process.name"
              @input="updateProcessProperty('name', $event)"
              placeholder="请输入流程名称"
            />
          </el-form-item>

          <el-form-item label="流程标识">
            <el-input
              :model-value="process.key"
              @input="updateProcessProperty('key', $event)"
              placeholder="流程标识（英文）"
            />
          </el-form-item>

          <el-form-item label="流程描述">
            <el-input
              :model-value="process.description || ''"
              @input="updateProcessProperty('description', $event)"
              type="textarea"
              :rows="3"
              placeholder="请输入流程描述"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 操作指南 -->
      <div class="property-section">
        <h4>操作指南</h4>
        <el-card class="operation-guide" shadow="never">
          <div class="guide-item">
            <el-icon class="guide-icon"><Mouse /></el-icon>
            <div class="guide-text">
              <div class="guide-title">添加节点</div>
              <div class="guide-desc">从左侧拖拽节点到画布</div>
            </div>
          </div>
          <div class="guide-item">
            <el-icon class="guide-icon"><Connection /></el-icon>
            <div class="guide-text">
              <div class="guide-title">创建连接</div>
              <div class="guide-desc">点击节点的输出点，再点击目标节点的输入点</div>
            </div>
          </div>
          <div class="guide-item">
            <el-icon class="guide-icon"><Setting /></el-icon>
            <div class="guide-text">
              <div class="guide-title">配置属性</div>
              <div class="guide-desc">选中节点或连接线，在此面板配置属性</div>
            </div>
          </div>
          <div class="guide-item">
            <el-icon class="guide-icon"><Delete /></el-icon>
            <div class="guide-text">
              <div class="guide-title">删除元素</div>
              <div class="guide-desc">右键连接线删除，或使用属性面板删除按钮</div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 抄送人选择弹窗 -->
    <el-dialog
      title="选择抄送人"
      v-model="ccReceiverSelectorVisible"
      width="720px"
      destroy-on-close
      :close-on-click-modal="false"
      class="cc-receiver-dialog"
    >
      <!-- 已选摘要 -->
      <div class="cc-selected-summary" v-if="ccSelectedDisplayItems.length">
        <div class="summary-title">已选：</div>
        <div class="summary-chips">
          <el-tag
            v-for="item in ccSelectedDisplayItems"
            :key="`${item.type}-${item.id}`"
            type="info"
            closable
            @close="removeCcTempSelectedItem(item)"
          >
            <span class="chip-type">[{{ item.type === 'dept' ? '部门' : item.type === 'role' ? '角色' : '用户' }}]</span>
            {{ item.label }}
          </el-tag>
        </div>
      </div>
      <div class="cc-selected-summary empty" v-else>未选择任何接收者</div>

      <el-tabs v-model="ccReceiverTab">
        <!-- 按用户选择 -->
        <el-tab-pane label="按用户" name="user">
          <div class="cc-receiver-body">
            <div class="cc-receiver-left">
              <el-tree
                :data="ccDeptTreeWithRoot"
                node-key="id"
                :props="ccDeptTreeProps"
                highlight-current
                @node-click="(node: any) => loadCcUsersByDept(node.id)"
              />
              <div class="empty-tip" v-if="!ccDeptTree.length">暂无部门数据</div>
            </div>
            <div class="cc-receiver-right">
              <el-input
                v-model="ccUserSearch"
                placeholder="搜索用户（姓名/账号）"
                clearable
                @input="loadCcUsersAll"
                @clear="loadCcUsersAll"
                style="margin-bottom:12px;"
              />
              <el-checkbox-group v-model="ccTempSelectedUserIds" class="user-checkbox-group">
                <el-checkbox
                  v-for="user in ccUserOptions"
                  :key="user.id"
                  :label="user.id"
                  :value="user.id"
                >
                  {{ getCcUserDisplayName(user) }}
                </el-checkbox>
              </el-checkbox-group>
              <div class="cc-user-pagination">
                <el-pagination
                  layout="prev, pager, next"
                  :current-page="ccUserPage.current"
                  :page-size="ccUserPage.size"
                  :total="ccUserPage.total"
                  small
                  @current-change="handleCcUserPageChange"
                />
              </div>
              <div v-if="!ccUserOptions.length" class="empty-tip">请选择左侧部门或搜索</div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 按部门选择 -->
        <el-tab-pane label="按部门" name="dept">
          <div class="cc-receiver-body">
            <div class="cc-receiver-left" style="width:100%;">
              <el-tree
                :data="ccDeptTree"
                node-key="id"
                show-checkbox
                :props="ccDeptTreeProps"
                highlight-current
                ref="ccDeptTreeRef"
                @check="handleCcDeptCheck"
              />
              <div class="empty-tip" v-if="!ccDeptTree.length">暂无部门数据</div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 按角色选择 -->
        <el-tab-pane label="按角色" name="role">
          <div class="cc-receiver-body">
            <div class="cc-receiver-left" style="width:100%;">
              <el-checkbox-group v-model="ccTempSelectedRoleIds">
                <el-checkbox
                  v-for="role in ccRoleOptions"
                  :key="role.id"
                  :label="role.id"
                  :value="role.id"
                  style="display:block;margin:6px 0"
                >
                  {{ role.roleName || role.name }}
                </el-checkbox>
              </el-checkbox-group>
              <div class="empty-tip" v-if="!ccRoleOptions.length">暂无角色数据</div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="ccReceiverSelectorVisible = false">取消</el-button>
          <el-button @click="clearCcSelection" type="info">清空</el-button>
          <el-button type="primary" @click="confirmCcReceiverSelection">确定</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 审批人选择弹窗 -->
    <el-dialog
      title="选择审批人"
      v-model="approvalReceiverSelectorVisible"
      width="720px"
      destroy-on-close
      :close-on-click-modal="false"
      class="cc-receiver-dialog"
    >
      <!-- 已选摘要 -->
      <div class="cc-selected-summary" v-if="approvalSelectedDisplayItems.length">
        <div class="summary-title">已选：</div>
        <div class="summary-chips">
          <el-tag
            v-for="item in approvalSelectedDisplayItems"
            :key="`${item.type}-${item.id}`"
            type="info"
            closable
            @close="removeApprovalTempSelectedItem(item)"
          >
            <span class="chip-type">[{{ item.type === 'dept' ? '部门' : item.type === 'role' ? '角色' : '用户' }}]</span>
            {{ item.label }}
          </el-tag>
        </div>
      </div>
      <div class="cc-selected-summary empty" v-else>未选择任何审批人</div>

      <el-tabs v-model="approvalReceiverTab">
        <!-- 按用户选择 -->
        <el-tab-pane label="按用户" name="user">
          <div class="cc-receiver-body">
            <div class="cc-receiver-left">
              <el-tree
                :data="approvalDeptTreeWithRoot"
                node-key="id"
                :props="ccDeptTreeProps"
                highlight-current
                @node-click="(node: any) => loadApprovalUsersByDept(node.id)"
              />
              <div class="empty-tip" v-if="!approvalDeptTree.length">暂无部门数据</div>
            </div>
            <div class="cc-receiver-right">
              <el-input
                v-model="approvalUserSearch"
                placeholder="搜索用户（姓名/账号）"
                clearable
                @input="loadApprovalUsersAll"
                @clear="loadApprovalUsersAll"
                style="margin-bottom:12px;"
              />
              <el-checkbox-group v-model="approvalTempSelectedUserIds" class="user-checkbox-group">
                <el-checkbox
                  v-for="user in approvalUserOptions"
                  :key="user.id"
                  :label="user.id"
                  :value="user.id"
                >
                  {{ getCcUserDisplayName(user) }}
                </el-checkbox>
              </el-checkbox-group>
              <div class="cc-user-pagination">
                <el-pagination
                  layout="prev, pager, next"
                  :current-page="approvalUserPage.current"
                  :page-size="approvalUserPage.size"
                  :total="approvalUserPage.total"
                  small
                  @current-change="handleApprovalUserPageChange"
                />
              </div>
              <div v-if="!approvalUserOptions.length" class="empty-tip">请选择左侧部门或搜索</div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 按部门选择 -->
        <el-tab-pane label="按部门" name="dept">
          <div class="cc-receiver-body">
            <div class="cc-receiver-left" style="width:100%;">
              <el-tree
                :data="approvalDeptTree"
                node-key="id"
                show-checkbox
                :props="ccDeptTreeProps"
                highlight-current
                ref="approvalDeptTreeRef"
                @check="handleApprovalDeptCheck"
              />
              <div class="empty-tip" v-if="!approvalDeptTree.length">暂无部门数据</div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 按角色选择 -->
        <el-tab-pane label="按角色" name="role">
          <div class="cc-receiver-body">
            <div class="cc-receiver-left" style="width:100%;">
              <el-checkbox-group v-model="approvalTempSelectedRoleIds">
                <el-checkbox
                  v-for="role in approvalRoleOptions"
                  :key="role.id"
                  :label="role.id"
                  :value="role.id"
                  style="display:block;margin:6px 0"
                >
                  {{ role.roleName || role.name }}
                </el-checkbox>
              </el-checkbox-group>
              <div class="empty-tip" v-if="!approvalRoleOptions.length">暂无角色数据</div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="approvalReceiverSelectorVisible = false">取消</el-button>
          <el-button @click="clearApprovalSelection" type="info">清空</el-button>
          <el-button type="primary" @click="confirmApprovalReceiverSelection">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, Plus, Mouse, Connection, Setting, User, InfoFilled, Refresh } from '@element-plus/icons-vue'
import { userApi, departmentApi, roleApi, userOrgApi, formApi } from '@/api'
import { useUserStore } from '@/stores/user'
import ConditionBuilder from './ConditionBuilder.vue'
import type {
  ProcessDefinition,
  ProcessNode,
  ProcessFlow,
  ApprovalNode,
  ConditionalBranchNode,
  ProcessVariable
} from '@/stores/workflow/processDesigner'

// 用户相关类型定义
interface User {
  id: number
  username: string
  realName?: string
  email?: string
}

// 消息配置类型定义
interface MessageTimingConfig {
  enabled: boolean
  title?: string
  content?: string
}

interface MessageConfig {
  onPending?: MessageTimingConfig
  onApprove?: MessageTimingConfig
  onReject?: MessageTimingConfig
  onComplete?: MessageTimingConfig
  params?: Record<string, { sourceType: string; value: string }>
}

// Props
interface Props {
  selectedNode?: ProcessNode | null
  selectedFlow?: ProcessFlow | null
  process: ProcessDefinition
}

const props = defineProps<Props>()

// Emits
const emit = defineEmits<{
  'node-updated': [nodeId: string, updates: Partial<ProcessNode>]
  'flow-updated': [flowId: string, updates: Partial<ProcessFlow>]
  'flow-deleted': [flowId: string]
  'variable-added': [variable: ProcessVariable]
  'variable-deleted': [variableName: string]
  'process-updated': [updates: Partial<ProcessDefinition>]
}>()

// 响应式数据

// 用户相关响应式数据
const userList = ref<User[]>([])
const userLoading = ref(false)
const allUsers = ref<User[]>([]) // 缓存所有用户

// 消息配置 Tab 状态
const activeMessageTab = ref('onPending')

// ========== 抄送人选择器相关数据 ==========
const ccReceiverSelectorVisible = ref(false)
const ccReceiverTab = ref('user')
const ccDeptTree = ref<any[]>([])
const ccRoleOptions = ref<any[]>([])
const ccUserOptions = ref<any[]>([])
const ccUserSearch = ref('')
const ccSelectedDeptId = ref<number | null>(null)
const ccDeptTreeRef = ref<any>(null)

// 临时选择状态（弹窗内使用）
const ccTempSelectedUserIds = ref<number[]>([])
const ccTempSelectedDeptIds = ref<number[]>([])
const ccTempSelectedRoleIds = ref<number[]>([])
const ccAllUsersCache = ref<any[]>([])

const ccUserPage = reactive({
  current: 1,
  size: 10,
  total: 0
})

// ========== 审批人选择器相关数据 ==========
const approvalReceiverSelectorVisible = ref(false)
const approvalReceiverTab = ref('user')
const approvalDeptTree = ref<any[]>([])
const approvalRoleOptions = ref<any[]>([])
const approvalUserOptions = ref<any[]>([])
const approvalUserSearch = ref('')
const approvalSelectedDeptId = ref<number | null>(null)
const approvalDeptTreeRef = ref<any>(null)

// 审批人临时选择状态（弹窗内使用）
const approvalTempSelectedUserIds = ref<number[]>([])
const approvalTempSelectedDeptIds = ref<number[]>([])
const approvalTempSelectedRoleIds = ref<number[]>([])
const approvalAllUsersCache = ref<any[]>([])

const approvalUserPage = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 部门树配置
const ccDeptTreeProps = {
  label: (data: any) =>
    data.deptName ||
    data.name ||
    data.label ||
    data.title ||
    data.dept ||
    data.departmentName ||
    `部门${data.id ?? ''}`,
  children: 'children'
}

// 带根节点的部门树
const ccDeptTreeWithRoot = computed(() => {
  const children = ccDeptTree.value || []
  return [
    {
      id: null,
      deptName: '全部用户',
      disabled: true,
      disableCheckbox: true,
      children
    }
  ]
})

// 部门ID到名称的映射
const ccFlattenDeptMap = computed(() => {
  const map = new Map()
  const walk = (nodes: any[] = []) => {
    nodes.forEach((n) => {
      map.set(n.id, ccDeptTreeProps.label(n))
      if (n.children && n.children.length) walk(n.children)
    })
  }
  walk(ccDeptTreeWithRoot.value)
  return map
})

// 角色ID到名称的映射
const ccRoleMap = computed(() => {
  const map = new Map()
  ;(ccRoleOptions.value || []).forEach((r: any) => map.set(r.id, r.roleName || r.name || `角色${r.id ?? ''}`))
  return map
})

// 获取部门标签
const getCcDeptLabel = (id: number) => ccFlattenDeptMap.value.get(id) || `部门${id ?? ''}`

// 获取角色标签
const getCcRoleLabel = (id: number) => ccRoleMap.value.get(id) || `角色${id ?? ''}`

// 临时选择的显示项（弹窗内使用）
const ccSelectedDisplayItems = computed(() => {
  const deptItems = ccTempSelectedDeptIds.value.map((id) => ({ id, type: 'dept', label: getCcDeptLabel(id) }))
  const roleItems = ccTempSelectedRoleIds.value.map((id) => ({ id, type: 'role', label: getCcRoleLabel(id) }))
  const userItems = ccTempSelectedUserIds.value.map((id) => {
    const user = ccAllUsersCache.value.find((u: any) => u.id === id) || ccUserOptions.value.find((u: any) => u.id === id)
    return { id, type: 'user', label: getCcUserDisplayName(user) }
  })
  return [...deptItems, ...roleItems, ...userItems]
})

// ========== 审批人选择器计算属性 ==========
// 带根节点的审批人部门树
const approvalDeptTreeWithRoot = computed(() => {
  const children = approvalDeptTree.value || []
  return [
    {
      id: null,
      deptName: '全部用户',
      disabled: true,
      disableCheckbox: true,
      children
    }
  ]
})

// 审批人部门ID到名称的映射
const approvalFlattenDeptMap = computed(() => {
  const map = new Map()
  const walk = (nodes: any[] = []) => {
    nodes.forEach((n) => {
      map.set(n.id, ccDeptTreeProps.label(n))
      if (n.children && n.children.length) walk(n.children)
    })
  }
  walk(approvalDeptTreeWithRoot.value)
  return map
})

// 审批人角色ID到名称的映射
const approvalRoleMap = computed(() => {
  const map = new Map()
  ;(approvalRoleOptions.value || []).forEach((r: any) => map.set(r.id, r.roleName || r.name || `角色${r.id ?? ''}`))
  return map
})

// 获取审批人部门标签
const getApprovalDeptLabel = (id: number) => approvalFlattenDeptMap.value.get(id) || `部门${id ?? ''}`

// 获取审批人角色标签
const getApprovalRoleLabel = (id: number) => approvalRoleMap.value.get(id) || `角色${id ?? ''}`

// 审批人临时选择的显示项（弹窗内使用）
const approvalSelectedDisplayItems = computed(() => {
  const deptItems = approvalTempSelectedDeptIds.value.map((id) => ({ id, type: 'dept', label: getApprovalDeptLabel(id) }))
  const roleItems = approvalTempSelectedRoleIds.value.map((id) => ({ id, type: 'role', label: getApprovalRoleLabel(id) }))
  const userItems = approvalTempSelectedUserIds.value.map((id) => {
    const user = approvalAllUsersCache.value.find((u: any) => u.id === id) || approvalUserOptions.value.find((u: any) => u.id === id)
    return { id, type: 'user', label: getCcUserDisplayName(user) }
  })
  return [...deptItems, ...roleItems, ...userItems]
})


// 计算属性
const selectedNode = computed(() => {
  // 当选中审批节点或抄送节点时，确保用户数据已加载
  if ((props.selectedNode?.type === 'APPROVAL' || props.selectedNode?.type === 'CC') && allUsers.value.length === 0) {
    loadAllUsers()
  }
  return props.selectedNode
})
const selectedFlow = computed(() => props.selectedFlow)
const process = computed(() => props.process)

// 从消息标题和内容中提取参数变量
const templateParams = computed(() => {
  const config = (selectedNode.value as any)?.messageConfig
  if (!config) return []

  // 从所有时机的标题和内容中提取参数
  const timings = ['onPending', 'onApprove', 'onReject', 'onComplete']
  let combined = ''
  for (const timing of timings) {
    if (config[timing]?.enabled) {
      combined += (config[timing]?.title || '') + ' ' + (config[timing]?.content || '') + ' '
    }
  }

  // 匹配 ${xxx} 格式的参数
  const regex = /\$\{([^}]+)\}/g
  const params: string[] = []
  let match
  while ((match = regex.exec(combined)) !== null) {
    if (!params.includes(match[1])) {
      params.push(match[1])
    }
  }
  return params
})

// 抄送节点消息参数提取
const ccTemplateParams = computed(() => {
  const config = (selectedNode.value as any)?.messageConfig
  if (!config || !config.enabled) return []

  const title = config.title || ''
  const content = config.content || ''
  const combined = title + ' ' + content

  const regex = /\$\{([^}]+)\}/g
  const params: string[] = []
  let match
  while ((match = regex.exec(combined)) !== null) {
    if (!params.includes(match[1])) {
      params.push(match[1])
    }
  }
  return params
})

// 方法
const getNodeTypeText = (type: string) => {
  const typeMap = {
    START: '开始节点',
    END: '结束节点',
    APPROVAL: '审批节点',
    CC: '抄送节点',
    CONDITIONAL_BRANCH: '条件分支',
    PARALLEL: '并行节点'
  }
  return typeMap[type as keyof typeof typeMap] || '未知节点'
}

const getNodeTypeTagType = (type: string) => {
  const typeMap = {
    START: 'success',
    END: 'danger',
    APPROVAL: 'primary',
    CC: 'info',
    CONDITIONAL_BRANCH: 'warning',
    PARALLEL: 'success'
  }
  return typeMap[type as keyof typeof typeMap] || 'info'
}

const getAssigneeTypePlaceholder = (type: string) => {
  const placeholderMap = {
    FIXED: '用户ID或用户名',
    ROLE: '角色标识',
    EXPRESSION: 'SpEL表达式，如：${currentUser.deptId}'
  }
  return placeholderMap[type as keyof typeof placeholderMap] || '请输入处理人'
}

const getNodeName = (nodeId: string) => {
  const node = process.value.nodes.find(n => n.id === nodeId)
  return node ? node.name : '未知节点'
}

const getTargetNodeName = (nodeId: string) => {
  const node = process.value.nodes.find(n => n.id === nodeId)
  return node ? node.name : '未知节点'
}

const getOutgoingFlows = (nodeId: string) => {
  return process.value.flows.filter(f => f.from === nodeId)
}

const getStartNodesCount = () => {
  return process.value.nodes.filter(n => n.type === 'START').length
}

const updateNodeProperty = (key: string, value: any) => {
  if (selectedNode.value) {
    const updates: any = { [key]: value }
    emit('node-updated', selectedNode.value.id, updates)
  }
}

// 处理审批人类型变更
const handleAssigneeTypeChange = (value: string) => {
  if (selectedNode.value) {
    const updates: any = {
      assigneeType: value,
      assignee: undefined // 清空原有的处理人配置
    }
    emit('node-updated', selectedNode.value.id, updates)
  }
}

// 处理抄送人类型变更
const handleCcAssigneeTypeChange = (value: string) => {
  if (selectedNode.value) {
    const updates: any = {
      assigneeType: value,
      assignee: undefined, // 清空原有的抄送人配置
      ccTargets: undefined // 清空多选配置
    }
    emit('node-updated', selectedNode.value.id, updates)
  }
}

// ========== 抄送人选择器方法 ==========

// 获取用户显示名称
const getCcUserDisplayName = (user: any) => {
  if (!user) return ''
  return user.realName || user.nickname || user.name || user.username || `用户${user.id ?? ''}`
}

// 获取抄送人显示文本
const getCcAssigneeDisplayText = () => {
  const ccTargets = (selectedNode.value as any)?.ccTargets
  if (!ccTargets || ccTargets.length === 0) {
    return ''
  }
  const count = ccTargets.length
  return `已选择 ${count} 个接收对象`
}

// 获取已选择的抄送人项
const getCcSelectedItems = () => {
  const ccTargets = (selectedNode.value as any)?.ccTargets || []
  return ccTargets.map((target: any) => {
    let label = ''
    if (target.type === 'user') {
      const user = allUsers.value.find((u: any) => u.id === target.id)
      label = user ? getCcUserDisplayName(user) : `用户${target.id}`
    } else if (target.type === 'dept') {
      label = getCcDeptLabel(target.id)
    } else if (target.type === 'role') {
      label = getCcRoleLabel(target.id)
    }
    return { ...target, label }
  })
}

// 移除已选择的抄送人项
const removeCcSelectedItem = (item: any) => {
  const ccTargets = (selectedNode.value as any)?.ccTargets || []
  const newTargets = ccTargets.filter((t: any) => !(t.type === item.type && t.id === item.id))
  emit('node-updated', selectedNode.value!.id, { ccTargets: newTargets })
}

// 打开抄送人选择器
const openCcReceiverSelector = async () => {
  ccReceiverSelectorVisible.value = true

  // 初始化临时选择状态（从节点配置中恢复）
  const ccTargets = (selectedNode.value as any)?.ccTargets || []
  ccTempSelectedUserIds.value = ccTargets.filter((t: any) => t.type === 'user').map((t: any) => t.id)
  ccTempSelectedDeptIds.value = ccTargets.filter((t: any) => t.type === 'dept').map((t: any) => t.id)
  ccTempSelectedRoleIds.value = ccTargets.filter((t: any) => t.type === 'role').map((t: any) => t.id)

  // 加载部门树
  if (!ccDeptTree.value.length) {
    try {
      const res = await departmentApi.getDepartmentTree()
      ccDeptTree.value = res?.data || []
      if ((!ccDeptTree.value || !ccDeptTree.value.length) && departmentApi.getAllDepartments) {
        const allRes = await departmentApi.getAllDepartments()
        const flat = allRes?.data || []
        const map = new Map()
        flat.forEach((d: any) => {
          map.set(d.id, { ...d, children: [] })
        })
        const roots: any[] = []
        flat.forEach((d: any) => {
          const node = map.get(d.id)
          if (d.parentId && map.get(d.parentId)) {
            map.get(d.parentId).children.push(node)
          } else {
            roots.push(node)
          }
        })
        ccDeptTree.value = roots
      }
    } catch (e) {
      console.error('获取部门失败', e)
    }
  }

  // 加载角色列表
  if (!ccRoleOptions.value.length) {
    try {
      const res = await roleApi.getAllRoles()
      ccRoleOptions.value = res?.data || []
    } catch (e) {
      console.error('获取角色失败', e)
    }
  }

  // 默认加载全部用户
  if (ccReceiverTab.value === 'user') {
    await loadCcUsersAll()
  }
}

// 按部门加载用户
const loadCcUsersByDept = async (deptId: number | null) => {
  ccSelectedDeptId.value = deptId
  ccUserOptions.value = []
  if (deptId === null || deptId === undefined) {
    await loadCcUsersAll()
    return
  }
  try {
    const res = await userOrgApi.getUsersByOrgId(deptId, {
      current: ccUserPage.current,
      size: ccUserPage.size,
      keyword: ccUserSearch.value
    })
    const users = res?.data?.records || res?.data?.list || res?.data || []
    ccUserPage.total = Number(res?.data?.total || users.length || 0)
    ccUserOptions.value = users
  } catch (e) {
    console.error('按部门获取用户失败', e)
    ccUserOptions.value = []
    ccUserPage.total = 0
  }
}

// 加载全部用户
const loadCcUsersAll = async () => {
  try {
    const res = await userApi.getUserList({
      current: ccUserPage.current,
      size: ccUserPage.size,
      keyword: ccUserSearch.value
    })
    const users = res?.data?.records || res?.data?.list || res?.data || []
    ccUserPage.total = Number(res?.data?.total || users.length || 0)
    ccUserOptions.value = users
    ccAllUsersCache.value = [...ccAllUsersCache.value, ...users.filter((u: any) => !ccAllUsersCache.value.find((c: any) => c.id === u.id))]
  } catch (e) {
    console.error('获取用户失败', e)
    ccUserOptions.value = []
    ccUserPage.total = 0
  }
}

// 处理部门选择
const handleCcDeptCheck = (data: any, state: any) => {
  ccTempSelectedDeptIds.value = (state?.checkedKeys || []).filter((id: any) => id !== undefined && id !== null)
}

// 处理用户分页
const handleCcUserPageChange = (page: number) => {
  ccUserPage.current = page
  if (ccReceiverTab.value === 'user') {
    loadCcUsersByDept(ccSelectedDeptId.value)
  }
}

// 移除临时选择项
const removeCcTempSelectedItem = (item: any) => {
  if (item.type === 'dept') {
    ccTempSelectedDeptIds.value = ccTempSelectedDeptIds.value.filter((id) => id !== item.id)
  } else if (item.type === 'role') {
    ccTempSelectedRoleIds.value = ccTempSelectedRoleIds.value.filter((id) => id !== item.id)
  } else if (item.type === 'user') {
    ccTempSelectedUserIds.value = ccTempSelectedUserIds.value.filter((id) => id !== item.id)
  }
}

// 清空选择
const clearCcSelection = () => {
  ccTempSelectedUserIds.value = []
  ccTempSelectedDeptIds.value = []
  ccTempSelectedRoleIds.value = []
  // 清空部门树的选中状态
  if (ccDeptTreeRef.value) {
    ccDeptTreeRef.value.setCheckedKeys([])
  }
}

// 确认选择
const confirmCcReceiverSelection = () => {
  const ccTargets = [
    ...ccTempSelectedDeptIds.value.map((id) => ({ id, type: 'dept' })),
    ...ccTempSelectedRoleIds.value.map((id) => ({ id, type: 'role' })),
    ...ccTempSelectedUserIds.value.map((id) => ({ id, type: 'user' }))
  ]

  if (selectedNode.value) {
    emit('node-updated', selectedNode.value.id, { ccTargets })
  }

  ccReceiverSelectorVisible.value = false
}

// ========== 审批人选择器方法 ==========

// 获取审批人显示文本
const getApprovalAssigneeDisplayText = () => {
  const approvalTargets = (selectedNode.value as any)?.approvalTargets
  if (!approvalTargets || approvalTargets.length === 0) {
    return ''
  }
  const count = approvalTargets.length
  return `已选择 ${count} 个审批对象`
}

// 获取已选择的审批人项
const getApprovalSelectedItems = () => {
  const approvalTargets = (selectedNode.value as any)?.approvalTargets || []
  return approvalTargets.map((target: any) => {
    let label = ''
    if (target.type === 'user') {
      const user = allUsers.value.find((u: any) => u.id === target.id)
      label = user ? getCcUserDisplayName(user) : `用户${target.id}`
    } else if (target.type === 'dept') {
      label = getApprovalDeptLabel(target.id)
    } else if (target.type === 'role') {
      label = getApprovalRoleLabel(target.id)
    }
    return { ...target, label }
  })
}

// 移除已选择的审批人项
const removeApprovalSelectedItem = (item: any) => {
  const approvalTargets = (selectedNode.value as any)?.approvalTargets || []
  const newTargets = approvalTargets.filter((t: any) => !(t.type === item.type && t.id === item.id))
  emit('node-updated', selectedNode.value!.id, { approvalTargets: newTargets })
}

// 打开审批人选择器
const openApprovalReceiverSelector = async () => {
  approvalReceiverSelectorVisible.value = true

  // 初始化临时选择状态（从节点配置中恢复）
  const approvalTargets = (selectedNode.value as any)?.approvalTargets || []
  approvalTempSelectedUserIds.value = approvalTargets.filter((t: any) => t.type === 'user').map((t: any) => t.id)
  approvalTempSelectedDeptIds.value = approvalTargets.filter((t: any) => t.type === 'dept').map((t: any) => t.id)
  approvalTempSelectedRoleIds.value = approvalTargets.filter((t: any) => t.type === 'role').map((t: any) => t.id)

  // 加载部门树（复用抄送的数据或重新加载）
  if (!approvalDeptTree.value.length) {
    try {
      const res = await departmentApi.getDepartmentTree()
      approvalDeptTree.value = res?.data || []
      if ((!approvalDeptTree.value || !approvalDeptTree.value.length) && departmentApi.getAllDepartments) {
        const allRes = await departmentApi.getAllDepartments()
        const flat = allRes?.data || []
        const map = new Map()
        flat.forEach((d: any) => {
          map.set(d.id, { ...d, children: [] })
        })
        const roots: any[] = []
        flat.forEach((d: any) => {
          const node = map.get(d.id)
          if (d.parentId && map.get(d.parentId)) {
            map.get(d.parentId).children.push(node)
          } else {
            roots.push(node)
          }
        })
        approvalDeptTree.value = roots
      }
    } catch (e) {
      console.error('获取部门失败', e)
    }
  }

  // 加载角色列表
  if (!approvalRoleOptions.value.length) {
    try {
      const res = await roleApi.getAllRoles()
      approvalRoleOptions.value = res?.data || []
    } catch (e) {
      console.error('获取角色失败', e)
    }
  }

  // 默认加载全部用户
  if (approvalReceiverTab.value === 'user') {
    await loadApprovalUsersAll()
  }
}

// 按部门加载审批人用户
const loadApprovalUsersByDept = async (deptId: number | null) => {
  approvalSelectedDeptId.value = deptId
  approvalUserOptions.value = []
  if (deptId === null || deptId === undefined) {
    await loadApprovalUsersAll()
    return
  }
  try {
    const res = await userOrgApi.getUsersByOrgId(deptId, {
      current: approvalUserPage.current,
      size: approvalUserPage.size,
      keyword: approvalUserSearch.value
    })
    const users = res?.data?.records || res?.data?.list || res?.data || []
    approvalUserPage.total = Number(res?.data?.total || users.length || 0)
    approvalUserOptions.value = users
  } catch (e) {
    console.error('按部门获取用户失败', e)
    approvalUserOptions.value = []
    approvalUserPage.total = 0
  }
}

// 加载全部审批人用户
const loadApprovalUsersAll = async () => {
  try {
    const res = await userApi.getUserList({
      current: approvalUserPage.current,
      size: approvalUserPage.size,
      keyword: approvalUserSearch.value
    })
    const users = res?.data?.records || res?.data?.list || res?.data || []
    approvalUserPage.total = Number(res?.data?.total || users.length || 0)
    approvalUserOptions.value = users
    approvalAllUsersCache.value = [...approvalAllUsersCache.value, ...users.filter((u: any) => !approvalAllUsersCache.value.find((c: any) => c.id === u.id))]
  } catch (e) {
    console.error('获取用户失败', e)
    approvalUserOptions.value = []
    approvalUserPage.total = 0
  }
}

// 处理审批人部门选择
const handleApprovalDeptCheck = (data: any, state: any) => {
  approvalTempSelectedDeptIds.value = (state?.checkedKeys || []).filter((id: any) => id !== undefined && id !== null)
}

// 处理审批人用户分页
const handleApprovalUserPageChange = (page: number) => {
  approvalUserPage.current = page
  if (approvalReceiverTab.value === 'user') {
    loadApprovalUsersByDept(approvalSelectedDeptId.value)
  }
}

// 移除审批人临时选择项
const removeApprovalTempSelectedItem = (item: any) => {
  if (item.type === 'dept') {
    approvalTempSelectedDeptIds.value = approvalTempSelectedDeptIds.value.filter((id) => id !== item.id)
  } else if (item.type === 'role') {
    approvalTempSelectedRoleIds.value = approvalTempSelectedRoleIds.value.filter((id) => id !== item.id)
  } else if (item.type === 'user') {
    approvalTempSelectedUserIds.value = approvalTempSelectedUserIds.value.filter((id) => id !== item.id)
  }
}

// 清空审批人选择
const clearApprovalSelection = () => {
  approvalTempSelectedUserIds.value = []
  approvalTempSelectedDeptIds.value = []
  approvalTempSelectedRoleIds.value = []
  // 清空部门树的选中状态
  if (approvalDeptTreeRef.value) {
    approvalDeptTreeRef.value.setCheckedKeys([])
  }
}

// 确认审批人选择
const confirmApprovalReceiverSelection = () => {
  const approvalTargets = [
    ...approvalTempSelectedDeptIds.value.map((id) => ({ id, type: 'dept' })),
    ...approvalTempSelectedRoleIds.value.map((id) => ({ id, type: 'role' })),
    ...approvalTempSelectedUserIds.value.map((id) => ({ id, type: 'user' }))
  ]

  if (selectedNode.value) {
    emit('node-updated', selectedNode.value.id, { approvalTargets })
  }

  approvalReceiverSelectorVisible.value = false
}

// 添加并行分支
const addParallelBranch = () => {
  if (!selectedNode.value || selectedNode.value.type !== 'PARALLEL') return

  const branches = (selectedNode.value as any).branches || []
  if (branches.length >= 10) {
    ElMessage.warning('并行分支最多支持10个')
    return
  }

  const newBranch = {
    id: `branch_${Date.now()}`,
    name: `分支${branches.length + 1}`,
    description: '',
    nodes: [],
    condition: null
  }

  const updatedBranches = [...branches, newBranch]
  emit('node-updated', selectedNode.value.id, { branches: updatedBranches })
}

// 删除并行分支
const removeParallelBranch = (branchId: string) => {
  if (!selectedNode.value || selectedNode.value.type !== 'PARALLEL') return

  const branches = (selectedNode.value as any).branches || []
  const updatedBranches = branches.filter((b: any) => b.id !== branchId)

  if (updatedBranches.length < 2) {
    ElMessage.warning('并行节点至少需要两个分支')
    return
  }

  emit('node-updated', selectedNode.value.id, { branches: updatedBranches })
}

// 更新并行分支名称
const updateParallelBranchName = (branchId: string, name: string) => {
  if (!selectedNode.value || selectedNode.value.type !== 'PARALLEL') return

  const branches = (selectedNode.value as any).branches || []
  const updatedBranches = branches.map((b: any) =>
    b.id === branchId ? { ...b, name } : b
  )

  emit('node-updated', selectedNode.value.id, { branches: updatedBranches })
}

// 更新并行分支描述
const updateParallelBranchDescription = (branchId: string, description: string) => {
  if (!selectedNode.value || selectedNode.value.type !== 'PARALLEL') return

  const branches = (selectedNode.value as any).branches || []
  const updatedBranches = branches.map((b: any) =>
    b.id === branchId ? { ...b, description } : b
  )

  emit('node-updated', selectedNode.value.id, { branches: updatedBranches })
}

// 更新并行分支条件
const updateParallelBranchCondition = (branchId: string, condition: string) => {
  if (!selectedNode.value || selectedNode.value.type !== 'PARALLEL') return

  const branches = (selectedNode.value as any).branches || []
  const updatedBranches = branches.map((b: any) =>
    b.id === branchId ? { ...b, condition: condition || null } : b
  )

  emit('node-updated', selectedNode.value.id, { branches: updatedBranches })
}

// 获取可用作人员字段的流程变量
const getUserVariables = () => {
  if (!process.value.variables) {
    return []
  }

  // 返回所有变量（任何类型的变量都可能包含用户ID）
  // 如果要严格限制，可以只返回 string 或 number 类型的变量
  return process.value.variables.filter(v =>
    v.type === 'string' || v.type === 'number' || v.type === 'long'
  )
}

// 获取人员选择组件变量（user-select类型）
const getUserSelectVariables = () => {
  if (!process.value.variables) {
    return []
  }
  return process.value.variables.filter(v => v.type === 'user-select')
}

// 获取部门选择组件变量（department-select类型）
const getDeptSelectVariables = () => {
  if (!process.value.variables) {
    return []
  }
  return process.value.variables.filter(v => v.type === 'department-select')
}

// 获取角色选择组件变量（role-select类型）
const getRoleSelectVariables = () => {
  if (!process.value.variables) {
    return []
  }
  return process.value.variables.filter(v => v.type === 'role-select')
}

const updateNodePosition = (axis: 'x' | 'y', value: number) => {
  if (selectedNode.value) {
    const newPosition = { ...selectedNode.value.position, [axis]: value }
    emit('node-updated', selectedNode.value.id, { position: newPosition })
  }
}

const updateFlowProperty = (flowId: string, key: string, value: any) => {
  emit('flow-updated', flowId, { [key]: value })
}

const updateProcessProperty = (key: string, value: any) => {
  emit('process-updated', { [key]: value })
}

const deleteSelectedNode = async () => {
  if (!selectedNode.value) return

  try {
    await ElMessageBox.confirm(
      `确定要删除节点"${selectedNode.value.name}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    // 由父组件处理删除逻辑
    // emit('node-deleted', selectedNode.value.id)
  } catch (error) {
    // 用户取消
  }
}

const deleteSelectedFlow = async () => {
  if (!selectedFlow.value) return

  try {
    await ElMessageBox.confirm(
      '确定要删除此连接吗？',
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    emit('flow-deleted', selectedFlow.value.id)
  } catch (error) {
    // 用户取消
  }
}

const deleteFlow = (flowId: string) => {
  emit('flow-deleted', flowId)
}

// 用户查询相关方法
const userStore = useUserStore()

const loadAllUsers = async () => {
  try {
    userLoading.value = true
    // 使用缓存的用户数据
    const users = await userStore.loadEnabledUsers()

    allUsers.value = users
    userList.value = allUsers.value

    console.log('[ProcessPropertiesPanel] 用户列表加载完成，总数:', userList.value.length)
  } catch (error) {
    console.error('[ProcessPropertiesPanel] 加载用户列表出错:', error)
    ElMessage.error('加载用户列表失败')
  } finally {
    userLoading.value = false
  }
}

const searchUsers = async (query: string) => {
  if (!query) {
    userList.value = allUsers.value
    return
  }

  // 本地搜索（从缓存的用户列表中过滤）
  const filteredUsers = allUsers.value.filter(user => {
    const searchText = query.toLowerCase()
    return (
      (user.realName && user.realName.toLowerCase().includes(searchText)) ||
      user.username.toLowerCase().includes(searchText) ||
      (user.email && user.email.toLowerCase().includes(searchText))
    )
  })

  userList.value = filteredUsers
}

// 获取处理人值（保持原始类型，不做转换）
const getAssigneeValue = (assignee: string | number | undefined): string | number | undefined => {
  // console.log(`[ProcessPropertiesPanel] getAssigneeValue: assignee=${assignee}, type=${typeof assignee}`)

  if (!assignee) {
    return undefined
  }

  // 直接返回原值，不做类型转换
  // 因为后端返回的用户ID可能是字符串类型，需要保持一致
  // console.log(`[ProcessPropertiesPanel] 返回原值: ${assignee}`)
  return assignee
}

// 组件挂载时加载用户列表
onMounted(() => {
  loadAllUsers()
})

// 获取消息时机启用状态
const getMessageTimingEnabled = (timing: string): boolean => {
  const config = (selectedNode.value as any)?.messageConfig
  return config?.[timing]?.enabled || false
}

// 切换消息时机启用状态
const toggleMessageTiming = (timing: string, enabled: boolean) => {
  if (!selectedNode.value) return

  const currentConfig = (selectedNode.value as any).messageConfig || {}
  const timingConfig = currentConfig[timing] || {}

  const newConfig: MessageConfig = {
    ...currentConfig,
    [timing]: {
      ...timingConfig,
      enabled
    }
  }

  emit('node-updated', selectedNode.value.id, { messageConfig: newConfig })
}

// 获取消息时机字段值
const getMessageTimingField = (timing: string, field: string): string => {
  const config = (selectedNode.value as any)?.messageConfig
  return config?.[timing]?.[field] || ''
}

// 更新消息时机字段值
const updateMessageTimingField = (timing: string, field: string, value: string) => {
  if (!selectedNode.value) return

  const currentConfig = (selectedNode.value as any).messageConfig || {}
  const timingConfig = currentConfig[timing] || { enabled: true }

  const newConfig: MessageConfig = {
    ...currentConfig,
    [timing]: {
      ...timingConfig,
      [field]: value
    }
  }

  emit('node-updated', selectedNode.value.id, { messageConfig: newConfig })
}

// 处理消息启用状态变更（已废弃，保留兼容）
const handleMessageEnabledChange = (enabled: boolean) => {
  // 已替换为 toggleMessageTiming
}

// 更新消息配置属性（已废弃，保留兼容）
const updateMessageConfigProperty = (key: string, value: any) => {
  // 已替换为 updateMessageTimingField
}

// ========== 抄送节点消息配置方法 ==========

// 获取抄送消息启用状态
const getCcMessageEnabled = (): boolean => {
  const config = (selectedNode.value as any)?.messageConfig
  return config?.enabled || false
}

// 切换抄送消息启用状态
const toggleCcMessageEnabled = (enabled: boolean) => {
  if (!selectedNode.value) return

  const currentConfig = (selectedNode.value as any).messageConfig || {}
  const newConfig = {
    ...currentConfig,
    enabled
  }

  emit('node-updated', selectedNode.value.id, { messageConfig: newConfig })
}

// 获取抄送消息字段值
const getCcMessageField = (field: string): string => {
  const config = (selectedNode.value as any)?.messageConfig
  return config?.[field] || ''
}

// 更新抄送消息字段值
const updateCcMessageField = (field: string, value: string) => {
  if (!selectedNode.value) return

  const currentConfig = (selectedNode.value as any).messageConfig || { enabled: true }
  const newConfig = {
    ...currentConfig,
    [field]: value
  }

  emit('node-updated', selectedNode.value.id, { messageConfig: newConfig })
}

// 获取抄送参数来源类型
const getCcParamSourceType = (paramName: string): string => {
  const config = (selectedNode.value as any)?.messageConfig
  return config?.params?.[paramName]?.sourceType || 'FIXED'
}

// 更新抄送参数来源类型
const updateCcParamSourceType = (paramName: string, sourceType: string) => {
  if (!selectedNode.value) return

  const currentConfig = (selectedNode.value as any).messageConfig || { enabled: true }
  const params = currentConfig.params || {}

  const newConfig = {
    ...currentConfig,
    params: {
      ...params,
      [paramName]: {
        ...(params[paramName] || {}),
        sourceType,
        value: '' // 清空值
      }
    }
  }

  emit('node-updated', selectedNode.value.id, { messageConfig: newConfig })
}

// 获取抄送参数值
const getCcParamValue = (paramName: string): string => {
  const config = (selectedNode.value as any)?.messageConfig
  return config?.params?.[paramName]?.value || ''
}

// 更新抄送参数值
const updateCcParamValue = (paramName: string, value: string) => {
  if (!selectedNode.value) return

  const currentConfig = (selectedNode.value as any).messageConfig || { enabled: true }
  const params = currentConfig.params || {}

  const newConfig = {
    ...currentConfig,
    params: {
      ...params,
      [paramName]: {
        ...(params[paramName] || { sourceType: 'FIXED' }),
        value
      }
    }
  }

  emit('node-updated', selectedNode.value.id, { messageConfig: newConfig })
}

// 获取参数来源类型
const getParamSourceType = (paramName: string): string => {
  const params = (selectedNode.value as any)?.messageConfig?.params || {}
  return params[paramName]?.sourceType || 'FIXED'
}

// 获取参数值
const getParamValue = (paramName: string): string => {
  const params = (selectedNode.value as any)?.messageConfig?.params || {}
  return params[paramName]?.value || ''
}

// 更新参数来源类型
const updateParamSourceType = (paramName: string, sourceType: string) => {
  if (!selectedNode.value) return

  const currentConfig = (selectedNode.value as any).messageConfig || { enabled: true }
  const currentParams = currentConfig.params || {}

  const newConfig: MessageConfig = {
    ...currentConfig,
    params: {
      ...currentParams,
      [paramName]: {
        sourceType,
        value: '' // 切换类型时清空值
      }
    }
  }

  emit('node-updated', selectedNode.value.id, { messageConfig: newConfig })
}

// 更新参数值
const updateParamValue = (paramName: string, value: string) => {
  if (!selectedNode.value) return

  const currentConfig = (selectedNode.value as any).messageConfig || { enabled: true }
  const currentParams = currentConfig.params || {}
  const currentParam = currentParams[paramName] || { sourceType: 'FIXED' }

  const newConfig: MessageConfig = {
    ...currentConfig,
    params: {
      ...currentParams,
      [paramName]: {
        ...currentParam,
        value
      }
    }
  }

  emit('node-updated', selectedNode.value.id, { messageConfig: newConfig })
}
</script>

<style scoped lang="scss">
.process-properties-panel {
  padding: 12px;
  min-height: 100%;
  display: flex;
  flex-direction: column;
  overflow-x: hidden;
}

.property-section {
  margin-bottom: 12px;

  h4 {
    margin: 0 0 8px 0;
    font-size: 13px;
    font-weight: 600;
    color: var(--text-primary, #1f2937);
    border-bottom: 1px solid var(--border-color, #e5e7eb);
    padding-bottom: 6px;
    background: transparent;
    text-align: left;
  }

  h5 {
    margin: 8px 0 6px 0;
    font-size: 12px;
    color: var(--text-secondary, #6b7280);
    background: transparent;
    text-align: left;
  }

  // 表单项样式优化
  :deep(.el-form) {
    .el-form-item {
      margin-bottom: 10px;

      .el-form-item__label {
        text-align: left;
        font-size: 12px;
        color: var(--text-secondary, #6b7280);
        padding-right: 8px;
      }

      .el-form-item__content {
        text-align: left;
      }
    }
  }

  // 输入框和选择器宽度控制
  :deep(.el-input),
  :deep(.el-select),
  :deep(.el-input-number) {
    width: 100%;
    max-width: 100%;
  }

  // 文本域样式
  :deep(.el-textarea__inner) {
    resize: none;
  }
}

.flows-config {
  .no-flows {
    text-align: center;
    padding: 20px 0;
  }

  .flow-config-item {
    margin-bottom: 12px;
    padding: 8px;
    border: 1px solid var(--border-color, #e5e7eb);
    border-radius: 4px;
    background: var(--bg-tertiary, #f1f5f9);

    .flow-info {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;

      .flow-label {
        font-size: 12px;
        color: var(--text-secondary, #6b7280);
      }
    }
  }
}

.condition-help {
  margin-top: 4px;
}

.property-actions {
  margin-top: 20px;
  text-align: center;
}

.property-section {
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;

    h4 {
      margin: 0;
      font-size: 14px;
      font-weight: 600;
      color: var(--text-primary, #1f2937);
    }
  }
}

.variables-list {
  .no-variables {
    text-align: center;
    padding: 20px 0;
  }

  .variable-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px;
    border: 1px solid var(--border-color, #e5e7eb);
    border-radius: 4px;
    margin-bottom: 8px;
    background: var(--bg-card, #ffffff);

    .variable-info {
      flex: 1;

      .variable-name {
        font-weight: 500;
        color: var(--text-primary, #1f2937);
        margin-bottom: 4px;
      }

      .variable-details {
        display: flex;
        align-items: center;
        gap: 8px;

        .variable-label {
          font-size: 12px;
          color: var(--text-secondary, #6b7280);
        }
      }
    }
  }
}

.operation-guide {
  background: var(--bg-tertiary, #f1f5f9);
  border: 1px solid var(--border-color, #e5e7eb);

  .guide-item {
    display: flex;
    align-items: flex-start;
    margin-bottom: 12px;

    &:last-child {
      margin-bottom: 0;
    }

    .guide-icon {
      font-size: 16px;
      color: var(--primary-color, #6366f1);
      margin-right: 8px;
      margin-top: 2px;
    }

    .guide-text {
      flex: 1;

      .guide-title {
        font-size: 13px;
        font-weight: 500;
        color: var(--text-primary, #1f2937);
        margin-bottom: 2px;
      }

      .guide-desc {
        font-size: 11px;
        color: var(--text-secondary, #6b7280);
        line-height: 1.4;
      }
    }
  }
}

:deep(.el-form-item) {
  margin-bottom: 12px;
}

:deep(.el-form-item__label) {
  font-size: 12px;
  color: var(--text-secondary, #6b7280);
  padding-right: 8px;
}

:deep(.el-input), :deep(.el-select) {
  .el-input__inner, .el-select__wrapper {
    font-size: 12px;
  }
}

:deep(.el-empty__description) {
  font-size: 12px;
}

// 用户选项样式
.user-option {
  display: flex;
  flex-direction: column;

  .user-name {
    font-size: 14px;
    color: var(--text-primary, #1f2937);
    font-weight: 500;
  }

  .user-info {
    font-size: 12px;
    color: var(--text-secondary, #6b7280);
    margin-top: 2px;
  }
}

// 处理人类型选项样式
.assignee-type-option {
  display: flex;
  flex-direction: column;
  gap: 2px;

  span {
    font-size: 13px;
    color: var(--text-primary, #1f2937);
  }

  .el-text {
    font-size: 11px;
  }
}

// 并行分支配置样式
.parallel-branches {
  margin-top: 8px;

  .branches-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;

    h5 {
      font-size: 12px;
      font-weight: 600;
      color: var(--text-primary, #1f2937);
      margin: 0;
    }
  }

  .branch-item {
    border: 1px solid var(--border-color, #e5e7eb);
    border-radius: 4px;
    padding: 10px;
    margin-bottom: 8px;
    background: var(--bg-tertiary, #f1f5f9);

    &:last-child {
      margin-bottom: 0;
    }

    .branch-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 6px;

      .branch-title {
        font-size: 12px;
        font-weight: 600;
        color: var(--text-primary, #1f2937);
      }
    }

    // 分支内表单紧凑样式
    :deep(.el-form-item) {
      margin-bottom: 8px;

      .el-form-item__label {
        font-size: 11px;
      }
    }
  }

  .no-branches {
    text-align: center;
    padding: 16px 0;
  }
}

// 变量选项样式（用于动态人员）
.variable-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;

  .variable-name {
    flex: 1;
    font-size: 13px;
    color: var(--text-primary, #1f2937);
  }

  .el-tag {
    margin-left: 8px;
  }
}

// 消息模板选项样式
.template-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;

  .template-name {
    flex: 1;
    font-size: 13px;
    color: var(--text-primary, #1f2937);
  }

  .el-tag {
    margin-left: 8px;
  }
}

// 模板预览样式
.template-preview {
  padding: 12px;
  background: var(--bg-tertiary, #f1f5f9);
  border: 1px solid var(--border-color, #e5e7eb);
  border-radius: 6px;
  width: 100%;

  .template-title {
    font-size: 13px;
    font-weight: 600;
    color: var(--text-primary, #1f2937);
    margin-bottom: 8px;
  }

  .template-content {
    font-size: 12px;
    color: var(--text-secondary, #6b7280);
    line-height: 1.5;
    white-space: pre-wrap;
    word-break: break-all;
  }
}

// 参数配置样式
.params-section {
  margin-top: 8px;

  .params-header {
    font-size: 11px;
    font-weight: 600;
    color: var(--text-primary, #1f2937);
    margin-bottom: 6px;
    padding-bottom: 4px;
    border-bottom: 1px dashed var(--border-color, #e5e7eb);
    text-align: left;
  }
}

.params-config {
  width: 100%;

  .param-item {
    padding: 6px 8px;
    background: var(--bg-tertiary, #f1f5f9);
    border: 1px solid var(--border-color, #e5e7eb);
    border-radius: 4px;
    margin-bottom: 4px;

    &:last-child {
      margin-bottom: 0;
    }

    .param-header {
      margin-bottom: 4px;
      text-align: left;

      .param-name {
        font-size: 10px;
        font-weight: 600;
        color: var(--primary-color, #6366f1);
        background: var(--primary-color-light, #eef2ff);
        padding: 1px 4px;
        border-radius: 2px;
      }
    }

    .param-body {
      display: flex;
      align-items: center;

      .el-select,
      .el-input {
        font-size: 11px;
      }
    }
  }
}
// 消息配置 Tab 样式
.message-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 6px;
  }

  :deep(.el-tabs__nav-wrap) {
    padding: 0;
  }

  :deep(.el-tabs__item) {
    height: 26px;
    line-height: 26px;
    padding: 0 10px;
    font-size: 11px;
  }

  :deep(.el-tabs__content) {
    padding: 0;
  }
}

.timing-config {
  :deep(.el-form-item) {
    margin-bottom: 6px;
  }

  :deep(.el-form-item__label) {
    font-size: 11px;
  }

  :deep(.el-input__inner),
  :deep(.el-textarea__inner) {
    font-size: 11px;
  }
}

// ========== 审批方式样式 ==========
.approval-mode-label {
  margin-right: 2px;
  font-size: 12px;
}

.info-icon {
  color: var(--el-color-info);
  font-size: 12px;
  cursor: help;
  vertical-align: middle;
}

.approval-mode-hint {
  margin-top: 6px;
  padding: 6px 10px;
  background: var(--el-fill-color-lighter);
  border-radius: 4px;
  border-left: 2px solid var(--el-color-primary);

  .el-text {
    font-size: 11px;
    line-height: 1.4;
  }
}

// ========== 抄送人选择器样式 ==========
.cc-selected-tags {
  margin-top: 8px;

  .tag-type {
    color: var(--el-color-primary);
    font-weight: 500;
    margin-right: 2px;
  }
}

.cc-selected-summary {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 14px 16px;
  border: 1px solid var(--el-border-color-lighter);
  border-radius: 10px;
  background: var(--el-fill-color-lighter);
  margin-bottom: 16px;
  min-height: 48px;

  &.empty {
    justify-content: center;
    align-items: center;
    color: var(--el-text-color-placeholder);
    font-size: 14px;
  }

  .summary-title {
    font-size: 14px;
    font-weight: 500;
    color: var(--el-text-color-primary);
    white-space: nowrap;
    line-height: 24px;
  }

  .summary-chips {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    flex: 1;

    .el-tag {
      border-radius: 6px;
      padding: 0 10px;
      height: 26px;
      line-height: 24px;
      font-size: 13px;
      background: var(--el-bg-color);
      border: 1px solid var(--el-border-color);

      .chip-type {
        color: var(--el-color-primary);
        font-weight: 500;
      }
    }
  }
}

.cc-receiver-body {
  display: flex;
  gap: 20px;
  min-height: 320px;

  .cc-receiver-left {
    width: 200px;
    background: var(--el-fill-color-lighter);
    border: 1px solid var(--el-border-color-lighter);
    border-radius: 8px;
    padding: 12px;
    overflow: auto;
    max-height: 350px;

    :deep(.el-tree) {
      background: transparent;

      .el-tree-node__content {
        height: 32px;
        border-radius: 6px;
        padding-left: 6px;

        &:hover {
          background: var(--el-fill-color);
        }
      }

      .el-tree-node.is-current > .el-tree-node__content {
        background: var(--el-color-primary-light-9);
        color: var(--el-color-primary);
      }
    }

    :deep(.el-checkbox-group) {
      .el-checkbox {
        display: flex;
        margin: 0;
        padding: 8px 10px;
        border-radius: 6px;

        &:hover {
          background: var(--el-fill-color);
        }

        &.is-checked {
          background: var(--el-color-primary-light-9);
        }
      }
    }
  }

  .cc-receiver-right {
    flex: 1;
    display: flex;
    flex-direction: column;

    .user-checkbox-group {
      background: var(--el-fill-color-lighter);
      border: 1px solid var(--el-border-color-lighter);
      border-radius: 8px;
      padding: 10px;
      flex: 1;
      overflow: auto;
      max-height: 260px;

      :deep(.el-checkbox) {
        display: flex;
        margin: 0;
        padding: 8px 12px;
        border-radius: 6px;
        width: 100%;

        &:hover {
          background: var(--el-fill-color);
        }

        &.is-checked {
          background: var(--el-color-primary-light-9);
        }
      }
    }
  }
}

.cc-user-pagination {
  margin-top: 12px;
  display: flex;
  justify-content: center;
}

.empty-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30px 15px;
  color: var(--el-text-color-placeholder);
  font-size: 13px;
  height: 100%;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
