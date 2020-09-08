package pw.cdmi.wechat.miniprogram.service;

import pw.cdmi.wechat.miniprogram.model.WxUserInfo;
import pw.cdmi.wechat.miniprogram.model.entities.TemplateMessage;

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
