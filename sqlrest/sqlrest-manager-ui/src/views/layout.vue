<template>
  <el-container class="index-container">
    <el-aside :width="isCollapse?'64px':'250px'">
      <!-- 左侧LOGO -->
      <div class="title">
        <img src="../assets/logo.png" />
        <span class="title-text"
              v-if="!isCollapse">{{title}}</span>
      </div>
      <!-- 左侧导航菜单 -->
      <asideBar ref="asideBar"></asideBar>
    </el-aside>
    <el-main>
      <el-header>
        <!-- 折叠图标 -->
        <div @click="handleToggleCollapse"
             class="collapse">
          <i class="el-icon-s-unfold"
             v-if="isCollapse"></i>
          <i class="el-icon-s-fold"
             v-else></i>
        </div>

        <!-- 面包屑导航 -->
        <breadcrumb></breadcrumb>
        <userDropdown></userDropdown>
      </el-header>

      <!-- 右侧显示区域 -->
      <viewMain></viewMain>
    </el-main>
  </el-container>
</template>

<script>
import asideBar from "@/components/asideBar/asideBar";
import breadcrumb from "@/components/breadcrumb/index";
import userDropdown from "@/components/userDropdown/index"
import viewMain from "@/views/viewer";

export default {
  name: "home",
  components: {
    asideBar,
    breadcrumb,
    userDropdown,
    viewMain
  },
  data () {
    return {
      title: "SQLREST",
      isCollapse: null
    };
  },
  computed: {},
  watch: {},
  methods: {
    handleToggleCollapse () {
      let status = !this.isCollapse;
      this.isCollapse = status;
      this.$refs.asideBar.updateCollapse(status);
    }
  },
  created () {
  },
  mounted () { }
};
</script>

<style scoped>
.index-container {
  height: 100%;
}

.el-aside {
  float: left;
  background: #002140;
  color: #333;
  text-align: left;
}

.el-aside .title {
  height: 60px;
  background: #001529;
  line-height: 64px;
  background: #002140;
  text-align: center;
}

.el-aside .title img {
  width: 50px;
  height: 50px;
  vertical-align: middle;
}

.el-aside .title .title-text {
  color: #fff;
  font-weight: 600;
  font-size: 20px;
  vertical-align: middle;
}

.el-header {
  padding: 0px;
  color: #ffffff;
  width: 100%;
  background: #fff;
}

.el-header .collapse {
  float: left;
  padding-left: 2px;
  font-size: 22px;
  color: #838383;
  margin-top: 10px;
  margin-right: 20px;
  cursor: pointer;
}

.el-main {
  padding: 0px;
  float: left;
  background-color: #eaedf1;
}
</style>
