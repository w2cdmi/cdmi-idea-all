var Language = require("../../common/language.js");
var Find = require("../../model/find.js");
var Translate = require("../../model/translate.js");
var Upload = require("../../model/upload.js");
var Utils = require("../../common/utils.js");
var config = require("../../common/config.js");

var azureKey = "fa3bdecdf0b143718da7c420b8411811"
var azureId = "dac030b4-d190-46a6-99d3-0daea4540b0d"
var azureTtsKey = "b88e5aa902d44e5998cad90959ef3acc"
var azureLangCodes = {
    ar: 'ar',
    da: 'da',
    de: 'de',
    en: 'en',
    es: 'es',

    fi: 'fi',
    fr: 'fr',
    iw: 'he',
    hi: 'hi',
    hu: 'hu',
    id: 'id',
    it: 'it',
    ja: 'ja',
    ko: 'ko',
    ms: 'ms',
    nl: 'nl',
    pl: 'pl',
    pt: 'pt',
    ru: 'ru',
    sv: 'sv',
    ta: 'ta',
    tr: 'tr',
    vi: 'vi',
    zh_YY: 'yue',
    zh: 'zh-Hans',
};

var ttsLanguages = {
    ar: {
        lang: 'ar-EG',
        name: 'Arabic (Egypt)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (ar-EG, Hoda)"
    },
    da: {
        lang: 'da-DK',
        name: 'Danish',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (da-DK, HelleRUS)"
    },
    de: {
        lang: 'de-DE',
        name: 'German (Germany)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (de-DE, Hedda) "
    },
    en: {
        lang: 'en-US',
        name: 'English (US)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (en-US, Jessa24kRUS)"
    },
    es: {
        lang: 'es-ES',
        name: 'Spanish (Spain)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (es-ES, Laura, Apollo)"
    },
    fi: {
        lang: 'fi-FI',
        name: 'Finnish',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (fi-FI, HeidiRUS)"
    },
    fr: {
        lang: 'fr-FR',
        name: 'French (France)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (fr-FR, Julie, Apollo)"
    },
    he: {
        lang: 'he-IL',
        name: 'Hebrew (Israel)',
        gender: 'Male',
        speaker: "Microsoft Server Speech Text to Speech Voice (he-IL, Asaf)"
    },
    hi: {
        lang: 'hi-IN',
        name: 'Hindi (India)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (hi-IN, Kalpana, Apollo)"
    },
    hu: {
        lang: 'hu-HU',
        name: 'Hungarian',
        gender: 'Male',
        speaker: "Microsoft Server Speech Text to Speech Voice (hu-HU, Szabolcs)"
    },
    id: {
        lang: 'id-ID',
        name: 'Indonesian',
        gender: 'Male',
        speaker: "Microsoft Server Speech Text to Speech Voice (id-ID, Andika)"
    },
    it: {
        lang: 'it-IT',
        name: 'Italian',
        gender: 'Male',
        speaker: "Microsoft Server Speech Text to Speech Voice (it-IT, Cosimo, Apollo)"
    },
    ja: {
        lang: 'ja-JP',
        name: 'Japanese',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (ja-JP, Ayumi, Apollo)"
    },
    ko: {
        lang: 'ko-KR',
        name: 'Korean',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (ko-KR, HeamiRUS)"
    },
    ms: {
        lang: 'ms-MY',
        name: 'Malay',
        gender: 'Male',
        speaker: "Microsoft Server Speech Text to Speech Voice (ms-MY, Rizwan)"
    },
    nl: {
        lang: 'nl-NL',
        name: 'Dutch',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (nl-NL, HannaRUS)"
    },
    pl: {
        lang: 'pl-PL',
        name: 'Polish',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (pl-PL, PaulinaRUS)"
    },
    pt: {
        lang: 'pt-PT',
        name: 'Portuguese (Portugal)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (pt-PT, HeliaRUS)"
    },
    ru: {
        lang: 'ru-RU',
        name: 'Russian',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (ru-RU, Irina, Apollo)"
    },
    sv: {
        lang: 'sv-SE',
        name: 'Swedish',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (sv-SE, HedvigRUS)"
    },
    ta: {
        lang: 'ta-IN',
        name: 'Tamil (India)',
        gender: 'Male',
        speaker: "Microsoft Server Speech Text to Speech Voice (ta-IN, Valluvar)"
    },
    tr: {
        lang: 'tr-TR',
        name: 'Turkish',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (tr-TR, SedaRUS)"
    },
    vi: {
        lang: 'vi-VN',
        name: 'Vietnamese',
        gender: 'Male',
        speaker: "Microsoft Server Speech Text to Speech Voice (vi-VN, An)"
    },
    'zh-YY': {
        lang: 'zh-HK',
        name: 'Chinese (Hong Kong)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (zh-HK, Tracy, Apollo)"
    },
    'zh': {
        lang: 'zh-CN',
        name: 'Chinese (Mainland)',
        gender: 'Female',
        speaker: "Microsoft Server Speech Text to Speech Voice (zh-CN, Yaoyao, Apollo)"
    }
};
var uploadTape = function({
    page
}, tempFilePaths, judge, tsnCode) {
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
            wx.showToast({
                title: '已自动翻译所有未填写并支持机器翻译的语言',
                icon: "none"
            })
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

var downVoice = function({
    page
}, lang, text, access_token) {
    if (!ttsLanguages[lang]) {
        lang = lang.split('_')[0];
    }
    var ttsMetadata = ttsLanguages[lang];
    var body = '<speak version="1.0" xmlns="http://www.w3.org/2001/10/synthesis" xml:lang="' +
        ttsMetadata.lang + '">' +
        '<voice name="' + ttsMetadata.speaker + '">' +
        text +
        '</voice></speak>';
    var url = 'https://www.xlingual.net/ac/api/azure/magic?token=' + encodeURIComponent(access_token) + '&body=' + encodeURIComponent(body);
    wx.downloadFile({
        url: url,
        header: {
            "accept-encoding": "identity"
        },
        type: 'audio',
        success: function(res) {
            var filePath = res.tempFilePath;
            console.log(res)
            var judge = "tsnVoice"
            if (res.statusCode == 200) {
                uploadTape({
                    page
                }, filePath, judge, lang)
            }
        }
    })
}

// 获取微软语音token
var getMicrosoftVoiceToken = function({
    page
}) {
    var data = {}
    var listDetail = page.data.listDetail;
    var currentSwiperIndex = page.data.currentSwiperIndex;
    var selectedLanguageIndex = page.data.selectedLanguageIndex;
    var names = listDetail[currentSwiperIndex].names
    Translate.microsoftVoiceToken(data, azureTtsKey).then((res) => {
        var access_token = res
        for (var key in names) {
            var lang = key;
            var text = names[key]
            downVoice({
                page
            }, lang, text, access_token)
        }
    })
}

var microsoftTextTsn = function({
    page
}) {
    var data = {};
    var language = wx.getStorageSync("copydestLang").payload;
    var listDetail = page.data.listDetail;
    var currentSwiperIndex = page.data.currentSwiperIndex;
    var selectedLanguageIndex = page.data.selectedLanguageIndex;
    var fromLang = azureLangCodes[language[0]];
    var text = listDetail[currentSwiperIndex].names[language[0]]
    var toList = language;
    if (!fromLang) {
        wx.showToast({
            title: '该语言不支持翻译',
            icon: "none"
        })
    }
    if (toList.length != 0) {
        var reverseMap = {}
        var params = toList.map(function(to) {
            var code = azureLangCodes[to];
            if (!code) {
                code = azureLangCodes[to.split('_')[0]];
            }
            if (code) {
                reverseMap[code] = to;
            }
            return code;
        }).filter(function(to) {
            return !!to;
        });

        if (params.length == 0) {
            wx.showToast({
                title: '未找到合适翻译',
                icon: "none"
            })
        }

        params = "&from=" + fromLang + "&to=" + params.join("&to=");
        var data = [{
            Text: text
        }]
        // var params = "&from=zh-Hans&to=en&to=de&to=ar"
        Translate.microsoftFanyi(data, azureKey, params).then((res) => {
            if (res && res[0] && res[0].translations && res[0].translations.length > 0) {
                var results = {};
                res[0].translations.forEach(function(ret) {
                    results[reverseMap[ret.to]] = ret.text;
                });
                for (var i = 0; i < language.length; i++) {
                    listDetail[currentSwiperIndex].names[language[i]] = results[language[i]]
                }

                page.setData({
                    listDetail: listDetail,
                    currentText: listDetail[currentSwiperIndex].names[language[selectedLanguageIndex]]
                }, function() {
                    getMicrosoftVoiceToken({
                        page
                    })
                })
            }
        })
    } else {
        getMicrosoftVoiceToken({
            page
        })
    }


}

module.exports = {
    microsoftTextTsn
};