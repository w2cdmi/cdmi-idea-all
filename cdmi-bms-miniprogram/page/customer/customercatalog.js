// page/customer/customercatalog.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        pagemodel: '',
        cataloies: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: '选择客户分类',
        });

        if (options.scene == 'options') {
            _this.setData({
                pagemodel: 'options',
            })
        };

        if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
            wx.showToast({
                title: '页面未能获取开发商信息，请联系开发商',
            })
            return;
        };
        //获取当前开发者所创建的客户类型
        const db = wx.cloud.database();
        db.collection('a_customer_types').where({
            ownerId: getApp().globalData.developerId,
        }).get().then(res => {
            var cataloies = [];
            if (res.data.length != 0) {
                res.data.forEach(function(item) {
                    var catalog = {};
                    catalog.id = item._id;
                    catalog.title = item.title; //客户分类
                    cataloies.push(catalog);
                })
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
     * 调转到新增客户分类
     */
    jumpToNewCustomerCatalog: function (e) {
        wx.navigateTo({
            url: 'editcustomertype?action=new',
        })
    },

    /**
     * 跳转到客户分类的详情编辑页
     */
    jumpToCustomerCatalog: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'editcustomertype?id=' + dataset.id,
        })
    },

    /**
     *  选择对应的客户分类
     */
    selectCustomerCatalog: function(e) {
        const dataset = e.currentTarget.dataset;
        var pages = getCurrentPages();
        var prevPage = pages[pages.length - 2]; //上一个页面
        var catalog = {};
        catalog.id = dataset.id;
        catalog.title = dataset.name;
        prevPage.setData({
            catalog: catalog,
        });
        wx.navigateBack();
    }
})