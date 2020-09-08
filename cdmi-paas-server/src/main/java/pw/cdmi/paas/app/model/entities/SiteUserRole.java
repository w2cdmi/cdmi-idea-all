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
 * 实体表 ,记录指定应用下的用户所拥有的权限角色和自定义权限角色下的权限信息.
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月1日
 ************************************************************/
@Data
@Entity
@Table(name = "p_account_permission")
@NamedQueries({
        @NamedQuery(name = "SiteUserRole.findListByUser", query = "select sup from SiteUserRole sup where userId = ?1 "),
        @NamedQuery(name = "SiteUserRole.findListByRole", query = "select sup from SiteUserRole sup where roleId = ?1 "),
        @NamedQuery(name = "SiteUserRole.deleteByUser", query = "delete from SiteUserRole where userId = ?1 "),
        @NamedQuery(name = "SiteUserRole.deleteByRole", query = "delete from SiteUserRole where roleId = ?1 ") })
public class SiteUserRole {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;							// 记录的编号

    private String userId;						// 用户账号的Id,对应的是SiteUser表的Id

    private String appId;						// 对应的应用的编号

    private String roleEnum;					// 用户所拥有的角色值，

    private String roleId;

    // @Column(length = 4)
    // private String privilegeName; // 该用户拥有的权限枚举名,只有当选择了自定义角色时候，该列才有数据
}
