// page/distributor/distributordetail.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        name: '',
        idcard: '',     //个人才有
        licence:'',     //企业才有
        linkmobile: '',
        type: '',
        level_title: '',
        level_id:'',
        linkmail: '',
        superior:{},    //上级渠道信息 
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: "渠道商详情",
        });
        if (options.id == null || options.id.trim() == ""){
            wx.showToast({
                title: '页面存在数据错误，请联系开发商',
            })
        };

        var _this = this;
        //获取当前指定的渠道商信息
        const db = wx.cloud.database();
        db.collection('distributors').doc(options.id.trim()).get().then(res => {
            console.info(res.data);
            if(res.data == null){
                wx.showToast({
                    title: '未能找到指定的渠道信息',
                });
            }else{
                if (res.data.targetType == 'Person'){
                    new Promise((resolve, reject) => {
                        if (res.data.level != null) {
                            db.collection('distributor_levels').doc(res.data.level).get().then(res => {
                                if (res.data != null) {
                                    resolve(res.data.title);
                                }
                            });
                        }
                    }).then(level => {
                        this.setData({
                            id: res.data._id,
                            name: res.data.name,
                            idcard: res.data.idcard,
                            linkmobile: res.data.linkmobile,
                            type: res.data.targetType,
                            level_title: level,
                            level_id: res.data.level,
                            linkmail: res.data.linkmail,
                        });
                    });

                } else if (res.data.targetType == 'Enterprise'){
                    this.setData({
                        id: res.data._id,
                        name: res.data.name,
                        licence: res.data.licence,
                        linkname: res.data.linkname,
                        linkmobile: res.data.linkmobile,
                        type: res.data.targetType,
                        linkmail: res.data.linkmail,
                        level_id: res.data.level,
                    });
                    if (res.data.level != null && res.data.level != '') {
                        db.collection('distributor_levels').doc(res.data.level).get().then(res => {
                            if (res.data != null) {
                                this.setData({
                                    level_title: res.data.title,
                                });
                            }
                        });
                    }
                }else{
                    wx.showToast({
                        title: '系统数据存在错误，请联系开发商',
                    })
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

    },

    setAgentBuz: function() {
        wx.navigateTo({
            url: '/page/distributor/setAgentBuz?distributorid=' + this.data.id,
        })
    },

    /**
     * 为渠道商设置渠道等级
     */
    selectLevel: function(){
        wx.navigateTo({
            url: '/page/distributor/setDistributorLevel?distributorid=' + this.data.id + '&current=' + this.data.level_id,
        }) 
    }
})