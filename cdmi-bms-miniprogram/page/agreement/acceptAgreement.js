// page/agreement/acceptAgreement.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        buz_id: '',
        initiator_name: '',
        buz_name: '',
        distributor_id: '',
        distributor_type: '',
        distributor_name: '',
        distributor_mobile: '',
        ratio: '',
        startime: '', //协议生效时间
        endtime: '', //协议需要续签的时间
        content: '',
        creatime: '',
        status: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '请您签署分销协议',
        });
        
        var _this = this;
        //协议已存在，则显示协议内容
        if (options.id != null && options.id != '') {
            const db = wx.cloud.database();
            new Promise((resolve, reject) => {
                db.collection('agreements').doc(options.id).get().then(res => {
                    console.info(res.data);
                    if (res.data != null) {
                        new Promise((resolve, reject) => {
                            if (res.data.initiator_type == 'DEVELOPER') {
                                db.collection('developers').doc(res.data.initiator_id).get().then(developer => {
                                    if (developer != null) {
                                        resolve(developer.data.name);
                                    } else {
                                        wx.showToast({
                                            title: '没有找到协议邀约方信息',
                                        })
                                    }
                                });
                            } else {
                                wx.showToast({
                                    title: '没有找到协议邀约方信息',
                                })
                            }
                        }).then(name => {
                            _this.setData({
                                initiator_name: name,
                                id: res.data._id,
                                buz_id: res.data.buz_id,
                                buz_name: res.data.buz_name,
                                distributor_id: res.data.invitee_id,
                                distributor_type: res.data.invitee_type,
                                distributor_name: res.data.invitee_name,
                                ratio: res.data.ratio,
                                startime: res.data.startime, //协议生效时间
                                endtime: res.data.endtime, //协议需要续签的时间
                                content: res.data.content,
                                status: res.data.status,
                            });
                            resolve(res.data.invitee_id);
                        }).catch(ex => {
                            console.error(ex);
                        });
                        //   //获取渠道的等级信息
                        //   _this.setData({
                        //       distributor_level: '',
                        //   });

                    };
                }).catch(ex => {
                    console.error(ex);
                });
            }).then(invitee_id => {
                db.collection('distributors').doc(invitee_id).get().then(res => {
                    if (res.data != null) {
                        _this.setData({
                            distributor_mobile: res.data.linkmobile,
                        });
                    }
                });
            });

        } else {
            wx.showToast({
                title: '该链接缺少关键信息，请联系开发商',
            })
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
     * 同意该协议
     */
    accept: function() {
        //弹出手机验证码输入窗体。
        //用户输入手机验证码后，提交验证。
        //验证码通过，提交修改协议状态
        this.data.status = "OK";
        wx.navigateTo({
            url: 'verificationCode?action=accept&mobile=' + this.data.distributor_mobile,
        })
    },

    /**
     * 拒绝该协议
     */
    reject: function() {
        //弹出手机验证码输入窗体。
        //用户输入手机验证码后，提交验证。
        //验证码通过，提交修改协议状态
        this.data.status = "Rejected";
        wx.navigateTo({
            url: 'verificationCode?action=reject&mobile=' + this.data.distributor_mobile,
        })
    },

    /**
     * 更新协议状态
     */
    saveAgreement() {
        var _this = this;
        if (this.data.id == null || this.data.id == '') {
            wx.showToast({
                title: '未能找到协议信息，请返回重试',
            })
        }
        const db = wx.cloud.database();
        db.collection('agreements').doc(id).update({
            data: {
                status: _this.data.status,
            }
        });
        //跳转到签约成功，跳转小程序页面。
        wx.navigateTo({
            url: 'agreementResult?status=' + _this.data.status,
        })

    }
})