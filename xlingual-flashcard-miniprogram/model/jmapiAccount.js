var config = require("../common/config.js");
var httpclient = require("../common/httpclient.js");

// 获取用户信息
var userLogin = (data) => {
    var header = {
    };
    return httpclient.post({
        url: config.jmapiHost + '/xlingual/wxuser/v1/login',
        data: data,
        header: header
    });
}


module.exports = {
    userLogin,
}