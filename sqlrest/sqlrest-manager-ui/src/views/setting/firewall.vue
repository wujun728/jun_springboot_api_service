<template>
  <div>
    <el-card>
      <el-form label-width="200px">
        <el-form-item label="访问控制">
          <el-switch v-model="status"
                     active-color="#13ce66"
                     active-value="ON"
                     inactive-value="OFF"
                     active-text="开启"
                     inactive-text="关闭">
          </el-switch>
        </el-form-item>
        <div v-show="status=='ON'">
          <el-form-item label="名单">
            <el-radio-group v-model="mode"
                            @change="modeChange">
              <el-radio label="BLACK">黑名单</el-radio>
              <el-radio label="WHITE">白名单</el-radio>
            </el-radio-group>
            <el-alert title="除了黑名单列表中的IP禁止访问API，其他IP一律允许访问"
                      type="warning"
                      :closable="false"
                      v-show="mode == 'BLACK'"></el-alert>
            <el-alert title="只有白名单列表中的IP才允许访问API，其他IP一律禁止访问"
                      type="warning"
                      :closable="false"
                      v-show="mode == 'WHITE'"></el-alert>
          </el-form-item>
          <el-form-item label="黑名单IP列表"
                        v-show="mode == 'BLACK'">
            <el-input type="textarea"
                      :autosize="{ minRows: 8, maxRows: 20 }"
                      v-model="addresses"
                      placeholder="每行一个IP，多个IP请用换行分隔.">
            </el-input>
          </el-form-item>
          <el-form-item label="白名单IP列表"
                        v-show="mode == 'WHITE'">
            <el-input type="textarea"
                      :autosize="{ minRows: 8, maxRows: 20 }"
                      v-model="addresses"
                      placeholder="每行一个IP，多个IP请用换行分隔.">
            </el-input>
          </el-form-item>
        </div>
        <el-form-item>
          <el-button type="primary"
                     @click="handleSave"
                     plain>保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>

export default {
  name: "firewall",
  data () {
    return {
      status: "ON",
      mode: "BLACK",
      addresses: ""
    }
  },
  methods: {
    loadData: function () {
      this.$http.get("/sqlrest/manager/api/v1/firewall/detail").then(res => {
        if (0 === res.data.code) {
          this.status = res.data.data.status;
          this.mode = res.data.data.mode;
          this.addresses = res.data.data.addresses;
        } else {
          alert("加载数据失败:" + res.data.message);
        }
      }
      );
    },
    modeChange (p) {
      console.log(p)
    },
    handleSave () {
      if (this.status === 'ON' && (!this.addresses || /^\s*$/.test(this.addresses))) {
        alert('IP列表不能为空!')
        return
      }
      this.$http({
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        url: "/sqlrest/manager/api/v1/firewall/save",
        data: JSON.stringify({
          status: this.status,
          mode: this.mode,
          addresses: this.addresses
        })
      }).then(res => {
        if (0 === res.data.code) {
          this.$message("保存成功");
          this.loadData();
        } else {
          alert("保存失败:" + res.data.message);
        }
      });
    }
  },
  created () {
    this.loadData();
  }
};
</script>

<style scoped>
.el-table {
  width: 100%;
  height: 100%;
}
.el-card,
.el-message {
  width: 100%;
  height: 100%;
  overflow: auto;
}
</style>
