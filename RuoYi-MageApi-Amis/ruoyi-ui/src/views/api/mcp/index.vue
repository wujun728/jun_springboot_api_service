<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="8">
        <el-card><div slot="header"><span>MCP Server 状态</span></div>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="服务状态"><el-tag type="success">运行中</el-tag></el-descriptions-item>
            <el-descriptions-item label="端点"><code>/mcp/sse</code></el-descriptions-item>
            <el-descriptions-item label="消息端点"><code>/mcp/message</code></el-descriptions-item>
            <el-descriptions-item label="协议">MCP 2024-11-05</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card><div slot="header"><span>已注册 Tools</span></div>
          <el-table :data="tools" border style="width:100%">
            <el-table-column prop="name" label="名称" width="200" />
            <el-table-column prop="description" label="描述" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  name: 'ApiMcp',
  data() {
    return {
      tools: [
        { name: 'listApis', description: '列出所有可用API' },
        { name: 'executeApi', description: '执行指定API' },
        { name: 'queryDatabase', description: '查询数据库' }
      ]
    }
  }
}
</script>