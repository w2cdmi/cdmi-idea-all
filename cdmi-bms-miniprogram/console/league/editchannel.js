// console/school/editchannel.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        name: '',
        icon: '',
        marker: '',
        cataloies:[],//频道下的类目
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        if (options.marker != null && options.marker != '') {
            this.data.marker = options.marker;
        }
        var title = '频道信息';
        if (options.action != null && options.action != '') {
            if (options.action == 'new') {
                title = "新建频道信息";
            } else if (options.action == 'edit') {
                title = "编辑频道信息";
            } else {
                console.error("不支持传入的参数值[" + options.action + "]，请联系开发商");
                return;
            }
            wx.setNavigationBarTitle({
                title: title,
            })
        } else {
            console.error("缺少必须的传入参数[action]，请联系开发商");
            return;
        };

        var _this = this;
        const db = wx.cloud.database();

        if (options.action == 'edit') {
            //编辑指定的频道信息
            console.log("执行频道信息编辑");
            if (options.id == null || options.id == '') {
                console.error("缺少必须的传入参数[id]，请联系开发商");
                return;
            }
            db.collection("a_channels").doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '传入的频道信息未找到，请退出后重试',
                    });
                }
                _this.setData({
                    id: res.data._id,
                    name: res.data.name,
                    icon: res.data.icon,
                });
            });
        };
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
     * 保存分类标识信息
     */
    saveChannel: function(e) {
        var _this = this;
        var data = e.detail.value;
        console.info(data);
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '频道的名称不能为空',
            });
            return;
        };

        const db = wx.cloud.database();
        if (_this.data.id != null && _this.data.id != '') {
            console.log("执行编辑频道操作");
            //编辑分类标记信息，检查标识名称是否已存在
            const _ = db.command;
            db.collection("a_channels").where({
                appId: _.eq(getApp().globalData.consoleAppId),
                _id: _.neq(_this.data.id),
                marker:_this.data.marker,
                name: _.eq(data.name.trim()),
            }).count().then(res => {
                //如果不等于0，则要保存的sign或name已存在。
                if (res.total > 0) {
                    wx.navigateTo({
                        url: '/page/error?content=频道类型名称已存在',
                    })
                    return;
                }
                //保存编辑后的结果
                console.log("保存编辑后的结果" + _this.data.marker);
                db.collection("a_channels").doc(_this.data.id).update({
                    data: {
                        name: data.name.trim(),
                        icon: data.icon,
                        marker: _this.data.marker,
                    }
                }).then(res=>{
                    //将结果设置回上一个页面，进行返回。不要跳转。
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    for (var i = 0; i < prePage.data.channels.length;i++){
                        if (prePage.data.channels[i].id == _this.data.id) {
                            prePage.data.channels[i].name = data.name.trim();
                            prePage.data.channels[i].icon = data.icon;
                            break;
                        }
                    }

                    prePage.setData({
                        channels: prePage.data.channels,
                    });
                    wx.navigateBack();
                });
            });
        } else {
            //保存频道信息，同一个应用下不能有同名的频道
            console.log("执行保存新的频道操作");

            db.collection("a_channels").where({
                appId: getApp().globalData.consoleAppId,
                name: data.name.trim(),
            }).count().then(res => {
                if (res.total == 0) {
                    db.collection('a_channels').add({
                        data: {
                            name: data.name.trim(),
                            icon: data.icon,
                            desc:data.desc,
                            marker: _this.data.marker,
                            appId: getApp().globalData.consoleAppId,
                            accountId: getApp().globalData.userId,
                            creatime: new Date(),
                        },
                    }).then(res => {
                        //将结果设置会上一个页面，进行返回。不要跳转。
                        var pages = getCurrentPages();
                        var prePage = pages[pages.length - 2];
                        var channel = {};
                        channel.id = res._id;
                        channel.name = data.name.trim();
                        channel.icon = data.icon;
                        //将新的类型插入到原有记录的首行
                        prePage.data.channels.unshift(channel);
                        prePage.setData({
                            channels: prePage.data.channels,
                        });
                        wx.navigateBack();
                    });
                } else {
                    wx.showToast({
                        title: '您已创建有同名的频道，请重新输入名称',
                    })
                }
            })
        }
    },

    /**
     * 删除当前指定频道
     */
    deleteChannel: function(e) {
        var _this = this;
        if (_this.data.id == null || _this.data.id == "") {
            wx.showToast({
                title: '未找到频道信息,请联系开发商',
            });
            return;
        };
        const db = wx.cloud.database();
        db.collection("a_channels").doc(_this.data.id).remove().then(res => {
            if (res.stats.removed == 1) {
                //从上一页的data中删除
                var pages = getCurrentPages();
                var prePage = pages[pages.length - 2];
                var channels = prePage.data.channels;
                for (var i = 0; i < channels.length; i++) {
                    if (channels[i].id == _this.data.id) {
                        channels.splice(i, 1);
                        break;
                    }
                };
                prePage.setData({
                    channels: channels,
                });
            }
            wx.navigateBack();
        });
    }
})