var Find = require("../../../model/find.js")

Page({
    data: {
        image: [],
        noteImage: '/pages/images/abilityTest/fail.png',
        hintImage: {
            win: '/pages/images/abilityTest/win.png',
            lose: '/pages/images/abilityTest/lose.png',
            fail: '/pages/images/abilityTest/fail.png',
            correct: '/pages/images/abilityTest/correct.png',
        },
        showNote: false,
        contentText: 'PE class',
        heartCount: 5,
        questionIndex: 0,
    },

    onLoad: function(options) {
        var isShared = options.isShared;
        this.setData({
            id: options.id,
            language: options.language,
            questionIndex: parseInt(options.index),
        });
        getDetail({
            page: this
        });
        this.audioCtx = wx.createAudioContext('myAudio');
    },

    abilityTest: function() {
        this.setData({
            showNote: true,
        });
    },

    hideNote: function() {
        if (this.data.heartCount == 0 || this.data.listDetail.cards.length === this.data.questionIndex) {
            wx.navigateBack({
                delta: 1
            });
        }

        this.setData({
            showNote: false,
        }, () => {
            list({
                page: this
            });
            this.audioCtx.play();
        });

    },

    selectImage: function(e) {
        var index = e.currentTarget.dataset.index;
        var item = e.currentTarget.dataset.item;
        if (this.data.heartCount - 1 == 0) {
            this.setData({
                noteImage: this.data.hintImage.lose,
                showNote: true,
            });
        }

        if (item === this.data.img) {
            if (this.data.listDetail.cards.length === this.data.questionIndex) {
                this.setData({
                    noteImage: this.data.hintImage.win,
                    showNote: true
                });
                return;
            }
            this.setData({
                noteImage: this.data.hintImage.correct,
                showNote: true,
            });
        } else {
            if (this.data.heartCount - 1 == 0) {
                this.setData({
                    noteImage: this.data.hintImage.lose,
                    showNote: true,
                    heartCount: this.data.heartCount - 1,
                });
                return;
            }
            this.setData({
                noteImage: this.data.hintImage.fail,
                heartCount: this.data.heartCount - 1,
                showNote: true,
            });
        }
    }
})


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
    var language = page.data.language;
    Find.getDetail(data)
        .then((res) => {
            page.setData({
                listDetail: res[0],
                currentText: res[0].cards[0].names[language],
                currentAudio: res[0].cards[0].audios[language]
            }, () => {
                list({
                    page
                });
                page.audioCtx.play();
            });
        }, () => {

        });
}

var list = ({
    page
}) => {
    var images = [];
    var cards = page.data.listDetail.cards;
    var listDetail = page.data.listDetail;
    cards.map((item, index) => {
        images.push(item.img);
    });

    var questionIndex = page.data.questionIndex;

    var currentQues = images.splice(questionIndex, 1);

    for (var i = 0; i < 3; i++) {
        var img = images.splice(Math.floor(Math.random() * images.length), 1)[0];
        currentQues.push(img);
    }

    var curImg = currentQues[0];

    var language = page.data.language;
    page.setData({
        randomArr: randomArr(currentQues),
        img: curImg,
        questionIndex: questionIndex + 1,
        currentText: cards[questionIndex].names[language],
        currentAudio: cards[questionIndex].audios[language]
    });
}

var randomArr = (arr) => {
    for (var i = 0; i < arr.length; i++) {
        var exchangeIndex = Math.floor(Math.random() * (arr.length - 1));
        var tem = arr[exchangeIndex];
        arr[exchangeIndex] = arr[i];
        arr[i] = tem;
    }
    return arr;
}

