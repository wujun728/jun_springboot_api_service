<template>
  <div class="aside-container">
    <el-row class="tac">
      <el-col :span="24">
        <!-- 整体左侧导航 -->
        <el-menu
          :router="true"
          unique-opened
          @open="handleOpen"
          @close="handleClose"
          background-color="#001529"
          text-color="rgb(191, 203, 217)"
          active-text-color="#ffffff"
          :collapse="collapsed"
          :default-active="initActivePath"
        >
          <!-- 左侧导航栏抽取循环部分 将路由列表传给子组件-->
          <asideBarItem v-for="router in routers" :router="router" v-if="showBarItem(router)" @setActivePath='setActivePath' :key="router.path"></asideBarItem>
        </el-menu>
      </el-col>
    </el-row>
  </div>
</template>
 
<script>
import asideBarItem from "@/components/asideBar/asideBarItem";
export default {
  name: "asideBar",
  components: {
    asideBarItem
  },
  data() {
    return {
      collapsed:false,
      initActivePath:'/dashboard'
    };
  },
  computed: {
    routers() {
      //console.log(this.$router.options);
      //this.$router.options获取路由列表
      return this.$router.options.routes[0].children;
    },
  },
  watch: {},
  methods: {
    showBarItem(router){
      if(router.hidden){
        return false
      }

      return true;
    },
    handleOpen(key, keyPath) {
      //点击打开时当前的路径
      //console.log(key, keyPath, 'handleOpen');
    },
    handleClose(key, keyPath) {
      //console.log(key, keyPath, 'handleClose');
    },
    updateCollapse(collapse){
      this.collapsed=collapse;
      //console.log("=========="+this.isCollapse);
    },
    setActivePath(path){
      this.initActivePath=path;
      window.sessionStorage.setItem("activePath", path);
      //console.log("update active path "+this.initActivePath);
    },
    getActivePath(){
      return window.sessionStorage.getItem("activePath");
    }
  },
  created() {
    this.initActivePath = this.getActivePath();
    //console.log("init active path "+this.initActivePath);
  },
  mounted() {
    // console.log(this.$router.options.routes[1])
  }
};
</script>
 
 <style scoped>
.aside-container {
  padding-top: 2px;
}

.el-menu {
  padding: 0;
  border-right: none;
}

</style>