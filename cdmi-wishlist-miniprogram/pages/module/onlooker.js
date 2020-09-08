var config = require("config.js");
var httpclient = require("httpclient.js");

function createOnlooker(data, callback) {
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.post(config.host + '/wishlist/crowdfundings/v1/onlooker', data, header, callback);
}

module.exports = {
    createOnlooker
};