// page/distributor/agreement.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id:'',
        buz_id: '',
        buz_name: '',
        distributor_id: '',
        distributor_type: '',
        distributor_name: '',
        distributor_level: '',
        ratio: '',
        startime:new Date(),                                        //协议生效时间
        endtime: new Date().setFullYear(new Date().getFullYear() + 1),     //协议需要续签的时间
        content:'',                                                 //协议明细内容

    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var _this = this;
        wx.setNavigationBarTitle({
            title: '签署业务代理分销协议',
        });

        //协议已存在，则显示协议内容
        if (options.id != null && options.id !=''){
            const db = wx.cloud.database();
            db.collection('agreements').doc(options.id).get().then(res => {
                console.info(res.data);
                if (res.data != null) {
                    _this.setData({
                        id: res.data._id,
                        buz_id: res.data.buz_id,
                        buz_name: res.data.buz_name,
                        distributor_id: res.data.invitee_id,
                        distributor_type: res.data.invitee_type,
                        distributor_name: res.data.invitee_name,
                        ratio: res.data.ratio,
                        startime: res.data.startime,                                        //协议生效时间
                        endtime: res.data.endtime,                                          //协议需要续签的时间
                        content: res.data.content,
                        creatime: res.data.creatime,
                        status: res.data.status,
                    });

                    //获取渠道的等级信息
                    _this.setData({
                        distributor_level: '',
                    });
                };
            });
            return;
        }
        if (options.distributorid == null || options.buzid == null) {
            wx.showToast({
                title: '页面存在数据错误，请联系开发商',
            })
        } else {
            _this.data.buz_id = options.buzid.trim();
            _this.data.distributor_id = options.distributorid.trim();
        }

        //获取指定的业务信息
        const db = wx.cloud.database();
        db.collection('buz_items').doc(_this.data.buz_id).get().then(res => {
            if (res.data == null) {
                wx.showToast({
                    title: '未获得要签署的业务信息',
                });
                wx.navigateBack();
            } else {
                _this.setData({
                    buz_name: res.data.name,
                });
            }
        });
        //获取指定的渠道商信息
        db.collection('distributors').doc(_this.data.distributor_id).get().then(res => {
            if (res.data == null) {
                wx.showToast({
                    title: '未获得要签署的渠道商信息',
                });
                wx.navigateBack();
            } else {
                _this.setData({
                    distributor_name: res.data.name,
                    distributor_type: res.data.targetType,
                });

                if (res.data.level != null) {
                    db.collection('distributor_levels').doc(res.data.level).get().then(res => {
                        if (res.data != null) {
                            _this.setData({
                                distributor_level: res.data.title,
                                ratio: res.data.ratio,
                            });
                        }
                    });
                }
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
        var _this = this;
        new Promise((resolve, reject) => {
            //保存拟定的合同信息
            const db = wx.cloud.database();
            if (_this.data.id == ''){
                db.collection('agreements').add({
                    data:{
                        buz_id: _this.data.buz_id,
                        buz_name: _this.data.buz_name,
                        invitee_id: _this.data.distributor_id,                               //受邀约方的Id
                        invitee_name: _this.data.distributor_name,                           //受邀约方的名称
                        invitee_type:"DISTRIBUTOR",
                        ratio: _this.data.ratio,
                        startime: _this.data.startime,                                        //协议生效时间
                        endtime: _this.data.endtime,                                          //协议需要续签的时间
                        content: _this.data.content,                                          //协议明细内容
                        creatime: new Date(),
                        initiator_id: getApp().globalData.developerId,                        //协议发起人
                        accountId: getApp().globalData.userId,
                        initiator_type:"DEVELOPER",                                           //发起人类型
                        status:'ToBeSigned',                                                  //待签署
                    }
                }).then(res =>{
                    _this.setData({
                        id : res._id,       //保存，以防多次邀请
                    });
                });
            }
            resolve();
        }).then(res => {
            //生成邀请签约的图片，图片上包含邀约发起人，合同计划生效时间
            console.info("生成图片");
            var timer = setTimeout(function () {
                console.log("----Countdown----");
            }, 5000);
            console.info("打开转发");
        });
        return {
            title: '张三邀请您签署分销协议',
            path: '/page/agreement/acceptAgreement?id=' + _this.data.id,
            imageUrl: '/images/QRCode.jpg',
        };
    },
})