var Account = require("../../../model/account.js");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        myCourseList: [],
        imgUrl: ""
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function() {
        accountInfo({
            page: this
        })
    },
    toUseMycourse: function() {
        wx.switchTab({
            url: '/pages/find/find'
        })
        setTimeout(function(){
            wx.showToast({
                title: '请开始试用吧！',
                icon: "none"
            })
        },1000)
    }
})
//获取账户用户信息
var accountInfo = ({
    page
}) => {
    var uid = wx.getStorageSync("loginInfo").userId;
    var id = wx.getStorageSync("loginInfo").id;
    var data = {}
    Account.accountInfo(data, uid, id)
        .then((res) => {
            for (var i = 0; i < res._xlingual.courseplan_set.length; i++) {
                var subEndTime = (res._xlingual.courseplan_set[i].end_time).substring(0, 10)
                res._xlingual.courseplan_set[i].end_time = subEndTime
            }
            page.setData({
                myCourseList: res._xlingual.courseplan_set,
                imgUrl: "http://www.xlingual.net"
            })

        }, () => {

        });
}