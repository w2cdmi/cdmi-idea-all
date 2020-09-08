// page/distributor/distributor.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        distributors: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: '渠道商',
        });
        if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
            wx.showToast({
                title: '页面未能获取开发商信息，请联系开发商',
            })
            return;
        };
        //获取当前开发者所拥有的一级渠道
        const db = wx.cloud.database();
        db.collection('distributors').where({
            ownerId: getApp().globalData.developerId,
        }).get().then(res => {
            if (res.data.length != 0) {
                Promise.all(res.data.map((item) => {
                    return new Promise((resolve, reject) => {
                        var distributor = {};
                        distributor.id = item._id;
                        distributor.name = item.name;
                        distributor.type = item.targetType;
                        distributor.status = item.status;
                        distributor.catalog = item.catalog;

                        if (item.level != null && item.level !='') { 
                            db.collection('distributor_levels').doc(item.level).get().then(res => {
                                if (res.data != null) {
                                    distributor.level = res.data.title;
                                }
                                resolve(distributor);
                            }).catch(ex =>{
                                resolve(distributor);
                            });
                        } else {
                            resolve(distributor);
                        }
                    });
                })).then(result => {
                    _this.setData({
                        distributors: result,
                    })
                }).catch(e =>{
                    console.error(e);
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
     * 新增渠道商
     */
    createDistributor: function() {
        console.log();
        wx.navigateTo({
            url: '/page/distributor/editdistributor?action=新增&distributorid=' + this.data.distributorid + '&distributorname=' + this.data.distributorname,
        })
    },

    /**
     * 跳转到指定的渠道商的详情页
     */
    jumpToDistributorDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/distributor/distributordetail?title=' + dataset.title + '&id=' + dataset.id,
        })
    }
})