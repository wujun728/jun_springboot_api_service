// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from './assets/axios.js';
import ElementUI from 'element-ui';
import './assets/iconfont/iconfont.css'
import './assets/dbicon/iconfont.css'
import 'element-ui/lib/theme-chalk/index.css';
import * as echarts from 'echarts'
import VueCodeMirror from 'vue-codemirror'
import 'codemirror/lib/codemirror.css'

Vue.use(VueCodeMirror)
Vue.use(axios)
Vue.use(ElementUI)

Vue.prototype.$http = axios
Vue.config.productionTip = false
Vue.prototype.$echarts = echarts


// http request 拦截器
axios.interceptors.request.use(config => {

  // 通过拦截request请求,对头部增加Authorization属性,以传递token值
  let token = sessionStorage.getItem('token');
  if (token) {
    config.headers.Authorization = 'Bearer ' + token;
  }

  return config;
}, function (error) {
  // 对请求错误做些什么
  return Promise.reject(error)
})

//返回状态判断(添加响应拦截器)
axios.interceptors.response.use(res => {
  //对响应数据做些事
  if (res.data && (res.data.code === 401 || res.data.code === 403 || res.data.code === 404)) {
    router.push({
      path: "/login"
    })
  }

  return res
}, error => {
  // 返回 response 里的错误信息
  //console.log(error);
  return Promise.reject(error.response)
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
