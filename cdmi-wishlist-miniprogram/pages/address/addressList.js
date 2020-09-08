var addressService = require('../module/address.js');

Page({

    /**
     * 页面的初始数据
     */
    data: {
        addressList: [],
        scene: ""
    },
    onLoad: function (options) {
        var scene = options.scene;
        if (scene != undefined && scene != ""){
            this.data.scene = scene;
        }
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        var page = this;
        getUserAddressAll(page);
    },
    gotoAddAddress: function() {
        wx.navigateTo({
            url: '/pages/address/address',
        })
    },
    editAddress: function(e){
        var id = e.currentTarget.dataset.id;
        wx.navigateTo({
            url: '/pages/address/address?id=' + id,
        })
    },
    deleteAddress: function(e){
        var page = this;
        var id = e.currentTarget.dataset.id;
        addressService.deleteAddress(id, function(data){
            if(data){
                wx.showToast({
                    title: '删除成功！',
                    duration: 600
                })
                setTimeout(function(){
                    getUserAddressAll(page);
                },600);
            }else{
                wx.showToast({
                    title: '删除失败！',
                })
            }
        });
    },
    radioChange: function(e){
        var page = this;
        var id = e.detail.value;
        addressService.setAddressToDefault(id, function(data){
            if(data){
                wx.showToast({
                    title: '设置成功！',
                })
            }
        });
    },
    selectAddress: function(e){
        if(this.data.scene == ""){
            return;
        }
        var page = this;
        var id = e.currentTarget.dataset.id;
        addressService.setAddressToDefault(id, function (data) {
            if (data) {
                wx.navigateBack({
                    delta: 1
                })
            }
        });
    }
})

function getUserAddressAll(page){
    addressService.getAddressAll(null, function(data){
        page.setData({
            addressList: data
        });
    });
}