// console/school/trainplace/editshop.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        shop_type: 'Person',
        name: '',
        license: '',
        linkname: '',
        linkmobile: '',
        linkmail: '',
        branches: [], //网点
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var title = '新增培训机构';
        if (options.id != null && options.id != '') {
            title = '编辑机构信息';
        }
        wx.setNavigationBarTitle({
            title: title,
        });
        var _this = this;
        const db = wx.cloud.database();
        if (options.id != null && options.id != '') {
            db.collection("a_shops").doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.redirectTo({
                        url: '/page/error?content=没有找到指定的机构信息',
                    });
                    return;
                }
                var shop = {
                    id: res.data._id,
                    name: res.data.name,
                    cityId: res.data.cityId,
                    provinceId: res.data.provinceId,
                    linkname: res.data.linkname,
                    linkmobile: res.data.linkmobile,
                    linkemail: res.data.linkemail,
                    status: "unactive", //未激活的
                };

                new Promise((resolve, reject) => {
                    if (res.data.targetType == 'Enterprise') {
                        db.collection("companies").doc(res.data.targetId).get().then(company => {
                            var licence = null;
                            if (company.data != null) {
                                licence = company.data.licence;
                            }
                            resolve(licence);
                        });
                    } else if (res.data.targetType == 'Person') {
                        db.collection("persons").doc(res.data.targetId).get().then(person => {
                            var idcard = null;
                            if (person.data != null) {
                                idcard = person.data.idcard;
                            }
                            resolve(idcard);
                        });
                    } else {
                        resolve("");
                    }
                }).then(target => {
                    if (res.data.targetType == 'Enterprise') {
                        shop.shop_type = res.data.targetType;
                        shop.licence = target;
                    } else if (res.data.targetType == 'Person') {
                        shop.shop_type = res.data.targetType;
                        shop.idcard = target;
                    }
                    _this.setData(shop);
                });
            });
        }
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
     * 选择培训机构的类型
     */
    selectShopType: function() {
        wx.navigateTo({
            url: 'shoptype',
        })
    },

    /**
     * 保存新建的分销商渠道
     */
    saveShop: function(e) {
        var _this = this;
        console.log('form发生了submit事件，携带数据为：', e.detail.value);
        var data = e.detail.value;

        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '培训机构名称不能为空',
            });
            return;
        };
        if (data.licence == null || data.licence.trim() == '') {
            wx.showToast({
                title: '需要填写培训机构营业执照号',
            });
            return;
        };
        if (data.linkname == null || data.linkname.trim() == '') {
            wx.showToast({
                title: '需要填写培训机构联系人名称',
            });
            return;
        };
        if (data.linkmobile == null || data.linkmobile.trim() == '') {
            wx.showToast({
                title: '需要填写培训机构联系人的联系手机',
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
            //再保存培训机构信息
            db.collection('a_shops').add({
                data: {
                    name: data.name,
                    licence: data.licence,
                    cityId: data.cityId,
                    provinceId: data.provinceId,
                    linkname: data.linkname,
                    linkmobile: data.linkmobile,
                    linkemail: data.linkemail,
                    appId: getApp().globalData.consoleAppId,
                    marker: '培训机构',
                    targetId: res, //企业信息id
                    targetType: data.type,
                    status: "unactive", //未激活的
                },
            }).then(res => {
                //数据保存成功跳转到培训机构列表页
                wx.navigateTo({
                    url: 'shopes',
                });
            })
        }).catch(e => {
            console.error(e);
            wx.showToast({
                title: '网络访问异常，请重试!',
            });
            return;
        });
    },

    /**
     * 跳转到网点列表页
     */
    navToplace: function() {
        wx.navigateTo({
            url: 'trainplace',
        })
    }
})