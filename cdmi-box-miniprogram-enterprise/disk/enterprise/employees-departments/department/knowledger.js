var enterpriseClient = require("../../../module/enterprise.js");
var config = require("../../../config.js");

var deptId = 0;

Page({

    data: {

    },

    setKnowledger: function () {
        wx.navigateTo({
            url: '/disk/enterprise/template/selectEmploye?deptId=' + deptId + "&departmentName=" + this.data.departmentName + '&knowledger=1',
        })
    },

    onLoad: function (options) {

        deptId = options.deptId;
        var departmentName = options.departmentName;
        this.setData({
            deptId: deptId,
            departmentName: departmentName
        })
        if (deptId == undefined) {
            wx.showToast({
                title: '获取部门ID失败！',
                icon: 'none',
                duration: 1000
            })
            setTimeout(function () {
                wx.navigateBack({
                    delta: 1
                })
            }, 1000);
        }

        // deptId = options.deptId;
        // if (deptId == undefined) {
        //     wx.showToast({
        //         title: '获取部门ID失败！',
        //         icon: 'none',
        //         duration: 1000
        //     })
        //     setTimeout(function () {
        //         wx.navigateBack({
        //             delta: 1
        //         })
        //     }, 1000);
        // }
    },

})