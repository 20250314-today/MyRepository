import { createApp } from 'vue'
import App from './App.vue'
import { createStore } from 'vuex';
import router from './router'
import './assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/theme-chalk/index.css' //不注释掉，样式不起作用
import 'simplebar-vue/dist/simplebar.min.css';
import 'video.js/dist/video-js.css'
import SimpleBar from 'simplebar-vue';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import VueVideoPlayer from '@videojs-player/vue'
import axios from 'axios'
import store from '../store' // 导入 Store
axios.defaults.baseURL = '/api'        //关键代码
const app = createApp(App)
// const store = createStore({
//     state: {
//         token: ''
//     },
//     mutations: {
//         setToken(state, token) {
//             state.token = token;
//         }
//     }
//     // 其他配置
// });

app.use(SimpleBar);
app.use(store) //  安装 Store
app.use(router)
app.use(ElementPlus)//引入element css样式
app.use(VueVideoPlayer)
app.mount('#app')

for (const [key,component] of Object.entries(ElementPlusIconsVue)){
    app.component(key,component)
}//使用icon图标

