package pw.cdmi.open.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.model.entities.Position;
import pw.cdmi.open.service.BusinessOrganizationService;

/**
 * **********************************************************
 * 控制类，提供对岗位的业务操作方法
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-11
 ***********************************************************
 */
@Controller
@SessionAttributes("position")
@RequestMapping("/position")
public class PositionController {
    @Autowired
    private BusinessOrganizationService organizationService;

    /**
     * 
     * 根据传入岗位的信息，创建新岗位
     * 
     * @param position 片区信息封装的position对象
     * @return
     */
    @RequestMapping(value = "/createPosition")
    @ResponseBody
    public Position createPosition(Position position) {
        if (position == null || StringUtils.isBlank(position.getName())) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        String companyId = organizationService.getCurrentCompanyId();
        position.setCompanyId(companyId);
        organizationService.createPosition(position);
        return position;
    }

    /**
     * 
     * 根据岗位id删除岗位信息
     * 
     * @param id 岗位id
     * @return
     */
    @RequestMapping(value = "/deletePosition/{ids}")
    @ResponseBody
    public Map<String, Object> deletePosition(@PathVariable String ids) {
        boolean b = true;
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(ids)) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        String[] idStrings = ids.split(",");
        for (String id : idStrings) {
            try {
                b = organizationService.deletePosition(id);
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
     * 根据岗位修改岗位信息
     * 
     * @param position 岗位对象
     * @return
     */
    @RequestMapping(value = "/updatePosition")
    @ResponseBody
    public Position updatePosition(Position position) {
        if (position == null || StringUtils.isBlank(position.getName())) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        organizationService.updatePosition(position);
        return position;
    }

    /**
     * 
     * 根据岗位id得到岗位详细信息
     * 
     * @param id 岗位id
     * @return
     */
    @RequestMapping(value = "/getPosition")
    @ResponseBody
    public JSONObject getPosition(String positionId) {
        if (positionId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        Position position = organizationService.getPosition(positionId);
        JSONObject json = new JSONObject();
        json.put("id", position.getId());
        json.put("name", position.getName());
        json.put("description", position.getDescription());
        return json;
    }

    /**
     * 
     * 根据名称模糊查询岗位列表
     * 
     * @param positionName 岗位名称
     * @return
     */
    @RequestMapping(value = "/findPositionList")
    @ResponseBody
    public JSONArray findPositionList(String positionName) {
        Iterable<Position> list = null;
        if (positionName == null || "".equals(positionName)) {
            list = organizationService.findAllPosition();
        }
        list = organizationService.findPositionByName(positionName);
        JSONArray array = new JSONArray();
        for (Position position : list) {
            JSONObject json = new JSONObject();
            json.put("id", position.getId());
            json.put("name", position.getName());
            json.put("description", position.getDescription());
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 查询所有的岗位列表
     * 
     * @return
     */
    @RequestMapping(value = "/findAllPosition")
    @ResponseBody
    public JSONArray findAllPosition() {
    	Iterable<Position> list = organizationService.findAllPosition();
        return JSONArray.fromObject(list);
    }

    @RequestMapping(value = "/findPositionName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findPositionName(String positionId, String positionName) {
        // 在一个部署单元中，公司名称是唯一的
        String companyId = organizationService.getCurrentCompanyId();
        boolean bool = true;
        Position position = organizationService.getPositionByName(companyId, positionName);
        if (position != null) {
            if (position.getId() != positionId) {
                bool = false;
            }
        }
        return bool;
    }
}
