// page/buz/buzapp/newbuzapp.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        name: '',
        code: '',
        desc: '',
        linkApps: '',
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

    saveBuzApp: function(e) {
        var _this = this;
        var data = e.detail.value;
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '业务名称不能为空',
            });
            return;
        }
        if (data.code == null || data.code.trim() == '') {
            wx.showToast({
                title: '业务编码不能为空',
            });
            return;
        }
        const db = wx.cloud.database();
        //检查页面编码，同一个开发者下不能有相同的业务编码
        db.collection("buz_items").where({
            developerId: getApp().globalData.developerId,
            code: data.code.trim(),
        }).count().then(res => {
            if (res.total != 0) {
                wx.showToast({
                    title: '业务编码已存在',
                });
                return;  
            }
            //保存业务信息，同一个开发者下不能有同名的业务
            db.collection("buz_items").where({
                developerId: getApp().globalData.developerId,
                name: data.name.trim(),
            }).count().then(res => {
                if (res.total == 0) {
                    db.collection('buz_items').add({
                        data: {
                            name: data.name.trim(),
                            icon: data.icon,
                            code: data.code,
                            desc: data.desc,
                            parentId: data.parentId,
                            linkApps: data.linkApps,
                            developerId: getApp().globalData.developerId,
                            accountId: getApp().globalData.userId,
                            creatime: new Date(),
                        },
                    }).then(res => {
                        wx.navigateTo({
                            url: '/page/buz/buzapp/buzapp',
                        })
                    });
                } else {
                    wx.showToast({
                        title: '您已创建有同名的业务，请重新输入名称',
                    })
                }
            });
        });
    }
})