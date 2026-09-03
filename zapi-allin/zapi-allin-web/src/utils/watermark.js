/**
 * 全局水印工具
 */

let watermarkElement = null
let watermarkObserver = null

// 默认配置
const defaultConfig = {
  enabled: true,
  text: '知录Admin',
  fontSize: 16,
  opacity: 0.25,
  rotate: -20
}

// 获取水印配置
const getWatermarkConfig = () => {
  const savedSettings = localStorage.getItem('watermarkSettings')
  if (savedSettings) {
    try {
      const settings = JSON.parse(savedSettings)
      return {
        enabled: settings.enabled !== undefined ? settings.enabled : defaultConfig.enabled,
        text: settings.text || defaultConfig.text,
        fontSize: settings.fontSize || defaultConfig.fontSize,
        opacity: settings.opacity || defaultConfig.opacity,
        rotate: settings.rotate || defaultConfig.rotate
      }
    } catch (e) {
      console.error('Failed to parse watermark settings:', e)
      return defaultConfig
    }
  }

  // 首次使用，保存默认配置
  localStorage.setItem('watermarkSettings', JSON.stringify(defaultConfig))
  return defaultConfig
}

// 创建水印
const createWatermark = (config) => {
  const canvas = document.createElement('canvas')
  const ctx = canvas.getContext('2d')

  const text = config.text || '知录Admin'
  const fontSize = config.fontSize || 16

  canvas.width = 300
  canvas.height = 200

  ctx.clearRect(0, 0, canvas.width, canvas.height)
  ctx.font = `${fontSize}px Arial`
  
  // 根据当前主题模式选择颜色和透明度
  const isDarkMode = document.documentElement.getAttribute('data-theme') === 'dark'
  
  if (isDarkMode) {
    // 深色模式：纯白色，使用很低的不透明度避免刺眼
    ctx.fillStyle = 'rgba(255, 255, 255, 0.08)'
  } else {
    // 浅色模式：黑色，使用配置的透明度
    ctx.fillStyle = `rgba(0, 0, 0, ${config.opacity || 0.25})`
  }
  
  ctx.textAlign = 'center'
  ctx.textBaseline = 'middle'

  // 旋转画布
  ctx.translate(canvas.width / 2, canvas.height / 2)
  ctx.rotate(((config.rotate || -20) * Math.PI) / 180)
  ctx.fillText(text, 0, 0)

  return canvas.toDataURL()
}

// 应用水印
const applyWatermark = () => {
  const config = getWatermarkConfig()

  if (!config.enabled) {
    removeWatermark()
    return
  }

  removeWatermark()

  const watermarkUrl = createWatermark(config)

  // 等待DOM加载完成
  const addWatermarkElement = () => {
    // 直接添加到 body，这样所有页面都能显示水印
    watermarkElement = document.createElement('div')
    watermarkElement.className = 'page-watermark'
    watermarkElement.style.cssText = `
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      pointer-events: none;
      z-index: 9999;
      background-image: url(${watermarkUrl});
      background-repeat: repeat;
    `
    document.body.appendChild(watermarkElement)

    // 监听水印被删除
    observeWatermark()
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', addWatermarkElement)
  } else {
    addWatermarkElement()
  }
}

// 移除水印
const removeWatermark = () => {
  if (watermarkElement && watermarkElement.parentNode) {
    watermarkElement.parentNode.removeChild(watermarkElement)
    watermarkElement = null
  }

  if (watermarkObserver) {
    watermarkObserver.disconnect()
    watermarkObserver = null
  }
}

// 监听水印元素，防止被删除
const observeWatermark = () => {
  if (!watermarkElement) return

  const config = getWatermarkConfig()

  watermarkObserver = new MutationObserver((mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.type === 'childList' && config.enabled) {
        const watermarkExists = document.querySelector('.page-watermark')
        if (!watermarkExists) {
          applyWatermark()
        }
      }
    })
  })

  // 监听 body 的变化
  watermarkObserver.observe(document.body, {
    childList: true,
    subtree: false
  })
}

// 更新水印
const updateWatermark = () => {
  applyWatermark()
}

// 初始化水印
const initWatermark = () => {
  applyWatermark()
  
  // 监听主题模式变化
  const themeObserver = new MutationObserver((mutations) => {
    mutations.forEach((mutation) => {
      if (mutation.type === 'attributes' && mutation.attributeName === 'data-theme') {
        // 主题模式变化时重新应用水印
        console.log('Theme changed, reapplying watermark...')
        const config = getWatermarkConfig()
        if (config.enabled) {
          // 延迟一点确保主题已经完全切换
          setTimeout(() => {
            applyWatermark()
          }, 100)
        }
      }
    })
  })
  
  // 监听 html 元素的 data-theme 属性变化
  themeObserver.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['data-theme']
  })
}

export default {
  init: initWatermark,
  apply: applyWatermark,
  remove: removeWatermark,
  update: updateWatermark
}
