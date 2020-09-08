// pages/myAccount/resetPassword/resetPassword.js

var Account = require("../../../model/account.js");
const Utils = require('../../../common/utils.js');
var Config = require('../../../common/config.js');
var timer;

Page({

    data: {
        checked: false,
        code: '',
        phone: '',
        password: '',
        countdown: 0,
  },

    getInputData: function(e) {
        var value = e.detail.value;
        var name = e.currentTarget.dataset.name;
        this.setData({
            [name]: value
        });
    },

    register: function() {
        var pageData = this.data;
        var code = pageData.code;
        var password = pageData.password;
        var phone = pageData.phone;

        if (!testBlank({
                phone,
                code,
                password
            })) {
            return;
        }

        var now = new Date().getTime();
        var uid = "@reset_password";
        var token = Utils.SHA1(Config.xlingual_token + "WoW" + uid + "WoW" + now);

        var data = {
            phone: phone.toString(),
            code: code,
            password: password,
            token: token,
            ts: now,
            uid: uid,
            zone: "86",
        }

        this.setData({
            countdown: 0,
        }, () => {
            clearTimeout(timer)
        })

        Account.resetPassword(data)
        .then(() => {
            wx.showToast({
                title: '修改成功，请登录',
            });
            setTimeout(() => {
                wx.navigateBack({
                    delta: 1
                });
            }, 500);
        });
    },

    getCode: function() {
        var phone = this.data.phone;
        if (!(/^1[3|4|5|8|7|9][0-9]\d{8}$/.test(phone))) {
            wx.showToast({
                title: '号码不正确',
            });
            return;
        }
        var data = {
            "phone": phone.toString(),
            'zone': '86'
        }

        var page = this;
        this.setData({
            countdown: 60,
        }, () => {
            countdown(page, 60);
        })

        Account.registerCode(data)
        .then((res) => {
            if (res.code === 477) {
                wx.showModal({
                    title: '提示',
                    content: '您今日已获取了10条短信，请明日再试',
                });
            }
        });
    }
})

function countdown(page, num) {
    page.setData({
        countdown: --num
    })
    timer = setTimeout(() => {
        num == 0 ? clearTimeout(timer) : countdown(page, num);
    }, 1000);
}

function showToast(title, icon = 'none') {
    wx.showToast({
        title: title,
        icon: icon
    });
    return false;
}

function testBlank({
    phone,
    code,
    password
}) {
    var testData = [{
            name: phone,
            text: '号码不能为空'
        },
        {
            name: code,
            text: '验证码不能为空'
        },
        {
            name: password,
            text: '密码不能为空'
        }
    ]
    for (var i = 0; i < testData.length; i++) {
        var item = testData[i];
        if (item.name == '') {
            return showToast(item.text);
        }
    }
    return true;
}