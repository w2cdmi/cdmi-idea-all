// page/distributor/setDistributorLevel.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        distributorId: '', //代理商ID
        currentLevel: '', //当前级别
        levels: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: '设置渠道商级别',
        });
        if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
            wx.showToast({
                title: '页面未能获取开发者信息，请联系开发商',
            })
            return;
        };
        if (options.distributorid == null || options.distributorid.trim() == '') {
            wx.showToast({
                title: '页面未能获取渠道商信息',
            })
            return;
        } else {
            _this.data.distributorId = options.distributorid.trim();
        };
        if (options.current != null && options.current.trim() != '') {
            _this.data.currentLevel = options.current.trim();
        }

        //获取可选择的渠道级别
        const db = wx.cloud.database();
        db.collection('distributor_levels').where({
            ownerId: getApp().globalData.developerId,
        }).get().then(res => {
            var levels = [];
            if (res.data.length != 0) {
                res.data.forEach(function(item) {
                    var level = {};
                    level.id = item._id;
                    level.title = item.title; //渠道等级
                    level.ratio = item.ratio; //分成比例
                    if (level.id == _this.data.currentLevel) {
                        level.isCurrent = true;
                    } else {
                        level.isCurrent = false;
                    }
                    levels.push(level);
                    console.info(levels);
                })
                _this.setData({
                    levels: levels,
                })
            }
        });
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

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function() {

    },

    selectLevel: function(e) {
        var _this = this;
        const dataset = e.currentTarget.dataset;
        if (_this.data.currentLevel == '' || dataset.id != _this.data.currentLevel) {
            //有变化,更新渠道商的渠道等级
            const db = wx.cloud.database();
            db.collection("distributors").doc(_this.data.distributorId).update({
                data: {
                    level: dataset.id,
                }
            }).then(res => {
                var pages = getCurrentPages();
                var prePage = pages[pages.length - 2];
                console.info(dataset.title);
                prePage.setData({
                    level_title: dataset.title,
                    level_id: dataset.id,
                });
                wx.navigateBack();
            }).catch(e => {
                console.info(e);
            });
        } else {
            wx.navigateBack();
        }
    }
})