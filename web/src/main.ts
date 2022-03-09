import {createApp} from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as antIcons from '@ant-design/icons-vue';
import axios from 'axios';
import 'animate.css'

const app = createApp(App).use(store).use(router).use(Antd)

//全局使用图标
// 注册组件
Object.keys(antIcons).forEach(key => {
    // @ts-ignore
    app.component(key, antIcons[key]);
})
// 添加到全局
app.config.globalProperties.$antIcons = antIcons
app.mount('#app')

//配置axios根路径
axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误：', error);
    const response = error.response;
    return Promise.reject(error);
});
