// page/microservice/microservice.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        microservices: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '微服务管理',
        });

        console.log("获取微服务列表");
        var _this = this;
        const db = wx.cloud.database();

        db.collection('microservices').get().then(res => {
            if (res.data.length != 0) {
                var microservices = [];
                res.data.forEach((item) => {
                    var microservice = {};
                    microservice.id = item._id;
                    microservice.name = item.name;
                    microservice.icon = item.icon;
                    microservices.push(microservice);
                });
                _this.setData({
                    microservices: microservices,
                });
            }
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
     * 跳转到创建一个新的微服务页
     */
    createMicroservice: function(e) {
        wx.navigateTo({
            url: '/page/microservice/editmicroservice?action=new',
        })
    },

    /**
     * 跳转到微服务编辑详情页
     */
    jumpToMicroservice: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/microservice/editmicroservice?action=edit&id=' + dataset.id,
        })
    }
})