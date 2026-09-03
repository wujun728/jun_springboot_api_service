<template>
  <div class="asideBarItem-container">
    <!-- 如果hasOwnProperty监测有children 就循环递归展示-->
    <el-submenu :index="router.path" v-if="hasChildrenAndShow(router)">
      <template slot="title">
      <i :class="router.icon"></i>
      <span slot="title">{{router.name}}</span>
      </template>
      <!--递归子孙导航-->
      <asideBarItem v-for="(child, childKey) in router.children" :key="child.path" :router="child"></asideBarItem>
    </el-submenu>
    <!-- 如果没有 就展示一级导航 -->
    <el-menu-item v-else :key="router.path" :index="router.path" @click="saveActivePath(router.path)">
       <i :class="router.icon"></i>
       <span>{{router.name}}</span>
    </el-menu-item>
  </div>
</template>

<script>
//hasOwnProperty可以用来检测一个对象是否含有特定的自身属性
export default {
  name: "asideBarItem",
  props: {
    router: {
      type: Object
    },
  },
  components: {},
  data() {
    return {
    };
  },
  computed: {
    // router () {
    //   return this.$router.options.routes
    // }
  },
  watch: {},
  methods: {
    hasChildrenAndShow(router){
      if(router.hidden){
        return false
      }

      return router.hasOwnProperty('children');
    },
    saveActivePath(path) {
      //alert(path);
      this.$emit('setActivePath',path);
    },
  },
  created() {
  },
  mounted() {}
};
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #1890ff !important;
}

/*隐藏文字*/
.el-menu--collapse .asideBarItem-container span{
  display: none;
}
/*隐藏 > */
.el-menu--collapse .asideBarItem-container .el-submenu__title .el-submenu__icon-arrow{
  display: none;
}

</style>
