// page/distributor/newdistributorlevel.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: "",
        title: "",
        ratio: "",
        catalog:{},
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '渠道商等级';
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

        var _this = this;
        const db = wx.cloud.database();
        //获取分销商分类信息
        if (options != null && options.catalogid != null) {
            db.collection("distributor_cataloies").doc(options.catalogid).get().then(res => {
                if(res.data != null){
                    var catalog = {};
                    catalog.id = options.catalogid;
                    catalog.title = res.data.name; 
                   _this.setData({
                       catalog: catalog,
                   }); 
                }
            });
        };
        console.info(options);
        if (options != null && options.id != null) {
            //检查页面编码，同一个开发者下不能有相同的渠道等级编码
            db.collection("distributor_levels").doc(options.id).get().then(res => {
                if (res.data != null) {
                    _this.setData({
                        id: res.data._id,
                        title: res.data.title,
                        ratio: res.data.ratio,
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
     * 删除指定的渠道等级
     */
    deleteDistributorLevel: function(){
        var _this = this;
        if (_this.data.id == null || _this.data.id == '') {
            wx.showToast({
                title: '页面数据存在错误',
            });
            return;
        };
        const db = wx.cloud.database();
        //TODO 需检查是否存在数据引用，如果存在，则不允许删除。
        db.collection("distributor_levels").doc(_this.data.id).remove().then(res =>{
            wx.navigateBack();
        })
    },

    createDistributorLevel: function(e) {
        var _this = this;
        console.log('form发生了submit事件，携带数据为：', e.detail.value);
        var data = e.detail.value;
        if (data.title == null || data.title.trim() == '') {
            wx.showToast({
                title: '渠道等级名称不能为空',
            });
            return;
        };
        if (data.ratio == null || data.ratio.trim() == '') {
            wx.showToast({
                title: '该渠道等级的默认的提成比例未设置',
            });
            return;
        };
        const db = wx.cloud.database();
        if (_this.data.id == null || _this.data.id == ''){
            //新建 检查页面编码，同一个开发者下不能有相同的渠道等级编码
            db.collection("distributor_levels").where({
                developerId: getApp().globalData.developerId,
                title: data.title.trim(),
            }).count().then(res => {
                if (res.total != 0) {
                    wx.showToast({
                        title: '您已创建有相同的渠道等级，请重新输入名称'
                    });
                    return;
                }
                db.collection('distributor_levels').add({
                    data: {
                        title: data.title.trim(),
                        ratio: data.ratio.trim(),
                        ownerId: getApp().globalData.developerId,
                        accountId: getApp().globalData.userId,
                        catalogId: _this.data.catalog.id,
                        creatime: new Date(),
                    },
                }).then(res => {
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    var level = {};
                    level.id = res._id;
                    level.title = data.title.trim();
                    level.ratio = data.ratio.trim();
                    level.catalogId = _this.data.catalog.id;
                    //将新的类型插入到原有记录的首行
                    prePage.data.levels.unshift(level);
                    prePage.setData({
                        levels: prePage.data.levels,
                    });

                    wx.navigateBack();
                });
            });
        }else{
            //编辑, 检查修改后的等级是否和其他渠道等级名称相同
            const _ = db.command;
            db.collection("distributor_levels").where({
                developerId: getApp().globalData.developerId,
                title: data.title.trim(),
                _id: _.neq(_this.data.id),
            }).count().then(res => {
                if (res.total != 0) {
                    wx.showToast({
                        title: '您已创建有相同的渠道等级，请重新输入名称'
                    });
                    return;
                }
                db.collection('distributor_levels').doc(_this.data.id).update({
                    data: {
                        title: data.title.trim(),
                        ratio: data.ratio.trim(),
                        catalogId: _this.data.catalog.id,
                    },
                }).then(res => {
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    for (var i = 0; i < prePage.data.levels.length; i++) {
                        if (prePage.data.levels[i].id == _this.data.id) {
                            prePage.data.levels[i].title = data.title.trim();
                            prePage.data.levels[i].ratio = data.ratio.trim();
                            prePage.data.levels[i].catalogId = _this.data.catalog.id;
                            break;
                        }
                    }
                    prePage.setData({
                        levels: prePage.data.levels,
                    });
                    wx.navigateBack();
                });
            });
        }
    }
})