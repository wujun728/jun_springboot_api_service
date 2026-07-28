<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="API标识" prop="apiId"><el-input v-model="queryParams.apiId" placeholder="请输入API标识" clearable @keyup.enter.native="handleQuery" /></el-form-item>
      <el-form-item><el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button></el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="版本号" align="center" prop="versionNo" width="80" />
      <el-table-column label="API路径" align="center" prop="apiPath" min-width="200" />
      <el-table-column label="API名称" align="center" prop="apiName" />
      <el-table-column label="操作类型" align="center" prop="operateType" width="100">
        <template slot-scope="s"><el-tag :type="s.row.operateType==='rollback'?'warning':s.row.operateType==='create'?'success':'primary'">{{ s.row.operateType }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作人" align="center" prop="operateBy" width="120" />
      <el-table-column label="操作时间" align="center" prop="operateTime" width="180" />
      <el-table-column label="操作" align="center" width="150">
        <template slot-scope="s">
          <el-button size="mini" type="text" icon="el-icon-view" @click="viewContent(s.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-refresh" @click="handleRollback(s.row)">回滚</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    <el-dialog title="版本内容" :visible.sync="openView" width="700px" append-to-body>
      <pre>{{ viewContent }}</pre>
    </el-dialog>
  </div>
</template>
<script>
import { listVersion, getVersion, rollbackVersion } from '@/api/api/version'
export default {
  name: 'ApiVersion',
  data() {
    return {
      loading: false, showSearch: true, openView: false, viewContent: '', total: 0, dataList: [],
      queryParams: { pageNum: 1, pageSize: 10, apiId: '' }
    }
  },
  created() { this.getList() },
  methods: {
    getList() { this.loading = true; listVersion(this.queryParams).then(r => { this.dataList = r.rows||[]; this.total = r.total||0; this.loading = false }) },
    handleQuery() { this.queryParams.pageNum = 1; this.getList() },
    resetQuery() { this.resetForm('queryForm'); this.handleQuery() },
    viewContent(row) { getVersion(row.id).then(r => { this.viewContent = r.data ? r.data.content : '无'; this.openView = true }) },
    handleRollback(row) { this.$confirm(`确认回滚到版本 ${row.versionNo}?`).then(() => rollbackVersion(row.apiId, row.versionNo).then(() => { this.$message.success('回滚成功'); this.getList() })) }
  }
}
</script>