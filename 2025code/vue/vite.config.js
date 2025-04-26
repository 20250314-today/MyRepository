import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite' //自动导入vue中的组件
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'



// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // vueDevTools(),
      AutoImport({
        resolvers:[ElementPlusResolver()],
      }),
      Components({
        // 使用elementPlus采用sass样式配置系统
        resolvers:[
            ElementPlusResolver({importStyle:"sass"})
        ],
      }),
  ],
  css:{
    preprocessorOptions:{
      scss:{
        additionalData:'@use "@/assets/css/index.scss" as *;',
      },
    },
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
})
