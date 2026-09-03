<template>
  <div class="test-component">
    <h2>表单设计器测试</h2>
    <p>如果你能看到这个文字，说明Vue组件正常渲染</p>
    
    <div class="test-section">
      <h3>测试拖拽组件</h3>
      <div class="test-drag-item" draggable="true" @dragstart="handleDragStart">
        拖拽我试试
      </div>
    </div>
    
    <div class="test-section">
      <h3>测试Element Plus</h3>
      <el-button type="primary">
        <el-icon class="el-icon--left"><Plus /></el-icon>
        Element Plus 按钮
      </el-button>
    </div>
    
    <div class="test-section">
      <h3>测试Store</h3>
      <p>当前表单名称: {{ formStore?.currentForm?.name || '未加载' }}</p>
      <el-button @click="testStore">测试Store</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { Plus } from '@element-plus/icons-vue'
import { useFormDesignerStore } from '@/stores/form/formDesigner'
import { ElMessage } from 'element-plus'

const formStore = useFormDesignerStore()

const handleDragStart = (event: DragEvent) => {
  console.log('拖拽开始', event)
  ElMessage.success('拖拽功能正常')
}

const testStore = () => {
  console.log('当前表单数据:', formStore.currentForm)
  ElMessage.info('Store测试完成，请查看控制台')
}
</script>

<style scoped lang="scss">
.test-component {
  padding: 20px;
  
  .test-section {
    margin: 20px 0;
    padding: 16px;
    border: 1px solid #dcdcdc;
    border-radius: 4px;
    
    h3 {
      margin-top: 0;
      color: #409EFF;
    }
  }
  
  .test-drag-item {
    padding: 12px;
    background: #f0f9ff;
    border: 2px dashed #409EFF;
    border-radius: 4px;
    cursor: move;
    text-align: center;
    
    &:hover {
      background: #e1f3ff;
    }
  }
}
</style>
