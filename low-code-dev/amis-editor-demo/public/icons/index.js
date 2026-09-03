amis.define('icons/index.tsx', function(require, exports, module, define) {

  "use strict";
  /**
   * @file 内置 svg 图标
   */
  Object.defineProperty(exports, "__esModule", { value: true });
  exports.Icon = void 0;
  var tslib_1 = require("node_modules/tslib/tslib");
  var amis_1 = require("node_modules/amis/lib/index");
  Object.defineProperty(exports, "Icon", { enumerable: true, get: function () { return amis_1.Icon; } });
  // @ts-ignore
  var pc_preview_svg_1 = tslib_1.__importDefault(require("icons/pc-preview.svg"));
  // @ts-ignore
  var h5_preview_svg_1 = tslib_1.__importDefault(require("icons/h5-preview.svg"));
  (0, amis_1.registerIcon)('pc-preview', pc_preview_svg_1.default);
  (0, amis_1.registerIcon)('h5-preview', h5_preview_svg_1.default);
  //# sourceMappingURL=/icons/index.js.map
  

});
