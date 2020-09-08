// page/appmgr/appmanager.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        appid: '',
        appname: '',
        admins: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: options.title + "-应用管理员",
        });
        console.log("appid:" + options.id);
        console.log("appname:" + options.title);
        _this.setData({
            appid: options.id,
            appname: options.title,
        });

        if(this.data.appid == null || _this.data.appname == null){
            wx.showToast({
                title: '页面访问存在参数错误，请联系开发商',
            })
            return;
        }
        //获取当前应用管理员
        const db = wx.cloud.database();
        db.collection('app_admins').where({
            appId: _this.data.appid,
        }).get().then(res => {
            var admins = [];
            if (res.data.length != 0){
                res.data.forEach(function(item) {
                    var admin = {};
                    admin.id = item._id;
                    admin.name = item.name;
                    admin.imageHead = item.imageHead;
                    admin.phoneNumber = item.phoneNumber;
                    admins.push(admin);
                })
                _this.setData({
                    admins: admins,
                })
            } else{
                //当前尚不存在记录
                wx.showModal({
                    title: '添加应用管理员',
                    content: '该应用尚未添加有管理员，该应用将无法被管理。是否将自己作为[' + _this.data.appname + ']的管理员',
                    showCancel: true,
                    success: function (res) {
                        if (res.confirm) {
                            const db = wx.cloud.database();
                            db.collection('accounts').doc(getApp().globalData.userId).get().then(res => {
                                var user = res.data;
                                if (user != null) {
                                    console.info(user);
                                    db.collection('app_admins').add({
                                        data: {
                                            name: user.name,
                                            imageHead: user.headImage,
                                            phoneNumber: user.phoneNumber,
                                            accountId: user._id,
                                            accountType:'SYS_ACCOUNT',
                                            appId: _this.data.appid,
                                            creatime: new Date(),
                                        }
                                    }).then(res => {
                                        var admin = {};
                                        var admins = [];
                                        admin.id = res._id;
                                        admin.name = user.name;
                                        admin.imageHead = user.headImage,
                                            admin.phoneNumber = user.phoneNumber,
                                            admins.push(admin);
                                        console.info(admin);
                                        _this.setData({
                                            admins: admins,
                                        })
                                    });
                                } else {
                                    wx.showToast({
                                        title: '您没有权限进行操作！',
                                    })
                                }
                            }).catch(ex =>{
                                console.error(ex);
                            });
                        }
                    }
                })
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

    },

    /**
     * 跳转到指定的应用管理员详情页
     */
    jumpToManagerDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: '/page/appmgr/appmanagerdetail?title=' + dataset.title + '&id=' + dataset.id + '&appid=' + this.data.appid + '&appname=' + this.data.appname,
        })
    },

    /**
     * 新增应用管理员
     */
    createAppManager: function() {
        console.log();
        wx.navigateTo({
            url: '/page/appmgr/editappmanager?action=新增&appid=' + this.data.appid + '&appname=' + this.data.appname,
        })
    }
})