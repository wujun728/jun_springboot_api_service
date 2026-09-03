<template>
    <div class="sql-editor-wrapper">
        <div class="sql-editor-top">
            <!-- 左侧面板：数据源和表 -->
            <div class="sql-editor-left">
                <div style="padding: 5px;">
                    <el-select v-model="selectedDatasource" placeholder="选择数据源" size="small" style="width: 100%;" @change="onDatasourceChange">
                        <el-option v-for="item in datasourceList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="table-list" v-if="tableList.length > 0">
                    <div class="table-item" v-for="table in tableList" :key="table.label">
                        <div class="table-name" @click="toggleTable(table)">
                            <i :class="table.expanded ? 'el-icon-arrow-down' : 'el-icon-arrow-right'"></i>
                            <i class="el-icon-s-grid" style="color: #409EFF;"></i>
                            {{ table.label }}
                        </div>
                        <div class="column-list" v-show="table.expanded">
                            <div class="column-item" v-for="col in table.columns" :key="col.label">
                                <span class="col-name">{{ col.label }}</span>
                                <span class="col-type">{{ col.TypeName }}</span>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-else style="padding: 10px; color: #999; font-size: 12px;">请选择数据源</div>
            </div>
            <!-- 右侧面板：SQL 编辑器 -->
            <div class="sql-editor-right">
                <div class="sql-toolbar">
                    <el-button size="mini" type="success" icon="el-icon-video-play" @click="runSql">执行 SQL</el-button>
                    <el-button size="mini" icon="el-icon-document" @click="parseDynamicSql">解析动态SQL</el-button>
                    <span style="margin-left: 10px; font-size: 12px; color: #999;">快捷标签：</span>
                    <el-button size="mini" @click="insertTag('foreach')">foreach</el-button>
                    <el-button size="mini" @click="insertTag('if')">if</el-button>
                    <el-button size="mini" @click="insertTag('where')">where</el-button>
                    <el-button size="mini" @click="insertTag('trim')">trim</el-button>
                </div>
                <div ref="codemirrorContainer" class="codemirror-container"></div>
            </div>
        </div>
        <!-- 底部面板：参数和结果 -->
        <div class="sql-editor-bottom">
            <el-tabs v-model="activeTab" type="border-card" style="height: 100%;">
                <el-tab-pane label="参数" name="params">
                    <el-input type="textarea" v-model="paramsJson" :rows="5" placeholder='JSON 格式参数，例如：{"name":"test"}'></el-input>
                </el-tab-pane>
                <el-tab-pane label="执行结果" name="result">
                    <div v-if="resultType === 'table' && resultData.length > 0">
                        <el-table :data="resultData" border stripe size="mini" max-height="250">
                            <el-table-column v-for="col in resultColumns" :key="col" :prop="col" :label="col" min-width="120" show-overflow-tooltip></el-table-column>
                        </el-table>
                    </div>
                    <div v-else-if="resultType === 'text'">
                        <pre style="margin: 0; white-space: pre-wrap; font-size: 13px;">{{ resultText }}</pre>
                    </div>
                    <div v-else style="color: #999; padding: 10px;">暂无结果</div>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
