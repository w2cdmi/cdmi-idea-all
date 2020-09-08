// console/school/expert/editexpert.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        name: '',
        mobile: '',
        headImage: '',
        fields_value: '',
        fields_text: '',
        brief: '',
        status_value: '',
        status_text: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        console.info(options);

        var title = '签约专家';
        if (options.action != null && options.action != '') {
            if (options.action == 'new') {
                title = "新建签约专家";
            } else if (options.action == 'edit') {
                title = "编辑签约专家";
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
            //编辑指定的签约专家
            if (options.id == null || options.id == '') {
                console.error("缺少必须的传入参数[id]，请联系开发商");
                return;
            }
            db.collection("a_experts").doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '指定的专家信息未找到，请退出后重试',
                    });
                }
                _this.setData({
                    id: res.data._id,
                    name: res.data.name,
                    mobile: res.data.mobile,
                    headImage: res.data.headImage,
                    //FIXME 转换fields。
                    fields_value: res.data.fields,
                    brief: res.data.brief,
                    //FIXME 转换fields。
                    status_value: res.data.status,
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
     * 保存新添加的专家信息
     */
    saveExpert: function(e) {
        var _this = this;
        var data = e.detail.value;
        console.info(data);
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '专家的名称不能为空',
            });
            return;
        };
        if (data.mobile == null || data.mobile.trim() == '') {
            wx.showToast({
                title: '专家的手机号码不能为空',
            });
            return;
        };
        const db = wx.cloud.database();
        if (_this.data.id != null && _this.data.id != '') {
            console.log("执行编辑专家信息的操作");
            //编辑分类标记信息，检查标识名称是否已存在
            const _ = db.command;
            db.collection("a_experts").where({
                appId: _.eq(getApp().globalData.consoleAppId),
                _id: _.neq(_this.data.id),
                mobile: _.eq(data.mobile.trim()),
            }).count().then(res => {
                //如果不等于0，则要保存的sign或name已存在。
                if (res.total > 0) {
                    wx.navigateTo({
                        url: '/page/error?content=专家手机号码已存在',
                    })
                    return;
                }
                //保存编辑后的结果
                console.log("保存编辑后的结果");
                db.collection("a_experts").doc(_this.data.id).update({
                    data: {
                        name: data.name.trim(),
                        mobile: data.mobile.trim(),
                        //TODO 未更新其他字段
                    }
                }).then(res => {
                    //将结果设置回上一个页面，进行返回。不要跳转。
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    for (var i = 0; i < prePage.data.experts.length; i++) {
                        if (prePage.data.experts[i].id == _this.data.id) {
                            prePage.data.experts[i].name = data.name.trim();
                            prePage.data.experts[i].mobile = data.mobile;
                            prePage.data.experts[i].headImage = data.headImage;
                            break;
                        }
                    }

                    prePage.setData({
                        experts: prePage.data.experts,
                    });
                    wx.navigateBack();
                });
            });
        } else {
            //保存频道信息，同一个应用下不能有同名的频道
            console.log("执行保存新添加专家信息操作");

            db.collection("a_experts").where({
                appId: getApp().globalData.consoleAppId,
                mobile: data.mobile.trim(),
            }).count().then(res => {
                if (res.total == 0) {
                    db.collection('a_experts').add({
                        data: {
                            name: data.name.trim(),
                            mobile: data.mobile,
                            headImage: data.headImage,
                            fields: data.fields,
                            brief: data.brief,
                            agreement: data.agreement,
                            appId: getApp().globalData.consoleAppId,
                            accountId: getApp().globalData.userId,
                            creatime: new Date(),
                            status:'ToBeSigned',
                        },
                    }).then(res => {
                        //将结果设置会上一个页面，进行返回。不要跳转。
                        var pages = getCurrentPages();
                        var prePage = pages[pages.length - 2];
                        var expert = {};
                        expert.id = res._id;
                        expert.name = data.name.trim();
                        expert.headImage = data.headImage;
                        expert.mobile = data.mobile.trim();
                        //将新的类型插入到原有记录的首行
                        prePage.data.experts.unshift(expert);
                        prePage.setData({
                            experts: prePage.data.experts,
                        });
                        wx.navigateBack();
                    });
                } else {
                    wx.showToast({
                        title: '已存在相同的手机号码，请重新输入名称',
                    })
                }
            })
        }
    }
})