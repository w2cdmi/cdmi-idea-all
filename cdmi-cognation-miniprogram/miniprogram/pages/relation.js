// miniprogram/pages/relation.js
var comm = require("../commjs/comm.js");

Page({
    /**
     * 页面的初始数据
     */
    data: {
        axis: {}, //血缘坐标
        members: [],
        cognation: {}, //与我的血缘关系
        pointer: {},
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        this.data.axis.x = options.x;
        this.data.axis.y = options.y;

        var _this = this;
        //根据坐标计算出与观测角色人家庭关系
        for (var i = 0; i < comm.cognations.length; i++) {
            if ((comm.cognations[i].axis.x == options.x) && (comm.cognations[i].axis.y == options.y)) {
                _this.data.cognation.key = comm.cognations[i].position.key;
                _this.data.cognation.value = comm.cognations[i].position.value;
                break;
            }
        };
        console.info(_this.data.cognation);

        //获取观点角色人的个人关系
        if (getApp().globalData.pointer != null) {
            this.data.pointer = getApp().globalData.pointer;
        }

        //刷新到页面
        this.setData({
            cognation: _this.data.cognation,
            axis: _this.data.axis,
            pointer: _this.data.pointer,
        });

        //获取该血亲关系的人员列表
        var members = [];
        if (this.data.axis.x == 0 && this.data.axis.y == 0){
            var member = {};
            member.id = getApp().globalData.pointer.id;
            member.name = getApp().globalData.pointer.name;
            member.relation = {
                key: 'SELF',
                value: "自己",
            };
            member.cognation = _this.data.cognation;
            members.push(member);
        };
        console.info(members);
        console.info(_this.data.cognation);
        const db = wx.cloud.database();
        db.collection("relations").where({
            pointerId: getApp().globalData.pointer.id,
            cognation: {
                key: _this.data.cognation.key,
            },
        }).get().then(res => {
            if (res.data.length != 0) {
                res.data.forEach(function(item) {
                    var member = {};
                    member.id = item.memberId;
                    member.name = item.name;
                    member.cognation = item.cognation;
                    member.relation = item.relation;
                    members.push(member);
                });
            }
            this.setData({
                members: members,
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

    navToNewPeron: function(e) {
        wx.navigateTo({
            url: 'person?cognation=' + this.data.cognation.key,
        });
    },

    navToPeron: function(e) {
        var dataset = e.currentTarget.dataset;
        var url = "";
        console.info(dataset);
        if(dataset.id == null){
            url = 'person?relation=' + dataset.relation + '&cognation=' + dataset.cognation;
        }else{
            url ='person?id=' + dataset.id + '&relation=' + dataset.relation + '&cognation=' + dataset.cognation;
        }
        wx.navigateTo({
            url: url,
        });
    }
})