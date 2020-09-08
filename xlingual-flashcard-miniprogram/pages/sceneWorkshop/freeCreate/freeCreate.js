var Language = require("../../../common/language.js");
var Find = require("../../../model/find.js");
var Translate = require("../../../model/translate.js");
var Upload = require("../../../model/upload.js");
var Utils = require("../../../common/utils.js");
var azureTranslation = require("../../template/azureTranslation.js")
var startRecord = false; // 录音标识
var startTime;
var endTime;
var recordTimer;
var timingTime;
var recordLength = 0;
var minute = "";
var second = "";
var isNull = false;
var index = 0;
var isAdd = false;
var recorderManager = wx.getRecorderManager();
const device = wx.getSystemInfoSync()

var touchX0 = 0;
var touchY0 = 0;
var touchX1 = 0;
var touchY1 = 0;
var imgLeft = 0;
var imgTop = 0;
var rectX = 0;
var rectY = 0;
var scaleWidth = 0;
var scaleHeight = 0;
var oldDistance = 0;
var newDistance = 0;
var oldScale = 1;
var newScale = 1;
var touchstarted = false;
var touchmoved = false;
var touchended = false;
var self = {
    touchstarted: "",
    touchmoved: "",
    touchended: ""
}
var TOUCH_STATE = ['touchstarted', 'touchmoved', 'touchended'];
Page({

    data: {
        listDetail: [],
        language: [],
        showText: true,
        curVoice: '',
        voice: [],
        currentText: '',
        selectedIndex: 0,
        autoplay: false, // 自动播放
        interval: 1000,
        isShared: true,
        focus:false,
        selectedLanguageIndex: 0,
        currentSwiperIndex: 0,
        selectedLanguageCode: "",
        sceneName: "",
        changesceneName: "",
        categoryName: "",
        img: "../../images/icon/bg_unnamed_disabled@2x.png",
        accessToken: "",
        destLang: [],
        chooseSize: false,
        minute: "00",
        second: "00",
        current: "0",
        accessToken: "",
        isNull: false,
        isEditChoose: false, //是否显示选择编辑场景
        learnLang: "",
        newLearnLang: [],
        isOperateCard: false, //是否操作卡片
        isShowDelete: false,
        isShowSort: false,
        isShowTextarea: true,
        isShowBack: true,
        listDetailName: [],
        scrollHeight: "",
        windowHeight: wx.getSystemInfoSync().windowHeight,
        isShowCanvas: false,
        imgPath: "",
        width: device.windowWidth,
        height: device.windowWidth,
        destWidth: 0,
        destHeight: 0,
        ctxX: 0,
        ctxY: 0,
        scale: 2.5,
        zoom: 8,
    },

    onLoad: function(options) {
        var isShared = options.isShared;
        var page = this;
        var copydestLang = wx.getStorageSync("destLang");
        var destLang = wx.getStorageSync("destLang").payload;
        wx.setStorageSync("copydestLang", copydestLang)
        var languagesName = [];
        var listCard = [];
        var newLearnLang = [];
        var names = {};
        var audios = {}
        for (var i = 0; i < destLang.length; i++) {
            names[destLang[i]] = ""
            audios[destLang[i]] = ""
        }
        var newNames = JSON.stringify(names);
        var newAudios = JSON.stringify(audios)
        listCard.push({
            names: JSON.parse(newNames),
            audios: JSON.parse(newAudios),
            img: "/pages/images/icon/bg_unnamed_disabled@2x.png"
        }, {
            names: JSON.parse(newNames),
            audios: JSON.parse(newAudios),
            img: "/pages/images/icon/bg_unnamed_disabled@2x.png"
        })
        destLang.forEach((item, index) => {
            for (var i = 0; i < Language.length; i++) {
                if (item == Language[i].code && Language[i].show == false) {
                    languagesName.push({
                        name: Language[i].name,
                        code: item
                    })
                    newLearnLang.push(Language[i].name);
                }
            }
        });
        if (page.data.sceneName == "") {
            page.setData({
                sceneName: "未命名的场景"
            })
        }
        if (page.data.categoryName == "") {
            page.setData({
                categoryName: "选择场景类别"
            })
        }

        page.setData({
            isShared: isShared === "true" ? true : false,
            listDetail: listCard,
            currentText: listCard[0].names[destLang[0]],
            currentAudio: listCard[0].audios[destLang[0]],
            language: languagesName,
            destLang: destLang,
            selectedLanguageCode: destLang[0],
            learnLang: newLearnLang.join(","),
            newLearnLang: newLearnLang,
            scrollHeight: (page.data.windowHeight - 63) + "px"
        });
    },
    onShow: function() {
        var page = this;
        var listDetail = page.data.listDetail;
        var destLang = wx.getStorageSync("copydestLang").payload;
        var destLangs = wx.getStorageSync("destLang").payload;
        var newDestLang = []
        var listCard = []
        var languagesName = [];
        var newLearnLang = [];
        var listDetailName = []
        var categoryName = wx.getStorageSync("categoryName")
        if (categoryName == "") {
            categoryName = "选择场景类别"
        }
        destLang.forEach((item, index) => {
            for (var i = 0; i < Language.length; i++) {
                if (item == Language[i].code && Language[i].show == false) {
                    languagesName.push({
                        name: Language[i].name,
                        code: item
                    })
                    newLearnLang.push(Language[i].name);
                }
            }
        });
        for (var key in listDetail[0].names) {
            newDestLang.push(key)
        }
        for (var i = 0; i < listDetail.length; i++) {

            for (var j = 0; j < destLang.length; j++) {
                if (newDestLang.indexOf(destLang[j]) === -1) {
                    listDetail[i].names[destLang[j]] = ""
                    listDetail[i].audios[destLang[j]] = ""
                }
            }
            for (var j = 0; j < destLangs.length; j++) {
                if (destLang.indexOf(newDestLang[j]) === -1) {
                    delete listDetail[i].names[newDestLang[j]]
                    delete listDetail[i].audios[newDestLang[j]]
                }
            }
            listDetailName.push(Object.values(listDetail[i].names))
        }

        page.setData({
            language: languagesName,
            destLang: destLang,
            learnLang: newLearnLang.join(","),
            newLearnLang: newLearnLang,
            listDetail: listDetail,
            categoryName: categoryName,
            listDetailName: listDetailName
        });
    },
    onReady: function() {
        this.audioCtx = wx.createAudioContext('myAudio')

    },
    toggleShowText: function() {
        this.setData({
            showText: this.data.showText ? false : true,
        });
    },
    getInputFocus:function(e){
        var page = this;
        page.setData({
            focus:true
        })
    },
    // 播放
    palyAudio: function() {
        if (this.data.currentAudio !== "") {
            this.audioCtx.play();
        } else {
            wx.showToast({
                title: '暂无音频',
                icon: "none"
            });
        }
    },
    getImage: function(e) {
        var sourceType = e.currentTarget.dataset.name;
        var page = this;
        wx.chooseImage({
            count: 1,
            sourceType: [sourceType],
            success: function(res) {
                // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
                var tempFilePaths = res.tempFilePaths;
                page.setData({
                    isShowCanvas: true,
                    isShowTextarea: false
                })
                wx.getImageInfo({
                    src: tempFilePaths[0],
                    success: function(res) {
                        var str = res.width / res.height;
                        const ctx = wx.createCanvasContext("imgCanvas")
                        if (str > 1) {
                            var height = ((device.windowWidth / 750) * 750) / str
                            var ctxY = (device.windowWidth - height) / 2
                            page.setData({
                                ctxX: 0,
                                ctxY: ctxY,
                                destWidth: page.data.width,
                                destHeight: height,
                                imgPath: res.path
                            })
                            rectX = 0;
                            rectY = ctxY;
                            imgLeft = 0;
                            imgTop = ctxY;
                            scaleWidth = page.data.width;
                            scaleHeight = height
                            ctx.drawImage(res.path, 0, ctxY, page.data.width, height);
                            ctx.draw()
                        } else {
                            var width = str * (device.windowWidth / 750) * 750
                            var ctxX = (device.windowWidth - width) / 2;
                            page.setData({
                                ctxX: ctxX,
                                ctxY: 0,
                                destWidth: width,
                                destHeight: page.data.height,
                                imgPath: res.path
                            })
                            rectX = ctxX;
                            rectY = 0;
                            imgLeft = ctxX;
                            imgTop = 0;
                            scaleWidth = width;
                            scaleHeight = page.data.height
                            ctx.drawImage(res.path, ctxX, 0, width, page.data.height);
                            ctx.draw()
                        }
                    }
                })
                // uploadImg({
                //     page
                // }, tempFilePaths[0])
            },
        });
    },
    // 取消图片裁剪

    cancelCut: function() {
        var page = this;
        page.setData({
            isShowCanvas: false,
            isShowTextarea: true
        })
    },
    touchStart: function(e) {
        var page = this;
        var touches = e.touches;
        setTouchState(self, true, null, null)
        if (touches.length == 1) {
            var touch0 = touches[0];
            oneTouchStart({
                page
            }, touch0)
        } else if (touches.length >= 2) {
            var touch0 = touches[0]
            var touch1 = touches[1]
            twoTouchStart({
                page
            }, touch0, touch1)
        }
    },
    touchMove: function(e) {
        console.log(e)
        var page = this;
        var touches = e.touches;
        setTouchState(self, null, true)
        if (touches.length == 1) {
            var touch0 = touches[0];
            oneTouchMove({
                page
            }, touch0)
        } else if (touches.length >= 2) {
            var touch0 = touches[0]
            var touch1 = touches[1]
            twoTouchMove({
                page
            }, touch0, touch1)
        }
    },
    touchEnd: function(e) {
        console.log(e)
        var page = this
        setTouchState(self, false, false, true);
        xtouchEnd()
    },
    confirmCut: function() {
        var page = this;
        wx.canvasToTempFilePath({
            x: imgLeft,
            y: imgTop,
            width: scaleWidth,
            height: scaleHeight,
            destWidth: scaleWidth,
            destHeight: scaleHeight,
            canvasId: 'imgCanvas',
            success: function(res) {
                var tempFilePaths = res.tempFilePath
                page.setData({
                    isShowCanvas: false,
                    isShowTextarea: true
                })
                uploadImg({
                    page
                }, tempFilePaths)
            }
        })
    },
    // 滑动事件
    bindchangeSwiper: function(e) {
        var listDetail = this.data.listDetail;
        var current = e.detail.current;
        var destLang = wx.getStorageSync("copydestLang").payload;
        var currentSwiperIndex = this.data.currentSwiperIndex;
        var selectedLanguageIndex = this.data.selectedLanguageIndex;
        var selectedLanguageCode = this.data.selectedLanguageCode;
        var names = {};
        var audios = {}
        for (var i = 0; i < destLang.length; i++) {
            if (current > currentSwiperIndex) {

                if ((listDetail[current - 1].names[destLang[i]] != "" || (listDetail[current - 1].img != "/pages/images/icon/bg_unnamed_disabled@2x.png" && listDetail[current - 1].img != "")) && !isAdd) {
                    isNull = true;
                    isAdd = true;
                    index = current
                    break;
                } else {
                    isNull = false
                    index = current - 1
                }
            } else if (current <= currentSwiperIndex) {
                isNull = true;
                isAdd = false;
                index = current;
                break;
            }
        }
        if (isNull && isAdd) {
            var realAdd = false;
            for (var i = 0; i < destLang.length; i++) {
                names[destLang[i]] = ""
                audios[destLang[i]] = ""
                if (((listDetail[listDetail.length - 2].names[destLang[i]] != "" && listDetail.length > 2) || (listDetail[listDetail.length - 2].img != "" && listDetail[listDetail.length - 2].img != "/pages/images/icon/bg_unnamed_disabled@2x.png" && listDetail.length > 2)) || (listDetail[listDetail.length - 1].names[destLang[i]] == "" && listDetail.length <= 2)) {
                    realAdd = true;
                    isNull = true;
                    isAdd = false;
                    continue;
                }
                isAdd = false;
            }
            console.log(realAdd)
            if (realAdd) {
                var newNames = JSON.stringify(names);
                var newAudios = JSON.stringify(audios)
                listDetail.push({
                    names: JSON.parse(newNames),
                    audios: JSON.parse(newAudios),
                    img: "/pages/images/icon/bg_unnamed_disabled@2x.png"
                })
                this.setData({
                    currentSwiperIndex: index,
                    listDetail: listDetail,
                    currentText: listDetail[current].names[selectedLanguageCode] != undefined ? listDetail[current].names[selectedLanguageCode] : "",
                    currentAudio: listDetail[current].audios[selectedLanguageCode] != undefined ? listDetail[current].audios[selectedLanguageCode] : "",
                });
            } else {
                this.setData({
                    currentSwiperIndex: index,
                    listDetail: listDetail,
                    currentText: listDetail[current].names[selectedLanguageCode] != undefined ? listDetail[current].names[selectedLanguageCode] : "",
                    currentAudio: listDetail[current].audios[selectedLanguageCode] != undefined ? listDetail[current].audios[selectedLanguageCode] : "",
                });
            }

        } else if (!isNull) {
            wx.showToast({
                title: '请先编辑当前卡片!',
                icon: "none"
            })
            this.setData({
                currentSwiperIndex: index,
                listDetail: listDetail,
                currentText: listDetail[current].names[selectedLanguageCode] != undefined ? listDetail[current].names[selectedLanguageCode] : "",
                currentAudio: listDetail[current].audios[selectedLanguageCode] != undefined ? listDetail[current].audios[selectedLanguageCode] : "",
            })

        } else if (isNull) {
            this.setData({
                currentSwiperIndex: index,
                listDetail: listDetail,
                currentText: listDetail[current].names[selectedLanguageCode] != undefined ? listDetail[current].names[selectedLanguageCode] : "",
                currentAudio: listDetail[current].audios[selectedLanguageCode] != undefined ? listDetail[current].audios[selectedLanguageCode] : "",
            })
        }
    },
    selectLanguage: function(e) {
        var page = this;
        var destLang = wx.getStorageSync("copydestLang").payload;
        var code = e.currentTarget.dataset.item.code;
        var index = e.currentTarget.dataset.index;
        var listDetail = page.data.listDetail;
        var currentSwiperIndex = page.data.currentSwiperIndex;
        page.setData({
            selectedLanguageIndex: index,
            selectedLanguageCode: code,
            currentText: listDetail[currentSwiperIndex].names[code] != undefined ? listDetail[currentSwiperIndex].names[code] : "",
            currentAudio: listDetail[currentSwiperIndex].audios[code] != undefined ? listDetail[currentSwiperIndex].audios[code] : "",
        });
    },
    getRecord: function(e) {
        startTime = new Date().getTime();
        var page = this;
        const options = {
            duration: 10000, //指定录音的时长，单位 ms
            sampleRate: 16000, //采样率
            numberOfChannels: 1, //录音通道数
            encodeBitRate: 96000, //编码码率
            format: 'mp3', //音频格式，有效值 aac/mp3
            frameSize: 50, //指定帧大小，单位 KB
        }

        //开始录音
        recordTimer = setTimeout(function() {
            recorderManager.start(options);
            recorderManager.onStart(() => {
                console.log('recorder start')
            });
            page.setData({
                chooseSize: true
            })
            setNewTime(page)
            //错误回调
            recorderManager.onError((res) => {
                console.log(res);
            })
        }, 500);


    },

    stopRecord: function() {
        endTime = new Date().getTime();
        var page = this;
        if ((endTime - startTime) < 500) {
            endTime = 0;
            startTime = 0;
            clearTimeout(recordTimer);
            wx.showToast({
                title: '录音时间太短,请长按',
                icon: "none"
            })
        } else {
            recorderManager.stop();
            clearTimeout(timingTime);
            recordLength = 0;
            recorderManager.onStop((res) => {
                var tempFilePaths = res.tempFilePath;
                page.setData({
                    chooseSize: false
                })
                // var recodeJudge = "recode"
                uploadTape({
                    page: this
                }, tempFilePaths)
            })
        }

    },

    changText: function(e) {
        var textValue = e.detail.value;
        var language = wx.getStorageSync("copydestLang").payload;
        var newlistDetail = [];
        var listDetail = this.data.listDetail;
        var currentSwiperIndex = this.data.currentSwiperIndex;
        var selectedLanguageIndex = this.data.selectedLanguageIndex;
        var listDetailName = []

        for (var i = 0; i < language.length; i++) {
            for (var j = 0; j < listDetail.length; j++) {
                if (j == currentSwiperIndex && i == selectedLanguageIndex) {
                    listDetail[currentSwiperIndex].names[language[selectedLanguageIndex]] = textValue
                }
            }
        }
        for (var i = 0; i < listDetail.length; i++) {
            listDetailName.push(Object.values(listDetail[i].names))
        }
        this.setData({
            currentText: textValue,
            listDetail: listDetail,
            listDetailName: listDetailName
        })
    },
    // 翻译
    translateLang: function() {
        var page = this;
        var language = wx.getStorageSync("copydestLang").payload;
        var listDetail = page.data.listDetail;
        var currentSwiperIndex = page.data.currentSwiperIndex;
        var selectedLanguageIndex = page.data.selectedLanguageIndex;
        var robotTranslateLang = wx.getStorageSync("robotTranslateLang")
        if (listDetail[currentSwiperIndex].names[language[0]] == undefined || listDetail[currentSwiperIndex].names[language[0]] == "") {
            wx.showToast({
                title: '请输入母语对应的内容',
                icon: "none"
            })
        } else {
            if (robotTranslateLang == "Baidu") {
                translateLang({
                    page
                });
            } else if (robotTranslateLang == "Microsoft") {
                azureTranslation.microsoftTextTsn({
                    page
                })
            }


        }

    },
    onUnload: function() {
        var page = this;
        var listDetail = page.data.listDetail
        var nameAry = Object.values(listDetail[0].names);
        var audioAry = Object.values(listDetail[0].audios);
        var isNames = true;
        var isAudio = true;
        for (var i = 0; i < nameAry.length; i++) {
            if (nameAry[i] !=""){
                console.log(333)
                isNames = true
                break;
            }else{
                isNames = false
            }
        }
        for (var i = 0; i < audioAry.length; i++) {
            if (audioAry[i] != "") {
                console.log(333)
                isAudio = true
                break;
            } else {
                isAudio = false
            }
        }
        wx.removeStorageSync("categoryName")
        if (!isAudio && !isNames && (listDetail[0].img == "" || listDetail[0].img == "/pages/images/icon/bg_unnamed_disabled@2x.png")) {
            return;
        }
        setLeadingScene({
            page: this
        })
    },
    //长按删除
    longPressDelete: function(e) {
        var page = this;
        var index = e.currentTarget.dataset.index
        var listDetail = page.data.listDetail;
        var listDetailName = page.data.listDetailName;
        var currentText = page.data.currentText;
        var currentAudio = page.data.currentAudio;
        var selectedLanguageCode = page.data.selectedLanguageCode;
        var currentSwiperIndex = page.data.currentSwiperIndex;
        if (listDetail.length - 1 == 1) {
            wx.showModal({
                title: '删除卡片',
                content: '您当前只有一张卡片，不可删除',
                success: function(res) {
                    if (res.confirm) {

                    } else if (res.cancel) {

                    }
                }
            })
        } else {
            wx.showModal({
                title: '删除卡片',
                content: '此操作不可恢复，确定删除？',
                success: function(res) {
                    if (res.confirm) {
                        if (index == listDetail.length - 1) {
                            currentText = listDetail[index - 1].names[selectedLanguageCode]
                            currentAudio = listDetail[index - 1].audios[selectedLanguageCode]
                            currentSwiperIndex = index - 1
                        } else {
                            currentText = listDetail[index + 1].names[selectedLanguageCode]
                            currentAudio = listDetail[index + 1].audios[selectedLanguageCode]
                            currentSwiperIndex = index
                        }
                        listDetail.splice(index, 1);
                        listDetailName.splice(index, 1);

                        page.setData({
                            listDetail: listDetail,
                            listDetailName: listDetailName,
                            currentText: currentText,
                            currentAudio: currentAudio,
                            currentSwiperIndex: currentSwiperIndex
                        })
                    } else if (res.cancel) {

                    }
                }
            })
        }
    },
    // 场景选择
    editChooseScene: function(e) {
        var page = this;
        page.setData({
            isEditChoose: true,
            isOperateCard: false,
            isShowTextarea: false,
            isShowBack: false
        })
    },
    //卡片操作
    cardOperate: function() {
        var page = this;
        var isOperateCard = page.data.isOperateCard;
        if (isOperateCard) {
            page.setData({
                isEditChoose: false,
                isOperateCard: false,
                isShowTextarea: true,
                isShowBack: true
            })
        } else {
            page.setData({
                isEditChoose: false,
                isOperateCard: true,
                isShowTextarea: false,
                isShowBack: false
            })
        }
    },
    editReturn: function(e) {
        var page = this;
        var sceneName = page.data.sceneName
        wx.showToast({
            title: '自动保存成功',
            icon: 'none'
        })
        page.setData({
            isEditChoose: false,
            isOperateCard: false,
            isShowTextarea: true,
            isShowBack: true
        })
    },
    changeSceneName: function(e) {
        var page = this;
        var value = e.detail.value;
        if (value == "") {
            page.setData({
                sceneName: "未命名的场景"
            })
        } else {
            page.setData({
                sceneName: value,
                changesceneName: value
            })
        }

    },
    // 选择场景类别
    chooseSceneType: function(e) {
        var page = this;
        var categoryName = page.data.categoryName;
        wx.navigateTo({
            url: '/pages/category/category?categoryName=' + categoryName,
        })
    },
    //选择语言
    setChooseLang: function(e) {
        wx.navigateTo({
            url: '/pages/myAccount/learnLanguage/learnLanguage?chooseType=editSceneLang',
        })
    },
    //删除卡片
    deleteCard: function() {
        var page = this;
        var isShowDelete = page.data.isShowDelete;
        if (isShowDelete) {
            page.setData({
                isShowDelete: false,
                isShowSort: false
            })
        } else {
            page.setData({
                isShowDelete: true,
                isShowSort: false
            })
        }
    },
    //调整次序
    sortOperate: function() {
        var page = this;
        var isShowSort = page.data.isShowSort;
        if (isShowSort) {
            page.setData({
                isShowDelete: false,
                isShowSort: false
            })
        } else {
            page.setData({
                isShowDelete: false,
                isShowSort: true
            })
        }
    },
    //删除单张卡片
    deleteOneCard: function(e) {
        var page = this;
        var index = e.currentTarget.dataset.index
        var listDetail = page.data.listDetail;
        var listDetailName = page.data.listDetailName;
        var currentText = page.data.currentText;
        var currentAudio = page.data.currentAudio;
        var selectedLanguageCode = page.data.selectedLanguageCode;
        var currentSwiperIndex = page.data.currentSwiperIndex;
        if (listDetail.length - 1 == 1) {
            wx.showToast({
                title: '您当前只有一张卡片，不可删除',
                icon: "none"
            })
        } else {
            wx.showModal({
                title: '删除卡片',
                content: '此操作不可恢复，确定删除？',
                success: function(res) {
                    if (res.confirm) {
                        if (index == currentSwiperIndex && index == listDetail.length - 1) {
                            currentText = listDetail[index - 1].names[selectedLanguageCode]
                            currentAudio = listDetail[index - 1].audios[selectedLanguageCode]
                            currentSwiperIndex = index - 1
                        } else if (index == currentSwiperIndex && index !== listDetail.length - 1) {
                            currentText = listDetail[index + 1].names[selectedLanguageCode]
                            currentAudio = listDetail[index + 1].audios[selectedLanguageCode]
                            currentSwiperIndex = index
                        }
                        listDetail.splice(index, 1);
                        listDetailName.splice(index, 1);

                        page.setData({
                            listDetail: listDetail,
                            listDetailName: listDetailName,
                            currentText: currentText,
                            currentAudio: currentAudio,
                            currentSwiperIndex: currentSwiperIndex
                        })
                    } else if (res.cancel) {

                    }
                }
            })
        }
    },
    descSort: function(e) {
        var page = this;
        var index = e.currentTarget.dataset.index;
        var listDetail = page.data.listDetail;
        var listDetailName = page.data.listDetailName
        swapItems(page, listDetail, listDetailName, index, index + 1)
    },
    escSort: function(e) {
        var page = this;
        var index = e.currentTarget.dataset.index;
        var listDetail = page.data.listDetail;
        var listDetailName = page.data.listDetailName
        swapItems(page, listDetail, listDetailName, index, index - 1)
    }
});
// 图片裁剪
var oneTouchStart = ({
    page
}, touch) => {
    touchX0 = touch.x;
    touchY0 = touch.y
}

