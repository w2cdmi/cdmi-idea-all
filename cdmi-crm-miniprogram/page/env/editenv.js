// page/env/editenv.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isNew:true,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var title = (options.title) ? options.title : '部署环境';
    title = (options.action == 'isnew') ? '新增' + title : title; 
    wx.setNavigationBarTitle({
      title: title,
    });
    //判断是新增还是修改信息
    if (options.action == 'isnew'){
      this.setData({
        isNew: true,
      })
    }else{
      this.setData({
        isNew: false,
      })
    }
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
  
  }
})