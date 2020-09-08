// page/distributor/setAgentBuz.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        distributorId: '', //代理商ID
        buz_items: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: '选择代理业务',
        });
        if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
            wx.showToast({
                title: '页面未能获取开发者信息，请联系开发商',
            })
            return;
        };
        if (options.distributorid == null || options.distributorid.trim() == '') {
            wx.showToast({
                title: '页面未能获取渠道商信息',
            })
            return;
        } else {
            _this.data.distributorId = options.distributorid.trim();
        }
        //获取当前开发者所拥有的业务
        const db = wx.cloud.database();
        db.collection('buz_items').where({
            developerId: getApp().globalData.developerId,
        }).get().then(res => {
            if (res.data.length != 0) {
                Promise.all(res.data.map((item) => {
                    return new Promise((resolve, reject) => {
                        db.collection("agreements").where({
                            buz_id: item._id,
                            invitee_type: 'DISTRIBUTOR',
                            invitee_id: _this.data.distributorId,
                            initiator: getApp().globalData.developerId,
                            initiator_type: 'DEVELOPER',
                        }).get().then(res =>{
                            var status = '';
                            if (res.data.length == 1){
                                switch (res.data[0].status){
                                    case 'ToBeSigned':
                                        status = "待对方签署"; 
                                        break;
                                    case 'Rejected':
                                        status = "被对方拒绝"; 
                                        break;
                                    case 'Expired':
                                        status = "协议已失效";
                                        break;
                                    case 'OK':
                                        status = "已签署协议";
                                        break;
                                    default:
                                        break;
                                }
                            } else if (res.data.length > 1){
                                for (var i = 0; i < res.data.length;i++){
                                    if (res.data[i].status == "OK") {
                                        status = "已签署协议";
                                        break;
                                    }
                                }
                            }
                            var buz_item = {};
                            buz_item.id = item._id;
                            buz_item.name = item.name;
                            buz_item.desc = item.desc;
                            buz_item.linkApps = item.linkApps;
                            buz_item.status = status;
                            resolve(buz_item);
                        }).catch(ex =>{
                            console.error(ex);
                        });
                    });
                })).then(result => {
                    _this.setData({
                        buz_items: result,
                    })
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
     * 选择与代理商签署代理协议
     */
    selectBuzApp: function(e) {
        const dataset = e.currentTarget.dataset;
        //检查协议是否已经产生
        var _this = this;
        const db = wx.cloud.database();
        db.collection('agreements').where({
            buz_id: dataset.buzid,
            invitee_type:'DISTRIBUTOR',
            invitee_id: this.data.distributorId,
            initiator_id: getApp().globalData.developerId,
            initiator_type:'DEVELOPER',
        }).get().then(res =>{
            console.info(res);
            if(res.data.length == 0){
                wx.navigateTo({
                    url: '/page/distributor/agreement?buzid=' + dataset.buzid + "&distributorid=" + this.data.distributorId,
                })
            } else if (res.data.length == 1){
                wx.navigateTo({
                    url: '/page/distributor/agreement?id=' + res.data[0]._id,
                })
            }else{
                wx.navigateTo({
                    url: '/page/agreement/agreementlist?buzid=' + dataset.buzid + "&distributorid=" + this.data.distributorId,
                })
            }
        });

    }
})