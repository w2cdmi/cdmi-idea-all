// page/distributor/distributorcataloies.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        channels: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '渠道商分类',
        });
        var _this = this;
        const db = wx.cloud.database();
        db.collection('distributor_cataloies').where({
            ownerId: getApp().globalData.developerId,
            ownerType:'DEVELOPER',
        }).get().then(res => {
            var channels = [];
            res.data.forEach(function(item) {
                var channel = {};
                channel.id = item._id;
                channel.name = item.name;
                channel.icon = item.icon;
                channels.push(channel);
            })
            _this.setData({
                channels: channels,
            })
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
     * 创建一个新的频道
     */
    createChannel: function() {
        wx.navigateTo({
            url: 'editdistributorcataloies?action=new',
        })
    },

    /**
     * 跳转到频道详情页面
     */
    jumpToChannelDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'editdistributorcataloies?action=edit&id=' + dataset.id,
        })
    },
})