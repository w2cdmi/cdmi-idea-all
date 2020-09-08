var config = require("../common/config.js");
var httpclient = require("../common/httpclient.js");
var Util = require("../common/utils.js");

var getList = (data) => {
    var header = {
        // Authorization: token
    };

    return httpclient.get({
        url: config.host + '/ac/api/scenario/',
        data: data,
        header: header
    });
}

var getTemplate = (data) => {
    var header = {
        // Authorization: token
    };

    return httpclient.get({
        url: config.host + '/ac/api/template/',
        data: data,
        header: header
    });
}

var getDetail = (data) => {
    var header = {
        // Authorization: token
    };

    return httpclient.get({
        url: config.host + '/ac/api/scenario/',
        data: data,
        header: header
    });
}

var getTemplateDetail = (data) => {
    return httpclient.get({
        url: config.host + '/ac/api/template/',
        data: data,
    });
}

// 获取我创建的场景
var getMyScreen = (data) => {
    return httpclient.get({
        url: config.host + '/ac/api/scenario/',
        data: data,
    });
}

// 删除场景
var deleteScreen = (id) => {
    return httpclient.remove({
        url: config.host + '/ac/api/scenario/' + id,
    });
}

// 发布场景
var releaseScreen = (id, data) => {
    var token = wx.getStorageSync("loginInfo").id;
    var header = {
        "authorization": token
    }
    return httpclient.put({
        url: config.host + '/ac/api/scenario/' + id,
        data: data,
        header: header
    });
}

//导入到我的场景
var setleadingMyScene = (data, token) => {
    var header = {
        Authorization: token
    };
    return httpclient.post({
        url: config.host + '/ac/api/scenario',
        data: data,
        header: header
    });
}

module.exports = {
    getList,
    getDetail,
    getTemplate,
    getTemplateDetail,
    getMyScreen,
    deleteScreen,
    releaseScreen,
    setleadingMyScene
}