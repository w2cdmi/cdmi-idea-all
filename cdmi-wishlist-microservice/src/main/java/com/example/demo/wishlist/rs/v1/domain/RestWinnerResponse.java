package com.example.demo.wishlist.rs.v1.domain;

import java.util.Date;

public class RestWinnerResponse {
    private Date createTime;						//宣布中奖的时间
    private String userName;                      //用户名字
    private String headImageUrl;                  //用户头像
    private String productName;                   //产品名字

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
