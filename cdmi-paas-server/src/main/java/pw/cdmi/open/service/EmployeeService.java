package pw.cdmi.open.service;

import java.util.List;
import java.util.Map;

import pw.cdmi.collection.PageView;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.model.entities.People;
import pw.cdmi.open.model.queryObject.EmployeeQuery;

/************************************************************
 * 接口类，提供对员工管理的操作方法
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-6
 ************************************************************/
public interface EmployeeService {

    public void createEmployee(Employee employee, People people);

    public void updateEmployee(Employee employee);

    public void updateEmployee(Employee employee, People people);

    public void updateUserInfo(Employee employee, People people);

    public void deleteEmployeeById(String id);

    public Map<String, Object> getEmployeeById(String id);

    public Employee getSingleEmployeeById(String id);

    public Employee getEmployeeByPeopleId(String companyId, String peopleId);

    public Employee getEmployeeByAccountId(String companyId, String accountId);

    /*public Employee getEmployeeByUserId(Long companyId, Long userId);*/

    public Employee getEmployeeByUserId(List<Long> companyId, String userId);

    /*public Employee getEmployeeByJobNumber(Long companyId, String JobNumber);*/

    public Employee getEmployeeByJobNumber(Iterable<String> companyId, String JobNumber);

    public Employee getEmployeeByEmail(Iterable<String> companyId, String JobNumber);

    public List<Map<String, Object>> findEmployeeByCondition(String code, String name, String telephone,
        Integer deptId);

    public PageView findEmployeeByConditionAndPage(Integer pageNo, Integer pageSize, String code, String name,
        String telephone, String deptName);

    public List<Employee> getAllEmployee();

    /**
     *  根据群组id查询员工列表
     * 
     * @param deptGroupId 群组id
     * @return
     */
    public List<Employee> findEmployeeByDeptGroupId(String deptGroupId);

    /**
     * 
     * 根据群组id得到员工个数
     * 
     * @param deptGroupId
     * @return
     */
    public long getCountEmployeeByDeptGroupId(String deptGroupId);

    /**
     *  根据办事处id查询员工列表
     * 
     * @param officeId 办事处id
     * @return
     */
    public List<Employee> findEmployeeByOfficeId(String officeId);

    /**
     * 
     * 根据办事处id得到员工个数
     * 
     * @param officeId
     * @return
     */
    public long getCountEmployeeByOfficeId(String officeId);

    /**
     * 
     * 根据办事处id得到不在该办事处的员工列表
     *  
     * @param officeId
     * @param name
     * @param code
     * @return
     */
    public List<Employee> findEmployeeNotOnOffice(String officeId, String name, String code);

    /**
     *  根据专员id查询员工列表
     * 
     * @param commissionerId 专员id
     * @return
     */
    public List<Employee> findEmployeeByCommissionerId(String commissionerId);

    /**
     * 
     * 根据专员id得到员工个数
     * 
     * @param commissionerId
     * @return
     */
    public long getCountEmployeeByCommissionerId(String commissionerId);

    /**
     * 
     * 根据专员id得到不是该专员的员工列表
     *  
     * @param commissionerId
     * @param name
     * @param code
     * @return
     */
    public List<Employee> findEmployeeNotIsCommissioner(String commissionerId, String name, String code);

    /**
     * 
     * 根据条件得到员工个数
     * 
     * @param employeeQuery
     * @return
     */
    public long getCountEmployeeByQuery(EmployeeQuery employeeQuery);

    /**
     * 
     * 根据条件得到在该公司下的员工列表
     *  
     * @param string
     * @return
     */
    public List<Employee> findEmployeeByQuery(EmployeeQuery employeeQuery);

    /**
     * 查询SiteUserId和对应的员工名称
     * 
     * @return	userId与employeeName集合
     */
    public List<Object> selectSiteUserIdAndEmployeeNameList();

    /**
     * 创建员工和IT账号
     * @return
     */
    public void createEmployeeAndAccount(Employee emp, String account, String pwd);

    public void adjustmentEmployee(Employee employee);

}
