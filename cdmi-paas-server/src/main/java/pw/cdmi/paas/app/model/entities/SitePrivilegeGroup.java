package pw.cdmi.paas.app.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 实体类，记录一个应用站点中某个用户登录域的操作权限分组.
 * 该表被取消
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_privilege_group")
@Deprecated
public class SitePrivilegeGroup {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;							// 记录的编号

	private Integer loginRole;					// 对应的登录域的权限标识，与应用登录域的ID 1对1对应。

	private String name;					// 权限分组名称

	private String siteId;					// 对应的应用的编号
}
