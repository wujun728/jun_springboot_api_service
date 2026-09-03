<template>
  <div style="height:auto;">
    <iframe id="iframe"
            v-loading="loading"
            :src="url"
            width="100%"
            @load="resizeIframe"
            style="min-height:300px; width:100%;border:0;"
            frameborder="0"></iframe>
  </div>
</template>
 
<script>

export default {
  name: 'SwaggerUI',
  data () {
    return {
      loading: false,
      url: ''
    }
  },
  methods: {
    resizeIframe: function (event) {
      // 设置iframe的高度为其内容的高度
      var ifm = document.getElementById("iframe");
      ifm.height = document.documentElement.clientHeight;
      ifm.width = document.documentElement.clientWidth;

      this.$http({
        method: "GET",
        url: "/sqlrest/manager/api/v1/node/gateway"
      }).then(
        res => {
          if (0 === res.data.code) {
            if (res.data.data && typeof res.data.data === 'string') {
              this.url = res.data.data + '/apidoc/index.html';
              console.log(this.url)
              // 加载完成后去掉加载效果
              this.loading = false;
            }
          } else {
            if (res.data.message) {
              alert("加载数据失败:" + res.data.message);
            }
          }
        }
      );
    }
  },
  mounted () {
    this.loading = true;
  },
}
</script>
 
<style>
/* 可以添加自定义样式 */
</style>