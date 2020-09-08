// console/league/channeloptions.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        marker:'',
        channels:[],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var marker = 'PDLX';
        if (options.marker != null && options.marker != '') {
            this.data.marker = options.marker;
        }

        var _this = this;
        const db = wx.cloud.database();
        console.log("获取应用的类目频道列表");
        db.collection('a_channels').where({
            // appId: getApp().globalData.consoleAppId,
            // marker: _this.data.marker,
            appId:'W-QNGXhEiJmgalzU',
            marker: marker,
        }).get().then(res => {
            var channels = [];
            res.data.forEach(function (item) {
                var channel = {};
                channel.id = item._id;
                channel.name = item.name;
                channel.icon = item.icon;
                channels.push(channel);
            })
            _this.setData({
                channels: channels,
            })
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

    checkboxChange:function(e){
        console.info(e);
        var options = e.detail.value;
        options.forEach(function(item){
            console.info(item.id);   
            console.info(item.name);
        });
    }
})