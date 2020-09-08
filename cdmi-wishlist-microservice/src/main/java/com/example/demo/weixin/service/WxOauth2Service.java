package com.example.demo.weixin.service;

import com.example.demo.weixin.message.model.TemplateMessage;
import com.example.demo.weixin.rs.v1.domain.WxUserAccessToken;
import com.example.demo.weixin.rs.v1.domain.WxUserInfo;
import com.example.demo.wishlist.model.entities.WxUser;
import com.example.demo.wishlist.rs.v1.domain.RestLoginResponse;

/**
 * 微信开放平台获取数据
 */
public interface WxOauth2Service {
    /**
     * 获取微信用户信息
     */
    public WxUserInfo getWxUserInfo(String code);

    /**
     * 获取用户信息
     * @param code
     * @param iv
     * @param encryptedData
     * @return  微信用户信息
     */
    public WxUserInfo getWxUserInfo(String code, String iv, String encryptedData);

    /**
     * 向用户发送模板消息
     * @param templateMessage
     */
    public void sendWechatMsgToUser(TemplateMessage templateMessage);
}
