<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="应用名称" prop="appName"><el-input v-model="queryParams.appName" placeholder="请输入应用名称" clearable @keyup.enter.native="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>
    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="编号" align="center" prop="id" width="80" />
      <el-table-column label="AppKey" align="center" prop="appKey" min-width="150" />
      <el-table-column label="AppSecret" align="center" prop="appSecret" min-width="200" />
      <el-table-column label="应用名称" align="center" prop="appName" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="s"><el-tag :type="s.row.status==='1'?'success':'danger'">{{ s.row.status==='1'?'启用':'禁用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="s">
          <el-button size="mini" type="text" icon="el-icon-key" @click="showSecret(s.row)">密钥</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(s.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="appName"><el-input v-model="form.appName" /></el-form-item>
        <el-form-item label="AppKey" prop="appKey"><el-input v-model="form.appKey" :disabled="!!form.id" /></el-form-item>
        <el-form-item label="AppSecret" prop="appSecret"><el-input v-model="form.appSecret" type="password" /></el-form-item>
        <el-form-item label="IP白名单"><el-input v-model="form.ipWhitelist" placeholder="127.0.0.1,localhost" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="cancel">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></div>
    </el-dialog>
    <el-dialog title="密钥信息" :visible.sync="openSecret" width="400px" append-to-body>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="AppKey">{{ secretInfo.appKey }}</el-descriptions-item>
        <el-descriptions-item label="AppSecret">{{ secretInfo.appSecret }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>
<script>
import { listOpenApiApp, getOpenApiApp, addOpenApiApp, updateOpenApiApp, delOpenApiApp } from '@/api/api/openapi'
export default {
  name: 'ApiOpenapi',
  data() {
    return {
      loading: false, showSearch: true, open: false, openSecret: false, title: '', total: 0, dataList: [], secretInfo: {},
      queryParams: { pageNum: 1, pageSize: 10, appName: '' },
      form: {}, rules: { appName: [{required:true,message:'不能为空',trigger:'blur'}], appKey: [{required:true,message:'不能为空',trigger:'blur'}], appSecret: [{required:true,message:'不能为空',trigger:'blur'}] }
    }
  },
  created() { this.getList() },
  methods: {
    getList() { this.loading = true; listOpenApiApp(this.queryParams).then(r => { this.dataList = r.rows||[]; this.total = r.total||0; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    handleAdd() { this.reset(); this.open = true; this.title = '新增应用' },
    showSecret(row) { this.secretInfo = { appKey: row.appKey, appSecret: row.appSecret }; this.openSecret = true },
    handleDelete(row) { this.$confirm('确认删除?').then(() => delOpenApiApp(row.id).then(() => { this.getList(); this.$message.success('删除成功') })) },
    submitForm() { this.$refs.form.validate(v => { if(v) { (this.form.id?updateOpenApiApp:addOpenApiApp)(this.form).then(() => { this.$message.success('操作成功'); this.open = false; this.getList() }) } }) },
    cancel() { this.open = false; this.reset() },
    reset() { this.form = { appKey: '', appSecret: '', appName: '', status: '1' }; this.resetForm('form') }
  }
}
</script>