<template>
  <div>
    <el-card class="box-card">
      <el-tabs v-model="activeName"
               @tab-click="handleClick">
        <image style="width: 100px; height: 100px"
               src="../../assets/logo.png" />
        <el-tab-pane label="账号信息"
                     name="userinfo">
          <el-card>
            <el-description title="账号的基本信息">
              <el-description-item label="账号"
                                   :span='15'
                                   :value="userinfo.username"></el-description-item>
              <el-description-item label="姓名"
                                   :span='15'
                                   :value="userinfo.realName"></el-description-item>
              <el-description-item label="邮箱"
                                   :span='15'
                                   :value="userinfo.email"></el-description-item>
              <el-description-item label="地址"
                                   :span='15'
                                   :value="userinfo.address"></el-description-item>
              <el-description-item label="锁定"
                                   :span='15'
                                   :value="userinfo.locked"></el-description-item>
              <el-description-item label="创建时间"
                                   :span='15'
                                   :value="userinfo.createTime"></el-description-item>
            </el-description>

          </el-card>
        </el-tab-pane>
        <el-tab-pane label="密码修改"
                     name="modifyPassword">
          <el-card>
            <ul>
              <li>
                <p class="desc">
                  修改密码:
                  <a href="#"
                     @click="showPassword=true">修改密码</a>
                </p>
              </li>
            </ul>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <el-dialog :visible.sync="showPassword"
               @close="clearPassword"
               :showClose="false"
               title="修改我的密码"
               width="360px"
               :before-close="handleClose">
      <el-form :model="pwdModify"
               :rules="rules"
               label-width="80px"
               ref="modifyPwdForm">
        <el-form-item :minlength="6"
                      label="原密码"
                      prop="password">
          <el-input show-password
                    v-model="pwdModify.password"></el-input>
        </el-form-item>
        <el-form-item :minlength="6"
                      label="新密码"
                      prop="newPassword">
          <el-input show-password
                    v-model="pwdModify.newPassword"></el-input>
        </el-form-item>
        <el-form-item :minlength="6"
                      label="确认密码"
                      prop="confirmPassword">
          <el-input show-password
                    v-model="pwdModify.confirmPassword"></el-input>
        </el-form-item>
      </el-form>
      <div class="dialog-footer"
           slot="footer">
        <el-button @click="showPassword=false">取 消</el-button>
        <el-button @click="savePassword"
                   type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import qs from "qs";
import ElDescription from '@/components/description/Description'
import ElDescriptionItem from '@/components/description/DescriptionItem'

export default {
  name: "Person",
  components: { ElDescription, ElDescriptionItem },
  data () {
    return {
      userinfo: {
        id: 0,
        username: "admin",
        realName: "管理员",
        email: "admin@126.com",
        address: "",
        locked: false,
        createTime: "2021-07-19 20:26:06",
        updateTime: "2021-07-19 20:26:06"
      },
      activeName: "userinfo",
      showPassword: false,
      pwdModify: {},
      rules: {
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, message: "最少6个字符", trigger: "blur" }
        ],
        newPassword: [
          { required: true, message: "请输入新密码", trigger: "blur" },
          { min: 6, message: "最少6个字符", trigger: "blur" }
        ],
        confirmPassword: [
          { required: true, message: "请输入确认密码", trigger: "blur" },
          { min: 6, message: "最少6个字符", trigger: "blur" },
          {
            validator: (rule, value, callback) => {
              if (value !== this.pwdModify.newPassword) {
                callback(new Error("两次密码不一致"));
              } else {
                callback();
              }
            },
            trigger: "blur"
          }
        ]
      }
    };
  },
  created () {
    this.loadData();
    console.log(this.userinfo);
  },
  methods: {
    loadData: function () {
      this.$http
        .get(
          "/sqlrest/manager/api/v1/user/detail/name?username=" +
          window.sessionStorage.getItem("username")
        )
        .then(
          res => {
            if (0 === res.data.code) {
              this.userinfo = res.data.data;
            } else {
              alert("加载数据失败:" + res.data.message);
            }
          },
          error => {
            this.$message({
              showClose: true,
              message: "数据加载错误",
              type: "error"
            });
          }
        );
    },
    handleClose () { },
    savePassword () {
      this.$http({
        method: 'POST',
        url: '/sqlrest/manager/api/v1/user/changePassword',
        data: qs.stringify({
          oldPassword: this.pwdModify.password,
          newPassword: this.pwdModify.newPassword
        }),
      }).then(res => {
        console.log(res);
        if (0 === res.data.code) {
          this.showPassword = false;
          this.$message.success("修改密码成功！");
        } else {
          this.showPassword = true;
          this.$message(res.data.message);
        }
      });
    },
    clearPassword () {
      this.pwdModify = {
        password: "",
        newPassword: "",
        confirmPassword: ""
      };
      this.$refs.modifyPwdForm.clearValidate();
    },
    handleClick () {

    }
  }
};
</script>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  padding: 18px 0;
}

.box-card {
  width: 95%;
}

.my-label {
  background: #e1f3d8;
}

.my-content {
  background: #fde2e2;
}
</style>
