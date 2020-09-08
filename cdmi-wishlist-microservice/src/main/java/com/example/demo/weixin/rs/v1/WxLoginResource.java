package com.example.demo.weixin.rs.v1;

import com.example.demo.exception.LoginAuthFailedException;
import com.example.demo.oauth.server.UserTokenHelper;
import com.example.demo.weixin.rs.v1.domain.UserToken;
import com.example.demo.weixin.rs.v1.domain.WxUserInfo;
import com.example.demo.weixin.service.WxOauth2Service;
import com.example.demo.wishlist.model.entities.WxUser;
import com.example.demo.wishlist.rs.v1.domain.RestLoginResponse;
import com.example.demo.wishlist.rs.v1.domain.RestWxUserLoginRequest;
import com.example.demo.wishlist.service.WxUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/wxuser/v1")
public class WxLoginResource {

    private static final Logger logger = LoggerFactory.getLogger(WxLoginResource.class);

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private WxOauth2Service wxOauth2Service;

    @Autowired
    private UserTokenHelper userTokenHelper;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<RestLoginResponse> login(@RequestBody RestWxUserLoginRequest loginRequest, HttpServletRequest request) {
        loginRequest.checkParameter(request);

        RestLoginResponse restLoginResponse = null;
        WxUserInfo wxUserInfo = wxOauth2Service.getWxUserInfo(loginRequest.getCode(), loginRequest.getIv(), loginRequest.getEncryptedData());
        if (wxUserInfo == null) {
            logger.error("WxUser login Failed: wxUserInfo is null.");
            throw new LoginAuthFailedException("Failed to get user info.");
        }
        if (wxUserInfo.hasError()) {
            logger.error("Can't get UserInfo of code {}: errcode={}, errmsg={}", loginRequest.getCode(), wxUserInfo.getErrcode(), wxUserInfo.getErrmsg());
            throw new LoginAuthFailedException("Failed to get user info.");
        }

        WxUser wxUser = wxUserService.getWxUserByWxOpenId(wxUserInfo.getOpenId());
        //创建用户
        if (wxUser == null) {
            wxUser = new WxUser();
            wxUser.setWxOpenId(wxUserInfo.getOpenId());
            wxUser.setWxUnionId(wxUserInfo.getUnionId());
            wxUser.setNick(wxUserInfo.getNickName());
            wxUser.setHeadImageUrl(wxUserInfo.getAvatarUrl());
            wxUser.setSponsorId(loginRequest.getSponsorId());
            wxUser = wxUserService.createWxUser(wxUser);
        }

        restLoginResponse = loginWithWxUser(wxUser);
        return new ResponseEntity<>(restLoginResponse, HttpStatus.OK);
    }

    public RestLoginResponse loginWithWxUser(WxUser wxUser) {
        RestLoginResponse restLoginResponse = new RestLoginResponse();

        UserToken token = new UserToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUserId(wxUser.getId());
        token.setOpenId(wxUser.getWxOpenId());
        token.setUnionId(wxUser.getWxUnionId());
        token.setHeadImageUrl(wxUser.getHeadImageUrl());
        token.setNick(wxUser.getNick());

        userTokenHelper.saveToCache(token);
        restLoginResponse.setToken(token.getToken());
        restLoginResponse.setNick(wxUser.getNick());
        restLoginResponse.setHeadImageUrl(wxUser.getHeadImageUrl());
        restLoginResponse.setUserId(wxUser.getId());
        //临时方案
        Boolean isAdmin = false;
        if (wxUser.getIsAdmin() == 1) {
            isAdmin = true;
        }
        restLoginResponse.setAdmin(isAdmin);
        return restLoginResponse;
    }
}
