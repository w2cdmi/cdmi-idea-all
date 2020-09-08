var config = require("config.js");
var httpClient = require("httpclient.js");

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
function isAuthorized() {
    var app = getApp();

    //App还未生成，返回true 全局变量globalData还未生成
    if (app == undefined || app.globalData == undefined) {
        return false
    }

    var isAuthorized = app.globalData.isAuthorized;
    //isAuthorized
    if (isAuthorized == true) {
        return true;
    }

    return false;
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
        // if (tokenIsExist() && isValid()) {
        if (isAuthorized()) {
            clearInterval(interval);
            if (typeof(callback) == 'function') {
                callback();
                wx.hideLoading();
            }
        }

        if (times == 80) {
            wx.hideLoading();
            wx.showToast({
                title: '登陆失败，无法链接服务器！',
            })
            clearInterval(interval);
        }
        times++;
    }, 100);
}

//通用方法,等待条件达到后执行
function waitAndInvoke1(callback, condition) {
    var times = 0;
    var interval = setInterval(function() {
        //等待登录成功执行回掉方法
        // if (tokenIsExist() && isValid()) {
        console.info("执行条件：" + condition);
        if (condition != null || condition) {
            clearInterval(interval);
            if (typeof(callback) == 'function') {
                callback();
            }
        }
        if (times == 80) {
            console.info("执行次数：" + times);
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

    if (isAuthorized() && isValid()) {
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
                        getApp().globalData.headImage = res.userInfo.avatarUrl;
                        getApp().globalData.userName = res.userInfo.nickName;
                        data = {
                            iv: res.iv,
                            encryptedData: res.encryptedData,
                            code: code,
                            rawData: res.rawData
                        }
                        cloud_login();
                        // httpClient.post({
                        //   url: config.HOST + config.HTTP_LOGIN,
                        //   data: data
                        // }).then((res) => {
                        //   console.info(res);
                        //   getApp().globalData.token = res.token;
                        //   getApp().globalData.userId = res.userId;
                        //   getApp().globalData.userName = res.nick;
                        //   getApp().globalData.avatarUrl = res.headImageUrl;
                        //   getApp().globalData.isAuthorized = true;
                        // }, (result) => {
                        //   getApp().globalData.isAuthorized = false;
                        // });
                    },
                    fail: function() {
                        wx.hideLoading();
                        isLogining = false;
                        clearInterval(interval);
                        getApp().globalData.isAuthorized = false;
                        wx.navigateTo({
                            url: '/page/authAndLogin',
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

// //注意：在登录前设置，不初始化企业列表。
// function initGlobalData() {
//   var globalData = {
//     token: '', //
//     userId: '', //当前登录用户ID
//     userName: '', //用户名字
//     expire: 0, //会话到期时间
//     avatarUrl: '', //微信头像
//     isAuthorized: false,
//   }

//   getApp().globalData = globalData;
// }

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

function cloud_login() {
    console.log("执行用户登陆");
    console.log(getApp().globalData);
    //逻辑如下：
    //1. 检查是否存在管理员账号，存在则判断当前用户是否为管理员
    //1.1 不存在，则将当前用户添加为管理员
    //1.2 添加管理员的同时需要先添加微信用户表和系统账号表
    //1.2.1 通过用户的微信信息判断该用户是否已存在。已存在，则获取系统账号ID
    //1.2.2 如果不存在，则创建微信用户信息，同时创建系统账号
    const db = wx.cloud.database();
    db.collection('admins').count().then(res => {
        if (res.total == 0) {
            var accountId = null;
            new Promise((resolve, reject) => {
                //如果当前无管理员，将当前用户设置为管理员,但需先获取accountId。
                //先从后台获取用户的openid
                wx.cloud.callFunction({
                    name: 'getUserInfo',
                }).then(res => {
                    //查找当前用户的openId是否在wxusers中存在，不存在，则增加一个微信用户
                    getApp().globalData.openId = res.result.OPENID;
                    db.collection('wxusers').where({
                        _openid: res.result.OPENID,
                    }).count().then(res => {
                        if (res.total == 0) {
                            //如果wxusers不存在，先创建系统账号
                            db.collection('accounts').add({
                                // 系统账号不存在，则添加一个系统账号
                                data: {
                                    name: getApp().globalData.userName,
                                    headImage: getApp().globalData.headImage,
                                    creatime: new Date(),
                                    updateime: new Date(),
                                    status: "enable",
                                    platform: "WEIXIN", //账号从哪里进行注册的
                                }
                            }).then(res => {
                                //再创建wxuser账号
                                accountId = res._id;
                                resolve(accountId);
                                db.collection('wxusers').add({
                                    data: {
                                        accountId: res._id,
                                        accountType:'SYS_ACCOUNT',
                                        name: getApp().globalData.userName,
                                        headImage: getApp().globalData.headImage,
                                        creatime: new Date(),
                                    }
                                })
                            })
                        } else {
                            db.collection('wxusers').where({
                                _openid: res.openId
                            }).get().then(res => {
                                accountId = res.data[0].accountId;
                                resolve(accountId);
                            })
                        }
                    })
                });
            }).then(accountId => {
                //等在上面执行完成，获取accountId后执行
                //创建一个admins账号
                db.collection('admins').add({
                    data: {
                        accountId: accountId,
                        name: getApp().globalData.userName,
                        headImage: getApp().globalData.headImage,
                        description: "系统管理员",
                        creatime: new Date(),
                        status: 'enable'
                    },
                    success: function(res) {
                        // res 是一个对象，其中有 _id 字段标记刚创建的记录的 id
                        getApp().globalData.userId = accountId;
                        getApp().globalData.adminId = res.data._id;
                        getApp().globalData.isAuthorized = true;
                    },
                    fail: function(res) {
                        getApp().globalData.isAuthorized = false;
                    }
                })
            })
        } else {
            //从后台获取用户的openid
            wx.cloud.callFunction({
                name: 'getUserInfo',
            }).then(res => {
                //查找当前用户的openId是否在admins中存在，不存在，则不允许登陆
                getApp().globalData.openId = res.result.OPENID;
                db.collection('admins').where({
                    _openid: res.result.OPENID
                }).get().then(res => {
                    if (res.data.length == 1) {
                        getApp().globalData.userId = res.data[0].accountId;
                        getApp().globalData.adminId = res.data[0]._id;
                        getApp().globalData.isAuthorized = true;
                    } else {
                        getApp().globalData.isAuthorized = false;
                    }
                })
            })
        }
    }).catch(ex =>{
        console.error(ex);
    });
}

module.exports = {
    login,
    invokeAfterLogin,
    // initGlobalData,
    getUserInfo,
    isLogin
};