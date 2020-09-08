var httpclient = require("commjs/httpclient.js");

App({
    globalData: {
        token: '', //当前用户请求Token
        expire: '', //当前Token过期时间
        userId: '', //当前登录用户ID
        adminId:'', //当前登陆者只能是管理员，这里是管理员的Id
        developerId: '',//对应的开发者ID
        openId:'',      //用户对应的微信OpenId
        consoleAppId:'',    //当前操作的应用的后台AppId，点击接入应用的后台管理时候写入
        userName: '', //用户名字
        avatarUrl: '', //用户头像
        systemName: '业务综合管理系统',
        systemEnv: 'edu-5ae962',
        windowHeight: wx.getSystemInfoSync().windowHeight,
        windowWidth: wx.getSystemInfoSync().windowWidth,
        distributorsAppId:'W81ZvZL-scb2DluD',       //对应经销商客户管理系统，用于邀请经销商
    },
    
    onLaunch: function(options) {
        console.info(options);
        //1. 使用云开发模式，优先对环境进行初始化
        wx.cloud.init({
            env: this.globalData.systemEnv,
            traceUser: true
        });
    },
    onShow: function() {
            //   httpclient.get({
            //     url: config.HOST + config.HTTP_CHECK_IS_DEVELOPER,
            //     data: data
            //   }).then((res) => {
            //     if (res == false) {
            //       wx.navigateTo({
            //         url: '/page/developer/newdeveloper',
            //       });
            //     }
            //   }, (result) => {  //失败后执行
            //     wx.showToast({
            //       title: result.data.message,
            //       icon: 'error'
            //     })
            //   });
            //注册完成，重新执行登陆流程。DO Nothing!
    },
    onHide: function() {
        console.log('App Hide');
    },
    onError: function(msg) {
        console.log(msg)
    }
});