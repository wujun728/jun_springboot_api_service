<template>
  <div>
    <el-card>
      <div class="connection-list-top">
        <div class="left-search-input-group">
          <div class="left-search-input">
            <el-input placeholder="请输入连接名称关键字搜索"
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
                     @click="addConnection">添加</el-button>
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
                         label="连接名称"
                         show-overflow-tooltip
                         min-width="20%"></el-table-column>
        <el-table-column prop="createTime"
                         label="创建时间"
                         min-width="18%"></el-table-column>
        <el-table-column label="数据库类型"
                         show-overflow-tooltip
                         min-width="15%">
          <template slot-scope="scope">
            <databaseIcon :type="scope.row.type"></databaseIcon>
            <span>{{ scope.row.type }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="url"
                         label="JDBC连接串"
                         show-overflow-tooltip
                         min-width="15%"></el-table-column>
        <el-table-column prop="username"
                         label="账号"
                         show-overflow-tooltip
                         min-width="10%"></el-table-column>
        <el-table-column label="操作"
                         min-width="35%">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="small"
                         type="danger"
                         icon="el-icon-video-play"
                         @click="handleTest(scope.$index, scope.row)"
                         round>测试</el-button>
              <el-button size="small"
                         type="primary"
                         icon="el-icon-document"
                         @click="handleMore(scope.$index, scope.row)"
                         round>详情</el-button>
              <el-button size="small"
                         type="warning"
                         icon="el-icon-edit"
                         @click="handleUpdate(scope.$index, scope.row)"
                         round>编辑</el-button>
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

      <el-dialog title="查看数据库连接信息"
                 :visible.sync="dialogFormVisible"
                 :showClose="false"
                 :before-close="handleClose">
        <el-form :model="queryForm"
                 size="mini">
          <el-form-item label="连接名称"
                        label-width="120px"
                        style="width:85%">
            <el-input v-model="queryForm.name"
                      auto-complete="off"
                      :readonly=true></el-input>
          </el-form-item>
          <el-form-item label="数据库类型"
                        label-width="120px"
                        style="width:85%">
            <el-input v-model="queryForm.type"
                      auto-complete="off"
                      :readonly=true></el-input>
          </el-form-item>
          <el-form-item label="数据库驱动"
                        label-width="120px"
                        style="width:85%">
            <el-input v-model="queryForm.driver"
                      auto-complete="off"
                      :readonly=true></el-input>
          </el-form-item>
          <el-form-item label="驱动版本号"
                        label-width="120px"
                        style="width:85%">
            <el-input v-model="queryForm.version"
                      auto-complete="off"
                      :readonly=true></el-input>
          </el-form-item>
          <el-form-item label="JDBC连接串"
                        label-width="120px"
                        style="width:85%">
            <el-input type="textarea"
                      :rows="6"
                      :spellcheck="false"
                      v-model="queryForm.url"
                      auto-complete="off"
                      :readonly=true></el-input>
          </el-form-item>
          <el-form-item label="账号名称"
                        label-width="120px"
                        style="width:85%">
            <el-input v-model="queryForm.username"
                      auto-complete="off"
                      :readonly=true></el-input>
          </el-form-item>
          <el-form-item label="连接密码"
                        label-width="120px"
                        style="width:85%">
            <el-input type="password"
                      v-model="queryForm.password"
                      auto-complete="off"
                      :readonly=true></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer"
             class="dialog-footer">
          <el-button @click="dialogFormVisible = false">关闭</el-button>
        </div>
      </el-dialog>

      <el-dialog title="添加数据源连接信息"
                 :visible.sync="createFormVisible"
                 :showClose="false"
                 :before-close="handleClose">
        <el-form :model="createform"
                 size="mini"
                 status-icon
                 :rules="rules"
                 ref="createform">
          <el-form-item label="连接名称"
                        label-width="120px"
                        :required=true
                        prop="name"
                        style="width:85%">
            <el-input v-model="createform.name"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="数据库类型"
                        label-width="120px"
                        :required=true
                        prop="type"
                        style="width:85%">
            <el-select v-model="createform.type"
                       @change="selectChangedDriverVersion"
                       placeholder="请选择数据库">
              <el-option v-for="(item,index) in databaseType"
                         :key="index"
                         :label="item.type"
                         :value="item.type">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="驱动版本"
                        label-width="120px"
                        :required=true
                        prop="version"
                        style="width:85%">
            <el-select v-model="createform.version"
                       placeholder="请选择版本">
              <el-option v-for="(item,index) in connectionDriver"
                         :key="index"
                         :label="item.driverVersion"
                         :value="item.driverVersion"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="JDBC连接串"
                        label-width="120px"
                        :required=true
                        prop="url"
                        style="width:85%">
            <el-alert title="样例："
                      type="warning"
                      :description="createform.sample">
            </el-alert>
            <el-input type="textarea"
                      :rows="6"
                      :spellcheck="false"
                      placeholder="请输入"
                      v-model="createform.url"
                      auto-complete="off">
            </el-input>
          </el-form-item>
          <el-form-item label="账号名称"
                        label-width="120px"
                        prop="username"
                        style="width:85%">
            <el-input v-model="createform.username"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="连接密码"
                        label-width="120px"
                        prop="password"
                        style="width:85%">
            <el-input type="password"
                      v-model="createform.password"
                      auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer"
             class="dialog-footer">
          <el-button @click="createFormVisible = false">取 消</el-button>
          <el-button type="primary"
                     @click="handleCreate">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="修改数据源连接信息"
                 :visible.sync="updateFormVisible"
                 :showClose="false"
                 :before-close="handleClose">
        <el-form :model="updateform"
                 size="mini"
                 status-icon
                 :rules="rules"
                 ref="updateform">
          <el-form-item label="连接名称"
                        label-width="120px"
                        :required=true
                        prop="name"
                        style="width:85%">
            <el-input v-model="updateform.name"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="数据库类型"
                        label-width="120px"
                        :required=true
                        prop="type"
                        style="width:85%">
            <el-select v-model="updateform.type"
                       @change="selectChangedDriverVersion"
                       placeholder="请选择数据库">
              <el-option v-for="(item,index) in databaseType"
                         :key="index"
                         :label="item.type"
                         :value="item.type"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="驱动版本"
                        label-width="120px"
                        :required=true
                        prop="version"
                        style="width:85%">
            <el-select v-model="updateform.version"
                       placeholder="请选择版本">
              <el-option v-for="(item,index) in connectionDriver"
                         :key="index"
                         :label="item.driverVersion"
                         :value="item.driverVersion"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="JDBC连接串"
                        label-width="120px"
                        :required=true
                        prop="url"
                        style="width:85%">
            <el-input type="textarea"
                      :rows="6"
                      :spellcheck="false"
                      v-model="updateform.url"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="账号名称"
                        label-width="120px"
                        prop="username"
                        style="width:85%">
            <el-input v-model="updateform.username"
                      auto-complete="off"></el-input>
          </el-form-item>
          <el-form-item label="连接密码"
                        label-width="120px"
                        prop="password"
                        style="width:85%">
            <el-input type="password"
                      v-model="updateform.password"
                      auto-complete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer"
             class="dialog-footer">
          <el-button @click="updateFormVisible = false">取 消</el-button>
          <el-button type="primary"
                     @click="handleSave">确 定</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import databaseIcon from "@/components/databaseIcon/databaseIcon";

export default {
  name: "datasource",
  components: {
    databaseIcon
  },
  data () {
    return {
      loading: true,
      keyword: null,
      lists: [],
      currentPage: 1,
      pageSize: 10,
      totalCount: 2,
      databaseType: [],
      connectionDriver: [],
      tableData: [
      ],
      queryForm: {
        title: "",
        type: "",
        url: "",
        diver: "",
        version: "",
        username: "",
        password: ""
      },
      createform: {
        title: "",
        type: "",
        diver: "",
        sample: "",
        url: "",
        version: "",
        username: "",
        password: ""
      },
      updateform: {
        id: 0,
        title: "",
        type: "",
        diver: "",
        version: "",
        username: "",
        password: ""
      },
      rules: {
        name: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "blur"
          }
        ],
        type: [
          {
            required: true,
            message: "数据库类型必须选择",
            trigger: "change"
          }
        ],
        version: [
          {
            required: true,
            message: "驱动版本必须选择",
            trigger: "change"
          }
        ],
        url: [
          {
            required: true,
            message: "Jdbc URL必须提供",
            trigger: "blur"
          }
        ],
        username: [
          {
            required: true,
            message: "连接账号名必须提供",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "连接密码必须提供",
            trigger: "blur"
          }
        ]
      },
      dialogFormVisible: false,
      createFormVisible: false,
      updateFormVisible: false
    }
  },
  methods: {
    loadData: function () {
      this.$http({
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        url: "/sqlrest/manager/api/v1/datasource/list",
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
          alert("加载任务列表失败:" + res.data.message);
        }
      },
        function () {
          console.log("load connection list failed");
        }
      );
    },
    searchByKeyword: function () {
      this.currentPage = 1;
      this.loadData();
    },
    loadDatabaseTypes: function () {
      this.databaseType = [];
      this.$http({
        method: "GET",
        url: "/sqlrest/manager/api/v1/datasource/types"
      }).then(
        res => {
          if (0 === res.data.code) {
            this.databaseType = res.data.data;
          } else {
            alert("加载任务列表失败:" + res.data.message);
          }
        },
        function () {
          console.log("failed");
        }
      );
    },
    handleClose (done) {
    },
    handleDelete: function (index, row) {
      this.$confirm(
        "此操作将此数据源ID=" + row.id + "删除么, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        this.$http.delete(
          "/sqlrest/manager/api/v1/datasource/delete/" + row.id
        ).then(res => {
          //console.log(res);
          if (0 === res.data.code) {
            this.loadData();
          } else {
            alert("删除任务失败:" + res.data.message);
          }
        });
      });
    },
    handleMore: function (index, row) {
      this.dialogFormVisible = true;
      this.queryForm = row;
    },
    handleTest: function (index, row) {
      this.$http.get(
        "/sqlrest/manager/api/v1/datasource/test/" + row.id
      ).then(res => {
        //console.log(res);
        if (0 === res.data.code) {
          alert("测试连接成功!");
        } else {
          alert("测试连接失败," + res.data.message);
        }
      });
    },
    addConnection: function () {
      this.createFormVisible = true;
      this.createform = {};
    },
    handleCreate: function () {
      let driverClass = "";
      if (this.databaseType.length > 0) {
        for (let i = 0; i < this.databaseType.length; i++) {
          //console.log(this.databaseType[i])
          if (this.databaseType[i].type == this.createform.type) {
            driverClass = this.databaseType[i].driver;
            break;
          }
        }
      }

      this.$refs['createform'].validate(valid => {
        if (valid) {
          this.$http({
            method: "POST",
            headers: {
              'Content-Type': 'application/json'
            },
            url: "/sqlrest/manager/api/v1/datasource/create",
            data: JSON.stringify({
              name: this.createform.name,
              type: this.createform.type,
              version: this.createform.version,
              driver: driverClass,
              url: this.createform.url,
              username: this.createform.username,
              password: this.createform.password
            })
          }).then(res => {
            if (0 === res.data.code) {
              this.createFormVisible = false;
              this.$message("添加连接信息成功");
              this.createform = {};
              this.loadData();
            } else {
              alert("添加连接信息失败:" + res.data.message);
            }
          });
        } else {
          alert("请检查输入");
        }
      });
    },
    selectChangedDriverVersion: function (value) {
      this.connectionDriver = [];
      this.$http.get(
        "/sqlrest/manager/api/v1/datasource/" + value + "/drivers"
      ).then(res => {
        if (0 === res.data.code) {
          this.connectionDriver = res.data.data;
          let varDatabaseType = this.databaseType.find(
            (item) => {
              return item.type === value;
            });
          if (varDatabaseType) {
            this.createform.sample = varDatabaseType.sample;
          }
        } else {
          this.$message.error("查询数据库可用的驱动版本失败," + res.data.message);
          this.connectionDriver = [];
        }
      });
    },
    handleUpdate: function (index, row) {
      this.updateform = JSON.parse(JSON.stringify(row));
      this.$http.get(
        "/sqlrest/manager/api/v1/datasource/" + this.updateform.type + "/drivers"
      ).then(res => {
        if (0 === res.data.code) {
          this.connectionDriver = res.data.data;
        } else {
          this.$message.error("查询数据库可用的驱动版本失败," + res.data.message);
          this.connectionDriver = [];
        }
      });
      this.updateFormVisible = true;
    },
    handleSave: function () {
      let driverClass = "";
      if (this.databaseType.length > 0) {
        for (let i = 0; i < this.databaseType.length; i++) {
          //console.log(this.databaseType[i])
          if (this.databaseType[i].type == this.updateform.type) {
            driverClass = this.databaseType[i].driver;
            break;
          }
        }
      }

      this.$refs['updateform'].validate(valid => {
        if (valid) {
          this.$http({
            method: "POST",
            headers: {
              'Content-Type': 'application/json'
            },
            url: "/sqlrest/manager/api/v1/datasource/update",
            data: JSON.stringify({
              id: this.updateform.id,
              name: this.updateform.name,
              type: this.updateform.type,
              version: this.updateform.version,
              driver: driverClass,
              url: this.updateform.url,
              username: this.updateform.username,
              password: this.updateform.password
            })
          }).then(res => {
            if (0 === res.data.code) {
              this.updateFormVisible = false;
              this.$message("修改连接信息成功");
              this.loadData();
              this.updateform = {};
            } else {
              alert("修改连接信息失败:" + res.data.message);
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
    }
  },
  created () {
    this.loadDatabaseTypes();
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
.connection-list-top {
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
