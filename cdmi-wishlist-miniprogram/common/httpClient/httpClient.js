// var session = require("../../session.js");
/**
 * 请求后台方法
 * url: 请求路径
 * params：请求参数；
 * params.isShowLoading：是否显示默认加载提示
 * params.method: 请求方式
 * params.data: 业务请求参数
 * params.header: 请求头信息
 */
const wxRequest = (url, params) => {
    if (params.isShowLoading) {
        wx.showLoading({
            title: '加载中',
            icon: 'loading',
            mask: true
        })
    }

    wx.request({
        url: url,
        method: params.method || 'GET',
        data: params.data || {},
        header: params.header || {
            'content-type': 'application/json'
        },
        success: (res) => {
            if (params.isShowLoading) {
                wx.hideLoading();
            }

            exceptionHandle(res);

            //返回200，才认为调用成功  201:表示创建成功
            if (res.statusCode == 200 || res.statusCode == 201) {
                //返回的信息中没有错误指示
                if (res.data.type == undefined || res.data.type != "error") {
                    (typeof params.success == 'function') && params.success(res.data);
                }
            }

            if (res.statusCode == 404) {
                if (typeof (res.data) != 'undefined' && res.data != "") {
                    if (res.data.code == "NoSuchFile" || res.data.code == "NoSuchItem") {
                        wx.showToast({
                            title: '文件已不存在！',
                        })
                    } else if (res.data.code === 'NoSuchLink') {
                        wx.showModal({
                            title: '提示',
                            content: '链接已失效',
                            showCancel: false,
                            success: (res) => {
                                if (res.confirm) {
                                    wx.navigateBack({
                                        delta: 1
                                    });
                                }
                            }
                        });
                        return 'INBoxisnotExist';
                    }
                    if (res.data.code === "LinkExpired") {
                        wx.redirectTo({
                            url: '/disk/shares/shareLinkFailure',
                        })
                    }
                }
            }

            if (res.statusCode == 403) {
                wx.showModal({
                    title: '提示',
                    content: '文件访问权限不足',
                    showCancel: false
                });
            }

            if (res.statusCode == 409) {
                if (typeof (res.data) != 'undefined' && res.data != "") {
                    if (res.data.code == "RepeatNameConflict") {
                        wx.showModal({
                            title: '提示',
                            content: '文件夹已经存在',
                            showCancel: false
                        })
                    } else if (res.data.code == "ExsitShortcut") {
                        wx.showModal({
                            title: '提示',
                            content: '快捷目录已存在',
                            showCancel: false
                        })
                    }
                }
            }

            if (res.statusCode == 401) {
                session.login({
                    enterpriseId: getApp().globalData.enterpriseId,
                });
                //等待登录成功后，执行
                session.invokeAfterLogin(function () {
                    wx.navigateBack({
                        delta: 0
                    });
                });
            }

            wx.hideLoading();
            return res;
        },
        fail: (res) => {
            wx.hideLoading();
            params.fail && params.fail(res);
            session.login({
                enterpriseId: 0
            });
        },
        complete: (res) => {
            params.complete && params.complete(res);
        }
    });
}

function get(url, data, header, success, fail, complete, isShowLoading) {
    return wxRequest(url, {
        "data": data,
        "header": header,
        "method": "GET",
        success: success,
        fail: fail,
        complete: complete,
        isShowLoading: isShowLoading
    });
}

function put(url, data, header, success, fail, complete, isShowLoading) {
    wxRequest(url, {
        data: data,
        header: header,
        method: "PUT",
        success: success,
        fail: fail,
        complete: complete,
        isShowLoading: isShowLoading
    });
}

function post(url, data, header, success, fail, complete, isShowLoading) {
    wxRequest(url, {
        data: data,
        header: header,
        method: "POST",
        success: success,
        fail: fail,
        complete: complete,
        isShowLoading: isShowLoading
    });
}

function remove(url, data, header, success, fail, complete, isShowLoading) {
    wxRequest(url, {
        data: data,
        header: header,
        method: "DELETE",
        success: success,
        fail: fail,
        complete: complete,
        isShowLoading: isShowLoading
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
    } else if (res.data.code === "NoSuchParent") {
        wx.showModal({
            title: '提示',
            content: '父目录不存在',
            showCancel: false
        });
    } else if (res.data.code === "NoSuchSource") {
        wx.showModal({
            title: '提示',
            content: '源文件或文件夹不存在',
            showCancel: false
        });
    } else if (res.data.code === "NoSuchDest") {
        wx.showModal({
            title: '提示',
            content: '目标文件或文件夹不存在',
            showCancel: false
        });
    } else if (res.data.code === "SubFolderConflict") {
        wx.showModal({
            title: '提示',
            content: '不能移动子目录下',
            showCancel: false
        });
    } else if (res.data.code === "SameNodeConflict") {
        wx.showModal({
            title: '提示',
            content: '目标文件夹与源文件夹相同',
            showCancel: false
        });
    } else if (res.data.code === "SameParentConflict") {
        wx.showModal({
            title: '提示',
            content: '目标文件夹已在该目录下',
            showCancel: false
        });
    } else if (res.data.code === "UserLocked") {
        wx.showModal({
            title: '提示',
            content: '用户被锁定',
            showCancel: false
        });
    } else if (res.data.code === "ExistMemberConflict") {
        wx.showModal({
            title: '提示',
            content: '成员已存在',
            showCancel: false
        });
    } else if (res.data.code === "ExistTeamspaceConflict") {
        wx.showModal({
            title: '提示',
            content: '协作空间已存在',
            showCancel: false
        });
    } else if (res.data.code === "ExceedQuota") {
        wx.showModal({
            title: '提示',
            content: '空间容量不足',
            showCancel: false
        });
    } else if (res.data.code === "ExceedUserAvailableSpace") {
        wx.showModal({
            title: '提示',
            content: '空间容量不足',
            showCancel: false
        });
    } else if (res.data.code === "UploadSizeTooLarge") {
        wx.showModal({
            title: '提示',
            content: '上传文件大小超过限制',
            showCancel: false
        });
    } else if (res.data.code === "ExsitShortcut") {
        wx.showModal({
            title: '提示',
            content: '快捷目录已经存在',
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
