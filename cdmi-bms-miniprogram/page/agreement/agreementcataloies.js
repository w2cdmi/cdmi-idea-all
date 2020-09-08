// page/agreement/agreementcataloies.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        cataloies:[],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: '合约模板',
        });
        if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
            wx.showToast({
                title: '页面未能获取开发商信息，请联系开发商',
            })
            return;
        };
        //获取当前开发者所创建的合约模板
        const db = wx.cloud.database();
        db.collection('agreement_cataloies').where({
            ownerId: getApp().globalData.developerId,
        }).get().then(res => {
            var cataloies = [];
            if (res.data.length != 0) {
                res.data.forEach(function (item) {
                    var catalog = {};
                    catalog.id = item._id;
                    catalog.name = item.name; //合约分类名称
                    cataloies.push(catalog);
                })
                console.info(cataloies);
                _this.setData({
                    cataloies: cataloies,
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
     * 跳转到合约模板详情编辑页
     */
    jumpToCustomerCatalogDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'editagreementcataloies?id=' + dataset.id,
        });
    },

    /**
     * 跳转到合约模板新建页
     */
    jumpToNewCustomerType: function (e) {
        wx.navigateTo({
            url: 'editagreementcataloies?action=new',
        })
    },
})