<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="路径" prop="apiPath"><el-input v-model="queryParams.apiPath" placeholder="请输入API路径" clearable @keyup.enter.native="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button></el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>
    <el-table v-loading="loading" :data="dataList">
      <el-table-column label="编号" align="center" prop="id" width="80" />
      <el-table-column label="路径" align="center" prop="apiPath" min-width="200" />
      <el-table-column label="名称" align="center" prop="apiName" />
      <el-table-column label="类型" align="center" prop="scriptType" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="s"><el-tag :type="s.row.status==='1'?'success':'danger'">{{ s.row.status==='1'?'启用':'禁用' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="s">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(s.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(s.row)">删除</el-button>
          <el-button size="mini" type="text" icon="el-icon-video-play" @click="handleTest(s.row)">测试</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="路径" prop="apiPath"><el-input v-model="form.apiPath" placeholder="/api/demo/hello" /></el-form-item>
        <el-form-item label="名称" prop="apiName"><el-input v-model="form.apiName" /></el-form-item>
        <el-form-item label="类型" prop="scriptType">
          <el-radio-group v-model="form.scriptType"><el-radio label="groovy">Groovy</el-radio><el-radio label="sql">SQL</el-radio><el-radio label="java">Java</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="脚本" prop="scriptContent">
          <el-input type="textarea" :rows="12" v-model="form.scriptContent" placeholder="return [message: 'Hello']" />
        </el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="cancel">取 消</el-button><el-button type="primary" @click="submitForm">确 定</el-button></div>
    </el-dialog>
    <el-dialog title="测试结果" :visible.sync="openTest" width="600px" append-to-body>
      <pre>{{ testResult }}</pre>
    </el-dialog>
  </div>
</template>
<script>
import { listGroovyApi, getGroovyApi, addGroovyApi, updateGroovyApi, delGroovyApi, testGroovyApi } from '@/api/api/groovy'
export default {
  name: 'ApiGroovy',
  data() {
    return {
      loading: false, showSearch: true, open: false, openTest: false, title: '', total: 0, dataList: [], testResult: '',
      queryParams: { pageNum: 1, pageSize: 10, apiPath: '' },
      form: {},
      rules: { apiPath: [{ required: true, message: '路径不能为空', trigger: 'blur' }] }
    }
  },
  created() { this.getList() },
  methods: {
    getList() { this.loading = true; listGroovyApi(this.queryParams).then(r => { this.dataList = r.rows||[]; this.total = r.total||0; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    handleAdd() { this.reset(); this.open = true; this.title = '新增Groovy API' },
    handleUpdate(row) { this.reset(); getGroovyApi(row.id).then(r => { this.form = r.data; this.open = true; this.title = '编辑Groovy API' }) },
    handleDelete(row) { this.$confirm('确认删除?').then(() => delGroovyApi(row.id).then(() => { this.getList(); this.$message.success('删除成功') })) },
    handleTest(row) { testGroovyApi(row.id).then(r => { this.testResult = JSON.stringify(r.data, null, 2); this.openTest = true }) },
    submitForm() { this.$refs.form.validate(v => { if(v) { (this.form.id?updateGroovyApi:addGroovyApi)(this.form).then(() => { this.$message.success('操作成功'); this.open = false; this.getList() }) } }) },
    cancel() { this.open = false; this.reset() },
    reset() { this.form = { apiPath: '', apiName: '', scriptType: 'groovy', scriptContent: '', status: '1' }; this.resetForm('form') }
  }
}
</script>