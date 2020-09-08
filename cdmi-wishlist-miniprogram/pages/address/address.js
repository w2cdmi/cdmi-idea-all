var addressService = require('../module/address.js');

Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: ''
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var page = this;
        var id = options.id;
        if(id != undefined){
            page.data.id = id;
            getAddressInfo(id, page);
        }
    },

    formSubmit: function (e) {
        var data = e.detail.value;
        var page = this;
        if (data == "" || data == undefined || data == {}) {
            wx.showToast({
                title: '参数为空',
                duration: 1000
            })
            return;
        }
        if (data.addressName == "") {
            wx.showToast({
                title: '收货人不能为空',
                duration: 1000
            })
            return;
        }
        if (data.phoneNumber == "") {
            wx.showToast({
                title: '电话不能为空',
                duration: 1000
            })
            return;
        }
        if (data.address == "") {
            wx.showToast({
                title: '收获地址不能为空',
                duration: 1000
            })
            return;
        }
        if(page.data.id == ''){
            addressService.createAddress(data, function (data) {
                if (data) {
                    wx.showToast({
                        title: '添加地址成功',
                        duration: 1000
                    })
                    setTimeout(function () {
                        wx.navigateBack({
                            delta: 1
                        })
                    }, 1000);
                }
            })
        }else{
            data.id = page.data.id;
            addressService.editAddress(data, function (data) {
                if (data) {
                    wx.showToast({
                        title: '修改地址成功',
                        duration: 1000
                    })
                    setTimeout(function () {
                        wx.navigateBack({
                            delta: 1
                        })
                    }, 1000);
                }
            })
        }
    }
})

function getAddressInfo(id, page){
    addressService.getAddressById(id, function(data){
        page.setData({
            address: data
        });
    });
}