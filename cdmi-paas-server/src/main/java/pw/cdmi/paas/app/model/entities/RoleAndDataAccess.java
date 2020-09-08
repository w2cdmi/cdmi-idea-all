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
 * 角色与数据权限的关联对象实体
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
@Data
@Entity
@Table(name = "p_role_and_data_access")
@NamedQueries({
	@NamedQuery(name = "RoleAndDataAccess.findListByRole", query = "select rada from RoleAndDataAccess rada where roleId = ?1 "),
	@NamedQuery(name = "RoleAndDataAccess.deleteByRole", query = "delete from RoleAndDataAccess where roleId = ?1 ")
})
public class RoleAndDataAccess {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;					//角色与数据权限的关联信息编号
	private String roleId;				//对应的角色编号
	private String dataAccessId;		//对应的数据权限编号
	private String siteId;				//对应应用的编号

}
