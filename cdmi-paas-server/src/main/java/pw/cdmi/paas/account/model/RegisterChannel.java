package pw.cdmi.paas.account.model;

/**
 * 新用户是从何处过来注册的
 * @author No.11
 *
 */
public enum RegisterChannel {
	QQ,						//对应的一个QQ用户
	WECHAT,					//对应一个微信用户
	MOBILE,					//对应一个手机用户
	EMAIL,					//对应一个邮件地址用户
	USER_PASSWORD,			//对应一个虚拟的通过用户名，密码注册的用户
	QY_WECHAT,				//对应一个企业微信用户
}
