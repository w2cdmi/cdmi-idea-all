package com.example.demo.weixin.rs.v1.domain;

import com.example.demo.wishlist.model.PayType;

public class RestWxPayRequest {

    private String productId;

    private PayType payType;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public void checkRequestPram(){
        if(productId == ""){
            return;
        }
    }
}
