package com.example.demo.oauth.server;

import com.example.demo.common.cache.RedisUtil;
import com.example.demo.weixin.rs.v1.domain.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
