package pw.cdmi.paas.app.model.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 实体类，用以记录应用中的功能导航菜单.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_menu")
@NamedQueries({
		@NamedQuery(name = "SiteMenu.findListByRoleId", query = "select m from SiteMenu m where m.id in "
				+ "(select sm.menuId from SiteMenuAcl sm where sm.roleId = ?1) and m.parentId = ?2 "),
		@NamedQuery(name = "SiteMenu.findListByParent", query = "select m from SiteMenu m where m.parentId = ?1 "),
		@NamedQuery(name = "SiteMenu.findAll", query = "select m from SiteMenu m where 1 = 1 "),
		@NamedQuery(name = "SiteMenu.findMenuById", query = "select m from SiteMenu m where m.id = ?1 "),
		@NamedQuery(name = "SiteMenu.deleteById", query = "delete from SiteMenu where id = ?1 ")})
        
public class SiteMenu {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						// 导航菜单对象编号

	@Column(length = 36)
	private String name;					// 导航菜单名称，默认等于模块名称

	private Boolean beClass;				// 是否为菜单分类,1为true,0为false

	private Long parentId;					// 当上级菜单为分类时，这里可以填写上级菜单的编码

	private Integer modelId;				// 对应的应用功能的编号，不能选择模块分类

	private String appId;					// 对应的应用站点的编号

	private String orderNumber;				// 排序号

	private String resourceURL;				// 对应的导航模块的访问URL,模块为分类时为空，冗余字段，

	private String rights;					// 该模块所拥有的全部权限标签列表，数组表示

	@Transient
	private Iterable<SiteMenu> subMenus;		// 用于存放子菜单列表
}
