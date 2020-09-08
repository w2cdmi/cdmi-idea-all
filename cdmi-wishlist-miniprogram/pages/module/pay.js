var config = require("config.js");
var httpclient = require("httpclient.js");

function unifiedorder(data, callback) {
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.post(config.host + '/wxpay/v1/unifiedorder', data, header, callback);
}

function buyProductUnifiedorder(data, callback){
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.post(config.host + '/wxpay/v1/unifiedorder', data, header, callback);
}

module.exports = {
    unifiedorder,
    buyProductUnifiedorder
};