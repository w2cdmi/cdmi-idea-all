package pw.cdmi.open.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import pw.cdmi.open.model.PeopleIDCard;
import pw.cdmi.open.model.entities.Area;
import pw.cdmi.open.model.entities.Commissioner;
import pw.cdmi.open.model.entities.Company;
import pw.cdmi.open.model.entities.Department;
import pw.cdmi.open.model.entities.DepartmentGroup;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.model.entities.EmployeeAndCommissioner;
import pw.cdmi.open.model.entities.EmployeeAndDeptGroup;
import pw.cdmi.open.model.entities.EmployeeAndOffice;
import pw.cdmi.open.model.entities.Office;
import pw.cdmi.open.model.entities.People;
import pw.cdmi.open.model.entities.Position;
import pw.cdmi.open.model.entities.PositionalTitle;

/**
 * **********************************************************
 * 接口类，提供对企业组织的方法
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-6
 ***********************************************************
 */
public interface BusinessOrganizationService {

    // 分公司相关接口
    /**
     * 
     * 向数据库中插入一条新分公司信息
     * 
     * @param company 新分公司信息
     */
    public void createCompany(Company company);

    /**
     * 
     * 更新分公司信息
     * 
     * @param company 分公司信息
     */
    public void updateCompany(Company company);

    /**
     * 删除一条分公司信息
     * @param id 待删除的分公司信息的Id
     */
    public boolean deleteCompany(String id);

    /**
     * 根据分公司信息的id 获得分公司信息
     * @param id 分公司信息表的主键
     * @return 分公司信息
     */
    public Company getCompany(String id);

    /**
     * 根据公司信息的名称 获得公司信息
     * @param companyName 公司信息表的名称
     * @return 公司信息
     */
    public Company getCompanyByName(String companyName);

    /**
     * 根据公司openId 获得公司信息
     * @param companyName 公司信息表的名称
     * @return 公司信息
     */
    public Company getCompanyByOpenId(String openId);

    /**
     * 根据公司信息的工商注册名 获得公司信息
     * @param registrationName 公司信息表的工商注册名
     * @return 公司信息
     */
    public Company getCompanyByRegistrationName(String registrationName);

    /**
     * 根据公司信息的税务登记编号 获得公司信息
     * @param taxCode 公司信息表的税务登记编号
     * @return 公司信息
     */
    public Company getCompanyByTaxCode(String taxCode);

    /**
     * 
     * 根据公司的几个关键字段，判断公司信息是否存在.
     * 
     * @param name
     * @param codeCertificate
     * @param licenseNumber
     * @param taxCode
     * @return
     */
    public Company getCompanyByKeyFields(String name, String codeCertificate, String licenseNumber, String taxCode);

    /**
     * 获得当前应用的拥有企业的Id
     * @return
     */
    public String getCurrentCompanyId();

    /**
     * 获得当前应用的拥有企业
     * @return
     */
    public Company getCurrentCompany();

    /**
     * 获得当前应用的拥有企业及子公司的ID集合
     * @return
     */
    public List<String> getCurrentAndSubCompanyIds();

    /**
     * 获取总公司
     * @return 总公司信息
     */
    public Company getSuperCompany();

    /**
     * 
     * 查询所有总公司列表
     * 
     * @return 
     */
    public Iterable<Company> findParentCompany();

    /**
     * 
     * 根据条件查询企业列表
     * 
     * @param company 条件参数封装的对象
     * @return 满足条件的企业信息
     */
    public Page<Company> findCompanyList(Company company, int pageNo, int pageSize);

    /**
     * 
     * 查询所有分公司列表
     * 
     * @return 
     */
    public Iterable<Company> findAllCompany();

    /**
     * 
     * 用分页的方式查询所有分公司列表
     * @param pageNo 页数
     * @param pageSize 每页条数 
     * @return 
     */
    public Page<Company> findAllCompany(int pageNo, int pageSize);

    /**
     * 
     * 根据条件查询所有分公司列表
     * 
     * @param company 条件参数封装的对象
     * @return 满足条件的分公司信息
     */
    public Iterable<Company> findSubCompanyList(Company company);

    /**
     * 
     * 根据employeeId查询所有分公司列表
     * 
     * @param employeeId 
     * @return supervisorId为employeeId的分公司信息
     */
    public Iterable<Company> findSubCompanyListByEmployeeId(String employeeId);

