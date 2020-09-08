// console/league/editproject.js

Page({

    /**
     * 页面的初始数据
     */
    data: {
        id: '',
        catalog: {},
        name: '',
        amount: null,
        advantages: '',
        directstore_count: null,
        napastore_count: null,
        scheme: {}, //招商项目方案

        view_window: '',
        cover: 'contain', //contain：包含，fill：填充，cover：覆盖
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        wx.setNavigationBarTitle({
            title: '编辑招商项目',
        });
        if (options.action == 'edit') {
            if (options.id == null && options.id == '') {
                console.error("缺少必须的传入参数[id]，请联系开发商");
                return;
            }
            var _this = this;
            const db = wx.cloud.database();
            db.collection("zs_projects").doc(options.id).get().then(res => {
                if (res.data == null) {
                    wx.showToast({
                        title: '传入的招商项目草稿信息未找到，请退出后重试',
                    });
                }

                _this.setData({
                    id: res.data._id,
                    name: res.data.name,
                    catalogid: res.data.catalogid,
                    name: res.data.name,
                    amount: res.data.amount,
                    advantages: res.data.advantages.join(' '),
                    directstore_count: res.data.directstore_count,
                    napastore_count: res.data.napastore_count,
                    scheme: res.data.scheme,
                    status: 'unreleased',
                });
                db.collection("a_cataloies").doc(res.data.catalogid).get().then(res => {
                    if (res.data == null) {
                        wx.showToast({
                            title: '传入的招商项目草稿信息未找到，请退出后重试',
                        });
                        return;
                    }
                    var catalog = {};
                    catalog.id = res.data._id;
                    catalog.name = res.data.name;
                    if (res.data.parentId != null && res.data.parentId != '') {
                        db.collection("a_cataloies").doc(res.data.parentId).get().then(parent => {
                            if (parent.data != null) {
                                catalog.name = parent.data.name + ' - ' + catalog.name;
                            }
                            _this.setData({
                                catalog: catalog,
                            });
                        });
                    } else {
                        _this.setData({
                            catalog: catalog,
                        });
                    }
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

    /**选择项目分类信息 */
    selectCatalog: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'cataloies?sence=options&marker=' + dataset.marker,
        })
    },
    
    /**
     * 选择要发布到的频道，支持选择多个频道
     */
    selectChannel:function(e){
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'channeloptions?marker=' + dataset.marker,
        })
    },

    /**
     * 保存招商项目信息
     */
    saveProject: function(e) {
        var _this = this;
        var data = e.detail.value;
        if (data.name == null || data.name.trim() == '') {
            wx.showToast({
                title: '招商项目的名称不能为空',
            });
            return;
        };
        if (data.amount == null || data.amount.trim() == '') {
            wx.showToast({
                title: '招商项目的最低投资金额不能为空',
            });
            return;
        };

        const db = wx.cloud.database();
        if (_this.data.id != null && _this.data.id != '') {
            console.log("执行编辑招商项目信息操作");
            db.collection("zs_projects").doc(_this.data.id).update({
                data: {
                    catalogid: data.catalogid,
                    name: data.name.trim(),
                    amount: data.amount.trim(),
                    advantages: data.advantages.split(" "),
                    directstore_count: data.directstore_count == null ? 0 : data.directstore_count,
                    napastore_count: data.napastore_count == null ? 0 : data.napastore_count,
                    scheme: {
                        video: data.video,
                        coverImage: data.coverImage,
                        contentImage: data.contentImage,
                        contentText: data.contentText,
                    },
                }
            });
            wx.navigateBack();
        } else {
            console.log("执行保存新的招商项目信息操作");
            db.collection('zs_projects').add({
                data: {
                    catalogid: data.catalogid,
                    name: data.name.trim(),
                    amount: data.amount.trim(),
                    advantages: data.advantages.split(" "),
                    directstore_count: data.directstore_count == null ? 0 : data.directstore_count,
                    napastore_count: data.napastore_count == null ? 0 : data.napastore_count,
                    scheme: {
                        video: data.video,
                        coverImage: data.coverImage,
                        contentImage: data.contentImage,
                        contentText: data.contentText,
                    },
                    appId: getApp().globalData.consoleAppId,
                    creatorId: getApp().globalData.userId,
                    creatorType: 'SYS_ACCOUNT',
                    status: 'unreleased',
                    creatime: new Date(),
                },
            }).then(res => {
                //将结果设置会上一个页面，进行返回。不要跳转。
                var pages = getCurrentPages();
                var prePage = pages[pages.length - 2];
                var project = {};
                project.id = res._id;
                project.name = data.name.trim();
                project.amount = data.amount.trim();
                //将新的类型插入到原有记录的首行
                prePage.data.projects.unshift(project);
                prePage.setData({
                    projects: prePage.data.projects,
                });
                wx.navigateBack();
            });
        }
    },

    /**
     * 上传视频
     */
    changeVideo: function() {
        var _this = this;
        //从相册中选择视频进行上传
        wx.chooseVideo({
            sourceType: ['album'],
            success(res) {
                wx.getFileInfo({
                    filePath: res.tempFilePath,
                    digestAlgorithm: 'sha1',
                    success(file) {
                        console.log(file.size);
                        console.log(file.digest);
                        var cloudPath = "user/" + getApp().globalData.consoleAppId + "/" + getApp().globalData.userId + "/" + file.digest;
                        console.info('云路径：' + cloudPath);
                        wx.cloud.uploadFile({
                            cloudPath: cloudPath,
                            filePath: res.tempFilePath, // 小程序临时文件路径
                        }).then(res => {
                            var scheme = _this.data.scheme;
                            _this.setData({
                                "scheme.video": res.fileID,
                            });
                            //保存到数据库
                            const db = wx.cloud.database();
                            db.collection("zs_projects").doc(_this.data.id).update({
                                data: {
                                    scheme: {
                                        video: scheme.video,
                                    },
                                }
                            });
                        }).catch(error => {
                            // handle error
                            console.log(error)
                        })
                    }
                });
            }
        });
    },

    /**
     * 播放视频
     */
    playVideo: function() {
        this.setData({
            view_window: 'video',
        });
    },

    /**
     * 预览封面图
     */
    playCoverImage: function() {
        this.setData({
            view_window: 'cover',
        });
    },

    /**
     * 预览招商方案图文
     */
    playContentImage: function() {
        this.setData({
            view_window: 'content',
        });
    },

    /**
     * 关闭并隐藏视频
     */
    hiddenVideo: function() {
        this.setData({
            view_window: '',
        });
    },

    /**
     * 上传招商方案封面图片
     */
    changeCoverImage: function() {
        var _this = this;
        wx.chooseImage({
            count: 1,
            sizeType: ['compressed'],
            sourceType: ['album'],
            success(res) {
                // tempFilePath可以作为img标签的src属性显示图片
                const tempFilePaths = res.tempFilePaths;
                wx.getFileInfo({
                    filePath: res.tempFilePaths[0],
                    digestAlgorithm: 'sha1',
                    success(file) {
                        console.log(file.size);
                        console.log(file.digest);
                        var cloudPath = "user/" + getApp().globalData.consoleAppId + "/" + getApp().globalData.userId + "/" + file.digest;
                        console.info('云路径：' + cloudPath);
                        wx.cloud.uploadFile({
                            cloudPath: cloudPath,
                            filePath: res.tempFilePaths[0], // 小程序临时文件路径
                        }).then(res => {
                            var scheme = _this.data.scheme;
                            _this.setData({
                                "scheme.coverImage": res.fileID,
                            });
                            //保存到数据库
                            const db = wx.cloud.database();
                            db.collection("zs_projects").doc(_this.data.id).update({
                                data: {
                                    scheme: {
                                        coverImage: scheme.coverImage,
                                    },
                                }
                            });
                        }).catch(error => {
                            // handle error
                            console.log(error)
                        });
                    }
                });
            }
        })
    },

    /**
     * 上传招商方案说明图文图片
     */
    changeContentImage: function() {
        var _this = this;
        wx.chooseImage({
            count: 1,
            sizeType: ['compressed'],
            sourceType: ['album'],
            success(res) {
                // tempFilePath可以作为img标签的src属性显示图片
                const tempFilePaths = res.tempFilePaths;
                wx.getFileInfo({
                    filePath: res.tempFilePaths[0],
                    digestAlgorithm: 'sha1',
                    success(file) {
                        console.log(file.size);
                        console.log(file.digest);
                        var cloudPath = "user/" + getApp().globalData.consoleAppId + "/" + getApp().globalData.userId + "/" + file.digest;
                        console.info('云路径：' + cloudPath);
                        wx.cloud.uploadFile({
                            cloudPath: cloudPath,
                            filePath: res.tempFilePaths[0], // 小程序临时文件路径
                        }).then(res => {
                            var scheme = _this.data.scheme;
                            _this.setData({
                                "scheme.contentImage": res.fileID,
                            });
                            //保存到数据库
                            const db = wx.cloud.database();
                            db.collection("zs_projects").doc(_this.data.id).update({
                                data: {
                                    scheme: {
                                        contentImage: scheme.contentImage,
                                    },
                                }
                            });
                        }).catch(error => {
                            // handle error
                            console.log(error)
                        });
                    }
                });
            }
        })
    },

    /**发布项目上线 */
    publicProject: function() {
        var _this = this;
        console.info(_this.data);
        if (_this.data.name == null || _this.data.name.trim() == '') {
            wx.showToast({
                title: '招商项目的名称不能为空',
            });
            return;
        };
        if (_this.data.amount == null || _this.data.amount.trim() == '') {
            wx.showToast({
                title: '招商项目的最低投资金额不能为空',
            });
            return;
        };
        if (_this.data.scheme == null) {
            wx.showToast({
                title: '招商项目的文案不能为空',
            });
            return;
        } else {
            if (_this.data.scheme.video == null) {
                wx.showToast({
                    title: '招商项目的文案视频尚未上传',
                });
                return;
            }
            if (_this.data.scheme.coverImage == null) {
                wx.showToast({
                    title: '招商项目的文案封面尚未上传',
                });
                return;
            }
            if (_this.data.scheme.contentImage == null) {
                wx.showToast({
                    title: '招商项目的图文方案尚未上传',
                });
                return;
            }
        }

        const db = wx.cloud.database();
        db.collection("zs_projects").doc(_this.data.id).get().then(res => {
            if (res.data.length != 0) {
                db.collection("zs_released_projects").add({
                    data: {
                        catalogid: res.data.catalogid,
                        name: res.data.name,
                        amount: res.data.amount,
                        advantages: res.data.advantages,
                        directstore_count: res.data.directstore_count,
                        napastore_count: res.data.napastore_count,
                        creatime: res.data.creatime,
                        publictime: new Date(),
                        scheme: res.data.scheme,
                        appId: res.data.appId,
                        creatorId: res.data.creatorId,
                        creatorType: 'SYS_ACCOUNT',
                        auditorId: getApp().globalData.userId,
                        auditorType: 'SYS_ACCOUNT',
                        status: 'released',
                    }
                }).then(res => {
                    _this.deleteProject();
                    wx.showToast({
                        title: '项目已通过审核，发布到指定栏目',
                    })
                });
            }
        });
    },

    /**
     * 删除招商未通过审核的项目
     */
    deleteProject: function() {
        const db = wx.cloud.database();
        db.collection("zs_projects").doc(this.data.id).remove().then(res => {
            if (res.stats.removed == 1) {
                //从上一页的data中删除
                var pages = getCurrentPages();
                var prePage = pages[pages.length - 2];
                var projects = prePage.data.projects;
                for (var i = 0; i < projects.length; i++) {
                    if (projects[i].id == this.data.id) {
                        projects.splice(i, 1);
                        break;
                    }
                };
                prePage.setData({
                    projects: projects,
                });
            }
            wx.navigateBack();
        });
    }
})