<template>
  <el-drawer
    v-model="visible"
    :show-close="false"
    :with-header="false"
    size="100%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :destroy-on-close="true"
    @close="handleClose"
  >
    <div class="grid-designer-container">
      <!-- 顶部工具栏 -->
      <div class="designer-toolbar">
        <div class="toolbar-left">
          <el-input
            v-if="gridInfo"
            v-model="gridInfo.gridName"
            placeholder="请输入表格名称"
            class="grid-name-input"
            style="width: 300px"
          />
        </div>
        <div class="toolbar-right">
          <el-button @click="handleClose">
            <el-icon><Close /></el-icon>
            关闭
          </el-button>
          <el-button type="primary" @click="handleSave">
            <el-icon><Document /></el-icon>
            保存表格
          </el-button>
        </div>
      </div>

      <!-- 表格设计器主体 - 使用核心组件 -->
      <div class="designer-main grid-designer-main">
        <GridDesignerCore 
          ref="gridDesignerRef"
          :grid-id="gridId"
          :app-id="appId"
          :initial-config="gridInfo?.gridConfig"
        />
      </div>
    </div>
  </el-drawer>
</template>

<script setup>
import { ref, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Close, Document } from '@element-plus/icons-vue'
import GridDesignerCore from './GridDesignerCore.vue'
import gridApi from '@/api/grid'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  gridId: {
    type: [String, Number],
    default: null
  },
  appId: {
    type: [String, Number],
    default: null
  },
  gridInfo: {
    type: Object,
    default: null
  }
})

const emit = defineEmits(['update:modelValue', 'saved', 'closed'])

const visible = ref(props.modelValue)
const gridDesignerRef = ref(null)

// 监听 modelValue 变化
watch(() => props.modelValue, (newVal) => {
  visible.value = newVal
})

// 监听 visible 变化，同步到父组件
watch(visible, (newVal) => {
  emit('update:modelValue', newVal)
})

// 保存表格设计
const handleSave = async () => {
  try {
    await ElMessageBox.confirm('确定保存表格配置吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })

    // 获取表格设计器的配置
    const gridConfig = gridDesignerRef.value?.getConfig()
    
    // 构造保存数据
    const gridData = {
      id: props.gridId,
      gridName: props.gridInfo?.gridName,
      gridDescription: props.gridInfo?.gridDescription || '',
      gridConfig: JSON.stringify(gridConfig),
      status: 1
    }

    // 调用更新接口
    const response = await gridApi.updateGrid(gridData)
    if (response.code === 200) {
      ElMessage.success('表格保存成功')
      emit('saved', gridData)
      handleClose()
    } else {
      ElMessage.error(response.message || '保存失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('保存表格失败:', error)
      ElMessage.error('保存表格失败')
    }
  }
}

// 关闭抽屉
const handleClose = () => {
  visible.value = false
  emit('closed')
}
</script>

<style lang="scss" scoped>
// 表格设计器容器
.grid-designer-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: var(--bg-secondary);

  .designer-toolbar {
    height: 56px;
    background: var(--bg-card);
    border-bottom: 1px solid var(--border-color);
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    flex-shrink: 0;

    .toolbar-left {
      display: flex;
      align-items: center;
      gap: 16px;

      .grid-name-input {
        :deep(.el-input__inner) {
          border: 1px dashed var(--border-color);
          background: transparent;
          font-size: 16px;
          font-weight: 500;

          &:hover {
            border-color: var(--primary-color);
          }

          &:focus {
            border-color: var(--primary-color);
            border-style: solid;
          }
        }
      }
    }

    .toolbar-right {
      display: flex;
      gap: 12px;
    }
  }

  .designer-main {
    flex: 1;
    overflow: hidden;
    position: relative;
  }

  .grid-designer-main {
    height: 100%;
  }
}

// 覆盖抽屉默认样式，移除内边距
:deep(.el-drawer__body) {
  padding: 0;
  overflow: hidden;
}
</style>