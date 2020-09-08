package pw.cdmi.open.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * **********************************************************
 * 员工与专员的关联对象
 * 
 * @author liujh
 * @version iSoc Service Platform, 2015-7-18
 ***********************************************************
 */
@Data
@Entity
@Table(name = "ept_employee_and_commissioner")
public class EmployeeAndCommissioner {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
    private String id;					// 部门群组与员工的关联对象编号

    private String employeeId;			// 员工编号

    private String commissionerId;		// 专员类型编号

}
