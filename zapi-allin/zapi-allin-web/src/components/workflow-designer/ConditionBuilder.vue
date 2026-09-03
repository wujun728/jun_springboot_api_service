<template>
  <div class="condition-builder">
    <div class="condition-row">
      <!-- 左侧：流程变量选择 -->
      <el-select
        v-model="localCondition.leftVariable"
        placeholder="选择变量"
        filterable
        clearable
        style="flex: 1"
        @change="handleConditionChange"
      >
        <el-option
          v-for="variable in processVariables"
          :key="variable.name"
          :label="variable.label || variable.name"
          :value="variable.name"
        >
          <div class="variable-option">
            <span class="variable-name">{{ variable.label || variable.name }}</span>
            <el-tag size="small" type="info">{{ variable.type }}</el-tag>
          </div>
        </el-option>
      </el-select>

      <!-- 中间：运算符选择 -->
      <el-select
        v-model="localCondition.operator"
        placeholder="运算符"
        style="width: 100px"
        @change="handleConditionChange"
      >
        <el-option
          v-for="op in availableOperators"
          :key="op.value"
          :label="op.label"
          :value="op.value"
        >
          <span>{{ op.label }}</span>
          <span class="operator-desc">{{ op.desc }}</span>
        </el-option>
      </el-select>

      <!-- 右侧：值类型选择 -->
      <el-radio-group
        v-model="localCondition.rightType"
        size="small"
        style="width: 120px"
        @change="handleRightTypeChange"
      >
        <el-radio-button value="static">静态值</el-radio-button>
        <el-radio-button value="variable">变量</el-radio-button>
      </el-radio-group>
    </div>

    <!-- 第二行：右侧值输入 -->
    <div class="value-row">
      <!-- 静态值输入 -->
      <template v-if="localCondition.rightType === 'static'">
        <el-input
          v-if="leftVariableType === 'string'"
          v-model="localCondition.rightValue"
          placeholder="请输入值"
          clearable
          @input="handleConditionChange"
        />
        <el-input-number
          v-else-if="leftVariableType === 'number' || leftVariableType === 'double' || leftVariableType === 'long'"
          v-model="localCondition.rightValue"
          placeholder="请输入数字"
          style="width: 100%"
          @change="handleConditionChange"
        />
        <el-switch
          v-else-if="leftVariableType === 'boolean'"
          v-model="localCondition.rightValue"
          active-text="true"
          inactive-text="false"
          @change="handleConditionChange"
        />
        <el-date-picker
          v-else-if="leftVariableType === 'date'"
          v-model="localCondition.rightValue"
          type="datetime"
          placeholder="选择日期时间"
          style="width: 100%"
          @change="handleConditionChange"
        />
        <el-input
          v-else
          v-model="localCondition.rightValue"
          placeholder="请输入值"
          clearable
          @input="handleConditionChange"
        />
      </template>

      <!-- 动态变量选择 -->
      <template v-else>
        <el-select
          v-model="localCondition.rightVariable"
          placeholder="选择变量"
          filterable
          clearable
          style="width: 100%"
          @change="handleConditionChange"
        >
          <el-option
            v-for="variable in processVariables"
            :key="variable.name"
            :label="variable.label || variable.name"
            :value="variable.name"
          >
            <div class="variable-option">
              <span class="variable-name">{{ variable.label || variable.name }}</span>
              <el-tag size="small" type="info">{{ variable.type }}</el-tag>
            </div>
          </el-option>
        </el-select>
      </template>
    </div>

    <!-- 生成的表达式预览 -->
    <div class="expression-preview">
      <el-text size="small" type="info">
        表达式预览：
      </el-text>
      <el-text size="small" class="expression-text">
        {{ generatedExpression || '请配置条件' }}
      </el-text>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { ProcessVariable } from '@/stores/workflow/processDesigner'

interface ConditionConfig {
  leftVariable: string      // 左侧变量名
  operator: string          // 运算符
  rightType: 'static' | 'variable'  // 右侧类型
  rightValue: any          // 右侧静态值
  rightVariable: string    // 右侧变量名
}