var oneTouchMove = ({
    page
}, touch) => {
    var xMove = void 0;
    var yMove = void 0;
    if (self.touchended) {
        return
    }
    xMove = touch.x - touchX0;
    yMove = touch.y - touchY0;
    imgLeft = rectX + xMove;
    imgTop = rectY + yMove;
    // outsideBound(imgLeft, imgTop)
    updateCanvas({
        page
    })
}

var twoTouchStart = ({
    page
}, touch0, touch1) => {
    var xMove = void 0;
    var yMove = void 0;
    touchX1 = rectX + scaleWidth / 2;
    touchY1 = rectY + scaleHeight / 2;
    xMove = touch1.x - touch0.x;
    yMove = touch1.y - touch0.y;
    oldDistance = Math.sqrt(xMove * xMove + yMove * yMove);
}

var twoTouchMove = ({
    page
}, touch0, touch1) => {
    var xMove = void 0;
    var yMove = void 0;
    var scale = page.data.scale;
    var zoom = page.data.zoom;
    xMove = touch1.x - touch0.x;
    yMove = touch1.y - touch0.y;
    newDistance = Math.sqrt(xMove * xMove + yMove * yMove)
    newScale = oldScale + 0.001 * zoom * (newDistance - oldDistance);
    if (newScale < 1) {
        newScale = 1
    } else if (newScale >= scale) {
        newScale = scale
    }
    scaleWidth = newScale * page.data.destWidth;
    scaleHeight = newScale * page.data.destHeight;
    imgLeft = touchX1 - scaleWidth / 2;
    imgTop = touchY1 - scaleHeight / 2;
    // outsideBound(imgLeft, imgTop)
    updateCanvas({
        page
    })
}
var xtouchEnd = () => {
    oldScale = newScale;
    rectX = imgLeft;
    rectY = imgTop;
}

