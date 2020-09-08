var config = require("../common/config.js");
var httpclient = require("../common/httpclient.js");

var unifiedorder = (data) => {
    var header = {};
    return httpclient.post({
        url: config.jmapiHost + "/xlingual/wxpay/v1/unifiedorder",
        data: data,
        header: header
    })
}
module.exports = {
    unifiedorder
}