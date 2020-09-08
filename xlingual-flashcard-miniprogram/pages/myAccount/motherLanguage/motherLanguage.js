
var lang = require("../../../common/language.js");
var Account = require("../../../model/account.js");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        motherLanList: [],
        scrollHeight: "",
        windowHeight: wx.getSystemInfoSync().windowHeight,
        chooseCode: ""
    },

    /**
     * 生命周期函数--监听页面加载
     */

    onLoad: function(options) {
        var page = this;
        var windowHeight = page.data.windowHeight;
        var scrollHeight = (windowHeight) + "px"
        page.setData({
            scrollHeight: scrollHeight
        })
    },

    /**
     * 生命周期函数--监听页面显示
     */

    onShow: function() {
        var page = this;
        var storLang = wx.getStorageSync("langFirst")
        var falseShow = [];
        var trueShow = [];
        for (var i = 0; i < lang.length; i++) {
            if (lang[i].show) {
                trueShow.push(lang[i])
            } else {
                falseShow.push(lang[i])
            }
        }
        var concatLang = falseShow.concat(trueShow)
        // console.log(falseShow)
        // console.log(trueShow)
        // var newLang = lang.sort(function (a, b) {
        //     return a.id - b.id;
        // })

        page.setData({
            motherLanList: falseShow,
            chooseCode: storLang.payload
        })
    },



    chooseOneMotherLanguage: function(e) {
        var page = this;
        var code = e.currentTarget.dataset.item.code;
        page.setData({
            chooseCode: code
        });
    },

    /**
     * 确认选择母语
     */
    confirmMotherLang: function() {
        var page = this;
        confirmChooseLang({
            page: this
        })
    }
})

var confirmChooseLang = ({
    page
}) => {
    var uid = wx.getStorageSync("loginInfo").userId;
    var id = wx.getStorageSync("loginInfo").id;
    var data = {
        "first_lang": page.data.chooseCode
    }
    Account.putAccountInfo(data, uid, id)
        .then((res) => {
            wx.navigateBack({
                delta:1
            })
        }, () => {

        });
}
