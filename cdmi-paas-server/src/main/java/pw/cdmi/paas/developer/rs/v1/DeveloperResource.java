package pw.cdmi.paas.developer.rs.v1;

import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.paas.core.exception.ClientReason;
import pw.cdmi.paas.developer.model.DeveloperResponse;
import pw.cdmi.paas.developer.model.DeveloperType;
import pw.cdmi.paas.developer.model.NewDeveloper;
import pw.cdmi.paas.developer.model.entities.AuthCertificate;
import pw.cdmi.paas.developer.model.entities.Developer;
import pw.cdmi.paas.developer.service.AuthCertificateService;
import pw.cdmi.paas.developer.service.DeveloperService;


@RestController
@RequestMapping("/pass/v3")
public class DeveloperResource  {
	@Autowired
	private DeveloperService developerService;
	
	@Autowired
	private AuthCertificateService authCertificateService;
	
	/**
	 * 注册用户添加开发者信息
	 * @param developer
	 * @return
	 */
	@RequestMapping(value="/developers/developer", method = RequestMethod.POST)
	public @ResponseBody String createDeveloper(@RequestParam NewDeveloper developer){
	//	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(developer==null || StringUtils.isBlank(developer.getName())
				||StringUtils.isBlank(developer.getType())
				||StringUtils.isBlank(developer.getLinkemail())
				||StringUtils.isBlank(developer.getLinkmobile())
				){
			throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
		}
		DeveloperType fromName = DeveloperType.fromName(developer.getType());
		if(fromName==null){
			throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
		}
		
		Developer newDeveloper = new Developer();
		switch (fromName) {
		case Person:
			newDeveloper.setName(developer.getName());
			newDeveloper.setType(fromName);
			newDeveloper.setManagerEmail(developer.getLinkemail());
			newDeveloper.setManagerMobile(developer.getLinkmobile());
			newDeveloper.setManagerName(developer.getName());
			newDeveloper.setDirector_id("123");
			
			break;
		case Enterprise:
			if(StringUtils.isBlank(developer.getLinkname())){
				throw new SecurityException("manage名字不能为空");
			}
			newDeveloper.setName(developer.getName());
			newDeveloper.setType(fromName);
			newDeveloper.setManagerEmail(developer.getLinkemail());
			newDeveloper.setManagerMobile(developer.getLinkmobile());
			newDeveloper.setManagerName(developer.getName());
			newDeveloper.setDirector_id(" ");
			break;

		default:
			break;
		}
		
		String newid =  developerService.createDeveloper(newDeveloper);
		return newid;		
	}
	
	@RequestMapping(value = "/developers/{developer_id}",method = RequestMethod.GET)
	public @ResponseBody DeveloperResponse getDeveloperById(@PathVariable("developer_id")String id){
		if(StringUtils.isBlank(id)){
			throw new SecurityException("参数传入错误");
		}
		Developer findById = developerService.findOneDeveloperById(id);
		if(findById==null){
			throw new SecurityException("未找到该开发者");
		}
		DeveloperResponse developerResponse = new DeveloperResponse();
		developerResponse.setId(findById.getId());
		developerResponse.setName(findById.getName());
		developerResponse.setCreatetime(findById.getCreateTime().toString());
		developerResponse.setType(findById.getType().toString());
		
		developerResponse.setLinkmail(findById.getManagerEmail());
		developerResponse.setLinkmobile(findById.getManagerMobile());
		developerResponse.setLinkname(findById.getManagerName());		
		return developerResponse;	
	}
	
	@RequestMapping(value="/certificates/certificate", method = RequestMethod.POST)
	public @ResponseBody AuthCertificate createCertificate(){
		String developerId = "";
		return authCertificateService.createCertificate(developerId);
	}
	
	@RequestMapping(value = "/developers/developer",method = RequestMethod.GET)
	public @ResponseBody boolean getDeveloperByUserId(@QueryParam("user_id")String userId){
		if(StringUtils.isBlank(userId)){
			throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
		}
		Developer developer = developerService.findDeveloperByUserId(userId);
		if(developer==null){
			return false;
		}
		return true;	
	}
	
}
