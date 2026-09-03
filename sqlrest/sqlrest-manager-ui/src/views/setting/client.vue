<template>
  <div>
    <el-card>
      <div class="client-list-top">
        <div class="left-search-input-group">
          <div class="left-search-input">
            <el-input placeholder="请输入名称关键字搜索"
                      v-model="keyword"
                      @change="searchByKeyword"
                      :clearable=true
                      style="width:300px">
            </el-input>
          </div>
        </div>
        <div class="right-add-button-group">
          <el-button type="primary"
                     size="mini"
                     icon="el-icon-document-add"
                     @click="addClient">添加</el-button>
        </div>
      </div>

      <el-table :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                :data="tableData"
                size="small"
                border>
        <el-table-column prop="id"
                         label="编号"
                         min-width="5%"></el-table-column>
        <el-table-column prop="name"
                         label="应用名称"
                         show-overflow-tooltip
                         min-width="20%"></el-table-column>
        <el-table-column prop="description"
                         label="描述"
                         show-overflow-tooltip
                         min-width="10%"></el-table-column>
        <el-table-column prop="appKey"
                         label="应用账号"
                         show-overflow-tooltip
                         min-width="12%"></el-table-column>
        <el-table-column prop="expireDuration"
                         label="过期时间"
                         :formatter="stringFormatExpireDuration"
                         show-overflow-tooltip
                         min-width="18%"></el-table-column>
        <el-table-column prop="isExpired"
                         label="是否过期"
                         show-overflow-tooltip
                         min-width="10%">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isExpired"
                    type="danger"
                    effect="dark"
                    size="mini">已过期
            </el-tag>
            <el-tag v-if="!scope.row.isExpired"
                    type="primary"
                    effect="dark"
                    size="mini">未过期
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime"
                         label="创建时间"
                         min-width="18%">
        </el-table-column>
        <el-table-column label="操作"
                         min-width="35%">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="small"
                         type="danger"
                         icon="el-icon-document"
                         @click="handleAuthorize(scope.$index, scope.row)"
                         round>授权</el-button>
              <el-button size="small"
                         type="warning"
                         icon="el-icon-document"
                         @click="handleShowSecret(scope.$index, scope.row)"
                         round>密钥</el-button>
              <el-button size="small"
                         type="success"
                         icon="el-icon-delete"
                         @click="handleDelete(scope.$index, scope.row)"
                         round>删除</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <div class="page"
           align="right">
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="currentPage"
                       :page-sizes="[5, 10, 20, 40]"
                       :page-size="pageSize"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="totalCount"></el-pagination>
      </div>

      <el-dialog title="添加应用信息"
                 :visible.sync="createFormVisible"
                 :showClose="false"
                 :before-close="handleClose">
        <el-form :model="createform"
                 size="mini"
                 status-icon
                 :rules="rules"
                 ref="createform">
          <el-form-item label="应用名称"
                        label-width="120px"
                        :required=true
                        prop="name"
                        style="width:85%">
            <el-input v-model="createform.name"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="描述"
                        label-width="120px"
                        prop="description"
                        style="width:85%">
            <el-input type="textarea"
                      :rows="6"
                      :spellcheck="false"
                      placeholder="请输入"
                      v-model="createform.description"
                      auto-complete="off">
            </el-input>
          </el-form-item>
          <el-form-item label="应用账号"
                        label-width="120px"
                        prop="appKey"
                        style="width:85%">
            <el-input v-model="createform.appKey"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="到期时长"
                        label-width="120px"
                        prop="expireTime"
                        style="width:85%">
            <el-select v-model="createform.expireTime">
              <el-option label="永远"
                         value="EXPIRE_FOR_EVER"></el-option>
              <el-option label="一次"
                         value="EXPIRE_ONLY_ONCE"></el-option>
              <el-option label="5分钟"
                         value="EXPIRE_05_MIN"></el-option>
              <el-option label="30分钟"
                         value="EXPIRE_30_MIN"></el-option>
              <el-option label="1小时"
                         value="EXPIRE_01_HOUR"></el-option>
              <el-option label="12小时"
                         value="EXPIRE_12_HOUR"></el-option>
              <el-option label="1天"
                         value="EXPIRE_01_DAY"></el-option>
              <el-option label="15天"
                         value="EXPIRE_15_DAY"></el-option>
              <el-option label="1个月"
                         value="EXPIRE_01_MOUTH"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer"
             class="dialog-footer">
          <el-button @click="createFormVisible = false">取 消</el-button>
          <el-button type="primary"
                     @click="handleCreate">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="查看密钥"
                 :visible.sync="ShowSecretDialog">
        <el-input type="input"
                  style="width:55%"
                  id="secretTextInput"
                  v-model="clientSecret"></el-input>
        <el-button @click="handleCopyText">点击复制</el-button>
        <span slot="footer">
          <el-button @click="ShowSecretDialog = false">取消</el-button>
        </span>
      </el-dialog>

      <el-dialog title="授权分组"
                 :visible.sync="showAuthDialog"
                 @open="loadAllGroups">
        <el-checkbox-group v-model="selectList">
          <el-checkbox v-for="item in groups"
                       :label="item.id"
                       :key="item.id">{{ item.name }}</el-checkbox>
        </el-checkbox-group>
        <span slot="footer">
          <el-button @click="showAuthDialog = false">取消</el-button>
          <el-button type="primary"
                     @click="handleSaveAuth()">保存</el-button>
        </span>
      </el-dialog>

    </el-card>
  </div>
