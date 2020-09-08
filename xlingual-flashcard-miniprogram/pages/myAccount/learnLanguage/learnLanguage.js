var lang = require("../../../common/language.js");
var Account = require("../../../model/account.js");
Page({

    /**
     * 页面的初始数据
     */
    data: {
        learnLanguageList: [],
        scrollHeight: "",
        windowHeight: wx.getSystemInfoSync().windowHeight,

        destMotherLang: {},
        chooseCheckedLang: [],
        chooseType: "",
        scene: ""
    },

    /**
     * 生命周期函数--监听页面加载
     */

    onLoad: function(options) {
        var page = this;
        var chooseType = options.chooseType
        var windowHeight = page.data.windowHeight;
        var scrollHeight = (windowHeight) + "px"
        var secene=""
        if (options.scene){
            secene= options.scene
        }
        page.setData({
            scrollHeight: scrollHeight,
            chooseType: chooseType,
            scene: secene
        })
    },

    /**
     * 生命周期函数--监听页面显示
     */

    onShow: function() {
        var page = this
        var falseShow = [];
        var trueShow = [];
        var chooseType = page.data.chooseType;

        var checkedLang = ""
        var destLearnLang = ""
        if (chooseType == "editChooseLang") {
            checkedLang = wx.getStorageSync("destLang").checkedLang;
            destLearnLang = wx.getStorageSync("destLang").payload
        } else if (chooseType == "editSceneLang") {
            var destMother = wx.getStorageSync("copydestLang").checkedLang
            checkedLang = wx.getStorageSync("editSceneCheckedLang");
            for (var i = 0; i < checkedLang.length; i++) {
                if (checkedLang[i].code == destMother[0].code){
                    checkedLang.splice(i,1);
                    checkedLang.unshift(destMother[0])
                }
            }
            destLearnLang = wx.getStorageSync("copydestLang").payload
        }
        destLearnLang.splice(0, 1)
        console.log(destLearnLang)
        for (var i = 0; i < lang.length; i++) {
            if (lang[i].show) {
                trueShow.push(lang[i])
            } else {
                falseShow.push(lang[i])
            }
        }

        for (var i = 0; i < checkedLang.length; i++) {
            for (var j = 0; j < falseShow.length; j++) {
                falseShow[j].checked = false
                if (checkedLang[0].code == falseShow[j].code) {
                    checkedLang[0].name = falseShow[j].name
                    falseShow.splice(j, 1);
                }
            }
        }
        for (var i = 0; i < checkedLang.length; i++) {
            for (var j = 0; j < falseShow.length; j++) {
                if (checkedLang[i].code == falseShow[j].code) {
                    falseShow[j].checked = true
                }
            }
        }
        var concatLang = falseShow.concat(trueShow);
        page.setData({
            learnLanguageList: falseShow,
            destMotherLang: checkedLang[0],
            chooseCheckedLang: destLearnLang
        })
    },
    /**
     * 学习语言选择
     */
    checkboxLangChange: function(e) {
        var page = this;
        page.setData({
            chooseCheckedLang: e.detail.value
        })
    },

    /**

     * 确认学习语言选择
     */
    confirmChooseLang: function() {
        var page = this;
        var chooseType = page.data.chooseType;
        var chooseCheckLang = page.data.chooseCheckedLang
        var copydestLang = wx.getStorageSync("copydestLang");
        var sceneType = wx.getStorageSync(page.data.scene)
        var motherLang = wx.getStorageSync("langFirst").payload
        var motherLangAry = [];
        var concatMotherDestLang = [];
        motherLangAry.push(motherLang)
        var newCheckedLang = [];
        concatMotherDestLang = motherLangAry.concat(chooseCheckLang)
        if (chooseType == "editChooseLang") {
            confirmChooseLang({
                page: this
            })
        } else if (chooseType == "editSceneLang") {
            for (var i = 0; i < concatMotherDestLang.length; i++) {
                newCheckedLang.push({
                    "code": concatMotherDestLang[i],
                    "checked": true
                })
            }
            copydestLang.payload = concatMotherDestLang;
            copydestLang.checkedLang = newCheckedLang
            sceneType.languages = concatMotherDestLang
            wx.setStorageSync("copydestLang", copydestLang);
            wx.setStorageSync("editSceneCheckedLang", newCheckedLang)
            wx.setStorageSync(page.data.scene, sceneType)
            wx.navigateBack({
                delta: 1
            })
        }

    },

})

var confirmChooseLang = ({
    page
}) => {
    var uid = wx.getStorageSync("loginInfo").userId;
    var id = wx.getStorageSync("loginInfo").id;
    var data = {
        "dest_langs": page.data.chooseCheckedLang
    }
    Account.putAccountInfo(data, uid, id)
        .then((res) => {
            wx.navigateBack({
                delta: 1
            })
        }, () => {

        });
}