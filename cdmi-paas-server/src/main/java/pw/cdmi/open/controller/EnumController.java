package pw.cdmi.open.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pw.cdmi.open.model.CertificatesInfo;
import pw.cdmi.open.model.Education;
import pw.cdmi.open.model.EmployeeProperty;
import pw.cdmi.open.model.Nation;
import pw.cdmi.open.model.Sex;

/************************************************************
 * 控制类，提供输出枚举类型列表的所有方法
 * 
 * @author 佘朝军
 * @version iSoc Service Platform, 2015-5-26
 ************************************************************/
@Controller
@RequestMapping(value = "/enum")
public class EnumController {

	private static final Logger log = LoggerFactory.getLogger(EnumController.class);

	@RequestMapping(value = "/sex", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> findAllSexEnum() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			for (Sex sex : Sex.values()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", sex.getText());
				map.put("value", sex.toString());
				list.add(map);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	@RequestMapping(value = "/employeeProperty", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> findAllEmployeePropertyEnum() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			for (EmployeeProperty ep : EmployeeProperty.values()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", ep.getText());
				map.put("value", ep.toString());
				list.add(map);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	@RequestMapping(value = "/nation", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> findAllNationEnum() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			for (Nation nation : Nation.values()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", nation.getText());
				map.put("value", nation.toString());
				list.add(map);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	@RequestMapping(value = "/education", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> findAllEducationEnum() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			for (Education education : Education.values()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", education.getText());
				map.put("value", education.toString());
				list.add(map);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return list;
	}
	@RequestMapping(value = "/certificate", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> findAllCertificateEnum() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			for (CertificatesInfo certificate : CertificatesInfo.values()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", certificate.getText());
				map.put("value", certificate.getValue()+"");
				list.add(map);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
			return null;
		}
		return list;
	}

}
