package pw.cdmi.wechat.miniprogram.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import pw.cdmi.paas.config.redis.RedisUtil;
import pw.cdmi.wechat.miniprogram.model.AesException;
import pw.cdmi.wechat.miniprogram.model.WXBizDataCrypt;
import pw.cdmi.wechat.miniprogram.model.WxUserInfo;
import pw.cdmi.wechat.miniprogram.model.entities.TemplateMessage;
import pw.cdmi.wechat.miniprogram.service.WxOauth2Service;
import pw.cdmi.wishlist.wechat.WxOauth2Proxy;
import pw.cdmi.wishlist.wechat.WxSessionKey;
import pw.cdmi.wishlist.wechat.WxUserAccessToken;

@Service
public class WxOauth2ServiceImpl implements WxOauth2Service{

    private static final Logger logger = LoggerFactory.getLogger(WxOauth2ServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private WxOauth2Proxy wxOauth2Proxy;

    @Override
    public WxUserInfo getWxUserInfo(String code) {
        WxUserAccessToken accessCode = getWxUserAccessToken(code);
        if(accessCode == null) {
            logger.error("Can't get WxUserInfo, the access toke info is null: code={}", code);
            return null;
        }

        return wxOauth2Proxy.getWxUserInfo(accessCode.getAccessToken(), accessCode.getOpenid());
    }

    @Override
    public WxUserInfo getWxUserInfo(String code, String iv, String encryptedData) {
        String json = decryptData(code, iv, encryptedData);
        logger.info(json);
        if(!StringUtils.isEmpty(json)) {
            return JSON.parseObject(json, WxUserInfo.class);
        }

        return null;
    }

    @Override
    public void sendWechatMsgToUser(TemplateMessage templateMessage) {
        JSONObject jsonObject = wxOauth2Proxy.sendMessageToUser(templateMessage);
        if("ok".equals(jsonObject.get("errmsg"))){

        }
    }

    private String decryptData(String code, String iv, String encryptedData) {
        WxSessionKey sessionInfo = wxOauth2Proxy.getSessionKeyByCode(code);
        if(sessionInfo == null) {
            logger.error("Failed to get WxUserInfo: session key is null.");
            return null;
        }

        if(sessionInfo.hasError()) {
            logger.error("Failed to get WxUserInfo, error occurred while getting session key: code={}, error={}, message={}", code, sessionInfo.getErrcode(), sessionInfo.getErrmsg());
            return null;
        }

        //获取用户信息
        String json;
        try {
            json = new WXBizDataCrypt(sessionInfo.getSessionKey(), iv).DecryptData(encryptedData);
        } catch (AesException e) {
            logger.error("Failed to get WxUserInfo, error occurred while decrypting data: code={}, exception={}", code, e.getMessage());
            return null;
        }

        return json;
    }

    /**
     * 获取微信用户访问凭证
     */
    public WxUserAccessToken getWxUserAccessToken(String code) {
        String tokenInfoKey = "WxUser.AccessCodeInfo_" + code;
        //1. 先检查缓存
        WxUserAccessToken accessTokeInfo = (WxUserAccessToken)redisUtil.get(tokenInfoKey);
        if(accessTokeInfo == null) {
            //2. 可能是缓存已经失效，检查是否有refreshToken
            String refreshKey = "WxUser.RefreshToken_" + code;
            String refreshToken = (String)redisUtil.get(refreshKey);

            //没有refreshToken或已失效（30天）
            if(refreshToken == null) {
                accessTokeInfo = wxOauth2Proxy.getWxUserAccessCode(code);
            } else {
                accessTokeInfo = wxOauth2Proxy.refreshWxUserAccessCode(refreshToken);
            }

            if(!accessTokeInfo.hasError()) {
                redisUtil.set(tokenInfoKey, accessTokeInfo, accessTokeInfo.getExpiresIn() * 1000);
                redisUtil.set(refreshKey, accessTokeInfo.getRefreshToken(), 2592000000L); //30天
            } else {
                logger.error("Can't get UserAccessToken: code={}: errcode={}, errmsg={}", code, accessTokeInfo.getErrcode(), accessTokeInfo.getErrmsg());
            }
        }

        return accessTokeInfo;
    }
}
