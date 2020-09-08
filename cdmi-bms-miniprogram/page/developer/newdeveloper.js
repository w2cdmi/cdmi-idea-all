// page/developer/newdeveloper.js
var config = require("../../commjs/config.js");
var httpClient = require("../../commjs/httpclient.js");

Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: "",
        developer_type: 'Person',
        name: '',
        licence: '',
        idcard: '',
        linkname: '',
        linkmobile: '',
        linkmail: '',
        cityId: '1130231',
        provinceId: '10721',
        accountId: '', //对应的平台用户账号
        completed: false //数据是否完整
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '新建开发者';
        if (options != null && options.title != null) {
            title = options.title;
        };
        wx.setNavigationBarTitle({
            title: title,
        });

        var _this = this;
        if (options != null && options.id != null) {
            this.data.id = options.id;
            const db = wx.cloud.database();
            db.collection('developers').doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '未找到指定的开发者信息',
                    });
                    return;
                } else {
                    _this.setData({
                        id: res.data._id,
                        developer_type: res.data.targetType,
                        name: res.data.name,
                        linkmobile: res.data.linkmobile,
                        linkmail: res.data.linkemail,
                        cityId: res.data.cityId,
                        provinceId: res.data.provinceId,
                        accountId: res.data.accountId, //对应的平台用户账号
                    });
                    if (res.data.targetType == "Enterprise") {
                        db.collection("companies").doc(res.data.targetId).get().then(company => {
                            if (company.data == null) {
                                wx.showToast({
                                    title: '未找到指定的开发者信息',
                                });
                                return;
                            } else {
                                _this.setData({
                                    licence: company.data.licence,
                                    linkname: company.data.linkname,
                                });
                            }
                        });
                    } else if (res.data.targetType == "Person") {
                        db.collection("persons").doc(res.data.targetId).get().then(person => {
                            if (person.data == null) {
                                wx.showToast({
                                    title: '未找到指定的开发者信息',
                                });
                                return;
                            } else {
                                _this.setData({
                                    idcard: person.data.idcard,
                                });
                            }
                        });
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
     * 选择开发者类型
     */
    selectDeveloperType: function() {
        wx.navigateTo({
            url: '/page/developer/developertype',
        })
    },

    /**
     * 设置开发者名称
     */
    setName: function(event) {
        this.setData({
            name: event.detail.value.trim(),
        })
    },

    /**
     * 选择工商所在地
     */
    selctedArea: function() {

    },

    /**
     * 设置企业营业证号码
     */
    setBusinessLicence: function(event) {
        this.setData({
            licence: event.detail.value.trim(),
        })
    },

    /**
     * 设置联系人姓名
     */
    setLinkName: function(event) {
        this.setData({
            linkname: event.detail.value.trim(),
        })
    },

    /**
     * 填写邮件地址
     */
    setEmail: function(event) {
        this.setData({
            linkmail: event.detail.value.trim(),
        })
    },

    /**
     * 填写电话号码
     */
    setPhone: function(event) {
        this.setData({
            linkmobile: event.detail.value.trim(),
        })
    },

    /**
     * 创建一个新的开发者信息
     */
    createDeveloper: function(e) {
        var data = e.detail.value;
        var _this = this;
        //新建
        if (this.data.id == null || this.data.id.trim() == ""){
            if (data.type == "Person") {
                if (data.name == null || data.name.trim() == '') {
                    wx.showToast({
                        title: '开发者名称不能为空',
                    });
                    return;
                }
                if (data.idcard == null || data.idcard.trim() == '') {
                    wx.showToast({
                        title: '需要填写开发者身份证号码',
                    });
                    return;
                }
                if (data.linkmobile == null || data.linkmobile.trim() == '') {
                    wx.showToast({
                        title: '需要填写开发者的联系电话',
                    });
                    return;
                }
                const db = wx.cloud.database();
                //检查个人信息是否已存在
                new Promise((resolve, reject) => {
                    db.collection('persons').where({
                        idcard: data.idcard.trim(),
                    }).get().then(res => {
                        if (res.data.length == 0) {
                            //不存在，则保存一份个人信息
                            db.collection('persons').add({
                                data: {
                                    name: data.name,
                                    cityId: data.cityId,
                                    provinceId: data.provinceId,
                                    idcard: data.idcard,
                                    mobile: data.linkmobile,
                                    mail: data.linkemail,
                                    accountId: getApp().globalData.userId,
                                    creatime: new Date(),
                                    updateime: new Date(),
                                },
                            }).then(res => {
                                resolve(res._id);
                            })
                        } else if (res.data.length == 1) {
                            //存在，则获取个人信息Id
                            resolve(res.data[0]._id);
                        } else {
                            wx.showToast({
                                title: '系统中存在多个身份证号一致的用户',
                            });
                            return;
                        }
                    }).catch(e => {
                        console.error(e);
                        wx.showToast({
                            title: '网络访问异常，请重试!',
                        });
                        return;
                    });
                }).then(res => {
                    //再保存开发者信息
                    db.collection('developers').add({
                        data: {
                            name: data.name,
                            cityId: data.cityId,
                            provinceId: data.provinceId,
                            linkmobile: data.linkmobile,
                            linkemail: data.linkemail,
                            accountId: getApp().globalData.userId,
                            targetId: res, //个人信息Id
                            targetType: data.type,
                            status: "enable",
                            creatime: new Date(),
                            updateime: new Date(),
                        },
                    }).then(res => {
                        db.collection("dev_members").add({
                            developerId: res._id,
                            name: data.name,
                            accountId: getApp().globalData.adminId,
                            accountType: 'Admin',
                            creatime: new Date(),
                            updateime: new Date(),
                        });
                        //数据保存成功跳转到开发者列表页
                        wx.navigateTo({
                            url: '/page/developer/developer',
                        });
                    })
                });
            } else if (data.type == "Enterprise") {
                if (data.name == null || data.name.trim() == '') {
                    wx.showToast({
                        title: '企业名称不能为空',
                    });
                    return;
                };
                if (data.licence == null || data.licence.trim() == '') {
                    wx.showToast({
                        title: '需要填写企业营业执照号',
                    });
                    return;
                };
                if (data.linkname == null || data.linkname.trim() == '') {
                    wx.showToast({
                        title: '需要填写企业联系人名称',
                    });
                    return;
                };
                if (data.linkmobile == null || data.linkmobile.trim() == '') {
                    wx.showToast({
                        title: '需要填写企业联系人的联系手机',
                    });
                    return;
                };
                const db = wx.cloud.database();
                //检查企业信息是否存在
                new Promise((resolve, reject) => {
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
                                    accountId: getApp().globalData.userId,
                                    status: "enable",
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
                                title: '系统中存在多个身份证号一致的用户',
                            });
                            return;
                        }
                    });
                }).then(res => {
                    //再保存开发者信息
                    db.collection('developers').add({
                        data: {
                            name: data.name,
                            cityId: data.cityId,
                            provinceId: data.provinceId,
                            linkmobile: data.linkmobile,
                            linkemail: data.linkemail,
                            accountId: getApp().globalData.userId,
                            targetId: res, //企业信息id
                            targetType: data.type,
                            status: "enable",
                            creatime: new Date(),
                            updateime: new Date(),
                        },
                    }).then(res => {
                        db.collection("dev_members").add({
                            developerId: res._id,
                            name: data.name,
                            accountId: getApp().globalData.adminId,
                            accountType: 'Admin',
                            creatime: new Date(),
                            updateime: new Date(),
                        });
                        //数据保存成功跳转到开发者列表页
                        wx.navigateTo({
                            url: '/page/developer/developer',
                        });
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
                    title: '未选择开发者类型',
                });
                return;
            }
        }else{
            //编辑，修改状态，不能修改开发者类型和开发者的证件号
            //TODO 未做输入值检验
            const db = wx.cloud.database();
            db.collection("developers").doc(_this.data.id).update({
                data:{
                    name: data.name.trim(),
                    linkmobile: data.linkmobile.trim(),
                    linkemail: data.linkemail,
                }
            }).then(res =>{
                //保存成功
                if(res.stats.updated == 1){
                    db.collection("dev_members").add({
                        developerId: _this.data.id,
                        name: data.name,
                        accountId: getApp().globalData.adminId,
                        accountType: 'Admin',
                        creatime: new Date(),
                        updateime: new Date(),
                    })
                    //数据保存成功跳转到开发者列表页
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length -2];
                    for (var i = 0; i < prePage.data.developers.length; i++) {
                        if (prePage.data.developers[i].id == _this.data.id) {
                            prePage.data.developers[i].name = data.name.trim();
                            break;
                        }
                    }

                    prePage.setData({
                        developers: prePage.data.developers,
                    });
                    wx.navigateBack();
                }
            });
        }
    

        // httpClient.post({
        //     url: config.HOST + config.HTTP_NEW_DEVELOPER,
        //     data: e.detail.value
        // }).then((res) => {
        //     if (res == false) {
        //         wx.navigateTo({
        //             url: '/page/developer/newdeveloper',
        //         });
        //     }
        // }, (result) => { //失败后执行
        //     wx.showToast({
        //         title: result.data.message,
        //         icon: 'error'
        //     })
        // });
        // console.log('form发生了submit事件，携带数据为：', e.detail.value)
    },


})