// console/league/ad/editadsense.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        name: '',
        sign: '',
        mouthprice:'',
        yearprice:'',
        count:'',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '新增广告位';
        if (options.action != null && options.action != '') {
            if (options.action == 'edit') {
                title = "编辑广告位";
            } else {
                console.error("不支持传入的参数值[" + options.action + "]，请联系开发商");
                return;
            }
            wx.setNavigationBarTitle({
                title: title,
            })
        };

        if (options.action == 'edit') {
            var _this = this;
            const db = wx.cloud.database();
            //编辑指定的广告位
            if (options.id == null || options.id == '') {
                console.error("缺少必须的传入参数[id]，请联系开发商");
                return;
            }
            db.collection("adsenses").doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '传入的广告位信息未找到，请退出后重试',
                    });
                }
                _this.setData({
                    id: res.data._id,
                    name: res.data.name,
                    sign: res.data.sign,
                    mouthprice: res.data.mouthprice,
                    yearprice: res.data.yearprice,
                    count: res.data.count,
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
     * 保存广告位信息
     */
    saveAdsense: function(e) {
        var _this = this;
        var data = e.detail.value;
        console.info(data);
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '广告位的名称不能为空',
            });
            return;
        };
        if (data.sign == null || data.sign.trim() == '') {
            wx.showToast({
                title: '广告位的标识码不能为空',
            });
            return;
        };

        const db = wx.cloud.database();
        if (_this.data.id != null && _this.data.id != '') {
            console.log("执行编辑广告位操作");
            //编辑广告位信息，检查标识名称是否已存在
            const _ = db.command;
            db.collection("adsenses").where({
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
                        url: '/page/error?content=标识码或广告位名称已存在',
                    })
                    return;
                }
                //保存编辑后的结果
                console.log("保存编辑后的结果");
                db.collection("adsenses").doc(_this.data.id).update({
                    data: {
                        name: data.name.trim(),
                        sign: data.sign.trim(),
                        mouthprice: data.mouthprice,
                        yearprice: data.yearprice,
                        count: data.count,
                    }
                });
                var pages = getCurrentPages();
                var prePage = pages[pages.length - 2];

                for (var i = 0; i < prePage.data.adsenses.length; i++) {
                    if (prePage.data.adsenses[i].id == _this.data.id) {
                        prePage.data.adsenses[i].name = data.name.trim();
                        prePage.data.adsenses[i].sign = data.sign;
                        prePage.data.adsenses[i].mouthprice = data.mouthprice;
                        prePage.data.adsenses[i].yearprice = data.yearprice;
                        prePage.data.adsenses[i].count = data.count;
                        break;
                    }
                }

                prePage.setData({
                    adsenses: prePage.data.adsenses,
                });

                wx.navigateBack();
            });
        } else {
            //保存广告位信息，同一个应用下不能有同名的广告位
            console.log("执行保存新的广告位操作");

            const _ = db.command;
            db.collection("adsenses").where({
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
                    db.collection('adsenses').add({
                        data: {
                            name: data.name.trim(),
                            sign: data.sign.trim(),
                            mouthprice: data.mouthprice,
                            yearprice: data.yearprice,
                            count : data.count,
                            appId: getApp().globalData.consoleAppId,
                            accountId: getApp().globalData.userId,
                            creatime: new Date(),
                        },
                    }).then(res => {
                        //将结果设置会上一个页面，进行返回。不要跳转。
                        var pages = getCurrentPages();
                        var prePage = pages[pages.length - 2];
                        var adsense = {};
                        adsense.id = res._id;
                        adsense.name = data.name.trim();
                        adsense.sign = data.sign.trim();
                        adsense.mouthprice = data.mouthprice;
                        adsense.yearprice = data.yearprice;
                        adsense.count = data.count;
                        //将新的类型插入到原有记录的首行
                        prePage.data.adsenses.unshift(adsense);
                        prePage.setData({
                            adsenses: prePage.data.adsenses,
                        });
                        wx.navigateBack();
                    });
                } else {
                    wx.showToast({
                        title: '您已创建有同名的广告位，请重新输入名称',
                    })
                }
            })
        }
    },
})