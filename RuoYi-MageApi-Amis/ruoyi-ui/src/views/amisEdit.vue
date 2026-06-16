<template>
  <div>
    <vue-amis-sdk 
      :key="dict.dictCode"
      id='editorName'
      theme='cxd'
      className='is-fixed'
      :preview='isPreview'
      :isMobile='isMobile'
      :value='schema'
      @change="change"
    />
   <div style="position: fixed;bottom: 0px;right: 5px;">
      <el-button type="primary" icon="el-icon-save" @click="save">保存</el-button>
    </div>
   <el-dialog :visible.sync="dialogVisible" width="80%">
    <el-table
      :data="menuList"
      row-key="menuId"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="menuName" label="菜单名称" :show-overflow-tooltip="true" width="160"></el-table-column>
      <el-table-column prop="orderNum" label="排序" width="60"></el-table-column>
      <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true"></el-table-column>
      <el-table-column prop="status" label="状态" width="80"></el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="mini"
            @click="handleEdit(scope.row)">选择</el-button>
        </template>
      </el-table-column>
    </el-table>
   </el-dialog> 
  </div>
</template>
  <script>
import { listMenu} from "@/api/system/menu";  
import request from '@/utils/request'
export default {
  name: "Edit",
  data() {
    return {
      dict:{},
      menuList: [],
      dialogVisible: true,
      isPreview: false,
      isMobile: false,
      schema: {},
    };
  },
  components: {
    VueAmisSdk: window.VueAmisSdk,
  },
  created() {
    listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, "menuId")
    });
  },
  methods: {
    change(e) {
      this.schema = e
    },
    handleEdit(row) {
      request({
        url: '/system/dict/data/page/' +  row.menuId,
        method: 'get'
      }).then(response => {
        const result = response.data;
        if(result){
          this.schema  = JSON.parse(result.remark)
          this.dict  = result
        }
        this.dialogVisible = false
      });
    },
    save(){
      this.dict.remark  = JSON.stringify(this.schema)
      request({
      url: '/system/dict/data',
      method: 'put',
      data: this.dict
      }).then(response => {
        this.$modal.msgSuccess("保存成功");
      });
    }
  },
};
</script>

<style>
</style>
