package com.example.demo.wishlist.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 愿望商品围观人员列表
 *
 * @author No.11
 */
@Entity
@Table(name = "wishlist_onlookwer")
public class Onlooker {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;
    private String productId;
    private String wxuserId;
    private String inviterId;                    //邀请人的Id
    private Date creatime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getWxuserId() {
        return wxuserId;
    }

    public void setWxuserId(String wxuserId) {
        this.wxuserId = wxuserId;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }

    public Date getCreatime() {
        return creatime;
    }

    public void setCreatime(Date creatime) {
        this.creatime = creatime;
    }
}
