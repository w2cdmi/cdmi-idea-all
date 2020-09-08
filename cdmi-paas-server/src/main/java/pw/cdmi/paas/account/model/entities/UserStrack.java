package pw.cdmi.paas.account.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_accesstrack")
public class UserStrack {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private Long id;							// 记录编号

	private Long accountId;						// 访问者的ID,对应的Account

	@Column(length = 30)
	private String userName;					// 用户名称，对应People的真名

	private String iPAddress;					// 用户访问时的IP地址

	private String iPLocation;					// 用户访问时的IP所对应的地址

	private String resourceURL;					// 访问轨迹URL

	private Integer action;					    // 访问操作枚举

	private String requestContent;				// 操作请求的内容

	private String responseContent;				// 操作响应的内容

	private Integer loginRole;					// 对应的登录域身份标识

	private String loginRoleName;				// 对应的登录域身份名称

	private Date createTime;					// 用户的访问时间

	private String siteId;						// 对应的应用的编号
}
