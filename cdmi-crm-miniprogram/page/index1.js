// page/index1.js
var hole_radius = 10; //吊孔的半径大小
var left_radius = 60; //显示框左上角的圆弧半径
var margin = 10; //显示框左右边距
var bottom_margin = 20; //显示框底部边距
var square_width = 0; //图片显示最大宽度
var square_height = 0; //图片显示最大高度
var text_height = 60; //文字区域高度
var backgroup_color = "#B0E0E6"; //卡片背景色
var paper_ratio = 1.414286; //打印纸张的长宽比例，这里以A4纸为例

Page({

  /**
   * 页面的初始数据
   */
  data: {
    card_array: [], // 本次要生成的闪卡集合
    album: {
      name: '公园玩沙子',
      owner_name: '张三丰',
      qrcoude_image: '/images/QRCode.jpg',
      languages: [{
          code: "zh_CN",
          name: "中文",
        },
        {
          code: "en_EN",
          name: "英文",
        },
        {
          code: "fr_FR",
          name: "法语",
        }
      ],
      items: [{
          index: 1,
          coverImage: '/images/timg.jpg',
          content: [{
              language: "zh_CN",
              text: "我的美女老师"
            },
            {
              language: "en_EN",
              text: "My Buttiy teacher"
            },
            {
              language: "fr_FR",
              text: "Ma Buee tadb"
            },
          ]
        },
        {
          index: 2,
          coverImage: '/images/timg.jpg',
          content: [{
              language: "zh_CN",
              text: "我的美女老师2"
            },
            {
              language: "en_EN",
              text: "My Buttiy teacher2"
            },
            {
              language: "fr_FR",
              text: "Ma Buee tadb2"
            },
          ],
        },
        {
          index: 3,
          coverImage: '/images/timg.jpg',
          content: [{
              language: "zh_CN",
              text: "我的美女老师3"
            },
            {
              language: "en_EN",
              text: "My Buttiy teacher3"
            },
            {
              language: "fr_FR",
              text: "Ma Buee tadb3"
            }
          ],
        },
      ],
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    square_width = getApp().globalData.windowWidth;
    square_height = square_width / paper_ratio;

    var languages = this.data.album.languages;
    for (var i in languages) {
      console.log("lanuguage:" + languages[i].code);
    }
    var album = this.data.album;
    var items = this.data.album.items;

    var card_array = [];

    for (var j in items) {
      var item = items[j];
      for (var k in item.content) {
        var card = {
          name: album.name,
          owner_name: album.owner_name,
          coverImage: item.coverImage,
          imageIndex: item.index,
          qrcoude_image: album.qrcoude_image + "?language=" + item.content[k].language,
          text: item.content[k].text,
          language: this.transform(languages, item.content[k].language),
        }
        card_array.push(card);
      }
    }

    console.log("width:" + square_width);
    console.log("height:" + square_height);

    this.setData({
      card_array: card_array,
    });

    //开始画闪卡
    for (var l in card_array) {
      this.drawCard(l, card_array[l]);
    }

    // const back_ctx = wx.createCanvasContext('back_canvas');
    // back_ctx.setFillStyle('#B0E0E6')
    // back_ctx.fillRect(0, 0, 316, 236);

    // back_ctx.beginPath();
    // back_ctx.arc(18, 18, 10, 0, 2 * Math.PI);
    // back_ctx.closePath();
    // back_ctx.setFillStyle('#EEEEEE')
    // back_ctx.fill();

    // back_ctx.beginPath();
    // back_ctx.arc(70, 70, 60, 1 * Math.PI, 1.5 * Math.PI);
    // back_ctx.lineTo(306, 10);
    // back_ctx.lineTo(306, 216);
    // back_ctx.lineTo(10, 216);
    // back_ctx.setFillStyle('white');
    // back_ctx.fill();

    // back_ctx.save();
    // back_ctx.beginPath();
    // back_ctx.arc(56,66, 20, 0, 2 * Math.PI);
    // back_ctx.setFillStyle('white');
    // back_ctx.fill();
    // back_ctx.clip();

    // back_ctx.scale(0.2, 0.2);
    // back_ctx.drawImage("/images/QRCode.jpg", 30 / 0.2, 40 / 0.2);
    // back_ctx.restore();

    // back_ctx.setFontSize(13);
    // back_ctx.setTextAlign('left');
    // back_ctx.setFillStyle("#000");
    // back_ctx.fillText('My Buttiy teacher', 30, 35);

    // back_ctx.setFontSize(13);
    // back_ctx.setTextAlign('right');
    // back_ctx.setFillStyle("#000");
    // back_ctx.fillText('伍嘉胤 : 公园玩沙子 (1)', 306, 232);

    // back_ctx.scale(0.2, 0.2);
    // back_ctx.drawImage("/images/QRCode.jpg", (268-20) / 0.2, 156 / 0.2);
    // back_ctx.scale(5, 5);
    // back_ctx.draw();
    // const ctx = wx.createCanvasContext('cover_canvas_o');
    // ctx.setFillStyle('white')
    // ctx.fillRect(0, 0, square_width,30);
    // ctx.setTextAlign('center');
    // ctx.setFillStyle("#000");
    // ctx.setFontSize(20);
    // const metrics = ctx.measureText("提交");
    
    // ctx.fillText("提交", (square_width - metrics.width)/2, 20);
    // ctx.draw();
  },

  //将语音代码转换为中文识别的语言类型
  transform(languages, code) {
    for (var i in languages){
      if (languages[i].code == code){
        return languages[i].name;
      }
    }
  },

  //计算每个专辑的闪卡数量
  calculateCardNumber(data) {
    var card_num = 0;
    var items = data.items;
    for (var i in items) {
      var contents = items[i].content;
      card_num = card_num + contents.length;
    }
    console.log("card_num:" + card_num);
    return card_num;
  },

  //画出闪卡图片
  drawCard: function(index, card) {
    //创建画布，并设置背景
    const ctx = wx.createCanvasContext('cover_canvas_' + index);
    ctx.setFillStyle(backgroup_color)
    ctx.fillRect(0, 0, square_width, square_height);

    //创建打孔位置，设置打孔位置圆心坐标
    var hole_arc_x;
    var hole_arc_y;
    hole_arc_x = hole_arc_y = hole_radius + margin - margin / 3;
    ctx.arc(hole_arc_x, hole_arc_y, hole_radius, 0, 2 * Math.PI);
    ctx.setFillStyle('#EEEEEE')
    ctx.fill();

    //创建闪卡内容显示区
    ctx.beginPath();
    ctx.arc(left_radius + margin, left_radius + margin, left_radius, 1 * Math.PI, 1.5 * Math.PI);
    ctx.lineTo(square_width - margin, margin);
    ctx.lineTo(square_width - margin, square_height - bottom_margin);
    ctx.lineTo(margin, square_height - bottom_margin);
    ctx.closePath();
    ctx.setFillStyle('white');
    ctx.fill();

    //显示闪卡图片
    //1. 获取最大显示区域
    var card_width = square_width - 2 * (left_radius + margin);
    var card_height = square_height - (text_height + bottom_margin) - 2 * margin;
    console.log("card_width:" + card_width);
    console.log("card_height:" + card_height);
    //2. FIXME 根据图片大小调整显示区域,通过图片的onload方法获取图片的原始大小
    ctx.drawImage("/images/timg.jpg", (square_width - card_width) / 2, (3 * margin), card_width, card_height);

    //显示闪卡文字
    const metrics = ctx.measureText(card.text);
    console.log(metrics.width);

    if (metrics.width > card_width) {
      ctx.setFontSize(13);
    } else {
      ctx.setFontSize(16);
    }
    ctx.setTextAlign('center');
    ctx.setFillStyle("#000");
    ctx.fillText(card.text, square_width / 2, square_height - bottom_margin - 15, card_width);

    //显示专辑名称
    ctx.setFontSize(12);
    ctx.setTextAlign('right');
    ctx.setFillStyle("#000");
    var text = card.owner_name + ': ' + card.name;
    if (card.imageIndex != '' || card.imageIndex != 'undified'){
      text = text + "(" + card.imageIndex + ")";
    }
    ctx.fillText(text, square_width - margin, square_height - 5);

    ctx.setFontSize(12);
    ctx.setTextAlign('left');
    ctx.setFillStyle("#000");
    ctx.fillText(card.language, margin, square_height - 5);

    //显示语音二维码
    //FIXME 需根据二维码的大小来重新计算位置和缩放比例,这里强制指令缩放10倍
    ctx.scale(0.08, 0.08);
    ctx.drawImage("/images/QRCode.jpg", (square_width - margin - left_radius + 15) / 0.08, (3 * margin + card_height) / 0.08);
    ctx.scale(10, 10);
    ctx.draw();
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

  }
})