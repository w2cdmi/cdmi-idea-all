// page/agreement/agreementlist.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        agreements: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '合约管理',
        });
        var _this = this;
        //获取当前开发者所拥有的合约
        const db = wx.cloud.database();
        db.collection('agreements').where({
            initiator_id: getApp().globalData.developerId,
        }).get().then(res => {
            var agreements = [];
            if (res.data.length != 0) {
                res.data.forEach(function(item) {
                    var agreement = {};
                    agreement.id = item._id;
                    agreement.buz_id = item.buz_id;
                    agreement.buz_name = item.buz_name;
                    agreement.ratio = item.ratio;
                    agreement.invitee_name = item.invitee_name;
                    agreement.status = item.status;
                    switch (item.status) {
                        case 'ToBeSigned':
                            agreement.status = "待对方签署";
                            break;
                        case 'Rejected':
                            agreement.status = "被对方拒绝";
                            break;
                        case 'Expired':
                            agreement.status = "协议已失效";
                            break;
                        case 'OK':
                            agreement.status = "已签署协议";
                            break;
                        default:
                            agreement.status = '';
                            break;
                    }
                    agreements.push(agreement);
                })
                _this.setData({
                    agreements: agreements,
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

    jumpToAgreementDetail: function(e){
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            // url: '/page/distributor/agreement?id=' + dataset.id,
            url: '/page/agreement/acceptAgreement?id=' + dataset.id,
        })
    }
})