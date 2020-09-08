var Find = require("../../../model/find.js")
Page({

    /**
     * 页面的初始数据
     */
    data: {
        showDrawer: false,
        undataList: [],
        redataList: [],
        unrelease: "",
        release: "",
        item: ""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {

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
        getMyScreen({
            page: this
        });
    },
    showDrawer: function(e) {
        var item = e.currentTarget.dataset.item;

        this.setData({
            showDrawer: true,
            item: item

        });
    },

    hideDrawer: function() {
        this.setData({
            showDrawer: false
        });
    },
    preventTouchMove: function() {

    },
    // 删除场景
    deleteScreen: function() {
        this.hideDrawer();
        var item = this.data.item;
        wx.showModal({
            title: '删除场景',
            content: '此操作不可恢复，确定删除？',
            success: (res) => {
                if (res.confirm) {
                    console.log('deleteScreen');
                    Find.deleteScreen(item.id)
                        .then(() => {
                            getMyScreen({
                                page: this
                            });
                        });
                }
            }
        })
    },
    // 场景详情
    gotoDetail: function(e) {
        var item = e.currentTarget.dataset.item;
        if (item.status == 0) {
            wx.navigateTo({
                url: '/pages/screenDetail/screenDetail?detailType=workMyScene&id=' + item.id,
            })
        } else {
            wx.showModal({
                title: '确认',
                content: '编辑前需要撤回已发布的场景',
                success: (res) => {
                    if (res.confirm) {
                        revokeScene({
                            page: this
                        }, item)
                    }
                }
            })
        }

    },
    // 编辑场景
    editScene:function(e){
        this.hideDrawer();
        var item = this.data.item;
        console.log(item)
        var edit = "edit"
        if (item.status == 0) {
            wx.setStorageSync("editScene", item)
            wx.navigateTo({
                url: '/pages/createScreen/createScreen?type=editScene',
            })
        } else {
            wx.showModal({
                title: '确认',
                content: '编辑前需要撤回已发布的场景',
                success: (res) => {
                    if (res.confirm) {
                        revokeScene({
                            page: this
                        }, item, edit)
                    }
                }
            })
        }
    }
})

var getMyScreen = ({
    page
}) => {
    var loginInfo = wx.getStorageSync("loginInfo")
    var data = {
        filter: {
            "include": ["authorPointer"],
            "where": {
                "and": [{
                        "status": {
                            "gte": 0
                        }
                    },
                    {
                        "author": loginInfo.userId
                    }
                ]
            },
            "limit": 1000
        }
    }
    Find.getMyScreen(data)
        .then((res) => {
            var undataList = [];
            var redataList = [];
            var data = res;
            res.forEach((data) => {
                // data.cover = data.cards[0][img];
                // delete data.cards;
                if (data.name == "") {
                    data.name = "未命名"
                }
                if (data.status == 0) {
                    undataList.push(data)
                } else {
                    redataList.push(data)
                }
            });
            page.setData({
                undataList: undataList,
                redataList: redataList,
                unrelease: "未发布",
                release: "已发布"
            });
        });
}

var revokeScene = ({
    page
}, item,edit) => {
    item.status = 0;
    var data = item;
    data.author = item.userid
    console.log(data)
    Find.releaseScreen(item.id, data).then((res) => {
        if(edit == "edit"){
            wx.setStorageSync("editScene", item)
            wx.navigateTo({
                url: '/pages/createScreen/createScreen?type=editScene',
            })
        }else{
            wx.navigateTo({
                url: '/pages/screenDetail/screenDetail?detailType=workMyScene&id=' + data.id,
            })
        }
        
    })
}