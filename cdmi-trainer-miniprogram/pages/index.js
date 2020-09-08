// pages/index.js
var screenHeight;
var playerHeight = 0;
var playerTitleHeight = 0;
var sceneMemberHeight = 0;
var toolConfirmHeight = 0;
var toolBarHeight = 0; //底部操作条的高度
var currentGesture = 0; //标识手势,0为没有滑动，1为向上滑动，2为向下滑动
var scrollPostion = 0;  //会话区域滚动位置标识，0为正常，1为到顶，2为到底

Page({
  /**
   * 页面的初始数据
   */
  data: {
    viewPlayer: true,         //是否存在播放画面,页面初始化时候，等于hasPlayer
    hasPlayer: true,          //页面中是否有该控件
    hasPlayerTile: false,
    hasMultiPlayer: true,
    hasMultiPlayer: true,
    hasToolConfirm: true,
    lastX: 0, //滑动开始x轴位置
    lastY: 0, //滑动开始y轴位置
    sessionY: 0,
    playerY: 0,
    playerTitleY: 0,
    sceneMemberY: 0,
    toolConfirmY: 0
  },

  //滑动移动事件
  handletouchmove: function(event) {
    var currentX = event.touches[0].pageX
    var currentY = event.touches[0].pageY
    var tx = currentX - this.data.lastX
    var ty = currentY - this.data.lastY

    //左右方向滑动
    // if (Math.abs(tx) > Math.abs(ty)) {
    //   if (tx < 0)
    //     action = "向左滑动"
    //   else if (tx > 0)
    //     action = "向右滑动"
    // }

    // //上下方向滑动
    // else {
    if (ty < 0.5 && (tx < -0.5 || tx > 0.5)) {
      currentGesture = 1;
    } else if (ty > 0.5 && (tx < -0.5 || tx > 0.5)) {
      currentGesture = 2;
    }
    // }

    //将当前坐标进行保存以进行下一次计算
    this.data.lastX = currentX
    this.data.lastY = currentY
  },

  //滑动开始事件
  handletouchtart: function(event) {
    this.data.lastX = event.touches[0].pageX
    this.data.lastY = event.touches[0].pageY
  },

  //滑动结束事件
  handletouchend: function(event) {
    if ('scene-player' == event.currentTarget.id){
      if (currentGesture == 1) { //只有向上滑动才会隐藏播放画面
        this.setData({
          viewPlayer: false,
        });
        this.data.playerY = 0;
      }
    }else{
      //由于scollView控件无bindtouchmove事件，将事件放到它的内容子对象上

      if (currentGesture == 2 && scrollPostion == 1 && !this.data.viewPlayer){
        this.setData({
          viewPlayer: true,
        });
        this.data.playerY = playerHeight;
      } else if (currentGesture == 1  && scrollPostion == 2 && this.data.viewPlayer) {
        this.setData({
          viewPlayer: false,
        });
        this.data.playerY = 0;
      } else {
        //do nothing
      }
    }
    console.log("currentGesture:" + currentGesture + "--scrollPostion:" + scrollPostion);
    currentGesture = 0;
    //重新设置会话容器的高度
    this.reSetSessionContainerHeight();
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    //1.判断场景
    //2.对Data进行初始化
    //3.计算窗体高度
    wx.getSystemInfo({
      success: function(res) {
        // 可使用窗口高度
        screenHeight = res.windowHeight;
      }
    });
    //计算各个控件所占据的窗口高度
    wx.createSelectorQuery().select('#tool-bar').boundingClientRect(function(rect) {
      toolBarHeight = rect.height // 播放内容节点的高度
    }).exec();
    //计算可变控件高度
    this.calcHeight();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {
    //4.显示各个控件
    this.reSetSessionContainerHeight();
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
  
  scroll:function(e){
    scrollPostion = 0;
    console.log("scroll:" + e.detail.scrollTop);
    console.log(e);
    if(e.detail.scrollTop < 20){ //可以算是滚到到最顶上了
      scrollPostion = 1;
    } else if ((e.detail.scrollHeight - (this.data.sessionY + e.detail.scrollTop)) > 20){ //可以算是到底部了
      scrollPostion = 2;
    }else{
      scrollPostion = 0;
    }
    console.log("scrollPostion:" + scrollPostion);
     
  },
  scrollBottom: function(e) {
    scrollPostion = 2;
    console.log("scrollPostion:" + scrollPostion);
  },

  scrollTop: function(e) {
    scrollPostion = 1;
    console.log("scrollPostion:" + scrollPostion);
  },
  /**
   * 通过点击鹰眼将播放画面显示出来
   */
  viewPlayer: function() {
    this.setData({
      viewPlayer: !this.data.viewPlayer,
    });
    //恢复播放画面所占据的屏幕高度
    this.data.playerY = playerHeight;
    //重新计算各可变控件高度
    this.calcHeight();
    //并重新设置会话容器的高度
    this.reSetSessionContainerHeight();
  },

  /**
   * 计算可变控件高度
   */
  calcHeight: function() {
    var that = this;
    //调用方法重新计算可动态变化的各个控件的高度
    if (that.data.viewPlayer) {
      wx.createSelectorQuery().select('#scene-player').boundingClientRect(function(rect) {
        that.data.playerY = playerHeight = rect.height // 播放画面节点的高度
      }).exec();
    }
    if (this.data.hasMultiPlayer) {
      wx.createSelectorQuery().select('#scene-member').boundingClientRect(function(rect) {
        that.data.sceneMemberY = sceneMemberHeight = rect.height // 场景成员节点的高度
      }).exec();
    }
    if (this.data.hasPlayerTile) {
      wx.createSelectorQuery().select('#player-title').boundingClientRect(function(rect) {
        that.data.playerTitleY = playerTitleHeight = rect.height // 播放内容节点的高度
      }).exec();
    }
    if (this.data.hasToolConfirm) {
      wx.createSelectorQuery().select('#tool-confirm').boundingClientRect(function(rect) {
        that.data.toolConfirmY = toolConfirmHeight = rect.height // 操作结果临时显示栏节点的高度
      }).exec();
    }
  },

  /**
   * 重新计算会话窗体的高度
   */
  reSetSessionContainerHeight: function() {
    let height = screenHeight - toolBarHeight - this.data.playerY - this.data.sceneMemberY - this.data.playerTitleY - this.data.toolConfirmY;
    this.setData({
      sessionY: height,
    });
  }
})