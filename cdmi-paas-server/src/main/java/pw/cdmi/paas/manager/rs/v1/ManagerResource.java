package pw.cdmi.paas.manager.rs.v1;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.core.http.exception.AWSClientException;
import pw.cdmi.core.http.exception.GlobalHttpClientError;
import pw.cdmi.open.auth.service.impl.UserTokenHelper;
import pw.cdmi.open.model.Sex;
import pw.cdmi.paas.account.model.UserType;
import pw.cdmi.paas.account.model.entities.UserAccount;
import pw.cdmi.paas.account.service.UserService;
import pw.cdmi.paas.auth.service.UserToken;
import pw.cdmi.paas.core.exception.ClientReason;
import pw.cdmi.paas.developer.model.entities.Developer;
import pw.cdmi.paas.developer.service.DeveloperService;
import pw.cdmi.paas.manager.model.ManagerLoginResponse;
import pw.cdmi.paas.manager.model.entities.Manager;
import pw.cdmi.paas.manager.service.ManagerService;
import pw.cdmi.wechat.miniprogram.exception.LoginAuthFailedException;
import pw.cdmi.wechat.miniprogram.model.NewWxUser;
import pw.cdmi.wechat.miniprogram.model.WxUserInfo;
import pw.cdmi.wechat.miniprogram.service.WxOauth2Service;
import pw.cdmi.wishlist.model.entities.WxUser;
import pw.cdmi.wishlist.service.WxUserService;

@RestController
@RequestMapping("/pass/v1/manager")
public class ManagerResource {

	private static final Logger logger = LoggerFactory.getLogger(ManagerResource.class);

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private WxOauth2Service wxOauth2Service;
	
	@Autowired
	private UserService userService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private UserTokenHelper userTokenHelper;
	
	@Autowired
	private DeveloperService developerService;

	/**
	 * 平台管理员通过微信登陆系统
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/wxminiprogram/login", method = RequestMethod.POST)
	public ManagerLoginResponse createManager(@RequestBody NewWxUser user) {
		if (user == null || StringUtils.isBlank(user.getCode())) {
			throw new AWSClientException(GlobalHttpClientError.MissingMandatoryParameter, ClientReason.InvalidParameter);
		}

		// 通过微信服务网关获取用户的微信用户信息
		WxUserInfo wxUserInfo = wxOauth2Service.getWxUserInfo(user.getCode(), user.getIv(), user.getEncryptedData());
		if (wxUserInfo == null) {
			logger.error("WxUser login Failed: wxUserInfo is null.");
			throw new LoginAuthFailedException("Failed to get user info.");
		}
		if (wxUserInfo.hasError()) {
			logger.error("Can't get UserInfo of code {}: errcode={}, errmsg={}", user.getCode(),
					wxUserInfo.getErrcode(), wxUserInfo.getErrmsg());
			throw new LoginAuthFailedException("Failed to get user info.");
		}
		ManagerLoginResponse loginResponse = null;
		
		WxUser wxUser = wxUserService.getWxUserByWxOpenId(wxUserInfo.getOpenId());

		// 判断系统中该微信用户是否创建，如果未创建，则创建微信用户
		if (wxUser == null) {
			wxUser = new WxUser();
			wxUser.setWxOpenId(wxUserInfo.getOpenId());
			wxUser.setWxUnionId(wxUserInfo.getUnionId());
			wxUser.setNick(wxUserInfo.getNickName());
			wxUser.setHeadImageUrl(wxUserInfo.getAvatarUrl());
			wxUser = wxUserService.createWxUser(wxUser);
		}
		
		//判断平台用户账号是否创建，如果没有则创建
		UserAccount account = userService.getUserAccountByWxUserId(wxUserInfo.getOpenId());
		if(account == null) {
			account = new UserAccount();
			account.setWxOpenId(wxUserInfo.getOpenId());
			account.setUserName(wxUserInfo.getNickName());
			account.setHeadImage(wxUserInfo.getAvatarUrl());
			account.setSex(Sex.fromValue(wxUserInfo.getGender()));
			String newid = userService.createUserAccount(account);
			account.setId(newid);
		}
		
		// 判断平台管理员是否存在，如果已存在，检查当前用户是否为管理员，
		// 如果不是，则不允许登陆，如果不存在，则将当前用户设置为管理员
		long number = managerService.countManager();
		Manager manager = null;
		if (number == 0) {
			// 将微信用户的ID,绑定到平台管理员
			manager = new Manager();
			manager.setUserId(account.getId());
			manager.setWxUserId(wxUser.getId());
			manager.setWxOpenId(wxUser.getWxOpenId());
			manager.setWxUnionId(wxUser.getWxUnionId());
			manager.setThirdUserTypes(new String[] { "WeChat" });
			String newid = managerService.createManager(manager);
			manager.setId(newid);
		} else {
			//判断当前微信用户是否为管理员
			manager = managerService.getManagerByUserId(account.getId());
			if (manager == null) {
				throw new AWSClientException(GlobalHttpClientError.NoPermissions, ClientReason.NotPaaSManager);
			}
		}
		
		//输出用户信息，将生成Token信息
		loginResponse = loginWithWxUser(wxUser, manager.getUserId());
		return loginResponse;
	}

	private ManagerLoginResponse loginWithWxUser(WxUser wxUser,String userId) {
		ManagerLoginResponse restLoginResponse = new ManagerLoginResponse();

		UserToken token = new UserToken();
		token.setToken(UUID.randomUUID().toString());
		token.setUserId(userId);
		token.setUserType(UserType.PaaSManager);
		token.setOpenId(wxUser.getWxOpenId());
		token.setUnionId(wxUser.getWxUnionId());
		token.setHeadImageUrl(wxUser.getHeadImageUrl());
		token.setNick(wxUser.getNick());

		userTokenHelper.saveToCache(token);
		
		restLoginResponse.setToken(token.getToken());
		restLoginResponse.setNick(wxUser.getNick());
		restLoginResponse.setHeadImageUrl(wxUser.getHeadImageUrl());
		restLoginResponse.setUserId(userId);

		return restLoginResponse;
	}
}
