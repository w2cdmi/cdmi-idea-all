// page/me/me.js
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

  /**
   * 跳转到团队成员管理页
   */
  navigateToMemberMgr: function(e) {
    console.info("member");
    wx.navigateTo({
      url: '/page/member/member',
    });
  },

  /**
   * 跳转到渠道管理页
   */
  navigateToDistributorMgr: function(e) {
    wx.navigateTo({
      // url: '/page/sharer/shareStatistics',
      url: '/page/distributor/distributor',
      // url: '/page/distributor/distributor',
    });
  },

  /**
   * 跳转到合约签署页
   */
  navigateToAgreementMgr: function(e) {
    wx.navigateTo({
      url: '/page/agreement/agreement',
    });
  },

  /**
   * 跳转到客户管理页
   */
  navigateToCustomerMgr: function(e) {
    wx.navigateTo({
      url: '/page/customer/customer',
    });
  },

  /**
   * 跳转到分享达人页面
   */
  navigateToShareStatistics: function(e) {
    wx.navigateTo({
      url: '/page/sharer/shareStatistics',
    });
  },

  /**
   * 跳转到代理商品页面
   */
  navigateToAgentProductMgr: function(e) {
    wx.navigateTo({
      url: '/page/agentproduct/product',
    });
  },

  /**
   * 跳转到系统配置页面
   */
  navigateToSettingMgr: function(e) {
    wx.navigateTo({
      url: '/page/setting/setting',
    });
  },
  
  /**
   * 跳转到订单管理页面
   */
  navigateToOrderMgr:function(e){
    wx.navigateTo({
      url: '/page/order/order',
    });
  }
})