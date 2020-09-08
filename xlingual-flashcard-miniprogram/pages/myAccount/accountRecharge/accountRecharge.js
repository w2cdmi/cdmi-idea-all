var Pay = require("../../../model/pay.js");
var balance = 0.00; //账户余额
var rechargeMoney = 100 //充值金额
var rechargeBalance = 100.00 //充值后余额
Page({

    /**
     * 页面的初始数据
     */
    data: {
        balance: balance,
        rechargeMoney: rechargeMoney,
        rechargeBalance: rechargeBalance,
        isShowIt: true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var page = this;
        balance = options.balance
        if (options.rechargeMoney){
            rechargeMoney = options.rechargeMoney
            page.setData({
                rechargeMoney: rechargeMoney
            })
        }
        rechargeBalance = (accAdd(balance, rechargeMoney)).toFixed(2)
        page.setData({
            balance: balance,
            rechargeBalance: rechargeBalance
        })
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

    },
    changeReMoney: function(e) {
        var page = this
        var value = e.detail.value;
        var reg = new RegExp(/^([0-9]{1,10})+(\.[0-9]{1,2})?$/)
        if (reg.exec(value)) {
            rechargeBalance = (accAdd(value,balance)).toFixed(2)
            rechargeMoney = value
            page.setData({
                isShowIt: true,
                rechargeBalance: rechargeBalance
            })
        } else {
            page.setData({
                isShowIt: false
            })
        }
    },
    goToPay: function() {
        var page = this;
        if (!page.data.isShowIt) {
            return;
        }
        var loginInfo = wx.getStorageSync("loginInfo")
        var rcode = getApp().globalData.rcode
        var data = {
            "userId": loginInfo.userId,
            "wxUserId": loginInfo.wxUsertId,
            "oic": rcode,
            "money": rechargeMoney
        }
        Pay.unifiedorder(data).then((res) => {
            wx.requestPayment({
                'timeStamp': res.timeStamp,
                'nonceStr': res.nonceStr,
                'package': res.package,
                'signType': res.signType,
                'paySign': res.paySign,
                'success': function(res) {
                    wx.navigateBack({
                        delta: 1,
                    });
                },
                'fail': function(res) {
                    wx.showToast({
                        title: '支付失败，请稍后重试',
                        icon: 'none',
                        duration: 1000
                    })
                }
            })
        }, () => {

        })
    }

})


function accAdd(arg1, arg2) {
    var r1, r2, m;
    try {
        r1 = arg1.toString().split(".")[1].length
    } catch (e) {
        r1 = 0
    }
    try {
        r2 = arg2.toString().split(".")[1].length
    } catch (e) {
        r2 = 0
    }
    m = Math.pow(10, Math.max(r1, r2))
    return (arg1 * m + arg2 * m) / m
}

