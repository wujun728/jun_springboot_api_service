import { defineStore } from 'pinia'
import { ref, reactive, computed } from 'vue'

// 流程变量定义
export interface ProcessVariable {
  name: string
  type: 'string' | 'double' | 'long' | 'boolean' | 'date'
  label: string
  defaultValue?: any
}

// 节点基础接口
export interface BaseNode {
  id: string
  name: string
  description?: string
  position: { x: number; y: number }
}

// 开始节点
export interface StartNode extends BaseNode {
  type: 'START'
}

// 处理人类型枚举
export type AssigneeType = 'FIXED' | 'DYNAMIC' | 'ROLE' | 'EXPRESSION'

// 审批节点
export interface ApprovalNode extends BaseNode {
  type: 'APPROVAL'
  assigneeType: AssigneeType
  assignee: string | number
  formKey?: string
  allowReject?: boolean
}

// 条件分支节点
export interface ConditionalBranchNode extends BaseNode {
  type: 'CONDITIONAL_BRANCH'
  defaultFlow?: string
}

// 抄送节点
export interface CcNode extends BaseNode {
  type: 'CC'
  assigneeType: AssigneeType
  assignee: string | number
  formKey?: string
  // 抄送通知方式
  notifyType?: 'EMAIL' | 'SMS' | 'SYSTEM' | 'ALL'
  // 是否必须查看
  requireRead?: boolean
}

// 并行节点
export interface ParallelNode extends BaseNode {
  type: 'PARALLEL'
  branches: ParallelBranch[]
  // 并行执行策略：ALL-全部完成，ANY-任意一个完成
  strategy?: 'ALL' | 'ANY'
}

// 并行分支
export interface ParallelBranch {
  id: string
  name: string
  description?: string
  nodes: string[] // 节点ID列表
  // 分支条件（可选）
  condition?: string | null
}

// 结束节点
export interface EndNode extends BaseNode {
  type: 'END'
}

// 联合节点类型
export type ProcessNode = StartNode | ApprovalNode | ConditionalBranchNode | CcNode | ParallelNode | EndNode

// 连线定义
export interface ProcessFlow {
  id: string
  name?: string
  from: string
  to: string
  condition?: string | null
}

// 完整的流程定义（符合后端规范）
export interface ProcessDefinition {
  id?: string
  name: string
  key: string
  description?: string
  variables?: ProcessVariable[]
  nodes: ProcessNode[]
  flows: ProcessFlow[]
}

