package com.example.demo.wishlist.service.impl;


import com.example.demo.wishlist.model.entities.WxUser;
import com.example.demo.wishlist.repositories.jpa.JpaWxUserRepository;
import com.example.demo.wishlist.rs.v1.domain.RestLoginResponse;
import com.example.demo.wishlist.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WxUserServiceImpl implements WxUserService {

    @Autowired
    private JpaWxUserRepository jpaWxUserRepository;

    @Override
    public WxUser createWxUser(WxUser wxUser) {
        WxUser responseWxUser = jpaWxUserRepository.save(wxUser);
        return responseWxUser;
    }

    @Override
    public WxUser getWxUserByWxOpenId(String openId) {
        WxUser responseWxUser = jpaWxUserRepository.findByWxOpenId(openId);
        return responseWxUser;
    }

    @Override
    public WxUser getById(String userId) {
        WxUser user = jpaWxUserRepository.findById(userId).get();
        return user;
    }
}
