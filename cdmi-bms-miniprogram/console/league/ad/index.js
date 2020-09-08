// console/league/ad/index.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        adsenses: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '广告位管理',
        });

        console.log("获取广告位列表");
        var _this = this;
        const db = wx.cloud.database();
        db.collection('adsenses').where({
            appId: getApp().globalData.consoleAppId,
        }).get().then(res => {
            if (res.data.length != 0) {
                var adsenses = [];
                res.data.forEach(function(item) {
                    var adsense = {};
                    adsense.id = item._id;
                    adsense.name = item.name;
                    adsense.sign = item.sign;
                    adsense.mouthprice = item.mouthprice;
                    adsense.yearprice = item.yearprice;
                    adsenses.push(adsense);
                })
                _this.setData({
                    adsenses: adsenses,
                })
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
     * 跳转到广告位详情页
     */
    jumpToAdsenseDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'editadsense?action=edit&id=' + dataset.id,
        })
    },

    /**
 * 跳转到新增广告位页面
 */
    jumpToNewAdsense: function (e) {
        wx.navigateTo({
            url: 'editadsense',
        })
    }
})