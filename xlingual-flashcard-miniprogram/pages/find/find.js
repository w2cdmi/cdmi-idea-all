
var Find = require("../../model/find.js")
var Session = require("../../common/session.js");

Page({
    data: {
        list: [{
            avatar: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528370815792&di=b11573fd5bdeeb436b7326e28aa0df7f&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201508%2F25%2F20150825002604_PLTdw.png',
            username: '张三',
            date: '2018-06-05',
            cover: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1528369085376&di=147927bed7b856d02c2985814e2dfd1d&imgtype=0&src=http%3A%2F%2Fpic129.nipic.com%2Ffile%2F20170518%2F24728704_094456632000_2.jpg',
            content: '233333333'
        }],
        dataList: [],
        offset: 0,
        TemplateOffset: 0,
        selectedHeader: 'share',
        hasNoMoreTemplateData: false,
        hasNoMoreShareData: false,
        scrollHeight: '10rpx'
    },

    onLoad: function (options) {
        var rcode = options.rcode;
        if (typeof rcode !== 'undefined') {
            wx.setStorage({
                key: 'rcode',
                data: rcode,
            });
        }

        getList({ page: this });
        wx.getSystemInfo({
            success: (res) => {
                var height = res.windowHeight - 80;
                this.setData({
                    scrollHeight: height + 'px'
                })
            },
        })
    },

    selectedHeader: function (e) {
        var name = e.currentTarget.dataset.name;
        this.setData({
            selectedHeader: name
        });
        if (this.data.dataList.length === 0) {
            getTemplate({ page: this });
        }
    },

    checkDetail: function (e) {
        var item = e.currentTarget.dataset.item;
        wx.navigateTo({
            url: '/pages/screenDetail/screenDetail?detailType=customShare&id=' + item.id,
        });
    },

    onPullDownRefresh: function () {
        if (this.data.selectedHeader === 'share') {
            this.setData({
                offset: 0,
            }, () => {
                getList({ page: this });
            })
        } else {
            this.setData({
                TemplateOffset: 0,
            }, () => {
                getTemplate({ page: this });
            })
        }

    },

    onReachBottom: function () {
        if (this.data.selectedHeader === 'share') {
            getList({ page: this });        
        } else {
            getTemplate({ page: this });        
        }
    },

    checkTemplateDetail: function (e) {
        var item = e.currentTarget.dataset.item;
        
        wx.navigateTo({
            url: '/pages/screenDetail/screenDetail?detailType=template&id=' + item.id,
        })
    }
})

var getList = ({ page }) => {
    var data = {
        filter: {
            "include": ["authorPointer"],
            "where": { "status": { "gte": 1 } },
            "limit": getApp().globalData.limit,
            "skip": page.data.offset,
            "order": "updatedAt DESC"
        }
    }
    if (page.data.hasNoMoreShareData) return;
    Find.getList(data)
        .then((res) => {
            if (res.length == 0) {
                page.setData({
                    hasNoMoreShareData: true,
                });
            }
            res = res.map((item) => {
                // item.updatedAt = item.updatedAt;
                item.updatedAt = item.updatedAt.substring(0, 10);
                return item;
            })
            if (page.data.offset == 0) {
                page.setData({
                    offset: page.data.offset + 10,
                    list: res
                });
            } else {
                page.setData({
                    offset: page.data.offset + 10,
                    list: page.data.list.concat(res)
                });
            }
            
        }, () => {

        });
}


var getTemplate = ({ page }) => {
    var data = {
        filter: {
            "include": ["authorPointer"],
            "where": { "status": 2 },
            "limit": 18,
            "skip": page.data.TemplateOffset,
            "order": "updatedAt DESC"
        }
    }
    if (page.data.hasNoMoreTemplateData) return;
    Find.getTemplate(data)
        .then((res) => {
            var data = res;
            res.forEach((data) => {
                delete data.cards;
            });
            if (res.length == 0) {
                page.setData({
                    hasNoMoreTemplateData: true,
                });
                return;
            }

            if (page.data.TemplateOffset == 0) {
                page.setData({
                    TemplateOffset: page.data.TemplateOffset + 18,
                    dataList: res
                });
            } else {
                page.setData({
                    TemplateOffset: page.data.TemplateOffset + 18,
                    dataList: page.data.dataList.concat(res)
                });
            }
        }, () => {

        });
}