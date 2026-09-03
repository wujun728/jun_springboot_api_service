<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="应用名称" prop="appManagerName">
        <el-input
          v-model="queryParams.appManagerName"
          placeholder="请输入应用名称"
          clearable
          maxlength="50"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接口名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入接口名称"
          clearable
          maxlength="50"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接口描述" prop="depict">
        <el-input
          v-model="queryParams.depict"
          placeholder="请输入接口描述"
          clearable
          maxlength="100"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="intfaceReverseHistoryList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        label="序号"
        type="index"
        :index="indexMethod">
      </el-table-column>
      <el-table-column label="应用名称" align="center" prop="appManagerName" />
      <el-table-column label="应用前缀" align="center" prop="appManagerPrefix" />
      <el-table-column label="接口名称" align="center" prop="name" />
      <el-table-column label="接口描述" align="center" prop="depict" />
      <el-table-column label="调用类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.intface_reverse_history_type" :value="scope.row.type"/>
        </template>
      </el-table-column>

      <el-table-column label="调用方" align="center" prop="type">
        <template slot-scope="scope">
          {{ handleReverseName(scope.row) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-reading"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['iservice:intfaceReverseHistory:list']"
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

    <!-- 添加或修改接口调用历史对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="应用名称" prop="appManagerName">
          <el-input v-model="form.appManagerName" placeholder="请输入应用名称" maxlength="50"  disabled="disabled" />
        </el-form-item>
        <el-form-item label="应用前缀" prop="appManagerPrefix">
          <el-input v-model="form.appManagerPrefix" placeholder="请输入a应用前缀"  disabled="disabled" />
        </el-form-item>
        <el-form-item label="接口名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入接口名称" maxlength="50"  disabled="disabled" />
        </el-form-item>
        <el-form-item label="接口描述" prop="depict">
          <el-input v-model="form.depict" placeholder="请输入接口描述" maxlength="100"  disabled="disabled" />
        </el-form-item>
        <el-form-item label="调用状态" prop="reverseStatus">
          <el-input v-model="reverseStatusStr" placeholder="请输入调用状态" disabled="disabled" />
        </el-form-item>
        <el-form-item label="调用耗时" prop="reverseTime">
          <el-input v-model="reverseTimeStr" placeholder="请输入调用耗时" disabled="disabled" />
        </el-form-item>
        <el-form-item label="调用时间" prop="createTime">
          <el-input v-model="form.createTime" placeholder="调用时间" disabled="disabled" />
        </el-form-item>
        <el-form-item label="输入参数" prop="inputParam">
          <el-input v-model="form.inputParam" placeholder="请输入内容" type="textarea" :rows="3"  disabled="disabled" />
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { listIntfaceReverseHistory, getIntfaceReverseHistory} from "@/api/iservice/intfaceReverseHistory";

export default {
  name: "IntfaceReverseHistory",
  dicts: ['intface_reverse_history_type'],
  computed: {
    indexMethod: function() {
      return (this.queryParams.pageNum - 1) * (this.queryParams.pageSize) + 1
    },
    reverseStatusStr: function(){
      let reverseStatus = this.form.reverseStatus;
      if(reverseStatus=="1"){
        return "调用成功";
      }else {
        return "调用失败";
      }
    },
    reverseTimeStr: function(){
      let reverseTime = this.form.reverseTime;
      if(reverseTime=="-1"){
        return "/";
      }else {
        return reverseTime+"毫秒";
      }
    },
  },
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
      // 接口调用历史表格数据
      intfaceReverseHistoryList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        appManagerName: null,
        depict: null,
        inputParam: null,
        reverseUserId: null,
        reverseAppApplyForId: null,
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
  methods: {
    handleReverseName(row){
      if(1==row.type){
        return row.reverseUserName
      }else if(2==row.type){
        return row.reverseAppApplyForName
      } if(4==row.type){
        return '- -';
      }
    },
    /** 查询接口调用历史列表 */
    getList() {
      this.loading = true;
      listIntfaceReverseHistory(this.queryParams).then(response => {
        this.intfaceReverseHistoryList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        selectIntfaceManagerId: null,
        name: null,
        appManagerName: null,
        appManagerPrefix: null,
        depict: null,
        inputParam: null,
        type: null,
        reverseUserId: null,
        reverseUserName: null,
        reverseAppApplyForId: null,
        reverseAppApplyForName: null,
        createTime: null
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
      this.title = "添加接口调用历史";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getIntfaceReverseHistory(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "调用详情";
      });
    },
  }
};
</script>