export const useProcessDesignerStore = defineStore('processDesigner', () => {
  // 当前流程定义
  const currentProcess = reactive<ProcessDefinition>({
    name: '新建流程',
    key: '',
    description: '',
    variables: [],
    nodes: [],
    flows: []
  })

  // 选中状态
  const selectedNodeId = ref<string>('')
  const selectedFlowId = ref<string>('')

  // 历史记录
  const history = reactive({
    undoStack: [] as string[],
    redoStack: [] as string[],
    maxHistorySize: 50
  })

  // 计算属性
  const selectedNode = computed(() => {
    return currentProcess.nodes.find(n => n.id === selectedNodeId.value) || null
  })

  const selectedFlow = computed(() => {
    return currentProcess.flows.find(f => f.id === selectedFlowId.value) || null
  })

  // 添加节点
  const addNode = (type: ProcessNode['type'], position: { x: number; y: number }) => {
    const nodeId = `${type.toLowerCase()}_${Date.now()}`

    let newNode: ProcessNode

    switch (type) {
      case 'START':
        newNode = {
          id: nodeId,
          name: '开始',
          type: 'START',
          description: '',
          position
        }
        break
      case 'APPROVAL':
        newNode = {
          id: nodeId,
          name: '审批节点',
          type: 'APPROVAL',
          assigneeType: 'FIXED',
          assignee: '',
          formKey: '',
          allowReject: true,
          description: '',
          position
        }
        break
      case 'CONDITIONAL_BRANCH':
        newNode = {
          id: nodeId,
          name: '条件分支',
          type: 'CONDITIONAL_BRANCH',
          defaultFlow: '',
          description: '',
          position
        }
        break
      case 'CC':
        newNode = {
          id: nodeId,
          name: '抄送节点',
          type: 'CC',
          assigneeType: 'FIXED',
          assignee: '',
          formKey: '',
          description: '',
          notifyType: 'SYSTEM',
          requireRead: false,
          position
        }
        break
      case 'PARALLEL':
        newNode = {
          id: nodeId,
          name: '并行节点',
          type: 'PARALLEL',
          branches: [
            {
              id: `branch_${Date.now()}_1`,
              name: '分支1',
              description: '',
              nodes: [],
              condition: null
            },
            {
              id: `branch_${Date.now()}_2`,
              name: '分支2',
              description: '',
              nodes: [],
              condition: null
            }
          ],
          strategy: 'ALL',
          description: '',
          position
        }
        break
      case 'END':
        newNode = {
          id: nodeId,
          name: '结束',
          type: 'END',
          description: '',
          position
        }
        break
      default:
        throw new Error(`未知节点类型: ${type}`)
    }

    currentProcess.nodes.push(newNode)
    selectedNodeId.value = newNode.id
    saveToHistory()

    return newNode
  }

  // 删除节点
  const deleteNode = (nodeId: string) => {
    const nodeIndex = currentProcess.nodes.findIndex(n => n.id === nodeId)
    if (nodeIndex > -1) {
      currentProcess.nodes.splice(nodeIndex, 1)
      // 删除相关连接
      currentProcess.flows = currentProcess.flows.filter(
          f => f.from !== nodeId && f.to !== nodeId
      )
      if (selectedNodeId.value === nodeId) {
        selectedNodeId.value = ''
      }
      saveToHistory()
    }
  }

  // 更新节点
  const updateNode = (nodeId: string, updates: Partial<ProcessNode>, skipHistory = false) => {
    const node = currentProcess.nodes.find(n => n.id === nodeId)
    if (node) {
      Object.assign(node, updates)
      if (!skipHistory) {
        saveToHistory()
      }
    }
  }

  // 添加连接
  const addFlow = (from: string, to: string, condition?: string | null) => {
    // 检查是否已存在连接
    const existingFlow = currentProcess.flows.find(
        f => f.from === from && f.to === to
    )
    if (existingFlow) {
      return existingFlow
    }

    const newFlow: ProcessFlow = {
      id: `flow_${Date.now()}`,
      from,
      to,
      condition: condition || null,
      name: ''
    }

    currentProcess.flows.push(newFlow)
    saveToHistory()

    return newFlow
  }

  // 删除连接
  const deleteFlow = (flowId: string) => {
    const index = currentProcess.flows.findIndex(f => f.id === flowId)
    if (index > -1) {
      currentProcess.flows.splice(index, 1)
      if (selectedFlowId.value === flowId) {
        selectedFlowId.value = ''
      }
      saveToHistory()
    }
  }

  // 选中节点
  const selectNode = (nodeId: string) => {
    selectedNodeId.value = nodeId
    selectedFlowId.value = ''
  }

  // 选中连接
  const selectFlow = (flowId: string) => {
    selectedFlowId.value = flowId
    selectedNodeId.value = ''
  }

  // 清空选择
  const clearSelection = () => {
    selectedNodeId.value = ''
    selectedFlowId.value = ''
  }

  // 保存到历史记录
  const saveToHistory = () => {
    const currentState = JSON.stringify({
      process: currentProcess,
      selectedNodeId: selectedNodeId.value,
      selectedFlowId: selectedFlowId.value
    })

    history.undoStack.push(currentState)

    // 限制历史记录大小
    if (history.undoStack.length > history.maxHistorySize) {
      history.undoStack.shift()
    }

    // 清空重做栈
    history.redoStack = []
  }

  // 验证流程
  const validateProcess = () => {
    const errors: string[] = []

    // 1. 检查流程基本信息
    if (!currentProcess.name.trim()) {
      errors.push('流程名称不能为空')
    }
    if (!currentProcess.key.trim()) {
      errors.push('流程标识不能为空')
    }

    // 2. 检查开始节点
    const startNodes = currentProcess.nodes.filter(n => n.type === 'START')
    if (startNodes.length === 0) {
      errors.push('流程必须包含一个开始节点')
    } else if (startNodes.length > 1) {
      errors.push('流程只能包含一个开始节点')
    }

    // 3. 检查结束节点
    const endNodes = currentProcess.nodes.filter(n => n.type === 'END')
    if (endNodes.length === 0) {
      errors.push('流程必须包含至少一个结束节点')
    }

    // 4. 检查审批节点配置
    currentProcess.nodes.forEach(node => {
      if (node.type === 'APPROVAL') {
        const approvalNode = node as ApprovalNode
        // 检查 approvalTargets（多人审批）或 assignee（单人审批）
        const hasApprovalTargets = approvalNode.approvalTargets && approvalNode.approvalTargets.length > 0
        const hasAssignee = approvalNode.assignee && approvalNode.assignee !== '' && approvalNode.assignee !== 0
        if (!hasApprovalTargets && !hasAssignee) {
          errors.push(`审批节点"${node.name}"必须指定处理人`)
        }
      }
      // 检查抄送节点配置
      if (node.type === 'CC') {
        const ccNode = node as CcNode
        // 检查 ccTargets（多人抄送）或 assignee（单人抄送）
        const hasCcTargets = ccNode.ccTargets && ccNode.ccTargets.length > 0
        const hasAssignee = ccNode.assignee && ccNode.assignee !== '' && ccNode.assignee !== 0
        if (!hasCcTargets && !hasAssignee) {
          errors.push(`抄送节点"${node.name}"必须指定抄送人`)
        }
      }
      // 检查并行节点配置
      if (node.type === 'PARALLEL') {
        const parallelNode = node as ParallelNode
        if (!parallelNode.branches || parallelNode.branches.length < 2) {
          errors.push(`并行节点"${node.name}"至少需要两个分支`)
        }
        if (parallelNode.branches && parallelNode.branches.length > 10) {
          errors.push(`并行节点"${node.name}"最多支持10个分支`)
        }
        // 检查分支名称是否重复
        if (parallelNode.branches) {
          const branchNames = parallelNode.branches.map(b => b.name).filter(Boolean)
          const uniqueNames = new Set(branchNames)
          if (branchNames.length !== uniqueNames.size) {
            errors.push(`并行节点"${node.name}"的分支名称不能重复`)
          }
        }
      }
    })

    // 5. 检查连接完整性
    currentProcess.nodes.forEach(node => {
      if (node.type !== 'END') {
        const outgoingFlows = currentProcess.flows.filter(f => f.from === node.id)
        if (outgoingFlows.length === 0) {
          errors.push(`节点"${node.name}"缺少输出连接`)
        }

        // START节点只能有一条输出连接且无条件
        if (node.type === 'START' && outgoingFlows.length > 1) {
          errors.push('开始节点只能有一条输出连接')
        }
        if (node.type === 'START' && outgoingFlows.some(f => f.condition)) {
          errors.push('开始节点的输出连接不能有条件')
        }
      }

      if (node.type !== 'START') {
        const incomingFlows = currentProcess.flows.filter(f => f.to === node.id)
        if (incomingFlows.length === 0) {
          errors.push(`节点"${node.name}"缺少输入连接`)
        }
      }
    })

    // 6. 检查条件分支节点的连接
    currentProcess.nodes.forEach(node => {
      if (node.type === 'CONDITIONAL_BRANCH') {
        const outgoingFlows = currentProcess.flows.filter(f => f.from === node.id)
        const conditionalFlows = outgoingFlows.filter(f => f.condition)
        if (conditionalFlows.length === 0) {
          errors.push(`条件分支节点"${node.name}"至少需要一条带条件的输出连接`)
        }
      }
    })

    // 7. 检查闭环（简单检查）
    const hasCircularReference = checkCircularReference()
    if (hasCircularReference) {
      errors.push('流程中存在循环引用，请检查连接')
    }

    return errors
  }

  // 检查循环引用
  const checkCircularReference = () => {
    const visited = new Set<string>()
    const visiting = new Set<string>()

    const dfs = (nodeId: string): boolean => {
      if (visiting.has(nodeId)) return true // 发现循环
      if (visited.has(nodeId)) return false

      visiting.add(nodeId)

      const outgoingFlows = currentProcess.flows.filter(f => f.from === nodeId)
      for (const flow of outgoingFlows) {
        if (dfs(flow.to)) return true
      }

      visiting.delete(nodeId)
      visited.add(nodeId)
      return false
    }

    for (const node of currentProcess.nodes) {
      if (!visited.has(node.id)) {
        if (dfs(node.id)) return true
      }
    }

    return false
  }

  // 初始化流程
  const initProcess = () => {
    currentProcess.name = '新建流程'
    currentProcess.key = `process_${Date.now()}`
    currentProcess.description = ''
    currentProcess.variables = []
    currentProcess.nodes = []
    currentProcess.flows = []
    selectedNodeId.value = ''
    selectedFlowId.value = ''
    saveToHistory()
  }

  // 导入流程
  const importProcess = (processData: ProcessDefinition) => {
    Object.assign(currentProcess, processData)
    selectedNodeId.value = ''
    selectedFlowId.value = ''
    saveToHistory()
  }

  // 导出流程（符合后端规范）
  const exportProcess = (): ProcessDefinition => {
    return JSON.parse(JSON.stringify(currentProcess))
  }

  // 清空流程
  const clearProcess = () => {
    currentProcess.nodes = []
    currentProcess.flows = []
    selectedNodeId.value = ''
    selectedFlowId.value = ''
    saveToHistory()
  }

  // 添加流程变量
  const addVariable = (variable: ProcessVariable) => {
    currentProcess.variables = currentProcess.variables || []
    currentProcess.variables.push(variable)
    saveToHistory()
  }

  // 删除流程变量
  const deleteVariable = (variableName: string) => {
    if (currentProcess.variables) {
      const index = currentProcess.variables.findIndex(v => v.name === variableName)
      if (index > -1) {
        currentProcess.variables.splice(index, 1)
        saveToHistory()
      }
    }
  }

  return {
    // 状态
    currentProcess,
    selectedNodeId,
    selectedFlowId,
    selectedNode,
    selectedFlow,
    history,

    // 方法
    addNode,
    deleteNode,
    updateNode,
    addFlow,
    deleteFlow,
    selectNode,
    selectFlow,
    clearSelection,
    saveToHistory,
    validateProcess,
    checkCircularReference,
    initProcess,
    importProcess,
    exportProcess,
    clearProcess,
    addVariable,
    deleteVariable
  }
})
