// console/school/trainplace.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
      branches: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var _this = this;
      wx.setNavigationBarTitle({
          title: "网点列表",
      });
      //判断，必须进入点击应用后台管理才能进行操作。
      if (getApp().globalData.consoleAppId == null || getApp().globalData.consoleAppId == '') {
          wx.showToast({
              title: '未获得应用信息，请联系开发商',
          });
          wx.redirectTo({
              url: '/page/error?content=未获得应用信息，请联系开发商',
          })
      }

      //检查当前用户是否为该应用的管理员，不是，则拒绝操作
      var _this = this;
      const db = wx.cloud.database();
      db.collection("app_admins").where({
          accountId: getApp().globalData.userId,
          accountType: 'SYS_ACCOUNT',
          appId: getApp().globalData.consoleAppId,
      }).get().then(res => {
          if (res.data.length == 0) {
              wx.redirectTo({
                  url: '/page/error?content=您不是该应用的管理员，请勿操作',
              })
          }
      });

      console.log("获取培训机构列表");

      db.collection('a_branches').where({
          appId: getApp().globalData.consoleAppId,
      }).get().then(res => {
          if (res.data.length != 0) {
              var branches = [];
              res.data.forEach((item) => {
                  var branch = {};
                  branch.id = item._id;
                  branch.name = item.name;
                  //转换状态
                  if (item.status == 'OK') {
                      expert.status = '已签约';
                  } else if (item.status == 'ToBeSigned') {
                      expert.status = '待签约';
                  } else {
                      expert.status = '未知'; //TODO 未来还有其他类型
                  }
                  branch.address = item.address;
                  console.info(branch);
                  branches.push(branch);
              });
              _this.setData({
                  branches: branches,
              });
          }
      });
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

  
})