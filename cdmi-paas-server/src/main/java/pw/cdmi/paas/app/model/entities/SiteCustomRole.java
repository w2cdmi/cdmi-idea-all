package pw.cdmi.paas.app.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "p_custom_role")
public class SiteCustomRole {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;							//记录的编号
	private Integer loginRole;					//对应的登录域的权限标识枚举值，与应用登录域的ID 1对1对应。
	private String name;						//权限名称
	@Column(length=2, nullable = false)
	private Integer customRoleEnumValue;		//对应p_role表中的自定义角色枚举值
	private String siteId;						//对应的应用的编号
	private String description;					//对权限的备注
}

