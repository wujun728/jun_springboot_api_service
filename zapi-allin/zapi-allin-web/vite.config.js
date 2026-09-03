/**
 * Vite 配置文件
 *
 * 功能说明：
 * 1. Vue 3 项目基础配置
 * 2. 开发服务器配置（代理、跨域等）
 * 3. 构建优化配置
 * 4. 浏览器环境兼容性处理
 *
 * @author zhonglz
 * @version 1.0.0
 */

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// 开发服务器配置
const SERVER_CONFIG = {
  host: '0.0.0.0', // 允许所有网络接口访问
  port: 5173,      // 开发服务器端口
  open: true,       // 自动打开浏览器
  cors: true,      // 允许跨域
}

// API 代理配置工厂函数
const createProxyConfig = (isDev) => ({
  '/zapi-allin-sys': {
    target: 'http://127.0.0.1:8080/',
    // target: 'http://43.136.77.194',
    changeOrigin: true,
    secure: false,
    ws: true, // 支持 WebSocket 代理
    configure: (proxy, options) => {
      // 代理错误处理
      proxy.on('error', (err, req, res) => {
        console.error('代理错误:', err.message)
      })
      // 代理请求日志（仅在开发环境）
      if (isDev) {
        proxy.on('proxyReq', (proxyReq, req, res) => {
          console.log(`[代理] ${req.method} ${req.url} -> ${options.target}${req.url}`)
        })
      }
    }
  }
})

export default defineConfig(({ command, mode }) => {
  const isServe = command === 'serve'
  const isDev = mode === 'development'

  return {
    // 插件配置
    plugins: [
      vue({
        // Vue 3 单文件组件支持
        include: [/\.vue$/],
      }),
    ],

    // 路径别名配置
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src'),
      },
      // 扩展名解析顺序
      extensions: ['.mjs', '.js', '.mts', '.ts', '.jsx', '.tsx', '.json', '.vue'],
    },

    define: {
      'process.env': '{}',
    },

    // 开发服务器配置（仅开发模式）
    ...(isServe ? {
      server: {
        ...SERVER_CONFIG,
        proxy: createProxyConfig(isDev),
      },
    } : {}),

    // 构建配置
    build: {
      outDir: 'dist',              // 输出目录
      assetsDir: 'assets',         // 静态资源目录
      sourcemap: false,            // 不生成 sourcemap（生产环境）
      cssCodeSplit: true,          // CSS 代码分割
      chunkSizeWarningLimit: 1000, // chunk 大小警告阈值（KB）

      // 压缩配置
      // 使用 esbuild 压缩（Vite 内置，比 terser 更快，无需额外安装）
      minify: isDev ? false : 'esbuild',

      // esbuild 配置：打包时移除 console 和 debugger
      esbuild: {
        // 生产环境移除 console 和 debugger
        drop: isDev ? [] : ['console', 'debugger'],
      },

      // Rollup 构建选项
      rollupOptions: {
        output: {
          // 自动代码分割，避免循环依赖
          manualChunks: undefined,
          // 输出文件命名规则
          chunkFileNames: 'assets/js/[name]-[hash].js',
          entryFileNames: 'assets/js/[name]-[hash].js',
          assetFileNames: 'assets/[ext]/[name]-[hash].[ext]',
        },
      },
    },

    // 预览服务器配置
    preview: {
      port: 4173,
      open: true,
    },

    // 优化依赖预构建
    optimizeDeps: {
      include: [
        'vue',
        'vue-router',
        'pinia',
        'element-plus',
        'axios',
        'sockjs-client',
        '@stomp/stompjs',
      ],
      exclude: ['pdfjs-dist'], // 排除 pdfjs-dist，使用 CDN
    },
  }
})
