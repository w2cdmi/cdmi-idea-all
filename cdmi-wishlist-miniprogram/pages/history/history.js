// pages/history/history.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [{
      img: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531198732304&di=4fe0860323dcb1a5c2865ee697ba8cc0&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2018-1%2F2018011116445675984.jpg',
      title: '123',
      originalPrice: '123',
      actualPrice: '123',
      ratedNumber: '123',
      onlookerNumber: '123',
      participantNumber: '123',
      headImageUrl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1531198732304&di=4fe0860323dcb1a5c2865ee697ba8cc0&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2018-1%2F2018011116445675984.jpg',
      userName: '张三'
    },]
  },

  onLoad: function () {
    // getHistoryInfo({page: this});
  }

})


function getHistoryInfo({ page }) {
  Product.getHistoryInfo(function (data) {
    if (data == undefined || data.length == 0) {
      page.setData({
        isShowProduct: false
      });

      return;
    }
    page.setData({
      list: data
    });

  });
}
