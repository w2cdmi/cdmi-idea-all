// page/developer/selectdeveloper.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
      developers:[],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
      var _this = this;
      wx.setNavigationBarTitle({
          title: "选择您使用的开发者身份",
      });
      //获取开发者列表
      const db = wx.cloud.database();
      db.collection('dev_members').where({
          accountId: getApp().globalData.adminId,
          accountType: "Admin",
      }).get().then(res => {
          Promise.all(res.data.map((item) => {
              return new Promise((resolve, reject) => {
                  db.collection('developers').doc(item.developerId).get().then(res =>{
                      if(res != null){
                          var developer = {};
                          developer.id = res.data._id;
                          developer.name = res.data.name;
                          developer.targetType = res.data.targetType;
                          resolve(developer);
                      }else{
                          resolve({});
                      }
                  })
              });
          })).then(result => {
              _this.setData({
                  developers: result,
              })
          });
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

    /** 
     * 选择当前要采用的开发者身份
     */
    selectDeveloper:function(e){
        var developer = e.currentTarget.dataset;
        getApp().globalData.developerId = developer.id;
        console.info(getApp().globalData.developerId);
        wx.redirectTo({
            url: '/page/system/system',
        });
    }
})