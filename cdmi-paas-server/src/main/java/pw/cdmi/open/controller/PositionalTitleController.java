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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.model.entities.Position;
import pw.cdmi.open.model.entities.PositionalTitle;
import pw.cdmi.open.service.BusinessOrganizationService;

/**
 * **********************************************************
 * 控制类，提供对职称的业务操作方法
 * 
 * @author liujh
 * @version cas Service Platform, 2015-5-11
 ***********************************************************
 */
@Controller
@RequestMapping("/positionalTitle")
public class PositionalTitleController {
    @Autowired
    private BusinessOrganizationService organizationService;

    /**
     * 
     * 根据传入职称的信息，创建新职称
     * 
     * @param positionalTitle 职称信息封装的positionalTitle对象
     * @return
     */
    @RequestMapping(value = "/createPositionalTitle")
    @ResponseBody
    public PositionalTitle createPositionalTitle(PositionalTitle positionalTitle) {
        if (positionalTitle == null) {
            throw new AWSClientException(GlobalHttpClientError.IncompleteBody, ClientReason.IncompleteBody);
        }
        String companyId = organizationService.getCurrentCompanyId();
        positionalTitle.setCompanyId(companyId);
        organizationService.createPositionalTitle(positionalTitle);
        return positionalTitle;
    }

    /**
    * 
    * 根据职称id删除职称信息
    * 
    * @param id 职称id
    * @return
    */
    @RequestMapping(value = "/deletePositionalTitle/{ids}")
    @ResponseBody
    public Map<String, Object> deletePositionalTitle(@PathVariable String ids) {
        boolean b = true;
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(ids)) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        String[] idStrings = ids.split(",");
        for (String id : idStrings) {
            try {
                b = organizationService.deletePositionalTitle(id);
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
     * @param positionalTitle 职称对象
     * @return
     */
    @RequestMapping(value = "/updatePositionalTitle")
    @ResponseBody
    public String updatePositionalTitle(PositionalTitle positionalTitle) {
        if (StringUtils.isBlank(positionalTitle.getName())) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        organizationService.updatePositionTitle(positionalTitle);
        return positionalTitle.getId();
    }

    /**
     * 
     * 根据职称id得到职称详细信息
     * 
     * @param id 职称id
     * @return
     */
    @RequestMapping(value = "/getPositionalTitle")
    @ResponseBody
    public JSONObject getPositionalTitle(String positionalTitleId) {
        if (positionalTitleId == null) {
            throw new AWSClientException(GlobalHttpClientError.InvalidRequest, ClientReason.InvalidRequest);
        }
        PositionalTitle positionalTitle = organizationService.getPositionalTitle(positionalTitleId);
        JSONObject json = new JSONObject();
        json.put("id", positionalTitle.getId());
        json.put("name", positionalTitle.getName());
        if (positionalTitle.getPositionId() != null) {
            Position position = organizationService.getPosition(positionalTitle.getPositionId());
            if (position != null) {
                json.put("positionName", position.getName());
            }
        }
        json.put("description", positionalTitle.getDescription());
        return json;
    }

    /**
     * 
     * 根据关键字与岗位id查询职称列表
     * 
     * @param positionalTitle 查询条件封装成的positionalTitle对象
     * @return
     */
    @RequestMapping(value = "/findPositionalTitleList")
    @ResponseBody
    public JSONArray findPositionalTitleList(String name, String positionId) {
    	Iterable<PositionalTitle> list = null;
        PositionalTitle searchQuery = new PositionalTitle();
        searchQuery.setName(name);
        searchQuery.setPositionId(positionId);
        list = organizationService.findPositionalTitle(searchQuery);
        JSONArray array = new JSONArray();
        for (PositionalTitle positionalTitle : list) {
            JSONObject json = new JSONObject();
            json.put("id", positionalTitle.getId());
            json.put("name", positionalTitle.getName());
            json.put("positionId", positionalTitle.getPositionId());
            if (positionalTitle.getPositionId() != null) {
                Position position = organizationService.getPosition(positionalTitle.getPositionId());
                if (position != null) {
                    json.put("positionName", position.getName());
                }
            }
            json.put("description", positionalTitle.getDescription());
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 根据关键字与岗位名称查询职称列表
     * 
     * @param positionalTitle 查询条件封装成的positionalTitle对象
     * @return
     */
    @RequestMapping(value = "/findPositionalTitleListByCondition")
    @ResponseBody
    public JSONArray findPositionalTitleListByPositionName(String name, String positionName) {
    	Iterable<PositionalTitle> list = organizationService.findPositionalTitle(name, positionName);
        JSONArray array = new JSONArray();
        for (PositionalTitle positionalTitle : list) {
            JSONObject json = new JSONObject();
            json.put("id", positionalTitle.getId());
            json.put("name", positionalTitle.getName());
            json.put("positionId", positionalTitle.getPositionId());
            if (positionalTitle.getPositionId() != null) {
                Position position = organizationService.getPosition(positionalTitle.getPositionId());
                if (position != null) {
                    json.put("positionName", position.getName());
                }
            }
            json.put("description", positionalTitle.getDescription());
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 根据岗位id查询职称列表，用于下拉列表
     * 
     * @param findListByPOsition 岗位id
     * @return
     */
    @RequestMapping(value = "/findListByPosition")
    @ResponseBody
    public JSONArray findListByPosition(String positionId) {
    	Iterable<PositionalTitle> list = null;
        PositionalTitle searchQuery = new PositionalTitle();
        if (positionId == null) {
            list = organizationService.findAllPositionalTitle();
        } else {
            searchQuery.setPositionId(positionId);
            list = organizationService.findPositionalTitle(searchQuery);
        }
        JSONArray array = new JSONArray();
        for (PositionalTitle positionalTitle : list) {
            JSONObject json = new JSONObject();
            json.put("id", positionalTitle.getId());
            json.put("name", positionalTitle.getName());
            array.add(json);
        }
        return array;
    }

    /**
     * 
     * 查询所有的职称列表
     * 
     * @return
     */
    @RequestMapping(value = "/findAllPositionalTitle")
    @ResponseBody
    public JSONArray findAllPositionalTitle() {
    	Iterable<PositionalTitle> list = organizationService.findAllPositionalTitle();
        JSONArray array = new JSONArray();
        for (PositionalTitle positionalTitle : list) {
            JSONObject json = new JSONObject();
            json.put("id", positionalTitle.getId());
            json.put("name", positionalTitle.getName());
            array.add(json);
        }
        return array;
    }

    @RequestMapping(value = "/findPosTitleName", method = RequestMethod.POST)
    @ResponseBody
    public boolean findPosTitleName(String posTitleId, String posTitleName) {
        boolean bool = true;
        String companyId = organizationService.getCurrentCompanyId();
        PositionalTitle positionalTitle = organizationService.getPositionalTitleByName(companyId, posTitleName);
        if (positionalTitle != null) {
            if (positionalTitle.getId().equals(posTitleId)) {
                bool = false;
            }
        }
        return bool;
    }
}