var updateCanvas = ({
    page
}) => {
    const ctx = wx.createCanvasContext("imgCanvas")
    var imgPath = page.data.imgPath;
    ctx.drawImage(imgPath, imgLeft, imgTop, scaleWidth, scaleHeight);
    ctx.draw()
}

function setTouchState(instance) {
    for (var _len = arguments.length, arg = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
        arg[_key - 1] = arguments[_key];
    }
    TOUCH_STATE.forEach(function(key, i) {

        if (arg[i] !== undefined) {
            instance[key] = arg[i];
        }
    });
}
var outsideBound = function(imgLeft, imgTop) {
    var x = 0;
    var y = 0;
    imgLeft = imgLeft >= x ? x : scaleWidth + imgLeft - x <= width ? x + width - scaleWidth : imgLeft;
    imgTop = imgTop >= y ? y : scaleHeight + imgTop - y <= height ? y + height - scaleHeight : imgTop;
}

function swapItems(page, arr, arr2, index1, index2) {
    arr[index1] = arr.splice(index2, 1, arr[index1])[0];
    arr2[index1] = arr2.splice(index2, 1, arr2[index1])[0];
    var currentText = page.data.currentText;
    var currentAudio = page.data.currentAudio;
    var selectedLanguageCode = page.data.selectedLanguageCode;
    var currentSwiperIndex = page.data.currentSwiperIndex;
    if (index1 == currentSwiperIndex) {
        currentText = arr[index1].names[selectedLanguageCode]
        currentAudio = arr[index1].audios[selectedLanguageCode]
    }
    page.setData({
        listDetail: arr,
        listDetailName: arr2,
        currentText: currentText,
        currentAudio: currentAudio
    })
}
var setLeadingScene = ({
    page
}) => {
    var listDetail = page.data.listDetail
    listDetail.pop();
    var categoryName = page.data.categoryName;
    var sceneName = page.data.sceneName;
    if (categoryName == "选择场景类别") {
        categoryName = ""
    } else {
        categoryName = categoryName
    }
    if (sceneName == "未命名的场景") {
        sceneName = ""
    } else {
        sceneName = sceneName
    }
    var data = {
        author: wx.getStorageSync("loginInfo").userId,
        cards: listDetail,
        category: categoryName,
        creator: wx.getStorageSync("creator"),
        languages: wx.getStorageSync("copydestLang").payload,
        name: sceneName,
        status: 0,
        userid: wx.getStorageSync("loginInfo").userId,
        version: 1
    }
    var token = wx.getStorageSync("loginInfo").id
    Find.setleadingMyScene(data, token).then((res) => {
        wx.showToast({
            title: '上传场景中，请稍候',
            icon: "none"
        })
    }, () => {

    })
}

