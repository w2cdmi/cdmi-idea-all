package com.example.demo.wishlist.rs.v1;

import com.example.demo.wishlist.rs.v1.domain.RestLoginResponse;
import com.example.demo.wishlist.rs.v1.domain.RestWxUserLoginRequest;
import com.example.demo.wishlist.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/wishlist/users/v1")
public class WxUserResource {

    @Autowired
    private WxUserService wxUserService;

    /*@RequestMapping(value = "login", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<RestLoginResponse> login(@RequestBody RestWxUserLoginRequest loginRequest, HttpServletRequest request){
        loginRequest.checkParameter(request);
        RestLoginResponse restLoginResponse = null;
        WxMpUserInfo mpUserInfo = wxUserService.getWxUser(login.getMpId(), login.getCode(), login.getIv(), login.getEncryptedData());
        if (mpUserInfo == null) {
            LOGGER.error("WxMp login Failed: wxMpUserInfo is null.");
            throw new LoginAuthFailedException("Failed to get user info.");
        }
        if (mpUserInfo.hasError()) {
            LOGGER.error("Can't get UserInfo of code {}: errcode={}, errmsg={}", login.getCode(), mpUserInfo.getErrcode(), mpUserInfo.getErrmsg());
            throw new LoginAuthFailedException("Failed to get user info.");
        }
        return new ResponseEntity<>(restLoginResponse, HttpStatus.OK);
    }*/


}
