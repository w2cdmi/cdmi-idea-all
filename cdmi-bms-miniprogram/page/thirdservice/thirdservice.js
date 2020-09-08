// page/thirdservice/thirdservice.js
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
    wx.setNavigationBarTitle({
      title: '第三方服务配置',
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
   * 跳转到微信开放平台信息页
   */
  jumpToOpenWx: function() {
    wx.navigateTo({
      url: '/page/thirdservice/openwx/openwx',
    })
  },

  /**
   * 跳转到微信第三方平台信息页
   */
  jumpToOpenWxThird: function() {
    wx.navigateTo({
      url: '/page/thirdservice/openwxthird/openwxthird',
    })
  },

  /**
   * 跳转到汇智文档云信息页
   */
  jumpToDocumentService: function() {
    wx.navigateTo({
      url: '/page/thirdservice/openwxthird/openwxthird',
    })
  }
})