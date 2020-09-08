// page/agreement/editagreementcataloies.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id:'',
        name:'',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '合约模板';
        if (options != null && options.title != null) {
            title = options.title;
        };
        if (options != null && options.action != null) {
            if (options.action.trim() == "new") {
                title = "新增" + title;
            }
        };
        wx.setNavigationBarTitle({
            title: title,
        });

        console.info(options);

        var _this = this;
        if (options != null && options.id != null) {
            const db = wx.cloud.database();
            db.collection("agreement_cataloies").doc(options.id).get().then(res => {
                if (res.data != null) {
                    _this.setData({
                        id: res.data._id,
                        name: res.data.name,
                        content: res.data.content,
                    })
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
     * 显示或编辑合约模板内容
     */
    viewContent:function(e){
        wx.navigateTo({
            url: 'agreementTemplateContent',
        })
    },
    /**
     * 删除指定的客户类型
     */
    deleteAgreementCatalog: function(e) {
        var _this = this;
        if (_this.data.id == null || _this.data.id == '') {
            wx.showToast({
                title: '页面数据存在错误',
            });
            return;
        };
        const db = wx.cloud.database();
        //TODO 需检查是否存在数据引用，如果存在，则不允许删除。
        db.collection("agreement_cataloies").doc(_this.data.id).remove().then(res => {
            if (res.stats.removed == 1) {
                //从上一页的data中删除
                var pages = getCurrentPages();
                var prePage = pages[pages.length - 2];
                var cataloies = prePage.data.cataloies;
                for (var i = 0; i < cataloies.length; i++) {
                    if (cataloies[i].id == _this.data.id) {
                        cataloies.splice(i, 1);
                        break;
                    }
                };
                prePage.setData({
                    cataloies: cataloies,
                });
            }
            wx.navigateBack();
        })
    },

    /**
     * 创建或者是编辑指定的合约分类
     */
    createAgreementCatalog: function(e) {
        var _this = this;
        var data = e.detail.value;
        console.info(data);

        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '合约模板名称不能为空',
            });
            return;
        };
        const db = wx.cloud.database();
        if (_this.data.id == null || _this.data.id == '') {
            //新建 检查页面编码，同一个开发者下不能有相同的合约类型名称
            db.collection("agreement_cataloies").where({
                ownerId: getApp().globalData.developerId,
                name: data.name.trim(),
            }).count().then(res => {
                if (res.total != 0) {
                    wx.showToast({
                        title: '您已创建有相同名称的合约模板，请重新输入名称'
                    });
                    return;
                }
                db.collection('agreement_cataloies').add({
                    data: {
                        name: data.name.trim(),
                        content: data.content,
                        ownerId: getApp().globalData.developerId,
                        accountId: getApp().globalData.userId,
                        creatime: new Date(),
                    },
                }).then(res => {
                    //将结果设置会上一个页面，进行返回。不要跳转。
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    var catalog = {};
                    catalog.id = res._id;
                    catalog.name = data.name.trim();
                    //将新的类型插入到原有记录的首行
                    prePage.data.cataloies.unshift(catalog);
                    prePage.setData({
                        cataloies: prePage.data.cataloies,
                    });
                    wx.navigateBack();
                });
            });
        } else {
            //编辑, 检查修改后的等级是否和其他渠道等级名称相同
            const _ = db.command;
            db.collection("agreement_cataloies").where({
                ownerId: getApp().globalData.developerId,
                name: data.name.trim(),
                _id: _.neq(_this.data.id),
            }).count().then(res => {
                if (res.total != 0) {
                    wx.showToast({
                        title: '您已创建有相同名称的合约模板，请重新输入名称'
                    });
                    return;
                }
                db.collection('agreement_cataloies').doc(_this.data.id).update({
                    data: {
                        name: data.name.trim(),
                        content: data.content,
                    },
                }).then(res => {
                    //将结果设置回上一个页面，进行返回。不要跳转。
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    for (var i = 0; i < prePage.data.cataloies.length; i++) {
                        if (prePage.data.cataloies[i].id == _this.data.id) {
                            prePage.data.cataloies[i].name = data.name.trim();
                            break;
                        }
                    }
                    prePage.setData({
                        cataloies: prePage.data.cataloies,
                    });

                    wx.navigateBack();
                });
            });
        }
    },
})