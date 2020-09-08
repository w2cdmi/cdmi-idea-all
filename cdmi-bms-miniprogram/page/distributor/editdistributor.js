// page/distributor/editdistributor.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        distributor_type: 'Enterprise',
        level: '',
        superior: {},
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '渠道商';
        if (options != null && options.title != null) {
            title = options.title;
        };
        wx.setNavigationBarTitle({
            title: options.action + title,
        })
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
     * 选择分销商渠道类型
     */
    selectDistributorType: function() {
        wx.navigateTo({
            url: '/page/distributor/distributortype',
        })
    },

    /**
     * 选择上级代理商
     */
    selectSuperior: function(e) {
        wx.navigateTo({
            url: '/page/distributor/superior',
        })
    },
    
    /**
     * 保存新建的分销商渠道
     */
    createDistributor: function(e) {
        var _this = this;
        console.log('form发生了submit事件，携带数据为：', e.detail.value);
        var data = e.detail.value;

        if (_this.data.distributor_type == "Person") {
            if (data.name == null || data.name.trim() == '') {
                wx.showToast({
                    title: '渠道商名称不能为空',
                });
                return;
            }
            if (data.idcard == null || data.idcard.trim() == '') {
                wx.showToast({
                    title: '需要填写个人渠道商身份证号码',
                });
                return;
            }
            if (data.linkmobile == null || data.linkmobile.trim() == '') {
                wx.showToast({
                    title: '需要填写个人渠道商的联系电话',
                });
                return;
            }
            const db = wx.cloud.database();
            //检查用户的身份信息是否已存在，存在，则不保存
            new Promise((resolve, reject) => {
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
            }).then(res => {
                //再保存个人渠道商信息
                db.collection('distributors').add({
                    data: {
                        name: data.name,
                        idcard: data.idcard,
                        cityId: data.cityId,
                        provinceId: data.provinceId,
                        linkmobile: data.linkmobile,
                        linkemail: data.linkemail,
                        ownerId: getApp().globalData.developerId,
                        level: data.level,
                        ownerType: "DEVELOPER",
                        targetId: res, //个人信息Id
                        targetType: data.type,
                        status: "unactive", //未激活的
                    },
                }).then(res => {
                    //数据保存成功跳转到开发者列表页
                    wx.navigateTo({
                        url: '/page/distributor/distributor',
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
            }).then(res => {
                //再保存企业渠道信息
                db.collection('distributors').add({
                    data: {
                        name: data.name,
                        licence: data.licence,
                        cityId: data.cityId,
                        provinceId: data.provinceId,
                        linkname: data.linkname,
                        linkmobile: data.linkmobile,
                        linkemail: data.linkemail,
                        level: data.level,
                        ownerId: getApp().globalData.developerId,
                        ownerType: "DEVELOPER",
                        targetId: res, //企业信息id
                        targetType: data.type,
                        status: "unactive", //未激活的
                    },
                }).then(res => {
                    //数据保存成功跳转到开发者列表页
                    wx.navigateTo({
                        url: '/page/distributor/distributor',
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
    }
})