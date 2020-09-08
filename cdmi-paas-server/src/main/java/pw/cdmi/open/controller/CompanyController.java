package pw.cdmi.open.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.model.entities.Area;
import pw.cdmi.open.model.entities.Company;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.model.entities.EmployeeAndOffice;
import pw.cdmi.open.model.entities.Office;
import pw.cdmi.open.service.BusinessOrganizationService;
import pw.cdmi.open.service.EmployeeService;

/**
 * ********************************************************** 
 * 控制类，提供对分公司的业务操作方法
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-11
 ***********************************************************
 */
@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private BusinessOrganizationService organizationService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 
     * 在部署初始化时候，系统自动创建应用的拥有企业信息
     * 
     * @param company
     *            分公司信息封装的company对象
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public void createCompany(Company company) {
        // TODO 补齐企业的工商注册号或税务登记证好
        if (StringUtils.isEmpty(company.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        organizationService.createCompany(company);
    }

    /**
     * 
     * 根据传入的分公司信息，创建新分公司
     * 
     * @param company
     *            分公司信息封装的company对象
     * @return
     */
    @RequestMapping(value = "/subCompany", method = RequestMethod.POST)
    @ResponseBody
    public void createSubCompany(Company company) {
        if (StringUtils.isEmpty(company.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        String parentId = organizationService.getCurrentCompanyId();
        company.setParentId(parentId);
        organizationService.createCompany(company);
    }

    /**
     * 
     * 根据分公司修改分公司信息
     * 
     * @param company
     * @return
     */
    @RequestMapping(value = "/updateCompany")
    @ResponseBody
    public Map<String, Object> updateCampany(Company company) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (company == null || StringUtils.isEmpty(company.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        try {
            Company comp = organizationService.getCompany(company.getId());
            comp.setName(company.getName());
            comp.setBusinessRegistrationName(company.getBusinessRegistrationName());
            comp.setEnglishName(company.getEnglishName());
            comp.setLicenseNumber(company.getLicenseNumber());
            comp.setTelephone(company.getTelephone());
            comp.setEmail(company.getEmail());
            comp.setAreaId(company.getAreaId());
            comp.setSupervisorId(company.getSupervisorId());
            comp.setFoundDate(company.getFoundDate());
            comp.setDescription(company.getDescription());
            organizationService.updateCompany(comp);
            map.put("message", "成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "失败！");
        }
        return map;
    }

    /**
     * 
     * 根据id删除分公司
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteCompany/{ids}")
    @ResponseBody
    public Map<String, Object> deleteCompany(@PathVariable String ids) {
        boolean b = true;
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(ids)) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        String[] idStrings = ids.split(",");
        for (String id : idStrings) {
            try {
                b = organizationService.deleteCompany(id);
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
     * 根据id得到分公司详细信息
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/getCompany")
    @ResponseBody
    public JSONObject getCompany(@RequestParam("companyId") String companyId) {
        if (companyId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        Company company = organizationService.getCompany(companyId);
        JSONObject json = new JSONObject();
        json.put("id", company.getId());
        json.put("name", company.getName());
        json.put("businessRegistrationName", company.getBusinessRegistrationName());
        // json.put("englishName", company.getEnglishName());
        // json.put("licenseNumber", company.getLicenseNumber());
        // json.put("webSite", company.getWebSite());
        json.put("telephone", company.getTelephone());
        json.put("email", company.getEmail());
        json.put("foundDate", company.getFoundDate());
        if (company.getFoundDate() != null) {
            json.put("foundDateTime", company.getFoundDate().getTime());
        }
        json.put("openId", company.getOpenId());
        json.put("createTime", company.getCreateTime().getTime());
        json.put("description", company.getDescription());
        json.put("areaId", company.getAreaId());
        if (company.getAreaId() != null) {
            Area area = organizationService.getArea(company.getAreaId());
            if (area != null) {
                json.put("areaName", area.getName());
            }
        }
        json.put("supervisorId", company.getSupervisorId());
        if (company.getSupervisorId() != null) {
            Employee employee = employeeService.getSingleEmployeeById(company.getSupervisorId());
            if (employee == null) {
                json.put("supervisorName", "");
            } else {
                json.put("supervisorName", employee.getName());
            }
        }
        json.put("parentId", company.getParentId());
        if (company.getParentId() == null) {
            json.put("parentName", "");
        } else {
            Company parentCompany = organizationService.getCompany(company.getParentId());
            json.put("parentName", parentCompany.getName());
        }
        return json;
    }

    /**
     * 
     * 根据条件查询企业列表
     * 
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/findCompanyList")
    @ResponseBody
    public JSONObject findCompanyList(@ModelAttribute Company comp, Integer page, Integer rows) {
        JSONObject jsonObject = new JSONObject();
        Page<Company> pageView = organizationService.findCompanyList(comp, page, rows);
        @SuppressWarnings("unchecked")
        List<Company> list = pageView.getContent();
        JSONArray array = new JSONArray();
        for (Company company : list) {
            JSONObject json = new JSONObject();
            json.put("id", company.getId());
            json.put("name", company.getName());
            json.put("businessRegistrationName", company.getBusinessRegistrationName());
            json.put("telephone", company.getTelephone());
            json.put("email", company.getEmail());
            if (company.getAreaId() != null) {
                Area area = organizationService.getArea(company.getAreaId());
                if (area != null) {
                    json.put("areaName", area.getName());
                }
            }
            json.put("location", company.getLocation());
            array.add(json);
        }
        jsonObject.put("rows", array);
        jsonObject.put("total", pageView.getTotalElements());
        return jsonObject;
    }

    /**
     * 
     * 根据总公司id查询分公司列表
     * 
     * @param parentId
     *            总公司id
     * @return
     */
    @RequestMapping(value = "/findSubCompanyListByParentId")
    @ResponseBody
    public Iterable<Company> findCompanyListByParentId(String parentId) {
        Company comp = new Company();
        if (parentId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        comp.setParentId(parentId);
        return organizationService.findSubCompanyList(comp);
    }

    /**
     * 
     * 快速查询分公司列表
     * 
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/findSubCompanyList")
    @ResponseBody
    public JSONArray findSubCompanyList(String keyword) {
        Company comp = new Company();
        comp.setName(keyword);
        Iterable<Company> list = organizationService.findSubCompanyList(comp);
        JSONArray array = new JSONArray();
        for (Company company : list) {
            JSONObject json = new JSONObject();
            json.put("id", company.getId());
            json.put("name", company.getName());
            json.put("businessRegistrationName", company.getBusinessRegistrationName());
            json.put("telephone", company.getTelephone());
            json.put("email", company.getEmail());
            json.put("areaId", company.getAreaId());
            if (company.getAreaId() != null) {
                Area area = organizationService.getArea(company.getAreaId());
                if (area != null) {
                    json.put("areaName", area.getName());
                }
            }
            json.put("supervisorId", company.getSupervisorId());
            if (company.getSupervisorId() != null) {
                Employee employee = employeeService.getSingleEmployeeById(company.getSupervisorId());
                if (employee != null) {
                    json.put("supervisorName", employee.getName());
                } else {
                    json.put("supervisorName", "");
                }
            } else {
                json.put("supervisorName", "");
            }
            json.put("foundDate", company.getFoundDate());
            json.put("description", company.getDescription());
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 快速查询分公司列表
     * 
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/findSubCompanyAndOfficeList")
    @ResponseBody
    public JSONArray findSubCompanyAndOfficeList() {
        Company comp = new Company();
        Company currentCompany = organizationService.getCurrentCompany();
        Iterable<Company> companyList = organizationService.findSubCompanyList(comp);
        Iterable<Office> officeList = organizationService.findAllOffice();
        JSONArray result = new JSONArray();

        // 添加总公司信息
        JSONObject currentCompanyJson = new JSONObject();
        currentCompanyJson.put("id", "company" + currentCompany.getId());
        currentCompanyJson.put("name", currentCompany.getName());
        currentCompanyJson.put("showType", "company");
        result.add(currentCompanyJson);

        for (Company company : companyList) {
            JSONObject json = new JSONObject();
            json.put("id", "company" + company.getId());
            json.put("name", company.getName());
            json.put("showType", "company");
            result.add(json);
        }
        for (Office office : officeList) {
            JSONObject json = new JSONObject();
            json.put("id", "office" + office.getId());
            json.put("name", office.getName());
            json.put("showType", "office");
            result.add(json);
        }
        return result;
    }

    /**
     * 
     * 查询所有的分公司列表,用于下拉框
     * 
     * @return
     */
    @RequestMapping(value = "/findAllCompany")
    @ResponseBody
    public Iterable<Company> findAllCompany() {
        return organizationService.findSubCompanyList(new Company());
    }

    /**
     * 
     * 查询所有的总公司列表,用于下拉框
     * 
     * @return
     */
    @RequestMapping(value = "/findParentCompany")
    @ResponseBody
    public JSONArray findParentCompany() {
    	Iterable<Company> list = organizationService.findParentCompany();
        JSONArray array = new JSONArray();
        for (Company company : list) {
            JSONObject json = new JSONObject();
            json.put("id", company.getId());
            json.put("name", company.getName());
            array.add(json);
        }
        return array;
    }

    @RequestMapping(value = "/findCompanyName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findCompanyName(String companyId, String companyName) {
        // 在一个部署单元中，公司名称是唯一的
        boolean bool = true;
        Company company = organizationService.getCompanyByName(companyName);
        if (company != null) { // 系统中该名称存在
            if (company.getId().equals(companyId)) {// 名称对应其他公司,说明该名称已存在，不能使用该名称
                bool = false;
            }
        }
        return bool;
    }

    @RequestMapping(value = "/findRegistrationName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findRegistrationName(String companyId, String registrationName) {
        // 在一个部署单元中，公司名称是唯一的
        boolean bool = true;
        Company company = organizationService.getCompanyByRegistrationName(registrationName);
        if (company != null) {
            if (company.getId().equals(companyId)) {
                bool = false;
            }
        }
        return bool;
    }

    @RequestMapping(value = "/createOffice", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createOffice(Office office) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(office.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        try {
            String companyId = organizationService.getCurrentCompanyId();
            office.setCompanyId(companyId);
            office.setOpenId(UUID.randomUUID().toString().toUpperCase().replace("-", ""));
            office.setCreateTime(new Date());
            organizationService.createOffice(office);
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "error");
        }
        return map;
    }

    /**
     * 
     * 根据办事处改修办事处信息
     * 
     * @param office
     * @return
     */
    @RequestMapping(value = "/updateOffice")
    @ResponseBody
    public Map<String, Object> updateCampany(Office office) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (office == null || StringUtils.isEmpty(office.getName())) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        try {
            Office agency = organizationService.getOffice(office.getId());
            agency.setName(office.getName());
            agency.setTelephone(office.getTelephone());
            agency.setEmail(office.getEmail());
            agency.setAreaId(office.getAreaId());
            agency.setSupervisorId(office.getSupervisorId());
            agency.setFoundDate(office.getFoundDate());
            agency.setDescription(office.getDescription());
            organizationService.updateOffice(agency);
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "error");
        }
        return map;
    }

    /**
     * 
     * 根据id删除办事处
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteOffice/{ids}")
    @ResponseBody
    public Map<String, Object> deleteOffice(@PathVariable String ids) {
        boolean b = true;
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isEmpty(ids)) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        String[] idStrings = ids.split(",");
        for (String id : idStrings) {
            try {
                b = organizationService.deleteOffice(id);
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
     * 根据id得到办事处详细信息
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/getOffice")
    @ResponseBody
    public JSONObject getOffice(@RequestParam("officeId") String officeId) {
        if (officeId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        Office office = organizationService.getOffice(officeId);
        JSONObject json = new JSONObject();
        json.put("id", office.getId());
        json.put("name", office.getName());
        json.put("openId", office.getOpenId());
        json.put("telephone", office.getTelephone());
        json.put("email", office.getEmail());
        if (office.getFoundDate() != null) {
            json.put("foundDate", office.getFoundDate().getTime());
        }
        json.put("createTime", office.getCreateTime().getTime());
        json.put("description", office.getDescription());
        json.put("areaId", office.getAreaId());
        if (office.getAreaId() != null) {
            Area area = organizationService.getArea(office.getAreaId());
            json.put("areaName", area.getName());
        }
        json.put("supervisorId", office.getSupervisorId());
        if (office.getSupervisorId() != null) {
            Employee employee = employeeService.getSingleEmployeeById(office.getSupervisorId());
            if (employee != null) {
                json.put("supervisorName", employee.getName());
            } else {
                json.put("supervisorName", "");
            }
        } else {
            json.put("supervisorName", "");
        }
        json.put("ComapnyId", office.getCompanyId());
        if (office.getCompanyId() != null) {
            Company company = organizationService.getCompany(office.getCompanyId());
            json.put("companyName", company.getName());
        }
        return json;
    }

    /**
     * 
     * 快速查询办事处列表
     * 
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/findOffice")
    @ResponseBody
    public JSONArray findOffice(String keyword) {
    	Iterable<Office> list = organizationService.findOfficeByKeyword(keyword);
        JSONArray array = new JSONArray();
        for (Office office : list) {
            JSONObject json = new JSONObject();
            json.put("id", office.getId());
            json.put("name", office.getName());
            json.put("openId", office.getOpenId());
            json.put("telephone", office.getTelephone());
            json.put("email", office.getEmail());
            json.put("foundDate", office.getFoundDate());
            json.put("description", office.getDescription());
            json.put("areaId", office.getAreaId());
            if (office.getAreaId() != null) {
                Area area = organizationService.getArea(office.getAreaId());
                if (area != null) {
                    json.put("areaName", area.getName());
                } else {
                    json.put("areaName", "");
                }

            }
            json.put("supervisorId", office.getSupervisorId());
            if (office.getSupervisorId() == null) {
                json.put("supervisorName", "");
            } else {
                Employee employee = employeeService.getSingleEmployeeById(office.getSupervisorId());
                if (employee == null) {
                    json.put("supervisorName", "");
                } else {
                    json.put("supervisorName", employee.getName());
                }
            }
            json.put("companyId", office.getCompanyId());
            if (office.getCompanyId() != null) {
                Company company = organizationService.getCompany(office.getCompanyId());
                if (company != null) {
                    json.put("companyName", company.getName());
                } else {
                    json.put("companyName", "");
                }

            }
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 查询所有的办事处列表,用于下拉框
     * 
     * @return
     */
    @RequestMapping(value = "/findAllOffice", method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Office> findAllOffice() {
    	Iterable<Office> list = organizationService.findAllOffice();
        return list;
    }

    @RequestMapping(value = "/findOfficeName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findOfficeName(String officeId, String officeName) {
        // 在一个部署单元中，公司名称是唯一的
        String companyId = organizationService.getCurrentCompanyId();
        boolean bool = true;
        Office office = organizationService.getOfficeByName(companyId, officeName);
        if (office != null) {
            if (office.getId() != officeId) {
                bool = false;
            }
        }
        return bool;
    }

    @RequestMapping(value = "/createEmpAndOffice", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createEmpAndOffice(String employeeId, String officeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        EmployeeAndOffice empAndOffice = new EmployeeAndOffice();
        empAndOffice.setEmployeeId(employeeId);
        empAndOffice.setOfficeId(officeId);
        try {
            organizationService.createEmployeeAndOffice(empAndOffice);
            map.put("message", "成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "失败！");
        }
        return map;
    }

    @RequestMapping(value = "/deleteEmpAndOffice", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteEmpAndOffice(String employeeId, String officeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            organizationService.deleteEmployeeAndOffice(employeeId, officeId);
            map.put("message", "成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "失败！");
        }
        return map;
    }

    @RequestMapping(value = "/updataEmpAndOffice", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> updataEmpAndOffice(String employeeId, String officeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        EmployeeAndOffice empAndOffice = new EmployeeAndOffice();
        empAndOffice.setEmployeeId(employeeId);
        empAndOffice.setOfficeId(officeId);
        try {
            organizationService.updateEmployeeAndOffice(empAndOffice);
            map.put("message", "成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "失败！");
        }
        return map;
    }

    @RequestMapping(value = "/findEmpAndOffice", method = RequestMethod.POST)
    @ResponseBody
    public boolean findEmpAndOffice(String employeeId) {
        boolean bool = true;
        EmployeeAndOffice empAndOffice = organizationService.getEmployeeAndOffice(employeeId);
        if (empAndOffice != null) {
            bool = false;
        }
        return bool;
    }
}
