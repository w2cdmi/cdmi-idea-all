// page/buz/buzapp/buzappdetail.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        buzid: '',
        buzname: '',
        buzcode: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: options.title,
        });
        this.setData({
            buzid: options.id,
            buzname: options.title,
        })
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
     * 调转到关联应用页面
     */
    jumpToLinkApps: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/console/trainer/index?title=' + dataset.title + '&id=' + dataset.id,
        })
    },

    /**
 * 调转到关联应用页面
 */
    jumpToScheme: function (e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'Scheme',
        })
    },
})