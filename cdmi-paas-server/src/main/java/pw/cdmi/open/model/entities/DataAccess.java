package pw.cdmi.open.model.entities;

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
 * 数据权限信息实体
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
@Data
@Entity
@Table(name = "p_data_access")
@NamedQueries({
		@NamedQuery(name = "DataAccess.findListByRoleId", query = "select d from DataAccess d where id in "
				+ "(select dataAccessId from RoleAndDataAccess where roleId = ?1) and parentId = ?2 "),
		@NamedQuery(name = "DataAccess.findAll", query = "select d from DataAccess d where parentId = ?1 ") })
public class DataAccess {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;					// 数据权限信息编号

	@Column(length = 30)
	private String name;				// 权限名称

	private Integer parentId;			// 父级权限编号

	@Column(length = 150)
	private String description;			// 权限描述

	@Transient
	private Iterable<DataAccess> subDataAccess;		// 下级数据权限列表

}
