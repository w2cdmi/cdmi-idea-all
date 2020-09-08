package pw.cdmi.paas.account.model;

public enum AuthMethod {
	USER_PASSWORD,						//采用用户名，密码登陆方式
	MOBILE_SMS,							//采用手机短信验证码登陆方式
	MOBILE_PASSWORD,					//采用手机 密码登陆方式
	EMAIL_PASSWORD,						//采用邮箱地址 密码登陆方式
	WECHAT_SCANCODE,					//采用微信二维码扫描方式登陆
	WECHAT_SERVICE_NUMBER,				//采用微信服务号授权登陆方式(小程序)
	OAUTH_WECHAT,						//以微信为第三方登陆平台进行登陆认证
	OAUTH_QQ,							//以QQ为第三方认证平台进行登陆认证
}
