// page/miniprogram/miniprogramdetail.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    app_id: '',               //小程序对应的应用Id
    miniprogram_id: '',       //小程序对应的终端服务Id
    miniprogram_name: '小程序',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.setNavigationBarTitle({
      title: options.title,
    });
    this.setData({
      miniprogram_id: options.id,
      miniprogram_name: options.title,
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
   * 调转到小程序开发信息页
   */
  jumpToMiniprogramDevelop: function(e){
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/miniprogram/miniprogramdevelop?title=小程序开发信息' + '&miniprogramid=' + this.data.miniprogram_id + '&miniprogramname=' + this.data.miniprogram_name,
    })
  }
})