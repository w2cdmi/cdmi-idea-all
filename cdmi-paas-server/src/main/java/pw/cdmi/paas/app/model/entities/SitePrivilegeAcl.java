package pw.cdmi.paas.app.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 表示系统的功能模块允许哪些角色进行访问.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_privilege_acl")
public class SitePrivilegeAcl {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						// 记录的编号
	
	private String roleId;                  // 对应的角色编号
	
	private String roleEnum;				// 角色的枚举, 这里之所以不用RoleID，是因为roleid为数据库的标示，在这里roleEnum会被硬编码到代码中

//	private Boolean isCustom;				// 角色是否是用户自定义的角色，1为true，是自定义的,0为false.表示角色是系统角色。

	private String siteId;					// 对应的应用的编号
	
	private String privilegeId;             // 对应的操作权限的信息编码

	private String authEnum;				// 该角色所拥有的权限枚举标示
}
