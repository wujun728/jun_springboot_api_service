<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="名称" prop="dsName">
        <el-input v-model="queryParams.dsName" placeholder="请输入数据源名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="类型" prop="dsType">
        <el-select v-model="queryParams.dsType" placeholder="数据库类型" clearable>
          <el-option label="MySQL" value="mysql" />
          <el-option label="Oracle" value="oracle" />
          <el-option label="PostgreSQL" value="postgresql" />
          <el-option label="SQL Server" value="sqlserver" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>
    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" width="80" />
      <el-table-column label="名称" align="center" prop="dsName" />
      <el-table-column label="类型" align="center" prop="dsType" width="120" />
      <el-table-column label="URL" align="center" prop="dsUrl" min-width="300" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '1' ? 'success' : 'danger'">{{ scope.row.status === '1' ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="dsName"><el-input v-model="form.dsName" placeholder="请输入数据源名称" /></el-form-item>
        <el-form-item label="类型" prop="dsType">
          <el-select v-model="form.dsType" placeholder="请选择数据库类型">
            <el-option label="MySQL" value="mysql" />
            <el-option label="Oracle" value="oracle" />
            <el-option label="PostgreSQL" value="postgresql" />
            <el-option label="SQL Server" value="sqlserver" />
          </el-select>
        </el-form-item>
        <el-form-item label="URL" prop="dsUrl"><el-input v-model="form.dsUrl" placeholder="jdbc:mysql://localhost:3306/db" /></el-form-item>
        <el-form-item label="用户名" prop="dsUsername"><el-input v-model="form.dsUsername" placeholder="请输入用户名" /></el-form-item>
        <el-form-item label="密码" prop="dsPassword"><el-input v-model="form.dsPassword" type="password" placeholder="请输入密码" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listDataSource, getDataSource, addDataSource, updateDataSource, delDataSource } from '@/api/api/datasource'
export default {
  name: 'ApiDatasource',
  data() {
    return {
      loading: false, showSearch: true, open: false, title: '', ids: [], single: true, multiple: true, total: 0,
      dataList: [],
      queryParams: { pageNum: 1, pageSize: 10, dsName: '', dsType: '' },
      form: {},
      rules: { dsName: [{ required: true, message: '名称不能为空', trigger: 'blur' }], dsUrl: [{ required: true, message: 'URL不能为空', trigger: 'blur' }], dsUsername: [{ required: true, message: '用户名不能为空', trigger: 'blur' }] }
    }
  },
  created() { this.getList() },
  methods: {
    getList() {
      this.loading = true
      listDataSource(this.queryParams).then(res => { this.dataList = res.rows || []; this.total = res.total || 0; this.loading = false })
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    handleSelectionChange(selection) { this.ids = selection.map(i => i.id); this.single = selection.length !== 1; this.multiple = !selection.length },
    handleAdd() { this.reset(); this.open = true; this.title = '新增数据源' },
    handleUpdate(row) { this.reset(); getDataSource(row.id || row.ids).then(res => { this.form = res.data; this.open = true; this.title = '修改数据源' }) },
    handleDelete(row) { const ids = row.id ? [row.id] : this.ids; this.$confirm('确认删除?').then(() => delDataSource(ids).then(() => { this.getList(); this.$message.success('删除成功') })) },
    submitForm() { this.$refs.form.validate(valid => { if (valid) { (this.form.id ? updateDataSource : addDataSource)(this.form).then(() => { this.$message.success('操作成功'); this.open = false; this.getList() }) } }) },
    cancel() { this.open = false; this.reset() },
    reset() { this.form = { dsName: '', dsType: 'mysql', dsUrl: '', dsUsername: '', dsPassword: '', status: '1' }; this.resetForm('form') }
  }
}
</script>