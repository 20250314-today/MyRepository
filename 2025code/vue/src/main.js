import { createApp } from 'vue'
import App from './App.vue'
import { createStore } from 'vuex';
import router from './router'
import './assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/index.css' //不注释掉，样式不起作用
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)
const store = createStore({
    state: {
        token: ''
    },
    mutations: {
        setToken(state, token) {
            state.token = token;
        }
    }
    // 其他配置
});
app.use(store);
app.use(router)
app.use(ElementPlus)//引入element css样式
app.mount('#app')

for (const [key,component] of Object.entries(ElementPlusIconsVue)){
    app.component(key,component)
}//使用icon图标

