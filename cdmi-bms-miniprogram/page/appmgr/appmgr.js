// page/appmgr/appmgr.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        apps: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: '接入应用管理',
        });
        //获取列表当前开发者所拥有的接入应用列表
        const db = wx.cloud.database();
        db.collection('apps').where({
            developerId: getApp().globalData.developerId,
        }).get().then(res => {
            var apps = [];
            res.data.forEach(function(item) {
                var app = {};
                app.id = item._id;
                app.name = item.name;
                app.icon = item.icon;
                apps.push(app);
            })
            _this.setData({
                apps: apps,
            })
        })
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
        console.info("执行onHide操作");
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
     * 跳转到指定的接入应用的详情页
     */
    jumpToAppDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/appmgr/appdetail?title=' + dataset.title + '&appid=' + dataset.id,
        })
    },

    /**
     * 跳转到创建一个新的接入应用页
     */
    createApp: function(e) {
        wx.navigateTo({
            url: '/page/appmgr/editapp?action=新增',
        })
    }
})