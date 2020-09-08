var Course = require("../../../../model/course.js")
var Util = require("../../../../common/utils.js")
Page({

    /**
     * 页面的初始数据
     */
    data: {
        courseDetailList: {},
        "isBuy": true,
        "chooseSize": false,
        "animationData": {}
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var page = this;
        var courseDetailList = wx.getStorageSync("courseDetail");
        var phoneNumber = wx.getStorageSync("phoneNumber")
        var priceSet = courseDetailList.price_set
        for (var i = 0; i < priceSet.length; i++) {
            priceSet[i].mealDueTime = priceSet[i].effective_length + getEffectiveTimeName(priceSet[i].effective_length_unit)
        }
        courseDetailList.price_set = priceSet
        var data = {
            courseId: courseDetailList.id,
            phone: phoneNumber
        }
        Course.inviteCode(data).then((res) => {
            courseDetailList.courseInviteCode = res.invite_code
            page.setData({
                courseDetailList: courseDetailList
            })
        }, () => {

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
    // 购买套餐
    gotoBuyMeal: function(e) {
        var page = this;
        page.setData({
            chooseSize: false
        })
        var userInfo = wx.getStorageSync("userInfo")
        var loginInfo = wx.getStorageSync("loginInfo")
        var balance = parseFloat(userInfo.balance)
        var item = e.currentTarget.dataset.item
        var rechargeMoney = (parseFloat(item.amount) - balance).toFixed(2)
        if (parseFloat(item.amount) > balance) {
            wx.showModal({
                title: '请先充值',
                content: '账户余额不足',
                confirmText: '去充值',
                success: function(res) {
                    if (res.confirm) {
                        wx.navigateTo({
                            url: '/pages/myAccount/accountRecharge/accountRecharge?rechargeMoney=' + rechargeMoney + "&balance=" + balance,
                        })
                    }
                }

            })
        } else {

            var cid = page.data.courseDetailList.id
            var oic = page.data.courseDetailList.courseInviteCode
            var pid = item.id
            var ts = new Date().getTime()
            var iv = ('000000000000000' + ts).slice(-16);
            iv += iv;
            var key = "01ab38d51415161710111213d4626107"
            var dataToEncrypt = loginInfo.userId + '__@__' + loginInfo.id
            var enc_key = Util.CryptoJS.AES.encrypt(dataToEncrypt, Util.CryptoJS.enc.Hex.parse(key), {
                iv: Util.CryptoJS.enc.Hex.parse(iv),
                mode: Util.CryptoJS.mode.CBC
            }).toString();
            var data = {
                ts: ts,
                token: enc_key,
                cid: cid,
                pid: pid,
                oic: oic
            }
            wx.showModal({
                title: '购买课程',
                content: '购买该课程将花费' + item.amount + item.currency.code,
                success: function(res) {
                    if (res.confirm) {
                        Course.purchaseCourse(data).then((res) => {
                            if (res.code == 200) {
                                wx.showToast({
                                    title: '购买成功',
                                    icon: "none"
                                })
                                setTimeout(function() {
                                    wx.navigateBack({
                                        delta: 2
                                    })
                                }, 1000)
                            } else {
                                wx.showToast({
                                    title: '购买失败，请重新购买',
                                    icon: "none"
                                })
                            }
                        }, () => {

                        })
                    }
                }
            })
        }
    },

    closeCurrent: function() {
        wx.navigateBack({
            delta: 1
        })
    },
    /**
     * 点击购买按钮
     */
    gotoBuyCourse: function(e) {
        // 用that取代this，防止不必要的情况发生
        var that = this;
        // 创建一个动画实例
        var animation = wx.createAnimation({
            // 动画持续时间
            duration: 300,
            // 定义动画效果，当前是匀速
            timingFunction: 'linear'
        })
        // 将该变量赋值给当前动画
        that.animation = animation
        // 先在y轴偏移，然后用step()完成一个动画
        animation.translateY(200).step()
        // 用setData改变当前动画
        that.setData({
            // 通过export()方法导出数据
            animationData: animation.export(),
            // 改变view里面的Wx：if
            chooseSize: true
        })
        // 设置setTimeout来改变y轴偏移量，实现有感觉的滑动
        setTimeout(function() {
            animation.translateY(0).step()
            that.setData({
                animationData: animation.export()
            })
        }, 200)
    },

    /**
     * 隐藏
     */
    hideModal: function(e) {
        var that = this;
        var animation = wx.createAnimation({
            duration: 300,
            timingFunction: 'linear'
        })
        that.animation = animation
        animation.translateY(200).step()
        that.setData({
            animationData: animation.export()
        })
        setTimeout(function() {
            animation.translateY(0).step()
            that.setData({
                animationData: animation.export(),
                chooseSize: false
            })
        }, 200)
    },
    preventTouchMove: function(e) {

    }
})

var getEffectiveTimeNameMap = {
    1: "年",
    2: "月",
    3: "日",
}

function getEffectiveTimeName(timeId) {
    return getEffectiveTimeNameMap[timeId]
}