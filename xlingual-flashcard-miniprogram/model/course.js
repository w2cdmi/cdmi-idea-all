var config = require("../common/config.js");
var httpclient = require("../common/httpclient.js");
// 获取课程商店列表
var getListCourse = (data) => {
    var header = {}
    return httpclient.get({
        url: config.host + "/ac/list_courses",
        data: data,
        header: header
    })
}
//获取课程邀请码
var inviteCode = (data) =>{
    var header = {};
    return httpclient.post({
        url: config.host + "/ac/get_invite_code/",
        data: data,
        header: header
    })
}

// 购买课程套餐
var purchaseCourse = (data) =>{
    var header = {};
    return httpclient.post({
        url: config.host + "/ac/purchase_course/",
        data: data,
        header: header
    })
}
module.exports = {
    getListCourse,
    inviteCode,
    purchaseCourse
}