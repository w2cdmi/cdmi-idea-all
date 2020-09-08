package pw.cdmi.open.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pw.cdmi.paas.auth.service.UserToken;
import pw.cdmi.paas.config.redis.RedisUtil;

@Component
public class UserTokenHelper {

    @Autowired
    private RedisUtil redisUtil;

    public void saveToCache(UserToken userToken) {
        redisUtil.set(userToken.getToken(), userToken, 3600);
    }

    public UserToken checkTokenAndGetUserToken(String tokenKey) {
        return (UserToken)redisUtil.get(tokenKey);
    }
}
