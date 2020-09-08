// page/distributor/distributor.js
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
      title: '渠道商',
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
 * 新增渠道商
 */
  createDistributor: function () {
    console.log();
    wx.navigateTo({
      url: '/page/distributor/editdistributor?action=新增&distributorid=' + this.data.distributorid + '&distributorname=' + this.data.distributorname,
    })
  },

  /**
   * 跳转到指定的渠道商的详情页
   */
  jumpToDistributorDetail: function (e) {
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/distributor/distributordetail?title=' + dataset.title + '&id=' + dataset.id,
    })
  }
})