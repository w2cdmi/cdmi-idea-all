package pw.cdmi.open.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSONArray;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.model.entities.Commissioner;
import pw.cdmi.open.model.entities.EmployeeAndCommissioner;
import pw.cdmi.open.service.BusinessOrganizationService;
import pw.cdmi.open.service.EmployeeService;

/**
 * **********************************************************
 * 控制类，提供对专员的业务操作方法
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-11
 ***********************************************************
 */
@Controller
@SessionAttributes("commissioner")
@RequestMapping("/commissioner")
public class CommissionerController {
    @Autowired
    private BusinessOrganizationService organizationService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 
     * 根据传入专员的信息，创建新专员
     * 
     * @param commissioner 片区信息封装的commissioner对象
     * @return
     */
    @RequestMapping(value = "/createCommissioner")
    @ResponseBody
    public Commissioner createCommissioner(Commissioner commissioner) {
        if (commissioner == null || StringUtils.isBlank(commissioner.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        String companyId = organizationService.getCurrentCompanyId();
        commissioner.setCompanyId(companyId);
        organizationService.createCommissioner(commissioner);
        return commissioner;
    }

    /**
     * 
     * 根据专员id删除专员信息
     * 
     * @param id 专员id
     * @return
     */
    @RequestMapping(value = "/deleteCommissioner/{ids}")
    @ResponseBody
    public Map<String, Object> deleteCommissioner(@PathVariable String ids) {
        boolean b = true;
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(ids)) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        String[] idStrings = ids.split(",");
        for (String id : idStrings) {
            try {
                b = organizationService.deleteCommissioner(id);
            } catch (Exception e) {
                b = false;
                break;
            }
        }
        if (b == true) {
            map.put("message", "删除成功！");
        } else {
            map.put("message", "删除失败！");
        }
        return map;
    }

    /**
     * 
     * 根据专员修改专员信息
     * 
     * @param commissioner 专员对象
     * @return
     */
    @RequestMapping(value = "/updateCommissioner")
    @ResponseBody
    public Commissioner updateCommissioner(Commissioner commissioner) {
        if (commissioner == null || StringUtils.isBlank(commissioner.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        organizationService.updateCommissioner(commissioner);
        return commissioner;
    }

    /**
     * 
     * 根据专员得到专员详细信息
     * 
     * @param id 专员id
     * @return
     */
    @RequestMapping(value = "/getCommissioner")
    @ResponseBody
    public Commissioner getCommissioner(@RequestParam("commissionerId") String commissionerId) {
        if (commissionerId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        return organizationService.getCommissioner(commissionerId);
    }

    /**
     * 
     * 根据专员名称查询专员列表
     * 
     * @param commissionerName 专员名称
     * @return
     */
    @RequestMapping(value = "/findCommissionerList")
    @ResponseBody
    public Iterable<Commissioner> findCommissionerList(String commissionerName) {
    	Iterable<Commissioner> list = null;
        if (commissionerName == null || "".equals(commissionerName)) {
            list = organizationService.findAllCommissioner();
        }
        list = organizationService.findCommissionerByName(commissionerName);
        return list;
    }

    /**
     * 
     * 查询所有的专员列表
     * 
     * @return
     */
    @RequestMapping(value = "/findAllCommissioner")
    @ResponseBody
    public Iterable<Commissioner> findAllCommissioner() {
    	Iterable<Commissioner> list = organizationService.findAllCommissioner();
        return list;
    }

    @RequestMapping(value = "/createEmpAndCommissioner", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createEmpAndCommissioner(String employeeId, String commissionerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        EmployeeAndCommissioner empAndCommissioner;
        try {
            empAndCommissioner = organizationService.getEmployeeAndCommissioner(employeeId, commissionerId);
            String employeeName = employeeService.getSingleEmployeeById(employeeId).getName();
            if (empAndCommissioner != null) {
                map.put("message", "失败，" + employeeName + "已为该专员！");
            } else {
                EmployeeAndCommissioner empAndCommi = new EmployeeAndCommissioner();
                empAndCommi.setEmployeeId(employeeId);
                empAndCommi.setCommissionerId(commissionerId);
                organizationService.createEmployeeAndCommissioner(empAndCommi);
                map.put("message", "成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "失败！");
        }
        return map;
    }

    @RequestMapping(value = "/deleteEmpAndCommissioner", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteEmpAndCommissioner(String employeeId, String commissionerId) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            organizationService.deleteEmployeeAndCommissioner(employeeId, commissionerId);
            map.put("message", "成功！");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "失败！");
        }
        return map;
    }

    @RequestMapping(value = "/findCommissionerName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findCommissionerName(String commissionerId, String commissionerName) {
        // 在一个部署单元中，公司名称是唯一的
        String companyId = organizationService.getCurrentCompanyId();
        boolean bool = true;
        Commissioner commissioner = organizationService.getCommissionerByName(companyId, commissionerName);
        if (commissioner != null) {
            if (commissioner.getId() != commissionerId) {
                bool = false;
            }
        }
        return bool;
    }

}
