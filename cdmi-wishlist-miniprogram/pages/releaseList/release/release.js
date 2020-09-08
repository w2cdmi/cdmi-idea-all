var config = require("../../module/config.js");
var productService = require("../../module/product.js");

Page({

    /**
     * 页面的初始数据
     */
    data: {
        startDate: new Date(),
        selectImages: []
    },

    bindChangeDate: function(e) {
        var value = e.detail.value;
        this.setData({
            selectDate: value
        })
    },

    bindChangeTime: function(e) {
        var value = e.detail.value;
        this.setData({
            selectTime: value
        })
    },

    formSubmit: function (e) {
        var page = this;
        var data = e.detail.value;
        if (data == "" || data == undefined || data == {}){
            wx.showToast({
                title: '参数为空',
                duration: 1000
            })
            return;
        }
        if (data.title == ""){
            wx.showToast({
                title: '商品名字为空',
                duration: 1000
            })
            return;
        }
        if (data.originalPrice == "") {
            wx.showToast({
                title: '商品市场价格为空',
                duration: 1000
            })
            return;
        }
        if (data.actualPrice == "") {
            wx.showToast({
                title: '商品众筹价格为空',
                duration: 1000
            })
            return;
        }
        if (data.salesValidity == "") {
            wx.showToast({
                title: '有效日期为空',
                duration: 1000
            })
            return;
        }
        if (data.salesTime == "") {
            wx.showToast({
                title: '有效时间为空',
                duration: 1000
            })
            return;
        }
        if (data.ratedNumber == "") {
            wx.showToast({
                title: '达标人数为空',
                duration: 1000
            })
            return;
        }
        data.salesValidity = data.salesValidity + " " + data.salesTime +":00";
        data.salesValidity = new Date(data.salesValidity);
        delete data.salesTime;
        productService.createProduct(data, function(data){
            if (data){
                var id = data.id;
                var index = 0;  //从第一个图片开始上传
                var uploadingList = page.data.selectImages;
                uploadImage(uploadingList, index, id);

                wx.showToast({
                    title: '发布成功！',
                    duration: 1000
                })
                setTimeout(function(){
                    wx.switchTab({
                        url: '../lottery/lottery',
                    })
                },1000);
            }
        })
    },
    selectImages: function(){
        var page = this;
        wx.chooseImage({
            success: function(res) {
                var paths = res.tempFilePaths;
                page.data.selectImages = page.data.selectImages.concat(paths);

                page.setData({
                    images: page.data.selectImages
                });
            },
        })
    }
})

function uploadImage(uploadingList, index, id){
    if (index > uploadingList.length - 1) {
        wx.showToast({
            title: '图片上传成功！',
            duration: 600
        });
        setTimeout(function(){
            wx.redirectTo({
                url: '../lottery/lottery',
            })
        },600);

        return;
    }

    wx.uploadFile({
        url: config.host + "/wishlist/products/v1/uploadImage/" + id,
        filePath: uploadingList[index],
        name: "image",
        success: function (res) {
            index += 1;
            uploadImage(uploadingList, index, id)
        },
        fail: function () {
            wx.showToast({
                title: '上传图片失败',
            })
        }
    });
}