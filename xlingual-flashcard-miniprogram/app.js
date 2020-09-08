const Utils = require('./common/utils.js');
const Account = require('./model/account.js');
const Session = require('./common/session.js');
var JmapiAccount = require("/model/jmapiAccount.js");

App({
    onLaunch: function() {
        var now = new Date().getTime();
        var uid = wx.getStorageSync("loginInfo").userId;
        var loginStatus = wx.getStorageSync("loginStatus");
        if (uid != null || uid != undefined) {
            this.globalData.userId = uid;
        }
        this.globalData.loginStatus = loginStatus;
    },
    globalData: {
        userInfo: null,
        limit: 10, // 每次获取数据条数
        loginStatus: false,
        userId: '',
        rcode: ''   // 机构码
    }
})