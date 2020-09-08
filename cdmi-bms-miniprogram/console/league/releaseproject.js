// console/league/releaseproject.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        projects: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '已发布的招商项目',
        });

        var _this = this;
        const db = wx.cloud.database();
        db.collection('zs_released_projects').where({
            appId: getApp().globalData.consoleAppId,
        }).get().then(res => {
            if (res.data.length != 0) {
                var projects = [];
                res.data.forEach(function(item) {
                    var project = {};
                    project.id = item._id;
                    project.name = item.name;
                    project.amount = item.amount;
                    projects.push(project);
                });
                _this.setData({
                    projects: projects,
                });
            }
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
     * 跳转到招商项目详情页
     */
    jumpToProjectDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'projectdetail?id=' + dataset.id,
        })
    }
})