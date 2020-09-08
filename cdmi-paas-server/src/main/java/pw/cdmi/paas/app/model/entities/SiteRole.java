package pw.cdmi.paas.app.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 实体类，记录指定的应用系统的指定登录域下的权限角色.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_role")
@NamedQueries({ @NamedQuery(name = "SiteRole.findAll", query = "select sr from SiteRole sr where 1 = 1 "),
		@NamedQuery(name = "SiteRole.getCount", query = "select count(*) from SiteRole where 1 = 1 ") })
public class SiteRole {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						// 记录的编号

	private String userDomain;				// 角色所对应的用户登录域标示

	@Column(length = 15, nullable = false)
	private String name;					// 角色名称

	@Column(length = 24)
	private String roleEnum;				// 角色的枚举值,同一个站内内唯一

	private String appId;					// 对应的应用的编号

	private String description;				// 对角色的备注
	
	private Boolean customRole;				// 是否为自定义角色
	
	private String whiteIpList;					// 来源IP白名单
}
