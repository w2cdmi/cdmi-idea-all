package pw.cdmi.paas.app.model.entities;

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
 * 实体类，记录应用中导航菜单的访问列表.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_menu_acl")
@NamedQueries({
		@NamedQuery(name = "SiteMenuAcl.findListByRole", query = "select sma from SiteMenuAcl sma where roleId = ?1 "),
		@NamedQuery(name = "SiteMenuAcl.deleteByRole", query = "delete from SiteMenuAcl where roleId = ?1 ") })
public class SiteMenuAcl {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;							// 记录的编号
	
	private String roleId;						// 访问者角色的ID，与业务相关
	
	private String menuId;						// 导航菜单的编号，与RoleId共同作为主键

	private String appId;						// 对应的应用的编号

	private String Rights;						// 可访问菜单的操作权限列表，数组表示
}
