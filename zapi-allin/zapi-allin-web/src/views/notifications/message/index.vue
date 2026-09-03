<template>
  <div class="notifications-page">
    <div class="page-header">
      <div class="header-content">
        <h1 class="page-title">消息通知</h1>
      </div>
      <div class="header-actions">
        <el-button type="primary" @click="handleSendNotification">
          <el-icon><Bell /></el-icon>
          发送通知
        </el-button>
        <el-button @click="fetchNotifications">
          <el-icon><Refresh /></el-icon>
          刷新
        </el-button>
      </div>
    </div>

    <div class="main-content">
      <div class="content-card">
        <div class="notification-list">
          <el-table :data="systemNotifications" class="notification-table" v-loading="loading">
            <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
            <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'warning'" size="small">
                  {{ row.status === 1 ? '已读' : '未读' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" width="180">
              <template #default="{ row }">
                {{ formatDate(row.createTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140" fixed="right">
              <template #default="{ row }">
                <el-button size="small" @click="viewNotification(row)">
                  <el-icon><View /></el-icon>
                  查看
                </el-button>
                <el-button
                  v-if="row.status === 0"
                  size="small"
                  type="primary"
                  @click="markAsRead(row.id)"
                >
                  标记已读
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div class="pagination-wrapper">
          <el-pagination
            layout="total, prev, pager, next, jumper"
            :total="Number(total)"
            v-model:current-page="currentPage"
            :page-size="pageSize"
            background
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>

    <!-- 发送通知对话框 -->
    <el-dialog
      title="发送通知"
      v-model="sendDialogVisible"
      width="600px"
      center
      :close-on-click-modal="false"
    >
      <el-form :model="sendForm" :rules="sendFormRules" ref="sendFormRef" label-width="100px">
        <el-form-item label="标题" prop="title" required>
          <el-input v-model="sendForm.title" placeholder="请输入通知标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content" required>
          <el-input
            v-model="sendForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入通知内容"
          />
        </el-form-item>
        <el-form-item label="接收者" prop="receiverIds" required>
          <el-input
            v-model="sendForm.receiverNames"
            placeholder="请选择接收者"
            readonly
            @click="openReceiverSelector"
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
            <template #append>
              <el-button @click="openReceiverSelector">选择</el-button>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="sendDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitSendNotification" :loading="sending">
            发送
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 选人弹窗 -->
    <el-dialog
      title="选择接收者"
      v-model="receiverSelectorVisible"
      width="720px"
      destroy-on-close
      :close-on-click-modal="false"
    >
      <div class="selected-summary" v-if="selectedDisplayItems.length">
        <div class="summary-title">已选：</div>
        <div class="summary-chips">
          <el-tag
            v-for="item in selectedDisplayItems"
            :key="`${item.type}-${item.id}`"
            type="info"
            closable
            @close="removeSelectedItem(item)"
          >
            <span class="chip-type">[{{ item.type === 'dept' ? '部门' : item.type === 'role' ? '角色' : '用户' }}]</span>
            {{ item.label }}
          </el-tag>
        </div>
      </div>
      <div class="selected-summary empty" v-else>未选择任何接收者</div>

      <el-tabs v-model="receiverTab">
        <el-tab-pane label="按用户" name="user">
          <div class="receiver-body">
            <div class="receiver-left" style="width:240px;">
              <el-tree
                :data="deptTreeWithRoot"
                node-key="id"
                :props="deptTreeProps"
                highlight-current
                @node-click="(node) => loadUsersByDept(node.id)"
              />
              <div class="empty-tip" v-if="!deptTree.length">暂无部门数据</div>
            </div>
            <div class="receiver-right">
              <el-input
                v-model="userSearch"
                placeholder="搜索用户（姓名/账号）"
                clearable
                @input="loadUsersAll"
                @clear="loadUsersAll"
                style="margin-bottom:12px;"
              />
              <el-checkbox-group v-model="selectedUserIds" style="display:block; max-height:300px; overflow:auto;">
                <el-checkbox
                  v-for="user in userOptions"
                  :key="user.id"
                  :label="user.id"
                  style="display:block; margin:6px 0;"
                >
                  {{ getUserDisplayName(user) }}
                </el-checkbox>
              </el-checkbox-group>
              <div class="user-pagination">
                <el-pagination
                  layout="prev, pager, next, jumper"
                  :current-page="userPage.current"
                  :page-size="userPage.size"
                  :total="userPage.total"
                  small
                  @current-change="handleUserPageChange"
                />
              </div>
              <div v-if="!userOptions.length" class="empty-tip">请选择左侧部门或搜索</div>
            </div>
          </div>
        </el-tab-pane>

        <el-tab-pane label="按部门" name="dept">
          <div class="receiver-body">
            <div class="receiver-left" style="width:100%;">
              <el-tree
                :data="deptTree"
                node-key="id"
                show-checkbox
                :props="deptTreeProps"
                highlight-current
                @check="handleDeptCheck"
              />
              <div class="empty-tip" v-if="!deptTree.length">暂无部门数据</div>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="按角色" name="role">
          <div class="receiver-body">
            <div class="receiver-left">
              <el-checkbox-group v-model="selectedRoleIds">
                <el-checkbox
                  v-for="role in roleOptions"
                  :key="role.id"
                  :label="role.id"
                  style="display:block;margin:6px 0"
                >
                  {{ role.roleName || role.name }}
                </el-checkbox>
              </el-checkbox-group>
              <div class="empty-tip" v-if="!roleOptions.length">暂无角色数据</div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="receiverSelectorVisible = false">取消</el-button>
          <el-button @click="clearSelection" type="info">清空</el-button>
          <el-button type="primary" @click="handleSelectUsers">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, watch, reactive, computed, onMounted, onBeforeUnmount } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Bell, Refresh, View, User } from '@element-plus/icons-vue'
import { messageApi, departmentApi, roleApi, userApi, userOrgApi, userRoleApi } from '@/api/index.js'
import { useUserStore } from '@/stores/user.js'
import notificationSocket from '@/utils/notificationSocket'

const userStore = useUserStore()
const currentUserId = computed(() => userStore.userInfo?.id ?? userStore.user?.id ?? null)

const loading = ref(false)
const sending = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const receiverSelectorVisible = ref(false)
const receiverTab = ref('user')
const deptTree = ref([])
const roleOptions = ref([])
const userOptions = ref([])
const userSearch = ref('')
const selectedDeptId = ref(null)
const selectedRoleId = ref(null)
const selectedDeptIds = ref([])
const selectedRoleIds = ref([])
const selectedUserIds = ref([])
const selectedReceiverNames = ref([])
const selectingLoading = ref(false)
const allUsersCache = ref([])
const userPage = reactive({
  current: 1,
  size: 10,
  total: 0
})

const mapNotificationRecord = (item = {}) => ({
  ...item,
  status: item.receiverStatus === 'READ' ? 1 : 0
})

const emitUnreadRefresh = () => {
  window.dispatchEvent(new CustomEvent('notification:refresh-unread'))
}

let unsubscribeRealtime = null

const deptTreeProps = {
  label: (data) =>
    data.deptName ||
    data.name ||
    data.label ||
    data.title ||
    data.dept ||
    data.departmentName ||
    `部门${data.id ?? ''}`,
  children: 'children'
}

const flattenDeptMap = computed(() => {
  const map = new Map()
  const walk = (nodes = []) => {
    nodes.forEach((n) => {
      map.set(n.id, deptTreeProps.label(n))
      if (n.children && n.children.length) walk(n.children)
    })
  }
  walk(deptTreeWithRoot.value)
  return map
})

const roleMap = computed(() => {
  const map = new Map()
  ;(roleOptions.value || []).forEach((r) => map.set(r.id, r.roleName || r.name || `角色${r.id ?? ''}`))
  return map
})

const getDeptLabel = (id) => flattenDeptMap.value.get(id) || `部门${id ?? ''}`
const getRoleLabel = (id) => roleMap.value.get(id) || `角色${id ?? ''}`

const selectedDisplayItems = computed(() => {
  const deptItems = selectedDeptIds.value.map((id) => ({ id, type: 'dept', label: getDeptLabel(id) }))
  const roleItems = selectedRoleIds.value.map((id) => ({ id, type: 'role', label: getRoleLabel(id) }))
  const userItems = selectedUserIds.value.map((id) => {
    const user = allUsersCache.value.find((u) => u.id === id) || userOptions.value.find((u) => u.id === id)
    return { id, type: 'user', label: getUserDisplayName(user) }
  })
  return [...deptItems, ...roleItems, ...userItems]
})

const deptTreeWithRoot = computed(() => {
  const children = deptTree.value || []
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

const systemNotifications = ref([])

const notificationSettings = reactive({
  systemNotification: true,
  emailNotification: false,
  smsNotification: false,
  desktopNotification: true,
  frequency: 'realtime'
})

const sendDialogVisible = ref(false)
const sendForm = reactive({
  title: '',
  content: '',
  receiverIds: [],
  receiverNames: ''
})

const sendFormRules = {
  title: [{ required: true, message: '请输入通知标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入通知内容', trigger: 'blur' }],
  receiverIds: [{ required: true, message: '请选择接收者', trigger: 'change' }]
}

const sendFormRef = ref()

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const fetchNotifications = async () => {
  if (!currentUserId.value) {
    ElMessage.error('请先登录后再查看通知')
    systemNotifications.value = []
    total.value = 0
    return
  }
  loading.value = true
  try {
    const response = await messageApi.getReceivedMessages({
      receiverId: currentUserId.value,
      current: currentPage.value,
      size: pageSize.value
    })

    if (response && response.data) {
      const records = response.data.records || response.data.list || []
      systemNotifications.value = (records || []).map(mapNotificationRecord)
      total.value = Number(response.data.total) || 0
    } else {
      systemNotifications.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取通知列表失败:', error)
    ElMessage.error('获取通知列表失败: ' + (error.message || '未知错误'))
    systemNotifications.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const appendRealtimeNotification = (payload) => {
  if (!payload) return
  const mapped = mapNotificationRecord(payload)
  const existingIndex = systemNotifications.value.findIndex((item) => item.id === mapped.id)
  if (existingIndex !== -1) {
    systemNotifications.value.splice(existingIndex, 1, mapped)
  } else {
    systemNotifications.value = [mapped, ...systemNotifications.value]
    total.value = Number(total.value || 0) + 1
  }
}

const handleRealtimeNotification = (event) => {
  if (!event || event.channel !== 'personal' || !event.payload) return
  appendRealtimeNotification(event.payload)
}

const viewNotification = (notification) => {
  ElMessage.info(`查看通知: ${notification.title}`)
}

const markAsRead = async (messageId) => {
  if (!currentUserId.value) {
    ElMessage.error('请先登录')
    return
  }
  try {
    const response = await messageApi.markAsRead(messageId, currentUserId.value)
    if (response && (response.data === true || response.code === 200)) {
      ElMessage.success('标记成功')
      fetchNotifications()
      emitUnreadRefresh()
    } else {
      ElMessage.error(response?.message || '标记失败')
    }
  } catch (error) {
    console.error('标记已读失败:', error)
    ElMessage.error('标记失败: ' + (error.message || '未知错误'))
  }
}

const openReceiverSelector = async () => {
  receiverSelectorVisible.value = true
  if (!deptTree.value.length) {
    try {
      const res = await departmentApi.getDepartmentTree()
      deptTree.value = res?.data || []
      if ((!deptTree.value || !deptTree.value.length) && departmentApi.getAllDepartments) {
        const allRes = await departmentApi.getAllDepartments()
        const flat = allRes?.data || []
        const map = new Map()
        flat.forEach((d) => {
          map.set(d.id, { ...d, children: [] })
        })
        const roots = []
        flat.forEach((d) => {
          const node = map.get(d.id)
          if (d.parentId && map.get(d.parentId)) {
            map.get(d.parentId).children.push(node)
          } else {
            roots.push(node)
          }
        })
        deptTree.value = roots
      }
    } catch (e) {
      console.error('获取部门失败', e)
    }
  }
  if (!roleOptions.value.length) {
    try {
      const res = await roleApi.getAllRoles()
      roleOptions.value = res?.data || []
    } catch (e) {
      console.error('获取角色失败', e)
    }
  }
  if (receiverTab.value === 'user' && deptTreeWithRoot.value.length) {
    // 默认加载全部用户
    await loadUsersAll()
  }
}

const loadUsersByDept = async (deptId) => {
  selectedDeptId.value = deptId
  userOptions.value = []
  if (deptId === null || deptId === undefined) {
    await loadUsersAll()
    return
  }
  try {
    selectingLoading.value = true
    const res = await userOrgApi.getUsersByOrgId(deptId, {
      current: userPage.current,
      size: userPage.size,
      keyword: userSearch.value
    })
    const users = res?.data?.records || res?.data?.list || res?.data || []
    userPage.total = Number(res?.data?.total || users.length || 0)
    userOptions.value = users
  } catch (e) {
    console.error('按部门获取用户失败', e)
    userOptions.value = []
    userPage.total = 0
  } finally {
    selectingLoading.value = false
  }
}

const loadUsersByRole = async (roleId) => {
  selectedRoleId.value = roleId
  userOptions.value = []
  try {
    const idsRes = await userRoleApi.getUserIdsByRoleId(roleId)
    const userIds = idsRes?.data || []
    if (!userIds.length) {
      userOptions.value = []
      return
    }
    const listRes = await userApi.getUserList({ current: 1, size: 500 })
    const allUsers = listRes?.data?.records || listRes?.data?.list || []
    userOptions.value = allUsers.filter((u) => userIds.includes(u.id))
  } catch (e) {
    console.error('按角色获取用户失败', e)
    userOptions.value = []
  }
}

const loadUsersAll = async () => {
  try {
    selectingLoading.value = true
    const res = await userApi.getUserList({
      current: userPage.current,
      size: userPage.size,
      keyword: userSearch.value
    })
    const users = res?.data?.records || res?.data?.list || res?.data || []
    userPage.total = Number(res?.data?.total || users.length || 0)
    userOptions.value = users
    allUsersCache.value = users
  } catch (e) {
    console.error('获取用户失败', e)
    userOptions.value = []
    userPage.total = 0
  } finally {
    selectingLoading.value = false
  }
}

const getUserDisplayName = (user) => {
  if (!user) return ''
  return user.realName || user.nickname || user.name || user.username || `用户${user.id ?? ''}`
}

const handleSelectUsers = () => {
  const idSet = new Set(selectedUserIds.value)
  selectedUserIds.value = [...idSet]
  const selectedMap = new Map()
  userOptions.value.forEach((u) => {
    if (selectedUserIds.value.includes(u.id)) {
      selectedMap.set(u.id, getUserDisplayName(u))
    }
  })
  selectedReceiverNames.value = Array.from(selectedMap.values())
  sendForm.receiverIds = [...selectedUserIds.value]
  sendForm.receiverNames = selectedReceiverNames.value.join('，')
  receiverSelectorVisible.value = false
}

const clearSelection = () => {
  selectedUserIds.value = []
  selectedReceiverNames.value = []
  sendForm.receiverIds = []
  sendForm.receiverNames = ''
  selectedDeptIds.value = []
  selectedRoleIds.value = []
}

const removeSelectedItem = (item) => {
  if (item.type === 'dept') {
    selectedDeptIds.value = selectedDeptIds.value.filter((id) => id !== item.id)
  } else if (item.type === 'role') {
    selectedRoleIds.value = selectedRoleIds.value.filter((id) => id !== item.id)
  } else if (item.type === 'user') {
    selectedUserIds.value = selectedUserIds.value.filter((id) => id !== item.id)
  }
}

const handleDeptCheck = (data, state) => {
  selectedDeptIds.value = (state?.checkedKeys || []).filter((id) => id !== undefined && id !== null)
}

const handlePageChange = (page) => {
  currentPage.value = page
  fetchNotifications()
}

const handleUserPageChange = (page) => {
  userPage.current = page
  if (receiverTab.value === 'user') {
    loadUsersByDept(selectedDeptId.value)
  }
}

const handleSendNotification = () => {
  sendForm.title = ''
  sendForm.content = ''
  sendForm.receiverIds = []
  sendForm.receiverNames = ''
  selectedUserIds.value = []
  selectedReceiverNames.value = []
  receiverSelectorVisible.value = false
  receiverTab.value = 'user'
  userSearch.value = ''
  selectedDeptId.value = null
  selectedRoleId.value = null
  selectedDeptIds.value = []
  selectedRoleIds.value = []
  sendDialogVisible.value = true
}

const buildMessagePayload = (receiverTargets) => ({
  title: sendForm.title,
  content: sendForm.content,
  receiverTargets,
  senderId: currentUserId.value,
  receiverId: null
})

const submitSendNotification = async () => {
  if (!currentUserId.value) {
    ElMessage.error('请先登录')
    return
  }
  try {
    await sendFormRef.value.validate()

    const receiverTargets = [
      ...selectedDeptIds.value.map((id) => ({ id, type: 'dept' })),
      ...selectedRoleIds.value.map((id) => ({ id, type: 'role' })),
      ...selectedUserIds.value.map((id) => ({ id, type: 'user' }))
    ]
    if (!receiverTargets.length) {
      ElMessage.warning('请选择接收者（部门/角色/用户）')
      return
    }

    sending.value = true
    const response = await messageApi.sendMessage(buildMessagePayload(receiverTargets))

    if (response && (response.data === true || response.code === 200)) {
      ElMessage.success('通知发送成功')
      sendDialogVisible.value = false
      fetchNotifications()
    } else {
      ElMessage.error(response?.message || '发送失败')
    }
  } catch (error) {
    if (error !== false) {
      console.error('发送通知失败:', error)
      ElMessage.error('发送失败: ' + (error.message || '未知错误'))
    }
  } finally {
    sending.value = false
  }
}

onMounted(() => {
  unsubscribeRealtime = notificationSocket.subscribe(handleRealtimeNotification)
})

onBeforeUnmount(() => {
  if (unsubscribeRealtime) {
    unsubscribeRealtime()
    unsubscribeRealtime = null
  }
})

watch(
  () => currentUserId.value,
  (id) => {
    if (id) {
      notificationSocket.connect(id)
      fetchNotifications()
    }
  },
  { immediate: true }
)
</script>

<style scoped lang="scss">
@import '../index.scss';

// 选人弹窗样式优化 - 简洁清新风格
:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 40px -8px rgba(0, 0, 0, 0.1);
  
  .el-dialog__header {
    background: var(--el-bg-color);
    padding: 20px 24px;
    margin: 0;
    border-bottom: 1px solid var(--el-border-color-lighter);
    
    .el-dialog__title {
      color: var(--el-text-color-primary);
      font-weight: 600;
      font-size: 17px;
    }
  }
  
  .el-dialog__body {
    padding: 24px;
    background: var(--el-bg-color);
  }
  
  .el-dialog__footer {
    padding: 16px 24px;
    border-top: 1px solid var(--el-border-color-lighter);
    background: var(--el-fill-color-lighter);
  }
}

// 已选摘要样式
.selected-summary {
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

// Tabs样式优化
:deep(.el-tabs) {
  .el-tabs__header {
    margin-bottom: 20px;
    
    .el-tabs__nav-wrap::after {
      height: 1px;
      background-color: var(--el-border-color-lighter);
    }
    
    .el-tabs__item {
      font-size: 14px;
      font-weight: 500;
      color: var(--el-text-color-regular);
      padding: 0 24px;
      height: 44px;
      line-height: 44px;
      
      &:hover {
        color: var(--el-color-primary);
      }
      
      &.is-active {
        color: var(--el-color-primary);
        font-weight: 600;
      }
    }
    
    .el-tabs__active-bar {
      background-color: var(--el-color-primary);
      height: 2px;
      border-radius: 1px;
    }
  }
}

// 接收者选择区域
.receiver-body {
  display: flex;
  gap: 20px;
  min-height: 380px;
  
  .receiver-left {
    width: 240px;
    background: var(--el-fill-color-lighter);
    border: 1px solid var(--el-border-color-lighter);
    border-radius: 10px;
    padding: 16px;
    overflow: auto;
    max-height: 400px;
    
    // 树形组件样式
    :deep(.el-tree) {
      background: transparent;
      
      .el-tree-node__content {
        height: 38px;
        border-radius: 8px;
        padding-left: 8px;
        transition: all 0.15s ease;
        
        &:hover {
          background: var(--el-fill-color);
        }
      }
      
      .el-tree-node.is-current > .el-tree-node__content {
        background: var(--el-color-primary-light-9);
        color: var(--el-color-primary);
      }
    }
    
    // 复选框组样式
    :deep(.el-checkbox-group) {
      .el-checkbox {
        display: flex;
        margin: 0;
        padding: 10px 12px;
        border-radius: 8px;
        transition: all 0.15s ease;
        
        &:hover {
          background: var(--el-fill-color);
        }
        
        &.is-checked {
          background: var(--el-color-primary-light-9);
        }
        
        .el-checkbox__label {
          font-size: 14px;
          color: var(--el-text-color-primary);
        }
      }
    }
  }
  
  .receiver-right {
    flex: 1;
    display: flex;
    flex-direction: column;
    
    :deep(.el-input) {
      .el-input__wrapper {
        border-radius: 8px;
        box-shadow: 0 0 0 1px var(--el-border-color) inset;
        
        &:hover,
        &.is-focus {
          box-shadow: 0 0 0 1px var(--el-color-primary) inset;
        }
      }
    }
    
    // 用户列表复选框
    :deep(.el-checkbox-group) {
      background: var(--el-fill-color-lighter);
      border: 1px solid var(--el-border-color-lighter);
      border-radius: 10px;
      padding: 12px;
      flex: 1;
      overflow: auto;
      
      .el-checkbox {
        display: flex;
        margin: 0;
        padding: 10px 14px;
        border-radius: 8px;
        width: 100%;
        transition: all 0.15s ease;
        
        &:hover {
          background: var(--el-fill-color);
        }
        
        &.is-checked {
          background: var(--el-color-primary-light-9);
          
          .el-checkbox__label {
            color: var(--el-color-primary);
          }
        }
        
        .el-checkbox__label {
          font-size: 14px;
        }
      }
    }
  }
}

// 分页器样式
.user-pagination {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

// 空提示
.empty-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  color: var(--el-text-color-placeholder);
  font-size: 14px;
  height: 100%;
}

// 对话框底部按钮
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  
  .el-button {
    min-width: 88px;
    border-radius: 8px;
    font-weight: 500;
    padding: 10px 20px;
  }
}
</style>

