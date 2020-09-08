var config = require("config.js");
var httpclient = require("httpclient.js");

function isJoinLottery(productId, callback) {
    var header = {
        Authorization: getApp().globalData.token
    };
    return httpclient.get(config.host + '/wishlist/crowdfundings/v1/isJoinLottery/' + productId, null, header, callback);
}

module.exports = {
    isJoinLottery
};