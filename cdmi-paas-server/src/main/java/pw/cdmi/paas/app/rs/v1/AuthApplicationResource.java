package pw.cdmi.paas.app.rs.v1;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import pw.cdmi.core.http.auth.Authorized;
import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.ClientReason;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.paas.app.model.AuthApplicationType;
import pw.cdmi.paas.app.model.ListAuthApplicationResponse;
import pw.cdmi.paas.app.model.NewApplication;
import pw.cdmi.paas.app.model.SiteAttribution;
import pw.cdmi.paas.app.service.AuthApplicationService;

@RestController
@RequestMapping("/pass/v3/apps")
public class AuthApplicationResource {
	@Autowired
	private AuthApplicationService authApplicationService;
	
	/**
	 * 注册一个平台自有的应用
	 * 
	 * @param authapp
	 * @return 返回新接入应用的Id，
	 */
	@Authorized(level="PlatformManager")
	@ApiOperation(value="注册一个平台自有的应用", notes="提供新的应用信息，创建一个平台自有应用")
	@ApiImplicitParam(name = "authapp", value = "用JSON数组表述的应用信息", required = true, dataType = "NewApplication", paramType = "body")
	@RequestMapping(value = "/app/self", method = RequestMethod.POST)
	public String createPlatformApplication(@RequestBody NewApplication authapp) {
		if (authapp == null || StringUtils.isBlank(authapp.getName())) {
			throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
		}
		if (authapp.getType() == null) {
			authapp.setType(AuthApplicationType.Browser_APP);
		}
		authapp.setAttribution(SiteAttribution.Self);
		String newid = authApplicationService.createAuthApplication(authapp);
		//微接入应用生成开发信息
		
		return newid;
	}
	
	/**
	 * 用户自行注册一个第三方接入应用
	 * 
	 * @param authapp
	 * @return 返回新接入应用的Id，
	 */
	@Authorized(level="PlatformUser")
	@RequestMapping(value = "/app", method = RequestMethod.POST)
	public String createAuthApplication(@RequestBody NewApplication authapp) {
		if (authapp == null || StringUtils.isBlank(authapp.getName())) {
			throw new AWSClientException(GlobalHttpClientError.InvalidParameter, ClientReason.InvalidParameter);
		}
		if (authapp.getType() == null) {
			authapp.setType(AuthApplicationType.Browser_APP);
		}
		authapp.setAttribution(SiteAttribution.Third);
		String owner = ""; // 从token中获取
		String newid = authApplicationService.createAuthApplication(authapp, owner);
		return newid;
	}
	
	@GetMapping
	public List<ListAuthApplicationResponse> listAuthApplication(){
		 
	
		return null;		
	}
	
}
