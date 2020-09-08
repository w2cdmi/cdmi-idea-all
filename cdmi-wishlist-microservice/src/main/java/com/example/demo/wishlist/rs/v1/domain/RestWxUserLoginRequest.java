package com.example.demo.wishlist.rs.v1.domain;

import com.example.demo.exception.InvalidParamterException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class RestWxUserLoginRequest {
    private String code;

    private String rawData;

    private String encryptedData;

    private String iv;

    private String sponsorId;


    public void checkParameter(HttpServletRequest request) {
        if (StringUtils.isEmpty(code)) {
            String msg = "code is null.";
            throw new InvalidParamterException(msg);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }
}
