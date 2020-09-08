// page/buz/buz.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
  
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  },

  /**
 * 调转到业务配置页面
 */
  jumpToBuzConfig: function () {
    wx.navigateTo({
      url: '/page/buz/buzconfig',
    })
  },

  /**
 * 调转到渠道商管理页面
 */
  jumpToDistributor:function(){
    wx.navigateTo({
      url: '/page/distributor/distributor',
    })
  },

  /**
 * 调转到业务应用管理页面
 */
  jumpToBuzApp:function(){
    wx.navigateTo({
      url: '/page/buz/buzapp/buzapp',
    })
  }
})