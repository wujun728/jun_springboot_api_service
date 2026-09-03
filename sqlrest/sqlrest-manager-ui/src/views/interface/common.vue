<template>
  <el-card>
    <el-row>
      <el-col :span="2">
        <el-button type="primary"
                   size="mini"
                   @click="handleShowTree">
          元数据查看
          <i class="el-icon-d-arrow-left"
             v-if="showTree"></i>
          <i class="el-icon-d-arrow-right"
             v-else></i>
        </el-button>
      </el-col>
      <el-col :span="22">
        <div style="float:right">
          <el-button type="primary"
                     size="mini"
                     @click="handleGoBack">
            <i class="el-icon-d-arrow-left">
              返回</i>
          </el-button>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter=2>
      <el-col :span="4"
              v-if="showTree">
        <div class="grid-content bg-purple">
          <el-select placeholder="请选择数据源"
                     v-model="createParam.dataSourceId"
                     @change="loadTreeData">
            <el-option v-for="(item,index) in connectionList"
                       :key="index"
                       :label="`[${item.id}]${item.name}`"
                       :value="item.id"></el-option>
          </el-select>
          <el-tree ref="tree"
                   empty-text="请选择数据源后查看"
                   style="min-height: 500px; max-height: 800px; overflow: auto;"
                   :indent=6
                   :data="treeData"
                   :props="props"
                   :load="loadNode"
                   :expand-on-click-node="true"
                   :highlight-current="true"
                   :render-content="renderContent"
                   lazy>
          </el-tree>
        </div>
      </el-col>
      <el-col :span='showTree?20:24'>
        <div class="grid-content bg-purple">
          <el-form size="mini"
                   :model="createParam"
                   :rules="rules"
                   label-position='left'
                   ref="form">
            <el-tabs type="border-card"
                     v-model="tabActiveName">
              <el-tab-pane label="基础配置>>"
                           name="basic">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="执行"
                                  label-width="65px">
                      <el-radio-group size="small"
                                      @change="agreeEngineChange"
                                      v-model="createParam.engine">
                        <el-radio-button label="SQL"
                                         :disabled="$route.query.id>0 && createParam.engine==='SCRIPT'">SQL语句</el-radio-button>
                        <el-radio-button label="SCRIPT"
                                         :disabled="$route.query.id>0 && createParam.engine==='SQL'">Groovy脚本</el-radio-button>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row v-if="createParam.engine==='SQL'">
                  <el-col :span="24">
                    <el-form-item label="语句"
                                  label-width="65px">
                      <el-tooltip placement="top">
                        <i class="el-icon-question"></i>
                        <div slot="content">
                          每个SQL窗口中至多写一条SQL语句，支持Mybatis的动态SQL语法
                        </div>
                      </el-tooltip>
                      <multi-sql-editer ref="sqlEditors"
                                        :tableHints="tableHints"
                                        :tabSqls="createParam.sqls"
                                        :canAddSql="!isOnlyShowDetail"></multi-sql-editer>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row v-if="createParam.engine==='SCRIPT'">
                  <el-col :span="24">
                    <el-form-item label="脚本"
                                  label-width="65px">
                      <el-tooltip placement="top">
                        <i class="el-icon-question"></i>
                        <div slot="content">
                          可以编写符合groovy语法格式的脚本内容
                        </div>
                      </el-tooltip>
                      <script-editer ref="scriptEditer"
                                     :content="createParam.script"></script-editer>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="3"
                          v-if="createParam.engine==='SQL'">
                    <el-button type="primary"
                               size="mini"
                               icon="el-icon-arrow-down"
                               v-if="!isOnlyShowDetail"
                               @click="handleParseInputParams">
                      入参解析
                    </el-button>
                  </el-col>
                  <el-col :span="3">
                    <el-button type="primary"
                               size="mini"
                               icon="el-icon-arrow-down"
                               v-if="!isOnlyShowDetail"
                               @click="handleAddInputParams">
                      添加入参
                    </el-button>
                  </el-col>
                  <el-col :span="3">
                    <el-button type="primary"
                               size="mini"
                               icon="el-icon-arrow-down"
                               v-if="!isOnlyShowDetail"
                               @click="handleAddPagableParams">
                      分页参数
                    </el-button>
                  </el-col>
                  <el-col :span="15">
                  </el-col>
                </el-row>
                <el-table :data="inputParams"
                          :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                          size="mini"
                          border>
                  <template slot="empty">
                    <span>请输入sql后点击"入参解析"按钮后解析出这里的入参</span>
                  </template>
                  <el-table-column label="参数名"
                                   min-width="25%">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.name"
                                type="string"
                                :disabled="isOnlyShowDetail"> </el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="参数类型"
                                   min-width="25%">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.type"
                                 :disabled="isOnlyShowDetail">
                        <el-option label='整型'
                                   value='LONG'></el-option>
                        <el-option label='浮点型'
                                   value='DOUBLE'></el-option>
                        <el-option label='字符串'
                                   value='STRING'></el-option>
                        <el-option label='日期'
                                   value='DATE'></el-option>
                        <el-option label='时间'
                                   value='TIME'></el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column label="为数组"
                                   min-width="25%">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.isArray"
                                 :disabled="isOnlyShowDetail">
                        <el-option label='是'
                                   :value=true></el-option>
                        <el-option label='否'
                                   :value=false></el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column label="必填"
                                   min-width="25%">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.required"
                                 :disabled="isOnlyShowDetail">
                        <el-option label='是'
                                   :value=true></el-option>
                        <el-option label='否'
                                   :value=false></el-option>
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column label="默认值"
                                   min-width="25%">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.defaultValue"
                                type="string"
                                :disabled="isOnlyShowDetail"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="描述"
                                   min-width="25%">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.remark"
                                type="string"
                                :disabled="isOnlyShowDetail"></el-input>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作"
                                   v-if="!isOnlyShowDetail"
                                   min-width="25%">
                    <template slot-scope="scope">
                      <el-button size="mini"
                                 type="danger"
                                 @click="deleteInputParamsItem(scope.$index)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
              <el-tab-pane label="接口配置>>"
                           name="detail">
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="路径"
                                  label-width="65px"
                                  style="width:80%"
                                  :required=true
                                  prop="path">
                      <el-input v-model="createParam.path"
                                :disabled="isOnlyShowDetail">
                        <template slot="prepend">{{gatewayApiPrefix}}</template>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="方法"
                                  label-width="65px"
                                  style="width:50%"
                                  :required=true
                                  prop="method">
                      <el-select v-model="createParam.method"
                                 :disabled="isOnlyShowDetail">
                        <el-option label="GET"
                                   value="GET"></el-option>
                        <el-option label="PUT"
                                   value="PUT"></el-option>
                        <el-option label="POST"
                                   value="POST"></el-option>
                        <el-option label="DELETE"
                                   value="DELETE"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="名称"
                                  label-width="65px"
                                  :required=true
                                  prop="name">
                      <el-input v-model="createParam.name"
                                auto-complete="off"
                                style="width:75%"
                                :disabled="isOnlyShowDetail"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="类型"
                                  style="width:80%"
                                  label-width="65px"
                                  :required=true
                                  prop="contentType">
                      <el-select v-model="createParam.contentType"
                                 :disabled="isOnlyShowDetail">
                        <el-option v-for="(item,index) in contentTypes"
                                   :key="index"
                                   :label="item"
                                   :value="item"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="模块"
                                  label-width="65px"
                                  :required=true
                                  style="width:80%"
                                  prop="module">
                      <el-select v-model="createParam.module"
                                 placeholder="请选择"
                                 :disabled="isOnlyShowDetail">
                        <el-option v-for="(item,index) in moduleList"
                                   :key="index"
                                   :label="`[${item.id}]${item.name}`"
                                   :value="item.id"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="授权"
                                  label-width="65px"
                                  style="width:80%"
                                  :required=true
                                  prop="group">
                      <el-select v-model="createParam.group"
                                 placeholder="请选择"
                                 :disabled="isOnlyShowDetail">
                        <el-option v-for="(item,index) in groupList"
                                   :key="index"
                                   :label="`[${item.id}]${item.name}`"
                                   :value="item.id"></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="描述"
                                  label-width="60px"
                                  prop="description"
                                  style="width:100%">
                      <el-input type="textarea"
                                v-model="createParam.description"
                                auto-complete="off"></el-input>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane label="出参格式>>"
                           name="outputParams">
                <el-row>
                  <el-col :span="12">
                    <div>
                      <el-form-item label="命名策略"
                                    label-width="80px"
                                    prop="description"
                                    style="width:100%">
                        <el-select v-model="createParam.namingStrategy"
                                   placeholder="请选择"
                                   :disabled="isOnlyShowDetail">
                          <el-option v-for="(item,index) in responseNamingStrategy"
                                     :key="index"
                                     :label="`[${item.key}]${item.value}`"
                                     :value="`${item.key}`"></el-option>
                        </el-select>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="数据格式"
                                  label-width="80px"
                                  prop="formatMap"
                                  style="width:100%">
                      <div v-for="item in createParam.formatMap"
                           :key="item.key"
                           v-bind="item">
                        {{item.remark}}:
                        <el-input type="text"
                                  :key="item.key"
                                  v-model="item.value"
                                  :value="item.value"> </el-input>
                      </div>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-tab-pane>
              <el-tab-pane label="流量控制"
                           name="flowControl">
                <el-row>
                  <el-col :span="24">
                    <el-form-item label="是否开启">
                      <el-switch v-model="createParam.flowStatus"
                                 active-color="#13ce66"
                                 :active-value="true"
                                 :inactive-value="false"
                                 active-text="开启"
                                 inactive-text="关闭"
                                 :disabled="isOnlyShowDetail">
                      </el-switch>
                    </el-form-item>
                    <div v-show="createParam.flowStatus">
                      <el-form-item label="阈值类型">
                        <el-radio-group size="small"
                                        v-model="createParam.flowGrade"
                                        :disabled="isOnlyShowDetail"
                                        border>
                          <el-radio :label="1">QPS</el-radio>
                          <el-radio :label="0">并发线程数</el-radio>
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item label="单机阈值">
                        <el-input-number v-model="createParam.flowCount"
                                         size="small"
                                         :step="1"
                                         :disabled="isOnlyShowDetail"
                                         step-strictly></el-input-number>
                      </el-form-item>
                    </div>
                  </el-col>
                </el-row>
              </el-tab-pane>
            </el-tabs>
          </el-form>
        </div>
      </el-col>
    </el-row>
    <el-row v-if="!isOnlyShowDetail">
      <el-col>
        <div style="float: right; padding: 5px">
          <el-button type="primary"
                     size="mini"
                     icon="el-icon-arrow-left"
                     @click="handleDebug">
            调试
          </el-button>
          <el-button type="primary"
                     size="mini"
                     icon="el-icon-arrow-left"
                     @click="handleSave">
            保存
          </el-button>
        </div>
      </el-col>
    </el-row>
    <el-drawer title="接口调试"
               :visible.sync="showDebugDrawer"
               direction="ltr"
               size="65%"
               :with-header="true">
      <el-card>
        <el-row>
          <el-col>
            <div style="float: right; padding: 25px">
              <el-button type="primary"
                         size="mini"
                         icon="el-icon-arrow-down"
                         v-if="!isOnlyShowDetail"
                         @click="handleAddDebugParams">
                添加入参
              </el-button>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-table :data="debugParams"
                      :header-cell-style="{background:'#eef1f6',color:'#606266'}"
                      size="mini"
                      border>
              <el-table-column label="参数名"
                               min-width="25%">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name"
                            type="string"> </el-input>
                </template>
              </el-table-column>
              <el-table-column label="参数类型"
                               min-width="25%">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type">
                    <el-option label='整型'
                               value='LONG'></el-option>
                    <el-option label='浮点型'
                               value='DOUBLE'></el-option>
                    <el-option label='字符串'
                               value='STRING'></el-option>
                    <el-option label='日期'
                               value='DATE'></el-option>
                    <el-option label='时间'
                               value='TIME'></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="为数组"
                               min-width="25%">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.isArray"
                             :disabled="true">
                    <el-option label='是'
                               :value=true></el-option>
                    <el-option label='否'
                               :value=false></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="必填"
                               min-width="25%">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.required"
                             :disabled="true">
                    <el-option label='是'
                               :value=true></el-option>
                    <el-option label='否'
                               :value=false></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="描述"
                               min-width="25%">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.remark"
                            :disabled="true"
                            type="string"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="值"
                               min-width="25%">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.value"
                            type="string"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="操作"
                               v-if="!isOnlyShowDetail"
                               min-width="25%">
                <template slot-scope="scope">
                  <el-button size="mini"
                             type="danger"
                             @click="deleteDebugParamsItem(scope.$index)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <div style="float: right; padding: 25px">
              <el-button type="primary"
                         size="mini"
                         icon="el-icon-arrow-left"
                         @click="handleExecuteDebug">
                执行
              </el-button>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <textarea v-model="debugResponse"
                      style="width:98%; height:400px"></textarea>
          </el-col>
        </el-row>
      </el-card>
    </el-drawer>
  </el-card>
