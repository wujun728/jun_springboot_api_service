amis.define('editor/DisabledEditorPlugin.tsx', function(require, exports, module, define) {

  "use strict";
  Object.defineProperty(exports, "__esModule", { value: true });
  exports.ManagerEditorPlugin = void 0;
  var tslib_1 = require("node_modules/tslib/tslib");
  var amis_editor_1 = require("node_modules/amis-editor/lib/index");
  /**
   * 用于隐藏一些不需要的Editor组件
   * 备注: 如果不知道当前Editor中有哪些预置组件，可以在这里设置一个断点，console.log 看一下 renderers。
   */
  // 需要在组件面板中隐藏的组件
  var disabledRenderers = [
      'audio',
      'carousel',
      'custom',
      'log',
      'sparkline', // 走势图
  ];
  var ManagerEditorPlugin = /** @class */ (function (_super) {
      tslib_1.__extends(ManagerEditorPlugin, _super);
      function ManagerEditorPlugin() {
          var _this = _super !== null && _super.apply(this, arguments) || this;
          _this.order = 9999;
          return _this;
      }
      ManagerEditorPlugin.prototype.buildSubRenderers = function (context, renderers) {
          // 更新NPM自定义组件排序和分类
          for (var index = 0, size = renderers.length; index < size; index++) {
              // 判断是否需要隐藏 Editor预置组件
              var pluginRendererName = renderers[index].rendererName;
              if (pluginRendererName && disabledRenderers.indexOf(pluginRendererName) > -1) {
                  renderers[index].disabledRendererPlugin = true; // 更新状态
              }
          }
      };
      return ManagerEditorPlugin;
  }(amis_editor_1.BasePlugin));
  exports.ManagerEditorPlugin = ManagerEditorPlugin;
  (0, amis_editor_1.registerEditorPlugin)(ManagerEditorPlugin);
  //# sourceMappingURL=/editor/DisabledEditorPlugin.js.map
  

});