    /**
     * 
     * 条件查询所有分公司分页列表
     * 
     * @param company 查询条件构建的分公司对象
     * @param pageNo 页数
     * @param pageSize 每页条数
     * @return 符合查询条件的所有分公司对象构成的LIST
     */
    public Page<Company> findSubCompanyListPage(Company company, int pageNo, int pageSize);

    // 办事处相关接口

    /**
     * 
     * 向数据库中插入一条新分公司信息
     * 
     * @param office 新办事处信息
     */
    public void createOffice(Office office);

    /**
     * 
     * 更新办事处信息
     * 
     * @param company 办事处信息
     */
    public void updateOffice(Office office);

    /**
     * 删除一条办事处信息
     * @param id 待删除的办事处信息的Id
     */
    public boolean deleteOffice(String id);

    /**
     * 根据办事处信息的id 获得办事处信息
     * @param id 办事处信息表的主键
     * @return 办事处信息
     */
    public Office getOffice(String id);

    /**
     * 
     * 根据办事处名称得到办事处信息.
     * 
     * @param companyId 对应企业id
     * @param officeName 办事处名称
     * @return 返回办事处信息
     */
    public Office getOfficeByName(String companyId, String officeName);

    /**
     * 
     * 查询所有办事处列表
     * 
     * @return 
     */
    public Iterable<Office> findAllOffice();

    /**
     * 
     * 用分页的方式查询所有办事处列表
     * @param pageNo 页数
     * @param pageSize 每页条数 
     * @return 
     */
    public Page<Office> findAllOffice(int pageNo, int pageSize);

    /**
     * 根据关键字获取办事处列表
     * 
     * @param keyword 关键字
     * @return
     */
    public Iterable<Office> findOfficeByKeyword(String keyword);

    /**
     * 根据employeeId获取办事处列表
     * 
     * @param employeeId 
     * @return supervisorId为employeeId的所有办事处
     */
    public Iterable<Office> findOfficeByEmployeeId(String employeeId);

    // 部门的相关接口

    /**
     * 
     * 向数据库中插入一条新部门信息
     * 
     * @param department 新部门信息
     */
    public void createDepartment(Department department);

    /**
     * 
     * 更新部门信息
     * 
     * @param department 部门信息
     */
    public void updateDepartment(Department department);

    /**
     * 删除一条部门信息
     * @param id 待删除的部门信息的Id
     */
    public boolean deleteDepartment(String id);

    /**
     * 根据部门信息的id 获得部门信息
     * @param id 部门信息表的主键
     * @return 部门信息
     */
    public Department getDepartment(String id);

    /**
     * 
     * 根据当前企业id与部门名称得到部门信息.
     * 
     * @param companyId
     * @param parentId
     * @param deptName
     * @return
     */
    public Department getDepartmentByName(String companyId, String parentId, String deptName);

    /**
     * 
     * 查询所有部门列表
     * 
     * @return 部门信息列表
     */
    public Iterable<Department> findAllDepartment();

    /**
     * 
     * 查询第一级部门id和名称列表
     * 
     * @return 部门信息列表
     */
    public Iterable<Department> findFirstDepartment();

    /**
     * 
     * 根据条件查询相关部门列表
     * @param queryObject 条件参数封装的对象
     * @return 满足条件的部门列表
     */
    public Iterable<Department> findDepartmentList(Department queryObject);

    /**
     * 
     * 根据条件查询所有部门分页列表
     * 
     * @param queryObject 条件参数封装的对象
     * @param pageNo 页数
     * @param pageSize 每页条数
     * @return 满足条件的部门信息进行分页显示
     */
    public Page<Department> findDepartmentListPage(Department queryObject, int paageNo, int pageSize);

    // 部门群组的相关接口
    /**
     * 
     * 向数据库中创建一条部门群组信息
     * 
     * @param departmentGroup 新部门群组信息
     */
    public void createDepartmentGroup(DepartmentGroup departmentGroup);

    /**
     * 
     * 更新部门群组信息
     * 
     * @param departmentGroup 部门群组信息
     */
    public void updateDepartmentGroup(DepartmentGroup departmentGroup);

    /**
     * 删除一条部门群组信息
     * @param id 待删除的部门群组信息的Id
     */
    public boolean deleteDepartmentGroup(String id);

