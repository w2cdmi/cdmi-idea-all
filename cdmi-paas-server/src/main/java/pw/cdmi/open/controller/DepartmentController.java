package pw.cdmi.open.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.model.entities.Company;
import pw.cdmi.open.model.entities.Department;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.service.BusinessOrganizationService;
import pw.cdmi.open.service.EmployeeService;
import pw.cdmi.utils.UUIDUtils;

/**
 * **********************************************************
 * 控制类，提供对部门的业务操作方法
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-11
 ***********************************************************
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private BusinessOrganizationService organizationService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 
     * 根据传入的部门信息，创建一个部门
     * 
     * @param department 部门信息封装的department对象
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createDepartment(Department department) {
        Map<String, Object> map = new HashMap<String, Object>();

        if (StringUtils.isBlank(department.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        try {
            Employee employee = null;
            department.setCode(UUIDUtils.getUUIDTo64());
            department.setCreateDate(new Date());
            if (department.getParentId() != null) {
                Department dept = organizationService.getDepartment(department.getParentId());
                int level = dept.getLevel();
                department.setLevel(level + 1);
            } else {
                department.setLevel(1);
            }
            if (department.getSupervisorId() != null) {
                employee = employeeService.getSingleEmployeeById(department.getSupervisorId());
                if (employee == null) {
                    department.setSupervisorId(null);// 主管并不存在
                }
                if (employee != null && null != employee.getDeptId() && employee.getDeptId() != department.getId()
                        && employee.getDeptId() != null) {
                    map.put("message", "部门主管并不在本部门！");
                    return map;
                }
            }
            String companyId = organizationService.getCurrentCompanyId();
            department.setCompanyId(companyId);
            organizationService.createDepartment(department);
            if (null != employee) {
                employee.setDeptId(department.getId());
                employee.setDeptManagerId(department.getId());
                employeeService.updateEmployee(employee);
            }
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 
     * 根据部门id删除部门
     * 
     * @param id 部门id
     * @return
     */
    @RequestMapping(value = "/delete/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, Object> deleteDepartment(@PathVariable String ids) {
        boolean b = true;
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(ids)) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        String[] idStrings = ids.split(",");
        for (String id : idStrings) {
            try {
                b = organizationService.deleteDepartment(id);
            } catch (Exception e) {
                b = false;
                break;
            }
        }
        if (b == true) {
            map.put("message", "success");
        } else {
            map.put("message", "error");
        }
        return map;
    }

    /**
     * 
     * 根据部门修改部门信息
     * 
     * @param department 部门对象
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updateDeparment(Department department) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Department dept = organizationService.getDepartment(department.getId());
            dept.setName(department.getName());
            dept.setDescription(department.getDescription());
            if (department.getSupervisorId() != null) {
                Employee employee = employeeService.getSingleEmployeeById(department.getSupervisorId());
                Employee employeeBefore = employeeService.getSingleEmployeeById(dept.getSupervisorId());
                if (null != employee && null != employee.getDeptId() && department.getId() != employee.getDeptId()) {
                    map.put("message", "该员工不在本部门！");
                    return map;
                } else {
                    dept.setSupervisorId(department.getSupervisorId());
                    String deptManagerId = employee.getDeptManagerId();
                    if (employeeBefore != null && null != employeeBefore.getDeptManagerId()) {
                        employeeBefore.setDeptManagerId(null);
                        employeeService.updateEmployee(employeeBefore);
                    }
                    if (deptManagerId == null || deptManagerId == dept.getId()) {
                        employee.setDeptManagerId(department.getId());
                        employee.setDeptId(department.getId());
                        employeeService.updateEmployee(employee);
                    } else {
                        map.put("message", "该员工已是其他部门主管，不能再设为主管！");
                        return map;
                    }
                }
            } else {
                String supervisorId = dept.getSupervisorId();
                if (supervisorId != null) {
                    Employee employee = employeeService.getSingleEmployeeById(supervisorId);
                    employee.setDeptManagerId(null);
                    employeeService.updateEmployee(employee);
                }
                dept.setSupervisorId(null);
            }
            if (department.getParentId() != null) {
                Department depart = organizationService.getDepartment(department.getParentId());
                int level = depart.getLevel();
                dept.setLevel(level + 1);
            } else {
                dept.setLevel(1);
            }
            dept.setParentId(department.getParentId());
            dept.setBuiltDate(department.getBuiltDate());
            dept.setCompanyId(department.getCompanyId());
            organizationService.updateDepartment(dept);
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 
     * 根据部门id查询部门详细信息
     * 
     * @param id 部门id
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getDepartment(String id) {
        if (id == null) {
        }
        Department dept = organizationService.getDepartment(id);
        JSONObject json = new JSONObject();
        json.put("id", dept.getId());
        json.put("name", dept.getName());
        json.put("level", dept.getLevel());
        if (dept.getBuiltDate() != null) {
            json.put("builtDateTime", dept.getBuiltDate().getTime());
        }
        if (dept.getParentId() != null) {
            Department parentDept = organizationService.getDepartment(dept.getParentId());
            if (parentDept != null) {
                json.put("parentName", parentDept.getName());
            }
        }
        json.put("createDate", dept.getCreateDate());
        json.put("Description", dept.getDescription());
        json.put("code", dept.getCode());
        if (dept.getCompanyId() != null) {
            Company company = organizationService.getCompany(dept.getCompanyId());
            if (company != null) {
                json.put("companyName", company.getName());
            }
        }
        if (dept.getSupervisorId() != null) {
            Employee employee = employeeService.getSingleEmployeeById(dept.getSupervisorId());
            if (employee == null) {
                json.put("supervisorName", "");
            } else {
                json.put("supervisorName", employee.getName());
            }
        }
        return json;
    }

    /**
     * 
     * 根据查询条件得到部门列表
     * 
     * @param department 查询条件封装成的department对象
     * @return
     */
    @RequestMapping(value = "/findList")
    @ResponseBody
    public JSONArray findDepartmentList(String name) {
        Department queryObject = new Department();
        queryObject.setName(name);
        queryObject.setCompanyId(organizationService.getCurrentCompanyId());
        Iterable<Department> list = organizationService.findDepartmentList(queryObject);
        JSONArray array = new JSONArray();
        for (Department dept : list) {
            JSONObject json = new JSONObject();
            json.put("id", dept.getId());
            json.put("name", dept.getName());
            json.put("code", dept.getCode());
            json.put("parentId", dept.getParentId());
            if (dept.getParentId() != null) {
                Department parentDept = organizationService.getDepartment(dept.getParentId());
                if (parentDept != null) {
                    json.put("parentName", parentDept.getName());
                }
            }
            json.put("createDate", dept.getCreateDate());
            json.put("description", dept.getDescription());
            if (dept.getBuiltDate() != null) {
                json.put("builtDate", dept.getBuiltDate());
            } else {
                json.put("builtDate", "");
            }
            json.put("supervisorId", dept.getSupervisorId());
            if (dept.getSupervisorId() != null) {
                Employee employee = employeeService.getSingleEmployeeById(dept.getSupervisorId());
                if (employee != null) {
                    json.put("supervisorName", employee.getName());
                } else {
                    json.put("supervisorName", "");
                }
            }
            json.put("level", dept.getLevel());
            json.put("companyId", dept.getCompanyId());
            if (dept.getCompanyId() != null) {
                Company company = organizationService.getCompany(dept.getCompanyId());
                if (company != null) {
                    json.put("companyName", company.getName());
                }
            }
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 根据上级部门ID查询部门列表，用于下拉列表
     * 
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/findListBySecondId")
    @ResponseBody
    public JSONArray findListBySecondId(String id) {
    	Iterable<Department> list = null;
        Department queryObject = new Department();
        if (id == null) {
            list = null;
        } else {
            queryObject.setParentId(id);
            list = organizationService.findDepartmentList(queryObject);
        }
        JSONArray array = new JSONArray();
        for (Department dept : list) {
            JSONObject json = new JSONObject();
            json.put("id", dept.getId());
            json.put("name", dept.getName());
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 根据上级部门ID查询部门列表，用于下拉列表
     * 
     * @param parentId
     * @return
     */
    @RequestMapping(value = "/findListByFirstId")
    @ResponseBody
    public JSONArray findListByFirstId(String id, String secondId) {
    	Iterable<Department> list = null;
        Department queryObject = new Department();
        if (id == null) {
            list = null;
        } else {
            queryObject.setParentId(id);
            list = organizationService.findDepartmentList(queryObject);
        }
        JSONArray array = new JSONArray();
        for (Department dept : list) {
            if (org.springframework.util.StringUtils.isEmpty(secondId) || secondId != dept.getId()) {
                JSONObject json = new JSONObject();
                json.put("id", dept.getId());
                json.put("name", dept.getName());
                array.add(json);
            }
        }
        return array;
    }

    /**
     * 
     * 查找一级部门列表，用于下拉列表
     * 
     * @return
     */
    @RequestMapping(value = "/findFirstList")
    @ResponseBody
    public JSONArray findFirstList() {
    	Iterable<Department> list = null;
        list = organizationService.findFirstDepartment();
        JSONArray array = new JSONArray();
        for (Department dept : list) {
            JSONObject json = new JSONObject();
            json.put("id", dept.getId());
            json.put("name", dept.getName());
            Department queryObject = new Department();
            queryObject.setParentId(dept.getId());
            Iterable<Department> chilren = organizationService.findDepartmentList(queryObject);
            if (chilren.iterator().hasNext()) {
                json.put("hasChidren", true);
            } else {
                json.put("hasChidren", false);
            }
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 查询所有部门列表
     * 
     * @return 
     */
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Department> findAllDeparment() {
    	Iterable<Department> list = organizationService.findAllDepartment();
        return list;
    }

    @RequestMapping(value = "/findDeptName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findDeptName(String id, String deptName) {
        boolean bool = true;
        Iterable<Department> deptanyList = organizationService.findAllDepartment();
        for (Department department : deptanyList) {
            String name = department.getName();
            if (id == null) {
                if (deptName.equals(name)) {
                    bool = false;
                }
            } else {
                Department dept = organizationService.getDepartment(id);
                String name1 = dept.getName();
                if (!deptName.equals(name1)) {
                    if (deptName.equals(name)) {
                        bool = false;
                    }
                }
            }
        }
        return bool;
    }

}
