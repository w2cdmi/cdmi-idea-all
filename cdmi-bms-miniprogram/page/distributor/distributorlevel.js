// page/distributor/distributorlevel.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        levels: [],
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
        var _this = this;
        wx.setNavigationBarTitle({
            title: '渠道商等级',
        });
        if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
            wx.showToast({
                title: '页面未能获取开发商信息，请联系开发商',
            })
            return;
        };
        //获取当前开发者所创建的渠道等级
        const db = wx.cloud.database();
        db.collection('distributor_levels').where({
            ownerId: getApp().globalData.developerId,
        }).get().then(res => {
            if (res.data.length != 0) {
                Promise.all(res.data.map((item) => {
                    return new Promise((resolve, reject) => {
                        if (item.catalogId != null && item.catalogId != '') {
                            db.collection('distributor_cataloies').doc(item.catalogId).get().then(res => {
                                if (res != null) {
                                    var level = {};
                                    level.id = item._id;
                                    level.title = item.title; //渠道等级
                                    level.ratio = item.ratio; //分成比例
                                    level.catalogTitle = " - " + res.data.name;
                                    level.catalogId = item.catalogId;
                                    resolve(level);
                                } else {
                                    resolve({});
                                }
                            })
                        } else {
                            var level = {};
                            level.id = item._id;
                            level.title = item.title; //渠道等级
                            level.ratio = item.ratio; //分成比例
                            resolve(level);
                        }
                    });
                })).then(result => {
                    _this.setData({
                        levels: result,
                    })
                });
            }
        });
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

    jumpToNewDistributorLevel: function() {
        wx.navigateTo({
            url: '/page/distributor/editdistributorlevel?action=new',
        })
    },

    jumpToDistributorLevelDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        if (dataset.catalog == null || dataset.catalog == '') {
            wx.navigateTo({
                url: '/page/distributor/editdistributorlevel?id=' + dataset.id,
            })
        } else {
            wx.navigateTo({
                url: '/page/distributor/editdistributorlevel?id=' + dataset.id + "&catalogid=" + dataset.catalog,
            })
        }
    }

})