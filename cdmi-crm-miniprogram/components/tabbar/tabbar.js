var objectUtils = require("../../module/objectUtils.js");

Component({
  options: {
    multipleSlots: true // 在组件定义时的选项中启用多slot支持
  },
  /**
   * 组件的属性列表
   * 用于组件自定义设置
   */
  properties: {
    selectIndex: {
      type: String,
      value: 0
    },
    mCount: {
      type: String,
      value: 0
    }
  },

  /**
   * 私有数据,组件的初始数据
   * 可用于模版渲染
   */
  data: {
    tabbars: [{
      "selectedIconPath": "/images/icon/index-active.png",
      "iconPath": "/images/icon/index.png",
      "pagePath": "/page/buz/buz",
      "text": "订单",
    }, {
      "selectedIconPath": "/images/icon/file-active.png",
      "iconPath": "/images/icon/file.png",
        "pagePath": "/page/buz/buz",
      "text": "用户",
    }, {
      "selectedIconPath": "/images/icon/find-active.png",
      "iconPath": "/images/icon/find.png",
      "pagePath": "/page/buz/buz",
      "text": "业务",
    }, {
      "selectedIconPath": "/images/icon/me-active.png",
      "iconPath": "/images/icon/me.png",
      "pagePath": "/page/system/system",
      "text": "系统",
    }]
  },

  /**
   * 组件的方法列表
   * 更新属性和数据的方法与更新页面数据的方法类似
   */
  methods: {
    onTabItem: function(e) {
      var index = e.currentTarget.dataset.index;
      var item = this.data.tabbars[index];
      if (objectUtils.isEmpty(item)) {
        return;
      }
      if (item.pagePath == "") {
        this.triggerEvent("onShowMenu");
        return;
      }
      wx.reLaunch({
        url: item.pagePath
      })
    }
  }
})