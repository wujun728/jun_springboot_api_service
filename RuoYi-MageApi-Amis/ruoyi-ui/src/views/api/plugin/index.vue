<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card><div slot="header"><span>缓存插件</span></div>
          <el-table :data="cachePlugins" border style="width:100%">
            <el-table-column prop="name" label="名称" /><el-table-column prop="className" label="类名" min-width="200" />
            <el-table-column label="状态" width="80"><template slot-scope="s"><el-tag type="success">就绪</el-tag></template></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card><div slot="header"><span>转换插件</span></div>
          <el-table :data="transformPlugins" border style="width:100%">
            <el-table-column prop="name" label="名称" /><el-table-column prop="className" label="类名" min-width="200" />
            <el-table-column label="状态" width="80"><template slot-scope="s"><el-tag type="success">就绪</el-tag></template></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10" style="margin-top:15px">
      <el-col :span="12">
        <el-card><div slot="header"><span>告警插件</span></div>
          <el-table :data="alarmPlugins" border style="width:100%">
            <el-table-column prop="name" label="名称" /><el-table-column prop="className" label="类名" min-width="200" />
            <el-table-column label="状态" width="80"><template slot-scope="s"><el-tag type="success">就绪</el-tag></template></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { listPlugins } from '@/api/api/plugin'
export default {
  name: 'ApiPlugin',
  data() { return { cachePlugins: [{name:'内存缓存',className:'com.ruoyi.api.plugin.impl.MemoryCachePlugin'}], transformPlugins: [{name:'简单加密',className:'com.ruoyi.api.plugin.impl.EncryptPlugin'}], alarmPlugins: [{name:'钉钉告警',className:'com.ruoyi.api.plugin.impl.DingTalkAlarmPlugin'}] } },
  created() {
    listPlugins('cache').then(r => { if(r.data) this.cachePlugins = r.data }).catch(() => {})
    listPlugins('transform').then(r => { if(r.data) this.transformPlugins = r.data }).catch(() => {})
    listPlugins('alarm').then(r => { if(r.data) this.alarmPlugins = r.data }).catch(() => {})
  }
}
</script>