var config = require("module/config.js");

var times = 0;  //一次登录等待的次数（计算时间，一次登录等待十秒）
var interval;   //等待登录成功计时器
var isLogining = false;    //是否已经在登录

/**
 * 判断token是否存在
 */
function tokenIsExist() {
    var app = getApp();

    //App还未生成，返回true 全局变量globalData还未生成
    if (app == undefined || app.globalData == undefined) {
        return false
    }

    var token = app.globalData.token;
    //Token不存在
    if (token == undefined || token == "") {
        return false;
    }

    return true;
}

//token是否有效
function isValid() {
    //已过有效期
    // var token = getApp().globalData.token;
    // var expire = getApp().globalData.expire;
    // if (token == undefined || token == '' || expire == undefined || expire == 0) {
    //     return false
    // }
    // var now = new Date().getTime();
    // if (expire > now) {
    //     return true;
    // } else {
    //     return false;
    // }
    return true;
}

//等待登录方法
function invokeAfterLogin(callback) {
    var times = 0;
    clearInterval(interval);
    interval = setInterval(function () {
        //等待登录成功执行回掉方法
        if (tokenIsExist() && isValid()) {
            if (typeof (callback) == 'function') {
                callback();
                clearInterval(interval);
            }
        }

        if (times == 80) {
            console.log("登录失败！！");
            clearInterval(interval);
        }
        times++;
    }, 100);
}

/**
 * 登录方法
 */
function login() {
    if (isLogining) {
        return;
    } else {
        isLogining = true;
    }

    if (tokenIsExist() && isValid()) {
        isLogining = false;
        return;
    }

    wx.showLoading({
        title: '登录中...',
        mask: true
    });

    login2Server();
}

/**
 * 登录服务器
 * userInfo: 从微信获取的用户信息
 */
function login2Server() {
    wx.login({
        success: function (res) {
            var code = res.code;
            if (code) {
                var data = {};
                wx.getUserInfo({
                    success: function (res) {
                        data = {
                            encryptedData: res.encryptedData,
                            iv: res.iv,
                            code: code
                        };
                        getApp().globalData.avatarUrl = res.userInfo.avatarUrl;

                        //向后台发送请求，由后台获取微信用户的ID，后台检查用户是否注册；
                        //如果没有注册，则给用户注册一个个人帐号。
                        wx.request({
                            url: config.host + '/wxuser/v1/login',
                            method: "POST",
                            data: data,
                            header: {
                                'content-type': 'application/json'
                            },
                            success: function (result) {
                                if (result.data.token != undefined) {
                                    getApp().globalData.token = result.data.token;
                                    getApp().globalData.isAdmin = result.data.admin;
                                    getApp().globalData.avatarUrl = result.data.headImageUrl;
                                    getApp().globalData.nickName = result.data.nick;
                                    getApp().globalData.userId = result.data.userId;
                                }
                                wx.hideLoading();
                                isLogining = false;
                            },
                            fail: function (res) {
                                wx.hideLoading();
                                isLogining = false;
                                clearInterval(interval);
                            }
                        })
                    },
                    fail: function () {
                        wx.hideLoading();
                        isLogining = false;
                        clearInterval(interval);
                        wx.navigateTo({
                            url: '/pages/authAndLogin/authAndLogin',
                        })
                    },
                    timeout: function () {
                        wx.hideLoading();
                        isLogining = false;
                        clearInterval(interval);
                        wx.showToast({
                            title: '获取用户信息，连接超时！',
                            icon: 'none'
                        })
                        wx.navigateTo({
                            url: '/pages/authAndLogin/authAndLogin',
                        })
                    }
                });
            } else {
                wx.showToast({
                    title: '获取code失败！',
                    icon: 'none'
                })
            }
        },
        fail: function (res) {
            wx.hideLoading();
            clearInterval(interval);
            isLogining = false;

            wx.showModal({
                title: '提示',
                content: '登录服务器失败，请稍后重试！',
                showCancel: false
            });
        },
        timeout: function () {
            wx.hideLoading();
            isLogining = false;
            clearInterval(interval);
            wx.showToast({
                title: '获取用户信息，连接超时！',
                icon: 'none'
            })
            wx.navigateTo({
                url: '/pages/authAndLogin/authAndLogin',
            })
        }
    });
}

//注意：在登录前设置，不初始化企业列表。
function initGlobalData() {
    var globalData = {
        token: '',
        isAdmin: false
    }

    getApp().globalData = globalData;
}

module.exports = {
    login,
    invokeAfterLogin,
    initGlobalData
};
