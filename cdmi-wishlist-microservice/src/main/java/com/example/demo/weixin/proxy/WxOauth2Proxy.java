package com.example.demo.weixin.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.HttpClientUtils;
import com.example.demo.weixin.message.model.TemplateMessage;
import com.example.demo.weixin.rs.v1.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/************************************************************
 * @version 3.0.1
 * @Description:
 * 微信开放平台中，与微信平台相关的功能接口, 主要用于获取用户鉴权信息
 ************************************************************/
@Component
@PropertySource("classpath:application.properties")
public class WxOauth2Proxy {
    private static final Logger logger = LoggerFactory.getLogger(WxOauth2Proxy.class);

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.appSecret}")
    private String appSecret;

    @Value("${wx.mchId}")
    private String matchId;

    @Value("${wx.key}")
    private String key;

    @Value("${wx.notify.lottery.url}")
    private String notifyLotteryUrl;

    @Value("${wx.notify.buy.url}")
    private String notifyBuyUrl;

    public  static final int  APPATOKEN_DEFAULT_EXPIRES = 7200;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getNotifyLotteryUrl() {
        return notifyLotteryUrl;
    }

    public void setNotifyLotteryUrl(String notifyLotteryUrl) {
        this.notifyLotteryUrl = notifyLotteryUrl;
    }

    public String getNotifyBuyUrl() {
        return notifyBuyUrl;
    }

    public void setNotifyBuyUrl(String notifyBuyUrl) {
        this.notifyBuyUrl = notifyBuyUrl;
    }

    /**
     * 使用登录凭证 code 获取登录微信用户的session_key 和 openid。
     * @param code 登录凭证码
     * @return session信息
     */
    public WxUserAccessToken getWxUserAccessCode(String code) {
        String json = HttpClientUtils.httpPostWithJsonBody("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code", null);

        return JSON.parseObject(json, WxUserAccessToken.class);
    }

    /**
     * 获取企业的关注用户的openids
     */
    public UserOpenIdResponse getUserList(String access_token) {
        String json = HttpClientUtils.httpPostWithJsonBody("https://api.weixin.qq.com/cgi-bin/user/get?access_token="+access_token+"&next_openid=",null);
        return JSON.parseObject(json, UserOpenIdResponse.class);
    }

    /**
     * 刷新登录凭证。
     * @param refreshCode 刷新凭证码
     * @return session信息
     */
    public WxUserAccessToken refreshWxUserAccessCode(String refreshCode) {
        String json = HttpClientUtils.httpPostWithJsonBody("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appId + "&grant_type=refresh_token&refresh_token=" + refreshCode, null);

        return JSON.parseObject(json, WxUserAccessToken.class);
    }

    /**
     * 使用access code和openid获取用户信息。
     * @param openId 用户Id
     * @return session信息
     */
    public WxUserInfo getWxUserInfo(String accessCode, String openId) {
        String json = HttpClientUtils.httpPostWithJsonBody("https://api.weixin.qq.com/sns/userinfo?access_token=" + accessCode + "&openid=" + openId, null);

        return JSON.parseObject(json, WxUserInfo.class);
    }

    /**
     * 使用登录凭证 code 获取登录微信用户的session_key 和 openid。
     * @param code 登录凭证码
     * @return session信息
     */
    public WxSessionKey getSessionKeyByCode(String code) {
        String json = HttpClientUtils.httpPostWithJsonBody("https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code", null);
        return JSON.parseObject(json, WxSessionKey.class);
    }

    /**
     * 获取企业的access_token
     */
    public AppAccessToken getAppAccessCode() {
        String json = HttpClientUtils.httpPostWithJsonBody("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret,null);

        return JSON.parseObject(json, AppAccessToken.class);
    }

    /**
     * 使用登录凭证 code 获取登录微信用户的session_key 和 openid。
     * @param templateMessage 模板消息
     * @return session信息
     */
    public JSONObject sendMessageToUser(TemplateMessage templateMessage) {
        AppAccessToken appAccessToken = getAppAccessCode();
        String json = HttpClientUtils.httpPostWithJsonBody("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + appAccessToken.getAccess_token(), templateMessage);
        return JSONObject.parseObject(json);
    }
}