    /**
     * 根据部门群组信息的id 获得部门群组信息
     * @param id 部门群组信息表的主键
     * @return 部门群组信息
     */
    public DepartmentGroup getDepartmentGroup(String id);

    /**
     * 
     * 根据部门id与部门群组名称得到部门群组信息.
     * 
     * @param deptId
     * @param deptGroupName
     * @return
     */
    public DepartmentGroup getDepartmentGroupByName(String deptId, String deptGroupName);

    /**
     * 根据员工的id 获得部门群组列表
     * @param id 员工信息表的主键
     * @return 部门群组信息
     */
    public Iterable<DepartmentGroup> findDeptGroupByEmployeeId(String employeeId);

    /**
     * 
     * 查询所有部门群组列表
     * 
     * @return 部门群组信息列表
     */
    public Iterable<DepartmentGroup> findAllDepartmentGroup();

    /**
     * 
     * 根据部门id查询所有部门群组列表
     * 
     * @param departmentId 部门id
     * @return 
     */
    public Iterable<DepartmentGroup> findDepartmentGroupByDeptId(String departmentId);

    /**
     * 
     * 根据关键字查询所有部门群组列表
     * 
     * @param keyword
     * 
     * @return 满足条件的部门群组信息
     */
    public Iterable<DepartmentGroup> findDepartmentGroupByKeyword(String keyword);

    // 专员的相关接口
    /**
     * 
     * 向数据库中创建一条专员信息
     * 
     * @param commissioner 新专员信息
     */
    public void createCommissioner(Commissioner commissioner);

    /**
     * 
     * 更新专员信息
     * 
     * @param commissioner 专员信息
     */
    public void updateCommissioner(Commissioner commissioner);

    /**
     * 删除一条专员信息
     * @param id 待删除的专员信息的Id
     */
    public boolean deleteCommissioner(String id);

    /**
     * 根据专员的id 获得专员信息
     * @param id 专员表的主键
     * @return 专员信息
     */
    public Commissioner getCommissioner(String id);

    /**
     * 
     * 根据当前企业id与专员名称得到专员信息.
     * 
     * @param companyId
     * @param commmissionerName
     * @return
     */
    public Commissioner getCommissionerByName(String companyId, String commissionerName);

    /**
     * 
     * 根据专员名称模糊查询专员列表
     * 
     * @param commissinerName 专员信息表的名称
     * @return 
     */
    public Iterable<Commissioner> findCommissionerByName(String commissionerName);

    /**
     * 
     * 查询所有专员列表
     * 
     * @return 专员信息列表
     */
    public Iterable<Commissioner> findAllCommissioner();

    // 片区的接口
    /**
     * 
     * 向数据库中创建一条片区信息
     * 
     * @param area 新片区信息
     */
    public void createArea(Area area);

    /**
     * 
     * 更新片区信息
     * 
     * @param area 片区信息
     */
    public void updateArea(Area area);

    /**
     * 删除一条片区信息
     * @param id 待删除的片区信息的Id
     */
    public boolean deleteArea(String id);

    /**
     * 根据片区信息的id 获得片区信息
     * @param id 片区表的主键
     * @return 片区信息
     */
    public Area getArea(String id);

    /**
     * 
     * 查询所有片区列表
     * 
     * @return 片区信息列表
     */
    public Iterable<Area> findAllArea();

    /**
     * 
     * 根据条件查询所有片区列表
     * 
     * @param area 条件参数封装的对象
     * 
     * @return 满足条件的片区信息
     */
    public Iterable<Area> findAreaList(Area area);

    // 岗位的接口

    /**
     * 
     * 向数据库中创建一条岗位信息
     * 
     * @param position 新岗位信息
     */
    public void createPosition(Position position);

    /*
     * 
     * 更新岗位信息
     * 
     * @param position 岗位信息
     */
    public void updatePosition(Position position);

    /**
     * 删除一条岗位信息
     * @param id 待删除的岗位信息的Id
     */
    public boolean deletePosition(String id);

    /**
     * 根据岗位的id 获得岗位信息
     * @param id 岗位表的主键
     * @return 岗位信息
     */
    public Position getPosition(String id);

    /**
     * 
     * 根据当前企业id与专员名称得到专员信息.
     * 
     * @param companyId
     * @param positionName
     * @return
     */
    public Position getPositionByName(String companyId, String positionName);

