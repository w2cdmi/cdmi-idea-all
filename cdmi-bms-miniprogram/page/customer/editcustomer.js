// page/customer/editcustomer.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        customer_type: 'Enterprise',
        level: '',
        distributor: {}, //邀请渠道
        name: '',
        linkname: '',
        linkmobile: '',
        catalog: {}, //对应的客户分类信息
        level: {}, //对应的客户级别
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '客户信息';
        if (options != null && options.title != null) {
            title = options.title;
        };
        if (options.action == 'new') {
            title = "新建客户信息"
        } else if (options.action == 'edit') {
            title = "编辑客户信息"
        }
        wx.setNavigationBarTitle({
            title: title,
        });

        var _this = this;
        console.info(options.id);
        if (options != null && options.id != null) {
            const db = wx.cloud.database();
            //获取客户详细信息
            db.collection("a_customers").doc(options.id).get().then(res => {
                if (res.data != null) {
                    var customer = {
                        id: res.data._id,
                        customer_type: res.data.ctype,
                        name: res.data.name,
                        linkmobile: res.data.linkmobile,
                    };
                    if (res.data.ctype == 'Enterprise') {
                        customer.licence = res.data.licence;
                        customer.linkname = res.data.linkname;
                    } else if (res.data.ctype == 'Person') {
                        customer.idcard = res.data.idcard;
                    };
                    customer.catalog = {};
                    customer.level = {};
                    if (res.data.catalogId != null && res.data.catalogId != '') {
                        _this.getCustomerCatalog(db, res.data.catalogId).then(catalog => {
                            customer.catalog = catalog;
                            if (res.data.levelId != null && res.data.levelId != '') {
                                _this.getCustomerLevel(db, res.data.levelId).then(level => {
                                    customer.level = level;
                                    _this.setData(customer);
                                }).catch(ex => {
                                    _this.setData(customer);
                                });
                            }else{
                                _this.setData(customer); 
                            }
                        }).catch(ex => {
                            _this.setData(customer);
                        });
                    } else {
                        if (res.data.levelId != null && res.data.levelId != '') {
                            _this.getCustomerLevel(db, res.data.levelId).then(level => {
                                customer.level = level;
                                _this.setData(customer);
                            }).catch( ex => {
                                _this.setData(customer);
                            });
                        }else{
                            _this.setData(customer);
                        }
                    }
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
     * 选择客户类型
     */
    selectCustomerType: function() {
        wx.navigateTo({
            url: '/page/customer/customertype',
        })
    },

    /**
     * 选择客户分类
     */
    selectCustomerCatalog: function() {
        wx.navigateTo({
            url: '/page/customer/customercatalog?scene=options',
        })
    },

    /**
     * 选择客户的级别
     */
    selectCustomerLevel: function() {
        wx.navigateTo({
            url: '/page/customer/customerlevel',
        })
    },

    /**
     * 保存新建的客户信息
     */
    createCustomer: function(e) {
        var _this = this;
        var data = e.detail.value;

        if (_this.data.id != null && _this.data.id != '') {
            this.editCustomer(data);
            return;
        }

        if (_this.data.customer_type == "Person") {
            if (data.name == null || data.name.trim() == '') {
                wx.showToast({
                    title: '客户名称不能为空',
                });
                return;
            }
            if (data.linkmobile == null || data.linkmobile.trim() == '') {
                wx.showToast({
                    title: '需要填写个人客户的联系电话',
                });
                return;
            }
            const db = wx.cloud.database();
            //检查用户的身份信息是否已存在，存在，则不保存
            new Promise((resolve, reject) => {
                if (data.idcord != null && data.idcord != '') {
                    db.collection('persons').where({
                        idcard: data.idcard.trim(),
                    }).get().then(res => {
                        if (res.data.length == 0) {
                            //不存在，则创建个人信息
                            db.collection('persons').add({
                                data: {
                                    name: data.name,
                                    cityId: data.cityId,
                                    provinceId: data.provinceId,
                                    idcard: data.idcard,
                                    mobile: data.linkmobile,
                                    mail: data.linkemail,
                                    creatime: new Date(),
                                    updateime: new Date(),
                                },
                            }).then(res => {
                                resolve(res._id);
                            })
                        } else if (res.data.length == 1) {
                            resolve(res.data[0]._id);
                        } else {
                            wx.showToast({
                                title: '系统中已存在多个该身份证号码的用户',
                            });
                            return;
                        }
                    });
                } else {
                    resolve(null);
                }

            }).then(res => {
                //再保存个人客户信息
                var customer = {
                    name: data.name,
                    idcard: data.idcard,
                    cityId: data.cityId,
                    provinceId: data.provinceId,
                    linkmobile: data.linkmobile,
                    linkemail: data.linkemail,
                    catalogId: data.catalogId,
                    levelId: data.levelId,
                    ctype: _this.data.customer_type,
                    ownerId: getApp().globalData.developerId,
                    ownerType: "DEVELOPER",
                    status: "potential", //潜在顾客
                    creatorId: getApp().globalData.userId,
                    creatorType: 'SYS_ACCOUNT',
                    creatime: new Date(),
                };

                if (res != null) {
                    customer.targetId = res;
                    customer.targetType = data.type;
                };

                db.collection('a_customers').add({
                    data: customer,
                }).then(res => {
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    customer.id = res._id;
                    customer.catalog = {};
                    customer.catalog.id = customer.catalogId;
                    customer.level = {};
                    customer.level.id = customer.levelId;
                    _this.insertNewCatalogToPage(db, customer, prePage);
                    wx.navigateBack();
                })
            });
        } else if (data.type == "Enterprise") {
            if (data.name == null || data.name.trim() == '') {
                wx.showToast({
                    title: '企业客户名称不能为空',
                });
                return;
            };
            if (data.linkname == null || data.linkname.trim() == '') {
                wx.showToast({
                    title: '需要填写企业客户联系人名称',
                });
                return;
            };
            if (data.linkmobile == null || data.linkmobile.trim() == '') {
                wx.showToast({
                    title: '需要填写企业客户联系人的联系手机',
                });
                return;
            };
            const db = wx.cloud.database();
            //检查企业信息是否存在
            new Promise((resolve, reject) => {
                if (data.licence != null && data.licence != '') {
                    db.collection('companies').where({
                        licence: data.licence.trim(),
                    }).get().then(res => {
                        if (res.data.length == 0) {
                            //先保存企业信息
                            db.collection('companies').add({
                                data: {
                                    name: data.name,
                                    cityId: data.cityId,
                                    provinceId: data.provinceId,
                                    licence: data.licence,
                                    linkname: data.linkname,
                                    linkmobile: data.linkmobile,
                                    linkemail: data.linkemail,
                                    creatime: new Date(),
                                    updateime: new Date(),
                                },
                            }).then(res => {
                                resolve(res._id);
                            })
                        } else if (res.data.length == 1) {
                            resolve(res.data[0]._id);
                        } else {
                            wx.showToast({
                                title: '系统中存在多个营业执照一致的用户',
                            });
                            return;
                        }
                    });
                } else {
                    resolve(null);
                }
            }).then(res => {
                //再保存企业客户信息
                var customer = {
                    name: data.name,
                    ctype: _this.data.customer_type,
                    licence: data.licence,
                    cityId: data.cityId,
                    provinceId: data.provinceId,
                    linkname: data.linkname,
                    linkmobile: data.linkmobile,
                    linkemail: data.linkemail,
                    catalogId: data.catalogId,
                    levelId: data.levelId,
                    ownerId: getApp().globalData.developerId,
                    ownerType: "DEVELOPER",
                    status: "potential", //潜在顾客
                    creatorId: getApp().globalData.userId,
                    creatorType: 'SYS_ACCOUNT',
                    creatime: new Date(),
                };

                if (res != null) {
                    customer.targetId = res;
                    customer.targetType = data.type;
                }
                db.collection('a_customers').add({
                    data: customer,
                }).then(res => {
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    customer.id = res._id;
                    customer.catalog = {};
                    customer.catalog.id = customer.catalogId;
                    customer.level = {};
                    customer.level.id = customer.levelId;
                    _this.insertNewCatalogToPage(db, customer, prePage);
                    wx.navigateBack();
                })
            }).catch(e => {
                console.error(e);
                wx.showToast({
                    title: '网络访问异常，请重试!',
                });
                return;
            });
        } else {
            wx.showToast({
                title: '未选择客户类型',
            });
            return;
        }
    },

    /**
     * 内部方法，将新添加的记录插入到上一个列表页面
     */
    insertNewCatalogToPage: function (db, customer,prePage){
        if (customer.catalog.id != null && customer.catalog.id != '') {
            this.getCustomerCatalog(db, customer.catalog.id).then(catalog => {
                customer.catalog = catalog;
                if (customer.level.id != null && customer.level.id != '') {
                    this.getCustomerLevel(db, customer.level.id).then(level => {
                        customer.level = level
                    });
                }
            });
        } else {
            if (customer.level.id != null && customer.level.id != '') {
                this.getCustomerLevel(db, customer.level.id).then(level => {
                    customer.level = level
                });
            }
        }
        //将新的类型插入到原有记录的首行
        prePage.data.customers.unshift(customer);
        prePage.setData({
            customers: prePage.data.customers,
        });
    },

    /**
     * 内部方法，将新更新的记录更新到上一个列表页面
     */
    updateCatalogToPage: function (db, customer, prePage) {
        if (customer.catalog.id != null && customer.catalog.id != '') {
            this.getCustomerCatalog(db, customer.catalog.id).then(catalog => {
                customer.catalog = catalog;
                if (customer.level.id != null && customer.level.id != '') {
                    this.getCustomerLevel(db, customer.level.id).then(level => {
                        customer.level = level
                    });
                }
            });
        } else {
            if (customer.level.id != null && customer.level.id != '') {
                this.getCustomerLevel(db, customer.level.id).then(level => {
                    customer.level = level
                });
            }
        }
        //找到原有记录进行更新
        for (var i = 0; i < prePage.data.customers.length; i++) {
            if (prePage.data.customers[i].id == this.data.id) {
                prePage.data.customers[i] = customer;
                break;
            }
        }
        prePage.setData({
            customers: prePage.data.customers,
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
     * 保存编辑后的客户信息
     */
    editCustomer: function(data) {
        var _this = this;

        if (_this.data.customer_type == "Person") {
            if (data.name == null || data.name.trim() == '') {
                wx.showToast({
                    title: '客户名称不能为空',
                });
                return;
            }
            if (data.linkmobile == null || data.linkmobile.trim() == '') {
                wx.showToast({
                    title: '需要填写个人客户的联系电话',
                });
                return;
            }
            const db = wx.cloud.database();
            //检查用户的身份信息是否已存在，存在，则不保存
            new Promise((resolve, reject) => {
                if (data.idcord != null && data.idcord != '') {
                    db.collection('persons').where({
                        idcard: data.idcard.trim(),
                    }).get().then(res => {
                        if (res.data.length == 0) {
                            //不存在，则创建个人信息
                            db.collection('persons').add({
                                data: {
                                    name: data.name,
                                    cityId: data.cityId,
                                    provinceId: data.provinceId,
                                    idcard: data.idcard,
                                    mobile: data.linkmobile,
                                    mail: data.linkemail,
                                    creatime: new Date(),
                                    updateime: new Date(),
                                },
                            }).then(res => {
                                resolve(res._id);
                            })
                        } else if (res.data.length == 1) {
                            resolve(res.data[0]._id);
                        } else {
                            wx.showToast({
                                title: '系统中已存在多个该身份证号码的用户',
                            });
                            return;
                        }
                    });
                } else {
                    resolve(null);
                }

            }).then(res => {
                //再保存个人客户信息
                var customer = {
                    name: data.name,
                    idcard: data.idcard,
                    cityId: data.cityId,
                    provinceId: data.provinceId,
                    linkmobile: data.linkmobile,
                    linkemail: data.linkemail,
                    catalogId: data.catalogId,
                    levelId: data.levelId,
                    ctype: _this.data.customer_type,
                };

                if (res != null) {
                    customer.targetId = res;
                    customer.targetType = data.type;
                };

                db.collection('a_customers').doc(_this.data.id).update({
                    data: customer,
                }).then(res => {
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    customer.id = _this.data.id;
                    customer.catalog = {};
                    customer.catalog.id = customer.catalogId;
                    customer.level = {};
                    customer.level.id = customer.levelId;
                    _this.updateCatalogToPage(db, customer, prePage);
                    wx.navigateBack();
                })
            });
        } else if (data.type == "Enterprise") {
            if (data.name == null || data.name.trim() == '') {
                wx.showToast({
                    title: '企业客户名称不能为空',
                });
                return;
            };
            if (data.linkname == null || data.linkname.trim() == '') {
                wx.showToast({
                    title: '需要填写企业客户联系人名称',
                });
                return;
            };
            if (data.linkmobile == null || data.linkmobile.trim() == '') {
                wx.showToast({
                    title: '需要填写企业客户联系人的联系手机',
                });
                return;
            };
            const db = wx.cloud.database();
            //检查企业信息是否存在
            new Promise((resolve, reject) => {
                if (data.licence != null && data.licence != '') {
                    db.collection('companies').where({
                        licence: data.licence.trim(),
                    }).get().then(res => {
                        if (res.data.length == 0) {
                            //先保存企业信息
                            db.collection('companies').add({
                                data: {
                                    name: data.name,
                                    cityId: data.cityId,
                                    provinceId: data.provinceId,
                                    licence: data.licence,
                                    linkname: data.linkname,
                                    linkmobile: data.linkmobile,
                                    linkemail: data.linkemail,
                                    creatime: new Date(),
                                    updateime: new Date(),
                                },
                            }).then(res => {
                                resolve(res._id);
                            })
                        } else if (res.data.length == 1) {
                            resolve(res.data[0]._id);
                        } else {
                            wx.showToast({
                                title: '系统中存在多个营业执照一致的用户',
                            });
                            return;
                        }
                    });
                } else {
                    resolve(null);
                }
            }).then(res => {
                //再保存企业客户信息
                var customer = {
                    name: data.name,
                    ctype: _this.data.customer_type,
                    licence: data.licence,
                    cityId: data.cityId,
                    provinceId: data.provinceId,
                    linkname: data.linkname,
                    linkmobile: data.linkmobile,
                    linkemail: data.linkemail,
                    catalogId: data.catalogId,
                    levelId: data.levelId,
                };

                if (res != null) {
                    customer.targetId = res;
                    customer.targetType = data.type;
                }
                db.collection('a_customers').doc(_this.data.id).update({
                    data: customer,
                }).then(res => {
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    customer.id = _this.data.id;
                    customer.catalog = {};
                    customer.catalog.id = customer.catalogId;
                    customer.level = {};
                    customer.level.id = customer.levelId;
                    _this.updateCatalogToPage(db, customer, prePage);
                    wx.navigateBack();
                })
            }).catch(e => {
                console.error(e);
                wx.showToast({
                    title: '网络访问异常，请重试!',
                });
                return;
            });
        } else {
            wx.showToast({
                title: '未选择客户类型',
            });
            return;
        }
    }
})