//app.js
App({
    globalData:{
        screenWidth:0,
        screenHeight:0,
        appid:'',
        openid:'',
        pointer:{},//当前的视野人
    },

    onLaunch: function() {
        console.info("启动：");
        if (!wx.cloud) {
            console.error('请使用 2.2.3 或以上的基础库以使用云能力')
        } else {
            wx.cloud.init({
                env: "cognation-dd243d",
                traceUser: true,
            })
        }

        this.globalData = {}

        //获取窗体大小
        var _this = this;
        wx.getSystemInfo({
            success: function (res) {
                _this.globalData.screenWidth = res.windowWidth;
                _this.globalData.screenHeight = res.windowHeight;
            },
        })
    }
})