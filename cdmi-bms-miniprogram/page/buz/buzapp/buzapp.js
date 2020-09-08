// page/buz/buzapp/buzapp.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        buz_items: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: '业务项目',
        });
        if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
            wx.showToast({
                title: '页面未能获取开发商信息，请联系开发商',
            })
            return;
        };
        //获取当前开发者所拥有的业务
        const db = wx.cloud.database();
        db.collection('buz_items').where({
            developerId: getApp().globalData.developerId,
        }).get().then(res => {
            var buz_items = [];
            if (res.data.length != 0) {
                res.data.forEach(function(item) {
                    var buz_item = {};
                    buz_item.id = item._id;
                    buz_item.name = item.name;
                    buz_item.desc = item.desc;
                    buz_item.linkApps = item.linkApps;
                    buz_items.push(buz_item);
                })
                _this.setData({
                    buz_items: buz_items,
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
     * 调转到业务应用页面
     */
    jumpToBuzAppDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/buz/buzapp/buzappdetail?title=' + dataset.title + '&id=' + dataset.id,
        })
    },

    /**
     * 调转到新创建业务应用页面
     */
    createBuzApp: function(e) {
        wx.navigateTo({
            url: '/page/buz/buzapp/newbuzapp',
        })
    }
})