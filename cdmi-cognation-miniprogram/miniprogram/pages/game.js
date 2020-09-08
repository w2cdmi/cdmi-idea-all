// miniprogram/pages/game.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        cavasWidth: getApp().globalData.screenWidth,
        cavasHeight: getApp().globalData.screenHeight - 40,
        range: 9,

        //原始数据
        raw_data: [[3, 0, 7, 0, 0, 0, 0, 0, 0], [9, 0, 0, 0, 0, 4, 6, 5, 0],
            [6, 8, 5, 0, 0, 9, 0, 0, 7], [8, 0, 0, 0, 2, 0, 0, 7, 9],
            [0, 0, 6, 0, 0, 0, 4, 0, 0], [5, 7, 0, 0, 3, 0, 0, 0, 6],
            [7, 0, 0, 6, 0, 0, 5, 2, 1], [0, 6, 2, 1, 0, 0, 0, 0, 3],
            [0, 0, 0, 0, 0, 0, 8, 0, 4]], 
        values:[],
        blocks:[],
        rows:[],
        cells:[],

        cells_data:[{
          x:1,
          y:1,
          fontColor:'red',
          backColor:'black',
          value:0,
          makable:false,
          tags:[]
        }],
        cellWidth: 0,
        cellHeight: 0,
        selectedCell:{},
        showInput:false,  //是否显示数字输入框。
    },

    /**
     * 内部方法：
     */
    setArrayValue:function(x, y,value){
      console.log("x:" + x + ";y=" + y +",value=" + value);
      var blocks = this.data.raw_data;
      blocks[x-1][y-1] = value;
      this.setData({
        raw_data: blocks,
      });
    },

    /**
     * 点击九宫格的指定单元格
     */
    clickCell:function(e){
        var dataset = e.currentTarget.dataset;
        var x = dataset.x;
        var y = dataset.y;
        var value = dataset.value;
        var read = dataset.read;

        this.data.selectedCell.x = x;
        this.data.selectedCell.y = y;

        if(!read){
          this.setData({
            showInput: true,
          });
        }else{
          this.setData({
            showInput: false,
          });
        }
    },

  /**
   * 为九宫格填写数字
   */

  setValue:function(e){
    var dataset = e.currentTarget.dataset;
    var value = dataset.value;
    this.setArrayValue(this.data.selectedCell.x, this.data.selectedCell.y,value);
    // row(this.data.selectedCell.y) = value;
  },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var range = this.data.range;
        var cellWidth = parseInt((this.data.cavasWidth) / range);
        var cellHeight = cellWidth;

        //假设中心的方框的坐标为0，0
        //中心点坐标为
        var x = (this.data.cavasWidth - cellWidth) / 2;
        var y = (this.data.cavasHeight - cellHeight) / 2

        var values = this.data.values;
      var blocks = this.data.raw_data;
        values = blocks.flat();
        console.log("cell value:" + values[80]);

        this.setData({
            cellWidth: cellWidth,
            cellHeight: cellHeight,
        });
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    }
})