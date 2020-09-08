package pw.cdmi.paas.app.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 实体类，记录一个应用所拥有的用户登陆域信息.
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_application_userdomain")
public class SiteUserDomain {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;					// 记录的编号

	private String siteId;				// 应用的编号
	
	private String name;				// 登录域的备注名称

	private String loginURL;			// 登录访问路径

	private String loginRole;			// 该路径对应的访问角色枚举，和具体的业务相关, 该字段与Site_Id共同组成不可重复记录

	private String returnURL;			// 验证后原网站验证结果接收路径

//	private String token;				// 访问时候的临时令牌
//
//	private Integer expireTime;		    // 令牌保存时间
}
