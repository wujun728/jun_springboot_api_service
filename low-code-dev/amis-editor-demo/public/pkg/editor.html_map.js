amis.require.resourceMap({
  "res": {
    "node_modules/tslib/tslib": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/object-assign/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react/cjs/react.production.min": {
      "type": "js",
      "deps": [
        "node_modules/object-assign/index"
      ],
      "pkg": "p0"
    },
    "node_modules/prop-types/lib/ReactPropTypesSecret": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/prop-types/lib/has": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/prop-types/checkPropTypes": {
      "type": "js",
      "deps": [
        "node_modules/prop-types/lib/ReactPropTypesSecret",
        "node_modules/prop-types/lib/has"
      ],
      "pkg": "p0"
    },
    "node_modules/react/cjs/react.development": {
      "type": "js",
      "deps": [
        "node_modules/object-assign/index",
        "node_modules/prop-types/checkPropTypes"
      ],
      "pkg": "p0"
    },
    "node_modules/react/index": {
      "type": "js",
      "deps": [
        "node_modules/react/cjs/react.production.min",
        "node_modules/react/cjs/react.development"
      ],
      "pkg": "p0"
    },
    "node_modules/scheduler/cjs/scheduler.production.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/scheduler/cjs/scheduler.development": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/scheduler/index": {
      "type": "js",
      "deps": [
        "node_modules/scheduler/cjs/scheduler.production.min",
        "node_modules/scheduler/cjs/scheduler.development"
      ],
      "pkg": "p0"
    },
    "node_modules/react-dom/cjs/react-dom.production.min": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/object-assign/index",
        "node_modules/scheduler/index"
      ],
      "pkg": "p0"
    },
    "node_modules/scheduler/cjs/scheduler-tracing.production.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/scheduler/cjs/scheduler-tracing.development": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/scheduler/tracing": {
      "type": "js",
      "deps": [
        "node_modules/scheduler/cjs/scheduler-tracing.production.min",
        "node_modules/scheduler/cjs/scheduler-tracing.development"
      ],
      "pkg": "p0"
    },
    "node_modules/react-dom/cjs/react-dom.development": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/object-assign/index",
        "node_modules/scheduler/index",
        "node_modules/prop-types/checkPropTypes",
        "node_modules/scheduler/tracing"
      ],
      "pkg": "p0"
    },
    "node_modules/react-dom/index": {
      "type": "js",
      "deps": [
        "node_modules/react-dom/cjs/react-dom.production.min",
        "node_modules/react-dom/cjs/react-dom.development"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx/lib/mobx.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mobx/lib/mobx": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mobx/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/mobx.min",
        "node_modules/mobx/lib/mobx"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-state-tree/dist/mobx-state-tree": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_freeGlobal": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_root": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_freeGlobal"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_Symbol": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getRawTag": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Symbol"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_objectToString": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseGetTag": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Symbol",
        "node_modules/lodash/_getRawTag",
        "node_modules/lodash/_objectToString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_overArg": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_getPrototype": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_overArg"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isObjectLike": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/isPlainObject": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/_getPrototype",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_listCacheClear": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/eq": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_assocIndexOf": {
      "type": "js",
      "deps": [
        "node_modules/lodash/eq"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_listCacheDelete": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_assocIndexOf"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_listCacheGet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_assocIndexOf"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_listCacheHas": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_assocIndexOf"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_listCacheSet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_assocIndexOf"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_ListCache": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_listCacheClear",
        "node_modules/lodash/_listCacheDelete",
        "node_modules/lodash/_listCacheGet",
        "node_modules/lodash/_listCacheHas",
        "node_modules/lodash/_listCacheSet"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_stackClear": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_ListCache"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_stackDelete": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_stackGet": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_stackHas": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/isObject": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/isFunction": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_coreJsData": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_isMasked": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_coreJsData"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_toSource": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsNative": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isFunction",
        "node_modules/lodash/_isMasked",
        "node_modules/lodash/isObject",
        "node_modules/lodash/_toSource"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getValue": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_getNative": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsNative",
        "node_modules/lodash/_getValue"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_Map": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getNative",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_nativeCreate": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getNative"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_hashClear": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_nativeCreate"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_hashDelete": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_hashGet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_nativeCreate"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_hashHas": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_nativeCreate"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_hashSet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_nativeCreate"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_Hash": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_hashClear",
        "node_modules/lodash/_hashDelete",
        "node_modules/lodash/_hashGet",
        "node_modules/lodash/_hashHas",
        "node_modules/lodash/_hashSet"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_mapCacheClear": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Hash",
        "node_modules/lodash/_ListCache",
        "node_modules/lodash/_Map"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_isKeyable": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_getMapData": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_isKeyable"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_mapCacheDelete": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getMapData"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_mapCacheGet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getMapData"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_mapCacheHas": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getMapData"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_mapCacheSet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getMapData"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_MapCache": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_mapCacheClear",
        "node_modules/lodash/_mapCacheDelete",
        "node_modules/lodash/_mapCacheGet",
        "node_modules/lodash/_mapCacheHas",
        "node_modules/lodash/_mapCacheSet"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_stackSet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_ListCache",
        "node_modules/lodash/_Map",
        "node_modules/lodash/_MapCache"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_Stack": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_ListCache",
        "node_modules/lodash/_stackClear",
        "node_modules/lodash/_stackDelete",
        "node_modules/lodash/_stackGet",
        "node_modules/lodash/_stackHas",
        "node_modules/lodash/_stackSet"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_setCacheAdd": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_setCacheHas": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_SetCache": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_MapCache",
        "node_modules/lodash/_setCacheAdd",
        "node_modules/lodash/_setCacheHas"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arraySome": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_cacheHas": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_equalArrays": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_SetCache",
        "node_modules/lodash/_arraySome",
        "node_modules/lodash/_cacheHas"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_Uint8Array": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_mapToArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_setToArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_equalByTag": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Symbol",
        "node_modules/lodash/_Uint8Array",
        "node_modules/lodash/eq",
        "node_modules/lodash/_equalArrays",
        "node_modules/lodash/_mapToArray",
        "node_modules/lodash/_setToArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayPush": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/isArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseGetAllKeys": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayPush",
        "node_modules/lodash/isArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayFilter": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/stubArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_getSymbols": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayFilter",
        "node_modules/lodash/stubArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseTimes": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsArguments": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isArguments": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsArguments",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/stubFalse": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/isBuffer": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_root",
        "node_modules/lodash/stubFalse"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_isIndex": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/isLength": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsTypedArray": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isLength",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseUnary": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_apply": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/identity": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_overRest": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_apply"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/constant": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_defineProperty": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getNative"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseSetToString": {
      "type": "js",
      "deps": [
        "node_modules/lodash/constant",
        "node_modules/lodash/_defineProperty",
        "node_modules/lodash/identity"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_shortOut": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_setToString": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseSetToString",
        "node_modules/lodash/_shortOut"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseRest": {
      "type": "js",
      "deps": [
        "node_modules/lodash/identity",
        "node_modules/lodash/_overRest",
        "node_modules/lodash/_setToString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isError": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isObjectLike",
        "node_modules/lodash/isPlainObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/attempt": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_apply",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/isError"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayEach": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseAssignValue": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_defineProperty"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_WeakMap": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getNative",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_metaMap": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_WeakMap"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseSetData": {
      "type": "js",
      "deps": [
        "node_modules/lodash/identity",
        "node_modules/lodash/_metaMap"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseCreate": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createCtor": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseCreate",
        "node_modules/lodash/isObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createBind": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createCtor",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_composeArgs": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_composeArgsRight": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_countHolders": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseLodash": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_LazyWrapper": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseCreate",
        "node_modules/lodash/_baseLodash"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/noop": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_getData": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_metaMap",
        "node_modules/lodash/noop"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_realNames": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_getFuncName": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_realNames"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_LodashWrapper": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseCreate",
        "node_modules/lodash/_baseLodash"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_copyArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_wrapperClone": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_LazyWrapper",
        "node_modules/lodash/_LodashWrapper",
        "node_modules/lodash/_copyArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/wrapperLodash": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_LazyWrapper",
        "node_modules/lodash/_LodashWrapper",
        "node_modules/lodash/_baseLodash",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isObjectLike",
        "node_modules/lodash/_wrapperClone"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_isLaziable": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_LazyWrapper",
        "node_modules/lodash/_getData",
        "node_modules/lodash/_getFuncName",
        "node_modules/lodash/wrapperLodash"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_setData": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseSetData",
        "node_modules/lodash/_shortOut"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getWrapDetails": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_insertWrapDetails": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseFindIndex": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsNaN": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_strictIndexOf": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIndexOf": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseFindIndex",
        "node_modules/lodash/_baseIsNaN",
        "node_modules/lodash/_strictIndexOf"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayIncludes": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIndexOf"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_updateWrapDetails": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayEach",
        "node_modules/lodash/_arrayIncludes"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_setWrapToString": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getWrapDetails",
        "node_modules/lodash/_insertWrapDetails",
        "node_modules/lodash/_setToString",
        "node_modules/lodash/_updateWrapDetails"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createRecurry": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_isLaziable",
        "node_modules/lodash/_setData",
        "node_modules/lodash/_setWrapToString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getHolder": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_reorder": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_copyArray",
        "node_modules/lodash/_isIndex"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_replaceHolders": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_createHybrid": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_composeArgs",
        "node_modules/lodash/_composeArgsRight",
        "node_modules/lodash/_countHolders",
        "node_modules/lodash/_createCtor",
        "node_modules/lodash/_createRecurry",
        "node_modules/lodash/_getHolder",
        "node_modules/lodash/_reorder",
        "node_modules/lodash/_replaceHolders",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createCurry": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_apply",
        "node_modules/lodash/_createCtor",
        "node_modules/lodash/_createHybrid",
        "node_modules/lodash/_createRecurry",
        "node_modules/lodash/_getHolder",
        "node_modules/lodash/_replaceHolders",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createPartial": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_apply",
        "node_modules/lodash/_createCtor",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_mergeData": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_composeArgs",
        "node_modules/lodash/_composeArgsRight",
        "node_modules/lodash/_replaceHolders"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_trimmedEndIndex": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseTrim": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_trimmedEndIndex"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isSymbol": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/toNumber": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseTrim",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isSymbol"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/toFinite": {
      "type": "js",
      "deps": [
        "node_modules/lodash/toNumber"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/toInteger": {
      "type": "js",
      "deps": [
        "node_modules/lodash/toFinite"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createWrap": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseSetData",
        "node_modules/lodash/_createBind",
        "node_modules/lodash/_createCurry",
        "node_modules/lodash/_createHybrid",
        "node_modules/lodash/_createPartial",
        "node_modules/lodash/_getData",
        "node_modules/lodash/_mergeData",
        "node_modules/lodash/_setData",
        "node_modules/lodash/_setWrapToString",
        "node_modules/lodash/toInteger"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/bind": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_createWrap",
        "node_modules/lodash/_getHolder",
        "node_modules/lodash/_replaceHolders"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_isFlattenable": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Symbol",
        "node_modules/lodash/isArguments",
        "node_modules/lodash/isArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseFlatten": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayPush",
        "node_modules/lodash/_isFlattenable"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/flatten": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseFlatten"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_flatRest": {
      "type": "js",
      "deps": [
        "node_modules/lodash/flatten",
        "node_modules/lodash/_overRest",
        "node_modules/lodash/_setToString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_toKey": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isSymbol"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/bindAll": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayEach",
        "node_modules/lodash/_baseAssignValue",
        "node_modules/lodash/bind",
        "node_modules/lodash/_flatRest",
        "node_modules/lodash/_toKey"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayMap": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsMatch": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Stack",
        "node_modules/lodash/_baseIsEqual"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_isStrictComparable": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getMatchData": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_isStrictComparable",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_matchesStrictComparable": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseMatches": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsMatch",
        "node_modules/lodash/_getMatchData",
        "node_modules/lodash/_matchesStrictComparable"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_isKey": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isArray",
        "node_modules/lodash/isSymbol"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/memoize": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_MapCache"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_memoizeCapped": {
      "type": "js",
      "deps": [
        "node_modules/lodash/memoize"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_stringToPath": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_memoizeCapped"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseToString": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Symbol",
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isSymbol"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/toString": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseToString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_castPath": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isArray",
        "node_modules/lodash/_isKey",
        "node_modules/lodash/_stringToPath",
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseGet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_castPath",
        "node_modules/lodash/_toKey"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/get": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGet"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseHasIn": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_hasPath": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_castPath",
        "node_modules/lodash/isArguments",
        "node_modules/lodash/isArray",
        "node_modules/lodash/_isIndex",
        "node_modules/lodash/isLength",
        "node_modules/lodash/_toKey"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/hasIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseHasIn",
        "node_modules/lodash/_hasPath"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseMatchesProperty": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsEqual",
        "node_modules/lodash/get",
        "node_modules/lodash/hasIn",
        "node_modules/lodash/_isKey",
        "node_modules/lodash/_isStrictComparable",
        "node_modules/lodash/_matchesStrictComparable",
        "node_modules/lodash/_toKey"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseProperty": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_basePropertyDeep": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGet"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/property": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseProperty",
        "node_modules/lodash/_basePropertyDeep",
        "node_modules/lodash/_isKey",
        "node_modules/lodash/_toKey"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIteratee": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseMatches",
        "node_modules/lodash/_baseMatchesProperty",
        "node_modules/lodash/identity",
        "node_modules/lodash/isArray",
        "node_modules/lodash/property"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/cond": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_apply",
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_baseRest"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_assignValue": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseAssignValue",
        "node_modules/lodash/eq"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_copyObject": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_assignValue",
        "node_modules/lodash/_baseAssignValue"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseAssign": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_isPrototype": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_nativeKeysIn": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseKeysIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isObject",
        "node_modules/lodash/_isPrototype",
        "node_modules/lodash/_nativeKeysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isArrayLike": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isFunction",
        "node_modules/lodash/isLength"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/keysIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayLikeKeys",
        "node_modules/lodash/_baseKeysIn",
        "node_modules/lodash/isArrayLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseAssignIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/keysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_cloneBuffer": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_copySymbols": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/_getSymbols"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getSymbolsIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayPush",
        "node_modules/lodash/_getPrototype",
        "node_modules/lodash/_getSymbols",
        "node_modules/lodash/stubArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_copySymbolsIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/_getSymbolsIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getAllKeysIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetAllKeys",
        "node_modules/lodash/_getSymbolsIn",
        "node_modules/lodash/keysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_DataView": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getNative",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_Promise": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getNative",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_Set": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getNative",
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getTag": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_DataView",
        "node_modules/lodash/_Map",
        "node_modules/lodash/_Promise",
        "node_modules/lodash/_Set",
        "node_modules/lodash/_WeakMap",
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/_toSource"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_initCloneArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_cloneArrayBuffer": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Uint8Array"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_cloneDataView": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_cloneArrayBuffer"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_cloneRegExp": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_cloneSymbol": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Symbol"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_cloneTypedArray": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_cloneArrayBuffer"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_initCloneByTag": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_cloneArrayBuffer",
        "node_modules/lodash/_cloneDataView",
        "node_modules/lodash/_cloneRegExp",
        "node_modules/lodash/_cloneSymbol",
        "node_modules/lodash/_cloneTypedArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_initCloneObject": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseCreate",
        "node_modules/lodash/_getPrototype",
        "node_modules/lodash/_isPrototype"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsMap": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getTag",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isMap": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsMap",
        "node_modules/lodash/_baseUnary",
        "node_modules/lodash/_nodeUtil"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsSet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getTag",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isSet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsSet",
        "node_modules/lodash/_baseUnary",
        "node_modules/lodash/_nodeUtil"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseClone": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Stack",
        "node_modules/lodash/_arrayEach",
        "node_modules/lodash/_assignValue",
        "node_modules/lodash/_baseAssign",
        "node_modules/lodash/_baseAssignIn",
        "node_modules/lodash/_cloneBuffer",
        "node_modules/lodash/_copyArray",
        "node_modules/lodash/_copySymbols",
        "node_modules/lodash/_copySymbolsIn",
        "node_modules/lodash/_getAllKeys",
        "node_modules/lodash/_getAllKeysIn",
        "node_modules/lodash/_getTag",
        "node_modules/lodash/_initCloneArray",
        "node_modules/lodash/_initCloneByTag",
        "node_modules/lodash/_initCloneObject",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isBuffer",
        "node_modules/lodash/isMap",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isSet",
        "node_modules/lodash/keys",
        "node_modules/lodash/keysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseConformsTo": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseConforms": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseConformsTo",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/conforms": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseClone",
        "node_modules/lodash/_baseConforms"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/defaultTo": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_createFlow": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_LodashWrapper",
        "node_modules/lodash/_flatRest",
        "node_modules/lodash/_getData",
        "node_modules/lodash/_getFuncName",
        "node_modules/lodash/isArray",
        "node_modules/lodash/_isLaziable"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/flow": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createFlow"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/flowRight": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createFlow"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/iteratee": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseClone",
        "node_modules/lodash/_baseIteratee"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/matches": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseClone",
        "node_modules/lodash/_baseMatches"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/matchesProperty": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseClone",
        "node_modules/lodash/_baseMatchesProperty"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/last": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseSlice": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_parent": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGet",
        "node_modules/lodash/_baseSlice"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseInvoke": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_apply",
        "node_modules/lodash/_castPath",
        "node_modules/lodash/last",
        "node_modules/lodash/_parent",
        "node_modules/lodash/_toKey"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/method": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseInvoke",
        "node_modules/lodash/_baseRest"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/methodOf": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseInvoke",
        "node_modules/lodash/_baseRest"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseFunctions": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayFilter",
        "node_modules/lodash/isFunction"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/mixin": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayEach",
        "node_modules/lodash/_arrayPush",
        "node_modules/lodash/_baseFunctions",
        "node_modules/lodash/_copyArray",
        "node_modules/lodash/isFunction",
        "node_modules/lodash/isObject",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseNth": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_isIndex"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/nthArg": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseNth",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/toInteger"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createOver": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_apply",
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_baseUnary",
        "node_modules/lodash/_flatRest"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/over": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_createOver"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayEvery": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/overEvery": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayEvery",
        "node_modules/lodash/_createOver"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/overSome": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arraySome",
        "node_modules/lodash/_createOver"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/propertyOf": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGet"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseRange": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_isIterateeCall": {
      "type": "js",
      "deps": [
        "node_modules/lodash/eq",
        "node_modules/lodash/isArrayLike",
        "node_modules/lodash/_isIndex",
        "node_modules/lodash/isObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createRange": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseRange",
        "node_modules/lodash/_isIterateeCall",
        "node_modules/lodash/toFinite"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/range": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createRange"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/rangeRight": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createRange"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/stubObject": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/stubString": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/stubTrue": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_castFunction": {
      "type": "js",
      "deps": [
        "node_modules/lodash/identity"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/times": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseTimes",
        "node_modules/lodash/_castFunction",
        "node_modules/lodash/toInteger"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/toPath": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_copyArray",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isSymbol",
        "node_modules/lodash/_stringToPath",
        "node_modules/lodash/_toKey",
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/uniqueId": {
      "type": "js",
      "deps": [
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/util": {
      "type": "js",
      "deps": [
        "node_modules/lodash/attempt",
        "node_modules/lodash/bindAll",
        "node_modules/lodash/cond",
        "node_modules/lodash/conforms",
        "node_modules/lodash/constant",
        "node_modules/lodash/defaultTo",
        "node_modules/lodash/flow",
        "node_modules/lodash/flowRight",
        "node_modules/lodash/identity",
        "node_modules/lodash/iteratee",
        "node_modules/lodash/matches",
        "node_modules/lodash/matchesProperty",
        "node_modules/lodash/method",
        "node_modules/lodash/methodOf",
        "node_modules/lodash/mixin",
        "node_modules/lodash/noop",
        "node_modules/lodash/nthArg",
        "node_modules/lodash/over",
        "node_modules/lodash/overEvery",
        "node_modules/lodash/overSome",
        "node_modules/lodash/property",
        "node_modules/lodash/propertyOf",
        "node_modules/lodash/range",
        "node_modules/lodash/rangeRight",
        "node_modules/lodash/stubArray",
        "node_modules/lodash/stubFalse",
        "node_modules/lodash/stubObject",
        "node_modules/lodash/stubString",
        "node_modules/lodash/stubTrue",
        "node_modules/lodash/times",
        "node_modules/lodash/toPath",
        "node_modules/lodash/uniqueId"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_nodeUtil": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_freeGlobal",
        "node_modules/lodash/util"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isTypedArray": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsTypedArray",
        "node_modules/lodash/_baseUnary",
        "node_modules/lodash/_nodeUtil"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayLikeKeys": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseTimes",
        "node_modules/lodash/isArguments",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isBuffer",
        "node_modules/lodash/_isIndex",
        "node_modules/lodash/isTypedArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_nativeKeys": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_overArg"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseKeys": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_isPrototype",
        "node_modules/lodash/_nativeKeys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/keys": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayLikeKeys",
        "node_modules/lodash/_baseKeys",
        "node_modules/lodash/isArrayLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_getAllKeys": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetAllKeys",
        "node_modules/lodash/_getSymbols",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_equalObjects": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_getAllKeys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsEqualDeep": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Stack",
        "node_modules/lodash/_equalArrays",
        "node_modules/lodash/_equalByTag",
        "node_modules/lodash/_equalObjects",
        "node_modules/lodash/_getTag",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isBuffer",
        "node_modules/lodash/isTypedArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsEqual": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsEqualDeep",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isEqual": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsEqual"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isNumber": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isNaN": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isNumber"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayIncludesWith": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_createSet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Set",
        "node_modules/lodash/noop",
        "node_modules/lodash/_setToArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseUniq": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_SetCache",
        "node_modules/lodash/_arrayIncludes",
        "node_modules/lodash/_arrayIncludesWith",
        "node_modules/lodash/_cacheHas",
        "node_modules/lodash/_createSet",
        "node_modules/lodash/_setToArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/uniq": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseUniq"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_assignMergeValue": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseAssignValue",
        "node_modules/lodash/eq"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createBaseFor": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_baseFor": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createBaseFor"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isArrayLikeObject": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isArrayLike",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_safeGet": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/toPlainObject": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/keysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseMergeDeep": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_assignMergeValue",
        "node_modules/lodash/_cloneBuffer",
        "node_modules/lodash/_cloneTypedArray",
        "node_modules/lodash/_copyArray",
        "node_modules/lodash/_initCloneObject",
        "node_modules/lodash/isArguments",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isArrayLikeObject",
        "node_modules/lodash/isBuffer",
        "node_modules/lodash/isFunction",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isPlainObject",
        "node_modules/lodash/isTypedArray",
        "node_modules/lodash/_safeGet",
        "node_modules/lodash/toPlainObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseMerge": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_Stack",
        "node_modules/lodash/_assignMergeValue",
        "node_modules/lodash/_baseFor",
        "node_modules/lodash/_baseMergeDeep",
        "node_modules/lodash/isObject",
        "node_modules/lodash/keysIn",
        "node_modules/lodash/_safeGet"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createAssigner": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_isIterateeCall"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/merge": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseMerge",
        "node_modules/lodash/_createAssigner"
      ],
      "pkg": "p0"
    },
    "node_modules/moment/moment": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_castSlice": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseSlice"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_hasUnicode": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_asciiToArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_unicodeToArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_stringToArray": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_asciiToArray",
        "node_modules/lodash/_hasUnicode",
        "node_modules/lodash/_unicodeToArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createCaseFirst": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_castSlice",
        "node_modules/lodash/_hasUnicode",
        "node_modules/lodash/_stringToArray",
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/upperFirst": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createCaseFirst"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseRepeat": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_asciiSize": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseProperty"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_unicodeSize": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_stringSize": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_asciiSize",
        "node_modules/lodash/_hasUnicode",
        "node_modules/lodash/_unicodeSize"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createPadding": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseRepeat",
        "node_modules/lodash/_baseToString",
        "node_modules/lodash/_castSlice",
        "node_modules/lodash/_hasUnicode",
        "node_modules/lodash/_stringSize",
        "node_modules/lodash/_stringToArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/padStart": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createPadding",
        "node_modules/lodash/_stringSize",
        "node_modules/lodash/toInteger",
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/capitalize": {
      "type": "js",
      "deps": [
        "node_modules/lodash/toString",
        "node_modules/lodash/upperFirst"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_basePropertyOf": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_escapeHtmlChar": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_basePropertyOf"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/escape": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_escapeHtmlChar",
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIsRegExp": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isRegExp": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsRegExp",
        "node_modules/lodash/_baseUnary",
        "node_modules/lodash/_nodeUtil"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/truncate": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseToString",
        "node_modules/lodash/_castSlice",
        "node_modules/lodash/_hasUnicode",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isRegExp",
        "node_modules/lodash/_stringSize",
        "node_modules/lodash/_stringToArray",
        "node_modules/lodash/toInteger",
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/uniqWith": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseUniq"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/uniqBy": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_baseUniq"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-formula/lib/evalutor": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/moment/moment",
        "node_modules/lodash/upperFirst",
        "node_modules/lodash/padStart",
        "node_modules/lodash/capitalize",
        "node_modules/lodash/escape",
        "node_modules/lodash/truncate",
        "node_modules/lodash/uniqWith",
        "node_modules/lodash/uniqBy",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/isPlainObject"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-formula/lib/lexer": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-formula/lib/parser": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/lexer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-formula/lib/filter": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/evalutor"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-formula/lib/function": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/evalutor"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-formula/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/evalutor",
        "node_modules/amis-formula/lib/parser",
        "node_modules/amis-formula/lib/lexer",
        "node_modules/amis-formula/lib/filter",
        "node_modules/amis-formula/lib/function"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/tokenize": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/keyToPath": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/getVariable": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/keyToPath"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/resolveVariable": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/index",
        "node_modules/amis-core/lib/utils/getVariable"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/makeSorter": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/resolveVariable"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseForOwn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseFor",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/transform": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayEach",
        "node_modules/lodash/_baseCreate",
        "node_modules/lodash/_baseForOwn",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_getPrototype",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isBuffer",
        "node_modules/lodash/isFunction",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isTypedArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayAggregator": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_createBaseEach": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isArrayLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseEach": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseForOwn",
        "node_modules/lodash/_createBaseEach"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseAggregator": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseEach"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createAggregator": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayAggregator",
        "node_modules/lodash/_baseAggregator",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/isArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/groupBy": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseAssignValue",
        "node_modules/lodash/_createAggregator"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/object": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/keyToPath",
        "node_modules/amis-core/lib/utils/resolveVariable"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/string2regExp": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/escapeHtml": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/formatDuration": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/prettyBytes": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/stripNumber": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/date": {
      "type": "js",
      "deps": [
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/tokenize"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/filter": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/makeSorter",
        "node_modules/lodash/transform",
        "node_modules/lodash/groupBy",
        "node_modules/lodash/uniqBy",
        "node_modules/lodash/uniq",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/string2regExp",
        "node_modules/amis-core/lib/utils/resolveVariable",
        "node_modules/amis-core/lib/utils/escapeHtml",
        "node_modules/amis-core/lib/utils/formatDuration",
        "node_modules/amis-core/lib/utils/prettyBytes",
        "node_modules/amis-core/lib/utils/stripNumber",
        "node_modules/amis-core/lib/utils/date"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/tpl-builtin": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/tslib/tslib",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/tokenize",
        "node_modules/amis-core/lib/utils/filter"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/assignInWith": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/_createAssigner",
        "node_modules/lodash/keysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseValues": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_customDefaultsAssignIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/eq"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_escapeStringChar": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_reInterpolate": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_reEscape": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_reEvaluate": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/templateSettings": {
      "type": "js",
      "deps": [
        "node_modules/lodash/escape",
        "node_modules/lodash/_reEscape",
        "node_modules/lodash/_reEvaluate",
        "node_modules/lodash/_reInterpolate"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/template": {
      "type": "js",
      "deps": [
        "node_modules/lodash/assignInWith",
        "node_modules/lodash/attempt",
        "node_modules/lodash/_baseValues",
        "node_modules/lodash/_customDefaultsAssignIn",
        "node_modules/lodash/_escapeStringChar",
        "node_modules/lodash/isError",
        "node_modules/lodash/_isIterateeCall",
        "node_modules/lodash/keys",
        "node_modules/lodash/_reInterpolate",
        "node_modules/lodash/templateSettings",
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/tpl-lodash": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/template",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/filter"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/tpl": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/tpl-builtin",
        "node_modules/amis-core/lib/utils/tpl-lodash",
        "node_modules/amis-formula/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/node_modules/qs/lib/formats": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/node_modules/qs/lib/utils": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/node_modules/qs/lib/formats"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/node_modules/qs/lib/stringify": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/node_modules/qs/lib/utils",
        "node_modules/amis-core/node_modules/qs/lib/formats"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/node_modules/qs/lib/parse": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/node_modules/qs/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/node_modules/qs/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/node_modules/qs/lib/stringify",
        "node_modules/amis-core/node_modules/qs/lib/parse",
        "node_modules/amis-core/node_modules/qs/lib/formats"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/autobind": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/isPureVariable": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/resolveVariableAndFilter": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/helper": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/isPlainObject",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/isNaN",
        "node_modules/lodash/uniq",
        "node_modules/lodash/last",
        "node_modules/lodash/merge",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/node_modules/qs/lib/index",
        "node_modules/amis-core/lib/utils/autobind",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/isPureVariable",
        "node_modules/amis-core/lib/utils/resolveVariable",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/mobx/lib/index",
        "node_modules/amis-core/lib/utils/string2regExp",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/amis-core/lib/utils/keyToPath"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/dataMapping": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/isPureVariable",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/amis-core/lib/utils/tokenize",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createFind": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/isArrayLike",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/findIndex": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseFindIndex",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/toInteger"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/find": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createFind",
        "node_modules/lodash/findIndex"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/SimpleMap": {
      "type": "js",
      "deps": [
        "node_modules/lodash/find",
        "node_modules/lodash/findIndex"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/manager": {
      "type": "js",
      "deps": [
        "node_modules/mobx-state-tree/dist/mobx-state-tree"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/node": {
      "type": "js",
      "deps": [
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/store/manager"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/iRenderer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/amis-core/lib/store/node"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/errors": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseUnset": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_castPath",
        "node_modules/lodash/last",
        "node_modules/lodash/_parent",
        "node_modules/lodash/_toKey"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_customOmitClone": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isPlainObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/omit": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseClone",
        "node_modules/lodash/_baseUnset",
        "node_modules/lodash/_castPath",
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/_customOmitClone",
        "node_modules/lodash/_flatRest",
        "node_modules/lodash/_getAllKeysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/classnames/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-json-view/dist/main": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/assertEnvironment": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/utils/reactBatchedUpdates": {
      "type": "js",
      "deps": [
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/utils": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/observerBatching": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index",
        "node_modules/mobx-react-lite/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/staticRendering": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/printDebugValue": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/reactionCleanupTracking": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/useQueuedForceUpdate": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/useObserver": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index",
        "node_modules/react/index",
        "node_modules/mobx-react-lite/lib/printDebugValue",
        "node_modules/mobx-react-lite/lib/reactionCleanupTracking",
        "node_modules/mobx-react-lite/lib/staticRendering",
        "node_modules/mobx-react-lite/lib/utils",
        "node_modules/mobx-react-lite/lib/useQueuedForceUpdate"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/observer": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/mobx-react-lite/lib/staticRendering",
        "node_modules/mobx-react-lite/lib/useObserver"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/ObserverComponent": {
      "type": "js",
      "deps": [
        "node_modules/mobx-react-lite/lib/useObserver"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/useAsObservableSource": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index",
        "node_modules/react/index",
        "node_modules/mobx-react-lite/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/useLocalStore": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index",
        "node_modules/react/index",
        "node_modules/mobx-react-lite/lib/useAsObservableSource",
        "node_modules/mobx-react-lite/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react-lite/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/mobx-react-lite/lib/assertEnvironment",
        "node_modules/mobx-react-lite/lib/utils/reactBatchedUpdates",
        "node_modules/mobx-react-lite/lib/observerBatching",
        "node_modules/mobx-react-lite/lib/staticRendering",
        "node_modules/mobx-react-lite/lib/observer",
        "node_modules/mobx-react-lite/lib/useObserver",
        "node_modules/mobx-react-lite/lib/ObserverComponent",
        "node_modules/mobx-react-lite/lib/utils",
        "node_modules/mobx-react-lite/lib/useAsObservableSource",
        "node_modules/mobx-react-lite/lib/useLocalStore",
        "node_modules/mobx-react-lite/lib/useQueuedForceUpdate"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react/dist/mobxreact.cjs.production.min": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index",
        "node_modules/react/index",
        "node_modules/mobx-react-lite/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react/dist/mobxreact.cjs.development": {
      "type": "js",
      "deps": [
        "node_modules/mobx/lib/index",
        "node_modules/react/index",
        "node_modules/mobx-react-lite/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/mobx-react/dist/index": {
      "type": "js",
      "deps": [
        "node_modules/mobx-react/dist/mobxreact.cjs.production.min",
        "node_modules/mobx-react/dist/mobxreact.cjs.development"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/offset": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/offsetParent": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/position": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/offset",
        "node_modules/amis-core/lib/utils/offsetParent"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/debug": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/react-dom/index",
        "node_modules/react-json-view/dist/main",
        "node_modules/mobx/lib/index",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/position"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/api": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/omit",
        "node_modules/amis-formula/lib/index",
        "node_modules/amis-core/lib/utils/escapeHtml",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/tokenize",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/debug"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/cloneDeep": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseClone"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/replaceText": {
      "type": "js",
      "deps": [
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/service": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/store/iRenderer",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/utils/replaceText",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/combo": {
      "type": "js",
      "deps": [
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/store/iRenderer",
        "node_modules/amis-core/lib/store/manager"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/now": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_root"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/debounce": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isObject",
        "node_modules/lodash/now",
        "node_modules/lodash/toNumber"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/throttle": {
      "type": "js",
      "deps": [
        "node_modules/lodash/debounce",
        "node_modules/lodash/isObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseToPairs": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_setToPairs": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_createToPairs": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseToPairs",
        "node_modules/lodash/_getTag",
        "node_modules/lodash/_mapToArray",
        "node_modules/lodash/_setToPairs"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/toPairs": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createToPairs",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseSet": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_assignValue",
        "node_modules/lodash/_castPath",
        "node_modules/lodash/_isIndex",
        "node_modules/lodash/isObject",
        "node_modules/lodash/_toKey"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_basePickBy": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGet",
        "node_modules/lodash/_baseSet",
        "node_modules/lodash/_castPath"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_basePick": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_basePickBy",
        "node_modules/lodash/hasIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/pick": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_basePick",
        "node_modules/lodash/_flatRest"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/form": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/lodash/debounce",
        "node_modules/lodash/throttle",
        "node_modules/lodash/toPairs",
        "node_modules/lodash/pick",
        "node_modules/amis-core/lib/store/service",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/flatten",
        "node_modules/lodash/find",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/isPureVariable",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/utils/keyToPath"
      ],
      "pkg": "p0"
    },
    "node_modules/file-saver/dist/FileSaver.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/extends": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/remove-accents/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/match-sorter/dist/match-sorter.cjs": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/remove-accents/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/crud": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/file-saver/dist/FileSaver.min",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/store/service",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/lodash/pick",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/match-sorter/dist/match-sorter.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseMap": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseEach",
        "node_modules/lodash/isArrayLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseSortBy": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_compareAscending": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isSymbol"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_compareMultiple": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_compareAscending"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseOrderBy": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseGet",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_baseMap",
        "node_modules/lodash/_baseSortBy",
        "node_modules/lodash/_baseUnary",
        "node_modules/lodash/_compareMultiple",
        "node_modules/lodash/identity",
        "node_modules/lodash/isArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/sortBy": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseFlatten",
        "node_modules/lodash/_baseOrderBy",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_isIterateeCall"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/table": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/store/iRenderer",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/isPureVariable",
        "node_modules/amis-core/lib/utils/resolveVariable",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/find",
        "node_modules/lodash/sortBy",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/store/manager"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/table2": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/lodash/find",
        "node_modules/lodash/isEqual",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/store/service",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/list": {
      "type": "js",
      "deps": [
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/store/iRenderer",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/find",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/modal": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/store/service",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isEqualWith": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIsEqual"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/validations": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-formula/lib/index",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/isPureVariable",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/lodash/memoize"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/normalizeOptions": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/isPlainObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/lodash": {
      "type": "js",
      "deps": [
        "node_modules/lodash/util"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/optionValueCompare": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/formItem": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/lodash/isEqualWith",
        "node_modules/amis-core/lib/store/form",
        "node_modules/amis-core/lib/utils/validations",
        "node_modules/amis-core/lib/store/combo",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/lodash/findIndex",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/lodash/find",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/amis-core/lib/store/node",
        "node_modules/amis-core/lib/store/manager",
        "node_modules/amis-core/lib/utils/normalizeOptions",
        "node_modules/amis-core/lib/utils/optionValueCompare",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/pagination": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/resolveVariable",
        "node_modules/tslib/tslib",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/store/iRenderer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/app": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/store/service",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/root": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/store/service",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/store/index": {
      "type": "js",
      "deps": [
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/store/iRenderer",
        "node_modules/amis-core/lib/store/service",
        "node_modules/amis-core/lib/store/combo",
        "node_modules/amis-core/lib/store/form",
        "node_modules/amis-core/lib/store/crud",
        "node_modules/amis-core/lib/store/table",
        "node_modules/amis-core/lib/store/table2",
        "node_modules/amis-core/lib/store/list",
        "node_modules/amis-core/lib/store/modal",
        "node_modules/lodash/find",
        "node_modules/amis-core/lib/store/node",
        "node_modules/amis-core/lib/store/formItem",
        "node_modules/amis-core/lib/store/manager",
        "node_modules/amis-core/lib/store/pagination",
        "node_modules/amis-core/lib/store/app",
        "node_modules/amis-core/lib/store/root"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/normalizeLink": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/hoist-non-react-statics/node_modules/react-is/cjs/react-is.production.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/hoist-non-react-statics/node_modules/react-is/cjs/react-is.development": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/hoist-non-react-statics/node_modules/react-is/index": {
      "type": "js",
      "deps": [
        "node_modules/hoist-non-react-statics/node_modules/react-is/cjs/react-is.production.min",
        "node_modules/hoist-non-react-statics/node_modules/react-is/cjs/react-is.development"
      ],
      "pkg": "p0"
    },
    "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs": {
      "type": "js",
      "deps": [
        "node_modules/hoist-non-react-statics/node_modules/react-is/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/Scoped": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/find",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/helper"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/filter-schema": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/lodash/isPlainObject",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/WithRootStore": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/WithStore": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/mobx/lib/index",
        "node_modules/mobx-react/dist/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/react/index",
        "node_modules/amis-core/lib/utils/filter-schema",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/WithRootStore"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/renderers/Placeholder": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/factory": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/store/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/utils/normalizeLink",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis-core/lib/Scoped",
        "node_modules/lodash/find",
        "node_modules/amis-core/lib/WithStore",
        "node_modules/amis-core/lib/renderers/Placeholder",
        "node_modules/amis-core/lib/utils/string2regExp"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/assignIn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/_createAssigner",
        "node_modules/lodash/keysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/extend": {
      "type": "js",
      "deps": [
        "node_modules/lodash/assignIn"
      ],
      "pkg": "p0"
    },
    "node_modules/react-visibility-sensor/dist/visibility-sensor": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/components/LazyComponent": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-visibility-sensor/dist/visibility-sensor"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/renderers/Form": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/extend",
        "node_modules/amis-core/lib/factory",
        "node_modules/amis-core/lib/store/form",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/filter-schema",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/lodash/debounce",
        "node_modules/lodash/flatten",
        "node_modules/lodash/find",
        "node_modules/amis-core/lib/Scoped",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/components/LazyComponent",
        "node_modules/mobx-state-tree/dist/mobx-state-tree"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/renderers/builtin": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/renderers/Placeholder",
        "node_modules/amis-core/lib/renderers/Form",
        "node_modules/amis-core/lib/factory"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isString": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isBoolean": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseGetTag",
        "node_modules/lodash/isObjectLike"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/grammar": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/formula": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isObject",
        "node_modules/lodash/isString",
        "node_modules/lodash/isBoolean",
        "node_modules/amis-formula/lib/index",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/moment/moment",
        "node_modules/tslib/tslib",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/grammar"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/renderers/wrapControl": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/debounce",
        "node_modules/lodash/isEqual",
        "node_modules/amis-core/lib/store/combo",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/formula",
        "node_modules/amis-core/lib/Scoped",
        "node_modules/amis-core/lib/store/formItem",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/mobx-react/dist/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-core/lib/WithRootStore",
        "node_modules/amis-core/lib/store/table",
        "node_modules/amis-core/lib/utils/getVariable"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/ColorScale": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/chunk": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseSlice",
        "node_modules/lodash/_isIterateeCall",
        "node_modules/lodash/toInteger"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/DataScope": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/keyToPath"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/DataSchema": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/DataScope",
        "node_modules/amis-core/lib/utils/helper"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/image": {
      "type": "js",
      "deps": [
        "node_modules/lodash/memoize"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/Action": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/omit",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/filter"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/resize-sensor": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/interopRequireDefault": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/dom-helpers/cjs/contains": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/dom-helpers/cjs/canUseDOM": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/dom-helpers/cjs/addEventListener": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/dom-helpers/cjs/canUseDOM"
      ],
      "pkg": "p0"
    },
    "node_modules/dom-helpers/cjs/removeEventListener": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/dom-helpers/cjs/listen": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/dom-helpers/cjs/addEventListener",
        "node_modules/dom-helpers/cjs/removeEventListener"
      ],
      "pkg": "p0"
    },
    "node_modules/@restart/hooks/cjs/useCommittedRef": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/@restart/hooks/cjs/useEventCallback": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/@restart/hooks/cjs/useCommittedRef"
      ],
      "pkg": "p0"
    },
    "node_modules/warning/warning": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/dom-helpers/cjs/ownerDocument": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-overlays/cjs/safeFindDOMNode": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-overlays/cjs/ownerDocument": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/dom-helpers/cjs/ownerDocument",
        "node_modules/react-overlays/cjs/safeFindDOMNode"
      ],
      "pkg": "p0"
    },
    "node_modules/react-overlays/cjs/useRootClose": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/dom-helpers/cjs/contains",
        "node_modules/dom-helpers/cjs/listen",
        "node_modules/react/index",
        "node_modules/@restart/hooks/cjs/useEventCallback",
        "node_modules/warning/warning",
        "node_modules/react-overlays/cjs/ownerDocument"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/mapValues": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseAssignValue",
        "node_modules/lodash/_baseForOwn",
        "node_modules/lodash/_baseIteratee"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_arrayReduce": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_deburrLetter": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_basePropertyOf"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/deburr": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_deburrLetter",
        "node_modules/lodash/toString"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_asciiWords": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_hasUnicodeWord": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/_unicodeWords": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/words": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_asciiWords",
        "node_modules/lodash/_hasUnicodeWord",
        "node_modules/lodash/toString",
        "node_modules/lodash/_unicodeWords"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_createCompounder": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayReduce",
        "node_modules/lodash/deburr",
        "node_modules/lodash/words"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/camelCase": {
      "type": "js",
      "deps": [
        "node_modules/lodash/capitalize",
        "node_modules/lodash/_createCompounder"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/typeof": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/interopRequireWildcard": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/typeof"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/objectWithoutPropertiesLoose": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/invariant/browser": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/uncontrollable/lib/cjs/utils": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/invariant/browser"
      ],
      "pkg": "p0"
    },
    "node_modules/uncontrollable/lib/cjs/hook": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectWithoutPropertiesLoose",
        "node_modules/react/index",
        "node_modules/uncontrollable/lib/cjs/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/setPrototypeOf": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/inheritsLoose": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/setPrototypeOf"
      ],
      "pkg": "p0"
    },
    "node_modules/react-lifecycles-compat/react-lifecycles-compat.cjs": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/uncontrollable/lib/cjs/uncontrollable": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/objectWithoutPropertiesLoose",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/inheritsLoose",
        "node_modules/react/index",
        "node_modules/react-lifecycles-compat/react-lifecycles-compat.cjs",
        "node_modules/invariant/browser",
        "node_modules/uncontrollable/lib/cjs/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/uncontrollable/lib/cjs/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/uncontrollable/lib/cjs/hook",
        "node_modules/uncontrollable/lib/cjs/uncontrollable"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/Animation": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib"
      ],
      "pkg": "p0"
    },
    "node_modules/prop-types/node_modules/react-is/cjs/react-is.production.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/prop-types/node_modules/react-is/cjs/react-is.development": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/prop-types/node_modules/react-is/index": {
      "type": "js",
      "deps": [
        "node_modules/prop-types/node_modules/react-is/cjs/react-is.production.min",
        "node_modules/prop-types/node_modules/react-is/cjs/react-is.development"
      ],
      "pkg": "p0"
    },
    "node_modules/prop-types/factoryWithTypeCheckers": {
      "type": "js",
      "deps": [
        "node_modules/prop-types/node_modules/react-is/index",
        "node_modules/object-assign/index",
        "node_modules/prop-types/lib/ReactPropTypesSecret",
        "node_modules/prop-types/lib/has",
        "node_modules/prop-types/checkPropTypes"
      ],
      "pkg": "p0"
    },
    "node_modules/prop-types/factoryWithThrowingShims": {
      "type": "js",
      "deps": [
        "node_modules/prop-types/lib/ReactPropTypesSecret"
      ],
      "pkg": "p0"
    },
    "node_modules/prop-types/index": {
      "type": "js",
      "deps": [
        "node_modules/prop-types/node_modules/react-is/index",
        "node_modules/prop-types/factoryWithTypeCheckers",
        "node_modules/prop-types/factoryWithThrowingShims"
      ],
      "pkg": "p0"
    },
    "node_modules/react-overlays/cjs/useWaitForDOMRef": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/dom-helpers/cjs/ownerDocument",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-overlays/cjs/Portal": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react-dom/index",
        "node_modules/react/index",
        "node_modules/react-overlays/cjs/useWaitForDOMRef"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/dom": {
      "type": "js",
      "deps": [
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/utils/offset",
        "node_modules/amis-core/lib/utils/position"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/RootClose": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-overlays/cjs/useRootClose",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/components/Overlay": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react-overlays/cjs/Portal",
        "node_modules/classnames/index",
        "node_modules/react-dom/index",
        "node_modules/react/index",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/utils/ColorScale",
        "node_modules/lodash/chunk",
        "node_modules/amis-formula/lib/index",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/DataSchema",
        "node_modules/amis-core/lib/utils/DataScope",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/debug",
        "node_modules/amis-core/lib/utils/dom",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isString",
        "node_modules/lodash/isBoolean",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/utils/image",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/lodash/debounce",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/utils/resize-sensor",
        "node_modules/amis-core/lib/utils/RootClose",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/lodash/mapValues",
        "node_modules/lodash/camelCase",
        "node_modules/uncontrollable/lib/cjs/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-core/lib/utils/validations",
        "node_modules/amis-core/lib/utils/Animation"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/theme": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/classnames/index",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/components/PopOver": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/theme",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/utils/ColorScale",
        "node_modules/lodash/chunk",
        "node_modules/amis-formula/lib/index",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/DataSchema",
        "node_modules/amis-core/lib/utils/DataScope",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/debug",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isString",
        "node_modules/lodash/isBoolean",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/utils/image",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/lodash/debounce",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/utils/resize-sensor",
        "node_modules/react-overlays/cjs/useRootClose",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/lodash/mapValues",
        "node_modules/lodash/camelCase",
        "node_modules/uncontrollable/lib/cjs/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-core/lib/utils/validations",
        "node_modules/amis-core/lib/utils/Animation"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/renderers/Item": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/mobx/lib/index",
        "node_modules/amis-core/lib/factory",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/WithStore",
        "node_modules/amis-core/lib/renderers/wrapControl",
        "node_modules/lodash/debounce",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/utils/ColorScale",
        "node_modules/lodash/chunk",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/DataSchema",
        "node_modules/amis-core/lib/utils/DataScope",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-formula/lib/index",
        "node_modules/amis-core/lib/utils/debug",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/lodash/isPlainObject",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isString",
        "node_modules/lodash/isBoolean",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/utils/image",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/utils/resize-sensor",
        "node_modules/react-overlays/cjs/useRootClose",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/lodash/mapValues",
        "node_modules/lodash/camelCase",
        "node_modules/uncontrollable/lib/cjs/index",
        "node_modules/amis-core/lib/utils/validations",
        "node_modules/amis-core/lib/utils/Animation",
        "node_modules/amis-core/lib/components/Overlay",
        "node_modules/amis-core/lib/components/PopOver"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/renderer-event": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/lodash/debounce"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/renderers/Options": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/mobx/lib/index",
        "node_modules/amis-core/lib/renderers/Item",
        "node_modules/react/index",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/isPureVariable",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/lodash/findIndex",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/normalizeOptions",
        "node_modules/amis-core/lib/utils/optionValueCompare",
        "node_modules/amis-core/lib/utils/ColorScale",
        "node_modules/lodash/chunk",
        "node_modules/amis-core/lib/utils/DataSchema",
        "node_modules/amis-core/lib/utils/DataScope",
        "node_modules/amis-core/lib/utils/debug",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/classnames/index",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isString",
        "node_modules/lodash/isBoolean",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/utils/image",
        "node_modules/amis-core/lib/utils/keyToPath",
        "node_modules/amis-core/lib/utils/renderer-event",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/utils/resize-sensor",
        "node_modules/react-overlays/cjs/useRootClose",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/lodash/mapValues",
        "node_modules/lodash/camelCase",
        "node_modules/uncontrollable/lib/cjs/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-core/lib/utils/validations",
        "node_modules/amis-core/lib/utils/Animation"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/renderers/register": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/factory",
        "node_modules/amis-core/lib/renderers/Options",
        "node_modules/amis-core/lib/renderers/Item"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/attachmentAdpator": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/columnsSplit": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/lodash/chunk"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/handleAction": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/api"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/highlight": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/icon": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/lodash/lodash"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/json-schema-2-amis-schema": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/scrollPosition": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/position"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/style": {
      "type": "js",
      "deps": [
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/tslib/tslib",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/lodash/mapValues",
        "node_modules/lodash/camelCase"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/uncontrollable": {
      "type": "js",
      "deps": [
        "node_modules/uncontrollable/lib/cjs/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/toNumber": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/utils/decodeEntity": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/locale": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/resolveVariable",
        "node_modules/amis-core/lib/utils/filter"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/LoopAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/resolveVariable",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/filter"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/BreakAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/ContinueAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/SwitchAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/ParallelAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/CustomAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/utils/ColorScale",
        "node_modules/react/index",
        "node_modules/lodash/chunk",
        "node_modules/amis-formula/lib/index",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/DataSchema",
        "node_modules/amis-core/lib/utils/DataScope",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/utils/debug",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isString",
        "node_modules/lodash/isBoolean",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/utils/image",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/lodash/debounce",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/utils/resize-sensor",
        "node_modules/react-overlays/cjs/useRootClose",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/lodash/mapValues",
        "node_modules/lodash/camelCase",
        "node_modules/uncontrollable/lib/cjs/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-core/lib/utils/validations",
        "node_modules/amis-core/lib/utils/Animation"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/BroadcastAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/renderer-event",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/CmptAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/AjaxAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/amis-core/lib/utils/object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/CopyAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/DialogAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/DrawerAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/EmailAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/pick",
        "node_modules/amis-core/node_modules/qs/lib/index",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/LinkAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/ToastAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/actions/PageAction": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/actions/Action"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/RootRenderer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-react/dist/index",
        "node_modules/react/index",
        "node_modules/amis-core/lib/Scoped",
        "node_modules/amis-core/lib/store/root",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/node_modules/qs/lib/index",
        "node_modules/lodash/pick",
        "node_modules/lodash/mapValues",
        "node_modules/file-saver/dist/FileSaver.min",
        "node_modules/amis-core/lib/utils/api"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseDifference": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_SetCache",
        "node_modules/lodash/_arrayIncludes",
        "node_modules/lodash/_arrayIncludesWith",
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseUnary",
        "node_modules/lodash/_cacheHas"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/difference": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseDifference",
        "node_modules/lodash/_baseFlatten",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/isArrayLikeObject"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/SchemaRenderer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/difference",
        "node_modules/lodash/omit",
        "node_modules/react/index",
        "node_modules/amis-core/lib/components/LazyComponent",
        "node_modules/amis-core/lib/factory",
        "node_modules/amis-core/lib/renderers/Item",
        "node_modules/amis-core/lib/Scoped",
        "node_modules/amis-core/lib/utils/debug",
        "node_modules/amis-core/lib/utils/filter-schema",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/amis-core/lib/utils/renderer-event",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/mobx/lib/index",
        "node_modules/amis-formula/lib/index",
        "node_modules/moment/moment",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/style"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/Root": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/isPlainObject",
        "node_modules/react/index",
        "node_modules/amis-core/lib/locale",
        "node_modules/amis-core/lib/RootRenderer",
        "node_modules/amis-core/lib/SchemaRenderer",
        "node_modules/amis-core/lib/Scoped",
        "node_modules/amis-core/lib/theme",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/WithRootStore"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/envOverwrite": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/utils/helper"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/env": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-core/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/factory",
        "node_modules/amis-core/lib/renderers/builtin",
        "node_modules/amis-core/lib/renderers/register",
        "node_modules/amis-core/lib/utils/api",
        "node_modules/amis-core/lib/utils/attachmentAdpator",
        "node_modules/amis-core/lib/utils/autobind",
        "node_modules/amis-core/lib/utils/ColorScale",
        "node_modules/amis-core/lib/utils/columnsSplit",
        "node_modules/amis-core/lib/utils/dataMapping",
        "node_modules/amis-core/lib/utils/DataSchema",
        "node_modules/amis-core/lib/utils/DataScope",
        "node_modules/amis-core/lib/utils/date",
        "node_modules/amis-core/lib/utils/debug",
        "node_modules/amis-core/lib/utils/dom",
        "node_modules/amis-core/lib/utils/errors",
        "node_modules/amis-core/lib/utils/escapeHtml",
        "node_modules/amis-core/lib/utils/filter-schema",
        "node_modules/amis-core/lib/utils/filter",
        "node_modules/amis-core/lib/utils/formatDuration",
        "node_modules/amis-core/lib/utils/formula",
        "node_modules/amis-core/lib/utils/getVariable",
        "node_modules/amis-core/lib/utils/grammar",
        "node_modules/amis-core/lib/utils/handleAction",
        "node_modules/amis-core/lib/utils/helper",
        "node_modules/amis-core/lib/utils/highlight",
        "node_modules/amis-core/lib/utils/icon",
        "node_modules/amis-core/lib/utils/image",
        "node_modules/amis-core/lib/utils/isPureVariable",
        "node_modules/amis-core/lib/utils/json-schema-2-amis-schema",
        "node_modules/amis-core/lib/utils/keyToPath",
        "node_modules/amis-core/lib/utils/makeSorter",
        "node_modules/amis-core/lib/utils/normalizeLink",
        "node_modules/amis-core/lib/utils/normalizeOptions",
        "node_modules/amis-core/lib/utils/object",
        "node_modules/amis-core/lib/utils/offset",
        "node_modules/amis-core/lib/utils/offsetParent",
        "node_modules/amis-core/lib/utils/optionValueCompare",
        "node_modules/amis-core/lib/utils/position",
        "node_modules/amis-core/lib/utils/prettyBytes",
        "node_modules/amis-core/lib/utils/renderer-event",
        "node_modules/amis-core/lib/utils/replaceText",
        "node_modules/amis-core/lib/utils/resize-sensor",
        "node_modules/amis-core/lib/utils/resolveVariable",
        "node_modules/amis-core/lib/utils/resolveVariableAndFilter",
        "node_modules/amis-core/lib/utils/RootClose",
        "node_modules/amis-core/lib/utils/scrollPosition",
        "node_modules/amis-core/lib/utils/SimpleMap",
        "node_modules/amis-core/lib/utils/string2regExp",
        "node_modules/amis-core/lib/utils/style",
        "node_modules/amis-core/lib/utils/tokenize",
        "node_modules/amis-core/lib/utils/tpl-builtin",
        "node_modules/amis-core/lib/utils/tpl",
        "node_modules/amis-core/lib/utils/uncontrollable",
        "node_modules/amis-core/lib/utils/validations",
        "node_modules/amis-core/lib/utils/toNumber",
        "node_modules/amis-core/lib/utils/decodeEntity",
        "node_modules/amis-core/lib/utils/Animation",
        "node_modules/amis-core/lib/store/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/locale",
        "node_modules/amis-core/lib/Scoped",
        "node_modules/amis-core/lib/theme",
        "node_modules/amis-core/lib/actions/LoopAction",
        "node_modules/amis-core/lib/actions/BreakAction",
        "node_modules/amis-core/lib/actions/ContinueAction",
        "node_modules/amis-core/lib/actions/SwitchAction",
        "node_modules/amis-core/lib/actions/ParallelAction",
        "node_modules/amis-core/lib/actions/CustomAction",
        "node_modules/amis-core/lib/actions/BroadcastAction",
        "node_modules/amis-core/lib/actions/CmptAction",
        "node_modules/amis-core/lib/actions/AjaxAction",
        "node_modules/amis-core/lib/actions/CopyAction",
        "node_modules/amis-core/lib/actions/DialogAction",
        "node_modules/amis-core/lib/actions/DrawerAction",
        "node_modules/amis-core/lib/actions/EmailAction",
        "node_modules/amis-core/lib/actions/LinkAction",
        "node_modules/amis-core/lib/actions/ToastAction",
        "node_modules/amis-core/lib/actions/PageAction",
        "node_modules/amis-core/lib/actions/Action",
        "node_modules/amis-core/lib/renderers/Item",
        "node_modules/amis-core/lib/renderers/Options",
        "node_modules/amis-core/lib/Root",
        "node_modules/amis-core/lib/envOverwrite",
        "node_modules/amis-core/lib/env",
        "node_modules/react/index",
        "node_modules/amis-formula/lib/index",
        "node_modules/amis-core/lib/components/LazyComponent",
        "node_modules/amis-core/lib/components/Overlay",
        "node_modules/amis-core/lib/components/PopOver",
        "node_modules/amis-core/lib/renderers/Form",
        "node_modules/amis-core/lib/utils/stripNumber",
        "node_modules/amis-core/lib/store/iRenderer",
        "node_modules/amis-core/lib/store/service",
        "node_modules/amis-core/lib/store/form",
        "node_modules/amis-core/lib/store/combo",
        "node_modules/amis-core/lib/store/crud",
        "node_modules/amis-core/lib/store/table",
        "node_modules/amis-core/lib/store/table2",
        "node_modules/amis-core/lib/store/list",
        "node_modules/amis-core/lib/store/modal",
        "node_modules/amis-core/lib/store/formItem",
        "node_modules/amis-core/lib/store/pagination",
        "node_modules/amis-core/lib/store/app",
        "node_modules/amis-core/lib/store/node"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/404": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-transition-group/cjs/config": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-transition-group/cjs/utils/PropTypes": {
      "type": "js",
      "deps": [
        "node_modules/prop-types/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-transition-group/cjs/TransitionGroupContext": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-transition-group/cjs/Transition": {
      "type": "js",
      "deps": [
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/react-transition-group/cjs/config",
        "node_modules/react-transition-group/cjs/utils/PropTypes",
        "node_modules/react-transition-group/cjs/TransitionGroupContext"
      ],
      "pkg": "p0"
    },
    "node_modules/keycode/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ModalManager": {
      "type": "js",
      "deps": [
        "node_modules/keycode/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/close.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/close-small.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/status-close.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/undo.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/redo.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/enter.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/volume.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/mute.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/play.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/pause.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/left-arrow.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/right-arrow.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/check.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/plus.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/minus.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/pencil.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/view.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/remove.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/retry.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/upload.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/download.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/file.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/status-success.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/status-fail.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/status-info.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/status-warning.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/success.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/fail.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/search.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/back.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/move.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/info.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/location.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/drag-bar.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/reload.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/exchange.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/columns.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/calendar.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/clock.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/tree-down.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/check-circle.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/cloud-upload.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/image.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/refresh.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/drag.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/edit.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/desk-empty.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/copy.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/filter.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/caret.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/right-arrow-bold.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/down-arrow-bold.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/column-filter.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/zoom-in.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/zoom-out.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/question.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/question-mark.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/window-restore.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/info-circle.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/warning.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/warning-mark.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/schedule.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/home.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/folder.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/sort-default.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/sort-asc.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/sort-desc.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/setting.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/plus-cicle.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/plus-fine.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/ellipsis-v.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/expand-alt.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/compress-alt.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/transparent.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/loading-outline.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/star.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/alert-success.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/alert-info.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/alert-warning.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/alert-danger.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/function.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/input-clear.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/slider-handle-icon.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/trash.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/menu.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/user-remove.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/role.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/department.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/post.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/dot.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/steps-finsh.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/invisible.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/date.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/down.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/right-double-arrow.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/new-edit.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/rotate-left.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/rotate-right.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/icons/scale-origin.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/icons": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/icons/close.svg",
        "node_modules/amis-ui/lib/icons/close-small.svg",
        "node_modules/amis-ui/lib/icons/status-close.svg",
        "node_modules/amis-ui/lib/icons/undo.svg",
        "node_modules/amis-ui/lib/icons/redo.svg",
        "node_modules/amis-ui/lib/icons/enter.svg",
        "node_modules/amis-ui/lib/icons/volume.svg",
        "node_modules/amis-ui/lib/icons/mute.svg",
        "node_modules/amis-ui/lib/icons/play.svg",
        "node_modules/amis-ui/lib/icons/pause.svg",
        "node_modules/amis-ui/lib/icons/left-arrow.svg",
        "node_modules/amis-ui/lib/icons/right-arrow.svg",
        "node_modules/amis-ui/lib/icons/check.svg",
        "node_modules/amis-ui/lib/icons/plus.svg",
        "node_modules/amis-ui/lib/icons/minus.svg",
        "node_modules/amis-ui/lib/icons/pencil.svg",
        "node_modules/amis-ui/lib/icons/view.svg",
        "node_modules/amis-ui/lib/icons/remove.svg",
        "node_modules/amis-ui/lib/icons/retry.svg",
        "node_modules/amis-ui/lib/icons/upload.svg",
        "node_modules/amis-ui/lib/icons/download.svg",
        "node_modules/amis-ui/lib/icons/file.svg",
        "node_modules/amis-ui/lib/icons/status-success.svg",
        "node_modules/amis-ui/lib/icons/status-fail.svg",
        "node_modules/amis-ui/lib/icons/status-info.svg",
        "node_modules/amis-ui/lib/icons/status-warning.svg",
        "node_modules/amis-ui/lib/icons/success.svg",
        "node_modules/amis-ui/lib/icons/fail.svg",
        "node_modules/amis-ui/lib/icons/search.svg",
        "node_modules/amis-ui/lib/icons/back.svg",
        "node_modules/amis-ui/lib/icons/move.svg",
        "node_modules/amis-ui/lib/icons/info.svg",
        "node_modules/amis-ui/lib/icons/location.svg",
        "node_modules/amis-ui/lib/icons/drag-bar.svg",
        "node_modules/amis-ui/lib/icons/reload.svg",
        "node_modules/amis-ui/lib/icons/exchange.svg",
        "node_modules/amis-ui/lib/icons/columns.svg",
        "node_modules/amis-ui/lib/icons/calendar.svg",
        "node_modules/amis-ui/lib/icons/clock.svg",
        "node_modules/amis-ui/lib/icons/tree-down.svg",
        "node_modules/amis-ui/lib/icons/check-circle.svg",
        "node_modules/amis-ui/lib/icons/cloud-upload.svg",
        "node_modules/amis-ui/lib/icons/image.svg",
        "node_modules/amis-ui/lib/icons/refresh.svg",
        "node_modules/amis-ui/lib/icons/drag.svg",
        "node_modules/amis-ui/lib/icons/edit.svg",
        "node_modules/amis-ui/lib/icons/desk-empty.svg",
        "node_modules/amis-ui/lib/icons/copy.svg",
        "node_modules/amis-ui/lib/icons/filter.svg",
        "node_modules/amis-ui/lib/icons/caret.svg",
        "node_modules/amis-ui/lib/icons/right-arrow-bold.svg",
        "node_modules/amis-ui/lib/icons/down-arrow-bold.svg",
        "node_modules/amis-ui/lib/icons/column-filter.svg",
        "node_modules/amis-ui/lib/icons/zoom-in.svg",
        "node_modules/amis-ui/lib/icons/zoom-out.svg",
        "node_modules/amis-ui/lib/icons/question.svg",
        "node_modules/amis-ui/lib/icons/question-mark.svg",
        "node_modules/amis-ui/lib/icons/window-restore.svg",
        "node_modules/amis-ui/lib/icons/info-circle.svg",
        "node_modules/amis-ui/lib/icons/warning.svg",
        "node_modules/amis-ui/lib/icons/warning-mark.svg",
        "node_modules/amis-ui/lib/icons/schedule.svg",
        "node_modules/amis-ui/lib/icons/home.svg",
        "node_modules/amis-ui/lib/icons/folder.svg",
        "node_modules/amis-ui/lib/icons/sort-default.svg",
        "node_modules/amis-ui/lib/icons/sort-asc.svg",
        "node_modules/amis-ui/lib/icons/sort-desc.svg",
        "node_modules/amis-ui/lib/icons/setting.svg",
        "node_modules/amis-ui/lib/icons/plus-cicle.svg",
        "node_modules/amis-ui/lib/icons/plus-fine.svg",
        "node_modules/amis-ui/lib/icons/ellipsis-v.svg",
        "node_modules/amis-ui/lib/icons/expand-alt.svg",
        "node_modules/amis-ui/lib/icons/compress-alt.svg",
        "node_modules/amis-ui/lib/icons/transparent.svg",
        "node_modules/amis-ui/lib/icons/loading-outline.svg",
        "node_modules/amis-ui/lib/icons/star.svg",
        "node_modules/amis-ui/lib/icons/alert-success.svg",
        "node_modules/amis-ui/lib/icons/alert-info.svg",
        "node_modules/amis-ui/lib/icons/alert-warning.svg",
        "node_modules/amis-ui/lib/icons/alert-danger.svg",
        "node_modules/amis-ui/lib/icons/function.svg",
        "node_modules/amis-ui/lib/icons/input-clear.svg",
        "node_modules/amis-ui/lib/icons/slider-handle-icon.svg",
        "node_modules/amis-ui/lib/icons/trash.svg",
        "node_modules/amis-ui/lib/icons/menu.svg",
        "node_modules/amis-ui/lib/icons/user-remove.svg",
        "node_modules/amis-ui/lib/icons/role.svg",
        "node_modules/amis-ui/lib/icons/department.svg",
        "node_modules/amis-ui/lib/icons/post.svg",
        "node_modules/amis-ui/lib/icons/dot.svg",
        "node_modules/amis-ui/lib/icons/steps-finsh.svg",
        "node_modules/amis-ui/lib/icons/invisible.svg",
        "node_modules/amis-ui/lib/icons/date.svg",
        "node_modules/amis-ui/lib/icons/down.svg",
        "node_modules/amis-ui/lib/icons/right-double-arrow.svg",
        "node_modules/amis-ui/lib/icons/new-edit.svg",
        "node_modules/amis-ui/lib/icons/rotate-left.svg",
        "node_modules/amis-ui/lib/icons/rotate-right.svg",
        "node_modules/amis-ui/lib/icons/scale-origin.svg"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Modal": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-transition-group/cjs/Transition",
        "node_modules/react-overlays/cjs/Portal",
        "node_modules/amis-ui/lib/components/ModalManager",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Html": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Tooltip": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TooltipWrapper": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Html",
        "node_modules/amis-core/lib/index",
        "node_modules/react-dom/index",
        "node_modules/amis-ui/lib/components/Tooltip"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Spinner": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-transition-group/cjs/Transition",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/mobx/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Button": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/TooltipWrapper",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Spinner"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Alert": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-ui/lib/components/Modal",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Html"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ContextMenu": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/react-transition-group/cjs/Transition"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/AsideNav": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Avatar": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Breadcrumb": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/TooltipWrapper",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Checkbox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Selection": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isEqual",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Checkbox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Collapse": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-transition-group/cjs/Transition"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/CollapseGroup": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/isEqual"
      ],
      "pkg": "p0"
    },
    "node_modules/moment/locale/zh-cn": {
      "type": "js",
      "deps": [
        "node_modules/moment/moment"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/PopUp": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-transition-group/cjs/Transition",
        "node_modules/react-overlays/cjs/Portal",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Button"
      ],
      "pkg": "p0"
    },
    "node_modules/react-is/cjs/react-is.production.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-is/cjs/react-is.development": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-is/index": {
      "type": "js",
      "deps": [
        "node_modules/react-is/cjs/react-is.production.min",
        "node_modules/react-is/cjs/react-is.development"
      ],
      "pkg": "p0"
    },
    "node_modules/compute-scroll-into-view/dist/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/downshift/dist/downshift.cjs": {
      "type": "js",
      "deps": [
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/react-is/index",
        "node_modules/compute-scroll-into-view/dist/index",
        "node_modules/tslib/tslib"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/forEach": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayEach",
        "node_modules/lodash/_baseEach",
        "node_modules/lodash/_castFunction",
        "node_modules/lodash/isArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/each": {
      "type": "js",
      "deps": [
        "node_modules/lodash/forEach"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/kebabCase": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_createCompounder"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/hooks/use-set-state": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isFunction"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/hooks/use-update-effect": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/hooks/use-touch": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/PickerColumn": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isObject",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/hooks/use-set-state",
        "node_modules/amis-ui/lib/hooks/use-update-effect",
        "node_modules/amis-ui/lib/hooks/use-touch",
        "node_modules/lodash/pick"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Picker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/PickerColumn"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/calendar/DaysView": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/moment/moment",
        "node_modules/react/index",
        "node_modules/downshift/dist/downshift.cjs",
        "node_modules/lodash/findIndex",
        "node_modules/lodash/extend",
        "node_modules/lodash/merge",
        "node_modules/lodash/omit",
        "node_modules/lodash/each",
        "node_modules/lodash/kebabCase",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Picker",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/calendar/YearsView": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/moment/moment",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Picker"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/calendar/MonthsView": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/moment/moment",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Picker"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/calendar/TimeView": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/moment/moment",
        "node_modules/react/index",
        "node_modules/lodash/merge",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Picker",
        "node_modules/downshift/dist/downshift.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/calendar/QuartersView": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/moment/moment",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/calendar/CalendarContainer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/calendar/DaysView",
        "node_modules/amis-ui/lib/components/calendar/YearsView",
        "node_modules/amis-ui/lib/components/calendar/MonthsView",
        "node_modules/amis-ui/lib/components/calendar/TimeView",
        "node_modules/amis-ui/lib/components/calendar/QuartersView"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/calendar/Calendar": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/calendar/CalendarContainer",
        "node_modules/classnames/index",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/CalendarMobile": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/moment/moment",
        "node_modules/amis-ui/lib/components/calendar/Calendar",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Input": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/DatePicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/moment/moment",
        "node_modules/moment/locale/zh-cn",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/PopUp",
        "node_modules/amis-ui/lib/components/calendar/Calendar",
        "node_modules/amis-ui/lib/components/CalendarMobile",
        "node_modules/amis-ui/lib/components/Input"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/DateRangePicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/moment/moment",
        "node_modules/lodash/omit",
        "node_modules/lodash/kebabCase",
        "node_modules/react-dom/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/calendar/Calendar",
        "node_modules/amis-ui/lib/components/PopUp",
        "node_modules/amis-ui/lib/components/CalendarMobile",
        "node_modules/amis-ui/lib/components/Input"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Drawer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-transition-group/cjs/Transition",
        "node_modules/react-overlays/cjs/Portal",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/components/ModalManager",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/PopOverContainer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/PopUp",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/sortablejs/Sortable.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Tabs": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-transition-group/cjs/Transition",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/lodash/debounce",
        "node_modules/react-dom/index",
        "node_modules/amis-ui/lib/components/TooltipWrapper",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/sortablejs/Sortable.min"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Editor": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Layout": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isInteger": {
      "type": "js",
      "deps": [
        "node_modules/lodash/toInteger"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/virtual-list/constants": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/virtual-list/SizeAndPositionManager": {
      "type": "js",
      "deps": [
        "node_modules/amis-ui/lib/components/virtual-list/constants"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/virtual-list/AutoSizer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/virtual-list/index": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/amis-ui/lib/components/virtual-list/SizeAndPositionManager",
        "node_modules/amis-ui/lib/components/virtual-list/constants",
        "node_modules/amis-ui/lib/components/virtual-list/AutoSizer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/WithStore": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/mobx-react/dist/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/WithRemoteConfig": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/lodash/debounce",
        "node_modules/amis-ui/lib/components/WithStore",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/index",
        "node_modules/mobx/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Select": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/lodash/isInteger",
        "node_modules/lodash/omit",
        "node_modules/lodash/merge",
        "node_modules/amis-ui/lib/components/virtual-list/index",
        "node_modules/amis-ui/lib/components/TooltipWrapper",
        "node_modules/downshift/dist/downshift.cjs",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/match-sorter/dist/match-sorter.cjs",
        "node_modules/lodash/isPlainObject",
        "node_modules/react-dom/index",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Input",
        "node_modules/amis-ui/lib/components/Spinner",
        "node_modules/amis-ui/lib/components/WithRemoteConfig",
        "node_modules/amis-ui/lib/components/PopUp"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Radios": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/Select"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Range": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/range",
        "node_modules/lodash/keys",
        "node_modules/lodash/isString",
        "node_modules/lodash/difference",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-dom/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Rating": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/SparkLine": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Switch": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/use-isomorphic-layout-effect/dist/use-isomorphic-layout-effect.browser.cjs": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/use-latest/dist/use-latest.cjs.prod": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/use-isomorphic-layout-effect/dist/use-isomorphic-layout-effect.browser.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/use-latest/dist/use-latest.cjs.dev": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/use-isomorphic-layout-effect/dist/use-isomorphic-layout-effect.browser.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/use-latest/dist/use-latest.cjs": {
      "type": "js",
      "deps": [
        "node_modules/use-latest/dist/use-latest.cjs.prod",
        "node_modules/use-latest/dist/use-latest.cjs.dev"
      ],
      "pkg": "p0"
    },
    "node_modules/use-composed-ref/dist/use-composed-ref.cjs": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-textarea-autosize/dist/react-textarea-autosize.browser.cjs": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectWithoutPropertiesLoose",
        "node_modules/react/index",
        "node_modules/use-latest/dist/use-latest.cjs",
        "node_modules/use-composed-ref/dist/use-composed-ref.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Textarea": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/react-textarea-autosize/dist/react-textarea-autosize.browser.cjs",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TitleBar": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Toast": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react-transition-group/cjs/Transition",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Html",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/lodash/groupBy"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Tree": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Spinner",
        "node_modules/amis-ui/lib/components/virtual-list/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Alert2": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/InputBox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Input",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TransferSearch": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/InputBox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ResultList": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/react-dom/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/TransferSearch",
        "node_modules/amis-ui/lib/components/virtual-list/index",
        "node_modules/amis-ui/lib/components/virtual-list/AutoSizer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TableSelection": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/virtual-list/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-ui/lib/components/virtual-list/AutoSizer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/GroupedSelection": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/virtual-list/index",
        "node_modules/amis-ui/lib/components/virtual-list/AutoSizer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ChainedSelection": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/lodash/times",
        "node_modules/amis-ui/lib/components/Spinner",
        "node_modules/amis-ui/lib/components/virtual-list/index",
        "node_modules/amis-ui/lib/components/virtual-list/AutoSizer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/AssociatedSelection": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/TableSelection",
        "node_modules/amis-ui/lib/components/ChainedSelection",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Tree"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ResultTableList": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/TransferSearch",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/TableSelection",
        "node_modules/amis-ui/lib/icons/close.svg"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ResultTreeList": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Tree",
        "node_modules/amis-ui/lib/components/TransferSearch"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Transfer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-ui/lib/components/ResultList",
        "node_modules/amis-ui/lib/components/TableSelection",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Tree",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/AssociatedSelection",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/ChainedSelection",
        "node_modules/amis-ui/lib/components/ResultTableList",
        "node_modules/amis-ui/lib/components/ResultTreeList"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TabsTransfer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Tabs",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/TableSelection",
        "node_modules/amis-ui/lib/components/Tree",
        "node_modules/amis-ui/lib/components/ChainedSelection",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/Transfer",
        "node_modules/amis-ui/lib/components/AssociatedSelection",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/lodash/debounce"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ResultBox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/lodash/omit",
        "node_modules/lodash/isInteger",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Input",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-ui/lib/components/TooltipWrapper"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ListGroup": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/toPrimitive": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/typeof"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/toPropertyKey": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/toPrimitive"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/defineProperty": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/toPropertyKey"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/arrayWithHoles": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/iterableToArrayLimit": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/arrayLikeToArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/unsupportedIterableToArray": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/arrayLikeToArray"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/nonIterableRest": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/slicedToArray": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/arrayWithHoles",
        "node_modules/@babel/runtime/helpers/iterableToArrayLimit",
        "node_modules/@babel/runtime/helpers/unsupportedIterableToArray",
        "node_modules/@babel/runtime/helpers/nonIterableRest"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/objectWithoutProperties": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/objectWithoutPropertiesLoose"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/KeyCode": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/Dom/canUseDom": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/hooks/useLayoutEffect": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/react/index",
        "node_modules/rc-util/lib/Dom/canUseDom"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/node_modules/react-is/cjs/react-is.production.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/rc-util/node_modules/react-is/cjs/react-is.development": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/rc-util/node_modules/react-is/index": {
      "type": "js",
      "deps": [
        "node_modules/rc-util/node_modules/react-is/cjs/react-is.production.min",
        "node_modules/rc-util/node_modules/react-is/cjs/react-is.development"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/hooks/useMemo": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/ref": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/rc-util/node_modules/react-is/index",
        "node_modules/rc-util/lib/hooks/useMemo"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/classCallCheck": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/createClass": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/toPropertyKey"
      ],
      "pkg": "p0"
    },
    "node_modules/@rc-component/mini-decimal/lib/supportUtil": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@rc-component/mini-decimal/lib/numberUtil": {
      "type": "js",
      "deps": [
        "node_modules/@rc-component/mini-decimal/lib/supportUtil"
      ],
      "pkg": "p0"
    },
    "node_modules/@rc-component/mini-decimal/lib/MiniDecimal": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/defineProperty",
        "node_modules/@rc-component/mini-decimal/lib/numberUtil",
        "node_modules/@rc-component/mini-decimal/lib/supportUtil"
      ],
      "pkg": "p0"
    },
    "node_modules/@rc-component/mini-decimal/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@rc-component/mini-decimal/lib/MiniDecimal",
        "node_modules/@rc-component/mini-decimal/lib/numberUtil"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/isMobile": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/hooks/useMobile": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/slicedToArray",
        "node_modules/react/index",
        "node_modules/rc-util/lib/isMobile"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-input-number/lib/StepHandler": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/defineProperty",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/rc-util/lib/hooks/useMobile"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-input-number/lib/utils/numberUtil": {
      "type": "js",
      "deps": [
        "node_modules/@rc-component/mini-decimal/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/warning": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/rc-input-number/lib/hooks/useCursor": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/react/index",
        "node_modules/rc-util/lib/warning"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/raf": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/rc-input-number/lib/hooks/useFrame": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/react/index",
        "node_modules/rc-util/lib/raf"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-input-number/lib/InputNumber": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/defineProperty",
        "node_modules/@babel/runtime/helpers/slicedToArray",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/rc-util/lib/KeyCode",
        "node_modules/rc-util/lib/hooks/useLayoutEffect",
        "node_modules/rc-util/lib/ref",
        "node_modules/@rc-component/mini-decimal/lib/index",
        "node_modules/rc-input-number/lib/StepHandler",
        "node_modules/rc-input-number/lib/utils/numberUtil",
        "node_modules/rc-input-number/lib/hooks/useCursor",
        "node_modules/rc-input-number/lib/hooks/useFrame"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-input-number/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/rc-input-number/lib/InputNumber"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/NumberInput": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isInteger",
        "node_modules/rc-input-number/lib/index",
        "node_modules/@rc-component/mini-decimal/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ArrayInput": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/SearchBox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/lodash",
        "node_modules/lodash/isInteger",
        "node_modules/lodash/debounce",
        "node_modules/moment/moment",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/AnchorNav": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/find"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TreeSelection": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Spinner",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/PullRefresh": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/hooks/use-set-state",
        "node_modules/amis-ui/lib/hooks/use-touch",
        "node_modules/tslib/tslib",
        "node_modules/lodash/pick",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/findLastIndex": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseFindIndex",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/toInteger"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseFilter": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseEach"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/filter": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayFilter",
        "node_modules/lodash/_baseFilter",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/isArray"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseIntersection": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_SetCache",
        "node_modules/lodash/_arrayIncludes",
        "node_modules/lodash/_arrayIncludesWith",
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseUnary",
        "node_modules/lodash/_cacheHas"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_castArrayLikeObject": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isArrayLikeObject"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/intersection": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseIntersection",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_castArrayLikeObject"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/table/HeadCellSort": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/table/HeadCellDropDown": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/table/HeadCellFilter": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/lodash/isEqual",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/table/HeadCellDropDown",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/table/HeadCellSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/table/HeadCellDropDown",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/table/ItemActionsWrapper": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/table/Cell": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/table/index": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/lodash/findLastIndex",
        "node_modules/lodash/find",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/filter",
        "node_modules/lodash/intersection",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Spinner",
        "node_modules/amis-ui/lib/components/table/HeadCellSort",
        "node_modules/amis-ui/lib/components/table/HeadCellFilter",
        "node_modules/amis-ui/lib/components/table/HeadCellSelect",
        "node_modules/amis-ui/lib/components/table/ItemActionsWrapper",
        "node_modules/amis-ui/lib/components/table/Cell"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ConfirmBox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Modal",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/Drawer",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Spinner"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/PickerContainer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/ConfirmBox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Badge": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/formula/VariableList": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/Tabs",
        "node_modules/amis-ui/lib/components/TreeSelection",
        "node_modules/amis-ui/lib/components/SearchBox",
        "node_modules/amis-ui/lib/components/Badge"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableList": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/formula/VariableList",
        "node_modules/amis-ui/lib/components/TooltipWrapper"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableListPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/PickerContainer",
        "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableList"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-formula/lib/doc": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/formula/plugin": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/formula/Editor"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/formula/FuncList": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Collapse",
        "node_modules/amis-ui/lib/components/CollapseGroup",
        "node_modules/amis-ui/lib/components/SearchBox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/CodeMirror": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/formula/Editor": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-formula/lib/doc",
        "node_modules/amis-ui/lib/components/formula/plugin",
        "node_modules/amis-ui/lib/components/formula/FuncList",
        "node_modules/amis-ui/lib/components/formula/VariableList",
        "node_modules/amis-ui/lib/components/CodeMirror",
        "node_modules/amis-ui/lib/components/Toast"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/formula/Picker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/formula/Editor",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Modal"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/json-schema/Array": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/json-schema/Item"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/InputBoxWithSuggestion": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/index",
        "node_modules/match-sorter/dist/match-sorter.cjs",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/amis-ui/lib/components/SearchBox",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/json-schema/Object": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/InputBoxWithSuggestion",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-ui/lib/components/json-schema/Item"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/json-schema/Item": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/NumberInput",
        "node_modules/amis-ui/lib/components/Switch",
        "node_modules/amis-ui/lib/components/json-schema/Array",
        "node_modules/amis-ui/lib/components/json-schema/Object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/json-schema/index": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/json-schema/Item"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/Field": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/SearchBox",
        "node_modules/amis-ui/lib/components/TreeSelection"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/Value": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/NumberInput",
        "node_modules/amis-ui/lib/components/DatePicker",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-ui/lib/components/Switch",
        "node_modules/amis-ui/lib/components/formula/Picker"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/InputSwitch": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/Func": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/condition-builder/Expression"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/Formula": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/InputBox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/Expression": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/condition-builder/Field",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/condition-builder/Value",
        "node_modules/amis-ui/lib/components/condition-builder/InputSwitch",
        "node_modules/amis-ui/lib/components/condition-builder/Func",
        "node_modules/amis-ui/lib/components/condition-builder/Formula"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/config": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/Item": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/condition-builder/Expression",
        "node_modules/amis-ui/lib/components/condition-builder/config",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/ResultBox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/GroupOrItem": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/condition-builder/Group",
        "node_modules/amis-ui/lib/components/condition-builder/Item",
        "node_modules/amis-ui/lib/components/Button"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/Group": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/condition-builder/GroupOrItem",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/icons/down-arrow-bold.svg"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/condition-builder/index": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/condition-builder/Group",
        "node_modules/amis-ui/lib/components/condition-builder/config",
        "node_modules/amis-ui/lib/components/PickerContainer",
        "node_modules/amis-ui/lib/components/ResultBox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/CityArea": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Picker",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/hooks/use-set-state",
        "node_modules/amis-ui/lib/hooks/use-update-effect",
        "node_modules/lodash/pick",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/PopUp"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ListMenu": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/node_modules/react-hook-form/dist/index.cjs": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/hooks/use-validation-resolver": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/pick",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Form": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/node_modules/react-hook-form/dist/index.cjs",
        "node_modules/amis-ui/lib/hooks/use-validation-resolver",
        "node_modules/lodash/debounce"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/FormField": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/node_modules/react-hook-form/dist/index.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/schema-editor/Common": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Form",
        "node_modules/amis-ui/lib/components/FormField",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/PickerContainer",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-ui/lib/components/Textarea"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/schema-editor/Array": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/schema-editor/Common",
        "node_modules/amis-ui/lib/components/schema-editor/Item"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/schema-editor/Object": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/schema-editor/Common",
        "node_modules/amis-ui/lib/components/schema-editor/Item"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/schema-editor/Item": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/schema-editor/Array",
        "node_modules/amis-ui/lib/components/schema-editor/Common",
        "node_modules/amis-ui/lib/components/schema-editor/Object"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/schema-editor/index": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/schema-editor/Item"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/BaiduMapPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/debounce",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/LocationPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Alert2",
        "node_modules/amis-ui/lib/components/BaiduMapPicker"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/intersectionBy": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseIntersection",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_castArrayLikeObject",
        "node_modules/lodash/last"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/compact": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Cascader": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/intersectionBy",
        "node_modules/lodash/compact",
        "node_modules/lodash/find",
        "node_modules/lodash/uniqBy",
        "node_modules/amis-ui/lib/components/Button"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TransferDropDown": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Transfer",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/PopOverContainer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TabsTransferPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/PickerContainer",
        "node_modules/amis-ui/lib/components/TabsTransfer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TransferPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Transfer",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/PickerContainer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Card": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/GridNav": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/components/Badge"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Link": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Pagination": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isInteger",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Select"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-progress/lib/common": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-progress/lib/Line": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/rc-progress/lib/common"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-progress/lib/hooks/useId": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/slicedToArray",
        "node_modules/react/index",
        "node_modules/rc-util/lib/Dom/canUseDom"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-progress/lib/Circle": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/rc-progress/lib/common",
        "node_modules/rc-progress/lib/hooks/useId"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-progress/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/rc-progress/lib/Line",
        "node_modules/rc-progress/lib/Circle"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Progress": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/rc-progress/lib/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Steps": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Tag": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/TimelineItem": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Timeline": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/TimelineItem"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ImageGallery": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/debounce",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Modal",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/MultilineText": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Button"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/hooks/use-sub-form": {
      "type": "js",
      "deps": [
        "node_modules/amis-ui/node_modules/react-hook-form/dist/index.cjs",
        "node_modules/lodash/debounce",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/hooks/use-validation-resolver"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Combo": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/node_modules/react-hook-form/dist/index.cjs",
        "node_modules/amis-ui/lib/hooks/use-sub-form",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/FormField",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/InputTable": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/node_modules/react-hook-form/dist/index.cjs",
        "node_modules/amis-ui/lib/hooks/use-sub-form",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/FormField",
        "node_modules/amis-ui/lib/components/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/UserTabSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/404",
        "node_modules/amis-ui/lib/components/Alert",
        "node_modules/amis-ui/lib/components/ContextMenu",
        "node_modules/amis-ui/lib/components/AsideNav",
        "node_modules/amis-ui/lib/components/Avatar",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/Breadcrumb",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-ui/lib/components/Collapse",
        "node_modules/amis-ui/lib/components/CollapseGroup",
        "node_modules/amis-ui/lib/components/DatePicker",
        "node_modules/amis-ui/lib/components/DateRangePicker",
        "node_modules/amis-ui/lib/components/Drawer",
        "node_modules/amis-ui/lib/components/Tabs",
        "node_modules/amis-ui/lib/components/Editor",
        "node_modules/amis-ui/lib/components/Html",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Layout",
        "node_modules/amis-ui/lib/components/Modal",
        "node_modules/amis-ui/lib/components/Radios",
        "node_modules/amis-ui/lib/components/Range",
        "node_modules/amis-ui/lib/components/Rating",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-ui/lib/components/SparkLine",
        "node_modules/amis-ui/lib/components/Spinner",
        "node_modules/amis-ui/lib/components/Switch",
        "node_modules/amis-ui/lib/components/Textarea",
        "node_modules/amis-ui/lib/components/TitleBar",
        "node_modules/amis-ui/lib/components/Toast",
        "node_modules/amis-ui/lib/components/Tooltip",
        "node_modules/amis-ui/lib/components/TooltipWrapper",
        "node_modules/amis-ui/lib/components/Tree",
        "node_modules/amis-ui/lib/components/Alert2",
        "node_modules/amis-ui/lib/components/Transfer",
        "node_modules/amis-ui/lib/components/TabsTransfer",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/ListGroup",
        "node_modules/amis-ui/lib/components/NumberInput",
        "node_modules/amis-ui/lib/components/ArrayInput",
        "node_modules/amis-ui/lib/components/SearchBox",
        "node_modules/amis-ui/lib/components/AnchorNav",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/ChainedSelection",
        "node_modules/amis-ui/lib/components/TableSelection",
        "node_modules/amis-ui/lib/components/TreeSelection",
        "node_modules/amis-ui/lib/components/AssociatedSelection",
        "node_modules/amis-ui/lib/components/PullRefresh",
        "node_modules/amis-ui/lib/components/table/index",
        "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableListPicker",
        "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableList",
        "node_modules/amis-ui/lib/components/formula/VariableList",
        "node_modules/amis-ui/lib/components/formula/Picker",
        "node_modules/amis-ui/lib/components/PickerContainer",
        "node_modules/amis-ui/lib/components/json-schema/index",
        "node_modules/amis-ui/lib/components/Badge",
        "node_modules/amis-ui/lib/components/WithRemoteConfig",
        "node_modules/amis-ui/lib/components/condition-builder/index",
        "node_modules/amis-ui/lib/components/CityArea",
        "node_modules/amis-ui/lib/components/ListMenu",
        "node_modules/amis-ui/lib/components/Input",
        "node_modules/amis-ui/lib/components/schema-editor/index",
        "node_modules/amis-ui/lib/components/LocationPicker",
        "node_modules/amis-ui/lib/components/PopUp",
        "node_modules/amis-ui/lib/components/Cascader",
        "node_modules/amis-ui/lib/components/TransferDropDown",
        "node_modules/amis-ui/lib/components/TabsTransferPicker",
        "node_modules/amis-ui/lib/components/ResultList",
        "node_modules/amis-ui/lib/components/TransferPicker",
        "node_modules/amis-ui/lib/components/UserSelect",
        "node_modules/amis-ui/lib/components/table/HeadCellDropDown",
        "node_modules/amis-ui/lib/components/Card",
        "node_modules/amis-ui/lib/components/GridNav",
        "node_modules/amis-ui/lib/components/Link",
        "node_modules/amis-ui/lib/components/virtual-list/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/amis-ui/lib/components/Pagination",
        "node_modules/amis-ui/lib/components/Progress",
        "node_modules/amis-ui/lib/components/Steps",
        "node_modules/amis-ui/lib/components/Tag",
        "node_modules/amis-ui/lib/components/Timeline",
        "node_modules/amis-ui/lib/components/ImageGallery",
        "node_modules/amis-ui/lib/components/BaiduMapPicker",
        "node_modules/amis-ui/lib/components/MultilineText",
        "node_modules/amis-ui/lib/components/Form",
        "node_modules/amis-ui/lib/components/FormField",
        "node_modules/amis-ui/lib/components/Combo",
        "node_modules/amis-ui/lib/components/InputTable",
        "node_modules/amis-ui/lib/components/ConfirmBox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/UserSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/404",
        "node_modules/amis-ui/lib/components/Alert",
        "node_modules/amis-ui/lib/components/ContextMenu",
        "node_modules/amis-ui/lib/components/AsideNav",
        "node_modules/amis-ui/lib/components/Avatar",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/Breadcrumb",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-ui/lib/components/Collapse",
        "node_modules/amis-ui/lib/components/CollapseGroup",
        "node_modules/amis-ui/lib/components/DatePicker",
        "node_modules/amis-ui/lib/components/DateRangePicker",
        "node_modules/amis-ui/lib/components/Drawer",
        "node_modules/amis-ui/lib/components/Tabs",
        "node_modules/amis-ui/lib/components/Editor",
        "node_modules/amis-ui/lib/components/Html",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Layout",
        "node_modules/amis-ui/lib/components/Modal",
        "node_modules/amis-ui/lib/components/Radios",
        "node_modules/amis-ui/lib/components/Range",
        "node_modules/amis-ui/lib/components/Rating",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-ui/lib/components/SparkLine",
        "node_modules/amis-ui/lib/components/Spinner",
        "node_modules/amis-ui/lib/components/Switch",
        "node_modules/amis-ui/lib/components/Textarea",
        "node_modules/amis-ui/lib/components/TitleBar",
        "node_modules/amis-ui/lib/components/Toast",
        "node_modules/amis-ui/lib/components/Tooltip",
        "node_modules/amis-ui/lib/components/TooltipWrapper",
        "node_modules/amis-ui/lib/components/Tree",
        "node_modules/amis-ui/lib/components/Alert2",
        "node_modules/amis-ui/lib/components/Transfer",
        "node_modules/amis-ui/lib/components/TabsTransfer",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/ListGroup",
        "node_modules/amis-ui/lib/components/NumberInput",
        "node_modules/amis-ui/lib/components/ArrayInput",
        "node_modules/amis-ui/lib/components/SearchBox",
        "node_modules/amis-ui/lib/components/AnchorNav",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/ChainedSelection",
        "node_modules/amis-ui/lib/components/TableSelection",
        "node_modules/amis-ui/lib/components/TreeSelection",
        "node_modules/amis-ui/lib/components/AssociatedSelection",
        "node_modules/amis-ui/lib/components/PullRefresh",
        "node_modules/amis-ui/lib/components/table/index",
        "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableListPicker",
        "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableList",
        "node_modules/amis-ui/lib/components/formula/VariableList",
        "node_modules/amis-ui/lib/components/formula/Picker",
        "node_modules/amis-ui/lib/components/PickerContainer",
        "node_modules/amis-ui/lib/components/json-schema/index",
        "node_modules/amis-ui/lib/components/Badge",
        "node_modules/amis-ui/lib/components/WithRemoteConfig",
        "node_modules/amis-ui/lib/components/condition-builder/index",
        "node_modules/amis-ui/lib/components/CityArea",
        "node_modules/amis-ui/lib/components/ListMenu",
        "node_modules/amis-ui/lib/components/Input",
        "node_modules/amis-ui/lib/components/schema-editor/index",
        "node_modules/amis-ui/lib/components/LocationPicker",
        "node_modules/amis-ui/lib/components/PopUp",
        "node_modules/amis-ui/lib/components/Cascader",
        "node_modules/amis-ui/lib/components/TransferDropDown",
        "node_modules/amis-ui/lib/components/TabsTransferPicker",
        "node_modules/amis-ui/lib/components/ResultList",
        "node_modules/amis-ui/lib/components/TransferPicker",
        "node_modules/amis-ui/lib/components/UserTabSelect",
        "node_modules/amis-ui/lib/components/table/HeadCellDropDown",
        "node_modules/amis-ui/lib/components/Card",
        "node_modules/amis-ui/lib/components/GridNav",
        "node_modules/amis-ui/lib/components/Link",
        "node_modules/amis-ui/lib/components/virtual-list/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/amis-ui/lib/components/Pagination",
        "node_modules/amis-ui/lib/components/Progress",
        "node_modules/amis-ui/lib/components/Steps",
        "node_modules/amis-ui/lib/components/Tag",
        "node_modules/amis-ui/lib/components/Timeline",
        "node_modules/amis-ui/lib/components/ImageGallery",
        "node_modules/amis-ui/lib/components/BaiduMapPicker",
        "node_modules/amis-ui/lib/components/MultilineText",
        "node_modules/amis-ui/lib/components/Form",
        "node_modules/amis-ui/lib/components/FormField",
        "node_modules/amis-ui/lib/components/Combo",
        "node_modules/amis-ui/lib/components/InputTable",
        "node_modules/amis-ui/lib/components/ConfirmBox",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/lodash/debounce",
        "node_modules/lodash/flatten"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/locale/zh-CN": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/locale/en-US": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/locale/de-DE": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/themes/cxd": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/themes/ang": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/themes/antd": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/themes/dark": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/amis-ui/lib/components/404",
        "node_modules/amis-ui/lib/components/Alert",
        "node_modules/amis-ui/lib/components/ContextMenu",
        "node_modules/amis-ui/lib/components/AsideNav",
        "node_modules/amis-ui/lib/components/Avatar",
        "node_modules/amis-ui/lib/components/Button",
        "node_modules/amis-ui/lib/components/Breadcrumb",
        "node_modules/amis-ui/lib/components/Checkbox",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis-ui/lib/components/Collapse",
        "node_modules/amis-ui/lib/components/CollapseGroup",
        "node_modules/amis-ui/lib/components/DatePicker",
        "node_modules/amis-ui/lib/components/DateRangePicker",
        "node_modules/amis-ui/lib/components/Drawer",
        "node_modules/amis-ui/lib/components/Tabs",
        "node_modules/amis-ui/lib/components/Editor",
        "node_modules/amis-ui/lib/components/Html",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-ui/lib/components/Layout",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/Modal",
        "node_modules/amis-ui/lib/components/Radios",
        "node_modules/amis-ui/lib/components/Range",
        "node_modules/amis-ui/lib/components/Rating",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-ui/lib/components/SparkLine",
        "node_modules/amis-ui/lib/components/Spinner",
        "node_modules/amis-ui/lib/components/Switch",
        "node_modules/amis-ui/lib/components/Textarea",
        "node_modules/amis-ui/lib/components/TitleBar",
        "node_modules/amis-ui/lib/components/Toast",
        "node_modules/amis-ui/lib/components/Tooltip",
        "node_modules/amis-ui/lib/components/TooltipWrapper",
        "node_modules/amis-ui/lib/components/Tree",
        "node_modules/amis-ui/lib/components/Alert2",
        "node_modules/amis-ui/lib/components/Transfer",
        "node_modules/amis-ui/lib/components/TabsTransfer",
        "node_modules/amis-ui/lib/components/ResultBox",
        "node_modules/amis-ui/lib/components/InputBox",
        "node_modules/amis-ui/lib/components/ListGroup",
        "node_modules/amis-ui/lib/components/NumberInput",
        "node_modules/amis-ui/lib/components/ArrayInput",
        "node_modules/amis-ui/lib/components/SearchBox",
        "node_modules/amis-ui/lib/components/AnchorNav",
        "node_modules/amis-ui/lib/components/GroupedSelection",
        "node_modules/amis-ui/lib/components/ChainedSelection",
        "node_modules/amis-ui/lib/components/TableSelection",
        "node_modules/amis-ui/lib/components/TreeSelection",
        "node_modules/amis-ui/lib/components/AssociatedSelection",
        "node_modules/amis-ui/lib/components/PullRefresh",
        "node_modules/amis-ui/lib/components/table/index",
        "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableListPicker",
        "node_modules/amis-ui/lib/components/schema-editor/SchemaVariableList",
        "node_modules/amis-ui/lib/components/formula/VariableList",
        "node_modules/amis-ui/lib/components/formula/Picker",
        "node_modules/amis-ui/lib/components/PickerContainer",
        "node_modules/amis-ui/lib/components/json-schema/index",
        "node_modules/amis-ui/lib/components/Badge",
        "node_modules/amis-ui/lib/components/WithRemoteConfig",
        "node_modules/amis-ui/lib/components/condition-builder/index",
        "node_modules/amis-ui/lib/components/CityArea",
        "node_modules/amis-ui/lib/components/ListMenu",
        "node_modules/amis-ui/lib/components/Input",
        "node_modules/amis-ui/lib/components/schema-editor/index",
        "node_modules/amis-ui/lib/components/LocationPicker",
        "node_modules/amis-ui/lib/components/PopUp",
        "node_modules/amis-ui/lib/components/Cascader",
        "node_modules/amis-ui/lib/components/TransferDropDown",
        "node_modules/amis-ui/lib/components/TabsTransferPicker",
        "node_modules/amis-ui/lib/components/ResultList",
        "node_modules/amis-ui/lib/components/TransferPicker",
        "node_modules/amis-ui/lib/components/UserSelect",
        "node_modules/amis-ui/lib/components/UserTabSelect",
        "node_modules/amis-ui/lib/components/table/HeadCellDropDown",
        "node_modules/amis-ui/lib/components/Card",
        "node_modules/amis-ui/lib/components/GridNav",
        "node_modules/amis-ui/lib/components/Link",
        "node_modules/amis-ui/lib/components/virtual-list/index",
        "node_modules/amis-ui/lib/components/WithStore",
        "node_modules/amis-ui/lib/components/PopOverContainer",
        "node_modules/amis-ui/lib/components/Pagination",
        "node_modules/amis-ui/lib/components/Progress",
        "node_modules/amis-ui/lib/components/Steps",
        "node_modules/amis-ui/lib/components/Tag",
        "node_modules/amis-ui/lib/components/Timeline",
        "node_modules/amis-ui/lib/components/ImageGallery",
        "node_modules/amis-ui/lib/components/BaiduMapPicker",
        "node_modules/amis-ui/lib/components/MultilineText",
        "node_modules/amis-ui/lib/components/Form",
        "node_modules/amis-ui/lib/components/FormField",
        "node_modules/amis-ui/lib/components/Combo",
        "node_modules/amis-ui/lib/components/InputTable",
        "node_modules/amis-ui/lib/components/ConfirmBox",
        "node_modules/amis-ui/lib/hooks/use-set-state",
        "node_modules/amis-ui/lib/hooks/use-update-effect",
        "node_modules/amis-ui/lib/hooks/use-touch",
        "node_modules/amis-ui/lib/hooks/use-validation-resolver",
        "node_modules/amis-ui/lib/locale/zh-CN",
        "node_modules/amis-ui/lib/locale/en-US",
        "node_modules/amis-ui/lib/locale/de-DE",
        "node_modules/amis-ui/lib/themes/cxd",
        "node_modules/amis-ui/lib/themes/ang",
        "node_modules/amis-ui/lib/themes/antd",
        "node_modules/amis-ui/lib/themes/dark",
        "node_modules/amis-ui/lib/icons/input-clear.svg",
        "node_modules/amis-ui/lib/icons/reload.svg",
        "node_modules/amis-ui/lib/icons/check.svg",
        "node_modules/amis-ui/lib/icons/plus.svg",
        "node_modules/amis-ui/lib/icons/minus.svg",
        "node_modules/amis-ui/lib/icons/pencil.svg",
        "node_modules/amis-ui/lib/icons/function.svg",
        "node_modules/amis-ui/lib/icons/menu.svg",
        "node_modules/amis-ui/lib/icons/user-remove.svg",
        "node_modules/amis-ui/lib/icons/role.svg",
        "node_modules/amis-ui/lib/icons/department.svg",
        "node_modules/amis-ui/lib/icons/post.svg",
        "node_modules/amis-ui/lib/icons/right-double-arrow.svg",
        "node_modules/amis-ui/lib/icons/close.svg",
        "node_modules/amis-ui/lib/icons/undo.svg",
        "node_modules/amis-ui/lib/icons/redo.svg",
        "node_modules/amis-ui/lib/icons/enter.svg",
        "node_modules/amis-ui/lib/icons/volume.svg",
        "node_modules/amis-ui/lib/icons/mute.svg",
        "node_modules/amis-ui/lib/icons/play.svg",
        "node_modules/amis-ui/lib/icons/pause.svg",
        "node_modules/amis-ui/lib/icons/left-arrow.svg",
        "node_modules/amis-ui/lib/icons/right-arrow.svg",
        "node_modules/amis-ui/lib/icons/down-arrow-bold.svg",
        "node_modules/amis-ui/lib/components/virtual-list/AutoSizer"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/preset": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/hotkeys-js/dist/hotkeys.common.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/hotkeys-js/dist/hotkeys.common": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/hotkeys-js/index": {
      "type": "js",
      "deps": [
        "node_modules/hotkeys-js/dist/hotkeys.common.min",
        "node_modules/hotkeys-js/dist/hotkeys.common"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Remark": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Action": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hotkeys-js/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/pick",
        "node_modules/lodash/omit",
        "node_modules/amis/lib/renderers/Remark"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Alert": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/App": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Avatar": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/StaticHoc": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/ButtonGroupSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/ButtonGroup": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/renderers/Form/ButtonGroupSelect",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/ButtonToolbar": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Breadcrumb": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/DropDownButton": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputDate": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/moment/moment",
        "node_modules/moment/locale/zh-cn",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Calendar": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/InputDate"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Collapse": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/CollapseGroup": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Color": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/pickBy": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_basePickBy",
        "node_modules/lodash/_getAllKeysIn"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/CRUD": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/pickBy",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/pick",
        "node_modules/react-dom/index",
        "node_modules/lodash/omit",
        "node_modules/lodash/find",
        "node_modules/lodash/findIndex"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/CRUD2": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/pick",
        "node_modules/react-dom/index",
        "node_modules/lodash/findIndex",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/lodash"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Pagination": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Cards": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/sortablejs/Sortable.min"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/QuickEdit": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/keycode/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/PopOver": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Copyable": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/TableCell": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/QuickEdit",
        "node_modules/amis/lib/renderers/Copyable",
        "node_modules/amis/lib/renderers/PopOver",
        "node_modules/mobx-react/dist/index",
        "node_modules/lodash/omit",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseXor": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseDifference",
        "node_modules/lodash/_baseFlatten",
        "node_modules/lodash/_baseUniq"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/xor": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayFilter",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_baseXor",
        "node_modules/lodash/isArrayLikeObject"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/HeadCellFilterDropdown": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/react-dom/index",
        "node_modules/lodash/xor"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/HeadCellSearchDropdown": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/TableRow": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/mobx-react/dist/index",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/TableBody": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/renderers/Table/TableRow",
        "node_modules/amis-core/lib/index",
        "node_modules/mobx-react/dist/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/ItemActionsWrapper": {
      "type": "js",
      "deps": [
        "node_modules/mobx-react/dist/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/TableContent": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/renderers/Table/TableBody",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis/lib/renderers/Table/ItemActionsWrapper",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/ColumnToggler": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/exportExcel": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Table/ColumnToggler",
        "node_modules/file-saver/dist/FileSaver.min",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/moment/moment"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table/index": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/lodash/isEqual",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/forEach",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/debounce",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/lodash/find",
        "node_modules/amis/lib/renderers/Table/TableCell",
        "node_modules/amis/lib/renderers/Table/HeadCellFilterDropdown",
        "node_modules/amis/lib/renderers/Table/HeadCellSearchDropdown",
        "node_modules/amis/lib/renderers/Table/TableContent",
        "node_modules/amis/lib/renderers/Table/TableBody",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis/lib/renderers/Table/ColumnToggler",
        "node_modules/amis/lib/renderers/Table/exportExcel",
        "node_modules/lodash/intersection"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Card": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/omit",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/QuickEdit",
        "node_modules/amis/lib/renderers/PopOver",
        "node_modules/amis/lib/renderers/Table/index",
        "node_modules/amis/lib/renderers/Copyable",
        "node_modules/react-dom/index",
        "node_modules/amis/lib/renderers/Table/TableCell"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Card2": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Custom": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/lodash/memoize",
        "node_modules/lodash/isString",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/isEqual"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Date": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/moment/moment"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Dialog": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/mobx/lib/index",
        "node_modules/react-dom/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Divider": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Each": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Flex": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Control": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Hidden": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputText": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/downshift/dist/downshift.cjs",
        "node_modules/match-sorter/dist/match-sorter.cjs",
        "node_modules/lodash/debounce",
        "node_modules/lodash/find",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/unionWith": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseFlatten",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_baseUniq",
        "node_modules/lodash/isArrayLikeObject",
        "node_modules/lodash/last"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputTag": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/downshift/dist/downshift.cjs",
        "node_modules/lodash/find",
        "node_modules/lodash/isInteger",
        "node_modules/lodash/unionWith",
        "node_modules/react-dom/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputNumber": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/@rc-component/mini-decimal/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Textarea": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseInRange": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/inRange": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseInRange",
        "node_modules/lodash/toFinite",
        "node_modules/lodash/toNumber"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Checkboxes": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/inRange",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Checkbox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputCity": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/ChartRadios": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputRating": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Switch": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Radios": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/JSONSchema": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/JSONSchemaEditor": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/ListSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/LocationPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Transfer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/find",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Select": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/find",
        "node_modules/lodash/debounce",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/Transfer",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Static": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Table/index",
        "node_modules/amis/lib/renderers/PopOver",
        "node_modules/amis/lib/renderers/QuickEdit",
        "node_modules/amis/lib/renderers/Copyable",
        "node_modules/lodash/omit",
        "node_modules/amis/lib/renderers/Table/TableCell"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputDateRange": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/moment/locale/zh-cn",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputFormula": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputRepeat": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputTree": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/TreeSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/match-sorter/dist/match-sorter.cjs",
        "node_modules/lodash/debounce",
        "node_modules/lodash/find",
        "node_modules/react-dom/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/react-dropzone/dist/index": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index"
      ],
      "pkg": "p0"
    },
    "node_modules/blueimp-canvastoblob/js/canvas-to-blob": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/attr-accept/dist/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputFile": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/find",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-ui/lib/index",
        "node_modules/react-dropzone/dist/index",
        "node_modules/lodash/merge",
        "node_modules/lodash/omit"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Image": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputImage": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-dropzone/dist/index",
        "node_modules/blueimp-canvastoblob/js/canvas-to-blob",
        "node_modules/lodash/find",
        "node_modules/amis-ui/lib/index",
        "node_modules/attr-accept/dist/index",
        "node_modules/amis/lib/renderers/Form/InputFile",
        "node_modules/amis/lib/renderers/Image",
        "node_modules/lodash/isPlainObject",
        "node_modules/lodash/merge",
        "node_modules/lodash/omit"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/UUID": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/MatrixCheckboxes": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputMonthRange": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/renderers/Form/InputDateRange",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputQuarterRange": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/renderers/Form/InputDateRange",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputYearRange": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/renderers/Form/InputDateRange",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputRange": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isNumber",
        "node_modules/lodash/isObject",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/forEach",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Combo": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/lodash/find",
        "node_modules/lodash/memoize",
        "node_modules/mobx-state-tree/dist/mobx-state-tree"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputArray": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/Combo"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/ConditionBuilder": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputSubForm": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/lodash/omit",
        "node_modules/lodash/pick",
        "node_modules/amis-ui/lib/index",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputExcel": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dropzone/dist/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputRichText": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Editor": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/DiffEditor": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputColor": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/ChainedSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/StaticHoc",
        "node_modules/lodash/find"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/union": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseFlatten",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_baseUniq",
        "node_modules/lodash/isArrayLikeObject"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/NestedSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/react-dom/index",
        "node_modules/lodash/xor",
        "node_modules/lodash/union",
        "node_modules/lodash/compact",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/TransferPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/Transfer",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputTable": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/omit",
        "node_modules/lodash/findIndex",
        "node_modules/lodash/cloneDeep",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/inRange",
        "node_modules/lodash/find",
        "node_modules/moment/moment",
        "node_modules/lodash/merge"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/intersectionWith": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseIntersection",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/_castArrayLikeObject",
        "node_modules/lodash/last"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Picker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/lodash/omit",
        "node_modules/lodash/find",
        "node_modules/lodash/isEqual",
        "node_modules/lodash/findIndex",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/intersectionWith"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/IconPickerIcons": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/IconPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/match-sorter/dist/match-sorter.cjs",
        "node_modules/keycode/index",
        "node_modules/downshift/dist/downshift.cjs",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/IconPickerIcons",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/IconSelectStore": {
      "type": "js",
      "deps": [
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/IconSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/match-sorter/dist/match-sorter.cjs",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/debounce",
        "node_modules/amis/lib/renderers/Form/IconSelectStore"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Formula": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/FieldSet": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Collapse"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/TabsTransfer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/lodash/find",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/Transfer",
        "node_modules/amis-ui/lib/components/Selection",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/TabsTransferPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Form/TabsTransfer",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/Group": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/InputGroup": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Form/UserSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/find",
        "node_modules/amis/lib/renderers/Form/StaticHoc"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Grid": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/pick",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Grid2D": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/HBox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/VBox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Images": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Image"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/List": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/lodash/omit",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/QuickEdit",
        "node_modules/amis/lib/renderers/PopOver",
        "node_modules/amis/lib/renderers/Table/index",
        "node_modules/amis/lib/renderers/Copyable",
        "node_modules/amis/lib/renderers/Table/TableCell"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Log": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Operation": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Page": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/PaginationWrapper": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Panel": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Plain": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Property": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Portlet": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/mapValues",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Spinner": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Tabs": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/find",
        "node_modules/lodash/findIndex",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Tpl": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Mapping": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Progress": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Status": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/merge",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Json": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react-json-view/dist/main"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Link": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Wizard": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Chart": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/lodash/debounce",
        "node_modules/lodash/pick"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Container": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/SearchBox": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Service": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/extend",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/SparkLine": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/objectSpread": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/defineProperty"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/assertThisInitialized": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/possibleConstructorReturn": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/assertThisInitialized"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/getPrototypeOf": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/inherits": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/setPrototypeOf"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/objectSpread2": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/defineProperty"
      ],
      "pkg": "p0"
    },
    "node_modules/redux/lib/redux": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/objectSpread2"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/actions/video": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/video-react/lib/utils/fullscreen": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/actions/player": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/video-react/lib/utils/fullscreen"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/reducers/player": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/objectSpread",
        "node_modules/video-react/lib/actions/video",
        "node_modules/video-react/lib/actions/player"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/reducers/operation": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/objectSpread",
        "node_modules/video-react/lib/actions/player"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/reducers/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/video-react/lib/reducers/player",
        "node_modules/video-react/lib/reducers/operation"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/Manager": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/objectSpread",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/redux/lib/redux",
        "node_modules/video-react/lib/reducers/index",
        "node_modules/video-react/lib/actions/player",
        "node_modules/video-react/lib/actions/video"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/BigPlayButton": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/LoadingSpinner": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/PosterImage": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/arrayWithoutHoles": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/arrayLikeToArray"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/iterableToArray": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/nonIterableSpread": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/toConsumableArray": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/arrayWithoutHoles",
        "node_modules/@babel/runtime/helpers/iterableToArray",
        "node_modules/@babel/runtime/helpers/unsupportedIterableToArray",
        "node_modules/@babel/runtime/helpers/nonIterableSpread"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/utils/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/toConsumableArray",
        "node_modules/@babel/runtime/helpers/objectSpread",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/Video": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/objectSpread",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/Bezel": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/utils/dom": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/Shortcut": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/defineProperty",
        "node_modules/@babel/runtime/helpers/toConsumableArray",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/video-react/lib/utils/dom"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/Slider": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/utils/dom"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/PlayProgressBar": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/LoadProgressBar": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/MouseTimeDisplay": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/SeekBar": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/components/Slider",
        "node_modules/video-react/lib/components/control-bar/PlayProgressBar",
        "node_modules/video-react/lib/components/control-bar/LoadProgressBar",
        "node_modules/video-react/lib/components/control-bar/MouseTimeDisplay",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/ProgressControl": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/utils/dom",
        "node_modules/video-react/lib/components/control-bar/SeekBar"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/PlayToggle": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/ForwardReplayControl": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/ForwardControl": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/video-react/lib/components/control-bar/ForwardReplayControl"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/ReplayControl": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/video-react/lib/components/control-bar/ForwardReplayControl"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/FullscreenToggle": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/time-controls/RemainingTimeDisplay": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/time-controls/CurrentTimeDisplay": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/time-controls/DurationDisplay": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/time-controls/TimeDivider": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/ClickableComponent": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectSpread",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/popup/Popup": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/popup/PopupButton": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectSpread",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/components/ClickableComponent",
        "node_modules/video-react/lib/components/popup/Popup"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/volume-control/VolumeLevel": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/volume-control/VolumeBar": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/components/Slider",
        "node_modules/video-react/lib/components/volume-control/VolumeLevel"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/VolumeMenuButton": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/components/popup/PopupButton",
        "node_modules/video-react/lib/components/volume-control/VolumeBar"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/menu/Menu": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/menu/MenuItem": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/menu/MenuButton": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/components/menu/Menu",
        "node_modules/video-react/lib/components/menu/MenuItem",
        "node_modules/video-react/lib/components/ClickableComponent"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/PlaybackRateMenuButton": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/components/menu/MenuButton"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/ControlBar": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/components/control-bar/ProgressControl",
        "node_modules/video-react/lib/components/control-bar/PlayToggle",
        "node_modules/video-react/lib/components/control-bar/ForwardControl",
        "node_modules/video-react/lib/components/control-bar/ReplayControl",
        "node_modules/video-react/lib/components/control-bar/FullscreenToggle",
        "node_modules/video-react/lib/components/time-controls/RemainingTimeDisplay",
        "node_modules/video-react/lib/components/time-controls/CurrentTimeDisplay",
        "node_modules/video-react/lib/components/time-controls/DurationDisplay",
        "node_modules/video-react/lib/components/time-controls/TimeDivider",
        "node_modules/video-react/lib/components/control-bar/VolumeMenuButton",
        "node_modules/video-react/lib/components/control-bar/PlaybackRateMenuButton",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/utils/browser": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/Player": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/objectSpread",
        "node_modules/@babel/runtime/helpers/defineProperty",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/Manager",
        "node_modules/video-react/lib/components/BigPlayButton",
        "node_modules/video-react/lib/components/LoadingSpinner",
        "node_modules/video-react/lib/components/PosterImage",
        "node_modules/video-react/lib/components/Video",
        "node_modules/video-react/lib/components/Bezel",
        "node_modules/video-react/lib/components/Shortcut",
        "node_modules/video-react/lib/components/control-bar/ControlBar",
        "node_modules/video-react/lib/utils/browser",
        "node_modules/video-react/lib/utils/dom",
        "node_modules/video-react/lib/utils/index",
        "node_modules/video-react/lib/utils/fullscreen"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/PlaybackRate": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/react/index",
        "node_modules/video-react/lib/components/control-bar/PlaybackRateMenuButton",
        "node_modules/video-react/lib/utils/index"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/components/control-bar/ClosedCaptionButton": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn",
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/assertThisInitialized",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/prop-types/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/video-react/lib/components/menu/MenuButton"
      ],
      "pkg": "p0"
    },
    "node_modules/video-react/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/video-react/lib/components/Player",
        "node_modules/video-react/lib/components/Video",
        "node_modules/video-react/lib/components/BigPlayButton",
        "node_modules/video-react/lib/components/LoadingSpinner",
        "node_modules/video-react/lib/components/PosterImage",
        "node_modules/video-react/lib/components/Slider",
        "node_modules/video-react/lib/components/Bezel",
        "node_modules/video-react/lib/components/Shortcut",
        "node_modules/video-react/lib/components/control-bar/ControlBar",
        "node_modules/video-react/lib/components/control-bar/PlayToggle",
        "node_modules/video-react/lib/components/control-bar/ForwardControl",
        "node_modules/video-react/lib/components/control-bar/ReplayControl",
        "node_modules/video-react/lib/components/control-bar/FullscreenToggle",
        "node_modules/video-react/lib/components/control-bar/ProgressControl",
        "node_modules/video-react/lib/components/control-bar/SeekBar",
        "node_modules/video-react/lib/components/control-bar/PlayProgressBar",
        "node_modules/video-react/lib/components/control-bar/LoadProgressBar",
        "node_modules/video-react/lib/components/control-bar/MouseTimeDisplay",
        "node_modules/video-react/lib/components/control-bar/VolumeMenuButton",
        "node_modules/video-react/lib/components/control-bar/PlaybackRateMenuButton",
        "node_modules/video-react/lib/components/control-bar/PlaybackRate",
        "node_modules/video-react/lib/components/control-bar/ClosedCaptionButton",
        "node_modules/video-react/lib/components/time-controls/RemainingTimeDisplay",
        "node_modules/video-react/lib/components/time-controls/CurrentTimeDisplay",
        "node_modules/video-react/lib/components/time-controls/DurationDisplay",
        "node_modules/video-react/lib/components/time-controls/TimeDivider",
        "node_modules/video-react/lib/components/menu/MenuButton",
        "node_modules/video-react/lib/actions/player",
        "node_modules/video-react/lib/actions/video",
        "node_modules/video-react/lib/reducers/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Video": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/video-react/lib/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Audio": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/upperFirst",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/Children/toArray": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/react/index",
        "node_modules/rc-util/node_modules/react-is/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/Dom/findDOMNode": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/react/index",
        "node_modules/react-dom/index"
      ],
      "pkg": "p0"
    },
    "node_modules/resize-observer-polyfill/dist/ResizeObserver": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/rc-resize-observer/lib/utils/observerUtil": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/resize-observer-polyfill/dist/ResizeObserver"
      ],
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/isNativeReflectConstruct": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@babel/runtime/helpers/createSuper": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/getPrototypeOf",
        "node_modules/@babel/runtime/helpers/isNativeReflectConstruct",
        "node_modules/@babel/runtime/helpers/possibleConstructorReturn"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-resize-observer/lib/SingleObserver/DomWrapper": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/classCallCheck",
        "node_modules/@babel/runtime/helpers/createClass",
        "node_modules/@babel/runtime/helpers/inherits",
        "node_modules/@babel/runtime/helpers/createSuper",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-resize-observer/lib/Collection": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-resize-observer/lib/SingleObserver/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/objectSpread2",
        "node_modules/rc-util/lib/ref",
        "node_modules/react/index",
        "node_modules/rc-util/lib/Dom/findDOMNode",
        "node_modules/rc-resize-observer/lib/utils/observerUtil",
        "node_modules/rc-resize-observer/lib/SingleObserver/DomWrapper",
        "node_modules/rc-resize-observer/lib/Collection"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-resize-observer/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/react/index",
        "node_modules/rc-util/lib/Children/toArray",
        "node_modules/rc-util/lib/warning",
        "node_modules/rc-resize-observer/lib/SingleObserver/index",
        "node_modules/rc-resize-observer/lib/Collection",
        "node_modules/rc-resize-observer/lib/utils/observerUtil"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-overflow/lib/Item": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectSpread2",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/rc-resize-observer/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-util/lib/hooks/useState": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireWildcard",
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/slicedToArray",
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-overflow/lib/hooks/useBatchFrameState": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/slicedToArray",
        "node_modules/react/index",
        "node_modules/rc-util/lib/raf",
        "node_modules/rc-util/lib/hooks/useState"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-overflow/lib/RawItem": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/rc-overflow/lib/Item",
        "node_modules/rc-overflow/lib/Overflow"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-overflow/lib/Overflow": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/@babel/runtime/helpers/typeof",
        "node_modules/@babel/runtime/helpers/extends",
        "node_modules/@babel/runtime/helpers/objectSpread2",
        "node_modules/@babel/runtime/helpers/slicedToArray",
        "node_modules/@babel/runtime/helpers/objectWithoutProperties",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/rc-resize-observer/lib/index",
        "node_modules/rc-util/lib/hooks/useLayoutEffect",
        "node_modules/rc-overflow/lib/Item",
        "node_modules/rc-overflow/lib/hooks/useBatchFrameState",
        "node_modules/rc-overflow/lib/RawItem"
      ],
      "pkg": "p0"
    },
    "node_modules/rc-overflow/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/@babel/runtime/helpers/interopRequireDefault",
        "node_modules/rc-overflow/lib/Overflow"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Nav": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/rc-overflow/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/immutability-helper/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Tasks": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/immutability-helper/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Drawer": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/mobx/lib/index",
        "node_modules/react-dom/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Wrapper": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/IFrame": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/BarCode": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/qrcode.react/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/QRCode": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-core/lib/index",
        "node_modules/qrcode.react/lib/index",
        "node_modules/lodash/mapValues"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Icon": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/lodash"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Carousel": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-transition-group/cjs/Transition",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Image"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/AnchorNav": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/find"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Steps": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Timeline": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Markdown": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/TableView": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Code": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isPlainObject",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/WebComponent": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/mapValues"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/GridNav": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/TooltipWrapper": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Tag": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table2/HeadCellSearchDropdown": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table2/TableCell": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Table/index",
        "node_modules/amis/lib/renderers/QuickEdit",
        "node_modules/amis/lib/renderers/Copyable",
        "node_modules/amis/lib/renderers/PopOver",
        "node_modules/amis/lib/renderers/Table/TableCell"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table2/ColumnToggler": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Table/ColumnToggler"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Table2/index": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/lodash/cloneDeep",
        "node_modules/lodash/isEqual",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/renderers/Table2/HeadCellSearchDropdown",
        "node_modules/amis/lib/renderers/Table2/TableCell",
        "node_modules/amis/lib/renderers/Table2/ColumnToggler"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Words": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/Password": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/DateRange": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/moment/moment"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/renderers/MultilineText": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/compat": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/renderers/Form/Checkbox",
        "node_modules/amis/lib/renderers/Form/FieldSet",
        "node_modules/amis/lib/renderers/Card",
        "node_modules/amis/lib/renderers/List",
        "node_modules/amis/lib/renderers/Form/ButtonGroupSelect",
        "node_modules/amis/lib/renderers/Form/InputFile",
        "node_modules/amis/lib/renderers/Form/InputImage",
        "node_modules/amis/lib/renderers/Form/InputRichText",
        "node_modules/amis/lib/renderers/Grid",
        "node_modules/amis/lib/renderers/HBox"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/schemaExtend": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/isEqual",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis/lib/preset",
        "node_modules/amis/lib/renderers/Action",
        "node_modules/amis/lib/renderers/Alert",
        "node_modules/amis/lib/renderers/App",
        "node_modules/amis/lib/renderers/Avatar",
        "node_modules/amis/lib/renderers/Remark",
        "node_modules/amis/lib/renderers/ButtonGroup",
        "node_modules/amis/lib/renderers/Form/ButtonToolbar",
        "node_modules/amis/lib/renderers/Breadcrumb",
        "node_modules/amis/lib/renderers/DropDownButton",
        "node_modules/amis/lib/renderers/Calendar",
        "node_modules/amis/lib/renderers/Collapse",
        "node_modules/amis/lib/renderers/CollapseGroup",
        "node_modules/amis/lib/renderers/Color",
        "node_modules/amis/lib/renderers/CRUD",
        "node_modules/amis/lib/renderers/CRUD2",
        "node_modules/amis/lib/renderers/Pagination",
        "node_modules/amis/lib/renderers/Cards",
        "node_modules/amis/lib/renderers/Card",
        "node_modules/amis/lib/renderers/Card2",
        "node_modules/amis/lib/renderers/Custom",
        "node_modules/amis/lib/renderers/Date",
        "node_modules/amis/lib/renderers/Dialog",
        "node_modules/amis/lib/renderers/Divider",
        "node_modules/amis/lib/renderers/Each",
        "node_modules/amis/lib/renderers/Flex",
        "node_modules/amis/lib/renderers/Form/Control",
        "node_modules/amis/lib/renderers/Form/Hidden",
        "node_modules/amis/lib/renderers/Form/InputText",
        "node_modules/amis/lib/renderers/Form/InputTag",
        "node_modules/amis/lib/renderers/Form/InputNumber",
        "node_modules/amis/lib/renderers/Form/Textarea",
        "node_modules/amis/lib/renderers/Form/Checkboxes",
        "node_modules/amis/lib/renderers/Form/Checkbox",
        "node_modules/amis/lib/renderers/Form/InputCity",
        "node_modules/amis/lib/renderers/Form/ChartRadios",
        "node_modules/amis/lib/renderers/Form/InputRating",
        "node_modules/amis/lib/renderers/Form/Switch",
        "node_modules/amis/lib/renderers/Form/Radios",
        "node_modules/amis/lib/renderers/Form/JSONSchema",
        "node_modules/amis/lib/renderers/Form/JSONSchemaEditor",
        "node_modules/amis/lib/renderers/Form/ListSelect",
        "node_modules/amis/lib/renderers/Form/LocationPicker",
        "node_modules/amis/lib/renderers/Form/Select",
        "node_modules/amis/lib/renderers/Form/Static",
        "node_modules/amis/lib/renderers/Form/InputDate",
        "node_modules/amis/lib/renderers/Form/InputDateRange",
        "node_modules/amis/lib/renderers/Form/InputFormula",
        "node_modules/amis/lib/renderers/Form/InputRepeat",
        "node_modules/amis/lib/renderers/Form/InputTree",
        "node_modules/amis/lib/renderers/Form/TreeSelect",
        "node_modules/amis/lib/renderers/Form/InputImage",
        "node_modules/amis/lib/renderers/Form/InputFile",
        "node_modules/amis/lib/renderers/Form/UUID",
        "node_modules/amis/lib/renderers/Form/MatrixCheckboxes",
        "node_modules/amis/lib/renderers/Form/InputMonthRange",
        "node_modules/amis/lib/renderers/Form/InputQuarterRange",
        "node_modules/amis/lib/renderers/Form/InputYearRange",
        "node_modules/amis/lib/renderers/Form/InputRange",
        "node_modules/amis/lib/renderers/Form/InputArray",
        "node_modules/amis/lib/renderers/Form/Combo",
        "node_modules/amis/lib/renderers/Form/ConditionBuilder",
        "node_modules/amis/lib/renderers/Form/InputSubForm",
        "node_modules/amis/lib/renderers/Form/InputExcel",
        "node_modules/amis/lib/renderers/Form/InputRichText",
        "node_modules/amis/lib/renderers/Form/Editor",
        "node_modules/amis/lib/renderers/Form/DiffEditor",
        "node_modules/amis/lib/renderers/Form/InputColor",
        "node_modules/amis/lib/renderers/Form/ChainedSelect",
        "node_modules/amis/lib/renderers/Form/NestedSelect",
        "node_modules/amis/lib/renderers/Form/Transfer",
        "node_modules/amis/lib/renderers/Form/TransferPicker",
        "node_modules/amis/lib/renderers/Form/InputTable",
        "node_modules/amis/lib/renderers/Form/Picker",
        "node_modules/amis/lib/renderers/Form/IconPicker",
        "node_modules/amis/lib/renderers/Form/IconSelect",
        "node_modules/amis/lib/renderers/Form/Formula",
        "node_modules/amis/lib/renderers/Form/FieldSet",
        "node_modules/amis/lib/renderers/Form/TabsTransfer",
        "node_modules/amis/lib/renderers/Form/TabsTransferPicker",
        "node_modules/amis/lib/renderers/Form/Group",
        "node_modules/amis/lib/renderers/Form/InputGroup",
        "node_modules/amis/lib/renderers/Form/UserSelect",
        "node_modules/amis/lib/renderers/Grid",
        "node_modules/amis/lib/renderers/Grid2D",
        "node_modules/amis/lib/renderers/HBox",
        "node_modules/amis/lib/renderers/VBox",
        "node_modules/amis/lib/renderers/Image",
        "node_modules/amis/lib/renderers/Images",
        "node_modules/amis/lib/renderers/List",
        "node_modules/amis/lib/renderers/Log",
        "node_modules/amis/lib/renderers/Operation",
        "node_modules/amis/lib/renderers/Page",
        "node_modules/amis/lib/renderers/PaginationWrapper",
        "node_modules/amis/lib/renderers/Panel",
        "node_modules/amis/lib/renderers/Plain",
        "node_modules/amis/lib/renderers/Property",
        "node_modules/amis/lib/renderers/Portlet",
        "node_modules/amis/lib/renderers/Spinner",
        "node_modules/amis/lib/renderers/Table/index",
        "node_modules/amis/lib/renderers/Tabs",
        "node_modules/amis/lib/renderers/Tpl",
        "node_modules/amis/lib/renderers/Mapping",
        "node_modules/amis/lib/renderers/Progress",
        "node_modules/amis/lib/renderers/Status",
        "node_modules/amis/lib/renderers/Json",
        "node_modules/amis/lib/renderers/Link",
        "node_modules/amis/lib/renderers/Wizard",
        "node_modules/amis/lib/renderers/Chart",
        "node_modules/amis/lib/renderers/Container",
        "node_modules/amis/lib/renderers/SearchBox",
        "node_modules/amis/lib/renderers/Service",
        "node_modules/amis/lib/renderers/SparkLine",
        "node_modules/amis/lib/renderers/Video",
        "node_modules/amis/lib/renderers/Audio",
        "node_modules/amis/lib/renderers/Nav",
        "node_modules/amis/lib/renderers/Tasks",
        "node_modules/amis/lib/renderers/Drawer",
        "node_modules/amis/lib/renderers/Wrapper",
        "node_modules/amis/lib/renderers/IFrame",
        "node_modules/amis/lib/renderers/BarCode",
        "node_modules/amis/lib/renderers/QRCode",
        "node_modules/amis/lib/renderers/Icon",
        "node_modules/amis/lib/renderers/Carousel",
        "node_modules/amis/lib/renderers/AnchorNav",
        "node_modules/amis/lib/renderers/Steps",
        "node_modules/amis/lib/renderers/Timeline",
        "node_modules/amis/lib/renderers/Markdown",
        "node_modules/amis/lib/renderers/TableView",
        "node_modules/amis/lib/renderers/Code",
        "node_modules/amis/lib/renderers/WebComponent",
        "node_modules/amis/lib/renderers/GridNav",
        "node_modules/amis/lib/renderers/TooltipWrapper",
        "node_modules/amis/lib/renderers/Tag",
        "node_modules/amis/lib/renderers/Table2/index",
        "node_modules/amis/lib/renderers/Words",
        "node_modules/amis/lib/renderers/Password",
        "node_modules/amis/lib/renderers/DateRange",
        "node_modules/amis/lib/renderers/MultilineText",
        "node_modules/amis/lib/compat",
        "node_modules/amis/lib/schemaExtend",
        "node_modules/amis/lib/renderers/Form/IconPickerIcons",
        "node_modules/amis/lib/renderers/Form/IconSelectStore"
      ],
      "pkg": "p0"
    },
    "node_modules/i18n-runtime/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/deep-diff/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/bind": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/utils": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/helpers/bind"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/buildURL": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/InterceptorManager": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/transformData": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/cancel/isCancel": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/normalizeHeaderName": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/enhanceError": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/createError": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/core/enhanceError"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/settle": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/core/createError"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/cookies": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/isAbsoluteURL": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/combineURLs": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/buildFullPath": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/helpers/isAbsoluteURL",
        "node_modules/axios/lib/helpers/combineURLs"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/parseHeaders": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/isURLSameOrigin": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/adapters/xhr": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils",
        "node_modules/axios/lib/core/settle",
        "node_modules/axios/lib/helpers/cookies",
        "node_modules/axios/lib/helpers/buildURL",
        "node_modules/axios/lib/core/buildFullPath",
        "node_modules/axios/lib/helpers/parseHeaders",
        "node_modules/axios/lib/helpers/isURLSameOrigin",
        "node_modules/axios/lib/core/createError"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/defaults": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils",
        "node_modules/axios/lib/helpers/normalizeHeaderName",
        "node_modules/axios/lib/adapters/xhr"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/dispatchRequest": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils",
        "node_modules/axios/lib/core/transformData",
        "node_modules/axios/lib/cancel/isCancel",
        "node_modules/axios/lib/defaults"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/mergeConfig": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/core/Axios": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils",
        "node_modules/axios/lib/helpers/buildURL",
        "node_modules/axios/lib/core/InterceptorManager",
        "node_modules/axios/lib/core/dispatchRequest",
        "node_modules/axios/lib/core/mergeConfig"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/cancel/Cancel": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/cancel/CancelToken": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/cancel/Cancel"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/spread": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/helpers/isAxiosError": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/axios/lib/axios": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/utils",
        "node_modules/axios/lib/helpers/bind",
        "node_modules/axios/lib/core/Axios",
        "node_modules/axios/lib/core/mergeConfig",
        "node_modules/axios/lib/defaults",
        "node_modules/axios/lib/cancel/Cancel",
        "node_modules/axios/lib/cancel/CancelToken",
        "node_modules/axios/lib/cancel/isCancel",
        "node_modules/axios/lib/helpers/spread",
        "node_modules/axios/lib/helpers/isAxiosError"
      ],
      "pkg": "p0"
    },
    "node_modules/axios/index": {
      "type": "js",
      "deps": [
        "node_modules/axios/lib/axios"
      ],
      "pkg": "p0"
    },
    "node_modules/json-ast-comments/lib/json": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/json-ast-comments/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/json-ast-comments/lib/json"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-editor-core/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/amis/lib/index",
        "node_modules/i18n-runtime/lib/index",
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/lodash/isEqual",
        "node_modules/mobx/lib/index",
        "node_modules/deep-diff/index",
        "node_modules/lodash/isPlainObject",
        "node_modules/lodash/isNumber",
        "node_modules/amis-core/lib/index",
        "node_modules/mobx-react/dist/index",
        "node_modules/react-dom/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/lodash/find",
        "node_modules/lodash/groupBy",
        "node_modules/lodash/uniq",
        "node_modules/lodash/findIndex",
        "node_modules/lodash/flatten",
        "node_modules/axios/index",
        "node_modules/lodash/debounce",
        "node_modules/json-ast-comments/lib/index",
        "node_modules/lodash/omit",
        "node_modules/lodash/isString",
        "node_modules/lodash/lodash",
        "node_modules/amis-ui/lib/index",
        "node_modules/react-json-view/dist/main",
        "node_modules/moment/moment",
        "node_modules/lodash/cloneDeep"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/forOwn": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseForOwn",
        "node_modules/lodash/_castFunction"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/map": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayMap",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_baseMap",
        "node_modules/lodash/isArray"
      ],
      "pkg": "p0"
    },
    "node_modules/reactcss/lib/flattenNames": {
      "type": "js",
      "deps": [
        "node_modules/lodash/isString",
        "node_modules/lodash/forOwn",
        "node_modules/lodash/isPlainObject",
        "node_modules/lodash/map"
      ],
      "pkg": "p0"
    },
    "node_modules/reactcss/lib/mergeClasses": {
      "type": "js",
      "deps": [
        "node_modules/lodash/forOwn",
        "node_modules/lodash/cloneDeep"
      ],
      "pkg": "p0"
    },
    "node_modules/reactcss/lib/autoprefix": {
      "type": "js",
      "deps": [
        "node_modules/lodash/forOwn"
      ],
      "pkg": "p0"
    },
    "node_modules/reactcss/lib/components/hover": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/reactcss/lib/components/active": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/reactcss/lib/loop": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/reactcss/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/reactcss/lib/flattenNames",
        "node_modules/reactcss/lib/mergeClasses",
        "node_modules/reactcss/lib/autoprefix",
        "node_modules/reactcss/lib/components/hover",
        "node_modules/reactcss/lib/components/active",
        "node_modules/reactcss/lib/loop"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/helpers/alpha": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-color/lib/helpers/checkboard": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/Checkboard": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/checkboard"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/Alpha": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/alpha",
        "node_modules/react-color/lib/components/common/Checkboard"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/EditableInput": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/helpers/hue": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/Hue": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/hue"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/Raised": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/helpers/saturation": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/Saturation": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/throttle",
        "node_modules/react-color/lib/helpers/saturation"
      ],
      "pkg": "p0"
    },
    "node_modules/tinycolor2/cjs/tinycolor": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-color/lib/helpers/color": {
      "type": "js",
      "deps": [
        "node_modules/lodash/each",
        "node_modules/tinycolor2/cjs/tinycolor"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/ColorWrap": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/lodash/debounce",
        "node_modules/react-color/lib/helpers/color"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/helpers/interaction": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/Swatch": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/interaction",
        "node_modules/react-color/lib/components/common/Checkboard"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/common/index": {
      "type": "js",
      "deps": [
        "node_modules/react-color/lib/components/common/Alpha",
        "node_modules/react-color/lib/components/common/Checkboard",
        "node_modules/react-color/lib/components/common/EditableInput",
        "node_modules/react-color/lib/components/common/Hue",
        "node_modules/react-color/lib/components/common/Raised",
        "node_modules/react-color/lib/components/common/Saturation",
        "node_modules/react-color/lib/components/common/ColorWrap",
        "node_modules/react-color/lib/components/common/Swatch"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/alpha/AlphaPointer": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/alpha/Alpha": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/alpha/AlphaPointer"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/block/BlockSwatches": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/map",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/block/Block": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/block/BlockSwatches"
      ],
      "pkg": "p0"
    },
    "node_modules/material-colors/dist/colors": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/circle/CircleSwatch": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/circle/Circle": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/map",
        "node_modules/lodash/merge",
        "node_modules/material-colors/dist/colors",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/circle/CircleSwatch"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/isUndefined": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/@icons/material/UnfoldMoreHorizontalIcon": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/chrome/ChromeFields": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/lodash/isUndefined",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/@icons/material/UnfoldMoreHorizontalIcon"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/chrome/ChromePointer": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/chrome/ChromePointerCircle": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/chrome/Chrome": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/chrome/ChromeFields",
        "node_modules/react-color/lib/components/chrome/ChromePointer",
        "node_modules/react-color/lib/components/chrome/ChromePointerCircle"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/compact/CompactColor": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/compact/CompactFields": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/compact/Compact": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/map",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/compact/CompactColor",
        "node_modules/react-color/lib/components/compact/CompactFields"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/github/GithubSwatch": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/github/Github": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/map",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/github/GithubSwatch"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/hue/HuePointer": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/hue/Hue": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/hue/HuePointer"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/material/Material": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/photoshop/PhotoshopFields": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/photoshop/PhotoshopPointerCircle": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/photoshop/PhotoshopPointer": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/photoshop/PhotoshopButton": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/photoshop/PhotoshopPreviews": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/photoshop/Photoshop": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/photoshop/PhotoshopFields",
        "node_modules/react-color/lib/components/photoshop/PhotoshopPointerCircle",
        "node_modules/react-color/lib/components/photoshop/PhotoshopPointer",
        "node_modules/react-color/lib/components/photoshop/PhotoshopButton",
        "node_modules/react-color/lib/components/photoshop/PhotoshopPreviews"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/sketch/SketchFields": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/sketch/SketchPresetColors": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/sketch/Sketch": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/sketch/SketchFields",
        "node_modules/react-color/lib/components/sketch/SketchPresetColors"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/slider/SliderSwatch": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/slider/SliderSwatches": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/components/slider/SliderSwatch"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/slider/SliderPointer": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/slider/Slider": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/slider/SliderSwatches",
        "node_modules/react-color/lib/components/slider/SliderPointer"
      ],
      "pkg": "p0"
    },
    "node_modules/@icons/material/CheckIcon": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/swatches/SwatchesColor": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/@icons/material/CheckIcon"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/swatches/SwatchesGroup": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/map",
        "node_modules/react-color/lib/components/swatches/SwatchesColor"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/swatches/Swatches": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/map",
        "node_modules/lodash/merge",
        "node_modules/material-colors/dist/colors",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/swatches/SwatchesGroup"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/twitter/Twitter": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/map",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/google/GooglePointerCircle": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/prop-types/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/google/GooglePointer": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/prop-types/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/google/GoogleFields": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/reactcss/lib/index",
        "node_modules/react-color/lib/helpers/color",
        "node_modules/react-color/lib/components/common/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/components/google/Google": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/reactcss/lib/index",
        "node_modules/lodash/merge",
        "node_modules/react-color/lib/components/common/index",
        "node_modules/react-color/lib/components/google/GooglePointerCircle",
        "node_modules/react-color/lib/components/google/GooglePointer",
        "node_modules/react-color/lib/components/google/GoogleFields"
      ],
      "pkg": "p0"
    },
    "node_modules/react-color/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/react-color/lib/components/alpha/Alpha",
        "node_modules/react-color/lib/components/block/Block",
        "node_modules/react-color/lib/components/circle/Circle",
        "node_modules/react-color/lib/components/chrome/Chrome",
        "node_modules/react-color/lib/components/compact/Compact",
        "node_modules/react-color/lib/components/github/Github",
        "node_modules/react-color/lib/components/hue/Hue",
        "node_modules/react-color/lib/components/material/Material",
        "node_modules/react-color/lib/components/photoshop/Photoshop",
        "node_modules/react-color/lib/components/sketch/Sketch",
        "node_modules/react-color/lib/components/slider/Slider",
        "node_modules/react-color/lib/components/swatches/Swatches",
        "node_modules/react-color/lib/components/twitter/Twitter",
        "node_modules/react-color/lib/components/google/Google",
        "node_modules/react-color/lib/components/common/ColorWrap"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/ColorPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/react-color/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/i18n-runtime/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/Border": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/classnames/index",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-theme-editor/lib/renderers/ColorPicker",
        "node_modules/i18n-runtime/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/checkbox-check.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/checkbox.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/code.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/component.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/global.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/custom.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/lock.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/unlock.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/italic.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/underline.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/line-through.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/text-left.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/text-center.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/text-right.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/text-justify.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/vertical-top.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/vertical-middle.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/vertical-bottom.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/add-button.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/delete-button.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/color-picker-img.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/color-picker-img-active.svg": {
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/icons/index": {
      "type": "js",
      "deps": [
        "node_modules/amis/lib/index",
        "node_modules/amis-theme-editor/lib/icons/checkbox-check.svg",
        "node_modules/amis-theme-editor/lib/icons/checkbox.svg",
        "node_modules/amis-theme-editor/lib/icons/code.svg",
        "node_modules/amis-theme-editor/lib/icons/component.svg",
        "node_modules/amis-theme-editor/lib/icons/global.svg",
        "node_modules/amis-theme-editor/lib/icons/custom.svg",
        "node_modules/amis-theme-editor/lib/icons/lock.svg",
        "node_modules/amis-theme-editor/lib/icons/unlock.svg",
        "node_modules/amis-theme-editor/lib/icons/italic.svg",
        "node_modules/amis-theme-editor/lib/icons/underline.svg",
        "node_modules/amis-theme-editor/lib/icons/line-through.svg",
        "node_modules/amis-theme-editor/lib/icons/text-left.svg",
        "node_modules/amis-theme-editor/lib/icons/text-center.svg",
        "node_modules/amis-theme-editor/lib/icons/text-right.svg",
        "node_modules/amis-theme-editor/lib/icons/text-justify.svg",
        "node_modules/amis-theme-editor/lib/icons/vertical-top.svg",
        "node_modules/amis-theme-editor/lib/icons/vertical-middle.svg",
        "node_modules/amis-theme-editor/lib/icons/vertical-bottom.svg",
        "node_modules/amis-theme-editor/lib/icons/add-button.svg",
        "node_modules/amis-theme-editor/lib/icons/delete-button.svg",
        "node_modules/amis-theme-editor/lib/icons/color-picker-img.svg",
        "node_modules/amis-theme-editor/lib/icons/color-picker-img-active.svg"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/ThemeWrapper": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-theme-editor/lib/icons/index",
        "node_modules/classnames/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-ui/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/Font": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-theme-editor/lib/icons/index",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-theme-editor/lib/renderers/ColorPicker",
        "node_modules/lodash/lodash",
        "node_modules/amis-theme-editor/lib/renderers/ThemeWrapper",
        "node_modules/i18n-runtime/lib/index",
        "node_modules/amis/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/ThemeSelect": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-core/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/PaddingAndMargin": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-theme-editor/lib/renderers/ThemeSelect"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/Radius": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/classnames/index",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-theme-editor/lib/renderers/ThemeSelect",
        "node_modules/i18n-runtime/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/Shadow": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-theme-editor/lib/icons/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-theme-editor/lib/renderers/ColorPicker",
        "node_modules/lodash/lodash",
        "node_modules/amis-theme-editor/lib/renderers/ThemeWrapper",
        "node_modules/classnames/index",
        "node_modules/i18n-runtime/lib/index",
        "node_modules/amis/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-theme-editor/lib/renderers/Size": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-theme-editor/lib/icons/index",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-theme-editor/lib/renderers/ThemeSelect",
        "node_modules/lodash/lodash",
        "node_modules/amis/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/assign": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_assignValue",
        "node_modules/lodash/_copyObject",
        "node_modules/lodash/_createAssigner",
        "node_modules/lodash/isArrayLike",
        "node_modules/lodash/_isPrototype",
        "node_modules/lodash/keys"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_baseReduce": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/reduce": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_arrayReduce",
        "node_modules/lodash/_baseEach",
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_baseReduce",
        "node_modules/lodash/isArray"
      ],
      "pkg": "p0"
    },
    "node_modules/@webcomponents/webcomponentsjs/custom-elements-es5-adapter": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/lodash/mergeWith": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseMerge",
        "node_modules/lodash/_createAssigner"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/_basePullAt": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseUnset",
        "node_modules/lodash/_isIndex"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/remove": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseIteratee",
        "node_modules/lodash/_basePullAt"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/set": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseSet"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-postcss/lib/index.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/toggle-selection/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/copy-to-clipboard/index": {
      "type": "js",
      "deps": [
        "node_modules/toggle-selection/index"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/CityDB": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/cropperjs/dist/cropper": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-cropper/dist/react-cropper.umd": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/cropperjs/dist/cropper"
      ],
      "pkg": "p0"
    },
    "node_modules/exceljs/dist/exceljs.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/tinymce": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/icons/default/icons": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/icons/default/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/icons/default/icons"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/themes/silver/theme": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/themes/silver/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/themes/silver/theme"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/models/dom/model": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/advlist/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/advlist/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/advlist/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/autolink/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/autolink/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/autolink/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/autoresize/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/autoresize/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/autoresize/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/lists/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/lists/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/lists/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/link/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/link/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/link/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/image/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/image/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/image/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/charmap/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/charmap/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/charmap/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/preview/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/preview/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/preview/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/anchor/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/anchor/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/anchor/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/searchreplace/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/searchreplace/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/searchreplace/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/visualblocks/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/visualblocks/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/visualblocks/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/code/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/code/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/code/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/fullscreen/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/fullscreen/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/fullscreen/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/insertdatetime/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/insertdatetime/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/insertdatetime/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/media/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/media/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/media/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/table/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/table/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/table/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/help/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/help/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/help/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/wordcount/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/wordcount/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/wordcount/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/pagebreak/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/pagebreak/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/pagebreak/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/visualchars/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/visualchars/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/visualchars/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/template/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/template/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/template/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/nonbreaking/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/nonbreaking/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/nonbreaking/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/emoticons/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/emoticons/index": {
      "type": "js",
      "deps": [
        "node_modules/tinymce/plugins/emoticons/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/emoticons/js/emojis": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/tinymce/plugins/quickbars/plugin": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Tinymce": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/tinymce/tinymce",
        "node_modules/tinymce/icons/default/index",
        "node_modules/tinymce/themes/silver/index",
        "node_modules/tinymce/models/dom/model",
        "node_modules/tinymce/plugins/advlist/index",
        "node_modules/tinymce/plugins/autolink/index",
        "node_modules/tinymce/plugins/autoresize/index",
        "node_modules/tinymce/plugins/lists/index",
        "node_modules/tinymce/plugins/link/index",
        "node_modules/tinymce/plugins/image/index",
        "node_modules/tinymce/plugins/charmap/index",
        "node_modules/tinymce/plugins/preview/index",
        "node_modules/tinymce/plugins/anchor/index",
        "node_modules/tinymce/plugins/searchreplace/index",
        "node_modules/tinymce/plugins/visualblocks/index",
        "node_modules/tinymce/plugins/code/index",
        "node_modules/tinymce/plugins/fullscreen/index",
        "node_modules/tinymce/plugins/insertdatetime/index",
        "node_modules/tinymce/plugins/media/index",
        "node_modules/tinymce/plugins/table/index",
        "node_modules/tinymce/plugins/help/index",
        "node_modules/tinymce/plugins/wordcount/index",
        "node_modules/tinymce/plugins/pagebreak/index",
        "node_modules/tinymce/plugins/visualchars/index",
        "node_modules/tinymce/plugins/template/index",
        "node_modules/tinymce/plugins/nonbreaking/index",
        "node_modules/tinymce/plugins/emoticons/index",
        "node_modules/tinymce/plugins/emoticons/js/emojis",
        "node_modules/tinymce/plugins/quickbars/plugin"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/froala_editor.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/align.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/colors.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/char_counter.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/code_view.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/draggable.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/entities.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/font_family.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/font_size.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/forms.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/fullscreen.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/help.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/image.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/inline_class.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/inline_style.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/line_breaker.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/line_height.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/link.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/lists.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/paragraph_format.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/paragraph_style.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/print.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/quick_insert.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/quote.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/save.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/special_characters.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/table.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/url.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/video.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/plugins/word_paste.min": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/froala-editor/js/languages/zh_cn": {
      "type": "js",
      "deps": [
        "node_modules/froala-editor/js/froala_editor.min"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/RichText": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/froala-editor/js/froala_editor.min",
        "node_modules/froala-editor/js/plugins/align.min",
        "node_modules/froala-editor/js/plugins/colors.min",
        "node_modules/froala-editor/js/plugins/char_counter.min",
        "node_modules/froala-editor/js/plugins/code_view.min",
        "node_modules/froala-editor/js/plugins/draggable.min",
        "node_modules/froala-editor/js/plugins/entities.min",
        "node_modules/froala-editor/js/plugins/font_family.min",
        "node_modules/froala-editor/js/plugins/font_size.min",
        "node_modules/froala-editor/js/plugins/forms.min",
        "node_modules/froala-editor/js/plugins/fullscreen.min",
        "node_modules/froala-editor/js/plugins/help.min",
        "node_modules/froala-editor/js/plugins/image.min",
        "node_modules/froala-editor/js/plugins/inline_class.min",
        "node_modules/froala-editor/js/plugins/inline_style.min",
        "node_modules/froala-editor/js/plugins/line_breaker.min",
        "node_modules/froala-editor/js/plugins/line_height.min",
        "node_modules/froala-editor/js/plugins/link.min",
        "node_modules/froala-editor/js/plugins/lists.min",
        "node_modules/froala-editor/js/plugins/paragraph_format.min",
        "node_modules/froala-editor/js/plugins/paragraph_style.min",
        "node_modules/froala-editor/js/plugins/print.min",
        "node_modules/froala-editor/js/plugins/quick_insert.min",
        "node_modules/froala-editor/js/plugins/quote.min",
        "node_modules/froala-editor/js/plugins/save.min",
        "node_modules/froala-editor/js/plugins/special_characters.min",
        "node_modules/froala-editor/js/plugins/table.min",
        "node_modules/froala-editor/js/plugins/url.min",
        "node_modules/froala-editor/js/plugins/video.min",
        "node_modules/froala-editor/js/plugins/word_paste.min",
        "node_modules/froala-editor/js/languages/zh_cn"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/ColorPicker": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/react-color/lib/index",
        "node_modules/amis-ui/lib/components/icons",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/PopUp"
      ],
      "pkg": "p0"
    },
    "node_modules/echarts/dist/echarts": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/echarts-stat/dist/ecStat": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/echarts-stat/index": {
      "type": "js",
      "deps": [
        "node_modules/echarts-stat/dist/ecStat"
      ],
      "pkg": "p0"
    },
    "node_modules/echarts/node_modules/tslib/tslib": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/zrender/lib/core/platform": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/zrender/lib/core/util": {
      "type": "js",
      "deps": [
        "node_modules/zrender/lib/core/platform"
      ],
      "pkg": "p0"
    },
    "node_modules/echarts/extension/dataTool/gexf": {
      "type": "js",
      "deps": [
        "node_modules/echarts/node_modules/tslib/tslib",
        "node_modules/zrender/lib/core/util"
      ],
      "pkg": "p0"
    },
    "node_modules/echarts/extension/dataTool/prepareBoxplotData": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/echarts/extension/dataTool/index": {
      "type": "js",
      "deps": [
        "node_modules/echarts/node_modules/tslib/tslib",
        "node_modules/echarts/dist/echarts",
        "node_modules/echarts/extension/dataTool/gexf",
        "node_modules/echarts/extension/dataTool/prepareBoxplotData"
      ],
      "pkg": "p0"
    },
    "node_modules/echarts/extension/bmap/BMapCoordSys": {
      "type": "js",
      "deps": [
        "node_modules/echarts/dist/echarts"
      ],
      "pkg": "p0"
    },
    "node_modules/echarts/extension/bmap/BMapModel": {
      "type": "js",
      "deps": [
        "node_modules/echarts/node_modules/tslib/tslib",
        "node_modules/echarts/dist/echarts"
      ],
      "pkg": "p0"
    },
    "node_modules/echarts/extension/bmap/BMapView": {
      "type": "js",
      "deps": [
        "node_modules/echarts/node_modules/tslib/tslib",
        "node_modules/echarts/dist/echarts"
      ],
      "pkg": "p0"
    },
    "node_modules/echarts/extension/bmap/bmap": {
      "type": "js",
      "deps": [
        "node_modules/echarts/node_modules/tslib/tslib",
        "node_modules/echarts/dist/echarts",
        "node_modules/echarts/extension/bmap/BMapCoordSys",
        "node_modules/echarts/extension/bmap/BMapModel",
        "node_modules/echarts/extension/bmap/BMapView"
      ],
      "pkg": "p0"
    },
    "node_modules/mpegts.js/dist/mpegts": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/hls.js/dist/hls": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/Barcode": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE39/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE128/constants": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/Barcode",
        "node_modules/jsbarcode/bin/barcodes/CODE128/constants"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE128/auto": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/CODE128/constants"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128_AUTO": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128",
        "node_modules/jsbarcode/bin/barcodes/CODE128/auto"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128A": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128",
        "node_modules/jsbarcode/bin/barcodes/CODE128/constants"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128B": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128",
        "node_modules/jsbarcode/bin/barcodes/CODE128/constants"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128C": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128",
        "node_modules/jsbarcode/bin/barcodes/CODE128/constants"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/CODE128/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128_AUTO",
        "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128A",
        "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128B",
        "node_modules/jsbarcode/bin/barcodes/CODE128/CODE128C"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/constants": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/encoder": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/constants"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/constants",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/encoder",
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN13": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/constants",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN8": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN5": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/constants",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/encoder",
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN2": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/constants",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/encoder",
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/UPC": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/encoder",
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/UPCE": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/encoder",
        "node_modules/jsbarcode/bin/barcodes/Barcode",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/UPC"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/EAN_UPC/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN13",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN8",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN5",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/EAN2",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/UPC",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/UPCE"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/ITF/constants": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/ITF/ITF": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/ITF/constants",
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/ITF/ITF14": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/ITF/ITF"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/ITF/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/ITF/ITF",
        "node_modules/jsbarcode/bin/barcodes/ITF/ITF14"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/MSI/MSI": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/MSI/checksums": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/MSI/MSI10": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI",
        "node_modules/jsbarcode/bin/barcodes/MSI/checksums"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/MSI/MSI11": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI",
        "node_modules/jsbarcode/bin/barcodes/MSI/checksums"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/MSI/MSI1010": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI",
        "node_modules/jsbarcode/bin/barcodes/MSI/checksums"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/MSI/MSI1110": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI",
        "node_modules/jsbarcode/bin/barcodes/MSI/checksums"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/MSI/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI",
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI10",
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI11",
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI1010",
        "node_modules/jsbarcode/bin/barcodes/MSI/MSI1110"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/pharmacode/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/codabar/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/GenericBarcode/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/Barcode"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/barcodes/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/CODE39/index",
        "node_modules/jsbarcode/bin/barcodes/CODE128/index",
        "node_modules/jsbarcode/bin/barcodes/EAN_UPC/index",
        "node_modules/jsbarcode/bin/barcodes/ITF/index",
        "node_modules/jsbarcode/bin/barcodes/MSI/index",
        "node_modules/jsbarcode/bin/barcodes/pharmacode/index",
        "node_modules/jsbarcode/bin/barcodes/codabar/index",
        "node_modules/jsbarcode/bin/barcodes/GenericBarcode/index"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/help/merge": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/help/linearizeEncodings": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/help/fixOptions": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/help/optionsFromStrings": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/options/defaults": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/help/getOptionsFromElement": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/help/optionsFromStrings",
        "node_modules/jsbarcode/bin/options/defaults"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/renderers/shared": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/help/merge"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/renderers/canvas": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/help/merge",
        "node_modules/jsbarcode/bin/renderers/shared"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/renderers/svg": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/help/merge",
        "node_modules/jsbarcode/bin/renderers/shared"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/renderers/object": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/renderers/index": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/renderers/canvas",
        "node_modules/jsbarcode/bin/renderers/svg",
        "node_modules/jsbarcode/bin/renderers/object"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/exceptions/exceptions": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/help/getRenderProperties": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/help/getOptionsFromElement",
        "node_modules/jsbarcode/bin/renderers/index",
        "node_modules/jsbarcode/bin/exceptions/exceptions"
      ],
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/exceptions/ErrorHandler": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/jsbarcode/bin/JsBarcode": {
      "type": "js",
      "deps": [
        "node_modules/jsbarcode/bin/barcodes/index",
        "node_modules/jsbarcode/bin/help/merge",
        "node_modules/jsbarcode/bin/help/linearizeEncodings",
        "node_modules/jsbarcode/bin/help/fixOptions",
        "node_modules/jsbarcode/bin/help/getRenderProperties",
        "node_modules/jsbarcode/bin/help/optionsFromStrings",
        "node_modules/jsbarcode/bin/exceptions/ErrorHandler",
        "node_modules/jsbarcode/bin/exceptions/exceptions",
        "node_modules/jsbarcode/bin/options/defaults"
      ],
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/BarCode": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/jsbarcode/bin/JsBarcode"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/common/entities": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/uc.micro/categories/P/regex": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mdurl/encode": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mdurl/decode": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mdurl/format": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mdurl/parse": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mdurl/index": {
      "type": "js",
      "deps": [
        "node_modules/mdurl/encode",
        "node_modules/mdurl/decode",
        "node_modules/mdurl/format",
        "node_modules/mdurl/parse"
      ],
      "pkg": "p0"
    },
    "node_modules/uc.micro/properties/Any/regex": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/uc.micro/categories/Cc/regex": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/uc.micro/categories/Cf/regex": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/uc.micro/categories/Z/regex": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/uc.micro/index": {
      "type": "js",
      "deps": [
        "node_modules/uc.micro/properties/Any/regex",
        "node_modules/uc.micro/categories/Cc/regex",
        "node_modules/uc.micro/categories/Cf/regex",
        "node_modules/uc.micro/categories/P/regex",
        "node_modules/uc.micro/categories/Z/regex"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/common/utils": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/entities",
        "node_modules/uc.micro/categories/P/regex",
        "node_modules/mdurl/index",
        "node_modules/uc.micro/index"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/helpers/parse_link_label": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/helpers/parse_link_destination": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/helpers/parse_link_title": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/helpers/index": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/helpers/parse_link_label",
        "node_modules/markdown-it/lib/helpers/parse_link_destination",
        "node_modules/markdown-it/lib/helpers/parse_link_title"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/renderer": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/ruler": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_core/normalize": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_core/block": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_core/inline": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_core/linkify": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_core/replacements": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_core/smartquotes": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/token": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_core/state_core": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/token"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/parser_core": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/ruler",
        "node_modules/markdown-it/lib/rules_core/normalize",
        "node_modules/markdown-it/lib/rules_core/block",
        "node_modules/markdown-it/lib/rules_core/inline",
        "node_modules/markdown-it/lib/rules_core/linkify",
        "node_modules/markdown-it/lib/rules_core/replacements",
        "node_modules/markdown-it/lib/rules_core/smartquotes",
        "node_modules/markdown-it/lib/rules_core/state_core"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/table": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/code": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/fence": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/blockquote": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/hr": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/list": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/reference": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/common/html_blocks": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/common/html_re": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/html_block": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/html_blocks",
        "node_modules/markdown-it/lib/common/html_re"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/heading": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/lheading": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/paragraph": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_block/state_block": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/token",
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/parser_block": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/ruler",
        "node_modules/markdown-it/lib/rules_block/table",
        "node_modules/markdown-it/lib/rules_block/code",
        "node_modules/markdown-it/lib/rules_block/fence",
        "node_modules/markdown-it/lib/rules_block/blockquote",
        "node_modules/markdown-it/lib/rules_block/hr",
        "node_modules/markdown-it/lib/rules_block/list",
        "node_modules/markdown-it/lib/rules_block/reference",
        "node_modules/markdown-it/lib/rules_block/html_block",
        "node_modules/markdown-it/lib/rules_block/heading",
        "node_modules/markdown-it/lib/rules_block/lheading",
        "node_modules/markdown-it/lib/rules_block/paragraph",
        "node_modules/markdown-it/lib/rules_block/state_block"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/text": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/newline": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/escape": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/backticks": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/strikethrough": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/emphasis": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/link": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/image": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/autolink": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/html_inline": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/html_re"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/entity": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/entities",
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/balance_pairs": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/text_collapse": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/rules_inline/state_inline": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/token",
        "node_modules/markdown-it/lib/common/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/parser_inline": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/ruler",
        "node_modules/markdown-it/lib/rules_inline/text",
        "node_modules/markdown-it/lib/rules_inline/newline",
        "node_modules/markdown-it/lib/rules_inline/escape",
        "node_modules/markdown-it/lib/rules_inline/backticks",
        "node_modules/markdown-it/lib/rules_inline/strikethrough",
        "node_modules/markdown-it/lib/rules_inline/emphasis",
        "node_modules/markdown-it/lib/rules_inline/link",
        "node_modules/markdown-it/lib/rules_inline/image",
        "node_modules/markdown-it/lib/rules_inline/autolink",
        "node_modules/markdown-it/lib/rules_inline/html_inline",
        "node_modules/markdown-it/lib/rules_inline/entity",
        "node_modules/markdown-it/lib/rules_inline/balance_pairs",
        "node_modules/markdown-it/lib/rules_inline/text_collapse",
        "node_modules/markdown-it/lib/rules_inline/state_inline"
      ],
      "pkg": "p0"
    },
    "node_modules/linkify-it/lib/re": {
      "type": "js",
      "deps": [
        "node_modules/uc.micro/properties/Any/regex",
        "node_modules/uc.micro/categories/Cc/regex",
        "node_modules/uc.micro/categories/Z/regex",
        "node_modules/uc.micro/categories/P/regex"
      ],
      "pkg": "p0"
    },
    "node_modules/linkify-it/index": {
      "type": "js",
      "deps": [
        "node_modules/linkify-it/lib/re"
      ],
      "pkg": "p0"
    },
    "node_modules/punycode/punycode": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/presets/default": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/presets/zero": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/presets/commonmark": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/markdown-it/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/common/utils",
        "node_modules/markdown-it/lib/helpers/index",
        "node_modules/markdown-it/lib/renderer",
        "node_modules/markdown-it/lib/parser_core",
        "node_modules/markdown-it/lib/parser_block",
        "node_modules/markdown-it/lib/parser_inline",
        "node_modules/linkify-it/index",
        "node_modules/mdurl/index",
        "node_modules/punycode/punycode",
        "node_modules/markdown-it/lib/presets/default",
        "node_modules/markdown-it/lib/presets/zero",
        "node_modules/markdown-it/lib/presets/commonmark"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it/index": {
      "type": "js",
      "deps": [
        "node_modules/markdown-it/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/markdown-it-html5-media/lib/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/amis-ui/lib/components/Markdown": {
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/markdown-it/index",
        "node_modules/markdown-it-html5-media/lib/index"
      ],
      "pkg": "p0"
    },
    "node_modules/lodash/without": {
      "type": "js",
      "deps": [
        "node_modules/lodash/_baseDifference",
        "node_modules/lodash/_baseRest",
        "node_modules/lodash/isArrayLikeObject"
      ],
      "pkg": "p0"
    },
    "node_modules/codemirror/lib/codemirror": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/codemirror/mode/javascript/javascript": {
      "type": "js",
      "deps": [
        "node_modules/codemirror/lib/codemirror"
      ],
      "pkg": "p0"
    },
    "node_modules/codemirror/mode/xml/xml": {
      "type": "js",
      "deps": [
        "node_modules/codemirror/lib/codemirror"
      ],
      "pkg": "p0"
    },
    "node_modules/codemirror/mode/css/css": {
      "type": "js",
      "deps": [
        "node_modules/codemirror/lib/codemirror"
      ],
      "pkg": "p0"
    },
    "node_modules/codemirror/mode/htmlmixed/htmlmixed": {
      "type": "js",
      "deps": [
        "node_modules/codemirror/lib/codemirror",
        "node_modules/codemirror/mode/xml/xml",
        "node_modules/codemirror/mode/javascript/javascript",
        "node_modules/codemirror/mode/css/css"
      ],
      "pkg": "p0"
    },
    "node_modules/codemirror/addon/mode/simple": {
      "type": "js",
      "deps": [
        "node_modules/codemirror/lib/codemirror"
      ],
      "pkg": "p0"
    },
    "node_modules/codemirror/addon/mode/multiplex": {
      "type": "js",
      "deps": [
        "node_modules/codemirror/lib/codemirror"
      ],
      "pkg": "p0"
    },
    "node_modules/tiny-warning/dist/tiny-warning.cjs": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/mini-create-react-context/dist/cjs/index": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/@babel/runtime/helpers/inheritsLoose",
        "node_modules/prop-types/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/resolve-pathname/cjs/resolve-pathname.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/resolve-pathname/cjs/resolve-pathname": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/resolve-pathname/index": {
      "type": "js",
      "deps": [
        "node_modules/resolve-pathname/cjs/resolve-pathname.min",
        "node_modules/resolve-pathname/cjs/resolve-pathname"
      ],
      "pkg": "p0"
    },
    "node_modules/value-equal/cjs/value-equal.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/value-equal/cjs/value-equal": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/value-equal/index": {
      "type": "js",
      "deps": [
        "node_modules/value-equal/cjs/value-equal.min",
        "node_modules/value-equal/cjs/value-equal"
      ],
      "pkg": "p0"
    },
    "node_modules/tiny-invariant/dist/tiny-invariant.cjs": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-router/node_modules/history/cjs/history.min": {
      "type": "js",
      "deps": [
        "node_modules/resolve-pathname/index",
        "node_modules/value-equal/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs",
        "node_modules/tiny-invariant/dist/tiny-invariant.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router/node_modules/history/cjs/history": {
      "type": "js",
      "deps": [
        "node_modules/resolve-pathname/index",
        "node_modules/value-equal/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs",
        "node_modules/tiny-invariant/dist/tiny-invariant.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router/node_modules/history/index": {
      "type": "js",
      "deps": [
        "node_modules/react-router/node_modules/history/cjs/history.min",
        "node_modules/react-router/node_modules/history/cjs/history"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router/node_modules/isarray/index": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-router/node_modules/path-to-regexp/index": {
      "type": "js",
      "deps": [
        "node_modules/react-router/node_modules/isarray/index"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router/node_modules/react-is/cjs/react-is.production.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-router/node_modules/react-is/cjs/react-is.development": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/react-router/node_modules/react-is/index": {
      "type": "js",
      "deps": [
        "node_modules/react-router/node_modules/react-is/cjs/react-is.production.min",
        "node_modules/react-router/node_modules/react-is/cjs/react-is.development"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router/cjs/react-router.min": {
      "type": "js",
      "deps": [
        "node_modules/mini-create-react-context/dist/cjs/index",
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs",
        "node_modules/react-router/node_modules/history/index",
        "node_modules/tiny-invariant/dist/tiny-invariant.cjs",
        "node_modules/react-router/node_modules/path-to-regexp/index",
        "node_modules/react-router/node_modules/react-is/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router/cjs/react-router": {
      "type": "js",
      "deps": [
        "node_modules/mini-create-react-context/dist/cjs/index",
        "node_modules/react/index",
        "node_modules/prop-types/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs",
        "node_modules/react-router/node_modules/history/index",
        "node_modules/tiny-invariant/dist/tiny-invariant.cjs",
        "node_modules/react-router/node_modules/path-to-regexp/index",
        "node_modules/react-router/node_modules/react-is/index",
        "node_modules/hoist-non-react-statics/dist/hoist-non-react-statics.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router/index": {
      "type": "js",
      "deps": [
        "node_modules/react-router/cjs/react-router.min",
        "node_modules/react-router/cjs/react-router"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router-dom/node_modules/history/cjs/history.min": {
      "type": "js",
      "deps": [
        "node_modules/resolve-pathname/index",
        "node_modules/value-equal/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs",
        "node_modules/tiny-invariant/dist/tiny-invariant.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router-dom/node_modules/history/cjs/history": {
      "type": "js",
      "deps": [
        "node_modules/resolve-pathname/index",
        "node_modules/value-equal/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs",
        "node_modules/tiny-invariant/dist/tiny-invariant.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router-dom/node_modules/history/index": {
      "type": "js",
      "deps": [
        "node_modules/react-router-dom/node_modules/history/cjs/history.min",
        "node_modules/react-router-dom/node_modules/history/cjs/history"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router-dom/cjs/react-router-dom.min": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/react-router/index",
        "node_modules/react-router-dom/node_modules/history/index",
        "node_modules/prop-types/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs",
        "node_modules/tiny-invariant/dist/tiny-invariant.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router-dom/cjs/react-router-dom": {
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/react-router/index",
        "node_modules/react-router-dom/node_modules/history/index",
        "node_modules/prop-types/index",
        "node_modules/tiny-warning/dist/tiny-warning.cjs",
        "node_modules/tiny-invariant/dist/tiny-invariant.cjs"
      ],
      "pkg": "p0"
    },
    "node_modules/react-router-dom/index": {
      "type": "js",
      "deps": [
        "node_modules/react-router-dom/cjs/react-router-dom.min",
        "node_modules/react-router-dom/cjs/react-router-dom"
      ],
      "pkg": "p0"
    },
    "node_modules/papaparse/papaparse.min": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/qs/lib/utils": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/qs/lib/formats": {
      "type": "js",
      "pkg": "p0"
    },
    "node_modules/qs/lib/stringify": {
      "type": "js",
      "deps": [
        "node_modules/qs/lib/utils",
        "node_modules/qs/lib/formats"
      ],
      "pkg": "p0"
    },
    "node_modules/qs/lib/parse": {
      "type": "js",
      "deps": [
        "node_modules/qs/lib/utils"
      ],
      "pkg": "p0"
    },
    "node_modules/qs/lib/index": {
      "type": "js",
      "deps": [
        "node_modules/qs/lib/stringify",
        "node_modules/qs/lib/parse",
        "node_modules/qs/lib/formats"
      ],
      "pkg": "p0"
    },
    "loadMonacoEditor.ts": {
      "url": "/loadMonacoEditor.js",
      "type": "js"
    },
    "node_modules/amis-editor/lib/locale/en-US": {
      "url": "/node_modules/amis-editor/lib/locale/en-US.js",
      "type": "js",
      "deps": [
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/locale/zh-CN": {
      "url": "/node_modules/amis-editor/lib/locale/zh-CN.js",
      "type": "js",
      "deps": [
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/event-control/comp-action-select": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/comp-action-select.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/event-control/helper": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/helper.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis/lib/index",
        "node_modules/i18n-runtime/lib/index",
        "node_modules/lodash/without",
        "node_modules/amis-editor/lib/renderer/event-control/comp-action-select"
      ]
    },
    "node_modules/amis-editor/lib/component/BaseControl": {
      "url": "/node_modules/amis-editor/lib/component/BaseControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/flatten",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/component/InputComponentName": {
      "url": "/node_modules/amis-editor/lib/component/InputComponentName.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/textarea-formula/utils": {
      "url": "/node_modules/amis-editor/lib/renderer/textarea-formula/utils.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-core/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/FormulaControl": {
      "url": "/node_modules/amis-editor/lib/renderer/FormulaControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isNumber",
        "node_modules/lodash/isBoolean",
        "node_modules/lodash/isPlainObject",
        "node_modules/lodash/isArray",
        "node_modules/lodash/isString",
        "node_modules/lodash/omit",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-ui/lib/components/formula/Editor",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/textarea-formula/utils",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/tpl/common": {
      "url": "/node_modules/amis-editor/lib/tpl/common.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/lodash/flatten",
        "node_modules/lodash/lodash",
        "node_modules/amis-editor/lib/component/InputComponentName",
        "node_modules/amis-editor/lib/renderer/FormulaControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/tpl/horizontal": {
      "url": "/node_modules/amis-editor/lib/tpl/horizontal.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/tpl/api": {
      "url": "/node_modules/amis-editor/lib/tpl/api.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/tpl/options": {
      "url": "/node_modules/amis-editor/lib/tpl/options.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/assign",
        "node_modules/lodash/cloneDeep",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/tpl/validations": {
      "url": "/node_modules/amis-editor/lib/tpl/validations.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis/lib/index",
        "node_modules/lodash/find",
        "node_modules/lodash/reduce",
        "node_modules/lodash/forEach",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/tpl/style": {
      "url": "/node_modules/amis-editor/lib/tpl/style.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/kebabCase",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/tpl/layout": {
      "url": "/node_modules/amis-editor/lib/tpl/layout.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/isNumber",
        "node_modules/lodash/isString",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Others/Action": {
      "url": "/node_modules/amis-editor/lib/plugin/Others/Action.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-ui/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Others/TableCell": {
      "url": "/node_modules/amis-editor/lib/plugin/Others/TableCell.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputArray": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputArray.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/react/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/ButtonGroupSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ButtonGroupSelect.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/ButtonToolbar": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ButtonToolbar.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/validator": {
      "url": "/node_modules/amis-editor/lib/validator.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/ChainedSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ChainedSelect.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Checkbox": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Checkbox.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Checkboxes": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Checkboxes.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputCity": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputCity.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputColor": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputColor.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/tinycolor2/cjs/tinycolor",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Combo": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Combo.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/ConditionBuilder": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ConditionBuilder.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-ui/lib/components/condition-builder/config",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Control": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Control.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputDate": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputDate.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/renderer/FormulaControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputDateRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputDateRange.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/renderer/FormulaControl",
        "node_modules/amis-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputDateTime": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputDateTime.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDate",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputDateTimeRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputDateTimeRange.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/DiffEditor": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/DiffEditor.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/renderers/Form/Editor",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/CodeEditor": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/CodeEditor.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/renderers/Form/Editor",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputText": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputText.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputEmail": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputEmail.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputText",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/FieldSet": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/FieldSet.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputFile": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputFile.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Form": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Form.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Formula": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Formula.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Group": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Group.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Hidden": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Hidden.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputImage": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputImage.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/validator",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputGroup": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputGroup.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Item": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Item.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/find",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/ListSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/ListSelect.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/LocationPicker": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/LocationPicker.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/UUID": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/UUID.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/MatrixCheckboxes": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/MatrixCheckboxes.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputMonth": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputMonth.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDate",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputMonthRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputMonthRange.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/NestedSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/NestedSelect.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputNumber": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputNumber.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputPassword": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputPassword.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputText",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputQuarter": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputQuarter.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDate",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputQuarterRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputQuarterRange.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Picker": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Picker.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Radios": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Radios.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputRange.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputRating": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputRating.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputRepeat": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputRepeat.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputRichText": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputRichText.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Select": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Select.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Static": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Static.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputSubForm": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputSubForm.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Switch": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Switch.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTable": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTable.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/lodash/flatten",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTag": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTag.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/TabsTransfer": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/TabsTransfer.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Textarea": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Textarea.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/validator",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/Transfer": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/Transfer.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/validator",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTime": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTime.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDate",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTimeRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTimeRange.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputTree": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputTree.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/validator",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/TreeSelect": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/TreeSelect.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/validator",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputURL": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputURL.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputText",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputYear": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputYear.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDate",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputYearRange": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputYearRange.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputExcel": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputExcel.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Form/InputKV": {
      "url": "/node_modules/amis-editor/lib/plugin/Form/InputKV.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Alert": {
      "url": "/node_modules/amis-editor/lib/plugin/Alert.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Audio": {
      "url": "/node_modules/amis-editor/lib/plugin/Audio.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Avatar": {
      "url": "/node_modules/amis-editor/lib/plugin/Avatar.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Button": {
      "url": "/node_modules/amis-editor/lib/plugin/Button.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/ButtonGroup": {
      "url": "/node_modules/amis-editor/lib/plugin/ButtonGroup.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Breadcrumb": {
      "url": "/node_modules/amis-editor/lib/plugin/Breadcrumb.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Card": {
      "url": "/node_modules/amis-editor/lib/plugin/Card.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-ui/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/flatten",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Card2": {
      "url": "/node_modules/amis-editor/lib/plugin/Card2.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Cards": {
      "url": "/node_modules/amis-editor/lib/plugin/Cards.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Carousel": {
      "url": "/node_modules/amis-editor/lib/plugin/Carousel.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Chart": {
      "url": "/node_modules/amis-editor/lib/plugin/Chart.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Collapse": {
      "url": "/node_modules/amis-editor/lib/plugin/Collapse.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/CollapseGroup": {
      "url": "/node_modules/amis-editor/lib/plugin/CollapseGroup.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Container": {
      "url": "/node_modules/amis-editor/lib/plugin/Container.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/CRUD": {
      "url": "/node_modules/amis-editor/lib/plugin/CRUD.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/lodash/get",
        "node_modules/lodash/cloneDeep",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Custom": {
      "url": "/node_modules/amis-editor/lib/plugin/Custom.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/CustomRegion": {
      "url": "/node_modules/amis-editor/lib/plugin/CustomRegion.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/isArray",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Date": {
      "url": "/node_modules/amis-editor/lib/plugin/Date.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Datetime": {
      "url": "/node_modules/amis-editor/lib/plugin/Datetime.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Date",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Dialog": {
      "url": "/node_modules/amis-editor/lib/plugin/Dialog.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Divider": {
      "url": "/node_modules/amis-editor/lib/plugin/Divider.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Drawer": {
      "url": "/node_modules/amis-editor/lib/plugin/Drawer.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/amis-editor/lib/plugin/Dialog",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/DropDownButton": {
      "url": "/node_modules/amis-editor/lib/plugin/DropDownButton.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Each": {
      "url": "/node_modules/amis-editor/lib/plugin/Each.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Flex": {
      "url": "/node_modules/amis-editor/lib/plugin/Flex.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Grid": {
      "url": "/node_modules/amis-editor/lib/plugin/Grid.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/HBox": {
      "url": "/node_modules/amis-editor/lib/plugin/HBox.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/IFrame": {
      "url": "/node_modules/amis-editor/lib/plugin/IFrame.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Image": {
      "url": "/node_modules/amis-editor/lib/plugin/Image.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Images": {
      "url": "/node_modules/amis-editor/lib/plugin/Images.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Json": {
      "url": "/node_modules/amis-editor/lib/plugin/Json.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Link": {
      "url": "/node_modules/amis-editor/lib/plugin/Link.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/List": {
      "url": "/node_modules/amis-editor/lib/plugin/List.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/ListItem": {
      "url": "/node_modules/amis-editor/lib/plugin/ListItem.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Log": {
      "url": "/node_modules/amis-editor/lib/plugin/Log.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Mapping": {
      "url": "/node_modules/amis-editor/lib/plugin/Mapping.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Markdown": {
      "url": "/node_modules/amis-editor/lib/plugin/Markdown.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Nav": {
      "url": "/node_modules/amis-editor/lib/plugin/Nav.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Operation": {
      "url": "/node_modules/amis-editor/lib/plugin/Operation.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Page": {
      "url": "/node_modules/amis-editor/lib/plugin/Page.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Pagination": {
      "url": "/node_modules/amis-editor/lib/plugin/Pagination.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Panel": {
      "url": "/node_modules/amis-editor/lib/plugin/Panel.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Plain": {
      "url": "/node_modules/amis-editor/lib/plugin/Plain.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Progress": {
      "url": "/node_modules/amis-editor/lib/plugin/Progress.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Property": {
      "url": "/node_modules/amis-editor/lib/plugin/Property.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/QRCode": {
      "url": "/node_modules/amis-editor/lib/plugin/QRCode.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Reset": {
      "url": "/node_modules/amis-editor/lib/plugin/Reset.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Button",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Service": {
      "url": "/node_modules/amis-editor/lib/plugin/Service.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Status": {
      "url": "/node_modules/amis-editor/lib/plugin/Status.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Steps": {
      "url": "/node_modules/amis-editor/lib/plugin/Steps.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Sparkline": {
      "url": "/node_modules/amis-editor/lib/plugin/Sparkline.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Submit": {
      "url": "/node_modules/amis-editor/lib/plugin/Submit.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Button",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Table": {
      "url": "/node_modules/amis-editor/lib/plugin/Table.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Table2": {
      "url": "/node_modules/amis-editor/lib/plugin/Table2.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/TableCell2": {
      "url": "/node_modules/amis-editor/lib/plugin/TableCell2.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Tabs": {
      "url": "/node_modules/amis-editor/lib/plugin/Tabs.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/findIndex",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Tasks": {
      "url": "/node_modules/amis-editor/lib/plugin/Tasks.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Time": {
      "url": "/node_modules/amis-editor/lib/plugin/Time.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Date",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Timeline": {
      "url": "/node_modules/amis-editor/lib/plugin/Timeline.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Tpl": {
      "url": "/node_modules/amis-editor/lib/plugin/Tpl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Tag": {
      "url": "/node_modules/amis-editor/lib/plugin/Tag.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/AnchorNav": {
      "url": "/node_modules/amis-editor/lib/plugin/AnchorNav.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/findIndex",
        "node_modules/amis-ui/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Video": {
      "url": "/node_modules/amis-editor/lib/plugin/Video.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Wizard": {
      "url": "/node_modules/amis-editor/lib/plugin/Wizard.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Wrapper": {
      "url": "/node_modules/amis-editor/lib/plugin/Wrapper.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/TooltipWrapper": {
      "url": "/node_modules/amis-editor/lib/plugin/TooltipWrapper.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/component/TableViewEditor": {
      "url": "/node_modules/amis-editor/lib/component/TableViewEditor.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/isEqual",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/TableView": {
      "url": "/node_modules/amis-editor/lib/plugin/TableView.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/component/TableViewEditor",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/CodeView": {
      "url": "/node_modules/amis-editor/lib/plugin/CodeView.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/WebComponent": {
      "url": "/node_modules/amis-editor/lib/plugin/WebComponent.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/@webcomponents/webcomponentsjs/custom-elements-es5-adapter",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/CRUD2": {
      "url": "/node_modules/amis-editor/lib/plugin/CRUD2.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/lodash/cloneDeep",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/ColumnToggler": {
      "url": "/node_modules/amis-editor/lib/plugin/ColumnToggler.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Layout/Layout_sorption_container": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/Layout_sorption_container.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Layout/Layout_fixed": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/Layout_fixed.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Layout/Layout1_2_v4": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/Layout1_2_v4.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Layout/FlexPluginBase",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/plugin/Layout/Layout_free_container": {
      "url": "/node_modules/amis-editor/lib/plugin/Layout/Layout_free_container.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/plugin/Container",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/OptionControl": {
      "url": "/node_modules/amis-editor/lib/renderer/OptionControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/classnames/index",
        "node_modules/lodash/uniqBy",
        "node_modules/lodash/omit",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/amis/lib/index",
        "node_modules/amis-ui/lib/components/Select",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/MapSourceControl": {
      "url": "/node_modules/amis-editor/lib/renderer/MapSourceControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/debounce",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/TimelineItemControl": {
      "url": "/node_modules/amis-editor/lib/renderer/TimelineItemControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/classnames/index",
        "node_modules/lodash/uniqBy",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/APIControl": {
      "url": "/node_modules/amis-editor/lib/renderer/APIControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/mergeWith",
        "node_modules/lodash/cloneDeep",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/ValidationItem": {
      "url": "/node_modules/amis-editor/lib/renderer/ValidationItem.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/mobx-state-tree/dist/mobx-state-tree",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/ValidationControl": {
      "url": "/node_modules/amis-editor/lib/renderer/ValidationControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/groupBy",
        "node_modules/lodash/remove",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/renderer/ValidationItem",
        "node_modules/amis-editor/lib/validator",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/SwitchMoreControl": {
      "url": "/node_modules/amis-editor/lib/renderer/SwitchMoreControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/StatusControl": {
      "url": "/node_modules/amis-editor/lib/renderer/StatusControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/ExpressionFormulaControl": {
      "url": "/node_modules/amis-editor/lib/renderer/ExpressionFormulaControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-ui/lib/components/formula/Editor",
        "node_modules/amis-editor/lib/renderer/textarea-formula/utils",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/textarea-formula/plugin": {
      "url": "/node_modules/amis-editor/lib/renderer/textarea-formula/plugin.js",
      "type": "js",
      "deps": [
        "node_modules/amis-ui/lib/components/formula/Editor"
      ]
    },
    "node_modules/amis-editor/lib/renderer/textarea-formula/FormulaPicker": {
      "url": "/node_modules/amis-editor/lib/renderer/textarea-formula/FormulaPicker.js",
      "type": "js",
      "deps": [
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/classnames/index",
        "node_modules/amis-ui/lib/components/formula/Editor"
      ]
    },
    "node_modules/amis-editor/lib/renderer/textarea-formula/TextareaFormulaControl": {
      "url": "/node_modules/amis-editor/lib/renderer/textarea-formula/TextareaFormulaControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-ui/lib/components/CodeMirror",
        "node_modules/amis-editor/lib/renderer/textarea-formula/plugin",
        "node_modules/amis-editor/lib/renderer/textarea-formula/FormulaPicker",
        "node_modules/lodash/debounce",
        "node_modules/amis-editor/lib/renderer/textarea-formula/utils",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/DateShortCutControl": {
      "url": "/node_modules/amis-editor/lib/renderer/DateShortCutControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/react-dom/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/BadgeControl": {
      "url": "/node_modules/amis-editor/lib/renderer/BadgeControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/util": {
      "url": "/node_modules/amis-editor/lib/util.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/lodash/isString"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/BoxModel": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/BoxModel.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/classnames/index",
        "node_modules/react/index",
        "node_modules/mobx-react/dist/index",
        "node_modules/lodash/camelCase",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/util"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/font-family": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/font-family.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/Font": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/Font.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/pick",
        "node_modules/lodash/mapValues",
        "node_modules/amis-editor/lib/renderer/style-control/font-family",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/Border": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/Border.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/classnames/index",
        "node_modules/react/index",
        "node_modules/lodash/camelCase",
        "node_modules/mobx-react/dist/index",
        "node_modules/amis/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/transformation": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/transformation.js",
      "type": "js",
      "deps": [
        "node_modules/lodash/isNumber",
        "node_modules/lodash/compact"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/BoxShadow": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/BoxShadow.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/mapValues",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor/lib/renderer/style-control/transformation",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/Background": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/Background.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/axios/index",
        "node_modules/classnames/index",
        "node_modules/lodash/pick",
        "node_modules/lodash/omit",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/Display": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/Display.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/lodash/pick",
        "node_modules/lodash/mapValues",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/InsetBoxModel": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/InsetBoxModel.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/classnames/index",
        "node_modules/react/index",
        "node_modules/mobx-react/dist/index",
        "node_modules/lodash/camelCase",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/util"
      ]
    },
    "node_modules/amis-editor/lib/renderer/RangePartsControl": {
      "url": "/node_modules/amis-editor/lib/renderer/RangePartsControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/DataBindingControl": {
      "url": "/node_modules/amis-editor/lib/renderer/DataBindingControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/match-sorter/dist/match-sorter.cjs",
        "node_modules/classnames/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/DataMappingControl": {
      "url": "/node_modules/amis-editor/lib/renderer/DataMappingControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/DataPickerControl": {
      "url": "/node_modules/amis-editor/lib/renderer/DataPickerControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor-core/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/GoConfigControl": {
      "url": "/node_modules/amis-editor/lib/renderer/GoConfigControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/FeatureControl": {
      "url": "/node_modules/amis-editor/lib/renderer/FeatureControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/classnames/index",
        "node_modules/amis/lib/index",
        "node_modules/lodash/lodash",
        "node_modules/amis-editor/lib/renderer/GoConfigControl",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/event-control/action-config-panel": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/action-config-panel.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/event-control/action-config-dialog": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/action-config-dialog.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/react/index",
        "node_modules/amis-editor/lib/renderer/event-control/action-config-panel",
        "node_modules/amis-editor/lib/renderer/event-control/comp-action-select",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/event-control/index": {
      "url": "/node_modules/amis-editor/lib/renderer/event-control/index.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/react-dom/index",
        "node_modules/classnames/index",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/amis/lib/index",
        "node_modules/lodash/cloneDeep",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-editor/lib/renderer/event-control/action-config-dialog",
        "node_modules/amis-editor/lib/renderer/event-control/helper",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/TreeOptionControl": {
      "url": "/node_modules/amis-editor/lib/renderer/TreeOptionControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/classnames/index",
        "node_modules/lodash/cloneDeep",
        "node_modules/lodash/get",
        "node_modules/lodash/set",
        "node_modules/sortablejs/Sortable.min",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/TransferTableControl": {
      "url": "/node_modules/amis-editor/lib/renderer/TransferTableControl.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis/lib/index",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/classnames/index",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/renderer/style-control/ThemeClassName": {
      "url": "/node_modules/amis-editor/lib/renderer/style-control/ThemeClassName.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/react/index",
        "node_modules/amis-ui/lib/index",
        "node_modules/amis-core/lib/index",
        "node_modules/amis-postcss/lib/index.min",
        "node_modules/lodash/lodash",
        "node_modules/i18n-runtime/lib/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/audio.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/audio.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/custom.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/custom.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/each.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/each.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/form.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/form.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/nav.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/nav.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/qrcode.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/qrcode.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/service.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/service.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/table.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/table.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/tasks.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/tasks.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/video.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/video.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/feat/wizard.svg": {
      "url": "/node_modules/amis-editor/lib/icons/feat/wizard.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/anchor-nav.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/anchor-nav.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/collapse.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/collapse.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/container.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/container.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/flex-container.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/flex-container.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/form-group.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/form-group.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/grid.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/grid.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/iframe.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/iframe.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/panel.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/panel.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/table-view.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/table-view.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/tabs.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/tabs.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/web-component.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/web-component.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/container/tooltip-wrapper.svg": {
      "url": "/node_modules/amis-editor/lib/icons/container/tooltip-wrapper.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/btn/btn-group.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/btn-group.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/btn/btn-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/btn-select.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/btn/btn-toolbar.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/btn-toolbar.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/btn/button.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/button.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/btn/dropdown-btn.svg": {
      "url": "/node_modules/amis-editor/lib/icons/btn/dropdown-btn.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/breadcrumb.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/breadcrumb.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/card.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/card.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/cards.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/cards.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/carousel.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/carousel.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/chart.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/chart.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/code.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/code.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/date.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/date.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/datetime.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/datetime.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/time.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/time.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/image.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/image.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/images.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/images.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/list.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/list.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/log.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/log.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/mapping.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/mapping.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/avatar.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/avatar.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/sparkline.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/sparkline.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/progress.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/progress.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/json-view.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/json-view.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/markdown.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/markdown.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/steps.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/steps.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/status.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/status.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/plain.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/plain.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/show/URL.svg": {
      "url": "/node_modules/amis-editor/lib/icons/show/URL.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/chained-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/chained-select.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/checkbox.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/checkbox.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/checkboxes.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/checkboxes.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/combo.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/combo.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/condition-builder.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/condition-builder.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/diff-editor.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/diff-editor.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/editor.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/editor.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/hidden.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/hidden.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-city.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-city.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-color.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-color.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-time-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-time-range.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-date-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-date-range.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-month-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-month-range.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-quarter-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-quarter-range.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-date.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-date.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-datetime.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-datetime.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-email.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-email.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-excel.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-excel.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-file.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-file.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-group.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-group.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-image.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-image.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-kv.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-kv.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-number.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-number.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-password.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-password.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-range.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-range.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-rating.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-rating.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-repeat.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-repeat.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-rich-text.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-rich-text.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-tag.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-tag.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-text.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-text.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-time.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-time.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-tree.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-tree.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-url.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-url.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/input-year.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/input-year.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/list-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/list-select.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/location-picker.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/location-picker.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/matrix-checkboxes.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/matrix-checkboxes.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/month.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/month.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/nested-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/nested-select.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/picker.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/picker.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/quarter.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/quarter.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/radios.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/radios.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/select.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/static.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/static.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/sub-form.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/sub-form.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/switch.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/switch.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/tabs-transfer.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/tabs-transfer.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/textarea.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/textarea.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/transfer.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/transfer.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/tree-select.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/tree-select.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/uuid.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/uuid.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/formula.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/formula.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/form/inputArray.svg": {
      "url": "/node_modules/amis-editor/lib/icons/form/inputArray.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/other/property-sheet.svg": {
      "url": "/node_modules/amis-editor/lib/icons/other/property-sheet.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/other/tooltip.svg": {
      "url": "/node_modules/amis-editor/lib/icons/other/tooltip.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/other/divider.svg": {
      "url": "/node_modules/amis-editor/lib/icons/other/divider.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-absolute.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-absolute.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-fixed.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-fixed.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-1with2.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-1with2.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-2cols.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-2cols.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-2row.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-2row.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-2with1.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-2with1.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-3cols.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-3cols.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-3row.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-3row.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout-full.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout-full.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout1-2.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout1-2.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout2-1.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout2-1.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout3-1.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout3-1.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/layout/layout3-2.svg": {
      "url": "/node_modules/amis-editor/lib/icons/layout/layout3-2.svg.js",
      "type": "js",
      "deps": [
        "node_modules/react/index"
      ]
    },
    "node_modules/amis-editor/lib/icons/index": {
      "url": "/node_modules/amis-editor/lib/icons/index.js",
      "type": "js",
      "deps": [
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/icons/feat/audio.svg",
        "node_modules/amis-editor/lib/icons/feat/custom.svg",
        "node_modules/amis-editor/lib/icons/feat/each.svg",
        "node_modules/amis-editor/lib/icons/feat/form.svg",
        "node_modules/amis-editor/lib/icons/feat/nav.svg",
        "node_modules/amis-editor/lib/icons/feat/qrcode.svg",
        "node_modules/amis-editor/lib/icons/feat/service.svg",
        "node_modules/amis-editor/lib/icons/feat/table.svg",
        "node_modules/amis-editor/lib/icons/feat/tasks.svg",
        "node_modules/amis-editor/lib/icons/feat/video.svg",
        "node_modules/amis-editor/lib/icons/feat/wizard.svg",
        "node_modules/amis-editor/lib/icons/container/anchor-nav.svg",
        "node_modules/amis-editor/lib/icons/container/collapse.svg",
        "node_modules/amis-editor/lib/icons/container/container.svg",
        "node_modules/amis-editor/lib/icons/container/flex-container.svg",
        "node_modules/amis-editor/lib/icons/container/form-group.svg",
        "node_modules/amis-editor/lib/icons/container/grid.svg",
        "node_modules/amis-editor/lib/icons/container/iframe.svg",
        "node_modules/amis-editor/lib/icons/container/panel.svg",
        "node_modules/amis-editor/lib/icons/container/table-view.svg",
        "node_modules/amis-editor/lib/icons/container/tabs.svg",
        "node_modules/amis-editor/lib/icons/container/web-component.svg",
        "node_modules/amis-editor/lib/icons/container/tooltip-wrapper.svg",
        "node_modules/amis-editor/lib/icons/btn/btn-group.svg",
        "node_modules/amis-editor/lib/icons/btn/btn-select.svg",
        "node_modules/amis-editor/lib/icons/btn/btn-toolbar.svg",
        "node_modules/amis-editor/lib/icons/btn/button.svg",
        "node_modules/amis-editor/lib/icons/btn/dropdown-btn.svg",
        "node_modules/amis-editor/lib/icons/show/breadcrumb.svg",
        "node_modules/amis-editor/lib/icons/show/card.svg",
        "node_modules/amis-editor/lib/icons/show/cards.svg",
        "node_modules/amis-editor/lib/icons/show/carousel.svg",
        "node_modules/amis-editor/lib/icons/show/chart.svg",
        "node_modules/amis-editor/lib/icons/show/code.svg",
        "node_modules/amis-editor/lib/icons/show/date.svg",
        "node_modules/amis-editor/lib/icons/show/datetime.svg",
        "node_modules/amis-editor/lib/icons/show/time.svg",
        "node_modules/amis-editor/lib/icons/show/image.svg",
        "node_modules/amis-editor/lib/icons/show/images.svg",
        "node_modules/amis-editor/lib/icons/show/list.svg",
        "node_modules/amis-editor/lib/icons/show/log.svg",
        "node_modules/amis-editor/lib/icons/show/mapping.svg",
        "node_modules/amis-editor/lib/icons/show/avatar.svg",
        "node_modules/amis-editor/lib/icons/show/sparkline.svg",
        "node_modules/amis-editor/lib/icons/show/progress.svg",
        "node_modules/amis-editor/lib/icons/show/json-view.svg",
        "node_modules/amis-editor/lib/icons/show/markdown.svg",
        "node_modules/amis-editor/lib/icons/show/steps.svg",
        "node_modules/amis-editor/lib/icons/show/status.svg",
        "node_modules/amis-editor/lib/icons/show/plain.svg",
        "node_modules/amis-editor/lib/icons/show/URL.svg",
        "node_modules/amis-editor/lib/icons/form/chained-select.svg",
        "node_modules/amis-editor/lib/icons/form/checkbox.svg",
        "node_modules/amis-editor/lib/icons/form/checkboxes.svg",
        "node_modules/amis-editor/lib/icons/form/combo.svg",
        "node_modules/amis-editor/lib/icons/form/condition-builder.svg",
        "node_modules/amis-editor/lib/icons/form/diff-editor.svg",
        "node_modules/amis-editor/lib/icons/form/editor.svg",
        "node_modules/amis-editor/lib/icons/form/hidden.svg",
        "node_modules/amis-editor/lib/icons/form/input-city.svg",
        "node_modules/amis-editor/lib/icons/form/input-color.svg",
        "node_modules/amis-editor/lib/icons/form/input-time-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-date-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-month-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-quarter-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-date.svg",
        "node_modules/amis-editor/lib/icons/form/input-datetime.svg",
        "node_modules/amis-editor/lib/icons/form/input-email.svg",
        "node_modules/amis-editor/lib/icons/form/input-excel.svg",
        "node_modules/amis-editor/lib/icons/form/input-file.svg",
        "node_modules/amis-editor/lib/icons/form/input-group.svg",
        "node_modules/amis-editor/lib/icons/form/input-image.svg",
        "node_modules/amis-editor/lib/icons/form/input-kv.svg",
        "node_modules/amis-editor/lib/icons/form/input-number.svg",
        "node_modules/amis-editor/lib/icons/form/input-password.svg",
        "node_modules/amis-editor/lib/icons/form/input-range.svg",
        "node_modules/amis-editor/lib/icons/form/input-rating.svg",
        "node_modules/amis-editor/lib/icons/form/input-repeat.svg",
        "node_modules/amis-editor/lib/icons/form/input-rich-text.svg",
        "node_modules/amis-editor/lib/icons/form/input-tag.svg",
        "node_modules/amis-editor/lib/icons/form/input-text.svg",
        "node_modules/amis-editor/lib/icons/form/input-time.svg",
        "node_modules/amis-editor/lib/icons/form/input-tree.svg",
        "node_modules/amis-editor/lib/icons/form/input-url.svg",
        "node_modules/amis-editor/lib/icons/form/input-year.svg",
        "node_modules/amis-editor/lib/icons/form/list-select.svg",
        "node_modules/amis-editor/lib/icons/form/location-picker.svg",
        "node_modules/amis-editor/lib/icons/form/matrix-checkboxes.svg",
        "node_modules/amis-editor/lib/icons/form/month.svg",
        "node_modules/amis-editor/lib/icons/form/nested-select.svg",
        "node_modules/amis-editor/lib/icons/form/picker.svg",
        "node_modules/amis-editor/lib/icons/form/quarter.svg",
        "node_modules/amis-editor/lib/icons/form/radios.svg",
        "node_modules/amis-editor/lib/icons/form/select.svg",
        "node_modules/amis-editor/lib/icons/form/static.svg",
        "node_modules/amis-editor/lib/icons/form/sub-form.svg",
        "node_modules/amis-editor/lib/icons/form/switch.svg",
        "node_modules/amis-editor/lib/icons/form/tabs-transfer.svg",
        "node_modules/amis-editor/lib/icons/form/textarea.svg",
        "node_modules/amis-editor/lib/icons/form/transfer.svg",
        "node_modules/amis-editor/lib/icons/form/tree-select.svg",
        "node_modules/amis-editor/lib/icons/form/uuid.svg",
        "node_modules/amis-editor/lib/icons/form/formula.svg",
        "node_modules/amis-editor/lib/icons/form/inputArray.svg",
        "node_modules/amis-editor/lib/icons/other/property-sheet.svg",
        "node_modules/amis-editor/lib/icons/other/tooltip.svg",
        "node_modules/amis-editor/lib/icons/other/divider.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-absolute.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-fixed.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-1with2.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-2cols.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-2row.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-2with1.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-3cols.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-3row.svg",
        "node_modules/amis-editor/lib/icons/layout/layout-full.svg",
        "node_modules/amis-editor/lib/icons/layout/layout1-2.svg",
        "node_modules/amis-editor/lib/icons/layout/layout2-1.svg",
        "node_modules/amis-editor/lib/icons/layout/layout3-1.svg",
        "node_modules/amis-editor/lib/icons/layout/layout3-2.svg"
      ]
    },
    "node_modules/amis-editor/lib/index": {
      "url": "/node_modules/amis-editor/lib/index.js",
      "type": "js",
      "deps": [
        "node_modules/amis/lib/index",
        "node_modules/amis-editor/lib/locale/en-US",
        "node_modules/amis-editor/lib/locale/zh-CN",
        "node_modules/amis-editor-core/lib/index",
        "node_modules/amis-editor/lib/tpl/common",
        "node_modules/amis-editor/lib/tpl/horizontal",
        "node_modules/amis-editor/lib/tpl/api",
        "node_modules/amis-editor/lib/tpl/options",
        "node_modules/amis-editor/lib/tpl/validations",
        "node_modules/amis-editor/lib/tpl/style",
        "node_modules/amis-editor/lib/tpl/layout",
        "node_modules/amis-editor/lib/plugin/Others/Action",
        "node_modules/amis-editor/lib/plugin/Others/TableCell",
        "node_modules/amis-editor/lib/plugin/Form/InputArray",
        "node_modules/amis-editor/lib/plugin/Form/ButtonGroupSelect",
        "node_modules/amis-editor/lib/plugin/Form/ButtonToolbar",
        "node_modules/amis-editor/lib/plugin/Form/ChainedSelect",
        "node_modules/amis-editor/lib/plugin/Form/Checkbox",
        "node_modules/amis-editor/lib/plugin/Form/Checkboxes",
        "node_modules/amis-editor/lib/plugin/Form/InputCity",
        "node_modules/amis-editor/lib/plugin/Form/InputColor",
        "node_modules/amis-editor/lib/plugin/Form/Combo",
        "node_modules/amis-editor/lib/plugin/Form/ConditionBuilder",
        "node_modules/amis-editor/lib/plugin/Form/Control",
        "node_modules/amis-editor/lib/plugin/Form/InputDate",
        "node_modules/amis-editor/lib/plugin/Form/InputDateRange",
        "node_modules/amis-editor/lib/plugin/Form/InputDateTime",
        "node_modules/amis-editor/lib/plugin/Form/InputDateTimeRange",
        "node_modules/amis-editor/lib/plugin/Form/DiffEditor",
        "node_modules/amis-editor/lib/plugin/Form/CodeEditor",
        "node_modules/amis-editor/lib/plugin/Form/InputEmail",
        "node_modules/amis-editor/lib/plugin/Form/FieldSet",
        "node_modules/amis-editor/lib/plugin/Form/InputFile",
        "node_modules/amis-editor/lib/plugin/Form/Form",
        "node_modules/amis-editor/lib/plugin/Form/Formula",
        "node_modules/amis-editor/lib/plugin/Form/Group",
        "node_modules/amis-editor/lib/plugin/Form/Hidden",
        "node_modules/amis-editor/lib/plugin/Form/InputImage",
        "node_modules/amis-editor/lib/plugin/Form/InputGroup",
        "node_modules/amis-editor/lib/plugin/Form/Item",
        "node_modules/amis-editor/lib/plugin/Form/ListSelect",
        "node_modules/amis-editor/lib/plugin/Form/LocationPicker",
        "node_modules/amis-editor/lib/plugin/Form/UUID",
        "node_modules/amis-editor/lib/plugin/Form/MatrixCheckboxes",
        "node_modules/amis-editor/lib/plugin/Form/InputMonth",
        "node_modules/amis-editor/lib/plugin/Form/InputMonthRange",
        "node_modules/amis-editor/lib/plugin/Form/NestedSelect",
        "node_modules/amis-editor/lib/plugin/Form/InputNumber",
        "node_modules/amis-editor/lib/plugin/Form/InputPassword",
        "node_modules/amis-editor/lib/plugin/Form/InputQuarter",
        "node_modules/amis-editor/lib/plugin/Form/InputQuarterRange",
        "node_modules/amis-editor/lib/plugin/Form/Picker",
        "node_modules/amis-editor/lib/plugin/Form/Radios",
        "node_modules/amis-editor/lib/plugin/Form/InputRange",
        "node_modules/amis-editor/lib/plugin/Form/InputRating",
        "node_modules/amis-editor/lib/plugin/Form/InputRepeat",
        "node_modules/amis-editor/lib/plugin/Form/InputRichText",
        "node_modules/amis-editor/lib/plugin/Form/Select",
        "node_modules/amis-editor/lib/plugin/Form/Static",
        "node_modules/amis-editor/lib/plugin/Form/InputSubForm",
        "node_modules/amis-editor/lib/plugin/Form/Switch",
        "node_modules/amis-editor/lib/plugin/Form/InputTable",
        "node_modules/amis-editor/lib/plugin/Form/InputTag",
        "node_modules/amis-editor/lib/plugin/Form/InputText",
        "node_modules/amis-editor/lib/plugin/Form/TabsTransfer",
        "node_modules/amis-editor/lib/plugin/Form/Textarea",
        "node_modules/amis-editor/lib/plugin/Form/Transfer",
        "node_modules/amis-editor/lib/plugin/Form/InputTime",
        "node_modules/amis-editor/lib/plugin/Form/InputTimeRange",
        "node_modules/amis-editor/lib/plugin/Form/InputTree",
        "node_modules/amis-editor/lib/plugin/Form/TreeSelect",
        "node_modules/amis-editor/lib/plugin/Form/InputURL",
        "node_modules/amis-editor/lib/plugin/Form/InputYear",
        "node_modules/amis-editor/lib/plugin/Form/InputYearRange",
        "node_modules/amis-editor/lib/plugin/Form/InputExcel",
        "node_modules/amis-editor/lib/plugin/Form/InputKV",
        "node_modules/amis-editor/lib/plugin/Alert",
        "node_modules/amis-editor/lib/plugin/Audio",
        "node_modules/amis-editor/lib/plugin/Avatar",
        "node_modules/amis-editor/lib/plugin/Button",
        "node_modules/amis-editor/lib/plugin/ButtonGroup",
        "node_modules/amis-editor/lib/plugin/Breadcrumb",
        "node_modules/amis-editor/lib/plugin/Card",
        "node_modules/amis-editor/lib/plugin/Card2",
        "node_modules/amis-editor/lib/plugin/Cards",
        "node_modules/amis-editor/lib/plugin/Carousel",
        "node_modules/amis-editor/lib/plugin/Chart",
        "node_modules/amis-editor/lib/plugin/Collapse",
        "node_modules/amis-editor/lib/plugin/CollapseGroup",
        "node_modules/amis-editor/lib/plugin/Container",
        "node_modules/amis-editor/lib/plugin/CRUD",
        "node_modules/amis-editor/lib/plugin/Custom",
        "node_modules/amis-editor/lib/plugin/CustomRegion",
        "node_modules/amis-editor/lib/plugin/Date",
        "node_modules/amis-editor/lib/plugin/Datetime",
        "node_modules/amis-editor/lib/plugin/Dialog",
        "node_modules/amis-editor/lib/plugin/Divider",
        "node_modules/amis-editor/lib/plugin/Drawer",
        "node_modules/amis-editor/lib/plugin/DropDownButton",
        "node_modules/amis-editor/lib/plugin/Each",
        "node_modules/amis-editor/lib/plugin/Flex",
        "node_modules/amis-editor/lib/plugin/Grid",
        "node_modules/amis-editor/lib/plugin/HBox",
        "node_modules/amis-editor/lib/plugin/IFrame",
        "node_modules/amis-editor/lib/plugin/Image",
        "node_modules/amis-editor/lib/plugin/Images",
        "node_modules/amis-editor/lib/plugin/Json",
        "node_modules/amis-editor/lib/plugin/Link",
        "node_modules/amis-editor/lib/plugin/List",
        "node_modules/amis-editor/lib/plugin/ListItem",
        "node_modules/amis-editor/lib/plugin/Log",
        "node_modules/amis-editor/lib/plugin/Mapping",
        "node_modules/amis-editor/lib/plugin/Markdown",
        "node_modules/amis-editor/lib/plugin/Nav",
        "node_modules/amis-editor/lib/plugin/Operation",
        "node_modules/amis-editor/lib/plugin/Page",
        "node_modules/amis-editor/lib/plugin/Pagination",
        "node_modules/amis-editor/lib/plugin/Panel",
        "node_modules/amis-editor/lib/plugin/Plain",
        "node_modules/amis-editor/lib/plugin/Progress",
        "node_modules/amis-editor/lib/plugin/Property",
        "node_modules/amis-editor/lib/plugin/QRCode",
        "node_modules/amis-editor/lib/plugin/Reset",
        "node_modules/amis-editor/lib/plugin/Service",
        "node_modules/amis-editor/lib/plugin/Status",
        "node_modules/amis-editor/lib/plugin/Steps",
        "node_modules/amis-editor/lib/plugin/Sparkline",
        "node_modules/amis-editor/lib/plugin/Submit",
        "node_modules/amis-editor/lib/plugin/Table",
        "node_modules/amis-editor/lib/plugin/Table2",
        "node_modules/amis-editor/lib/plugin/TableCell2",
        "node_modules/amis-editor/lib/plugin/Tabs",
        "node_modules/amis-editor/lib/plugin/Tasks",
        "node_modules/amis-editor/lib/plugin/Time",
        "node_modules/amis-editor/lib/plugin/Timeline",
        "node_modules/amis-editor/lib/plugin/Tpl",
        "node_modules/amis-editor/lib/plugin/Tag",
        "node_modules/amis-editor/lib/plugin/AnchorNav",
        "node_modules/amis-editor/lib/plugin/Video",
        "node_modules/amis-editor/lib/plugin/Wizard",
        "node_modules/amis-editor/lib/plugin/Wrapper",
        "node_modules/amis-editor/lib/plugin/TooltipWrapper",
        "node_modules/amis-editor/lib/plugin/TableView",
        "node_modules/amis-editor/lib/plugin/CodeView",
        "node_modules/amis-editor/lib/plugin/WebComponent",
        "node_modules/amis-editor/lib/plugin/CRUD2",
        "node_modules/amis-editor/lib/plugin/ColumnToggler",
        "node_modules/amis-editor/lib/plugin/Layout/Layout_sorption_container",
        "node_modules/amis-editor/lib/plugin/Layout/Layout_fixed",
        "node_modules/amis-editor/lib/plugin/Layout/Layout1_2_v4",
        "node_modules/amis-editor/lib/plugin/Layout/Layout_free_container",
        "node_modules/amis-editor/lib/renderer/OptionControl",
        "node_modules/amis-editor/lib/renderer/MapSourceControl",
        "node_modules/amis-editor/lib/renderer/TimelineItemControl",
        "node_modules/amis-editor/lib/renderer/APIControl",
        "node_modules/amis-editor/lib/renderer/ValidationControl",
        "node_modules/amis-editor/lib/renderer/ValidationItem",
        "node_modules/amis-editor/lib/renderer/SwitchMoreControl",
        "node_modules/amis-editor/lib/renderer/StatusControl",
        "node_modules/amis-editor/lib/renderer/FormulaControl",
        "node_modules/amis-editor/lib/renderer/ExpressionFormulaControl",
        "node_modules/amis-editor/lib/renderer/textarea-formula/TextareaFormulaControl",
        "node_modules/amis-editor/lib/renderer/DateShortCutControl",
        "node_modules/amis-editor/lib/renderer/BadgeControl",
        "node_modules/amis-editor/lib/renderer/style-control/BoxModel",
        "node_modules/amis-editor/lib/renderer/style-control/Font",
        "node_modules/amis-editor/lib/renderer/style-control/Border",
        "node_modules/amis-editor/lib/renderer/style-control/BoxShadow",
        "node_modules/amis-editor/lib/renderer/style-control/Background",
        "node_modules/amis-editor/lib/renderer/style-control/Display",
        "node_modules/amis-editor/lib/renderer/style-control/InsetBoxModel",
        "node_modules/amis-editor/lib/renderer/RangePartsControl",
        "node_modules/amis-editor/lib/renderer/DataBindingControl",
        "node_modules/amis-editor/lib/renderer/DataMappingControl",
        "node_modules/amis-editor/lib/renderer/DataPickerControl",
        "node_modules/amis-editor/lib/renderer/FeatureControl",
        "node_modules/amis-editor/lib/renderer/event-control/index",
        "node_modules/amis-editor/lib/renderer/TreeOptionControl",
        "node_modules/amis-editor/lib/renderer/TransferTableControl",
        "node_modules/amis-editor/lib/renderer/style-control/ThemeClassName",
        "node_modules/amis-theme-editor/lib/renderers/Border",
        "node_modules/amis-theme-editor/lib/renderers/ColorPicker",
        "node_modules/amis-theme-editor/lib/renderers/Font",
        "node_modules/amis-theme-editor/lib/renderers/PaddingAndMargin",
        "node_modules/amis-theme-editor/lib/renderers/Radius",
        "node_modules/amis-theme-editor/lib/renderers/Shadow",
        "node_modules/amis-theme-editor/lib/renderers/Size",
        "node_modules/amis-editor/lib/component/BaseControl",
        "node_modules/amis-editor/lib/icons/index"
      ]
    },
    "renderer/MyRenderer.tsx": {
      "url": "/renderer/MyRenderer.js",
      "type": "js",
      "deps": [
        "node_modules/tslib/tslib",
        "node_modules/amis/lib/index",
        "node_modules/react/index"
      ]
    }
  },
  "pkg": {
    "p0": {
      "url": "/pkg/npm.js",
      "type": "js"
    }
  }
});