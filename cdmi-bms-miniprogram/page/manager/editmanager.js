// page/manager/editmanager.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        manager_id:'',
        truename: '',
        phoneNumber:'',
        view_invite:false,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '平台管理员';
        if (options != null && options.title != null) {
            title = options.title;
        };
        wx.setNavigationBarTitle({
            title: options.action + title,
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
     * 完成对名称输入
     */
    confirmName: function(e) {
        const value = e.detail.value;
        if (value != null && value.trim != null) {
            this.data.truename = value;
        }
        if (this.data.truename != '' && this.data.phoneNumber != '') {
            this.saveInviteManager();
        }
    },

    /**
     * 完成对电话号码的输入
     */
    confirmPhoneNumber: function (e) {
        const value = e.detail.value;
        if (value != null && value.trim != null) {
            this.data.phoneNumber = value;
        }
        if (this.data.truename != '' && this.data.phoneNumber != '') {
            this.saveInviteManager();
        }
    },
    /**
     * 
     */
    hiddenButton: function(){
        if (this.data.truename == "" || this.data.phoneNumber == "") {
            this.setData({
                view_invite: false,
            })
        }
    },

    //保存待邀请的新管理员信息
    saveInviteManager: function() {
        var _this = this;
        const db = wx.cloud.database();
        console.info("af:" + _this.data.manager_id);
        if (_this.data.manager_id == ''){
            db.collection('invite_admins').add({
                data: {
                    truename: _this.data.truename,
                    phoneNumber: _this.data.phoneNumber,
                    creatime:new Date(),
                },
                success: function (res) {
                    _this.setData({
                        view_invite: true,
                        manager_id: res._id,
                    })
                },
                fail: function (res) {
                    console.error(res);
                }
            })
        }else{
            db.collection('invite_admins').doc(_this.data.manager_id).update({
                // data 传入需要局部更新的数据
                data: {
                    truename: _this.data.truename,
                    phoneNumber: _this.data.phoneNumber,
                },
                success: function (res) {
                    _this.setData({
                        view_invite: true,
                    })
                },
                fail: function (res) {
                    console.error(res);
                }
            })  
        }
    },
})