package pw.cdmi.wishlist.service;

import pw.cdmi.wishlist.model.entities.WxUser;

public interface WxUserService {

    public WxUser createWxUser(WxUser wxUser);

    //根据openId查询微信用户信息
    public WxUser getWxUserByWxOpenId(String openId);

    public WxUser getById(String userId);
}
