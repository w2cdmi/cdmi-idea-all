// var session = require("./session.js");

const wxRequest = (url, params) => {
    return new Promise((resolve, reject) => {
        if (wx.getStorageSync('isShowLoading') !== false) {
            wx.showLoading({
                title: '加载中',
                icon: 'loading',
                mask: true
            });
        }
        wx.setStorageSync('isShowLoading', true);

        wx.request({
            url: url,
            method: params.method || 'GET',
            data: params.data || {},
            header: params.header || {
                'content-type': 'application/json'
            },
            success: (res) => {
                wx.hideLoading();

                //返回200，才认为调用成功  201:表示创建成功
                if (res.statusCode == 200 || res.statusCode == 201) {
                    // 执行回调
                    if (res.data.type == undefined || res.data.type != "error") {
                        resolve(res.data);
                    }
                } else {
                    //只要与服务器正常交互，就会回调此方法，哪怕返回4XX、5XX错误
                    // exceptionHandle(res);
                    reject(res);
                }
            },
            fail: (res) => {
                reject(res);
                wx.hideLoading();
                // session.login({
                //     enterpriseId: 0
                // });
            }
        });
    })
}

function put({ url, data, header }) {
    return wxRequest(url, {
        data: data,
        header: header,
        method: "PUT"
    });
}

function get({ url, data, header }) {
    return wxRequest(url, {
        "data": data,
        "header": header,
        "method": "GET"
    });
}

function post({ url, data, header }) {
    return wxRequest(url, {
        data: data,
        header: header,
        method: "POST"
    });
}

function remove({ url, data, header }) {
    return wxRequest(url, {
        data: data,
        header: header,
        method: "DELETE"
    });
}

//系统常见异常
function exceptionHandle(res) {
    if (res.data.code === "SameParentConflict") {
        wx.showModal({
            title: '提示',
            content: '相同目录不能进行操作',
            showCancel: false
        });
    }
}

module.exports = {
    put,
    get,
    post,
    remove
};