package pw.cdmi.paas.app.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import pw.cdmi.open.model.Sex;
import pw.cdmi.paas.account.model.UserOnlineStatus;
import pw.cdmi.paas.account.model.UserStatus;

/****************************************************
 * 基础数据，应用用户表。不同的应用，如果有额外的字段数据属性，应该在应用中创
 * 建扩展的用户信息类，而不是直接使用该类型
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
@Entity
@Data
@Table(name = "p_authapp_user")
public class SiteUser {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;						// 应用使用用户信息编号

    private String appId;					// 对应的应用的编号

    private String accountId;				// 对应的使用用户账号的信息编号

    private String userDomain;				// 用户在该系统中所拥有的登录域Id，不同的登录域Id，可以设置多个密码。

    private Sex sex;                        // 在该系统中所扮演的性别
    
    private String isReal;			    	// 是否实名
    
    private String headImgurl;				// 会员头像的URL

    private String password;				// 用户在该应用中的专有密码

    private String nickName;				// 用户在本系统中的昵称

    private Date registerTime;				// 用户被授权访问该应用的时间

    @Enumerated(EnumType.STRING)
    private UserStatus status;				// 用户在该系统中的帐号状态枚举
    
    private UserOnlineStatus onlineStatus;	// 用户的在线状态
    
    private int praiseNumber;				// 点赞次数
    
    private int appraisalNumber;			// 评价次数
    
    private int userScore;					// 用户的综合评分
    
    private int focus_people_number;		// 被关注的人数
    
    private int follow_people_number;		// 关注用户数
    
    private Date birthday;					// 用户的生日，冗余字段
    
    /******** 账号上次登录时候的信息 *********/
    private Date lastLoginTime;         	// 最后一次登录的时间

    private String latitude;            	// 最后一次登录的地理位置纬度

    private String Longitude;           	// 最后一次登录的地理位置经度

    private String accurate;            	// 地理位置精度

    private String gisType;         		// 坐标的类型，对应坐标类型枚举

    private String location;            	// 上次访问的所在地址

    private String cityName;            	// 上次登录时候所在城市

    private String language;            	// 账号上次访问时选择的语言

    private String ipAddress;           	// 账号上次访问时的IP地址
}
