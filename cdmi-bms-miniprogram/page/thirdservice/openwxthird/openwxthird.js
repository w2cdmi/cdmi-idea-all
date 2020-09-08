// page/thirdservice/openwxthird/openwxthird.js
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
    wx.setNavigationBarTitle({
      title: '微信第三方平台',
    })
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
   * 新增微信开放平台信息
   */
  createOpenWxThird: function () {

  },

  /**
   * 跳转到微信第三方平台详情页
   */
  jumpToOpenWxThirdDetail: function (e) {
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/thirdservice/openwxthird/openwxthirddetail?title=' + dataset.title + '&id=' + dataset.id,
    })
  }
})