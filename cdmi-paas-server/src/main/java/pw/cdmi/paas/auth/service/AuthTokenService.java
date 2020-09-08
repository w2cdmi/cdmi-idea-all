package pw.cdmi.paas.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pw.cdmi.paas.config.redis.RedisUtil;

@Component
public class AuthTokenService {
    @Autowired
    private RedisUtil redisUtil;

    public void saveToCache(UserToken userToken) {
        redisUtil.set(userToken.getToken(), userToken, 3600);
    }

    public UserToken checkTokenAndGetUserToken(String tokenKey) {
        return (UserToken)redisUtil.get(tokenKey);
    }
}
