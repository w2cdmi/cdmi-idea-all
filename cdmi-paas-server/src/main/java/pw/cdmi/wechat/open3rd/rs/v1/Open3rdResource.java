package pw.cdmi.wechat.open3rd.rs.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.wechat.open3rd.model.AuthorizerTokenResponse;
import pw.cdmi.wechat.open3rd.model.ComponentAccessToken;
import pw.cdmi.wechat.open3rd.model.ComponentAuthorizationResponse;
import pw.cdmi.wechat.open3rd.model.DraftListResponse;
import pw.cdmi.wechat.open3rd.model.GZHAuthorizerInfoResponse;
import pw.cdmi.wechat.open3rd.model.MiniProAuthorizerInfoResponse;
import pw.cdmi.wechat.open3rd.model.Open3rdComponent;
import pw.cdmi.wechat.open3rd.model.PreAuthCodeResponse;
import pw.cdmi.wechat.open3rd.model.TemplateListResponse;

@RestController
@RequestMapping("/wechat/open3rd/v1")
public class Open3rdResource {

	// 获取第三方平台component_access_token
	// https://api.weixin.qq.com/cgi-bin/component/api_component_token
	@PostMapping(value = "/component/componenttoken")
	public ComponentAccessToken getComponentToken(@RequestBody Open3rdComponent open3rdComponent) {
		if (open3rdComponent != null) {
			return null;
		} else {
			return null;
		}
	}

	// 获取预授权码pre_auth_code
	// https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=xxx
	@PostMapping(value = "/component/preauthcode")
	public PreAuthCodeResponse createPreAuthCode(@RequestParam("component_appid") String component_appid) {
		return null;
	}

	// 使用授权码换取公众号或小程序的接口调用凭据和授权信息
	// https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx
	@PostMapping(value = "/component/authorizationinfo")
	public ComponentAuthorizationResponse getAuthorizationInfo(@RequestParam("component_appid") String component_appid,
			@RequestParam("authorization_code") String authorization_code) {
		return null;
	}

	// 获取（刷新）授权公众号或小程序的接口调用凭据（令牌）
	// https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=xxxxx
	@PostMapping(value = "/component/authorizertoken")
	public AuthorizerTokenResponse getAuthorizerToken(@RequestParam("component_appid") String component_appid,
			@RequestParam("authorizer_appid") String authorizer_appid,
			@RequestParam("authorizer_refresh_token") String authorizer_refresh_token) {
		return null;
	}

	// 获取授权方(公众号)的帐号基本信息
	// https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=xxxx
	@PostMapping(value = "/component/authorizerinfo/gzh")
	public GZHAuthorizerInfoResponse getGZHAuthorizerInfo(@RequestParam("component_appid") String component_appid,
			@RequestParam("authorizer_appid") String authorizer_appid) {
		return null;
	}

	// 获取授权方(小程序)的帐号基本信息
	// https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token=xxxx
	@PostMapping(value = "/component/authorizerinfo/minipro")
	public MiniProAuthorizerInfoResponse getMiniProAuthorizerInfo(
			@RequestParam("component_appid") String component_appid,
			@RequestParam("authorizer_appid") String authorizer_appid) {
		return null;
	}

	// 设置授权方的选项信息
	// https://api.weixin.qq.com/cgi-bin/component/api_set_authorizer_option?component_access_token=xxxx
	@GetMapping(value = "/component/authorizeroption")
	public void setAuthorizerOption(@RequestParam("component_appid") String component_appid,
			@RequestParam("authorizer_appid") String authorizer_appid, @RequestParam("option_name") String option_name,
			@RequestParam("option_value") String option_value) {
		return;
	}
	
	//创建 开放平台帐号并绑定公众号/小程序
	//https://api.weixin.qq.com/cgi-bin/open/create?access_token=xxxx
	@PostMapping(value = "/open/authorizeroption")
	public String createOpenAccount(@RequestParam("appid") String appid) {
		String open_appid = null;
		
		return open_appid;
	}
	
	//将公众号/小程序绑定到开放平台帐号下
	//https://api.weixin.qq.com/cgi-bin/open/bind?access_token=xxxx
	@PostMapping(value = "/open/binded")
	public void bindToOpenAccount(@RequestParam("appid") String appid, @RequestParam("open_appid") String open_appid) {
		
		return;
	}
	
	//将公众号/小程序从开放平台帐号下解绑
	//https://api.weixin.qq.com/cgi-bin/open/unbind?access_token=xxxx
	@PostMapping(value = "/open/unbinded")
	public void unbindFromOpenAccount(@RequestParam("appid") String appid, @RequestParam("open_appid") String open_appid) {
		
		return;
	}
	
	//获取公众号/小程序所绑定的开放平台帐号
	//https://api.weixin.qq.com/cgi-bin/open/get?access_token=xxxx
	@PostMapping(value = "/open/account")
	public String getOpenAccountId(@RequestParam("appid") String appid) {
		String open_appid = null;
		
		return open_appid;
	}
	
	// 获取草稿箱内的所有临时代码草稿
	// https://api.weixin.qq.com/wxa/gettemplatedraftlist?access_token=TOKEN
	@GetMapping(value = "/drafts")
	public DraftListResponse getTemplateDraftList() {
		return null;
	}

	// 获取代码模版库中的所有小程序代码模版
	// https://api.weixin.qq.com/wxa/gettemplatelist?access_token=TOKEN
	@GetMapping(value = "/templates")
	public TemplateListResponse getTemplateList() {
		return null;
	}

	// 将草稿箱的草稿选为小程序代码模版
	// https://api.weixin.qq.com/wxa/addtotemplate?access_token=TOKEN
	@PostMapping(value = "/templates/template")
	public void addToTemplate(@RequestParam("draft_id") String draft_id) {
		return;
	}

	// 删除指定小程序代码模版
	// https://api.weixin.qq.com/wxa/deletetemplate?access_token=TOKEN
	@PostMapping(value = "/templates/deleted")
	public void deleteTemplate(@RequestParam("template_id") String template_id) {
		return;
	}
}