    /**
     * 
     * 根据岗位的名称模糊查询岗位列表
     * 
     * @param positionName 岗位信息表的名称
     * @return 
     */
    public Iterable<Position> findPositionByName(String positionName);

    /**
     * 
     * 查询所有岗位列表
     * 
     * @return 岗位信息列表
     */
    public Iterable<Position> findAllPosition();

    // 职称的接口
    /**
     * 
     * 向数据库中创建一条职称信息
     * 
     * @param positionalTitle 新职称信息
     */
    public void createPositionalTitle(PositionalTitle positionalTitle);

    /**
     * 
     * 更新职称信息
     * 
     * @param positionalTitle 职称信息
     */
    public void updatePositionTitle(PositionalTitle positionalTitle);

    /**
     * 删除一条职称信息
     * @param id 待删除的职称信息的Id
     */
    public boolean deletePositionalTitle(String id);

    /**
     * 根据职称的id 获得职称信息
     * @param id 职称表的主键
     * @return 职称信息
     */
    public PositionalTitle getPositionalTitle(String id);

    /**
     * 
     * 根据岗位id与职称名称得到职称信息.
     * @param companyId
     * @param positionalTitleName
     * @return
     */
    public PositionalTitle getPositionalTitleByName(String companyId, String positionalTitleName);

    /**
     * 
     * 根据条件查询所有职称列表
     * @param positionalTitle 条件参数封装成的PositionalTitle对象
     * @return 
     */
    public Iterable<PositionalTitle> findPositionalTitle(PositionalTitle positionalTitle);

    public Iterable<PositionalTitle> findPositionalTitle(String name, String positionName);

    /**
     * 
     * 查询所有职称列表
     * 
     * @return 所有的职称信息
     */
    public Iterable<PositionalTitle> findAllPositionalTitle();

    /**
     * 向系统中添加一个新的雇员
     * @param employee 新的雇员信息
     */
    public void createEmployee(String companyId, Employee employee);

    public void createEmployee(String companyId, Employee employee, PeopleIDCard people);

    public void updateEmployee(Employee employee);

    public void updateEmployee(Employee employee, People people);

    public void deleteEmployeeById(String id);

    public Map<String, Object> getEmployeeById(String id);

    public Employee getSingleEmployeeById(String id);

    public Employee getEmployeeByCompanyIdAndCode(String companyId, String code);

    public Employee getEmployeeByAccountId(String accountId);

    public Employee getSingleEmployeeByPeopleId(String peopleId, String employeeId);

    public Iterable<Employee> findEmployeeByCondition(String code, String name, String telephone, String deptId);

    public Page<Employee> findEmployeeByConditionAndPage(int pageNo, int pageSize, String code, String name, String telephone,
        String deptId);

    public Iterable<Employee> getAllEmployee();

    /**
     *  根据群组id查询员工列表
     * 
     * @param deptGroupId 群组id
     * @return
     */
    public Iterable<Employee> findEmployeeByDeptGroupId(String deptGroupId);

    /**
     * 
     * 根据群组id得到员工个数
     * 
     * @param deptGroupId
     * @return
     */
    public int getCountEmployeeByDeptGroupId(String deptGroupId);

    /**
     *  根据办事处id查询员工列表
     * 
     * @param officeId 办事处id
     * @return
     */
    public Iterable<Employee> findEmployeeByOfficeId(String officeId);

    /**
     * 
     * 根据办事处id得到员工个数
     * 
     * @param officeId
     * @return
     */
    public int getCountEmployeeByOfficeId(String officeId);

    /**
     * 
     * 根据办事处id得到不在该办事处的员工列表
     *  
     * @param officeId
     * @param name
     * @param code
     * @return
     */
    public Iterable<Employee> findEmployeeNotOnOffice(String officeId, String name, String code);

    /**
     *  根据专员id查询员工列表
     * 
     * @param commissionerId 专员id
     * @return
     */
    public Iterable<Employee> findEmployeeByCommissionerId(String commissionerId);

    /**
     * 
     * 根据专员id得到员工个数
     * 
     * @param commissionerId
     * @return
     */
    public int getCountEmployeeByCommissionerId(String commissionerId);

    /**
     * 
     * 根据专员id得到不是该专员的员工列表
     *  
     * @param commissionerId
     * @param name
     * @param code
     * @return
     */
    public Iterable<Employee> findEmployeeNotIsCommissioner(String commissionerId, String name, String code);

