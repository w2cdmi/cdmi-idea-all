// miniprogram/pages/index.js

Page({
    /**
     * 页面的初始数据
     */
    data: {
        cover: "cover",
        userLikeVideo: true,
        publisher: {
            nickname: '天天向上',
        },
        videoInfo: {
            videoDesc: '天天都这么开心就好了.',
        }
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
        this.videoContext = wx.createVideoContext('myVideo');
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

    onTabItem: function(e) {

    },

    playover: function(e) {
        console.info(e);
        console.info("播放结束,继续播放下一个");
    },

    pause: function(e) {
        console.info("执行暂停");
        console.info(this.videoContext);
        this.videoContext.pause();
    },
    likeVideoOrNot: function() {
        console.info("执行暂停1");
        console.info(this.videoContext);
        this.videoContext.pause();
    },

    /**
     * 上传视频
     */
    upload: function() {

    },

    /**
     * 调转到列表首页
     */
    navToIndex: function() {
        wx.navigateTo({
            url: 'video/index',
        })
    },
    /**
     * 调转到我的页面
     */
    navToMine: function() {
        wx.navigateTo({
            url: 'mine/index',
        })
    },
})