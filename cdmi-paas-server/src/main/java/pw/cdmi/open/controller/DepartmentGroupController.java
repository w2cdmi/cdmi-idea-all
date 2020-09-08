package pw.cdmi.open.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import pw.cdmi.open.model.entities.Department;
import pw.cdmi.open.model.entities.DepartmentGroup;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.model.entities.EmployeeAndDeptGroup;
import pw.cdmi.open.security.SecurityUser;
import pw.cdmi.open.service.BusinessOrganizationService;
import pw.cdmi.open.service.EmployeeService;

/**
 * **********************************************************
 * 控制类，提供对群组的业务操作方法
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-11
 ***********************************************************
 */
@Controller
@RequestMapping("/departmentGroup")
public class DepartmentGroupController {
    @Autowired
    private BusinessOrganizationService organizationService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 
     * 根据传入群组的信息，创建新群组
     * 
     * @param departmentGroup 群组信息封装的departmentGroup对象
     * @return
     */
    @RequestMapping(value = "/createDeptGroup")
    @ResponseBody
    public Map<String, Object> createDeptGroup(DepartmentGroup departmentGroup, HttpSession session) {
        Map<String, Object> map = new HashMap<String, Object>();
        EmployeeAndDeptGroup employeeAndDeptGroup = new EmployeeAndDeptGroup();
        if (StringUtils.isBlank(departmentGroup.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        try {
            String companyId = organizationService.getCurrentCompanyId();
            departmentGroup.setCompanyId(companyId);
            Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            SecurityUser userDetails = (SecurityUser) user;
            Employee employee = organizationService.getEmployeeByAccountId(userDetails.getAccountId());
            departmentGroup.setSupervisorId(employee.getId());
            organizationService.createDepartmentGroup(departmentGroup);
            employeeAndDeptGroup.setDeptGroupId(departmentGroup.getId());
            employeeAndDeptGroup.setEmployeeId(employee.getId());
            organizationService.createEmployeeAndDeptGroup(employeeAndDeptGroup);
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 
     * 根据群组id删除群组信息
     * 
     * @param id 群组id
     * @return
     */
    @RequestMapping(value = "/deleteDeptGroup/{ids}")
    @ResponseBody
    public Map<String, Object> deleteDeptGroup(@PathVariable String ids) {
        boolean b = true;
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(ids)) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        String[] idStrings = ids.split(",");
        for (String id : idStrings) {
            try {
                b = organizationService.deleteDepartmentGroup(id);
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
     * 根据职称修改职称信息
     * 
     * @param departmentGroup 职称对象
     * @return
     */
    @RequestMapping(value = "/updateDeptGroup")
    @ResponseBody
    public Map<String, Object> updateDeptGroup(DepartmentGroup departmentGroup) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            DepartmentGroup deptGroup = organizationService.getDepartmentGroup(departmentGroup.getId());
            deptGroup.setName(departmentGroup.getName());
            deptGroup.setDescription(departmentGroup.getDescription());
            deptGroup.setDeptId(departmentGroup.getDeptId());
            // deptGroup.setSupervisorId(departmentGroup.getSupervisorId());
            organizationService.updateDepartmentGroup(deptGroup);
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "error");
        }
        return map;
    }

    /**
     * 
     * 更改群组管理员
     * 
     * @param deptGroupId
     * @param employeeId
     * @return
     */
    @RequestMapping(value = "/asAdministrator")
    @ResponseBody
    public Map<String, Object> asAdministrator(String deptGroupId, String employeeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            DepartmentGroup deptGroup = organizationService.getDepartmentGroup(deptGroupId);
            deptGroup.setSupervisorId(employeeId);
            organizationService.updateDepartmentGroup(deptGroup);
            map.put("message", "设置成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "设置失败！");
        }
        return map;
    }

    /**
     * 
     * 根据群组id得到群组详细信息
     * 
     * @param id 群组id
     * @return
     */
    @RequestMapping(value = "/getDeptGroup")
    @ResponseBody
    public JSONObject getDeptGroup(String departmentGroupId, HttpSession session) {
        DepartmentGroup deptGroup = null;
        if (departmentGroupId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee employee = organizationService.getEmployeeByAccountId(userDetails.getAccountId());
        deptGroup = organizationService.getDepartmentGroup(departmentGroupId);
        JSONObject json = new JSONObject();
        json.put("userId", employee.getId());
        json.put("id", deptGroup.getId());
        json.put("name", deptGroup.getName());
        json.put("description", deptGroup.getDescription());
        json.put("supervisorId", deptGroup.getSupervisorId());
        if (deptGroup.getSupervisorId() != null) {
            employee = employeeService.getSingleEmployeeById(deptGroup.getSupervisorId());
            if (employee != null) {
                json.put("supervisorName", employee.getName());
            } else {
                json.put("supervisorName", "");
            }
        } else {
            json.put("supervisorName", "");
        }
        if (deptGroup.getDeptId() != null) {
            Department dept = organizationService.getDepartment(deptGroup.getDeptId());
            if (dept != null) {
                json.put("deptName", dept.getName());
            }
        }
        return json;
    }

    /**
     * 
     * 根据条件查询群组列表
     * 
     * @param departmentGroup 查询条件封装成的departmentGroup对象
     * @return
     */
    @RequestMapping(value = "/findDeptGroupByKeyword")
    @ResponseBody
    public JSONArray findDeptGroupByKeyword(String keyword) {
    	Iterable<DepartmentGroup> list = organizationService.findDepartmentGroupByKeyword(keyword);
        JSONArray array = new JSONArray();
        for (DepartmentGroup deptGroup : list) {
            JSONObject json = new JSONObject();
            json.put("id", deptGroup.getId());
            json.put("name", deptGroup.getName());
            json.put("description", deptGroup.getDescription());
            json.put("supervisorId", deptGroup.getSupervisorId());
            if (deptGroup.getSupervisorId() != null) {
                Employee employee = employeeService.getSingleEmployeeById(deptGroup.getSupervisorId());
                if (employee != null) {
                    json.put("supervisorName", employee.getName());
                } else {
                    json.put("supervisorName", "");
                }
            } else {
                json.put("supervisorName", "");
            }
            json.put("deptId", deptGroup.getDeptId());
            if (deptGroup.getDeptId() != null) {
                Department dept = organizationService.getDepartment(deptGroup.getDeptId());
                if (dept != null) {
                    json.put("deptName", dept.getName());
                }
            }
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 根据员工id查询群组列表
     * 
     * @param employeeId
     * @return
     */
    @RequestMapping(value = "/findDeptGroupByEmployeeId")
    @ResponseBody
    public JSONArray findDeptGroupByEmployeeId(HttpSession session) {
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee employee = organizationService.getEmployeeByAccountId(userDetails.getAccountId());
        String employeeId = employee.getId();
        Iterable<DepartmentGroup> list = organizationService.findDeptGroupByEmployeeId(employeeId);
        JSONArray array = new JSONArray();
        for (DepartmentGroup deptGroup : list) {
            JSONObject json = new JSONObject();
            json.put("id", deptGroup.getId());
            json.put("name", deptGroup.getName());
            json.put("employeeId", employeeId);
            if (deptGroup.getSupervisorId() != null) {
                Employee emp = employeeService.getSingleEmployeeById(deptGroup.getSupervisorId());
                if (emp != null) {
                    json.put("supervisorName", emp.getName());
                    json.put("supervisorId", emp.getId());
                } else {
                    json.put("supervisorName", "");
                }
            }
            json.put("description", deptGroup.getDescription());
            json.put("deptId", deptGroup.getDeptId());
            if (deptGroup.getDeptId() != null) {
                Department dept = organizationService.getDepartment(deptGroup.getDeptId());
                json.put("deptName", dept.getName());
            }
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 查询所有的群组列表
     * 
     * @return
     */
    @RequestMapping(value = "/findAllDeptGroup")
    @ResponseBody
    public Iterable<DepartmentGroup> findAllDeptGroup() {
        return organizationService.findAllDepartmentGroup();
    }

    /**
     * 
     * 创建群组与员工的关联
     * 
     * @param employeeAndDeptGroup
     * @return
     */
    @RequestMapping(value = "/createEmployeeAndDeptGroup")
    @ResponseBody
    public Map<String, Object> createEmployeeAndDeptGroup(String employeeId, String deptGroupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        EmployeeAndDeptGroup empAndDeptGroup;
        try {
            String employeeName = employeeService.getSingleEmployeeById(employeeId).getName();
            empAndDeptGroup = organizationService.getEmployeeAndDeptGroup(employeeId, deptGroupId);
            if (empAndDeptGroup != null) {
                map.put("message", "添加失败，" + employeeName + "已在本群！");
            } else {
                EmployeeAndDeptGroup empAndGroup = new EmployeeAndDeptGroup();
                empAndGroup.setDeptGroupId(deptGroupId);
                empAndGroup.setEmployeeId(employeeId);
                organizationService.createEmployeeAndDeptGroup(empAndGroup);
                map.put("message", "添加成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "添加失败！");
        }
        return map;
    }

    /**
     * 
     * 根据员工id和群组id删除员工与群组的关联
     * 
     * @param employeeId
     * @param deptGroupId
     * @return
     */
    @RequestMapping(value = "/deleteEmployeeAndDeptGroup")
    @ResponseBody
    public Map<String, Object> deleteEmployeeAndDeptGroup(String employeeId, String deptGroupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (employeeId == null || deptGroupId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        int num = organizationService.deleteEmployeeAndDeptGroup(employeeId, deptGroupId);
        switch (num) {
        case 1:
            map.put("message", "删除成功！");
            break;

        case 2:
            map.put("message", "已解散该群！");
            break;

        default:
            map.put("message", "失败，没有找到数据！");
            break;
        }
        return map;
    }

    @RequestMapping(value = "/findDeptGroupName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findDeptGroupName(String deptId, String id, String groupName) {
        if (null == deptId) {
            return true;
        }
        Iterable<DepartmentGroup> groupList = organizationService.findDepartmentGroupByDeptId(deptId);
        for (DepartmentGroup deptGroup : groupList) {
            String name = deptGroup.getName();
            if (null == id) {
                if (groupName.equals(name)) {
                    return false;
                }
            } else {
                DepartmentGroup group = organizationService.getDepartmentGroup(id);
                String name1 = group.getName();
                if (!groupName.equals(name1)) {
                    if (groupName.equals(name)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
