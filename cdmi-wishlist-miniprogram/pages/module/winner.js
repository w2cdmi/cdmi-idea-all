var config = require("config.js");
var httpclient = require("httpclient.js");

function getWinnerAll(callback) {
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.get(config.host + '/wishlist/crowdfundings/v1/winners', null, header, callback);
}

module.exports = {
    getWinnerAll
};