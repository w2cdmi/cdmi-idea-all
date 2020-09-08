// page/system/system.js
var session = require("../../commjs/session.js");
var config = require("../../commjs/config.js");

Page({

    /**
     * 页面的初始数据
     */
    data: {
        isShowMenu: false,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '系统管理',
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
        var _this = this;
        //2. 登陆平台
        session.login();
        //3. 检查对应的开发者信息，如果开发者信息不存在，则跳转到开发者信息页面进行注册。
        //原因：只有拥有开发者账号的用户才能创建应用以及分销商等
        session.invokeAfterLogin(function() {
            //判断是否获得了开发者信息，如果没有要求用户创建开发者信息或选择开发者进行登陆
            if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
                const adminId = getApp().globalData.adminId;
                const db = wx.cloud.database();
                //这里如果有多个管理员。那么这里会要求注册多个开发者
                db.collection('dev_members').where({
                    accountId: adminId,
                    accountType: "Admin",
                }).get().then(res => {
                    if (res.data.length == 0) {
                        wx.redirectTo({
                            url: '/page/developer/newdeveloper',
                        });
                    } else if (res.data.length == 1) {
                        getApp().globalData.developerId = res.data[0].developerId;
                    } else {
                        //如果存在多个，请用户进行选择
                        wx.redirectTo({
                            url: '/page/developer/selectdeveloper',
                        });
                    }
                });
            }
        });
    },

    /**
     * 继承操作菜单的方法
     */
    onShowMenu: function() {
        this.setData({
            isShowMenu: "true"
        })
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
     * 调转到部署环境页面
     */
    jumpToEnv: function() {
        wx.navigateTo({
            url: '/page/env/env',
        })
    },

    /**
     * 调转到平台微服务页面
     */
    jumpToMicroService: function() {
        wx.navigateTo({
            url: '/page/microservice/microservice',
        })
    },

    /**
     * 调转到第三方服务配置页面
     */
    jumpToThirdService: function() {
        wx.navigateTo({
            url: '/page/thirdservice/thirdservice',
        })
    },

    /**
     * 调转到平台管理员页面
     */
    jumpToBMSManager: function() {
        wx.navigateTo({
            url: '/page/manager/manager',
        })
    },

    /**
     * 调转到接入应用管理页面
     */
    jumpToAppMgr: function() {
        wx.navigateTo({
            url: '/page/appmgr/appmgr',
        })
    },

    /**
     * 调转到微信小程序管理页面
     */
    jumpToMiniprogram: function() {
        wx.navigateTo({
            url: '/page/miniprogram/miniprogram',
        })
    },

    jumpToDeveloper: function() {
        wx.navigateTo({
            url: '/page/developer/developer',
        })
    }
})