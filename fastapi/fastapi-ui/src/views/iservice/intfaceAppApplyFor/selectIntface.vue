<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用名称" prop="appManagerName">
        <el-input
          v-model="queryParams.appManagerName"
          placeholder="请输入应用名称"
          maxlength="50"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接口名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入接口名称"
          maxlength="50"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接口描述" prop="depict">
        <el-input
          v-model="queryParams.depict"
          placeholder="请输入接口描述"
          maxlength="100"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="addIntfaceAppAuthMethod"
          v-hasPermi="['iservice:intfaceAppApplyFor:addAuth']"
        >添加授权</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="cancleAuthMethod"
          v-hasPermi="['iservice:intfaceAppApplyFor:cancelAuth']"
        >取消授权</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-close"
          size="mini"
          @click="handleClose"
        >关闭</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="selectIntfaceManagerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="序号"
        type="index"
        :index="indexMethod">
      </el-table-column>
      <el-table-column label="应用名称" align="center" prop="appManagerName" :show-overflow-tooltip="true" />
      <el-table-column label="应用前缀" align="center" prop="appManagerPrefix" :show-overflow-tooltip="true" />
      <el-table-column label="接口名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="接口描述" align="center" prop="depict" :show-overflow-tooltip="true" />
      <el-table-column label="接口状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.interface_auth_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-reading"
            @click="selectIntface(scope.row)"
            v-hasPermi="['imanager:selectIntfaceManager:query']"
          >查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改查询接口管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="接口名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入接口名称" disabled="disabled" maxlength="50" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="结果类型" prop="resultType">
              <el-select v-model="form.resultType" placeholder="请选择结果类型" disabled="disabled">
                <el-option
                  v-for="dict in dict.type.result_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="启用缓存" prop="openCache">
              <el-select v-model="form.openCache" placeholder="请选择是否启用缓存" disabled="disabled">
                <el-option
                  v-for="dict in dict.type.open_cache"
                  :key="dict.value"
                  :label="dict.label"
                  :value="parseInt(dict.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="所属应用" prop="appManagerId" class="tree-wrap">
          <treeselect v-model="form.appManagerId" :multiple="false"   placeholder="请选择所属应用" disabled="disabled"
                      :show-count="true" :options="appManagerIdOptions" />
        </el-form-item>

        <el-form-item label="sql语句" prop="sqlStr">
          <el-input v-model="form.sqlStr" :rows="5" type="textarea" placeholder="请输入内容" maxlength="2000" disabled="disabled" />
        </el-form-item>

        <el-form-item label="接口描述" prop="depict">
          <el-input v-model="form.depict" placeholder="请输入接口描述" maxlength="100" disabled="disabled" />
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
  import { listSelectIntfaceManager, getSelectIntfaceManager, delSelectIntfaceManager, addSelectIntfaceManager, updateSelectIntfaceManager, updateConverInfo } from "@/api/imanager/selectIntfaceManager";
  import { findAll } from "@/api/base/appManager";
  import { listIntfaceAppAuth, addIntfaceAppAuth, cancleAuth } from "@/api/iservice/intfaceAppAuth";

  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "SelectIntfaceManager",
    components: { Treeselect },
    dicts: ['open_cache', 'result_type', 'interface_auth_status'],
    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 查询接口管理表格数据
        selectIntfaceManagerList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 所有应用列表
        appManagerIdOptions:[],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: null,
          resultType: null,
          appManagerName: null,
          depict: null,
          appManagerId: null,
          intfaceAppId: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {

        }
      };
    },
    created() {
      this.getList();
    },
    computed: {
      indexMethod: function() {
        return (this.queryParams.pageNum - 1) * (this.queryParams.pageSize) + 1
      }
    },
    methods: {
      /** 关闭按钮操作 */
      handleClose() {
        const obj = { path: "/iservice/intfaceAppApplyFor" };
        this.$tab.closeOpenPage(obj);
      },
      /** 添加授权操作 */
      addIntfaceAppAuthMethod(row) {
        const ids = row.id || this.ids;
        const intfaceAppId = this.$route.params && this.$route.params.intfaceAppId;
        this.$modal.confirm('是否确认添加授权？').then(function() {
          const data = {
            "intfaceAppId" : intfaceAppId,
            "selectIntfaceId" : ids
          }
          return addIntfaceAppAuth(data);
        }).then(() => {
          this.$modal.msgSuccess("添加授权成功");
          this.getList();
        }).catch(() => {});
      },
      /** 取消授权操作 */
      cancleAuthMethod(row) {
        const ids = row.id || this.ids;
        const intfaceAppId = this.$route.params && this.$route.params.intfaceAppId;
        this.$modal.confirm('是否确认取消授权？').then(function() {
          const data = {
            "intfaceAppId" : intfaceAppId,
            "selectIntfaceId" : ids
          }
          return cancleAuth(data);
        }).then(() => {
          this.$modal.msgSuccess("取消授权成功");
          this.getList();
        }).catch(() => {});
      },
      /** 查询查询接口管理列表 */
      getList() {
        this.loading = true;
        this.queryParams.intfaceAppId = this.$route.params && this.$route.params.intfaceAppId;
        debugger;
        listIntfaceAppAuth(this.queryParams).then(response => {
          this.selectIntfaceManagerList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          id: null,
          name: null,
          resultType: null,
          appManagerId: null,
          appManagerName: null,
          appManagerPrefix: null,
          openCache: null,
          depict: null,
          kvConver: null,
          rcConver: null,
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 重置按钮操作 */
      converResetQuery() {
        this.form.kvConver = null;
        this.form.rcConver = null;
        this.form.keyExtract = null;
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.id)
        this.single = selection.length!==1
        this.multiple = !selection.length
      },
      /** 修改按钮操作 */
      selectIntface(row) {
        findAll().then(response => {
          debugger;
          this.appManagerIdOptions = [];
          for (let i = 0; i < response.data.length; i++) {
            var data = response.data[i];
            var obj = {label:data.name, id:data.id};
            this.appManagerIdOptions.push(obj);
          }
        });
        this.reset();
        const id = row.id || this.ids
        getSelectIntfaceManager(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "查看接口详情";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateSelectIntfaceManager(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addSelectIntfaceManager(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
    }
  };
</script>

<style lang="scss" scoped>
  .tree-wrap ::v-deep .vue-treeselect__control {
    padding-left: 10px;
  }
</style>