module.exports = {
    props: {
        value: { type: String, default: '' },
        datasourceId: { type: String, default: '' }
    },
    data: function() {
        return {
            editor: null,
            datasourceList: [],
            selectedDatasource: this.datasourceId || '',
            tableList: [],
            paramsJson: '{}',
            activeTab: 'params',
            resultType: '',
            resultData: [],
            resultColumns: [],
            resultText: ''
        };
    },
    mounted: function() {
        this.loadDatasources();
        this.initEditor();
    },
    watch: {
        datasourceId: function(val) {
            if (val && val !== this.selectedDatasource) {
                this.selectedDatasource = val;
                this.loadTables(val);
            }
        },
        value: function(val) {
            if (this.editor && val !== this.editor.getValue()) {
                this.editor.setValue(val || '');
            }
        }
    },
    methods: {
        initEditor: function() {
            var me = this;
            var container = this.$refs.codemirrorContainer;
            if (window.CodeMirror) {
                this.editor = CodeMirror(container, {
                    value: this.value || '',
                    mode: 'text/x-sql',
                    theme: 'idea',
                    lineNumbers: true,
                    lineWrapping: true,
                    indentWithTabs: true,
                    smartIndent: true,
                    matchBrackets: true,
                    autofocus: true,
                    extraKeys: { 'Ctrl-Space': 'autocomplete' },
                    hintOptions: { completeSingle: false }
                });
                this.editor.on('change', function() {
                    me.$emit('input', me.editor.getValue());
                });
                this.editor.setSize('100%', '100%');
            } else {
                // Fallback: textarea
                var textarea = document.createElement('textarea');
                textarea.style.cssText = 'width:100%;height:100%;font-family:monospace;font-size:13px;padding:8px;border:none;resize:none;';
                textarea.value = this.value || '';
                textarea.addEventListener('input', function() {
                    me.$emit('input', textarea.value);
                });
                container.appendChild(textarea);
                this.editor = {
                    getValue: function() { return textarea.value; },
                    setValue: function(v) { textarea.value = v; },
                    replaceSelection: function(text) {
                        var start = textarea.selectionStart;
                        var end = textarea.selectionEnd;
                        textarea.value = textarea.value.substring(0, start) + text + textarea.value.substring(end);
                        textarea.selectionStart = textarea.selectionEnd = start + text.length;
                    }
                };
            }
        },
        loadDatasources: function() {
            var me = this;
            dbapi.ajax('/datasource/getAll', function(res) {
                me.datasourceList = res.data || res || [];
                if (me.selectedDatasource) {
                    me.loadTables(me.selectedDatasource);
                }
            });
        },
        onDatasourceChange: function(val) {
            this.$emit('update:datasourceId', val);
            this.loadTables(val);
        },
        loadTables: function(sourceId) {
            var me = this;
            me.tableList = [];
            dbapi.ajax('/table/getAllTables', { sourceId: sourceId }, function(res) {
                var list = res.data || res || [];
                me.tableList = list.map(function(item) {
                    return { label: item.label || item, columns: item.columns || [], expanded: false };
                });
            });
        },
        toggleTable: function(table) {
            if (table.expanded) {
                table.expanded = false;
                this.$forceUpdate();
                return;
            }
            if (table.columns && table.columns.length > 0) {
                table.expanded = true;
                this.$forceUpdate();
                return;
            }
            var me = this;
            dbapi.ajax('/table/getAllColumns', { sourceId: this.selectedDatasource, table: table.label }, function(res) {
                table.columns = res.data || res || [];
                table.expanded = true;
                me.$forceUpdate();
            });
        },
        getSql: function() {
            return this.editor ? this.editor.getValue() : '';
        },
        setSql: function(sql) {
            if (this.editor) this.editor.setValue(sql || '');
        },
        insertTag: function(type) {
            var templates = {
                'foreach': '<foreach open="(" close=")" collection="" separator="," item="item">#{item}</foreach>',
                'if': '<if test="">\n\n</if>',
                'where': '<where>\n\n</where>',
                'trim': '<trim prefix="" suffix="" suffixesToOverride="" prefixesToOverride="">\n\n</trim>'
            };
            if (this.editor) {
                this.editor.replaceSelection(templates[type] || '');
            }
        },
        runSql: function() {
            var me = this;
            var sql = this.getSql();
            if (!sql.trim()) return this.$message.warning('请输入 SQL');
            if (!this.selectedDatasource) return this.$message.warning('请选择数据源');

            dbapi.ajax('/apiConfig/sql/execute', {
                sql: sql,
                datasourceId: this.selectedDatasource,
                params: this.paramsJson
            }, function(res) {
                me.activeTab = 'result';
                var data = res.data || res;
                if (Array.isArray(data) && data.length > 0 && typeof data[0] === 'object') {
                    me.resultType = 'table';
                    me.resultData = data;
                    me.resultColumns = Object.keys(data[0]);
                } else {
                    me.resultType = 'text';
                    me.resultText = typeof data === 'string' ? data : JSON.stringify(data, null, 2);
                    me.resultData = [];
                    me.resultColumns = [];
                }
            });
        },
        parseDynamicSql: function() {
            var me = this;
            var sql = this.getSql();
            if (!sql.trim()) return this.$message.warning('请输入 SQL');
            dbapi.ajax('/apiConfig/parseDynamicSql', {
                sql: sql,
                params: this.paramsJson
            }, function(res) {
                me.activeTab = 'result';
                var data = res.data || res;
                me.resultType = 'text';
                me.resultText = '解析后SQL: ' + (data.sql || '') + '\n\nJDBC参数: ' + JSON.stringify(data.jdbcParamValues || []);
                me.resultData = [];
                me.resultColumns = [];
            });
        }
    }
};
</script>

<style scoped>
.sql-editor-wrapper { display: flex; flex-direction: column; height: 100%; min-height: 500px; }
.sql-editor-top { display: flex; flex: 1; min-height: 250px; border: 1px solid #dcdfe6; }
.sql-editor-left { width: 220px; border-right: 1px solid #dcdfe6; overflow-y: auto; background: #fafafa; }
.sql-editor-right { flex: 1; display: flex; flex-direction: column; }
.sql-toolbar { padding: 5px 8px; border-bottom: 1px solid #dcdfe6; background: #f5f7fa; }
.codemirror-container { flex: 1; overflow: hidden; }
.codemirror-container .CodeMirror { height: 100%; }
.codemirror-container textarea { outline: none; }
.sql-editor-bottom { height: 280px; margin-top: 5px; }
.table-list { font-size: 12px; }
.table-item { border-bottom: 1px solid #ebeef5; }
.table-name { padding: 6px 8px; cursor: pointer; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.table-name:hover { background: #ecf5ff; }
.column-list { padding-left: 20px; }
.column-item { padding: 3px 8px; display: flex; justify-content: space-between; }
.column-item:hover { background: #f5f7fa; }
.col-name { color: #303133; }
.col-type { color: #909399; font-size: 11px; }
</style>
