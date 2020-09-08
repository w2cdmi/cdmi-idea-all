// miniprogram/pages/person.js
var comm = require("../commjs/comm.js");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        cognation: {},
        relation: {},
        options_titles: [],
        view_options: false,
        isSelf: false,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        this.data.cognation.key = options.cognation;
        this.data.relation.key = options.relation;

        console.info(options);

        //如果与我的关系不存在，则提取出可关系可选项
        if (this.data.relation.key == null) {
            for (var i = 0; i < comm.relations.length; i++) {
                if (comm.relations[i].key == options.cognation) {
                    if (options.cognation == 'QJ'){
                        var relations = [];
                        comm.relations[i].titles.forEach(function(item){
                            if (item.sex != getApp().globalData.pointer.sex){
                                relations.push(item);
                            }
                        });
                        this.data.options_titles = relations;
                        break;
                    }else{
                        this.data.options_titles = comm.relations[i].titles;
                    }
                }
            };
        } else {
            //获得亲人与自己的关系
            for (var i = 0; i < comm.relations.length; i++) {
                console.info(options.cognation);
                if (comm.relations[i].key == options.cognation) {
                    var titles = comm.relations[i].titles;
                    for (var j = 0; j < titles.length; j++) {
                        console.info(options.relation + ":" + titles[j].key);
                        if (titles[j].key == options.relation) {
                            this.data.relation.key = titles[j].key;
                            this.data.relation.value = titles[j].value;
                            this.data.relation.antonym = titles[j].antonym;
                            this.data.relation.sex = titles[j].sex;
                            break;
                        }
                    }
                    break;
                }
            };
        }

        var _this = this;
        this.setData({
            cognation: _this.data.cognation,
            relation: _this.data.relation,
            options_titles: _this.data.options_titles,
        });
        console.info(_this.data);
        //存在对象id，查看或编辑对象
        if (options.id != null && options.id != '') {
            const db = wx.cloud.database();
            db.collection("members").doc(options.id).get().then(member => {
                if (member.data != null) {
                    if (member.data.bindId == getApp().globalData.userid) {
                        _this.data.isSelf = true;
                    }
                    _this.setData({
                        id: options.id,
                        name: member.data.name,
                        birthday: member.data.birthday,
                        phone: member.data.phone,
                        sex: member.data.sex,
                        idcard: member.data.idcard,
                        isSelf: _this.data.isSelf,
                        // relation: member.data.relation,
                    });
                }
                if (member.data.axis.x != 0 && member.data.axis.y != 0) {
                    db.collection("relations").where({
                        pointerId: getApp().globalData.pointer.id,
                        memberId: member.data._id,
                    }).get().then(res => {
                        if (res.data.length == 1) {
                            _this.setData({
                                relation: res.data[0].relation,
                                remark: res.data[0].remark,
                            });
                        } else if (res.data.length == 0) {
                            console.error("系统存在错误，没有找到两个人之间的关系");
                        } else {
                            console.error("两个人只能有一种关系");
                        }
                    });
                }
            });
        } else {
            if (this.data.relation.key == 'SELF') {
                _this.setData({
                    view_options: false,
                });
            } else {
                _this.setData({
                    view_options: true,
                });
            }
        };
        console.info(this.data.view_options);
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

    createPerson: function(e) {
        var data = e.detail.value;
        const db = wx.cloud.database();

        //如果是自己的信息，需要检查自己的信息是否存在
        var _this = this;
        if (data.relation == 'SELF') {
            db.collection("members").where({
                bindId: getApp().globalData.openid,
                bindType: 'WEIXIN',
                appId: getApp().globalData.appid,
            }).get().then(res => {
                console.info(res.data.length);
                if (res.data.length == 1) {
                    //更新数据
                    db.collection("members").doc(res.data[0]._id).update({
                        data: {
                            name: data.name,
                            birthday: data.birthday,
                            phone: data.phone,
                            idcard: data.idcard,
                            sex: data.sex,
                        }
                    }).then(result => {
                        //TODO 如果Name变化，则要更新relations表,Mark被修改，需要保存到relations表

                        //将结果设置会上一个页面，进行返回。不要跳转。
                        var pages = getCurrentPages();
                        var prePage = pages[pages.length - 2];
                        //将修改后的结果设置回原来的记录
                        for (var i = 0; i < prePage.data.members.length; i++) {
                            if (prePage.data.members[i].id == _this.data.id) {
                                prePage.data.members[i].name = data.name.trim();
                                prePage.data.members[i].relation = _this.data.relation;
                                break;
                            }
                        }
                        prePage.setData({
                            members: prePage.data.members,
                        });
                        wx.navigateBack();
                    });
                } else if (res.data.length == 0) {
                    //重新获取关系坐标
                    var axis = {};
                    for (var i = 0; i < comm.cognations.length; i++) {
                        if (comm.cognations[i].position.key == data.cognation) {
                            axis = comm.cognations[i].axis;
                            break;
                        }
                    };
                    //新加数据
                    db.collection("members").add({
                        data: {
                            axis: axis,
                            // cognation: _this.data.cognation,
                            // relation: _this.data.relation,
                            name: data.name,
                            birthday: data.birthday,
                            phone: data.phone,
                            idcard: data.idcard,
                            // remark:data.remark,
                            bindId: getApp().globalData.openid, //绑定到微信上
                            bindType: 'WEIXIN',
                            ownerId: getApp().globalData.openid,
                            ownerType: 'WEIXIN',
                            appId: getApp().globalData.appid,
                            creatime: new Date(),
                        }
                    }).then(res => {
                        console.info(res);
                        console.info(_this.data);
                        db.collection("members").doc(res._id).update({
                            data: {
                                creatorId: res._id,
                            }
                        }).then(update => {
                            //Do Nothing
                        });
                        //添加记录到relation表中
                        // db.collection("relations").add({
                        //     data:{
                        //         pointerId: res.data._id,
                        //         axis: axis,
                        //         cognation: _this.data.cognation,
                        //         relation: _this.data.relation,
                        //         name: data.name,
                        //         remark:data.remark,
                        //         memberId:res._id,
                        //         creatime :  new Date(),
                        //         appId: getApp().globalData.appid,
                        //     }
                        // });
                        //将结果设置会上一个页面，进行返回。不要跳转。
                        var pages = getCurrentPages();
                        var prePage = pages[pages.length - 2];
                        var member = {};
                        member.id = res._id;
                        member.name = data.name.trim();
                        member.relation = _this.data.relation;
                        // if (member.relation != null && member.relation.key == 'SELF') {
                        //     prePage.data.hasNoself = true;
                        // }
                        // //设置此时的Pointer信息
                        getApp().globalData.pointer = member;
                        console.info(getApp().globalData.pointer);
                        // //将新的类型插入到原有记录的首行
                        // prePage.data.members.unshift(member);
                        // prePage.setData({
                        //     members: prePage.data.members,
                        //     hasNoself: prePage.data.hasNoself,
                        // });
                        // wx.navigateBack();
                        prePage.setData({
                            render: true,
                        });
                        wx.navigateBack();
                    });
                } else {
                    console.error("数据库中存在重复的用户数据");
                }
            });
        } else {
            if (_this.data.id == null || _this.data.id == '') {
                console.info(_this.data);
                //重新获取关系坐标
                var axis = {};
                for (var i = 0; i < comm.cognations.length; i++) {
                    console.info(comm.cognations[i].position.key);
                    if (comm.cognations[i].position.key == data.cognation) {
                        axis = comm.cognations[i].axis;
                        console.info(axis);
                        break;
                    }
                };
                //新加数据
                db.collection("members").add({
                    data: {
                        axis: axis,
                        //    cognation: _this.data.cognation,
                        //    relation: _this.data.relation,
                        name: data.name,
                        birthday: data.birthday,
                        phone: data.phone,
                        idcard: data.idcard,
                        sex: data.sex,
                        //    remark: data.remark,
                        //    pointerId: getApp().globalData.pointer.id,
                        ownerId: getApp().globalData.openid,
                        ownerType: 'WEIXIN',
                        appId: getApp().globalData.appid,
                        creatorId: getApp().globalData.pointer.id,
                        creatime: new Date(),
                    }
                }).then(res => {
                    //添加记录到relation表中
                    var member = {};
                    member.id = res._id;
                    member.name = data.name.trim();
                    member.relation = _this.data.relation;

                    _this.addrelations(getApp().globalData.pointer.id, member, axis, _this.data.cognation, _this.data.relation);
                    _this.addCreatorRelations(getApp().globalData.pointer.id, member, axis, _this.data.relation);
                    //将结果设置会上一个页面，进行返回。不要跳转。
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    //将新的类型插入到原有记录的首行
                    prePage.data.members.unshift(member);
                    prePage.setData({
                        members: prePage.data.members,
                    });
                    wx.navigateBack();
                });
            } else {
                //更新数据
                db.collection("members").doc(_this.data.id).update({
                    data: {
                        name: data.name,
                        birthday: data.birthday,
                        phone: data.phone,
                        idcard: data.idcard,
                        sex: data.sex,
                    }
                }).then(result => {
                    //将结果设置会上一个页面，进行返回。不要跳转。
                    var pages = getCurrentPages();
                    var prePage = pages[pages.length - 2];
                    //将修改后的结果设置回原来的记录
                    for (var i = 0; i < prePage.data.members.length; i++) {
                        if (prePage.data.members[i].id == _this.data.id) {
                            prePage.data.members[i].name = data.name.trim();
                            prePage.data.members[i].relation = _this.data.relation;
                            break;
                        }
                    }
                    prePage.setData({
                        members: prePage.data.members,
                    });
                    wx.navigateBack();
                });
            }
        }
    },

    /**选择与当前操作人的关系 */
    selectRelation: function(e) {
        const dataset = e.currentTarget.dataset;
        var selected_option = {};
        selected_option.key = dataset.key;
        selected_option.value = dataset.value;
        this.setData({
            relation: selected_option,
            sex: dataset.sex,
        });
    },

    /** 加入关系 **/
    addrelations: function(pointerId, member, axis, cognation, relation) {
        var _this = this;
        const db = wx.cloud.database();
        /** 将成员添加到新的观测者下面 */
        db.collection("relations").where({
            pointerId: pointerId,
            memberId: member.id,
        }).get().then(res => {
            if (res.data.length == 0) {
                db.collection("relations").add({
                    data: {
                        pointerId: pointerId,
                        axis: axis,
                        cognation: cognation,
                        relation: relation,
                        name: member.name,
                        remark: member.remark,
                        memberId: member.id,
                        creatime: new Date(),
                    }
                });
            }
        });

        //颠倒观察人,把添加人已有的社会关系添加到新成员上
        var newaxis = {
            x: -axis.x,
            y: -axis.y,
        };
        var newcognation = {};
        //根据坐标计算出与观测角色人家庭关系
        for (var i = 0; i < comm.cognations.length; i++) {
            if ((comm.cognations[i].axis.x == newaxis.x) && (comm.cognations[i].axis.y == newaxis.y)) {
                newcognation.key = comm.cognations[i].position.key;
                //    newcognation.value = comm.cognations[i].position.value;
                break;
            }
        };

        //重新计算关系
        var newrelation = {};
        console.info("pointerId:" + pointerId + "relation:" + relation.key);
        if (relation != null && relation.key != null) {
            console.info("获取新关系");
            newrelation = comm.findAntonymRelation(relation.key, member.sex);
        }
        console.info(pointerId);

        //获取观察人的member信息
        db.collection("members").doc(pointerId).get().then(mem => {
            db.collection("relations").where({
                pointerId: member.id,
                memberId: mem.data._id,
            }).get().then(res => {
                if (res.data.length == 0) {
                    db.collection("relations").add({
                        data: {
                            pointerId: member.id,
                            axis: newaxis,
                            cognation: newcognation,
                            relation: newrelation,
                            name: mem.data.name,
                            remark: '',
                            memberId: mem.data._id,
                            creatime: new Date(),
                        }
                    });
                }
            });
        });


        //获取更多关系
        console.info("获取其他关系人信息");
        const _ = db.command;
        //找到以当前Pointer为创建人的其他成员
        console.info("pointerId:" + pointerId);
        console.info("memberId:" + member.id);
        db.collection("members").where({
            creatorId: _.eq(pointerId),
            _id: _.neq(pointerId).and(_.neq(member.id)),
        }).get().then(res => {
            if (res.data.length != 0) {
                console.info(res.data.length);
                // Promise.all(res.data.map((item) => {
                //     return new Promise((resolve, reject) => {
                res.data.forEach(function(item) {
                    console.info(item.name);
                    var target_axis = {};
                    target_axis.x = axis.x - item.axis.x;
                    target_axis.y = axis.y - item.axis.y;
                    console.info('添加更多');
                    console.info(target_axis);
                    if ((Math.abs(target_axis.x) <= 5) && (Math.abs(target_axis.y) <= 5) && (Math.abs(target_axis.x) + Math.abs(target_axis.y) <= 5)) {
                        //获取member信息
                        //根据坐标计算出与观测角色人家庭关系
                        var target_cognation = {};
                        for (var i = 0; i < comm.cognations.length; i++) {
                            if ((comm.cognations[i].axis.x == target_axis.x) && (comm.cognations[i].axis.y == target_axis.y)) {
                                target_cognation.key = comm.cognations[i].position.key;
                                target_cognation.value = comm.cognations[i].position.value;
                                break;
                            }
                        };
                        console.info("pointerId:" + item._id);
                        console.info("memberId:" + member.id);
                        console.info(target_cognation);
                        _this.addrelations(item._id, member, target_axis, target_cognation, {});
                    }
                });
            }
        });

        // db.collection("relations").where({
        //     pointerId: pointerId,
        //     memberId: _.neq(pointerId).and(_.neq(member.id)),
        //     direction: true, //修改为判断记录是否存在，存在则
        // }).get().then(res => {
        //     if (res.data.length != 0) {
        //         console.info(res.data.length);
        //         // Promise.all(res.data.map((item) => {
        //         //     return new Promise((resolve, reject) => {
        //         res.data.forEach(function (item) {
        //             console.info(item.name);
        //             var target_axis = {};
        //             target_axis.x = axis.x - item.axis.x;
        //             target_axis.y = axis.y - item.axis.y;
        //             console.info('添加更多');
        //             console.info(target_axis);
        //             if ((Math.abs(target_axis.x) <= 5) && (Math.abs(target_axis.y) <= 5) && (Math.abs(target_axis.x) + Math.abs(target_axis.y) <= 5)) {
        //                 //获取member信息
        //                 //根据坐标计算出与观测角色人家庭关系
        //                 var target_cognation = {};
        //                 for (var i = 0; i < comm.cognations.length; i++) {
        //                     if ((comm.cognations[i].axis.x == target_axis.x) && (comm.cognations[i].axis.y == target_axis.y)) {
        //                         target_cognation.key = comm.cognations[i].position.key;
        //                         //    target_cognation.value = comm.cognations[i].position.value;
        //                         break;
        //                     }
        //                 };
        //                 console.info("pointerId:" + item.memberId);
        //                 console.info("memberId:" + pointerId);
        //                 _this.addrelations(item.memberId, member, target_axis, target_cognation, {});
        //             }
        //         });
        //     }
        // });
    },

    addCreatorRelations: function(pointerId, member, axis, relation) {
        var _this = this;
        const db = wx.cloud.database();
        const _ = db.command;
        //找到当前Pointer的创建人,找到这个创建人的上级创建人以及他创建的
        db.collection("members").doc(pointerId).get().then(mem => {
            db.collection("members").where({
                creatorId: _.eq(mem.data.creatorId).and(_.neq(pointerId)),
                _id: _.neq(pointerId),
            }).get().then(res => {
                console.info(res.data.length);
                if (res.data.length != 0) {
                    // Promise.all(res.data.map((item) => {
                    //     return new Promise((resolve, reject) => {
                    res.data.forEach(function(item) {
                        console.info(item.name);
                        var target_axis = {};
                        target_axis.x = axis.x - item.axis.x;
                        target_axis.y = axis.y - item.axis.y;
                        console.info('添加更多');
                        console.info(target_axis);
                        if ((Math.abs(target_axis.x) <= 5) && (Math.abs(target_axis.y) <= 5) && (Math.abs(target_axis.x) + Math.abs(target_axis.y) <= 5)) {
                            //获取member信息
                            //根据坐标计算出与观测角色人家庭关系
                            var target_cognation = {};
                            for (var i = 0; i < comm.cognations.length; i++) {
                                if ((comm.cognations[i].axis.x == target_axis.x) && (comm.cognations[i].axis.y == target_axis.y)) {
                                    target_cognation.key = comm.cognations[i].position.key;
                                    target_cognation.value = comm.cognations[i].position.value;
                                    break;
                                }
                            };
                            console.info("pointerId:" + item._id);
                            console.info("memberId:" + member.id);
                            console.info(target_cognation);
                            _this.addrelations(item._id, member, target_axis, target_cognation, {});
                        }
                    });
                }
            });

            console.info('想上寻找创建者人');
            if (mem.data.creatorId != pointerId) {
                var target_axis = {};
                target_axis.x = mem.data.axis.x - axis.x;
                target_axis.y = mem.data.axis.y - axis.y;
                console.info(target_axis);
                _this.addCreatorRelations(mem.data.creatorId, member, target_axis, {});
            }
        });
    },
})