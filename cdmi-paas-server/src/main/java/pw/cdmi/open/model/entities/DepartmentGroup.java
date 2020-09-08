package pw.cdmi.open.model.entities;

import javax.persistence.Column;
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
 * 部门群组信息实体
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
@NamedQueries({ @NamedQuery(name = "DepartmentGroup.findAll", query = "from DepartmentGroup dg"),
        @NamedQuery(name = "DepartmentGroup.findByDeptId", query = "from DepartmentGroup dg where dg.deptId = :deptId"),
        @NamedQuery(name = "DepartmentGroup.findByName", query = "from DepartmentGroup dg where dg.name = :name"),
        @NamedQuery(name = "DepartmentGroup.findByKeyword", query = "from DepartmentGroup dg where dg.name like :name") })
@Data
@Entity
@Table(name = "ept_dept_group")
public class DepartmentGroup {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;

    @Column(nullable = false)
    private String name;					// 部门群组信息编号

    private String deptId;					// 对应部门编号

    private String description;				// 群组说明

    private String supervisorId;			// 群组管理员，关联对应的雇员编号

    private String companyId;				// 对应当前企业id

}
