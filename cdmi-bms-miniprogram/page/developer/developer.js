// page/developer/developer.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        developers: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '开发者管理',
        });

        var _this = this;
        //获取当前开发者所拥有的一级渠道
        const db = wx.cloud.database();
        db.collection('developers').get().then(res => {
            if (res.data.length != 0) {
                var developers = [];
                res.data.forEach(function(item) {
                    var developer = {};
                    developer.id = item._id;
                    developer.name = item.name;
                    developer.ownertype = item.targetType;
                    developer.ownerId = item.targetId;
                    developer.managerId = item.accountId;
                    developers.push(developer);
                });
                _this.setData({
                    developers: developers,
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
     * 手动添加一个开发者信息
     */
    createDeveloper:function(){
        wx.navigateTo({
            url: 'newdeveloper?action=new',
        })
    },

    /**
     * 查看开发者详情
     */
    jumpToDeveloperDetail:function(e){
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'newdeveloper?action=edit&id=' + dataset.id + "&title=" + dataset.title,
        })
    }
})