var Find = require("../../model/find.js")
var Language = require("../../common/language.js");
var appGlobalData = getApp().globalData;
Page({
    data: {
        listDetail: [],
        language: [],
        showText: true,
        curVoice: '',
        voice: [],
        selectedLanguageIndex: 0,
        currentSwiperIndex: 0,
        currentText: '',
        autoplay: false, // 自动播放
        interval: 1000,
        isShared: "",
        questionIndex: 1,
        swiperWidth: 1233,
        showDrawer: false,
        hideShowWord: '隐去文字',
        windowHeight: wx.getSystemInfoSync().windowHeight,
        swipeHeight: "",
        showBackBtn: false, // 返回首页按钮，默认不显示，只有分享进入的页面显示
        detailType: ""
    },

    showDrawer: function(e) {
        this.setData({
            showDrawer: true,
        });
    },

    hideDrawer: function() {
        this.setData({
            showDrawer: false
        });
    },

    releaseScreen: function() {
        var data = this.data.res[0];
        var id = data.id;
        data.author = getApp().globalData.userId;
        data.status = 1;
        this.hideDrawer();
        Find.releaseScreen(id, data)
            .then((res) => {
                if (res) {
                    wx.showToast({
                        title: '发布成功',
                    });
                }
            }, () => {
                wx.showToast({
                    title: '发布失败，请重试',
                });
            });
    },

    onShareAppMessage: function() {
        this.hideDrawer();
        // 机构码
        var rcode = getApp().globalData.rcode;
        console.log('rcode: ', rcode);
        return {
            title: '',
            path: '/pages/screenDetail/screenDetail?detailType=customShare&id=' + this.data.id + '&rcode=' + rcode,
        }
    },

    onLoad: function(options) {
        var detailType = options.detailType;
        this.setData({
            isShared: detailType,
            id: options.id,
            swipeHeight: (this.data.windowHeight - 60) + "px",
            detailType: detailType
        });

        
    },
    onShow: function(){
        var detailType = this.data.detailType
        switch (detailType) {
            case "template":
                getTemplateDetail({
                    page: this
                });
                break;
            case "customShare":
                (() => {
                    getDetail({
                        page: this
                    });
                    var rcode = options.rcode;
                    if (typeof rcode !== 'undefined') {
                        wx.setStorage({
                            key: 'rcode',
                            data: rcode,
                        });
                        this.setData({
                            showBackBtn: true,
                        });
                    }
                })();
                break;
            case "myScreen":
                getDetail({
                    page: this
                });
                break;
            case "workTemplate":
                getTemplateDetail({
                    page: this
                });
                break;
            case "workMyScene":
                getDetail({
                    page: this
                });
                break;
        }
    },
    onReady: function() {
        this.audioCtx = wx.createAudioContext('myAudio')
        if (this.data.currentAudio !== "noAudio") {
            this.audioCtx.play();
        }
    },

    toggleShowText: function() {
        this.setData({
            showText: this.data.showText ? false : true,
            hideShowWord: this.data.showText ? '显示文字' : '隐去文字'
        });
    },

    bindchangeSwiper: function(e) {
        var listDetail = this.data.listDetail;
        var currentSwiperIndex = e.detail.current;
        var index = this.data.selectedLanguageIndex;
        var curLanguages = this.data.language;

        var currentText = listDetail[currentSwiperIndex].names[curLanguages[index]['code']];
        var currentAudio = listDetail[currentSwiperIndex].audios[curLanguages[index]['code']];
        this.setData({
            currentSwiperIndex: currentSwiperIndex,
            currentText: currentText ? currentText : '',
            currentAudio: (currentAudio != null && currentAudio != undefined) ? currentAudio : 'noAudio'
        }, () => {
            if (this.data.currentAudio !== "noAudio") {
                this.audioCtx.play();
            } else {
                wx.showToast({
                    title: '暂无音频',
                    icon: 'none'
                })
            }
        });
    },

    palyAudio: function() {
        if (this.data.currentAudio !== "noAudio") {
            this.audioCtx.play();
        } else {
            wx.showToast({
                title: '暂无音频',
                icon: "none"
            });
        }

    },

    selectLanguage: function(e) {
        var index = e.currentTarget.dataset.index;
        var listDetail = this.data.listDetail;
        var currentSwiperIndex = this.data.currentSwiperIndex;
        var curLanguages = this.data.language;
        var currentText = listDetail[currentSwiperIndex].names[curLanguages[index]['code']];
        var currentAudio = listDetail[currentSwiperIndex].audios[curLanguages[index]['code']];

        if (typeof currentText === 'undefined') {
            currentText = '';
        }

        if (currentAudio == null && currentAudio == undefined) {
            currentAudio = 'noAudio'
        }

        this.setData({
            selectedLanguageIndex: index,
            selectedLanguage: curLanguages[index]['code'],
            currentText: currentText,
            currentAudio: (currentAudio != null && currentAudio != undefined) ? currentAudio : 'noAudio'
        }, () => {
            this.palyAudio();
        });
    },

    test: function() {
        var language = this.data.selectedLanguage;
        wx.navigateTo({
            url: '/pages/screenDetail/test/test?language=' + language + '&id=' + this.data.id + '&index=' + this.data.currentSwiperIndex,
        });
    },
    // 导入到我的场景
    leadingMyScene: function() {
        // appGlobalData.loginStatus
        if (wx.getStorageSync("loginStatus")) {
            wx.navigateTo({
                url: '/pages/createScreen/createScreen?type=leadingScene',
            })
        } else {
            wx.showToast({
                title: '用户登录过期,请重新登录',
                icon: "none"
            })
        }
    },
    editScene: function() {
        wx.navigateTo({
            url: '/pages/createScreen/createScreen?type=editScene',
        })
    },

    // 返回首页
    backHomepage: function() {
        wx.switchTab({
            url: '/pages/find/find',
        })
    }
});


