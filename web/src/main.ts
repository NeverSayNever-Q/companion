import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import Antd from "ant-design-vue";
import "ant-design-vue/dist/antd.css";
import * as antIcons from "@ant-design/icons-vue";
import axios from "axios";
import "animate.css";
import { message } from "@/config/config";

const app = createApp(App).use(store).use(router).use(Antd);

//全局使用图标
// 注册组件
Object.keys(antIcons).forEach((key) => {
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  app.component(key, antIcons[key]);
});
// 添加到全局
app.config.globalProperties.$antIcons = antIcons;
app.config.globalProperties.message = message;

app.mount("#app");

//配置axios根路径
axios.defaults.baseURL = process.env.VUE_APP_SERVER;
