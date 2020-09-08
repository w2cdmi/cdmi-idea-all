package pw.cdmi.open.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/****************************************************
 * 基础数据，提交手机检验信息。
 * 
 * @author 伍伟
 * @version CDMI Service Platform, July 23, 2014
 ***************************************************/
@Data
@Entity
@Table(name = "p_mobile_verification")
public class MobileVerification {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						// 手机校验信息的编号

	private String mobile; 					// 手机号码

	private String code;					// 生产的手机检验码

	private String content;					// 校验短信信息

	private Integer expireTime;				// 手机校验码的有效时间,单位小时

	private Date createTime;				// 检验码生产的时间
		
	private String appId;					// 对应消费该短信应用站点Id
	
	private String siteId;					// 对应消费该短信应用站点Id
	
	private String tenantId;				// 对应消费该短信的租户Id
	
	private String userId;					// 对应消费该短信的用户的ID
	
}