interface Props {
  modelValue?: string  // 表达式字符串，如 "${field_input_8cnzsteaq = '李四'}"
  processVariables?: ProcessVariable[]  // 流程变量列表
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  processVariables: () => []
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

// 本地条件配置
const localCondition = ref<ConditionConfig>({
  leftVariable: '',
  operator: '==',
  rightType: 'static',
  rightValue: '',
  rightVariable: ''
})

// 可用的运算符
const operators = [
  { value: '==', label: '等于', desc: '相等', types: ['string', 'number', 'double', 'long', 'boolean', 'date'] },
  { value: '!=', label: '不等于', desc: '不相等', types: ['string', 'number', 'double', 'long', 'boolean', 'date'] },
  { value: '>', label: '大于', desc: '>', types: ['number', 'double', 'long', 'date'] },
  { value: '<', label: '小于', desc: '<', types: ['number', 'double', 'long', 'date'] },
  { value: '>=', label: '大于等于', desc: '>=', types: ['number', 'double', 'long', 'date'] },
  { value: '<=', label: '小于等于', desc: '<=', types: ['number', 'double', 'long', 'date'] }
]

// 计算左侧变量的类型
const leftVariableType = computed(() => {
  const variable = props.processVariables.find(v => v.name === localCondition.value.leftVariable)
  return variable?.type || 'string'
})

// 根据左侧变量类型过滤可用的运算符
const availableOperators = computed(() => {
  return operators.filter(op => op.types.includes(leftVariableType.value))
})

// 生成表达式
const generatedExpression = computed(() => {
  const { leftVariable, operator, rightType, rightValue, rightVariable } = localCondition.value
  
  if (!leftVariable || !operator) {
    return ''
  }
  
  let rightSide = ''
  if (rightType === 'static') {
    if (rightValue === '' || rightValue === null || rightValue === undefined) {
      return ''
    }
    // 根据左侧变量类型决定是否加引号
    if (leftVariableType.value === 'string') {
      rightSide = `'${rightValue}'`
    } else if (leftVariableType.value === 'boolean') {
      rightSide = rightValue ? 'true' : 'false'
    } else if (leftVariableType.value === 'date') {
      // 日期类型需要格式化
      if (rightValue instanceof Date) {
        rightSide = `'${rightValue.toISOString()}'`
      } else {
        rightSide = `'${rightValue}'`
      }
    } else {
      rightSide = String(rightValue)
    }
  } else {
    if (!rightVariable) {
      return ''
    }
    rightSide = rightVariable
  }
  
  return `\${${leftVariable} ${operator} ${rightSide}}`
})

// 解析现有表达式到本地配置
const parseExpression = (expression: string) => {
  if (!expression) {
    resetCondition()
    return
  }
  
  // 去除 ${ } 包装
  const cleanExpr = expression.replace(/^\$\{/, '').replace(/\}$/, '').trim()
  
  // 尝试匹配各种运算符
  for (const op of operators) {
    const regex = new RegExp(`^(.+?)\\s*${op.value.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')}\\s*(.+)$`)
    const match = cleanExpr.match(regex)
    
    if (match) {
      const leftVar = match[1].trim()
      let rightPart = match[2].trim()
      
      localCondition.value.leftVariable = leftVar
      localCondition.value.operator = op.value
      
      // 判断右侧是变量还是静态值
      if (rightPart.startsWith("'") && rightPart.endsWith("'")) {
        // 字符串静态值
        localCondition.value.rightType = 'static'
        localCondition.value.rightValue = rightPart.slice(1, -1)
      } else if (rightPart.startsWith('"') && rightPart.endsWith('"')) {
        // 字符串静态值（双引号）
        localCondition.value.rightType = 'static'
        localCondition.value.rightValue = rightPart.slice(1, -1)
      } else if (rightPart === 'true' || rightPart === 'false') {
        // 布尔值
        localCondition.value.rightType = 'static'
        localCondition.value.rightValue = rightPart === 'true'
      } else if (!isNaN(Number(rightPart))) {
        // 数字
        localCondition.value.rightType = 'static'
        localCondition.value.rightValue = Number(rightPart)
      } else {
        // 可能是变量
        localCondition.value.rightType = 'variable'
        localCondition.value.rightVariable = rightPart
      }
      
      break
    }
  }
}

// 重置条件配置
const resetCondition = () => {
  localCondition.value = {
    leftVariable: '',
    operator: '==',
    rightType: 'static',
    rightValue: '',
    rightVariable: ''
  }
}

// 处理右侧类型变化
const handleRightTypeChange = () => {
  localCondition.value.rightValue = ''
  localCondition.value.rightVariable = ''
  handleConditionChange()
}

// 处理条件变化
const handleConditionChange = () => {
  emit('update:modelValue', generatedExpression.value)
}

// 监听输入的表达式变化
watch(() => props.modelValue, (newValue) => {
  if (newValue !== generatedExpression.value) {
    parseExpression(newValue)
  }
}, { immediate: true })

// 监听左侧变量变化，自动调整运算符
watch(() => localCondition.value.leftVariable, () => {
  // 如果当前运算符不适用于新变量类型，重置为等于
  if (!availableOperators.value.find(op => op.value === localCondition.value.operator)) {
    localCondition.value.operator = '=='
  }
})
</script>

<style scoped lang="scss">
.condition-builder {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 8px;
  border: 1px solid var(--border-color, #e5e7eb);
  border-radius: 6px;
  background: var(--bg-card, #ffffff);
}

.condition-row {
  display: flex;
  gap: 8px;
  align-items: center;
}

.value-row {
  display: flex;
  gap: 8px;
}

.variable-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  
  .variable-name {
    flex: 1;
    font-size: 13px;
  }
  
  .el-tag {
    margin-left: 8px;
  }
}

.operator-desc {
  float: right;
  color: var(--text-secondary, #6b7280);
  font-size: 11px;
  margin-left: 8px;
}

.expression-preview {
  padding: 8px;
  background: var(--bg-tertiary, #f1f5f9);
  border-radius: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
  
  .expression-text {
    font-family: 'Monaco', 'Menlo', 'Consolas', monospace;
    color: var(--primary-color, #6366f1);
    font-weight: 500;
    word-break: break-all;
  }
}

:deep(.el-input), :deep(.el-select) {
  .el-input__inner, .el-select__wrapper {
    font-size: 12px;
  }
}

:deep(.el-radio-button__inner) {
  font-size: 11px;
  padding: 5px 8px;
}
</style>

