package pw.cdmi.open.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/************************************************************
 * 部门群组与员工的关联对象
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
@Data
@Entity
@Table(name = "ept_employee_and_dept_group")
public class EmployeeAndDeptGroup {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;					// 部门群组与员工的关联对象编号

    private String employeeId;			// 员工编号

    private String deptGroupId;		// 部门群组编号

}
