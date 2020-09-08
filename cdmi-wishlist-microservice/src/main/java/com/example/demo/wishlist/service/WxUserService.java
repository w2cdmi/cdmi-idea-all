package com.example.demo.wishlist.service;

import com.example.demo.wishlist.model.entities.WxUser;
import com.example.demo.wishlist.rs.v1.domain.RestLoginResponse;
import org.springframework.stereotype.Service;

public interface WxUserService {

    public WxUser createWxUser(WxUser wxUser);

    //根据openId查询微信用户信息
    public WxUser getWxUserByWxOpenId(String openId);

    public WxUser getById(String userId);
}
