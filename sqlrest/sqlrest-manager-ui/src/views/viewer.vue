<template>
  <div class="viewer-container">
    <router-view></router-view>
  </div>
</template>
 
<script>
export default {
  name: "viewer",
  components: {},
  data () {
    return {};
  },
  computed: {},
  watch: {},
  methods: {},
  created () { },
  mounted () { }
};
</script>
 
<style scoped>
.viewer-container {
  padding-top: 10px;
  padding-right: 10px;
  padding-bottom: 10px;
  padding-left: 10px;
}
</style>