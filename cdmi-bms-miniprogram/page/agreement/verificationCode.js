// page/agreement/verificationCode.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        accountId: '', //获得的用户账号信息
        accountType: '', //获取的用户账号类型
        action: '', //操作
        mobile: '',
        code: '', //返回的验证码

        number1: {
            focus: true,
            value: "",
            cursor: -1,
        },
        number2: {
            focus: false,
            value: "",
            cursor: -1,
        },
        number3: {
            focus: false,
            value: "",
            cursor: -1,
        },
        number4: {
            focus: false,
            value: "",
            cursor: -1,
        }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        if (options.mobile == null || options.mobile == "") {
            wx.showToast({
                title: '未获取到验证手机号码',
            });
            return;
        } else {
            //国内手机
            if (options.mobile.substring(0, 1) != "+") {
                this.data.mobile = "+86" + options.mobile;
            } else {
                this.data.mobile = options.mobile;
            }
        };
        if (options.action == null || options.action == "") {
            wx.showToast({
                title: '未获取到用户的操作',
            });
            return;
        } else {
            this.data.action = options.action;
        };

        //
        console.info(options);
        var _this = this;
        new Promise((resolve, reject) => {
            //如果当前无管理员，将当前用户设置为管理员,但需先获取accountId。
            //先从后台获取用户的openid
            wx.cloud.callFunction({
                name: 'getUserInfo',
            }).then(res => {
                console.info(res);
                const db = wx.cloud.database();
                //查找当前用户的openId是否在wxusers中存在，不存在，则增加一个微信用户
                getApp().globalData.openId = res.result.openId;
                db.collection('wxusers').where({
                    _openid: res.result.openId
                }).get().then(res => {
                    if (res.data.length == 0) {
                        //如果wxusers不存在，先创建应用系统账号
                        db.collection('app_accounts').add({
                            // 应用账号不存在，则添加一个应用账号
                            data: {
                                name: getApp().globalData.userName,
                                headImage: getApp().globalData.headImage,
                                creatime: new Date(),
                                updateime: new Date(),
                                status: "enable",
                                appId: getApp().globalData.distributorsAppId,
                                platform: "WEIXIN", //账号从哪里进行注册的
                            }
                        }).then(res => {
                            console.info("appId:" + getApp().globalData.distributorsAppId);
                            //再创建wxuser账号
                            accountId = res._id;
                            db.collection('wxusers').add({
                                data: {
                                    accountId: res._id,
                                    accountType: "APP_ACCOUNT",
                                    name: getApp().globalData.userName,
                                    headImage: getApp().globalData.headImage,
                                    creatime: new Date(),
                                }
                            }).catch(ex => {
                                console.error(ex);
                            })
                            resolve({
                                accountId: accountId,
                                accountType: 'APP_ACCOUNT',
                            });
                        }).catch(ex => {
                            console.error(ex);
                        });
                    } else if (res.data.length == 1) {
                        console.log("已有微信，再检查是否有应用用户账号");
                        var accountId = res.data[0].accountId;
                        console.info(res.data[0]);
                        //已有微信号，再检查是否具有应用用户账号
                        if ('SYS_ACCOUNT'== res.data[0].accountType){
                            db.collection('app_accounts').where({
                                accountId: accountId,
                                accountType: 'SYS_ACCOUNT',
                                appId: getApp().globalData.distributorsAppId,
                            }).get().then(res => {
                                if(res.data.length == 0){
                                    //该微信用户尚未创建应用用户账号，创建之
                                    console.info(res.data.length);
                                    db.collection('app_accounts').add({
                                        // 应用账号不存在，则添加一个应用账号
                                        data: {
                                            name: getApp().globalData.userName,
                                            headImage: getApp().globalData.headImage,
                                            creatime: new Date(),
                                            updateime: new Date(),
                                            status: "enable",
                                            accountId: accountId,
                                            accountType: 'SYS_ACCOUNT',                                     
                                            appId: getApp().globalData.distributorsAppId,
                                            platform: "WEIXIN", //账号从哪里进行注册的
                                        }
                                    }).then(res => {
                                        resolve({
                                            accountId: res._id,
                                            accountType: 'APP_ACCOUNT',
                                        });
                                    });
                                } else if (res.data.length == 1){
                                    resolve({
                                        accountId: res.data[0]._id,
                                        accountType: 'SYS_ACCOUNT',
                                    });
                                }else{
                                    console.error('在应用用户表中同一应用下存在多个相同系统用户账号的数据，与设计不符');
                                    wx.showToast({
                                        title: '系统存在重复的数据，请联系开发商',
                                    })
                                }
                            })
                        }else{
                            wx.showToast({
                                title: '该用户操作不被支持，请联系开发商',
                            })
                        }
                    }else{
                        console.error('在微信用户表中存在多个相同OpenId的数据，与设计不符');
                        wx.showToast({
                            title: '系统存在重复的数据，与设计不符，请联系开发商',
                        })       
                    }
                })
            }).catch(ex => {
                console.error(ex);
            })
        }).then(res => {
            _this.data.accountId = res.accountId;
            _this.data.accountType = res.accountType;
            console.info(_this.data);
            //发送一条短信
            wx.cloud.callFunction({
                name: 'sendSmsCode',
                data: {
                    accountId: _this.data.accountId,
                    accountType: _this.data.accountType,
                    mobile: _this.data.mobile,
                }
            }).then(res => {
                console.log("远程调用发送短信接口后的返回结果：");
                console.log(res);
                if (res != null && res.result.status == 0) {
                    _this.data.code = res.result.value;
                } else {
                    wx.showToast({
                        title: '短信发送失败，请联系开发商',
                    })
                }
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

    inputVerificationCode: function(e) {
        console.info(e.detail.value);
        console.info("keyCode :" + e.detail.keyCode);
        const dataset = e.currentTarget.dataset;
        if (e.detail.value != '') {
            if (dataset.order == 1) {
                this.setData({
                    'number1.focus': false,
                    'number1.value': e.detail.value,
                    'number2.focus': true,
                    'number2.cursor': -1,
                })
            } else if (dataset.order == 2) {
                this.setData({
                    'number2.focus': false,
                    'number2.value': e.detail.value,
                    'number3.focus': true,
                    'number3.cursor': -1,
                })
            } else if (dataset.order == 3) {
                this.setData({
                    'number3.focus': false,
                    'number3.value': e.detail.value,
                    'number4.focus': true,
                    'number4.cursor': -1,
                })
            } else {
                this.setData({
                    'number4.focus': false,
                    'number4.value': e.detail.value,
                    'number4.cursor': -1,
                });
            };
            //检查是否存在空项
            if (this.data.number1.value == '') {
                this.setData({
                    'number1.focus': true,
                })
            } else if (this.data.number2.value == '') {
                this.setData({
                    'number2.focus': true,
                })
            } else if (this.data.number3.value == '') {
                this.setData({
                    'number3.focus': true,
                })
            } else if (this.data.number4.value == '') {
                this.setData({
                    'number4.focus': true,
                })
            } else {
                //提交验证码
                var code = this.data.number1.value + this.data.number2.value + this.data.number3.value + this.data.number4.value
                if (code == this.data.code) {
                    //验证通过，调用上一页接口，保存协议状况
                    var pages = getCurrentPages();
                    var lastpage = pages[pages.length - 2];
                    lastpage.saveAgreement();
                } else {
                    //验证不通过，重新输入，从第四个数字框开始
                    this.setData({
                        'number4.focus': true,
                        'number4.cursor': -1,
                    })
                }
            }
        } else {
            if (e.detail.keyCode == 8) { //点后退键, 光标迁移
                if (dataset.order == 2) {
                    this.setData({
                        'number1.focus': true,
                        'number1.cursor': -1,
                        'number2.value': '',
                    })
                } else if (dataset.order == 3) {
                    this.setData({
                        'number2.focus': true,
                        'number2.cursor': -1,
                        'number3.value': '',
                    })
                } else if (dataset.order == 4) {
                    this.setData({
                        'number3.focus': true,
                        'number3.cursor': -1,
                        'number4.value': '',
                    })
                } else {
                    this.setData({
                        'number1.value': '',
                    })
                }
            }
        }
    },

    getFocus: function(e) {
        const dataset = e.currentTarget.dataset;

        if (dataset.order == 1) {
            this.setData({
                'number1.focus': true,
                'number1.cursor': -1,
            })
        } else if (dataset.order == 2) {
            this.setData({
                'number2.focus': true,
                'number2.cursor': -1,
            })
        } else if (dataset.order == 3) {
            this.setData({
                'number3.focus': true,
                'number3.cursor': -1,
            })
        } else if (dataset.order == 4) {
            this.setData({
                'number4.focus': true,
                'number4.cursor': -1,
            })
        }
        console.info(this.data);
    }
})