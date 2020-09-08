var config = require("../common/config.js");
var httpclient = require("../common/httpclient.js");
// 获取用户信息
var userInfo = (data) => {
    var header = {
    };
    return httpclient.post({
        url: config.host + '/ac/user_info/',
        data: data,
        header: header
    });
}
var accountInfo = (data, uid, id) => {
    var header = {
        Authorization: id
    };
    return httpclient.get({
        url: config.host + '/ac/api/user/' + uid,
        data: data,
        header: header
    });
}
var putAccountInfo = (data, uid, id) => {
    var header = {
        Authorization: id
    };
    return httpclient.put({
        url: config.host + '/ac/api/user/' + uid,
        data: data,
        header: header
    });
}
// 用户登录
var userLogin = (data) => {
    var header = {

    };
    return httpclient.post({
        url: config.host + '/ac/api/user/login',
        data: data,
        header: header
    });
}

// 用户注销
var userLogout = (data, token) => {
    var header = {
        Authorization: token
    };
    return httpclient.post({
        url: config.host + '/ac/api/user/logout',
        data: data,
        header: header
    });
}

// 用户注销
var userLogout = (data, token) => {
    var header = {
        Authorization: token
    };
    return httpclient.post({
        url: config.host + '/ac/api/user/logout',
        data: data,
        header: header
    });
}

// 验证码
var registerCode = (getInviteCodeData) => {
    var header = {
        // Authorization: token
    };

    var verifyCodeHeader = {
        appkey: '1afdc6e4ed788',
        token: '5b4c592f7ff3dfff071027fa1',
        hash: '660e9219' 
    }

    return httpclient.post({
        url: config.host + '/ac/get_verification_code_wxmp/',
        // url: config.host + '/ac/get_invite_code/',
        data: getInviteCodeData,
        header: header
    });

    // httpclient.post({
    //     url: config.mobHost + '/verify/code',
    //     data: {},
    //     header: verifyCodeHeader
    // });
}


// 注册
var register = (data) => {
    var header = {
        // Authorization: token
    };

    return httpclient.post({
        url: config.host + '/ac/create_user_wxmp/',
        data: data,
        header: header
    });
}

// 重置密码
var resetPassword = (data) => {
    var header = {};

    return httpclient.post({
        url: config.host + '/ac/reset_password/',
        data: data,
        header: header
    });
}

module.exports = {
    userInfo,
    accountInfo,
    userLogin,
    putAccountInfo,
    userLogout,
    registerCode,
    register,
    resetPassword
}