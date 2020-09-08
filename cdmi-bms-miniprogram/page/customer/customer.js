// page/customer/customer.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        customers: [],
        monthCustomerCount: 0,
        totalCustomerCount: 0,
        monthGrowth: 0,
        dayGrowth:0,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '客户管理',
        });

        var _this = this;
        if (getApp().globalData.developerId == null || getApp().globalData.developerId == '') {
            wx.showToast({
                title: '页面未能获取开发商信息，请联系开发商',
            })
            return;
        };
        //获取当前开发者所拥有的客户
        const db = wx.cloud.database();
        const _ = db.command;
        var now = new Date();
        var yesterday_milliseconds = now.getTime() - 1000 * 60 * 60 * 24;
        var yesterday = new Date();
        yesterday.setTime(yesterday_milliseconds);

        db.collection('a_customers').where({
            ownerId: getApp().globalData.developerId,
            creatime: _.gte(yesterday),
        }).get().then(res => {
            if (res.data.length != 0) {
                Promise.all(res.data.map((item) => {
                    return new Promise((resolve, reject) => {
                        var customer = {};
                        customer.id = item._id;
                        customer.name = item.name;
                        customer.type = item.type;
                        customer.status = item.status;
                        customer.catalog = {};
                        customer.catalog.id = item.catalogId;
                        customer.creatime = item.creatime;
                        customer.level = {};

                        if (item.catalogId != null && item.catalogId != '') {
                            _this.getCustomerCatalog(db, item.catalogId).then(catalog => {
                                customer.catalog = catalog;
                                if (item.levelId != null && item.levelId != '') {
                                    _this.getCustomerLevel(db, item.levelId).then(level => {
                                        customer.level = level
                                    });
                                    resolve(customer);
                                } else {
                                    resolve(customer);
                                }
                            });
                        } else {
                            if (item.levelId != null && item.levelId != '') {
                                _this.getCustomerLevel(db, item.levelId).then(level => {
                                    customer.level = level
                                });
                                resolve(customer);
                            } else {
                                resolve(customer);
                            }
                        }
                    });
                })).then(result => {
                    _this.setData({
                        customers: result,
                    })
                }).catch(e => {
                    console.error(e);
                });
            }
        });

        //历史总客户数
        db.collection('a_customers').where({
            ownerId: getApp().globalData.developerId,
        }).count().then(res => {
            this.setData({
                totalCustomerCount: res.total,
            });
        });

        //获取月上个月的新增客户
        var lastmonthStartDate = new Date(now.getFullYear(), now.getMonth() - 1, 1);
        var monthStartDate = new Date(now.getFullYear(), now.getMonth(), 1);
        db.collection('a_customers').where({
            ownerId: getApp().globalData.developerId,
            creatime: _.gte(lastmonthStartDate).and(_.lt(monthStartDate)),
        }).count().then(res => {
            //获取本月新增客户
            var lastCount = res.total;
            var currentCount = 0;
            db.collection('a_customers').where({
                ownerId: getApp().globalData.developerId,
                creatime: _.gte(monthStartDate),
            }).count().then(res => {
                currentCount = res.total;
                _this.setData({
                    monthCustomerCount: currentCount,
                    monthGrowth: currentCount - lastCount,
                });
            }).catch(res => {
                _this.setData({
                    monthCustomerCount: currentCount,
                    monthGrowth: currentCount - lastCount,
                });
            });
        });
    },

    /**
     * 内部方法，根据catalogId获取客户分类信息
     */
    getCustomerCatalog: function(db, catalogId) {
        return new Promise((resolve, reject) => {
            var catalog = {};
            db.collection('a_customer_types').doc(catalogId).get().then(res => {
                if (res.data != null) {
                    catalog.title = res.data.title;
                    catalog.id = res.data._id;
                }
                resolve(catalog);
            }).catch(ex => {
                resolve(catalog);
            });
        });
    },

    /**
     * 内部方法，根据levelId获取客户分类信息
     */
    getCustomerLevel: function(db, levelId) {
        return new Promise((resolve, reject) => {
            var level = {};
            db.collection('a_customer_levels').doc(levelId).get().then(res => {
                if (res.data != null) {
                    level.title = res.data.title;
                    level.id = res.data._id;
                }
                resolve(level);
            }).catch(ex => {
                resolve(level);
            });
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

    createCustomer: function(e) {
        wx.navigateTo({
            url: 'editcustomer?action=new',
        })
    },

    navigateToCustomerDetail: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'editcustomer?id=' + dataset.id,
        })
    }
})