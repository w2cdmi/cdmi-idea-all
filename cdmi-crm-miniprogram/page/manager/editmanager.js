// page/manager/editmanager.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    current_order: '',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var title = '平台管理员';
    if (options != null && options.title != null) {
      title = options.title;
    };
    wx.setNavigationBarTitle({
      title: options.action + title,
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
   * 判断是否要显示删除图标
   */
  judgeViewDeleteImage: function(value, order) {
    if (value == null || value.length == 0) {
      this.setData({
        current_order: '',
      })
    } else {
      this.setData({
        current_order: order,
      })
    }
  },

  /**
   * 在文本框中输入文字，显示删除图标
   */
  inputText: function(e) {
    const value = e.detail.value;
    const dataset = e.currentTarget.dataset;
    if (value == null || value.length == 0) {
      this.setData({
        current_order: '',
      })
    } else {
      this.setData({
        current_order: dataset.order,
      })
    }
  },

  /**
   * 光标切换时候，判断是否要显示删除图标
   */
  focusText: function(e) {
    const value = e.detail.value;
    const dataset = e.currentTarget.dataset;
    if (value == null || value.length == 0) {
      this.setData({
        current_order: '',
      })
    } else {
      this.setData({
        current_order: dataset.order,
      })
    }
  },


})