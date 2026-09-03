/**
 * 浏览器环境 Polyfills
 * 修复 Node.js 库在浏览器环境中的兼容性问题
 * 
 * 必须在所有其他模块导入之前执行，确保库加载时 global 已存在
 */

// 确保 globalThis 存在（兼容旧浏览器）
if (typeof globalThis === 'undefined') {
  window.globalThis = window
}

// 修复 global 变量（sockjs-client 等库需要）
// 必须在所有模块加载之前设置，确保库可以正确访问
if (typeof global === 'undefined') {
  // 创建 global 对象引用
  const globalObj = globalThis
  
  // 在 window 上定义 global
  try {
    Object.defineProperty(window, 'global', {
      value: globalObj,
      writable: true,
      configurable: true,
      enumerable: false
    })
  } catch (e) {
    // 如果 defineProperty 失败，使用直接赋值
    window.global = globalObj
  }
}

// 获取 global 对象（可能是 window.global 或全局的 global）
const globalRef = (function() {
  if (typeof global !== 'undefined') {
    return global
  }
  if (typeof window !== 'undefined' && window.global) {
    return window.global
  }
  return globalThis
})()

// 修复 Request 等 Web API 在 global 上的访问
// 某些库会尝试从 global.Request 解构，需要确保这些 API 存在
if (globalRef && typeof globalRef.Request === 'undefined') {
  // 确保 global 对象有必要的 Web API（如果浏览器支持）
  if (typeof Request !== 'undefined') {
    globalRef.Request = Request
  }
  if (typeof Response !== 'undefined') {
    globalRef.Response = Response
  }
  if (typeof Headers !== 'undefined') {
    globalRef.Headers = Headers
  }
  if (typeof fetch !== 'undefined') {
    globalRef.fetch = fetch
  }
}

export {}
