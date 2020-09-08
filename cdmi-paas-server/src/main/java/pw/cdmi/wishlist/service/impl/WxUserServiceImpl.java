package pw.cdmi.wishlist.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.wishlist.model.entities.WxUser;
import pw.cdmi.wishlist.repositories.jpa.JpaWxUserRepository;
import pw.cdmi.wishlist.service.WxUserService;

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
