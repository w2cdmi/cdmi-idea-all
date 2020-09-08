package com.example.demo.wishlist.model.entities;

import com.example.demo.wishlist.model.ProductStatus;
import com.example.demo.wishlist.model.SaleRule;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 愿望众筹商品表
 *
 * @author No.11
 */
@Entity
@Table(name = "wishlist_product")
public class Product {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;
    private String title;                             //商品的名称或标题
    private String photoList;                        //商品的展示图，JSON数组格式
    private float originalPrice;                    //商品的原始价格
    private float actualPrice;                       //商品的成交价格
    private float lotteryPrice = 100;                //商品抽奖价格(单位分)
    private int mount = 1;                           //商品的数量，默认为1
    private Date salesValidity;                      //众筹有效期
    private Integer ratedNumber=0;                     //众筹规定人数
    private long onlookerNumber=0;                    //围观人数
    private long participantNumber=0;                //参与竞标的人数
    private ProductStatus status;                    //众筹状态
    private String cancelReason;                    //取消原因
    private String winnerId;                         //众筹获奖人的Id(会更新)
    private Date payLimitTime;                       //众筹支付限制时间
    private Date winTime;                            //众筹获奖时间（会更新）
    private long totalMoney;                        //众筹总付费;
    private SaleRule salerule;                       //众筹规则

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoList() {
        return photoList;
    }

    public void setPhotoList(String photoList) {
        this.photoList = photoList;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public float getLotteryPrice() {
        return lotteryPrice;
    }

    public void setLotteryPrice(float lotteryPrice) {
        this.lotteryPrice = lotteryPrice;
    }

    public float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(float actualPrice) {
        this.actualPrice = actualPrice;
    }

    public int getMount() {
        return mount;
    }

    public void setMount(int mount) {
        this.mount = mount;
    }

    public Integer getRatedNumber() {
        return ratedNumber;
    }

    public void setRatedNumber(Integer ratedNumber) {
        this.ratedNumber = ratedNumber;
    }

    public Date getSalesValidity() {
        return salesValidity;
    }

    public void setSalesValidity(Date salesValidity) {
        this.salesValidity = salesValidity;
    }

    public long getOnlookerNumber() {
        return onlookerNumber;
    }

    public void setOnlookerNumber(long onlookerNumber) {
        this.onlookerNumber = onlookerNumber;
    }

    public long getParticipantNumber() {
        return participantNumber;
    }

    public void setParticipantNumber(long participantNumber) {
        this.participantNumber = participantNumber;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public Date getPayLimitTime() {
        return payLimitTime;
    }

    public void setPayLimitTime(Date payLimitTime) {
        this.payLimitTime = payLimitTime;
    }

    public Date getWinTime() {
        return winTime;
    }

    public void setWinTime(Date winTime) {
        this.winTime = winTime;
    }

    public long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(long totalMoney) {
        this.totalMoney = totalMoney;
    }

    public SaleRule getSalerule() {
        return salerule;
    }

    public void setSalerule(SaleRule salerule) {
        this.salerule = salerule;
    }

    //检查创建查询时，前台传入的参数
    public void checkRequestPram() {
        if (StringUtils.isEmpty(title)) {
            return;
        }
        if (actualPrice == 0) {
            return;
        }
        if (originalPrice == 0) {
            return;
        }
    }
}