    /**
     * 
     * 根据条件得到员工个数
     * 
     * @param employeeQuery
     * @return
     */
    public int getCountEmployeeByQuery(Employee employee);

    /**
     * 
     * 根据条件得到在该公司下的员工列表
     *  
     * @param string
     * @return
     */
    public Iterable<Employee> findEmployeeByQuery(Employee employee);

    // 群组与员工关联
    /**
     * 
     * 创建群组与员工关联
     *
     */
    public void createEmployeeAndDeptGroup(EmployeeAndDeptGroup employeeAndDeptGroup);

    /**
     * 
     * 得到群组与员工关联
     * 
     * @param employeeId
     * @param deptGroupId
     * @return
     */
    public EmployeeAndDeptGroup getEmployeeAndDeptGroup(String employeeId, String deptGroupId);

    /**
     * 
     * 删除群组与员工的关联
     * 
     * @param employeeId
     * @param deptGroupId
     */
    public int deleteEmployeeAndDeptGroup(String employeeId, String deptGroupId);

    /**
     * 
     * 删除群组与员工的关联
     * 
     * @param deptGroupId
     */
    public void deleteEmployeeAndDeptGroupByDeptGroup(String deptGroupId);

    /**
     * 
     * 删除群组与员工的关联
     * 
     * @param employeeId
     */
    public void deleteEmployeeAndDeptGroupByEmployee(String employeeId);

    // 办事处与员工关联
    /**
     * 创建办事处与员工的关联
     * 
     * @param employeeAndOffice
     */
    public void createEmployeeAndOffice(EmployeeAndOffice employeeAndOffice);

    /**
     * 
     * 得到办事处与员工关联
     * 
     * @param employeeId
     * @param officeId
     * @return
     */
    public EmployeeAndOffice getEmployeeAndOffice(String employeeId, String officeId);

    /**
     * 
     * 得到办事处与员工关联
     * 
     * @param employeeId
     * @return
     */
    public EmployeeAndOffice getEmployeeAndOffice(String employeeId);

    /**
     * 
     * 删除办事处与员工的关联
     * 
     * @param employeeId
     * @param officeId
     */
    public boolean deleteEmployeeAndOffice(String employeeId, String officeId);

    /**
     * 
     * 删除办事处与员工的关联
     * 
     * @param officeId
     */
    public void deleteEmployeeAndOfficeByOfficeId(String officeId);

    /**
     * 
     * 删除办事处与员工的关联
     * 
     * @param employeeId
     */
    public void deleteEmployeeAndOfficeByEmployeeId(String employeeId);

    /**
     * 
     * 更改办事处与员工的关联
     * 
     * @param empAndOffice
     */
    public void updateEmployeeAndOffice(EmployeeAndOffice empAndOffice);

    // 专员与员工关联
    /**
     * 创建专员与员工的关联
     * 
     * @param employeeAndCommissioner
     */
    public void createEmployeeAndCommissioner(EmployeeAndCommissioner employeeAndCommissioner);

    /**
     * 
     * 得到专员与员工关联
     * 
     * @param employeeId
     * @param commissionerId
     * @return
     */
    public EmployeeAndCommissioner getEmployeeAndCommissioner(String employeeId, String commissionerId);

    /**
     * 
     * 删除专员与员工的关联
     * 
     * @param employeeId
     * @param commissionerId
     */
    public boolean deleteEmployeeAndCommissioner(String employeeId, String commissionerId);

    /**
     * 
     * 删除专员与员工的关联
     * 
     * @param commissionerId
     */
    public void deleteEmployeeAndCommissionerByCommissioner(String commissionerId);

    /**
     * 
     * 删除专员与员工的关联
     * 
     * @param employeeId
     */
    public void deleteEmployeeAndCommissioner(String employeeId);

    /**
     * 补充组织机构单位的开户行信息
     * @param openId 组织机构单位的OpenId
     * @param bankid 对应的银行ID
     * @param bankname 对应的银行名称
     * @param accountName 对应组织机构单位在银行的开户名称
     * @param accountNumber 对应组织机构单位在银行的开户银行账号
     */
    public void fillBankInfo(String openId, String bankid, String bankname, String accountName, String accountNumber);
}
