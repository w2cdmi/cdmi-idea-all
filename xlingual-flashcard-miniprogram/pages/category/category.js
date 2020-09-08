Page({

    /**
     * 页面的初始数据
     */
    data: {
        categoryList: [{
                categoryName: "饮食"
            },
            {
                categoryName: "日常"
            },
            {
                categoryName: "交通"
            },
            {
                categoryName: "工具"
            },
            {
                categoryName: "运动玩耍"
            },
            {
                categoryName: "购物消费"
            },
            {
                categoryName: "公共场所"
            },
            {
                categoryName: "节假日"
            },
            {
                categoryName: "劳作"
            },
            {
                categoryName: "旅游"
            },
            {
                categoryName: "其他"
            },
        ],
        chooseName: ""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var page = this;
        var categoryName = options.categoryName
        if (categoryName == "选择场景类别") {
            page.setData({
                chooseName: "饮食"
            })
        } else {
            page.setData({
                chooseName: categoryName
            })
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
    chooseCategoryName: function(e) {
        var item = e.currentTarget.dataset.item;
        var page = this;
        wx.setStorageSync("categoryName", item.categoryName)
        page.setData({
            chooseName: item.categoryName
        });
        wx.navigateBack({
            delta:1
        })
    }
})