var getDetail = ({
    page
}) => {
    var data = {
        filter: {
            "include": ["authorPointer"],
            "where": {
                "_id": page.data.id
            },
            "limit": 1000
        }
    }
    Find.getDetail(data)
        .then((res) => {
            var languages = res[0].languages;
            var curLanguages = [];
            languages.forEach((item, index) => {
                for (var value of Language) {
                    if (item == value.code && value.show === false) {
                        curLanguages.push(value);
                    }
                }
            });
            var swiperWidth = curLanguages.length * 200;
            if (swiperWidth > 750) {
                page.setData({
                    swiperWidth: 750,
                });
            } else {
                page.setData({
                    swiperWidth: swiperWidth,
                })
            }
            var currentText = res[0].cards[0].names[curLanguages[0]['code']];
            var currentAudio = res[0].cards[0].audios[curLanguages[0]['code']];

            page.setData({
                res: res,
                listDetail: res[0].cards,
                language: curLanguages,
                currentText: currentText,
                currentAudio: currentAudio ? currentAudio : 'noAudio',
                selectedLanguage: curLanguages[0]['code'],
            }, () => {
                wx.setStorageSync("editScene", res[0])
            });
        }, () => {

        });
}

var getTemplateDetail = ({
    page
}) => {
    var data = {
        filter: {
            "include": ["authorPointer"],
            "where": {
                "_id": page.data.id
            }
        }
    }
    Find.getTemplateDetail(data)
        .then((res) => {
            var languages = res[0].languages;
            var curLanguages = [];
            languages.forEach((item, index) => {
                for (var value of Language) {
                    if (item == value.code && value.show === false) {
                        curLanguages.push(value);
                    }
                }
            });
            var scrollWidth = curLanguages.length * 200;
            if (scrollWidth > 750) {
                page.setData({
                    scrollWidth: 750,
                })
            } else {
                page.setData({
                    scrollWidth: scrollWidth,
                })
            }
            var currentText = res[0].cards[0].names[curLanguages[0]['code']];
            var currentAudio = res[0].cards[0].audios[curLanguages[0]['code']];

            page.setData({
                listDetail: res[0].cards,
                language: curLanguages,
                currentText: currentText,
                currentAudio: currentAudio ? currentAudio : 'noAudio'
            }, () => {
                console.log('currentAudio: ', currentAudio)
                wx.setStorageSync("leadingScene", res[0])
            });
        }, () => {

        });
}