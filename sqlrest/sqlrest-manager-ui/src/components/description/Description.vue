<template>
  <div class="descriptions">
    <div v-if="Boolean(title)"
         class="descriptions-title">{{ title }}</div>
    <div class="descriptions-view">
      <el-row class="descriptions-row">
        <slot v-if="$slots.default" />
        <div v-else
             style="text-align: center; color: grey;">暂无数据</div>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ElDescription',
  props: {
    title: {
      type: String,
      required: false,
      default: ''
    }
  }
}
</script>

<style scoped>
.descriptions .descriptions-title {
  margin-bottom: 20px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 700;
  font-size: 16px;
  line-height: 1.5;
}
.descriptions .descriptions-view {
  width: 100%;
  overflow: hidden;
  table {
    width: 100%;
    table-layout: fixed;
    border-collapse: collapse;
  }
}

.descriptions .descriptions-view table {
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;
}
</style>