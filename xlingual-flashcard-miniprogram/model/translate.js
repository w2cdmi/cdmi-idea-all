var config = require("../common/config.js");
var httpclient = require("../common/httpclient.js");
var Util = require("../common/utils.js");

var getBaiduTranslateToken = (data, app_id, app_secret) => {
    var header = {
        // Authorization: token
    };
    return httpclient.get({
        url: config.baiduHost + '/oauth/2.0/token?grant_type=client_credentials&client_id=' + app_id + '&client_secret=' + app_secret,
        data: data,
        header: header
    });
}
// 翻译
var baiduFanyi = (data) => {
    var header = {
        // Authorization: token
    };
    return httpclient.post({
        url: config.baiduFanyiHost + '/api/trans/vip/translate',
        data: data,
        header: header
    });
}
var getbaiduFanyi = (data) => {
    var header = {
        // Authorization: token
    };
    return httpclient.get({
        url: config.baiduFanyiHost + '/api/trans/vip/translate',
        data: data,
        header: header
    });
}

// 翻译语音
var voiceFanyi = (data) => {
    var header = {
        // Authorization: token
    };
    return httpclient.get({
        url: config.baiduTsnHost + '/text2audio',
        data: data,
        header: header
    });
}

// 微软翻译
var microsoftFanyi = (data, azureKey, params) => {
    var header = {
        'content-type': 'application/json',
        "Ocp-Apim-Subscription-Key": azureKey
    };
    return httpclient.post({
        url: config.microsoftTextTsnHost + '/translate?api-version=3.0' + params,
        data: data,
        header: header
    })
}

// 微软语音翻译token
var microsoftVoiceToken = (data, azureTtsKey) => {
    var header = {
        "Ocp-Apim-Subscription-Key": azureTtsKey
    };
    return httpclient.post({
        url: config.microsoftVoiceTokenHost + '/sts/v1.0/issueToken',
        data: data,
        header: header
    })
}
module.exports = {
    getBaiduTranslateToken,
    baiduFanyi,
    getbaiduFanyi,
    voiceFanyi,
    microsoftFanyi,
    microsoftVoiceToken
}