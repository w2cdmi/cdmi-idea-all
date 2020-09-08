// 云函数入口文件
const cloud = require('wx-server-sdk');
var https = require('https'); //引入https模块
var url = require('url'); //引入url模块

cloud.init()

// 云函数入口函数
exports.main = async(event, context) => {
    console.info(event);
    if (event.mobile == null) {
        return {
            status: -1,
            error: '手机号码未传入!'
        }
    }
    //必填,请参考"开发准备"获取如下数据,替换为实际值
    var realUrl = 'https://api.rtc.huaweicloud.com:10443/sms/batchSendSms/v1'; //APP接入地址+接口访问URI
    var appKey = '2LKCSxrjz26NcLguH3yQ7r0xV2R8'; //APP_Key
    var appSecret = '164k0luFD1WQev7N6fIIyZY02ixs'; //APP_Secret
    var sender = 'csms18052531'; //签名通道号
    var templateId = '31a3fbd6e25f47c6afb3a2621c6e50d3'; //模板ID

    //必填,短信接收人号码,多个号码之间用英文逗号分隔
    var receiver = event.mobile;

    //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告
    var statusCallBack = '';

    /**
     * 选填,使用无变量模板时请赋空值 var templateParas = '';
     * 单变量模板示例:模板内容为"您的验证码是${NUM_6}"时,templateParas可填写为'["369751"]'
     * 双变量模板示例:模板内容为"您有${NUM_2}件快递请到${TXT_32}领取"时,templateParas可填写为'["3","人民公园正门"]'
     */
    /** 随机生成4位数字 */
    var code = "";
    for (var i = 0; i < 4; i++) {
        code += Math.floor(Math.random() * 10)
    }

    var templateParas = '["' + code + '"]'; //模板变量,查看更多模板变量规则

    var urlobj = url.parse(realUrl); //解析realUrl字符串并返回一个URL对象

    var options = {
        host: urlobj.hostname, //主机名
        port: urlobj.port, //端口
        path: urlobj.pathname, //URI
        method: 'POST', //请求方法为POST
        headers: { //请求Headers
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': 'WSSE realm="SDP",profile="UsernameToken",type="Appkey"',
            'X-WSSE': buildWsseHeader(appKey, appSecret)
        },
        rejectUnauthorized: false //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
    };

    var body = buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack); //请求Body

    var req = https.request(options, (res) => {
        console.log('statusCode:', res.statusCode); //打印响应码
        console.log('headers:', res.headers); //打印响应Headers

        res.setEncoding('utf8'); //设置响应数据编码格式
        res.on('data', (d) => {
            console.log('resp:', d); //打印响应数据
        });
    });
    req.on('error', (e) => {
        console.error(e.message); //请求错误时,打印错误信息
    });
    req.write(body); //发送请求Body数据
    req.end(); //结束请求
    console.log("发送请求体数据：" + body);
    return {
        status: 0,
        value: code,
    }
}

/**
 * 构造请求Body体
 */
function buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack) {
    var {
        URLSearchParams
    } = require('url');

    var ops = new URLSearchParams();
    ops.append('from', sender);
    ops.append('to', receiver);
    ops.append('templateId', templateId);
    ops.append('templateParas', templateParas);
    ops.append('statusCallback', statusCallBack);

    return ops.toString();
}

/**
 * 构造X-WSSE参数值
 */
function buildWsseHeader(appKey, appSecret) {
    var crypto = require('crypto');
    var util = require('util');

    var time = new Date(Date.now()).toISOString().replace(/.[0-9]+\Z/, 'Z');
    var nonce = crypto.randomBytes(16).toString('hex'); //随机数,推荐使用长度不低于8位
    var passwordDigestBase64Str = crypto.createHash('sha256').update(nonce + time + appSecret).digest('base64');

    return util.format('UsernameToken Username="%s",PasswordDigest="%s",Nonce="%s",Created="%s"', appKey, passwordDigestBase64Str, nonce, time);
}