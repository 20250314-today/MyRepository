import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite' //自动导入vue中的组件
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'



// https://vite.dev/config/
export default defineConfig({
  // 关闭 eslint 校验（在 Vite 中一般通过配置单独处理 eslint，这里这种写法不适用，不过先保留逻辑）
  // 可以在.vscode/settings.json 等地方配置 eslint 相关规则，而不是在这里设置
  // 以下配置是关于开发服务器的设置
  server: {
    proxy: {
      '/api': {
        target: 'https://api.deepseek.com',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
        headers: {
          // 在此处添加认证头（安全警告：仅用于开发环境）
          Authorization: 'Bearer sk-a3e5266cdbc447bea67808ccea570685'
        },
        pathRewrite: {
          '/api': ''
        }
      }
    },
    // 配置是否在浏览器中显示 eslint 相关的警告和错误
    overlay: {
      warning: false,
      errors: false
    },
    // 配置跨域代理
  },
  plugins: [
    vue({
      template: {
        compilerOptions: {
          isCustomElement: (tag) => ['custom-element'].includes(tag)
        },
        babelParserPlugins: ['optionalChainingAssign']
      }
    }),
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
