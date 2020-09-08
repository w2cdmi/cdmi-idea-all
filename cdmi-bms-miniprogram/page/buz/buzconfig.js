// page/buz/buzconfig.js
Page({

    /**
     * 页面的初始数据
     */
    data: {

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

    jumpToDistributorCataloies:function(){
        wx.navigateTo({
            url: '/page/distributor/distributorcataloies',
        })
    },

    jumpToDistributorLevel: function() {
        wx.navigateTo({
            url: '/page/distributor/distributorlevel',
        })
    },

    jumpToCustomerType:function(){
        wx.navigateTo({
            url: '/page/customer/customercatalog',
        })  
    },

    /**
     * 跳转到合约分类页面
     */
    jumpToAgreementcataloies:function(e){
        wx.navigateTo({
            url: '/page/agreement/agreementcataloies',
        })  
    },
})