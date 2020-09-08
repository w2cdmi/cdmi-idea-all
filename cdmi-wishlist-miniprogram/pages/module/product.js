var httpClient = require('./httpclient.js');
var config = require('./config.js');

function createProduct(data, callback){
    var header = {
        Authorization: getApp().globalData.token
    };
    httpClient.post(config.host + '/wishlist/products/v1', data, header, callback);
}

// 获取所有商品
function getAllProducts(callback) {
  var header = {
    Authorization: getApp().globalData.token
  };
  httpClient.get(config.host + '/wishlist/products/v1/list/inProgress', {}, header, callback);
}

// 获取单个商品
function getProductDetail(callback){
    var header = {
        Authorization: getApp().globalData.token
    };
    httpClient.get(config.host + '/wishlist/products/v1/list/inProgress', {}, header, callback);
}

// 历史信息列表
function getHistoryInfo(callback) {
  var header = {
    Authorization: getApp().globalData.token
  };
  httpClient.get(config.host + '/wishlist/products/v1/list/inProgress', {}, header, callback);
}

function getProductById(productId, callback) {
    var header = {
        Authorization: getApp().globalData.token
    };
    httpClient.get(config.host + '/wishlist/products/v1/' + productId, {}, header, callback);
}

function getNotPayProductById(productId, callback){
    var header = {
        Authorization: getApp().globalData.token
    };
    httpClient.get(config.host + '/wishlist/products/v1/' + productId + "/notpay", {}, header, callback);
}

module.exports = {
    createProduct,
    getAllProducts,
    getProductDetail,
    getProductById,
    getHistoryInfo,
    getNotPayProductById
}