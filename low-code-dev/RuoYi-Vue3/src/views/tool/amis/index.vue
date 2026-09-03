<template>
  <div id="box"></div>
</template>

<script>
import 'amis/sdk/sdk.js';
import 'amis/lib/themes/default.css';
import 'amis/sdk/sdk'
import 'amis/sdk/sdk.css'
import 'amis/sdk/iconfont.css'
import 'amis/sdk/helper.css'
import 'amis/sdk/charts'
// import "@/api/tool/amis"
import {getId} from "@/api/tool/amis";
import {checkPermi, checkRole} from "@/utils/permission"; // 权限判断函数
export default {
  data() {
    return {
      json: {},
      amisScoped: null
    }
  },
  mounted() {
    getId(this.$route.query.amisid).then(response => {
          var json = response.data.rows[0].json
          this.amisScoped = amisRequire('amis/embed').embed("#box", JSON.parse(json), {
            data: {checkPermi: checkPermi}
          })
        }
    );
  }
}
</script>

<style scoped>

</style>
