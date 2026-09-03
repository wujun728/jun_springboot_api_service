// PDF.js 测试工具
export const testPDFJS = async () => {

  try {
    // 测试1: 静态导入

    try {
      const staticImport = await import('pdfjs-dist')
      
        GlobalWorkerOptions: !!staticImport.GlobalWorkerOptions

    } catch (error) {

    }
    
    // 测试2: 动态导入

    const pdfjsLib = await import('pdfjs-dist')
    
    // 测试3: Worker 配置

    if (pdfjsLib.GlobalWorkerOptions) {
      pdfjsLib.GlobalWorkerOptions.workerSrc = '/pdf.worker.min.js'

    } else {

    }
    
    // 测试4: API 获取

    let PDFJS = null
    if (pdfjsLib.getDocument) {
      PDFJS = pdfjsLib

    } else if (pdfjsLib.default && pdfjsLib.default.getDocument) {
      PDFJS = pdfjsLib.default

    } else {
      throw new Error('无法找到 getDocument 方法')
    }
    
    // 测试5: 简单的 PDF 加载测试（使用一个简单的 PDF URL）

    try {
      // 创建一个简单的 PDF 数据 URL 进行测试
      const testPdfUrl = 'data:application/pdf;base64,JVBERi0xLjQKMSAwIG9iago8PAovVHlwZSAvQ2F0YWxvZwovUGFnZXMgMiAwIFIKPj4KZW5kb2JqCjIgMCBvYmoKPDwKL1R5cGUgL1BhZ2VzCi9LaWRzIFszIDAgUl0KL0NvdW50IDEKPD4KZW5kb2JqCjMgMCBvYmoKPDwKL1R5cGUgL1BhZ2UKL1BhcmVudCAyIDAgUgovTWVkaWFCb3ggWzAgMCA2MTIgNzkyXQo+PgplbmRvYmoKeHJlZgowIDQKMDAwMDAwMDAwMCA2NTUzNSBmIAowMDAwMDAwMDA5IDAwMDAwIG4gCjAwMDAwMDAwNTggMDAwMDAgbiAKMDAwMDAwMDExNSAwMDAwMCBuIAp0cmFpbGVyCjw8Ci9TaXplIDQKL1Jvb3QgMSAwIFIKPj4Kc3RhcnR4cmVmCjE3NQolJUVPRg=='
      
      const loadingTask = PDFJS.getDocument(testPdfUrl)
      const pdf = await loadingTask.promise

      return {
        success: true,
        message: 'PDF.js 测试完全成功',
        pdfjs: PDFJS
      }
    } catch (pdfError) {

      return {
        success: false,
        message: 'PDF 加载测试失败: ' + pdfError.message,
        pdfjs: PDFJS
      }
    }
    
  } catch (error) {

    return {
      success: false,
      message: 'PDF.js 测试失败: ' + error.message,
      pdfjs: null
    }
  }
}

// 简化的 PDF.js 初始化函数
export const initSimplePDFJS = async () => {
  try {
    const pdfjsLib = await import('pdfjs-dist')
    
    // 设置 worker - 使用本地文件
    if (pdfjsLib.GlobalWorkerOptions) {
      pdfjsLib.GlobalWorkerOptions.workerSrc = '/pdf.worker.min.js'
    }
    
    // 获取 API
    if (pdfjsLib.getDocument) {
      return pdfjsLib
    } else if (pdfjsLib.default && pdfjsLib.default.getDocument) {
      return pdfjsLib.default
    } else {
      throw new Error('无法找到 PDF.js API')
    }
  } catch (error) {

    throw error
  }
}
