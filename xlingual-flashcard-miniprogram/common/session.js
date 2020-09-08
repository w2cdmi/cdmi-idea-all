var config = require("./config.js");
var httpClient = require("./httpclient.js");

var times = 0; //一次登录等待的次数（计算时间，一次登录等待十秒）
var interval; //等待登录成功计时器
var isLogining = false; //是否已经在登录

/**
 * 检查Session是否过期，过期后重新生成。
 */
function invokeAfterLogin(callback) {
    //等待登录成功回调用方法
    waitAndInvoke(callback);
}

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
    var token = getApp().globalData.token;
    var expire = getApp().globalData.expire;
    if (token == undefined || token == '' || expire == undefined || expire == 0) {
        return false
    }
    var now = new Date().getTime();
    if (expire > now) {
        return true;
    } else {
        return false;
    }
}

//等待登录方法
function waitAndInvoke(callback) {
    var times = 0;
    clearInterval(interval);
    interval = setInterval(function() {
        //等待登录成功执行回掉方法
        if (tokenIsExist() && isValid()) {
            if (typeof(callback) == 'function') {
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
 * __params： 服务器登录信息，enterpriseId等信息
 */
function login2Server() {
    wx.login({
        success: function(res) {
            var code = res.code;
            if (code) {
                var data = {};
                wx.getUserInfo({
                    success: function(res) {
                        getApp().globalData.avatarUrl = res.userInfo.avatarUrl;
                        getApp().globalData.userName = res.userInfo.nickName;

                        data = {
                            iv: res.iv,
                            encryptedData: res.encryptedData,
                            code: code,
                            rawData: res.rawData
                        }

                        httpClient.post({
                            url: config.jmapiHost + '/xlingual/wxuser/v1/auth',
                            data: data
                        }).then((res) => {
                            getApp().globalData.isAuthorized = true;
                        }, () => {
                            getApp().globalData.isAuthorized = false;
                        });
                    },
                    fail: function() {
                        wx.hideLoading();
                        isLogining = false;
                        clearInterval(interval);
                        getApp().globalData.isAuthorized = false;
                        wx.navigateTo({
                            url: '/pages/authAndLogin/authAndLogin',
                        });
                    },
                    timeout: function() {
                        wx.hideLoading();
                        isLogining = false;
                        clearInterval(interval);
                        wx.showToast({
                            title: '获取用户信息，连接超时！',
                            icon: 'none'
                        })
                        getApp().globalData.isAuthorized = false;
                    }
                });
            } else {
                getApp().globalData.isAuthorized = false;
                wx.showToast({
                    title: '获取code失败！',
                    icon: 'none'
                })
            }
        },
        fail: function(res) {
            wx.hideLoading();
            clearInterval(interval);
            isLogining = false;
            getApp().globalData.isAuthorized = false;

            wx.showModal({
                title: '提示',
                content: '登录服务器失败，请稍后重试！',
                showCancel: false
            });
        },
        timeout: function() {
            wx.hideLoading();
            isLogining = false;
            clearInterval(interval);
            wx.showToast({
                title: '获取用户信息，连接超时！',
                icon: 'none'
            })

            getApp().globalData.isAuthorized = false;
        }
    });
}

//注意：在登录前设置，不初始化企业列表。
function initGlobalData() {
    var globalData = {
        token: '', //
        userId: '', //当前登录用户ID
        userName: '', //用户名字
        expire: 0, //会话到期时间
        avatarUrl: '', //微信头像
        isAuthorized: false,
    }

    getApp().globalData = globalData;
}

function getUserInfo(userInfo) {
    return new Promise((resolve, reject) => {
        wx.login({
            success: function(res) {
                var code = res.code;
                if (typeof code !== undefined) {
                    wx.getUserInfo({
                        success: function(res) {
                            getApp().globalData.avatarUrl = res.userInfo.avatarUrl;
                            getApp().globalData.userName = res.userInfo.nickName;
                            
                            var data = {
                                iv: res.iv,
                                encryptedData: res.encryptedData,
                                code: code,
                                rawData: res.rawData
                            }

                            resolve(data);
                        }
                    });
                }
            },
            fail: function(err) {
                reject(err);
            }
        })
    });
}

// 检查用户登录状态，如果未登录就跳转到/pages/myAccount/myAccount页面
function isLogin(url) {
    var loginStatus = wx.getStorageSync("loginStatus");
    if (!loginStatus) {
        wx.showModal({
            title: '提示',
            content: '未登录，请登录后再使用',
            success: function() {
                wx.switchTab({
                    // url: url,
                    url: '/pages/myAccount/myAccount',
                });
            }
        });
        return;
    }
}

module.exports = {
    login,
    invokeAfterLogin,
    initGlobalData,
    getUserInfo,
    isLogin
};