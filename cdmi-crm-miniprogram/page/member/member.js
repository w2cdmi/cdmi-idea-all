// page/member/member.js

var isFocusTOInput = true; //是否可以再次进入input获取焦点事件

Page({

  /**
   * 页面的初始数据
   */
  data: {
    scrollHeight: 0,
    inputVal:"",
    isSearch:false,
    showModalStatus: false,
    members: [{
        "id": "xsw3332242",
        "isConfirmation": true,
        "imageImage": '/images/me-management.png',
        "truename": "张三",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "isConfirmation": true,
        "imageImage": '/images/me-management.png',
        "truename": "里斯",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "isConfirmation": true,
        "imageImage": '/images/me-management.png',
        "truename": "王五",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "isConfirmation": true,
        "imageImage": '/images/me-management.png',
        "truename": "庄顺",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "isConfirmation": false,
        "imageImage": '/images/me-management.png',
        "truename": "张三",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "isConfirmation": false,
        "imageImage": '/images/me-management.png',
        "truename": "里斯",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "imageImage": '/images/me-management.png',
        "truename": "王五",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "imageImage": '/images/me-management.png',
        "truename": "庄顺",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "imageImage": '/images/me-management.png',
        "truename": "张三",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "imageImage": '/images/me-management.png',
        "truename": "里斯",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "imageImage": '/images/me-management.png',
        "truename": "王五",
        "mobile": "18767983243",
      },
      {
        "id": "xsw3332242",
        "imageImage": '/images/me-management.png',
        "truename": "庄顺",
        "mobile": "18767983243",
      },
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    //计算滚动区域高度
    var windowHeight = wx.getSystemInfoSync().windowHeight;
    var searchbarHeight = 0;
    var actionbarHeight = 0;
    let scrollHeight = 0;

    new Promise((resolve) => {
      wx.createSelectorQuery().select('#search-bar').boundingClientRect(function (rect) {
        searchbarHeight = rect.height;
        resolve();
      }).exec();
    }).then((res) => {
      new Promise((resolve) => {
        wx.createSelectorQuery().select('#action-bar').boundingClientRect(function (rect) {
          actionbarHeight = rect.height;
          resolve();
        }).exec();
      }).then(() => {
        scrollHeight = windowHeight - searchbarHeight - actionbarHeight;
        this.setData({
          scrollHeight: scrollHeight,
        });
      })
    });
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
   * 拨打电话
   */
  callme: function(e) {
    let mobile = e.target.dataset.value;
    if (mobile != 'undefined') {
      wx.makePhoneCall({
        phoneNumber: mobile,
      })
    }
  },

  setModalStatus: function(e) {
    var page = this;

    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation;
    animation.translateY(300).step();
    this.setData({
      animationData: animation.export(),
    })
    if (e.currentTarget.dataset.status == 1) {
      this.setData({
        showModalStatus: true
      });
    }
    setTimeout(function() {
      animation.translateY(0).step();
      this.setData({
        animationData: animation,
      })
      if (e.currentTarget.dataset.status == 0) {
        this.setData({
          showModalStatus: false,
        });
      }
    }.bind(this), 200)
  },

  //搜索框获取到焦点，显示搜索页面
  showSearchPage: function () {
    var data = this.data;

    if (isFocusTOInput) {
      isFocusTOInput = false;
      var beforeSearchInfo = new Object();
      beforeSearchInfo.isShowBlankPage = data.isShowBlankPage; //备份空白页面
      beforeSearchInfo.folders = this.data.folders;
      beforeSearchInfo.files = this.data.files;
      wx.setStorageSync("beforeSearchInfo", beforeSearchInfo);

      this.setData({
        isSearch: false, //不隐藏搜索框
        folders: [],
        files: [],
        isShowBlankPage: false,
        currentPageIcon: true,
        isPaging: false, //搜索不分页
        WXh: this.data.WXh + 41
      });
    }
  },

  /**
* 跳转到指定的应用管理员详情页
*/
  jumpToMemberDetail: function (e) {
    const dataset = e.currentTarget.dataset;
    wx.navigateTo({
      url: '/page/member/memberdetail?title=' + dataset.title + '&id=' + dataset.id + '&appid=' + this.data.appid + '&appname=' + this.data.appname,
    })
  },
})