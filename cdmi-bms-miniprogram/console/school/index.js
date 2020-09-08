// console/school/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        app_id: '',
        app_name: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        //检查必要的参数：appid
        if (options.appid == null || options.appid == "") {
            wx.showToast({
                title: '缺失必要的请求参数[id]，请联系开发商',
            });
            return;
        };
        wx.setNavigationBarTitle({
            title: '应用后台管理',
        });

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
            });
            //必须：修改当前操作的接入应用
            getApp().globalData.consoleAppId = options.appid;
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
     * 跳转到课程类目页面
     */
    jumpToCourseCataloies: function() {
        wx.navigateTo({
            url: '/console/school/cataloies',
        })
    },

    /**
     * 跳转到专家列表页面
     */
    jumpToExperts: function() {
        wx.navigateTo({
            url: '/console/school/expert/expert',
        })
    },

    /**
     * 跳转到分类标记列表页面
     */
    jumpToMarkers: function() {
        wx.navigateTo({
            url: '/console/school/markers',
        })
    },

    /**
     * 跳转到频道列表页面
     */
    jumpToChannels: function() {
        wx.navigateTo({
            url: '/console/school/channels',
        })
    },

    /**
     * 跳转到培训机构页面
     */
    jumpToShops:function(){
        wx.navigateTo({
            url: '/console/school/trainplace/shopes',
        })
    }
})