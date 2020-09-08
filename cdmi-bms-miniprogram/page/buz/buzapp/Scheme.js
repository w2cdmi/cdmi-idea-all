// page/buz/buzapp/Scheme.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        suport_distribution: true,
        probation_view: "无", //试用期
        probation_value: 0,
        strategy_value: 0,
        strategy_view: '', //
        enableAreaExclusive: true, //区域独家经营权,收入与区域经销商进行再分配
        enableBuzExclusive: false, //业务独家经营权,该业务收入与业务经销商进行再分配
        areaExclusiveCount: 0, //当前区域独家经营权数量
        buzExclusiveCount: 0, //当前区域独家经营权数量
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '推广方案',
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
     * 跳转到分销策略页面
     */
    navToStrategy: function(e) {
        wx.navigateTo({
            url: 'SchemeStrategy',
        })
    },

    /**
     * 跳转到转发奖励页面
     */
    navToShareReward: function(e) {
        wx.navigateTo({
            url: 'SchemeShareReward',
        })
    },

    /**
     * 跳转到转发奖励页面
     */
    navToProbation: function(e) {
        wx.navigateTo({
            url: 'SchemeProbation',
        })
    },

    /**
     * 是否支持多级分销
     */
    switchChange: function(e) {
        this.setData({
            suport_distribution: e.detail.value,
        });
    },

    /**
     * 关闭或启用区域独家经营权
     */
    changeEnableAreaExclusive: function(e) {
        this.setData({
            enableAreaExclusive: e.detail.value,
        });
    },

    /**
     * 关闭或启用独家业务经营权
     */
    changeEnableBuzExclusive: function(e) {
        this.setData({
            enableBuzExclusive: e.detail.value,
        });
    },

    /**
     * 调转到独家业务代理商业务
     */
    navBuzExclusive: function() {

    },

    /**
     * 调转到独家区域代理商业务
     */
    navAreaExclusive: function() {

    },
})