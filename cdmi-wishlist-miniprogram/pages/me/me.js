// pages/me/me.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {

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
    this.setData({
      isAdmin: getApp().globalData.isAdmin,
      avatarUrl: getApp().globalData.avatarUrl,
      nickName: getApp().globalData.nickName
    });
  },

  gotoAddressListPage: function() {
    wx.navigateTo({
      url: '/pages/address/addressList',
    })
  },

  gotoPublishProductPage: function() {
    wx.navigateTo({
      url: '/pages/releaseList/releaseList',
    })
  },

  gotoWinnerPage: function() {
    wx.navigateTo({
      url: '/pages/winner/winner?productId=ff8080816445cd4b016445d8ba0a0006',
    })
  },

  gotoHistory: function() {
    wx.navigateTo({
      url: '/pages/history/history?productId=ff8080816445cd4b016445d8ba0a0006',
    }) 
  }

})