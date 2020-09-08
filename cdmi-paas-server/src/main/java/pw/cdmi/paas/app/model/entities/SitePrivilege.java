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
 * 实体类，记录指定应用系统的指定用户登录域下的操作权限
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_privilege")
public class SitePrivilege {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;							// 记录的编号

	private Integer loginRole;					// 对应的登录域的权限标识，与应用登录域的ID 1对1对应。

//	private Integer groupId;					// 对应的权限分组信息ID
	private String modelEnum;				    // 对应的应用功能模块的标示，之所以不用编号，是因为该值被内置到系统中

	private String name;					    // 权限名称

	@Column(length = 8, nullable = false)
	private String authEnum;				// 对应业务系统的权限的枚举

	private String siteId;					// 对应的应用的编号

	private String description;				// 对权限的备注
}
