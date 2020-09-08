var config = require("config.js");
var httpclient = require("httpclient.js");

function createAddress(data, callback) {
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.post(config.host + '/wishlist/crowdfundings/v1/address', data, header, callback);
}

function editAddress(data, callback){
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.put(config.host + '/wishlist/crowdfundings/v1/address', data, header, callback);
}

function getAddressAll(data, callback){
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.get(config.host + '/wishlist/crowdfundings/v1/address/userId', data, header, callback);
}

function deleteAddress(id, callback){
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.remove(config.host + '/wishlist/crowdfundings/v1/address/' + id, null, header, callback);
}

function getAddressById(id, callback){
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.get(config.host + '/wishlist/crowdfundings/v1/address/' + id, null, header, callback);
}

function setAddressToDefault(id, callback){
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.put(config.host + '/wishlist/crowdfundings/v1/address/setDefault/' + id, null, header, callback);
}

function getDefaultAddress(callback){
    var header = {
        Authorization: getApp().globalData.token,
        'content-type': 'application/json'
    };
    return httpclient.get(config.host + '/wishlist/crowdfundings/v1/address/default', null, header, callback);
}

module.exports = {
    createAddress,
    editAddress,
    getAddressAll,
    deleteAddress,
    getAddressById,
    setAddressToDefault,
    getDefaultAddress
};