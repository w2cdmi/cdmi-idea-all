// pages/releaseList/releaseList.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isShowDrawer: false,
    list: [{
      img: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531198732304&di=4fe0860323dcb1a5c2865ee697ba8cc0&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2018-1%2F2018011116445675984.jpg',
      title: '123',
      originalPrice: '123',
      actualPrice: '123',
      ratedNumber: '123',
      onlookerNumber: '123',
      participantNumber: '123'
    }]
  },

  showDrawer: function (e) {
    var item = e.currentTarget.dataset.item;
    this.setData({
      isShowDrawer: true,
      item: item
    })
  },

  hideDrawer: function () {
    this.setData({
      isShowDrawer: false,
    })
  },

  releaseItem: function () {
    var item = this.data.item;
    wx.showModal({
      title: '提示',
      content: '确定要发布吗？',
      success: function (e) {
        if (e.confirm) {

        }
      }
    })
    this.hideDrawer();
    
  },

  editItem: function () {
    var item = this.data.item;
    wx.navigateTo({
      url: '/pages/releaseList/release/release?id=' + item.id,
    }, () => {
      this.hideDrawer();
    });
  },

  deleteItem: function () {
    var item = this.data.item;
    this.hideDrawer();
    wx.showModal({
      title: '提示',
      content: '确定要删除吗？',
      success: function (e) {
        if (e.confirm) {

        }
      }
    })
  }

})