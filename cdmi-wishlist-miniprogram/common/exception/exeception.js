function exceptionHandle(res, callBack) {
    if(res.data.code == "NoSuchFile" || res.data.code == "NoSuchItem") {
        wx.showModal({
            title: '提示',
            content: '文件已不存在！',
            showCancel: false
        });
    } else if (res.data.code === 'NoSuchLink') {
        wx.showModal({
            title: '提示',
            content: '链接已失效',
            showCancel: false
        });
    } else if (res.data.code === "LinkExpired"){
        wx.showModal({
            title: '提示',
            content: '链接已过期',
            showCancel: false
        });
    }else if (res.data.code === "SameParentConflict") {
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
    exceptionHandle
};