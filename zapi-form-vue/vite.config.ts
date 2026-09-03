import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  server: {
    // 允许所有网络接口访问
    host: '0.0.0.0',
    // 可选：指定端口号（默认是5173）
    port: 5173,
    // 自动打开浏览器（可选）
    open: true,
    // 允许跨域（可选）
    cors: true
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
