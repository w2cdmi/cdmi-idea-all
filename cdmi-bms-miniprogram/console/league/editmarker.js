// console/school/editmarker.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        name: '',
        sign: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '分类标识';
        if (options.action != null && options.action != '') {
            if (options.action == 'new') {
                title = "新建分类标识";
            } else if (options.action == 'edit') {
                title = "编辑分类标识";
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
            //编辑指定的分类标识
            if (options.id == null || options.id == '') {
                console.error("缺少必须的传入参数[id]，请联系开发商");
                return;
            }
            db.collection("a_markers").doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '传入的分类标识信息未找到，请退出后重试',
                    });
                }
                _this.setData({
                    id: res.data._id,
                    name: res.data.name,
                    sign: res.data.sign,
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
    saveMarker: function(e) {
        var _this = this;
        var data = e.detail.value;
        console.info(data);
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '分类标记的名称不能为空',
            });
            return;
        };
        if (data.sign == null || data.sign.trim() == '') {
            wx.showToast({
                title: '分类标记的标识码不能为空',
            });
            return;
        };

        const db = wx.cloud.database();
        if (_this.data.id != null && _this.data.id != '') {
            console.log("执行编辑分类标记操作");
            //编辑分类标记信息，检查标识名称是否已存在
            const _ = db.command;
            db.collection("a_markers").where({
                appId: _.eq(getApp().globalData.consoleAppId),
                _id: _.neq(_this.data.id),
            }).where(
                _.or([{
                        name: data.name.trim()
                    },
                    {
                        sign: data.sign.trim()
                    }
                ])
            ).count().then(res => {
                //如果不等于0，则要保存的sign或name已存在。
                if (res.total > 0) {
                    wx.navigateTo({
                        url: '/page/error?content=标识码或分类标记名称已存在',
                    })
                    return;
                }
                //保存编辑后的结果
                console.log("保存编辑后的结果");
                db.collection("a_markers").doc(_this.data.id).update({
                    data: {
                        name: data.name.trim(),
                        sign: data.sign.trim(),
                    }
                });
                wx.navigateBack();
            });
        } else {
            //保存分类标记信息，同一个应用下不能有同名的分类标记
            console.log("执行保存新的分类标记操作");

            const _ = db.command;
            db.collection("a_markers").where({
                appId: _.eq(getApp().globalData.consoleAppId)
            }).where(
                _.or([{
                        name: data.name.trim()
                    },
                    {
                        sign: data.sign.trim()
                    }
                ])
            ).count().then(res => {
                if (res.total == 0) {
                    db.collection('a_markers').add({
                        data: {
                            name: data.name.trim(),
                            sign: data.sign.trim(),
                            appId: getApp().globalData.consoleAppId,
                            accountId: getApp().globalData.userId,
                            creatime: new Date(),
                        },
                    }).then(res => {
                        //将结果设置会上一个页面，进行返回。不要跳转。
                        var pages = getCurrentPages();
                        var prePage = pages[pages.length - 2];
                        var marker = {};
                        marker.id = res._id;
                        marker.name = data.name.trim();
                        marker.sign = data.sign.trim();
                        //将新的类型插入到原有记录的首行
                        prePage.data.markers.unshift(marker);
                        prePage.setData({
                            markers: prePage.data.markers,
                        });
                        wx.navigateBack();
                    });
                } else {
                    wx.showToast({
                        title: '您已创建有同名的分类标记，请重新输入名称',
                    })
                }
            })
        }
    },
})