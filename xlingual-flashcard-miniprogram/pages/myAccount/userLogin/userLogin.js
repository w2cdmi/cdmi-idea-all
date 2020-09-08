var Session = require("../../../common/session.js");

var JmapiAccount = require("../../../model/jmapiAccount.js");

var Utils = require("../../../common/utils.js");
var appGlobalData = getApp().globalData;
Page({

    /**
     * 页面的初始数据
     */
    data: {
        phoneNumber: "",
        password: ""
    },

    getInputData: function(e) {
        var value = e.detail.value;
        var name = e.currentTarget.dataset.name;
        this.setData({
            [name]: value
        });
    },

    gotoRegister: function() {
        wx.navigateTo({
            url: '/pages/myAccount/register/register',
        })
    },

    gotoResetPassword: function() {
        wx.navigateTo({
            url: '/pages/myAccount/resetPassword/resetPassword',
        })
    },

    /**
     * 用户登录
     */
    userLogin: function(e) {
        var page = this;
        var password = page.data.password;
        var phone = page.data.phoneNumber;

       if (!testBlank({
            phone,
            password,
        })) {
            return;
        }

        var detail = {
            encryptedData: e.detail.encryptedData,
            iv: e.detail.iv,
            rawData: e.detail.rawData,
        }

        Session.getUserInfo(detail)
            .then((data) => {
                data.password = password;
                data.phoneNumber = phone;
                userLoginInfo(page, data);
            })
    }
})


function showToast(title, icon = 'none') {
    wx.showToast({
        title: title,
        icon: icon
    });
    return false;
}


var userLoginInfo = (page, data) => {
    JmapiAccount.userLogin(data)
        .then((res) => {
            wx.setStorageSync("loginInfo", res);
            appGlobalData.loginStatus = true;
            wx.setStorageSync("loginStatus", true);
            var robotTranslateLang = wx.getStorageSync("robotTranslateLang")
            if (robotTranslateLang == "" || robotTranslateLang == undefined) {
                wx.setStorageSync("robotTranslateLang", "Baidu")
            } else {
                wx.setStorageSync("robotTranslateLang", robotTranslateLang)
            }

            wx.showToast({
                title: '登陆成功',
                duration: 1500,
                success: setTimeout(function() {
                    wx.navigateBack({
                        delta: 1
                    })
                }, 1500)
            });
        }, (error) => {
            wx.showToast({
                title: '登录失败，用户名或密码错误',
                icon: 'none',
            })
        });
}


function testBlank({
    phone,
    // code,
    password,
    // rcode
}) {
    var testData = [{
        name: phone,
        text: '号码不能为空'
    },
    // {
    //     name: code,
    //     text: '验证码不能为空'
    // },
    {
        name: password,
        text: '密码不能为空'
    },
        // {
        //     name: rcode,
        //     text: '机构邀请码不能为空'
        // }
    ]
    for (var i = 0; i < testData.length; i++) {
        var item = testData[i];
        if (item.name == '') {
            return showToast(item.text);
        }
    }
    return true;
}