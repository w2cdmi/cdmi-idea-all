// page/agreement/agreementResult.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        status: 'OK',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        //   if (options.status == null || options.status == ''){
        //       wx.showToast({
        //           title: '未能获取协议签约状态',
        //       });
        //       return;
        //   }else{
        this.setData({
            status: options.status,
        });
        //   }
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
     * 跳转到分销商业务管理小程序
     */
    navigateToMiniProgram: function() {
        wx.navigateToMiniProgram({
            appId: 'wx44f75df7c3db4fb2',//转到企微CRM系统
            path: 'pages/index/index?id=123',
            extraData: {
                foo: 'bar'
            },
            envVersion: 'develop',
            success(res) {
                // 打开成功
            }
        })
    },

    /**
     * 关闭当前小程序
     */
    closeMiniProgram: function() {
        var pages = getCurrentPages();
        wx.navigateBack({
            delta: pages.length,
        })
    }
})