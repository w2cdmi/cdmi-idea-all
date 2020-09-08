var Session = require("../../common/session.js");

Page({

    /**
     * 页面的初始数据
     */
    data: {

    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {

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
        Session.isLogin();
    },
    /**
     * 点击预设模版
     */
    goToOfficeTemplate: function() {
        wx.navigateTo({
            url: '/pages/sceneWorkshop/officeTemplate/officeTemplate',
        })
    },
    /**
     * 自由创作
     */
    freeCreateScene: function() {
        if (wx.getStorageSync("loginStatus")) {
            wx.navigateTo({
                url: '/pages/sceneWorkshop/freeCreate/freeCreate',
            })
        } else {
            wx.showToast({
                title: '用户登录过期,请重新登录',
                icon: "none"
            })
        }
    },
    /**
     * 我的场景
     */
    workMyscene:function(){
        if (wx.getStorageSync("loginStatus")) {
            wx.navigateTo({
                url: '/pages/sceneWorkshop/workMyscene/workMyscene',
            })
        } else {
            wx.showToast({
                title: '用户登录过期,请重新登录',
                icon: "none"
            })
        }
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

    }
})