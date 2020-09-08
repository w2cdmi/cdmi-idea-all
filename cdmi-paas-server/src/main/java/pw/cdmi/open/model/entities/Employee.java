package pw.cdmi.open.model.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import pw.cdmi.open.model.Education;
import pw.cdmi.open.model.EmployeeProperty;
import pw.cdmi.open.model.LaborSystem;
import pw.cdmi.open.model.Nation;
import pw.cdmi.open.model.PositionNature;
import pw.cdmi.open.model.Sex;
import pw.cdmi.open.model.WorkStatus;

/************************************************************
 * TODO(对类的简要描述说明 – 必须).
 * TODO(对类的作用含义说明 – 可选).
 * TODO(对类的使用方法说明 – 可选).
 * 
 * @author WUWEI
 * @version iSoc Service Platform, 2015年3月2日
 ************************************************************/
@Data
@Entity
@Table(name = "ept_employee")
@NamedQueries({
	@NamedQuery(name = "Employee.findAll", query = "select e from Employee e"),
	@NamedQuery(name = "Employee.findAllByCondition", query = "select e from Employee e where e.status ='OK'"),
	@NamedQuery(name = "Employee.findEmployeesByCondition", query = "select e from Employee e, Department d where e.deptId = d.id and e.status ='OK'"),
	@NamedQuery(name = "Employee.getCount", query = "select count(*) from Employee e where e.status ='OK'"),
	@NamedQuery(name = "Employee.getEmployeesCount", query = "select count(*) from Employee e, Department d where e.deptId = d.id and e.status ='OK'"),
	@NamedQuery(name = "Employee.findByPeople", query = "from Employee e where e.companyId = :companyId and e.peopleId = :peopleId and e.status ='OK'"),
	@NamedQuery(name = "Employee.findByIdAndName", query="select new Map(e.id as id,e.name as name) from Employee e where e.status ='OK'")})
public class Employee {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;						// 员工信息编号

	private String code;					// 员工的工号

	private String peopleId;					// 员工对应的身份信息

	private String name;					// 员工的名称,同身份信息

	private String email;					// 员工的办公邮件

	@Column(length = 20)
	private String telephone;				// 员工的办公电话

	private String deptId;						// 对应的部门编号

	private String companyId;					// 对应的企业编号
	
	private String officeId;					// 对应的办事处编号
	
	private String accountId;					// 员工拥有的IT权限的用户身份，对应Account的Id字段

	@Enumerated(EnumType.STRING)
	private PositionNature positionNature;	// 岗位性质，枚举,如:计件，绩效

	@Enumerated(EnumType.STRING)
	private LaborSystem laborSystem;		// 所属编制，行政编制，事业编制，社聘人员，劳务派遣人员。

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date joinTime;					// 加入公司的时间
	
	@Enumerated(EnumType.STRING)
	private WorkStatus status;				// 员工在职状态，NN 枚举，见雇员在职状态枚举

	@Enumerated(EnumType.STRING)
	private EmployeeProperty employeeProperty;	// 员工性质。如：正式、试用、实习

	private String positionId;					// 对应的岗位编号

	private String positionalTitleId;			// 对应的职称编号

	@Column(length = 30)
	private String emergencyContactName;	// 紧急联系人

	@Column(length = 20)
	private String emergencyContactPhone;	// 紧急联系人电话

	@Column(length = 30)
	private String emergencyContactRelation;// 与紧急联系人关系

	@Enumerated(EnumType.STRING)
	private Education education;			// 学历

	private String deptManagerId;			// 部门主管所对应的部门编号

	/**********未保存公民信息时候，将信息保存到员工表*****************/
	@Enumerated(EnumType.STRING)
	private Sex sex;								// 个人的性别,对应Sex枚举

	@Enumerated(EnumType.STRING)
	private Nation nation;							// 个人的民族

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;							// 出生日期
	
	/***** 人员调整时是否勾选职位调整checkbox ，不映射进数据库*********/
	@Transient  //不映射进数据库
	private String changeDeptManagerId;
}
