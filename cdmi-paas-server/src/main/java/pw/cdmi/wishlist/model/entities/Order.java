package pw.cdmi.wishlist.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import pw.cdmi.wishlist.model.OrderStatus;
import pw.cdmi.wishlist.model.PayType;

@Entity
@Table(name = "wishlist_order")
public class Order {
    @Id
    private String id;

    private String prepayId;

    private String productId;

    private Float originalPrice;

    private Float actualPrice;

    private Float lotteryPrice;

    private Float PayPrice;

    private PayType payType;

    private String userId;

    private Date submitDate;

    private Date finishedDate;

    private OrderStatus status;

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setId(String id) {

        this.id = id;
    }

    public Float getPayPrice() {
        return PayPrice;
    }

    public void setPayPrice(Float payPrice) {
        PayPrice = payPrice;
    }

    public Float getOriginalPrice() {
        return originalPrice;

    }

    public Float getLotteryPrice() {
        return lotteryPrice;
    }

    public void setLotteryPrice(Float lotteryPrice) {
        this.lotteryPrice = lotteryPrice;
    }

    public void setOriginalPrice(Float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Float actualPrice) {
        this.actualPrice = actualPrice;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }
}
