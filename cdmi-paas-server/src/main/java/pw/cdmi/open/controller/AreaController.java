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
import com.alibaba.fastjson.JSONObject;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.model.entities.Area;
import pw.cdmi.open.model.entities.Employee;
import pw.cdmi.open.service.BusinessOrganizationService;
import pw.cdmi.open.service.EmployeeService;
import pw.cdmi.paas.core.exception.ClientReason;

/**
 * **********************************************************
 * 控制类，提供对片区的业务操作方法
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-11
 ***********************************************************
 */
@Controller
@SessionAttributes("area")
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private BusinessOrganizationService organizationService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 
     * 根据传入的片区信息，创建新片区
     * 
     * @param area 片区信息封装的area对象
     * @return
     */
    @RequestMapping(value = "/createArea")
    @ResponseBody
    public Map<String, Object> createArea(Area area) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(area.getName())) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidParameter);
        }
        try {
            organizationService.createArea(area);
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "error");
        }
        return map;
    }

    /**
     * 
     * 根据片区id删除片区信息
     * 
     * @param id 片区id
     * @return
     */
    @RequestMapping(value = "/deleteArea/{ids}")
    @ResponseBody
    public Map<String, Object> deleteArea(@PathVariable String ids) {
        boolean b = true;
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(ids)) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidParameter);
        }
        String[] idStrings = ids.split(",");
        for (String id : idStrings) {
            try {
                b = organizationService.deleteArea(id);
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
     * 根据片区修改片区信息
     * 
     * @param area 片区对象
     * @return
     */
    @RequestMapping(value = "/updateArea")
    @ResponseBody
    public Map<String, Object> updateArea(Area newArea) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (newArea == null || StringUtils.isBlank(newArea.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.InvalidParameter);
        }
        try {
            Area area = organizationService.getArea(newArea.getId());
            area.setId(newArea.getId());
            area.setName(newArea.getName());
            area.setSupervisorId(newArea.getSupervisorId());
            area.setDescription(newArea.getDescription());
            organizationService.updateArea(area);
            map.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("message", "error");
        }
        return map;
    }

    /**
     * 
     * 根据片区id得到片区详细信息
     * 
     * @param id 片区id
     * @return
     */
    @RequestMapping(value = "/getArea")
    @ResponseBody
    public JSONObject getArea(@RequestParam("areaId") String areaId) {
        if (areaId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidParameter);
        }
        Area area = organizationService.getArea(areaId);
        JSONObject json = new JSONObject();
        json.put("id", area.getId());
        json.put("name", area.getName());
        if (area.getSupervisorId() != null) {
            Employee employee = employeeService.getSingleEmployeeById(area.getSupervisorId());
            if (employee == null) {
                json.put("supervisorName", "");
            } else {
                json.put("supervisorName", employee.getName());
            }
        }
        json.put("description", area.getDescription());
        return json;
    }

    /**
     * 
     * 根据条件查询片区列表
     * 
     * @param newArea 查询条件封装成的area对象
     * @return
     */
    @RequestMapping(value = "/findAreaList")
    @ResponseBody
    public JSONArray findAreaList(String name) {
    	Iterable<Area> list = null;
        Area queryObject = new Area();
        if (name == null || "".equals(name)) {
            list = organizationService.findAllArea();
        } else {
            queryObject.setName(name);
            list = organizationService.findAreaList(queryObject);
        }
        JSONArray array = new JSONArray();
        for (Area area : list) {
            JSONObject json = new JSONObject();
            json.put("id", area.getId());
            json.put("name", area.getName());
            json.put("supervisorId", area.getSupervisorId());
            if (area.getSupervisorId() != null) {
                Employee employee = employeeService.getSingleEmployeeById(area.getSupervisorId());
                if (employee == null) {
                    json.put("supervisorName", "");
                } else {
                    json.put("supervisorName", employee.getName());
                }
            }
            json.put("description", area.getDescription());
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 查询所有的片区列表
     * 
     * @return
     */
    @RequestMapping(value = "/findAllArea")
    @ResponseBody
    public JSONArray findAllArea() {
    	Iterable<Area> list = organizationService.findAllArea();
        JSONArray array = new JSONArray();
        for (Area area : list) {
            JSONObject json = new JSONObject();
            json.put("id", area.getId());
            json.put("name", area.getName());
            array.add(json);
        }
        return array;
    }

    @RequestMapping(value = "/findAreaName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findAreaName(String id, String areaName) {
        boolean bool = true;
        Iterable<Area> areaList = organizationService.findAllArea();
        for (Area area : areaList) {
            String name = area.getName();
            if (id == null) {
                if (areaName.equals(name)) {
                    bool = false;
                }
            } else {
                area = organizationService.getArea(id);
                String name1 = area.getName();
                if (!areaName.equals(name1)) {
                    if (areaName.equals(name)) {
                        bool = false;
                    }
                }
            }
        }
        return bool;
    }
}
