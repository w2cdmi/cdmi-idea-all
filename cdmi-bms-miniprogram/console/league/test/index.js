// console/league/test/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        cataloies: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        const db = wx.cloud.database();
        var appId = "W-QNGXhEiJmgalzU";
        //获得首层分类列表
        db.collection('a_cataloies').where({
            appId: appId,
            parentId: '',
        }).get().then(res => {
            console.info(res);
            if (res.data.length != 0) {
                var cataloies = [];
                res.data.forEach(function(item) {
                    var catalog = {};
                    catalog.id = item._id;
                    catalog.name = item.name;
                    cataloies.push(catalog);
                });
                _this.setData({
                    cataloies: cataloies,
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

    }
})