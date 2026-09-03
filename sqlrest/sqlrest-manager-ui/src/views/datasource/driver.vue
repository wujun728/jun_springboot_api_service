<template>
  <div>
    <el-card>
      <div class="container">
        <el-card class="box-card">
          <div slot="header"
               align="center"
               class="clearfix">
            <span><b>数据库类型列表</b></span>
          </div>
          <div class="navsBox">
            <ul>
              <li v-for="(item,index) in connectionTypes"
                  :key="index"
                  @click="handleChooseClick(item.type,index)"
                  :class="{active:index==isActive}">
                  <databaseIcon :type="item.type"></databaseIcon>
                  [{{item.id}}]{{item.type}}</li>
            </ul>
          </div>
        </el-card>

        <div class="contentBox">
          <div align="right"
               style="margin:10px 5px;"
               width="95%">
            <el-button type="primary"
                       size="mini"
                       icon="el-icon-document-add"
                       @click="dialogVisible=true">添加</el-button>
          </div>
          <el-table :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                    :data="versionDrivers"
                    size="small"
                    stripe
                    border>
            <template slot="empty">
              <span>单击左侧数据库类型来查看对应的驱动版本信息</span>
            </template>
            <el-table-column property="driverVersion"
                             label="驱动版本号"
                             min-width="15%"></el-table-column>
            <el-table-column property="driverClass"
                             label="驱动类名"
                             min-width="20%"></el-table-column>
            <el-table-column property="jarFiles"
                             :formatter="formatJarFileList"
                             label="驱动JAR名称"
                             min-width="30%"></el-table-column>
            <el-table-column property="driverPath"
                             label="驱动版本路径"
                             min-width="50%"></el-table-column>

          </el-table>
        </div>
      </div>
    </el-card>
    <el-dialog title="添加数据库驱动JAR说明"
               :visible.sync="dialogVisible"
               width="40%"
               :before-close="handleClose">
      <span>请按照驱动路径所在的目录${DBSWITCH_HOME}/drivers下，在数据库类型为名称的目录下，以驱动版本号为名称创建目录并放置对应的驱动jar文件，然后重启即可生效。具体可参考https://gitee.com/inrgihc/dbswitch/tree/master/drivers下的目录结构。</span>
      <span></span>
      <span>特殊说明：驱动版本目录下的所有JAR必须无任何外部依赖，否则，也需将其依赖JAR一起放置到对应的目录下。</span>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary"
                   @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import databaseIcon from "@/components/databaseIcon/databaseIcon";
export default {
  data () {
    return {
      dialogVisible: false,
      loading: true,
      connectionTypes: [],
      versionDrivers: [],
      isActive: -1,
    };
  },
  components: {
    databaseIcon
  },
  methods: {
    loadConnectionTypes: function () {
      this.$http({
        method: "GET",
        url: "/sqlrest/manager/api/v1/datasource/types"
      }).then(res => {
        if (0 === res.data.code) {
          this.connectionTypes = res.data.data;
          this.handleChooseClick('MYSQL', 0);
        } else {
          if (res.data.message) {
            alert("初始化数据库类型信息失败:" + res.data.message);
          }
        }
      }
      );
    },
    handleChooseClick: function (type, index) {
      this.isActive = index;
      this.$http.get(
        "/sqlrest/manager/api/v1/datasource/" + type + "/drivers"
      ).then(res => {
        if (0 === res.data.code) {
          this.versionDrivers = res.data.data;
        } else {
          if (res.data.message) {
            alert("查询驱动版本信息失败," + res.data.message);
          }
        }
      });
    },
    handleClose (done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done();
        })
        .catch(_ => { });
    },
    formatJarFileList: function (row, column) {
      let jarFiles = row[column.property];
      return jarFiles.join(';\n');
    }
  },
  created () {
    this.loadConnectionTypes();
  },
  beforeDestroy () {
  },
};
</script>

<style scoped>
.el-card,
.el-message {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.el-table {
  width: 100%;
  border-collapse: collapse;
}

.demo-table-expand {
  font-size: 0;
}

.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}

.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}

.filter {
  margin: 10px;
}

.container {
  display: flex;
  height: 100%;
}

.container > * {
  float: left; /* 水平排列 */
}

.container .el-card {
  width: 20%;
  height: 100%;
  overflow: auto;
}

.container .el-card__header {
  padding: 8px 10px;
  border-bottom: 1px solid #ebeef5;
  box-sizing: border-box;
}

.container .navsBox ul {
  margin: 0;
  padding-left: 10px;
}

.container .navsBox ul li {
  list-style: none;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrop;
  cursor: pointer; /*鼠标悬停变小手*/
  padding: 10px 0;
  border-bottom: 1px solid #e0e0e0;
  width: 100%;
}

.container .navsBox .active {
  background: #bcbcbe6e;
  color: rgb(46, 28, 88);
}

.container .contentBox {
  padding: 10px;
  width: calc(100% - 250px);
}
</style>
