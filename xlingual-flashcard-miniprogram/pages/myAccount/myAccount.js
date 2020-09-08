var Account = require("../../model/account.js");
var Config = require('../../common/config.js');

var lang = require("../../common/language.js");
var Utils = require("../../common/utils.js")
var appGlobalData = getApp().globalData;
var firstLang = ""
var destLang = []
Page({

    /**
     * 页面的初始数据
     */
    data: {
        accountAllInfo: {}, //获取账户所有信息
        motherLang: {},
        learnLang: "",
        // loginStatus: appGlobalData.loginStatus,
        loginStatus: wx.getStorageSync("loginStatus"),
        robotTranslateLang: "",
        balance: "0.00"
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function() {
        var page = this;
        var robotTranslateLang = wx.getStorageSync("robotTranslateLang")
        page.setData({
            // loginStatus: appGlobalData.loginStatus,
            loginStatus: wx.getStorageSync("loginStatus"),
            robotTranslateLang: robotTranslateLang
        })
        if (wx.getStorageSync("loginStatus")) {
            userInfo({
                page: this
            });
            accountInfo({
                page: this
            })
        }

    },

    /**
     * 用户登录
     */
    pleaseLogin: function() {
        wx.navigateTo({
            url: '/pages/myAccount/userLogin/userLogin',
        })
    },

    /**
     * 点击个人信息
     */
    setPersonInfo: function(e) {
        var personInfo = e.currentTarget.dataset.item
        wx.navigateTo({
            url: '/pages/myAccount/personInfo/personInfo?head=' + personInfo.avatar + '&nickName=' + personInfo.nickname + '&phoneNumber=' + personInfo.mobile,
        })
    },
    /**
     * 点击账户充值
     */
    gotoAccountRecharge: function(e) {
        var balance = e.currentTarget.dataset.item
        wx.navigateTo({
            url: '/pages/myAccount/accountRecharge/accountRecharge?balance=' + balance,
        })
    },

    /**
     * 点击购买课程
     */
    goToBuyCourse: function(e) {
        wx.navigateTo({
            url: '/pages/myAccount/buyCourse/buyCourse',
        })
    },
    /**
     * 点击我的课程
     */
    goToMyCourse: function(e) {
        wx.navigateTo({
            url: '/pages/myAccount/myCourse/myCourse',
        })
    },
    //   点击选择母语

    chooseMotherLanguage: function(e) {
        wx.navigateTo({
            url: '/pages/myAccount/motherLanguage/motherLanguage',
        })
    },

    /**
     * 点击选择学习语言
     */

    setLearnLanguage: function() {
        wx.navigateTo({
            url: '/pages/myAccount/learnLanguage/learnLanguage?chooseType=editChooseLang',
        })
    },

    /**
     * 切换翻译
     */
    switchTranslate: function() {
        var page = this;
        var robotTranslateLang = wx.getStorageSync("robotTranslateLang")
        if (robotTranslateLang == "Baidu") {
            page.setData({
                robotTranslateLang: "Microsoft"
            })
            wx.setStorageSync("robotTranslateLang", "Microsoft")
        } else if (robotTranslateLang == "Microsoft") {
            page.setData({
                robotTranslateLang: "Baidu"
            })
            wx.setStorageSync("robotTranslateLang", "Baidu")
        }
    },

    /**
     * 点击邀请新用户
     */

    setInviteUser: function(e) {
        var page = this;
        page.setShareModalStatus(e);
    },
    //分享-抽屉层

    setShareModalStatus: function(e) {
        var animation = createAnimation(this);
        this.setData({
            ShareAnimationData: animation.export()
        })

        var status;
        if (typeof e.currentTarget == 'undefined') {
            status = 0;
        } else {
            status = e.currentTarget.dataset.status;
        }
        if (status == 1) {
            this.setData({
                showShareModal: true
            });
        }

        setTimeout(function() {
            animation.translateY(0).step()
            this.setData({
                ShareAnimationData: animation
            })
            if (status == 0) {
                this.setData({
                    showShareModal: false
                });
            }
        }.bind(this), 200)
    },
    //分享抽屉层[取消]按钮

    delLink: function(e) {
        var page = this;
        this.setShareModalStatus(e);
    },

    // 清除缓存
    clearStorage: function() {
        wx.removeStorageSync("leadingScene");
        wx.removeStorageSync("editScene");
        wx.showToast({
            title: '清除完成',
            icon: "none"
        })
    },

    onShareAppMessage: function(e) {
        this.delLink(e);
        // 机构码
        var rcode = getApp().globalData.rcode;
        return {
            title: '邀您加入爱灵格',
            path: '/pages/find/find?rcode=' + rcode,
        }
    },

})
//创建动画
function createAnimation(page) {
    var animation = wx.createAnimation({
        duration: 200,
        timingFunction: "linear",
        delay: 0
    })
    page.animation = animation
    animation.translateY(300).step()
    return animation;
}

//获取用户信息
var userInfo = ({
    page
}) => {
    var now = new Date().getTime();
    var uid = wx.getStorageSync("loginInfo").userId;

    var token = Utils.SHA1(Config.xlingual_token + "WoW" + uid + "WoW" + now)
    getApp().globalData.userId = uid;
    getApp().globalData.token = token;
    var data = {
        token: token,
        ts: now,
        uid: uid
    }
    Account.userInfo(data)
        .then((res) => {
            wx.setStorageSync("userInfo", res.payload)
        }, () => {

        });
}

//获取账户用户信息
var accountInfo = ({
    page
}) => {
    var uid = wx.getStorageSync("loginInfo").userId;
    var id = wx.getStorageSync("loginInfo").id;
    var data = {}
    Account.accountInfo(data, uid, id)
        .then((res) => {
            // 保存机构邀请码
            getApp().globalData.rcode = res.rcode;

            firstLang = res.first_lang;
            // var learnLang = {};
            // var concatMotherDestLang = [];
            // var destMotherLang = [];
            destLang = res.dest_langs;
            if (firstLang == "" && destLang.length == 0) {
                var data1 = {
                    "first_lang": "zh"
                }
                var data2 = {
                    "dest_langs": ["en"]
                }
                Account.putAccountInfo(data1, uid, id)
                    .then((res) => {
                        firstLang = res.first_lang;
                        Account.putAccountInfo(data2, uid, id)
                            .then((res) => {
                                destLang = res.dest_langs;
                                console.log(firstLang)
                                console.log(destLang)
                                resetAccount({
                                    page
                                }, res, firstLang, destLang)
                            }, () => {

                            });
                    }, () => {

                    });


            } else {
                resetAccount({
                    page
                }, res, firstLang, destLang)
            }


        }, () => {

        });
}

function resetAccount({
    page
}, res, firstLang, destLang) {
    var learnLang = {};
    var concatMotherDestLang = [];
    var destMotherLang = [];
    var indexFirstLang = {};
    var newLearnLang = [];
    var checkedLang = []
    var balance = ""
    for (var i = 0; i < lang.length; i++) {
        if (lang[i].code == firstLang && lang[i].show == false) {
            indexFirstLang.name = lang[i].name;
            indexFirstLang.payload = res.first_lang;
            indexFirstLang.eventName = "lang-first-change";
        }
    }
    wx.setStorageSync("langFirst", indexFirstLang)
    wx.setStorageSync("creator", res.nickname)
    wx.setStorageSync("phoneNumber", res.mobile)
    destMotherLang.push(firstLang)
    if (destLang.indexOf(firstLang) > -1) {
        destLang.splice(destLang.indexOf(firstLang), 1)
    } else {
        destLang = destLang
        destMotherLang.concat(destLang)
    }
    concatMotherDestLang = destMotherLang.concat(destLang)
    for (var i = 0; i < concatMotherDestLang.length; i++) {
        checkedLang.push({
            "code": concatMotherDestLang[i],
            "checked": true
        })
        for (var j = 0; j < lang.length; j++) {
            if (concatMotherDestLang[i] == lang[j].code && lang[j].show == false) {
                newLearnLang.push(lang[j].name);
            }
        }
    }
    if (res.avatar == '' || res.avatar == undefined || res.avatar == null) {
        res.avatar = "../images/myAccount/head.png"
    }
    learnLang.eventName = "lang-dest-change";
    learnLang.payload = concatMotherDestLang;
    learnLang.first_lang = res.first_lang;
    learnLang.checkedLang = checkedLang;
    wx.setStorageSync("destLang", learnLang)
    // wx.setStorageSync("destLangTwo", learnLang)
    var robotTranslateLang = wx.getStorageSync("robotTranslateLang")
    if (robotTranslateLang == "" || robotTranslateLang == undefined) {
        wx.setStorageSync("robotTranslateLang", "Baidu")
    } else {
        wx.setStorageSync("robotTranslateLang", robotTranslateLang)
    }
    wx.setStorageSync("courseStore", res._xlingual)
    page.setData({
        accountAllInfo: res,
        motherLang: indexFirstLang,
        learnLang: newLearnLang.join(","),
        robotTranslateLang: robotTranslateLang,
        balance: res._xlingual.balance
    });
}