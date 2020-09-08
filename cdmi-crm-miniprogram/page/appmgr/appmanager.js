// page/appmgr/appmanager.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    appid: '',
    appname:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.setNavigationBarTitle({
      title: options.title + "-应用管理员",
    });
    console.log("appid:" + options.id);
    console.log("appname:" + options.title);
    this.setData({
      appid: options.id,
      appname: options.title,
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
 * 跳转到指定的应用管理员详情页
 */
  jumpToManagerDetail: function (e) {
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/appmgr/appmanagerdetail?title=' + dataset.title + '&id=' + dataset.id + '&appid=' + this.data.appid + '&appname=' + this.data.appname,
    })
  },

  /**
   * 新增应用管理员
   */
  createAppManager: function() {
    console.log();
    wx.navigateTo({
      url: '/page/appmgr/editappmanager?action=新增&appid=' + this.data.appid + '&appname=' + this.data.appname,
    })
  }
})