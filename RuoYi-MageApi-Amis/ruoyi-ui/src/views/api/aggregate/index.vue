<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="前缀" prop="prefix"><el-input v-model="queryParams.prefix" placeholder="应用前缀" clearable @keyup.enter.native="handleQuery" /></el-form-item>
      <el-form-item label="名称" prop="name"><el-input v-model="queryParams.name" placeholder="API名称" clearable @keyup.enter.native="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button></el-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>
    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="编号" align="center" prop="id" width="80" />
      <el-table-column label="前缀" align="center" prop="prefix" width="120" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="结果类型" align="center" prop="resultType" width="100">
        <template slot-scope="s">{{ s.row.resultType===1?'单对象':'列表' }}</template>
      </el-table-column>
      <el-table-column label="缓存" align="center" prop="openCache" width="80">
        <template slot-scope="s"><el-tag :type="s.row.openCache===1?'success':'info'">{{ s.row.openCache===1?'开启':'关闭' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template slot-scope="s">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(s.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(s.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="前缀" prop="prefix"><el-input v-model="form.prefix" /></el-form-item>
        <el-form-item label="名称" prop="name"><el-input v-model="form.name" placeholder="多个用逗号分隔" /></el-form-item>
        <el-form-item label="SQL" prop="sqlContent"><el-input type="textarea" :rows="8" v-model="form.sqlContent" /></el-form-item>
        <el-form-item label="结果类型"><el-radio-group v-model="form.resultType"><el-radio :label="1">单对象</el-radio><el-radio :label="2">列表</el-radio></el-radio-group></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="cancel">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></div>
    </el-dialog>
  </div>
</template>
<script>
import { listAggregate, getAggregate, addAggregate, updateAggregate, delAggregate } from '@/api/api/aggregate'
export default {
  name: 'ApiAggregate',
  data() {
    return {
      loading: false, showSearch: true, open: false, title: '', total: 0, dataList: [],
      queryParams: { pageNum: 1, pageSize: 10, prefix: '', name: '' },
      form: {}, rules: { prefix: [{required:true,message:'不能为空',trigger:'blur'}], name: [{required:true,message:'不能为空',trigger:'blur'}] }
    }
  },
  created() { this.getList() },
  methods: {
    getList() { this.loading = true; listAggregate(this.queryParams).then(r => { this.dataList = r.rows||[]; this.total = r.total||0; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    handleAdd() { this.reset(); this.open = true; this.title = '新增聚合API' },
    handleUpdate(row) { this.reset(); getAggregate(row.id).then(r => { this.form = r.data; this.open = true; this.title = '编辑聚合API' }) },
    handleDelete(row) { this.$confirm('确认删除?').then(() => delAggregate(row.id).then(() => { this.getList(); this.$message.success('删除成功') })) },
    submitForm() { this.$refs.form.validate(v => { if(v) { (this.form.id?updateAggregate:addAggregate)(this.form).then(() => { this.$message.success('操作成功'); this.open = false; this.getList() }) } }) },
    cancel() { this.open = false; this.reset() },
    reset() { this.form = { prefix: '', name: '', sqlContent: '', resultType: 2, openCache: 0, status: '1' }; this.resetForm('form') }
  }
}
</script>