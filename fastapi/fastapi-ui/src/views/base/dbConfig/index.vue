<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="数据库名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入数据库名称"
          maxlength="50"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="数据库ip" prop="ipAddress">
        <el-input
          v-model="queryParams.ipAddress"
          placeholder="请输入数据库ip"
          maxlength="50"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检查状态" prop="checkStatus">
        <el-select v-model="queryParams.checkStatus" placeholder="检查状态" clearable>
          <el-option
            v-for="dict in dict.type.check_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['base:dbConfig:add']"
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
          v-hasPermi="['base:dbConfig:edit']"
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
          v-hasPermi="['base:dbConfig:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleCheckStatus"
        >一键自检</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dbConfigList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="数据库名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="数据库ip" align="center" prop="ipAddress" :show-overflow-tooltip="true" />
      <el-table-column label="检查状态" align="center" prop="checkStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.check_status" :value="scope.row.checkStatus"/>
        </template>
      </el-table-column>

      <el-table-column label="检查时间" align="center" prop="checkTime" :show-overflow-tooltip="true" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['base:dbConfig:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['base:dbConfig:remove']"
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

    <!-- 添加或修改数据源配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="数据库名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入数据库名称" maxlength="50" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库ip" prop="ipAddress">
              <el-input v-model="form.ipAddress" placeholder="请输入数据库ip" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库驱动" prop="connectDriver">
              <el-select v-model="form.connectDriver" placeholder="请选择数据库驱动">
                <el-option
                  v-for="dict in dict.type.connect_driver"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="连接url" prop="connectUrl">
          <el-input v-model="form.connectUrl" placeholder="请输入连接url" maxlength="255" />
        </el-form-item>

        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名" prop="userName">
              <el-input v-model="form.userName" placeholder="请输入用户名" maxlength="50" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码" prop="password">
              <el-input v-model="form.password" placeholder="请输入密码" type="password" maxlength="50" />
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { listDbConfig, getDbConfig, delDbConfig, addDbConfig, updateDbConfig, checkStatus } from "@/api/base/dbConfig";

  export default {
    name: "DbConfig",
    dicts: ['check_status', 'connect_driver'],
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
        // 数据源配置表格数据
        dbConfigList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          name: null,
          ipAddress: null,
          currentStatus: null,
          checkStatus: null,
        },
        // 表单参数
        form: {},
        // 表单校验
        // 表单校验
        rules: {
          name: [
            { required: true, message: "数据库名称不能为空", trigger: "blur" }
          ],
          ipAddress: [
            { required: true, message: "数据库ip不能为空", trigger: "blur" }
          ],
          connectUrl: [
            { required: true, message: "连接url不能为空", trigger: "blur" }
          ],
          connectDriver: [
            { required: true, message: "数据库驱动不能为空", trigger: "change" }
          ],
          userName: [
            { required: true, message: "用户名不能为空", trigger: "blur" }
          ],
          password: [
            { required: true, message: "密码不能为空", trigger: "blur" }
          ],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询数据源配置列表 */
      getList() {
        this.loading = true;
        listDbConfig(this.queryParams).then(response => {
          this.dbConfigList = response.rows;
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
          ipAddress: null,
          connectUrl: null,
          connectDriver: null,
          userName: null,
          password: null,
          currentStatus: 0,
          createBy: null,
          createTime: null,
          updateBy: null,
          updateTime: null,
          remark: null,
          checkStatus: 1,
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
        this.reset();
        this.open = true;
        this.title = "添加数据源配置";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const id = row.id || this.ids
        getDbConfig(id).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改数据源配置";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.id != null) {
              updateDbConfig(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addDbConfig(this.form).then(response => {
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
        this.$modal.confirm('是否确认删除数据源配置编号为"' + ids + '"的数据项？').then(function() {
          return delDbConfig(ids);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 一键自检按钮操作 */
      handleCheckStatus() {
        this.$modal.msgSuccess("已启动自检,请稍后刷新");
        checkStatus();
      },
    }
  };
</script>
