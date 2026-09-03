<template>
  <div class="login-container">
    <el-form :model="ruleForm2"
             :rules="rules2"
             status-icon
             ref="ruleForm2"
             label-position="left"
             label-width="0px"
             class="demo-ruleForm login-page">
      <h3 class="title"
          align="center">系统登录</h3>
      <el-form-item prop="username">
        <el-input type="text"
                  v-model="ruleForm2.username"
                  auto-complete="off"
                  placeholder="用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password"
                  v-model="ruleForm2.password"
                  auto-complete="off"
                  placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button type="primary"
                   style="width:100%;"
                   @click="handleSubmit"
                   :loading="logining"
                   @keyup.enter="handleSubmit">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import qs from "qs";

export default {
  data () {
    return {
      logining: false,
      ruleForm2: {
        username: "",
        password: ""
      },
      rules2: {
        username: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "blur"
          }
        ]
      },
      checked: false
    };
  },
  created () {
    //按enter键提交功能参考：https://www.cnblogs.com/cristina-guan/p/9440035.html
    var lett = this;
    document.onkeydown = function (e) {
      var key = window.event.keyCode;
      if (key == 13) {
        lett.handleSubmit(e);
      }
    };
  },
  methods: {
    handleSubmit () {
      this.$refs.ruleForm2.validate(valid => {
        if (valid) {
          this.$http({
            method: 'POST',
            url: '/user/login',
            data: {
              username: this.ruleForm2.username,
              password: this.ruleForm2.password
            },
          }).then(res => {
            if (0 === res.data.code) {
              this.logining = true;
              window.sessionStorage.setItem('token', res.data.data.accessToken);
              window.sessionStorage.setItem('username', this.ruleForm2.username);
              window.sessionStorage.setItem('realname', res.data.data.realName);
              this.$router.push({
                path: '/dashboard'
              });
            } else {
              this.logining = false;
              this.$message(res.data.message);
            }
          });
        }
      });
    }
  }
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100%;
}

.login-page {
  -webkit-border-radius: 5px;
  border-radius: 5px;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

label.el-checkbox.rememberme {
  margin: 0px 0px 15px;
  text-align: left;
}
</style>
