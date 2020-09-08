// page/appmgr/appconsoleconfig.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        app_id: '',
        app_name: '',
        console_path: '',
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
        } else {
            this.setData({
                app_id: options.appid,
                app_name: options.appname,
            })
        }


        //读取该应用的后台管理路径是否存在，如果存在，显示出来
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
     * 保存应用的后台管理路径
     */
    saveConsolePath: function(e) {
        console.info(e);
        console.log("执行更新应用后台路径操作");
        var _this = this;
        var data = e.detail.value;
        if (data.path == null || data.path.trim() == '') {
            wx.showToast({
                title: '后台管理路径不能为空',
            });
            return;
        }
        const db = wx.cloud.database();
        //保存应用信息，同一个开发者下不能有同名的应用
        db.collection("apps").doc(this.data.app_id).update({
            data: {
                consolePath: data.path,
            }
        }).then(res => {
            var pages = getCurrentPages();
            var prevPage = pages[pages.length - 2]; //上一个页面
            prevPage.setData({
                console_path: data.path,
            });
            wx.navigateBack();
        }).catch(ex => {
            console.error(ex);
        })
    }
})