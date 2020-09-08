// miniprogram/pages/index.js
var comm = require("../commjs/comm.js");

Page({

    /**
     * 页面的初始数据
     */
    data: {
        cavasHeight: getApp().globalData.screenHeight - 40,
        cavasWidth: getApp().globalData.screenWidth,
        view_range: 4,
        cognations: [],
        rectWidth: 0,
        rectHeight: 0,
        render:false,       //是否需要重新刷新数据
        pointer: {},
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        //页面要显示的关系块与界面关系
        var level = this.data.view_range;
        var num = 2 * level + 1;
        var rectWidth = parseInt((this.data.cavasWidth) / num);
        var rectHeight = parseInt((this.data.cavasHeight) / num);

        //假设中心的方框的坐标为0，0
        //中心点坐标为
        var x = (this.data.cavasWidth - rectWidth) / 2;
        var y = (this.data.cavasHeight - rectHeight) / 2

        for (var j = -level; j <= level; j++) {
            var row = [];
            for (var i = -level; i <= level; i++) {
                if (Math.abs(i) + Math.abs(j) <= level) { //这样才能执行
                    var item = {
                        "x": i,
                        "y": j
                    };
                    row.push(item);
                }
            }
            this.data.cognations.push(row);
        }

        this.setData({
            cognations: this.data.cognations,
            rectWidth: rectWidth,
            rectHeight: rectHeight,
        });
        
        console.log("检查用户是否登陆，获取当前登陆用户信息");
        console.log(getApp().globalData.openid);
        //检查用户是否登陆
        if (getApp().globalData.userid == null) {
            wx.cloud.callFunction({
                name: 'getUserInfo',
            }).then(res => {
                getApp().globalData.appid = res.result.appid;

                console.log("检查用户微信信息");
                const db = wx.cloud.database();
                //查找当前用户的openId是否在wxusers中存在，不存在，则增加一个微信用户
                db.collection('wxusers').where({
                    _openid: res.result.openid
                }).get().then(result => {
                    console.debug(result);
                    if (result.data.length == 0) {
                        //如果wxusers不存在，先创建WXUSER账号
                        db.collection('wxusers').add({
                            data: {
                                name: getApp().globalData.userName,
                                headImage: getApp().globalData.headImage,
                                creatime: new Date(),
                            }
                        })
                    } else if (result.data.length == 1){
                        //获取当前观察视角用户信息
                        getApp().globalData.userid = result.data[0]._id;
                        db.collection('members').where({
                            bindId: getApp().globalData.userid,
                            bindType: 'WEIXIN',
                            appId: getApp().globalData.appid,
                        }).get().then(result => {
                            console.info(result.data.length);
                            if(result.data.length == 0){
                                wx.navigateTo({
                                    url: 'person?cognation=QJ&relation=SELF',
                                });
                                return;
                            }
                            var pointer = {};
                            pointer.id = result.data[0]._id;
                            pointer.name = result.data[0].name;
                            pointer.sex = result.data[0].sex;
                            pointer.relation = {
                                key: 'SELF',
                                value: "自己",
                            };
                            getApp().globalData.pointer = pointer;
                            //获取观点角色人的个人关系
                            this.setData({
                                pointer: getApp().globalData.pointer,
                            });
                        });
                    }else{
                        console.error("WXUSERS数据库中存在重复数据");
                    }
                });
            });
        };
        wx.navigateTo({
            url: 'private',
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
        console.info("render:" + this.data.render);
        if (this.data.render){ //如果需要重新刷新数据，则重新提取数据
            this.setData({
                pointer: getApp().globalData.pointer,
            });
        };
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

    sliderchange: function(e) {
        console.info(e.detail.value);

    },

    /**
     * 导航到关系页面
     */
    navToPerson: function(e) {
        var dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'relation?x=' + dataset.x + '&y=' + dataset.y,
        })
    },

    /**
     * 切换预览与添加人身份
     */
    switchPerson:function(){
        wx.navigateTo({
            url: 'persionoption',
        })
    }
})