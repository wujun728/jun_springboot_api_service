import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface WorkflowNode {
  id: string
  type: string
  title: string
  description?: string
  x: number
  y: number
  width: number
  height: number
  properties?: Record<string, any>
}

export interface WorkflowConnection {
  id: string
  fromNodeId: string
  toNodeId: string
  fromPort?: string
  toPort?: string
  label?: string
  condition?: string
  properties?: Record<string, any>
}

export interface WorkflowData {
  id?: string
  name: string
  description?: string
  nodes: WorkflowNode[]
  connections: WorkflowConnection[]
  version?: string
  createdAt?: string
  updatedAt?: string
  metadata?: Record<string, any>
}

export const useWorkflowDesignerStore = defineStore('workflowDesigner', () => {
  // 状态
  const currentWorkflow = ref<WorkflowData | null>(null)
  const workflowList = ref<WorkflowData[]>([])
  const selectedNode = ref<string | null>(null)
  const selectedConnection = ref<string | null>(null)
  const isLoading = ref(false)
  const isDirty = ref(false) // 是否有未保存的更改

  // 计算属性
  const hasWorkflow = computed(() => !!currentWorkflow.value)
  const nodeCount = computed(() => currentWorkflow.value?.nodes.length || 0)
  const connectionCount = computed(() => currentWorkflow.value?.connections.length || 0)
  
  const selectedNodeData = computed(() => {
    if (!selectedNode.value || !currentWorkflow.value) return null
    return currentWorkflow.value.nodes.find(node => node.id === selectedNode.value) || null
  })
  
  const selectedConnectionData = computed(() => {
    if (!selectedConnection.value || !currentWorkflow.value) return null
    return currentWorkflow.value.connections.find(conn => conn.id === selectedConnection.value) || null
  })

  // 动作
  const createNewWorkflow = (name: string = '新建流程') => {
    const newWorkflow: WorkflowData = {
      id: `workflow_${Date.now()}`,
      name,
      description: '',
      nodes: [],
      connections: [],
      version: '1.0.0',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      metadata: {}
    }
    
    currentWorkflow.value = newWorkflow
    isDirty.value = false
    clearSelection()
  }

  const setWorkflow = (workflow: Partial<WorkflowData>) => {
    if (currentWorkflow.value) {
      currentWorkflow.value = {
        ...currentWorkflow.value,
        ...workflow,
        updatedAt: new Date().toISOString()
      }
    } else {
      currentWorkflow.value = {
        id: `workflow_${Date.now()}`,
        name: workflow.name || '新建流程',
        description: workflow.description || '',
        nodes: workflow.nodes || [],
        connections: workflow.connections || [],
        version: '1.0.0',
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString(),
        metadata: workflow.metadata || {}
      }
    }
    isDirty.value = true
  }

  const updateWorkflowProperty = (property: keyof WorkflowData, value: any) => {
    if (currentWorkflow.value) {
      (currentWorkflow.value as any)[property] = value
      currentWorkflow.value.updatedAt = new Date().toISOString()
      isDirty.value = true
    }
  }

  // 节点操作
  const addNode = (node: Omit<WorkflowNode, 'id'>) => {
    if (!currentWorkflow.value) return null
    
    const newNode: WorkflowNode = {
      ...node,
      id: `node_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
    }
    
    currentWorkflow.value.nodes.push(newNode)
    currentWorkflow.value.updatedAt = new Date().toISOString()
    isDirty.value = true
    
    return newNode
  }

  const updateNode = (nodeId: string, updates: Partial<WorkflowNode>) => {
    if (!currentWorkflow.value) return false
    
    const nodeIndex = currentWorkflow.value.nodes.findIndex(node => node.id === nodeId)
    if (nodeIndex === -1) return false
    
    currentWorkflow.value.nodes[nodeIndex] = {
      ...currentWorkflow.value.nodes[nodeIndex],
      ...updates
    }
    currentWorkflow.value.updatedAt = new Date().toISOString()
    isDirty.value = true
    
    return true
  }

  const deleteNode = (nodeId: string) => {
    if (!currentWorkflow.value) return false
    
    // 删除节点
    const nodeIndex = currentWorkflow.value.nodes.findIndex(node => node.id === nodeId)
    if (nodeIndex === -1) return false
    
    currentWorkflow.value.nodes.splice(nodeIndex, 1)
    
    // 删除相关连接
    currentWorkflow.value.connections = currentWorkflow.value.connections.filter(
      conn => conn.fromNodeId !== nodeId && conn.toNodeId !== nodeId
    )
    
    // 清除选择
    if (selectedNode.value === nodeId) {
      selectedNode.value = null
    }
    
    currentWorkflow.value.updatedAt = new Date().toISOString()
    isDirty.value = true
    
    return true
  }

  const moveNode = (nodeId: string, x: number, y: number) => {
    return updateNode(nodeId, { x, y })
  }

  const duplicateNode = (nodeId: string) => {
    if (!currentWorkflow.value) return null
    
    const sourceNode = currentWorkflow.value.nodes.find(node => node.id === nodeId)
    if (!sourceNode) return null
    
    const duplicatedNode = addNode({
      ...sourceNode,
      x: sourceNode.x + 20,
      y: sourceNode.y + 20,
      title: `${sourceNode.title} (副本)`
    })
    
    return duplicatedNode
  }

  // 连接操作
  const addConnection = (connection: Omit<WorkflowConnection, 'id'>) => {
    if (!currentWorkflow.value) return null
    
    // 检查是否已存在相同连接
    const existingConnection = currentWorkflow.value.connections.find(
      conn => conn.fromNodeId === connection.fromNodeId && conn.toNodeId === connection.toNodeId
    )
    if (existingConnection) return null
    
    const newConnection: WorkflowConnection = {
      ...connection,
      id: `connection_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`
    }
    
    currentWorkflow.value.connections.push(newConnection)
    currentWorkflow.value.updatedAt = new Date().toISOString()
    isDirty.value = true
    
    return newConnection
  }

  const updateConnection = (connectionId: string, updates: Partial<WorkflowConnection>) => {
    if (!currentWorkflow.value) return false
    
    const connectionIndex = currentWorkflow.value.connections.findIndex(conn => conn.id === connectionId)
    if (connectionIndex === -1) return false
    
    currentWorkflow.value.connections[connectionIndex] = {
      ...currentWorkflow.value.connections[connectionIndex],
      ...updates
    }
    currentWorkflow.value.updatedAt = new Date().toISOString()
    isDirty.value = true
    
    return true
  }

  const deleteConnection = (connectionId: string) => {
    if (!currentWorkflow.value) return false
    
    const connectionIndex = currentWorkflow.value.connections.findIndex(conn => conn.id === connectionId)
    if (connectionIndex === -1) return false
    
    currentWorkflow.value.connections.splice(connectionIndex, 1)
    
    // 清除选择
    if (selectedConnection.value === connectionId) {
      selectedConnection.value = null
    }
    
    currentWorkflow.value.updatedAt = new Date().toISOString()
    isDirty.value = true
    
    return true
  }

  // 选择操作
  const selectNode = (nodeId: string | null) => {
    selectedNode.value = nodeId
    selectedConnection.value = null
  }

  const selectConnection = (connectionId: string | null) => {
    selectedConnection.value = connectionId
    selectedNode.value = null
  }

  const clearSelection = () => {
    selectedNode.value = null
    selectedConnection.value = null
  }

  // 工作流管理
  const saveWorkflow = async () => {
    if (!currentWorkflow.value) return false
    
    try {
      isLoading.value = true
      
      // 模拟保存到后端
      await new Promise(resolve => setTimeout(resolve, 1000))
      
      // 更新工作流列表
      const existingIndex = workflowList.value.findIndex(wf => wf.id === currentWorkflow.value!.id)
      if (existingIndex >= 0) {
        workflowList.value[existingIndex] = { ...currentWorkflow.value }
      } else {
        workflowList.value.push({ ...currentWorkflow.value })
      }
      
      // 保存到本地存储
      localStorage.setItem('workflows', JSON.stringify(workflowList.value))
      localStorage.setItem('currentWorkflow', JSON.stringify(currentWorkflow.value))
      
      isDirty.value = false
      return true
    } catch (error) {
      console.error('保存工作流失败:', error)
      return false
    } finally {
      isLoading.value = false
    }
  }

  const loadWorkflow = async (workflowId: string) => {
    try {
      isLoading.value = true
      
      // 从列表中查找
      const workflow = workflowList.value.find(wf => wf.id === workflowId)
      if (workflow) {
        currentWorkflow.value = { ...workflow }
        isDirty.value = false
        clearSelection()
        return true
      }
      
      return false
    } catch (error) {
      console.error('加载工作流失败:', error)
      return false
    } finally {
      isLoading.value = false
    }
  }

  const deleteWorkflow = async (workflowId: string) => {
    try {
      isLoading.value = true
      
      // 从列表中删除
      workflowList.value = workflowList.value.filter(wf => wf.id !== workflowId)
      
      // 如果删除的是当前工作流，清空当前工作流
      if (currentWorkflow.value?.id === workflowId) {
        currentWorkflow.value = null
        clearSelection()
      }
      
      // 更新本地存储
      localStorage.setItem('workflows', JSON.stringify(workflowList.value))
      if (currentWorkflow.value) {
        localStorage.setItem('currentWorkflow', JSON.stringify(currentWorkflow.value))
      } else {
        localStorage.removeItem('currentWorkflow')
      }
      
      return true
    } catch (error) {
      console.error('删除工作流失败:', error)
      return false
    } finally {
      isLoading.value = false
    }
  }

  const loadWorkflowList = async () => {
    try {
      isLoading.value = true
      
      // 从本地存储加载
      const savedWorkflows = localStorage.getItem('workflows')
      if (savedWorkflows) {
        workflowList.value = JSON.parse(savedWorkflows)
      }
      
      const savedCurrentWorkflow = localStorage.getItem('currentWorkflow')
      if (savedCurrentWorkflow) {
        currentWorkflow.value = JSON.parse(savedCurrentWorkflow)
        isDirty.value = false
      }
      
      return true
    } catch (error) {
      console.error('加载工作流列表失败:', error)
      return false
    } finally {
      isLoading.value = false
    }
  }

  // 导入导出
  const exportWorkflow = (workflowId?: string) => {
    const workflow = workflowId 
      ? workflowList.value.find(wf => wf.id === workflowId)
      : currentWorkflow.value
    
    if (!workflow) return null
    
    const exportData = {
      ...workflow,
      exportedAt: new Date().toISOString(),
      exportVersion: '1.0.0'
    }
    
    return JSON.stringify(exportData, null, 2)
  }

  const importWorkflow = async (workflowData: string) => {
    try {
      const parsedWorkflow = JSON.parse(workflowData)
      
      // 验证数据格式
      if (!parsedWorkflow.name || !Array.isArray(parsedWorkflow.nodes)) {
        throw new Error('工作流数据格式不正确')
      }
      
      // 生成新的ID避免冲突
      const importedWorkflow: WorkflowData = {
        ...parsedWorkflow,
        id: `workflow_${Date.now()}`,
        name: `${parsedWorkflow.name} (导入)`,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      }
      
      currentWorkflow.value = importedWorkflow
      isDirty.value = true
      clearSelection()
      
      return true
    } catch (error) {
      console.error('导入工作流失败:', error)
      return false
    }
  }

  // 工具方法
  const validateWorkflow = () => {
    if (!currentWorkflow.value) return { valid: false, errors: ['没有工作流数据'] }
    
    const errors: string[] = []
    const { nodes, connections } = currentWorkflow.value
    
    // 检查是否有开始节点
    const startNodes = nodes.filter(node => node.type === 'start')
    if (startNodes.length === 0) {
      errors.push('缺少开始节点')
    } else if (startNodes.length > 1) {
      errors.push('只能有一个开始节点')
    }
    
    // 检查是否有结束节点
    const endNodes = nodes.filter(node => node.type === 'end')
    if (endNodes.length === 0) {
      errors.push('缺少结束节点')
    }
    
    // 检查连接是否有效
    connections.forEach(conn => {
      const fromNode = nodes.find(node => node.id === conn.fromNodeId)
      const toNode = nodes.find(node => node.id === conn.toNodeId)
      
      if (!fromNode) {
        errors.push(`连接 ${conn.id} 的起始节点不存在`)
      }
      if (!toNode) {
        errors.push(`连接 ${conn.id} 的目标节点不存在`)
      }
    })
    
    return {
      valid: errors.length === 0,
      errors
    }
  }

  const getNodeStatistics = () => {
    if (!currentWorkflow.value) return {}
    
    const nodeTypes = currentWorkflow.value.nodes.reduce((acc, node) => {
      acc[node.type] = (acc[node.type] || 0) + 1
      return acc
    }, {} as Record<string, number>)
    
    return {
      totalNodes: currentWorkflow.value.nodes.length,
      totalConnections: currentWorkflow.value.connections.length,
      nodeTypes
    }
  }

  // 初始化
  const init = async () => {
    await loadWorkflowList()
  }

  return {
    // 状态
    currentWorkflow,
    workflowList,
    selectedNode,
    selectedConnection,
    isLoading,
    isDirty,
    
    // 计算属性
    hasWorkflow,
    nodeCount,
    connectionCount,
    selectedNodeData,
    selectedConnectionData,
    
    // 动作
    createNewWorkflow,
    setWorkflow,
    updateWorkflowProperty,
    
    // 节点操作
    addNode,
    updateNode,
    deleteNode,
    moveNode,
    duplicateNode,
    
    // 连接操作
    addConnection,
    updateConnection,
    deleteConnection,
    
    // 选择操作
    selectNode,
    selectConnection,
    clearSelection,
    
    // 工作流管理
    saveWorkflow,
    loadWorkflow,
    deleteWorkflow,
    loadWorkflowList,
    
    // 导入导出
    exportWorkflow,
    importWorkflow,
    
    // 工具方法
    validateWorkflow,
    getNodeStatistics,
    
    // 初始化
    init
  }
})
