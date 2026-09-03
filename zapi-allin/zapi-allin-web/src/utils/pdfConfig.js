// PDF.js 配置工具 - 完全使用 CDN 版本 (2.6.347)
// 不再使用 npm 包，避免版本冲突和私有字段问题

export const configurePDFJS = async () => {
  try {
    console.log('开始配置 PDF.js...')
    
    // 等待 CDN 加载完成（最多等待 5 秒）
    let retries = 0
    const maxRetries = 50
    
    while (typeof window.pdfjsLib === 'undefined' && retries < maxRetries) {
      await new Promise(resolve => setTimeout(resolve, 100))
      retries++
    }
    
    if (typeof window.pdfjsLib === 'undefined') {
      throw new Error('PDF.js CDN 加载超时，请检查网络连接')
    }
    
    const pdfjsLib = window.pdfjsLib
    
    // 验证 getDocument 方法存在
    if (typeof pdfjsLib.getDocument !== 'function') {
      throw new Error('PDF.js getDocument 方法不存在')
    }
    
    console.log('✓ PDF.js 配置成功')
    console.log('✓ 版本: 2.6.347 (本地)')
    console.log('✓ Worker:', pdfjsLib.GlobalWorkerOptions.workerSrc)
    
    return {
      success: true,
      pdfjs: pdfjsLib,
      workerSrc: pdfjsLib.GlobalWorkerOptions.workerSrc
    }
  } catch (error) {
    console.error('✗ PDF.js 配置失败:', error)
    return {
      success: false,
      error: error.message
    }
  }
}

// 测试 PDF 加载功能
export const testPDFLoad = async (pdfUrl) => {
  try {
    const config = await configurePDFJS()
    if (!config.success) {
      throw new Error('PDF.js 配置失败: ' + config.error)
    }
    
    const pdfjsLib = config.pdfjs
    const loadingTask = pdfjsLib.getDocument(pdfUrl)
    const pdf = await loadingTask.promise

    return {
      success: true,
      pdf: pdf,
      numPages: pdf.numPages
    }
  } catch (error) {
    console.error('PDF 加载测试失败:', error)
    return {
      success: false,
      error: error.message
    }
  }
}
