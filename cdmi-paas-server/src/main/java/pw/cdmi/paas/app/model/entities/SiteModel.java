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
 * 实体类，用以记录应用内置的导航功能模块列表.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_model")
public class SiteModel {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						// 导航功能模块列表编号

	@Column(length = 36)
	private String name;					// 模块名称

	private Boolean IsClass;				// 是否只是模块分类,1为true,0为false

	private String parentId;				// 当上级模块为分类时，这里可以填写上级模块的编码

	private String siteId;					// 对应的应用的编号

	private String resourceURL;				// 导航模块的访问URL,模块为分类时为空

	private String Rights;					// 该模块所拥有的全部权限标签列表，数组表示

	private Integer orderNumber;				// 排序号
}
