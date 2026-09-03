amis.define('route/Editor.tsx', function(require, exports, module, define) {

  "use strict";
  Object.defineProperty(exports, "__esModule", { value: true });
  var tslib_1 = require("node_modules/tslib/tslib");
  var react_1 = tslib_1.__importDefault(require("node_modules/react/index"));
  var amis_editor_1 = require("node_modules/amis-editor/lib/index");
  var mobx_react_1 = require("node_modules/mobx-react/dist/index");
  var amis_1 = require("node_modules/amis/lib/index");
  var i18n_runtime_1 = require("node_modules/i18n-runtime/lib/index");
  var index_1 = require("icons/index.tsx");
  require("editor/DisabledEditorPlugin.tsx"); // 用于隐藏一些不需要的Editor预置组件
  require("renderer/MyRenderer.tsx");
  require("editor/MyRenderer.tsx");
  var currentIndex = -1;
  var host = "".concat(window.location.protocol, "//").concat(window.location.host);
  var iframeUrl = '/editor.html';
  // 如果在 gh-pages 里面
  if (/^\/amis-editor-demo/.test(window.location.pathname)) {
      host += '/amis-editor';
      iframeUrl = '/amis-editor-demo' + iframeUrl;
  }
  var schemaUrl = "".concat(host, "/schema.json");
  var editorLanguages = [
      {
          label: '简体中文',
          value: 'zh-CN'
      },
      {
          label: 'English',
          value: 'en-US'
      }
  ];
  // @ts-ignore
  '/schema.json';
  exports.default = (0, mobx_react_1.inject)('store')((0, mobx_react_1.observer)(function (_a) {
      var store = _a.store, location = _a.location, history = _a.history, match = _a.match;
      var index = parseInt(match.params.id, 10);
      var curLanguage = (0, i18n_runtime_1.currentLocale)(); // 获取当前语料类型
      if (index !== currentIndex) {
          currentIndex = index;
          store.updateSchema(store.pages[index].schema);
      }
      function save() {
          store.updatePageSchemaAt(index);
          amis_1.toast.success('保存成功', '提示');
      }
      function onChange(value) {
          store.updateSchema(value);
          store.updatePageSchemaAt(index);
      }
      function changeLocale(value) {
          localStorage.setItem('suda-i18n-locale', value);
          window.location.reload();
      }
      function exit() {
          history.push("/".concat(store.pages[index].path));
      }
      return (react_1.default.createElement("div", { className: "Editor-Demo" },
          react_1.default.createElement("div", { className: "Editor-header" },
              react_1.default.createElement("div", { className: "Editor-title" }, "amis \u53EF\u89C6\u5316\u7F16\u8F91\u5668"),
              react_1.default.createElement("div", { className: "Editor-view-mode-group-container" },
                  react_1.default.createElement("div", { className: "Editor-view-mode-group" },
                      react_1.default.createElement("div", { className: "Editor-view-mode-btn editor-header-icon ".concat(!store.isMobile ? 'is-active' : ''), onClick: function () {
                              store.setIsMobile(false);
                          } },
                          react_1.default.createElement(index_1.Icon, { icon: "pc-preview", title: "PC\u6A21\u5F0F" })),
                      react_1.default.createElement("div", { className: "Editor-view-mode-btn editor-header-icon ".concat(store.isMobile ? 'is-active' : ''), onClick: function () {
                              store.setIsMobile(true);
                          } },
                          react_1.default.createElement(index_1.Icon, { icon: "h5-preview", title: "\u79FB\u52A8\u6A21\u5F0F" })))),
              react_1.default.createElement("div", { className: "Editor-header-actions" },
                  react_1.default.createElement(amis_editor_1.ShortcutKey, null),
                  react_1.default.createElement(amis_1.Select, { className: 'margin-left-space', options: editorLanguages, value: curLanguage, clearable: false, onChange: function (e) { return changeLocale(e.value); } }),
                  react_1.default.createElement("div", { className: "header-action-btn m-1 ".concat(store.preview ? 'primary' : ''), onClick: function () {
                          store.setPreview(!store.preview);
                      } }, store.preview ? '编辑' : '预览'),
                  !store.preview && (react_1.default.createElement("div", { className: "header-action-btn exit-btn", onClick: exit }, "\u9000\u51FA")))),
          react_1.default.createElement("div", { className: "Editor-inner" },
              react_1.default.createElement(amis_editor_1.Editor, { theme: 'cxd', preview: store.preview, isMobile: store.isMobile, value: store.schema, onChange: onChange, onPreview: function () {
                      store.setPreview(true);
                  }, onSave: save, className: "is-fixed", "$schemaUrl": schemaUrl, iframeUrl: iframeUrl, showCustomRenderersPanel: true, amisEnv: {
                      fetcher: store.fetcher,
                      notify: store.notify,
                      alert: store.alert,
                      copy: store.copy,
                  } }))));
  }));
  //# sourceMappingURL=/route/Editor.js.map
  

});
