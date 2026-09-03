<template>
  <div>
    <el-button type="primary"
               size='mini'
               v-if="canAddSql"
               @click="addTab()"
               style='left:10px;top:5px;'>添加SQL窗口</el-button>
    <div class="quick-script-tag">
      <div class="mybatis-named-tag"
           @click="addSqlTag('foreach')">foreach</div>
      <div class="mybatis-named-tag"
           @click="addSqlTag('if')">if</div>
      <div class="mybatis-named-tag"
           @click="addSqlTag('where')">where</div>
      <div class="mybatis-named-tag"
           @click="addSqlTag('trim')">trim</div>
    </div>
    <el-tabs v-model="currentTabName"
             tab-position="top"
             type="border-card"
             closable
             @tab-remove="removeTab"
             @tab-click="handleClickTab()">
      <el-tab-pane v-for="(item,index) in editableTabs"
                   :key="item.name"
                   :label="item.title"
                   :name="item.name">
        <codemirror class="custom-code-mirror"
                    :ref="editableTabs[index].name"
                    :value="editableTabs[index].content"
                    :options="cmOptions"
                    @ready="onCmReady"
                    @focus="onCmFocus"
                    @inputRead="onCmCodeChange">
        </codemirror>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { codemirror } from 'vue-codemirror'

import 'codemirror/theme/solarized.css'
import 'codemirror/theme/idea.css'
import 'codemirror/theme/darcula.css'
import 'codemirror/theme/base16-light.css'
import "codemirror/addon/hint/show-hint.css";
import 'codemirror/mode/sql/sql.js'

require("codemirror/lib/codemirror");
require("codemirror/mode/sql/sql");
require("codemirror/addon/hint/show-hint");
require("codemirror/addon/hint/sql-hint");

export default {
  name: "multiSqlEditer",
  data () {
    return {
      currentTabName: '1',
      editableTabs: [],
      tabIndex: 0,
      cmMapper: new Map(),
      cmOptions: {
        styleActiveLine: true,
        lineNumbers: true,
        mode: 'text/x-mysql',
        theme: 'darcula',
        lint: true,                     // 代码出错提醒
        lineWrapping: true, // 是否应滚动或换行以显示长行
        fontSize: 10,
        autofocus: true,
        matchBrackets: true,
        extraKeys: { "Tab": "autocomplete" },  //Tab可以弹出选择项
        hintOptions: { // 自定义提示选项
          completeSingle: false, // 当匹配只有一项的时候是否自动补全
          tables: {}
        }
      },
    }
  },
  props: {
    tabSqls: {
      type: Array,
      default: () => []
    },
    tableHints: {
      type: Object,
      default: () => { }
    },
    canAddSql: {
      type: Boolean,
      default: true
    }
  },
  components: {
    codemirror
  },
  watch: {
    tabSqls (newVal, OldVal) {
      this.loadTabSqls()
    },
    tableHints (newVal, OldVal) {
      this.cmOptions.hintOptions.tables = newVal
    }
  },
  methods: {
    onCmReady (cm) {
      this.cmMapper.set(this.currentTabName, cm)
      cm.setSize('100%', '300px')
    },
    onCmFocus (cm) {
    },
    onCmCodeChange (cm, changeObj) {
      if (/^[a-zA-Z.]/.test(changeObj.text[0])) {
        //如果输入的是字母才提示，空格不提示
        cm.showHint()
      }
    },
    addTab: function (sqlContent) {
      if (this.editableTabs.length > 6) {
        alert("开启的SQL窗口个数已达最大个数")
        return
      }
      let newTabName = ++this.tabIndex + '';
      this.editableTabs.push({
        title: 'SQL窗口(' + newTabName + ')',
        name: newTabName,
        content: sqlContent ? sqlContent : ""
      });
      this.currentTabName = newTabName;
    },
    removeTab: function (targetName) {
      let tabs = this.editableTabs;
      let activeName = this.currentTabName;
      if (activeName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              activeName = nextTab.name;
            }
          }
        });
      }
      this.cmMapper.delete(targetName)
      this.editableTabs = tabs.filter(tab => tab.name !== targetName);
      this.currentTabName = activeName;
    },
    addSqlTag: function (tag) {
      let val = ''
      if (tag == 'foreach') {
        val = "\n<foreach open=\"(\" close=\")\" collection=\"\" separator=\",\" item=\"item\" index=\"index\">#{item}</foreach>"
      } else if (tag == 'if') {
        val = "\n<if test=\"\" ></if>"
      } else if (tag == 'choose') {
        val = "\n<choose><when test=\"\"></when></choose>"
      } else if (tag == 'where') {
        val = "\n<where></where>"
      } else if (tag == 'trim') {
        val = "\n<trim prefix=\"\" suffix=\"\" suffixesToOverride=\"\" prefixesToOverride=\"\"></trim>"
      }

      if(this.cmMapper.size===0){
        alert("请先点击‘添加SQL窗口’来添加一个SQL窗口！")
        return
      }

      var editerInstance = this.cmMapper.get(this.currentTabName);
      if (!editerInstance) {
        alert("error happen!");
        return;
      }
      editerInstance.replaceSelection(val)
    },
    loadTabSqls: function () {
      for (let sql of this.tabSqls) {
        this.addTab(sql)
      }
    },
    setTableHints: function (newVal) {
      this.cmOptions.hintOptions.tables = newVal
    },
    queryCurrentTabSql: function () {
      var cm = this.cmMapper.get(this.currentTabName);
      if (cm) {
        return cm.getValue()
      }
      return ""
    },
    queryContent: function () {
      var sqls = []
      this.cmMapper.forEach((cm, key) => {
        sqls.push(cm.getValue())
      })
      return sqls
    },
    handleClickTab (tab, event) {
    }
  },
  mounted () {
    this.loadTabSqls()
    this.currentTabName = this.editableTabs.length + ""
  },
  created () {
  },
}
</script>

<style scoped>
.quick-script-tag {
  display: flex;
  float: right;
}

.quick-script-tag .mybatis-named-tag {
  background-color: #170f7cb9;
  color: #fff;
  border-radius: 3px;
  margin: 2px;
  line-height: 22px;
  padding: 0 5px;
  cursor: pointer;
}

.custom-code-mirror {
  font-size: 13px;
  line-height: 150%;
}
</style>
