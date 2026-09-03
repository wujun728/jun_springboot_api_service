<template>
  <el-card class="api-selector-card" shadow="never">
    <div style="text-align: left">
      <span class="page-description" @click="handleBack">
          <svg-icon icon-class="left"></svg-icon>
          <span>返回</span>
        </span>
    </div>
    <div style="text-align: right">
      <el-button type="primary" @click="handleCreate">
        新增版本
      </el-button>
      <el-button type="success" @click="handleCreateNext" v-if="hasPublishedVersion">
        新增下一个版本
      </el-button>
      <el-button type="primary" @click="handleRefresh" :loading="refreshing">
        刷新
      </el-button>
    </div>
  </el-card>
</template>

<script setup>
import {ref, defineProps, defineEmits} from 'vue'
import SvgIcon from "@/components/SvgIcon/index.vue";

const props = defineProps({
  apiList: {
    type: Array,
    default: () => []
  },
  selectedApiId: {
    type: [String, Number],
    default: null
  },
  loading: {
    type: Boolean,
    default: false
  },
  hasPublishedVersion: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:selectedApiId', 'create', 'create-next', 'refresh', 'back'])

const refreshing = ref(false)

const handleApiChange = (value) => {
  emit('update:selectedApiId', value)
}

const handleCreate = () => {
  emit('create')
}

const handleCreateNext = () => {
  emit('create-next')
}

const handleRefresh = async () => {
  refreshing.value = true
  try {
    await emit('refresh')
  } finally {
    refreshing.value = false
  }
}

const handleBack = () => {
  emit('back');
};
</script>

<style scoped lang="scss">
.api-selector-card {
  margin-bottom: 12px;
  border-radius: 12px;
  border: 1px solid #e4e7ed;
}

.api-selector-card  {
  :deep(.el-card__body){
    padding: 10px 0px;
    border-bottom: 1px solid #f0f2f5;
    font-weight: 600;
    color: #303133;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}

.api-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.api-name {
  font-weight: 500;
  color: #303133;
}

.api-path {
  font-size: 12px;
  color: #909399;
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
}

.page-description {
  color: #909399;
  font-size: 14px;
  cursor: pointer;
}
</style>