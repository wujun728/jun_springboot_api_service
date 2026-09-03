<template>
  <div class="statistic-card elegant-container" :class="getCardClass()">
    <div class="statistic-content">
      <div class="statistic-icon">
        <svg-icon :icon-class="icon" class-name="icon" />
      </div>
      <div class="statistic-info">
        <div class="statistic-value">
          <AutoCounter
              :startAmount="0"
              :endAmount="value"
              :duration="2"
              :autoinit="true"
              class="value"
          />
        </div>
        <div class="statistic-title">{{ title }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import AutoCounter from 'vue3-autocounter'

interface Props {
  title: string
  value: number
  icon: string
  color?: string
  type?: 'primary' | 'success' | 'warning' | 'danger' | 'info'
}

const props = defineProps<Props>()

const getCardClass = () => {
  if (props.type) {
    return `stat-card-${props.type}`
  }
  return ''
}
</script>

<style scoped lang="scss">
@import "@/assets/styles/variables.module.scss";

.statistic-card {
  padding: 20px;
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: all 0.3s ease;
  
  // 默认样式使用素雅白色背景
  background: white;
  color: $gray-900;
  
  // 不同类型的卡片样式
  &.stat-card-primary {
    .statistic-icon .icon {
      color: $primary;
    }
    .statistic-value .value {
      color: $primary-dark;
    }
  }
  
  &.stat-card-success {
    .statistic-icon .icon {
      color: $success;
    }
    .statistic-value .value {
      color: #219653;
    }
  }
  
  &.stat-card-warning {
    .statistic-icon .icon {
      color: $warning;
    }
    .statistic-value .value {
      color: #D35400;
    }
  }
  
  &.stat-card-danger {
    .statistic-icon .icon {
      color: $danger;
    }
    .statistic-value .value {
      color: #C0392B;
    }
  }
  
  &.stat-card-info {
    .statistic-icon .icon {
      color: $info;
    }
    .statistic-value .value {
      color: #2980B9;
    }
  }

  &:hover {
    transform: translateY(-4px);
  }

  .statistic-content {
    display: flex;
    align-items: center;
    gap: 16px;

    .statistic-icon {
      .icon {
        font-size: 40px;
        opacity: 0.9;
      }
    }

    .statistic-info {
      flex: 1;

      .statistic-value {
        .value {
          font-size: 32px;
          font-weight: bold;
          line-height: 1.2;
        }
      }

      .statistic-title {
        font-size: 14px;
        color: $gray-600;
        margin-top: 4px;
      }
    }
  }
}
</style>
