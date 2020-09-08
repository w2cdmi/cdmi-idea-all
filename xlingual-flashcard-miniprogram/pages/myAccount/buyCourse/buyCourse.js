var Course = require("../../../model/course.js")
Page({

    /**
     * 页面的初始数据
     */
    data: {
        courseStoreList: [],
        imgUrl:""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        var page = this;
        var data = {};
        var courseStoreList =[]
        Course.getListCourse(data).then((res) => {
            courseStoreList.push(res.payload[0])
            page.setData({
                courseStoreList: courseStoreList,
                imgUrl: "http://www.xlingual.net"
            })
        }, () => {

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
        
       
    },

    /**
     * 点击查看课程详情
     */
    seeCourseDetail: function(e) {
        console.log(e)
        var courseDetail = e.currentTarget.dataset.item
        wx.setStorageSync("courseDetail", courseDetail)
        wx.navigateTo({
            url: '/pages/myAccount/buyCourse/courseDetail/courseDetail',
        })
    }
})