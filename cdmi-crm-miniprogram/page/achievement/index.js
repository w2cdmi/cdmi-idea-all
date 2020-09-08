// page/achievement/index.js
var session = require("../../commjs/session.js");

Page({

  /**
   * 页面的初始数据
   */
  data: {
    isMultilevel: true,
    orders: [
      {
        orderid: "x28291133",
        username: "张三",
        productname: "多语闪卡",
        creatime: "13:00",
        commission: "15.00",      //佣金
      },
      {
        orderid: "x28292233",
        username: "里斯",
        productname: "爱灵格会员服务",
        creatime: "14:00",
        commission: "18.00",      //佣金
      },
      {
        orderid: "x28292233",
        username: "王菲",
        productname: "爱灵格会员服务",
        creatime: "14:00",
        commission: "18.00",      //佣金
      },
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
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
    session.login();
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