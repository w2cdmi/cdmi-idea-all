package pw.cdmi.open.controller;

import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import pw.cdmi.open.service.SingleSiteApplicationService;

/************************************************************
 * 控制类，处理与应用站点信息的请求操作
 * 
 * @author 伍伟
 * @version iSoc Service Platform, 2015-5-19
 ************************************************************/
@Controller
@RequestMapping(value = "/app")
public class ApplicationController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private SingleSiteApplicationService appService;
    
    /**
     * 上传Liense文件，激活该应用系统并获得应用的相关授权信息，secretkey相当于密钥
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject registerSite(FileInputStream license) {
//    	SiteApplication app = appService.registerSiteAplication("", "");
        JSONObject object = new JSONObject();
//        object.put("appkey", app.getAppKey());
//        object.put("secretkey", app.getAppSecret());
        return object;
    }

}
