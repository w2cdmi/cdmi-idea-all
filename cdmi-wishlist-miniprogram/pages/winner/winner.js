var config = require('../module/config.js');
var productService = require('../module/product.js');
var addressService = require('../module/address.js');
var sessionService = require('../session.js');
var payService = require('../module/pay.js');

Page({

    /**
     * 页面的初始数据
     */
    data: {
        productId: "",
        productImage: "",
        product: {
            title: "",
            actualPrice: 0,
            originalPrice: 0
        },
        address: {
            addressName: "",
            phoneNumber: "",
            address: ""
        }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var page = this;
        var productId = options.productId;
        if(productId != undefined){
            page.data.productId =productId;
        }
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        var page = this;
        sessionService.login();
        sessionService.invokeAfterLogin(function () {
            getProductInfo(page.data.productId, page);
            getDefaultAddress(page);
        });
    },
    gotoAddressListPage: function(){
        wx.navigateTo({
            url: '../address/addressList?scene=selectAddress',
        })
    },
    productOrder: function () {
        if(this.data.product.title == undefined || this.data.product.title == ""){
            wx.showToast({
                title: '商品信息不能为空！',
            })
        }
        if(this.data.address.addressName == undefined || this.data.address.addressName == ""){
            wx.showToast({
                title: '请选择一个地址！',
            })
        }
        var page = this;
        var data = {
            productId: page.data.productId,
            payType: "buy"
        }
        payService.buyProductUnifiedorder(data, function (data) {
            wx.requestPayment({
                'timeStamp': data.timeStamp,
                'nonceStr': data.nonceStr,
                'package': data.package,
                'signType': data.signType,
                'paySign': data.paySign,
                'success': function (res) {
                    wx.showToast({
                        title: '支付成功！',
                        duration: 1000
                    });
                    setTimeout(function () {
                        wx.navigateTo({
                            url: '../lottery/lottery',
                        })
                    }, 1000);
                },
                'fail': function (res) {
                    wx.showToast({
                        title: '支付失败，请稍后重试',
                        icon: 'none',
                        duration: 1000
                    })
                }
            })
        })
    }
})

function getProductInfo(productId, page) {
    productService.getNotPayProductById(productId ,function (product) {
        if (product == undefined || product == "") {
            wx.showToast({
                title: '获取商品信息失败！',
            })
            wx.switchTab({
                url: '../lottery/lottery',
            })
            return;
        }
        if (product.photoList != undefined) {
            var photoList = product.photoList;
            var imageList = photoList.split(",");
            var productImage = config.host + "/wishlist/products/v1/download/" + imageList[0];
            page.setData({
                productImage: productImage,
                product: product
            });
        }
    });
}

function getDefaultAddress(page){
    addressService.getDefaultAddress(function (address) {
        if (address == undefined || address == "") {
            wx.showToast({
                title: '获取用户地址失败！',
            })

            return;
        }
        page.setData({
            address: address
        });
    });
}