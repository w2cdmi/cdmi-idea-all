// page/appmgr/appdetail.js

Page({

    /**
     * 页面的初始数据
     */
    data: {
        app_id: '',
        app_name: '',
        console_path: '',
        app_desc: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        //检查必要的参数：title
        if (options.title == null || options.title == "") {
            wx.showToast({
                title: '缺失必要的请求参数[title]，请联系开发商',
            });
            return;
        } else {
            wx.setNavigationBarTitle({
                title: options.title,
            });
        }
        //检查必要的参数：appid
        if (options.appid == null || options.appid == "") {
            wx.showToast({
                title: '缺失必要的请求参数[id]，请联系开发商',
            });
            return;
        };

        //获取该应用信息
        var _this = this;
        const db = wx.cloud.database();
        db.collection('apps').doc(options.appid).get().then(res => {
            if (res.data == null) {
                wx.showToast({
                    title: '应用信息不存在，请退出小程序重试',
                });
                return;
            }
            _this.setData({
                console_path: res.data.consolePath,
                app_id: options.appid,
                app_name: res.data.name,
                app_icon: res.data.icon,
            });
        });
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function() {
        
    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function() {

    },

    /**
     * 跳转到指定接入应用的应用管理员列表页
     */
    jumpToAppManagerList: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/appmgr/appmanager?title=' + dataset.title + '&id=' + dataset.id,
        })
    },

    /**
     * 跳转到指定接入应用的终端应用管理页
     */
    jumpToTerminalApps: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/appmgr/terminalmgr?title=' + dataset.title + '&id=' + dataset.id,
        })
    },

    /**
     * 跳转到指定接入应用的开发信息页
     */
    jumpToAppDevelop: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/appmgr/appmgrdevelop?title=应用开发信息' + '&appid=' + this.data.app_id + '&appname=' + this.data.app_name,
        })
    },

    /**
     * 跳转到指定接入应用的配置参数页
     */
    jumpToAppConfig: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/appmgr/appconfig?title=应用配置参数' + '&appid=' + this.data.app_id + '&appname=' + this.data.app_name,
        })
    },

    /**
     * 跳转到应用的管理后台页面,需要在页面上加上参数，跳转后才能读取该应用的信息
     */
    jumpToConsole:function(e){
        wx.navigateTo({
            url: this.data.console_path + "?appid=" + this.data.app_id,
        })  
    },

    jumpToUser:function(e){
        wx.navigateTo({
            url:  "/page/user/users" + "?appid=" + this.data.app_id,
        }) 
    }
})