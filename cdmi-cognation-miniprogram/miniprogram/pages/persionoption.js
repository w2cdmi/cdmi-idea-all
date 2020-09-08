// miniprogram/pages/persionoption.js
var comm = require("../commjs/comm.js");

Page({

    /**
     * 页面的初始数据
     */
    data: {
        members:[],
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        //获取该血亲关系的人员列表
        const db = wx.cloud.database();
        db.collection("relations").where({
            pointerId: getApp().globalData.pointer.id,
        }).get().then(res => {
            if (res.data.length != 0) {
                var members = [];
                res.data.forEach(function (item) {
                    var member = {};
                    member.id = item.memberId;
                    member.name = item.name;
                    member.cognation = item.cognation;
                    member.relation = item.relation;
                    members.push(member);
                });
                this.setData({
                    members: members,
                });
            }
        });
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },

    /** 选中目标用户视野 */
    selectPerson: function(e){
        // console.info(getApp().globalData);
        // console.info(e.currentTarget.dataset);
        var dataset = e.currentTarget.dataset;
        var pointer = {};
        pointer.id = dataset.id;
        pointer.name = dataset.name;
        var relation = {};
        pointer.relation = relation;
        //补充观察角色与操作者的关系信息
        for (var i = 0; i < comm.relations.length; i++) {
            if (comm.relations[i].key == dataset.cognation) {
                var titles = comm.relations[i].titles;
                for (var j = 0; j < titles.length; j++) {
                    if (titles[j].key == dataset.relation) {
                        pointer.relation.key = titles[j].key;
                        pointer.relation.value = titles[j].value;
                        break;
                    }
                }
                break;
            }
        };
        getApp().globalData.pointer = pointer;

        //要求返回后重新刷新数据
        var pages = getCurrentPages();
        var prePage = pages[pages.length - 2];
        prePage.setData({
            pointer: pointer,
            render: true,
        });

        wx.navigateBack();
    }
})