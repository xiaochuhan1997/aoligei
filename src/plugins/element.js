import ElementPlus, { ElMessage, ElMessageBox } from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import locale from 'element-plus/lib/locale/lang/zh-cn'
export default (app) => {
    app.use(ElementPlus, { locale })
        // app.use(Search)
    for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
        app.component(key, component)
    }
    app.config.globalProperties.$message = ElMessage
    app.config.globalProperties.$messagebox = ElMessageBox
}