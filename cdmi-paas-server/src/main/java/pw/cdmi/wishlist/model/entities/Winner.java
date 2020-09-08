package pw.cdmi.wishlist.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 愿望众筹商品获奖表
 * @author No.11
 *
 */
@Entity
@Table(name = "wishlist_winner")
public class Winner {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	private String productId;
	private String wxuserId;						//中奖人员的微信用户Id
	private boolean isPayed;						//是否完成支付
	private Date payedTime;							//支付完成时间
	private boolean isGiveUp = false;				//是否放弃领奖
	private Date giveUpTime;						//放弃领奖时间
	private Date createTime;							//宣布中奖的时间

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

	public boolean isPayed() {
		return isPayed;
	}

	public void setPayed(boolean payed) {
		isPayed = payed;
	}

	public Date getPayedTime() {
		return payedTime;
	}

	public void setPayedTime(Date payedTime) {
		this.payedTime = payedTime;
	}

	public boolean isGiveUp() {
		return isGiveUp;
	}

	public void setGiveUp(boolean giveUp) {
		isGiveUp = giveUp;
	}

	public Date getGiveUpTime() {
		return giveUpTime;
	}

	public void setGiveUpTime(Date giveUpTime) {
		this.giveUpTime = giveUpTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
