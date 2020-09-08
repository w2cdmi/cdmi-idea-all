const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

//最近预览时间
function formatNewestTime(timestamp) {
    if (timestamp == undefined) {
        return "00 : 00 : 00";
    }
    var days = parseInt(timestamp / 1000 / 60 / 60 / 24, 10); //计算剩余的天数 
    var hours = parseInt(timestamp / 1000 / 60 / 60 % 24, 10); //计算剩余的小时 
    var minutes = parseInt(timestamp / 1000 / 60 % 60, 10);//计算剩余的分钟 
    var seconds = parseInt(timestamp / 1000 % 60, 10);//计算剩余的秒数 
    hours = days*24 + hours;
    hours = checkTime(hours);
    minutes = checkTime(minutes);
    seconds = checkTime(seconds);

    return hours + " : " + minutes + " : " + seconds
}

function checkTime(i) { //将0-9的数字前面加上0，例1变为01 
    if (i < 10) {
        i = "0" + i;
    }
    return i;
} 

/**  
*转换日期对象为日期字符串  
* @param l long值  
* @param pattern 格式字符串,例如：yyyy-MM-dd hh:mm:ss  
* @return 符合要求的日期字符串  
*/
function getFormatDate(date, pattern) {
    if (date == undefined) {
        date = new Date();
    }
    if (typeof date == 'number') {
        date = new Date(date);
    }
    if (pattern == undefined) {
        pattern = "yyyy-MM-dd";
    }
    if (date.length == 10) {
        return date.replace("/", "-");
    }

    var o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
        "q+": Math.floor((date.getMonth() + 3) / 3),
        "S": date.getMilliseconds()
    }
    if (/(y+)/.test(pattern)) {
        pattern = pattern.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(pattern)) {
            pattern = pattern.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }

    return pattern;
}

module.exports = {
  formatTime: formatTime,
  formatNewestTime: formatNewestTime,
  getFormatDate: getFormatDate
}
