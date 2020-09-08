// console/school/editcatalog.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        parent: {},
        id:'',
        name:'',
        icon:'',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        console.info(options);

        var title = '课程类目';
        if (options.action != null && options.action != '') {
            if (options.action == 'new') {
                title = "新建类目";
            } else if (options.action == 'edit') {
                title = "编辑类目";
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
        //获取显示上一级目录
        if (options.parentid != null && options.parentid.trim() != "") {
            db.collection("a_cataloies").doc(options.parentid).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '传入的父类目信息未找到，请退出后重试',
                    });
                }
                var parent = {};
                parent.id = res.data._id;
                parent.name = res.data.name;
                _this.setData({
                    parent: parent,
                });
            });
            return;
        }

        if (options.action == 'edit') {
            //编辑指定的类目
            if (options.id == null || options.id == '') {
                console.error("缺少必须的传入参数[id]，请联系开发商");
                return;
            }
            db.collection("a_cataloies").doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '传入的类目信息未找到，请退出后重试',
                    });
                }
                _this.setData({
                    id: res.data.id,
                    name:res.data.name,
                    icon:res.data.icon,
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
     * 保存栏目信息
     */
    saveCatalog: function(e) {
        var _this = this;
        var data = e.detail.value;
        console.info(data);
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '类目的名称不能为空',
            });
            return;
        };

        const db = wx.cloud.database();
        if(_this.data.id != null && _this.data.id != ''){
            console.log("执行编辑类目信息操作");
            //编辑类目信息
            db.collection("cataloies").doc(_this.data.id).update({
                data:{
                    name:data.name,
                }
            });
        }else{
            //保存类目信息，同一个应用下不能有同名的类目
            console.log("执行保存新类目信息操作");
            var parentId = '';
            if (_this.data.parent.id != null && _this.data.parent.id != ""){
                parentId = _this.data.parent.id;
            }

            db.collection("a_cataloies").where({
                appId: getApp().globalData.consoleAppId,
                name: data.name.trim(),
            }).count().then(res => {
                if (res.total == 0) {
                    db.collection('a_cataloies').add({
                        data: {
                            name: data.name.trim(),
                            icon: data.icon,
                            desc: data.desc,
                            parentId: parentId,  /**必须的，不然不能作为查询关键字 */
                            appId: getApp().globalData.consoleAppId,
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
                        catalog.childrenNumber = 0;
                        //将新的类型插入到原有记录的首行
                        prePage.data.cataloies.unshift(catalog);
                        prePage.setData({
                            cataloies: prePage.data.cataloies,
                        });
                        wx.navigateBack();
                    });
                } else {
                    wx.showToast({
                        title: '您已创建有同名的类目，请重新输入名称',
                    })
                }
            })
        }
    }
})