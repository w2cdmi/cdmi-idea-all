// console/league/projectdetail.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        catalog: {},
        name: '',
        amount: null,
        advantages: '',
        directstore_count: null,
        napastore_count: null,
        scheme: {}, //招商项目方案

        view_window: '',
        cover: 'contain', //contain：包含，fill：填充，cover：覆盖
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '编辑招商项目',
        });
        if (options.id == null && options.id == '') {
            console.error("缺少必须的传入参数[id]，请联系开发商");
            return;
        }
        var _this = this;
        const db = wx.cloud.database();
        db.collection("zs_released_projects").doc(options.id).get().then(res => {
            if (res.data == null) {
                wx.showToast({
                    title: '传入的招商项目草稿信息未找到，请退出后重试',
                });
            }

            _this.setData({
                id: res.data._id,
                name: res.data.name,
                catalogid: res.data.catalogid,
                name: res.data.name,
                amount: res.data.amount,
                advantages: res.data.advantages.join(' '),
                directstore_count: res.data.directstore_count,
                napastore_count: res.data.napastore_count,
                scheme: res.data.scheme,
                status: 'unreleased',
            });
            db.collection("a_cataloies").doc(res.data.catalogid).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '传入的招商项目草稿信息未找到，请退出后重试',
                    });
                    return;
                }
                var catalog = {};
                catalog.id = res.data._id;
                catalog.name = res.data.name;
                if (res.data.parentId != null && res.data.parentId != '') {
                    db.collection("a_cataloies").doc(res.data.parentId).get().then(parent => {
                        if (parent.data != null) {
                            catalog.name = parent.data.name + ' - ' + catalog.name;
                        }
                        _this.setData({
                            catalog: catalog,
                        });
                    });
                } else {
                    _this.setData({
                        catalog: catalog,
                    });
                }
            });
        });
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function() {
        console.info(this.data.view_window);
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

    /** 列举所有的广告位 */
    listAdsense: function () {
        var _this = this;
        const db = wx.cloud.database();
        db.collection('adsenses').where({
            appId: getApp().globalData.consoleAppId,
        }).get().then(res => {
            if (res.data.length != 0) {
                var adsenses = [];
                res.data.forEach(function (item) {
                    var adsense = {};
                    adsense.id = item._id;
                    adsense.name = item.name;
                    adsense.sign = item.sign;
                    adsense.mouthprice = item.mouthprice;
                    adsense.yearprice = item.yearprice;
                    adsenses.push(adsense);
                });
                _this.setData({
                    adsenses: adsenses,
                    view_window: 'public',
                });
            }
        });
    },

    publicToAdsense:function(){

    }
})