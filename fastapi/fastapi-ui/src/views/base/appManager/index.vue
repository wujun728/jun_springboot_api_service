<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入应用名称"
          clearable
          maxlength="50"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="应用前缀" prop="prefix">
        <el-input
          v-model="queryParams.prefix"
          placeholder="请输入应用前缀"
          clearable
          maxlength="50"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['base:appManager:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['base:appManager:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="single"
          @click="handleDelete"
          v-hasPermi="['base:appManager:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appManagerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="应用名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="应用前缀" align="center" prop="prefix" :show-overflow-tooltip="true" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:appManager:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:appManager:remove']"
          >删除</el-button>
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

    <!-- 添加或修改应用管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="应用名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入应用名称" maxlength="50" />
        </el-form-item>
        <el-form-item label="应用前缀" prop="prefix">
          <el-input v-model="form.prefix" placeholder="请输入应用前缀" maxlength="50" />
        </el-form-item>
        <el-form-item label="数据源配置" prop="prefix">
          <treeselect v-model="form.dbConfigId" :multiple="false"   placeholder="请选择数据源配置"
                      :show-count="true" :options="dbConfigOptions" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAppManager, getAppManager, delAppManager, addAppManager, updateAppManager } from "@/api/base/appManager";
import { findAll } from "@/api/base/dbConfig";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
export default {
  name: "AppManager",
  components: { Treeselect },
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
      // 应用管理表格数据
      appManagerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 所有数据源列表
      dbConfigOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        prefix: null,
        dbConfigId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "应用名称不能为空", trigger: "blur" }
        ],
        prefix: [
          { required: true, message: "应用前缀不能为空", trigger: "blur" }
        ],
        dbConfigId: [
          { required: true, message: "数据源id不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询应用管理列表 */
    getList() {
      this.loading = true;
      listAppManager(this.queryParams).then(response => {
        this.appManagerList = response.rows;
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
        prefix: null,
        dbConfigId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        dbConfigId: null,
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      findAll().then(response => {
        debugger;
        this.dbConfigOptions = [];
        for (let i = 0; i < response.data.length; i++) {
          var data = response.data[i];
          var obj = {label:data.name, id:data.id};
          this.dbConfigOptions.push(obj);
        }
      });
      this.reset();
      this.open = true;
      this.title = "添加应用管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      findAll().then(response => {
        debugger;
        this.dbConfigOptions = [];
        for (let i = 0; i < response.data.length; i++) {
          var data = response.data[i];
          var obj = {label:data.name, id:data.id};
          this.dbConfigOptions.push(obj);
        }
      });
      this.reset();
      const id = row.id || this.ids;
      getAppManager(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改应用管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAppManager(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAppManager(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除应用管理编号为"' + ids + '"的数据项？').then(function() {
        return delAppManager(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('base/appManager/export', {
        ...this.queryParams
      }, `appManager_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
