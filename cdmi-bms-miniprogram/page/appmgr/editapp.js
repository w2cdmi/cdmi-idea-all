// page/appmgr/editapp.js
var config = require("../../commjs/config.js");
var httpClient = require("../../commjs/httpclient.js");

Page({

    /**
     * 页面的初始数据
     */
    data: {
        name: '',
        icon: '',
        desc: '',
        linkServices: [],
        consolePath: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '接入应用';
        if (options != null && options.title != null) {
            title = options.title;
        };
        wx.setNavigationBarTitle({
            title: options.action + title,
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
     * 输入或修改接入应用的名字
     */
    changeName: function(e) {
        this.data.appinfo.name = e.detail.value;
    },

    /**
     * 改变接入应用的图标
     */
    changeIcon: function() {
        wx.navigateTo({
            url: '../appmgr/changeIcon',
        })
    },

    /**
     * 保存新的接入应用信息
     */
    saveNewApp: function(e) {
        var _this = this;
        var data = e.detail.value;
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '应用的名称不能为空',
            });
            return;
        }
        const db = wx.cloud.database();
        //保存应用信息，同一个开发者下不能有同名的应用
        db.collection("apps").where({
            developerId: getApp().globalData.developerId,
            name: data.name.trim(),
        }).count().then(res => {
            if (res.total == 0) {
                db.collection('apps').add({
                    data: {
                        name: data.name.trim(),
                        icon: data.icon,
                        desc: data.desc,
                        consolePath: data.consolePath,
                        linkServices: data.linkServices,
                        developerId: getApp().globalData.developerId,
                        accountId: getApp().globalData.userId,
                        creatime: new Date(),
                    },
                }).then(res => {
                    wx.navigateTo({
                        url: '/page/appmgr/appmgr',
                    })
                });
            } else {
                wx.showToast({
                    title: '您已创建有同名的应用，请重新输入名称',
                })
            }
        })
    }
})