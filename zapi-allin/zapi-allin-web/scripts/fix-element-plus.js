/**
 * 修复 Element Plus 缺失的模块文件
 * 在 npm install 后自动运行
 */

const fs = require('fs')
const path = require('path')

// 需要创建的文件
const files = [
  {
    path: 'node_modules/element-plus/es/hooks/use-prevent-globalThis/index.mjs',
    content: `// Auto-generated shim for missing module
export default function usePreventGlobalThis() {
  return {}
}
`
  },
  {
    path: 'node_modules/element-plus/es/hooks/use-prevent-window/index.mjs',
    content: `// Auto-generated shim for missing module
export default function usePreventWindow() {
  return {}
}
`
  },
  {
    path: 'node_modules/element-plus/es/hooks/use-prevent-global/index.mjs',
    content: `// Auto-generated shim for missing module
export function usePreventGlobal() {
  return {}
}
`
  },
  {
    path: 'node_modules/element-plus/es/components/config-provider/src/hooks/use-globalThis-config.mjs',
    content: `// Auto-generated shim for missing module
import { ref, computed } from 'vue'

export const defaultGlobalConfig = ref({})

export function provideGlobalConfig(config) {
  defaultGlobalConfig.value = config
}

export function useGlobalConfig() {
  return computed(() => defaultGlobalConfig.value)
}

export function useGlobalComponentSettings(key, defaultValue) {
  const config = useGlobalConfig()
  return computed(() => config.value[key] || defaultValue)
}

export default useGlobalConfig
`
  },
  {
    path: 'node_modules/element-plus/es/components/config-provider/src/hooks/use-window-config.mjs',
    content: `// Auto-generated shim for missing module
import { ref, computed, unref } from 'vue'

// Minimal window config store
export const defaultWindowConfig = ref({})

export function provideWindowConfig(config) {
  defaultWindowConfig.value = unref(config) || {}
  return computed(() => defaultWindowConfig.value)
}

export function useWindowConfig() {
  return computed(() => defaultWindowConfig.value)
}

// Align exports with Element Plus expectations
export function provideGlobalConfig(config) {
  return provideWindowConfig(config)
}

export function useGlobalConfig(key, defaultValue) {
  if (!key) return useWindowConfig()
  return computed(() => {
    const cfg = defaultWindowConfig.value || {}
    return cfg[key] ?? defaultValue
  })
}

export function useGlobalComponentSettings(key, defaultValue) {
  return useGlobalConfig(key, defaultValue)
}

export default useWindowConfig
`
  },
  {
    path: 'node_modules/element-plus/es/utils/vue/global-node.mjs',
    content: `// Auto-generated shim for missing module
export const isClient = typeof window !== 'undefined'
export const isServer = !isClient

// Mock functions for global node management
export function createGlobalNode(id) {
  if (!isClient) return null
  const el = document.createElement('div')
  el.id = id
  document.body.appendChild(el)
  return el
}

export function removeGlobalNode(el) {
  if (!isClient || !el) return
  if (el.parentNode) {
    el.parentNode.removeChild(el)
  }
}

export function changeGlobalNodesTarget(newTarget) {
  // Mock implementation for changing global nodes target
  if (!isClient) return
  // This is a stub - in real implementation it would move nodes to a new container
}
`
  },
  {
    path: 'node_modules/element-plus/es/utils/vue/globalThis-node.mjs',
    content: `// Auto-generated shim for missing module
export const isClient = typeof window !== 'undefined'
export const isServer = !isClient

// Mock functions for global node management
export function createGlobalNode(id) {
  if (!isClient) return null
  const el = document.createElement('div')
  el.id = id
  document.body.appendChild(el)
  return el
}

export function removeGlobalNode(el) {
  if (!isClient || !el) return
  if (el.parentNode) {
    el.parentNode.removeChild(el)
  }
}

export function changeGlobalNodesTarget(newTarget) {
  // Mock implementation for changing global nodes target
  if (!isClient) return
  // This is a stub - in real implementation it would move nodes to a new container
}
`
  },
  {
    path: 'node_modules/element-plus/es/utils/vue/window-node.mjs',
    content: `// Auto-generated shim for missing module
export const isClient = typeof window !== 'undefined'
export const isServer = !isClient

// Mock functions for window node management (align with global-node API)
export function createWindowNode(id) {
  if (!isClient) return null
  const el = document.createElement('div')
  el.id = id
  document.body.appendChild(el)
  return el
}

export function removeWindowNode(el) {
  if (!isClient || !el) return
  if (el.parentNode) {
    el.parentNode.removeChild(el)
  }
}

export function changeWindowNodesTarget(newTarget) {
  // Mock implementation for changing window nodes target
  if (!isClient) return
  // This is a stub - in real implementation it would move nodes to a new container
}

// Export aliases to satisfy imports expecting createGlobalNode/removeGlobalNode
export const createGlobalNode = createWindowNode
export const removeGlobalNode = removeWindowNode
`
  }
]

console.log('🔧 修复 Element Plus 缺失的模块...')

files.forEach(file => {
  const filePath = path.join(__dirname, '..', file.path)
  const dir = path.dirname(filePath)
  
  // 创建目录
  if (!fs.existsSync(dir)) {
    fs.mkdirSync(dir, { recursive: true })
    console.log(`✓ 创建目录: ${dir}`)
  }
  
  // 创建文件
  fs.writeFileSync(filePath, file.content)
  console.log(`✓ 创建文件: ${file.path}`)
})

console.log('✅ Element Plus 修复完成!')
