package pw.cdmi.paas.account.rs.v1;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pw.cdmi.paas.account.model.AuthMethod;
import pw.cdmi.paas.account.model.LoginAuth;
import pw.cdmi.paas.account.model.NewUser;
import pw.cdmi.paas.account.model.RegisterChannel;
import pw.cdmi.paas.account.model.entities.UserAccount;
import pw.cdmi.paas.account.service.UserService;

/************************************************************
 * 控制类，处理用户或账号信息的请求操作
 * 
 * @author 伍伟
 * @version iSoc Service Platform, 2015-5-19
 ************************************************************/
@RestController
@RequestMapping("/users/v3")
public class UserResource {

	private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

	@Autowired
	private UserService userService;

	/**
	 * 新增一个注册用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public String createUserAccount(@RequestBody NewUser user) {

		if (user == null || user.getChannel() == null || user.getInfo() == null
				|| StringUtils.isBlank(user.getInfo().getAccount())) {
			throw new RuntimeException("");
		}
		String name = user.getInfo().getAccount();
		UserAccount account = new UserAccount();
		RegisterChannel channel = user.getChannel();

		// TODO 信息校验、保存
		switch (channel) {
		case WECHAT:
			account.setWechat(name);
			if (userService.existUserAccount(account)) {
				// 存在该用户
				throw new SecurityException("该微信账号已绑定");
			}
			account.setEmail(user.getInfo().getEmail());
			account.setMobile(user.getInfo().getMobile());
			account.setHeadImage(user.getInfo().getHead_image());
			break;
		case MOBILE:

			account.setMobile(name);
			if (userService.existUserAccount(account)) {
				// 存在该用户
				throw new SecurityException("该电话已注册");
			}
			account.setEmail(user.getInfo().getEmail());
			account.setHeadImage(user.getInfo().getHead_image());
			break;
		case EMAIL:
			account.setEmail(name);
			if (userService.existUserAccount(account)) {
				// 存在该用户
				throw new SecurityException("该邮箱已注册");
			}
			account.setMobile(user.getInfo().getMobile());
			account.setHeadImage(user.getInfo().getHead_image());
			break;
		case QQ:
			account.setQq(name);
			if (userService.existUserAccount(account)) {
				// 存在该用户
				throw new SecurityException("该QQ账号已注册");
			}
			account.setEmail(user.getInfo().getEmail());
			account.setMobile(user.getInfo().getMobile());
			account.setHeadImage(user.getInfo().getHead_image());
			break;
		case USER_PASSWORD:
			if (StringUtils.isBlank(user.getInfo().getPassword())) {
				throw new RuntimeException("必要的参数未传入");
			}
			account.setUserName(name);
			if (userService.existUserAccount(account)) {
				// 存在该用户
				throw new SecurityException("该账号已存在");
			}
			account.setEmail(user.getInfo().getEmail());
			account.setMobile(user.getInfo().getMobile());
			account.setHeadImage(user.getInfo().getHead_image());
			account.setPassword(user.getInfo().getPassword());
			break;
		case QY_WECHAT:
			account.setQyWechat(name);
			if (userService.existUserAccount(account)) {
				// 存在该用户
				throw new SecurityException("该企业微信账号已注册");
			}
			account.setEmail(user.getInfo().getEmail());
			account.setMobile(user.getInfo().getMobile());
			account.setHeadImage(user.getInfo().getHead_image());
			break;
		default: // 默认为用户名和密码方式
			if (StringUtils.isBlank(user.getInfo().getPassword())) {
				throw new RuntimeException("必要的参数未传入");
			}
			account.setUserName(name);
			if (userService.existUserAccount(account)) {
				// 存在该用户
				throw new SecurityException("该账号已存在");
			}
			account.setEmail(user.getInfo().getEmail());
			account.setMobile(user.getInfo().getMobile());
			account.setHeadImage(user.getInfo().getHead_image());
			account.setPassword(user.getInfo().getPassword());

			break;
		}
		account.setIsReal(false);

		return userService.createUserAccount(account);
	}

	/**
	 * 登陆
	 * 
	 * @param user
	 * @return
	 */
	@PreAuthorize("hasRole('anonymous')")
	@RequestMapping(value = "/user/login", method = RequestMethod.PUT)
	public void login(LoginAuth user) {

		if (user != null || user.getType() != null || user.getInfo() != null
				|| StringUtils.isBlank(user.getInfo().getAccount())) {
			throw new RuntimeException("");
		}
		AuthMethod type = user.getType();
		UserAccount userAccount = new UserAccount();
		switch (type) {
		case USER_PASSWORD: // 采用用户名，密码登陆方式:
			if (StringUtils.isBlank(user.getInfo().getPassword())) {
				throw new SecurityException("password is null");
			}

			userAccount.setUserName(user.getInfo().getAccount());
			if (!userService.existUserAccount(userAccount)) {
				throw new SecurityException("该用户不存在");
			}
			UserAccount findUserAccountByUserNameAndPassword = userService
					.findUserAccountByUserNameAndPassword(user.getInfo().getAccount(), user.getInfo().getPassword());
			if (findUserAccountByUserNameAndPassword == null) {
				throw new SecurityException("密码错误");
			}
			break;
		case MOBILE_SMS: // 采用手机短信验证码登陆方式
			// TODO 待实现

			break;
		case MOBILE_PASSWORD: // 采用手机 密码登陆方式
			// TODO
			if (StringUtils.isBlank(user.getInfo().getPassword())) {
				throw new SecurityException("password is null");
			}

			userAccount.setMobile(user.getInfo().getAccount());
			if (!userService.existUserAccount(userAccount)) {
				throw new SecurityException("该用户不存在");
			}
			UserAccount findUserAccountByMobileAndPassword = userService
					.findUserAccountByMobileAndPassword(user.getInfo().getAccount(), user.getInfo().getPassword());
			if (findUserAccountByMobileAndPassword == null) {
				throw new SecurityException("密码错误");
			}

			break;
		case EMAIL_PASSWORD: // 采用邮箱地址 密码登陆方式
			if (StringUtils.isBlank(user.getInfo().getPassword())) {
				throw new SecurityException("password is null");
			}

			userAccount.setEmail(user.getInfo().getAccount());
			if (!userService.existUserAccount(userAccount)) {
				throw new SecurityException("该用户不存在");
			}

			UserAccount findUserAccountByEmailAndPassword = userService
					.FindUserAccountByEmailAndPassword(user.getInfo().getAccount(), user.getInfo().getPassword());
			if (findUserAccountByEmailAndPassword == null) {
				throw new SecurityException("密码错误");
			}
			break;

		case WECHAT_SCANCODE: // 采用微信二维码扫描方式登陆

			break;
		case WECHAT_SERVICE_NUMBER: // 采用微信服务号授权登陆方式(小程序)

			break;
		case OAUTH_WECHAT: // 以微信为第三方登陆平台进行登陆认证

			break;
		case OAUTH_QQ: // 以QQ为第三方认证平台进行登陆认证

			break;
		default:
			break;
		}
	}

}
