// page/microservice/editmicroservice.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id:'',
        name:'',
        icon:'',
        endpoint:'',
        desc:'',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '新建微服务';
        if (options != null && options.title != null) {
            title = options.title;
        };
        if (options.action == 'edit') {
            title = "微服务详情";
        };
        wx.setNavigationBarTitle({
            title: title,
        });
        if (options.id != null && options.id != '') {
            var _this = this;
            const db = wx.cloud.database();

            db.collection('microservices').doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '未能找到指定的微服务信息',
                    });
                    return;
                }else{
                    _this.setData({
                        id: options.id,
                        name: res.data.name,
                        endpoint: res.data.endpoint,
                        icon: res.data.icon,
                        desc: res.data.desc,
                    });
                }
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
     * 调转到微服务的部署参数页面
     */
    jumpToMicroserviceConfig: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'microserviceConfig?id=' + this.data.id + '&name=' + this.data.name,
        })
    },

    /**
     * 保存新的微服务信息
     */
    saveMicroservice: function(e) {
        var _this = this;
        var data = e.detail.value;
        console.info(data);
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '微服务名称不能为空',
            });
            return;
        };
        const db = wx.cloud.database();
        if (_this.data.id != null && _this.data.id != '') {
            console.log("执行编辑微服务信息的操作");
            const _ = db.command;
            db.collection("microservices").where({
                _id: _.neq(_this.data.id),
                name: _.eq(data.name.trim()),
            }).count().then(res => {
                //如果不等于0，则要保存的sign或name已存在。
                if (res.total > 0) {
                    wx.navigateTo({
                        url: '/page/error?content=微服务名称已存在',
                    })
                    return;
                }
                //保存编辑后的结果
                console.log("保存编辑后的结果");
                db.collection("microservices").doc(_this.data.id).update({
                    data: {
                        name: data.name.trim(),
                        icon: data.icon,
                        desc: data.desc,
                        endpoint: data.endpoint,
                        //TODO 未更新其他字段
                    }
                }).then(res => {
                    //将结果设置回上一个页面，进行返回。不要跳转。
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    for (var i = 0; i < prePage.data.microservices.length; i++) {
                        if (prePage.data.microservices[i].id == _this.data.id) {
                            prePage.data.microservices[i].name = data.name.trim();
                            prePage.data.microservices[i].icon = data.icon;
                            break;
                        }
                    }

                    prePage.setData({
                        microservices: prePage.data.microservices,
                    });
                    wx.navigateBack();
                });
            });
        } else {
            //新微服务信息,不能有同名的微服务
            console.log("执行保存新添加微服务信息操作");

            db.collection("microservices").where({
                name: data.name.trim(),
            }).count().then(res => {
                if (res.total == 0) {
                    db.collection('microservices').add({
                        data: {
                            name: data.name.trim(),
                            icon: data.icon,
                            desc: data.desc,
                            endpoint: data.endpoint,
                            creatorId: getApp().globalData.userId,
                            creatime: new Date(),
                        },
                    }).then(res => {
                        //将结果设置会上一个页面，进行返回。不要跳转。
                        var pages = getCurrentPages();
                        var prePage = pages[pages.length - 2];
                        var microservice = {};
                        microservice.id = res._id;
                        microservice.name = data.name.trim();
                        microservice.icon = data.icon;
                        //将新的类型插入到原有记录的首行
                        prePage.data.microservices.unshift(microservice);
                        prePage.setData({
                            microservices: prePage.data.microservices,
                        });
                        wx.navigateBack();
                    });
                } else {
                    wx.showToast({
                        title: '已存在相同名称的微服务，请重新输入名称',
                    })
                }
            })
        }
    },
})