// page/system/system.js

Page({

  /**
   * 页面的初始数据
   */
  data: {
    isShowMenu:false,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    wx.setNavigationBarTitle({
      title: '系统管理',
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
   * 继承操作菜单的方法
   */
  onShowMenu: function () {
    this.setData({
      isShowMenu: "true"
    })
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
   * 调转到部署环境页面
   */
  jumpToEnv: function() {
    wx.navigateTo({
      url: '/page/env/env',
    })
  },

  /**
   * 调转到平台微服务页面
   */
  jumpToMicroService:function(){
    wx.navigateTo({
      url: '/page/microservice/microservice',
    })
  },
  
  /**
   * 调转到第三方服务配置页面
   */
  jumpToThirdService: function() {
    wx.navigateTo({
      url: '/page/thirdservice/thirdservice',
    })
  },

  /**
   * 调转到平台管理员页面
   */
  jumpToBMSManager: function() {
    wx.navigateTo({
      url: '/page/manager/manager',
    })
  },

  /**
   * 调转到接入应用管理页面
   */
  jumpToAppMgr: function() {
    wx.navigateTo({
      url: '/page/appmgr/appmgr',
    })
  },

  /**
   * 调转到微信小程序管理页面
   */
  jumpToMiniprogram: function(){
    wx.navigateTo({
      url: '/page/miniprogram/miniprogram',
    })
  },
})