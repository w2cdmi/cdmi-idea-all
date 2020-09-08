package pw.cdmi.wishlist.model.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信用户信息表
 *
 * @author No.11
 */
@Entity
@Table(name = "p_wxuser")
public class WxUser {
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    private String id;
    private String wxOpenId;
    private String wxUnionId;
    private String nick;
    private String headImageUrl;
    private String phoneNumber;
    private String address;
    private String sponsorId;                    	//邀请人，推荐人
    private String sponsortype;                    	//邀请人类型，这里对应的WXUser
    private long inviteCount;                    	//邀请人员的数量
    private Byte isAdmin = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxUnionId() {
        return wxUnionId;
    }

    public void setWxUnionId(String wxUnionId) {
        this.wxUnionId = wxUnionId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHeadImageUrl() {
        return headImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        this.headImageUrl = headImageUrl;
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

    public String getSponsorId() {
        return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
        this.sponsorId = sponsorId;
    }

    public String getSponsortype() {
        return sponsortype;
    }

    public void setSponsortype(String sponsortype) {
        this.sponsortype = sponsortype;
    }

    public long getInviteCount() {
        return inviteCount;
    }

    public void setInviteCount(long inviteCount) {
        this.inviteCount = inviteCount;
    }

    public Byte getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Byte isAdmin) {
        this.isAdmin = isAdmin;
    }
}
