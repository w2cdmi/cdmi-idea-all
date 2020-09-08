var HOST = 'http://127.0.0.1';
var tm_id = "";
var app_id = "463A2frec~!678Erhbv9223111r.iy88";
var app_token = '463A2frec~!678Erhbv9223111r.iy88';
var HTTP_LOGIN = '/pass/v1/manager/wxminiprogram/login';
var HTTP_CHECK_IS_DEVELOPER = '/pass/v3/developers/developer';  //判断用户是否已成为开发者
var HTTP_NEW_DEVELOPER = '/pass/v3/developers/developer';       //新建一个开发者

module.exports = {
  HOST,
  tm_id,
  app_id,
  app_token,
  HTTP_LOGIN,
  HTTP_CHECK_IS_DEVELOPER,
  HTTP_NEW_DEVELOPER
};