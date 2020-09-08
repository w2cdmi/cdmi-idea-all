var Account = require("../../../model/account.js");
const Utils = require('../../../common/utils.js');
var Config = require('../../../common/config.js');
var timer;

Page({

    data: {
        checked: true,
        code: '',
        password: '',
        phone: '',
        rcode: '',
        countdown: 0,
    },

    onReady: function() {
        // var rcode = getApp().globalData.rcode;
        // var data = getApp().globalData;
        wx.getStorage({
            key: 'rcode',
            success: (res) => {
                var rcode = res.data;
                this.setData({
                    rcode: rcode,
                });
            },
        })
        // if (typeof rcode !== 'undefined') {
        //     this.setData({
        //         rcode: rcode,
        //     });
        // }
    },

    getInputData: function(e) {
        var value = e.detail.value;
        var name = e.currentTarget.dataset.name;
        this.setData({
            [name]: value
        });
    },

    checkboxChange: function(e) {
        this.setData({
            checked: this.data.checked ? false : true,
        });
    },

    register: function() {
        var now = new Date().getTime();
        var token = Utils.SHA1(Config.xlingual_token + "WoW@create_userWoW" + now);
        var pageData = this.data;

        var code = pageData.code;
        var password = pageData.password;
        var phone = pageData.phone;
        var rcode = pageData.rcode;
        var checked = pageData.checked;

        if (!testBlank({
                phone,
                code,
                password,
                rcode
            })) {
            return;
        }

        if (!checked) {
            return showToast('请同意《用户使用协议》');
        }

        this.setData({
            countdown: 0,
        }, () => {
            clearTimeout(timer)
        })

        var data = {
            code: code,
            password: password,
            phone: phone,
            rcode: rcode,
            uid: "@create_user",
            zone: "86",
        }

        Account.register(data)
            .then((res) => {
                showToast(res, 'success');
                wx.navigateBack({
                    delta: 2,
                });
            }, (res) => {
                if (res.data.error && res.data.error.name === 'uniqueness') {
                    var text = '电话号码已注册';
                    showToast(text);
                    return;
                }
                if (res.data) {
                    showToast(res.data);
                    return;
                }
            });
    },

    getCode: function() {
        var phoneNumber = this.data.phone;
        if (!(/^1[3|4|5|8|7|9][0-9]\d{8}$/.test(phoneNumber))) {
            return showToast('号码不正确');
        }
        var getInviteCodeData = {
            "phone": phoneNumber.toString(),
            "zone": "86"
        }
        var page = this;

        this.setData({
            countdown: 60,
        }, () => {
            countdown(page, 60);
        })

        Account.registerCode(getInviteCodeData)
            .then((res) => {
                if (res.code === 477) {
                    wx.showModal({
                        title: '提示',
                        content: '您今日已获取了10条短信，请明日再试',
                    });
                }
            });
    }
});


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
    password,
    rcode
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