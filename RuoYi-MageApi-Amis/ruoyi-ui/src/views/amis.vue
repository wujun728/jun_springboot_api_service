<template>
  <div
    ref="box"
    style="
      text-align: center;
      font-size: 24px;
      font-weight: 700;
      margin-bottom: 20px;
    "
  >
  </div>
</template>
<script>
import request from '@/utils/request'
export default {
  mounted() {
    request({
        url: '/system/dict/data/page/'+this.$router.currentRoute.meta.menuId,
        method: 'get'
      }).then(response => {
        amisRequire("amis/embed").embed(this.$refs.box,JSON.parse(response.data.remark));
      });
  },
};
</script>