var translateLang = ({
    page
}) => {
    var data = {};
    var language = wx.getStorageSync("copydestLang").payload;
    var listDetail = page.data.listDetail;
    var currentSwiperIndex = page.data.currentSwiperIndex;
    var selectedLanguageIndex = page.data.selectedLanguageIndex;
    var app_id = "AugrroIhpUzNAlZRutkeWkj6";
    var app_secret = "c7bf9a1b1eeb4c9180fa72978ce4bb2b";
    Translate.getBaiduTranslateToken(data, app_id, app_secret).then((res) => {
        // var texts = encodeURI(text);
        var accessToken = res.access_token;
        var fromText = listDetail[currentSwiperIndex].names[language[0]];
        for (var i = 0; i < language.length; i++) {
            // var fromText = listDetail[currentSwiperIndex].names[language[0]];
            // if (listDetail[currentSwiperIndex].names[language[i]] == undefined || listDetail[currentSwiperIndex].names[language[i]] == "") {
            var fromCode = language[0];
            var toCode = language[i];
            baiDutranslate({
                page
            }, fromCode, toCode, fromText, language, listDetail, currentSwiperIndex, accessToken)
            // }
        }
        var judge = "tsnVoice"
        downloadVoice({
            page
        }, fromText, accessToken, judge, language[0])

    })
}

