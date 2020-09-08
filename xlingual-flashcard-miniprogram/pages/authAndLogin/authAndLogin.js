Page({
    getUserInfo: function (e) {
        if (e.detail == undefined || e.detail.errMsg != 'getUserInfo:ok') {
            return;
        }
        //返回微信用户信息
        var res = e.detail;
        var data = {
            encryptedData: res.encryptedData,
            iv: res.iv
        };
        getApp().globalData.avatarUrl = res.userInfo.avatarUrl;
        wx.reLaunch({
          url: '/pages/find/find',
        })
    },
    exitLogin: function(){
        wx.reLaunch({
            url: '/pages/authAndLogin/authAndLogin',
        })
        setTimeout(function(){
            wx.navigateBack({
                delta: 2
            })
        },500);
    }
})