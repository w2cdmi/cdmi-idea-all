// page/index.js
var windowWidth = 0; //设备屏幕的宽
var windowHeight = 0; //设备屏幕的高
var original_imageHeight = ''; //原始图片高度
var original_imageWidth = ''; //原始图片高度

Page({

  /**
   * 页面的初始数据
   */

  data: {
    viewWidth: "", //视图框宽度
    viewHeight: "", //试图框高度
    shareTempFilePath: "",
    souce_image: "/images/timg.jpg",
    move_x: '',
    move_y: '',
    scale: '',
    isview: true,
    cutImageHeight: '', //要剪切区域高度
    cutImageWidth: '', //要剪切区域宽度
  },

  refresh: function() {
    this.setData({
      isview: false,
      souce_image: "/images/timg.jpg",
    });
  },

  cutphoto1: function() {
    var _this = this;
    _this.setData({
      cutImageHeight: 320,
    });
    const ctx = wx.createCanvasContext('photo_canvas');
    ctx.scale(0.4,0.4);
    ctx.translate(-320, -320);
    ctx.drawImage(this.data.souce_image);
    ctx.draw();
    //下载canvas图片
    setTimeout(function () {
      wx.canvasToTempFilePath({
        canvasId: 'photo_canvas',
        success: function (res) {
          _this.data.shareTempFilePath = res.tempFilePath;
          _this.saveImageToPhotosAlbum();
          _this.setData({
            souce_image: res.tempFilePath,
          });
        },
        fail: function (error) {
          console.log(error)
        }
      })
    }, 1000)
  },

  cutphoto: function() {
    var that = this;
    that.setData({
      isview: true,
    });

    wx.createSelectorQuery().select('#player-image').boundingClientRect(function(rect) {
      if (original_imageHeight > original_imageWidth) {
        that.data.scale = original_imageHeight / rect.height;
      } else {
        that.data.scale = original_imageWidth / rect.width;
      }
      console.info("rect.height:" + rect.height + " that.data.viewHeight:" + that.data.viewHeight);
      if (rect.height < that.data.viewHeight) {
        that.data.cutImageHeight = rect.height;
      }
      if (rect.width < that.data.viewWidth) {
        that.data.cutImageWidth = rect.width;
      }
      that.data.move_x = rect.left;
      that.data.move_y = rect.top;
      console.info(rect);
    }).exec();

    // wx.chooseImage({
    //   sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
    //   sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
    //   success: function (res) {
    //     ctx.drawImage(res.tempFilePaths[0], 0, 0, 150, 100);
    //     ctx.draw();
    //   }
    // });



    // console.info("scale:" + this.data.scale);
    // ctx.translate(that.data.move_x, that.data.move_y);
    // console.info("move_x:" + this.data.move_x + "move_y:" + this.data.move_y);
    // ctx.drawImage(that.data.souce_image,0,0 -that.data.move_x, -that.data.move_y, that.data.scaleWidth, that.data.scaleWidth);
    setTimeout(function() {

      const ctx = wx.createCanvasContext('photo_canvas');

      var dx = 0;
      var dy = 0;
      var dWidth = that.data.cutImageWidth;
      var dHeight = that.data.cutImageHeight
      var sx = that.data.move_x;
      var sy = that.data.move_y;
      var sWidth = that.data.cutImageWidth;
      var sHeight = that.data.cutImageHeight;
      that.setData({
        cutImageWidth: dWidth,
        cutImageHeight: dHeight,
      });
      console.info("cutImageWidth:" + that.data.cutImageWidth + " cutImageHeight:" + that.data.cutImageHeight);
      ctx.translate(that.data.move_x, that.data.move_y);
      console.info("scale:" + 1 / that.data.scale);
      ctx.scale(1 / that.data.scale, 1 / that.data.scale);
      ctx.drawImage(that.data.souce_image);
      // ctx.scale(1 / that.data.scale, 1 / that.data.scale);
      // ctx.drawImage(that.data.souce_image, sx, sy, sWidth, sHeight, dx, dy, dWidth, dHeight);

      ctx.draw();
      //下载canvas图片
      setTimeout(function() {
        wx.canvasToTempFilePath({
          canvasId: 'photo_canvas',
          success: function(res) {
            that.data.shareTempFilePath = res.tempFilePath;
            that.saveImageToPhotosAlbum();
            that.setData({
              isview: false,
            });
          },
          fail: function(error) {
            console.log(error)
          }
        })
      }, 1000)
    }, 1000);
  },

  loadImage: function(e) {
    original_imageHeight = e.detail.height;
    original_imageWidth = e.detail.width;
  },

  onChange: function(e) {
    console.log("onChange:" + e.detail)
    // this.data.move_x = e.detail.x;
    // this.data.move_y = e.detail.y;
  },
  onScale: function(e) {
    console.log("onScale:" + e.detail)
    this.data.scale = e.detail.scale;
    // this.data.move_x = e.detail.x;
    // this.data.move_y = e.detail.y;
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    var res = wx.getSystemInfoSync(); //获取系统信息的同步方法，我用了异步里面提示我this.setData错了
    windowWidth = res.windowWidth;
    windowHeight = res.windowHeight;
    //那就给前面的图片进行赋值，高，宽以及路劲 
    this.setData({
      viewHeight: windowWidth,
      viewWidth: windowWidth,
      cutImageHeight: windowWidth,
      cutImageWidth: windowWidth,
    })
    
  },

  //保存至相册
  saveImageToPhotosAlbum: function() {
    if (!this.data.shareTempFilePath) {
      wx.showModal({
        title: '提示',
        content: '图片绘制中，请稍后重试',
        showCancel: false,
      })
    }
    wx.saveImageToPhotosAlbum({
      filePath: this.data.shareTempFilePath,
      success: (res) => {
        console.log(res)
      },
      fail: (err) => {
        console.log(err)
      }
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

  }
})