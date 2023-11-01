import { createApp } from 'vue'
import App from './App.vue'

import router from './router'
import installElementPlus from './plugins/element'
import axios from 'axios'
import './assets/css/global.css'
import VueCodeMirror from 'vue-codemirror'
import Codemirror from 'vue-codemirror'
// import "jsoneditor";


// import VueBus from 'vue-bus';
// script src = "https://cdn.jsdelivr.net/npm/vue-bus/dist/vue-bus.js" > < /script>
// Vue.prototype.$bus = Bus


const app = createApp(App)
installElementPlus(app)
app.config.globalProperties.$http = axios
axios.defaults.baseURL = 'http://localhost:8081'
axios.defaults.withCredentials = true
axios.defaults.credentials = 'include';

// axios.interceptors.request.use(config => {
//     NProgress.start();

//     config.headers.Authorization = window.sessionStorage.getItem('token')
//     return config
// })

// axios.interceptors.response.use(config => {
//     NProgress.done();
//     return config
// })

app.use(router).use(VueCodeMirror).use(Codemirror).mount('#app')
