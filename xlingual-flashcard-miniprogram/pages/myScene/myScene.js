var Find = require("../../model/find.js");
var Session = require("../../common/session.js");
var app = getApp().globalData;

Page({

    data: {
        showDrawer: false,
        dataList: []
    },

    onShow: function() {
        Session.isLogin();
        var loginStatus = app.loginStatus;
        if (loginStatus) {
            getMyScreen({
                page: this
            });
        }
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

    gotoDetail: function(e) {
        var item = e.currentTarget.dataset.item;
        if (!item) {
            item = this.data.item;
        }
        this.hideDrawer();
        wx.navigateTo({
            url: '/pages/screenDetail/screenDetail?detailType=myScreen&id=' + item.id,
        });
    },

    deleteScreen: function() {
        this.hideDrawer();
        var item = this.data.item;
        wx.showModal({
            title: '删除场景',
            content: '此操作不可恢复，确定删除？',
            success: (res) => {
                if (res.confirm) {
                    Find.deleteScreen(item.id)
                        .then(() => {
                            getMyScreen({
                                page: this
                            });
                        });
                }
            }
        })
    }
})

var getMyScreen = ({
    page
}) => {
    var uid = getApp().globalData.userId;
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
                        "author": uid
                    }
                ]
            },
            "limit": 1000
        }
    }
    Find.getMyScreen(data)
        .then((res) => {
            res = res.map((item) => {
                if (item.cards.length >= 1) {
                    item.cover = item.cards[0].img;
                }
                delete item.cards;
                return item;
            });
            page.setData({
                dataList: res
            });
        });
}