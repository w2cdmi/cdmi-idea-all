// pages/index/index.js
var session = require('../../common/session.js')
Page({

    /**
     * 页面的初始数据
     */
    data: {

    },
    bindgetuserinfo: function () {
        session.login();
    }
})