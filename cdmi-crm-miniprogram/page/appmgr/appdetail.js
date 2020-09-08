// page/appmgr/appdetail.js

Page({

  /**
   * 页面的初始数据
   */
  data: {
    appid: '',
    appname: '',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    wx.setNavigationBarTitle({
      title: options.title,
    });
    this.setData({
      appid: options.id,
      appname: options.title,
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
   * 跳转到指定接入应用的应用管理员列表页
   */
  jumpToAppManagerList: function(e) {
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/appmgr/appmanager?title=' + dataset.title + '&id=' + dataset.id,
    })
  },

  /**
   * 跳转到指定接入应用的终端应用管理页
   */
  jumpToTerminalApps: function(e) {
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/appmgr/terminalmgr?title=' + dataset.title + '&id=' + dataset.id,
    })
  },

  /**
   * 跳转到指定接入应用的开发信息页
   */
  jumpToAppDevelop:function(e){
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/appmgr/appmgrdevelop?title=应用开发信息' + '&appid=' + this.data.appid + '&appname=' + this.data.appname,
    })
  },

  /**
   * 跳转到指定接入应用的配置参数页
   */
  jumpToAppConfig: function (e) {
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/appmgr/appconfig?title=应用配置参数' + '&appid=' + this.data.appid + '&appname=' + this.data.appname,
    })
  }
})