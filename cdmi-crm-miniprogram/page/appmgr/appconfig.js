// page/appmgr/appconfig.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    app_id: '',
    app_name: '',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    wx.setNavigationBarTitle({
      title: options.title,
    });
    this.setData({
      app_id: options.appid,
      app_name: options.appname,
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
   * 跳转到创建一个新的接入部署配置参数页
   */
  createAppConfig: function(e) {
    wx.navigateTo({
      url: '/page/appmgr/editappconfig?action=新增',
    })
  },

  /**
 * 跳转到指定接入应用的配置参数的详细页
 */
  jumpToAppConfigItem: function (e) {
      const dataset = e.currentTarget.dataset;
      wx.navigateTo({
        url: '/page/appmgr/appconfigdetail?title=' + dataset.title + '&id=' + dataset.id + '&appid=' + this.data.app_id + '&appname=' + this.data.app_name,
      })
    }
})