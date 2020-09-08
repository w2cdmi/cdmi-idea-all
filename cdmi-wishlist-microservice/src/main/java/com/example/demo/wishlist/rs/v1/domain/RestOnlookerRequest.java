package com.example.demo.wishlist.rs.v1.domain;

import com.example.demo.exception.InvalidParamterException;
import org.springframework.util.StringUtils;

import java.util.Date;

public class RestOnlookerRequest {

    private String productId;
    private String inviterId;                    //邀请人的Id

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public void checkParameter(){
        if (StringUtils.isEmpty(productId)) {
            String msg = "productId is null.";
            throw new InvalidParamterException(msg);
        }
    }
}