</template>

<script>

export default {
  name: "client",
  components: {
  },
  data () {
    return {
      loading: true,
      keyword: null,
      lists: [],
      currentPage: 1,
      pageSize: 10,
      totalCount: 0,
      tableData: [
      ],
      groups: [],
      clientId: 0,
      selectList: [],
      showAuthDialog: false,
      ShowSecretDialog: false,
      clientSecret: '',
      createform: {
        name: "",
        description: "",
        appKey: "",
        expireTime: ""
      },
      rules: {
        name: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "blur"
          }
        ],
        appKey: [
          {
            required: true,
            message: "应用账号不能为空",
            trigger: "blur"
          }
        ],
        expireTime: [
          {
            required: true,
            message: "到期时间必须选择",
            trigger: "change"
          }
        ]
      },
      createFormVisible: false
    }
  },
  methods: {
    loadData: function () {
      this.$http({
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        url: "/sqlrest/manager/api/v1/client/list",
        data: JSON.stringify({
          searchText: this.keyword,
          page: this.currentPage,
          size: this.pageSize
        })
      }).then(res => {
        if (0 === res.data.code) {
          this.currentPage = res.data.pagination.page;
          this.pageSize = res.data.pagination.size;
          this.totalCount = res.data.pagination.total;
          this.tableData = res.data.data;
        } else {
          alert("加载数据失败:" + res.data.message);
        }
      });
    },
    loadAllGroups: function () {
      this.$http.post("/sqlrest/manager/api/v1/group/listAll").then((res) => {
        if (0 === res.data.code) {
          this.groups = res.data.data
        } else {
          alert("加载数据失败:" + res.data.message);
        }
      }).catch((error) => {
      })
    },
    stringFormatExpireDuration (row, column) {
      if (row.expireDuration === "FOR_EVER") {
        return "永不过期";
      } else if (row.expireDuration === "ONLY_ONCE") {
        return "一次过期";
      } else if (row.expireDuration === "TIME_VALUE") {
        return row.expireAtStr;
      }
      return "-";
    },
    searchByKeyword: function () {
      this.currentPage = 1;
      this.loadData();
    },
    handleClose (done) {
    },
    handleDelete: function (index, row) {
      this.$confirm(
        "此操作将此应用ID=" + row.id + "删除么, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        this.$http.delete(
          "/sqlrest/manager/api/v1/client/delete/" + row.id
        ).then(res => {
          if (0 === res.data.code) {
            this.loadData();
          } else {
            alert("删除失败:" + res.data.message);
          }
        });
      });
    },
    addClient: function () {
      this.createFormVisible = true;
      this.createform = {};
    },
    handleCreate: function () {
      this.$refs['createform'].validate(valid => {
        if (valid) {
          this.$http({
            method: "POST",
            headers: {
              'Content-Type': 'application/json'
            },
            url: "/sqlrest/manager/api/v1/client/create",
            data: JSON.stringify({
              name: this.createform.name,
              description: this.createform.description,
              appKey: this.createform.appKey,
              expireTime: this.createform.expireTime
            })
          }).then(res => {
            if (0 === res.data.code) {
              this.createFormVisible = false;
              this.$message("添加信息成功");
              this.createform = {};
              this.loadData();
            } else {
              alert("添加信息失败:" + res.data.message);
            }
          });
        } else {
          alert("请检查输入");
        }
      });
    },
    handleSizeChange: function (pageSize) {
      this.loading = true;
      this.pageSize = pageSize;
      this.loadData();
    },
    handleCurrentChange: function (currentPage) {
      this.loading = true;
      this.currentPage = currentPage;
      this.loadData();
    },
    handleShowSecret: function (index, row) {
      this.ShowSecretDialog = true
      this.$http.get("/sqlrest/manager/api/v1/client/secret/" + row.id)
        .then((res) => {
          if (0 === res.data.code) {
            this.clientSecret = res.data.data
          } else {
            alert("操作失败：" + res.data.message)
          }
        })
    },
    handleCopyText: function () {
      secretTextInput
      var d = document.getElementById("secretTextInput")
      d.select() //选中
      document.execCommand("copy")
      this.$message.success("复制成功")
    },
    handleAuthorize: function (index, row) {
      this.showAuthDialog = true
      this.$http.get("/sqlrest/manager/api/v1/client/auth/" + row.id)
        .then((res) => {
          this.selectList = []
          this.clientId = row.id
          for (let item of res.data) {
            this.selectList.push(item.id)
          }
        })
    },
    handleSaveAuth: function () {
      this.$http({
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        url: "/sqlrest/manager/api/v1/client/auth/create",
        data: JSON.stringify({
          id: this.clientId,
          groupIds: this.selectList
        })
      }).then(res => {
        if (0 === res.data.code) {
          this.showAuthDialog = false
        } else {
          alert("操作失败:" + res.data.message);
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
.client-list-top {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.left-search-input-group {
  width: calc(100% - 100px);
  margin-right: auto;
  display: flex;
  justify-content: space-between;
}
.left-search-input {
  width: 300px;
  margin-right: auto;
  margin: 10px 5px;
}
.right-add-button-group {
  width: 100px;
  margin-left: auto;
  margin: 10px 5px;
}
</style>
