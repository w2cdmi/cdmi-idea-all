var Find = require("../../../model/find.js")

Page({

    /**
     * 页面的初始数据
     */
    data: {
        dataList: [],
        offset: 0,
        TemplateOffset: 0,
        hasNoMoreTemplateData: false,
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

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
        getTemplate({ page: this });
    },
    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        this.setData({
            TemplateOffset: 0,
        }, () => {
            getTemplate({ page: this });
        })

    },
    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {
        getTemplate({ page: this });
    },
    /**
     * 点击模版详情
     */
    checkTemplateDetail: function (e) {
        var item = e.currentTarget.dataset.item;

        wx.navigateTo({
            url: '/pages/screenDetail/screenDetail?detailType=workTemplate&id=' + item.id,
        })
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

    }
})

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
            console.log(res.length);
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