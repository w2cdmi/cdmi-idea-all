// page/manager/manager.js
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
      title: "平台管理员",
    });
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
 * 跳转到指定的平台管理员详情页
 */
  jumpToManagerDetail: function (e) {
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/manager/managerdetail?title=' + dataset.title + '&id=' + dataset.id + '&self=' + dataset.self,
    })
  },

  /**
   * 跳转到创建一个新的平台管理员页
   */
  createManager: function (e) {
    wx.navigateTo({
      url: '/page/manager/editmanager?action=新增',
    })
  }
})