</template>

<script>
import multiSqlEditer from '@/components/codeEditer/multiSqlEditer'
import scriptEditer from '@/components/codeEditer/scriptEditer'
import urlencode from "urlencode";
import qs from "qs";

export default {
  name: "common",
  data () {
    return {
      tabActiveName: 'basic',
      groupList: [],
      moduleList: [],
      connectionList: [],
      contentTypes: ['application/x-www-form-urlencoded'],
      showTree: true,
      createParam: {
        id: null,
        name: null,
        description: null,
        dataSourceId: null,
        group: null,
        module: null,
        method: null,
        path: null,
        contentType: 'application/x-www-form-urlencoded',
        engine: 'SQL',
        sqls: [],
        script: '',
        open: false,
        namingStrategy: 'CAMEL_CASE',
        formatMap: null,
        flowStatus: false,
        flowGrade: 1,
        flowCount: 5
      },
      showDebugDrawer: false,
      gatewayApiPrefix: 'http://127.0.0.1:8081/api/',
      treeData: [],
      props: {
        label: 'label',
        children: 'children',
        disabled: false,
        isLeaf: false
      },
      tableHints: {
        'mysql': ['user']
      },
      keywordHints: [],
      inputParams: [],
      debugParams: [],
      debugResponse: "",
      responseNamingStrategy: [],
      responseTypeFormat: [],
      rules: {
        name: [
          {
            required: true,
            message: "名称不能为空",
            trigger: "blur"
          }
        ],
        dataSourceId: [
          {
            required: true,
            message: "数据源必须选择",
            trigger: "change"
          }
        ],
        group: [
          {
            required: true,
            message: "分组必须选择",
            trigger: "change"
          }
        ],
        module: [
          {
            required: true,
            message: "模块必须选择",
            trigger: "change"
          }
        ],
        method: [
          {
            required: true,
            message: "方法必须选择",
            trigger: "change"
          }
        ],
        path: [
          {
            required: true,
            message: "路径必须提供",
            trigger: "blur"
          }
        ],
        contentType: [
          {
            required: true,
            message: "内容类型必须选择",
            trigger: "change"
          }
        ],
      },
    }
  },
  props: {
    isOnlyShowDetail: {
      type: Boolean,
      default: false
    }
  },
  components: { multiSqlEditer, scriptEditer },
  methods: {
    isUpdatePage: function () {
      if (this.$route.query.id) {
        return true;
      }
      return false;
    },
    loadAssignmentDetail: function () {
      if (!this.isUpdatePage()) {
        return;
      }
      this.$http.get(
        "/sqlrest/manager/api/v1/assignment/detail/" + this.$route.query.id
      ).then(res => {
        if (0 === res.data.code) {
          this.showTree = false;
          console.log("showTree:" + this.showTree)
          let detail = res.data.data;
          this.createParam = {
            id: detail.id,
            name: detail.name,
            description: detail.description,
            method: detail.method,
            path: detail.path,
            contentType: detail.contentType,
            open: detail.open,
            group: detail.groupId,
            module: detail.moduleId,
            dataSourceId: detail.datasourceId,
            engine: detail.engine,
            sqls: [],
            script: "",
            namingStrategy: detail.namingStrategy,
            formatMap: detail.formatMap,
            flowStatus: detail.flowStatus,
            flowGrade: detail.flowGrade,
            flowCount: detail.flowCount
          }
          this.inputParams = []
          if (detail.params) {
            this.inputParams = detail.params
          }
          if (detail.sqlList && detail.sqlList.length > 0) {
            if (this.createParam.engine === 'SQL') {
              this.createParam.sqls = detail.sqlList.map(obj => obj['sqlText'])
            } else {
              this.createParam.script = detail.sqlList[0].sqlText
            }
          }
        } else {
          if (res.data.message) {
            alert("查询失败," + res.data.message);
          }
        }
      });
    },
    loadConnections: function () {
      this.connectionList = [];
      this.$http({
        method: "GET",
        headers: {
          'Content-Type': 'application/json'
        },
        url: "/sqlrest/manager/api/v1/datasource/list/name",
      }).then(
        res => {
          if (0 === res.data.code) {
            this.connectionList = res.data.data;
          } else {
            if (res.data.message) {
              alert("加载数据失败:" + res.data.message);
              this.connectionList = [];
            }
          }
        }
      );
    },
    loadGroups: function () {
      this.groupList = [];
      this.$http({
        method: "POST",
        url: "/sqlrest/manager/api/v1/group/listAll"
      }).then(
        res => {
          if (0 === res.data.code) {
            this.groupList = res.data.data;
          } else {
            if (res.data.message) {
              alert("加载数据失败:" + res.data.message);
              this.groupList = [];
            }
          }
        }
      );
    },
    loadModules: function () {
      this.moduleList = [];
      this.$http({
        method: "POST",
        url: "/sqlrest/manager/api/v1/module/listAll"
      }).then(
        res => {
          if (0 === res.data.code) {
            this.moduleList = res.data.data;
          } else {
            if (res.data.message) {
              alert("加载数据失败:" + res.data.message);
              this.moduleList = [];
            }
          }
        }
      );
    },
    loadGateway: function () {
      this.$http({
        method: "GET",
        url: "/sqlrest/manager/api/v1/node/prefix"
      }).then(
        res => {
          if (0 === res.data.code) {
            if (res.data.data && typeof res.data.data === 'string') {
              this.gatewayApiPrefix = res.data.data;
            }
          } else {
            if (res.data.message) {
              alert("加载数据失败:" + res.data.message);
            }
          }
        }
      );
    },
    loadKeywordHints: function () {
      this.$http.get(
        "/sqlrest/manager/api/v1/assignment/completions"
      ).then(res => {
        if (0 === res.data.code) {
          this.keywordHints = res.data.data;
        }
      });
    },
    loadResponseNamingStrategy: function () {
      this.$http.get(
        "/sqlrest/manager/api/v1/assignment/response-naming-strategy"
      ).then(res => {
        if (0 === res.data.code) {
          this.responseNamingStrategy = res.data.data;
          console.log(this.responseNamingStrategy)
        }
      });
    },
    loadResponseTypeFormat: function () {
      this.$http.get(
        "/sqlrest/manager/api/v1/assignment/response-type-format"
      ).then(res => {
        if (0 === res.data.code) {
          this.responseTypeFormat = res.data.data;
          if (!this.createParam.formatMap) {
            this.createParam.formatMap = res.data.data;
          }
        }
      });
    },
    loadTreeData: function () {
      if (this.createParam.dataSourceId && this.createParam.dataSourceId > 0 && this.showTree) {
        this.treeData = []
        setTimeout(() => {
          this.$http({
            method: "GET",
            url: "/sqlrest/manager/api/v1/datasource/schemas/get/" + this.createParam.dataSourceId
          }).then(
            res => {
              if (0 === res.data.code) {
                for (let element of res.data.data) {
                  let obj = new Object();
                  obj['label'] = element;
                  obj['parent'] = null;
                  obj['value'] = element;
                  obj['hasChild'] = true;
                  obj['type'] = 'DATABASE';
                  this.treeData.push(obj);
                }
              } else {
                this.$alert("加载失败，原因：" + res.data.message, '数据加载失败');
              }
            }
          );
        }, 500);
      }
    },
    loadNode: function (node, resolve) {
      setTimeout(() => {
        if (node.level === 1) {
          let tableView = [
            {
              'label': '表',
              'parent': this.createParam.dataSourceId,
              'value': node.label,
              'hasChild': true,
              'type': 'TABLE',
            },
            {
              'label': '视图',
              'parent': this.createParam.dataSourceId,
              'value': node.label,
              'hasChild': true,
              'type': 'VIEW',
            }
          ]
          resolve(tableView);
        } else if (node.level === 2) {
          this.loadTablesList(resolve, this.createParam.dataSourceId, node.data.value, node.data.type)
        } else if (node.level === 3) {
          this.loadColumnList(resolve, this.createParam.dataSourceId, node.data.value, node.data.label)
        } else {
          resolve([]);
        }
      }, 500);
    },
    loadTablesList: function (resolve, id, schema, type) {
      var tableType = 'VIEW' === type ? 'views' : 'tables'
      this.$http({
        method: "GET",
        url: "/sqlrest/manager/api/v1/datasource/" + tableType + "/get/" + id + "?schema=" + urlencode(schema)
      }).then(
        res => {
          if (0 === res.data.code) {
            let tableList = []
            let nameList = []
            for (let element of res.data.data) {
              let obj = new Object();
              obj['label'] = element;
              obj['parent'] = id;
              obj['value'] = schema;
              obj['hasChild'] = true;
              obj['type'] = type;
              tableList.push(obj);
              nameList.push(element)
            }

            // SQL关键词提示
            if (this.tableHints[schema] && Array.isArray(this.tableHints[schema])) {
              this.tableHints[schema].push(nameList)
            } else {
              this.tableHints[schema] = nameList;
            }
            if (this.$refs.sqlEditors) {
              this.$refs.sqlEditors.setTableHints(this.tableHints)
            }
            // if (this.$refs.scriptEditer) {
            //   let keywords = []
            //   for (let key in this.tableHints) {
            //     let value = this.tableHints[key]
            //     for (let item of value) {
            //       keywords.push({ meta: "数据表", caption: key + "." + item, value: key + "." + item, score: 1 });
            //     }
            //   }
            //   this.$refs.scriptEditer.setTableHints(keywords)
            // }

            return resolve(tableList);
          } else {
            this.$alert("加载失败，原因：" + res.data.message, '数据加载失败');
          }
        }
      );
    },
    loadColumnList: function (resolve, id, schema, table) {
      this.$http({
        method: "GET",
        url: "/sqlrest/manager/api/v1/datasource/columns/get/" + id + "?schema=" + urlencode(schema) + "&table=" + urlencode(table)
      }).then(
        res => {
          if (0 === res.data.code) {
            let columnList = []
            for (let element of res.data.data) {
              let obj = new Object();
              obj['label'] = element.name;
              obj['parent'] = table;
              obj['value'] = element.name;
              obj['hasChild'] = false;
              obj['type'] = element.type;
              columnList.push(obj);
            }
            return resolve(columnList);
          } else {
            this.$alert("加载失败，原因：" + res.data.message, '数据加载失败');
          }
        }
      );
    },
    renderContent (h, { node, data, store }) {
      // https://www.cnblogs.com/zhoushuang0426/p/11059989.html
      if (node.level === 1) {
        return (
          <div class="custom-tree-node">
            <i class="iconfont icon-shujuku1"></i>
            <el-tooltip class="item" effect="light" placement="left">
              <div slot="content">{node.label}</div>
              <span>{data.label}</span>
            </el-tooltip>
          </div>
        );
      } else if (node.level === 2) {
        var icon_pic = "iconfont icon-shitu_biaoge";
        if (data.type === 'VIEW') {
          icon_pic = "iconfont icon-viewList"
        }
        return (
          <div class="custom-tree-node">
            <i class={icon_pic}></i>
            <span>{data.label}</span>
          </div>
        );
      } else if (node.level === 3) {
        var icon_pic = "iconfont icon-shitu_biaoge";
        if (data.type === 'VIEW') {
          icon_pic = "iconfont icon-viewList"
        }
        return (
          <div class="custom-tree-node">
            <i class={icon_pic}></i>
            <el-tooltip class="item" effect="light" placement="left">
              <div slot="content">{node.label}</div>
              <span>{data.label}</span>
            </el-tooltip>
          </div>
        );
      } else {
        return (
          <div class="custom-tree-node">
            <i class="el-icon-set-up"></i>
            <el-tooltip class="item" effect="light" placement="left">
              <div slot="content">{data.type}</div>
              <span>{data.label}({data.type})</span>
            </el-tooltip>
          </div>
        );
      }

    },
    handleNodeClick: function () {

    },
    handleGoBack: function () {
      this.$router.go(-1);
    },
    handleShowTree: function () {
      let status = !this.showTree;
      this.showTree = status;
      if (this.showTree) {
        this.loadTreeData();
      }
    },
    handleParseInputParams: function () {
      var currTabSql = this.$refs.sqlEditors.queryCurrentTabSql()
      if (/^\s*$/.test(currTabSql)) {
        alert("SQL内容不能为空")
        return
      }
      this.$http({
        method: "POST",
        url: "/sqlrest/manager/api/v1/assignment/parse",
        data: qs.stringify({
          sql: currTabSql
        }),
      }).then(
        res => {
          if (0 === res.data.code) {
            if (res.data.data && res.data.data.length === 0) {
              alert("解析的入参为空")
              return
            }
            for (let item of res.data.data) {
              this.inputParams.push(
                {
                  name: item.name,
                  location: 'FORM_DATA',
                  type: "STRING",
                  isArray: item.isArray,
                  required: true,
                  defaultValue: "",
                  remark: ""
                }
              )
            };
          } else {
            if (res.data.message) {
              alert("操作失败失败:" + res.data.message);
            }
          }
        }
      );
    },
    agreeEngineChange: function () {
      if (this.createParam.engine === 'SCRIPT') {
        this.$refs.scriptEditer.setTableHints(this.keywordHints);
      }
    },
    handleAddInputParams: function () {
      this.inputParams.push(
        {
          name: "",
          location: 'FORM_DATA',
          type: "STRING",
          isArray: false,
          required: true,
          defaultValue: "",
          remark: ""
        }
      )
    },
    handleAddPagableParams: function () {
      var add = false
      if (!this.inputParams.find(item => item.name === 'apiPageNum')) {
        add = true
        this.inputParams.push(
          {
            name: "apiPageNum",
            type: "LONG",
            location: 'FORM_DATA',
            isArray: false,
            required: true,
            defaultValue: "1",
            remark: "页号"
          }
        );
      }
      if (!this.inputParams.find(item => item.name === 'apiPageSize')) {
        add = true
        this.inputParams.push(
          {
            name: "apiPageSize",
            type: "LONG",
            location: 'FORM_DATA',
            isArray: false,
            required: true,
            defaultValue: "10",
            remark: "页大小"
          },
        )
      }
      if (!add) {
        alert("已经存在分页参数了！")
      }
    },
    deleteInputParamsItem: function (index) {
      this.inputParams.splice(index, 1);
    },
    checkSqlsOrScriptEmpty: function (sqls) {
      if (sqls === null || sqls === undefined || !Array.isArray(sqls) || sqls.length === 0 || sqls.includes('')) {
        return true
      }
      for (let str in sqls) {
        if (str === null || str === undefined || str.trim() === '' || str.trim().length === 0) {
          return true
        }
      }
      return false
    },
    handleSave: function () {
      this.$refs.form.validate(valid => {
        if (valid) {
          var sqls = []
          var isSql = true;
          if (this.createParam.engine === 'SQL') {
            isSql = true
            sqls = this.$refs.sqlEditors.queryContent()
          } else {
            isSql = false
            sqls = this.$refs.scriptEditer.queryContent()
          }

          if (this.checkSqlsOrScriptEmpty(sqls)) {
            alert(isSql ? '请检查SQL窗口内容' : '请检查脚本内容')
          } else {
            if (this.isUpdatePage()) {
              this.handleUpdateSave(sqls);
            } else {
              this.handleCreateSave(sqls);
            }
          }
        } else {
          alert("请检查输入");
        }
      });
    },
    handleCreateSave: function (sqls) {
      this.$http({
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        url: "/sqlrest/manager/api/v1/assignment/create",
        data: JSON.stringify({
          groupId: this.createParam.group,
          moduleId: this.createParam.module,
          datasourceId: this.createParam.dataSourceId,
          name: this.createParam.name,
          description: this.createParam.description,
          method: this.createParam.method,
          contentType: this.createParam.contentType,
          path: this.createParam.path,
          open: this.createParam.open,
          namingStrategy: this.createParam.namingStrategy,
          formatMap: this.createParam.formatMap,
          flowStatus: this.createParam.flowStatus,
          flowGrade: this.createParam.flowGrade,
          flowCount: this.createParam.flowCount,
          engine: this.createParam.engine,
          contextList: sqls,
          params: this.inputParams
        })
      }).then(
        res => {
          if (0 === res.data.code) {
            this.$router.push({ path: '/interface/list' });
            this.$message("添加信息成功");
          } else {
            if (res.data.message) {
              alert("操作失败失败:" + res.data.message);
            }
          }
        }
      );
    },
    handleUpdateSave: function (sqls) {
      this.$http({
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        url: "/sqlrest/manager/api/v1/assignment/update",
        data: JSON.stringify({
          id: this.createParam.id,
          groupId: this.createParam.group,
          moduleId: this.createParam.module,
          datasourceId: this.createParam.dataSourceId,
          name: this.createParam.name,
          description: this.createParam.description,
          method: this.createParam.method,
          contentType: this.createParam.contentType,
          path: this.createParam.path,
          open: this.createParam.open,
          namingStrategy: this.createParam.namingStrategy,
          formatMap: this.createParam.formatMap,
          flowStatus: this.createParam.flowStatus,
          flowGrade: this.createParam.flowGrade,
          flowCount: this.createParam.flowCount,
          engine: this.createParam.engine,
          contextList: sqls,
          params: this.inputParams
        })
      }).then(
        res => {
          if (0 === res.data.code) {
            this.$router.push({ path: '/interface/list' });
            this.$message("更新信息成功");
          } else {
            if (res.data.message) {
              alert("操作失败失败:" + res.data.message);
            }
          }
        }
      );
    },
    handleDebug: function () {
      this.debugResponse = ""
      var sqls = []
      var isSql = true;
      if (this.createParam.engine === 'SQL') {
        isSql = true
        sqls = this.$refs.sqlEditors.queryContent()
      } else {
        isSql = false
        sqls = this.$refs.scriptEditer.queryContent()
      }

      if (this.checkSqlsOrScriptEmpty(sqls)) {
        alert(isSql ? '请检查SQL窗口内容' : '请检查脚本内容')
      } else {
        this.debugParams = []
        this.inputParams.forEach(item => {
          this.debugParams.push(
            {
              name: item.name,
              type: item.type,
              isArray: item.isArray,
              required: item.required,
              defaultValue: item.defaultValue,
              remark: item.remark,
              value: null,
            },
          )
        })
        this.showDebugDrawer = true
      }
    },
    handleAddDebugParams: function () {
      this.debugParams.push(
        {
          name: '',
          type: 'LONG',
          isArray: true,
          required: true,
          defaultValue: '',
          remark: null,
          value: null,
        },
      )
    },
    deleteDebugParamsItem: function (index) {
      this.debugParams.splice(index, 1);
    },
    handleExecuteDebug: function () {
      var sqls = []
      if (this.createParam.engine === 'SQL') {
        sqls = this.$refs.sqlEditors.queryContent()
      } else {
        sqls = this.$refs.scriptEditer.queryContent()
      }

      this.$http({
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        url: "/sqlrest/manager/api/v1/assignment/debug",
        data: JSON.stringify({
          dataSourceId: this.createParam.dataSourceId,
          engine: this.createParam.engine,
          namingStrategy: this.createParam.namingStrategy,
          formatMap: this.createParam.formatMap,
          contextList: sqls,
          paramValues: this.debugParams
        })
      }).then(
        res => {
          if (0 === res.data.code) {
            this.debugResponse = JSON.stringify(res.data.data, null, 2);
          } else {
            if (res.data.message) {
              alert("调试操作失败:" + res.data.message);
            }
          }
        }
      );
    }
  },
  created () {
    this.loadAssignmentDetail();
    this.loadConnections();
    this.loadGroups();
    this.loadModules();
    this.loadGateway();
    this.loadKeywordHints();
    this.loadTreeData();
    this.loadResponseNamingStrategy();
    this.loadResponseTypeFormat();
  },
}
</script>

<style scoped>
.el-card {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.tip-content {
  font-size: 12px;
}

.name-mapper-table,
.name-mapper-table table tr th,
.name-mapper-table table tr td {
  border-collapse: collapse;
  border: 1px solid #e0dddd;
  width: 100%;
}

.el-descriptions__body
  .el-descriptions__table
  .el-descriptions-row
  .el-descriptions-item__label {
  min-width: 20px;
  max-width: 60px;
}

.custom-tree-node span {
  font-size: 12px;
}

.el-col .el-select {
  width: 98%;
}
.el-tree {
  overflow: auto;
}
.el-tree-node__content {
  height: 20px;
}
.el-drawer__wrapper {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  margin: 0;
}
</style>