package pw.cdmi.open.model;

import java.util.Date;

import net.sf.json.JSONObject;
import pw.cdmi.paas.account.model.UserHeadImageSize;

/****************************************************
 * 微信SDK，微信用户的基本信息。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
public class UserInfo {

	private String openId;
	private String nickname;
	private Sex sex;
	private String language;
	private String city;
	private String province;
	private String country;
	private ImageURL headImgurl;
	private Date subscribeTime;
	private String unionId;

	public UserInfo(JSONObject user) {
		if(user != null){
			this.openId = user.getString("openid");
			this.nickname = user.getString("nickname");
			this.sex = Sex.fromValue(user.getInt("sex"));
			this.language = user.getString("language");
			this.city = user.getString("city");
			this.province = user.getString("province");
			this.country = user.getString("country");
			String imgurl = user.getString("headimgurl");
			if(imgurl != null){
				headImgurl = new ImageURL("/");
				headImgurl.setImgUrl(imgurl);
				headImgurl.setSize(UserHeadImageSize.SIZE132);
			}
	        long msgsubscribeTime = user.getLong("subscribe_time") * 1000L;  
			this.subscribeTime = new Date(msgsubscribeTime);
			this.unionId = user.getString("unionid");
		}
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = Sex.fromValue(sex);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public ImageURL getHeadImgurl() {
		return headImgurl;
	}

	public void setHeadImgurl(String imgurl) {
		if(imgurl != null){
			headImgurl = new ImageURL("/");
			headImgurl.setImgUrl(imgurl);
			headImgurl.setSize(UserHeadImageSize.SIZE132);
		}
	}

	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(long subscribeTime) {
		this.subscribeTime = new Date(subscribeTime * 1000L);
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

}
