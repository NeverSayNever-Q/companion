import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';
import * as Icons from '@ant-design/icons-vue';
import axios from 'axios';

const app = createApp(App).use(store).use(router).use(Antd)

//全局使用图标
const icons: any = Icons;
for(const i in icons){
    app.component(i, icons[i]);
}

app.mount('#app')

//配置axios根路径
axios.defaults.baseURL = process.env.VUE_APP_SERVER;
