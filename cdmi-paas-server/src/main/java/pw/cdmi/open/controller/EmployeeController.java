package pw.cdmi.open.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.collection.PageView;
import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.model.CertificatesInfo;
import pw.cdmi.open.model.WorkStatus;
import pw.cdmi.open.model.entities.Company;
import pw.cdmi.open.model.entities.Department;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.model.entities.Office;
import pw.cdmi.open.model.entities.People;
import pw.cdmi.open.model.entities.Position;
import pw.cdmi.open.model.entities.PositionalTitle;
import pw.cdmi.open.model.queryObject.EmployeeQuery;
import pw.cdmi.open.service.BusinessOrganizationService;
import pw.cdmi.open.service.EmployeeService;
import pw.cdmi.open.service.PeopleService;
import pw.cdmi.paas.account.model.UserStatus;
import pw.cdmi.paas.account.model.entities.UserAccount;
import pw.cdmi.paas.account.service.UserService;
import pw.cdmi.paas.app.model.entities.SiteUser;

/************************************************************
 * 控制器，处理员工管理请求的操作方法
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-12
 ************************************************************/
@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private BusinessOrganizationService organizationService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private PeopleService peopleService;


    /**
     * 绑定员工对象的参数
     * 
     * @param binder
     */
    @InitBinder("employee")
    public void initBinderFirst(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("employee.");
    }

    /**
     * 绑定公民信息对象的参数
     * 
     * @param binder
     */
    @InitBinder("people")
    public void initBinderSecond(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("people.");
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createEmployee(@ModelAttribute Employee employee, @ModelAttribute People people) {
        Map<String, Object> map = new HashMap<String, Object>();
        Integer certificate = people.getCertificate();
        try {

            if (StringUtils.isNotBlank(people.getIdCard())) {
                String code = people.getIdCard();
                if (CertificatesInfo.SocialSecurityCode.getValue() == certificate) {
                    people.setSocialSecurityCode(code);
                } else if (CertificatesInfo.DriverLicenseNumber.getValue() == certificate) {
                    people.setDriverLicenseNumber(code);
                } else if (CertificatesInfo.PassportNumber.getValue() == certificate) {
                    people.setPassportNumber(code);
                }
                if (CertificatesInfo.IdCard.getValue() != certificate) {
                    people.setIdCard(null);
                }
                people.setTrueName(employee.getName());
                employee.setSex(people.getSex());
                employee.setBirthday(people.getBirthday());
                employee.setNation(people.getNation());
            }
            employee.setStatus(WorkStatus.OK);
            employeeService.createEmployee(employee, people);
            map.put("message", "success");
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> updateEmployee(Employee employee) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            employeeService.adjustmentEmployee(employee);
            map.put("message", "success");
        } catch (Exception e) {
            log.error(e.getMessage().toString());
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editEmployee(@ModelAttribute Employee employee, @ModelAttribute People people) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            employeeService.updateEmployee(employee, people);
            map.put("message", "success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * @author jiangdie
     * @date 2015-12-21
     * @decription : 修改个人信息
     * @param employee
     * @param people
     * @return
     */
    @RequestMapping(value = "/edituserinfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> editUserInfo(Employee employee, People people) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            employeeService.updateUserInfo(employee, people);
            map.put("message", "成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
            map.put("message", e.getMessage());
        }
        return map;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteEmployee(String employeeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            employeeService.deleteEmployeeById(employeeId);
            map.put("message", "success");
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
            map.put("message", "error");
        }
        return map;
    }

    @RequestMapping(value = "/deleteAndCancelIt", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteEmployeeAndCancelIt(String employeeId) {
    	String appId = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Employee emp = employeeService.getSingleEmployeeById(employeeId);
            if (null != emp) {
                if (null != emp.getAccountId()) {
                    UserAccount userAccount = userService.getUserAccountByIdAndStatus(emp.getAccountId());
                    SiteUser siteUser = userService.getSiteUserByAccountId(emp.getAccountId(), appId);
                    if (null != userAccount) {
                        userAccount.setStatus(UserStatus.fromValue(7));
                        userService.updateUserAccount(userAccount);
                    }
                    if (null != siteUser) {
                        siteUser.setStatus(UserStatus.fromValue(7));
                        userService.updateSiteUser(siteUser);
                    }
                }
                employeeService.deleteEmployeeById(employeeId);
                map.put("message", "success");
            }
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
            map.put("message", "error");
        }
        return map;
    }

    /*@RequestMapping(value = "/single/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmployee(@PathVariable("id")
    int id) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	try {
    		map = employeeService.getEmployeeById(id);
    	} catch (Exception e) {
    		log.error(e.getStackTrace().toString());
    		e.printStackTrace();
    		map.put("message", "获取员工信息异常！");
    	}
    	return map;
    }*/
    @RequestMapping(value = "/single/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getEmployee(@PathVariable("id") String id) {
        JSONObject json = new JSONObject();
        Employee employee = employeeService.getSingleEmployeeById(id);
        json.put("id", employee.getId());
        json.put("name", employee.getName());
        json.put("code", employee.getCode());
        json.put("education", employee.getEducation());
        if (employee.getEducation() != null) {
            json.put("educationName", employee.getEducation().getText());
        }
        json.put("email", employee.getEmail());
        json.put("telephone", employee.getTelephone());
        json.put("deptManagerId", employee.getDeptManagerId());
        if (employee.getDeptManagerId() != null) {
            json.put("deptManagerName", "部门主管");
        } else {
            json.put("deptManagerName", "普通员工");
        }
        json.put("employeeProperty", employee.getEmployeeProperty());
        if (employee.getEmployeeProperty() != null) {
            json.put("employeePropertyName", employee.getEmployeeProperty().getText());
        }
        json.put("emergencyContactName", employee.getEmergencyContactName());
        json.put("emergencyContactPhone", employee.getEmergencyContactPhone());
        json.put("emergencyContactRelation", employee.getEmergencyContactRelation());
        json.put("peopleId", employee.getPeopleId());
        if (employee.getPeopleId() != null) {
            People people = peopleService.getPeopleById(employee.getPeopleId());
            if (StringUtils.isNotBlank(people.getIdCard())) {
                json.put("idCard", people.getIdCard());
                json.put("certificate", CertificatesInfo.IdCard.getValue());
            } else if (StringUtils.isNotBlank(people.getSocialSecurityCode())) {
                json.put("idCard", people.getSocialSecurityCode());
                json.put("certificate", CertificatesInfo.SocialSecurityCode.getValue());
            } else if (StringUtils.isNotBlank(people.getDriverLicenseNumber())) {
                json.put("idCard", people.getDriverLicenseNumber());
                json.put("certificate", CertificatesInfo.DriverLicenseNumber.getValue());
            } else if (StringUtils.isNotBlank(people.getPassportNumber())) {
                json.put("idCard", people.getPassportNumber());
                json.put("certificate", CertificatesInfo.PassportNumber.getValue());
            }
            json.put("sex", people.getSex());
            if (people.getSex() != null) {
                json.put("sexName", people.getSex().getText());
            }
            json.put("sexName", people.getSex().getText());
            if (people.getBirthday() != null) {
                json.put("birthday", people.getBirthday());
            }
            json.put("nation", people.getNation());
            if (people.getNation() != null) {
                json.put("nationName", people.getNation().getText());
            }
        }

        String globleCompanyId = organizationService.getCurrentCompanyId();

        json.put("companyId", employee.getCompanyId());
        json.put("officeId", employee.getOfficeId());
        if (globleCompanyId != employee.getCompanyId() || null == employee.getOfficeId()) {
            json.put("companyAndOfficeId", "company" + employee.getCompanyId());
            json.put("showCompanyOrOffice", "company");
        } else {
            json.put("companyAndOfficeId", "office" + employee.getOfficeId());
            json.put("showCompanyOrOffice", "office");
        }

        if (employee.getCompanyId() != null) {
            Company comp = organizationService.getCompany(employee.getCompanyId());
            if (comp != null) {
                json.put("companyName", comp.getName());
            }
        }
        if (employee.getOfficeId() != null) {
            Office office = organizationService.getOffice(employee.getOfficeId());
            if (office != null) {
                json.put("officeName", office.getName());
            }
        }
        json.put("deptId", employee.getDeptId());
        if (employee.getDeptId() != null) {
            Department dept = organizationService.getDepartment(employee.getDeptId());
            if (dept != null) {
                json.put("deptName", dept.getName());
            }
        }
        json.put("positionId", employee.getPositionId());
        if (employee.getPositionId() != null) {
            Position position = organizationService.getPosition(employee.getPositionId());
            if (position != null) {
                json.put("positionName", position.getName());
            }
        }
        json.put("positionalTitleId", employee.getPositionalTitleId());
        if (employee.getPositionalTitleId() != null) {
            PositionalTitle positionalTitle = organizationService.getPositionalTitle(employee.getPositionalTitleId());
            if (positionalTitle != null) {
                json.put("positionalTitleName", positionalTitle.getName());
            }
        }
        if (employee.getJoinTime() != null) {
            json.put("joinTime", employee.getJoinTime());
        }
        return json;
    }

    @RequestMapping(value = "/selectList", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray getAllEmployee() {
        JSONArray jsonArray = new JSONArray();
        try {
            List<Employee> list = employeeService.getAllEmployee();
            if (!list.isEmpty()) {
                for (Employee employee : list) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", employee.getId());
                    jsonObject.put("name", employee.getName());
                    jsonArray.add(jsonObject);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return jsonArray;
    }

    @RequestMapping(value = "/selectSiteUserIdAndEmployeeNameList", method = RequestMethod.GET)
    @ResponseBody
    public List<Object> selectSiteUserIdAndEmployeeNameList() {
        return employeeService.selectSiteUserIdAndEmployeeNameList();
    }

    @RequestMapping(value = "/findListByPage", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject findEmployeeListByPage(int page, int pageSize, String code, String name, String telephone,
        String deptName) {
        PageView pageView = new PageView();
        pageView = employeeService.findEmployeeByConditionAndPage(page, pageSize, code, name, telephone, deptName);
        JSONArray array = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        @SuppressWarnings("unchecked")
        List<Employee> list = pageView.getList();
        for (Employee employee : list) {
            JSONObject json = new JSONObject();
            json.put("id", employee.getId());
            json.put("name", employee.getName());
            json.put("code", employee.getCode());
            json.put("email", employee.getEmail());
            json.put("telephone", employee.getTelephone());
            json.put("companyId", employee.getCompanyId());
            if (employee.getCompanyId() != null) {
                Company comp = organizationService.getCompany(employee.getCompanyId());
                if (comp != null) {
                    json.put("companyName", comp.getName());
                }
            }
            json.put("deptId", employee.getDeptId());
            if (employee.getDeptId() != null) {
                Department dept = organizationService.getDepartment(employee.getDeptId());
                if (dept != null) {
                    json.put("deptName", dept.getName());
                }
            }
            json.put("positionId", employee.getPositionId());
            if (employee.getPositionId() != null) {
                Position position = organizationService.getPosition(employee.getPositionId());
                if (position != null) {
                    json.put("positionName", position.getName());
                }
            }
            json.put("positionalTitleId", employee.getPositionalTitleId());
            if (employee.getPositionalTitleId() != null) {
                PositionalTitle positionalTitle = organizationService
                    .getPositionalTitle(employee.getPositionalTitleId());
                if (positionalTitle != null) {
                    json.put("positionalTitleName", positionalTitle.getName());
                }
            }
            array.add(json);
        }
        jsonObject.put("data", array);
        jsonObject.put("total", pageView.getTotalRecord());
        return jsonObject;
    }

    @RequestMapping(value = "/findList", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> findEmployeeList(String code, String name, String telephone, int deptId) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            list = employeeService.findEmployeeByCondition(code, name, telephone, deptId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "/findByDeptGroupId", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray findEmployeeByDeptGroupId(String deptGroupId) {
        List<Employee> list = employeeService.findEmployeeByDeptGroupId(deptGroupId);
        JSONArray array = new JSONArray();
        for (Employee employee : list) {
            JSONObject json = new JSONObject();
            json.put("id", employee.getId());
            json.put("name", employee.getName());
            json.put("code", employee.getCode());
            json.put("deptId", employee.getDeptId());
            if (employee.getDeptId() != null) {
                Department dept = organizationService.getDepartment(employee.getDeptId());
                if (dept != null) {
                    json.put("deptName", dept.getName());
                }
            }
            array.add(json);
        }
        return array;
    }

    @RequestMapping(value = "/getCountByDeptGroupId", method = RequestMethod.GET)
    @ResponseBody
    public long getCountEmployeeByDeptGroupId(String deptGroupId) {
        long count = 0;
        try {
            if (deptGroupId == null) {
                throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
            }
            count = employeeService.getCountEmployeeByDeptGroupId(deptGroupId);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
        }
        return count;
    }

    @RequestMapping(value = "/findByCompanyId", method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> findByCompanyId(String companyId) {
        List<Employee> list = new ArrayList<Employee>();
        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setCompanyId(companyId);
        try {
            list = employeeService.findEmployeeByQuery(employeeQuery);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "/getCountByCompanyId", method = RequestMethod.GET)
    @ResponseBody
    public long getCountByCompanyId(String companyId) {
        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setCompanyId(companyId);
        long count = 0;
        try {
            if (companyId == null) {
                throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
            }
            count = employeeService.getCountEmployeeByQuery(employeeQuery);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
        }
        return count;
    }

    @RequestMapping(value = "/findByOfficeId", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray findByOfficeId(String officeId) {
        List<Employee> list = employeeService.findEmployeeByOfficeId(officeId);
        JSONArray array = new JSONArray();
        for (Employee employee : list) {
            JSONObject json = new JSONObject();
            json.put("id", employee.getId());
            json.put("name", employee.getName());
            json.put("code", employee.getCode());
            json.put("deptId", employee.getDeptId());
            if (employee.getDeptId() != null) {
                Department dept = organizationService.getDepartment(employee.getDeptId());
                if (dept != null) {
                    json.put("deptName", dept.getName());
                }
            }
            array.add(json);
        }
        return array;
    }

    @RequestMapping(value = "/getCountByOfficeId", method = RequestMethod.GET)
    @ResponseBody
    public long getCountByOfficeId(String officeId) {
        long count = 0;
        try {
            if (officeId == null) {
                throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
            }
            count = employeeService.getCountEmployeeByOfficeId(officeId);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
        }
        return count;
    }

    @RequestMapping(value = "/findByDeptId", method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> findByDeptId(String deptId) {

        List<Employee> result = new ArrayList<Employee>();
        List<Employee> data = new ArrayList<Employee>();

        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setDeptId(deptId);

        try {
            data = employeeService.findEmployeeByQuery(employeeQuery);
            if (null != data) {
                for (Employee emp : data) {
                    WorkStatus ws = emp.getStatus();
                    int workstatus = ws.getValue();
                    if (workstatus == 0 || workstatus == 1 || workstatus == 2) {
                        result.add(emp);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return result;
    }

    // @RequestMapping(value = "/getCountByDeptId", method = RequestMethod.GET)
    // @ResponseBody
    // public int getCountByDeptId(int deptId) {
    // EmployeeQuery employeeQuery = new EmployeeQuery();
    // employeeQuery.setDeptId(deptId);
    // int count = 0;
    // try {
    // if (deptId == 0) {
    // throw new AWSClientException(SystemError.InvalidRequest, "请求失败！");
    // }
    // count = employeeService.getCountEmployeeByQuery(employeeQuery);
    // } catch (Exception e) {
    // log.error(e.getStackTrace().toString());
    // e.printStackTrace();
    // }
    // return count;
    // }
    @RequestMapping(value = "/getCountByDeptId", method = RequestMethod.GET)
    @ResponseBody
    public int getCountByDeptId(String deptId) {
        List<Employee> emp = findByDeptId(deptId);
        return emp.size();
    }

    @RequestMapping(value = "/findByCommissionerId", method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> findByCommissionerId(String commissionerId) {
        List<Employee> list = new ArrayList<Employee>();
        if (commissionerId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        try {
            list = employeeService.findEmployeeByCommissionerId(commissionerId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "/getCountByCommissionerId", method = RequestMethod.GET)
    @ResponseBody
    public long getCountByCommissionerId(String commissionerId) {
        long count = 0;
        try {
            if (commissionerId == null) {
                throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
            }
            count = employeeService.getCountEmployeeByCommissionerId(commissionerId);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
        }
        return count;
    }

    @RequestMapping(value = "/findByPositionId", method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> findByPositionId(String positionId) {
        List<Employee> list = new ArrayList<Employee>();
        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setPositionId(positionId);
        try {
            list = employeeService.findEmployeeByQuery(employeeQuery);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "/getCountByPositionId", method = RequestMethod.GET)
    @ResponseBody
    public long getCountByPositionId(String positionId) {
        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setPositionId(positionId);
        long count = 0;
        try {
            if (positionId == null) {
                throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
            }
            count = employeeService.getCountEmployeeByQuery(employeeQuery);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
        }
        return count;
    }

    @RequestMapping(value = "/findByPositionalTitleId", method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> findByPositionalTitleId(String positionalTitleId) {
        List<Employee> list = new ArrayList<Employee>();
        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setPositionalTitleId(positionalTitleId);
        try {
            list = employeeService.findEmployeeByQuery(employeeQuery);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "/getCountByPositionalTitleId", method = RequestMethod.GET)
    @ResponseBody
    public long getCountByPositionalTitleId(String positionalTitleId) {
        EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.setPositionalTitleId(positionalTitleId);
        long count = 0;
        try {
            if (positionalTitleId == null) {
                throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
            }
            count = employeeService.getCountEmployeeByQuery(employeeQuery);
        } catch (Exception e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
        }
        return count;
    }

    @RequestMapping(value = "/findByAread", method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> findByAread(String areaId) {
        List<Employee> list = new ArrayList<Employee>();
        Iterable<Company> companyList = new ArrayList<Company>();
        Company company = new Company();
        try {
            company.setAreaId(areaId);
            companyList = organizationService.findSubCompanyList(company);
            for (Company comp : companyList) {
                String companyId = comp.getId();
                EmployeeQuery employeeQuery = new EmployeeQuery();
                employeeQuery.setCompanyId(companyId);
                list = employeeService.findEmployeeByQuery(employeeQuery);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping(value = "/getCountByAreaId", method = RequestMethod.GET)
    @ResponseBody
    public long getCountByAreaId(String areaId) {
        long count = 0;
        Iterable<Company> companyList = new ArrayList<Company>();
        Company company = new Company();
        try {
            company.setAreaId(areaId);
            companyList = organizationService.findSubCompanyList(company);
            for (Company comp : companyList) {
                String companyId = comp.getId();
                EmployeeQuery employeeQuery = new EmployeeQuery();
                employeeQuery.setCompanyId(companyId);
                count = employeeService.getCountEmployeeByQuery(employeeQuery);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return count;
    }

    @RequestMapping(value = "/findEmployeeCode", method = RequestMethod.GET)
    @ResponseBody
    public boolean findEmployeeCode(@RequestParam(value = "employeId", defaultValue = "0") String employeId,
        String employeeCode) {
        boolean bool = true;
        // long companyId = organizationService.getCurrentCompanyId();
        List<String> companyIds = organizationService.getCurrentAndSubCompanyIds();
        // 在一个部署单元中，工号是唯一的
        Employee employee = employeeService.getEmployeeByJobNumber(companyIds, employeeCode);
        if (employee != null) { // 系统中该工号存在
            if (employee.getId() != employeId) {// 工号对应其他员工,说明该工号已存在，不能使用该工号
                bool = false;
            }
        }
        return bool;
    }

    @RequestMapping(value = "/findEmployeeEmail", method = RequestMethod.GET)
    @ResponseBody
    public boolean findEmployeeEmail(@RequestParam(value = "employeId", defaultValue = "0") String employeId,
        String employeeEmail) {
        boolean bool = true;
        // long companyId = organizationService.getCurrentCompanyId();
        List<String> companyIds = organizationService.getCurrentAndSubCompanyIds();
        // 在一个部署单元中，邮箱号是唯一的
        Employee employee = employeeService.getEmployeeByEmail(companyIds, employeeEmail);
        if (employee != null) { // 系统中该邮箱号存在
            if (employee.getId() != employeId) {// 邮箱号对应其他员工,说明该邮箱号已存在，不能使用该邮箱号
                bool = false;
            }
        }
        return bool;
    }

    @RequestMapping(value = "/findEmployeeIdCard", method = RequestMethod.GET)
    @ResponseBody
    public boolean findEmployeeIdCard(@RequestParam(value = "employeId", defaultValue = "0") String employeId,
        String certificateNumber, String certificateType) {
        People people = null;
        String certificateType1 = CertificatesInfo.IdCard.getValue() + "";
        String certificateType2 = CertificatesInfo.DriverLicenseNumber.getValue() + "";
        String certificateType3 = CertificatesInfo.SocialSecurityCode.getValue() + "";
        String certificateType4 = CertificatesInfo.PassportNumber.getValue() + "";
        if (certificateType1.equals(certificateType) || certificateType2.equals(certificateType)
                || certificateType3.equals(certificateType)) {
            people = peopleService.getPeopleByIdCode(certificateNumber);
            if (people == null) {
                people = peopleService.getPeopleByDriverLicenseNumber(certificateNumber);
                if (people == null) {
                    people = peopleService.getPeopleBySocialCode(certificateNumber);
                }
            }
        } else if (certificateType4.equals(certificateType)) {
            people = peopleService.getPeopleByPassportNumber(certificateNumber);
        }
        if (people != null) {// 该证件号已存在
            if (employeId != null) {
                Employee emp = employeeService.getSingleEmployeeById(employeId);
                if ((emp != null) && (emp.getPeopleId() != people.getId())) {// 员工对应的People信息与根据身份证查找到的People不是同一个人，说明该身份证号已被其他用户占用
                    return false;
                }
                return true;
            }
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/createEmployeeAndAccount", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> createEmployeeAndAccount(String name, String code, String email, String account,
        String pwd) {

        Map<String, String> map = new HashMap<String, String>();

        try {
            Employee emp = new Employee();
            emp.setCode(code);
            emp.setName(name);
            emp.setEmail(email);
            String companyId = organizationService.getCurrentCompanyId();
            emp.setCompanyId(companyId);
            emp.setStatus(WorkStatus.OK);
            employeeService.createEmployeeAndAccount(emp, account, pwd);
            map.put("message", "success");
        } catch (Exception e) {
            map.put("message", "failure");
        }
        return map;
    }
}
