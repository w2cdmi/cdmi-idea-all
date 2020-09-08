package pw.cdmi.open.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import pw.cdmi.collection.PageView;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.model.entities.People;
import pw.cdmi.open.model.queryObject.EmployeeQuery;
import pw.cdmi.open.service.EmployeeService;
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public void createEmployee(Employee employee, People people) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateEmployee(Employee employee) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateEmployee(Employee employee, People people) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateUserInfo(Employee employee, People people) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteEmployeeById(String id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Map<String, Object> getEmployeeById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getSingleEmployeeById(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getEmployeeByPeopleId(String companyId, String peopleId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getEmployeeByAccountId(String companyId, String accountId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getEmployeeByUserId(List<Long> companyId, String userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getEmployeeByJobNumber(Iterable<String> companyId, String JobNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Employee getEmployeeByEmail(Iterable<String> companyId, String JobNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Map<String, Object>> findEmployeeByCondition(String code, String name, String telephone,
        Integer deptId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageView findEmployeeByConditionAndPage(Integer pageNo, Integer pageSize, String code, String name,
        String telephone, String deptName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Employee> getAllEmployee() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Employee> findEmployeeByDeptGroupId(String deptGroupId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCountEmployeeByDeptGroupId(String deptGroupId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Employee> findEmployeeByOfficeId(String officeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCountEmployeeByOfficeId(String officeId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Employee> findEmployeeNotOnOffice(String officeId, String name, String code) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Employee> findEmployeeByCommissionerId(String commissionerId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCountEmployeeByCommissionerId(String commissionerId) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Employee> findEmployeeNotIsCommissioner(String commissionerId, String name, String code) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getCountEmployeeByQuery(EmployeeQuery employeeQuery) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Employee> findEmployeeByQuery(EmployeeQuery employeeQuery) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Object> selectSiteUserIdAndEmployeeNameList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void createEmployeeAndAccount(Employee emp, String account, String pwd) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void adjustmentEmployee(Employee employee) {
        // TODO Auto-generated method stub
        
    }

}
