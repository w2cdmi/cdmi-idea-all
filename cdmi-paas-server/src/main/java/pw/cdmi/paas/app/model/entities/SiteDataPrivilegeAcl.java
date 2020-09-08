package pw.cdmi.paas.app.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 实体类，记录应用中角色可访问的数据权限列表.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_data_acl")
@NamedQueries({
		@NamedQuery(name = "SiteDataAcl.findListByRole", query = "select sma from SiteDataPrivilegeAcl sma where roleId = ? "),
		@NamedQuery(name = "SiteDataAcl.deleteByRole", query = "delete from SiteDataPrivilegeAcl where roleId = ? ") })
public class SiteDataPrivilegeAcl {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;					    // 记录的编号

	private String dataId;					// 需要权限控制的数据编号，与RoleId共同作为主键,

	private String roleId;                  // 对应的角色编号
	
	private String roleEnum;				// 角色的枚举, 这里之所以不用RoleID，是因为roleid为数据库的标示，在这里roleEnum会被硬编码到代码中

	private String appId;					// 对应的应用的编号,对应SiteApplication

	private String authEnum;				// 该角色所拥有的权限枚举标示
}
