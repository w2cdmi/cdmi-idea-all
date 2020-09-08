package pw.cdmi.open.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Version;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import pw.cdmi.open.model.Nation;
import pw.cdmi.open.model.Sex;

/****************************************************
 * 基础数据，居民身份信息。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
@Data
@Entity
@Table(name = "p_people")
public class People {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id; // 信息编号

	private String openId; // 个人信息的OpenId

	private String trueName; // 个人的名称

	private String secondName; // 曾用名,保存为数组

	@Enumerated(EnumType.STRING)
	private Sex sex; // 个人的性别,对应Sex枚举

	@Enumerated(EnumType.STRING)
	private Nation nation; // 个人的民族

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday; // 出生日期

	/***** 居民身份证和社保信息 *********/
	@Column(unique = true)
	private String idCard; // 居民身份证信息

	private String idCardUrl; // 居民身份证复印件的URL

	private String location; // 身份证上面的居民地址

	private Integer cityId; // 对应的城市ID

	private Integer provinceId; // 对应的省份ID

	private Integer countryId; // 对应的国家ID

	private String policeStation; // 身份证上面的办理公安局

	@Column(unique = true)
	private String socialSecurityCode; // 个人社保编号

	@Column(unique = true)
	private String driverLicenseNumber; // 个人驾驶证号

	@Column(unique = true)
	private String passportNumber; // 个人护照号

	/***** 居民当前居住信息 *********/
	private Integer liveCityId; // 当前居住城市

	private Integer liveDistrictId; // 当前居住的区县

	private String liveTownId; // 当前居住的乡镇或街道办，由于太多，这里存储文本

	private Integer liveCommunityId; // 当前居住的社区或村组

	private String liveResidence; // 当前居住的具体住所，几栋几单元几号

	/***** 入职登记的证件类型 ，不映射进数据库 *********/
	@Transient // 不映射进数据库
	private Integer certificate;

	@Version
	private Long version;
}
