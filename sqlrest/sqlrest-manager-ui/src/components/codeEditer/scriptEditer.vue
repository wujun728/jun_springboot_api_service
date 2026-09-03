<template>
  <div class="custom-code-script">
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
    <editor v-model="value"
            ref="aceEditor"
            @init="editorInit"
            lang="groovy"
            theme="vibrant_ink"
            height="300"
            :options="options"></editor>
  </div>
</template>

<script>
// https://blog.csdn.net/sunsineq/article/details/116980241

import Editor from 'vue2-ace-editor'
export default {
  name: 'scriptEditor',
  components: {
    Editor
  },
  data () {
    return {
      editor: null,
      value: "",
      options: {
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: true,
        showPrintMargin: false,
        showLineNumbers: true,
        tabSize: 6,
        fontSize: 14,
      },
    }
  },
  props: {
    content: {
      type: String,
      default: ''
    },
    keywordHints: {
      type: Array,
      default: () => [
      ]
    }
  },
  methods: {
    editorInit: function (editor) {
      this.editor = editor;
      require('brace/ext/language_tools') //language extension prerequsite...
      require('brace/mode/groovy')    //language
      require('brace/theme/chrome')   // theme
      require('brace/theme/vibrant_ink') // theme
      require('brace/snippets/groovy') //snippet
      this.setCompletionHints(this.keywordHints)
    },
    setCompletionHints: function (data) {
      let langTools = ace.acequire('ace/ext/language_tools')
      const completer = {
        getCompletions: function (editors, session, pos, prefix, callback) {
          if (prefix.length === 0) {
            return callback(null, [])
          } else {
            return callback(null, data)
          }
        }
      }
      langTools.addCompleter(completer)
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
      this.editor.insert(val)
    },
    setTableHints: function (data) {
      this.keywordHints = []
      for (let i = 0; i < data.length; i++) {
        this.keywordHints.push(data[i]);
      }
      this.setCompletionHints(this.keywordHints)
    },
    queryContent: function () {
      return [this.editor.getValue()]
    }
  },
  mounted () {
    this.value = this.content
  },
  watch: {
    keywordHints (newVal, OldVal) {
      this.keywordHints = newVal
    }
  },
}
</script>

<style scoped>
.custom-code-script {
  font-size: 13px;
  line-height: 150%;
}
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
</style>
