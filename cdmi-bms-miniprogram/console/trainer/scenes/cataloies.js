// console/school/cataloies.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        parent: {},
        cataloies: [],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '类目管理',
        });
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

        console.log("获取当前目录信息:[options.parentid->" + options.parentid + "]");
        var parentId = '';
        if (options.parentid != null && options.parentid != '') {
            parentId = options.parentid;
            db.collection("a_cataloies").doc(options.parentid).get().then(res => {
                if (res.data != null) {
                    _this.data.parent.id = parentId;
                    _this.data.parent.name = res.data.name;
                    _this.setData({
                        parent: _this.data.parent,
                    });
                } else {
                    wx.showToast({
                        title: '父类目信息未找到，请退出后重试',
                    })
                }
            });
        };

        console.log("获取课程类目列表");
        db.collection('a_cataloies').where({
            appId: getApp().globalData.consoleAppId,
            parentId: parentId,
        }).get().then(res => {
            if (res.data.length != 0) {
                Promise.all(res.data.map((item) => {
                    return new Promise((resolve, reject) => {
                        db.collection('a_cataloies').where({
                            appId: getApp().globalData.consoleAppId,
                            parentId: item._id,
                        }).count().then(count => {
                            var catalog = {};
                            catalog.id = item._id;
                            catalog.name = item.name;
                            catalog.childrenNumber = count.total;
                            resolve(catalog);
                        });
                    });
                })).then(result => {
                    _this.setData({
                        cataloies: result,
                    });
                });
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

    /**
     * 创建一个新的类目
     */
    createCatalog: function(e) {
        if (this.data.parent.id == null || this.data.parent.id == "") {
            wx.navigateTo({
                url: 'editcatalog?action=new',
            })
        } else {
            wx.navigateTo({
                url: 'editcatalog?action=new&parentid=' + this.data.parent.id,
            })
        }
    },

    /**
     * 跳转到指定的类目详情以及子类目列表
     */
    jumpToCatalogDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        console.info(dataset);
        wx.navigateTo({
            url: 'cataloies?parentid=' + dataset.id,
        })
    },

    /**
     * 修改栏目详细信息
     */
    editCatalog: function(e) {
        wx.navigateTo({
            url: 'editcatalog?action=edit&id=' + dataset.id + '&parentid=' + this.data.parent.id,
        })
    },

    /**
     * 删除当前父目录
     */
    deleteCatalog:function(e){
        var _this = this;
        if (_this.data.parent.id == null || _this.data.parent.id == ""){
            wx.showToast({
                title: '未找到类目信息,请联系开发商',
            });
            return;
        };
        const db = wx.cloud.database();
        db.collection("a_cataloies").doc(_this.data.parent.id).remove().then(res =>{
            console.info(res.stats.removed);
            if (res.stats.removed == 1){
                //从上一页的data中删除
                var pages = getCurrentPages();
                var prePage = pages[pages.length - 2];
                var cataloies = prePage.data.cataloies;
                for (var i = 0; i < cataloies.length;i++){
                    if(cataloies[i].id == _this.data.parent.id){
                        cataloies.splice(i,1);
                        break;
                    }
                };
                prePage.setData({
                    cataloies: cataloies,
                });
            }
            wx.navigateBack();
        });
        
    }
})