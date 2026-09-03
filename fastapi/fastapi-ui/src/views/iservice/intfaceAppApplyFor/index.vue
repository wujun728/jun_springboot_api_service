<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入应用名称"
          clearable
          maxlength="20"
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
          v-hasPermi="['iservice:intfaceAppApplyFor:add']"
        >新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="intfaceAppApplyForList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="序号"
        type="index"
        :index="indexMethod">
      </el-table-column>
      <el-table-column label="应用名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="访问ip" align="center" prop="accessIp" :show-overflow-tooltip="true"  />
      <el-table-column label="联系人" align="center" prop="linkPerson" :show-overflow-tooltip="true"  />
      <el-table-column label="联系电话" align="center" prop="linkPhone" :show-overflow-tooltip="true"  />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iservice:intfaceAppApplyFor:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['iservice:intfaceAppApplyFor:remove']"
          >删除</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
            <span class="el-dropdown-link">
              <i class="el-icon-d-arrow-right el-icon--right"></i>接口权限
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="selectSecretMethod" icon="el-icon-key"
                                v-hasPermi="['iservice:intfaceAppApplyFor:query']">查看密钥</el-dropdown-item>
              <el-dropdown-item command="selectIntfaceMethod" icon="el-icon-notebook-1"
                                v-hasPermi="['iservice:intfaceAppApplyFor:query']">查看接口</el-dropdown-item>
              <el-dropdown-item command="resetSecretMethod" icon="el-icon-refresh"
                                v-hasPermi="['iservice:intfaceAppApplyFor:edit']">重置密钥</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
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

    <!-- 添加或修改接口应用申请对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="应用名称" prop="name">
          <el-input :disabled="disabledName" v-model="form.name" maxlength="20" placeholder="请输入应用名称" />
        </el-form-item>
        <el-form-item label="访问ip" prop="accessIp">
          <el-input v-model="form.accessIp" maxlength="50" placeholder="请输入访问ip,多个以逗号分割" />
        </el-form-item>
        <el-form-item label="联系人" prop="linkPerson">
          <el-input v-model="form.linkPerson" maxlength="20" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="linkPhone">
          <el-input v-model="form.linkPhone" maxlength="20" placeholder="请输入联系电话" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改接口应用申请对话框 -->
    <el-dialog :title="title" :visible.sync="secretOpen" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="clientId:" prop="clientId">
          <label>{{ form.clientId }}</label>
        </el-form-item>
        <el-form-item label="clientSecret:" prop="clientSecret">
          <label>{{ form.clientSecret }}</label>
        </el-form-item>
        <span style="color:red">当前密钥有效期为{{ form.clientExpireDay }}天({{ form.clientStartTime }} 至 {{ form.clientEndTime }}),到期后将自动重置密钥</span>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import { listIntfaceAppApplyFor, getIntfaceAppApplyFor, delIntfaceAppApplyFor, addIntfaceAppApplyFor, updateIntfaceAppApplyFor,
         selectSecret, resetSecret } from "@/api/iservice/intfaceAppApplyFor";

export default {
  name: "IntfaceAppApplyFor",
  data() {
    return {
      disabledName:false,
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
      // 接口应用申请表格数据
      intfaceAppApplyForList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层
      secretOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "应用名称不能为空", trigger: "blur" }
        ],
        accessIp: [
          /*{ required: true, message: "访问ip不能为空", trigger: "blur" }*/
        ],
        linkPerson: [
          { required: true, message: "联系人不能为空", trigger: "blur" }
        ],
        linkPhone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
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
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "selectSecretMethod":
          this.selectSecretMethod(row);
          break;
        case "resetSecretMethod":
          this.resetSecretMethod(row);
          break;
        case "selectIntfaceMethod":
          this.selectIntfaceMethod(row);
          break;
        default:
          break;
      }
    },

    /** 查询密钥 */
    selectSecretMethod(row) {
      this.reset();
      const id = row.id;
      selectSecret(id).then(response => {
        this.form = response.data;
        debugger;
        this.secretOpen = true;
        this.title = "查看密钥";
      });
    },

    /** 重置密钥 */
    resetSecretMethod(row) {
      this.reset();
      const id = row.id;
      this.$modal.confirm('是否重置密钥？').then(function() {
        return resetSecret(id).then(response => {

        });
      }).then(() => {
        this.$modal.msgSuccess("重置成功");
      }).catch(() => {});
    },

    /** 查看接口 */
    selectIntfaceMethod(row) {
      this.reset();
      const id = row.id;
      this.$router.push("/iservice/AppApplyFor/selectIntface/auth/"+id);
    },

    /** 查询接口应用申请列表 */
    getList() {
      this.loading = true;
      listIntfaceAppApplyFor(this.queryParams).then(response => {
        this.intfaceAppApplyForList = response.rows;
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
        accessIp: null,
        linkPerson: null,
        linkPhone: null,
        clientId: null,
        clientSecret: null,
        clientStartTime: null,
        clientEndTime: null,
        clientExpireDay: null,
        createId: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
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
      this.disabledName = false
      this.open = true;
      this.title = "添加接口应用申请";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      this.disabledName = true
      getIntfaceAppApplyFor(id).then(response => {
        this.form = response.data;
        debugger;
        this.open = true;
        this.title = "修改接口应用申请";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateIntfaceAppApplyFor(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addIntfaceAppApplyFor(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除' + row.name + '？').then(function() {
        return delIntfaceAppApplyFor(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('iservice/intfaceAppApplyFor/export', {
        ...this.queryParams
      }, `intfaceAppApplyFor_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
