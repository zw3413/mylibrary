'use strict'

const path=require('path')

const config =require('../config')

module.exports={
    build:{
        assetsRoot: path.resolve('__dirname','../dist'),
        assetsSubDirectory:'static',
        assetsPublicPath:'/',
        productionSourceMap:true,
        devtool:'#source-map',
        index:path.resolve(__dirname,'../dist/index.html'),
        productionGzip:false,
        productionGzipExtensions:['js','css'],
        bundleAnalyzerReport:process.env.npm_config_report
    },
    dev:{
        assetsSubDirectory:'static',
        assetsPublicPath:'/',
        cssSourceMap:false,
        devtool:'eval-source-map',// 定义用来生成source map的模式
        host:'localhost',
        port:11000,
        autoOpenBrowser:false,
        errorOverlay:true,
        notifyOnErrors:true,
        poll:false,
        useEslint:true,
        showEslintErrorsInOverlay:false,
        proxyTable:{
            '/rest':{
                target:'http://127.0.0.1:8080'
            }
        }
    }


}
