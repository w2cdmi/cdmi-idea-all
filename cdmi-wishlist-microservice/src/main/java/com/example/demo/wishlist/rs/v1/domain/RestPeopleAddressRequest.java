package com.example.demo.wishlist.rs.v1.domain;

import com.example.demo.exception.InvalidParamterException;
import org.springframework.util.StringUtils;

public class RestPeopleAddressRequest {
    private String id;                      //地址编号
    private String addressName;           //收货人名字
    private String phoneNumber;           //收货人电话
    private String address;			    // 收获人的详细地址

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void checkParameter(){
        if (StringUtils.isEmpty(addressName)) {
            String msg = "addressName is null.";
            throw new InvalidParamterException(msg);
        }

        if (StringUtils.isEmpty(phoneNumber)) {
            String msg = "phoneNumber is null.";
            throw new InvalidParamterException(msg);
        }

        if (StringUtils.isEmpty(address)) {
            String msg = "address is null.";
            throw new InvalidParamterException(msg);
        }
    }
}
