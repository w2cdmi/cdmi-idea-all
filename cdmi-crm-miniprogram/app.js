App({
    globalData: {
        token: '', //请求的短期Token
        teamId: '', //机构Id或机构码
        userId: '', //当前登录用户ID
        avatarUrl: '', //用户的微信头像
        userName: '', //用户名字
        acountType: '', //账号类型:admin还是一般用户
        systemName: '分销业绩管理系统',
        loginStatus: false, //用户的登陆状态
        windowHeight: wx.getSystemInfoSync().windowHeight,
        windowWidth: wx.getSystemInfoSync().windowWidth,
        systemEnv: 'edu-5ae962',
    },

    onLaunch: function(options) {
        wx.cloud.init({
            env: this.globalData.systemEnv,
            traceUser: true
        });

        const db = wx.cloud.database();
        db.collection('agreements').count().then(res => {
            console.info(res.total);
        });

        var now = new Date().getTime();
        var uid = wx.getStorageSync("loginInfo").userId;
        var loginStatus = wx.getStorageSync("loginStatus");
        if (uid != null || uid != undefined) {
            this.globalData.userId = uid;
        }
        this.globalData.loginStatus = loginStatus;
    },
    onShow: function() {
        console.log('App Show');
    },
    onHide: function() {
        console.log('App Hide');
    },
    onError: function(msg) {
        console.log(msg)
    }
});