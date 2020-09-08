var sessionService = require('../session.js');
var productService = require('../module/product.js');
var payService = require('../module/pay.js');
var onlookerService = require('../module/onlooker.js');
var winnerService = require('../module/winner.js');
var participantService = require('../module/participant.js');
var config = require('../module/config.js');
var util = require('../../utils/util.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    countdown: '00 : 00 : 00',
    images: [],
    product: {
      id: '',
      title: '',
      actualPrice: 0,
      originalPrice: 0,
      onlookerNumber: 0,
      ratedNumber: 0,
      participantNumber: 0
    },
    isShowJoinBtn: true,
    isFirstLoad: true,
    sponsorId: "" //邀请人Id
  },

  onLoad: function(options) {
    var sponsorId = options.sponsorId;
    if (sponsorId != undefined && sponsorId != "") {
      this.data.sponsorId = sponsorId;
    }
  },

  unifiedorder: function() {
    var page = this;
    var data = {
      productId: page.data.product.id
    }
    payService.unifiedorder(data, function(data) {
      wx.requestPayment({
        'timeStamp': data.timeStamp,
        'nonceStr': data.nonceStr,
        'package': data.package,
        'signType': data.signType,
        'paySign': data.paySign,
        'success': function(res) {
          wx.showToast({
            title: '支付成功！',
            duration: 1000
          });
          // setTimeout(function () {
          //     wx.navigateTo({
          //         url: '/pages/address/address',
          //     })
          // }, 1000);
        },
        'fail': function(res) {
          wx.showToast({
            title: '支付失败，请稍后重试',
            icon: 'none',
            duration: 1000
          })
        }
      })
    })
  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    var page = this;

    sessionService.login();
    sessionService.invokeAfterLogin(function() {

      getProductInfo(page);
      getWinnerAll(page);

      if (!page.data.isFirstLoad) {
        return;
      }

      //建立连接
      wx.connectSocket({
        url: config.ws_host + "/websocket"
      })
      //连接成功
      wx.onSocketOpen(function() {
        console.log("websocket连接成功！");
        page.data.isFirstLoad = false;
      })
      //接收数据
      wx.onSocketMessage(function(data) {
        var message = JSON.parse(data.data);
        if (message.countdown != undefined) {
          page.setData({
            countdown: util.formatNewestTime(message.countdown)
          });
        } else if (message.onlookerNumber != undefined) {
          page.data.product.onlookerNumber = message.onlookerNumber;
          page.setData({
            product: page.data.product
          });
        } else if (message.participantNumber != undefined) {
          page.data.product.participantNumber = message.participantNumber;
          page.setData({
            product: page.data.product
          });
        }
      })
      //连接失败
      wx.onSocketError(function(res) {
        console.log(res);
        console.log('websocket连接失败！');
      })
    });

  },
  onShareAppMessage: function() {
    var path = "pages/lottery/lottery?sponsorId=" + getApp().globalData.userId;
    return {
      title: "半价购买" + this.data.product.title,
      path: "pages/lottery/lottery?sponsorId=" + getApp().globalData.userId,
      success: function(res) {
        console.log("转发成功")
        // 转发成功
      },
      fail: function(res) {
        console.log("转发失败")
        // 转发失败
      },
      complete: function() {
        console.log("转发结束")
      }
    }
  }
})

function getProductInfo(page) {
  productService.getProductDetail(function(data) {
    if (data == undefined || data.length == 0) {
      page.setData({
        isShowProduct: false
      });

      return;
    }
    //先取首个众筹商品
    var product = data[0];
    if (product != undefined && product.photoList != undefined) {
      var photoList = product.photoList;
      var imageList = photoList.split(",");
      for (var i = 0; i < imageList.length; i++) {
        imageList[i] = config.host + "/wishlist/products/v1/download/" + imageList[i];
      }
      page.setData({
        isShowProduct: true,
        product: product,
        images: imageList
      });
    }

    createOnlooker(product.id, page.data.sponsorId);
    isJoinLottery(product.id, page);
  });
}

function createOnlooker(productId, sponsorId) {
  var data = {
    productId: productId
  }
  if (sponsorId != undefined && sponsorId != "") {
    data.inviterId = sponsorId;
  }
  onlookerService.createOnlooker(data, function(data) {
    if (data != undefined) {

    }
  });
}

function getWinnerAll(page) {
  winnerService.getWinnerAll(function(data) {
    data.forEach((item) => {
      var index = item.createTime.indexOf("T");
      item.createDate = item.createTime.substring(0, index);
      // item.createDate = util.getFormatDate(new Date(item.createTime).getTime());
    });

    page.setData({
      winners: data
    });
  });
}

function isJoinLottery(productId, page) {
  participantService.isJoinLottery(productId, function(data) {
    page.setData({
      isShowJoinBtn: !data
    });
  });
}