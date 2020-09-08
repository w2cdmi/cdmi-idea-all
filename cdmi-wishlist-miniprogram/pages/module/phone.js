var httpClient = require('./httpclient.js');
var config = require('./config.js');

var getPhoneInfo = (data, callback) => {
    var header = {};
    httpClient.post(config.host + '', data, header, callback);
}

var getHistoryInfo = (data, callback) => {
    var header = {};
    httpClient.post(config.host + '', data, header, callback);
}

var setAddressInfo = (data, callback) => {
    var header = {};
    httpClient.post(config.host + '', data, header, callback);
}



module.exports = {
    getPhoneInfo,
    getHistoryInfo,
    setAddressInfo
}