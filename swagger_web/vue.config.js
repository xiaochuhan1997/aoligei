const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    lintOnSave: false,
    // devServer: {
    //     host: "localhost", //配置本项目运行主机
    //     port: 8001, //配置本项目运行端口
    //     proxy: {
    //         '/api': {
    //             target: 'http://localhost:8080', // 代理地址，这里设置的地址会代替axios中设置的baseURL
    //             changeOrigin: true, // 如果接口跨域，需要进行这个参数配置
    //             ws: true,
    //             pathRewrite: {
    //                 '^/api': ''
    //             }
    //         },
    //     }
    // }

})