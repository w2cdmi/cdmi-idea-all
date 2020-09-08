// console/school/index.js
/**
 * 品牌连锁招商加盟投资指南
 * 一流的投资项目，为财富增值，为创业圆梦
 * 万般领域，诸多机会，适合的才是最好的
 * 追随成功，成就梦想，有案例才有说服力
 * 看视频，析图文，听评论，这里的故事最真实
 * 
 * 运营方案：
 * 1. 第一阶段，我方帮忙重新设计招商文稿或用户提供。文稿形式为图片或视频。
 * 2. 用户上传招商方案可加入分销计划，成交有奖。
 * 3. 每个分享的人可以看到其分享记录，招商机构可以根据该记录给分享着返利。
 * 商家需配合：
 * 1. 素材输出；
 * 2. 调查表输出;(用以完善招商信息，线上自填不需要)
 * 3. 要求已加盟商家积极参与分享和点评，并设定奖励办法
 * 4. 商家需缴纳技术服务费以及保证金（如果评论中有负面评论或无评论，则会从保证金中扣除）
 * 
 * 
 * 小程序：您想找那方面的项目呢，我可以帮你快速筛选
 * 用户：说出行业或领域
 * 小程序：语音识别完成后，快速检索，告诉用户符合条件的项目数。
 * 小程序：告诉用户，是否要播放项目还是用户自行查看。
 * 用户：回答播放项目
 * 小程序：一一播放符合条件的视频
 * 用户：回答自行查找，那么只显示检索结果列表,不进入播放 
 * 如果播放过程用户退出，则返回检索结果列表
 */
Page({

    /**
     * 页面的初始数据
     */
    data: {
        app_id: '',
        app_name: '',
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function(options) {
        //检查必要的参数：appid
        if (options.appid == null || options.appid == "") {
            wx.showToast({
                title: '缺失必要的请求参数[id]，请联系开发商',
            });
            return;
        };
        wx.setNavigationBarTitle({
            title: '应用后台管理',
        });

        //获取该应用信息
        var _this = this;
        const db = wx.cloud.database();
        db.collection('apps').doc(options.appid).get().then(res => {
            if (res.data == null) {
                wx.showToast({
                    title: '应用信息不存在，请退出小程序重试',
                });
                return;
            }
            _this.setData({
                console_path: res.data.consolePath,
                app_id: options.appid,
                app_name: res.data.name,
            });
            //必须：修改当前操作的接入应用
            getApp().globalData.consoleAppId = options.appid;
        });
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function() {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function() {

    },

    /**
     * 跳转到课程类目页面
     */
    jumpToCourseCataloies: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'cataloies?marker=' + dataset.marker + '&channelmarker=' + dataset.channel,
        })
    },


    /**
     * 跳转到分类标记列表页面
     */
    jumpToMarkers: function() {
        wx.navigateTo({
            url: 'markers',
        })
    },

    /**
     * 跳转到频道列表页面
     */
    jumpToChannels: function(e) {
        const dataset = e.currentTarget.dataset;
        wx.navigateTo({
            url: 'channels?marker=' + dataset.marker,
        })
    },

    /**
     * 跳转到培训机构页面
     */
    jumpToShops: function() {
        wx.navigateTo({
            url: 'trainplace/shopes',
        })
    },

    /**
     * 跳转到招商项目草稿
     */
    jumpToLeagueProject: function() {
        wx.navigateTo({
            url: 'project',
        })
    },

    /**
     * 跳转到招商项目草稿
     */
    jumpToReleaseProject: function() {
        wx.navigateTo({
            url: 'releaseproject',
        })
    },

    /**
     * 跳转到广告位管理
     */
    jumpToAdPannel: function() {
        wx.navigateTo({
            url: 'ad/index',
        })
    },

    jumpToTest:function(){
        wx.navigateTo({
            url: 'test/index',
        })
    }
})