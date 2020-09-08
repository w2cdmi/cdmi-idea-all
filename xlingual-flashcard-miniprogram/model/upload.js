var config = require("../common/config.js");
var httpclient = require("../common/httpclient.js");
var Util = require("../common/utils.js");

var uploadTape = (data) => {
    var header = {
        // Authorization: token
        // "content-type": "multipart/form-data"
    };
    return httpclient.post({
        url: 'https://upload-z2.qiniup.com',
        data: data,
        header: header
    });
}

module.exports = {
    uploadTape
}