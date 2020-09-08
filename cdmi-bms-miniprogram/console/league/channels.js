// console/school/channels.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        channels: [],
        marker:'',
        sence:'', //被其他页应用
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        if (options.marker != null && options.marker != '') {
            this.data.marker = options.marker;
        }
        wx.setNavigationBarTitle({
            title: '频道列表',
        });
        if (options.sence == 'options') {
            this.setData({
                sence: options.sence,
            });
        }

        //判断，必须进入点击应用后台管理才能进行操作。
        if (getApp().globalData.consoleAppId == null || getApp().globalData.consoleAppId == '') {
            wx.showToast({
                title: '未获得应用信息，请联系开发商',
            });
            wx.redirectTo({
                url: '/page/error?content=未获得应用信息，请联系开发商',
            })
        }

        //检查当前用户是否为该应用的管理员，不是，则拒绝操作
        var _this = this;
        const db = wx.cloud.database();
        db.collection("app_admins").where({
            accountId: getApp().globalData.userId,
            accountType: 'SYS_ACCOUNT',
            appId: getApp().globalData.consoleAppId,
        }).get().then(res => {
            if (res.data.length == 0) {
                wx.redirectTo({
                    url: '/page/error?content=您不是该应用的管理员，请勿操作',
                })
            }
        });

        console.log("获取应用的类目频道列表");
        db.collection('a_channels').where({
            appId: getApp().globalData.consoleAppId,
            marker: _this.data.marker,
        }).get().then(res => {
            var channels = [];
            res.data.forEach(function(item) {
                var channel = {};
                channel.id = item._id;
                channel.name = item.name;
                channel.icon = item.icon;
                channels.push(channel);
            })
            _this.setData({
                channels: channels,
            })
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

    /**
     * 创建一个新的频道
     */
    createChannel: function() {
        wx.navigateTo({
            url: 'editchannel?action=new&marker=' + this.data.marker,
        })
    },

    /**
     * 跳转到频道详情页面
     */
    jumpToChannelDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'editchannel?action=edit&id=' + dataset.id + '&marker=' + this.data.marker,
        })
    },
    
    /**
     * 在其他页面引用时，选择指定的频道操作
     */
    selectChannel:function(e){
        const dataset = e.currentTarget.dataset;
        var pages = getCurrentPages();
        var prevPage = pages[pages.length - 2]; //上一个页面
        var channel = {};
        channel.id = dataset.id;
        channel.name = dataset.title;
        prevPage.setData({
            channel: channel,
        });
        wx.navigateBack();
    }
})