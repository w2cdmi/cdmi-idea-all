package pw.cdmi.paas.account.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import pw.cdmi.open.model.Sex;
import pw.cdmi.paas.account.model.UserStatus;

/****************************************************
 * 基础数据，用户系统账号信息。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
@Data
@Entity
@Table(name = "p_account")
public class UserAccount {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    @Column(unique = true)
    private String id;						// 帐号信息编号

    @Column(nullable = false, unique = true)
    private String openId;					// 账号的OpenId

    private String peopleId;				// 该账号对应的真实信息Id，对应People表

    @Column(unique = true)
    private String userName;				// 用户帐号名称

    @Column(unique = true)
    private Sex sex;						// 用户帐号性别
    
    private Boolean isReal;			    	// 是否实名
    
    @Column(unique = true)
    private String mobile;					// 用户账号的手机号码

    @Column(unique = true)
    private String email;					// 用户账号的的邮件地址

    @Column(unique = true)
    private String qq;						// 用户对应的QQ号码
    
    @Column(unique = true)
    private String wechat;					// 用户对应的微信账号
    
    @Column(unique = true)
    private String qyWechat;				// 用户对应的企业微信账号
    
    private String password;				// 账号密码
    

    private String nickName;				// 账号昵称

    private String headImage;				// 会员头像的URL

    @Enumerated(EnumType.STRING)
    private UserStatus status; 				// 账号状态

    private Date registerTime;				// 账号创建时间

    @Column(name = "protect_AQ")
    private String protectAQ;				// 用户在该系统中的密码提示问题，用JSON格式，Map方式保存

    private String channel;					// 用户注册来源应用，对应的是应用的appid，如果是直接通过平台注册的，则未空
    
    /******** 平台账号关联的其他可登陆信息 *********/
    private String wxOpenId;				// 对应的微信OpenId
    
    /******** 账号上次登录时候的信息 *********/
    private Date lastLoginTime;				// 最后一次登录的时间

    private String latitude;				// 最后一次登录的地理位置纬度

    private String Longitude;				// 最后一次登录的地理位置经度

    private String accurate;				// 地理位置精度

    private String gisType;			    	// 坐标的类型，对应坐标类型枚举

    private String location;				// 上次访问的所在地址

    private String cityName;				// 上次登录时候所在城市

    private String language;				// 账号上次访问时选择的语言

    private String ipAddress;				// 账号上次访问时的IP地址

    private String validateCode;			// 激活码

}
