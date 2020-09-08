
var Account = require("../../../model/account.js");
var Utils = require("../../../common/utils.js");
var appGlobalData = getApp().globalData;
Page({

    /**
     * 页面的初始数据
     */
    data: {

        personInfo: {},
        "chooseSize": false,
        "animationData": {},
        nickNameValue: ""
    },

    /**
     * 生命周期函数--监听页面加载
     */

    onLoad: function(options) {
        this.setData({
            personInfo: options,
            nickNameValue: options.nickName
        })
    },

    /**
     * 设置手机号码
     */
    setPhoneNumber: function() {
        wx.showModal({
            title: '抱歉',
            content: '暂时不支持更改手机号',
            showCancel: false
        })
    },

    /**
     * 修改昵称
     */

    setNickName: function() {
        var page = this;
        page.setData({
            chooseSize: true
        })
    },

    /**
     * 
     */
    getNewNickName: function(e) {
        this.setData({
            nickNameValue: e.detail.value
        })
    },
    /**
     * 隐藏
     */
    hideModal: function(e) {
        var page = this;
        page.setData({
            chooseSize: false
        })
    },

    /**
     * 确认修改用户昵称
     */
    confirmChangNickName: function() {
        var page = this;
        putNickName({
            page: this
        })
    },
    /**
     * 用户注销
     */
    userLogout: function() {
        userLogout({
            page: this
        })
    },
    preventTouchMove: function(e) {

    }
})

var putNickName = ({
    page
}) => {
    var uid = wx.getStorageSync("loginInfo").userId;
    var id = wx.getStorageSync("loginInfo").id;
    var data = {
        "nickname": page.data.nickNameValue
    }
    Account.putAccountInfo(data, uid, id)
        .then((res) => {
            var personInfo = {};
            personInfo.head = res.avatar;
            personInfo.nickName = res.nickname;
            personInfo.phoneNumber = res.mobile
            page.setData({
                personInfo: personInfo,
                chooseSize: false
            })
        }, () => {

        });
}

var userLogout = ({
    page
}) => {
    var token = wx.getStorageSync("loginInfo").id;
    var data = {
        token: token,
    }
    Account.userLogout(data, token).then((res) => {
        wx.setStorageSync("loginStatus", false);
        appGlobalData.loginstatus = false;
        wx.showToast({
            title: '注销成功',
            duration: 1500,
            success: setTimeout(function() {
                wx.navigateBack({
                    delta: 1
                })
            },1500)
        })
    }, () => {

    })
}

