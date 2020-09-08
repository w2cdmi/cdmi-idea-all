// 云函数入口文件
const cloud = require('wx-server-sdk')

cloud.init()

// 云函数入口函数
exports.main = async (event, context) => {
    console.info(event);
    console.info(context);
    return {
        openid : cloud.getWXContext().OPENID,
        appid: cloud.getWXContext().APPID,
        unionid: cloud.getWXContext().UNIONID,
    };
}