var baiDutranslate = ({
    page
}, fromCode, toCode, fromText, language, listDetail, currentSwiperIndex, accessToken) => {
    var baiduLangCodes = {
        zh: 'zh',
        en: 'en',
        zh_YY: 'yue',
        ja: 'jp',
        ko: 'kor',
        fr: 'fra',
        es: 'spa',
        th: 'th',
        ar: 'ara',
        ru: 'ru',
        pt: 'pt',
        de: 'de',
        it: 'it',
        el: 'el',
        nl: 'nl',
        pl: 'pl',
        bg: 'bul',
        et: 'est',
        da: 'dan',
        fi: 'fin',
        cs: 'cs',
        ro: 'rom',
        sl: 'slo',
        sv: 'swe',
        hu: 'hu',
        vi: 'vie',
    };
    var salt = new Date().getTime();
    let baiduID = "20160403000017497";
    var baiduKey = "mLjszIYtLXtOIy_NvC84";
    var credential = baiduID + fromText + salt + baiduKey;
    var sign = Utils.MD5(credential);
    var fromLang = baiduLangCodes[fromCode];
    var toLang = baiduLangCodes[toCode];
    var selectedLanguageIndex = page.data.selectedLanguageIndex;
    var destLang = wx.getStorageSync("copydestLang").payload;
    var data = {
        q: fromText,
        from: fromLang,
        to: toLang,
        appid: baiduID,
        salt: salt,
        sign: sign,
    };
    Translate.getbaiduFanyi(data).then((res) => {
        wx.showToast({
            title: '已自动翻译所有未填写并支持机器翻译的语言',
            icon: "none"
        })
        console.log(res)
        var fanyiLang = res.trans_result[0].dst;
        for (var i = 0; i < language.length; i++) {
            if (language[i] == toCode) {
                listDetail[currentSwiperIndex].names[toCode] = fanyiLang;

            }
        }
        page.setData({
            listDetail: listDetail,
            currentText: listDetail[currentSwiperIndex].names[destLang[selectedLanguageIndex]]
        })
        var judge = "tsnVoice"
        if (toCode != "zh" && toCode != "en") {
            return;
        } else {
            downloadVoice({
                page
            }, fanyiLang, accessToken, judge, toCode)
        }

    })
}
var uploadTape = ({
    page
}, tempFilePaths, judge, tsnCode) => {
    var userId = wx.getStorageSync("loginInfo").userId;
    var urlPrefix = userId + '/' + "audio/";
    var urlSuffix = '';
    var dotIdx = tempFilePaths.lastIndexOf('.');
    if (dotIdx > 0) {
        urlSuffix = tempFilePaths.substr(dotIdx);
    }
    var secret = "ewy219lrC-dScQss4nY96cvyoIAxIoAat5Vc_yH3";
    var access = "hkqZpCDJI6_lN4tqlvSNdcN2TlqQROyLKKQEgpAS"
    var raw = Utils.newPolicyMp3(urlPrefix, urlSuffix);
    var policy = Utils.base64encode(JSON.stringify(raw));
    var sign = Utils.b64_hmac_sha1(secret, policy).replace(/\//g, '_').replace(/\+/g, '-');
    var uptoken = access + ":" + sign + ":" + policy;
    var data = {
        values: {
            token: uptoken
        },
        files: {
            file: tempFilePaths
        }
    }
    wx.uploadFile({
        url: 'https://upload-z2.qiniup.com',
        filePath: tempFilePaths,
        name: 'file',
        formData: {
            // values: {
            token: uptoken,
            file: tempFilePaths
            // }
        },
        success: function(res) {
            var selectedLanguageIndex = page.data.selectedLanguageIndex;
            var listDetail = page.data.listDetail;
            var currentSwiperIndex = page.data.currentSwiperIndex;
            var destLang = wx.getStorageSync("copydestLang").payload;
            var data = JSON.parse(res.data)
            var key = data.key
            if (judge == "tsnVoice") {
                listDetail[currentSwiperIndex].audios[tsnCode] = "http://cdn-users-pub.xlingual.net/" + key
            } else {
                listDetail[currentSwiperIndex].audios[destLang[selectedLanguageIndex]] = "http://cdn-users-pub.xlingual.net/" + key
            }

            page.setData({
                listDetail: listDetail,
                currentAudio: listDetail[currentSwiperIndex].audios[destLang[selectedLanguageIndex]] || listDetail[currentSwiperIndex].audios[tsnCode]
            })
        }
    });
}

// 上传图片
var uploadImg = ({
    page
}, tempFilePaths) => {
    var userId = wx.getStorageSync("loginInfo").userId;
    var urlPrefix = userId + '/' + "img/";
    var urlSuffix = '';
    var dotIdx = tempFilePaths.lastIndexOf('.');
    if (dotIdx > 0) {
        urlSuffix = tempFilePaths.substr(dotIdx);
    }
    var secret = "ewy219lrC-dScQss4nY96cvyoIAxIoAat5Vc_yH3";
    var access = "hkqZpCDJI6_lN4tqlvSNdcN2TlqQROyLKKQEgpAS"
    var raw = Utils.newPolicy(urlPrefix, urlSuffix);
    var policy = Utils.base64encode(JSON.stringify(raw));
    var sign = Utils.b64_hmac_sha1(secret, policy).replace(/\//g, '_').replace(/\+/g, '-');
    var uptoken = access + ":" + sign + ":" + policy;
    var data = {
        values: {
            token: uptoken
        },
        files: {
            file: tempFilePaths
        }
    }
    wx.uploadFile({
        url: 'https://upload-z2.qiniup.com',
        filePath: tempFilePaths,
        name: 'file',
        formData: {
            // values: {
            token: uptoken,
            file: tempFilePaths
            // }
        },
        success: function(res) {
            var listDetail = page.data.listDetail;
            var currentSwiperIndex = page.data.currentSwiperIndex;
            var data = JSON.parse(res.data)
            var key = data.key;
            listDetail[currentSwiperIndex].img = "http://cdn-users.xlingual.net/" + key + "/wm.jpg"
            page.setData({
                listDetail: listDetail,
            })
        }
    });
}

function setNewTime(page) {
    var second = ("" + (100 + recordLength % 60)).substr(1);
    var minute = Math.floor(recordLength / 60);
    recordLength += 1;
    page.setData({
        minute: "" + minute,
        second: second
    })
    timingTime = setTimeout(function() {
        setNewTime(page)
    }, 1000)
}


var downloadVoice = function({
    page
}, texts, accessToken, judge, code) {
    var voiceText = encodeURI(texts)
    wx.downloadFile({
        url: "https://tsn.baidu.com/text2audio?tex=" + voiceText + "&lan=zh&cuid=xlingual&ctp=1&per=3&tok=" + accessToken,
        // header: {
        //     'content-type': 'audio/mp3',
        // },
        success: function(res) {
            if (res.statusCode === 200) {
                var filePath = res.tempFilePath;
                // var judge = "tsnVoice"
                uploadTape({
                    page
                }, filePath, judge, code)
            }
        }
    })
}