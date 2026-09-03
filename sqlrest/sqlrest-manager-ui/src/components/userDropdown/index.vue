<template>
  <div class="user-dropdown-wrap">
    <el-dropdown>
      <div class="user-dropdown-photo">
        <span class="user-dropdown-text">
          {{ nickname }}({{username}})
          <i class="el-icon-caret-bottom"></i>
        </span>
        <img src="../../assets/user.jpg"
             alt="user" />
      </div>
      <el-dropdown-menu solt="dropdown">
        <el-dropdown-item>
          <router-link to="/user/self">
            <i class="el-icon-s-custom"></i>个人信息
          </router-link>
        </el-dropdown-item>
        <el-dropdown-item divided>
          <a @click="hadleLogout()">
            <i class="el-icon-switch-button"></i>退出登录
          </a>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  data () {
    return {
      username: "",
      nickname: ""
    };
  },
  created () {
    this.username = window.sessionStorage.getItem("username");
    this.nickname = window.sessionStorage.getItem("realname");
  },
  methods: {
    hadleLogout () {
      window.sessionStorage.clear();
      this.$http({
        method: 'GET',
        url: '/user/logout'
      }),
        this.$router.push("/login");
    }
  },
  destroyed () {
    window.sessionStorage.setItem("activePath", "/");
  }
};
</script>
<style scoped>
.user-dropdown-wrap {
  height: 60px;
  padding: 10px 0;
  float: right;
}

.user-dropdown-wrap .user-dropdown-photo img {
  width: 30px;
  height: 30px;
  vertical-align: middle;
}
</style>
